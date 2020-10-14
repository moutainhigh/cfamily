package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiMyAlbumInput;
import com.cmall.familyhas.api.result.ApiMyAlbumResult;
import com.cmall.familyhas.api.result.ApiUserAlbumsResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiMyAlbum extends RootApiForVersion<ApiMyAlbumResult, ApiMyAlbumInput> {

	@Override
	public ApiMyAlbumResult Process(ApiMyAlbumInput inputParam, MDataMap mRequestMap) {
		ApiMyAlbumResult albumResult = new ApiMyAlbumResult();
		String open_id = inputParam.getOpen_id();
		MDataMap mWhereMap = new MDataMap();
		if(StringUtils.isNotBlank(open_id)) {
//			String sql = "SELECT hma.album_id AS album_id , hmai.preview_img AS preview_img , hmat.template_title AS template_title" + 
//					"FROM hp_music_album hma" + 
//					"INNER JOIN hp_music_album_img hmai ON hma.album_id = hmai.album_id" + 
//					"INNER JOIN hp_music_album_template hmat ON hma.template_id = hmat.template_id WHERE hma.use_flag = 'Y' and hmat.use_flag = 'Y' and hmai.review_state = '1' and hma.open_id = '"+open_id+"'";
//			List<Map<String, Object>> dataSqlList = DbUp.upTable("hp_music_album").dataSqlList(sql, null);
//			List<ApiUserAlbumsResult> user_albums = albumResult.getUser_albums();
//			for(Map<String, Object> map : dataSqlList) {
//				ApiUserAlbumsResult albumsResult = new ApiUserAlbumsResult();
//				albumsResult.setAlbum_id(map.get("album_id").toString());
//				albumsResult.setAlbum_preview_img(map.get("preview_img").toString());
//				albumsResult.setTemplate_title(map.get("template_title").toString());
//				albumsResult.setTemplate_title(map.get("share_title").toString());
//				albumsResult.setTemplate_title(map.get("share_img").toString());
//				albumsResult.setAlbums_date(DateUtil.toString(DateUtil.toDate(map.get("create_time").toString(),DateUtil.DATE_FORMAT_DATETIME), DateUtil.DATE_FORMAT_DATEONLY));
//				user_albums.add(albumsResult);
//			}
			mWhereMap.put("open_id", open_id);
			List<ApiUserAlbumsResult> user_albums = albumResult.getUser_albums();
			List<Map<String,Object>> dataSqlList = DbUp.upTable("hp_music_album").dataSqlList("select * from hp_music_album where open_id =:open_id and use_flag = 'Y' order by create_time desc", mWhereMap);
			mWhereMap.clear();
			for(Map<String,Object> map : dataSqlList) {
				ApiUserAlbumsResult albumsResult = new ApiUserAlbumsResult();
				String album_id = map.get("album_id").toString();
				albumsResult.setAlbum_id(album_id);
				mWhereMap.put("album_id", album_id);
				Map<String, Object> dataSqlOne = DbUp.upTable("hp_music_album_img").dataSqlOne("select * from hp_music_album_img where album_id =:album_id order by img_sort limit 0,1", mWhereMap);
				if(dataSqlOne!=null) {
					albumsResult.setAlbum_preview_img(dataSqlOne.get("album_img").toString());
					albumsResult.setShare_img(dataSqlOne.get("share_img").toString());
					albumsResult.setAlbum_date(DateUtil.toFormatDate(map.get("create_time").toString(), DateUtil.DATE_FORMAT_DATEONLY));
					mWhereMap.put("template_id", map.get("template_id").toString());
					dataSqlOne = DbUp.upTable("hp_music_album_template").dataSqlOne("select * from hp_music_album_template where template_id =:template_id", mWhereMap);
					albumsResult.setTemplate_title(dataSqlOne.get("template_title").toString());
					albumsResult.setShare_title(dataSqlOne.get("open_title").toString());
					user_albums.add(albumsResult);
				}
			}
			albumResult.setUser_albums(user_albums);
		}
		return albumResult;
	}

}
