package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForPurchaseProductInput;
import com.cmall.familyhas.api.result.ApiForPurchaseProductResult;
import com.cmall.familyhas.model.OrderDetail;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;


public class ApiForPurchaseProduct extends RootApi<ApiForPurchaseProductResult,  ApiForPurchaseProductInput>{

	@Override
	public ApiForPurchaseProductResult Process(ApiForPurchaseProductInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub\
		ApiForPurchaseProductResult result = new ApiForPurchaseProductResult();
		String skuCodes = inputParam.getSkuCodes();
		if (StringUtils.isEmpty(skuCodes)) {
			return result;
		} else {
			//解决前端商品选择框的一个bug,有时会传过来空sku的信息
			skuCodes = skuCodes.replace(",,", ",");
			if(StringUtils.startsWith(skuCodes, ",")) {
				skuCodes = StringUtils.substringAfter(skuCodes, ",");
			}
			if(StringUtils.endsWith(skuCodes, ",")) {
				skuCodes = StringUtils.substringBeforeLast(skuCodes, ",");
			}
		}
		String sWhere = "sku_code in ('"+skuCodes.replace(",", "','")+"')";
		String sFields = "";
		List<MDataMap> mapList=DbUp.upTable("pc_skuinfo").queryAll(sFields, "", sWhere,null);
		for (MDataMap mDataMap : mapList) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setIf_selected("1");
			orderDetail.setProduct_code(mDataMap.get("product_code").toString());
			orderDetail.setSku_code(mDataMap.get("sku_code").toString());
			orderDetail.setProduct_name(mDataMap.get("sku_name").toString());
			orderDetail.setProduct_property(mDataMap.get("sku_keyvalue").toString());
			orderDetail.setProduct_img(mDataMap.get("sku_picurl").toString());
			orderDetail.setSku_num("1");
			BigDecimal sm =new BigDecimal(mDataMap.get("sell_price").toString());
			BigDecimal roundHalfUp = MoneyHelper.roundHalfUp(sm);
			orderDetail.setRowSumMoney(roundHalfUp.toString());	
			orderDetail.setCost_money(MoneyHelper.roundHalfUp(new BigDecimal(mDataMap.get("cost_price").toString())).toString());
			orderDetail.setSell_money(MoneyHelper.roundHalfUp(new BigDecimal(mDataMap.get("sell_price").toString())).toString());
			result.getOrderDetailList().add(orderDetail);
		}
		return result;
	}



}
