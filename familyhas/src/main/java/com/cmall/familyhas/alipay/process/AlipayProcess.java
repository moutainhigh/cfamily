package com.cmall.familyhas.alipay.process;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.webfunc.FamilyOrderShoppingService;
import com.cmall.groupcenter.service.AlipayOrderShoppingService;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.ordercenter.model.PayResult;
import com.cmall.ordercenter.service.AlipayProcessService;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebSessionHelper;

/**
 * 支付宝
 * 
 * @author wz
 * 
 */
public class AlipayProcess extends BaseClass {

	public static final String OC_ORDERINFO = "oc_orderinfo";// 主表
	public static final String OC_PAYMENT = "oc_payment";// 支付宝接口表名

	/**
	 * 支付宝移动回调(移动端)
	 * 
	 * @param out_trade_no
	 * @param trade_no
	 * @param trade_status
	 * @param notify_time
	 * @param notify_type
	 * @param notify_id
	 * @param buyer_email
	 * @param seller_email
	 * @param sign_type
	 * @param sign
	 * @param total_fee
	 * @param mark
	 * @param gmt_payment
	 * @param out_channel_inst
	 */

	public void resultForm(String mark) {
		// WebSessionHelper webSession=new WebSessionHelper();
		// webSession.upRequest("");
		AlipayOrderShoppingService orderShoppingService = new AlipayOrderShoppingService();

		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();

		Map mMaps = request.getParameterMap();

		StringBuffer str = new StringBuffer();
		String endStr = "";
		MDataMap insertDatamap = new MDataMap();
		List<String> list = new ArrayList<String>();
		for (Object oKey : mMaps.keySet()) {
			insertDatamap.put(oKey.toString(),
					request.getParameter(oKey.toString()));
			insertDatamap.put("mark", mark);

			if (!"sign_type".equals(oKey.toString())
					&& !"sign".equals(oKey.toString())) {
				list.add(oKey.toString() + "="
						+ request.getParameter(oKey.toString()));
			}

		}
		Collections.sort(list); // 对List内容进行排序

		for (String nameString : list) {
			str.append(nameString + "&");
		}

		endStr = str.substring(0, str.toString().length() - 1);

		AlipayProcessService alipayProcessService = new AlipayProcessService();

		PayResult payResult = alipayProcessService.resultValue(insertDatamap, endStr); // 将数据插入到oc_payment 验签
		if (payResult.upFlagTrue()) {
			
//			Map<String,Object> mapOrder = DbUp.upTable("oc_orderinfo").dataSqlOne("select * from oc_orderinfo where order_code=:order_code or big_order_code=:order_code", 
//					new MDataMap("order_code",payResult.getOrderCode()));
//			
//			if(mapOrder!=null && !"".equals(mapOrder)){
//				/**
//				 * 只有ld的商品会同步到ld，其余商品不同步(只有small_seller_code为SI2003的时候说明是ld的商品)
//				 */
//				if("SI2003".equals(String.valueOf(mapOrder.get("small_seller_code")))){
//					orderShoppingService.deletefamilySkuToShopCart(payResult
//							.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品 并且 同步支付数据
//				}else{
//					orderShoppingService.deletefamilySkuToShopCartNotSynchronization(payResult
//							.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品     不同步支付数据
//				}
//				
//			}
			
			//update by jlin 2015-11-07 12:59:02
			List<MDataMap> orderList=DbUp.upTable("oc_orderinfo").queryAll("", "", "order_code=:order_code or big_order_code=:order_code", new MDataMap("order_code", payResult.getOrderCode()));
			boolean ld_flag=false;
			boolean th_flag=false;
			for (MDataMap mDataMap : orderList) {
				if(ld_flag&&th_flag){
					break;
				}
				
				String small_seller_code=mDataMap.get("small_seller_code");
				/**
				 * 只有ld的商品会同步到ld，其余商品不同步(只有small_seller_code为SI2003的时候说明是ld的商品)
				 */
				if (MemberConst.MANAGE_CODE_HOMEHAS.equals(small_seller_code)) {
					if(ld_flag){
						continue;
					}
					orderShoppingService.deletefamilySkuToShopCart(payResult.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品
					ld_flag=true;
				} else {
					if(th_flag){
						continue;
					}
					orderShoppingService.deletefamilySkuToShopCartNotSynchronization(payResult.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品
					th_flag=true;
				}
			}
			//end
			
			
			
			/**
			 * 同步kJT支付完成后的订单
			 */
			OrderService os = new OrderService();
			List<Map<String, Object>> listMap = os.ifKJTOrder(payResult.getOrderCode(), "SF03KJT");
			for(Map<String, Object> map : listMap){
//				JobExecHelper.createExecInfo("449746990003", String.valueOf(map.get("order_code")), null);
				JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, String.valueOf(map.get("order_code")), "" , "AlipayProcess line 157");
			}
			
			
			/**
			 * 
			 */
			if (VersionHelper.checkServerVersion("3.5.62.55")) {
				MDataMap bigOrderMap = DbUp.upTable("oc_orderinfo_upper").one(
						"big_order_code", insertDatamap.get("out_trade_no"));

				if (bigOrderMap != null && !"".equals(bigOrderMap)
						&& bigOrderMap.size() > 0) {

					if (AppConst.MANAGE_CODE_HOMEHAS.equals(bigOrderMap
							.get("seller_code")) || AppConst.MANAGE_CODE_CDOG.equals(bigOrderMap
									.get("seller_code"))) {
						// 通过buyer_code获取手机号
						MemberLoginSupport mls = new MemberLoginSupport();
						String loginName = mls.getMoblie(bigOrderMap
								.get("buyer_code"));

						if (loginName != null && !"".equals(loginName)) {
							// 货到付款下单送优惠券
							JmsNoticeSupport.INSTANCE.sendQueue(
									JmsNameEnumer.OnDistributeCoupon,
									CouponConst.pay_coupon, new MDataMap(
											"mobile", loginName, "manage_code", bigOrderMap.get("seller_code"), 
											"big_order_code", bigOrderMap.get("big_order_code"),
											"member_code", bigOrderMap.get("buyer_code")));
						}
					}
				}
			}
		}
	}
	

	/**
	 * 支付宝移动回调(移动端)
	 * 
	 * @param out_trade_no
	 * @param trade_no
	 * @param trade_status
	 * @param notify_time
	 * @param notify_type
	 * @param notify_id
	 * @param buyer_email
	 * @param seller_email
	 * @param sign_type
	 * @param sign
	 * @param total_fee
	 * @param mark
	 * @param gmt_payment
	 * @param out_channel_inst
	 */

	public void resultFormNew(String mark) {
		// WebSessionHelper webSession=new WebSessionHelper();
		// webSession.upRequest("");
		AlipayOrderShoppingService orderShoppingService = new AlipayOrderShoppingService();

		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();

		Map mMaps = request.getParameterMap();

		StringBuffer str = new StringBuffer();
		String endStr = "";
		MDataMap insertDatamap = new MDataMap();
		List<String> list = new ArrayList<String>();

		for (Object oKey : mMaps.keySet()) {
			insertDatamap.put(oKey.toString(),
					request.getParameter(oKey.toString()));
			insertDatamap.put("mark", mark);

			if (!"sign_type".equals(oKey.toString())
					&& !"sign".equals(oKey.toString())) {
				list.add(oKey.toString() + "="
						+ request.getParameter(oKey.toString()));
			}

		}
		Collections.sort(list); // 对List内容进行排序

		for (String nameString : list) {
			str.append(nameString + "&");
		}

		endStr = str.substring(0, str.toString().length() - 1);

		AlipayProcessService alipayProcessService = new AlipayProcessService();

		PayResult payResult = alipayProcessService.resultValueNew(
				insertDatamap, endStr); // 将数据插入到oc_payment 验签
		if (payResult.upFlagTrue()) {

			orderShoppingService.deletefamilySkuToShopCartNew(payResult
					.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品 并且 同步支付数据
			
			/**
			 * 同步kJT支付完成后的订单
			 */
			OrderService os = new OrderService();
			List<Map<String, Object>> listMap = os.ifKJTOrder(payResult.getOrderCode(), "SF03KJT");
			for(Map<String, Object> map : listMap){
//				JobExecHelper.createExecInfo("449746990003", String.valueOf(map.get("order_code")), null);
				JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, String.valueOf(map.get("order_code")), "" , "AlipayProcess line 262");
			}
		}
	}

	/**
	 * 调用支付宝网页支付
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public String upSubmitWebForm(String sOrderCode, String domainName) {
		String requestForm = "";
		// 判断订单号是否为空
		if (!"".equals(sOrderCode) && sOrderCode != null) {
			AlipayProcessService alipayProcessService = new AlipayProcessService();
			requestForm = alipayProcessService.createForm(sOrderCode,
					domainName);
		} else {
			requestForm = bInfo(922401020);
		}

		return requestForm;
	}

	// public void result
	/**
	 * 支付宝移动回调
	 * 
	 * @param out_trade_no
	 * @param trade_no
	 * @param trade_status
	 * @param notify_time
	 * @param notify_type
	 * @param notify_id
	 * @param buyer_email
	 * @param seller_email
	 * @param sign_type
	 * @param sign
	 * @param total_fee
	 * @param mark
	 * @param gmt_payment
	 * @param out_channel_inst
	 */

	public void resultWebForm() {
		// WebSessionHelper webSession=new WebSessionHelper();
		// webSession.upRequest("");

		AlipayOrderShoppingService orderShoppingService = new AlipayOrderShoppingService();

		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();

		List<String> list = new ArrayList<String>();

		StringBuffer str = new StringBuffer();

		String endStr = "";
		// 获取支付宝POST过来反馈信息
		MDataMap params = new MDataMap();
		Map requestParams = request.getParameterMap();

		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}

			list.add(name + "=" + valueStr);
			params.put(name, URLDecoder.decode(valueStr));
		}

		Collections.sort(list); // 对List内容进行排序

		for (String nameString : list) {
			str.append(nameString + "&");
		}
		endStr = str.substring(0, str.toString().length() - 1);

		AlipayProcessService alipayProcessService = new AlipayProcessService();

		PayResult payResult = alipayProcessService.resultWebValue(params,
				endStr); // 将数据插入到oc_payment 验签
		if (payResult.upFlagTrue()) {

			orderShoppingService.deletefamilySkuToShopCart(payResult
					.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品 不同步支付数据
			
			/**
			 * 同步kJT支付完成后的订单
			 */
			OrderService os = new OrderService();
			List<Map<String, Object>> listMap = os.ifKJTOrder(payResult.getOrderCode(), "SF03KJT");
			for(Map<String, Object> map : listMap) {
//				JobExecHelper.createExecInfo("449746990003", String.valueOf(map.get("order_code")), null);
				JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, String.valueOf(map.get("order_code")), "" , "AlipayProcess line 362");
			}

			String sellerCode = "", memberCode = "", bigOrderCode = "";
			if(StringUtils.startsWith(payResult.getOrderCode(), "OS")) {
				List<MDataMap> orderInfoList = DbUp.upTable("oc_orderinfo").queryAll("zid,uid,order_code,order_status,buyer_code,seller_code,small_seller_code", "", "", new MDataMap("big_order_code",payResult.getOrderCode()));
				if(null != orderInfoList && orderInfoList.size() > 0) {
					MDataMap orderMap = orderInfoList.get(0);
					sellerCode = orderMap.get("seller_code");
					memberCode = orderMap.get("buyer_code");
					bigOrderCode = payResult.getOrderCode();
				}
			} else if(StringUtils.startsWith(payResult.getOrderCode(), "DD")) {
				MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code", payResult.getOrderCode());
				if(null != orderMap && !orderMap.isEmpty()) {
					sellerCode = orderMap.get("seller_code");
					memberCode = orderMap.get("buyer_code");
					bigOrderCode = orderMap.get("big_order_code");
				}
			}
			
			if(memberCode != null && sellerCode != null) {
				String mobileNO = new MemberLoginSupport().getMoblie(memberCode);
				if(StringUtils.isNotEmpty(mobileNO)) {
					JmsNoticeSupport.INSTANCE.sendQueue(
							JmsNameEnumer.OnDistributeCoupon,
							CouponConst.pay_coupon, new MDataMap(
									"mobile", mobileNO, "manage_code", sellerCode, 
									"big_order_code", bigOrderCode, "member_code", memberCode));
				}
			}
		}
	}

}
