package e204.autocazing.kafka.config;

import e204.autocazing.kafka.entity.solution.IngredientCreateEntity;
import e204.autocazing.kafka.entity.solution.MenuCreateEntity;
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
import e204.autocazing.kafka.entity.alert.IngredientWarnEntity;

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

	// to alert service
	@Bean
	public ProducerFactory<String, IngredientWarnEntity> ingredientWarnProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, IngredientWarnEntity> ingredientWarnKafkaTemplate() {
		return new KafkaTemplate<>(ingredientWarnProducerFactory());
	}

	@Bean
	public ProducerFactory<String, ProducerEntity> producerRefreshProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, ProducerEntity> producerRefreshKafkaTemplate() {
		return new KafkaTemplate<>(producerRefreshProducerFactory());
	}

	// to solutino service
	@Bean
	public ProducerFactory<String, IngredientCreateEntity> ingredientCreateProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, IngredientCreateEntity> ingredientCreateKafkaTemplate() {
		return new KafkaTemplate<>(ingredientCreateProducerFactory());
	}

	@Bean
	public ProducerFactory<String, MenuCreateEntity> menuCreateProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, MenuCreateEntity> menuCreateKafkaTemplate() {
		return new KafkaTemplate<>(menuCreateProducerFactory());
	}
}
