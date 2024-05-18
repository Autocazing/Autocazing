package e204.autocazing.kafka.entity.solution.expiration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpirationEntity {

    List<ExpirationIngredients> expirationIngredients;

}
