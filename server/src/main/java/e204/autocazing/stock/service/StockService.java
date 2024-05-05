package e204.autocazing.stock.service;

import e204.autocazing.db.entity.IngredientEntity;
import e204.autocazing.db.entity.StockEntity;
import e204.autocazing.db.repository.IngredientRepository;
import e204.autocazing.db.repository.StockRepository;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.stock.dto.UpdateStockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

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
        return stockRepository.findAll().stream()
                .map(stock -> new StockDetailsDto(
                        stock.getStockId(),
                        stock.getQuantity(),
                        stock.getExpirationDate(),
                        stock.getIngredient().getIngredientId()))
                .collect(Collectors.toList());
    }

    // 재고 상세 조회
    public StockDetailsDto findStockById(Integer stockId) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        StockDetailsDto stockDetails = new StockDetailsDto();
        stockDetails.setStockId(stock.getStockId());
        stockDetails.setIngredientId(stock.getIngredient().getIngredientId());
        stockDetails.setExpirationDate(stock.getExpirationDate());
        stockDetails.setQuantity(stock.getQuantity());

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
}
