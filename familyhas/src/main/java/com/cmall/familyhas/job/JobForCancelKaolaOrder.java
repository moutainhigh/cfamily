package com.cmall.familyhas.job;

import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.groupcenter.homehas.RsyncKaoLaSupport;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 未支付取消网易考拉订单
 * 调用网易考拉关闭订单接口：/api/closeOrder
 * @author cc
 *
 */
public class JobForCancelKaolaOrder extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		
//		String orderStatus = new KaolaOrderService().upKaolaQueryOrderInterface(orderCode);
//		if(!"4497153900010001".equals(orderStatus)) {
//			mWebResult.setResultCode(0);
//			mWebResult.setResultMessage("订单状态不对，不能取消！");
//			return mWebResult;
//		}
		String apiName = "closeOrder";
		TreeMap<String, String> params = new TreeMap<String, String>();
		//channelId,timestamp,v,sign_method,app_key,sign
		//reasonId 枚举如下 1 收货人信息有误 2 商品数量或款式需调整 3 有更优惠的购买方案 4 考拉一直未发货 5 商品缺货 6 我不想买了 7 其他原因
		params.put("thirdpartOrderId", orderCode);
		params.put("reasonId", "6");
		String result = RsyncKaoLaSupport.doPostRequest(apiName, "channelId", params);
		JSONObject resultJson = JSON.parseObject(result);
		if(resultJson.getBooleanValue("closeResult")) {
			//true--关闭订单成功，false--关闭订单失败
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage(resultJson.getString("closeCodeDesc"));			
		} else {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("其他错误！");
		}
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990011");
		config.setMaxExecNumber(30);
		return config;
	}

}
