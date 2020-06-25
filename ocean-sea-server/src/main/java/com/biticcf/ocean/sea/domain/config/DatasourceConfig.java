/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import java.util.List;
import java.util.Properties;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

import com.alibaba.druid.wall.WallFilter;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.biticcf.mountain.core.common.transaction.ManualManagedTransactionFactory;
import com.github.biticcf.mountain.core.common.transaction.TransactionAutoConfig;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;

/**
 * @Author: DanielCao
 * @Date:   2017年5月3日
 * @Time:   下午11:04:35
 * 配置自定义数据库连接池和自定义事务模板
 * 单数据源配置
 */
@Configuration(proxyBeanMethods = false)
@MapperScan(basePackages = {"${mybatis.type-dao-package:com.biticcf.ocean.sea.domain.dao}"}, 
            sqlSessionFactoryRef = "sqlSessionFactory")  
@AutoConfigureOrder(-100)
@AutoConfigureBefore({TransactionAutoConfig.class})
@AutoConfigureAfter({MybatisAutoConfiguration.class})
public class DatasourceConfig extends AbstractDatasourceConfig {
	protected static Log logger = LogFactory.getLog("DAO.LOG");
	
	/**
	 * + 数据库配置定义
	 * @return DataSourceProperties
	 */
	@Bean("dataSourcePropertiesMaster")
	@ConfigurationProperties(prefix = "datasource.master")
	@Primary
	public DataSourceProperties getDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	/**
	 * +定义主数据源
	 * @return 主数据源
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	@Primary
	public DataSource dataSource(@Nullable XADataSourceWrapper wrapper, 
			@Qualifier("dataSourcePropertiesMaster") DataSourceProperties properties) throws Exception {
		CommonDataSource commonDataSource = createDataSource(properties);
		if (XADataSource.class.isAssignableFrom(commonDataSource.getClass())) {
			return wrapper.wrapDataSource((XADataSource) commonDataSource);
		}
		
		if (DataSource.class.isAssignableFrom(commonDataSource.getClass())) {
			return (DataSource) commonDataSource;
		}
		
		throw new Exception("Init DataSource Error!");
	}
	
	/**
	 * +配置主MyBatis SqlSession
	 * @param dataSource 主dataSource
	 * @param properties properties
	 * @param resourceLoader resourceLoader
	 * @param configurationCustomizersProvider configurationCustomizersProvider
	 * @param interceptorsProvider interceptorsProvider
	 * @param pageInterceptor pageInterceptor
	 * @param databaseIdProvider databaseIdProvider
	 * @param manualManagedTransactionFactory manualManagedTransactionFactory
	 * @param languageDriversProvider languageDriversProvider
     * @param applicationContext applicationContext
     * @param paginationInterceptor paginationInterceptor
     * 
	 * @return SqlSessionFactory
	 * @throws Exception Exception
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(
			@Qualifier("dataSource") DataSource dataSource,
			@Qualifier("myBatisPlusProperties") MybatisPlusProperties properties,
			ResourceLoader resourceLoader,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			@Qualifier("pageInterceptor") Interceptor pageInterceptor,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			@Qualifier("manualManagedTransactionFactory") @Nullable ManualManagedTransactionFactory manualManagedTransactionFactory, 
			ObjectProvider<LanguageDriver[]> languageDriversProvider,
            ApplicationContext applicationContext,
            @Qualifier("paginationInterceptor") Interceptor paginationInterceptor) throws Exception {
		return super.sqlSessionFactory(dataSource, properties, resourceLoader, 
        		configurationCustomizersProvider, interceptorsProvider, pageInterceptor, 
        		databaseIdProvider, manualManagedTransactionFactory, languageDriversProvider, 
        		applicationContext, paginationInterceptor);
	}
	
	/**
	 * +自定义事务管理工厂,用以管理事务的生命周期
	 * @return TransactionFactory
	 */
	@ConditionalOnExpression("${spring.transaction.with-strict-flag:false}")
	@Bean(name = "manualManagedTransactionFactory")
	public ManualManagedTransactionFactory manualManagedTransactionFactory() {
		return new ManualManagedTransactionFactory();
	}
	
	/**
	 * +主Mybatis+MybatisPlus属性配置
     * @return MybatisPlusProperties
	 */
	@Primary
	@Bean(name = "myBatisPlusProperties")
	@ConfigurationProperties(prefix = "mybatis.master")
	public MybatisPlusProperties myBatisPlusProperties() {
        return new MybatisPlusProperties();
	}
	
	/**
	 * pageHelper属性配置
	 * @return Properties
	 */
	@Bean(name = "pageHelperProperties")
	@ConfigurationProperties(prefix = "mybatis.master.pagehelper")
	public PageHelperProperties pageHelperProperties() {
		return new PageHelperProperties();
	}
	
	/**
	 * page插件配置
	 * @param extProperties pageHelper附加属性(closeConn问题)
	 * @return Interceptor
	 */
	@Bean(name = "pageInterceptor")
	public Interceptor pageInterceptor(@Qualifier("pageHelperProperties") PageHelperProperties extProperties) {
		PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        if (extProperties != null) {
        	properties.putAll(extProperties.getProperties());
        }
        interceptor.setProperties(properties);
        
        return interceptor;
	}
	
	/**
     * +Druid防火墙过滤器
     * @return 自定义的WallFilter
     */
    @Bean(name = "wallFilter")
    @ConfigurationProperties(prefix = "druid.filters.wall")
    public WallFilter wallFilter() {
        return new WallFilter();
    }
    
    /**
     * +Mybatis Plus 拦截器
     * @return 自定义的PaginationInterceptor
     */
    @Bean(name = "paginationInterceptor")
    public Interceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
