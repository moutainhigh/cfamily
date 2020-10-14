package com.cmall.familyhas.orderpay;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.xmaspay.PaymentChannel;
import com.srnpr.xmaspay.PaymentResult;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.process.notify.ApplePayNotifyPayProcess;
import com.srnpr.xmaspay.process.notify.PayGateNotifyPayProcess;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.ApplePayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.UnionFenqiPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.UnionPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.VjmobiPayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.WechatPreparePayProcess;
import com.srnpr.xmaspay.process.refund.PayGateRefundNotifyProcess;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmaspay.util.PayServiceSupport;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.helper.WebSessionHelper;

public class OrderPayProcess extends BaseClass {

	/**
	 * APP支付宝付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public AlipayPreparePayProcess.PaymentResult aliPayAppPrepare(String bigOrderCode){
		return PayServiceSupport.aliPayPrepare(PaymentChannel.APP, bigOrderCode, "");
	}
	
	/**
	 * APP支付宝付款（网关渠道）（5.6.6 新版参数）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public AlipayPreparePayProcess.PaymentResult aliPayAppPrepareNew(String bigOrderCode){
		AlipayPreparePayProcess.PaymentInput input = new AlipayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.APP;
		input.v = 1; // 1 表示走新版参数
		return PayServiceFactory.getInstance().getAlipayPreparePayProcess().process(input);
	}
	
	/**
	 * APP支付宝H5付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @param isInApp 是否在app内 
	 * @return
	 */
	public AlipayPreparePayProcess.PaymentResult aliPayH5Prepare(String bigOrderCode, String returnUrl, boolean isInApp){
		AlipayPreparePayProcess.PaymentInput input = new AlipayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		if(isInApp) {
			input.payChannel = PaymentChannel.WAPAPP;
		}else {
			input.payChannel = PaymentChannel.WAP;
		}
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getAlipayPreparePayProcess().process(input);
	}
	
	/**
	 * APP支付宝H5付款（网关渠道）（5.6.6 新版参数）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @param isInApp 是否在app内 
	 * @return
	 */
	public AlipayPreparePayProcess.PaymentResult aliPayH5PrepareNew(String bigOrderCode, String returnUrl, boolean isInApp, String errurl){
		AlipayPreparePayProcess.PaymentInput input = new AlipayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		if(isInApp) {
			input.payChannel = PaymentChannel.WAPAPP;
		}else {
			input.payChannel = PaymentChannel.WAP;
		}
		input.reurl = returnUrl;
		input.v = 1; // 1 表示走新版参数
		input.errurl = errurl;
		return PayServiceFactory.getInstance().getAlipayPreparePayProcess().process(input);
	}
	
	/**
	 * 微信付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public WechatPreparePayProcess.PaymentResult wechatPrepare(String bigOrderCode){
		return PayServiceSupport.wechatPrepare(PaymentChannel.APP, bigOrderCode, "");
	}
	
	/**
	 * 微信JSAPI付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @param isInApp 是否在app内 
	 * @return
	 */
	public WechatPreparePayProcess.PaymentResult wechatJSAPIPrepare(String bigOrderCode, String openID, String returnUrl, boolean isInApp){
		WechatPreparePayProcess.PaymentInput input = new WechatPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.openid = openID;
		if(isInApp) {
			input.payChannel = PaymentChannel.WAPAPP;
		}else {
			input.payChannel = PaymentChannel.JSAPI;
		}
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getWechatPreparePayProcess().process(input);
	}
	
	/**
	 * 微信SMS短信付款（网关渠道）
	 * @param bigOrderCode 待支付大订单号
	 * @param isInApp 是否在app内 
	 * @return
	 */
	public WechatPreparePayProcess.PaymentResult wechatSmsPrepare(String bigOrderCode, String returnUrl){
		WechatPreparePayProcess.PaymentInput input = new WechatPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.WAPSMS;
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getWechatPreparePayProcess().process(input);
	}
	
	/**
	 * 苹果支付
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public ApplePayPreparePayProcess.PaymentResult applePayPrepare(String bigOrderCode){
		return PayServiceSupport.applePayPrepare(bigOrderCode, "");
	}
	
	/**
	 * 银联付款（APP网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public UnionPreparePayProcess.PaymentResult unionPayAppPrepare(String bigOrderCode){
		UnionPreparePayProcess.PaymentInput input = new UnionPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.APP;
		return PayServiceFactory.getInstance().getBean(UnionPreparePayProcess.class).process(input);
	}
	
	/**
	 * 银联付款（WAP网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @param isInApp 是否在app内 
	 * @return
	 */
	public UnionPreparePayProcess.PaymentResult unionPayWapPrepare(String bigOrderCode, String returnUrl, boolean isInApp){
		UnionPreparePayProcess.PaymentInput input = new UnionPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		if(isInApp) {
			input.payChannel = PaymentChannel.WAPAPP;
		}else {
			input.payChannel = PaymentChannel.WAP;
		}
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getBean(UnionPreparePayProcess.class).process(input);
	}
	
	/**
	 * 微匠支付（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public VjmobiPayPreparePayProcess.PaymentResult vjmobiPayWapPrepare(String bigOrderCode, String returnUrl, String payGate,String openid){
		VjmobiPayPreparePayProcess.PaymentInput input = new VjmobiPayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.WAP;
		input.memo1 = returnUrl;
		input.openid = openid;
		input.paygate = payGate;
		return PayServiceFactory.getInstance().getBean(VjmobiPayPreparePayProcess.class).process(input);
	}
	
	/**
	 * 银联付款 分期（WAP网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public UnionFenqiPreparePayProcess.PaymentResult unionPayFenqiWapPrepare(String bigOrderCode, String returnUrl){
		UnionFenqiPreparePayProcess.PaymentInput input = new UnionFenqiPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.WAP;
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getBean(UnionFenqiPreparePayProcess.class).process(input);
	}
	
	/**
	 * 银联付款 分期（APP网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public UnionFenqiPreparePayProcess.PaymentResult unionPayFenqiAppPrepare(String bigOrderCode, String returnUrl){
		UnionFenqiPreparePayProcess.PaymentInput input = new UnionFenqiPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.APP;
		input.reurl = returnUrl;
		return PayServiceFactory.getInstance().getBean(UnionFenqiPreparePayProcess.class).process(input);
	}
	
	/**
	 * 网关支付通知
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String handlePayGateNotify(){
		HttpServletRequest req = WebSessionHelper.create().upHttpRequest();
		Enumeration<String> names = req.getParameterNames();
		Map<String,String> notifyParam = new HashMap<String, String>();
		String name;
		while(names.hasMoreElements()){
			name = names.nextElement();
			notifyParam.put(name, StringUtils.trimToEmpty(req.getParameter(name)));
		}
		
		PayGateNotifyPayProcess.PaymentResult result = PayServiceSupport.payGateNotify(notifyParam);
		
		if(result.getResultCode() == PaymentResult.SUCCESS){
			updateOrderStatusPayed(result.notify.bigOrderCode);
			saveFromSmsOrder(result.notify.bigOrderCode);
		}
		
		StringBuilder build = new StringBuilder();
		// 出现签名错误时暂时让网关重复通知一下
		if(result.getResultMessage() != null && result.getResultMessage().contains("签名错误")){
			build.append("<result>0</result><reURL>"+result.reURL+"</reURL>");
		}else{
			build.append("<result>1</result><reURL>"+result.reURL+"</reURL>");
		}
		
		if(result.getResultCode() != PaymentResult.SUCCESS){
			build.append("<msg>").append(result.getResultMessage()).append("</msg>");
		}
		return build.toString();
	}
	
	/**
	 * 苹果支付结果通知
	 * @param notifyParam 通知参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String handleApplePayNotify(){
		Map<String,String> param = new HashMap<String, String>();
		ServletInputStream in = null;
		try {
			in = WebSessionHelper.create().upHttpRequest().getInputStream();
			String jsonText = IOUtils.toString(in, "UTF-8");
			param = JSONObject.parseObject(jsonText, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		ApplePayNotifyPayProcess.PaymentResult result = PayServiceSupport.applePayNotify(param);
		if(result.getResultCode() == PaymentResult.SUCCESS){
			updateOrderStatusPayed(result.notify.bigOrderCode);
		}
		
		return "{\"ret_code\": \"0000\",\"ret_msg\": \"success\"}";
	}
	
	/**
	 * 网关退款通知
	 * @return
	 */
	public String handlePayGateRefund(){
		String sign = WebSessionHelper.create().upHttpRequest().getParameter("sign");
		String postData = null;
		ServletInputStream in = null;
		try {
			in = WebSessionHelper.create().upHttpRequest().getInputStream();
			postData = IOUtils.toString(in, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		if(StringUtils.isBlank(sign)){
			return "{\"result\": 100,\"descrption\": \"缺少参数sign\"}";
		}
		
		if(StringUtils.isBlank(postData)){
			return "{\"result\": 100,\"descrption\": \"PostBody内容不能为空\"}";
		}
		
		PayGateRefundNotifyProcess process = PayServiceFactory.getInstance().getBean(PayGateRefundNotifyProcess.class);
		PayGateRefundNotifyProcess.PaymentInput input = new PayGateRefundNotifyProcess.PaymentInput();
		input.sign = sign;
		input.postData = postData;
		PayGateRefundNotifyProcess.PaymentResult result = process.process(input);
		
		if(result.getResultCode() != PaymentResult.SUCCESS){
			return "{\"result\": 100,\"descrption\": \""+result.getResultMessage()+"\"}";
		}
		
		return "{\"result\": 1,\"descrption\": \"操作成功\"}";
	}
	
	/**
	 * 更改订单为已支付状态
	 * @param bigOrderCode
	 */
	@SuppressWarnings("deprecation")
	private void updateOrderStatusPayed(String bigOrderCode){
		//List<MDataMap> orderInfoList = DbUp.upTable("oc_orderinfo").queryAll("zid,uid,order_code,order_status,buyer_code,seller_code,small_seller_code", "", "", new MDataMap("big_order_code",bigOrderCode));
		List<Map<String,Object>> orderInfoList = DbUp.upTable("oc_orderinfo").upTemplate().queryForList("select zid,uid,order_code,order_status,buyer_code,seller_code,due_money,small_seller_code,delivery_store_type from oc_orderinfo where big_order_code = :big_order_code", new MDataMap("big_order_code",bigOrderCode));
		String memberCode = null;
		String sellerCode = null;
		String smallSellerCode = null;
		MDataMap updateMap;
		for(Map<String,Object> map : orderInfoList) {
			if(FamilyConfig.ORDER_STATUS_UNPAY.equals(map.get("order_status"))){
				/*
				FlowBussinessService fs = new FlowBussinessService();
				String flowBussinessUid = map.get("uid")+"";
				String fromStatus = map.get("order_status")+"";
				String toStatus = FamilyConfig.ORDER_STATUS_PAYED;//下单成功-未发货
				String flowType = "449715390008";
				String userCode = "system";
				String remark = "auto by system";
				MDataMap md = new MDataMap();
				md.put("order_code", map.get("order_code")+"");
				fs.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, md);
				*/
				
				updateMap = new MDataMap();
				updateMap.put("zid", map.get("zid")+"");
				updateMap.put("order_status", FamilyConfig.ORDER_STATUS_PAYED);
				updateMap.put("update_time", FormatHelper.upDateTime());
				DbUp.upTable("oc_orderinfo").dataUpdate(updateMap, "order_status,update_time", "zid");
				
				updateMap = new MDataMap();
				updateMap.put("code", map.get("order_code")+"");
				updateMap.put("info", "订单支付");
				updateMap.put("create_time", FormatHelper.upDateTime());
				updateMap.put("create_user", "system");
				updateMap.put("old_status", FamilyConfig.ORDER_STATUS_UNPAY);
				updateMap.put("now_status", FamilyConfig.ORDER_STATUS_PAYED);
				DbUp.upTable("lc_orderstatus").dataInsert(updateMap);
				
				memberCode = map.get("buyer_code")+"";
				sellerCode = map.get("seller_code")+"";
				smallSellerCode = map.get("small_seller_code")+"";
				if(FamilyConfig.hjy.equals(smallSellerCode) || FamilyConfig.jyh.equals(smallSellerCode)){
					JobExecHelper.createExecInfo(Constants.ZA_EXEC_TYPE_SYNC_PAYINFO_LD, map.get("order_code")+"", "");
				}
				
				if("SF03KJT".equals(smallSellerCode)){
//					JobExecHelper.createExecInfo(Constants.ZA_EXEC_TYPE_SYNC_KJT, map.get("order_code"), "");
					JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, map.get("order_code")+"" , "" , "OrderPayProcess line 122");
				}
				
				//判断是否为拼团订单，如果是的话，修改拼团状态
				String orderCode = MapUtils.getString(map, "order_code", "");
				MDataMap collageItemMap = DbUp.upTable("sc_event_collage_item").onePriLib("collage_ord_code", orderCode, "is_confirm", "449748320001");
				if(collageItemMap != null) {
					String collageCode = MapUtils.getString(collageItemMap, "collage_code", "");
					if(!"".equals(collageCode)) {
						String collageNeedCountSql = "select (("
									+ "select i.collage_person_count from sc_event_collage c, sc_event_info i where c.collage_code = '" + collageCode + "' and c.event_code = i.event_code"
								+ ")-("
									+ "select count(1) from sc_event_collage_item im where im.collage_code = '" + collageCode + "' and im.is_confirm = '449748320002'"
								+ ")) from dual";
						Integer needCount = DbUp.upTable("sc_event_collage_item").upTemplate().queryForInt(collageNeedCountSql, new HashMap<String, Object>());
						if(needCount != null) {
							if(needCount > 1) {//算上当前付款用户还不能凑成一个团，仅更新明细表为已确认
								DbUp.upTable("sc_event_collage_item").dataUpdate(new MDataMap("zid", MapUtils.getString(collageItemMap, "zid", ""), "is_confirm", "449748320002"), "is_confirm", "zid");
							}else if(needCount == 1) {//算上当前付款用户正好凑成一个团，更新明细表为已确认并且更新拼团主表为拼团成功
								DbUp.upTable("sc_event_collage_item").dataUpdate(new MDataMap("zid", MapUtils.getString(collageItemMap, "zid", ""), "is_confirm", "449748320002"), "is_confirm", "zid");
								DbUp.upTable("sc_event_collage").dataUpdate(new MDataMap("collage_code", collageCode, "collage_status", "449748300002"), "collage_status", "collage_code");
							}else {//当前团已成团，重新开一个新团，将拼团明细表的拼团标示修改为新成立的团
								MDataMap collageMap = DbUp.upTable("sc_event_collage").onePriLib("collage_code", collageCode);
								String newCollageCode = WebHelper.upCode("PT");
								String eventCode = MapUtils.getString(collageMap, "event_code", "");
								/**
								 * 查询拼团时效以及活动结束时间
								 */
								LoadEventInfo load = new LoadEventInfo();
								PlusModelEventQuery tQuery = new PlusModelEventQuery();
								tQuery.setCode(eventCode);
								PlusModelEventInfo eventInfo = load.upInfoByCode(tQuery);
								String endTime = eventInfo.getEndTime();//获取活动结束时间
								String expireTime ="";
								Integer timeliness = eventInfo.getCollageTimeLiness();//获取拼团时效
								if(timeliness == -1||timeliness==null) {
									expireTime = endTime;
								}else {
									expireTime = DateUtil.addDateMinut(DateUtil.getSysDateTimeString(), timeliness);
								}
								if(!DateUtil.compareDateTime(expireTime, endTime)) {
									expireTime = endTime;
								}
								DbUp.upTable("sc_event_collage").dataInsert(new MDataMap("collage_code", newCollageCode, "event_code", eventCode, "collage_status", "449748300001", 
										"create_time", DateUtil.getSysDateTimeString(),"expire_time",expireTime));
								DbUp.upTable("sc_event_collage_item").dataUpdate(new MDataMap("zid", MapUtils.getString(collageItemMap, "zid", ""), "collage_code", newCollageCode, "is_confirm", "449748320002", 
										"re_collage", "1", "collage_member_type", "449748310001"), "collage_code,is_confirm,re_collage,collage_member_type", "zid");
							}
						}
					}
				}
				
				/**
				 * 如果是网易考拉订单，用户支付完成后需要调用支付接口,插入一个定时任务
				 */
				if(AppConst.MANAGE_CODE_WYKL.equals(smallSellerCode)) {
					if(new BigDecimal("0.00").compareTo(new BigDecimal(map.get("due_money")+"")) < 0) {
						//非0元单时写入定时
						payKaolaOrder(map.get("order_code").toString());
					}
				}
				
				/**京东商品订单创建下单任务*/
				if(com.srnpr.xmassystem.Constants.SMALL_SELLER_CODE_JD.equals(smallSellerCode)) {
					JobExecHelper.createExecInfo("449746990017", map.get("order_code")+"", null);
				}
				
				/**多货主商品订单创建下单任务*/
				String delivery_store_type = map.get("delivery_store_type")+"";
				if("4497471600430002".equals(delivery_store_type)) {
					JobExecHelper.createExecInfo("449746990018", map.get("order_code")+"", null);
				}
			}
		}
		
		if(memberCode != null && sellerCode != null){
			String mobileNO = new MemberLoginSupport().getMoblie(memberCode);
			if(StringUtils.isNotEmpty(mobileNO)) {
				JmsNoticeSupport.INSTANCE.sendQueue(
						JmsNameEnumer.OnDistributeCoupon,
						CouponConst.pay_coupon, new MDataMap(
								"mobile", mobileNO, "manage_code", sellerCode, 
								"big_order_code", bigOrderCode, "member_code", memberCode));
				
			}
		}
		
		//付款成功后订单发送通知 20180327 --rhb
		String order_sources = bConfig("xmasorder.send_notice_order_source");
		List<Map<String, Object>> orderInfo = DbUp.upTable("oc_orderinfo").listByWhere("big_order_code", bigOrderCode);
		
		if(null != orderInfo && orderInfo.size() > 0) {
			String order_source = orderInfo.get(0).get("order_source") + "";
			if(order_sources.contains(order_source)) {//通过订单来源判断是否发送通知
				
				MDataMap parmas = new MDataMap();
				parmas.put("big_order_code", bigOrderCode);
				parmas.put("isPayed", "true");
				String str = JSON.toJSONString(parmas);
				
				//写入定时任务，定时执行发送下单成功通知
				JobExecHelper.createExecInfo("449746990005", str, null);
			}
		}
	}
	
	/**
	 * 记录从短信链接支付的订单
	 * @param bigOrderCode
	 */
	private void saveFromSmsOrder(String bigOrderCode) {
		// 全局控制参数
		if(!"1".equals(XmasKv.upFactory(EKvSchema.SmsPayFlag).get("all"))) {
			return;
		}
		
		// 是否小程序支付标识
		if(!"1".equals(XmasKv.upFactory(EKvSchema.SmsPayFlag).get(bigOrderCode))) {
			return;
		}
		
		// 清理标识
		XmasKv.upFactory(EKvSchema.SmsPayFlag).del(bigOrderCode);
		
		List<MDataMap> orderList = DbUp.upTable("oc_orderinfo").queryAll("order_code", "", "", new MDataMap("big_order_code", bigOrderCode));
		
		String orderCode;
		for(MDataMap m : orderList) {
			orderCode = m.get("order_code");
			
			// 记录短信链接支付订单号
			if(DbUp.upTable("oc_order_pay_fromsms").count("order_code",orderCode) == 0) {
				DbUp.upTable("oc_order_pay_fromsms").dataInsert(new MDataMap(
						"order_code", orderCode,
						"create_time", FormatHelper.upDateTime()
						));
			}
		}
		
	}
	
	/**
	 * 将网易考拉取消订单的任务写入定时
	 * @param order_code
	 */
	private void payKaolaOrder(String order_code) {
		//写入定时任务，定时执行返还微公社金额
		MDataMap jobMap = new MDataMap();
		jobMap.put("uid", WebHelper.upUuid());
		jobMap.put("exec_code", WebHelper.upCode("ET"));
		jobMap.put("exec_type", "449746990013");
		jobMap.put("exec_info", order_code);
		jobMap.put("create_time", DateUtil.getSysDateTimeString());
		jobMap.put("begin_time", "");
		jobMap.put("end_time", "");
		jobMap.put("exec_time", DateUtil.getSysDateTimeString());
		jobMap.put("flag_success","0");
		jobMap.put("remark", "OrderPayProcess line 384");
		jobMap.put("exec_number", "0");
		DbUp.upTable("za_exectimer").dataInsert(jobMap);
	}
}
