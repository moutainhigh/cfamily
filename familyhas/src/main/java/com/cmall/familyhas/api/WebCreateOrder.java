package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.WebCreateOrderInput;
import com.cmall.familyhas.api.result.WebCreateOrderResult;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.service.MemberLevelService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.homehas.RsyncEmployAccountInfo;
import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.OrderItemList;
import com.cmall.ordercenter.model.UserInfo;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠家有网站暂存款、储值金支付 
 * @author xiegj
 * 
 */
public class WebCreateOrder extends RootApiForToken <WebCreateOrderResult, WebCreateOrderInput>{

	public WebCreateOrderResult Process(WebCreateOrderInput inputParam,
			MDataMap mRequestMap) {
		WebCreateOrderResult result = new WebCreateOrderResult();
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.setCpsCode(inputParam.getCpsCode());
		//添加是够内购
		teslaXOrder.setIsPurchase(inputParam.getIsPurchase());
		//添加用户编号
		teslaXOrder.setIsMemberCode(inputParam.getIsMemberCode());
		teslaXOrder.getStatus().setExecStep(ETeslaExec.PCCreate);
		if(inputParam.getWgsUseMoney()>0){
			//先校验微公社余额
			GroupAccountInfoResult groupResult = new GroupAccountService().getAccountInfo(new GroupAccountService().getAccountCodeByMemberCode(getUserCode()));
			if("0".equals(groupResult.getFlagEnable())){
				result.setResultCode(916421164);
				result.setResultMessage(bInfo(916421164));
			}else if (Double.valueOf(groupResult.getWithdrawMoney())==0) {
				result.setResultCode(916421166);
				result.setResultMessage(bInfo(916421166));
			}else if (inputParam.getWgsUseMoney()>Double.valueOf(groupResult.getWithdrawMoney())) {
				result.setResultCode(916421165);
				result.setResultMessage(bInfo(916421165));
			}
			if(!result.upFlagTrue()){return result;}
		}
		// 应付款
		teslaXOrder.setCheck_pay_money(BigDecimal.valueOf(inputParam
				.getCheck_pay_money()));
		// 订单基本信息
		teslaXOrder.getUorderInfo().setBuyerCode(getUserCode());
		teslaXOrder.getUorderInfo().setBuyerMobile(getOauthInfo().getLoginName());
		teslaXOrder.getUorderInfo().setSellerCode(getManageCode());
		teslaXOrder.getUorderInfo().setOrderType(inputParam.getOrder_type());
		teslaXOrder.getUorderInfo().setOrderSource(inputParam.getOrder_souce());
		teslaXOrder.getUorderInfo().setPayType(inputParam.getPay_type());
		teslaXOrder.getUorderInfo().setAppVersion(inputParam.getApp_vision());
		// 商品信息
		List<GoodsInfoForAdd> infoForAdds = inputParam.getGoods();
		// 订单详细信息
		List<OrderItemList> orderItemList = new ArrayList<OrderItemList>();//考拉订单信息		
		String sSql = "select pc_productinfo.product_name,pc_productinfo.product_code,pc_skuinfo.sku_code,pc_productinfo.sell_productcode as kaola_productcode,pc_skuinfo.sell_productcode as kaola_skucode,pc_skuinfo.cost_price from pc_productinfo" + 
					  " left join pc_skuinfo on pc_productinfo.product_code = pc_skuinfo.product_code" +
					  " where pc_productinfo.product_code =:product_code and pc_skuinfo.sku_code =:sku_code and pc_productinfo.small_seller_code =:small_seller_code";
		String kaolaProduct = "";
		for (GoodsInfoForAdd goodsInfo : infoForAdds) {
			TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
			orderDetail.setProductCode(goodsInfo.getProduct_code());
			orderDetail.setSkuCode(goodsInfo.getSku_code());
			orderDetail.setSkuNum(goodsInfo.getSku_num());			
			String skuCode = goodsInfo.getSku_code();
			if(skuCode != null && "IC".equals(skuCode.substring(0, 2))) {
				skuCode = KaolaOrderService.upSkuCode(skuCode);
			}
			if(!"".equals(skuCode)) {
				Map<String, Object> map = DbUp.upTable("pc_productinfo").dataSqlOne(sSql, new MDataMap("product_code", goodsInfo.getProduct_code(),"sku_code",skuCode,"small_seller_code",AppConst.MANAGE_CODE_WYKL));
				if(map != null && map.get("kaola_productcode") != null && map.get("kaola_skucode") != null) {				
					OrderItemList item = new OrderItemList();
					item.setGoodsId(map.get("kaola_productcode").toString());
					item.setSkuId(map.get("kaola_skucode").toString());
					item.setBuyAmount(goodsInfo.getSku_num());
					item.setChannelSalePrice(new BigDecimal(map.get("cost_price").toString()));
					orderItemList.add(item);
					orderDetail.setIsKaolaGood(1);
					kaolaProduct += "|" + map.get("product_name");
					
					// 保存考拉商品的编码供后续拆单时使用
					orderDetail.setSell_productcode(item.getGoodsId());
					orderDetail.setSell_skucode(item.getSkuId());
				} 
			}	
			teslaXOrder.getOrderDetails().add(orderDetail);
		}
		if(orderItemList != null && orderItemList.size() > 0) {
			UserInfo userInfo = new UserInfo();
			AddressInformation ai = (new AddressService()).getAddressOne(inputParam.getBuyer_address_id(), getUserCode(), getManageCode());
			if(!KaolaOrderService.setOrderAddress(userInfo, ai.getArea_code())) {
				result.setResultCode(916421172);
				result.setResultMessage(bInfo(916421172));
				return result;
			}
			teslaXOrder.setIsKaolaOrder(1);
			//userInfo.setAccountId("lxhtest10@163.com");
			userInfo.setAccountId(getUserCode());//正式环境传用户编号
			userInfo.setName(ai.getAddress_name());//收货人
			userInfo.setMobile(ai.getAddress_mobile());//手机号码			
			userInfo.setAddress(ai.getAddress_street());
			if(!new KaolaOrderService().upKaolaConfirmOrderInterface(orderItemList, userInfo, teslaXOrder)) {
				result.setResultCode(963906057);
				result.setResultMessage("此商品库存不足，暂不支持购买！");
				return result;
			}	
		}
		// 订单地址
		teslaXOrder.getAddress().setAddress(inputParam.getBuyer_address());
		teslaXOrder.getAddress().setAddressCode(inputParam.getBuyer_address_id());
		teslaXOrder.getAddress().setAreaCode(inputParam.getBuyer_address_code());
		teslaXOrder.getAddress().setMobilephone(inputParam.getBuyer_mobile());
		teslaXOrder.getAddress().setReceivePerson(inputParam.getBuyer_name());
		teslaXOrder.getAddress().setRemark(inputParam.getRemark());
		teslaXOrder.getAddress().setInvoiceTitle(inputParam.getBillInfo().getBill_title());
		teslaXOrder.getAddress().setInvoiceType(inputParam.getBillInfo().getBill_Type());
		teslaXOrder.getAddress().setInvoiceContent(inputParam.getBillInfo().getBill_detail());
		// 用户支付相关
		teslaXOrder.getUse().setWgs_money(BigDecimal.valueOf(inputParam.getWgsUseMoney()));
		if (inputParam.getCoupon_codes() != null
				&& !inputParam.getCoupon_codes().isEmpty()) {
			teslaXOrder.getUse().setCoupon_codes(inputParam.getCoupon_codes());
		}
		//储值金、暂存款使用
		teslaXOrder.getUse().setCzj_money(inputParam.getCzj_money());
		teslaXOrder.getUse().setZck_money(inputParam.getZck_money());
		// 订单附加信息
		teslaXOrder.getExtras().setVision(inputParam.getApp_vision());
		teslaXOrder.getExtras().setFromCode(inputParam.getFrom());
		teslaXOrder.getExtras().setMac(inputParam.getMac());
		teslaXOrder.getExtras().setModel(inputParam.getModel());
		teslaXOrder.getExtras().setNetType(inputParam.getNet_type());
		teslaXOrder.getExtras().setOp(inputParam.getOp());
		teslaXOrder.getExtras().setOs(inputParam.getOs());
		teslaXOrder.getExtras().setOsInfo(inputParam.getOs_info());
		teslaXOrder.setPay_ip(inputParam.getPay_ip());
		teslaXOrder.getExtras().setProduct(inputParam.getProduct());
		teslaXOrder.getExtras().setScreen(inputParam.getScreen());
		teslaXOrder.getExtras().setUniqid(inputParam.getUniqid());
		// 渠道编号
		teslaXOrder.setChannelId(inputParam.getChannelId());
		
		//用户是否是黑名单用户或者警惕用户
		String mobile = getOauthInfo().getLoginName();
		String memberLevel = "";
		if(StringUtils.isNotBlank(mobile)) {
			memberLevel = new MemberLevelService().upMemberLevel(mobile);
		}
		if("90".equals(memberLevel)) {
			//黑名单会员
			result.setResultCode(916423402);
			result.setResultMessage(bInfo(916423402));
			return result;
		}	
				
		//执行创建订单
		TeslaXResult reTeslaXResult = new ApiConvertTeslaService()
				.ConvertOrder(teslaXOrder);
		
		if (reTeslaXResult.upFlagTrue()) {
			createKaolaOrder(teslaXOrder);
			doOthershing(teslaXOrder);
			Map weChatMap = new HashMap();
			result.setOrder_code(teslaXOrder.getUorderInfo().getBigOrderCode());
//			if (inputParam.getPay_type() != null&& "449716200002".equals(inputParam.getPay_type())) {// 货到付款
//				result.setPay_url("");
//				result.setSign_detail("");
//				
//				
//				//此处临时解决方案
//				if (teslaXOrder.getUorderInfo().getDueMoney().compareTo(BigDecimal.ZERO) == 0) {
//					String big_order_code=teslaXOrder.getUorderInfo().getBigOrderCode();
//	
//					for (MDataMap orderInfo : DbUp.upTable("oc_orderinfo").queryByWhere("big_order_code",big_order_code,"order_status","4497153900010001")) {
//						
//						String order_code=orderInfo.get("order_code");
//						
//						DbUp.upTable("oc_orderinfo").dataUpdate(new MDataMap("order_code",order_code,"order_status","4497153900010002","update_time",DateUtil.getSysDateTimeString()), "order_status,update_time", "order_code");
//						
//						DbUp.upTable("lc_orderstatus").dataInsert(new MDataMap("code",order_code,"create_time",DateUtil.getSysDateTimeString(),"create_user","system","old_status",orderInfo.get("order_status"),"now_status","4497153900010002","info","下单补救"));
//						
//					}
//				}
//				
//			} else {// 在线支付
//				if (teslaXOrder.getUorderInfo().getDueMoney().compareTo(BigDecimal.ZERO) == 0) {
//					result.setPay_url("");
//					result.setSign_detail("");
//					
//					//此处临时解决方案
//					String big_order_code=teslaXOrder.getUorderInfo().getBigOrderCode();
//					
//					for (MDataMap orderInfo : DbUp.upTable("oc_orderinfo").queryByWhere("big_order_code",big_order_code,"order_status","4497153900010001")) {
//						
//						String order_code=orderInfo.get("order_code");
//						
//						DbUp.upTable("oc_orderinfo").dataUpdate(new MDataMap("order_code",order_code,"order_status","4497153900010002","update_time",DateUtil.getSysDateTimeString()), "order_status,update_time", "order_code");
//						
//						DbUp.upTable("lc_orderstatus").dataInsert(new MDataMap("code",order_code,"create_time",DateUtil.getSysDateTimeString(),"create_user","system","old_status",orderInfo.get("order_status"),"now_status","4497153900010002","info","下单补救"));
//						
//					}
//					
//				} else {
//					if ("449716200001".equals(inputParam.getPay_type())
//							|| "449746280003".equals(inputParam.getPay_type())) {
//						ApiMoveTeslaProcessService se = new ApiMoveTeslaProcessService();
//						result.setPay_url(se.alipayMoveParameter(teslaXOrder
//								.getUorderInfo().getBigOrderCode(), false,
//								teslaXOrder.getUorderInfo().getSellerCode(),
//								String.valueOf(teslaXOrder.getUorderInfo().getDueMoney())));
//						result.setSign_detail(se.alipaySign(
//								teslaXOrder.getUorderInfo().getBigOrderCode(),
//								teslaXOrder.getUorderInfo().getSellerCode(),
//								String.valueOf(teslaXOrder.getUorderInfo()
//										.getDueMoney())).get("sign"));
//					} else if ("449746280005".equals(inputParam.getPay_type())) {
//						// 惠家友微信支付
//						if (AppConst.MANAGE_CODE_HOMEHAS
//								.equals(getManageCode())) {
//							ApiWechatTeslaProcessService wechatProcessService = new ApiWechatTeslaProcessService();
//							// 接收财付通通知的URL
//							String notify_url = bConfig("ordercenter.NOTIFY_URL_HUIJIAYOU");
//							String appid = bConfig("ordercenter.APP_ID_HUJIAYOU");
//							String mch_id = bConfig("ordercenter.PARTNER_HUJIAYOU");
//							weChatMap = wechatProcessService
//									.wechatMovePaymentVersionNew(teslaXOrder
//											.getUorderInfo().getBigOrderCode(),
//											inputParam.getPay_ip(), result,
//											notify_url, appid, mch_id,
//											teslaXOrder.getUorderInfo()
//													.getDueMoney());
//
//						} else if (AppConst.MANAGE_CODE_CDOG
//								.equals(getManageCode())) {
//							// 沙皮狗微信支付
//							ApiWechatTeslaProcessService wechatProcessService = new ApiWechatTeslaProcessService();
//							// 接收财付通通知的URL
//							String notify_url = bConfig("ordercenter.NOTIFY_URL_HUIJIAYOU");
//							String appid = bConfig("ordercenter.APP_ID_SHAPIGOU");
//							String mch_id = bConfig("ordercenter.PARTNER_SHAPIGOU");
//							weChatMap = wechatProcessService
//									.wechatMovePaymentVersionNew(teslaXOrder
//											.getUorderInfo().getBigOrderCode(),
//											inputParam.getPay_ip(), result,
//											notify_url, appid, mch_id,
//											teslaXOrder.getUorderInfo()
//													.getDueMoney());
//						}
//						if (weChatMap != null && !weChatMap.isEmpty()) {
//							result.getChatpayment().setAppid(
//									weChatMap.get("appid").toString());
//							result.getChatpayment().setMch_id(
//									weChatMap.get("mch_id").toString());
//							result.getChatpayment().setNonce_str(
//									weChatMap.get("nonce_str").toString());
//							result.getChatpayment().setPrepay_id(
//									weChatMap.get("prepay_id").toString());
//							result.getChatpayment().setResult_code(
//									weChatMap.get("result_code").toString());
//							result.getChatpayment().setReturn_code(
//									weChatMap.get("return_code").toString());
//							result.getChatpayment().setSign(
//									weChatMap.get("sign").toString());
//							result.getChatpayment().setTrade_type(
//									weChatMap.get("trade_type").toString());
//							result.getChatpayment().setTimestamp(
//									weChatMap.get("timestamp").toString());
//						}
//					}
//				}
//			}
			
		}else {
			/*取消暂存款、储息金占用*/
			//employAmt(teslaXOrder, CrudFlagEnum.D.name());
			
			result.setResultCode(reTeslaXResult.getResultCode());
			result.setResultMessage(reTeslaXResult.getResultMessage());
		}
		/*暂存款、储息金使用*/
		//employAmt(teslaXOrder, CrudFlagEnum.U.name());
		
		return result;
	}
	
	private void doOthershing(TeslaXOrder teslaXOrder){//送券
		if("449716200002".equals(teslaXOrder.getUorderInfo().getPayType())){
			JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
					CouponConst.al_order_coupon,new MDataMap("mobile", getOauthInfo().getLoginName(), "manage_code", getManageCode()));
		}else {//在线支付下单送优惠券
			JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
					CouponConst.order_coupon,new MDataMap("mobile", getOauthInfo().getLoginName(), "manage_code", getManageCode()));
		}
	}

	public boolean employAmt(TeslaXOrder teslaXOrder,String crudFlag){
		
		boolean flag = false;
		
		if((teslaXOrder.getUse().getZck_money() > 0 || teslaXOrder.getUse().getCzj_money() > 0) && "1".equals(teslaXOrder.getUse().getAmt_status())){
			
			RsyncEmployAccountInfo employAccountInfo = new RsyncEmployAccountInfo();
			
			employAccountInfo.initAmtRequest(teslaXOrder);
			
			employAccountInfo.upRsyncRequest().setCrud_flag(crudFlag);
			
			flag = employAccountInfo.doRsync();
			
			
		}
		
		return flag;
		
	}

	/**
	 * 创建网易考拉订单
	 * @param teslaXOrder
	 * @return
	 */
	private void createKaolaOrder(TeslaXOrder teslaXOrder) {
		//调用网易考拉下单接口
		if(teslaXOrder.getIsKaolaOrder() == 1) {
			for(TeslaModelOrderInfo so : teslaXOrder.getSorderInfo()) {
				if(so.getIsKaolaOrder() == 1) {
					String orderCode = so.getOrderCode();
					MDataMap jobMap = new MDataMap();
					jobMap.put("uid", WebHelper.upUuid());
					jobMap.put("exec_code", WebHelper.upCode("ET"));
					jobMap.put("exec_type", "449746990014");
					jobMap.put("exec_info", orderCode);
					jobMap.put("create_time", DateUtil.getSysDateTimeString());
					jobMap.put("begin_time", "");
					jobMap.put("end_time", "");
					jobMap.put("exec_time", DateUtil.getSysDateTimeString());
					jobMap.put("flag_success","0");
					jobMap.put("remark", "WebCreateOrder -> createKaolaOrder");
					jobMap.put("exec_number", "0");
					DbUp.upTable("za_exectimer").dataInsert(jobMap);
				}
			}
		}
	}
}
