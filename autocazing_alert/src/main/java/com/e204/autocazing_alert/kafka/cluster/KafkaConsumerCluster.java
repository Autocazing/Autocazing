package com.e204.autocazing_alert.kafka.cluster;

import com.e204.autocazing_alert.alert.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.e204.autocazing_alert.kafka.entity.DeliveryRefreshEntity;
import com.e204.autocazing_alert.kafka.entity.IngredientWarnEntity;
import com.e204.autocazing_alert.kafka.entity.SalesRefreshEntity;

@Slf4j
@Component
public class KafkaConsumerCluster {

	private SseService sseService;

	@KafkaListener(topics = "ingredient_warn", groupId = "${spring.kafka.consumer.group-id}")
	public void ingredientWarn(@Payload IngredientWarnEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {
		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
		sseService.sendRestockNotification(key, message);	// 받은 kafka 메시지 클라이언트에 전달
	}

	@KafkaListener(topics = "sales_refresh", groupId = "${spring.kafka.consumer.group-id}")
	public void salesRefresh(@Payload SalesRefreshEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {

		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
	}

	@KafkaListener(topics = "delivery_refresh", groupId = "${spring.kafka.consumer.group-id}")
	public void deliveryRefresh(@Payload DeliveryRefreshEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {

		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
	}
}