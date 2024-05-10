package e204.autocazing.order.service;

import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.*;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.order.dto.*;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private RestockOrderService restockOrderService;
    @Autowired
    private RestockOrderRepository restockOrderRepository;
    @Autowired
    private RestockOrderSpecificRepository restockOrderSpecificRepository;
    @Autowired
    private StoreRepository storeRepository;

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderResponseDto convertToDto(OrderEntity order) {
        List<OrderSpecificDto> orderSpecifics = order.getOrderSpecific().stream()
                .map(specific -> new OrderSpecificDto(specific.getMenuId(), specific.getMenuQuantity(), specific.getMenuPrice()))
                .collect(Collectors.toList());
        return new OrderResponseDto(order.getOrderId(), orderSpecifics);
    }

    public OrderResponseDto getOrderById(Integer orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<OrderSpecificDto> orderSpecifics = orderEntity.getOrderSpecific().stream()
                .map(specific -> new OrderSpecificDto(specific.getMenuId(), specific.getMenuQuantity(), specific.getMenuPrice()))
                .collect(Collectors.toList());

        return new OrderResponseDto(orderEntity.getOrderId(), orderSpecifics);

    }


        @Transactional
        public void addOrder(PostOrderDto postOrderDto) {

            OrderEntity order = new OrderEntity();
            StoreEntity store = storeRepository.findById(postOrderDto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("StoreId not found with id: " + postOrderDto.getStoreId()));
            order.setStore(store);
            System.out.println(store.getStoreName());

            List<OrderSpecific> orderSpecifics = postOrderDto.getOrderSpecifics().stream()
                    .map(detail -> {
                        MenuEntity menu = menuRepository.findByMenuId(detail.getMenuId());
                        if (menu == null) {
                            throw new IllegalStateException("Menu not found: " + detail.getMenuId());
                        }
                        return new OrderSpecific(detail.getMenuId(), detail.getMenuQuantity(), menu.getMenuPrice());
                    })
                    .toList();
            order.setOrderSpecific(orderSpecifics);

            // 재료와 재고 처리 로직
            postOrderDto.getOrderSpecifics().forEach(detail -> {
                MenuEntity menu = menuRepository.findByMenuId(detail.getMenuId());
                menu.getMenuIngredients().forEach(ingredient -> {
                    stockService.decreaseStock(ingredient.getIngredient().getIngredientId(), ingredient.getCapacity() * detail.getMenuQuantity());
                });
            });


            orderRepository.save(order);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndAddRestockOrderSpecifics()  {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        ingredients.forEach(ingredient -> {
            //총 재고 조회 (같은 재료 유통기한만 다른것도)
            Integer totalQuantity = stockRepository.findTotalQuantityByIngredientId(ingredient.getIngredientId());
            StockEntity stock = stockRepository.findByIngredient(ingredient);
            //배송중인 물품 수
            int totalDelivering = restockOrderService.isDelivering(ingredient);

            if (totalQuantity + totalDelivering <= ingredient.getMinimumCount()) {
                restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
            }

            //stock 이 null이거나 (새상품) 배송중 + 재고의 양이 재료의 설정값보다 적을때
//            if (stock == null || restockOrderService.isDelivering(ingredient) + stock.getQuantity() <= ingredient.getMinimumCount()) {
//                    restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
//            }
        });
    }



    public void deleteOrderById(Integer orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if(orderEntity.isPresent()){
            orderRepository.deleteById(orderId);
            System.out.println("삭제완료");
        }
    }


}
