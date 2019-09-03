/**
 * 
 */
package com.biticcf.ocean.sea.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.biticcf.ocean.sea.domain.support.ConstantContext;
import com.biticcf.ocean.sea.model.DemoModel;
import com.biticcf.ocean.sea.model.enums.ResultEnum;
import com.github.biticcf.mountain.core.common.result.WdCallbackResult;
import com.github.biticcf.mountain.core.common.util.PaginationSupport;

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
		PaginationSupport<DemoModel> result = constantContext.getDemoDomainRepository().queryList(p, ps);
		
		return WdCallbackResult.success(ResultEnum.SUCCESS.getCode(), result);
	}

	@Override
	public void executeAfterSuccess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void executeAfterFailure(Throwable e) {
		// TODO Auto-generated method stub
	}
}
