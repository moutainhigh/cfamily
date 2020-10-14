package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class CSellerInfoService extends BaseClass {

	/**
	 * 获取商户名称
	 * @param small_seller_code
	 * @return
	 */
	public String getSellerName(String small_seller_code){
		
		if(StringUtils.isBlank(small_seller_code)){
			return "";
		}
		
		if(bConfig("familyhas.seller_code_KJT").equals(small_seller_code)){
			return "跨境通";
		}
		
		//不用判断LD 跨境通等，没查到直接反 空即可
		String seller_name=(String)DbUp.upTable("uc_sellerinfo").dataGet("seller_name", "small_seller_code=:small_seller_code", new MDataMap("small_seller_code",small_seller_code));
		
		return seller_name==null?"":seller_name;
	}
	
}
