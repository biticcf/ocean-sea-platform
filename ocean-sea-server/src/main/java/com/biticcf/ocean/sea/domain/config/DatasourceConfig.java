/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.beyonds.phoenix.mountain.core.common.service.WdServiceTemplate;
import com.beyonds.phoenix.mountain.core.common.service.WdServiceTemplateImpl;
import com.github.pagehelper.PageInterceptor;

/**
 * @Author: DanielCao
 * @Date:   2017年5月3日
 * @Time:   下午11:04:35
 * 配置自定义数据库连接池和自定义事务模板
 * 单数据源配置
 */
@Configuration
@MapperScan(basePackages = {"${mybatis.type-dao-package:com.biticcf.ocean.sea.domain.dao}"}, 
            sqlSessionFactoryRef = "sqlSessionFactory")  
@EnableTransactionManagement
@AutoConfigureOrder(-100)
@AutoConfigureAfter({MybatisAutoConfiguration.class})
public class DatasourceConfig {
	protected static Logger logger = LoggerFactory.getLogger("DAO.LOG");
	
	@Value("${spring.datasource.type}")
	private Class<? extends DataSource> datasourceType;
	
	/**
	 * 定义服务模板
	 * @param transactionTemplate 事务模板
	 * @return 服务模板
	 */
	@Bean(name = "wdServiceTemplate")
	public WdServiceTemplate wdServiceTemplate(
			@Qualifier("transactionTemplate") TransactionTemplate transactionTemplate) {
		return new WdServiceTemplateImpl(transactionTemplate);
	}
	
	/**
	 * 定义主数据源
	 * @return 主数据源
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "datasource.master")
	public DataSource dataSource() {
	    return DataSourceBuilder.create().type(datasourceType).build();
	}
	
	/**
	 * 定义事务管理器
	 * @param dataSource 数据源
	 * @return 事务管理器
	 */
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
    }
	
	/**
	 * 定义事务模板
	 * @param transactionManager 事务管理器
	 * @return 事务模板
	 * @throws Exception 异常
	 */
	@Bean(name = "transactionTemplate")
	public TransactionTemplate transactionTemplate(
			@Qualifier("transactionManager") DataSourceTransactionManager transactionManager) 
					throws Exception {
		DefaultTransactionDefinition defaultTransactionDefinition = 
				new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		defaultTransactionDefinition.setTimeout(60); // 秒钟
	    
	    return new TransactionTemplate(transactionManager, defaultTransactionDefinition);
	}
	
	/**
	 * +定义分页插件
	 * @param pageProperties
	 * @return Interceptor
	 */
	@Bean
	public Interceptor getInterceptor(@Qualifier("pageProperties")Properties pageProperties) {
		PageInterceptor pageInterceptor = new PageInterceptor();
		pageInterceptor.setProperties(pageProperties);
		
		return pageInterceptor;
	}
	/**
	 * +定义分页插件属性
	 * @return Properties
	 */
	@Bean("pageProperties")
	@ConfigurationProperties(prefix = "mybatis.interceptors.page-interceptor")
	public Properties pageProperties() {
		return new Properties();
	}
}
