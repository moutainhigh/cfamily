package com.cmall.familyhas.api;

import java.util.Map;

import com.cmall.familyhas.api.input.ApiForRedPackageCountInput;
import com.cmall.familyhas.api.result.ApiForRedPackageCountEventResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 获取当前正在进行的抢红包活动编号
 * @author Angel Joy
 *
 */
public class ApiForRedPackageCount extends RootApiForVersion<ApiForRedPackageCountEventResult , ApiForRedPackageCountInput > {

	@Override
	public ApiForRedPackageCountEventResult Process(ApiForRedPackageCountInput inputParam, MDataMap mRequestMap) {
		String eventTypeCode = inputParam.getEventTypeCode();
		ApiForRedPackageCountEventResult result = new ApiForRedPackageCountEventResult();
		String sql = "SELECT * FROM systemcenter.sc_hudong_event_info WHERE begin_time <sysdate() AND end_time > sysdate() AND event_type_code = '"+eventTypeCode+"' AND event_status = '4497472700020002' LIMIT 1";
		Map<String,Object> eventInfo = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sql, null);
		if(eventInfo == null) {
			result.setResultMessage("当前暂无活动");
			result.setResultCode(2);
			result.setEventCode("");
			return result;
		}
		//校验当前是否有奖项
		String eventCode = eventInfo.get("event_code")!=null?eventInfo.get("event_code").toString():"";
		String jxSql = "SELECT * FROM systemcenter.sc_hudong_event_jx_info WHERE event_code = '"+eventCode+"' AND begin_time <= sysdate() AND end_time >= sysdate() AND jx_status = '4497472700020002' limit 1";
		Map<String,Object> jxInfo = DbUp.upTable("sc_hudong_event_jx_info").dataSqlOne(jxSql, null);
		if(jxInfo == null) {
			result.setResultMessage("当前暂无奖项");
			result.setResultCode(2);
			result.setEventCode("");
			return result;
		}
		//校验用户是否登录
		boolean flag = getFlagLogin();
		String timesStr = jxInfo.get("person_limit_num") != null?jxInfo.get("person_limit_num").toString():"0";
		Integer times = Integer.parseInt(timesStr);
		String jxCode = jxInfo.get("jx_code") != null?jxInfo.get("jx_code").toString():"";
		if(flag) {
			MOauthInfo oauthInfo = getOauthInfo();
			String memberCode = oauthInfo.getUserCode();
			//获取用户参与记录
			Integer count = DbUp.upTable("lc_red_package_get_log").count("member_code",memberCode,"jx_code",jxCode);
			times = times - count;
			if(times < 0) {
				times = 0;
			}
		}
		result.setTimes(times);
		result.setEventCode((String)eventInfo.get("event_code"));
		String countDown = eventInfo.get("countdown") != null ? eventInfo.get("countdown").toString():"0";
		result.setCountDown(Integer.parseInt(countDown));
		return result;
	}

}
