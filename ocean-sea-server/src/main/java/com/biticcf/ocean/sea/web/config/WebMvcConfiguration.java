/**
 * 
 */
package com.biticcf.ocean.sea.web.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.biticcf.mountain.core.common.service.StringDateConverter;
import com.github.biticcf.mountain.core.common.service.StringDecoderForHeaderConverter;

/**
 * @Author: DanielCao
 * @Date:   2017年5月11日
 * @Time:   下午3:35:28
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ HttpProperties.class })
@Import({ WebMvcAutoConfiguration.class })
@ComponentScan(
        value = "com.biticcf.ocean.sea.web",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)
        })
public class WebMvcConfiguration implements WebMvcConfigurer {
    
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
     * +对于header中的中文字进行解码
     * @return 转换结果
     */
    @Bean
    public StringDecoderForHeaderConverter stringHeaderConverter(HttpProperties httpProperties) {
    	return new StringDecoderForHeaderConverter(httpProperties.getEncoding().getCharset());
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
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setSerializerFeatures(
        		SerializerFeature.WriteMapNullValue, 
        		SerializerFeature.WriteDateUseDateFormat, 
        		SerializerFeature.DisableCircularReferenceDetect);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        
        // 默认的StringHttpMessageConverter是iso-8859-1编码,需要转化为utf-8编码
        // 它用来解析text/plain和其他未定义类型的消息
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        
        return new HttpMessageConverters(fastJsonHttpMessageConverter, stringHttpMessageConverter);
    }
    
    /**
     * +配置druid控制台
     * @return druid信息
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        //用户名
        initParameters.put("loginUsername", "test");
        //密码
        initParameters.put("loginPassword", "test");
        // 禁用HTML页面上的“Reset All”功能
        initParameters.put("resetEnable", "false");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        initParameters.put("allow", "");
        // IP黑名单 (存在共同时，deny优先于allow)
        initParameters.put("deny", "");
        servletRegistrationBean.setInitParameters(initParameters);
        
        return servletRegistrationBean;
    }
    
    /**
     * +配置参数
     * @return 参数信息
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        
        return filterRegistrationBean;
    }
    
    // +系统默认静态文件路径,优先级比config文件高
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/**")
    	        .addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/");
    }
}
