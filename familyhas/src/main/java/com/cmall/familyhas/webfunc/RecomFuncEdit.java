package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class RecomFuncEdit extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		
		MWebResult mResult = new MWebResult();
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = mEditMaps.get("uid").trim();
		MDataMap dataMap = DbUp.upTable("fh_brand_preference").one("uid",uid);
		if(dataMap != null){
			/*排序*/
			String sort_num = mEditMaps.get("sort_num").replace("'", "").trim();
			
			if(null == sort_num || "".equals(sort_num)){
				dataMap.put("sort_num", "0");
			}else{
				dataMap.put("sort_num", sort_num);
			}
			
			/*图片*/
			String photos = mEditMaps.get("photos").trim();
			if(null == photos || "".equals(photos)){
				dataMap.put("photos", "");
			}else{
				dataMap.put("photos", photos);
			}
			String up_time = mEditMaps.get("up_time").trim();
			if(null == up_time || "".equals(up_time)){
				dataMap.put("up_time","");
			}else{
				dataMap.put("up_time",up_time);
			}
			String down_time = mEditMaps.get("down_time").trim();
			if(null == down_time || "".equals(down_time)){
				dataMap.put("down_time","");
			}else{
				dataMap.put("down_time",down_time);
			}
			/*状态*/
			dataMap.put("flag_show", mEditMaps.get("flag_show").trim());
			/*卖点描述*/
			dataMap.put("selling_describe", mEditMaps.get("selling_describe").trim());
			
			try{
				if (mResult.upFlagTrue()) {
					/**将惠家有信息更改fh_brand_preference表中*/
					DbUp.upTable("fh_brand_preference").update(dataMap);
				}
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
		}
		return mResult;
	}

}
