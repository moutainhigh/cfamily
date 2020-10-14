package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetBigWheelNumAndYQInput;
import com.cmall.familyhas.api.model.MemberSync;
import com.cmall.familyhas.api.result.ApiForGetBigWheelNumAndYQResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 大转盘页面获取剩余抽奖次数和被邀请人信息接口
 * @author lgx
 *
 */
public class ApiForGetBigWheelNumAndYQ extends RootApiForVersion<ApiForGetBigWheelNumAndYQResult, ApiForGetBigWheelNumAndYQInput> {

	// 大转盘初始化抽奖次数
	private final String BigWheelOriginalDrawNum = bConfig("familyhas.bigWheelOriginalDrawNum");
	// 大转盘分享获得最大抽奖次数
	private final String BigWheelShareDrawNum = bConfig("familyhas.bigWheelShareDrawNum");
	
	@Override
	public ApiForGetBigWheelNumAndYQResult Process(ApiForGetBigWheelNumAndYQInput inputParam, MDataMap mRequestMap) {
		ApiForGetBigWheelNumAndYQResult result = new ApiForGetBigWheelNumAndYQResult();
		
		String nowTime = FormatHelper.upDateTime();
		String startTime = nowTime.substring(0, 10)+" 00:00:00";
		String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		// 查询当前时间段内已经发布状态的大转盘活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的福利转盘活动!");
			return result;
		}

		// 根据用户编号和当前时间获取剩余抽奖次数
		if (getFlagLogin()) {
			//MOauthInfo oauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token"));
			MOauthInfo oauthInfo = getOauthInfo();
			if (oauthInfo != null) {
				// 直接查询剩余抽奖次数
				String sSql3 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ oauthInfo.getUserCode() +"' AND event_code = '" + eventInfoMap.get("event_code") + "'";
				Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sSql3, new MDataMap());
				if(drawNumMap == null) {
					// 查不到说明是第一次登录进入抽奖页面,初始化抽奖次数2次
					/*MDataMap eventSycsMap = new MDataMap();
					eventSycsMap.put("uid", WebHelper.upUuid());
					eventSycsMap.put("member_code", oauthInfo.getUserCode());
					eventSycsMap.put("sycs", BigWheelOriginalDrawNum);
					eventSycsMap.put("update_time", nowTime);
					eventSycsMap.put("event_code", (String) eventInfoMap.get("event_code"));
					DbUp.upTable("sc_huodong_event_sycs").dataInsert(eventSycsMap);*/
					result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
				}else {
					// 查到了,看更新时间
					String updateTime = drawNumMap.get("update_time").toString().substring(0, 10);
					if(nowTime.substring(0, 10).equals(updateTime)) {
						// 更新时间是今天
						result.setRemainDrawNum((int) drawNumMap.get("sycs"));
					}else {
						// 不是今天,更新次数初始化为2次,并更新时间
						MDataMap eventSycsMap = new MDataMap();
						eventSycsMap.put("uid", drawNumMap.get("uid").toString());
						eventSycsMap.put("sycs", BigWheelOriginalDrawNum);
						eventSycsMap.put("update_time", nowTime);
						DbUp.upTable("sc_huodong_event_sycs").dataUpdate(eventSycsMap, "sycs,update_time", "uid");
						result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
					}
				}
				
			}else {
				result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
			}
		}else {
			result.setRemainDrawNum(Integer.parseInt(BigWheelOriginalDrawNum));
		}
		
		// 查询被邀请人信息
		if(getFlagLogin()) {
			// 最大邀请人数
			int sumYq = 2 * Integer.parseInt(BigWheelShareDrawNum);
			// 如果登录,获取登录人信息
			MOauthInfo oauthInfo2 = getOauthInfo();
			// 查询被邀请人
			String yqsql = "SELECT * FROM sc_huodong_event_yq WHERE member_code_yqr = '"+oauthInfo2.getUserCode()+"' AND event_code = '"+eventInfoMap.get("event_code")+"' AND yq_time >= '"+startTime+"' AND yq_time <= '"+endTime+"' ORDER BY yq_time LIMIT "+sumYq;
			List<Map<String,Object>> yqSqlList = DbUp.upTable("sc_huodong_event_yq").dataSqlList(yqsql, new MDataMap());
			List<MemberSync> list2 = new ArrayList<MemberSync>();
			if(null != yqSqlList && yqSqlList.size() > 0) {
				for (Map<String, Object> map : yqSqlList) {
					// 获取被邀请人信息
					MemberSync memberSync = new MemberSync();
					String syncsql = "SELECT * FROM mc_member_sync WHERE member_code = '"+map.get("member_code_byqr")+"'";
					Map<String, Object> member = DbUp.upTable("mc_member_sync").dataSqlOne(syncsql, new MDataMap());
					if(null != member) {						
						if(null != member.get("avatar")) {						
							memberSync.setAvatar((String) member.get("avatar"));
						}
						if(null != member.get("nickname")) {						
							memberSync.setNickname(member.get("nickname").toString());
						}
						memberSync.setMobile(member.get("login_name").toString());
					}else {
						String syncsql2 = "SELECT * FROM mc_login_info WHERE member_code = '"+map.get("member_code_byqr")+"'";
						Map<String, Object> loginInfo = DbUp.upTable("mc_login_info").dataSqlOne(syncsql2, new MDataMap());
						memberSync.setMobile(loginInfo.get("login_name").toString());
						memberSync.setNickname(loginInfo.get("login_name").toString().substring(0,3)+"****"+loginInfo.get("login_name").toString().substring(7));
					}
					list2.add(memberSync);
				}
			}
			result.setList(list2);
		}
		
		return result;
	}

}
