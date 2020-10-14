package com.cmall.familyhas.api;


import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.cmall.familyhas.api.input.APiForCheckOldUserInput;
import com.cmall.familyhas.api.result.ApiForCheckOldUserResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;


/** 
* @ClassName: ApiForCheckOldUser 
* @Description: 查询来用户领过的优惠券
* @author 张海生
* @date 2015-8-13 下午4:48:14 
*  
*/
public class ApiForCheckOldUser extends
	RootApiForManage<ApiForCheckOldUserResult, APiForCheckOldUserInput> {

	public ApiForCheckOldUserResult Process(
			APiForCheckOldUserInput inputParam, MDataMap mRequestMap) {
		ApiForCheckOldUserResult result = new ApiForCheckOldUserResult();
		String mobile = inputParam.getMobile();
		String sql = "SELECT ol.activity_code, op.coupon_type_code,op.money,op.start_time,op.end_time,op.valid_type,op.valid_day" +
				" FROM oc_coupon_relative  ol inner join oc_coupon_type op on ol.activity_code = op.activity_code" +
				" and op.status='4497469400030002' where ol.relative_type = '1' and ol.manage_code=:manage_code";
		Map<String, Object> couponTypeMap =  DbUp.upTable("oc_coupon_type").dataSqlOne(sql, new MDataMap("manage_code", getManageCode()));
		if(couponTypeMap != null){
			MDataMap provideMap = DbUp.upTable("oc_coupon_provide").oneWhere(
					"create_time", "", "", "coupon_type_code",
					(String) couponTypeMap.get("coupon_type_code"), "mobile",
					mobile);// 查询用户是否已经领过券
			if (provideMap != null) {
				String msql = "SELECT ol.activity_code, sum(op.money) total_money" +
						" FROM oc_coupon_relative  ol inner join oc_coupon_type op on ol.activity_code = op.activity_code" +
						" and op.status='4497469400030002' where ol.relative_type = '1' and ol.manage_code=:manage_code";
				Map<String, Object> moneyMap =  DbUp.upTable("oc_coupon_type").dataSqlOne(msql, new MDataMap("manage_code", getManageCode()));
				result.setMoney(moneyMap.get("total_money").toString());//面额
				if("4497471600080002".equals((String)couponTypeMap.get("valid_type"))){//有效类型为日期范围
					result.setStartTime((String)couponTypeMap.get("start_time"));
					result.setEndTime((String)couponTypeMap.get("end_time"));
				}else if("4497471600080001".equals((String)couponTypeMap.get("valid_type"))){//有效类型为天数
					String createTime = provideMap.get("create_time");
					result.setStartTime(createTime);
					try {
						Date d1 = DateUtil.convertToDate(createTime, DateUtil.DATE_FORMAT_DATETIME);
						Date d2 = DateUtil.addDays(d1, Integer.valueOf(couponTypeMap.get("valid_day").toString()));
						result.setEndTime(DateUtil.toString(d2, DateUtil.DATE_FORMAT_DATETIME));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				result.inErrorMessage(916421259);// 用户已经领取过优惠券
			}else{
				result.inErrorMessage(916421260);// 老用户不能领取优惠券
			}
		}else{
			result.setResultCode(0);
			result.setResultMessage("没有优惠券信息");
		}
		return result;
	}

	
}
