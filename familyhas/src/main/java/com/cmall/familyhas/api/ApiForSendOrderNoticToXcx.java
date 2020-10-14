package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.cmall.familyhas.api.input.ApiForSendOrderNoticInput;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webwx.WxGateSupport;

public class ApiForSendOrderNoticToXcx extends RootApiForVersion<RootResult, ApiForSendOrderNoticInput> {

	@Override
	public RootResult Process(ApiForSendOrderNoticInput inputParam, MDataMap mRequestMap) {

		String big_order_code = inputParam.getOrderCode();
		RootResult result = new RootResult();
		
		MDataMap bigOrder = DbUp.upTable("oc_orderinfo_upper").one("big_order_code", big_order_code);
		
		if(null != bigOrder) {
			
			String due_money = bigOrder.get("due_money");
			
			String openid_xch = inputParam.getOpenId();
			
			WxGateSupport wxGateSupport = new WxGateSupport();
			String sendResult = "";
			//判断能否用小程序发消息
			MDataMap payInfo = DbUp.upTable("oc_payment_paygate").one("c_order", big_order_code);
			
			//如果是微信小程序支付的 则用小程序发通知
			if(null != payInfo && "764".equals(payInfo.get("c_paygate")) && null!=openid_xch && !"".equals(openid_xch)) {
				
				String receivers = openid_xch + "|7||" + bConfig("ordercenter.xcx_details_link");;
				sendResult = wxGateSupport.sendOrderNoticeByXcx(receivers, big_order_code, due_money+"元", bConfig("ordercenter.remark_word"));
				bLogInfo(0, big_order_code + "调用微信小程序发送下单成功通知响应结果:" + sendResult);
			}
			
			result.setResultMessage(sendResult + "");
			
			MDataMap resultMap = decode(sendResult);
			if(null == resultMap || !"0".equals(resultMap.get("resultcode"))) {
				result.setResultCode(0);
				
				if("".equals(sendResult)) {
					result.setResultMessage("该订单非微信小程序支付!");
				}
			}
		}
		
		return result;
	
	}
	
	/**
	 * 解码
	 * @param param eg: resultcode=0&resultmessage=操作成功&successreceivers=&failedreceivers=&mac=c6c9297bf57a77fd4ff0955864c2ba70
	 * @return
	 */
	private MDataMap decode(String param) {
		
		if(null == param || "".equals(param)) {
			return null;
		}
		
		try {//解码
			param = URLDecoder.decode(param, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		MDataMap resultMap = new MDataMap();
		String[] resultArr = param.split("&");
	    for(String strSplit:resultArr){
	          String[] arrSplitEqual=null;         
	          arrSplitEqual= strSplit.split("=");
	         
	          //解析出键值
	          if(arrSplitEqual.length>1){
	              //正确解析
	        	  resultMap.put(arrSplitEqual[0], arrSplitEqual[1]);
	          }else{
	              if(arrSplitEqual[0]!=""){
		              //只有参数没有值，不加入
	            	  resultMap.put(arrSplitEqual[0], "");       
	              }
	          }
	    }
	    return resultMap;
	}

}
