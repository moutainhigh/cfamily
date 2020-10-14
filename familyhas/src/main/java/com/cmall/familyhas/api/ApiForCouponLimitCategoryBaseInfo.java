package com.cmall.familyhas.api;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForCouponLimitCategoryBaseInfoInput;
import com.cmall.familyhas.api.result.ApiForCouponLimitCategoryBaseInfoResult;
import com.cmall.ordercenter.model.CategoryBaseInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取优惠券类型限制条件中的分类信息
 * 
 * @author ligj
 * time:2015-06-06 15:34:00
 */
public class ApiForCouponLimitCategoryBaseInfo extends 
			RootApi< ApiForCouponLimitCategoryBaseInfoResult,  ApiForCouponLimitCategoryBaseInfoInput>{
	
	public ApiForCouponLimitCategoryBaseInfoResult Process(ApiForCouponLimitCategoryBaseInfoInput inputParam,
			MDataMap mResquestMap){
		ApiForCouponLimitCategoryBaseInfoResult result=new ApiForCouponLimitCategoryBaseInfoResult();
		List<String> categoryCodeArr = inputParam.getCategoryCodeArr();
		if (null == categoryCodeArr || categoryCodeArr.size() <= 0) {
			return result;
		}
		
		Map<String,MDataMap> categoryObjMap = new HashMap<String, MDataMap>();
		List<MDataMap> listCategory=DbUp.upTable("uc_sellercategory").queryAll("", "", " seller_code=:seller_code ", new MDataMap("seller_code",inputParam.getSellerCode()));
		for (MDataMap mDataMap : listCategory) {
			categoryObjMap.put(mDataMap.get("category_code"), mDataMap);
		}
		for (String categoryCode : categoryCodeArr) {
			MDataMap categoryMap = categoryObjMap.get(categoryCode);
			if(categoryMap!=null && !categoryMap.isEmpty()){//防止变态的修改数据库
				CategoryBaseInfo categoryInfo = new CategoryBaseInfo();
				categoryInfo.setCategoryCode(categoryMap.get("category_code"));
				categoryInfo.setParentCode(categoryMap.get("parent_code"));
				categoryInfo.setSort(categoryMap.get("sort"));
				
				if ("3".equals(categoryMap.get("level"))) {
					MDataMap parentCategoryMap = categoryObjMap.get(categoryMap.get("parent_code"));  	//上一级别的分类信息
					categoryInfo.setCategoryName(parentCategoryMap.get("category_name")+"->"+categoryMap.get("category_name"));
				}else if ("4".equals(categoryMap.get("level"))) {
					MDataMap parentCategoryMap = categoryObjMap.get(categoryMap.get("parent_code"));  				//上一级别的分类信息
					MDataMap superParentCategoryMap = categoryObjMap.get(parentCategoryMap.get("parent_code"));  	//第一级别的分类信息
					categoryInfo.setCategoryName(superParentCategoryMap.get("category_name")+"->"+parentCategoryMap.get("category_name")+"->"+categoryMap.get("category_name"));
				}else{
					categoryInfo.setCategoryName(categoryMap.get("category_name"));
				}
				result.getCategoryInfoList().add(categoryInfo);
			}
		}
		return result;
	}
}
