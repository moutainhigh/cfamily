package com.cmall.familyhas.api;




import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForCouponLimitProductBaseInfoInput;
import com.cmall.familyhas.api.result.ApiForCouponLimitProductBaseInfoResult;
import com.cmall.ordercenter.model.ProductBaseInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取优惠券类型限制条件中的商品信息
 * 
 * @author ligj
 * time:2015-06-04 21:26
 */
public class ApiForCouponLimitProductBaseInfo extends 
			RootApi< ApiForCouponLimitProductBaseInfoResult,  ApiForCouponLimitProductBaseInfoInput>{
	
	public ApiForCouponLimitProductBaseInfoResult Process(ApiForCouponLimitProductBaseInfoInput inputParam,
			MDataMap mResquestMap){
		ApiForCouponLimitProductBaseInfoResult result=new ApiForCouponLimitProductBaseInfoResult();
		String productCodeStr = inputParam.getProductCodeArr();
		if (StringUtils.isEmpty(productCodeStr)) {
			return result;
		}
		
		String sWhere = "product_code in ('"+productCodeStr.replace(",", "','")+"')";
		String sFields = "product_code,product_name";
		List<MDataMap> map=DbUp.upTable("pc_productinfo").queryAll(sFields, "", sWhere,null);
		for (MDataMap mDataMap : map) {
			ProductBaseInfo pInfo = new ProductBaseInfo();
			pInfo.setProductCode(mDataMap.get("product_code"));
			pInfo.setProductName(mDataMap.get("product_name"));
			result.getProductInfoList().add(pInfo);
		}
		return result;
	}
}
