/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.biticcf.ocean.sea.domain.config.KafkaConfig;

/**
 * @Author: Daniel.Cao
 * @Date:   2018年12月13日
 * @Time:   下午3:20:47
 *
 */
@Component
@ConditionalOnBean({KafkaConfig.class})
@ConditionalOnExpression("${spring.kafka.consumer.enabled:false}")
public class KafkaConsumer {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 接收kafka消息并且处理
	 * @param record 消息内容
	 */
    @KafkaListener(topics = {"${spring.kafka.consumer.topic:trade-order}"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());
        
        // TODO
    }
}
