/**
 * 
 */
package com.biticcf.ocean.sea.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beyonds.phoenix.mountain.core.common.util.ClazzConverter;
import com.beyonds.phoenix.mountain.core.common.util.PaginationSupport;
import com.biticcf.ocean.sea.domain.dao.DemoDAO;
import com.biticcf.ocean.sea.domain.dao.po.DemoPo;
import com.biticcf.ocean.sea.model.DemoModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
	 * +分页查询
	 * @param page page值
	 * @param pageSize pageSize值
	 * @return 查询结果集
	 */
	public PaginationSupport<DemoModel> queryList(final int page, final int pageSize) {
		int p = page, ps = pageSize;
		if (pageSize <= 0) {
			ps = PaginationSupport.DEFAULT_PAGESIZE;
		} else if (pageSize > PaginationSupport.DEFAULT_MAX_PAGESIZE) {
			ps = PaginationSupport.DEFAULT_MAX_PAGESIZE;
		}
		if (page <= 0) {
			p = 1;
		}
		
		Page<DemoModel> pageInfo = PageHelper.startPage(p, ps);
		
		List<DemoPo> demoPoList = demoDAO.queryList();
		
		List<DemoModel> list = (List<DemoModel>) ClazzConverter.converterClass(demoPoList, DemoModel.class);
		if (list == null) {
			list = new ArrayList<>();
		}
		
		return new PaginationSupport<>(list, (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum());
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
