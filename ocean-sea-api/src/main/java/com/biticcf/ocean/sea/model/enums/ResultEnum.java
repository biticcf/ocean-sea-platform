/**
 *
 */
package com.biticcf.ocean.sea.model.enums;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author  DanielCao
 * @date    2015年4月1日
 * @time    下午8:51:03
 *
 */
public enum ResultEnum {
	SUCCESS(200, "SUCCESS", "成功"),
	PATH_NOT_FOUND(404, "PATH_NOT_FOUND", "请求地址不存在"),
	
	/**
	 * 参数错误4xxx
	 */
	PARAM_ERROR(4000, "PARAM_ERROR", "参数错误"),
	PARAM_GOODS_CODE_LENGTH_ERROR(4001, "PARAM_LENGTH_ERROR", "goodsCode参数长度错误"),
	PARAM_GOODS_CODE_NULL_ERROR(4002, "PARAM_GOODS_CODE_NULL_ERROR", "goodsCode参数不允许空"),
	PARAM_SIZE_RANGE_ERROR(4003, "PARAM_SIZE_RANGE_ERROR", "size参数取值范围错误"),
	PARAM_SIZE_NULL_ERROR(4004, "PARAM_SIZE_NULL_ERROR", "size参数不允许空"),
	
	/**
	 * 业务错误5xxx
	 */
	SYS_ERROR(5000, "SYS_ERROR", "系统异常"),
	NOT_FOUND(5001, "NOT_FOUND", "数据未找到"),
	ADD_FAIL(5002, "ADD_FAIL", "数据添加失败"),
	
	ASYNC_TASK_ERROR(5999, "ASYNC_TASK_ERROR", "异步执行任务失败"),
	
	/**
	 * 系统错误99xx
	 */
	DB_ERROR(9998, "DB_ERROR", "数据库异常"),
	FAILURE(9999, "FAILURE", "业务失败"),
	UNKNOWN(-1, "UNKNOWN", "未定义错误");

	private int code;
	private String message;
	private String desc;

	ResultEnum(int code, String message, String desc) {
		this.code = code;
		this.message = message;
		this.desc = desc;
	}
	
	/**
	 * 根据code查询枚举对象
	 * @param code code值
	 * @return 枚举对象
	 */
	public static ResultEnum valueOf(int code) {
		ResultEnum[] enums = values();
		if (enums == null || enums.length == 0) {
			return UNKNOWN;
		}
		for (ResultEnum _enu : enums) {
			if (code == _enu.getCode()) {
				return _enu;
			}
		}
		
		return UNKNOWN;
	}
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		try {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			        .append("code", code)
					.append("message", message)
					.append("desc", desc)
					.toString();
        } catch (Exception e) {
            // NOTICE: 这样做的目的是避免由于toString()的异常导致系统异常终止
            // 大部分情况下，toString()用在日志输出等调试场景
            return super.toString();
        }
	}
}
