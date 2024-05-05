package e204.autocazing.restock.service;

import e204.autocazing.db.entity.RestockOrderEntity;
import e204.autocazing.db.repository.RestockOrderRepository;
import e204.autocazing.restock.dto.PostRestockDto;
import e204.autocazing.restock.dto.RestockDetailsDto;
import e204.autocazing.restock.dto.UpdateRestockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestockOrderService {
    @Autowired
    private RestockOrderRepository restockOrderRepository;

    // Create
    @Transactional
    public void createRestockOrder(PostRestockDto postRestockDto) {
        RestockOrderEntity restockOrder = new RestockOrderEntity();
        restockOrder.setStatus(postRestockDto.getStatus());
        restockOrderRepository.save(restockOrder);
    }

    // Read All
    public List<RestockDetailsDto> findAllRestockOrders() {
        return restockOrderRepository.findAll().stream()
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

    // Update
    @Transactional
    public RestockDetailsDto updateRestockOrder(Integer restockOrderId, UpdateRestockDto updateRestockDto) {
        RestockOrderEntity restockOrder = restockOrderRepository.findById(restockOrderId)
                .orElseThrow(() -> new RuntimeException("Restock order not found"));

        restockOrder.setStatus(updateRestockDto.getStatus());
        restockOrderRepository.save(restockOrder);
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
}
