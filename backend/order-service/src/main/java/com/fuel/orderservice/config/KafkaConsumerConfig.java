package com.fuel.orderservice.config;

import java.util.HashMap;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fuel.orderservice.dto.ScheduleDTO;

@Configuration
class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		// same can be put on the properties file
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return props;
	}

	@Bean
	public ConsumerFactory<String, ScheduleDTO> orderConsumerFactory() {
		// Spring Kafka provides JsonSerializer and JsonDeserializer implementations
		// that are based on the Jackson JSON object mapper.
		// It allows us to convert any Java object to bytes[]
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(ScheduleDTO.class));
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ScheduleDTO>> orderKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ScheduleDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(orderConsumerFactory());
		return factory;
	}
}