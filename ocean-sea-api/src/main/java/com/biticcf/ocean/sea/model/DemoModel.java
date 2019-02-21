/**
 * 
 */
package com.biticcf.ocean.sea.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import com.beyonds.phoenix.mountain.core.common.model.WdBaseModel;
import com.beyonds.phoenix.mountain.core.common.validator.GroupCreate;
import com.beyonds.phoenix.mountain.core.common.validator.GroupUpdate;

/**
 * @author  DanielCao
 * @date    2015年4月8日
 * @time    下午6:23:10
 * 注意：1,错误信息里的message请使用#ResultEnum里对应的code值!!!
 *      2,@NotBlank表示不允许null且长度不允许0,无此约束则只有非空时才校验其他约束
 */
@Validated
public class DemoModel extends WdBaseModel {
	private static final long serialVersionUID = -3690814072354261953L;
	
	@NotNull(groups = {GroupUpdate.class})
	private Long		 id; //自增主键
	@NotBlank(message = "4002")
	@Length(min = 5, max = 20, message = "4001")
	private String       goodsCode; 
	@NotNull(message = "4004", groups = {GroupCreate.class, Default.class})
	@Range(min = 10, max = 100, message = "4003", groups = {GroupCreate.class, Default.class})
	private Integer      size;
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
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
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
