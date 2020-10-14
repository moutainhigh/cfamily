package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiGetPropertyForPInput;
import com.cmall.familyhas.api.model.Propertyinfo;
import com.cmall.familyhas.api.result.ApiGetPropertyForPResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.SerializeSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
/**
 * 查询商品属性
 * @author 李国杰
 *、
 */
public class ApiGetPropertyForP extends RootApiForMember<ApiGetPropertyForPResult,ApiGetPropertyForPInput>  {
	
	private final String PROPERTY_TYPE = "449736200004";				//自定义属性
	public ApiGetPropertyForPResult Process(ApiGetPropertyForPInput api,MDataMap mRequestMap) {
		ApiGetPropertyForPResult result = new ApiGetPropertyForPResult();
		String productCode=api.getProductCode();
		// 自定义属性
		MDataMap mWhereMapProperty = new MDataMap();
		mWhereMapProperty.put("property_type", PROPERTY_TYPE);
		mWhereMapProperty.put("product_code", productCode);
		
		List<MDataMap> properties = DbUp.upTable("pc_productproperty")
				.queryAll("", "property_type,small_sort desc,zid asc ", "", mWhereMapProperty);
		
		for (MDataMap mDataMap : properties) {
			Propertyinfo  property = new Propertyinfo();
			if ("内联赠品".equals(mDataMap.get("property_key"))) {
				continue;
			}
			SerializeSupport<Propertyinfo> sSupport = new SerializeSupport<Propertyinfo>();
			sSupport.serialize(mDataMap, property);
			result.getPropertyinfo().add(property);
		}
		result.setProductCode(productCode);
		
		return result;
	}
}
