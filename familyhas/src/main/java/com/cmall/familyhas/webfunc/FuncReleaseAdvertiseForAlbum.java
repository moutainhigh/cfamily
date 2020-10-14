package com.cmall.familyhas.webfunc;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;

import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @author zb
 */
public class FuncReleaseAdvertiseForAlbum extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String now = FormatHelper.upDateTime();
		String uid = mAddMaps.get("uid");
		if(StringUtils.isEmpty(uid)){
			mResult.setResultMessage("发布失败");
		}else{
			MDataMap one = DbUp.upTable("hp_music_album_adv").one("uid",uid);
			if(one!=null) {
				String releaseFlag = one.get("state").toString();
				if("44975021002".equals(releaseFlag)) {//发布

				DbUp.upTable("hp_music_album_adv").dataUpdate(new MDataMap("uid",uid,"state","44975021001"), "state", "uid");
				}
				else {//取消发布
					DbUp.upTable("hp_music_album_adv").dataUpdate(new MDataMap("uid",uid,"state","44975021002"), "state", "uid");
				}
			}
			else {
				mResult.setResultMessage("发布失败,未查询到该条广告信息！");
			}
			
		}
     	return mResult;
	}
	
	public static boolean compareDateTime(String startTime, String endTime) {
		// TODO Auto-generated method stub
		if(startTime.compareTo(endTime)>0) {
			return true;
		}
			return false;
	}
}
