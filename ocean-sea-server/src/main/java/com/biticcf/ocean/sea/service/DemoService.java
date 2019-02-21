
package com.biticcf.ocean.sea.service;

import com.beyonds.phoenix.mountain.core.common.annotation.ProductCode;
import com.beyonds.phoenix.mountain.core.common.annotation.ProductLogLevelEnum;
import com.beyonds.phoenix.mountain.core.common.result.CallResult;
import com.beyonds.phoenix.mountain.core.common.util.PaginationSupport;
import com.beyonds.phoenix.mountain.shackle.ShackleDomain;
import com.beyonds.phoenix.mountain.shackle.ShackleTemplate;
import com.biticcf.ocean.sea.domain.DemoInsertDomain;
import com.biticcf.ocean.sea.domain.DemoQueryDomain;
import com.biticcf.ocean.sea.domain.DemoQueryListDomain;
import com.biticcf.ocean.sea.model.DemoModel;

/**
 * 
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午1:41:07
 *
 */
@ShackleTemplate(name = "demoService")
public interface DemoService {
	/**
	 * 查询数据
	 * @param id 参数
	 * @return 查询结果
	 */
	@ShackleDomain(value = "queryById", domain = DemoQueryDomain.class, domainName = "demoQueryDomain", withTrans = false)
	@ProductCode(code = "00000", version = "1.0", logLevel = ProductLogLevelEnum.INFO)
	CallResult<DemoModel> queryById(Long id);
	
	/**
	 * 保存数据对象
	 * @param demoModel 数据对象
	 * @return 保存执行结果
	 */
	@ShackleDomain(value = "insertDemo", domain = DemoInsertDomain.class, domainName = "demoInsertDomain", withTrans = true)
	@ProductCode(code = "00000", version = "1.0", logLevel = ProductLogLevelEnum.INFO)
	CallResult<DemoModel> insertDemo(DemoModel demoModel);
	
	/**
	 * 分页查询数据
	 * @param p 分页参数
	 * @param ps 分页参数
	 * @return 查询结果集
	 */
	@ShackleDomain(value = "queryList", domain = DemoQueryListDomain.class, domainName = "demoQueryListDomain", withTrans = false)
	@ProductCode(code = "00000", version = "1.0", logLevel = ProductLogLevelEnum.INFO)
	CallResult<PaginationSupport<DemoModel>> queryList(int p, int ps);
}
