/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.jta.JtaProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.biticcf.mountain.core.common.transaction.ManualManagedTransactionFactory;
import com.github.pagehelper.PageInterceptor;

/**
 * author： Daniel.Cao
 * date:   2020年6月23日
 * time:   下午5:04:54
 * + 数据库配置基本类
 */
public abstract class AbstractDatasourceConfig implements BeanClassLoaderAware {
	private ClassLoader classLoader;
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * + 配置日jta志路径
	 * @param jtaProperties
	 * @return 路径地址
	 */
	protected String getLogBaseDir(JtaProperties jtaProperties) {
		if (StringUtils.hasLength(jtaProperties.getLogDir())) {
			return jtaProperties.getLogDir();
		}
		File home = new ApplicationHome().getDir();
		
		return new File(home, "transaction-logs").getAbsolutePath();
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
	public SqlSessionFactory sqlSessionFactory(
            DataSource dataSource, MybatisPlusProperties properties,
            ResourceLoader resourceLoader,
            ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
            ObjectProvider<Interceptor[]> interceptorsProvider,
            Interceptor pageInterceptor,
            ObjectProvider<DatabaseIdProvider> databaseIdProvider,
            @Nullable ManualManagedTransactionFactory manualManagedTransactionFactory,
            ObjectProvider<LanguageDriver[]> languageDriversProvider,
            ApplicationContext applicationContext,
            Interceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();

        factory.setDataSource(dataSource);
        
        // 自定义事务处理器
		if (manualManagedTransactionFactory != null) {
			factory.setTransactionFactory(manualManagedTransactionFactory);
		}
        
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
          factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        }
        com.baomidou.mybatisplus.core.MybatisConfiguration configuration = properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
          configuration = new com.baomidou.mybatisplus.core.MybatisConfiguration();
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
        Interceptor[] interceptors = filterInterceptors(interceptorsProvider.getIfAvailable(), 
                pageInterceptor, paginationInterceptor);
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
        
        // Mybatis Plus 相关配置
        LanguageDriver[] languageDrivers = languageDriversProvider.getIfAvailable();
        Class<? extends LanguageDriver> defaultLanguageDriver = properties.getDefaultScriptingLanguageDriver();
        if (!ObjectUtils.isEmpty(languageDrivers)) {
            factory.setScriptingLanguageDrivers(languageDrivers);
        }
        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);
        
        if (StringUtils.hasLength(properties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(properties.getTypeEnumsPackage());
        }
        
        // 此处必为非 NULL
        GlobalConfig globalConfig = properties.getGlobalConfig();
        if (applicationContext.getBeanNamesForType(MetaObjectHandler.class, false, false).length > 0) {
            MetaObjectHandler metaObjectHandler = applicationContext.getBean(MetaObjectHandler.class);
            globalConfig.setMetaObjectHandler(metaObjectHandler);
        }
        if (applicationContext.getBeanNamesForType(IKeyGenerator.class, false, false).length > 0) {
            IKeyGenerator keyGenerator = applicationContext.getBean(IKeyGenerator.class);
            globalConfig.getDbConfig().setKeyGenerator(keyGenerator);
        }
        if (applicationContext.getBeanNamesForType(ISqlInjector.class, false, false).length > 0) {
            ISqlInjector iSqlInjector = applicationContext.getBean(ISqlInjector.class);
            globalConfig.setSqlInjector(iSqlInjector);
        }
        factory.setGlobalConfig(globalConfig);
        
        return factory.getObject();
    }
	
    /**
     * +过滤PageInterceptor
     * @param interceptors 环境上下文中的Interceptor
     * @param pageInterceptor 分页的Interceptor
     * @param paginationInterceptor MybatisPlus的Interceptor
     *
     * @return 过滤其他PageInterceptor之后的interceptors
     */
    private Interceptor[] filterInterceptors(Interceptor[] interceptors, Interceptor pageInterceptor, 
            Interceptor paginationInterceptor) throws Exception {
        List<Interceptor> otherInterceptors = new ArrayList<>();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor instanceof PageInterceptor) {
                    continue;
                }
                otherInterceptors.add(interceptor);
            }
        }
        if (pageInterceptor != null) {
            otherInterceptors.add(pageInterceptor);
        }
        if (paginationInterceptor != null) {
            otherInterceptors.add(paginationInterceptor);
        }
        
        if (!otherInterceptors.isEmpty()) {
            return otherInterceptors.toArray(new Interceptor[otherInterceptors.size()]);
        }
        
        return null;
    }
    
    /**
     * + 根据配置文件创建数据源
     * @param properties
     * @return CommonDataSource
     */
    public CommonDataSource createDataSource(DataSourceProperties properties) {
		String className = properties.getXa().getDataSourceClassName();
		if (!StringUtils.hasLength(className)) {
			className = DatabaseDriver.fromJdbcUrl(properties.determineUrl()).getXaDataSourceClassName();
		}
		Assert.state(StringUtils.hasLength(className), "No DataSource class name specified");
		CommonDataSource dataSource = createDataSourceInstance(className);
		bindProperties(dataSource, properties);
		
		return dataSource;
	}

    /**
     * + 实例化数据源
     * @param className
     * @return CommonDataSource
     */
	private CommonDataSource createDataSourceInstance(String className) {
		try {
			Class<?> dataSourceClass = ClassUtils.forName(className, this.classLoader);
			Object instance = BeanUtils.instantiateClass(dataSourceClass);
			Assert.isInstanceOf(CommonDataSource.class, instance);
			
			return (CommonDataSource) instance;
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to create DataSource instance from '" + className + "'");
		}
	}

	/**
	 * + 属性绑定
	 * @param target
	 * @param dataSourceProperties
	 */
	private void bindProperties(CommonDataSource target, DataSourceProperties dataSourceProperties) {
		Binder binder = new Binder(getBinderSource(dataSourceProperties));
		binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(target));
	}

	/**
	 * + 初始化属性信息
	 * @param dataSourceProperties
	 * @return ConfigurationPropertySource
	 */
	private ConfigurationPropertySource getBinderSource(DataSourceProperties dataSourceProperties) {
		MapConfigurationPropertySource source = new MapConfigurationPropertySource();
		source.put("user", dataSourceProperties.determineUsername());
		source.put("password", dataSourceProperties.determinePassword());
		source.put("url", dataSourceProperties.determineUrl());
		source.putAll(dataSourceProperties.getXa().getProperties());
		ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
		aliases.addAliases("user", "username");
		
		return source.withAliases(aliases);
	}
}
