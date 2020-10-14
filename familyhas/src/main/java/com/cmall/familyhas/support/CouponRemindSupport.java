package com.cmall.familyhas.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * （惠家有）优惠券过期提醒信列表显示
 * @author wei.che
 * @date 2015-12-21
 */
public class CouponRemindSupport extends BaseClass {

	/**
	 * 用于优惠券提醒设置下信息的显示
	 * @param msg_uid 短信表的UId
	 * @return 短信完成时间及完成状态
	 */
	public Map<String, String> getMsgMap(String msg_uid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("finish_time", "");
		map.put("flag_finish", "");
		if(StringUtils.isNotBlank(msg_uid)){
			MDataMap dataMap = DbUp.upTable("za_message").one("uid",msg_uid);
			if(dataMap!=null && StringUtils.isNotBlank(dataMap.get("create_time"))){
				map.put("finish_time", dataMap.get("finish_time"));
				map.put("flag_finish", dataMap.get("flag_finish"));
			}
		}
		return map;
	}
	
}
