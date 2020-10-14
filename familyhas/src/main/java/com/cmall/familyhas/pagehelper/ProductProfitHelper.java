package com.cmall.familyhas.pagehelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 取商品的毛利:  xx%<br>
 * 多SKU不同毛利时输出最低和最高用中划线分割  xx%-xx%
 */
public class ProductProfitHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		List<MDataMap> mapList = DbUp.upTable("pc_skuinfo").queryAll("sell_price,cost_price", "", "", new MDataMap("product_code", (String)params[0],"sale_yn", "Y"));
	
		List<BigDecimal> profitList = new ArrayList<BigDecimal>();
		BigDecimal sellPrice,costPrice;
		for(MDataMap map : mapList) {
			sellPrice = new BigDecimal(map.get("sell_price"));
			costPrice = new BigDecimal(map.get("cost_price"));
			
			if(costPrice.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			
			// 毛利率 =（销售价-成本价）/ 销售价 * 100
			profitList.add(sellPrice.subtract(costPrice).multiply(new BigDecimal(100)).divide(sellPrice,0,BigDecimal.ROUND_HALF_UP));
		}
		
		if(profitList.isEmpty()) {
			return "";
		}
		
		// 按毛利率升序排列
		java.util.Collections.sort(profitList);
		
		// 所有SKU的毛利率都一致时取第一个
		if(profitList.get(0).compareTo(profitList.get(profitList.size() - 1)) == 0) {
			return format(profitList.get(0));
		}
		
		// 毛利率不一致时把最小和最大拼接到一起
		return format(profitList.get(0)) + " - " + format(profitList.get(profitList.size() - 1));
	}
	
	private String format(BigDecimal v) {
		return v.compareTo(BigDecimal.ZERO) < 0 ? "(-" + v + "%)" : v + "%";
	}

}
