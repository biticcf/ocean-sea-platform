/**
 * 
 */
package com.biticcf.ocean.sea.domain.feign.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.clientconfig.HttpClientFeignConfiguration;
import org.springframework.cloud.openfeign.support.DefaultGzipDecoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.biticcf.ocean.sea.domain.feign.fallback.CategoryFeignClientFallback;

import feign.Client;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.optionals.OptionalDecoder;

/**
 * @Author: DanielCao
 * @Date:   2017年6月29日
 * @Time:   上午1:21:36
 * +Feignclient系统配置
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({HttpClientFeignConfiguration.class})
@AutoConfigureBefore({FeignClientsConfiguration.class})
public class FeignConfig {
	/**
	 * 解决post的url encoder和multipart/form-data文件上传问题
	 * @param messageConverters 消息编码器集合
	 * @return 消息编码器
	 */
    @Bean
    @Primary
    public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
    
    /**
     * +使用Apache Httpclient实现FeignClient客户端
     * @param httpClient #HttpClientFeignConfiguration.customHttpClient()
     * @return ApacheHttpClient实例
     */
    @Bean
    @Primary
    public Client feignClient(HttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }
    
    /**
     * +用Spring定义的日志系统代理feign日志系统
     * @return 代理日志系统
     */
    @Bean
    @Primary
    public Logger logger() {
        return new FeignLog(this.getClass());
    }
    
    /**
     * +重试机制,任何情况下都不重试
     * @return 禁止重试机制
     */
    @Bean
    @Primary
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }
    
    /**
     * +定义GzipDecoder
     * @param messageConverters
     * @return DefaultGzipDecoder
     */
    @Bean
    @Primary
    @ConditionalOnProperty("feign.compression.response.useGzipDecoder")
    public Decoder responseGzipDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(new ResponseEntityDecoder(
                new DefaultGzipDecoder(new SpringDecoder(messageConverters))));
    }
	
    /**
     * 熔断结果定义
     * @return 熔断结果
     */
    @Bean
    public CategoryFeignClientFallback fb() {
        return new CategoryFeignClientFallback();
    }
    
    /**
     * Springboot的代理log
     * author： Daniel.Cao
     * date:   2020年1月7日
     * time:   下午4:07:57
     *
     */
    final class FeignLog extends Logger {
        private Log log;
        
        public FeignLog(Class<?> clazz) {
            log = LogFactory.getLog(clazz);
        }
        
        @Override
        protected void log(String configKey, String format, Object... args) {
            if (log.isDebugEnabled()) {
                log.debug(String.format(methodTag(configKey) + format, args));
            }
        }
    }

}
