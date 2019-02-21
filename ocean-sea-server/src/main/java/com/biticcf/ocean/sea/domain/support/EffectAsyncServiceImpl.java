/**
 * 
 */
package com.biticcf.ocean.sea.domain.support;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.beyonds.phoenix.mountain.core.common.lang.Logable;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年1月24日
 * @Time:   下午2:59:10
 * +使用高效线程池(asyncEffective)
 * +该类内的方法都是异步方法
 */
@Service("effectAsyncService")
@Async("asyncEffective")
public class EffectAsyncServiceImpl implements EffectAsyncService, Logable {
	
	@Override
	public void asyncEffectQuery() {
		try {
			Thread.sleep(3L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("OK[EffectAsyncService.asyncEffectQuery]~");
	}

}
