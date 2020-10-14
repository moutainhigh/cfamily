package com.cmall.familyhas.job;

import java.util.List;
import java.util.UUID;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.AfterSaleOrderForGetTime;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 新增定时任务，每天上午8：00和15：00捞取过期时间（待维护时间）小于24小时的退货单，推送客户push
 * @author Angel Joy
 * @date 2019-09-10 11:36:00
 */
public class JobForSendRemindShipmentsMessageToUser extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		AfterSaleOrderForGetTime server = new AfterSaleOrderForGetTime();
		List<MDataMap> afterSaleOrders = DbUp.upTable("oc_return_goods").queryByWhere("status","4497153900050005");//4497153900050005 用户待完善物流消息的售后单
		for(MDataMap map : afterSaleOrders) {
			String return_code = map.get("return_code");
			MDataMap timeMap = server.getDeadLineForFillShipments(return_code);
			String day = timeMap.get("day");
			if("0".equals(day)) {//不足一天了，需要发送push消息
				MDataMap afterSaleMap = DbUp.upTable("oc_order_after_sale").one("asale_code",return_code);
				MDataMap loginInfo = DbUp.upTable("mc_login_info").one("member_code",map.get("buyer_code"));
				MDataMap pusgNewsMap = new MDataMap();
				pusgNewsMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
				pusgNewsMap.put("member_code", map.get("buyer_code"));
				pusgNewsMap.put("phone_no",loginInfo.get("login_name"));
				pusgNewsMap.put("title","维护物流单号提醒");
				pusgNewsMap.put("message","请尽快填写物流单号，过期退货将自动取消");
				pusgNewsMap.put("create_time",DateUtil.getNowTime());
				pusgNewsMap.put("after_sale_status",afterSaleMap.get("asale_status"));
				pusgNewsMap.put("after_sale_code",return_code);
				pusgNewsMap.put("to_page","13");
				pusgNewsMap.put("if_read","0");
				DbUp.upTable("nc_aftersale_push_news").dataInsert(pusgNewsMap);//消息推送表插入数据
			}
		}
	}
	
	
}
