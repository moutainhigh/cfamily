package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class BrandPreferenceFuncEdit extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = mEditMaps.get("uid").trim();
		MDataMap dataMap = DbUp.upTable("fh_brand_preference").one("uid",uid);
		if(dataMap != null){
			/*系统当前时间*/
			String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
			String startTime = mEditMaps.get("up_time").trim();
			String endTime = mEditMaps.get("down_time").trim();
			if (endTime.compareTo(startTime) <= 0) {
				//开始时间必须小于结束时间!
				mResult.inErrorMessage(916401201);
				return mResult;
			}else if (endTime.compareTo(createTime) <= 0) {
				//当前时间必须小于结束时间!
				mResult.inErrorMessage(916401214);
				return mResult;
			}
			String shareFlag = mEditMaps.get("share_flag");
			if("449747110001".equals(shareFlag)){//不分享
				dataMap.put("share_img_url","");
				dataMap.put("share_title","");
				dataMap.put("share_content","");
			}
			dataMap.put("share_flag",shareFlag);
			/*名称*/
			String info_title = mEditMaps.get("info_title").replace("'", "").trim();
			
			/*排序*/
			String sort_num = mEditMaps.get("sort_num").replace("'", "").trim();
			
			if(null == info_title || "".equals(info_title)){
				dataMap.put("info_title", "");
			}else{
				dataMap.put("info_title", info_title);
			}
			
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
//			String up_time = mEditMaps.get("up_time").trim();
			if(null == startTime || "".equals(startTime)){
				dataMap.put("up_time","");
			}else{
				dataMap.put("up_time",startTime);
			}
//			String down_time = mEditMaps.get("down_time").trim();
			if(null == endTime || "".equals(endTime)){
				dataMap.put("down_time","");
			}else{
				dataMap.put("down_time",endTime);
			}
			/*链接地址*/
			dataMap.put("link_addr", mEditMaps.get("link_addr").trim());
			/*状态*/
			dataMap.put("flag_show", mEditMaps.get("flag_show").trim());
			
			/*分享内容*/ 
			String share_content = mEditMaps.get("share_content").trim();
			if(null == share_content || "".equals(share_content)) {
				dataMap.put("share_content","");
			} else {
				dataMap.put("share_content",share_content);
			}
			/*分享标题*/
			String share_img_url = mEditMaps.get("share_img_url").trim();
			if(null == share_img_url || "".equals(share_img_url)) {
				dataMap.put("share_img_url","");
			} else {
				dataMap.put("share_img_url",share_img_url);
			}
			/*分享图片连接*/
			String share_title = mEditMaps.get("share_title").trim();
			if(null == share_title || "".equals(share_title)) {
				dataMap.put("share_title","");
			} else {
				dataMap.put("share_title",share_title);
			}
			dataMap.put("discount_type", mEditMaps.get("discount_type").trim());
			dataMap.put("brand_code", mEditMaps.get("brand_code"));
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
