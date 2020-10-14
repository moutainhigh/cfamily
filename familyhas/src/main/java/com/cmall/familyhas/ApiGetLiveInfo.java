package com.cmall.familyhas;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiGetLiveInfoInput;
import com.cmall.familyhas.api.result.ApiGetLiveInfoResult;
import com.cmall.familyhas.api.result.ApiGoodsBeanResult;
import com.cmall.familyhas.api.result.ApiReplayResult;
import com.cmall.familyhas.api.result.ApiRoomInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiGetLiveInfo extends RootApiForVersion<ApiGetLiveInfoResult, ApiGetLiveInfoInput> {

	@Override
	public ApiGetLiveInfoResult Process(ApiGetLiveInfoInput inputParam, MDataMap mRequestMap) {
		int pageNum = inputParam.getPageNum();
		pageNum = pageNum - 1;
		int pageSize = inputParam.getPageSize();
		pageNum = pageNum*pageSize;
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("status", "449746250001");
		ApiGetLiveInfoResult apiGetLiveInfoResult = new ApiGetLiveInfoResult();
		StringBuilder builder = new StringBuilder();
		builder.append("select * from ((SELECT * from (select * FROM fh_liveinfo fh1 WHERE  fh1.live_status in('101','105') AND fh1.`status` = '449746250001' order BY fh1.start_time DESC) AS a1)");
		builder.append(" UNION");
		builder.append(String.format(" (SELECT * from (select * from fh_liveinfo fh2 where fh2.live_status in ('102','103') AND fh2.`status` = '449746250001' order BY fh2.live_status,fh2.start_time DESC) AS a2)) AS a3 LIMIT %s,%s",pageNum,pageSize));
		String sql = builder.toString();
		List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_liveinfo").dataSqlList(sql, mWhereMap);
		for(Map<String,Object> map : dataSqlList) {
			ApiRoomInfoResult apiRoomInfoResult = new ApiRoomInfoResult();
			String roomid = MapUtils.getString(map, "roomid");
			apiRoomInfoResult.setName(MapUtils.getString(map, "name"));
			apiRoomInfoResult.setRoomid(roomid);
			apiRoomInfoResult.setCover_img(MapUtils.getString(map, "cover_img"));
			apiRoomInfoResult.setLive_status(MapUtils.getString(map, "live_status"));
			apiRoomInfoResult.setStart_time(MapUtils.getString(map, "start_time"));
			apiRoomInfoResult.setEnd_time(MapUtils.getString(map, "end_time"));
			apiRoomInfoResult.setAnchor_name(MapUtils.getString(map, "anchor_name"));
			apiRoomInfoResult.setAnchor_img(MapUtils.getString(map, "anchor_img"));
			sql = "select * from fh_livegoodinfo where roomid =:roomid";
			mWhereMap.put("roomid", roomid);
			List<Map<String,Object>> livegoods = DbUp.upTable("fh_livegoodinfo").dataSqlList(sql, mWhereMap);
			for(Map<String,Object> goodMap : livegoods) {
				ApiGoodsBeanResult apiGoodsBeanResult = new ApiGoodsBeanResult();
				apiGoodsBeanResult.setCover_img(MapUtils.getString(goodMap, "cover_img"));
				apiGoodsBeanResult.setUrl(MapUtils.getString(goodMap, "url"));
				apiGoodsBeanResult.setPrice(MapUtils.getString(goodMap, "price"));
				apiGoodsBeanResult.setName(MapUtils.getString(goodMap, "name"));
				apiGoodsBeanResult.setPrice2(MapUtils.getString(goodMap, "price_two"));
				apiGoodsBeanResult.setPrice_type(MapUtils.getString(goodMap, "price_type"));
				apiRoomInfoResult.getGoods().add(apiGoodsBeanResult);
			}
			sql = "select * from fh_livereplay where roomid =:roomid";
			List<Map<String,Object>> livereplays = DbUp.upTable("fh_livereplay").dataSqlList(sql, mWhereMap);
			for(Map<String,Object> replayMap : livereplays) {
				ApiReplayResult apiReplayResult = new ApiReplayResult();
				apiReplayResult.setExpire_time(MapUtils.getString(replayMap, "expire_time"));
				apiReplayResult.setCreate_time(MapUtils.getString(replayMap, "create_time"));
				apiReplayResult.setMedia_url(MapUtils.getString(replayMap, "media_url"));
				apiRoomInfoResult.getReplays().add(apiReplayResult);
			}
			apiGetLiveInfoResult.getRoomInfo().add(apiRoomInfoResult);
		}
		builder = new StringBuilder();
		builder.append("select count(*) as count from ((SELECT * from (select * FROM fh_liveinfo fh1 WHERE  fh1.live_status in('101','105') AND fh1.`status` = '449746250001' order BY fh1.start_time DESC) AS a1)");
		builder.append(" UNION");
		builder.append(" (SELECT * from (select * from fh_liveinfo fh2 where fh2.live_status in ('102','103') AND fh2.`status` = '449746250001' order BY fh2.live_status,fh2.start_time DESC) AS a2)) AS a3");
		Map<String, Object> dataSqlOne = DbUp.upTable("fh_liveinfo").dataSqlOne(builder.toString(), mWhereMap);
		Integer dataCount = MapUtils.getInteger(dataSqlOne, "count");
		int totalPage = 0;
		if(dataCount%pageSize == 0) {
			totalPage = dataCount/pageSize;
		}else {
			totalPage = dataCount/pageSize + 1;
		}
		apiGetLiveInfoResult.setTotalPage(totalPage);
		apiGetLiveInfoResult.setTotal(dataCount);
		return apiGetLiveInfoResult;
	}

}
