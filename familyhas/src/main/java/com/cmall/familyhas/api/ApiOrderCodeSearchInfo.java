package com.cmall.familyhas.api;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.OrderCodeSearchInfoInput;
import com.cmall.familyhas.api.model.ProductPunishModel;
import com.cmall.familyhas.api.result.OrderCodeSearchInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiOrderCodeSearchInfo extends RootApi<OrderCodeSearchInfoResult,OrderCodeSearchInfoInput>{

	@Override
	public OrderCodeSearchInfoResult Process(OrderCodeSearchInfoInput input, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		OrderCodeSearchInfoResult result = new OrderCodeSearchInfoResult();
		String orderCode = input.getOrderCode();
		String companyCode = input.getCompanyCode();
		String orderTime =  "";
		//根据订单编号和公司编号查询这条订单信息，判断订单号和公司编号是否匹配或有误
		String sql = "SELECT * FROM ordercenter.oc_orderdetail a  LEFT JOIN ordercenter.oc_orderinfo b  ON a.order_code = b.order_code  " + 
				"   WHERE a.order_code = '"+orderCode+"' AND b.small_seller_code = '"+companyCode+"';";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderdetail").dataSqlList(sql, null);
		
		if(dataSqlList==null||dataSqlList.size()==0) {
			//没找到
			result.setResultCode(0);
			result.setResultMessage("输入的订单号或公司编码有误");
		}else {
			List<ProductPunishModel> list = new ArrayList<>(); 
			for(Map<String, Object>  map  : dataSqlList) {
				ProductPunishModel model =  new  ProductPunishModel();
				model.setProductCode(map.get("sku_code").toString());
				model.setProductCost(new  BigDecimal(map.get("cost_price").toString()));
				model.setProductName(map.get("sku_name").toString());
				model.setProductSellPrice(new  BigDecimal(map.get("show_price").toString()));
				list.add(model);
				if("".equals(orderTime)) {
					orderTime = map.get("create_time").toString();
					result.setOrderTime(orderTime);
				}
			}
			result.setList(list);
		}
		return result;
	}

}
