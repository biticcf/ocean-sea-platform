/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.beyonds.phoenix.mountain.core.common.service.WdServiceTemplate;
import com.beyonds.phoenix.mountain.core.common.service.WdServiceTemplateImpl;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;

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
	 * +定义服务模板
	 * @param transactionTemplate 事务模板
	 * @return 服务模板
	 */
	@Bean(name = "wdServiceTemplate")
	public WdServiceTemplate wdServiceTemplate(
			@Qualifier("transactionTemplate") TransactionTemplate transactionTemplate) {
		return new WdServiceTemplateImpl(transactionTemplate);
	}
	
	/**
	 * +定义主数据源
	 * @return 主数据源
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "datasource.master")
	public DataSource dataSource() {
	    return DataSourceBuilder.create().type(datasourceType).build();
	}
	
	/**
	 * +定义事务管理器
	 * @param dataSource 数据源
	 * @return 事务管理器
	 */
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
    }
	
	/**
	 * +定义事务模板
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
	 * +配置主MyBatis SqlSession
	 * @param dataSource 主dataSource
	 * @param properties properties
	 * @param resourceLoader resourceLoader
	 * @param configurationCustomizersProvider configurationCustomizersProvider
	 * @param interceptorsProvider interceptorsProvider
	 * @param databaseIdProvider databaseIdProvider
	 * @return SqlSessionFactory
	 * @throws Exception Exception
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(
			@Qualifier("dataSource") DataSource dataSource,
			@Qualifier("mybatisProperties") MybatisProperties properties,
			ResourceLoader resourceLoader,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			@Qualifier("pageInterceptor") Interceptor pageInterceptor,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			@Qualifier("manualManagedTransactionFactory") ManualManagedTransactionFactory manualManagedTransactionFactory) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		
		if (manualManagedTransactionFactory != null) {
			factory.setTransactionFactory(manualManagedTransactionFactory);
		}
        
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
          factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        }
        org.apache.ibatis.session.Configuration configuration = properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
          configuration = new org.apache.ibatis.session.Configuration();
        }
        List<ConfigurationCustomizer> configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        if (configuration != null && !CollectionUtils.isEmpty(configurationCustomizers)) {
          for (ConfigurationCustomizer customizer : configurationCustomizers) {
            customizer.customize(configuration);
          }
        }
        factory.setConfiguration(configuration);
        if (properties.getConfigurationProperties() != null) {
          factory.setConfigurationProperties(properties.getConfigurationProperties());
        }
        Interceptor[] interceptors = filterInterceptors(interceptorsProvider.getIfAvailable(), pageInterceptor);
        if (!ObjectUtils.isEmpty(interceptors)) {
          factory.setPlugins(interceptors);
        }
        DatabaseIdProvider _databaseIdProvider = databaseIdProvider.getIfAvailable();
        if (_databaseIdProvider != null) {
          factory.setDatabaseIdProvider(_databaseIdProvider);
        }
        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
          factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
          factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
          factory.setMapperLocations(properties.resolveMapperLocations());
        }
        
        return factory.getObject();
	}
	
	/**
	 * +自定义事务管理工厂,用以管理事务的生命周期
	 * @return TransactionFactory
	 */
	@Bean(name = "manualManagedTransactionFactory")
	public ManualManagedTransactionFactory manualManagedTransactionFactory() {
		return new ManualManagedTransactionFactory();
	}
	
	/**
	 * +过滤PageInterceptor
	 * @param interceptors 环境上下文中的Interceptor
	 * @param pageInterceptor 分页的Interceptor
	 * @return 过滤其他PageInterceptor之后的interceptors
	 */
	private Interceptor[] filterInterceptors(Interceptor[] interceptors, Interceptor pageInterceptor) throws Exception {
		List<Interceptor> otherInterceptors = new ArrayList<>();
        if (!ObjectUtils.isEmpty(interceptors)) {
        	for (Interceptor interceptor : interceptors) {
        		if (interceptor instanceof PageInterceptor) {
        			continue;
        		}
        		otherInterceptors.add(interceptor);
        	}
        }
        otherInterceptors.add(pageInterceptor);
        
        if (!otherInterceptors.isEmpty()) {
        	return otherInterceptors.toArray(new Interceptor[otherInterceptors.size()]);
        }
        
        return null;
	}
	
	/**
	 * +主Mybatis属性配置
	 * @return MybatisProperties
	 */
	@Primary
	@Bean(name = "mybatisProperties")
	@ConfigurationProperties(prefix = "mybatis.master")
	public MybatisProperties myBatisProperties() {
		return new MybatisProperties();
	}
	
	/**
	 * pageHelper属性配置
	 * @return Properties
	 */
	@Bean(name = "pageProperties")
	@ConfigurationProperties(prefix = "mybatis.master.pagehelper")
	public Properties pageProperties() {
		return new Properties();
	}
	
	@Bean(name = "pageHelperProperties")
	@ConfigurationProperties(prefix = "mybatis.master.pagehelper")
	public PageHelperProperties pageHelperProperties() {
		return new PageHelperProperties();
	}
	
	/**
	 * page插件配置
	 * @param pageProperties pageHelper属性
	 * @param extProperties pageHelper附加属性(closeConn问题)
	 * @return Interceptor
	 */
	@Bean(name = "pageInterceptor")
	public Interceptor pageInterceptor(@Qualifier("pageProperties") Properties pageProperties, 
			@Qualifier("pageHelperProperties") PageHelperProperties extProperties) {
		PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        //先把一般方式配置的属性放进去
        properties.putAll(pageProperties);
        //在把特殊配置放进去，由于close-conn 利用上面方式时，属性名就是 close-conn 而不是 closeConn，所以需要额外的一步
        if (extProperties != null) {
        	properties.putAll(extProperties.getProperties());
        }
        interceptor.setProperties(properties);
        
        return interceptor;
	}
}
