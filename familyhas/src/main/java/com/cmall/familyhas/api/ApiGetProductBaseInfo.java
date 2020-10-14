package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetProductBaseInfoInput;
import com.cmall.familyhas.api.model.ProductPic;
import com.cmall.familyhas.api.result.ApiGetProductBaseInfoResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 家有调用的商品信息接口
 * 
 */
public class ApiGetProductBaseInfo extends
		RootApiForMember<ApiGetProductBaseInfoResult, ApiGetProductBaseInfoInput> {
	
	public ApiGetProductBaseInfoResult Process(ApiGetProductBaseInfoInput api,
			MDataMap mRequestMap) {
		ApiGetProductBaseInfoResult result = new ApiGetProductBaseInfoResult();
		List<ProductPic> productPic = new ArrayList<ProductPic>();
		List<String> productCodes = api.getProductCodes();
		
		if (null == productCodes) {
			return result;
		}
		String LDProductCodeStr = StringUtils.join(productCodes,"','");
		List<MDataMap> productCodeMapList = DbUp.upTable("pc_productinfo").queryAll("product_code,product_code_old", "", "product_code_old in ('"+LDProductCodeStr+"') and seller_code='SI2003' and small_seller_code='SI2003'", null);
	
		if (null != productCodeMapList && !productCodeMapList.isEmpty()) {
			LoadProductInfo loadProductInfo = new LoadProductInfo();
			for (MDataMap mDataMap : productCodeMapList) {
				
				// 获取商品信息
				PlusModelProductInfo plusModelProductinfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(mDataMap.get("product_code")));
				if (null == plusModelProductinfo || null == plusModelProductinfo.getProductCode()
						|| "".equals(plusModelProductinfo.getProductCode())) {
					continue;
				}
				ProductPic pic = new ProductPic();
				
				pic.setProductCode(mDataMap.get("product_code_old"));
				pic.setMainpicUrl(plusModelProductinfo.getMainpicUrl());
				pic.setProductStatus(plusModelProductinfo.getProductStatus());
				
				if (null != plusModelProductinfo.getDescription()) {
					String[] descriptionPicArr = plusModelProductinfo.getDescription().getDescriptionPic().split("\\|");
					for (String descriptionPic : descriptionPicArr) {	//描述图片
						pic.getDiscriptPicList().add(descriptionPic);
					}
				}
				if (null != plusModelProductinfo.getPcPicList()) {
					for (String pcPic : plusModelProductinfo.getPcPicList()) {	// 轮播图
						pic.getPcPicList().add(pcPic);
					}
				}
				productPic.add(pic);
			}
		}
		result.setProductPic(productPic);
		return result;
	}

}
