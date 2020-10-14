package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.input.ApiGetSellerCategoryInput;
import com.cmall.familyhas.api.model.SellerCategory;
import com.cmall.familyhas.api.result.ApiGetSellerCategoryResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.SerializeSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
/**
 * 获取虚类子类信息
 * @author 李国杰
 *
 */
public class ApiGetSellerCategory extends RootApiForMember<ApiGetSellerCategoryResult,ApiGetSellerCategoryInput>  {

	public ApiGetSellerCategoryResult Process(ApiGetSellerCategoryInput api,MDataMap mRequestMap) {
		ApiGetSellerCategoryResult result = new ApiGetSellerCategoryResult();
		String categoryCode=api.getParentCategoryCode();
		String sellerCode=	bConfig("familyhas.app_code");		//获取appCode
		
		List<SellerCategory> categoryList = new ArrayList<SellerCategory>();
		List<MDataMap> categoryMapList = DbUp.upTable("uc_sellercategory").queryByWhere("parent_code",categoryCode,"seller_code",sellerCode);
		for (MDataMap mDataMap : categoryMapList) {
			SellerCategory sellerCategory = new SellerCategory();
			SerializeSupport<SellerCategory> sSupport = new SerializeSupport<SellerCategory>();
			sSupport.serialize(mDataMap, sellerCategory);
			categoryList.add(sellerCategory);
		}
		result.setCategory(categoryList);
		return result;
	}
}
