package e204.autocazing.stock.service;

import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.*;
import e204.autocazing.exception.InsufficientStockException;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.restockSpecific.dto.UpdateRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import e204.autocazing.stock.dto.*;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.providers.HateoasHalProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RestockOrderService restockOrderService;
    @Autowired
    private RestockSpecificService restockSpecificService;
    @Autowired
    private RestockOrderSpecificRepository restockOrderSpecificRepository;
    @Autowired
    private RestockOrderRepository restockOrderRepository;


    @Transactional
    public void createStock(PostRequestDto postRequestDto, String loginId) {
        //StoreId 조회
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        List<PostStockDto> postStockDtoList=postRequestDto.getPostStockDtoList();
        // PostStockDto 리스트를 순회하며 StockEntity 저장
        for (PostStockDto postStockDto : postStockDtoList) {
            // IngredientEntity 조회
            IngredientEntity ingredientEntity = ingredientRepository.findById(postStockDto.getIngredientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + postStockDto.getIngredientId()));

            // StockEntity 생성 및 저장
            StockEntity stockEntity = new StockEntity();
            stockEntity.setQuantity(postStockDto.getQuantity());
            stockEntity.setExpirationDate(postStockDto.getExpirationDate());
            stockEntity.setIngredient(ingredientEntity);
            stockEntity.setStoreId(storeId); // StoreId 설정

            stockRepository.save(stockEntity);
            //재고 반영 후 , 자동발주로 추가된 재료들은 COMPLETE처리하기.
            //엑셀로 처리할때
            if(postRequestDto.getOnExcel()){
                //발주된 재료 완료하러가기
                //(restockOrderSpecificId,UpdateRestockSpecificDto)
                //재료id , StoreId
                Integer ingredientId = ingredientEntity.getIngredientId();
                RestockOrderSpecificEntity restockOrderSpecificEntity = restockOrderSpecificRepository.findTopByIngredientIdAndStatusOrderByCreatedAtAsc(
                                ingredientId, RestockOrderSpecificEntity.RestockSpecificStatus.ARRIVED)
                        .orElseThrow(() -> new RuntimeException("No ARRIVED restock order specific found for ingredientId: " + ingredientId));

                restockOrderSpecificEntity.setStatus(RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE);
                restockOrderSpecificRepository.save(restockOrderSpecificEntity);
                //여기서 RestockOrder COMPLETE 체크?
                checkRestockComplete(restockOrderSpecificEntity.getRestockOrderSpecificId());
//                restockSpecificService.updateRestockOrderSpecific(restockOrderSpecificEntity.getRestockOrderSpecificId(),RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE);
            }
        }

    }

    private void checkRestockComplete(Integer restockOrderSpecificId) {

        RestockOrderEntity restockOrderEntity = restockOrderSpecificRepository.findRestockOrderByRestockOrderSpecificId(restockOrderSpecificId);
        // 해당 RestockOrderEntity의 모든 RestockOrderSpecificEntity를 가져옴
        List<RestockOrderSpecificEntity> specifics = restockOrderSpecificRepository.findByRestockOrder_RestockOrderId(restockOrderEntity.getRestockOrderId());

        // 모든 RestockOrderSpecificEntity의 상태가 COMPLETE인지 확인
        boolean allComplete = specifics.stream()
                .allMatch(specific -> specific.getStatus() == RestockOrderSpecificEntity.RestockSpecificStatus.COMPLETE);

        // 모든 상태가 COMPLETE이면 RestockOrderEntity의 상태를 COMPLETE으로 변경
        if (allComplete) {
            restockOrderEntity.setStatus(RestockOrderEntity.RestockStatus.COMPLETE);
            restockOrderRepository.save(restockOrderEntity);
        }

    }

    // 전체 재고 조회
    public List<StockDetailsDto> findAllStocks(String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        System.out.println("storeId :" + storeId);
        // 전체 재고를 조회하고 StockDetailsDto 리스트로 변환합니다.
        List<StockEntity> stocks = stockRepository.findAllByStoreId(storeId);

        List<StockDetailsDto> stockDetailsList = new ArrayList<>();
        log.info("재고 조회 전");
        for (StockEntity stock : stocks) {
            int deliveringCount = restockOrderService.isDelivering(stock.getIngredient());
            log.info("재고 조회 deliveringCount");
            StockDetailsDto stockDetails = new StockDetailsDto();
            stockDetails.setStockId(stock.getStockId());
            stockDetails.setIngredientId(stock.getIngredient().getIngredientId());
            stockDetails.setIngredientName(stock.getIngredient().getIngredientName());
            stockDetails.setExpirationDate(stock.getExpirationDate());
            stockDetails.setQuantity(stock.getQuantity());
            stockDetails.setDeliveringCount(deliveringCount);
            stockDetails.setUsed(stock.getUsed());
            stockDetailsList.add(stockDetails);
        }
        //이건 뭐지?
        System.out.println("SIZE : " + stockDetailsList.size());
        return stockDetailsList;
    }

    // 재고 상세 조회
    public StockDetailsDto findStockById(Integer stockId) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        int deliveringCount = restockOrderService.isDelivering(stock.getIngredient());
        System.out.println("@@StockService deliveringCount : " + deliveringCount);
        StockDetailsDto stockDetails = new StockDetailsDto();
        stockDetails.setStockId(stock.getStockId());
        stockDetails.setIngredientId(stock.getIngredient().getIngredientId());
        stockDetails.setExpirationDate(stock.getExpirationDate());
        stockDetails.setQuantity(stock.getQuantity());
        stockDetails.setDeliveringCount(deliveringCount);
        //setDeliveringCount
        return stockDetails;
    }

    // 재고 수정
    public StockDetailsDto updateStock(Integer stockId, UpdateStockDto stockDto) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.setQuantity(stockDto.getQuantity());
        stock.setExpirationDate(stockDto.getExpirationDate());
        stockRepository.save(stock);
        StockDetailsDto updateStock = new StockDetailsDto();
        updateStock.setIngredientId(stock.getIngredient().getIngredientId());
        updateStock.setQuantity(stock.getQuantity());
        updateStock.setStockId(stockId);
        updateStock.setExpirationDate(stock.getExpirationDate());

        return updateStock;
    }

    // 재고 삭제
    public void deleteStock(Integer stockId) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stockRepository.delete(stock);
    }

    //주문 들어온 메뉴의 들어가는 재료를 재고에서 빼기.
    //뺀 후에 재고 체크를 통해 자동 발주 신청 까지.
    @Transactional
    public void decreaseStock(Integer ingredientId, Integer quantity) {
        int requiredQuantity = quantity;
        List<StockEntity> stocks = stockRepository.findByIngredientIngredientIdOrderByExpirationDateAsc(ingredientId);
        System.out.println("stocks SIZE : " + stocks.size());
        if (stocks.isEmpty()) {
            throw new RuntimeException("No stock found for ingredient ID: " + ingredientId);
        }
        System.out.println("decrese 인데 여기는 오니?");

        Integer ingredientCapacity = ingredientRepository.findIngredientCapacityByIngredientId(ingredientId);

        System.out.println("ingredientId : " + ingredientId);
        System.out.println("ingredientCapacity : " + ingredientCapacity);
        System.out.println("check Stock");
        //재고를 체크하는 로직
        for (StockEntity stock : stocks) {
            System.out.println("start");
            if (requiredQuantity <= 0) {
                break; // 필요한 재고를 모두 사용했으면 반복 종료
            }

            //가지고 있는 수량
            int currentQuantity = stock.getQuantity();
            //가지고 있는 수량을 이용하여 현재 stock 의 총 재고 체크
            int currentTotal = (currentQuantity * ingredientCapacity) -stock.getUsed();

            //필요한 재료량이 현재 stock의 총 재고보다 적을때
            if(currentTotal <= requiredQuantity){
                requiredQuantity -= currentTotal; // 이 재고를 모두 사용
                stock.setQuantity(0); // 재고 소진
                stockRepository.delete(stock);
            }

            //현재 가지고있는 stock으로 처리가 가능할때
            else {
                while(requiredQuantity > 0){
                    //현재 사용중인 남은 헌 것
                    int unUsedStock = ingredientCapacity - stock.getUsed();
                    //필요량 < 현재 남은 헌것
                    if(requiredQuantity < unUsedStock){
                        //unUsedStock = unUsedStock - requiredQuantity;
                        //사용량 = 기존사용량 + 필요량
                        stock.setUsed(stock.getUsed() + requiredQuantity);
                        stockRepository.save(stock);
                        requiredQuantity = 0;
                    }
                    //필요량 > 현재남은 헌것 (새거 개봉해야됨)
                    else{
                        requiredQuantity -= unUsedStock;
                        stock.setUsed(stock.getUsed() + unUsedStock);
                        if (Objects.equals(stock.getUsed(), ingredientCapacity)){
                            stock.setQuantity(stock.getQuantity()-1);
                            stock.setUsed(0);
                        }
                        if(stock.getQuantity() == 0) {
                            stockRepository.delete(stock);
                            break;
                        }else{
                            System.out.println("여기 들어올 일이 있나?");
                            stockRepository.save(stock);
                        }
                    }
                }
            }
            System.out.println("end");
        }
        System.out.println("checkout End");
        System.out.println("탈출 못하나?");
        String name = ingredientRepository.findIngredientNameByIngredientId(ingredientId);
        //재고 부족
        if (requiredQuantity > 0) {
            throw new InsufficientStockException("Insufficient stock for ingredient: " + name);
        }
    }

    //todo : 아래 부분 이용 할거같은데 아직 이용을 안함
    //재고체크 재료의 수량이 설정한 값보다 낮으면 발주 리스트 추가.
    @Transactional
    public void checkAndAddRestockOrderSpecifics() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();

        for (IngredientEntity ingredient : ingredients) {
            StockEntity stock = stockRepository.findByIngredient(ingredient);
            if (stock != null && stock.getQuantity() <= ingredient.getMinimumCount()) {
                restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
            }
        }
    }

    //유통기한 지난 상품 삭제
    //todo : 트리거 설정을 어떻게 해야할지 정해야함.
    @Transactional
    @Scheduled(cron = "0 5 12 * * ?") // 매일 자정에 실행
    public void removeExpiredStocks() {
        //모든 유저한테 해당
        LocalDate today = LocalDate.now();
        stockRepository.deleteByExpirationDateBefore(today);
    }

    //유통기한 임박 상품 발주로직(총재고 - 임박상품 <= 재료.minimumCount)

    //    @Scheduled(cron = "0 0 1 * * ?") //매일 새벽 1시에 실행
    @Transactional
    @Scheduled(cron = "0 5 12 * * ?") // 매일 오전 12시 46분에 실행
    public HashMap<String,List<NearExpiredDto>> checkAndOrderNearExpiryProducts() {
        System.out.println("시작하겠습니다");
        List<StoreEntity> storeEntities = storeRepository.findAll();
        HashMap<String,List<NearExpiredDto>> hashMap = new HashMap<>();
        for(StoreEntity storeEntity : storeEntities){
            System.out.println("loginId : " + storeEntity.getLoginId());
            List<IngredientEntity> ingredients = ingredientRepository.findAllByStore(storeEntity);
            LocalDate today = LocalDate.now();
            List<NearExpiredDto> nearExpiredDtoList = new ArrayList<>();
            ingredients.forEach(ingredient -> {
                LocalDate expireDay = today.plusDays(ingredient.getDeliveryTime()); // 배송기한 포함 계산
                int totalQuantity = stockRepository.sumQuantityByIngredient(ingredient.getIngredientId());
                int expiredQuantity = stockRepository.countByIngredientAndExpirationDateBefore(ingredient.getIngredientId(), expireDay);
                int quantity = totalQuantity - expiredQuantity;
                //유통기한 임박상품이 있다면
                if(expiredQuantity > 0 ) {
                    System.out.println("추가하겠심더~");
                    NearExpiredDto nearExpiredDto = new NearExpiredDto();
                    nearExpiredDto.setIngredientId(ingredient.getIngredientId());
                    System.out.println("@@@@@@@@@@@@ Name: " + ingredient.getIngredientName());
                    nearExpiredDto.setIngredientName(ingredient.getIngredientName());
                    nearExpiredDto.setQuantity(expiredQuantity);
                    nearExpiredDtoList.add(nearExpiredDto);


                    //유통기한 임박상품 재고 수량이 기준치보다 작다면 발주
//                    if (quantity <= ingredient.getMinimumCount()) {
//                        restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
//                    }
                }
                else{
                    System.out.println("추가할게 없나봅니다~");
                }
            });

            hashMap.put(storeEntity.getLoginId(),nearExpiredDtoList);

            System.out.println("이번 계정 끝");
        }//계정마다
        System.out.println("hashmap size: " +hashMap.size());
        System.out.println(hashMap);
//        return nearExpiredDtoList;
        return hashMap;
    }

}
