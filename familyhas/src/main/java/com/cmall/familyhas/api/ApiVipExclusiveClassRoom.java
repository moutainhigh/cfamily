package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiVipExclusiveClassRoomInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveClassRoomResult;
import com.cmall.familyhas.model.VipClassRoom;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusiveClassRoom extends RootApiForToken<ApiVipExclusiveClassRoomResult, ApiVipExclusiveClassRoomInput> {
	
	private static final String VIP_TYPE = "449748130005";
	
	@SuppressWarnings("deprecation")
	@Override
	public ApiVipExclusiveClassRoomResult Process(ApiVipExclusiveClassRoomInput inputParam, MDataMap mRequestMap) {
		String searchKey = inputParam.getSearchKey();//搜索关键词
		int page = inputParam.getPage() - 1;//当前页数
		int pageCount = inputParam.getPageCount();//每页记录个数
		int start = page * pageCount;
		
		//查询符合条件的会员课堂
		String dataHeadSql = "select d.uid, d.title, d.pic_url, d.vcr_url";
		String countHeadSql = "select count(1)";
		String commonSql = " from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '" + VIP_TYPE + "' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate()";
		if(!"".equals(searchKey)) {
			commonSql += " and d.title like '%" + searchKey + "%'";
		}
		String dataSql = dataHeadSql + commonSql + " order by d.seq limit " + start + "," + pageCount;
		List<Map<String, Object>> list = DbUp.upTable("fh_apphome_channel").dataSqlList(dataSql, new MDataMap());
		
		String countSql = countHeadSql + commonSql;
		int totalCount = DbUp.upTable("fh_apphome_channel").upTemplate().queryForInt(countSql, new HashMap<String, Object>());
		
		//格式化会员课堂数据
		List<VipClassRoom> roomList = new ArrayList<VipClassRoom>();
		for(Map<String, Object> map : list) {
			VipClassRoom room = new VipClassRoom();
			room.setInfoId(map.get("uid") == null ? "" : map.get("uid").toString());
			room.setTitle(map.get("title") == null ? "" : map.get("title").toString());
			room.setPicUrl(map.get("pic_url") == null ? "" : map.get("pic_url").toString());
			room.setVcrUrl(map.get("vcr_url") == null ? "" : map.get("vcr_url").toString());
			roomList.add(room);
		}
		
		ApiVipExclusiveClassRoomResult result = new ApiVipExclusiveClassRoomResult();
		result.setTotalPage(totalCount);
		result.setVipRoomList(roomList);
		
		//添加微信分享信息
		String shareSql = "select c.channel_name, c.share_title, c.is_share, c.share_desc, c.share_pic from fh_apphome_channel c where c.channel_type = '" + VIP_TYPE + "' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> shareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(shareSql, new MDataMap());
		if(shareMap != null) {
			result.setChannelName(shareMap.get("channel_name") == null ? "" : shareMap.get("channel_name").toString());
			result.setShareTitle(shareMap.get("share_title") == null ? "" : shareMap.get("share_title").toString());
			result.setIsShare(shareMap.get("is_share") == null ? "" : shareMap.get("is_share").toString());
			result.setShareDesc(shareMap.get("share_desc") == null ? "" : shareMap.get("share_desc").toString());
			result.setSharePic(shareMap.get("share_pic") == null ? "" : shareMap.get("share_pic").toString());
		}
		return result;
	}
}
