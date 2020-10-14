package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiMyAlbumResult extends RootResultWeb{
	@ZapcomApi(value = "相册集")
	List<ApiUserAlbumsResult> user_albums = new ArrayList<ApiUserAlbumsResult>();

	public List<ApiUserAlbumsResult> getUser_albums() {
		return user_albums;
	}

	public void setUser_albums(List<ApiUserAlbumsResult> user_albums) {
		this.user_albums = user_albums;
	}
	
}
