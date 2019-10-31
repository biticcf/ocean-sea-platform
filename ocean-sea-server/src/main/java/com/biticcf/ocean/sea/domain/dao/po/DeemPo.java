/**
 * 
 */
package com.biticcf.ocean.sea.domain.dao.po;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.biticcf.mountain.core.common.model.WdBaseModel;
import com.github.biticcf.mountain.generator.annotation.ColumnConfig;
import com.github.biticcf.mountain.generator.annotation.EnuFieldType;
import com.github.biticcf.mountain.generator.annotation.TableConfig;

/**
 * @author  DanielCao
 * @date    2015年4月8日
 * @time    下午6:23:10
 * 商品基本信息数据模型,对应于WD_DEEM_INFO表(不存在的表,实际应用时替换为实际表)
 */
@TableConfig(poName = "DeemPo", tableName = "WD_DEEM_INFO")
@TableName(value = "WD_DEEM_INFO")
public class DeemPo extends WdBaseModel {
	private static final long serialVersionUID = -3690814072354261953L;
	
	@ColumnConfig(propertyName = "id", columnName = "id", primaryKeyFlag = true, columnType = EnuFieldType.BIGINT)
	@TableId(value = "id", type = IdType.AUTO)
    @TableField(value = "id", jdbcType = JdbcType.BIGINT)
	private Long		 id; //自增主键
	@ColumnConfig(propertyName = "goodsCode", columnName = "goods_code", columnType = EnuFieldType.VARCHAR)
	@TableField(value = "goods_code", jdbcType = JdbcType.VARCHAR)
	private String       goodsCode; //商品代码
	@ColumnConfig(propertyName = "goodsSn", columnName = "goods_sn", columnType = EnuFieldType.VARCHAR)
	@TableField(value = "goods_sn", jdbcType = JdbcType.VARCHAR)
	private String       goodsSn; //商品编码(64位长度编码)(对应原ID)(唯一非空)
	
	/**
	 * 注意：以下4个字段,在每张表中都要有
	 */
	@ColumnConfig(propertyName = "status", columnName = "status", columnType = EnuFieldType.TINYINT)
	@TableField(value = "status", jdbcType = JdbcType.TINYINT)
	private Byte         status;  //记录状态,0有效,1无效
	@ColumnConfig(propertyName = "createTime", columnName = "create_time", columnType = EnuFieldType.DATETIME)
	@TableField(value = "create_time", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE)
	private Date         createTime;  //创建时间
	@ColumnConfig(propertyName = "updateTime", columnName = "update_time", columnType = EnuFieldType.TIMESTAMP)
	@TableField(value = "update_time", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE)
	private Date         updateTime; //更新时间
	@ColumnConfig(propertyName = "version", columnName = "version", columnType = EnuFieldType.INTEGER)
	@TableField(value = "version", jdbcType = JdbcType.INTEGER)
	private Integer      version; //乐观锁版本号
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the goodsCode
	 */
	public String getGoodsCode() {
		return goodsCode;
	}
	/**
	 * @param goodsCode the goodsCode to set
	 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	/**
	 * @return the goodsSn
	 */
	public String getGoodsSn() {
		return goodsSn;
	}
	/**
	 * @param goodsSn the goodsSn to set
	 */
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
}
