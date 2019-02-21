/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年1月24日
 * @Time:   下午2:56:09
 * +常规异步业务服务
 * +特点是异步业务响应慢,如果是响应快的业务,可以使用EffectAsyncService
 * +使用接口的原因是使用JDK代理,与整个框架保持一致
 */
public interface CommonAsyncService {
	/**
	 * 测试异步线程业务
	 */
	void asyncCommonQuery();
}
