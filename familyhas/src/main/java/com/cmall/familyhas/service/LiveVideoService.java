package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.model.LiveControlInfo;
import com.cmall.familyhas.model.LiveRoom;
import com.cmall.familyhas.model.LiveRoomBaseInfo;
import com.cmall.familyhas.model.LiveRoomDataInfo;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;




/**
 * 惠家有直播&视频service
 * @author zhangbo
 *
 */
public class LiveVideoService extends BaseClass{ 

     
	
	public String getLiveRoomInfos(String live_room_id) {
		LiveRoom liveRoom = new LiveRoom();
		
		MDataMap one = DbUp.upTable("lv_live_room").one("live_room_id",live_room_id);
		if(one!=null) {
			LiveRoomBaseInfo liveRoomBaseInfo =new LiveRoomBaseInfo();
			liveRoomBaseInfo.setAnchorNickname(one.get("anchor_nickname"));
			liveRoomBaseInfo.setAnchorPhone(one.get("anchor_phone"));
			//优先获取日志中真实记录的开播时间，没有则获取配置的时间
			//List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_live_room_log").dataSqlList("select * from lc_live_room_log where start_time>DATE_SUB(NOW(),INTERVAL 24 HOUR) order by zid asc limit 0,1", null);
			List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_live_room_log").dataSqlList("select * from lc_live_room_log where live_room_id=:live_room_id order by zid asc limit 0,1", new MDataMap("live_room_id",live_room_id));
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				String dateToChinese = DateUtil.converDateToChinese(map.get("start_time").toString());
				liveRoomBaseInfo.setStartTime(dateToChinese);
			}
			else if(StringUtils.isNotBlank(one.get("start_time"))) {
				String dateToChinese = DateUtil.converDateToChinese(one.get("start_time"));
				liveRoomBaseInfo.setStartTime(dateToChinese);
			}
			if(StringUtils.isNotBlank(one.get("end_time"))) {
				String dateToChinese = DateUtil.converDateToChinese(one.get("end_time"));
				liveRoomBaseInfo.setEndTime(dateToChinese);
			}
			liveRoomBaseInfo.setLiveCoverPicture(one.get("live_cover_picture"));
			liveRoomBaseInfo.setLiveRoomId(one.get("live_room_id"));
			liveRoomBaseInfo.setLiveTitle(one.get("live_title"));
			int count = DbUp.upTable("lv_live_room_product").count("live_room_id",live_room_id,"delete_flag","1");
			liveRoomBaseInfo.setProductNum(count);
			liveRoom.setLiveRoomBaseInfo(liveRoomBaseInfo);
			
			LiveRoomDataInfo liveRoomDataInfo =  new LiveRoomDataInfo();
			Map<String, Object> dataSqlOne = DbUp.upTable("lv_live_room_behavior_statistics").dataSqlOne("select IFNULL(sum(case when behavior_type='449748630001' then 1 else 0 end ),0) seepepole, IFNULL(sum(case when behavior_type='449748630001' then num else 0 end ),0) seetimes,IFNULL(sum(case when behavior_type='449748630002' then 1 else 0 end ),0) likepeople,"
					+ " IFNULL(sum(case when behavior_type='449748630002' then num else 0 end ),0) liketimes, IFNULL(sum(case when behavior_type='449748630003' then 1 else 0 end ),0) commentspeople, IFNULL(sum(case when behavior_type='449748630003' then num else 0 end ),0) commentstimes  from lv_live_room_behavior_statistics "
					+ " where live_room_id='"+live_room_id+"' ", null);
			Map<String, Object> dataSqlOne2 = DbUp.upTable("lv_live_room_product_statistics").dataSqlOne("select IFNULL(sum(case when behavior_type='449748620001' then 1 else 0 end ),0) productclickpeople, IFNULL(sum(case when behavior_type='449748620001' then num else 0 end ),0) productclicktimes "
					+ " from lv_live_room_product_statistics  where live_room_id='"+live_room_id+"'", null);
			
			Map<String, Object> dataSqlOne3 = DbUp.upTable("lv_live_room_orders").dataSqlOne("select IFNULL(count(l.order_code),0) ordernum,IFNULL(sum(l.order_money),0) ordermoney from livevideo.lv_live_room_orders l,ordercenter.oc_orderinfo o where  l.live_room_id='"+live_room_id+"' and l.order_code= o.order_code and o.order_status!='4497153900010001' and o.order_status!='4497153900010006' ",null);

			liveRoomDataInfo.setCommentsPeople(Integer.parseInt(dataSqlOne.get("commentspeople").toString()));
			liveRoomDataInfo.setCommentsTimes(Integer.parseInt(dataSqlOne.get("commentstimes").toString()));
			liveRoomDataInfo.setLikePeople(Integer.parseInt(dataSqlOne.get("likepeople").toString()));
			liveRoomDataInfo.setLikeTimes(Integer.parseInt(dataSqlOne.get("liketimes").toString()));
			liveRoomDataInfo.setOrderMoney(new BigDecimal(dataSqlOne3.get("ordermoney").toString()));
			liveRoomDataInfo.setOrderNum(Integer.parseInt(dataSqlOne3.get("ordernum").toString()));
			liveRoomDataInfo.setProductClickPeople(Integer.parseInt(dataSqlOne2.get("productclickpeople").toString()));
			liveRoomDataInfo.setProductClickTimes(Integer.parseInt(dataSqlOne2.get("productclicktimes").toString()));
			liveRoomDataInfo.setSeePepole(Integer.parseInt(dataSqlOne.get("seepepole").toString()));
			liveRoomDataInfo.setSeeTimes(Integer.parseInt(dataSqlOne.get("seetimes").toString()));
			liveRoom.setLiveRoomDataInfo(liveRoomDataInfo);
			
			LiveControlInfo liveControlInfo =  new LiveControlInfo();
			 
			liveControlInfo.setIfStopComment("0".equals(one.get("if_stop_comment"))?"开启":"关闭");
			liveControlInfo.setIfStopReplay("0".equals(one.get("if_stop_replay"))?"开启":"关闭");
			liveControlInfo.setLiveStatus(DbUp.upTable("sc_define_other").one("define_code",one.get("live_status")).get("define_name"));
			
			if(StringUtils.isNotBlank(one.get("live_time"))) {
				String  live_time= DateUtil.converSecondsToDate(Integer.parseInt(one.get("live_time")));
				liveControlInfo.setLiveTime(live_time);
			}else {
				//开始时间到现在的秒数
				String start_time= one.get("start_time");
				String nowTime = DateUtil.getNowTime();
				int liveSeconds = DateUtil.timeSubtraction(start_time, nowTime, "yyyy-MM-dd HH:mm:ss");
				String  live_time= DateUtil.converSecondsToDate(liveSeconds);
				liveControlInfo.setLiveTime(live_time);
			}
			liveRoom.setLiveControlInfo(liveControlInfo);	
		}
		
		return  JSON.toJSONString(liveRoom);
	}
	
	
	
	//获取用户姓名
	public String getUserName(String userCode) {
		MDataMap one = DbUp.upTable("za_userinfo").one("user_code",userCode);
		return one==null?"":one.get("real_name");
	}
	
	




	
}
