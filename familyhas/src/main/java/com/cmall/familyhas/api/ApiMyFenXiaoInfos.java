package com.cmall.familyhas.api;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.result.ApiMyFenXiaoInfosResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 我的分销页接口
 * 
 */

public class ApiMyFenXiaoInfos extends RootApiForVersion<ApiMyFenXiaoInfosResult, RootInput> {

	@Override
	public ApiMyFenXiaoInfosResult Process(RootInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiMyFenXiaoInfosResult result = new ApiMyFenXiaoInfosResult();
		String today = DateUtil.getSysDateString();
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_agent_member_info").dataSqlList("select * from fh_agent_member_info where member_code=:member_code order by zid desc", new MDataMap("member_code",getOauthInfo().getUserCode()));
		if(dataSqlList!=null&&dataSqlList.size()>0) {
			Map<String, Object> map2 = dataSqlList.get(0);
			String real_money = map2.get("real_money").toString();
			String pay_money = map2.get("pay_money").toString();	
			int dataCount = DbUp.upTable("fh_agent_withdraw").dataCount("member_code=:member_code and apply_status in('4497484600050001','4497484600050002','4497484600050003','4497484600050004') ",new MDataMap("member_code",getOauthInfo().getUserCode()));
			if(dataCount>0) {
				result.getRealMoney().setHaveApply(true);
			}
			real_money=MoneyHelper.roundHalfUp(new BigDecimal(real_money)).toString();
			pay_money=MoneyHelper.roundHalfUp(new BigDecimal(pay_money)).toString();
			result.getRealMoney().setRealMoney(real_money);
			result.getRealMoney().setPayMoney(pay_money);
			String fans_num = map2.get("fans_num").toString();
			int todayFansNum = DbUp.upTable("fh_agent_member_info").dataCount("parent_code=:parent_code and create_time like '%"+today+"%' ", new MDataMap("parent_code",getOauthInfo().getUserCode()));
		    result.getMyFans().setAllFans(fans_num);
		    result.getMyFans().setTodayFans(todayFansNum+"");
		}
		List<Map<String, Object>> dataSqlList2 = DbUp.upTable("fh_agent_profit_detail").dataSqlList("select * from fh_agent_profit_detail where member_code=:member_code order by zid desc", new MDataMap("member_code",getOauthInfo().getUserCode()));
		if(dataSqlList2!=null&&dataSqlList2.size()>0) {
			BigDecimal todayMoney = BigDecimal.ZERO;
			BigDecimal fxMoney = BigDecimal.ZERO;
			BigDecimal fansMoney = BigDecimal.ZERO;
			for (Map<String, Object> map : dataSqlList2) {
				String createTimeDay = map.get("create_time").toString().substring(0, 10);
				if(today.equals(createTimeDay)&&("4497484600030001".equals(map.get("profit_type").toString()))) {
					//检查今日的收益是否为今日订单中的
					int dataCount = DbUp.upTable("fh_agent_order_detail").dataCount("order_code=:order_code and create_time like '%"+today+"%'", new MDataMap("order_code",map.get("order_code").toString()));
					if(dataCount>0) {
						todayMoney=todayMoney.add(new BigDecimal(map.get("profit").toString()));
					}	
				}
				if("4497484600040001".equals(map.get("profit_source").toString())&&("4497484600030001".equals(map.get("profit_type").toString()))) {
					fxMoney=fxMoney.add(new BigDecimal(map.get("profit").toString()));
				}
				if("4497484600040002".equals(map.get("profit_source").toString())&&("4497484600030001".equals(map.get("profit_type").toString()))) {
					fansMoney=fansMoney.add(new BigDecimal(map.get("profit").toString()));
				}
			}
			todayMoney=MoneyHelper.roundHalfUp(todayMoney);
			fxMoney=MoneyHelper.roundHalfUp(fxMoney);
			fansMoney=MoneyHelper.roundHalfUp(fansMoney);
			result.getPreMoney().setTodayMoney(todayMoney.toString());
			result.getPreMoney().setFxMoney(fxMoney.toString());
			result.getPreMoney().setFsMoney(fansMoney.toString());
		}
		List<Map<String, Object>> dataSqlList3 = DbUp.upTable("fh_agent_order_detail").dataSqlList("select create_time from fh_agent_order_detail where agent_code=:agent_code order by zid desc", new MDataMap("agent_code",getOauthInfo().getUserCode()));
		if(dataSqlList3!=null&&dataSqlList3.size()>0) {
		  int allNum = 0;
		  int todayNum = 0;
		  for (Map<String, Object> map : dataSqlList3) {
			  allNum++;
			 String createTimeDay = map.get("create_time").toString().substring(0, 10);
			 if(today.equals(createTimeDay)) {
				 todayNum++; 
			 }
		}
		  result.getFxOrders().setAllOrders(allNum+"");
		  result.getFxOrders().setTodayOrders(todayNum+"");
		}
		
		
		return result;
	}}
