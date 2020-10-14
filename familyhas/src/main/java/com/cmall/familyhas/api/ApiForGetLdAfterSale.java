package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ld.ApiForGetLdAfterSaleInput;
import com.cmall.familyhas.api.result.ld.AfterSaleOrder;
import com.cmall.familyhas.api.result.ld.ApiForGetLdAfterSaleResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * @desc 查询LD退货售后列表
 * 
 */
public class ApiForGetLdAfterSale extends RootApiForToken<ApiForGetLdAfterSaleResult, ApiForGetLdAfterSaleInput> {

	@Override
	public ApiForGetLdAfterSaleResult Process(
			ApiForGetLdAfterSaleInput inputParam, MDataMap mRequestMap) {
		ApiForGetLdAfterSaleResult result = new ApiForGetLdAfterSaleResult();
		String memberCode = inputParam.getMemberCode();
		String sSql = "select * from ordercenter.oc_after_sale_ld where member_code = '"+memberCode+"'";
		List<Map<String,Object>> list = DbUp.upTable("oc_after_sale_ld").dataSqlList(sSql, null);
		List<AfterSaleOrder> list2 = new ArrayList<AfterSaleOrder>();
		for(Map<String,Object> map : list){
			AfterSaleOrder order = new AfterSaleOrder();
			order.setAfterSaleCodeApp(map.get("after_sale_code_app") != null? map.get("after_sale_code_app").toString():"");
			order.setAfterSaleCodeLd(map.get("after_sale_code_ld") !=null?map.get("after_sale_code_ld").toString():"");
			order.setAfterSaleStatus(map.get("after_sale_status") != null?map.get("after_sale_status").toString():"");
			order.setCreateTime(map.get("create_time") != null?map.get("create_time").toString():"");
			order.setMemberCode(map.get("member_code") !=null?map.get("member_code").toString():"");
			order.setModifTime(map.get("modif_time") !=null?map.get("modif_time").toString():"");
			order.setOrderCode(map.get("order_code") !=null?map.get("order_code").toString():"");
			order.setProductCode(map.get("product_code") !=null?map.get("product_code").toString():"");
			order.setReturnCode(map.get("return_code") !=null?map.get("return_code").toString():"");
			order.setSellerCode(map.get("seller_code") !=null?map.get("seller_code").toString():"");
			order.setSkuCode(map.get("sku_code") !=null?map.get("sku_code").toString():"");
			order.setUid(map.get("uid") !=null?map.get("uid").toString():"");
			order.setZid(Integer.parseInt(map.get("zid")!=null?map.get("zid").toString():"0"));
			list2.add(order);
		}
		result.setAfterSaleOrderList(list2);
		return result;
	}

	
}
