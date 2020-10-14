package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForCutCakeGetAllPrizeInput;
import com.cmall.familyhas.api.model.CutCakeDrawJl;
import com.cmall.familyhas.api.result.ApiForCutCakeGetAllPrizeResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取全部切蛋糕抽奖记录(200条)
 * @author lgx
 *
 */
public class ApiForCutCakeGetAllPrize extends RootApiForVersion<ApiForCutCakeGetAllPrizeResult, ApiForCutCakeGetAllPrizeInput> {

	public ApiForCutCakeGetAllPrizeResult Process(ApiForCutCakeGetAllPrizeInput inputParam, MDataMap mRequestMap) {
		
		ApiForCutCakeGetAllPrizeResult result = new ApiForCutCakeGetAllPrizeResult();
		
		List<CutCakeDrawJl> list = new ArrayList<CutCakeDrawJl>();
		
		String nowTime = FormatHelper.upDateTime();
		String eventCode = "";
		
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
		
		// 查询全部切蛋糕中奖记录(倒序200条)
		String sql = "SELECT * FROM sc_hudong_cake_draw_jl WHERE event_code = '"+eventCode+"' ORDER BY create_time DESC limit 200";
		List<Map<String, Object>> cakeDrawList = DbUp.upTable("sc_hudong_cake_draw_jl").dataSqlList(sql, new MDataMap());
		if(cakeDrawList != null && cakeDrawList.size() > 0) {
			for (Map<String, Object> map : cakeDrawList) {
				CutCakeDrawJl cutCakeDrawJl = new CutCakeDrawJl();
				//String create_time = MapUtils.getString(map, "create_time").substring(0, 10);
				String jp_type = MapUtils.getString(map, "jp_type");
				String jp_name = MapUtils.getString(map, "jp_name");
				String nickName = "";
				String member_code = MapUtils.getString(map, "member_code");
				// 查询昵称
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+member_code+"'", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+member_code+"'", new MDataMap());
					nickName = (String) login_info.get("login_name");
					if(isPhone(nickName)) {
						nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
					}
				}else { // 如果昵称不是空
					nickName = (String) member_sync.get("nickname");
				}
				
				cutCakeDrawJl.setNickName(nickName);
				//cutCakeDrawJl.setCreateTime(create_time);
				cutCakeDrawJl.setJpName(jp_name);
				cutCakeDrawJl.setJpType(jp_type);
				
				list.add(cutCakeDrawJl);
			}
		}
		
		result.setList(list);
		
		return result;
	}

	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}

}
