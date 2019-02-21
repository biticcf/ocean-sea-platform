/**
 *
 */
package com.biticcf.ocean.sea.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.beyonds.phoenix.mountain.core.common.lang.Logable;

/**
 * 
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午2:57:10
 *
 */
@ControllerAdvice
public abstract class BaseController implements Logable {
	protected static Logger logger = LoggerFactory.getLogger("WEB.LOG");
	
	public BaseController() {
		
	}
}
