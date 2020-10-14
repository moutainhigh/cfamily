package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 拼团服务类
 * @author Angel Joy
 *
 */
public class CollageService extends BaseClass {

	/**
	 * @param collageCode 拼团编号
	 * 机器人补团公共方法
	 * @return
	 */
	public boolean robotComplateCollage(String collageCode) {
		MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
		MDataMap itemInfo = DbUp.upTable("sc_event_collage_item").one("collage_code",collageCode,"collage_member_type","449748310001");
		String orderCode = itemInfo.get("collage_ord_code");
		String eventCode = collageInfo.get("event_code");
		if(orderCode.contains("X")) {//机器人开团的，不做自动拼团操作
			return false;
		}
		PlusModelEventQuery tQuery = new PlusModelEventQuery();
		tQuery.setCode(eventCode);
		PlusModelEventInfo plusEventInfo = new LoadEventInfo().upInfoByCode(tQuery);
		if(plusEventInfo == null) {
			return false;
		}
		Integer collagePersonCount = Integer.parseInt(plusEventInfo.getCollagePersonCount());
		Integer count = DbUp.upTable("sc_event_collage_item").count("collage_code",collageCode,"is_confirm","449748320002");
		MDataMap collageItemOld = DbUp.upTable("sc_event_collage_item").one("collage_code",collageCode,"is_confirm","449748320002","collage_member_type","449748310001");
		if(collageItemOld == null || collageItemOld.isEmpty()) {
			return false;
		}
		Integer needCount = collagePersonCount - count;
		for(int i = 0;i < needCount; i++) {
			MDataMap collageItem = new MDataMap();
			collageItem.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
			collageItem.put("re_collage",collageItemOld.get("re_collage"));
			collageItem.put("is_confirm","449748320002");
			collageItem.put("collage_code",collageCode);
			String collageMember = this.getCollageMember();
			if(StringUtils.isEmpty(collageMember)) {
				continue;
			}
			collageItem.put("collage_member",collageMember);
			String collageOrdCode = this.getCollageOrdCode();
			collageItem.put("collage_ord_code",collageOrdCode);
			collageItem.put("collage_member_type","449748310002");
			collageItem.put("collage_time",DateUtil.getSysDateTimeString());
			collageItem.put("product_code",collageItemOld.get("product_code"));
			Integer countConfirm = DbUp.upTable("sc_event_collage_item").count("collage_code",collageCode,"is_confirm","449748320002");
			if(countConfirm < collagePersonCount) {//双重验证，防止这个过程中有新拼团人进来
				DbUp.upTable("sc_event_collage_item").dataInsert(collageItem);
			}
		}
		collageInfo.put("collage_status", "449748300002");//拼团成功
		DbUp.upTable("sc_event_collage").dataUpdate(collageInfo, "collage_status", "uid");
		return true;
	}
	
	/**
	 * 生成虚拟订单号
	 * @return
	 */
	public String getCollageOrdCode() {
		String orderCode = KvHelper.upCode("DD");
		orderCode = orderCode.replace("DD", "XN");
		return orderCode;
	}

	/**
	 * 获取虚拟拼团人编号
	 * @return
	 */
	public String getCollageMember() {
		String sql = "SELECT MAX(use_times) useTimes FROM mc_robot_info";
		Map<String,Object> map = DbUp.upTable("mc_robot_info").dataSqlOne(sql, null);
		if(map == null) {
			return "";
		}
		String useTimes = map.get("useTimes").toString();
		Integer use_times = Integer.parseInt(useTimes);
		String sql2 = "SELECT * FROM membercenter.mc_robot_info WHERE use_times != "+use_times;
		List<Map<String,Object>> robots = new ArrayList<Map<String,Object>>();
		robots = DbUp.upTable("mc_robot_info").dataSqlList(sql2, null);
		if(robots.size()==0) {
			use_times += 1;
			String sql3 =  "SELECT * FROM membercenter.mc_robot_info WHERE use_times != "+use_times;
			robots = DbUp.upTable("mc_robot_info").dataSqlList(sql3, null);
		}
		if(robots.size()==0) {
			return "";
		}
		Random rand = new Random();
		int randNum = 0;
		if(robots.size()>1) {
			randNum = rand.nextInt(robots.size()-1);
		}
		MDataMap robotInfo = new MDataMap(robots.get(randNum));
		robotInfo.put("use_times", use_times.toString());
		DbUp.upTable("mc_robot_info").dataUpdate(robotInfo, "use_times", "uid");
		return robotInfo.get("member_code");
	}
}
