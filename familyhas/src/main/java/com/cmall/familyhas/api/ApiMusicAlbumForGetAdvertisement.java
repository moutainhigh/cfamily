package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiMusicAlbumForGetAdvertisementInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.AdvertisementObj;
import com.srnpr.zapweb.webapi.ApiMusicAlbumForGetAdvertisementResult;

/**
 *微信小程序-音乐相册  获取广告信息接口
 *
 *2019/09/25 10:07:00
 *
 *zhangbo
 *
 */

public class ApiMusicAlbumForGetAdvertisement extends RootApi<ApiMusicAlbumForGetAdvertisementResult, ApiMusicAlbumForGetAdvertisementInput>{

	@Override
	public ApiMusicAlbumForGetAdvertisementResult Process(ApiMusicAlbumForGetAdvertisementInput inputParam,
			MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiMusicAlbumForGetAdvertisementResult result = new ApiMusicAlbumForGetAdvertisementResult();
		String pageType = inputParam.getPageType();
		List<Map<String, Object>> dataSqlList = DbUp.upTable("hp_music_album_adv").dataSqlList("select * from hp_music_album_adv where (page_type=:page_type or page_type='44975022003') and state='44975021001'", new MDataMap("page_type",pageType));
		if(dataSqlList!=null&&dataSqlList.size()>0) {
			for (Map<String, Object> map : dataSqlList) {
				AdvertisementObj subObj = new AdvertisementObj();
				subObj.setAdvertiseCode(map.get("advertise_code").toString());
				subObj.setAdvertiseName(map.get("advertise_name").toString());
				subObj.setAdvertiseType(map.get("advertise_type").toString());
				subObj.setPosition(map.get("position").toString());
				subObj.setAdvertiseImg(map.get("advertise_img").toString());
				subObj.setJumpAddress(map.get("jump_address").toString());
				subObj.setPath(map.get("path").toString());
				result.getList().add(subObj);
			}
		}
		return result;
	}
}


