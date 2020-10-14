package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiOrderIfSuccessInput;
import com.cmall.familyhas.api.model.ProductInfoForOrderSuccess;
import com.cmall.familyhas.api.result.AdvertiseColumnmentInfo;
import com.cmall.familyhas.api.result.ApiOrderIfSuccessResult;
import com.cmall.familyhas.api.result.OrderInfoIfSuccess;
import com.cmall.familyhas.api.result.ld.ShowLinkForLDMsgPaySuccess;
import com.cmall.familyhas.service.PaymentTypeService;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.productcenter.model.ReminderContent;
import com.cmall.productcenter.service.MyService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusProductModelQuery;
import com.srnpr.xmassystem.support.PlusSupportMember;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 返回订单支付是否成功、货到付款是否下单成功接口
 * @author wz
 *
 */
public class ApiOrderIfSuccess extends RootApiForVersion<ApiOrderIfSuccessResult,ApiOrderIfSuccessInput>{
	
	public static final String SMALLSELLERCODEKJT = "SF03KJT";   //快境通small_seller_code编号
	public static final String SELLERCODEHJY =  "SI2003";   //惠家有seller_code编号
	
	public ApiOrderIfSuccessResult Process(ApiOrderIfSuccessInput inputParam, MDataMap mRequestMap) {
		String channel_id ="";
		String channelId = getChannelId();
		if("449747430001".equals(channelId) || "449747430003".equals(channelId)) {
			channel_id = "449748610001";
		}else if("449747430023".equals(channelId)) {
			channel_id = "449748610002";
		}else {
			channel_id = "449748610004";
		}
		if(!StringUtils.isEmpty(inputParam.getLdMsgPayChannel())) {//不为空时。
			channel_id = inputParam.getLdMsgPayChannel();
		}
		String os = getApiClient().get("os");
		String version = getApiClient().get("app_vision");
		ApiOrderIfSuccessResult result = new ApiOrderIfSuccessResult();
		List<OrderInfoIfSuccess> orderInfoIfSuccesslist = new ArrayList<OrderInfoIfSuccess>();
		List<Map<String, Object>> flashOrder = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> joinOrder = new ArrayList<Map<String, Object>>();
		//5.5.4版本添加广告信息
		String adverEntrType = inputParam.getAdverEntrType();
		if(StringUtils.isNotBlank(adverEntrType)) {
			String sysDateTimeString = DateUtil.getSysDateTimeString();
			List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_advert").dataSqlList("select * from fh_advert where start_time<'"+sysDateTimeString+"' and end_time>'"+sysDateTimeString+"' and adver_entry_type=:adver_entry_type and is_release='449746250001' and channel_id in ('"+channel_id+"','449748610004') order by start_time", new MDataMap("adver_entry_type",adverEntrType));
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.0") < 0) {
					if(1 == Integer.parseInt(map.get("programa_num").toString())){
						result.getAdverInfo().setAdverEntrType(map.get("adver_entry_type").toString());
						List<Map<String,Object>> dataSqlListColumn = DbUp.upTable("fh_advert_column").dataSqlList("select * from fh_advert_column where advertise_code =:advertise_code", new MDataMap("advertise_code",map.get("advertise_code").toString()));
						for(Map<String,Object> mapCo : dataSqlListColumn) {
							result.getAdverInfo().setImgHeight(Integer.parseInt(mapCo.get("img_height").toString()));
							result.getAdverInfo().setImgWidth(Integer.parseInt(mapCo.get("img_width").toString()));
							result.getAdverInfo().setImgUrl(mapCo.get("img_url").toString());
							String is_share = mapCo.get("is_share").toString();
							if("449746250001".equals(is_share)) {
								is_share = "Y";
							}else if("449746250002".equals(is_share)) {
								is_share = "N";
							}
							result.getAdverInfo().setIsShare(is_share);
							result.getAdverInfo().setShareContent(mapCo.get("share_content").toString());
							result.getAdverInfo().setShareTitle(mapCo.get("share_title").toString());
							result.getAdverInfo().setUrlLink(mapCo.get("link_url").toString());
							result.getAdverInfo().setShareImgUrl(mapCo.get("share_img_url").toString());
						}
					}
				}else {
					result.getAdvert().setAdverEntrType(map.get("adver_entry_type").toString());
					result.getAdvert().setPrograma_num(map.get("programa_num").toString());
					List<Map<String,Object>> dataSqlListColumn = DbUp.upTable("fh_advert_column").dataSqlList("select * from fh_advert_column where advertise_code =:advertise_code", new MDataMap("advertise_code",map.get("advertise_code").toString()));
					for(Map<String,Object> mapCo : dataSqlListColumn) {
						AdvertiseColumnmentInfo co = new AdvertiseColumnmentInfo();
						co.setImgHeight(Integer.parseInt(mapCo.get("img_height").toString()));
						co.setImgWidth(Integer.parseInt(mapCo.get("img_width").toString()));
						co.setImgUrl(mapCo.get("img_url").toString());
						co.setSort_num(Integer.parseInt(mapCo.get("sort_num").toString()));
						co.setJumpType(MapUtils.getString(mapCo,"link_type",""));
						String linkValue = mapCo.get("link_url").toString();
						if("4497471600640001".equals(MapUtils.getString(mapCo,"link_type",""))) {//小程序跳轉
							String links [] = linkValue.split("\\|");
							if(links.length>1) {
								co.setAppId(links[1]);
							}
							if(links.length>0) {
								co.setPath(links[0]);
							}
						}
						co.setUrlLink(mapCo.get("link_url").toString());
						co.setIsShare(mapCo.get("is_share").toString());
						co.setShareContent(mapCo.get("share_content").toString());
						co.setShareTitle(mapCo.get("share_title").toString());
						co.setShareImgUrl(mapCo.get("share_img_url").toString());
						result.getAdvert().getAdvertiseColumnmentInfos().add(co);
					}
				}
			}
		}
		boolean flag = false;
		String cxTime = "";
		String defaultPayType = "";

		MDataMap md = new MDataMap();
		md.put("big_order_code", inputParam.getOrderCode());
		Map<String,Object> mapUpper = DbUp.upTable("oc_orderinfo_upper").dataSqlOne("select * from oc_orderinfo_upper where big_order_code=:big_order_code", md);
		
		if(mapUpper!=null && mapUpper.size()>0) {
			LoadProductInfo loadProductInfo = new LoadProductInfo();
			List<String> paymentTypeAllList = new ArrayList<String>();
			paymentTypeAllList.add("449716200001");   //在线支付   默认支付宝
			
			if("IOS".equalsIgnoreCase(inputParam.getDeviceType())){
				paymentTypeAllList.addAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.IOS, inputParam.getOrderCode()));
			} else if("ANDROID".equalsIgnoreCase(inputParam.getDeviceType())){
				paymentTypeAllList.addAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.ANDROID, inputParam.getOrderCode()));
			} else if("WAP".equalsIgnoreCase(inputParam.getDeviceType()) || "WX".equalsIgnoreCase(inputParam.getDeviceType())){
				paymentTypeAllList.addAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.WAP, inputParam.getOrderCode()));
			} else if("WEB".equalsIgnoreCase(inputParam.getDeviceType()) || "PC".equalsIgnoreCase(inputParam.getDeviceType())){
				paymentTypeAllList.addAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.WEB, inputParam.getOrderCode()));
			} else {
				paymentTypeAllList.addAll(new PaymentTypeService().getSupportPayTypeList(PaymentTypeService.Channel.ANDROID, inputParam.getOrderCode()));
			}

			result.setPaymentTypeAll(paymentTypeAllList);
			
			result.setCreateDate(String.valueOf(mapUpper.get("create_time")));
			result.setDueMoney(String.valueOf(mapUpper.get("due_money")));
			
			defaultPayType = new PlusSupportPay().upPayFrom(String.valueOf(mapUpper.get("big_order_code")));
			if(defaultPayType!=null && !"".equals(defaultPayType)){
				result.setDefault_Pay_type(defaultPayType);  
			}
			
			// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
			if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
				result.setDefault_Pay_type("449746280003");
			}
			
//				result.setDefault_Pay_type(String.valueOf(mapUpper.get("pay_type")));
			result.setSafetyHint("安全提醒：惠家有不会以任何理由要求您提供银行卡信息或支付额外费用，请谨慎防钓鱼链接或诈骗电话。");
			
			// 查询退换货信息的Service
			MyService myService = new MyService();
			// 使用Set类型防止重复
			Set<String> smallSellerCodeList = new HashSet<String>();
			
			List<Map<String,Object>> listOrderInfo = DbUp.upTable("oc_orderinfo").dataSqlList("select * from oc_orderinfo where big_order_code=:big_order_code", md);
			BigDecimal wgsMoney = new BigDecimal(0);
			for (Map<String, Object> mapOrderInfo : listOrderInfo) {
				OrderInfoIfSuccess orderInfoIfSuccess = new OrderInfoIfSuccess();
				
//					if(SMALLSELLERCODEKJT.equals(mapOrderInfo.get("small_seller_code")) && SELLERCODEHJY.equals(mapOrderInfo.get("seller_code"))){
				if(listOrderInfo.size()>1){
					result.setSplitOrderHint("提示: 商品从不同仓库发货, 会分成多个订单配送!");
				}else{
					result.setSplitOrderHint("");
				}
				
				//查询该商品是否参与拼团，如果拼团返回拼团编码
				MDataMap collageItemMap = DbUp.upTable("sc_event_collage_item").one("collage_ord_code", String.valueOf(mapOrderInfo.get("order_code")));
				if(collageItemMap != null) {
					result.setCollageCode(String.valueOf(collageItemMap.get("collage_code")));
					MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",MapUtils.getString(collageItemMap, "collage_code"));
					if(collageInfo != null && !collageInfo.isEmpty()) {
						MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code",collageInfo.get("event_code"));
						if (eventInfo!=null&&!eventInfo.isEmpty()) {
							result.setCollageType(eventInfo.get("collage_type"));
						}
					}
				}
				
//					int orderPayCount = DbUp.upTable("oc_order_pay").dataCount("order_code=:order_code and (pay_type=:alipay or pay_type=:wechat)", new MDataMap("alipay","449746280003","wechat","449746280005","order_code",String.valueOf(mapOrderInfo.get("order_code"))));
//					if(orderPayCount!=0){
//						
//					}
				//判断此订单是否支付 
				if("4497153900010002".equals(mapOrderInfo.get("order_status"))){
					flag = true;
					//如果不是货到付款取大订单表payed_money
					if(!"449716200002".equals(mapOrderInfo.get("pay_type"))){
						result.setDueMoney(String.valueOf(mapUpper.get("payed_money")));
					}
				}
				
				//判断此订单是否为微公社余额支付
				Map<String, Object>  orderPayMap = DbUp.upTable("oc_order_pay").dataSqlOne("select * from oc_order_pay where pay_type=:pay_type and order_code=:order_code ", new MDataMap("pay_type","449746280009","order_code",String.valueOf(mapOrderInfo.get("order_code"))));
				if(orderPayMap!=null && !"".equals(orderPayMap) && orderPayMap.size()>0){
					//result.setDefault_Pay_type("449746280009");
					wgsMoney = wgsMoney.add(new BigDecimal(orderPayMap.get("payed_money")+"")).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				
				// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
				if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
					result.setDefault_Pay_type("449746280003");
				}
				
				orderInfoIfSuccess.setOrderCode(String.valueOf(mapOrderInfo.get("order_code")));
				orderInfoIfSuccesslist.add(orderInfoIfSuccess);
				
				smallSellerCodeList.add(mapOrderInfo.get("small_seller_code")+"");
				
				//给商品赋值
				String orderCode = MapUtils.getString(mapOrderInfo,"order_code","");
				String sql = "SELECT product_code,SUM(sku_num) sku_num FROM ordercenter.oc_orderdetail WHERE order_code = :order_code AND gift_flag = '1' GROUP BY product_code";
				List<Map<String,Object>> orderDetails = DbUp.upTable("oc_orderdetail").dataSqlList(sql, new MDataMap("order_code",orderCode));
				for(Map<String,Object> orderDetail : orderDetails) {
					String productCode = MapUtils.getString(orderDetail,"product_code","");
					Integer productNum = MapUtils.getInteger(orderDetail,"sku_num",0);
					ProductInfoForOrderSuccess p = new ProductInfoForOrderSuccess();
					p.setProductCode(productCode);
					p.setProductNum(productNum);
					String productName = "";
					if(StringUtils.isNotEmpty(productCode)) {
						PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
						productName = productInfo.getProductName();
					}
					p.setProductName(productName);
					result.getProductList().add(p);
				}
			}
			
			// 0元单的情况下支付类型分： 余额支付(449746280009)、优惠支付(449746280017)
			if(new BigDecimal("0.00").compareTo(new BigDecimal(mapUpper.get("due_money")+"")) == 0){
				if(wgsMoney.compareTo(new BigDecimal("0.00")) > 0){
					result.setDefault_Pay_type("449746280009");
				}else{
					result.setDefault_Pay_type("449746280017");
				}
			}
			
			if(flag){
				if("449716200002".equals(String.valueOf(mapUpper.get("pay_type")))){
					//货到付款成功
					result.setOrderPayStatue("01");
				}else{
					//在线支付成功;
					result.setOrderPayStatue("00");	  
					promptCoupon(result, inputParam.getOrderCode());
					
				}					
				
				// 设置支付成功页的退换货信息
				List<ReminderContent> contentList = myService.getReminderList(new ArrayList<String>(smallSellerCodeList), "4497471600270002");
				for(ReminderContent rc : contentList){
					result.getTips().add(rc.getContent());
				}
			}else{
				result.setOrderPayStatue("11");   //失败;
				/**
				 * 如果支付失败   ，  默认支付方式去缓存里的值
				 */
				defaultPayType = new PlusSupportPay().upPayFrom(String.valueOf(mapUpper.get("big_order_code")));
				if(defaultPayType!=null && !"".equals(defaultPayType)){
					result.setDefault_Pay_type(defaultPayType);  
				}
				
				// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
				if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
					result.setDefault_Pay_type("449746280003");
				}
				
				//闪够订单
				flashOrder = DbUp.upTable("oc_orderinfo").dataSqlList("select p.create_time,p.order_code from oc_orderinfo p JOIN oc_order_activity q ON p.order_code = q.order_code where (p.order_code =:order_code or p.big_order_code =:order_code) and q.activity_type = '449715400004' order by p.create_time DESC, p.zid DESC", new MDataMap("order_code",String.valueOf(mapUpper.get("big_order_code"))));
				//普通订单 
				joinOrder = DbUp.upTable("oc_orderinfo").dataSqlList("select p.create_time,p.order_code from oc_orderinfo p JOIN oc_order_activity q ON p.order_code = q.order_code where p.order_code =:order_code or p.big_order_code =:order_code", new MDataMap("order_code",String.valueOf(mapUpper.get("big_order_code"))));
				//
				List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList("select * from oc_orderinfo where big_order_code=:big_order_code", new MDataMap("big_order_code",inputParam.getOrderCode()));
				
				for(Map<String, Object> orderInfo : orderInfoList){
					//促销系统商品订单自动取消订单提示  (扫码购、特价、秒杀)
					List<MDataMap> ics = DbUp.upTable("oc_order_activity").queryAll("activity_code,out_active_code", "", " activity_code like 'CX%' and order_code=:order_code", new MDataMap("order_code",String.valueOf(orderInfo.get("order_code"))));
					List<String> icCodes = new ArrayList<String>();
					for (int jj = 0; jj < ics.size(); jj++) {
						icCodes.add(ics.get(jj).get("activity_code"));
					}
					cxTime = new PlusSupportProduct().getOrderRemind(icCodes);
				}
				
				if(flashOrder!=null && !"".equals(flashOrder) && flashOrder.size()>0){
					result.setDateMessage("提示: 闪够订单将在15分钟后取消,请尽快支付!");
				}else if(joinOrder!=null && !"".equals(joinOrder) && joinOrder.size()>0){
					result.setDateMessage("提示: 订单将在24小时后取消,请尽快支付!");
				}else{
					result.setDateMessage("提示: 订单将在24小时后取消,请尽快支付!");
				}
				if(cxTime!=null && !"".equals(cxTime)){   ////促销系统商品订单自动取消订单提示  (扫码购、特价、秒杀)
					result.setDateMessage(bInfo(916421258, cxTime));
				}
//					result.setDateMessage("提示: 订单将在"+DateNewUtils.addDay(String.valueOf(mapUpper.get("create_time")), 1)+"时间后取消,请尽快支付!");
				if(orderInfoIfSuccesslist!=null && !"".equals(orderInfoIfSuccesslist) && orderInfoIfSuccesslist.size()>0){
					orderInfoIfSuccesslist.clear();
				}
				OrderInfoIfSuccess orderInfoIfSuccess = new OrderInfoIfSuccess();
				orderInfoIfSuccess.setOrderCode(String.valueOf(mapUpper.get("big_order_code")));
				orderInfoIfSuccesslist.add(orderInfoIfSuccess);
			}
			
			result.setSplitOrderList(orderInfoIfSuccesslist);
		}
		
		// 判断是否购买的plus会员卡商品
		String plusFlagSql = "SELECT 1 FROM ordercenter.oc_orderinfo o,ordercenter.oc_orderdetail d "
				+ " WHERE o.order_code = d.order_code AND o.big_order_code = :big_order_code "
				+ " AND d.product_code = :product_code";
		if(!DbUp.upTable("oc_orderinfo").dataSqlList(plusFlagSql, new MDataMap("big_order_code",inputParam.getOrderCode(),"product_code",bConfig("xmassystem.plus_product_code"))).isEmpty()) {
			result.setPlusFlag(1);
		}
		
		// 判断是否参与特殊活动 暂时仅为投票换购
		String sql = "SELECT ei.event_code,ep.item_code,ep.product_code,ep.sku_code "
				+ "FROM systemcenter.sc_event_item_product ep "
				+ "LEFT JOIN systemcenter.sc_event_info ei ON ep.event_code=ei.event_code "
				+ "WHERE event_type_code='4497472600010029' AND event_status='4497472700020002' AND begin_time <= NOW() AND end_time > NOW()";
		Map<String, Object> event = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sql, new MDataMap());
		if(null != event) {
			String eventFlagSql = "SELECT 1 FROM ordercenter.oc_order_activity oa LEFT JOIN ordercenter.oc_orderinfo oi ON oa.order_code=oi.order_code WHERE oi.big_order_code=:big_order_code and oa.activity_code=:activity_code ";
			if(!DbUp.upTable("oc_order_activity").dataSqlList(eventFlagSql, new MDataMap("big_order_code",inputParam.getOrderCode(),"activity_code",event.get("event_code")+"")).isEmpty()) {
				result.setSpecialEventFlag(1);
			}
		}
		//判断是否是特殊橙意卡专享购买活动
		String sqlCyk = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code='4497472600010032' AND event_status='4497472700020002' AND begin_time <= NOW() AND end_time > NOW()";
		Map<String, Object> cyk = DbUp.upTable("sc_event_info").dataSqlOne(sqlCyk, new MDataMap());
		if(null != cyk) {
			String eventFlagSql = "SELECT 1 FROM ordercenter.oc_order_activity oa LEFT JOIN ordercenter.oc_orderinfo oi ON oa.order_code=oi.order_code WHERE oi.big_order_code=:big_order_code and oa.activity_code=:activity_code ";
			if(!DbUp.upTable("oc_order_activity").dataSqlList(eventFlagSql, new MDataMap("big_order_code",inputParam.getOrderCode(),"activity_code",cyk.get("event_code")+"")).isEmpty()) {
				result.setSpecialEventFlag(1);
			}
		}
		
		// 小程序悬浮图标
		if("weapphjy".equals(os)) {
			result.getTopGuide().setShow(Boolean.parseBoolean((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","469923330005", "define_dids", "4699233300050001"))));
			result.getTopGuide().setGuideType(NumberUtils.toInt(((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","469923330005", "define_dids", "4699233300050002")))));
			result.getTopGuide().setGuideValue(StringUtils.trimToEmpty((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","469923330005", "define_dids", "4699233300050003"))));
			result.getTopGuide().setGuideImage(StringUtils.trimToEmpty((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","469923330005", "define_dids",  "4699233300050004"))));
		}
		
		//LD短信支付成功之后，页面配置广告按钮
		this.setShowLink(result);
		
		return result;
	}

	/**
	 * 
	 * @param result
	 * 2020-8-19
	 * Angel Joy
	 * void
	 */
	private void setShowLink(ApiOrderIfSuccessResult result) {
		ShowLinkForLDMsgPaySuccess showLinkOne = new ShowLinkForLDMsgPaySuccess();
		showLinkOne.setChooseFlag(0);
		showLinkOne.setTitle("节目表");
		showLinkOne.setTarget("com_cmall_familyhas_api_ApiForGetTVData");
		Map<String,Object> operateProduct = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne("SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1", null);
		if(operateProduct != null && !operateProduct.isEmpty()) {
			ShowLinkForLDMsgPaySuccess showLinkTwo = new ShowLinkForLDMsgPaySuccess();
			if("449748600001".equals(MapUtils.getString(operateProduct, "if_recommend",""))) {//推荐
				showLinkTwo.setChooseFlag(1);
			}else {
				showLinkTwo.setChooseFlag(0);
				showLinkOne.setChooseFlag(1);
			}
			if("4497471600660001".equals(MapUtils.getString(operateProduct, "recommend_type",""))) {//猜你喜欢
				//需要返回商品编号。
				List<OrderInfoIfSuccess> orderInfos = result.getSplitOrderList();
				String orderCode = orderInfos.get(0).getOrderCode();
				String sql = "SELECT * FROM ordercenter.oc_orderdetail WHERE order_code = :order_code AND gift_flag = '1' limit 1";
				Map<String,Object> orderDetails = DbUp.upTable("oc_orderdetail").dataSqlOne(sql, new MDataMap("order_code",orderCode));
				if(orderDetails != null && !orderDetails.isEmpty()) {
					showLinkTwo.setProductCode(MapUtils.getString(orderDetails, "product_code",""));
				}
				showLinkTwo.setTarget("com_cmall_familyhas_api_ApiDgGetRecommend");
			}else {
				showLinkTwo.setTarget("com_cmall_familyhas_api_ApiRecommendForLdPaySuccess");
			}
			showLinkTwo.setTitle("为您推荐");
			result.getShowLink().add(showLinkTwo);
		}else {
			showLinkOne.setChooseFlag(1);
		}
		result.getShowLink().add(showLinkOne);
	}

/*	private void updateDistributionInfos(Map<String, Object> mapOrderInfo) {
		// TODO Auto-generated method stub buyer_code
		String memberCode = mapOrderInfo.get("buyer_code").toString();
		String order_code = mapOrderInfo.get("order_code").toString();
		String currentTime = DateUtil.getSysDateTimeString();
		MDataMap one = DbUp.upTable("oc_orderdetail").one("order_code",order_code);
		if(one!=null&&StringUtils.isNotBlank(one.get("distribution_member_id"))) {
			String sku_num = one.get("sku_num").toString();
			String product_code = one.get("product_code").toString();
			BigDecimal sku_price =BigDecimal.valueOf(Double.parseDouble(one.get("sku_price").toString()));
			List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_distribution_info").dataSqlList("select * from lc_distribution_info where distribution_junior_member_id='"+memberCode+"' and distribution_junior_member_id!=distribution_member_id and distribution_product_code='"+product_code+"' order by zid desc", null);
			if(dataSqlList!=null&&dataSqlList.size()>0) {
			//更新分销信息
			Map<String, Object> map = dataSqlList.get(0);
			String create_time = map.get("create_time").toString();
			String distribution_member_id = map.get("distribution_member_id").toString();
			String endTime = DateUtil.addDateHour(create_time, 24);
			if(DateUtil.compareDateTime(currentTime, endTime)) {
				Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
			    if(dataSqlOne!=null) {
					Integer share_order_num = Integer.parseInt(dataSqlOne.get("share_order_num").toString());
			    	BigDecimal share_sales_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("share_sales_value").toString()));
			    	share_order_num=share_order_num+1;
			    	BigDecimal allSkuPrice = sku_price.multiply(BigDecimal.valueOf(Double.parseDouble(sku_num)));
			    	share_sales_value=share_sales_value.add(allSkuPrice);
			    	share_sales_value.setScale(2,BigDecimal.ROUND_HALF_DOWN);
			    	DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("share_order_num",share_order_num.toString(),"share_sales_value",share_sales_value.toString(),"distribution_member_id",distribution_member_id), "share_order_num,share_sales_value", "distribution_member_id");  
			    }
			} 
		 }
		}
	}*/

	private void promptCoupon(ApiOrderIfSuccessResult result, String bigOrderCode) {
		try {
			String sql = "select ac.begin_time, ac.end_time, ac.assign_trigger,ac.activity_code "
					+ "from oc_activity ac, oc_coupon_relative re "
					+ "where ac.activity_code=re.activity_code and ac.activity_type='449715400007' and ac.flag=1 and re.relative_type=3 and re.manage_code='SI2003'";
			Map<String,Object> activityMap = DbUp.upTable("oc_activity").dataSqlOne(sql, new MDataMap());

			if (activityMap != null) {
				String nowTime = DateUtil.getNowTime();
				String activityCode = StringUtils.isEmpty((String)activityMap.get("activity_code")) ? "" : activityMap.get("activity_code").toString();
				String startTime = StringUtils.isEmpty((String)activityMap.get("begin_time")) ? "" : activityMap.get("begin_time").toString();
				String endTime = StringUtils.isEmpty((String)activityMap.get("end_time")) ? "" : activityMap.get("end_time").toString();
				if (DateUtil.compareTime(startTime, nowTime, "yyyy-MM-dd HH:mm:ss") > 0) {
					// 活动未开始
					return ;
				}
				if (DateUtil.compareTime(nowTime, endTime, "yyyy-MM-dd HH:mm:ss") > 0) {
					// 活动已结束
					return ;
				}
				
				String assignTrigger = StringUtils.isEmpty((String)activityMap.get("assign_trigger")) ? "" : activityMap.get("assign_trigger").toString();
				if(StringUtils.isEmpty(assignTrigger)) {
					return ;
				}
				
				if("4497471600340001".equals(assignTrigger)) {
					//下单满X元
					sql = "select payed_money from oc_orderinfo_upper_payment where big_order_code='" + bigOrderCode + "'";
					Map<String, Object> payMap = DbUp.upTable("oc_orderinfo_upper_payment").dataSqlOne(sql, new MDataMap());
					if(null != payMap && payMap.size() > 0) {
						double orderMoney =payMap.get("payed_money") == null ? 0.0 : Double.parseDouble(payMap.get("payed_money").toString());
						double lineMoney = StringUtils.isEmpty((String) activityMap.get("assign_line")) ? 0.0 : Double.parseDouble(activityMap.get("assign_line").toString());
						if(orderMoney >= lineMoney) {
							//查询送券总金额
							sql = "select sum(money) as money from oc_coupon_type where activity_code='" + activityCode + "'";
							Map<String, Object> couponTypeMap = DbUp.upTable("oc_orderinfo_upper_payment").dataSqlOne(sql, new MDataMap());
							String money = null == couponTypeMap.get("money") ? "0.0" : couponTypeMap.get("money").toString();
							
							ApiOrderIfSuccessResult.CouponInfo cinfo =  new ApiOrderIfSuccessResult.CouponInfo();
							cinfo.setTrigger(bInfo(916423100));
							cinfo.setComment(bInfo(916423101) + "\r\n" + bInfo(916423102) + "\r\n" + bInfo(916423103) + "\r\n" + bInfo(916423104));
							cinfo.setAmount(Integer.parseInt(money));
							result.setCoupon(cinfo);
						}
					}
				} else if("4497471600340002".equals(assignTrigger)) {
					//首次下单
					sql = "select buyer_code from oc_orderinfo_upper where big_order_code='" + bigOrderCode + "'";
					Map<String, Object> payMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(sql, new MDataMap());
					if(null != payMap.get("buyer_code") && StringUtils.isNotEmpty(payMap.get("buyer_code").toString())) {
						String memberCode = payMap.get("buyer_code").toString();
						PlusSupportMember psm = new PlusSupportMember();
						if(psm.upFlagFirstOrderAfterBooking(memberCode)) {
							//查询送券总金额
							sql = "select sum(money) as money from oc_coupon_type where activity_code='" + activityCode + "'";
							Map<String, Object> couponTypeMap = DbUp.upTable("oc_orderinfo_upper_payment").dataSqlOne(sql, new MDataMap());
							String money = StringUtils.isEmpty((String)couponTypeMap.get("money")) ? "0.0" : couponTypeMap.get("money").toString();
							
							ApiOrderIfSuccessResult.CouponInfo cinfo = new ApiOrderIfSuccessResult.CouponInfo();
							cinfo.setTrigger(bInfo(916423100));
							cinfo.setComment(bInfo(916423101) + "\r\n" + bInfo(916423102) + "\r\n" + bInfo(916423103) + "\r\n" + bInfo(916423104));
							cinfo.setAmount(Integer.parseInt(money));
							result.setCoupon(cinfo);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ApiOrderIfSuccess s = new ApiOrderIfSuccess();
		ApiOrderIfSuccessResult r = new ApiOrderIfSuccessResult();
		s.promptCoupon(r, "OS38321104");
	}
}
