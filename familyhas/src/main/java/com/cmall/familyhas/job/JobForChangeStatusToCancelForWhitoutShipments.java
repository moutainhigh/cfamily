package com.cmall.familyhas.job;

import java.util.List;
import java.util.UUID;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.AfterSaleOrderForGetTime;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 新增定时任务，每分钟执行一次，查询过期未维护售后运单号的退货单，取消售后申请。
 * @author Angel Joy
 * @date 2019-09-10 16:44:25
 */
public class JobForChangeStatusToCancelForWhitoutShipments extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		AfterSaleOrderForGetTime server = new AfterSaleOrderForGetTime();
		List<MDataMap> afterSaleOrders = DbUp.upTable("oc_return_goods").queryByWhere("status","4497153900050005");//4497153900050005 用户待完善物流消息的售后单
		for(MDataMap map : afterSaleOrders) {
			String smallSellerCode = map.get("small_seller_code");
			if(Constants.SMALL_SELLER_CODE_JD.equals(smallSellerCode)) {//JD 订单不走此逻辑
				continue;
			}
			String return_code = map.get("return_code");
			MDataMap timeMap = server.getDeadLineForFillShipments(return_code);
			int timeStamp = Integer.parseInt(timeMap.get("timeStamp"));
			if(timeStamp<=0) {//需要取消掉该售后单
				String flowBussinessUid = map.get("uid");
				String fromStatus = map.get("status");
				String toStatus = "4497153900050007";
				String flowType = "449715390005";
				new FlowBussinessService().ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, "system", "取消申请", new MDataMap());
			}
		}
	}
	
	
}
