package com.cmall.familyhas;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class SellerMoveRetentionMoney {

	public static void initSellerMoney(String smallSellerCode){
		if(DbUp.upTable("oc_seller_retention_money").count("small_seller_code", smallSellerCode) > 0){
			return;
		}
		
		MDataMap sellerInfo = DbUp.upTable("uc_seller_info_extend").one("small_seller_code", smallSellerCode);
		if(sellerInfo == null) return;
		
		MDataMap retentionMoney = new MDataMap();
		retentionMoney.put("small_seller_code", smallSellerCode);
		retentionMoney.put("small_seller_name", sellerInfo.get("seller_company_name"));
		retentionMoney.put("settle_type", "4497477900040004");
		retentionMoney.put("max_retention_money", sellerInfo.get("quality_retention_money"));
		retentionMoney.put("money_collection_way", sellerInfo.get("money_collection_way"));
		
		retentionMoney.put("receive_retention_money", "0");
		retentionMoney.put("adjust_retention_money", "0");
		retentionMoney.put("deduct_retention_money", "0");
		retentionMoney.put("retention_money_sum", "0");
		retentionMoney.put("receive_retention_money_date", "");
		retentionMoney.put("adjust_retention_money_date", "");
		
		retentionMoney.put("create_time", FormatHelper.upDateTime());
		retentionMoney.put("creator", "商户平移");
		retentionMoney.put("update_time", "");
		retentionMoney.put("updator", "");
		DbUp.upTable("oc_seller_retention_money").dataInsert(retentionMoney);
		
		System.out.println("success-> "+smallSellerCode);
	}
	
	public static void main(String[] args) throws Exception {
		//initSellerMoney("PF03100202");
		List<String> lines = FileUtils.readLines(new File("C:\\Users\\Administrator\\Desktop\\data.txt"));
		for(String line : lines){
			if(StringUtils.isNotBlank(line)){
				initSellerMoney(StringUtils.trimToEmpty(line));
			}
		}
	}
}
