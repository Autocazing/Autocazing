package e204.autocazing.stock.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.StockEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.StockRepository;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.stock.dto.UpdateStockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RestockOrderService restockOrderService;


    public void createStock(PostStockDto postStockDto) {
        IngredientEntity ingredient = ingredientRepository.findById(postStockDto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Not found ingredient ID"));

        StockEntity stock = new StockEntity();
        stock.setQuantity(postStockDto.getQuantity());
        stock.setExpirationDate(postStockDto.getExpirationDate());
        stock.setIngredient(ingredient);
        stockRepository.save(stock);
    }

    // 전체 재고 조회
    public List<StockDetailsDto> findAllStocks() {
        // 전체 재고를 조회하고 StockDetailsDto 리스트로 변환합니다.
        List<StockEntity> stocks = stockRepository.findAll();
        List<StockDetailsDto> stockDetailsList = new ArrayList<>();


        for (StockEntity stock : stocks) {
            int deliveringCount = restockOrderService.isdelivering(stock.getIngredient());
            StockDetailsDto stockDetails = new StockDetailsDto();
            stockDetails.setStockId(stock.getStockId());
            stockDetails.setIngredientId(stock.getIngredient().getIngredientId());
            stockDetails.setExpirationDate(stock.getExpirationDate());
            stockDetails.setQuantity(stock.getQuantity());
            stockDetails.setDeliveringCount(deliveringCount);
            stockDetailsList.add(stockDetails);
        }
        return stockDetailsList;
    }

    // 재고 상세 조회
    public StockDetailsDto findStockById(Integer stockId) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        int deliveringCount = restockOrderService.isdelivering(stock.getIngredient());
        System.out.println("@@StockService deliveringCount : " + deliveringCount);
        StockDetailsDto stockDetails = new StockDetailsDto();
        stockDetails.setStockId(stock.getStockId());
        stockDetails.setIngredientId(stock.getIngredient().getIngredientId());
        stockDetails.setExpirationDate(stock.getExpirationDate());
        stockDetails.setQuantity(stock.getQuantity());
        stockDetails.setDeliveringCount(deliveringCount);
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

        for (StockEntity stock : stocks) {
            if (quantity <= 0) {
                break; // 필요한 재고를 모두 사용했으면 반복 종료
            }

            int currentQuantity = stock.getQuantity();
            if (currentQuantity <= quantity) {
                quantity -= currentQuantity; // 이 재고를 모두 사용
                stock.setQuantity(0); // 재고 소진
            } else {
                stock.setQuantity(currentQuantity - quantity);
                quantity = 0; // 필요한 재고를 모두 사용했으므로 0으로 설정
            }
            stockRepository.save(stock);
        }

        //재고 부족
        if (quantity > 0) {
            throw new RuntimeException("Insufficient stock for ingredient ID: " + ingredientId);
        }
    }

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

}
