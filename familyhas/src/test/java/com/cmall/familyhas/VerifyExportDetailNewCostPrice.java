package com.cmall.familyhas;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 根据oc_bill_final_export表的cost_price更新oc_bill_product_detail_new表的cost_price
 * @author Administrator
 *
 */
public class VerifyExportDetailNewCostPrice {
	public static void main(String[] args) {
		int i = 0;
		List<Map<String, Object>> list=DbUp.upTable("oc_bill_product_detail_new").dataSqlList("select * from oc_bill_product_detail_new where cost_price=0.00", new MDataMap());
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String smallSellerCode = map.get("small_seller_code") != "" ? map.get("small_seller_code").toString() : "";
				String settleCode = map.get("settle_code") != "" ? map.get("settle_code").toString() : "";
				String productCode = map.get("product_code") != "" ? map.get("product_code").toString() : "";
				String skuCode = map.get("sku_code") !=null ? map.get("sku_code").toString() : "";
				if(StringUtils.isBlank(skuCode)) {
					List<Map<String,Object>> tempList = DbUp.upTable("oc_bill_final_export").dataSqlList("select cost_price from oc_bill_final_export "
							+ "where small_seller_code=:smallSellerCode and settle_code=:settleCode and product_code=:productCode", 
							new MDataMap("smallSellerCode",smallSellerCode,"settleCode",settleCode,"productCode", productCode));
					
					if(tempList != null && tempList.size() == 1) {
						i++;
						Map<String, Object> costPriceMap = tempList.get(0);
						String cost_price = costPriceMap.get("cost_price") != null ? costPriceMap.get("cost_price").toString() : "";
						DbUp.upTable("oc_bill_product_detail_new").dataUpdate(new MDataMap("cost_price", cost_price, "small_seller_code", smallSellerCode, 
								"settle_code", settleCode, "product_code", productCode), "cost_price", "small_seller_code,settle_code,product_code");
						
					} else {
						System.out.println(smallSellerCode + "@" + settleCode + "@" + productCode);
					}
					
				} else {
//					Map<String, Object> costPriceMap = DbUp.upTable("oc_bill_final_export").dataSqlOne("select cost_price from oc_bill_final_export "
//							+ "where small_seller_code=:smallSellerCode and settle_code=:settleCode and product_code=:productCode and sku_code=:skuCode", 
//							new MDataMap("smallSellerCode",smallSellerCode,"settleCode",settleCode,"productCode", productCode,"skuCode", skuCode));
					
					List<Map<String,Object>> tempList = DbUp.upTable("oc_bill_final_export").dataSqlList("select cost_price from oc_bill_final_export "
							+ "where small_seller_code=:smallSellerCode and settle_code=:settleCode and product_code=:productCode and sku_code=:skuCode", 
							new MDataMap("smallSellerCode",smallSellerCode,"settleCode",settleCode,"productCode", productCode,"skuCode", skuCode));
					
					if(tempList != null && tempList.size() == 1) {
						i++;
						Map<String, Object> costPriceMap = tempList.get(0);
						String cost_price = costPriceMap.get("cost_price") != null ? costPriceMap.get("cost_price").toString() : "";
						DbUp.upTable("oc_bill_product_detail_new").dataUpdate(new MDataMap("cost_price", cost_price, "small_seller_code", smallSellerCode, 
								"settle_code", settleCode, "product_code", productCode, "sku_code", skuCode), "cost_price", "small_seller_code,settle_code,product_code,sku_code");
					} else {
						System.out.println(smallSellerCode + "@" + settleCode + "@" + productCode + "@" + skuCode);
					}
				}
			}
			System.out.println("i=" + i);
		}
	}
}
