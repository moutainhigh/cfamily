package com.cmall.familyhas.job;

import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.groupcenter.homehas.RsyncKaoLaSupport;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 用户支付完成后需要调用网易考拉支付接口
 * payOrder
 * @author cc
 *
 */
public class JobForPayKaolaOrder extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		
		//根据订单号查询是否是拼团单。
		MDataMap groupOrderMap = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(groupOrderMap != null && !groupOrderMap.isEmpty()){//不为空时，证明是拼团单，然后检查是否已经拼团成功。
			String collageCode = groupOrderMap.get("collage_code");
			MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
			//判断此团是否拼团成功
			String collageStatus = collageInfo.get("collage_status");
			if(!"449748300002".equals(collageStatus)){//非拼团成功的订单做以下操作
				//将同步次数改为1，防止长时间未拼团成功。此订单通知次数用完之后，不再通知。
				DbUp.upTable("za_exectimer").dataUpdate(new MDataMap("exec_info", orderCode,"exec_type","449746990013" ,"exec_number","1","remark",""),"exec_number,remark","exec_info,exec_type");
				//操作失败标识
				mWebResult.setResultCode(99);
				return mWebResult;
			}
		}
		
//		String orderStatus = new KaolaOrderService().upKaolaQueryOrderInterface(orderCode);
//		if(!"4497153900010001".equals(orderStatus)) {
//			mWebResult.setResultCode(0);
//			mWebResult.setResultMessage("订单状态不对，不能支付！");
//			return mWebResult;
//		}
		
		
		String orderStatus = (String)DbUp.upTable("oc_orderinfo").dataGet("order_status", "", new MDataMap("order_code", orderCode));
		if("4497153900010006".equals(orderStatus)){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("考拉订单同步支付信息失败，订单已经取消");
			return mWebResult;
		}
		
		String apiName = "payOrder";
		TreeMap<String, String> params = new TreeMap<String, String>();
		//channelId,timestamp,v,sign_method,app_key,sign
		params.put("thirdPartOrderId", orderCode);
		String result = RsyncKaoLaSupport.doPostRequest(apiName, "source", params);
		JSONObject resultJson = JSON.parseObject(result);
		if(resultJson.getInteger("recCode") == 200) {
//			状态码	描述	处理方式
//			200	支付成功	无需处理
//			-106	支付失败	可以重试三次
//			-101	支付失败，系统内部异常	
//			-107	支付失败，没有下单成功	
//			-21	支付失败，参数错误	
//			-240	订单关闭不能支付	需要重新下单做支付操作
//			-241	订单已经支付成功	无需处理
//			-243	下单失败不能支付	需要下单成功后做支付操作
//			-200	其它错误	
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage(resultJson.getString("recMeg"));
		} else if(resultJson.getInteger("recCode") == -106) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("支付失败！");
		} else if(resultJson.getInteger("recCode") == -101) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("支付失败，系统内部异常！");
		} else if(resultJson.getInteger("recCode") == -107) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("支付失败，没有下单成功！");
		} else if(resultJson.getInteger("recCode") == -240) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("订单关闭不能支付！");
		} else if(resultJson.getInteger("recCode") == -241) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单已经支付成功！");
		} else if(resultJson.getInteger("recCode") == -243) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("下单失败不能支付！");
		} else {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("其他错误！");
		}
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990013");
		config.setMaxExecNumber(30);
		return config;
	}

}
