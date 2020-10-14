package com.cmall.familyhas.api;


import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiCreateAlbumInput;
import com.cmall.familyhas.api.result.ApiCreateAlbumResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiCreateAlbum extends RootApiForVersion<ApiCreateAlbumResult, ApiCreateAlbumInput> {

	@Override
	public ApiCreateAlbumResult Process(ApiCreateAlbumInput inputParam, MDataMap mRequestMap) {
		ApiCreateAlbumResult albumResult = new ApiCreateAlbumResult();
		String album_id = inputParam.getAlbum_id();
		if(StringUtils.isNotBlank(album_id)) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("album_id", album_id);
			mDataMap.put("use_flag", "Y");
			mDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("hp_music_album").dataUpdate(mDataMap, "use_flag,create_time", "album_id");
			albumResult.setSuccess("true");
		}else {
			albumResult.setSuccess("false");
		}
		return albumResult;
	}

	

}
