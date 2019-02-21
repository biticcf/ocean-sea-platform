/**
 * 
 */
package com.biticcf.ocean.sea.facade;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beyonds.phoenix.mountain.core.common.result.ReturnResult;
import com.biticcf.ocean.sea.model.DemoModel;

/**
 * @Author: DanielCao
 * @Date:   2018年5月20日
 * @Time:   下午8:33:39
 *
 */
@RequestMapping(value = "/ocean/sea/v1")
public interface DemoFacade {
	/**
	 * 保存数据
	 * @param demoModel 数据对象
	 * @return 保存结果
	 */
	@RequestMapping(value = {"/demo"}, 
			        method = {RequestMethod.POST}, 
			        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<DemoModel> insertDemo(@RequestBody DemoModel demoModel) throws Throwable;
	
	/**
	 * 根据id查询对象
	 * @param id 对象id
	 * @param t 参数
	 * @return 对象结果
	 */
	@RequestMapping(value = {"/demo/{id}"}, 
	        method = {RequestMethod.GET}, 
	        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<DemoModel> queryById(
			@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "t", required = true) String t) throws Throwable;

    /**
	 * 分页查询数据
	 * @param p 分页参数
	 * @param ps 分页参数
	 * @return 查询数据集
	 */
	@RequestMapping(value = {"/demos"}, 
	        method = {RequestMethod.GET}, 
	        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<List<DemoModel>> queryList(
			@RequestParam(value = "p", required = true) int p,
			@RequestParam(value = "ps", required = true) int ps) throws Throwable;
}
