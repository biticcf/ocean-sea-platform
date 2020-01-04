/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.biticcf.ocean.sea.domain.config.KafkaConfig;

/**
 * @Author: Daniel.Cao
 * @Date:   2018年12月13日
 * @Time:   下午8:17:47
 *
 */
@Component
@ConditionalOnBean({KafkaConfig.class})
@ConditionalOnExpression("${spring.kafka.producer.enabled:false}")
public class KafkaProducer {
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${spring.kafka.producer.topic:trade-order}")
	private String topic;
	
	/**
	 * 发送kafka消息
	 * @param objMsg 原内容
	 */
	public void sendMessage(Object objMsg) {
        String jsonString = JSON.toJSONString(objMsg);
        logger.info("Message[" + jsonString + "].");
        
        kafkaTemplate.send(topic, jsonString);
    }
}
