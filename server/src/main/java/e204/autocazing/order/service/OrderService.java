package e204.autocazing.order.service;

import e204.autocazing.db.entity.OrderEntity;
import e204.autocazing.db.repository.OrderRepository;
import e204.autocazing.exception.ResourceNotFoundException;
import e204.autocazing.order.dto.DetailOrderResponseDto;
import e204.autocazing.order.dto.OrderRequestDto;
import e204.autocazing.order.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponseDto> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        if(orders.isEmpty()) {
            System.out.println("텅텅빔요");
        }
        // 가져온 MapEntity 리스트를 MapResponseDto 리스트로 변환
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(orderEntity -> OrderResponseDto.fromEntity(orderEntity))
                .collect(Collectors.toList());

        return orderResponseDtos;
    }

    public DetailOrderResponseDto getOrderSpecific(Integer orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        DetailOrderResponseDto detailResponseDto = new DetailOrderResponseDto();
        detailResponseDto.setOrderId(orderEntity.getOrderId());
        // OrderSpecific 목록을 OrderDetailDto 리스트로 변환
        List<DetailOrderResponseDto.OrderDetailDto> orderDetails = orderEntity.getOrderSpecific().stream()
                .map(this::convertToOrderDetailDto)
                .collect(Collectors.toList());
        detailResponseDto.setOrderDetails(orderDetails); // 변환된 상세 목록을 DTO에 설정

        return detailResponseDto;
    }
    private DetailOrderResponseDto.OrderDetailDto convertToOrderDetailDto(OrderEntity.OrderSpecific orderSpecific) {
        DetailOrderResponseDto.OrderDetailDto elementDto = new DetailOrderResponseDto.OrderDetailDto();
        elementDto.setMenuId(orderSpecific.getMenuId());
        elementDto.setQuantity(orderSpecific.getQuantity());
        elementDto.setPrice(orderSpecific.getPrice());
        return elementDto;
    }

    public void addOrder(OrderRequestDto orderRequestDto) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderEntity.OrderSpecific> orderSpecifics = orderRequestDto.getOrderDetails().stream()
                .map(detail -> {
                    OrderEntity.OrderSpecific specific = new OrderEntity.OrderSpecific(); // 인스턴스 생성 방법을 주목
                    specific.setMenuId(detail.getMenuId());
                    specific.setQuantity(detail.getQuantity());
                    specific.setPrice(detail.getPrice());
                    return specific;
                }).collect(Collectors.toList());
        orderEntity.setOrderSpecific(orderSpecifics);
        orderRepository.save(orderEntity); // 주문 정보 저장
    }

    public void deleteOrder(Integer orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if(orderEntity.isPresent()){
            orderRepository.deleteById(orderId);
            System.out.println("삭제완료");
        }
    }
}
