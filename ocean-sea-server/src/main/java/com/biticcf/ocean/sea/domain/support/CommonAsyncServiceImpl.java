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
 * +使用常规线程池(asyncCommon)
 * +该类内的方法都是异步方法
 */
@Service("commonAsyncService")
@Async("asyncCommon")
public class CommonAsyncServiceImpl implements CommonAsyncService, Logable {
	
	@Override
	public void asyncCommonQuery() {
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("OK[CommonAsyncService.asyncCommonQuery]~");
	}

}
