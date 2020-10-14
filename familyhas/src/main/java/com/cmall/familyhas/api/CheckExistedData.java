package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiCheckExistedDataInput;
import com.cmall.familyhas.api.input.ApiForLiveHomeInput;
import com.cmall.familyhas.api.result.ApiForCheckExistedDataResult;
import com.cmall.familyhas.api.result.ApiForLiveHomeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.dbsupport.DbTemplate;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class CheckExistedData extends RootApiForManage<ApiForCheckExistedDataResult,ApiCheckExistedDataInput>
{

	@Override
	public ApiForCheckExistedDataResult Process(ApiCheckExistedDataInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		
		String uid = inputParam.getUid();
		String sSql="";
		if(StringUtils.isBlank(uid)) {
			sSql= "select attribute_product from pc_product_classify_labels where flag_enable=:flag_enable";
		}
		else {
			sSql= "select attribute_product from pc_product_classify_labels where flag_enable=:flag_enable and uid !='"+uid+"'";
		}
		List<Map<String,Object>> resultList = DbUp.upTable("pc_product_classify_labels").dataSqlList(sSql, new MDataMap("flag_enable","1"));
		ApiForCheckExistedDataResult returnResult = new ApiForCheckExistedDataResult();
		returnResult.setResultList(resultList);
		return returnResult;
	}}
