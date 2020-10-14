package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.model.LiveControlInfo;
import com.cmall.familyhas.model.LiveRoom;
import com.cmall.familyhas.model.LiveRoomBaseInfo;
import com.cmall.familyhas.model.LiveRoomDataInfo;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;




/**
 * 微信绑定优惠券活动service
 * @author zhangbo
 *
 */
public class WeChatBoundService extends BaseClass{ 

	public String getWeChatBoundActivity() {
		MDataMap one = DbUp.upTable("oc_coupon_wechat_bound").one();
		if(one==null) {
			one = new MDataMap();
			one.put("activity_code", "");
			one.put("activity_name", "");
		}
		return  JSON.toJSONString(one);
	}		
	
}
