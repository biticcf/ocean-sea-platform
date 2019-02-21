/**
 * 
 */
package com.biticcf.ocean.sea.domain.feign.fallback;

import com.biticcf.ocean.sea.domain.feign.CategoryFeignClient;

/**
 * @Author: DanielCao
 * @Date:   2017年6月29日
 * @Time:   上午1:15:24
 *
 */
public class CategoryFeignClientFallback implements CategoryFeignClient {
	@Override
	public String getCategorys(String categoryIds) {
		return "{\"status\":5001}";
	}
}
