/**
 * 
 */
package com.biticcf.ocean.sea.domain.dao.sqlprovider;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.biticcf.ocean.sea.domain.dao.po.DemoPo;

/**
 * @Author: Daniel.Cao
 * @Date:   2019年3月16日
 * @Time:   下午8:52:52
 * +注意:方法中只能有一个参数Map或对象bean
 */
public class DemoSqlProvider {
	/**
	 * +添加数据sql
	 * @param demoPo 传入对象值
	 * @return 拼成的sql
	 */
	public String insert(DemoPo demoPo) {
		return new SQL() {
			{
				INSERT_INTO("`WD_DEMO_INFO`");
				INTO_COLUMNS("`GOODS_CODE`", "`GOODS_SN`", "`STATUS`", "`CREATE_TIME`", "`UPDATE_TIME`", "`VERSION`");
				INTO_VALUES("#{goodsCode}", "#{goodsSn}", "0", "now()", "now()", "0");
			}
		}.toString();
	}
	
	/**
	 * +批量添加数据sql
	 * @param map 批量数据
	 * @return 拼成的sql
	 */
	public String inserts(Map<String, List<DemoPo>> map) {
		List<DemoPo> demoList = map.get("list");
		if (demoList == null || demoList.isEmpty()) {
			return null;
		}
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append("INSERT INTO `WD_DEMO_INFO`");
		sql.append("(`GOODS_CODE`, `GOODS_SN`, `STATUS`, `CREATE_TIME`, `UPDATE_TIME`, `VERSION`)");
		sql.append("VALUES");
		for (int i = 0; i < demoList.size(); i++) {
			sql.append("(#{list[" + i + "].goodsCode}, #{list[" + i + "].goodsSn}, 0, now(), now(), 0),");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(";");
		
		return sql.toString();
	}
}
