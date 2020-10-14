package com.cmall.familyhas.api.video;

import com.cmall.familyhas.api.video.input.ApiForUploadVideoInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 上传视频接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiForUploadVideo extends RootApiForToken<RootResult,ApiForUploadVideoInput> {
	
	public RootResult Process(ApiForUploadVideoInput input, MDataMap mRequestMap) {
		
		RootResult result = new RootResult();
		String userCode = getUserCode();
		MDataMap insertMap = new MDataMap();
		insertMap.put("video_code", input.getVideoCode());
		insertMap.put("member_code", userCode);
		insertMap.put("img_url", input.getPicUrl());
		insertMap.put("link_url", input.getLinkUrl());
		insertMap.put("title", input.getVideoTitle());
		DbUp.upTable("lv_video_info").dataInsert(insertMap);
		
		return result;
	}
}
