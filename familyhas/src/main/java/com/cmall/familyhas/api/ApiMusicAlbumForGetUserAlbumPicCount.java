package com.cmall.familyhas.api;


import com.cmall.familyhas.api.input.ApiMusicAlbumForGetUserAlbumPicCountInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.ApiMusicAlbumForGetUserAlbumPicCountResult;

/**
 *微信小程序-音乐相册  获取个人上传图片信息
 *
 *2019/09/25 10:07:00
 *
 *zhangbo
 *
 */

public class ApiMusicAlbumForGetUserAlbumPicCount extends RootApi<ApiMusicAlbumForGetUserAlbumPicCountResult, ApiMusicAlbumForGetUserAlbumPicCountInput>{

	@Override
	public ApiMusicAlbumForGetUserAlbumPicCountResult Process(ApiMusicAlbumForGetUserAlbumPicCountInput inputParam,
			MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiMusicAlbumForGetUserAlbumPicCountResult result = new ApiMusicAlbumForGetUserAlbumPicCountResult();
		String openId = inputParam.getOpenId();
		int albumNum = DbUp.upTable("hp_music_album").dataCount("open_id=:open_id and use_flag=:use_flag", new MDataMap("open_id",openId,"use_flag","Y"));
		int picNum = DbUp.upTable("hp_music_album_user_pic").dataCount("open_id=:open_id", new MDataMap("open_id",openId));
		result.setAlbumNum(albumNum);
		result.setPicNum(picNum);
		return result;
	}
}


