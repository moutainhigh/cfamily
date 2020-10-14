package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.CollageService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForRobotCreateCollage extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		String sql = "SELECT * FROM systemcenter.sc_event_item_product WHERE event_code IN (SELECT event_code FROM systemcenter.sc_event_info WHERE end_time > sysdate() AND event_type_code = '4497472600010024' AND collage_type = '4497473400050001' AND event_status = '4497472700020002' AND begin_time < sysdate()) AND flag_enable = 1 AND sales_num > 0";
		List<Map<String,Object>> products = DbUp.upTable("sc_event_item_product").dataSqlList(sql, null);
		if(products == null || products.size() == 0) {
			return;
		}
		for(Map<String,Object> map : products) {
			MDataMap procudtItem = new MDataMap(map);
			String productCode = procudtItem.get("product_code");
			String eventCode = procudtItem.get("event_code");
			boolean flag = this.checkCollageByProductCode(productCode);//返回true，可以机器人开团。
			if(flag) {
				this.robotCreateCollage(productCode,eventCode);
			}
		}
	}

	/**
	 * 创建机器人虚拟开团数据
	 * @param productCode
	 */
	private void robotCreateCollage(String productCode,String eventCode) {
		MDataMap itemMap = new MDataMap();
		String collageCode = WebHelper.upCode("PT");
		PlusModelEventQuery tQuery = new PlusModelEventQuery();
		tQuery.setCode(eventCode);
		PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(tQuery);
		if(eventInfo == null) {
			return;
		}
		Integer collagePersonCount = Integer.parseInt(eventInfo.getCollagePersonCount());
		if(collagePersonCount == 0) {
			return;
		}
		Integer collageTimeLiness =  eventInfo.getCollageTimeLiness();
		if(collageTimeLiness == -1) {
			collageTimeLiness = 24;
		}
		String endTime = eventInfo.getEndTime();//获取活动结束时间
		String expireTime = "";
		Integer timeliness = eventInfo.getCollageTimeLiness();//获取拼团时效
		if(timeliness == -1||timeliness==null) {
			expireTime = endTime;
		}else {
			expireTime = DateUtil.addDateMinut(DateUtil.getSysDateTimeString(), timeliness);
		}
		if(!DateUtil.compareDateTime(expireTime, endTime)) {
			expireTime = endTime;
		}
		DbUp.upTable("sc_event_collage").dataInsert(new MDataMap("collage_code", collageCode, "event_code", eventCode, "collage_status", "449748300001", 
				"create_time", DateHelper.upNow(),"expire_time",expireTime));
		for(int i = 0;i<collagePersonCount-1;i++) {
			String memberType = "449748310002";//参团人
			if(i == 0) {
				memberType = "449748310001";//开团人
			}
			String userCode = new CollageService().getCollageMember();
			String orderCode = new CollageService().getCollageOrdCode();
			itemMap.put("collage_code", collageCode);//团编码
			itemMap.put("collage_member", userCode);//member_code
			itemMap.put("collage_ord_code", orderCode);//订单编号
			itemMap.put("collage_member_type", memberType);//拼团人类型
			itemMap.put("is_confirm", "449748320002");//已确认
			itemMap.put("collage_time", DateUtil.getSysDateTimeString());//拼团时间
			itemMap.put("product_code", productCode);//拼团时间
			DbUp.upTable("sc_event_collage_item").dataInsert(itemMap);
		}
	}

	/**
	 * 检查是否有拼团，返回true则没有拼团，符合机器人开团规则
	 * @param productCode
	 * @return
	 */
	private boolean checkCollageByProductCode(String productCode) {
		String sql = "SELECT * FROM systemcenter.sc_event_collage WHERE collage_status = '449748300001' AND collage_code IN (SELECT collage_code FROM systemcenter.sc_event_collage_item WHERE is_confirm = '449748320002' AND  product_code = '"+productCode+"')";
		List<Map<String,Object>> list = DbUp.upTable("sc_event_collage").dataSqlList(sql, null);
		if(list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

}
