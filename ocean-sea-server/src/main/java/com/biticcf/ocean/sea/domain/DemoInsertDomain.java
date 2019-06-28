/**
 * 
 */
package com.biticcf.ocean.sea.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.beyonds.phoenix.mountain.core.common.result.WdCallbackResult;
import com.biticcf.ocean.sea.domain.support.ConstantContext;
import com.biticcf.ocean.sea.model.DemoModel;
import com.biticcf.ocean.sea.model.enums.ResultEnum;

/**
 * @Author: DanielCao
 * @Date:   2017年5月9日
 * @Time:   下午5:09:04
 *
 */
@Service("demoInsertDomain")
@Scope("prototype")
public class DemoInsertDomain extends AbstractBaseDomain<DemoModel> {
	private ConstantContext constantContext;
	private DemoModel demoModel;
	
	// 注意：service中的参数通过构造方法传入domain，构造方法第一参数必须是ConstantContext，之后的参数顺序与service方法中的顺序必须一致
	public DemoInsertDomain(ConstantContext constantContext, DemoModel demoModel) {
		super(constantContext);
		
		this.constantContext = constantContext;
		this.demoModel = demoModel;
	}

	@Override
	public WdCallbackResult<DemoModel> executeCheck() {
		// 检查参数
		return WdCallbackResult.success(ResultEnum.SUCCESS.getCode());
	}

	@Override
	public WdCallbackResult<DemoModel> executeAction() {
		// 保存demoModel
		int rt = constantContext.getDemoDomainRepository().insert(demoModel);
		if (rt <= 0) {
			return WdCallbackResult.failure(ResultEnum.SYS_ERROR.getCode(), "保存数据失败");
		}
		
		return WdCallbackResult.success(ResultEnum.SUCCESS.getCode(), demoModel);
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
