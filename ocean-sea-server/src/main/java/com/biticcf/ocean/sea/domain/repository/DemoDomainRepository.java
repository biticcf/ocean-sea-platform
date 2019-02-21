/**
 * 
 */
package com.biticcf.ocean.sea.domain.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beyonds.phoenix.mountain.core.common.util.ClazzConverter;
import com.biticcf.ocean.sea.domain.dao.DemoDAO;
import com.biticcf.ocean.sea.domain.dao.po.DemoPo;
import com.biticcf.ocean.sea.model.DemoModel;

/**
 * @author  DanielCao
 * @date    2015年6月29日
 * @time    上午10:53:27
 *
 */
@Repository("demoDomainRepository")
public class DemoDomainRepository {
	@Autowired
	private DemoDAO demoDAO;
	/**
	 * 根据id查询
	 * @param id id值
	 * @return 返回结果
	 */
	public DemoModel queryById(long id) {
		DemoPo demoPo = demoDAO.queryById(id);
		
		return ClazzConverter.converterClass(demoPo, DemoModel.class);
	}
	
	/**
	 * 分页查询
	 * @param offset 查询其实偏移量
	 * @param limit 查询最大数量
	 * @return 查询结果集
	 */
	public List<DemoModel> queryList(int offset, int limit) {
		List<DemoPo> demoPoList = demoDAO.queryList(offset, limit);
		
		return (List<DemoModel>) ClazzConverter.converterClass(demoPoList, DemoModel.class);
	}
	
	/**
	 * 查询总条数
	 * @return 结果总条数
	 */
	public int queryCount() {
		return demoDAO.queryCount();
	}
	/**
	 * 保存一条记录
	 * @param demoModel 对象
	 * @return 1成功,其他失败
	 */
	public int insert(DemoModel demoModel) {
		DemoPo demoPo = ClazzConverter.converterClass(demoModel, DemoPo.class);
		if (demoPo == null) {
			return 0;
		}
		
		int rt = demoDAO.insert(demoPo);
		if (rt > 0) {
			demoModel.setId(demoPo.getId());
		}
		
		return rt;
	}
	/**
	 * 批量保存
	 * @param list 对象集合 
	 * @return 保存成功条数
	 */
	public int batchInsert(List<DemoModel> list) {
		if (list == null || list.isEmpty()) {
			return 0;
		}
		List<DemoPo> demoList = (List<DemoPo>) ClazzConverter.converterClass(list, DemoPo.class);
		if (demoList == null || demoList.size() != list.size()) {
			return 0;
		}
		
		return demoDAO.batchInsert(demoList);
	}
}
