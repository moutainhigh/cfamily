package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 公众号渠道添加
 */
public class FuncAddSmallAppChannel extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String name = StringUtils.trimToEmpty(mDataMap.get("zw_f_name"));
		String channelId = StringUtils.trimToEmpty(mDataMap.get("zw_f_channel_id"));
		
		if(DbUp.upTable("fh_smallapp_channel").count("name",name) > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("公众号名称已存在");
			return mResult;
		}
		
		if(DbUp.upTable("fh_smallapp_channel").count("channel_id",channelId) > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("渠道号已存在");
			return mResult;
		}
		
		if(!channelId.matches("[0-9a-zA-Z]+")) {
			mResult.setResultCode(0);
			mResult.setResultMessage("渠道号仅支持字母和数字");
			return mResult;
		}
		
		mDataMap.put("zw_f_create_time", FormatHelper.upDateTime());
		mDataMap.put("zw_f_create_user", UserFactory.INSTANCE.create().getLoginName());
		mDataMap.put("zw_f_url", bConfig("cfamily.smallAppChannelUrl")+channelId);
		mDataMap.put("zw_f_wx_url", bConfig("cfamily.wxChannelUrl")+channelId);
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		
		return mResult;
	}
}
