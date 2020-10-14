package com.cmall.familyhas.pagehelper;

import java.math.BigDecimal;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 跟提供成本和售价计算毛利率<br>
 * 参数顺序： 1 成本价，2 售价
 */
public class PriceProfitHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		BigDecimal costPrice = new BigDecimal(params[0].toString());
		BigDecimal sellPrice = new BigDecimal(params[1].toString());
		
		if(sellPrice.compareTo(BigDecimal.ZERO) <= 0) {
			return "";
		}
		
		// 毛利率 =（销售价-成本价）/ 销售价 * 100
		BigDecimal profit = sellPrice.subtract(costPrice).multiply(new BigDecimal(100)).divide(sellPrice,0,BigDecimal.ROUND_HALF_UP);
		
		// 毛利率不一致时把最小和最大拼接到一起
		return format(profit);
	}
	
	private String format(BigDecimal v) {
		return v.compareTo(BigDecimal.ZERO) < 0 ? "(-" + v + "%)" : v + "%";
	}

}
