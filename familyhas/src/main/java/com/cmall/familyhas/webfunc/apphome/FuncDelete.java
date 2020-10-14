package com.cmall.familyhas.webfunc.apphome;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDelete extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		if (mResult.upFlagTrue()) {
			if (mDelMaps.containsKey("uid")) {
				
				
				MDataMap mThisMap=null;
				

				// 循环所有结构
				for (MWebField mField : mPage.getPageFields()) {

					if (mField.getFieldTypeAid().equals("104005003")) {
						
						if(mThisMap==null)
						{
							mThisMap=DbUp.upTable(mPage.getPageTable()).one("uid",mDelMaps.get("uid"));
						}
						
						
						WebUp.upComponent(mField.getSourceCode()).inDelete(mField,
								mThisMap);
						
					}
				}
				
				
				
				String uid = mDelMaps.get("uid");
				MDataMap channel_details = DbUp.upTable("fh_apphome_channel_details").one("uid",uid);
				String channel_uid = channel_details.get("channel_uid");
				MDataMap channel = DbUp.upTable("fh_apphome_channel").one("uid",channel_uid);
				if("2".equals(channel.get("status"))){
					//已发布状态
					int count = DbUp.upTable("fh_apphome_channel_details").count("channel_uid",channel_uid);
					if(count == 1){//只有一条的情况下，禁止删除
						mResult.setResultCode(10000);
						mResult.setResultMessage("该栏目已经发布，禁止删除最后一条内容");
						return mResult;
					}
				}
				DbUp.upTable(mPage.getPageTable()).delete("uid",
						uid);

			}
		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}

		return mResult;
	}

}
