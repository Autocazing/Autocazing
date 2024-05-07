package e204.autocazing.order.controller;


import e204.autocazing.exception.RestockProcessingException;
import e204.autocazing.order.dto.DetailOrderResponseDto;
import e204.autocazing.order.dto.OrderRequestDto;
import e204.autocazing.order.dto.OrderResponseDto;
import e204.autocazing.order.dto.PostOrderDto;
import e204.autocazing.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity getOrders(){
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity detailsOrder(@PathVariable(name = "orderId") Integer orderId){
        OrderResponseDto detailOrder = orderService.getOrderById(orderId);
        return new ResponseEntity(detailOrder, HttpStatus.OK);
    }

    //여기 예외처리 추가할 예정.
    @PostMapping("")
    public ResponseEntity addOrder(@RequestBody PostOrderDto postOrderDto){

            //주문 받기 및 재고 검사로 주문 받을 수 있는 지 검사 + 재고 재료 사용한만큼 줄이기.
            orderService.addOrder(postOrderDto);
            // 발주 검사 및 발주추가
            orderService.checkAndAddRestockOrderSpecifics();
            return ResponseEntity.ok(HttpStatus.CREATED);

    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity alterOrder(@PathVariable(name = "orderId") Integer orderId){
        orderService.deleteOrder(orderId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
