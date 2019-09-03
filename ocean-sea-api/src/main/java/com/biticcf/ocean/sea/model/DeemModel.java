/**
 * 
 */
package com.biticcf.ocean.sea.model;

import java.util.Date;

import com.github.biticcf.mountain.core.common.model.WdBaseModel;

/**
 * @author  DanielCao
 * @date    2019年4月6日
 * @time    下午4:23:10
 * 商品基本信息数据模型
 */
public class DeemModel extends WdBaseModel {
	private static final long serialVersionUID = 5028574067480079207L;
	
	private Long		 id; //自增主键
	private String       goodsCode; //商品代码
	private String       goodsSn; //商品编码(64位长度编码)(对应原ID)(唯一非空)
	
	/**
	 * 注意：以下4个字段,在每张表中都要有
	 */
	private Byte         status;  //记录状态,0有效,1无效
	private Date         createTime;  //创建时间
	private Date         updateTime; //更新时间
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
