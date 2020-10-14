package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForCutCakeGetDrawNumInput;
import com.cmall.familyhas.api.result.ApiForCutCakeGetDrawNumResult;
import com.cmall.groupcenter.util.HttpUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 获取用户剩余切蛋糕次数接口
 * @author lgx
 *
 */
public class ApiForCutCakeGetDrawNum extends RootApiForToken<ApiForCutCakeGetDrawNumResult, ApiForCutCakeGetDrawNumInput> {

	@Override
	public ApiForCutCakeGetDrawNumResult Process(ApiForCutCakeGetDrawNumInput inputParam, MDataMap mRequestMap) {
		
		ApiForCutCakeGetDrawNumResult result = new ApiForCutCakeGetDrawNumResult();
		
		int cutCakeNum = 0;
		String canAddNum = "1";
		String isBlessing = "0";
		
		String nowTime = FormatHelper.upDateTime();
		String startTime = nowTime.substring(0, 10)+" 00:00:00";
		String endTime = nowTime.substring(0, 10)+" 23:59:59";
		String eventCode = "";
		String memberCode = getUserCode();
		
		// 查询当前时间段内已经发布状态的切蛋糕活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210011' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的切蛋糕活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 获取登录用户手机号
		String loginSql = "SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003' ORDER BY create_time DESC LIMIT 1";
		Map<String, Object> loginInfoMap = DbUp.upTable("mc_login_info").dataSqlOne(loginSql, new MDataMap());
		if(loginInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("用户信息有误!");
			return result;
		}
		String login_name = (String) loginInfoMap.get("login_name");
		// 根据手机号查询该用户今天在LD支付成功订单数
		int ldOrderCount = 0;
		String url = bConfig("groupcenter.rsync_homehas_url")+"getCutCakeNum";
		Map<String, Object> postParams = new HashMap<String, Object>();
		postParams.put("tel", login_name);
		String postResult = HttpUtil.post(url, JSONArray.toJSONString(postParams), "UTF-8");
		JSONObject jo = JSONObject.parseObject(postResult);
		if((boolean) jo.get("success")){			
			ldOrderCount = (int) jo.get("result");
		}
		
		// 查询该用户在惠家有支付成功订单数
		String orderCountSql = "SELECT count(1) num FROM ( " + 
				" SELECT o1.zid  FROM oc_orderinfo o1 WHERE buyer_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"' " + 
				"	AND order_status in ('4497153900010002','4497153900010003','4497153900010004','4497153900010005') AND small_seller_code != 'SI2003' " + 
				" UNION ALL " + 
				" SELECT o2.zid  FROM oc_orderinfo o2 WHERE buyer_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"' " + 
				"	AND order_status in ('4497153900010002','4497153900010003','4497153900010004','4497153900010005') AND small_seller_code = 'SI2003' AND out_order_code = ''" + 
				") u";
		Map<String, Object> orderCountMap = DbUp.upTable("oc_orderinfo").dataSqlOne(orderCountSql, new MDataMap());
		int orderCount = 0;
		if(orderCountMap != null) {
			orderCount = MapUtils.getIntValue(orderCountMap, "num");
		}
		
		// 查询当天是否已经发表过祝福且没有抽过该奖品,如果已经发表过祝福且没抽过奖,则赠送免费抽奖次数
		int blessCount = 0;
		String blessSql = "SELECT * FROM sc_hudong_cake_blessing WHERE member_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"'";
		Map<String, Object> cake_blessing = DbUp.upTable("sc_hudong_cake_blessing").dataSqlOne(blessSql, new MDataMap());
		if(cake_blessing != null) {
			// 查到祝福,返回今日已经送祝福
			isBlessing = "1";
			// 今日获得免费次数
			blessCount = 1;
		}
		
		// 获取切蛋糕配置次数
		// 每天免费次数
		MDataMap cake_config1 = DbUp.upTable("sc_hudong_cake_config").one("type","1");
		int num1 = MapUtils.getIntValue(cake_config1,"num");
		// 每天下单可获得次数
		MDataMap cake_config2 = DbUp.upTable("sc_hudong_cake_config").one("type","2");
		int num2 = MapUtils.getIntValue(cake_config2,"num");
		
		// 今日切过几次蛋糕
		String drawCountsql = "SELECT count(1) num FROM sc_hudong_cake_draw_jl WHERE event_code = '"+eventCode+"' AND member_code = '"+memberCode+"' "
				+ " AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"'";
		Map<String, Object> drawCountMap = DbUp.upTable("sc_hudong_cake_draw_jl").dataSqlOne(drawCountsql, new MDataMap());
		int drawCount = 0;
		if(drawCountMap != null) {
			drawCount = MapUtils.getIntValue(drawCountMap, "num");
		}
		
		// 计算可切蛋糕次数:(LD那边接口返回有效订单数(根据手机号查) + 惠家有有效订单数(small_seller_code不是SI2003的) + 是否有免费机会(最小为0,最大为5)) - 已经切过的次数 
		int count = ldOrderCount + orderCount;
		if(count > num2) {
			count = num2;
		}
		cutCakeNum = count + blessCount - drawCount;
		if(cutCakeNum < 0) {
			cutCakeNum = 0;
		}
		
		// 判断用户是否还能下单获得切蛋糕次数
		if(cake_blessing != null) {
			// 获得免费次数
			if((cutCakeNum + drawCount) >= (num1 + num2)) {
				canAddNum = "0";
			}else {
				canAddNum = "1";
			}
		}else { 
			// 没有免费抽奖机会
			if((cutCakeNum + drawCount) >= num2) {
				canAddNum = "0";
			}else {
				canAddNum = "1";
			}
		}
		
		result.setCanAddNum(canAddNum);
		result.setCutCakeNum(cutCakeNum);
		result.setIsBlessing(isBlessing);
		
		return result;
	}

}
