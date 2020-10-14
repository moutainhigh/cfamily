package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiPaymentAllInput;
import com.cmall.familyhas.api.result.AlipayPaymentResult;
import com.cmall.familyhas.api.result.ApiPaymentAllResult;
import com.cmall.familyhas.api.result.UnionPaymentResult;
import com.cmall.familyhas.api.result.WeChatpaymentResult;
import com.cmall.familyhas.orderpay.OrderPayProcess;
import com.cmall.ordercenter.service.ApiWechatProcessService;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.ApplePayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.UnionFenqiPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.UnionPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.WechatPreparePayProcess;
import com.srnpr.xmaspay.response.ApplePayResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * <p>惠家有支付和沙皮狗支付</p>
 * <p>apk和ios中调用</p>
 * 获取支付参数
 * @author wz
 * 449716200001(在线支付)||449746280003 是支付宝
 * 449746280005(微信)
 */
public class ApiPaymentAll extends RootApiForVersion<ApiPaymentAllResult, ApiPaymentAllInput>{

	public ApiPaymentAllResult Process(ApiPaymentAllInput inputParam,
			MDataMap mRequestMap) {
		
		ApiPaymentAllResult apiPaymentAllResult = new ApiPaymentAllResult();
		
		String appVersion = getApiClientValue("app_vision");
		
		/*订单交易失败不再进行支付控制*/
		if (StringUtils.startsWith(inputParam.getOrder_code(), "DD")) {

			MDataMap mDataMap = DbUp.upTable("oc_orderinfo").one("order_code", inputParam.getOrder_code());

			if (mDataMap==null || (mDataMap != null
					&& StringUtils.equals(mDataMap.get("order_status"), FamilyConfig.ORDER_STATUS_TRADE_FAILURE))) {
				
				return apiPaymentAllResult;

			}

		}
		
		if (StringUtils.startsWith(inputParam.getOrder_code(), "OS")) {

			int count = DbUp.upTable("oc_orderinfo").count("big_order_code", inputParam.getOrder_code(), "order_status",
					FamilyConfig.ORDER_STATUS_TRADE_FAILURE);

			if (count > 0) {

				return apiPaymentAllResult;

			}

			//把此订单的支付方式  放入缓存中,  目的是为了在详情页中查询此订单的支付方式
			new PlusSupportPay().fixPayFrom(inputParam.getOrder_code(), inputParam.getPay_type());   
			
			// 清理短信支付标识
			XmasKv.upFactory(EKvSchema.SmsPayFlag).del(inputParam.getOrder_code());
		}
		
		if("449716200001".equals(inputParam.getPay_type()) || "449746280003".equals(inputParam.getPay_type())){  //支付宝支付
			/*
			String alipayValue = null;
			AlipayPaymentResult alipayPaymentResult = new AlipayPaymentResult();
			ApiAlipayMoveProcessService apiAlipayMoveProcessService = new ApiAlipayMoveProcessService();
			// 获取支付宝移动支付RootResult相关信息
			alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(inputParam.getOrder_code(),false); 
			
			if(StringUtils.isEmpty(alipayValue)){
				
				apiPaymentAllResult.setResultCode(-1);
				apiPaymentAllResult.setResultMessage("订单已经失效");
				
				return apiPaymentAllResult;
				
			}
			
			alipayPaymentResult.setAlipaySign(alipayValue);
			alipayPaymentResult.setAlipayUrl(FamilyConfig.ali_url_http);
			
			apiPaymentAllResult.setAlipayPaymentResult(alipayPaymentResult);
			*/
			AlipayPreparePayProcess.PaymentResult result = null;
			// 5.6.6 版本开始走新版支付宝参数
			if(AppVersionUtils.compareTo(appVersion, "5.6.60") >= 0) {
				result = new OrderPayProcess().aliPayAppPrepareNew(inputParam.getOrder_code());
			} else {
				result = new OrderPayProcess().aliPayAppPrepare(inputParam.getOrder_code());
			}
			
			AlipayPaymentResult alipayPaymentResult = new AlipayPaymentResult();
			if(result.upFlagTrue()){
				alipayPaymentResult.setAlipaySign(result.payInfo);
				alipayPaymentResult.setAlipayUrl(FamilyConfig.ali_url_http);	
				apiPaymentAllResult.setAlipayPaymentResult(alipayPaymentResult);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}else if("449746280005".equals(inputParam.getPay_type())){   //微信支付(惠家有)
			/*
			RootResult rootResult = new RootResult();
			WeChatpaymentResult weChatpaymentResult = new WeChatpaymentResult();
			
			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
			//获取微信支付相关信息
			Map map = apiWechatProcessService.wechatMovePaymentVersionNew(inputParam.getOrder_code(), inputParam.getIp(), rootResult);
//			Map map = apiWechatProcessService.wechatPcPaymentNATIVE(inputParam.getOrder_code(), inputParam.getIp(), rootResult);
			
			if(map!=null && !"".equals(map) && map.size()>0){
				weChatpaymentResult.setAppid(String.valueOf(map.get("appid")));
				weChatpaymentResult.setMch_id(String.valueOf(map.get("mch_id")));
				weChatpaymentResult.setNonce_str(String.valueOf(map.get("nonce_str")));
				weChatpaymentResult.setPrepay_id(String.valueOf(map.get("prepay_id")));
				weChatpaymentResult.setSign(String.valueOf(map.get("sign")));
				weChatpaymentResult.setTrade_type(String.valueOf(map.get("trade_type")));
				weChatpaymentResult.setResult_code(String.valueOf(map.get("result_code")));
				weChatpaymentResult.setReturn_code(String.valueOf(map.get("return_code")));
				weChatpaymentResult.setReturn_msg(String.valueOf(map.get("return_msg")));
				weChatpaymentResult.setTimestamp(String.valueOf(map.get("timestamp")));
				
				apiPaymentAllResult.setWeChatpaymentResult(weChatpaymentResult);
			}else{
				apiPaymentAllResult.setResultCode(rootResult.getResultCode());
				apiPaymentAllResult.setResultMessage(rootResult.getResultMessage());
			}
			*/
			WechatPreparePayProcess.PaymentResult result = new OrderPayProcess().wechatPrepare(inputParam.getOrder_code());
			WeChatpaymentResult paymentResult = new WeChatpaymentResult();
			if(result.upFlagTrue()){
				paymentResult.setAppid(result.appid);	
				paymentResult.setMch_id(result.partnerid);
				paymentResult.setNonce_str(result.noncestr);
				paymentResult.setPrepay_id(result.prepayid);
				paymentResult.setSign(result.sign);
				paymentResult.setTrade_type("APP");
				paymentResult.setTimestamp(result.timestamp);
				apiPaymentAllResult.setWeChatpaymentResult(paymentResult);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}else if("449746280005ShaPiGouAPP".equals(inputParam.getPay_type())){   //微信支付(沙皮狗)
			RootResult rootResult = new RootResult();
			WeChatpaymentResult weChatpaymentResult = new WeChatpaymentResult();
			
			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
			//获取微信支付相关信息
			Map map = apiWechatProcessService.wechatMovePaymentVersionShaPIGou(inputParam.getOrder_code(), inputParam.getIp(), rootResult);
//			Map map = apiWechatProcessService.wechatPcPaymentNATIVE(inputParam.getOrder_code(), inputParam.getIp(), rootResult);
			
			if(map!=null && !"".equals(map) && map.size()>0){
				weChatpaymentResult.setAppid(String.valueOf(map.get("appid")));
				weChatpaymentResult.setMch_id(String.valueOf(map.get("mch_id")));
				weChatpaymentResult.setNonce_str(String.valueOf(map.get("nonce_str")));
				weChatpaymentResult.setPrepay_id(String.valueOf(map.get("prepay_id")));
				weChatpaymentResult.setSign(String.valueOf(map.get("sign")));
				weChatpaymentResult.setTrade_type(String.valueOf(map.get("trade_type")));
				weChatpaymentResult.setResult_code(String.valueOf(map.get("result_code")));
				weChatpaymentResult.setReturn_code(String.valueOf(map.get("return_code")));
				weChatpaymentResult.setReturn_msg(String.valueOf(map.get("return_msg")));
				weChatpaymentResult.setTimestamp(String.valueOf(map.get("timestamp")));
				
				apiPaymentAllResult.setWeChatpaymentResult(weChatpaymentResult);
			}else{
				apiPaymentAllResult.setResultCode(rootResult.getResultCode());
				apiPaymentAllResult.setResultMessage(rootResult.getResultMessage());
			}
		
			
		}else if("449746280013".equals(inputParam.getPay_type())){
			/*
			ApplePayResponse applePayResponse = PayServiceFactory.getInstance().getApplePayProcess().process(inputParam.getOrder_code());
			
			apiPaymentAllResult.setApplePayResult(applePayResponse);
			*/
			ApplePayPreparePayProcess.PaymentResult result = new OrderPayProcess().applePayPrepare(inputParam.getOrder_code());
			ApplePayResponse paymentResult = new ApplePayResponse();
			if(result.upFlagTrue()){
				paymentResult.setAp_merchant_id(result.ap_merchant_id);
				paymentResult.setBusi_partner(result.busi_partner);
				paymentResult.setDt_order(result.dt_order);
				paymentResult.setMoney_order(result.money_order);
				paymentResult.setNo_order(result.no_order);
				paymentResult.setNotify_url(result.notify_url);
				paymentResult.setOid_partner(result.oid_partner);
				paymentResult.setRisk_item(result.risk_item);
				paymentResult.setSign(result.sign);
				paymentResult.setSign_type(result.sign_type);
				paymentResult.setUser_id(result.user_id);
				paymentResult.setValid_order(result.valid_order);
				apiPaymentAllResult.setApplePayResult(paymentResult);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}else if("449746280014".equals(inputParam.getPay_type())){
			
			//UnionPayResponse unionPayResponse = PayServiceFactory.getInstance().getUnionPayProcess().process(inputParam.getOrder_code());
			
			//apiPaymentAllResult.setFnCode(unionPayResponse.getTn());
			
			
			UnionPreparePayProcess.PaymentResult result = new OrderPayProcess().unionPayAppPrepare(inputParam.getOrder_code());
			if(result.upFlagTrue()){
				UnionPaymentResult paymentResult = new UnionPaymentResult();
				paymentResult.setPayUrl(result.payUrl);
				apiPaymentAllResult.setUnionPayResult(paymentResult);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}else if("449746280020".equals(inputParam.getPay_type())){
			
			//UnionPayResponse unionPayResponse = PayServiceFactory.getInstance().getUnionPayProcess().process(inputParam.getOrder_code());
			
			//apiPaymentAllResult.setFnCode(unionPayResponse.getTn());
			
			
			UnionFenqiPreparePayProcess.PaymentResult result = new OrderPayProcess().unionPayFenqiAppPrepare(inputParam.getOrder_code(), null);
			if(result.upFlagTrue()){
				UnionPaymentResult paymentResult = new UnionPaymentResult();
				paymentResult.setPayUrl(result.payUrl);
				apiPaymentAllResult.setUnionPayResult(paymentResult);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}else if("wechat_wxss".equalsIgnoreCase(inputParam.getPay_type())){
			new PlusSupportPay().fixPayFrom(inputParam.getOrder_code(), "449746280005");   
			
			WechatPreparePayProcess.PaymentInput input = new WechatPreparePayProcess.PaymentInput();
			input.bigOrderCode = inputParam.getOrder_code();
			input.payChannel = PaymentChannel.JSAPI_WXSS;
			input.openid = inputParam.getOpenid();
			WechatPreparePayProcess.PaymentResult result = PayServiceFactory.getInstance().getWechatPreparePayProcess().process(input);
			if(result.upFlagTrue()){
				apiPaymentAllResult.setJsapiparam(result.jsapiparam);
			}else{
				apiPaymentAllResult.setResultCode(result.getResultCode());
				apiPaymentAllResult.setResultMessage(result.getResultMessage());
			}
		}
		
//		else if("000000000000".equals(inputParam.getPay_type())){   //微信支付Wap 支付   (后台所传的paytype  值以让是449746280005(微信支付))
//			RootResult rootResult = new RootResult();
//			WeChatpaymentResult weChatpaymentResult = new WeChatpaymentResult();
//			
//			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
//			
//			//获取微信支付相关信息
//			Map map = apiWechatProcessService.wechatMovePaymentWapVersionNew(inputParam.getOrder_code(), inputParam.getIp(), rootResult,inputParam.getOpenid());
//			
//			if(map!=null && !"".equals(map) && map.size()>0){
//				weChatpaymentResult.setAppid(String.valueOf(map.get("appid")));
//				weChatpaymentResult.setMch_id(String.valueOf(map.get("mch_id")));
//				weChatpaymentResult.setNonce_str(String.valueOf(map.get("nonce_str")));
//				weChatpaymentResult.setPrepay_id(String.valueOf(map.get("prepay_id")));
//				weChatpaymentResult.setSign(String.valueOf(map.get("sign")));
//				weChatpaymentResult.setTrade_type(String.valueOf(map.get("trade_type")));
//				weChatpaymentResult.setResult_code(String.valueOf(map.get("result_code")));
//				weChatpaymentResult.setReturn_code(String.valueOf(map.get("return_code")));
//				weChatpaymentResult.setReturn_msg(String.valueOf(map.get("return_msg")));
//				weChatpaymentResult.setTimestamp(String.valueOf(map.get("timestamp")));
//				
//				apiPaymentAllResult.setWeChatpaymentResult(weChatpaymentResult);
//			}else{
//				apiPaymentAllResult.setResultCode(rootResult.getResultCode());
//				apiPaymentAllResult.setResultMessage(rootResult.getResultMessage());
//			}
//		
//			
//		}
		return apiPaymentAllResult;
	}


}
