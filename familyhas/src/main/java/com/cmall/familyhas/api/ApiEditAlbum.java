package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.input.ApiEditAlbumInput;
import com.cmall.familyhas.api.input.UserTemplateImgsInput;
import com.cmall.familyhas.api.result.ApiEditAlbumResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 相册编辑接口
 * @author 张圣瑞
 *
 */
public class ApiEditAlbum extends RootApiForVersion<ApiEditAlbumResult, ApiEditAlbumInput> {
	@Override
	public ApiEditAlbumResult Process(ApiEditAlbumInput inputParam, MDataMap mRequestMap) {
		ApiEditAlbumResult albumResult = new ApiEditAlbumResult();
		MDataMap mWhereMap = new MDataMap();
		String open_title = inputParam.getOpen_title().toString();
		String album_id = inputParam.getAlbum_id().toString();
		mWhereMap.put("album_id", album_id);
		String template_id = inputParam.getTemplate_id();
		int bmgId = inputParam.getBmgId();
		String openId = inputParam.getOpenId();
		if(StringUtils.isNotBlank(album_id)) {
			mWhereMap.put("use_flag", "Y");
			mWhereMap.put("template_id", template_id);
			mWhereMap.put("open_title", open_title);
			mWhereMap.put("update_time", FormatHelper.upDateTime());
			mWhereMap.put("bmg_id", bmgId+"");
			DbUp.upTable("hp_music_album").dataUpdate(mWhereMap, "use_flag,update_time,template_id,open_title,bmg_id", "album_id"); 
		}
		else {
			album_id = WebHelper.upCode("AM");
			 MDataMap mDataMap = new MDataMap();
			 mDataMap.put("open_id",openId);
			 mDataMap.put("album_id", album_id);
			 mDataMap.put("use_flag", "Y");
			 mDataMap.put("template_id", template_id);
			 mDataMap.put("bmg_id", bmgId+"");
			 mDataMap.put("uid", WebHelper.upUuid());
			 mDataMap.put("create_time", FormatHelper.upDateTime());
			 mDataMap.put("update_time",  FormatHelper.upDateTime());
			 DbUp.upTable("hp_music_album").dataInsert(mDataMap); 
		}
		//Map<String, Object> mapalbum = DbUp.upTable("hp_music_album").dataSqlOne("SELECT * FROM hp_music_album WHERE album_id =:album_id and use_flag = 'Y'", mWhereMap);

		
//		Map<String, Object> dataSqlOne = DbUp.upTable("hp_music_album").dataSqlOne("select hmat.uid as uid from hp_music_album hma INNER JOIN hp_music_album_template hmat where hma.template_id = hmat.template_id and album_id =:album_id", mWhereMap); 
		//String template_id = mapalbum.get("template_id").toString();
//		mWhereMap.clear();
//		mWhereMap.put("uid", dataSqlOne.get("uid").toString());
//		mWhereMap.put("template_id", template_id);
//		mWhereMap.put("open_title", open_title);
//		mWhereMap.put("update_time", FormatHelper.upDateTime());
//		DbUp.upTable("hp_music_album_template").dataUpdate(mWhereMap, "open_title,update_time,template_id", "uid");
		List<UserTemplateImgsInput> user_template_imgs = inputParam.getUser_template_imgs();
		mWhereMap.clear();
		mWhereMap.put("album_id", album_id);
		//删除原有的
		DbUp.upTable("hp_music_album_img").dataExec("delete from hp_music_album_img where album_id =:album_id ", mWhereMap);
		//添加最新的
		for (UserTemplateImgsInput userTemplateImgsInput : user_template_imgs) {
			String img_id = userTemplateImgsInput.getImg_id();
			String img_sort = userTemplateImgsInput.getImg_sort();
			MDataMap map = DbUp.upTable("hp_music_album_user_pic").one("img_id",img_id);
			map.remove("zid");
			map.remove("open_id");
			map.put("img_sort", img_sort);
			map.put("update_time", FormatHelper.upDateTime());
			map.put("create_time", FormatHelper.upDateTime());
			map.put("uid", WebHelper.upUuid());
			map.put("album_id", album_id);
			map.put("is_valid", "1");
			map.put("review_state","1");
			DbUp.upTable("hp_music_album_img").dataInsert(map);
		}
		
/*		List<Map<String,Object>> dataSqlList = DbUp.upTable("hp_music_album_img").dataSqlList("select * from hp_music_album_img where album_id =:album_id", mWhereMap);
		List<String> imgidlist = new ArrayList<String>();
		for(Map<String,Object> map : dataSqlList) {
			imgidlist.add(map.get("img_id").toString());
		}
		mWhereMap.clear();
		for(UserTemplateImgsInput u : user_template_imgs) {
			String img_id = u.getImg_id();
			String img_sort = u.getImg_sort();
			mWhereMap.put("img_id", img_id);
			mWhereMap.put("img_sort", img_sort);
			mWhereMap.put("is_valid", "1");
			mWhereMap.put("update_time", FormatHelper.upDateTime());
			DbUp.upTable("hp_music_album_img").dataUpdate(mWhereMap, "img_sort,update_time,is_valid", "img_id");
			imgidlist.remove(img_id);
		}
		mWhereMap.clear();
		mWhereMap.put("album_id", album_id);
		for(String imgid : imgidlist) {
			mWhereMap.put("img_id", imgid);
			DbUp.upTable("hp_music_album_img").dataExec("delete from hp_music_album_img where album_id =:album_id and img_id =:img_id", mWhereMap);
		}*/
		
		
		
		
		albumResult.setSuccess("true");
		albumResult.setAlbum_id(album_id);
		return albumResult;
	}

}
