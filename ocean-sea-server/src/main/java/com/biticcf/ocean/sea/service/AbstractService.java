/**
 * 
 */
package com.biticcf.ocean.sea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.biticcf.mountain.core.common.util.LogModel;

/**
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午2:46:00
 *
 */
public class AbstractService {
	protected static Logger logger = LoggerFactory.getLogger("SERVICE.LOG");
	
	/**
	 * info日志
	 * @param lm 日志收集器
	 */
	protected void writeLog(LogModel lm) {
		if (logger.isInfoEnabled()) {
			logger.info(lm.toJson());
		}
	}

	/**
	 * info日志
	 * @param lm 日志收集器
	 * @param isClear 是否清除缓存日志,true清除,false不清除
	 */
	protected void writeLog(LogModel lm, boolean isClear) {
		if (logger.isInfoEnabled()) {
			logger.info(lm.toJson(isClear));
		}
	}

	/**
	 * warn日志
	 * @param message 日志信息
	 */
	protected void writeLog(String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(message);
		}
	}

	/**
	 * error日志
	 * @param lm 日志收集器
	 * @param e 异常信息
	 */
	protected void writeErrorLog(LogModel lm, Throwable e) {
		if (logger.isErrorEnabled()) {
			logger.error(lm.toJson(false), e);
		}
	}

	/**
	 * info日志
	 * @param logger 日志工具
	 * @param lm 日志收集器
	 */
	protected void writeLog(Logger logger, LogModel lm) {
		if (logger.isInfoEnabled()) {
			logger.info(lm.toJson());
		}
	}

	/**
	 * info日志
	 * @param logger 日志工具
	 * @param lm 日志收集器
	 * @param isClear 是否清除缓存日志,true清除,false不清除
	 */
	protected void writeLog(Logger logger, LogModel lm, boolean isClear) {
		if (logger.isInfoEnabled()) {
			logger.info(lm.toJson(isClear));
		}
	}

	/**
	 * warn日志
	 * @param logger 日志工具
	 * @param message 日志信息
	 */
	protected void writeLog(Logger logger, String message) {
		if (logger.isWarnEnabled()) {
			logger.warn(message);
		}
	}

	/**
	 * error日志
	 * @param logger 日志工具
	 * @param lm 日志收集器
	 * @param e 异常信息
	 */
	protected void writeErrorLog(Logger logger, LogModel lm, Throwable e) {
		if (logger.isErrorEnabled()) {
			logger.error(lm.toJson(false), e);
		}
	}
}
