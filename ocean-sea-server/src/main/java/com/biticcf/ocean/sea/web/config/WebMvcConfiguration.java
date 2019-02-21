/**
 * 
 */
package com.biticcf.ocean.sea.web.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.beyonds.phoenix.mountain.core.common.service.StringDateConverter;

/**
 * @Author: DanielCao
 * @Date:   2017年5月11日
 * @Time:   下午3:35:28
 *
 */
@Configuration
@Import({ WebMvcAutoConfiguration.class })
@ComponentScan(
        value = "com.biticcf.ocean.sea.web",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	/**
	 * 定义HttpClient
	 * @return HttpClient
	 */
    @Bean
    @ConfigurationProperties(prefix = "feign.httpclient")
    public HttpClientBuilder apacheHttpClientBuilder() {
		return HttpClientBuilder.create();
	}
    
    /**
     * 自定义输入的日期格式
     * 覆盖spring.mvc.date-format
     * @return 日期格式转换器
     */
    @Bean
    public StringDateConverter dateConverter() {
    	return new StringDateConverter();
    }
    
    /**
     * 支持fastjson的HttpMessageConverter
     * @return HttpMessageConverters
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        
        return new HttpMessageConverters(converter);
    }
}
