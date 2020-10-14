package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBigWheelInviteSuccessInput;
import com.cmall.familyhas.api.result.ApiForBigWheelInviteSuccessResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 大转盘-邀请成功接口
 * @author lgx
 *
 */
public class ApiForBigWheelInviteSuccess extends RootApiForVersion<ApiForBigWheelInviteSuccessResult, ApiForBigWheelInviteSuccessInput> {

	// 大转盘初始化抽奖次数
	private final String BigWheelOriginalDrawNum = bConfig("familyhas.bigWheelOriginalDrawNum");
	// 大转盘分享获得最大抽奖次数
	private final String BigWheelShareDrawNum = bConfig("familyhas.bigWheelShareDrawNum");
	
	
	public ApiForBigWheelInviteSuccessResult Process(ApiForBigWheelInviteSuccessInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelInviteSuccessResult result = new ApiForBigWheelInviteSuccessResult();
		// 最大邀请人数
		int sumYq = 2 * Integer.parseInt(BigWheelShareDrawNum);
		String nowTime = FormatHelper.upDateTime();
		String startTime = nowTime.substring(0, 10)+" 00:00:00";
		String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		String eventCode = inputParam.getEventCode(); // 活动编号
		String memberCodeYqr = inputParam.getMemberCodeYqr(); // 邀请人用户编号
		String memberCodeByqr = getOauthInfo().getUserCode(); // 被邀请人用户编号
		
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_code = '"+eventCode+"' AND event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("该福利转盘活动已失效!");
			return result;
		}
		
		if(StringUtils.isEmpty(memberCodeYqr) || StringUtils.isEmpty(memberCodeByqr)) {
			result.setResultCode(-1);
			result.setResultMessage("邀请人和被邀请人都不能为空!");
			return result;
		}
		if(memberCodeYqr.equals(memberCodeByqr)) {
			result.setResultCode(-1);
			result.setResultMessage("不能给自己助力呀!");
			return result;
		}
		
		// 检查邀请人和被邀请人信息是否正确
		int count1 = DbUp.upTable("mc_login_info").dataCount("member_code=:member_code", new MDataMap("member_code",memberCodeYqr));
		int count2 = DbUp.upTable("mc_login_info").dataCount("member_code=:member_code", new MDataMap("member_code",memberCodeByqr));
		if(count1 > 0 && count2 > 0) {
			// 可以查到邀请人和被邀请人信息
		}else {
			result.setResultCode(-1);
			result.setResultMessage("邀请人或被邀请人信息有误!");
			return result;
		}
		
		// 检查被邀请人今天是否已经被邀请过
		String sql = "SELECT * FROM sc_huodong_event_yq WHERE member_code_byqr = '"+memberCodeByqr+"' AND event_code = '"+eventCode+"' AND yq_time >= '"+startTime+"' AND yq_time <= '"+endTime+"'";
		List<Map<String,Object>> dataSqlList = DbUp.upTable("sc_huodong_event_yq").dataSqlList(sql, new MDataMap());
		if(null != dataSqlList && dataSqlList.size() > 0) {
			// 被邀请过,不能再被邀请(固定错误码,不给用户展示)
			result.setResultCode(12345);
			result.setResultMessage("您今天已经被邀请过!");
			return result;
		}
		
		// 校验邀请人今日邀请成功的人数
		String yqsql = "SELECT * FROM sc_huodong_event_yq WHERE member_code_yqr = '"+memberCodeYqr+"' AND event_code = '"+eventCode+"' AND yq_time >= '"+startTime+"' AND yq_time <= '"+endTime+"'";
		List<Map<String,Object>> yqSqlList = DbUp.upTable("sc_huodong_event_yq").dataSqlList(yqsql, new MDataMap());
		if(null != yqSqlList) {
			if(yqSqlList.size() >= sumYq) {
				// 邀请人今日邀请成功的人数已达到最大,不能被邀请(固定错误码,不给用户展示)
				result.setResultCode(12345);
				result.setResultMessage("邀请次数已经达到最大次数!");
				return result;
			}
		}
		// 添加邀请记录
		MDataMap eventYqMap = new MDataMap();
		eventYqMap.put("uid", WebHelper.upUuid());
		eventYqMap.put("member_code_yqr", memberCodeYqr);
		eventYqMap.put("member_code_byqr", memberCodeByqr);
		eventYqMap.put("zs_yn", "N");
		eventYqMap.put("yq_time", nowTime);
		eventYqMap.put("event_code", eventCode);
		DbUp.upTable("sc_huodong_event_yq").dataInsert(eventYqMap);

		// 活动最大免费抽奖次数
		int baseSum = Integer.parseInt(BigWheelOriginalDrawNum) + Integer.parseInt(BigWheelShareDrawNum);
		
		// 查询剩余抽奖次数
		String sSql3 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCodeYqr +"' AND event_code = '" + eventCode + "'";
		Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sSql3, new MDataMap());
		String updateTime = drawNumMap.get("update_time").toString().substring(0, 10);
		//判断今天是否有抽奖记录
		String sql1 = "SELECT * FROM lc_huodong_event_jl WHERE member_code = '"+memberCodeYqr+"' AND event_code = '" + eventCode + "' AND draw_type = '0' AND zj_time >= '"+startTime+"' AND zj_time <= '"+endTime+"'";
		List<Map<String, Object>> jlList = DbUp.upTable("lc_huodong_event_jl").dataSqlList(sql1, new MDataMap());
		// 计算已抽奖数+剩余抽奖数
		int sum = 0;
		if(nowTime.substring(0, 10).equals(updateTime)) {
			// 抽奖次数更新时间是今天,计算已抽奖数+剩余抽奖数
			sum = jlList.size() + Integer.parseInt(drawNumMap.get("sycs").toString());
		}else {
			// 不是今天,更新次数初始化为2次,并更新时间
			MDataMap eventSycsMap = new MDataMap();
			eventSycsMap.put("uid", drawNumMap.get("uid").toString());
			eventSycsMap.put("sycs", BigWheelOriginalDrawNum);
			eventSycsMap.put("update_time", nowTime);
			DbUp.upTable("sc_huodong_event_sycs").dataUpdate(eventSycsMap, "sycs,update_time", "uid");
			sum = jlList.size() + Integer.parseInt(BigWheelOriginalDrawNum);
		}
		
		if(baseSum > sum) {
			// 可添加抽奖次数
			String sql2 = "SELECT * FROM sc_huodong_event_yq WHERE member_code_yqr = '"+memberCodeYqr+"' AND event_code = '"+eventCode+"' AND yq_time >= '"+startTime+"' AND yq_time <= '"+endTime+"' AND zs_yn = 'N' ORDER BY yq_time";
			//List<Map<String, Object>> yqList = DbUp.upTable("sc_huodong_event_yq").dataSqlList(sql2, new MDataMap());
			List<Map<String, Object>> yqList = DbUp.upTable("sc_huodong_event_yq").upTemplate().queryForList(sql2, new HashMap<String, String>());
			if(null != yqList && yqList.size() >= 2) {
				// 今天未赠送次数的邀请用户满2人,则加一次抽奖次数,且更新 是否赠送 为 Y
				for (int i = 0; i < 2; i++) {
					Map<String, Object> yqMap = yqList.get(i);
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("uid", yqMap.get("uid").toString());
					mDataMap.put("zs_yn", "Y");
					DbUp.upTable("sc_huodong_event_yq").dataUpdate(mDataMap, "zs_yn", "uid");
				}
				MDataMap eventSycsMap = new MDataMap();
				int count = Integer.parseInt(drawNumMap.get("sycs").toString()) + 1;
				eventSycsMap.put("uid", drawNumMap.get("uid").toString());
				eventSycsMap.put("sycs", count+"");
				eventSycsMap.put("update_time", nowTime);
				DbUp.upTable("sc_huodong_event_sycs").dataUpdate(eventSycsMap, "sycs,update_time", "uid");
			}
		}else {
			// 不能添加抽奖次数,剩余邀请记录"是否赠送"为"N"
		}
		
		return result;
	}

}
