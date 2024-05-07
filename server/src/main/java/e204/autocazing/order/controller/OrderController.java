package e204.autocazing.order.controller;

import e204.autocazing.order.dto.DetailOrderResponseDto;
import e204.autocazing.order.dto.OrderRequestDto;
import e204.autocazing.order.dto.OrderResponseDto;
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
        DetailOrderResponseDto detailOrder = orderService.getOrderSpecific(orderId);
        return new ResponseEntity(detailOrder, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity addOrder(@RequestBody OrderRequestDto orderRequestDto){
        orderService.addOrder(orderRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity alterOrder(@PathVariable(name = "orderId") Integer orderId){
        orderService.deleteOrder(orderId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
