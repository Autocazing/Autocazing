package e204.autocazing.restock.service;

import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.RestockOrderRepository;
import e204.autocazing.db.repository.RestockOrderSpecificRepository;
import e204.autocazing.db.repository.StockRepository;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.restock.dto.PostRestockDto;
import e204.autocazing.restock.dto.RestockDetailsDto;
import e204.autocazing.restock.dto.RestockOrderStatusDto;
import e204.autocazing.restock.dto.UpdateRestockDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
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


    // Create
    @Transactional
    public void createRestockOrder(PostRestockDto postRestockDto) {
        RestockOrderEntity restockOrder = new RestockOrderEntity();
        restockOrder.setStatus(postRestockDto.getStatus());
        StoreEntity store = storeRepository.findById(postRestockDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + postRestockDto.getStoreId()));
        restockOrder.setStore(store);
        restockOrderRepository.save(restockOrder);
    }

    // Read All
    public List<RestockDetailsDto> findAllRestockOrders(List<RestockOrderStatusDto> status) {
        List<RestockOrderEntity> orders;
        if (status == null || status.isEmpty()) {
            orders = restockOrderRepository.findAll();
        } else {
            orders = restockOrderRepository.findByStatusIn(status);
        }
        return orders.stream()
                .map(restock -> new RestockDetailsDto(
                        restock.getRestockOrderId(),
                        restock.getStatus(),
                        restock.getCreatedAt(),
                        restock.getUpdatedAt()))
                .collect(Collectors.toList());
    }


    // Read Single
    public RestockDetailsDto findRestockOrderById(Integer restockOrderId) {
        RestockOrderEntity restock = restockOrderRepository.findById(restockOrderId)
                .orElseThrow(() -> new RuntimeException("Restock order not found"));
        RestockDetailsDto restockDetailsDto = new RestockDetailsDto();
        restockDetailsDto.setRestockOrderId(restockOrderId);
        restockDetailsDto.setStatus(restock.getStatus());
        restockDetailsDto.setCreatedAt(restock.getCreatedAt());
        restockDetailsDto.setUpdatedAt(restock.getUpdatedAt());
        return restockDetailsDto;
    }

    // RestockOrder수정
    //RestockOrder status 가 WRITING 에서 변경된게 감지될때, 새로운 RestockOrder 생성.
    @Transactional
    public RestockDetailsDto updateRestockOrder(Integer restockOrderId, UpdateRestockDto updateRestockDto) {
        RestockOrderEntity restockOrder = restockOrderRepository.findById(restockOrderId)
                .orElseThrow(() -> new RuntimeException("Restock order not found"));

        RestockOrderEntity.RestockStatus previousStatus = restockOrder.getStatus(); // 기존 상태 저장
        restockOrder.setStatus(updateRestockDto.getStatus());
        restockOrderRepository.save(restockOrder);
        //발주 상태가 완료 되면 재고에 해당 발주물품 추가
        if(restockOrder.getStatus() == RestockOrderEntity.RestockStatus.COMPLETE){
            List<RestockOrderSpecificEntity> restockOrderSpecifics = restockOrderSpecificRepository.findByRestockOrderRestockOrderId(restockOrderId);
            for (RestockOrderSpecificEntity specific : restockOrderSpecifics) {
                IngredientEntity ingredient = specific.getIngredient();
                Integer quantityToAdd = specific.getIngredientQuantity();
                // 재료에 해당하는 Stock 엔터티 찾기
                StockEntity stock = stockRepository.findByIngredient(ingredient);
                if (stock == null) {
                    // Stock에 해당 재료가 없는 경우 새로 생성
                    stock = new StockEntity();
                    stock.setIngredient(ingredient);
                    stock.setQuantity(quantityToAdd);
                    // 유통 기한은 임시로 1년.
                    stock.setExpirationDate(LocalDate.now().plusYears(1));
                } else {
                    // Stock에 해당 재료가 있는 경우 수량 추가
                    stock.setQuantity(stock.getQuantity() + quantityToAdd);
                }
                // Stock 저장
                stockRepository.save(stock);
            }
        }

        // 상태가 WRITING에서 변경되었을 때 새로운 RestockOrder 생성
        if (previousStatus == RestockOrderEntity.RestockStatus.WRITING && restockOrder.getStatus() != RestockOrderEntity.RestockStatus.WRITING) {
            PostRestockDto postRestockDto = new PostRestockDto();
            createRestockOrder(postRestockDto);
        }

        RestockDetailsDto restockDetailsDto = new RestockDetailsDto();
        restockDetailsDto.setRestockOrderId(restockOrderId);
        restockDetailsDto.setCreatedAt(restockOrder.getCreatedAt());
        restockDetailsDto.setUpdatedAt(restockOrder.getUpdatedAt());
        restockDetailsDto.setStatus(restockOrder.getStatus());
        return restockDetailsDto;
    }

    // Delete
    @Transactional
    public void deleteRestockOrder(Integer restockOrderId) {
        restockOrderRepository.deleteById(restockOrderId);
    }

    @Transactional
    public void addRestockOrderSpecific(IngredientEntity ingredient, int quantity) {
        // WRITING 상태의 가장 최신 RestockOrder 조회
        RestockOrderEntity restockOrder = restockOrderRepository.findFirstByStatusOrderByCreatedAtDesc(RestockOrderEntity.RestockStatus.WRITING)
                .orElseThrow(() -> new RuntimeException("No WRITING status RestockOrder found"));

        // 새로운 RestockOrderSpecific 생성 및 추가
        RestockOrderSpecificEntity restockOrderSpecific = new RestockOrderSpecificEntity();
        restockOrderSpecific.setRestockOrder(restockOrder);
        restockOrderSpecific.setIngredient(ingredient);
        restockOrderSpecific.setIngredientQuantity(quantity);
        restockOrderSpecific.setIngredientPrice(ingredient.getIngredientPrice() * quantity);
        restockOrderSpecificRepository.save(restockOrderSpecific);
    }

    //배송중인지 체크하기 이게 Order 도메인에 있는게 맞나?
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
                IngredientEntity orderedIngredient = specific.getIngredient();
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
}
