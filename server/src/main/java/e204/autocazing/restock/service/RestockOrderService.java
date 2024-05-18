package e204.autocazing.restock.service;

import e204.autocazing.config.SmsUtil;
import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.*;
import e204.autocazing.exception.IngredientAlreadyExistsException;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.kafka.cluster.KafkaProducerCluster;
import e204.autocazing.kafka.entity.ProducerEntity;
import e204.autocazing.kafka.entity.solution.restock.KafkaRestockOrderSpecific;
import e204.autocazing.kafka.entity.solution.restock.RestockOrderCreateEntity;
import e204.autocazing.restock.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RestockOrderService {
    @Autowired
    private RestockOrderRepository restockOrderRepository;
    @Autowired
    private RestockOrderSpecificRepository restockOrderSpecificRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private VenderRepository venderRepository;
    @Autowired
    private KafkaProducerCluster kafkaProducerCluster;

    // 장바구니생성
    @Transactional
    public void createRestockOrder(PostRestockDto postRestockDto, String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);

        RestockOrderEntity restockOrder = new RestockOrderEntity();
        restockOrder.setStatus(postRestockDto.getStatus());
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));
        restockOrder.setStore(store);
        restockOrderRepository.save(restockOrder);
    }

    // 발주  조회
    public List<RestockOrderResponse> findAllRestockOrders(List<RestockOrderEntity.RestockStatus> status, String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);

        List<RestockOrderEntity> orders;
        if (status == null || status.isEmpty()) {
            orders = restockOrderRepository.findAllByStoreId(storeId);
        } else {
            orders = restockOrderRepository.findByStatusInAndStoreId(status, storeId);
        }
        //발주Entity 리트에 넣기 set하기
        return orders.stream().map(this::mapToRestockOrderResponse).collect(Collectors.toList());
    }

    private RestockOrderResponse mapToRestockOrderResponse(RestockOrderEntity entity) {
        RestockOrderResponse response = new RestockOrderResponse();
        response.setRestockOrderId(entity.getRestockOrderId());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }


    // 발주 상세조회 (테스트)

    // 기존 리턴 :RestockOrderDetailsDto

    public List<ResponseDto> findRestockOrderById(RestockOrderEntity.RestockStatus status , String loginId) {
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Store not found for loginId: " + loginId));
        //조회했는데 장바구니가 없을때 만들기.
        if(status == RestockOrderEntity.RestockStatus.WRITING){
            RestockOrderEntity restockOrderEntity = restockOrderRepository.findRestockOrderByStoreAndStatus(storeEntity, RestockOrderEntity.RestockStatus.WRITING);
            if(restockOrderEntity == null) {
                PostRestockDto postRestockDto = new PostRestockDto();
                postRestockDto.setStatus(RestockOrderEntity.RestockStatus.WRITING);
                createRestockOrder(postRestockDto,loginId);
            }
        }

        List<ResponseDto> responseDtos = new ArrayList<>();
        //진행중인 발주 조회
        if(status == RestockOrderEntity.RestockStatus.ORDERED) {
            //구현
            List<RestockOrderEntity> restockOrderEntityList = restockOrderRepository.findAllByStoreAndStatus(storeEntity, RestockOrderEntity.RestockStatus.ORDERED);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@" + restockOrderEntityList.size());
            for (RestockOrderEntity restockEntity : restockOrderEntityList) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setRestockOrderId(restockEntity.getRestockOrderId());
                List<RestockOrderSpecificDetailDto> specifics = restockEntity.getRestockOrderSpecific().stream()
                        .filter(specific -> specific.getStatus() != RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE)
                        .map(this::convertToSpecificDetailDto)
                        .collect(Collectors.toList());

                responseDto.setSpecifics(specifics);
                responseDtos.add(responseDto);

            }
            return responseDtos;
        }

        //WRITING (장바구니)
        else if (status == RestockOrderEntity.RestockStatus.WRITING){
            RestockOrderEntity restockOrderEntity = restockOrderRepository.findRestockOrderByStoreAndStatus(storeEntity, RestockOrderEntity.RestockStatus.WRITING);
            ResponseDto responseDto = new ResponseDto();
            responseDto.setRestockOrderId(restockOrderEntity.getRestockOrderId());
            responseDto.setSpecifics(restockOrderEntity.getRestockOrderSpecific().stream()
                    .map(this::convertToSpecificDetailDto)
                    .collect(Collectors.toList()));

            responseDtos.add(responseDto);
            return responseDtos;

        }
        else if (status == RestockOrderEntity.RestockStatus.COMPLETE){
            List<RestockOrderEntity> restockOrderEntityList = restockOrderRepository.findAllByStoreAndStatus(storeEntity, RestockOrderEntity.RestockStatus.COMPLETE);
            for (RestockOrderEntity restockEntity : restockOrderEntityList) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setRestockOrderId(restockEntity.getRestockOrderId());
                List<RestockOrderSpecificDetailDto> specifics = restockEntity.getRestockOrderSpecific().stream()
                        .filter(specific -> specific.getStatus() == RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE)
                        .map(this::convertToSpecificDetailDto)
                        .collect(Collectors.toList());

                responseDto.setSpecifics(specifics);
                responseDtos.add(responseDto);

            }
            return responseDtos;
        }
        return responseDtos;
    }

    private RestockOrderSpecificDetailDto convertToSpecificDetailDto(RestockOrderSpecificEntity specific) {
        RestockOrderSpecificDetailDto detailDto = new RestockOrderSpecificDetailDto();
        IngredientEntity ingredientEntity = ingredientRepository.findById(specific.getIngredientId())
                .orElseThrow(() -> new RuntimeException("ingredientId  not found :" +specific.getIngredientId() ));

        detailDto.setRestockOrderSpecificId(specific.getRestockOrderSpecificId());
        detailDto.setIngredientName(specific.getIngredientName());
        detailDto.setIngredientQuanrtity(specific.getIngredientQuantity());
        detailDto.setIngredientPrice(specific.getIngredientPrice().doubleValue());
        detailDto.setVenderName(ingredientEntity.getVender().getVenderName());
        detailDto.setDeliveryTime(ingredientEntity.getDeliveryTime());
        detailDto.setCreatedAt(specific.getCreatedAt());
        detailDto.setUpdatedAt(specific.getUpdatedAt());
        detailDto.setArrivedAt(specific.getUpdatedAt().toLocalDate().plusDays(ingredientEntity.getDeliveryTime()));
        detailDto.setRestockSpecificStatus(specific.getStatus());
        return detailDto;
    }

    //status 상태변경
    @Transactional
    public UpdatedRestockDto updateRestockOrderStatus(Integer restockOrderId, UpdateRestockDto updateRestockDto, String loginId) {
        RestockOrderEntity restockOrderEntity = restockOrderRepository.findById(restockOrderId)
                .orElseThrow(() -> new RuntimeException("restockOrderId  not found :" +restockOrderId));

        //이전 상태 저장
        RestockOrderEntity.RestockStatus previousStatus = restockOrderEntity.getStatus();
        //발주 status == COMPLETE (재고 반영)
        if (updateRestockDto.getStatus() == RestockOrderEntity.RestockStatus.COMPLETE) {
            // 모든 RestockOrderSpecific 상태가 COMPLETE인지 확인
            boolean allSpecificsComplete = restockOrderEntity.getRestockOrderSpecific().stream()
                    .allMatch(specific -> specific.getStatus() == RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE);
            // COMPLETE 가 아닌 재료가 있을때 예외
            if (!allSpecificsComplete) {
                throw new RuntimeException("All restock specifics must be COMPLETE to change order status to COMPLETE");
            }
            //todo : PostStocks 호출해야됨.
            System.out.println("재고반영");
        }


        //새로운 상태 세팅,저장
        restockOrderEntity.setStatus(updateRestockDto.getStatus());
        restockOrderRepository.save(restockOrderEntity);



        //발주하기 status  WRITING ->ORDERED , 새로운 장바구니 만들기

        if (previousStatus == RestockOrderEntity.RestockStatus.WRITING && restockOrderEntity.getStatus() == RestockOrderEntity.RestockStatus.ORDERED) {
            //새로운 장바구니 만들기
            createNewRestockOrder(loginId);


            //status가 ORDERED 로 바뀌었으면 , 발주업체에 메일 or 문자보내기 로직 있어야함.
            //다시 커밋하겠음.
            if(restockOrderEntity.getStatus() == RestockOrderEntity.RestockStatus.ORDERED) {
                Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
                StoreEntity storeEntity = storeRepository.findById(storeId)
                        .orElseThrow(() -> new RuntimeException("No StoreENtity By storeId :  " +storeId ));
                String storeName = storeEntity.getStoreName();

                //restock order에서, order specific 접근해서 주문할 재료, 수량 저장
                List<RestockOrderSpecificEntity> restockOrderSpecificEntityList = restockOrderEntity.getRestockOrderSpecific();
                Map<Integer, Integer> order = new HashMap<>();
                for(RestockOrderSpecificEntity restockOrderSpecific : restockOrderSpecificEntityList){
                    Integer ingredientId = restockOrderSpecific.getIngredientId();
                    Integer ingredientQuantity = restockOrderSpecific.getIngredientQuantity();
                    if (order.containsKey(ingredientId)) {
                        order.put(ingredientId, order.get(ingredientId) + ingredientQuantity);
                    } else {
                        order.put(ingredientId, ingredientQuantity);
                    }
                    restockOrderSpecific.setStatus(RestockOrderSpecificEntity.RestockSpecificStatus.ORDERED);
                }

                // 재료 ID로 업체 정보 가져오기
                Map<String, List<Map<String, Integer>>> contactOrdersMap = new HashMap<>();
                for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
                    // entry의 재료ID로 담당 업체 연락처(String) 불러오기
                    String contact = ingredientRepository.findContactByIngredientId(entry.getKey(), storeId);
                    String ingredientName = ingredientRepository.findNameByIngredientId(entry.getKey(), storeId);

                    // 연락처를 키 값으로 하고, value는 재료 이름과 수량을 포함한 맵 리스트
                    contactOrdersMap.computeIfAbsent(contact, k -> new ArrayList<>()).add(Map.of(ingredientName, entry.getValue()));
                }

                // List<SmsRequestDto> : 연락처, 주문 리스트
                List<SmsRequestDto> requestDtoList = new ArrayList<>();

                for (Map.Entry<String, List<Map<String, Integer>>> contactEntry : contactOrdersMap.entrySet()) {
                    SmsRequestDto requestDto = new SmsRequestDto();

                    String venderContact = contactEntry.getKey();
                    Integer venderId = venderRepository.findByContact(venderContact, storeId);

                    requestDto.setVenderId(venderId);
                    requestDto.setVenderManagerContact(contactEntry.getKey());
                    requestDto.setOrderList(contactEntry.getValue());
                    requestDtoList.add(requestDto);
                }
                smsUtil.sendOne(requestDtoList,restockOrderId,storeName);

                // kafka 메시지 로직
                sendKafkaRestockOrder(restockOrderEntity, loginId);
            }
        }

        UpdatedRestockDto updatedRestockDto = new UpdatedRestockDto();
        updatedRestockDto.setRestockOrderId(restockOrderId);
        updatedRestockDto.setCreatedAt(restockOrderEntity.getCreatedAt());
        updatedRestockDto.setUpdatedAt(restockOrderEntity.getUpdatedAt());
        updatedRestockDto.setStatus(restockOrderEntity.getStatus());

        // kafka 메시지 "delivery_refresh"에 발행
        kafkaProducerCluster.sendProducerMessage("delivery_refresh", loginId, new ProducerEntity("DELIVERY", "Refresh delivery status"));

        return updatedRestockDto;
    }

    public void sendKafkaRestockOrder(RestockOrderEntity restockOrderEntity, String loginId) {
        RestockOrderCreateEntity restockOrderCreateEntity = new RestockOrderCreateEntity(restockOrderEntity.getRestockOrderId(), new ArrayList<>(), restockOrderEntity.getStatus().toString());
        for (RestockOrderSpecificEntity originRestockOrderSpecific : restockOrderEntity.getRestockOrderSpecific()) {
            restockOrderCreateEntity.getRestockOrderSpecifics().add(new KafkaRestockOrderSpecific(originRestockOrderSpecific.getIngredientName(), originRestockOrderSpecific.getIngredientQuantity(), originRestockOrderSpecific.getIngredientPrice(), originRestockOrderSpecific.getStatus().toString()));
        }
        kafkaProducerCluster.sendRestockOrderCreateMessage("restock_order", loginId, restockOrderCreateEntity);
    }

    //발주완료 재고반영
    @Transactional
    public void updateStockWithCompleteOrder(Integer restockOrderId) {
        List<RestockOrderSpecificEntity> restockOrderSpecifics = restockOrderSpecificRepository.findByRestockOrderRestockOrderId(restockOrderId);
        for (RestockOrderSpecificEntity specific : restockOrderSpecifics) {

            IngredientEntity ingredient = ingredientRepository.findIngredientByIngredientId(specific.getIngredientId());
            StockEntity stock = stockRepository.findByIngredient(ingredient);
            if (stock == null) {
                stock = new StockEntity();
                stock.setIngredient(ingredient);
                stock.setQuantity(specific.getIngredientQuantity());
                stock.setExpirationDate(LocalDate.now().plusYears(1));
            } else {
                stock.setQuantity(stock.getQuantity() + specific.getIngredientQuantity());
            }
            stockRepository.save(stock);
        }
    }
    //새로운 장바구니 만들기
    private void createNewRestockOrder(String loginId) {
        PostRestockDto postRestockDto = new PostRestockDto();
        createRestockOrder(postRestockDto, loginId);
    }

    //주문 후 발주할 재료 추가
    @Transactional
    public void addRestockOrderSpecific(IngredientEntity ingredient, int quantity) {
        // WRITING 상태의 가장 최신 RestockOrder 조회
        RestockOrderEntity restockOrder = restockOrderRepository.findFirstByStatusOrderByCreatedAtDesc(RestockOrderEntity.RestockStatus.WRITING)
                .orElseThrow(() -> new RuntimeException("No WRITING status RestockOrder found"));

        // 새로운 RestockOrderSpecific 생성 및 추가
        RestockOrderSpecificEntity restockOrderSpecific = new RestockOrderSpecificEntity();
        restockOrderSpecific.setRestockOrder(restockOrder);
        restockOrderSpecific.setIngredientId(ingredient.getIngredientId());
        //재료 알아내기
        restockOrderSpecific.setIngredientName(ingredient.getIngredientName());
        restockOrderSpecific.setIngredientQuantity(quantity);
        restockOrderSpecific.setIngredientPrice(ingredient.getIngredientPrice() * quantity);
        restockOrderSpecificRepository.save(restockOrderSpecific);
    }

    //배송중인지 체크하기 (재고에서 배송중인 재료수량 체크하기 위함)
    public int isDelivering(IngredientEntity ingredient) {
        int deliverdCount = 0 ;
        //완료처리 안된 발주목록 가져오기.
        // RestockOrderRepository 호출
        List<RestockOrderEntity> incompleteRestocks = restockOrderRepository.findByStatusNot(
                EnumSet.of(RestockOrderEntity.RestockStatus.COMPLETE, RestockOrderEntity.RestockStatus.WRITING)
        );
        // 각 RestockOrder에 대해 상세 조회하여 재료를 주문한 경우를 확인
        for (RestockOrderEntity restockOrder : incompleteRestocks) {
            // RestockOrder에 해당하는 RestockOrderSpecificEntity 리스트 조회
            List<RestockOrderSpecificEntity> restockOrderSpecifics = restockOrderSpecificRepository.findByRestockOrderRestockOrderId(restockOrder.getRestockOrderId());

            // 해당 RestockOrder에 주문한 재료가 있는지 확인 (있으면 카운트)

            for (RestockOrderSpecificEntity specific : restockOrderSpecifics) {
                // RestockOrderSpecificEntity에서 재료 확인
                Integer ingredientId = specific.getIngredientId();
                log.info("재고 조회 전");
                IngredientEntity orderedIngredient = ingredientRepository.findIngredientByIngredientId(ingredientId);
                log.info("재고 조회 후");
                if (ingredient.equals(orderedIngredient)) {
                    //배송중인 재료가 있다면
                    deliverdCount += specific.getIngredientQuantity();
                }
            }
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("deliverdCount : " + deliverdCount);
        return deliverdCount;

    }

    //수동 발주 추가
    public AddSpecificResponse addSpecific(String type,AddSpecificRequest addDto,String loginId) {

        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));

        //장바구니 가져오기
        RestockOrderEntity restockOrder = restockOrderRepository.findRestockOrderByStoreAndStatus(storeEntity, RestockOrderEntity.RestockStatus.WRITING);

        System.out.println("발주 1단계");
        RestockOrderSpecificEntity specific = new RestockOrderSpecificEntity();
        IngredientEntity ingredientEntity = ingredientRepository.findById(addDto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        specific.setIngredientName(ingredientEntity.getIngredientName());
        if(type.equals("auto")){
            System.out.println("자동발주");
            specific.setIngredientQuantity(ingredientEntity.getOrderCount());

        }
        //수동 발주 일땐
        else if(type.equals("manual")){
            System.out.println("수동발주");
            specific.setIngredientQuantity(addDto.getIngredientQuantity());
        }
        Boolean exists = restockOrderSpecificRepository.existsByRestockOrderAndIngredientId(restockOrder, ingredientEntity.getIngredientId());
        specific.setIngredientPrice(ingredientEntity.getIngredientPrice() * addDto.getIngredientQuantity());
        specific.setIngredientId(ingredientEntity.getIngredientId());
        specific.setRestockOrder(restockOrder);
        specific.setStatus(RestockOrderSpecificEntity.RestockSpecificStatus.WRITING);

        if(!exists) {//존재하지 않을때만 save
            restockOrderSpecificRepository.save(specific);
        }


//        restockOrderSpecificRepository.save(specific);

        System.out.println( "발주 2단계");
        AddSpecificResponse addSpecificResponse = new AddSpecificResponse();
        addSpecificResponse.setVenderName(ingredientEntity.getVender().getVenderName());
        addSpecificResponse.setDeliveryTime(ingredientEntity.getDeliveryTime());
        addSpecificResponse.setIngredientName(specific.getIngredientName());
        addSpecificResponse.setIngredientPrice(specific.getIngredientPrice());
        addSpecificResponse.setIngredientQuantity(specific.getIngredientQuantity());

        return addSpecificResponse;
    }

}
