/**
 * 
 */
package com.biticcf.ocean.sea.domain.feign.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.biticcf.ocean.sea.domain.feign.fallback.CategoryFeignClientFallback;

import feign.Feign;
import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.hystrix.HystrixFeign;

/**
 * @Author: DanielCao
 * @Date:   2017年6月29日
 * @Time:   上午1:21:36
 *
 */
@Import(FeignAutoConfiguration.class)
@Configuration
public class FeignConfig {
	/**
	 * 定义日志级别
	 * @return 日志级别
	 */
	@Bean
    public Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
    
	/**
	 * 定义feign
	 * @return feign
	 */
	@Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return HystrixFeign.builder();
    }
    
	/**
	 * 解决post的url encoder和multipart/form-data文件上传问题
	 * @param messageConverters 消息编码器集合
	 * @return 消息编码器
	 */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
	
    /**
     * 熔断结果定义
     * @return 熔断结果
     */
    @Bean
    public CategoryFeignClientFallback fb() {
        return new CategoryFeignClientFallback();
    }

}
