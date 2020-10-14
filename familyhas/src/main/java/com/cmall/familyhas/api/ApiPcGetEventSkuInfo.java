package com.cmall.familyhas.api;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.model.Category;
import com.cmall.familyhas.api.result.ApiGetSkuInfoResult;
import com.cmall.familyhas.api.result.ApiPcGetSkuInfoResult;
import com.cmall.familyhas.help.ProductCategoryHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * PC版商品基本信息
 * @author dyc
 * */
public class ApiPcGetEventSkuInfo extends RootApiForMember<ApiPcGetSkuInfoResult, ApiGetSkuInfoInput> {

	public ApiPcGetSkuInfoResult Process(ApiGetSkuInfoInput inputParam,
			MDataMap mRequestMap) {
		ApiPcGetSkuInfoResult re = new ApiPcGetSkuInfoResult();
	
		ApiGetEventSkuInfo api = new ApiGetEventSkuInfo();
		api.checkAndInit(inputParam, mRequestMap);
		//调用APP商品详情接口
		ApiGetSkuInfoResult result = api.Process(inputParam, mRequestMap);
		clone(result, re);
		//查询商品分类信息
		List<Map<String, Object>> categoryList =  new ProductCategoryHelper().getProductNavigate(inputParam.getProductCode());
		if(categoryList!=null){
			for (int i = categoryList.size()-1; i >=0; i--) {
				Map<String, Object> map = categoryList.get(i);
				Category cate = new Category();
				cate.setCategoryCode(map.get("category_code").toString());
				cate.setCategoryName(map.get("category_name").toString());
				re.getCategoryList().add(cate);
			}			
		}
		return re;
	}

	private void clone(ApiGetSkuInfoResult a,ApiPcGetSkuInfoResult b) {
		Class<?> type = a.getClass();
		try {
			do {
				for (Field f : type.getDeclaredFields()) {
					String fieldName = f.getName();
					String firstCharUpper =  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					String getFieldName = "get" + firstCharUpper;
					String setFieldName = "set" + firstCharUpper;
//					System.out.println(getFieldName + "  " + setFieldName);
					Object date = type.getMethod(getFieldName).invoke(a);					
					if((date instanceof Integer)&&(!fieldName.equals("flagSale"))&&(!fieldName.equals("commentSumCounts"))){
						type.getMethod(setFieldName,Integer.TYPE).invoke(b,date);
					}else if((date instanceof List)){
						type.getMethod(setFieldName,List.class).invoke(b,date);
					}else if((date instanceof Map)){
						type.getMethod(setFieldName,Map.class).invoke(b,date);
					}else{
						type.getMethod(setFieldName,date.getClass()).invoke(b,date);
					}							
				}
				type = type.getSuperclass();
			} while (null != type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
