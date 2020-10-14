package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiCreateOrderInput;
import com.cmall.familyhas.api.result.APiCreateOrderResult;
import com.cmall.familyhas.api.result.WeChatpaymentResult;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.api.GiftVoucherInfo;
import com.cmall.familyhas.orderpay.OrderPayProcess;
import com.cmall.familyhas.service.CouponService;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.familyhas.active.ActiveReq;
import com.cmall.ordercenter.familyhas.active.product.ActiveForSource;
import com.cmall.ordercenter.service.ApiAlipayMoveProcessService;
import com.cmall.ordercenter.service.ApiWechatProcessService;
import com.cmall.ordercenter.service.OrderShoppingService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.cmall.systemcenter.util.AESUtil;
import com.cmall.systemcenter.util.MD5Code;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.WechatPreparePayProcess;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 惠家有创建订单接口
 * 
 * @author xiegj
 * 
 */
public class APiCreateOrder extends
		RootApiForToken<APiCreateOrderResult, APiCreateOrderInput> {

	public APiCreateOrderResult Process(APiCreateOrderInput inputParam,
			MDataMap mRequestMap) {
		
		APiCreateOrderResult result = new APiCreateOrderResult();
		String api_token = getApiClient().get("api_token");
		String encryptToken = inputParam.getEncryptToken();
		if(1 == Integer.parseInt(bConfig("familyhas.confirmpassword"))) {
			if(StringUtils.isNotBlank(api_token) && StringUtils.isNotBlank(encryptToken)) {
				AESUtil aesUtil = new AESUtil();
				aesUtil.initialize();
				String decryptToken = aesUtil.decrypt(encryptToken);
				if(!api_token.endsWith(decryptToken.split("_")[0])) {
					result.inErrorMessage(969905919);
					return result;
				}
			}else {
				result.inErrorMessage(969905919);
				return result;
			}
		}
		List<GoodsInfoForAdd> goodF = new ArrayList<GoodsInfoForAdd>();
		List<ActiveReq> activeRequests = new ArrayList<ActiveReq>();
		
		String sLockKey=KvHelper.lockCodes(10, getUserCode());
		
		if(StringUtils.isBlank(sLockKey))
		{
			result.inErrorMessage(939301124);
			return result;
		}
		
		if (inputParam.getGoods() != null && !inputParam.getGoods().isEmpty()) {
			for (int i = 0; i < inputParam.getGoods().size(); i++) {
				ShopCartService se = new ShopCartService();
				GoodsInfoForAdd add = inputParam.getGoods().get(i);
				add.setSku_code(se.getSkuCodeForValue(add.getProduct_code(),
						add.getSku_code()));
				goodF.add(add);
				if (add.getSku_num() <= 0) {
					result.setResultCode(916401118);
					result.setResultMessage(bInfo(916401118));
					return result;
				}
				ActiveReq activeReq = new ActiveReq();
				activeReq.setBuyer_code(getUserCode());
				activeReq.setProduct_code(add.getProduct_code());
				activeReq.setSku_code(add.getSku_code());
				activeReq.setSku_num(add.getSku_num());
				activeRequests.add(activeReq);
			}
		} else {
			result.setResultCode(916401118);
			result.setResultMessage(bInfo(916401118));
			return result;
		}

		String sLockKeyString = "";

		inputParam.setGoods(goodF);
		inputParam.setBuyer_code(getUserCode());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_type", inputParam.getOrder_type());// 订单类型
		map.put("order_souce", inputParam.getOrder_souce());// 订单来源
		map.put("buyer_code", getUserCode());// 买家编号
		map.put("buyer_address_id", inputParam.getBuyer_address_id());// 收货人维护地址的维护编号
		map.put("buyer_name", inputParam.getBuyer_name());// 收货人姓名
		map.put("buyer_address", inputParam.getBuyer_address());// 收件人地址
		map.put("buyer_address_code", inputParam.getBuyer_address_code());// 收货人地址第三极编号
		map.put("buyer_mobile", inputParam.getBuyer_mobile());// 收件人手机号
		map.put("pay_type", inputParam.getPay_type());// 支付类型
		map.put("billInfo", inputParam.getBillInfo());// 发票信息 bill_Type发票类型
														// bill_title发票抬头
														// bill_detail发票内容
		map.put("goods", inputParam.getGoods());// 商品列表
		map.put("seller_code", getManageCode());
		map.put("app_vision", inputParam.getVersion());
		map.put("check_pay_money", inputParam.getCheck_pay_money());
		
		/*
		 * 判断是否是一元购订单    fq++
		 */
		if(inputParam.getOrder_type().equals("449715200014")) {
			String yygOrderNo = inputParam.getYygOrderNo();
			String yygMac = inputParam.getYygMac();
			String yygPayMoney = inputParam.getYygPayMoney();
			
			String key = bConfig("familyhas.sync_yyg_key");
			/*验证加密字符串*/
			try {
				String encode = MD5Code.encode(yygOrderNo+yygPayMoney+key);
				if(StringUtils.isNotBlank(yygMac) && !encode.equals(yygMac)) {
					result.setResultCode(3);
					result.setResultMessage("一元购加密认证失败");
					return result;
				}
			} catch (UnsupportedEncodingException e) {
				result.setResultCode(3);
				result.setResultMessage("一元购加密认证失败");
				return result;
			}
			
		}
		//一元购参数添加
		map.put("yygOrderNo", inputParam.getYygOrderNo());
		map.put("yygPayMoney", inputParam.getYygPayMoney());
		
		List<String> couponCodes = new ArrayList<String>();
		if (inputParam.getCoupon_codes() != null
				&& !inputParam.getCoupon_codes().isEmpty()) {
			sLockKeyString = WebHelper.addLock(60, inputParam.getCoupon_codes()
					.get(0));

			if (StringUtils.isBlank(sLockKeyString)) {
				result.inErrorMessage(916401230);
				return result;
			}
			//如果是礼金券，可以叠加使用
			couponCodes = inputParam.getCoupon_codes();
		}
		if(Double.valueOf(inputParam.getWgsUseMoney())>=0){
			map.put("wgsUseMoney", String.valueOf(inputParam.getWgsUseMoney()));
		}
		map.put("channelId", inputParam.getChannelId());
		if (inputParam.getGoods() == null || inputParam.getGoods().isEmpty()) {
			result.setResultCode(916401113);
			result.setResultMessage(bInfo(916401113));
			return result;
		}
		bLogError(0, "--------------------------------------------------------");
		// 校验购买条件是否符合
		ShopCartService service = new ShopCartService();
		RootResult rootResult = service.checkGoodsLimit(getManageCode(),
				getUserCode(), inputParam.getGoods());
		if (rootResult.getResultCode() > 1) {
			result.setResultCode(rootResult.getResultCode());
			result.setResultMessage(rootResult.getResultMessage());
			return result;
		}else {
			RootResultWeb ww = new RootResultWeb();
			new ActiveForSource().checkVipSpecialLimitCount(activeRequests, ww);
			if(ww.getResultCode()>1){
				result.setResultCode(ww.getResultCode());
				result.setResultMessage(ww.getResultMessage());
				return result;
			}
		}
		//下单商品绑定的分销信息
		map.put("productSharedInfos", inputParam.getProductSharedInfos());
		Map<String, Object> re = service.createOrder(map, couponCodes);
		if (re.containsKey("resultCode")) {
			result.setResultCode(Integer.valueOf(re.get("resultCode")
					.toString()));
			result.setResultMessage(re.get("resultMessage").toString());
			return result;
		}
		//回写ld使用的礼金券明细（hjy订单DD编号、礼金券编码）
		if (re.containsKey("reWriteLD")) {
			List<GiftVoucherInfo> reWriteLD = (List<GiftVoucherInfo>)re.get("reWriteLD");
			if(reWriteLD != null && reWriteLD.size() > 0) {
				new CouponService().reWriteGiftVoucherToLD(reWriteLD, "U");
			}
		}
		boolean ngFlag = new ActiveForSource().checkIsVipSpecialForFamilyhas(getUserCode());
		boolean ngsjFlag = new ActiveForSource().checkIsVipSpecialDayForFamilyhas();
		if(re.containsKey("check_pay_money_error")&&ngFlag&&ngsjFlag){
			result.setResultCode(916421248);
			result.setResultMessage(re.get("check_pay_money_error").toString());
			return result;
		}else if (re.containsKey("check_pay_money_error")) {
			result.setResultCode(916401133);
			result.setResultMessage(re.get("check_pay_money_error").toString());
			return result;
		}

		if (re.containsKey("error")
				&& re.get("error") != null
				&& !"".equals(re.get("error").toString())
				&& (re.containsKey("order_code") && !re.get("order_code")
						.toString().equals(re.get("error").toString()))) {
			result.setResultCode(2);
			result.setResultMessage(re.get("error").toString());
			return result;
		} else {
			this.saveClientInfo(inputParam, re.get("order_code").toString());

			// 同步家有
			BigDecimal dm = new BigDecimal(0.00);// 拆单时应付总款
			if (getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS) || getManageCode()
							.equals(MemberConst.MANAGE_CODE_SPDOG)) {// 后台逻辑代码版本控制
				// 拆单*************************************start****************************************拆单
					List<MDataMap> orderCodes = DbUp.upTable("oc_orderinfo")
							.queryAll("order_code,due_money,small_seller_code","","",new MDataMap("big_order_code", re.get(
											"order_code").toString()));
					for (int i = 0; i < orderCodes.size(); i++) {
						dm = dm.add(new BigDecimal(orderCodes.get(i).get("due_money")));
						String oc = orderCodes.get(i).get("order_code");
						if(com.cmall.systemcenter.util.StringUtility.isNotNull(orderCodes.get(i).get("small_seller_code"))
								&&!orderCodes.get(i).get("small_seller_code").startsWith("SF03")
								&&!MemberConst.MANAGE_CODE_SPDOG.equals(orderCodes.get(i).get("small_seller_code"))){
							this.saveOrder(oc);
						}else if (AppConst.MANAGE_CODE_KJT.equals(orderCodes.get(i).get("small_seller_code"))&&
								new BigDecimal(orderCodes.get(i).get("due_money")).compareTo(new BigDecimal(0.00))==0) {
//							JobExecHelper.createExecInfo("449746990003", oc, null);
							JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, oc, "" , "APiCreateOrder line 233");
						}
					}
				// 拆单*************************************end****************************************拆单
			}
			result.setOrder_code(re.get("order_code").toString());
			if (inputParam.getPay_type() != null
					&& "449716200002".equals(inputParam.getPay_type())) {// 货到付款
				result.setPay_url("");
				result.setSign_detail("");
				if(VersionHelper.checkServerVersion("3.5.62.55")&& (AppConst.MANAGE_CODE_HOMEHAS.equals(getManageCode()) || AppConst.MANAGE_CODE_CDOG.equals(getManageCode()))){//货到付款下单送优惠券
					JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
							CouponConst.al_order_coupon,new MDataMap("mobile", getOauthInfo().getLoginName(), "manage_code", getManageCode()));
				}
			} else {// 在线支付
				if (dm.compareTo(new BigDecimal(0.00)) == 0) {
					result.setPay_url("");
					result.setSign_detail("");
				} else {
					if("449716200001".equals(inputParam.getPay_type())||"449746280003".equals(inputParam.getPay_type())){
						
						ApiAlipayMoveProcessService se = new ApiAlipayMoveProcessService();
                        //result.setPay_url(se.alipayMoveParameter(re.get("order_code").toString(),false));
                        //result.setSign_detail(se.alipaySign(re.get("order_code").toString()).get("sign"));
						
						AlipayPreparePayProcess.PaymentResult paymentResult = new OrderPayProcess().aliPayAppPrepare(re.get("order_code").toString());
						if(paymentResult.upFlagTrue()){
							result.setPay_url(paymentResult.payInfo);
							// 已确认，客户端不需要单独的签名内容
							result.setSign_detail(se.alipaySign(re.get("order_code").toString()).get("sign"));
						}
						//把此订单的支付状态  放入缓存中,  目的是为了在详情页中查询此订单的支付方式
						new PlusSupportPay().fixPayFrom(re.get("order_code").toString(), "449746280003");  
					}else if ("449746280005".equals(inputParam.getPay_type())) {
						//惠家友微信支付
						if(AppConst.MANAGE_CODE_HOMEHAS.equals(getManageCode())){
							/*
							ApiWechatProcessService wechatProcessService = new ApiWechatProcessService();
							Map weChatMap = wechatProcessService.wechatMovePaymentVersionNew(re.get("order_code").toString(), inputParam.getPay_ip(), rootResult);
							if(weChatMap!=null&&!weChatMap.isEmpty()){
								result.getChatpayment().setAppid(weChatMap.get("appid").toString());
								result.getChatpayment().setMch_id(weChatMap.get("mch_id").toString());
								result.getChatpayment().setNonce_str(weChatMap.get("nonce_str").toString());
								result.getChatpayment().setPrepay_id(weChatMap.get("prepay_id").toString());
								result.getChatpayment().setResult_code(weChatMap.get("result_code").toString());
								result.getChatpayment().setReturn_code(weChatMap.get("return_code").toString());
								result.getChatpayment().setSign(weChatMap.get("sign").toString());
								result.getChatpayment().setTrade_type(weChatMap.get("trade_type").toString());
								result.getChatpayment().setTimestamp(weChatMap.get("timestamp").toString());
							}
							*/
							WechatPreparePayProcess.PaymentResult paymentResult = new OrderPayProcess().wechatPrepare(re.get("order_code").toString());
							WeChatpaymentResult wcpayment = new WeChatpaymentResult();
							if(result.upFlagTrue()){
								wcpayment.setAppid(paymentResult.appid);	
								wcpayment.setMch_id(paymentResult.partnerid);
								wcpayment.setNonce_str(paymentResult.noncestr);
								wcpayment.setPrepay_id(paymentResult.prepayid);
								wcpayment.setSign(paymentResult.sign);
								wcpayment.setTrade_type("APP");
								wcpayment.setTimestamp(paymentResult.timestamp);
								result.setChatpayment(wcpayment);
							}
							new PlusSupportPay().fixPayFrom(re.get("order_code").toString(), "449746280005");  
						}else if(AppConst.MANAGE_CODE_CDOG.equals(getManageCode())) {
							//沙皮狗微信支付
							ApiWechatProcessService wechatProcessService = new ApiWechatProcessService();
							Map weChatMap = wechatProcessService.wechatMovePaymentVersionShaPIGou(re.get("order_code").toString(), inputParam.getPay_ip(), rootResult);
							if(weChatMap!=null&&!weChatMap.isEmpty()){
								result.getChatpayment().setAppid(weChatMap.get("appid").toString());
								result.getChatpayment().setMch_id(weChatMap.get("mch_id").toString());
								result.getChatpayment().setNonce_str(weChatMap.get("nonce_str").toString());
								result.getChatpayment().setPrepay_id(weChatMap.get("prepay_id").toString());
								result.getChatpayment().setResult_code(weChatMap.get("result_code").toString());
								result.getChatpayment().setReturn_code(weChatMap.get("return_code").toString());
								result.getChatpayment().setSign(weChatMap.get("sign").toString());
								result.getChatpayment().setTrade_type(weChatMap.get("trade_type").toString());
								result.getChatpayment().setTimestamp(weChatMap.get("timestamp").toString());
							}
							
						}
						
						
					}
				}
				if(VersionHelper.checkServerVersion("3.5.62.55")&&(AppConst.MANAGE_CODE_HOMEHAS.equals(getManageCode()) || AppConst.MANAGE_CODE_CDOG.equals(getManageCode()))){//在线支付下单送优惠券
					JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
							CouponConst.order_coupon,new MDataMap("mobile", getOauthInfo().getLoginName(), "manage_code", getManageCode()));
				}
			}
			if ("449716200002".equals(inputParam.getPay_type())) {
				new OrderShoppingService().deleteSkuToShopCartNotstatus(re.get("order_code").toString());
			}
			if (inputParam.getGoods() != null
					&& !inputParam.getGoods().isEmpty()) {
				for (int i = 0; i < inputParam.getGoods().size(); i++) {// 更新购物车数据为已结算
					GoodsInfoForAdd add = inputParam.getGoods().get(i);
					ShopCartService ss = new ShopCartService();
					ss.updateAccountFlag(getUserCode(), add.getSku_code());
				}
			}
		}

		if (StringUtils.isNotBlank(sLockKeyString)) {
			WebHelper.unLock(sLockKeyString);
		}
		return result;
	}
	
	private void saveOrder(String liOrderCode){
		MDataMap jobMap = new MDataMap();
		jobMap.put("uid", WebHelper.upUuid());
		jobMap.put("exec_code", WebHelper.upCode("ET"));
		jobMap.put("exec_type", "449746990002");
		jobMap.put("exec_info", liOrderCode);
		jobMap.put("create_time", DateUtil.getSysDateTimeString());
		jobMap.put("begin_time", "");
		jobMap.put("end_time", "");
		jobMap.put("exec_time", DateUtil.getSysDateTimeString());
		jobMap.put("flag_success","0");
		jobMap.put("remark", "APiCreateOrder line 355 saveOrder()");
		jobMap.put("exec_number", "0");
		DbUp.upTable("za_exectimer").dataInsert(jobMap);
	}
	//保存终端信息
	private void saveClientInfo(APiCreateOrderInput inputParam,String orderCode){
		try {
			MDataMap clientMap = new MDataMap();
			if (StringUtility.isNotNull(inputParam.getApp_vision())) {
				clientMap.put("version", inputParam.getApp_vision());
			}
			if (StringUtility.isNotNull(inputParam.getModel())) {
				clientMap.put("model", inputParam.getModel());
			}
			if (StringUtility.isNotNull(inputParam.getUniqid())) {
				clientMap.put("uniqid", inputParam.getUniqid());
			}
			if (StringUtility.isNotNull(inputParam.getMac())) {
				clientMap.put("mac", inputParam.getMac());
			}
			if (StringUtility.isNotNull(inputParam.getOs())) {
				clientMap.put("os", inputParam.getOs());
			}
			if (StringUtility.isNotNull(inputParam.getFrom())) {
				clientMap.put("from_code", inputParam.getFrom());
			}
			if (StringUtility.isNotNull(inputParam.getScreen())) {
				clientMap.put("screen", inputParam.getScreen());
			}
			if (StringUtility.isNotNull(inputParam.getOp())) {
				clientMap.put("op", inputParam.getOp());
			}
			if (StringUtility.isNotNull(inputParam.getProduct())) {
				clientMap.put("product", inputParam.getProduct());
			}
			if (StringUtility.isNotNull(inputParam.getNet_type())) {
				clientMap.put("net_type", inputParam.getNet_type());
			}
			if (StringUtility.isNotNull(inputParam.getOs_info())) {
				clientMap.put("os_info", inputParam.getOs_info());
			}
			clientMap.put("order_code", orderCode);
			clientMap.put("seller_code", getManageCode());
			clientMap.put("create_user", getUserCode());
			clientMap.put("create_time", FormatHelper.upDateTime());
			new ShopCartService().saveClienInfo(clientMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
