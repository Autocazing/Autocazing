package e204.autocazing.order.service;

import e204.autocazing.db.entity.*;
import e204.autocazing.db.repository.*;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.kafka.cluster.KafkaProducerCluster;
import e204.autocazing.kafka.entity.alert.IngredientWarnEntity;
import e204.autocazing.kafka.entity.alert.IngredientWarnInfoEntity;
import e204.autocazing.kafka.entity.ProducerEntity;
import e204.autocazing.kafka.entity.solution.order.KafkaOrderSpecific;
import e204.autocazing.kafka.entity.solution.order.OrderCreateEntity;
import e204.autocazing.order.dto.*;
import e204.autocazing.restock.dto.AddSpecificRequest;
import e204.autocazing.restock.service.RestockOrderService;
import e204.autocazing.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private KafkaProducerCluster kafkaProducerCluster;
    @Autowired
    private MenuIngredientRepository menuIngredientRepository;

    public List<OrderResponseDto> getAllOrders(String loginId) {
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));

        return orderRepository.findByStore(storeEntity).stream()
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
    public void addOrder(PostOrderDto postOrderDto, String loginId) {

        OrderEntity order = new OrderEntity();
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));

        order.setStore(storeEntity);
        OrderCreateEntity kafkaOrderCreateEntity = new OrderCreateEntity(1, new ArrayList<>());
        List<OrderSpecific> orderSpecifics = postOrderDto.getOrderSpecifics().stream()
                .map(detail -> {
                    MenuEntity menu = menuRepository.findByMenuId(detail.getMenuId());
                    if (menu == null) {
                        throw new IllegalStateException("Menu not found: " + detail.getMenuId());
                    }

                    //menu가 할인된 상품이면 할인 가격 적용
                    Integer price = 0;
                    if (menu.getOnEvent() != null && menu.getOnEvent()) {
                        price = (int) (menu.getMenuPrice() * (1 - (menu.getDiscountRate() / 100.0)));
                    } else {
                        price = menu.getMenuPrice();
                    }
                    // kafka order entity에 order specific 저장
                    kafkaOrderCreateEntity.getOrderSpecifics().add(new KafkaOrderSpecific(menu.getMenuId(), menu.getMenuName(), detail.getMenuQuantity(), menu.getMenuPrice()));
                    return new OrderSpecific(detail.getMenuId(), detail.getMenuQuantity(), price);
                })
                .toList();
        order.setOrderSpecific(orderSpecifics);

        // 재료와 재고 처리 로직
        postOrderDto.getOrderSpecifics().forEach(detail -> {
            MenuEntity menu = menuRepository.findByMenuId(detail.getMenuId());
            menu.getMenuIngredients().forEach(menuIngredient -> {
                stockService.decreaseStock(menuIngredient.getIngredient().getIngredientId(), menuIngredient.getCapacity() * detail.getMenuQuantity());
            });
        });

        kafkaOrderCreateEntity.setOrderId(orderRepository.save(order).getOrderId());    // kafka message에 orderId 설정

        // kafka message 발신
        kafkaProducerCluster.sendProducerMessage("sales_refresh", loginId, new ProducerEntity("SALES", "Refresh sales"));
        kafkaProducerCluster.sendOrderCreateMessage("order", loginId, kafkaOrderCreateEntity);
    }


    //자동발주
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndAddRestockOrderSpecifics(List<Integer> menuIdList,String loginId)  {
        StoreEntity storeEntity = storeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with loginId: " + loginId));
//        List<IngredientEntity> ingredients = ingredientRepository.findByStore(storeEntity);

        List<IngredientEntity> ingredients = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            List<IngredientEntity> ingredientsForMenu = menuIngredientRepository.findIngredientsByMenuId(menuId);
            ingredients.addAll(ingredientsForMenu);
        }

        ingredients.forEach(ingredient -> {
            //총 재고 조회 (같은 재료 유통기한만 다른것도)
            Integer totalQuantity = stockRepository.findTotalQuantityByIngredientId(ingredient.getIngredientId());
            System.out.println("@@@@@@@@@@@@@@@totalQuantity : " + totalQuantity);
//            StockEntity stock = stockRepository.findByIngredient(ingredient);
            //배송중인 물품 수
            int totalDelivering = restockOrderService.isDelivering(ingredient, loginId);
            System.out.println("@@@@@@@@@@@@@@@totalDelivering : " + totalDelivering);
            //minimumCount 보다 낮을때 발주에 재료 등록
            if (totalQuantity + totalDelivering <= ingredient.getMinimumCount()) {
                // WRITING 상태의 가장 최신 RestockOrder 조회
                RestockOrderEntity restockOrderEntity = restockOrderRepository.findFirstByStatusAndStoreOrderByCreatedAtDesc(RestockOrderEntity.RestockStatus.WRITING,storeEntity)
                        .orElseThrow(() -> new RuntimeException("No WRITING status RestockOrder found"));
                AddSpecificRequest addSpecificRequest = new AddSpecificRequest();
//                addSpecificRequest.setRestockOrderId(restockOrderEntity.getRestockOrderId());
                addSpecificRequest.setIngredientId(ingredient.getIngredientId());
                addSpecificRequest.setIngredientQuantity(ingredient.getOrderCount());

                kafkaProducerCluster.sendIngredientWarnMessage("ingredient_warn", loginId, new IngredientWarnEntity("INGREDIENT_WARN", new IngredientWarnInfoEntity(restockOrderEntity.getRestockOrderId(), ingredient.getIngredientId(), ingredient.getIngredientName(), ingredient.getOrderCount())));
                // 프론트한테 이 알림 받으면 /api/restocks/specifics로 요청 보내는데, type을 auto로 줘라고 하기
                restockOrderService.addSpecific("auto",addSpecificRequest,loginId);
//                restockOrderService.addRestockOrderSpecific(ingredient, ingredient.getOrderCount());
            }
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
