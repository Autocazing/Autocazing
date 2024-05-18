package e204.autocazing.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import e204.autocazing.kafka.entity.ProducerEntity;
import e204.autocazing.kafka.entity.IngredientWarnEntity;
import e204.autocazing.kafka.entity.SalesRefreshEntity;

@Configuration
public class KafkaProducerConfigCluster {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, IngredientWarnEntity> ingredientWarnProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, IngredientWarnEntity> ingredientWarnKafkaTemplate() {
		return new KafkaTemplate<>(ingredientWarnProducerFactory());
	}

	@Bean
	public ProducerFactory<String, SalesRefreshEntity> salesRefreshProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, SalesRefreshEntity> salesRefreshKafkaTemplate() {
		return new KafkaTemplate<>(salesRefreshProducerFactory());
	}

	@Bean
	public ProducerFactory<String, ProducerEntity> deliveryRefreshProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, ProducerEntity> deliveryRefreshKafkaTemplate() {
		return new KafkaTemplate<>(deliveryRefreshProducerFactory());
	}
}
