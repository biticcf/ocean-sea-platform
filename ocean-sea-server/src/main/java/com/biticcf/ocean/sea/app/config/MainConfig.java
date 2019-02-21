/**
 * 
 */
package com.biticcf.ocean.sea.app.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;

import com.beyonds.phoenix.mountain.core.common.framework.PerformFramework;
import com.biticcf.ocean.sea.domain.config.DatasourceConfig;
import com.biticcf.ocean.sea.domain.config.KafkaConfig;
import com.biticcf.ocean.sea.domain.config.RedisCacheConfig;
import com.biticcf.ocean.sea.web.config.Swagger2Configuration;
import com.biticcf.ocean.sea.web.config.WebMvcConfiguration;

/**
 * @Author: DanielCao
 * @Date:   2017年5月1日
 * @Time:   下午8:26:31
 *
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@Import(value = {
		PerformFramework.class, 
		DatasourceConfig.class,
		RedisCacheConfig.class,
		KafkaConfig.class,
		WebMvcConfiguration.class,
		Swagger2Configuration.class
		})
@ComponentScan(
        value = {"com.biticcf.ocean.sea"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
public class MainConfig {
	
}
