package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.EventListGoodsEntity;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/***
 * 价格区间对象
 * @author zhangbo
 *
 */
public class PriceChooseObj {
	@ZapcomApi(value = "搜索最低价格", remark="用户搜索最低价格,默认最低价格为0")
	private BigDecimal minPrice =BigDecimal.ZERO ;
	
	@ZapcomApi(value = "搜索最高价格", remark="用户搜索最高价格")
	private BigDecimal maxPrice =new BigDecimal(30);
	
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	
}
