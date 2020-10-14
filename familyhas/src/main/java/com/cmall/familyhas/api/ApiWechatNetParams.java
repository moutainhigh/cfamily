package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiWechatNetParamsInput;
import com.cmall.familyhas.api.result.ApiWechatNetParamsResult;
import com.cmall.ordercenter.alipay.util.PayUtil;
import com.cmall.ordercenter.alipay.util.WXUtil;
import com.cmall.ordercenter.util.DateNewUtils;
import com.srnpr.xmassystem.support.PlusSupportSystem;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 通过.net提供参数，返回前段mac
 * @author wz
 *
 */
public class ApiWechatNetParams extends RootApiForManage<ApiWechatNetParamsResult,ApiWechatNetParamsInput>{

	public ApiWechatNetParamsResult Process(ApiWechatNetParamsInput inputParam,
			MDataMap mRequestMap) {
		
		ApiWechatNetParamsResult apiWechatNetParamsResult = new ApiWechatNetParamsResult();
		
		String nowTime = DateNewUtils.nowDateDouble();
		MDataMap map = new MDataMap();
		map.put("merchantid", inputParam.getMerchantid());
		map.put("tradetype", inputParam.getTradetype());
		map.put("orderno", inputParam.getOrderno());
		
		map.put("tradetime", nowTime);
		map.put("TradeCode", inputParam.getTradeCode());
		map.put("channelid", inputParam.getChannelid());
		map.put("CallBackURL", inputParam.getCallBackURL());
		
		PayUtil pay = new PayUtil();
		String mac = pay.getWechatOpenID(map);
		
		/*
		 * 获取二维码连接 
		 */
		PlusSupportSystem plusSupport = new PlusSupportSystem();
		//plusSupport.upQrCode("aa", 500);
		String qrCodeSrc = plusSupport.upQrCode("weixin://wxpay/bizpayurl?pr=nmTJjrt",500);
		apiWechatNetParamsResult.setMac(mac);
		apiWechatNetParamsResult.setTradetime(nowTime);
		
		return apiWechatNetParamsResult;
	}


}
