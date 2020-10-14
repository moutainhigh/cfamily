package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiGetADCategoryInput;
import com.cmall.familyhas.api.result.ApiGetADCategoryResult;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetADCategory extends RootApiForManage<ApiGetADCategoryResult, ApiGetADCategoryInput>{

	public ApiGetADCategoryResult Process(ApiGetADCategoryInput inputParam,
			MDataMap mRequestMap) {
		
		ApiGetADCategoryResult result = new ApiGetADCategoryResult();
		List<MDataMap> mdate = DbUp.upTable("fh_category").query("category_code,link_address,product_code,product_name,link_url,product_link,line_head",
				"", "category_code=:category_code and manage_code=:manageCode", new MDataMap("category_code",inputParam.getCategory_code(),"manageCode",getManageCode()), 0, 1);
		if(mdate.size() > 0) {
			MDataMap mDataMap = mdate.get(0);
			result.setCategory_code(mDataMap.get("category_code"));
			result.setLink_address(mDataMap.get("link_address"));
			result.setProduct_code(mDataMap.get("product_code"));
			result.setProduct_name(mDataMap.get("product_name"));
			result.setLink_url(mDataMap.get("link_url"));
			result.setProduct_link(mDataMap.get("product_link"));
			result.setLine_head(mDataMap.get("line_head"));
			if (new ProductService().checkProductKjt(mDataMap.get("product_code"))) {
				result.setFlagTheSea("1");
			}
		} else {
			result.setCategory_code(inputParam.getCategory_code());
			result.setResultCode(0);
			result.setResultMessage("没有栏目信息");
		}
		return result;
	}

}
