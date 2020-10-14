package com.cmall.familyhas.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.CouponService;
import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.ordercenter.service.money.ReturnMoneyResult;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobCollageCancelOrd extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		FlowBussinessService fService = new FlowBussinessService();
		CreateMoneyService createMoneyService = new CreateMoneyService();
		CouponService couponService = new CouponService();
		/**
		 * 正常超时的拼团单列表
		 */
		String sql1 = "SELECT info.uid,info.order_code,info.order_status,m.collage_code,m.is_confirm FROM ordercenter.oc_orderinfo info,sc_event_collage_item m,sc_event_collage c WHERE info.order_code = m.collage_ord_code AND c.collage_code = m.collage_code AND c.expire_time <= sysdate() AND c.collage_status = '449748300001'";
		List<Map<String, Object>> list1 = DbUp.upTable("sc_event_collage_item").dataSqlList(sql1, new MDataMap());
		/**
		 * 时间未超时，活动作废或是商品作废的拼团列表
		 */
		String sql2 = "SELECT info.uid,info.order_code,info.order_status,m.collage_code,m.is_confirm FROM ordercenter.oc_orderinfo info,sc_event_collage_item m,sc_event_collage c,sc_event_info i WHERE info.order_code = m.collage_ord_code AND c.collage_code = m.collage_code AND c.event_code = i.event_code AND c.collage_status = '449748300001' AND c.expire_time > sysdate() AND i.event_status = '4497472700020003'";
		List<Map<String, Object>> list2 = DbUp.upTable("sc_event_collage_item").dataSqlList(sql2, new MDataMap());
		/**
		 * 活动未作废，时间未超时，拼团商品作废的拼团列表
		 */
		List<Map<String,Object>> list3 = this.getZfList();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.addAll(list1);
		list.addAll(list2);
		list.addAll(list3);
		for(Map<String, Object> map : list) {
			String isConfirm = MapUtils.getString(map, "is_confirm", "");
			String operater = "pt_system";
			String flowBussinessUid = MapUtils.getString(map, "uid", "");
			String fromStatus = MapUtils.getString(map, "order_status", "");
			String orderCode = MapUtils.getString(map, "order_code", "");
			String toStatus = "4497153900010006";
			String flowType = "449715390008";
			
			updateCollageStatus(MapUtils.getString(map, "collage_code", ""));
			RootResult ret = fService.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, operater, "拼团活动时间已结束，未成团的订单自动取消", new MDataMap("order_code", orderCode));
			if("449748320002".equals(isConfirm)) {//已确认(已付款)
				if (ret.getResultCode() == 1) {
					//生成退款单
					ReturnMoneyResult rm = createMoneyService.creatReturnMoney(orderCode, operater, "拼团活动时间已结束，未成团的订单自动取消");
					if(rm.getList() != null && rm.getList().size() > 0) {
						couponService.reWriteGiftVoucherToLD(rm.getList(), "R"); //取消发货回写礼金券给LD
					}
				}else{
					WebHelper.errorMessage(orderCode, "cancelOrder", 1, "cancelOrder on ChangeFlow", ret.getResultMessage(), null);
				}
			}
		}
		/**
		 * 机器人开团为成团的重置拼团失败
		 */
		String robotSql = "SELECT * FROM systemcenter.sc_event_collage WHERE expire_time <= sysdate() AND collage_status = '449748300001'";
		List<Map<String,Object>> robotList = DbUp.upTable("sc_event_collage").dataSqlList(robotSql, null);
		if(robotList != null&&robotList.size()>0) {
			for(Map<String,Object> map : robotList) {
				if(map == null) {
					continue;
				}
				String collageCode = MapUtils.getString(map, "collage_code", "");
				String sqlItem = "SELECT * FROM systemcenter.sc_event_collage_item WHERE collage_code = '"+collageCode+"' AND collage_ord_code like 'DD%'";
				List<Map<String,Object>> maps = DbUp.upTable("sc_event_collage_item").dataSqlList(sqlItem, null);
				if(maps == null || maps.size() == 0) {//确保该团下面没有DD订单，是纯虚拟用户开团未成团的团，只做团状态变更。
					updateCollageStatus(collageCode);
				}
			}
		}
	}
	/**
	 * 获取作废商品List
	 * @return
	 */
	private List<Map<String, Object>> getZfList() {
		String sql2 = "SELECT i.event_code eventCode, m.product_code productCode,info.uid,info.order_code,info.order_status,m.collage_code,m.is_confirm FROM ordercenter.oc_orderinfo info left join sc_event_collage_item m on info.order_code = m.collage_ord_code left join sc_event_collage c on m.collage_code = c.collage_code left join sc_event_info i on c.event_code = i.event_code WHERE c.collage_status = '449748300001' AND c.expire_time > sysdate() AND i.event_status in ('4497472700020002','4497472700020004') group by info.order_code";
		List<Map<String,Object>> maps = DbUp.upTable("sc_event_collage_item").dataSqlList(sql2, null);
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : maps) {
			if(map == null) {
				continue;
			}
			String eventCode = map.get("eventCode")!=null?map.get("eventCode").toString():"";
			String productCode = map.get("productCode")!=null?map.get("productCode").toString():"";
			MDataMap hasOther = DbUp.upTable("sc_event_item_product").one("event_code",eventCode,"product_code",productCode,"flag_enable","1");
			if(hasOther == null || hasOther.isEmpty()) {//没有商品了
				results.add(map);
			}
		}
		return results;
	}

	private void updateCollageStatus(String collageCode) {
		if(!"".equals(collageCode)) {
			DbUp.upTable("sc_event_collage").dataUpdate(new MDataMap("collage_status", "449748300003", "collage_code", collageCode), "collage_status", "collage_code");
		}
	}
}
