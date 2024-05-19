package com.e204.autocazing_alert.kafka.config;

import com.e204.autocazing_alert.kafka.entity.ConsumerEntity;
import com.e204.autocazing_alert.kafka.entity.IngredientWarnEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfigCluster {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConsumerFactory<String, ConsumerEntity> pushEntityConsumerFactory() {
		JsonDeserializer<ConsumerEntity> deserializer = new JsonDeserializer<>(ConsumerEntity.class);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ConsumerEntity> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ConsumerEntity> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(pushEntityConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}

	@Bean
	public ConsumerFactory<String, IngredientWarnEntity> ingredientWarnEntityConsumerFactory() {
		JsonDeserializer<IngredientWarnEntity> deserializer = new JsonDeserializer<>(IngredientWarnEntity.class);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, IngredientWarnEntity> ingredientWarnEntityKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, IngredientWarnEntity> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(ingredientWarnEntityConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}
}
