package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiAlbumDetailsInput;
import com.cmall.familyhas.api.result.ApiAlbumDetailsResult;
import com.cmall.familyhas.api.result.UserTemplateImgsResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiAlbumDetails extends RootApiForVersion<ApiAlbumDetailsResult,ApiAlbumDetailsInput> {

	@Override
	public ApiAlbumDetailsResult Process(ApiAlbumDetailsInput inputParam, MDataMap mRequestMap) {
		ApiAlbumDetailsResult apiImgDetailsResult = new ApiAlbumDetailsResult();
		String album_id = inputParam.getAlbum_id();
		MDataMap mWhereMap = new MDataMap();
		if(StringUtils.isNotBlank(album_id)) {
			mWhereMap.put("album_id", album_id);
			Map<String, Object> mapalbum = DbUp.upTable("hp_music_album").dataSqlOne("SELECT * FROM hp_music_album WHERE album_id =:album_id and use_flag = 'Y'", mWhereMap);
			if(mapalbum == null) {
				apiImgDetailsResult.inErrorMessage(916423900);
				return apiImgDetailsResult;
			}
			String template_id = mapalbum.get("template_id").toString();
			mWhereMap.put("template_id", template_id);
			Map<String, Object> maptemplate = DbUp.upTable("hp_music_album_template").dataSqlOne("SELECT * FROM hp_music_album_template WHERE template_id =:template_id and use_flag = 'Y'", mWhereMap);
			apiImgDetailsResult.setTemplate_id(Integer.parseInt(template_id));
			apiImgDetailsResult.setTemplate_title(maptemplate.get("template_title").toString());
			apiImgDetailsResult.setOpen_title(maptemplate.get("open_title").toString());
			apiImgDetailsResult.setMax_up_imgs(Integer.parseInt(maptemplate.get("max_up_imgs").toString()));
			apiImgDetailsResult.setShare_title(maptemplate.get("open_title").toString());
			MDataMap one = DbUp.upTable("hp_music_album").one("album_id",album_id);
			if(one!=null&&one.size()>0) {
				apiImgDetailsResult.setBmgId(Integer.parseInt(one.get("bmg_id")));
			}
			List<Map<String,Object>> albumtemplateimgmaps = DbUp.upTable("hp_music_album_img").dataSqlList("SELECT * FROM hp_music_album_img WHERE album_id =:album_id and review_state = '1' and is_valid = '1' order by img_sort", mWhereMap);
			List<UserTemplateImgsResult> user_template_imgs = apiImgDetailsResult.getUser_template_imgs();
			Map<String,Object> map1 = null;
			int i = 999999999;
			for(Map<String,Object> map : albumtemplateimgmaps) {
				UserTemplateImgsResult img = new UserTemplateImgsResult();
				img.setImg_id(map.get("img_id").toString());
				int j = Integer.parseInt(map.get("img_sort").toString());
				if(j < i) {
					i = j;
					map1 = map;
				}
				img.setImg_sort(j);
				img.setImg_url(map.get("main_img").toString());
				img.setImg_preview_url(map.get("preview_img").toString());
				user_template_imgs.add(img);
			}
			if(map1 != null) {
				apiImgDetailsResult.setShare_img(map1.get("share_img").toString());
			}
			
		}
		return apiImgDetailsResult;
	}
}
