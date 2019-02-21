/**
 * 
 */
package com.biticcf.ocean.sea.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSON;

/**
 * @Author: DanielCao
 * @Date:   2017年5月15日
 * @Time:   上午8:28:34
 *
 */
public class TestDemoController extends AbstTestController {
	private HttpHeaders headers;
	
	/**
	 * 初始化
	 */
	@Before
	public void before() {
		headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
	}
	
	/**
	 * 测试根据id查询接口
	 */
	@Test
	public void testDemoQueryById() {
		boolean testFlag = true;
		if (!testFlag) {
			return;
		}
		
		Object param = new Object();
		HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(param), headers);
		Map<String, String> params = new HashMap<String, String>();
		params.put("a", "aa");
		params.put("id", "1");
		params.put("t", "1");
		
		String uri = "/ocean/sea/v1/demo/{id}?a={a}&t={t}";
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, formEntity, String.class, params);
		
		logger.info(response.getBody());
	}
}
