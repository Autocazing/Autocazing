package e204.autocazing.order.controller;

import e204.autocazing.order.dto.DetailOrderResponseDto;
import e204.autocazing.order.dto.OrderRequestDto;
import e204.autocazing.order.dto.OrderResponseDto;
import e204.autocazing.order.dto.PostOrderDto;
import e204.autocazing.order.service.OrderService;
import e204.autocazing.sale.dto.SalesResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "주문 전체 조회", description = "주문 전체조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity getOrders(HttpServletRequest httpServletRequest){
        String loginId = httpServletRequest.getHeader("loginId");
        List<OrderResponseDto> orders = orderService.getAllOrders(loginId);
        return ResponseEntity.ok(orders);

    }

    @Operation(summary = "주문 상세 조회", description = "주문 상세조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDto.class)
                    )
            )
    })
    @GetMapping("/{orderId}")
    public ResponseEntity detailsOrder(@PathVariable(name = "orderId") Integer orderId){
        OrderResponseDto detailOrder = orderService.getOrderById(orderId);
        return new ResponseEntity(detailOrder, HttpStatus.OK);
    }

    //여기 예외처리 추가할 예정.
    @Operation(summary = "주문 요청", description = "(포스기) 주문요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 요청 성공",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PostOrderDto.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity addOrder(@RequestBody PostOrderDto postOrderDto, HttpServletRequest httpServletRequest){
        String loginId = httpServletRequest.getHeader("loginId");
        //주문 받기 및 재고 검사로 주문 받을 수 있는 지 검사 + 재고 재료 사용한만큼 줄이기.
        orderService.addOrder(postOrderDto,loginId);
        // 발주 검사 및 발주추가
        orderService.checkAndAddRestockOrderSpecifics(loginId);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Operation(summary = "주문 삭제", description = "(포스기) 주문삭제 요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "코스 삭제 성공",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Post 삭제 body",
                                    summary = "Post 삭제 body의 예시",
                                    value = " "
                            )
                    })
            )
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable(name = "orderId") Integer orderId){
        orderService.deleteOrderById(orderId);
        return new ResponseEntity(HttpStatus.OK);
    }
}