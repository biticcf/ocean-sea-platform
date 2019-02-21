/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年1月24日
 * @Time:   下午2:54:02
 * +高效异步业务服务
 * +特点是异步业务响应快,如果是响应慢的业务,请使用CommonAsyncService
 * +使用接口的原因是使用JDK代理,与整个框架保持一致
 */
public interface EffectAsyncService {
	/**
	 * 测试异步线程业务
	 */
	void asyncEffectQuery();
}
