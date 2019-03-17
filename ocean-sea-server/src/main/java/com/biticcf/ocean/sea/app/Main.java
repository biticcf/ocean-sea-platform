/**
 * 
 */
package com.biticcf.ocean.sea.app;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import com.beyonds.phoenix.mountain.shackle.EnableShackleTemplates;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;

/**
 * @Author: DanielCao
 * @Date:   2017年5月1日
 * @Time:   下午5:39:52
 * SpringBoot 主方法
 * @SpringBootApplication相当于
 *     @Configuration+@EnableAutoConfiguration+@ComponentScan
 * 
 */
@SpringBootApplication(scanBasePackages = {"com.biticcf.ocean.sea"}, exclude = {PageHelperAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.biticcf.ocean.sea.domain.feign"})
@EnableShackleTemplates(basePackages = {"com.biticcf.ocean.sea.service"})
@EnableEurekaClient
@EnableAsync
public class Main extends SpringBootServletInitializer {
	// war启动入口
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}
	
	/**
	 * 主程序入口(jar格式)
	 * @param args 命令行参数
	 * @throws Exception 执行异常
	 */
	public static void main(String[] args) throws Exception {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}
	
	/**
	 * 定义程序入口
	 * @param builder SpringApplicationBuilder
	 * @return SpringApplicationBuilder
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(Main.class).bannerMode(Banner.Mode.CONSOLE).logStartupInfo(true).registerShutdownHook(true).web(WebApplicationType.SERVLET);
    }
}
