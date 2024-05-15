package e204.autocazing.restock.service;

import e204.autocazing.config.SmsUtil;
import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.*;
import e204.autocazing.restock.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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


    // 발주 상세조회
    public RestockOrderDetailsDto findRestockOrderById(Integer restockOrderId) {
         RestockOrderEntity restockOrderEntity = restockOrderRepository.findById(restockOrderId)
            .orElseThrow(() -> new EntityNotFoundException("Restock not found"));

        RestockOrderDetailsDto restockOrderDetailsDto = new RestockOrderDetailsDto();
        restockOrderDetailsDto.setRestockOrderId(restockOrderEntity.getRestockOrderId());
        restockOrderDetailsDto.setSpecifics(restockOrderEntity.getRestockOrderSpecific().stream()
            .map(this::convertToSpecificDetailDto)
            .collect(Collectors.toList()));
        return restockOrderDetailsDto;
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
        return detailDto;
    }

    //status 상태변경
    @Transactional
    public UpdatedRestockDto updateRestockOrderStatus(Integer restockOrderId, UpdateRestockDto updateRestockDto, String loginId) {
        RestockOrderEntity restockOrder = restockOrderRepository.findById(restockOrderId)
                .orElseThrow(() -> new RuntimeException("Restock order not found"));

        //이전 상태 저장
        RestockOrderEntity.RestockStatus previousStatus = restockOrder.getStatus();

        //새로운 상태 세팅,저장
        restockOrder.setStatus(updateRestockDto.getStatus());
        restockOrderRepository.save(restockOrder);

        //발주 status == COMPLETE (재고 반영)
        if (restockOrder.getStatus() == RestockOrderEntity.RestockStatus.COMPLETE) {
            updateStockWithCompleteOrder(restockOrderId);
        }

        //발주하기 status  WRITING ->ORDERED , 새로운 장바구니 만들기
        if (previousStatus == RestockOrderEntity.RestockStatus.WRITING && restockOrder.getStatus() != RestockOrderEntity.RestockStatus.WRITING) {

            createNewRestockOrder(loginId);

            if(restockOrder.getStatus() == RestockOrderEntity.RestockStatus.ORDERED) {
                Integer storeId = storeRepository.findByLoginId(loginId);
                Optional<StoreEntity> store = storeRepository.findById(storeId);
                String storeName = store.get().getStoreName();

                //restock order에서, order specific 접근해서 주문할 재료, 수량 저장
                List<RestockOrderSpecificEntity> restockOrderSpecificEntityList = restockOrder.getRestockOrderSpecific();

                Map<Integer, Integer> order = new HashMap<>();
                for(RestockOrderSpecificEntity restockOrderSpecific : restockOrderSpecificEntityList){
                    Integer ingredientId = restockOrderSpecific.getIngredientId();
                    Integer ingredientQuantity = restockOrderSpecific.getIngredientQuantity();
                    if (order.containsKey(ingredientId)) {
                        order.put(ingredientId, order.get(ingredientId) + ingredientQuantity);
                    } else {
                        order.put(ingredientId, ingredientQuantity);
                    }
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
                    requestDto.setVenderManagerContact(contactEntry.getKey());
                    requestDto.setOrderList(contactEntry.getValue());
                    requestDtoList.add(requestDto);
                }
                smsUtil.sendOne(requestDtoList,restockOrderId,storeName);
            }
        }

        UpdatedRestockDto updatedRestockDto = new UpdatedRestockDto();
        updatedRestockDto.setRestockOrderId(restockOrderId);
        updatedRestockDto.setCreatedAt(restockOrder.getCreatedAt());
        updatedRestockDto.setUpdatedAt(restockOrder.getUpdatedAt());
        updatedRestockDto.setStatus(restockOrder.getStatus());
        return updatedRestockDto;
    }

    //발주완료 재고반영
    @Transactional
    public void updateStockWithCompleteOrder(Integer restockOrderId) {
        List<RestockOrderSpecificEntity> restockOrderSpecifics = restockOrderSpecificRepository.findByRestockOrderRestockOrderId(restockOrderId);
        for (RestockOrderSpecificEntity specific : restockOrderSpecifics) {
            IngredientEntity ingredient = ingredientRepository.findByIngredientName(specific.getIngredientName());
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
                String ingredientName = specific.getIngredientName();
                IngredientEntity orderedIngredient = ingredientRepository.findByIngredientName(ingredientName);
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
    public AddSpecificResponse addSpecific(AddSpecificRequest addDto) {
        RestockOrderEntity restockOrder = restockOrderRepository.findById(addDto.getRestockOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Restock not found"));

        System.out.println("수동발주 1");
        RestockOrderSpecificEntity specific = new RestockOrderSpecificEntity();
        IngredientEntity ingredientEntity = ingredientRepository.findById(addDto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found"));
        specific.setIngredientName(ingredientEntity.getIngredientName());
        specific.setIngredientQuantity(addDto.getIngredientQuantity());
        specific.setIngredientPrice(ingredientEntity.getIngredientPrice() * addDto.getIngredientQuantity());
        specific.setIngredientId(ingredientEntity.getIngredientId());
        specific.setRestockOrder(restockOrder);
        restockOrderSpecificRepository.save(specific);

        System.out.println( "수동발주 2");
        AddSpecificResponse addSpecificResponse = new AddSpecificResponse();
        addSpecificResponse.setVenderName(ingredientEntity.getVender().getVenderName());
        addSpecificResponse.setDeliveryTime(ingredientEntity.getDeliveryTime());
        addSpecificResponse.setIngredientName(specific.getIngredientName());
        addSpecificResponse.setIngredientPrice(specific.getIngredientPrice());
        addSpecificResponse.setIngredientQuantity(specific.getIngredientQuantity());

        return addSpecificResponse;
    }

}
