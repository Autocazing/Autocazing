package e204.autocazing.restock.controller;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.restock.dto.PostRestockDto;
import e204.autocazing.restock.dto.RestockDetailsDto;
import e204.autocazing.restock.dto.UpdateRestockDto;
import e204.autocazing.restock.service.RestockOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restocks")
public class RestockController {
    @Autowired
    private RestockOrderService restockOrderService;

    @PostMapping("")
    public ResponseEntity createRestockOrder(@RequestBody PostRestockDto postRestockDto) {
        restockOrderService.createRestockOrder(postRestockDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity getAllRestockOrders() {
        List<RestockDetailsDto> restocks = restockOrderService.findAllRestockOrders();
        return ResponseEntity.ok(restocks);
    }

    @GetMapping("/{restockOrderId}")
    public ResponseEntity getRestockOrderById(@PathVariable(name = "restockOrderId") Integer restockOrderId) {
        RestockDetailsDto restockDetailsDto =restockOrderService.findRestockOrderById(restockOrderId);
        return ResponseEntity.ok(restockDetailsDto);
    }

    @PutMapping("/{restockOrderId}")
    public ResponseEntity updateRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId, @RequestBody UpdateRestockDto updateRestockDto) {
        RestockDetailsDto restockDetails = restockOrderService.updateRestockOrder(restockOrderId, updateRestockDto);
        return ResponseEntity.ok(restockDetails);
    }

    @DeleteMapping("/{restockOrderId}")
    public ResponseEntity deleteRestockOrder(@PathVariable(name = "restockOrderId") Integer restockOrderId) {
        restockOrderService.deleteRestockOrder(restockOrderId);
        return ResponseEntity.ok().build();
    }
}
