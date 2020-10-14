package com.cmall.familyhas.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.WxGZHService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时检查是否有需要审核的售后单，给商户推送消息
 * @author Angel Joy
 * @date 2019-09-10 11:22:00
 * @desc 每天 10：00和17：00 执行
 */
public class JobForSendRemindCheckMessageToPartner extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		WxGZHService wxGZHservice = new WxGZHService();
		List<MDataMap> afterSaleOrders = DbUp.upTable("oc_return_goods").queryByWhere("status","4497153900050004");//4497153900050004 待商户审核（554之后，需要回寄商品的，只有用户维护了物流单号才会到待商户审核）
		for(MDataMap map : afterSaleOrders) {
			String return_code = map.get("return_code");
			MDataMap afterSaleMap = DbUp.upTable("oc_order_after_sale").one("asale_code",return_code);
			String time = "";
			if(afterSaleMap != null && !afterSaleMap.isEmpty()) {
				time = afterSaleMap.get("update_time");
			}
			String flah_return_goods = map.get("flag_return_goods");//是否需要回寄商品
			if("4497477800090001".equals(flah_return_goods)) {//需要回寄，查询物流单号，是否已签收，签收的更新oc_order_shipments 中if_receive字段 值4497471600480001 ：已签收
				MDataMap shipmentsMap = DbUp.upTable("oc_order_shipments").one("order_code",return_code);
				if(shipmentsMap == null || shipmentsMap.isEmpty()) {//数据为空，暂时不处理
					continue;
				}
				//查询此订单是否已经签收
				String if_receive = shipmentsMap.get("if_receive");
				if("4497471600480001".equals(if_receive)) {//已经签收的，发送通知。
					wxGZHservice.sendWxGZHRemindCheckAfterSale(map.get("small_seller_code"), return_code, map.get("order_code"),time,"售后审核提醒");
				}else {//没有签收的，需要去oc_express_detail表中查询是否已经签收，如果签收，更新oc_order_shipments表中的签收状态，并且发微信通知。
					String waybill = shipmentsMap.get("waybill");
					Integer count = DbUp.upTable("oc_express_detail").count("order_code",return_code,"waybill",waybill,"status","签收");//状态为签收的轨迹。
					if(count > 0) {//证明已经签收 更新 oc_order_shipments 中的 if_receive状态  发送通知
						shipmentsMap.put("if_receive", "4497471600480001");
						DbUp.upTable("oc_order_shipments").dataUpdate(shipmentsMap, "if_receive", "order_code");
						wxGZHservice.sendWxGZHRemindCheckAfterSale(map.get("small_seller_code"), return_code, map.get("order_code"),time,"售后审核提醒");
					}
				}
			}else {//不需要回寄的直接发送商户微信通知
				wxGZHservice.sendWxGZHRemindCheckAfterSale(map.get("small_seller_code"), return_code, map.get("order_code"),time,"售后审核提醒");
			}
		}
	}
	
	
}
