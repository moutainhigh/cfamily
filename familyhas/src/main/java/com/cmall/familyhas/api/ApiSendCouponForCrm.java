package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.SendCouponInput;
import com.cmall.ordercenter.alipay.util.JsonUtil;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

import net.sf.json.util.JSONUtils;

public class ApiSendCouponForCrm extends RootApi<RootResult,SendCouponInput>{

	@Override
	public RootResult Process(SendCouponInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult msgSendResult = new RootResult();
		CouponUtil couponUtil = new CouponUtil();
		String[] codes = inputParam.getMemberCodes().split(",");
		Map<String, Object> failMap = new HashMap<String, Object>();
		if(codes.length>0){
			String sql = "SELECT * from oc_coupon_type o INNER JOIN oc_activity t ON o.activity_code = t.activity_code where o.activity_code = '"+inputParam.getActivityCode()+"' AND o.`status` = '4497469400030002' AND NOW() BETWEEN t.begin_time AND t.end_time AND t.flag = '1'"+
					" AND ((NOW() BETWEEN o.start_time AND o.end_time) OR (o.start_time = '' AND o.end_time = ''))";
			List<Map<String, Object>> list = DbUp.upTable("oc_coupon_type").dataSqlList(sql, null);
			if(list.size()<=0){
				msgSendResult.setResultCode(0);
				msgSendResult.setResultMessage("该活动下没有可发放的优惠券");
				return msgSendResult;
			}			
			for(Map<String, Object> map:list){
				for(String memberCode:codes){
					try {
						msgSendResult = couponUtil.provideCoupon(memberCode, map.get("coupon_type_code").toString(), "0", "","",1,inputParam.getOperate_id());
						if(msgSendResult.getResultCode()!=1){
							failMap.put(memberCode, msgSendResult.getResultMessage());
						}
					} catch (Exception e) {
						failMap.put(memberCode, e.getMessage());
						continue;
					}
				}				
			}
		}else{
			msgSendResult.setResultCode(0);
			msgSendResult.setResultMessage("用户编码不能为空");
			return msgSendResult;
		}
		
		if(failMap.size()>0){
			msgSendResult.setResultMessage("以下用户发放失败："+new JSONObject(failMap).toString());
		}
		return msgSendResult;
	}
	
	

}
