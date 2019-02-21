/**
 * 
 */
package com.biticcf.ocean.sea.domain.config;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: DanielCao
 * @Date:   2017年6月1日
 * @Time:   下午1:41:53
 *
 */
@Configuration
@EnableCaching //启用缓存，这个注解很重要
public class RedisCacheConfig extends CachingConfigurerSupport {
	protected static Logger logger = LoggerFactory.getLogger("CACHE.LOG");
	
	/**
	 * 缓存管理器
	 * @param connectionFactory 缓存链接工厂
	 * @return 缓存管理器
	 */
	@Bean(name = "cacheManager")
	public CacheManager cacheManager(JedisConnectionFactory connectionFactory) {
		//初始化一个RedisCacheWriter
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
		
		//设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,与redisTemplate保持一致
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		//Long类型不可以会出现异常信息;
		RedisSerializer<String> redisSerializer = new StringRedisSerializer(Charset.forName("UTF8"));
		SerializationPair<String> serializationPair = SerializationPair.fromSerializer(redisSerializer);
		cacheConfig.serializeKeysWith(serializationPair);
		cacheConfig.serializeValuesWith(serializationPair);
		
		//设置默认超过期时间是不超时
		cacheConfig.entryTtl(Duration.ZERO);
		//初始化RedisCacheManager
		RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, cacheConfig);
		
		return cacheManager;
	}
	
	/**
	 * 定义redis模板
	 * @param connectionFactory redis链接工厂
	 * @return redis模板
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		
		redisTemplate.setConnectionFactory(connectionFactory);
		
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        //或者JdkSerializationRedisSerializer序列化方式;
		//Long类型不可以会出现异常信息;
        RedisSerializer<String> redisSerializer = new StringRedisSerializer(Charset.forName("UTF8"));
        
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        
        return redisTemplate;
    }
	
	/**
	 * 定义连接工厂
	 * @param poolConfig 连接池
	 * @return 连接工厂
	 */
	@Bean(name = "connectionFactory", destroyMethod = "destroy")
	@ConfigurationProperties(prefix = "redis")
	public JedisConnectionFactory connectionFactory(JedisPoolConfig poolConfig) {
		return new JedisConnectionFactory(poolConfig);
	}
	
	/**
	 * jedis连接池参数详情,可以在配置文件通过jedis.pool.xxx=xxxx覆盖这些默认值
	 * @return 连接池
	 */
	@Bean(name = "poolConfig")
	@ConfigurationProperties(prefix = "redis.pool")
	public JedisPoolConfig poolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		
		/**
		 * 以下设置是默认值
		 */
		//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		poolConfig.setBlockWhenExhausted(true);
		//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
		poolConfig.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
		//是否启用pool的jmx管理功能, 默认true
		poolConfig.setJmxEnabled(true);
		//MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,
		//name=" + "pool" + i); 默 认为"pool"
		poolConfig.setJmxNamePrefix("pool");
		//是否启用后进先出, 默认true
		poolConfig.setLifo(true);
		//最大空闲连接数, 默认8个
		poolConfig.setMaxIdle(8);
		//最大连接数, 默认8个
		poolConfig.setMaxTotal(8);
		//最小空闲连接数, 默认0
		poolConfig.setMinIdle(0);
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),
		//如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		poolConfig.setMaxWaitMillis(-1);
		
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		poolConfig.setMinEvictableIdleTimeMillis(60000);
		//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		poolConfig.setNumTestsPerEvictionRun(-1);
		//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,
		//不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		
		//在创建连接的时候检查有效性, 默认false
		poolConfig.setTestOnCreate(false);
		//在获取连接的时候检查有效性, 默认false
		poolConfig.setTestOnBorrow(false);
		//在归还连接的时候检查有效性, 默认false
		poolConfig.setTestOnReturn(false);
		//在空闲时检查有效性, 默认false
		poolConfig.setTestWhileIdle(true);
		
		return poolConfig;
	}
	
	/**
	 * 自定义key生成策略:keyGenerator
	 * 主要用于在标签 @Cacheable、@CacheEvict中使用，可以不指定keyGenerator
	 * 
	 */
	@Bean(name = "keyGenerator")
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
			    StringBuilder sb = new StringBuilder();
                
				sb.append(target.getClass().getName());
				sb.append("_");
                sb.append(method.getName());
                
                if (params != null && params.length > 0) {
                	for (int i = 0; i < params.length; i++) {
                		String  objStr = params[i] == null ? "null" : params[i].toString().trim();
                		if ("".equals(objStr)) {
                			objStr = "null";
                		}
                	    sb.append("_").append(objStr);
                    }
                }
                
                return sb.toString();
            }
		};
	}
	
	@Bean(name = "errorHandler")
	@Override
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				logger.error("Get[" + key + "]Error!", exception);
				
				throw exception;
			}

			@Override
			public void handleCachePutError(
					RuntimeException exception, 
					Cache cache, 
					Object key, 
					Object value) {
				logger.error("Put[" + key + "," + value + "]Error!", exception);
				
				throw exception;
			}

			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
				logger.error("Evict[" + key + "]Error!", exception);
				
				throw exception;
			}

			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {
				logger.error("Clear[" + cache.getName() + "]Error!", exception);
				
				throw exception;
			}
		};
	}
}
