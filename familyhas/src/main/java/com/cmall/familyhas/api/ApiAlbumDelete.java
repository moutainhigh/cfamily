package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiAlbumDeleteInput;
import com.cmall.familyhas.api.result.ApiAlbumDeleteResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiAlbumDelete extends RootApiForVersion<ApiAlbumDeleteResult, ApiAlbumDeleteInput> {

	@Override
	public ApiAlbumDeleteResult Process(ApiAlbumDeleteInput inputParam, MDataMap mRequestMap) {
		ApiAlbumDeleteResult albumDeleteResult = new ApiAlbumDeleteResult();
		String album_id = inputParam.getAlbum_id();
		String open_id = inputParam.getOpen_id();
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("album_id", album_id);
		mDataMap.put("open_id", open_id);
		DbUp.upTable("hp_music_album").dataExec("delete from hp_music_album where open_id =:open_id and album_id =:album_id", mDataMap);
		DbUp.upTable("hp_music_album_img").dataExec("delete from hp_music_album_img where album_id =:album_id", mDataMap);
		albumDeleteResult.setSuccess("true");
		return albumDeleteResult;
	}
	
}
