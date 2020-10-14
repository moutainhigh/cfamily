package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetLiveRoomListInput;
import com.cmall.familyhas.api.model.LiveProd;
import com.cmall.familyhas.api.model.LiveRoom;
import com.cmall.familyhas.api.result.ApiForGetLiveRoomListResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取直播房间列表接口
 * @author lgx
 *
 */
public class ApiForGetLiveRoomList extends RootApiForVersion<ApiForGetLiveRoomListResult, ApiForGetLiveRoomListInput> {

	@Override
	public ApiForGetLiveRoomListResult Process(ApiForGetLiveRoomListInput inputParam, MDataMap mRequestMap) {
		ApiForGetLiveRoomListResult result = new ApiForGetLiveRoomListResult();
		String channelId = getChannelId();
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		int page = inputParam.getPage();
		// 起始索引
		int index = (page-1) * 10;
		
		int totalPage = 0;
		List<LiveRoom> liveRoomList = new ArrayList<LiveRoom>();
		
		String roomSql = "SELECT s.* FROM ( " + 
				"	SELECT r1.* FROM (SELECT lr1.live_room_id, lr1.live_title, lr1.live_status, lr1.start_time, lr1.live_cover_picture, '' file_id, '' video_url, '' i_start_time, 1 seq FROM lv_live_room lr1 WHERE lr1.is_delete = '0' AND lr1.live_status = '449746320003' ORDER BY lr1.start_time) r1 " + 
				"	UNION ALL " + 
				"	SELECT r2.* FROM (SELECT lr2.live_room_id, lr2.live_title, lr2.live_status, lr2.start_time, lr2.live_cover_picture, '' file_id, '' video_url, '' i_start_time, 2 seq FROM lv_live_room lr2 WHERE lr2.is_delete = '0' AND lr2.live_status = '449746320001' ORDER BY lr2.start_time) r2 " + 
				"	UNION ALL " + 
				"	SELECT r3.* FROM (SELECT lr3.live_room_id, lr3.live_title, lr3.live_status, lr3.start_time, lr3.live_cover_picture, lr3.file_id file_id, lr3.video_url, lr3.i_start_time, 3 seq " + 
				"		FROM (SELECT r.*, i.video_url, i.file_id, i.start_time i_start_time FROM lv_live_room r LEFT JOIN lv_live_room_replay_infos i ON r.live_room_id = i.live_room_id " + 
				"				WHERE r.is_delete = '0' AND r.live_status = '449746320002' AND r.if_stop_replay = '0' ) lr3 ORDER BY lr3.start_time DESC, lr3.i_start_time ASC) r3 " + 
				" ) s LIMIT "+index+",10 ";
		List<Map<String, Object>> roomList = DbUp.upTable("lv_live_room").dataSqlList(roomSql, new MDataMap());
		if(roomList != null && roomList.size() > 0) {
			for (Map<String, Object> roomMap : roomList) {
				LiveRoom liveRoom = new LiveRoom();
				String liveRoomId = MapUtils.getString(roomMap, "live_room_id");
				String liveStatus = MapUtils.getString(roomMap, "live_status");
				// 查询观看人数(直播中和回放?)
				int lookNum = 0;
				String lookSql = "SELECT sum(num) lookNum FROM lv_live_room_behavior_statistics WHERE live_room_id = '"+liveRoomId+"' AND behavior_type = '449748630001'";
				 Map<String, Object> lookMap = DbUp.upTable("lv_live_room_behavior_statistics").dataSqlOne(lookSql, new MDataMap());
				 if(lookMap != null) {
					 lookNum = MapUtils.getIntValue(lookMap, "lookNum");
				 }
				
				// 直播商品总数
				int liveRoomProdNum = DbUp.upTable("lv_live_room_product").dataCount("live_room_id = '"+liveRoomId+"' and delete_flag = '1'", new MDataMap());
				
				// 查询直播间商品信息(按顺序只查前10个)
				List<LiveProd> liveProdList = new ArrayList<LiveProd>();
				String liveProdSql = "SELECT * FROM lv_live_room_product WHERE live_room_id = '"+liveRoomId+"' AND delete_flag = '1' ORDER BY sort DESC LIMIT 4";
				List<Map<String, Object>> liveProdMapList = DbUp.upTable("lv_live_room_product").dataSqlList(liveProdSql, new MDataMap());
				if(liveProdMapList != null && liveProdMapList.size() > 0) {
					for (Map<String, Object> map : liveProdMapList) {
						LiveProd liveProd = new LiveProd();
						
						String productCode = MapUtils.getString(map, "product_code");
						PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
						
						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						List<String> productCodeArr = new ArrayList<String>();
						productCodeArr.add(productCode);
						skuQuery.setCode(StringUtils.join(productCodeArr, ","));
						skuQuery.setMemberCode(memberCode);
						skuQuery.setIsPurchase(1);
						skuQuery.setChannelId(channelId);
						// 获取商品最低销售价格和对应的划线价
						Map<String, PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
						if (null != productPriceMap.get(productCode)) {
							BigDecimal sellPrice1 = productPriceMap.get(productCode).getSellPrice();
							// 销售价
							liveProd.setSellPrice(MoneyHelper.format(sellPrice1));
						} else {
							// 销售价
							liveProd.setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
						}
						
						liveProd.setMainpicUrl(productInfo.getMainpicUrl());
						
						liveProdList.add(liveProd);
					}
				}
				
				String start_time = MapUtils.getString(roomMap, "start_time");
				start_time = start_time.substring(5, 7)+"月"+start_time.substring(8, 10)+"日 "+start_time.substring(11, 16);
				start_time = start_time.replaceAll("^(0+)", "");
				liveRoom.setLiveProdList(liveProdList);
				liveRoom.setLookNum(lookNum);
				liveRoom.setLiveRoomProdNum(liveRoomProdNum);
				liveRoom.setLiveRoomId(liveRoomId);
				liveRoom.setLiveTitle(MapUtils.getString(roomMap, "live_title"));
				liveRoom.setLiveStatus(liveStatus);
				liveRoom.setStartTime(start_time);
				liveRoom.setLiveCoverPicture(MapUtils.getString(roomMap, "live_cover_picture"));
				liveRoom.setFileId(MapUtils.getString(roomMap, "file_id"));
				liveRoom.setVideoUrl(MapUtils.getString(roomMap, "video_url"));
				
				liveRoomList.add(liveRoom);
			}
		}
		
		String countSql = "SELECT count(1) num FROM ( " + 
				"	SELECT r1.* FROM (SELECT lr1.live_room_id, lr1.live_title, lr1.live_status, lr1.start_time, lr1.live_cover_picture, '' file_id, '' video_url, '' i_start_time, 1 seq FROM lv_live_room lr1 WHERE lr1.is_delete = '0' AND lr1.live_status = '449746320003' ) r1 " + 
				"	UNION ALL " + 
				"	SELECT r2.* FROM (SELECT lr2.live_room_id, lr2.live_title, lr2.live_status, lr2.start_time, lr2.live_cover_picture, '' file_id, '' video_url, '' i_start_time, 2 seq FROM lv_live_room lr2 WHERE lr2.is_delete = '0' AND lr2.live_status = '449746320001' ) r2 " + 
				"	UNION ALL " + 
				"	SELECT r3.* FROM (SELECT lr3.live_room_id, lr3.live_title, lr3.live_status, lr3.start_time, lr3.live_cover_picture, lr3.file_id file_id, lr3.video_url, lr3.i_start_time, 3 seq " + 
				"		FROM (SELECT r.*, i.video_url, i.file_id, i.start_time i_start_time FROM lv_live_room r LEFT JOIN lv_live_room_replay_infos i ON r.live_room_id = i.live_room_id " + 
				"				WHERE r.is_delete = '0' AND r.live_status = '449746320002' AND r.if_stop_replay = '0') lr3 ) r3 " + 
				" ) s ";
		Map<String, Object> countMap = DbUp.upTable("lv_live_room").dataSqlOne(countSql, new MDataMap());
		double num = MapUtils.getDoubleValue(countMap, "num");
		totalPage = (int) Math.ceil(num/10);
		
		result.setTotalPage(totalPage);
		result.setLiveRoomList(liveRoomList);
		
		return result;
	}

}
