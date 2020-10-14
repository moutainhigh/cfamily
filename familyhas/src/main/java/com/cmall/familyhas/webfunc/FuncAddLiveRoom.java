package com.cmall.familyhas.webfunc;


import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.service.FarmService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.webfunc.apphome.FuncAdd;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webmodel.MWebResult;


public class FuncAddLiveRoom extends FuncAdd{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		FarmService farmService = new FarmService();
		String anchor_phone = mAddMaps.get("anchor_phone");
		String nickName = "";
		String avatar = "";
		//测试做直播间数量限制处理
		if(StringUtils.equals("Y", bConfig("familyhas.limitLiveRoomFlag"))) {
			Map<String, Object> dataSqlOne = DbUp.upTable("lv_live_room").dataSqlOne("select IFNULL(count(uid),0) num from lv_live_room where is_delete='0' and live_status in ('449746320001','449746320003') ", null);
			int num = Integer.parseInt(dataSqlOne.get("num").toString());
			if(num>=2) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("未开始和直播中的测试房间总数不能超过两个!");
				return mWebResult;
			}
		}
		Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE login_name = '"+anchor_phone+"'", new MDataMap());
		if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
			// 如果昵称是空,查询手机号
			Map<String, Object> login_info2 = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE login_name = '"+anchor_phone+"'", new MDataMap());
			if(login_info2==null) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("主播账号不存在!");
				return mWebResult;
			}
			nickName = (String) login_info2.get("login_name");
			if(farmService.isPhone(nickName)) {
				nickName = nickName.substring(0, 3) + "****" + nickName.substring(7);
			}
		}else { // 如果昵称不是空
			nickName = (String) member_sync.get("nickname");
			avatar = (String) member_sync.get("avatar");
		}
		if(mDataMap.get("zw_f_live_title").length()>25) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("直播标题不能超过25个字!");
			return mWebResult;
		}
		String timeString = DateUtil.getSysDateTimeString();
		mDataMap.put("zw_f_live_room_id", WebHelper.upCode("LV"));
		mDataMap.put("zw_f_uid", WebHelper.upUuid());
		mDataMap.put("zw_f_anchor_nickname", nickName);
		mDataMap.put("zw_f_anchor_avatar", avatar);
		mDataMap.put("zw_f_create_time",timeString );
		mDataMap.put("zw_f_update_time", timeString);
		
		MDataMap newmAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		DbUp.upTable("lv_live_room").dataInsert(newmAddMaps);
		return mWebResult;
	}
}
