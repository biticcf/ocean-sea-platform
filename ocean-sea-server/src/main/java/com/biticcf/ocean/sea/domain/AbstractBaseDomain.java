/**
 * 
 */
package com.biticcf.ocean.sea.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biticcf.ocean.sea.domain.support.ConstantContext;
import com.github.biticcf.mountain.core.common.service.WdServiceCallback;

/**
 * @Author: DanielCao
 * @Date:   2017年5月9日
 * @Time:   下午5:00:52
 *
 * @param <T> 数据类型
 */
public abstract class AbstractBaseDomain<T> implements WdServiceCallback<T> {
	protected static Log logger = LogFactory.getLog("COMMON.LOG");
	
	private ConstantContext constantContext;
	
	public AbstractBaseDomain(ConstantContext constantContext) {
		this.constantContext = constantContext;
	}
	
	public ConstantContext getConstantContext() {
		return constantContext;
	}
	
	/**
	 * info日志输出
	 * @param message 日志信息
	 */
	protected void info(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	/**
	 * warn日志输出
	 * @param message 日志信息
	 */
	protected void warn(String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(message);
		}
	}

	/**
	 * error日志输出
	 * @param message 日志信息
	 */
	protected void error(String message) {
		if (logger.isErrorEnabled()) {
			logger.error(message);
		}
	}
	
	/**
	 * error日志输出
	 * @param message 日志信息
	 * @param th 异常信息
	 */
	protected void error(String message, Throwable th) {
		if (logger.isErrorEnabled()) {
			logger.error(message, th);
		}
	}

}
