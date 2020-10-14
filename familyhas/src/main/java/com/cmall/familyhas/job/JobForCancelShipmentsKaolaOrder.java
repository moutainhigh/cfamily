package com.cmall.familyhas.job;

import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.service.CouponService;
import com.cmall.groupcenter.homehas.RsyncKaoLaSupport;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.cmall.groupcenter.third.model.GroupRefundInput;
import com.cmall.groupcenter.third.model.GroupRefundResult;
import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.ordercenter.service.money.ReturnMoneyResult;
import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ApiCallSupport;

/**
 * 网易考拉订单取消发货
 * 调用网易考拉取消订单接口：/api/cancelOrder
 * @author cc
 *
 */
public class JobForCancelShipmentsKaolaOrder extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		
//		String orderStatus = new KaolaOrderService().upKaolaQueryOrderInterface(orderCode);
//		if("4497153900010006".equals(orderStatus)) {
//			mWebResult.setResultCode(1);
//			mWebResult.setResultMessage("订单已经关闭！");
//			return mWebResult;
//		}
//		if(!"4497153900010002".equals(orderStatus)) {
//			mWebResult.setResultCode(0);
//			mWebResult.setResultMessage("订单状态不对，不能取消发货！");
//			return mWebResult;
//		}
		String apiName = "cancelOrder";
		TreeMap<String, String> params = new TreeMap<String, String>();
		//channelId,timestamp,v,sign_method,app_key,sign
		//reasonId 枚举如下 1 收货人信息有误 2 商品数量或款式需调整 3 有更优惠的购买方案 4 考拉一直未发货 5 商品缺货 6 我不想买了 7 其他原因
		params.put("thirdpartOrderId", orderCode);
		params.put("reasonId", "6");
		params.put("remark", "");
		String result = RsyncKaoLaSupport.doPostRequest(apiName, "channelId", params);
		JSONObject resultJson = JSON.parseObject(result);
		if(resultJson.getInteger("recCode") == 200) {
			// 取消接口调用成功不表示订单取消成功，具体以考拉订单状态同步定时为准  2019-11-15
			return mWebResult;
//			状态码	描述
//			200	查询成功
//			-100	缺少必选参数
//			2	参数校验未通过
//			1000	订单不存在
//			1002	订单状态不符，已经发货了，或者是还没有支付
//			-200	其它错误
//			orderStatus = new KaolaOrderService().upKaolaQueryOrderInterface(orderCode);
//			if("4497153900010006".equals(orderStatus)) {
			//网易考拉异步处理取消发货，只要收到200就可以认为订单已经取消发货了
//			mWebResult.setResultCode(1);
//			mWebResult.setResultMessage(resultJson.getString("recMsg"));
//			
//			//取消成功，生成退款单
//			MDataMap order = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
//			RootResult res = cancelOrder(order,"网易考拉订单取消发货");
//			if(res.getResultCode()==1){
//				//退返微公社部分
//				MDataMap payInfo=DbUp.upTable("oc_order_pay").one("order_code",orderCode,"pay_type","449746280009");
//				if(payInfo!=null&&!payInfo.isEmpty()){
//					GroupRefundInput groupRefundInput = new GroupRefundInput();
//					groupRefundInput.setTradeCode(payInfo.get("pay_sequenceid"));
//					groupRefundInput.setMemberCode(order.get("buyer_code"));
//					groupRefundInput.setRefundMoney(payInfo.get("payed_money"));
//					groupRefundInput.setOrderCode(orderCode);
//					groupRefundInput.setRefundTime(DateUtil.getSysDateTimeString());
//					groupRefundInput.setRemark("取消发货");
//					groupRefundInput.setBusinessTradeCode(payInfo.get("pay_sequenceid"));//一个流水值退一次
//					ApiCallSupport<GroupRefundInput, GroupRefundResult> apiCallSupport=new ApiCallSupport<GroupRefundInput, GroupRefundResult>();
//					try {
//						apiCallSupport.doCallApi(bConfig("xmassystem.group_pay_url"),bConfig("xmassystem.group_pay_refund_face"),bConfig("xmassystem.group_pay_key"),bConfig("xmassystem.group_pay_pass"), groupRefundInput,new GroupRefundResult());
//					} catch (Exception e) {
//						//此处暂时流程，退款失败，不影响总流程
//						e.printStackTrace();
//					}
//				}
//			}
//			} else {
//				mWebResult.setResultCode(0);
//				mWebResult.setResultMessage("网易考拉订单没有正常关闭！");
//			}				
		} else if(resultJson.getInteger("recCode") == 1000){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("网易考拉订单不存在！");
		} else if(resultJson.getInteger("recCode") == 1002){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("网易考拉订单状态不符，已经发货了，或者是还没有支付！");
		} else {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("其他错误！");
		}
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990012");
		config.setMaxExecNumber(30);
		return config;
	}

	/***
	 * 取消发货
	 * @param order_code
	 * @param operater
	 * @param remark
	 * @return
	 */
	public RootResult cancelOrder(MDataMap orderInfo,String remark) {
		
		RootResult ret = new RootResult();
		FlowBussinessService fs = new FlowBussinessService();
		String flowBussinessUid = orderInfo.get("uid");
		String fromStatus = orderInfo.get("order_status");
		String operater = orderInfo.get("buyer_code");
		String order_code = orderInfo.get("order_code");		
		String toStatus = "4497153900010006";
		String flowType = "449715390008";
		ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, remark, new MDataMap("order_code",order_code));
		
		if (ret.getResultCode() == 1) {			
			//生成退款单
			CreateMoneyService createMoneyService = new CreateMoneyService();
			ReturnMoneyResult rm = createMoneyService.creatReturnMoney(order_code,operater,remark);
			if(rm.getList() != null && rm.getList().size() > 0) {
				new CouponService().reWriteGiftVoucherToLD(rm.getList(), "R"); //取消发货回写礼金券给LD
			}
		}else{
			WebHelper.errorMessage(order_code, "cancelOrder", 1,"cancelOrder on ChangeFlow", ret.getResultMessage(),null);
		}
		
		return ret;
	}
}