package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiGetAvailableCouponInput;
import com.cmall.familyhas.api.result.ApiGetAvailableCouponResult;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.service.CouponsService;
import com.cmall.ordercenter.model.CouponInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 可用优惠劵查询
 * @author liqt
 *
 */
public class ApiGetAvailableCoupon extends RootApiForToken<ApiGetAvailableCouponResult, ApiGetAvailableCouponInput>{
	
	public ApiGetAvailableCouponResult Process(ApiGetAvailableCouponInput input,MDataMap mRequestMap){
		ApiGetAvailableCouponResult result=new ApiGetAvailableCouponResult();
		//获取用户编号
		String member_code=getUserCode();
		//新增入参，支付方式。
		String paymentType = "";
		if(!StringUtils.isEmpty(input.getPaymentType())){
			paymentType = input.getPaymentType();
		}
		
		if(input.getGoods()==null||input.getGoods().isEmpty()){
			input.setGoods(input.getSkuCodeEntitylist());
		}
		
		BigDecimal shouldPay=input.getShouldPay();
				
		CouponsService couponService=new CouponsService();
		List<com.cmall.ordercenter.model.GoodsInfoForAdd> goodsList = new ArrayList<com.cmall.ordercenter.model.GoodsInfoForAdd>();
		for (GoodsInfoForAdd goodsInfoForAdd : input.getGoods()) {
			com.cmall.ordercenter.model.GoodsInfoForAdd goods = new com.cmall.ordercenter.model.GoodsInfoForAdd();
			goods.setIsPurchase(input.getIsPurchase());
			goods.setProduct_code(goodsInfoForAdd.getProduct_code());
			goods.setSku_code(goodsInfoForAdd.getSku_code());
			goods.setSku_num(goodsInfoForAdd.getSku_num());
			goods.setArea_code(goodsInfoForAdd.getArea_code());
			goodsList.add(goods);
		}
		Map<String, List<CouponInfo>> map=couponService.couponList(member_code, shouldPay.toString(),goodsList,getManageCode(),input.getChannelId(),getApiClient().get("app_vision"),paymentType);
		result.setCouponList(map.get("available"));
		result.setDisableCouponList(map.get("disable"));
		
		//可用优惠劵数量
		
		if( null == map.get("available") || map.get("available").isEmpty()){
			result.setCouponCount(0);
		}
		else {
			result.setCouponCount(map.get("available").size());
		}
		
		//不可用优惠劵数量
		
		if( null == map.get("disable") || map.get("disable").isEmpty()){
			result.setDisableCouponCount(0);
		}
		else {
			result.setDisableCouponCount(map.get("disable").size());
		}
		String skuCodes = "";
		for (int i = 0; i < input.getGoods().size(); i++) {
			GoodsInfoForAdd gf = input.getGoods().get(i);
			if("".equals(skuCodes)){
				skuCodes=gf.getSku_code();
			}else {
				skuCodes=skuCodes+","+gf.getSku_code();
			}
		}
		return result;
	}

}
