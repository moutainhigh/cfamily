package com.cmall.familyhas.service;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webwx.WxGateSupport;

public class WxGZHService extends BaseClass {

	/**
	 * 微信公众号发送取消订单的通知
	 * @param small_seller_code
	 * @param order_code
	 * @param order_money
	 * @param address
	 */
	public void sendWxGZHCancelOrder(String small_seller_code, String order_code, String order_money, String address, String product_name) {
		String sql = "SELECT openid from uc_wechat_account WHERE manage_code=:manage_code and flag_enable=1";
		List<Map<String, Object>> mapList  = DbUp.upTable("uc_wechat_account").dataSqlList(sql, new MDataMap("manage_code",small_seller_code));
		if(mapList != null && mapList.size() > 0) {
			for(Map<String, Object> mapWechat : mapList){
				String openid = mapWechat.get("openid")==null?"":mapWechat.get("openid").toString();
				if(openid != "") {
					//发送微信消息
					WxGateSupport wxGateSupport = new WxGateSupport(bConfig("ordercenter.wx_url"), bConfig("ordercenter.merchant_key"), bConfig("ordercenter.merchant_id"), bConfig("ordercenter.word_color"));
					String receivers = openid + "|" + bConfig("ordercenter.send_cancel_template_num") +"||";
					String sendResult = wxGateSupport.sendOrderNoticeByGzh10(receivers, "有用户取消订单",order_money + "元", product_name, address, order_code, "");
					bLogInfo(0, order_code + "调用微信公众号发送取消订单成功通知响应结果:" + sendResult);
				}
			}
		}
	}
	
	/**
	 * 微信公众号发送提醒审核退货单的通知
	 * @param small_seller_code
	 * @param after_sale_code
	 * @param order_code
	 */
	public void sendWxGZHRemindCheckAfterSale(String small_seller_code, String after_sale_code,String order_code,String time,String type) {
		String sql = "SELECT openid from uc_wechat_account WHERE manage_code=:manage_code and flag_enable=1";
		List<Map<String, Object>> mapList  = DbUp.upTable("uc_wechat_account").dataSqlList(sql, new MDataMap("manage_code",small_seller_code));
		if(mapList != null && mapList.size() > 0) {
			for(Map<String, Object> mapWechat : mapList){
				String openid = mapWechat.get("openid")==null?"":mapWechat.get("openid").toString();
				if(openid != "") {
					//发送微信消息
					WxGateSupport wxGateSupport = new WxGateSupport(bConfig("ordercenter.wx_url"), bConfig("ordercenter.merchant_key"), bConfig("ordercenter.merchant_id"), bConfig("ordercenter.word_color"));
					String receivers = openid + "|6||";
					String content = "惠家有提示您：有售后订单需要处理，订单号："+order_code+"，退货单号："+after_sale_code;
					String message = "{\"keyword1\":{\"color\":\"#336699\",\"value\":\""+content+"\"},\"keyword2\":{\"color\":\"#336699\",\"value\":\""+type+"\"},\"keyword3\":{\"color\":\"#336699\",\"value\":\""+time+"\"}}";
					String sendResult = wxGateSupport.sendMsgForNotice(receivers, message, "");
					bLogInfo(0, after_sale_code + "调用微信公众号发送提醒商家审核售后单通知响应结果:" + sendResult);
				}
			}
		}
	}
}
