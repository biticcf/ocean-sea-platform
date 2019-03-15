/**
 * 
 */
package com.biticcf.ocean.sea.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.beyonds.phoenix.mountain.core.common.result.WdCallbackResult;
import com.beyonds.phoenix.mountain.core.common.util.PaginationSupport;
import com.biticcf.ocean.sea.domain.support.ConstantContext;
import com.biticcf.ocean.sea.model.DemoModel;
import com.biticcf.ocean.sea.model.enums.ResultEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @Author: DanielCao
 * @Date:   2017年5月9日
 * @Time:   下午5:09:04
 *
 */
@Service("demoQueryListDomain")
@Scope("prototype")
public class DemoQueryListDomain extends AbstractBaseDomain<PaginationSupport<DemoModel>> {
	private ConstantContext constantContext;
	
	private int p;
	private int ps;
	
	// 注意：service中的参数通过构造方法传入domain，构造方法第一参数必须是ConstantContext，之后的参数顺序与service方法中的顺序必须一致
	public DemoQueryListDomain(ConstantContext constantContext, int p, int ps) {
		super(constantContext);
		
		this.constantContext = constantContext;
		this.p = p;
		this.ps = ps;
	}

	@Override
	public WdCallbackResult<PaginationSupport<DemoModel>> executeCheck() {
		// 检查参数
		return WdCallbackResult.success(ResultEnum.SUCCESS.getCode());
	}

	@Override
	public WdCallbackResult<PaginationSupport<DemoModel>> executeAction() {
		int offset = 0, limit = ps;
		if (ps <= 0) {
			limit = PaginationSupport.DEFAULT_PAGESIZE;
		} else if (ps > PaginationSupport.DEFAULT_MAX_PAGESIZE) {
			limit = PaginationSupport.DEFAULT_MAX_PAGESIZE;
		} else {
			limit = ps;
		}
		if (p <= 0) {
			offset = 0;
		} else {
			offset = limit * (p - 1);
		}
		Page<DemoModel> page = PageHelper.offsetPage(offset, limit);
		List<DemoModel> resultList = constantContext.getDemoDomainRepository().queryList();
		if (resultList == null) {
			resultList = new ArrayList<>();
		}
		
		PaginationSupport<DemoModel> result = new PaginationSupport<>(resultList, (int) page.getTotal(), page.getPageSize(), page.getPageNum());
		
		return WdCallbackResult.success(ResultEnum.SUCCESS.getCode(), result);
	}

	@Override
	public void executeAfter() {
		//
	}
}
