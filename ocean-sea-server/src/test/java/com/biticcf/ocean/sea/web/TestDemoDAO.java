/**
 * 
 */
package com.biticcf.ocean.sea.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.beyonds.phoenix.mountain.core.common.util.PaginationSupport;
import com.biticcf.ocean.sea.model.DemoModel;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年3月16日
 * @Time:   下午9:54:04
 *
 */
public class TestDemoDAO extends AbstTestController {
	/**
	 * +测试根据id查询接口
	 */
	@Test
	public void testDemoQueryById() {
		boolean testFlag = false;
		if (!testFlag) {
			return;
		}
		
		DemoModel model = constantContext.getDemoDomainRepository().queryById(16);
		
		logger.info(model == null ? "null" : model.toString());
	}
	
	/**
	 * +测试分页查询接口
	 */
	@Test
	public void testDemoQueryByPage() {
		boolean testFlag = true;
		if (!testFlag) {
			return;
		}
		
		PaginationSupport<DemoModel> modelList = constantContext.getDemoDomainRepository().queryList(1, 5);
		
		logger.info(JSON.toJSONString(modelList));
	}
	
	/**
	 * +测试添加接口
	 */
	@Test
	public void testDemoInsert() {
		boolean testFlag = false;
		if (!testFlag) {
			return;
		}
		
		DemoModel model = new DemoModel();
		model.setGoodsCode("CODE123321");
		model.setGoodsSn("SN123321");
		model.setSize(20);
		
		int rt = constantContext.getDemoDomainRepository().insert(model);
		logger.info("" + rt);
	}
	
	/**
	 * +测试批量添加接口
	 */
	@Test
	public void testDemoInserts() {
		boolean testFlag = false;
		if (!testFlag) {
			return;
		}
		
		List<DemoModel> list = new ArrayList<>();
		DemoModel model = new DemoModel();
		model.setGoodsCode("CODE123329");
		model.setGoodsSn("SN123329");
		model.setSize(20);
		list.add(model);
		model = new DemoModel();
		model.setGoodsCode("CODE123330");
		model.setGoodsSn("SN123330");
		model.setSize(21);
		list.add(model);
		model = new DemoModel();
		model.setGoodsCode("CODE123331");
		model.setGoodsSn("SN123331");
		model.setSize(21);
		list.add(model);
		model = new DemoModel();
		model.setGoodsCode("CODE123332");
		model.setGoodsSn("SN123332");
		model.setSize(21);
		list.add(model);
		
		int rt = constantContext.getDemoDomainRepository().batchInsert(list);
		if (rt > 0) {
			logger.info(JSON.toJSONString(list));
		} else {
			logger.info("0");
		}
	}
}
