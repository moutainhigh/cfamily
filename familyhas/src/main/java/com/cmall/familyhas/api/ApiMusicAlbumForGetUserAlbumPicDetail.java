package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.cmall.familyhas.api.input.ApiMusicAlbumForGetUserAlbumPicDetailInput;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.AlbumPicObj;
import com.srnpr.zapweb.webapi.ApiMusicAlbumForGetUserAlbumPicDetailResult;
import com.srnpr.zapweb.webapi.PicListObj;

/**
 * 微信小程序-音乐相册 获取个人上传图片详细信息
 *
 * 2019/09/25 10:07:00
 *
 * zhangbo
 *
 */

public class ApiMusicAlbumForGetUserAlbumPicDetail
		extends RootApi<ApiMusicAlbumForGetUserAlbumPicDetailResult, ApiMusicAlbumForGetUserAlbumPicDetailInput> {
	
	@Override
	public ApiMusicAlbumForGetUserAlbumPicDetailResult Process(ApiMusicAlbumForGetUserAlbumPicDetailInput inputParam,
			MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiMusicAlbumForGetUserAlbumPicDetailResult result = new ApiMusicAlbumForGetUserAlbumPicDetailResult();
		String openId = inputParam.getOpenId();
		int pageSize = inputParam.getPageSize();
		int nextPage = inputParam.getNextPage();
		int totalPage= 1;
		int start = pageSize*(nextPage-1);
		int count = DbUp.upTable("hp_music_album_user_pic").dataCount("open_id=:open_id", new MDataMap("open_id",openId));
		totalPage = count/pageSize;
		if(count%pageSize!=0) {totalPage++;}
		List<Map<String, Object>> list = DbUp.upTable("hp_music_album_user_pic").dataSqlList(
				"select * from hp_music_album_user_pic where review_state='1' and open_id=:open_id order by create_time desc limit "+start+","+pageSize+"", new MDataMap("open_id", openId));
		Map<String, List<AlbumPicObj>> mapList = new HashMap<String, List<AlbumPicObj>>();
		for (Map<String, Object> map : list) {
			String subTime = converTimeStr(map.get("create_time").toString());
			if (mapList.containsKey(subTime)) {
				AlbumPicObj albumPicObj = new AlbumPicObj();
				albumPicObj.setImg_preview_url(map.get("preview_img").toString());
				albumPicObj.setImg_url(map.get("main_img").toString());
				albumPicObj.setImg_id(map.get("img_id").toString());
				mapList.get(subTime).add(albumPicObj);
			} else {
				ArrayList<AlbumPicObj> subList = new ArrayList<AlbumPicObj>();
				AlbumPicObj albumPicObj = new AlbumPicObj();
				albumPicObj.setImg_preview_url(map.get("preview_img").toString());
				albumPicObj.setImg_url(map.get("main_img").toString());
				albumPicObj.setImg_id(map.get("img_id").toString());
				subList.add(albumPicObj);
				mapList.put(subTime, subList);
			}
		}
		if(mapList.size()>0) {
			Set<String> keySet = mapList.keySet();
			for (String string : keySet) {
				List<AlbumPicObj> subList = mapList.get(string);
				PicListObj picListObj = new PicListObj();
				picListObj.setOperateTime(string);
				for (AlbumPicObj subAlbumPicObj : subList) {
					picListObj.getPicList().add(subAlbumPicObj);
				}
				result.getPicListObj().add(picListObj);
			}
		}
		result.setPicListObj(sortList(result));
		result.setTotalPage(totalPage);
		return result;
	}
	
	private static List<PicListObj> sortList(ApiMusicAlbumForGetUserAlbumPicDetailResult result) {
		List<PicListObj> picListObjList = result.getPicListObj();
			Collections.sort(picListObjList, new Comparator<PicListObj>() {
				@Override
				public int compare(PicListObj o1, PicListObj o2) {
					String time1 = o1.getOperateTime();
					String time2 = o2.getOperateTime();
					return time2.compareTo(time1);
				}
			});

		return picListObjList ;
	}

	String converTimeStr(String timeStr) {
		String newTime = "";
		if (StringUtils.isNotBlank(timeStr)) {
			newTime += timeStr.substring(0, 4) + "年";
			newTime += timeStr.substring(5, 7) + "月";
			newTime += timeStr.substring(8, 10) + "日";
		}
		return newTime;
	}
}

