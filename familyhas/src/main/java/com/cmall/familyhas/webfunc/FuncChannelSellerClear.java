package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.srnpr.xmasorder.channel.service.PorscheOrderService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncChannelSellerClear
 * @Description: 渠道商清算
 * @author lgx
 * 
 */
public class FuncChannelSellerClear extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String channel_seller_code = mDataMap.get("channelSellerCode");
		String remark = mDataMap.get("zw_f_remark");
		
		Map<String, Object> channel_seller = DbUp.upTable("uc_channel_sellerinfo").dataSqlOne("SELECT * FROM uc_channel_sellerinfo WHERE channel_seller_code = '"+channel_seller_code+"'", new MDataMap());
		if(channel_seller==null) {
			result.setResultCode(-1);
			result.setResultMessage("查不到该条数据,操作失败");
			return result;
		}
		
		// 查询渠道商户下有无可售后的订单,有则提示不能清算,没有再向下执行
		// 确保订单状态都是交易成功和交易失败
		String sql = "SELECT count(1) num FROM oc_orderinfo oi " + 
				"	LEFT JOIN oc_order_channel oc ON oc.order_code = oi.order_code " + 
				" WHERE oc.channel_seller_code = '"+channel_seller_code+"' " + 
				"	AND oi.order_status NOT in ('4497153900010005','4497153900010006')";
		Map<String, Object> orderMap = DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap());
		if(!"0".equals(orderMap.get("num")+"")) {
			result.setResultCode(-1);
			result.setResultMessage("还有未完成的订单存在,请确保所有订单完成再清算");
			return result;
		}
		// 确保售后单都是完成状态
		String sql2 = "SELECT count(1) num FROM oc_order_after_sale oas " + 
				"	LEFT JOIN oc_order_channel oc ON oc.order_code = oas.order_code " + 
				" WHERE oc.channel_seller_code = '"+channel_seller_code+"' " + 
				"	AND oas.flow_end = '0'";
		Map<String, Object> afterSaleMap = DbUp.upTable("oc_order_after_sale").dataSqlOne(sql2, new MDataMap());
		if(!"0".equals(afterSaleMap.get("num")+"")) {
			result.setResultCode(-1);
			result.setResultMessage("还有未完成的售后单存在,请确保所有售后单完成再清算");
			return result;
		}
		
		String before = "";
		String now = "";
		String nowTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		MDataMap updateDataMap = new MDataMap();
		updateDataMap.put("channel_seller_code", channel_seller_code);
		updateDataMap.put("update_time", nowTime);
		updateDataMap.put("advance_balance", "0.00");
		String cooperation_status = (String) channel_seller.get("cooperation_status");
		if("4497471600560005".equals(cooperation_status)) {
			// 清算中,点击清算变为终止合作
			updateDataMap.put("cooperation_status", "4497471600560003");
			before = "清算中";
			now = "终止合作";
		}else {
			result.setResultCode(-1);
			result.setResultMessage("操作失败,请刷新重试");
			return result;
		}
		int dataUpdate = DbUp.upTable("uc_channel_sellerinfo").dataUpdate(updateDataMap, "cooperation_status,advance_balance,update_time", "channel_seller_code");
		
		if(dataUpdate > 0) {			
			// 往 lc_channel_freeze_cooperation_log 插入状态修改日志数据
			MDataMap map = new MDataMap();
			map.put("channel_seller_code", channel_seller_code);
			map.put("channel_seller_name", (String) channel_seller.get("channel_seller_name"));
			map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map.put("register_time", nowTime);
			map.put("remark", UserFactory.INSTANCE.create().getLoginName()+"清算了"+channel_seller.get("channel_seller_name")+
					"由"+before+"变为"+now+":"+remark);
			DbUp.upTable("lc_channel_freeze_cooperation_log").dataInsert(map);
			
			// 往 lc_channel_seller_invest_log 插入清算日志数据
			MDataMap map1 = new MDataMap();
			map1.put("channel_seller_code", channel_seller_code);
			map1.put("channel_seller_name", (String) channel_seller.get("channel_seller_name"));
			map1.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map1.put("recharge_money", "-"+channel_seller.get("advance_balance"));
			map1.put("recharge_date", nowTime);
			map1.put("remark", remark);
			DbUp.upTable("lc_channel_seller_invest_log").dataInsert(map1);
			
			// 往渠道商预存款变动日志表lc_operation_channel_money插入充值日志
			new PorscheOrderService().insertChannelMoneyLog(channel_seller_code, "449748420005", channel_seller.get("advance_balance")+"", "0.00", "", "系统清算");
		
		}else {
			result.setResultCode(-1);
			result.setResultMessage("操作失败");
			return result;
		}
		
		return result;
	}


}
