package com.cmall.familyhas.api;




import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForProductInfoInput;
import com.cmall.familyhas.api.model.SkuBaseInfo;
import com.cmall.familyhas.api.result.ApiForProductInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取商品基本信息
 * 
 * @author ligj
 * time:2015-06-07 12:38
 */
public class ApiForProductInfo extends RootApi<ApiForProductInfoResult,  ApiForProductInfoInput>{

	public ApiForProductInfoResult Process(ApiForProductInfoInput inputParam,
			MDataMap mRequestMap) {
		ApiForProductInfoResult result=new ApiForProductInfoResult();
		String skuCode = inputParam.getProductCode();
		String subsidy = inputParam.getSubsidy();
		String[] subsidyCode = subsidy.split(",");
		if (StringUtils.isEmpty(skuCode)) {
			return result;
		} else {
			//解决前端商品选择框的一个bug,有时会传过来空sku的信息
			//形如:114197,120002,130067,132131,,7016100342,8016410841,6016100200
			skuCode = skuCode.replace(",,", ",");
			if(StringUtils.startsWith(skuCode, ",")) {
				skuCode = StringUtils.substringAfter(skuCode, ",");
			}
			if(StringUtils.endsWith(skuCode, ",")) {
				skuCode = StringUtils.substringBeforeLast(skuCode, ",");
			}
		}
		
		String sWhere = "product_code in ('"+skuCode.replace(",", "','")+"')";
		String sFields = "";
		List<MDataMap> map=DbUp.upTable("pc_productinfo").queryAll(sFields, "", sWhere,null);
		int i = 0;
		for (MDataMap mDataMap : map) {
			SkuBaseInfo si = new SkuBaseInfo();
			if(StringUtils.isBlank(subsidyCode[i])){
				si.setSubsidy("449746250001");
			}else{
				si.setSubsidy(subsidyCode[i]);
			}
			si.setSkuCode(mDataMap.get("product_code"));
			si.setSkuName(mDataMap.get("product_name"));
			result.getSkuInfoList().add(si);
			i += 1;
		}
		return result;
	}
	
}
