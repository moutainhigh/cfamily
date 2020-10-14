package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 批量删除积分商城某一活动下的详细信息
 * 
 * @author zhouguohui
 *
 */
public class FuncDeleteAllChannelDetails extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		String uids = mAddMaps.get("uids");
		String[] uidsArray = uids.split(",");
		MDataMap channel_details = DbUp.upTable("fh_apphome_channel_details").one("uid",uidsArray[0]);
		String channel_uid = channel_details.get("channel_uid");
		MDataMap channel = DbUp.upTable("fh_apphome_channel").one("uid",channel_uid);
		String status = channel.get("status");
		int count = DbUp.upTable("fh_apphome_channel_details").count("channel_uid",channel_uid);
		if("2".equals(status)){//已发布
			if(count == uidsArray.length){
				mResult.setResultCode(10000);
				mResult.setResultMessage("该栏目已发布，请勿删除所有栏目信息");
				return mResult;
			}
		}
		for (int i = 0; i < uidsArray.length; i++) {
			String uid = uidsArray[i];
			DbUp.upTable("fh_apphome_channel_details").delete("uid",uid);
		}
		return mResult;
	}

}
