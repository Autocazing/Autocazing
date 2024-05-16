package e204.autocazing.stock.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.StockEntity;
import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.StockRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.stock.dto.UpdateStockDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


    @Transactional
    public void createStock(List<PostStockDto> postStockDtos,String loginId) {
        //StoreId 조회
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        // PostStockDto 리스트를 순회하며 StockEntity 저장
        for (PostStockDto postStockDto : postStockDtos) {
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
            stockDetailsList.add(stockDetails);
        }
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
        List<StockEntity> stocks = stockRepository.findByIngredientIngredientIdOrderByExpirationDateAsc(ingredientId);
        if (stocks.isEmpty()) {
            throw new RuntimeException("No stock found for ingredient ID: " + ingredientId);
        }

        //재고를 체크하는 로직
        for (StockEntity stock : stocks) {
            if (quantity <= 0) {
                break; // 필요한 재고를 모두 사용했으면 반복 종료
            }

            int currentQuantity = stock.getQuantity();
            //재고를 소진 했을때 삭제하는 건데,, 이걸 넣을지 말지 고민.
            if (currentQuantity <= quantity) {
                quantity -= currentQuantity; // 이 재고를 모두 사용
                stock.setQuantity(0); // 재고 소진
                stockRepository.delete(stock);
            } else {
                stock.setQuantity(currentQuantity - quantity);
                quantity = 0; // 필요한 재고를 모두 사용했으므로 0으로 설정
                stockRepository.save(stock);
            }

        }

        //재고 부족
        if (quantity > 0) {
            throw new RuntimeException("Insufficient stock for ingredient ID: " + ingredientId);
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
    public void removeExpiredStocks() {
        LocalDate today = LocalDate.now();
        stockRepository.deleteByExpirationDateBefore(today);
    }

    //유통기한 임박 상품 발주로직(총재고 - 임박상품 <= 재료.minimumCount)
    //todo : 언제 불러서 사용할지 정해야함.
    @Transactional
    public void checkAndOrderNearExpiryProducts() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        LocalDate today = LocalDate.now();

        ingredients.forEach(ingredient -> {
            LocalDate expireDay = today.plusDays(ingredient.getDeliveryTime()); // 배송기한 포함 계산
            int totalQuantity = stockRepository.sumQuantityByIngredient(ingredient.getIngredientId());
            int expiredQuantity = stockRepository.countByIngredientAndExpirationDateBefore(ingredient.getIngredientId(), expireDay);
            int quantity = totalQuantity - expiredQuantity;

            if (quantity <= ingredient.getMinimumCount()) {
                restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
            }
        });
    }


}
