package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.result.ApiForContactUsResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.cc.VideoService;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/** 
* @Author fufu
* @Time 2020-8-18 11:13:07 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForContactUs extends RootApiForVersion<ApiForContactUsResult, RootInput> {

	@Override
	public ApiForContactUsResult Process(RootInput inputParam, MDataMap mRequestMap) {
		Map<String,Object> map = DbUp.upTable("sc_contact_us_ldpay").dataSqlOne("SELECT * FROM systemcenter.sc_contact_us_ldpay limit 1",new MDataMap());
		ApiForContactUsResult result = new ApiForContactUsResult();
		VideoService service = new VideoService();
		if(map != null && !map.isEmpty()) {
			result.setPicUrl(MapUtils.getString(map,"pic_url",""));
			result.setVideoUrl(service.getCcPlayUrl(MapUtils.getString(map,"video_url","")));
			result.setVideoMainPic(MapUtils.getString(map,"video_main_pic",""));
			result.setDesc(MapUtils.getString(map,"contact_desc",""));
			result.setPhoneOne(MapUtils.getString(map,"phone_one",""));
			result.setPhoneTwo(MapUtils.getString(map,"phone_two",""));
		}
		return result;
	}

}
