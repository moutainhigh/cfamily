package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetSellerCategoryByCodeInput;
import com.cmall.familyhas.api.result.ApiGetSellerCategoryByCodeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/** 
* @ClassName: ApiGetSellerCategoryByCode 
* @Description: 根据分类code获取分类名称
* @author 张海生
* @date 2015-3-16 上午9:49:48 
*  
*/
public class ApiGetSellerCategoryByCode
		extends
		RootApi<ApiGetSellerCategoryByCodeResult, ApiGetSellerCategoryByCodeInput> {

	public ApiGetSellerCategoryByCodeResult Process(
			ApiGetSellerCategoryByCodeInput api, MDataMap mRequestMap) {
		ApiGetSellerCategoryByCodeResult result = new ApiGetSellerCategoryByCodeResult();
		String categoryCode = api.getCategoryCode();
		String sellerCode=	bConfig("familyhas.app_code");		//获取appCode
		MDataMap md = DbUp.upTable("uc_sellercategory").oneWhere(
				"category_name", null, null, "category_code", categoryCode,
				"seller_code", sellerCode);
		if(md != null && StringUtils.isNotEmpty(md.get("category_name"))){
			result.getCategory().setCategoryName(md.get("category_name"));
		}else{
			result.getCategory().setCategoryName("");
		}
		return result;
	}
}
