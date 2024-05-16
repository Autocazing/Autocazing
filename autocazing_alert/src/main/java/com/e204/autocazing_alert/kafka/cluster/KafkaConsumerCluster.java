package com.e204.autocazing_alert.kafka.cluster;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.KafkaHeaders;

import com.e204.autocazing_alert.kafka.entity.DeliveryRefreshEntity;
import com.e204.autocazing_alert.kafka.entity.IngredientWarnEntity;
import com.e204.autocazing_alert.kafka.entity.SalesRefreshEntity;

@Slf4j
@Component
public class KafkaConsumerCluster {

	@KafkaListener(topics = "test", groupId = "${spring.kafka.consumer.group-id}")
	public void ingredient_warn(@Payload IngredientWarnEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {

		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
	}

	@KafkaListener(topics = "test", groupId = "${spring.kafka.consumer.group-id}")
	public void sales_refresh(@Payload SalesRefreshEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {

		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
	}

	@KafkaListener(topics = "test", groupId = "${spring.kafka.consumer.group-id}")
	public void sales_refresh(@Payload DeliveryRefreshEntity message,
		@Headers MessageHeaders messageHeaders,
		@Header(KafkaHeaders.RECEIVED_KEY) String key) {

		log.info("consumer: success >>> key: {}, message: {}, headers: {}", key, message.toString(), messageHeaders);
	}
}
