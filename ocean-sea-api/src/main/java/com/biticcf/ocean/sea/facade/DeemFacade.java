/**
 * 
 */
package com.biticcf.ocean.sea.facade;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beyonds.phoenix.mountain.core.common.result.ReturnResult;
import com.beyonds.phoenix.mountain.generator.annotation.FacadeConfig;
import com.beyonds.phoenix.mountain.generator.annotation.MethodConfig;
import com.biticcf.ocean.sea.model.DeemModel;

/**
 * @Author: DanielCao
 * @Date:   2018年5月20日
 * @Time:   下午8:33:39
 *
 */
@FacadeConfig(name = "Deem", description = "Deem接口定义", execGenerator = true, useSwagger = true, genDaoCode = true)
@RequestMapping(value = "/ocean/sea/v1")
public interface DeemFacade {
	/**
	 * 保存数据
	 * @param demoModel 数据对象
	 * @return 保存结果
	 */
	@MethodConfig(name = "insertDeem", description = "insertDeem方法定义说明", listResultFlag = false, paginationFlag = false, withTransaction = true)
	@RequestMapping(value = {"/deem"}, 
			        method = {RequestMethod.POST}, 
			        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<DeemModel> insertDeem(@RequestBody @Valid DeemModel deemModel) throws Throwable;
	
	/**
	 * 根据id查询对象
	 * @param id 对象id
	 * @param t 参数
	 * @return 对象结果
	 */
	@MethodConfig(name = "queryById", description = "queryById方法定义说明", listResultFlag = false, paginationFlag = false, withTransaction = false)
	@RequestMapping(value = {"/deem/{id}"}, 
	        method = {RequestMethod.GET}, 
	        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<DeemModel> queryById(
			@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "t", required = true) String t) throws Throwable;

    /**
	 * 分页查询数据
	 * @param p 分页参数
	 * @param ps 分页参数
	 * @return 查询数据集
	 */
	@MethodConfig(name = "queryList", description = "queryList方法定义说明", listResultFlag = true, paginationFlag = true, withTransaction = false)
	@RequestMapping(value = {"/deems"}, 
	        method = {RequestMethod.GET}, 
	        produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	ReturnResult<List<DeemModel>> queryList(
			@RequestParam(value = "p", required = true) Integer p,
			@RequestParam(value = "ps", required = true) Integer ps) throws Throwable;
}
