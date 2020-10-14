package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webexport.ExportChart;
import com.srnpr.zapweb.webmodel.MPageData;

/**
 * 三方合作商品导出
 */
public class FuncExportThirdPartyProduct extends ExportChart {

	@Override
	public void exportExcel(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		super.exportExcel(sOperateId, request, response);
		
		MPageData pageData = getPageData();
		int productIndex = pageData.getPageField().indexOf("product_code");
		
		MPageData newPageData = new MPageData();
		newPageData.getPageHead().add("商品编号");
		newPageData.getPageHead().add("SKU编号");
		newPageData.getPageHead().add("商品名称");
		newPageData.getPageHead().add("SKU名称");
		newPageData.getPageHead().add("销售价");
		newPageData.getPageHead().add("成本价");
		newPageData.getPageHead().add("是否可卖");
		
		if(pageData.getPageData() == null
				|| productIndex < 0) {
			return;
		}
		
		newPageData.setPageData(new ArrayList<List<String>>());
		
		String sSql = "select p.product_name,s.sku_code,s.sku_name,s.sell_price,s.cost_price,s.sale_yn FROM pc_productinfo p,pc_skuinfo s WHERE p.product_code = s.product_code AND p.product_code = :product_code";
		
		String productCode;
		List<Map<String,Object>> skuList;
		for(List<String> dataList : pageData.getPageData()) {
			productCode = dataList.get(productIndex);
			
			skuList = DbUp.upTable("pc_skuinfo").dataSqlList(sSql, new MDataMap("product_code", productCode));
			for(Map<String,Object> map : skuList) {
				newPageData.getPageData().add(Arrays.asList(
						productCode,
						map.get("sku_code").toString(),
						map.get("product_name").toString(),
						map.get("sku_name").toString(),
						map.get("sell_price").toString(),
						map.get("cost_price").toString(),
						"Y".equals(map.get("sale_yn")) ? "可卖" : "不可卖"
						));
			}
		}
		
		setPageData(newPageData);
	}
	
}
