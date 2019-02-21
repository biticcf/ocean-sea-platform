/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.beyonds.phoenix.mountain.core.common.service.ReferContext;
import com.biticcf.ocean.sea.domain.repository.DemoDomainRepository;

/**
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午6:27:27
 *
 */
@Service("constantContext")
public class ConstantContext implements ReferContext {
	@Autowired
	private DemoDomainRepository demoDomainRepository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ElasticsearchTemplate esTemplate;
	@Autowired
	private CommonAsyncService commonAsyncService;
	@Autowired
	private EffectAsyncService effectAsyncService;
	
	public DemoDomainRepository getDemoDomainRepository() {
		return demoDomainRepository;
	}
	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}
	
	public ElasticsearchTemplate getEsTemplate() {
		return esTemplate;
	}

	public CommonAsyncService getCommonAsyncService() {
		return commonAsyncService;
	}

	public EffectAsyncService getEffectAsyncService() {
		return effectAsyncService;
	}
}
