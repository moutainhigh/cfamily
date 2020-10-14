package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiChargeConfigAdd extends RootFunc {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String zw_f_seller_type = mDataMap.get("zw_f_seller_type");   // 商户类型
		String zw_f_charge_type = mDataMap.get("zw_f_charge_type");   // 商品分类
		String zw_f_charge_name = mDataMap.get("zw_f_charge_name");   // 佣金类型
		String zw_f_charge_ratio = mDataMap.get("zw_f_charge_ratio"); // 佣金百分比
		String zw_f_profit_ratio = mDataMap.get("zw_f_profit_ratio"); // 利润百分比
		
		String createTime = DateUtil.getNowTime();
		String user = UserFactory.INSTANCE.create().getRealName();
		MWebResult mResult = new MWebResult();
		int failureCount = 0;
		String[] sellerTypes = zw_f_seller_type.split(",");
		String[] chargeTypes = zw_f_charge_type.split(",");
		String[] chargeNames = zw_f_charge_name.split(",");
		String[] chargeRatios = zw_f_charge_ratio.split(",");
		String[] profitRatios = zw_f_profit_ratio.split(",");
		if(chargeTypes.length == chargeNames.length && chargeNames.length == chargeRatios.length && chargeRatios.length == sellerTypes.length && sellerTypes.length == profitRatios.length) {
			for(int i = 0; i < chargeTypes.length; i ++) {
				String sellerType = sellerTypes[i];
				String chargeType = chargeTypes[i];
				String chargeName = chargeNames[i];
				String chargeRatio = chargeRatios[i];
				String profitRatio = profitRatios[i];
				
				if("0".equals(chargeType)) {
					//全部类型
					MDataMap chargeNameMap = DbUp.upTable("sc_define").one("define_code", chargeName);
					List<Map<String, Object>> categoryList = DbUp.upTable("uc_sellercategory").listByWhere("seller_code", "SI2003", "flaginable", "449746250001", "level", "2");
					for(Map<String, Object> category : categoryList) {
						int count = DbUp.upTable("fh_bf_charge_config").count("charge_type", category.get("category_code").toString(), "charge_name", chargeName, "seller_type", sellerType);
						if(count <= 0) {
							DbUp.upTable("fh_bf_charge_config").insert("charge_type", category.get("category_code").toString(), "charge_name", chargeName, "charge_ratio", chargeRatio, "seller_type", 
									sellerType, "profit_ratio", profitRatio);
							
							//记录缤纷利润佣金操作日志
							DbUp.upTable("fh_bf_charge_log").insert("operate_type", "新增", "good_type", category.get("category_name").toString(), "charge_name", chargeNameMap.get("define_name"), "change_ratio", 
									chargeRatio, "operator", user, "operate_time", createTime, "seller_type", sellerType, "change_profit", profitRatio);
						}
					}
					
				}else {
					int count = DbUp.upTable("fh_bf_charge_config").count("charge_type", chargeType, "charge_name", chargeName, "seller_type", sellerType);
					if(count <= 0) {
						DbUp.upTable("fh_bf_charge_config").insert("charge_type", chargeType, "charge_name", chargeName, "charge_ratio", chargeRatio, "seller_type", sellerType, "profit_ratio", profitRatio);
						
						//记录缤纷利润佣金操作日志
						MDataMap chargeNameMap = DbUp.upTable("sc_define").one("define_code", chargeName);
						MDataMap chargeTypeMap = DbUp.upTable("uc_sellercategory").one("category_code", chargeType, "seller_code", "SI2003", "flaginable", "449746250001", "level", "2");
						DbUp.upTable("fh_bf_charge_log").insert("operate_type", "新增", "good_type", chargeTypeMap.get("category_name"), "charge_name", chargeNameMap.get("define_name"), "change_ratio", 
								chargeRatio, "operator", user, "operate_time", createTime, "seller_type", sellerType, "change_profit", profitRatio);
					}else {
						failureCount += 1;
					}
				}
				
				/**
				int count = DbUp.upTable("fh_bf_charge_config").count("charge_type", chargeType, "charge_name", chargeName);
				if(count <= 0) {
					DbUp.upTable("fh_bf_charge_config").insert("charge_type", chargeType, "charge_name", chargeName, "charge_ratio", chargeRatio);
					
					//记录缤纷利润佣金操作日志
					String createTime = DateUtil.getNowTime();
					String user = UserFactory.INSTANCE.create().getRealName();
					MDataMap chargeNameMap = DbUp.upTable("sc_define").one("define_code", chargeName);
					MDataMap chargeTypeMap = DbUp.upTable("uc_sellercategory").one("category_code", chargeType, "seller_code", "SI2003", "flaginable", "449746250001", "level", "2");
					DbUp.upTable("fh_bf_charge_log").insert("operate_type", "新增", "good_type", chargeTypeMap.get("category_name"), "charge_name", chargeNameMap.get("define_name"), "change_ratio", chargeRatio, "operator", user, "operate_time", createTime);
				}else {
					failureCount += 1;
				}
				**/
			}
			
			String msg = "保存成功";
			if(failureCount > 0) {
				msg += "，失败" + failureCount + "条。";
			}else {
				msg += "。";
			}
			mResult.setResultCode(0);
			mResult.setResultMessage(msg);
		}else {
			mResult.setResultCode(0);
			mResult.setResultMessage("填写信息不合规范!");
		}
		return mResult;
	}
}
