package e204.autocazing.restock.dto;

import e204.autocazing.db.entity.RestockOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestockDetailsDto {

    private Integer restockOrderId;
    private RestockOrderEntity.RestockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
