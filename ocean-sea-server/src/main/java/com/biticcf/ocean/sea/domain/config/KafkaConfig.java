/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

/**
 * 
 * @Author: Daniel.Cao
 * @Date:   2018年12月13日
 * @Time:   下午3:05:12
 *
 */
@EnableKafka
@Configuration
@ConditionalOnExpression("${spring.kafka.enabled:false}")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {
	private final KafkaProperties properties;
	private final RecordMessageConverter messageConverter;
	
	public KafkaConfig(KafkaProperties properties,
			ObjectProvider<RecordMessageConverter> messageConverter) {
		this.properties = properties;
		this.messageConverter = messageConverter.getIfUnique();
	}
	
	/**
	 * +定义KafkaProducer
	 * @param kafkaProducerFactory kafkaProducerFactory
	 * @param kafkaProducerListener kafkaProducerListener
	 * @return kafkaTemplate
	 */
	@Bean
	@ConditionalOnExpression("${spring.kafka.producer.enabled:false}")
	public KafkaTemplate<?, ?> kafkaTemplate(
			ProducerFactory<Object, Object> kafkaProducerFactory,
			ProducerListener<Object, Object> kafkaProducerListener) {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(
				kafkaProducerFactory);
		if (this.messageConverter != null) {
			kafkaTemplate.setMessageConverter(this.messageConverter);
		}
		kafkaTemplate.setProducerListener(kafkaProducerListener);
		kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}
	
	/**
	 * kafkaProducerFactory
	 * @return kafkaProducerFactory
	 */
	@Bean
	public ProducerFactory<?, ?> kafkaProducerFactory() {
		DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
				this.properties.buildProducerProperties());
		String transactionIdPrefix = this.properties.getProducer()
				.getTransactionIdPrefix();
		if (transactionIdPrefix != null) {
			factory.setTransactionIdPrefix(transactionIdPrefix);
		}
		return factory;
	}
	
	/**
	 * kafkaConsumer
	 * @return kafkaConsumerFactory
	 */
	@Bean
	@ConditionalOnExpression("${spring.kafka.consumer.enabled:false}")
	public ConsumerFactory<?, ?> kafkaConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(
				this.properties.buildConsumerProperties());
	}
}
