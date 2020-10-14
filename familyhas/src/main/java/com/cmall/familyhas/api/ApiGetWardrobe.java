package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiGetWardrobeInput;
import com.cmall.familyhas.api.model.Wardrobe;
import com.cmall.familyhas.api.result.ApiGetWardrobeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetWardrobe extends RootApiForManage<ApiGetWardrobeResult, ApiGetWardrobeInput>{

	public ApiGetWardrobeResult Process(ApiGetWardrobeInput inputParam,
			MDataMap mRequestMap) {
		
		ApiGetWardrobeResult result = new ApiGetWardrobeResult();
		List<MDataMap> queryAll = DbUp.upTable("fh_program_wardrobe").queryAll("uid,wardrobe_describe,share_img,share_title,share_content,share_link,wardrobe_code", "", "", null);
		for (MDataMap mDataMap : queryAll) {
			
			Wardrobe war = new Wardrobe();
//			mDataMap.get("uid");
			war.setDescribe(mDataMap.get("wardrobe_describe"));
			war.setShare_content(mDataMap.get("share_content"));
			war.setShare_img(mDataMap.get("share_img"));
			war.setShare_link(mDataMap.get("share_link"));
			war.setShare_title(mDataMap.get("share_title"));
			war.setWardrobe_code(mDataMap.get("wardrobe_code"));
			result.setWardrobe(war);
		}
		return result;
	}
	

}
