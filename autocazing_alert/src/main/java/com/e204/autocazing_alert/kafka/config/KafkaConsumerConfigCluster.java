package com.e204.autocazing_alert.kafka.config;

import com.e204.autocazing_alert.kafka.entity.ConsumerEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import com.e204.autocazing_alert.kafka.entity.IngredientWarnEntity;

@Configuration
public class KafkaConsumerConfigCluster {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, ConsumerEntity> pushEntityConsumerFactory() {
		JsonDeserializer<ConsumerEntity> deserializer = gcmPushEntityJsonDeserializer();
		return new DefaultKafkaConsumerFactory<>(
			consumerFactoryConfig(deserializer),
			new StringDeserializer(),
			deserializer);
	}

	private Map<String, Object> consumerFactoryConfig(JsonDeserializer<ConsumerEntity> deserializer) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return props;
	}

	private JsonDeserializer<ConsumerEntity> gcmPushEntityJsonDeserializer() {
		JsonDeserializer<ConsumerEntity> deserializer = new JsonDeserializer<>(ConsumerEntity.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);
		return deserializer;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ConsumerEntity>
	kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ConsumerEntity> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(pushEntityConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}

}