/**
 * 
 */
package com.biticcf.ocean.sea.domain.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.type.JdbcType;

import com.biticcf.ocean.sea.domain.dao.po.DemoPo;
import com.biticcf.ocean.sea.domain.dao.sqlprovider.DemoSqlProvider;
import com.github.biticcf.mountain.core.common.service.MountainBaseMapper;

/**
 * @author  DanielCao
 * @date    2015年6月29日
 * @time    上午10:20:48
 *
 */
@CacheNamespace(eviction = LruCache.class, flushInterval = 60000L, size = 1024, readWrite = true)
@Mapper
public interface DemoDAO extends MountainBaseMapper<DemoPo> {
	/**
	 * 根据id查询
	 * @param id ID值
	 * @return 查询结果
	 */
	@Options(useCache = true, flushCache = Options.FlushCachePolicy.FALSE, timeout = 60000)
	@Results(id = "demoMap", value = {
			@Result(property = "id", column = "id", id = true, javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "goodsCode", column = "GOODS_CODE", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "goodsSn", column = "GOODS_SN", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "status", column = "STATUS", javaType = Byte.class, jdbcType = JdbcType.TINYINT),
			@Result(property = "createTime", column = "CREATE_TIME", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "updateTime", column = "UPDATE_TIME", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "version", column = "VERSION", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
	})
	@Select("SELECT * FROM `WD_DEMO_INFO` WHERE `ID` = #{id}")
	DemoPo queryById(@Param("id") long id);
	/**
	 * +查询列表
	 * @return 查询结果
	 */
	@Options(useCache = true, flushCache = Options.FlushCachePolicy.FALSE, timeout = 60000)
	@Select("SELECT * FROM `WD_DEMO_INFO` ORDER BY `ID` DESC")
	@ResultMap(value = {"demoMap"})
	List<DemoPo> queryList();
	/**
	 * +保存一条记录
	 * @param demoPo 数据记录
	 * @return 保存成功条数
	 */
	@Options(useCache = true, flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@InsertProvider(type = DemoSqlProvider.class, method = "insert")
	int insert(DemoPo demoPo);
	/**
	 * 批量保存
	 * @param demoList 对象集
	 * @return 保存成功条数
	 */
	@Options(useCache = true, flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@InsertProvider(type = DemoSqlProvider.class, method = "inserts")
	int batchInsert(List<DemoPo> demoList);
}
