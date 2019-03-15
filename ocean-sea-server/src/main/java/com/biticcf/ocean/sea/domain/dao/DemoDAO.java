/**
 * 
 */
package com.biticcf.ocean.sea.domain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.biticcf.ocean.sea.domain.dao.po.DemoPo;

/**
 * @author  DanielCao
 * @date    2015年6月29日
 * @time    上午10:20:48
 *
 */
@Mapper
public interface DemoDAO {
	/**
	 * 根据id查询
	 * @param id ID值
	 * @return 查询结果
	 */
	@Select("SELECT * FROM WD_DEMO_INFO WHERE ID = #{id}")
	DemoPo queryById(@Param("id") long id);
	/**
	 * 分页查询
	 * @return 查询结果
	 */
	@Select("SELECT * FROM WD_DEMO_INFO ORDER BY ID DESC")
	List<DemoPo> queryList();
	
	/**
	 * 查询总数
	 * @return 总数值
	 */
	@Select("SELECT count(*) FROM WD_DEMO_INFO")
	int queryCount();
	/**
	 * 保存一条记录
	 * @param demoPo 数据记录
	 * @return 保存成功条数
	 */
	int insert(DemoPo demoPo);
	/**
	 * 批量保存
	 * @param demoList 对象集
	 * @return 保存成功条数
	 */
	int batchInsert(List<DemoPo> demoList);
}
