package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDelVideoType extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if (mResult.upFlagTrue()) {
			if (mDelMaps.containsKey("uid")) {
				Map<String, Object> dataSqlOne = DbUp.upTable("fh_video").dataSqlOne("select * from fh_video left join fh_video_type on  fh_video.video_type=fh_video_type.type_code where fh_video_type.uid=:uid", new MDataMap("uid",mDelMaps.get("uid")));
			if(dataSqlOne!=null&&dataSqlOne.size()>0) {
				mResult.setResultCode(0);
				mResult.setResultMessage("该视频分类下存在视频,不能删除!");
				return mResult;
			}
			DbUp.upTable("fh_video_type").dataDelete("uid=:uid", new MDataMap("uid",mDelMaps.get("uid").toString()), null);
			}
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}
	
	
}
