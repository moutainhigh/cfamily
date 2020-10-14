package com.cmall.familyhas.api;


import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.APiForGetCouponNumInput;
import com.cmall.familyhas.api.result.ApiForGetCouponNumResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;



/** 
* @ClassName: ApiForGetCouponNum 
* @Description: 获取优惠券剩余张数(仅限活动下有一个优惠券类型的情况)
* @author 张海生
* @date 2015-8-25 下午3:46:37 
*  
*/
public class ApiForGetCouponNum extends
	RootApiForManage<ApiForGetCouponNumResult, APiForGetCouponNumInput> {

	public ApiForGetCouponNumResult Process(
			APiForGetCouponNumInput inputParam, MDataMap mRequestMap) {
		ApiForGetCouponNumResult result = new ApiForGetCouponNumResult();
		MDataMap whereMap = new MDataMap();
		whereMap.put("relative_type", inputParam.getReletiveType());
		whereMap.put("manage_code", getManageCode());
		//注册
		String sql = "SELECT ol.activity_code, oc.provide_num FROM oc_coupon_relative ol inner join oc_activity oc" +
				" on ol.activity_code = oc.activity_code and oc.flag=1 and now() <= end_time where ol.relative_type =:relative_type and manage_code=:manage_code";
		Map<String, Object> ocMap = DbUp.upTable("oc_activity").dataSqlOne(sql, whereMap);
		//推荐
	/*	whereMap.put("relative_type", "7");
		String sql2 = "SELECT ol.activity_code, oc.provide_num FROM oc_coupon_relative ol inner join oc_activity oc" +
				" on ol.activity_code = oc.activity_code and oc.flag=1 and now() <= end_time where ol.relative_type =:relative_type and manage_code=:manage_code";*/
		/*Map<String, Object> ocMap2 = DbUp.upTable("oc_activity").dataSqlOne(sql, whereMap);*/
		
		if(ocMap != null){
			//注册
			String sSql = "select coupon_type_code,money,money_type from oc_coupon_type where activity_code=:activity_code"
					+ " and status=:status  and (valid_type= 4497471600080001 or (valid_type= 4497471600080002 and now() < end_time))";
			List<Map<String, Object>> couponTypeList = DbUp
					.upTable("oc_coupon_type").dataSqlList(
							sSql,
							new MDataMap("activity_code", 
									(String) ocMap.get("activity_code"), "status",
									"4497469400030002"));
			//推荐
	/*		String sSql2 = "select coupon_type_code,money,money_type from oc_coupon_type where activity_code=:activity_code"
					+ " and status=:status  and (valid_type= 4497471600080001 or (valid_type= 4497471600080002 and now() < end_time))";
			List<Map<String, Object>> couponTypeList2 = DbUp
					.upTable("oc_coupon_type").dataSqlList(
							sSql,
							new MDataMap("activity_code", 
									(String) ocMap2.get("activity_code"), "status",
									"4497469400030002"));*/
		
			if(couponTypeList != null && couponTypeList.size() > 0){
				int count = DbUp.upTable("oc_coupon_provide").count(
						"coupon_type_code",
						(String)couponTypeList.get(0).get("coupon_type_code"));//已发放的注册优惠券个数
				
			/*	int count2 = DbUp.upTable("oc_coupon_provide").count(
						"coupon_type_code",
						(String)couponTypeList2.get(0).get("coupon_type_code"));//已发放的推荐优惠券个数
*/				
				int couponNum = Integer.parseInt(ocMap.get("provide_num").toString()) - count;
				/*int couponNum2 = Integer.parseInt(ocMap2.get("provide_num").toString()) - count2;*/
				result.setCouponNum(couponNum<=0?0:1);
				int total = 0;//返券金额
				int sum = 0;   //折扣券
				
				if(couponTypeList !=null && couponTypeList.size() > 0){
					for (Map<String, Object> cmap : couponTypeList) {
						if("449748120002".equals(cmap.get("money_type"))) {
							sum++;
						}
						else {
							total += Integer.parseInt(cmap.get("money").toString());
						}
							
					}
				}
				result.setMoney(total);
				result.setMany(sum);
			}
		}else{
			result.setCouponNum(0);
		}
		return result;
	}

	
}
