/**
 * 
 */
package com.biticcf.ocean.sea.app.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;

/**
 * @Author: DanielCao
 * @Date:   2017年5月1日
 * @Time:   下午8:26:31
 *
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@ComponentScan(
        value = {"com.biticcf.ocean.sea"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
public class MainConfig {
	
}
