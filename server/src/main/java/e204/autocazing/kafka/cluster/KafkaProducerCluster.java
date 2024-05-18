package e204.autocazing.kafka.cluster;

import java.util.concurrent.CompletableFuture;

import e204.autocazing.kafka.entity.solution.ingredient.IngredientCreateEntity;
import e204.autocazing.kafka.entity.ProducerEntity;
import e204.autocazing.kafka.entity.alert.IngredientWarnEntity;
import e204.autocazing.kafka.entity.solution.menu.MenuCreateEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerCluster {

	private final KafkaTemplate<String, IngredientWarnEntity> ingredientWarnKafkaTemplate;
	private final KafkaTemplate<String, ProducerEntity> producerKafkaTemplate;
	private final KafkaTemplate<String, IngredientCreateEntity> ingredientCreateKafkaTemplate;
	private final KafkaTemplate<String, MenuCreateEntity> menuCreateEntityKafkaTemplate;

	public void sendIngredientWarnMessage(String topicName, String loginId, IngredientWarnEntity message) {
		sendMessage(ingredientWarnKafkaTemplate, topicName, loginId, message);
	}

	public void sendProducerMessage(String topicName, String loginId, ProducerEntity message) {	// 배송, 매출 갱신 메시지 전송에 쓰임
		sendMessage(producerKafkaTemplate, topicName, loginId, message);
	}

	public void sendIngredientCreateMessage(String topicName, String loginId, IngredientCreateEntity ingredientCreateEntity) {
		sendMessage(ingredientCreateKafkaTemplate, topicName, loginId, ingredientCreateEntity);
	}

	public void sendMenuCreateMessage(String topicName, String loginId, MenuCreateEntity menuCreateEntity) {
		sendMessage(menuCreateEntityKafkaTemplate, topicName, loginId, menuCreateEntity);
	}

	private <T> void sendMessage(KafkaTemplate<String, T> kafkaTemplate, String topicName, String loginId, T message) {
		Message<T> kafkaMessage = MessageBuilder
			.withPayload(message)
			.setHeader(KafkaHeaders.TOPIC, topicName)
			.setHeader(KafkaHeaders.KEY, loginId)
			.build();

		CompletableFuture<SendResult<String, T>> future = kafkaTemplate.send(kafkaMessage);

		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("producer: success >>> key: {}, message: {}, offset: {}",
					loginId, result.getProducerRecord().value().toString(), result.getRecordMetadata().offset());
			} else {
				log.error("producer: failure >>> key: {}, message: {}, error: {}",
					loginId, message.toString(), ex.getMessage());
			}
		});
	}

}
