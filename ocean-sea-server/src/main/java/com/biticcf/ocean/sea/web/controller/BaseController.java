/**
 *
 */
package com.biticcf.ocean.sea.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.github.biticcf.mountain.core.common.lang.Logable;

/**
 * 
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午2:57:10
 *
 */
@ControllerAdvice
public abstract class BaseController implements Logable {
	protected static Log logger = LogFactory.getLog("WEB.LOG");
	
	public BaseController() {
		
	}
}
