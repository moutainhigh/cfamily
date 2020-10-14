package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 统计页面活跃的类
 */
public class PageActiveService {

	public void active(String memberCode, String channelId, String pageCode) {
		String now = FormatHelper.upDateTime();
		String today = now.substring(0,10);
		String lockKey = memberCode+pageCode;
		
		// 如果已经存在当日的标识则直接返回
		if(today.equals(XmasKv.upFactory(EKvSchema.PageActiveFlag).get(lockKey))) {
			return;
		}
		
		String lockRes = KvHelper.lockCodes(60, lockKey);
		if(StringUtils.isBlank(lockRes)) {
			return;
		}
		
		// 检查当日数据是否已经存在
		if(DbUp.upTable("lc_page_active_log").dataCount("member_code = :member_code AND page_code = :page_code AND create_time like :today", new MDataMap(
					"member_code", memberCode,
					"page_code", pageCode,
					"today", today + "%"
				)) > 0) {
			
			KvHelper.unLockCodes(lockRes, lockKey);
			return;
		}
		
		DbUp.upTable("lc_page_active_log").dataInsert(new MDataMap(
				"member_code", memberCode,
				"page_code", pageCode,
				"channel_id", channelId,
				"create_time", now
				));
		
		KvHelper.unLockCodes(lockRes, lockKey);
		
		// 记录当日的标识
		XmasKv.upFactory(EKvSchema.PageActiveFlag).setex(lockKey, 24*3600, today);
		
	}
}
