package e204.autocazing.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;

import e204.autocazing.kafka.cluster.KafkaProducerCluster;
import e204.autocazing.kafka.entity.ProducerEntity;
import e204.autocazing.kafka.entity.IngredientWarnEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaTestController {

	private final KafkaProducerCluster producer;

	@PostMapping("/kafka/produce/saleRefresh")
	public String sendSalesMessage(@RequestBody ProducerEntity testDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
		String loginId = httpServletRequest.getHeader("loginId");

		producer.sendProducerMessage("sales_refresh", loginId, testDto);

		return "ok";
	}

	@PostMapping("/kafka/produce/deliveryRefresh")
	public String sendDeliveryMessage(@RequestBody ProducerEntity testDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
		String loginId = httpServletRequest.getHeader("loginId");

		producer.sendProducerMessage("delivery_refresh", loginId, testDto);

		return "ok";
	}

	@PostMapping("/kafka/produce/ingredientWarn")
	public String sendMessage(@RequestBody IngredientWarnEntity testDto, HttpServletRequest httpServletRequest) throws JsonProcessingException {
//		String loginId = httpServletRequest.getHeader("loginId");
//
//		IngredientWarnEntity kafkaEntity = new IngredientWarnEntity(testDto.getRestockOrderId(), testDto.getIngredientId(), testDto.getIngredientQuantity());
//		producer.sendIngredientWarnMessage("ingredient_warn", loginId, kafkaEntity);
//
		return "ok";
	}

}
