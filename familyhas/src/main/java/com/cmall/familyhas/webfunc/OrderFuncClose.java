package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.service.CouponService;
import com.cmall.ordercenter.model.api.ApiCancelOrderResult;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.webwx.WxGateSupport;

/**
 * 关闭订单
 * 
 * @author 张海生
 * 
 */
public class OrderFuncClose extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		MDataMap mSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if (mResult.upFlagTrue()) {
			// 订单的uid
			String uid = mSubMap.get("uid");
			// 先判断登录是否有效
			if (UserFactory.INSTANCE.create() == null
					|| UserFactory.INSTANCE.create().getLoginName().equals("")) {
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			try {
				MDataMap mdata = DbUp.upTable("oc_orderinfo").oneWhere(
						"order_code,order_status", null, null, "uid", uid);
				if(!"4497153900010001".equals(mdata.get("order_status"))){
					mResult.setResultCode(0);
					mResult.setResultMessage("订单不是未付款状态，不能直接取消订单！");
					return mResult;
				}
				
				if (mdata != null) {
					//判断订单是否是拼团单，如果是拼团单则不允许取消
					OrderService os = new OrderService();
					String orderCode = mdata.get("order_code");
					//根据订单查询拼团表中是否有数据
					int count = DbUp.upTable("sc_event_collage_item").count("collage_ord_code",orderCode);
					if(count> 0 ) {//是拼团单，不允许取消
						mResult.setResultCode(123456789);
						mResult.setResultMessage("拼团单不允许取消订单");
						return mResult;
					}
					String order_status = (String) mdata.get("order_status"); //订单状态
					ApiCancelOrderResult rr = os.CancelOrderForList(orderCode);
					if (rr.getResultCode() == 1) { // 取消成功,向取消表中添加一条记录
						String sql = "SELECT oc_orderinfo.order_code,oc_orderinfo.buyer_code,oc_orderinfo.out_order_code,oc_orderinfo.small_seller_code," +
									 "oc_orderinfo.order_source,oc_orderinfo.order_money, oc_orderadress.address, oc_orderinfo.product_name " +
									 " from oc_orderinfo left join oc_orderadress " +
									 " on oc_orderinfo.order_code = oc_orderadress.order_code " +
									 " where oc_orderinfo.order_code=:order_code ";
						Map<String, Object> map = DbUp.upTable("oc_orderinfo").dataSqlOne(sql,new MDataMap("order_code", orderCode));
						String order_code = (String) map.get("order_code");
						String buyer_code = (String) map.get("buyer_code");
						String out_order_code = (String) map.get("out_order_code");
						String order_money = map.get("order_money").toString(); //订单金额
						String address = (String) map.get("address"); //收货信息	
						String product_name = (String) map.get("product_name"); //所有商品名称
						String small_seller_code = (String)map.get("small_seller_code");
						String now = DateUtil.getSysDateTimeString();
						String manageCode = UserFactory.INSTANCE.create()
								.getManageCode();
						if (FamilyConfig.hjy.equals(manageCode)
								|| FamilyConfig.jyh.equals(manageCode)) {// 如果是惠家有或家有汇则插入取消订单记录
							DbUp.upTable("oc_order_cancel_h").insert(
									"order_code", order_code, "buyer_code",
									buyer_code, "out_order_code",
									out_order_code, "call_flag", "1",
									"create_time", now, "update_time", now,"canceler",UserFactory.INSTANCE.create().getUserCode());
						}
						//未付款的订单不发通知
						if(!"4497153900010001".equals(order_status)) {
							//微信推送订单取消通知
							String sqlwechat = "SELECT openid from uc_wechat_account WHERE manage_code=:manage_code and flag_enable=1";
							List<Map<String, Object>> mapList  = DbUp.upTable("uc_wechat_account").dataSqlList(sqlwechat, new MDataMap("manage_code",small_seller_code));
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
						//回写礼金券到LD
						new CouponService().reWriteGiftVoucherToLD(rr.getReWriteLD(), "R");
					} else {
						mResult.setResultCode(rr.getResultCode());
						mResult.setResultMessage(rr.getResultMessage());
					}
				}
			} catch (Exception e) {
				mResult.setResultCode(941901078);
				mResult.setResultMessage(bInfo(941901078, "关闭订单失败！"));
				e.printStackTrace();
			}
		}
		return mResult;
	}
}