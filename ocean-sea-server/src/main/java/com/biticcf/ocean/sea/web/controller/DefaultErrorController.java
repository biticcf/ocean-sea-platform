/**
 * 
 */
package com.biticcf.ocean.sea.web.controller;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年1月5日
 * @Time:   下午2:36:56
 *
 */
@ApiIgnore
@RestController
public class DefaultErrorController {
	/**
	 * +HttpStatusCode错误处理
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws HttpStatusCodeException HttpStatusCodeException
	 */
	@RequestMapping(value = {"/error"}, method = {RequestMethod.GET})
    public void error(HttpServletRequest request, HttpServletResponse response) throws HttpStatusCodeException {
		Object obj = request.getAttribute("javax.servlet.error.request_uri");
		String errorUrl = obj == null? "" : obj.toString();
		Object errorCodeObj = request.getAttribute("javax.servlet.error.status_code");
		int errorCode = -1;
		if (errorCodeObj != null) {
			try {
				errorCode = Integer.parseInt(errorCodeObj.toString());
			} catch (Exception e) {
			}
		}
		
		HttpStatus httpStatus = HttpStatus.valueOf(errorCode);
		HttpStatusCodeException httpException = null;
		
		if (errorCode >= 400 && errorCode < 500) {
			httpException = HttpClientErrorException.create(httpStatus, errorUrl, null, null, Charset.forName("utf8"));
		} else if (errorCode >= 500 && errorCode < 600) {
			httpException = HttpServerErrorException.create(httpStatus, errorUrl, null, null, Charset.forName("utf8"));
		} else {
			httpException = new HttpClientErrorException(httpStatus, errorUrl, null, null, Charset.forName("utf8"));
		}
		
        throw httpException;
    }
}
