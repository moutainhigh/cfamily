package com.cmall.familyhas.api;




import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForCouponLimitBrandBaseInfoInput;
import com.cmall.familyhas.api.result.ApiForCouponLimitBrandBaseInfoResult;
import com.cmall.ordercenter.model.BrandBaseInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取优惠券类型限制条件中的品牌信息
 * 
 * @author ligj
 * time:2015-06-07 12:38
 */
public class ApiForCouponLimitBrandBaseInfo extends 
			RootApi< ApiForCouponLimitBrandBaseInfoResult,  ApiForCouponLimitBrandBaseInfoInput>{
	
	public ApiForCouponLimitBrandBaseInfoResult Process(ApiForCouponLimitBrandBaseInfoInput inputParam,
			MDataMap mResquestMap){
		ApiForCouponLimitBrandBaseInfoResult result=new ApiForCouponLimitBrandBaseInfoResult();
		String brandCodeStr = inputParam.getBrandCodeArr();
		if (StringUtils.isEmpty(brandCodeStr)) {
			return result;
		}
		
		String sWhere = "brand_code in ('"+brandCodeStr.replace(",", "','")+"')";
		String sFields = "";
		List<MDataMap> map=DbUp.upTable("pc_brandinfo").queryAll(sFields, "", sWhere,null);
		for (MDataMap mDataMap : map) {
			BrandBaseInfo bInfo = new BrandBaseInfo();
			bInfo.setBrandCode(mDataMap.get("brand_code"));
			bInfo.setBrandZNName(mDataMap.get("brand_name"));
			bInfo.setBrandUNName(mDataMap.get("brand_name_en"));
			bInfo.setBrandPic(mDataMap.get("brand_pic"));
			result.getBrandInfoList().add(bInfo);
		}
		return result;
	}
}
