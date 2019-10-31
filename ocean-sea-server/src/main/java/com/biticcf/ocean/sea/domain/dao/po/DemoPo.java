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

/**
 * @author  DanielCao
 * @date    2015年4月8日
 * @time    下午6:23:10
 * 商品基本信息数据模型,对应于WD_DEMO_INFO表(不存在的表,实际应用时替换为实际表)
 */
@TableName(value = "WD_DEMO_INFO")
public class DemoPo extends WdBaseModel {
	private static final long serialVersionUID = -3690814072354261953L;
	
	@TableId(value = "ID", type = IdType.AUTO)
    @TableField(value = "ID", jdbcType = JdbcType.BIGINT)
	private Long		 id; //自增主键
	@TableField(value = "GOODS_CODE", jdbcType = JdbcType.VARCHAR)
	private String       goodsCode; //商品代码
	@TableField(value = "GOODS_SN", jdbcType = JdbcType.VARCHAR)
	private String       goodsSn; //商品编码(64位长度编码)(对应原ID)(唯一非空)
	
	/**
	 * 注意：以下4个字段,在每张表中都要有
	 */
	@TableField(value = "STATUS", jdbcType = JdbcType.TINYINT)
	private Byte         status;  //记录状态,0有效,1无效
	@TableField(value = "CREATE_TIME", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE)
	private Date         createTime;  //创建时间
	@TableField(value = "UPDATE_TIME", jdbcType = JdbcType.TIMESTAMP_WITH_TIMEZONE)
	private Date         updateTime; //更新时间
	@TableField(value = "VERSION", jdbcType = JdbcType.INTEGER)
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
