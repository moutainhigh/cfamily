package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.api.input.APiOrderConfirmInput;
import com.cmall.familyhas.api.result.APiOrderConfirmResult;
import com.cmall.familyhas.model.Activity;
import com.cmall.familyhas.model.GoodsInfoForConfirm;
import com.cmall.familyhas.model.OrderSort;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.service.MemberLevelService;
import com.cmall.familyhas.service.PaymentTypeService;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.DlvPayLoader;
import com.cmall.groupcenter.homehas.RsyncCheckDlvPay;
import com.cmall.groupcenter.homehas.model.RsyncRequestCheckDlvPay;
import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.cmall.ordercenter.model.CouponInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.OrderItemList;
import com.cmall.ordercenter.model.UserInfo;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.productcenter.model.ReminderContent;
import com.cmall.productcenter.service.MyService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.service.InvoiceService;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelJJG;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmasorder.service.TeslaCouponService;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadProductAuthorityLogoForSeller;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelProductAuthorityLogoForSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.service.PlusServiceEventPlus;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠家有订单确认接口
 * 
 * @author shiyz
 *
 */
public class TeslaOrderConfirm extends RootApiForToken<APiOrderConfirmResult, APiOrderConfirmInput> {

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	public APiOrderConfirmResult Process(APiOrderConfirmInput inputParam,
			MDataMap mRequestMap) {
		//5.4.0版本校验优惠券是否与支付方式兼容
		this.checkCoupon(inputParam);
		String appVision = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		APiOrderConfirmResult result = new APiOrderConfirmResult();
		//558获取用户购物车中选择的换购商品
		ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
		ShoppingCartCacheInfo info = serviceForCache.queryShopCart(getOauthInfo()==null?null:getOauthInfo().getUserCode(),new ArrayList<ShoppingCartGoodsInfoForAdd>());
	    if("1".equals(inputParam.getIfFromShopCar())&&info.getJJGList().size()>0) {
	    	for (TeslaModelJJG jjg : info.getJJGList()) {
	    		result = validateJJG(jjg,inputParam,result);
			}
	    	if(!StringUtils.equals("", result.getJjgTips())) {
	    		result.setJjgTips("因活动["+result.getJjgTips()+"]已结束，部分商品已经取消选择");
	    	}
	    }
	
	    TeslaXOrder teslaXOrder = new TeslaXOrder();
	    //订单基本信息
	    TeslaModelOrderInfoUpper orderInfo =  teslaXOrder.getUorderInfo();
	    orderInfo.setBuyerCode(getUserCode());
	    orderInfo.setSellerCode(getManageCode());
	    orderInfo.setOrderType(inputParam.getOrder_type());
	    orderInfo.setOrderSource(inputParam.getOrder_souce());
	    orderInfo.setPayType(inputParam.getPayType());
	    teslaXOrder.setUorderInfo(orderInfo);
	    teslaXOrder.setIsOriginal(inputParam.getIsOriginal());
	    teslaXOrder.setCollageFlag(inputParam.getCollageFlag());
	    teslaXOrder.setCollageCode(inputParam.getCollageCode());
	    teslaXOrder.setActivityCode(inputParam.getActivityCode());
	    teslaXOrder.setRedeemCode(inputParam.getRedeemCode());
	    teslaXOrder.setTreeCode(inputParam.getTreeCode());
	    teslaXOrder.setEventCode(inputParam.getEventCode());
	    teslaXOrder.setHuDongCode(inputParam.getHuDongCode());
	    
	    if(getApiClient() != null ){
	    	orderInfo.setAppVersion(getApiClient().get("app_vision"));
	    }
	    
	    teslaXOrder.setQrcode(StringUtils.trimToEmpty(inputParam.getQrcode()));
	    
	    teslaXOrder.setAutoSelectCoupon(inputParam.getAutoSelectCoupon());
	    
	    //订单详细信息
	    List<OrderItemList> orderItemList = new ArrayList<OrderItemList>();//考拉订单信息		
		String sSql = "select pc_productinfo.product_name,pc_productinfo.product_code,pc_skuinfo.sku_code,pc_productinfo.sell_productcode as kaola_productcode,pc_skuinfo.sell_productcode as kaola_skucode,pc_skuinfo.cost_price from pc_productinfo" + 
					  " left join pc_skuinfo on pc_productinfo.product_code = pc_skuinfo.product_code" +
					  " where pc_productinfo.product_code =:product_code and pc_skuinfo.sku_code =:sku_code and pc_productinfo.small_seller_code =:small_seller_code";
		String kaolaProduct = "";
		List<TeslaModelOrderDetail> orderDetails = new ArrayList<TeslaModelOrderDetail>();
	    //商品信息
	    Map<String, String> map = new HashMap<String, String>();
	    //560厂商收款判断
	    List<Map<String, String>> dlrList = new ArrayList<Map<String,String>>();
	    
		List<GoodsInfoForAdd> infoForAdds = inputParam.getGoods();
		//561专题页直接购买橙意卡商品
		if(inputParam.getIsCyk().equals("1")){
			infoForAdds.clear();
			GoodsInfoForAdd tempinfo = new GoodsInfoForAdd();
			tempinfo.setProduct_code(bConfig("xmassystem.plus_product_code"));
			tempinfo.setSku_code(bConfig("xmassystem.plus_sku_code"));
			tempinfo.setSku_num(1);
			infoForAdds.add(tempinfo);
		}
		
		boolean hasPlusProduct = false;
		for(GoodsInfoForAdd goodsInfo :infoForAdds) {
			PlusModelSkuInfo plusModelSkuInfo  = new PlusModelSkuInfo();
			if(StringUtils.equals("0", goodsInfo.getIfJJGFlag())) {
				//非换购品
				goodsInfo.setIsPurchase(inputParam.getIsPurchase());
				//add by zht
				//查询订单商品sku状态
				plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(goodsInfo.getSku_code(), 
						teslaXOrder.getUorderInfo().getBuyerCode(),teslaXOrder.getIsMemberCode(),teslaXOrder.getIsPurchase());
			}else {
				//换购品
				//查询订单商品sku状态
				plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCodeForJJG(goodsInfo.getSku_code(), 
						teslaXOrder.getUorderInfo().getBuyerCode(),teslaXOrder.getIsMemberCode());
			}
			
			//商品上架并且当前sku售卖状态为Y或售卖状态为空时,则saleStatus为1;sku售卖状态为N时,saleStatus为0. 
			//sale_yn为空时默认可售,是为了兼容上线,因为线上xs-Sku-xx无sale_yn字段,升级会产生两种
			//情况1:线上删除所有sku缓存,但后台LoadSkuInfo未更新,立即会有新缓存加载,还是没有sale_yn;
			//情况2:若先升级后台接口且sale_yn为空时默认不可售, 则所有用户的购物车内的商品都失效;
			String saleStatus = "0";
			String productCode = plusModelSkuInfo.getProductCode();
			if(StringUtils.isNotBlank(productCode)) {
				//查询商品状态
				PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(productCode);
				PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
				if(plusModelProductinfo != null) {
					saleStatus = "4497153900060002".equals(plusModelProductinfo.getProductStatus())  &&plusModelSkuInfo.getMaxBuy()>0
							&& (StringUtils.isBlank(plusModelSkuInfo.getSaleYn()) || plusModelSkuInfo.getSaleYn().equals("Y")) ? "1" : "0";
					Map<String, String> dlrMap = new HashMap<String, String>();
					dlrMap.put("productCode", productCode);
					dlrMap.put("dlrCharge", plusModelProductinfo.getDlrCharge());
					dlrMap.put("productName", plusModelProductinfo.getProductName());
					dlrList.add(dlrMap);
				}
			}
			
			if(saleStatus.equals("1")) {
				TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail(); 
				orderDetail.setProductCode(goodsInfo.getProduct_code());
				orderDetail.setSkuCode(goodsInfo.getSku_code());
				orderDetail.setSkuNum(goodsInfo.getSku_num());
				orderDetail.setIsSkuPriceToBuy(goodsInfo.getFlg());
				orderDetail.setIfJJGFlag(goodsInfo.getIfJJGFlag());
				orderDetail.setFxrcode(goodsInfo.getFxrcode());
				orderDetail.setShareCode(goodsInfo.getShareCode());
				orderDetail.setTgzShowCode(goodsInfo.getTgzShowCode());
				orderDetail.setTgzUserCode(goodsInfo.getTgzUserCode());
				String skuCode = goodsInfo.getSku_code();
				if(skuCode != null && "IC".equals(skuCode.substring(0, 2))) {
					skuCode = KaolaOrderService.upSkuCode(skuCode);
				}
				if(!"".equals(skuCode)) {
					Map<String, Object> smap = DbUp.upTable("pc_productinfo").dataSqlOne(sSql, new MDataMap("product_code", goodsInfo.getProduct_code(),"sku_code",skuCode,"small_seller_code",AppConst.MANAGE_CODE_WYKL));
					if(smap != null && smap.get("kaola_productcode") != null && smap.get("kaola_skucode") != null) {				
						OrderItemList item = new OrderItemList();
						item.setGoodsId(smap.get("kaola_productcode").toString());
						item.setSkuId(smap.get("kaola_skucode").toString());
						item.setBuyAmount(goodsInfo.getSku_num());
						item.setChannelSalePrice(new BigDecimal(smap.get("cost_price").toString()));
						orderItemList.add(item);
						orderDetail.setIsKaolaGood(1);
						kaolaProduct += "|" + map.get("product_name");
						
						// 保存考拉商品的编码供后续拆单时使用
						orderDetail.setSell_productcode(item.getGoodsId());
						orderDetail.setSell_skucode(item.getSkuId());
					} 
				}
				
				orderDetails.add(orderDetail);
				map.put(orderDetail.getSkuCode(), "");
			}else {
				NoStockOrFailureGoods noStockOrFailureGoods = new NoStockOrFailureGoods();
				noStockOrFailureGoods.setProduct_code(plusModelSkuInfo.getProductCode());
				noStockOrFailureGoods.setSku_pic(plusModelSkuInfo.getSkuPicUrl());
				noStockOrFailureGoods.setSku_code(plusModelSkuInfo.getSkuCode());
				noStockOrFailureGoods.setSku_name(plusModelSkuInfo.getSkuName());
				noStockOrFailureGoods.setSku_num(goodsInfo.getSku_num());
				result.getNoStockOrFailureGoods().add(noStockOrFailureGoods);
			}
			
			// 判断购买的是否橙意会员卡商品
			if(bConfig("xmassystem.plus_product_code").equals(goodsInfo.getProduct_code())) {
				hasPlusProduct = true;
			}
		}
		
		if(hasPlusProduct && StringUtils.isNotBlank(appVision) && AppVersionUtils.compareTo("5.5.80", appVision) > 0) {
			result.setResultCode(963906057);
			result.setResultMessage("您当前的APP版本不支持购买橙意会员卡商品，请升级到最新版本后重试！");
			return result;
		}
		//560校验厂商收款字段
		List<String> pList = new ArrayList<String>();//厂商收款商品编号集合
		String dlrType = "";//01同时存在02全厂商收款03没有厂商收款
		String tempV = "";
		String dlrName = "";
		for(Map<String, String> dlrMap:dlrList){
			if(StringUtils.isBlank(tempV)||tempV.equals(dlrMap.get("dlrCharge"))){
				tempV = dlrMap.get("dlrCharge");
			}else{
				dlrType = "01";
				tempV = dlrMap.get("dlrCharge");
			}
			if(tempV.equals("Y")){
				pList.add(tempV);
				if(StringUtils.isBlank(dlrName)){
					dlrName += dlrMap.get("productName");
				}else{
					dlrName += ","+dlrMap.get("productName");
				}
				
			}
		}
		if(StringUtils.isBlank(dlrType)&&tempV.equals("Y")){
			dlrType = "02";
			inputParam.setPayType("449716200002");
			orderInfo.setPayType("449716200002");
		}
		if(StringUtils.isBlank(dlrType)&&tempV.equals("N")){
			dlrType = "03";
		}
		if(dlrType.equals("01")){
			result.setResultCode(916424003);//为了返回购物车的错误码
			result.setResultMessage(dlrName+"等商品不支持在线支付，请分开结算！");
			result.setDlrProductList(pList);
			return result;
		}
		
		//add by zht.
		//有无效商品.固定返回916421255错误码 前端存在特殊展示！！！
		if(infoForAdds.size() > orderDetails.size()) {
			result.setResultCode(916421255);
			result.setResultMessage("存在无效商品，请重新选择！");
			return result;
		}
		
		teslaXOrder.setOrderDetails(orderDetails);
		
		//三级区域编码
		teslaXOrder.getAddress().setAreaCode(inputParam.getArea_code());
		//优惠券
		if (inputParam.getCoupon_codes() != null
				&& !inputParam.getCoupon_codes().isEmpty()) {
			teslaXOrder.getUse().setCoupon_codes(new ArrayList<String>(new HashSet<String>(inputParam.getCoupon_codes())));
		}
		
		GroupAccountService accountService = new GroupAccountService();
//			GroupAccountInfoResult groupResult = accountService.getAccountInfo(accountService.getAccountCodeByMemberCode(getUserCode()));
		GroupAccountInfoResult groupResult= null;
		// 微公社余额接口查询增加系统标识开关判断
//			if("1".equals(XmasKv.upFactory(EKvSchema.CgroupMoney).get("use"))){
		// modify by zht.
		if("1".equals(XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", "use"))) {
			//判断用户来源
			if(getUserCode().substring(8,9).equals("1")) {//来源于微公社（用户编号第九位为1的用户编号为微公社来源）
				groupResult = accountService.getAccountInfoByApi(getUserCode());
			} else {
				//来源于会员中心（用户编号第九位为7的用户编号为会员中心）
				groupResult = new GroupAccountInfoResult();
				groupResult.setFlagEnable("0");
			}
			
		}else{
			groupResult = new GroupAccountInfoResult();
			groupResult.setFlagEnable("0");
		}

		checkWgs(inputParam, groupResult, result);
		teslaXOrder.getUse().setWgs_money(BigDecimal.valueOf(inputParam.getWgsUseMoney()));
		// 渠道来源
		teslaXOrder.setChannelId(inputParam.getChannelId());
		teslaXOrder.getStatus().setExecStep(ETeslaExec.Confirm);
		// 用户编号
		teslaXOrder.setIsMemberCode(inputParam.getIsMemberCode());
		// 是否内购
		teslaXOrder.setIsPurchase(inputParam.getIsPurchase());

		// 惠豆添加
		teslaXOrder.getUse().setHjyBean(BigDecimal.valueOf(inputParam.getUsedBeanTotal()));
		// 积分添加
		teslaXOrder.getUse().setIntegral(inputParam.getUsedIntegralTotal());
		//储值金添加 20180521 -rhb 
		if(Integer.parseInt(inputParam.getIntegralFlag())==0) {//Since5.2.0版本储值金不支持积分商城
			teslaXOrder.getUse().setCzj_money(inputParam.getUsedCzjTotal());
		}else {
			teslaXOrder.getUse().setCzj_money(0);
		}
		//暂存款添加 20180521 -rhb 添加版本兼容5.2.0（不含）以上版本支出暂存款 20180705
		if(Integer.parseInt(inputParam.getIntegralFlag())==0 && getApiClient() != null && AppVersionUtils.compareTo(getApiClient().get("app_vision"), "5.2.0") > 0) {//Since5.2.2版本暂存款不支持积分商城
			teslaXOrder.getUse().setZck_money(inputParam.getUsedZckTotal()); 
		}else {
			teslaXOrder.getUse().setZck_money(0);
		}
		
		// 使用惠币
		teslaXOrder.getUse().setHjycoin(inputParam.getUsedhjycoinTotal());
		
		result.setInformation((new AddressService()).getAddressOne(inputParam.getBuyer_address_id(), getUserCode(), getManageCode()));
		if (StringUtils.isBlank(teslaXOrder.getAddress().getAreaCode())) {
			teslaXOrder.getAddress().setAreaCode(result.getInformation().getArea_code());
		}		
		
		//网易考拉UserInfo参数
		if(orderItemList != null && orderItemList.size() > 0 && StringUtils.isNotBlank(result.getInformation().getAddress_id())) {
			UserInfo userInfo = new UserInfo();
			if(!KaolaOrderService.setOrderAddress(userInfo, result.getInformation().getArea_code())) {
				result.setResultCode(916421172);
				result.setResultMessage(bInfo(916421172));
				return result;
			}
			teslaXOrder.setIsKaolaOrder(1);			
			//userInfo.setAccountId("lxhtest10@163.com");
			userInfo.setAccountId(getUserCode());//正式环境传用户编号
			userInfo.setName(result.getInformation().getAddress_name());//收货人
			userInfo.setMobile(result.getInformation().getAddress_mobile());//手机号码			
			userInfo.setAddress(result.getInformation().getAddress_street());
			if(!new KaolaOrderService().upKaolaConfirmOrderInterface(orderItemList, userInfo, teslaXOrder)) {
				result.setResultCode(963906057);
				result.setResultMessage("此商品库存不足，暂不支持购买！");
				return result;
			}
		}
		
		//积分商城订单确认，校验积分商城活动库存数是否正确
		int one_allowed = 0;
		int buyedCount = 0;
		int allow_count = 0;
		if(!"0".equals(inputParam.getIntegralFlag())) {
			//设置积分商城标示
			teslaXOrder.setPointShop(true);
			
			Map<String, Object> channelDetail = DbUp.upTable("fh_apphome_channel_details").dataSqlOne("select one_allowed, allow_count from fh_apphome_channel_details where uid = '"
					+ inputParam.getIntegralDetailId() + "'", new MDataMap());
			if(channelDetail != null) {
				allow_count = MapUtils.getInteger(channelDetail, "allow_count", 0);
				if(inputParam.getGoods().get(0).getSku_num() > allow_count) {
					result.setResultCode(916423400);
					result.setResultMessage(bInfo(916423400));
					return result;
				}
				
				one_allowed = MapUtils.getIntValue(channelDetail, "one_allowed", 0);
				if(one_allowed != 0) {
					buyedCount = DbUp.upTable("oc_orderinfo").upTemplate().queryForInt("select ifnull(sum(d.sku_num), 0) from oc_orderinfo i, oc_orderdetail d, "
							+ "oc_order_activity a where i.order_code = d.order_code and i.order_code = a.order_code and a.product_code = d.product_code and a.sku_code = d.sku_code and i.buyer_code = '"
							+ getUserCode() + "' and i.order_status != '4497153900010006' and d.product_code = '" + inputParam.getGoods().get(0).getProduct_code() + "' " + "and a.activity_code = '" + inputParam.getIntegralDetailId() + "'", 
							new HashMap<String, Object>());
					if((one_allowed - buyedCount) < inputParam.getGoods().get(0).getSku_num()) {
						result.setResultCode(916423401);
						result.setResultMessage(bInfo(916423401));
						return result;
					}
				}
			}
		}
		
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
		
		TeslaXResult reTeslaXResult = new ApiConvertTeslaService().ConvertOrder(teslaXOrder);
		if (reTeslaXResult.upFlagTrue()) {
			/** ===============积分相关=================== */
			result.setMyIntegralTotal(teslaXOrder.getAllIntegral().intValue());
			result.setUsableIntegralTotal(teslaXOrder.getMaxUseIntegral().intValue());
			result.setUsableIntegralTotalMoney(plusServiceAccm.accmAmtToMoney(new BigDecimal(result.getUsableIntegralTotal()),2));
			
			// 启用了积分且用户有可用积分时需要显示积分
			if(plusServiceAccm.isEnabledAccm() && teslaXOrder.getMaxUseIntegral().intValue() > 0 && "0".equals(inputParam.getIntegralFlag())){
				result.setIntegralShowFlag(1);
			}
			
			// 如果互动活动不支持积分
			if(teslaXOrder.getHuDongEvent() != null && !teslaXOrder.getHuDongEvent().getOrderLimit().contains("449748520005")) {
				result.setIntegralShowFlag(0);
				result.setUsableBeanTotal(0);
				result.setUsableIntegralTotalMoney(BigDecimal.ZERO);
				result.setIntegralZh(1);
			}
			
			// 惠币
			if("0".equals(inputParam.getIntegralFlag())) {
				result.setHjycoinFlag(1);
				result.setHjycoinMoney(teslaXOrder.getAllHjycoin());
				result.setHjycoinMaxMoney(teslaXOrder.getMaxUseHjycoin());
				result.setUsedHjycoinMoney(new BigDecimal(teslaXOrder.getUse().getHjycoin()).setScale(0, BigDecimal.ROUND_HALF_UP));
				
				// 没有惠币时隐藏
				if(result.getHjycoinMoney().compareTo(BigDecimal.ZERO) <= 0) {
					result.setHjycoinFlag(0);
				}
			}
			
			// 判断订单来源是否可以使用惠币
			if(!new HjycoinService().checkUseEnabled(teslaXOrder.getUorderInfo().getOrderSource())) {
				result.setHjycoinFlag(0);
			}
			
			/** ===============积分相关=================== */
			
			/** ===============储值金、暂存款相关=================== */
			//设置是否显示储值金、总储值金、最大可用储值金    20180521 -rhb
			boolean flag1 = false; //tv品订单标识
			boolean flag2 = false; //自营品订单标识
			for(TeslaModelOrderInfo teslaModelOrderInfo : teslaXOrder.getSorderInfo()) {
				if(teslaModelOrderInfo.getSmallSellerCode().equals("SI2003") || teslaModelOrderInfo.getSmallSellerCode().equals("SI2009")) {
					flag1 = true;
				}else {
					flag2 = true;
				}
			}
			//如果存在tv品订单 且不存在自营品订单 则判断用户拥有的储值金取整后是否大于零
			if((flag1 && !flag2 && teslaXOrder.getAllCzj().setScale(0, BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO) > 0)
					|| (flag1 && flag2 && teslaXOrder.getAllCzj().doubleValue() > 0)
					|| (!flag1 && teslaXOrder.getAllCzj().doubleValue() > 0)) {
				//是否显示储值金
				result.setCzjUsableFlag(1);
			}
			//总储值金
			result.setCzjMoney(teslaXOrder.getAllCzj().doubleValue());
			//本单最大可用储值金
			result.setCzjMaxMoney(teslaXOrder.getMaxUseCzj().doubleValue());
			//Since5.2.0版本储值金不支持积分商城 -rhb
			if(Integer.parseInt(inputParam.getIntegralFlag())==1) {
				result.setCzjUsableFlag(0);
				result.setCzjMaxMoney(0);
			}
			
			// 如果互动活动不支持储值金
			if(teslaXOrder.getHuDongEvent() != null && !teslaXOrder.getHuDongEvent().getOrderLimit().contains("449748520003")) {
				result.setCzjUsableFlag(0);
				result.setCzjMaxMoney(0);
				result.setCzjZh(1);
			}
			
			//设置是否显示暂存款、总暂存款、最大可用暂存款  20180521 -rhb
			//如果存在tv品订单 且不存在自营品订单 则判断用户拥有的储值金取整后是否大于零
			if((flag1 && !flag2 && teslaXOrder.getAllZck().setScale(0, BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO) > 0)
					|| (flag1 && flag2 && teslaXOrder.getAllZck().doubleValue() > 0)
					|| (!flag1 && teslaXOrder.getAllZck().doubleValue() > 0)) {
				//是否显示储值金
				result.setZckUsableFlag(1);
			}
			result.setZckMoney(teslaXOrder.getAllZck().doubleValue());
			result.setZckMaxMoney(teslaXOrder.getMaxUseZck().doubleValue());
			//Since5.2.0版本暂存款不支持积分商城 -rhb  添加版本兼容5.2.0（不含）以上版本支出暂存款
			if(Integer.parseInt(inputParam.getIntegralFlag())==1  || getApiClient() == null || AppVersionUtils.compareTo(getApiClient().get("app_vision"), "5.2.0") <= 0 ) {
				result.setZckUsableFlag(0);
				result.setZckMaxMoney(0);
			}
			
			// 如果互动活动不支持暂存款
			if(teslaXOrder.getHuDongEvent() != null && !teslaXOrder.getHuDongEvent().getOrderLimit().contains("449748520004")) {
				result.setZckUsableFlag(0);
				result.setZckMaxMoney(0);
				result.setZckZh(1);
			}
			
			/** ===============储值、暂存款金相关=================== */
			
			result.setIsVerifyIdNumber(teslaXOrder.getOrderOther().getIsVerifyIdNumber());
			result.setBills((new InvoiceService()).getInvoiceListRedis(getManageCode()));
			result.setCash_back(new ShopCartService().getScaleReckonMapTesla(teslaXOrder)
					.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
			List<String> couponCodes = teslaXOrder.getUse().getCoupon_codes();
			List<BigDecimal> couponMoneys = teslaXOrder.getUse().getCoupon_Moneys();
			List<String> couponTypes = teslaXOrder.getUse().getCoupon_types();
			List<String> isMultiUses = teslaXOrder.getUse().getIs_multi_uses();
			//积分商城购买时，不返回优惠券
			if (couponCodes != null && couponCodes.size() > 0 && "0".equals(inputParam.getIntegralFlag())) {
				for(int i=0; i<couponCodes.size(); i++) {
					CouponInfo ci = new CouponInfo();
					ci.setCouponCode(couponCodes.get(i));
					ci.setSurplusMoney(couponMoneys.get(i));
					ci.setMoneyType(couponTypes.get(i));
					ci.setActivityCode(new TeslaCouponService().getCouponActivityCode(couponCodes.get(i)));
					ci.setIs_multi_use(isMultiUses.get(i));
					result.getCoupons().add(ci);
				}				
			}
			result.setDisList(teslaXOrder.getShowMoney());
			
			// 已推荐最优方式文字
			if("1".equals(teslaXOrder.getAutoSelectCoupon()) && couponCodes != null && !couponCodes.isEmpty()){
				result.setCouponRecommend(bConfig("familyhas.coupon_best_tips"));
			}
			
			List<String> disRemarks = new ArrayList<String>();
			if (AppConst.MANAGE_CODE_CDOG.equals(getManageCode())) {
				disRemarks.add(bConfig("sharpei.fullSubActivityOtherRemark"));
				result.setBillRemark(bConfig("sharpei.billrenark"));
			} else {
				disRemarks.add(bConfig("familyhas.fullSubActivityOtherRemark"));
				result.setBillRemark(bConfig("familyhas.billrenark"));
			}
			result.setDisRemarks(disRemarks);
			BigDecimal sentMoney = BigDecimal.ZERO;
			for (int i = 0; i < teslaXOrder.getRoles().size(); i++) {
				OrderSort sort = new OrderSort();
				sort.setSkuCodes(teslaXOrder.getRoles().get(i).getSkus());
				sort.setJjgFlag(teslaXOrder.getRoles().get(i).getJjgFlag());
				sort.setTranMoney(teslaXOrder.getRoles().get(i).getTransportMoney().doubleValue());
				sentMoney = sentMoney.add(teslaXOrder.getRoles().get(i).getTransportMoney());
				result.getOrders().add(sort);
			}
			
			//积分商城只允许在线支付
			if("1".equals(inputParam.getIntegralFlag())) {
				result.setPay_type("449716200001");
			} 
			//网易考拉商品只允许在线支付
			else if(teslaXOrder.getIsKaolaOrder() == 1) {
				result.setPay_type("449716200001");
			} else {
				result.setPay_type(teslaXOrder.getUorderInfo().getPayType());
			}
			if("80".equals(memberLevel)) {
				//警惕会员
				result.setPay_type("449716200001");
			}
			
			double totalSkuPrice = 0;//购买的是所有商品价格总和，针对于积分商城用
			int totalUsePoint = 0;//总使用积分
			for (int i = 0; i < teslaXOrder.getShowGoods().size(); i++) {
				if ("0".equals(teslaXOrder.getShowGoods().get(i).getGiftFlag())) {
					continue;
				}
				GoodsInfoForConfirm goodsInfo = new GoodsInfoForConfirm();
				goodsInfo.setFlagTheSea(teslaXOrder.getShowGoods().get(i).getFlagTheSea());
				goodsInfo.setNow_stock(
						new PlusSupportStock().upAllStock(teslaXOrder.getShowGoods().get(i).getSkuCode()));
				goodsInfo.setPic_url(teslaXOrder.getShowGoods().get(i).getProductPicUrl());
				goodsInfo.setProduct_code(teslaXOrder.getShowGoods().get(i).getProductCode());
				goodsInfo.setSales_info(teslaXOrder.getShowGoods().get(i).getActivity_name());
				goodsInfo.setSales_type(teslaXOrder.getShowGoods().get(i).getActivity_name());
				goodsInfo.setAlert(teslaXOrder.getShowGoods().get(i).getAlert());
				goodsInfo.setFxFlag(teslaXOrder.getShowGoods().get(i).getFxFlag());
				if("1".equals(teslaXOrder.getShowGoods().get(i).getIfJJGFlag())) {
					goodsInfo.setIfJJGFlag("1");
					goodsInfo.setJjgFlagPic(bConfig("familyhas.jjgFlagPic"));
				}
				//524:走缓存添加商品分类标签 加载不到 ，走库查询的东西又太多，折中：要啥查啥
				PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(teslaXOrder.getShowGoods().get(i).getProductCode()));
				
				String ssc =productInfo.getSmallSellerCode();
				String st="";
				if("SI2003".equals(ssc)) {
					st="4497478100050000";
				}
				else {
					st = WebHelper.getSellerType(ssc);
				}
				//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
				Map productTypeMap = WebHelper.getAttributeProductType(st);
				goodsInfo.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
				if (map.containsKey(teslaXOrder.getShowGoods().get(i).getSkuActivityCode())) {
					goodsInfo.setSku_code(teslaXOrder.getShowGoods().get(i).getSkuActivityCode());

				} else if (map.containsKey(teslaXOrder.getShowGoods().get(i).getSkuCode())) {

					goodsInfo.setSku_code(teslaXOrder.getShowGoods().get(i).getSkuCode());
				}
				goodsInfo.setSku_name(teslaXOrder.getShowGoods().get(i).getSkuName());
				goodsInfo.setSku_num(teslaXOrder.getShowGoods().get(i).getSkuNum());
//				goodsInfo.setLabelsList(teslaXOrder.getShowGoods().get(i).getLabelsList());
//				goodsInfo.setLabelsPic(teslaXOrder.getShowGoods().get(i).getLabelsPic());
				goodsInfo.setSku_property(new ShopCartService().rePropertiesForTesla(teslaXOrder.getShowGoods().get(i).getSkuCode(),
								teslaXOrder.getShowGoods().get(i).getSku_keyValue()));
				//在订单确认页增加权威标识图标(如7天退货等). add by zht 2017-03-03
				PlusModelProductAuthorityLogoForSellerQuery query = new PlusModelProductAuthorityLogoForSellerQuery(teslaXOrder.getShowGoods().get(i).getProductCode());
				PlusModelAuthorityLogos logos = new LoadProductAuthorityLogoForSeller().upInfoByCode(query);
				if(null != logos && !logos.getAuthorityLogos().isEmpty()) {
					goodsInfo.getAuthorityLogo().addAll(logos.getAuthorityLogos());
				}
				
				if (teslaXOrder.getShowGoods().get(i).isIs_activity()) {
					Activity activity = new Activity();
					activity.setActivity_name(teslaXOrder.getShowGoods().get(i).getActivity_name());
					activity.setActivity_info(teslaXOrder.getShowGoods().get(i).getActivity_name());
					goodsInfo.getActivitys().add(activity);
				}
				
				//设置是否原价购买返回前段
				goodsInfo.setFlg(teslaXOrder.getShowGoods().get(i).getIsSkuPriceToBuy());
				
				//如果是积分商城数据，则返回积分
				if("1".equals(inputParam.getIntegralFlag())) {
					Map<String, Object> channelDetail = DbUp.upTable("fh_apphome_channel_details").dataSqlOne("select d.extra_charges, (d.jf_cost * 200) jf_cost from fh_apphome_channel_details d where d.uid = '" 
							+ inputParam.getIntegralDetailId() + "'", new MDataMap());
					goodsInfo.setIntegral(MapUtils.getInteger(channelDetail, "jf_cost", 0) + "");
					goodsInfo.setSku_price(Double.parseDouble(MapUtils.getString(channelDetail, "extra_charges", "0")));
					
					//为适应Double丢精度问题，做一下兼容处理
					BigDecimal skuPrice = new BigDecimal(MapUtils.getString(channelDetail, "extra_charges", "0"));
					totalSkuPrice = totalSkuPrice + skuPrice.multiply(new BigDecimal(goodsInfo.getSku_num())).doubleValue();
					totalUsePoint += (MapUtils.getInteger(channelDetail, "jf_cost", 0) * goodsInfo.getSku_num());
				}else {
					goodsInfo.setEventType(teslaXOrder.getShowGoods().get(i).getEventType());
					goodsInfo.setSku_price(teslaXOrder.getShowGoods().get(i).getSkuPrice().doubleValue());
				}
				
				// 显示市场价
				if(hasPlusProduct && productInfo.getMarketPrice().compareTo(BigDecimal.ZERO) > 0) {
					goodsInfo.setMarketPrice(MoneyHelper.format(productInfo.getMarketPrice()));
				}
				
				// 推广赚
				goodsInfo.setTgzShowCode(teslaXOrder.getShowGoods().get(i).getTgzShowCode());
				goodsInfo.setTgzUserCode(teslaXOrder.getShowGoods().get(i).getTgzUserCode());
				
				result.getResultGoodsInfo().add(goodsInfo);
			}
			
			//积分商城计算积分抵扣金额需要用原商品总金额 - 现价
			if("1".equals(inputParam.getIntegralFlag())) {
				result.setUsedIntegralTotal(totalUsePoint);
				result.setUsedIntegralMoney(new BigDecimal(teslaXOrder.getUorderInfo().getProductMoney().doubleValue() - totalSkuPrice));
				result.setPay_money(totalSkuPrice + sentMoney.doubleValue());//商品价+运费
				result.setCost_money(totalSkuPrice);
				result.setCouponAbleNum(0);
				
				//针对储值金，暂存款处理，储值金，暂存款，使用dueMoney
				teslaXOrder.getUorderInfo().setDueMoney(new BigDecimal(result.getPay_money()));
				/**start 储值金、暂存款兼容积分商城 2018-06-21 -rhb */
				if(result.getCzjUsableFlag() == 1) {
					BigDecimal dueMoney = teslaXOrder.getUorderInfo().getDueMoney();
					//因为积分商城一单一或 故可以这样取 
					if((flag2 && dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).compareTo(BigDecimal.ZERO) > 0) 
							|| (flag1 && dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).setScale(0,BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO) > 0)) {
						if(result.getCzjMaxMoney() > dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).doubleValue()) {
							if(flag1) {//自营品要取整
								result.setCzjMaxMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).setScale(0,BigDecimal.ROUND_DOWN).doubleValue());
							}else {
								result.setCzjMaxMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).doubleValue());
							}
							if(inputParam.getUsedCzjTotal() > 0) {
								List<TeslaModelDiscount> showMoney = teslaXOrder.getShowMoney();
								for (TeslaModelDiscount teslaModelDiscount : showMoney) {
									if(teslaModelDiscount.getDis_name().equals(bConfig("xmasorder.czj_name"))) {
										teslaModelDiscount.setDis_price(result.getCzjMaxMoney());
									}
								}
								teslaXOrder.getUorderInfo().setDueMoney(BigDecimal.ZERO);
								teslaXOrder.getSorderInfo().get(0).setDueMoney(BigDecimal.ZERO);
								result.setPay_money(0);
							}
						}else {
							if(inputParam.getUsedCzjTotal() > 0) {
								teslaXOrder.getUorderInfo().setDueMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney())
										.subtract(BigDecimal.valueOf(result.getCzjMaxMoney())));
								teslaXOrder.getSorderInfo().get(0).setDueMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney())
										.subtract(BigDecimal.valueOf(result.getCzjMaxMoney())));
								result.setPay_money(result.getPay_money() - result.getCzjMaxMoney());
							}
						}
					}else {
						result.setCzjMaxMoney(0);
					}
				}
				
				if(result.getZckUsableFlag() == 1) {
					BigDecimal dueMoney = teslaXOrder.getUorderInfo().getDueMoney();
					//因为积分商城一单一或 故可以这样取
					if((flag2 && dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).compareTo(BigDecimal.ZERO) > 0) 
							|| (flag1 && dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).setScale(0,BigDecimal.ROUND_DOWN).compareTo(BigDecimal.ZERO) > 0)) {
						if(result.getZckMaxMoney() > dueMoney.doubleValue()) {
							if(flag1) {//自营品要取整
								result.setZckMaxMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).setScale(0,BigDecimal.ROUND_DOWN).doubleValue());
							}else {
								result.setZckMaxMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney()).doubleValue());
							}
							if(inputParam.getUsedZckTotal() > 0) {
								List<TeslaModelDiscount> showMoney = teslaXOrder.getShowMoney();
								for (TeslaModelDiscount teslaModelDiscount : showMoney) {
									if(teslaModelDiscount.getDis_name().equals(bConfig("xmasorder.zck_name"))) {
										teslaModelDiscount.setDis_price(result.getZckMaxMoney());
									}
								}
								teslaXOrder.getUorderInfo().setDueMoney(BigDecimal.ZERO);
								teslaXOrder.getSorderInfo().get(0).setDueMoney(BigDecimal.ZERO);
								result.setPay_money(0);
							}
						}else {
							if(inputParam.getUsedZckTotal() > 0) {
								teslaXOrder.getUorderInfo().setDueMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney())
										.subtract(BigDecimal.valueOf(result.getZckMaxMoney())));
								teslaXOrder.getSorderInfo().get(0).setDueMoney(dueMoney.subtract(teslaXOrder.getSorderInfo().get(0).getTransportMoney())
										.subtract(BigDecimal.valueOf(result.getZckMaxMoney())));
								result.setPay_money(result.getPay_money() - result.getZckMaxMoney());
							}
						}
					}else {
						result.setZckMaxMoney(0);
					}
				}
				/**end 储值金、暂存款兼容积分商城 2018-06-21 -rhb */
			}else {
				result.setUsedIntegralTotal(teslaXOrder.getUse().getIntegral());
				result.setUsedIntegralMoney(plusServiceAccm.accmAmtToMoney(new BigDecimal(result.getUsedIntegralTotal()),2));
				result.setPay_money(teslaXOrder.getUorderInfo().getDueMoney().doubleValue());
				result.setCost_money(teslaXOrder.getUorderInfo().getProductMoney().doubleValue());
				
				int size = 0;
				for(TeslaModelCouponInfo ci : teslaXOrder.getCouponInfoList()) {
					if("1".equals(ci.getSelectLimit())) {
						size++;
					}
				}
				
				result.setCouponAbleNum(size);
			}
			
			// 存在分销商品则屏蔽积分、储值金
			if(teslaXOrder.getActivityAgent() != null) {
				result.setCzjUsableFlag(0);
				result.setIntegralShowFlag(0);
			}
			
			result.setSent_money(sentMoney.doubleValue());
			result.setMicroCommuneOpenFlag(0);
		} else {
			//存在无库存商品 需要将无库存商品信息返回
			if(reTeslaXResult.getResultCode() == 916425001) {
				result.getNoStockOrFailureGoods().addAll(teslaXOrder.getNoStockOrFailureGoods());
			}
			result.setResultCode(reTeslaXResult.getResultCode());
			result.setResultMessage(reTeslaXResult.getResultMessage());
		}

		PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
		List<String> smallSellerCodes = new ArrayList<String>();
		Set<String> tempSet = new HashSet<String>();
		boolean warningflag = false;
		for (GoodsInfoForAdd goods : inputParam.getGoods()) {
			plusModelProductinfo = new LoadProductInfo()
					.upInfoByCode(new PlusModelProductQuery(goods.getProduct_code()));
			if (null != plusModelProductinfo) {
				smallSellerCodes.add(plusModelProductinfo.getSmallSellerCode());
			}
			if(bConfig("familyhas.happy_new_year_small_seller_code").contains(plusModelProductinfo.getSmallSellerCode()) ) {
				tempSet.add(plusModelProductinfo.getProductName());
			}
			if("SI2003".equals(plusModelProductinfo.getSmallSellerCode())) {//如果有LD品
				warningflag = true;
			}
		}
		
		List<ReminderContent> listr = new MyService().getReminderList(smallSellerCodes, "4497471600270001");
		if (listr != null && !listr.isEmpty()) {
			for (ReminderContent content : listr) {
				result.getTips().add(content.getContent());
			}
		}
		//2019新年停止发货提示 临时功能
		if(!tempSet.isEmpty() && DateUtil.compareDateTime(DateUtil.getSysDateTimeString(), bConfig("familyhas.happy_new_year_end_datetime"))) {
			String goodNames = "";
			if(tempSet.size()==1) {
				goodNames = tempSet.iterator().next();
			}else {
				Iterator<String> iterator = tempSet.iterator();
				while (iterator.hasNext()) {
					String goodName = iterator.next();
					goodNames += StringUtils.left(goodName, 10) + "、";
					
				}
				goodNames = goodNames.substring(0, goodNames.length()-1);
			}
			result.getTips().add(bConfig("familyhas.happy_new_year_tip_start")+goodNames);
		}

		// 添加LD商品金额共前端使用
		BigDecimal productMoney = BigDecimal.ZERO;
		for (TeslaModelOrderInfo modelOrderInfo : teslaXOrder.getSorderInfo()) {
			if (AppConst.MANAGE_CODE_HOMEHAS.equals(modelOrderInfo.getSmallSellerCode())
					|| AppConst.MANAGE_CODE_HPOOL.equals(modelOrderInfo.getSmallSellerCode())) {
				productMoney = productMoney.add(modelOrderInfo.getDueMoney());
			}
		}
			
		List<TeslaModelOrderDetail> detailList= teslaXOrder.getOrderDetails();
		List<String> productCodeList = new ArrayList<String>();
		for(TeslaModelOrderDetail detail : detailList){
			productCodeList.add(detail.getProductCode());
		}
		
		if("449747430002".equals(inputParam.getChannelId())){
			result.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(teslaXOrder.getUorderInfo().getDueMoney(), PaymentTypeService.Channel.WAP, productCodeList));	
		}else if("449747430003".equals(inputParam.getChannelId()) || "449747430023".equals(inputParam.getChannelId())){
			result.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(teslaXOrder.getUorderInfo().getDueMoney(), PaymentTypeService.Channel.WAP, productCodeList));	
		}else if("449747430004".equals(inputParam.getChannelId())){
			result.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(teslaXOrder.getUorderInfo().getDueMoney(), PaymentTypeService.Channel.WEB, productCodeList));	
		}else{
			result.setPaymentTypeAll(new PaymentTypeService().getSupportPayTypeList(teslaXOrder.getUorderInfo().getDueMoney(), PaymentTypeService.Channel.ANDROID, productCodeList));
		}
		
		// 支持货到付款的订单再检查一下家有的系统是否对区域限制了
		if(result.getResultCode() == 1 
				&& "449716200002".equals(result.getPay_type())
				&& StringUtils.isNotBlank(teslaXOrder.getAddress().getAreaCode())
				&& DlvPayLoader.hasContains(teslaXOrder.getAddress().getAreaCode())){
			setPayType(result, detailList, inputParam, teslaXOrder);
		}
		
		result.setRoomId(inputParam.getRoomId());
		result.setAnchorId(inputParam.getAnchorId());
		result.setProductMoneyForLD(productMoney.doubleValue());
		
		// 积分商城不显示立减多少元
		if("0".equals(inputParam.getIntegralFlag())) {
			if(teslaXOrder.getOnlinePayEventMoney().compareTo(BigDecimal.ZERO) > 0){
				result.setOnlinePayTips(String.format(bConfig("familyhas.online_pay_tips"), new DecimalFormat("#.##").format(teslaXOrder.getOnlinePayEventMoney())));
			}
		}
		
		// 在线支付再减优惠券金额
		if(teslaXOrder.getOnlinePayEventMoney().compareTo(BigDecimal.ZERO) > 0){
			result.setOnlinePayTips(String.format(bConfig("familyhas.online_pay_tips"), new DecimalFormat("#.##").format(teslaXOrder.getOnlinePayEventMoney())));
		}
		
//			String isShowDisMoney = XmasKv.upFactory(EKvSchema.CgroupMoney).get("view");
		//modify by zht
		//String isShowDisMoney = XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", "view");
		//result.setIsShowDisMoney(StringUtils.isBlank(isShowDisMoney) ? 0 : Integer.parseInt(isShowDisMoney));
		result.setIntegralRegulation(this.bConfig("xmassystem.integralRegulation"));
		// 确认订单，如果是单件商品订单，返回库存信息
		if(teslaXOrder.getStockInfo() != null) {
			result.setShowLimitNum(teslaXOrder.getStockInfo().getShowLimitNum());
			result.setStockNumSum(teslaXOrder.getStockInfo().getStockNumSum());
			result.setMiniOrder(teslaXOrder.getStockInfo().getMiniOrder());
			List<GoodsInfoForConfirm> resultGoodsInfo = result.getResultGoodsInfo();
			boolean neigouFlag = false;
			String productCode = "";
			if(null!=resultGoodsInfo&&resultGoodsInfo.size()==1) {//单商品、有内购则加限购
				if("4497472600010006".equals(resultGoodsInfo.get(0).getEventType())) {
					neigouFlag = true;
					productCode =  resultGoodsInfo.get(0).getProduct_code();
				}
			}
			if(!"0".equals(inputParam.getIntegralFlag())) {
				int real_stock = teslaXOrder.getStockInfo().getStockNumSum();
				//积分显示限购
				if(one_allowed == 0) {
					result.setShowLimitNum(0); //不限制购买数量
					result.setMaxBuyCount(real_stock < allow_count ? real_stock : allow_count);
				}else {
					result.setShowLimitNum(1);	
					
					int maxbuyCount = one_allowed - buyedCount < 0 ? 0 : one_allowed - buyedCount;					
					int min = (real_stock < one_allowed) ? real_stock : one_allowed;
					min = (min < allow_count) ? min : allow_count;
					if(maxbuyCount > min) {
						maxbuyCount = min;
					}
					result.setMaxBuyCount(maxbuyCount);
					result.setLimitBuy(min);										
				}												
			} else if(neigouFlag){
				//内购显示限购
				result.setShowLimitNum(1);
				Integer numHistory = numHistory(getUserCode(),productCode);
				result.setMaxBuyCount(2 - numHistory);
				result.setLimitBuy(2);
			}else {
				result.setMaxBuyCount(teslaXOrder.getStockInfo().getMaxBuyCount());
				result.setLimitBuy(teslaXOrder.getStockInfo().getLimitBuy());
			}			
		}		
		
		//544需求添加
		for(TeslaModelOrderDetail goodsInfo : teslaXOrder.getOrderDetails()) {
			//根据商户编号查询商品是否是LD品
			// 排除赠品
			if("1".equals(goodsInfo.getGiftFlag()) && "SI2003".equals(goodsInfo.getSmallSellerCode())) {
				// 先去查一下最小起订量,最小起订量如果＞5则不提示
				Map<String, Object> skuInfoMap = DbUp.upTable("pc_skuinfo").dataSqlOne(
						"SELECT pc_skuinfo.mini_order FROM pc_skuinfo WHERE pc_skuinfo.sku_code =:sku_code", 
							new MDataMap("sku_code", goodsInfo.getSkuCode()));
				int mini_order = (int) skuInfoMap.get("mini_order");
				if(mini_order <= 5) {					
					//LD品一次购买最多到5件
					if(result.getMaxBuyCount() > 5) {
						result.setMaxBuyCount(5);
					}
				}
			}
		}
		
		//由于拼团业务不支持货到付款，将支付方式改为在线支付
		if("1".equals(teslaXOrder.getCollageFlag())) {
			result.setPay_type("449716200001");
		}
		
		//果园农场兑换仅支持在线支付且不支持优惠券
		if(teslaXOrder.getHuDongEvent() != null) {
			result.setPay_type("449716200001");
			result.setCouponUsableFlag(0);
		}
		
		//判断版本号，5.4.2版本以后
		if(result.getResultCode() == 1) {
			String version = teslaXOrder.getUorderInfo().getAppVersion();
			String areaCode = teslaXOrder.getAddress().getAreaCode();
			if(areaCode != null && !"".equals(areaCode)) {
				if(AppVersionUtils.compareTo(version, "5.4.2") >= 0) {
					int count = DbUp.upTable("sc_tmp").count("code", areaCode, "code_lvl", "4", "use_yn", "Y", "send_yn", "Y");
					if(count <= 0) {
						result.setResultCode(963910001);
						result.setResultMessage(bInfo(963910001));
					}
				}else {
					int count = DbUp.upTable("sc_tmp").count("code", areaCode, "use_yn", "Y", "send_yn", "Y");
					if(count <= 0) {
						result.setResultCode(963910001);
						result.setResultMessage(bInfo(963910001));
					}
				}
			}
		}
		result.setConfirmpassword(Integer.parseInt(bConfig("familyhas.confirmpassword")));
		//548兑换码兑换、惠惠农场
		if(StringUtils.isNotEmpty(inputParam.getActivityCode()) && StringUtils.isNotBlank(inputParam.getRedeemCode())) {
			if(teslaXOrder.getUorderInfo().getDueMoney().compareTo(BigDecimal.ZERO) == 0) {
				result.getPaymentTypeAll().clear();
				result.setPay_type("");
			}else {
				result.setPay_type("449716200001");
			}
			hasPlusProduct = false;
			result.setIntegralShowFlag(0);
			result.setCzjUsableFlag(0);
			result.setZckUsableFlag(0);
			result.setCouponUsableFlag(0);
		}
		//受新冠病毒影响，新增延迟配送区域提示。
		if(warningflag) {
			String areaCode = teslaXOrder.getAddress().getAreaCode();
			if(StringUtils.isNotBlank(areaCode)) {
				String tip = this.checkDelay(areaCode);//校验是否受影响
				if(!StringUtils.isEmpty(tip)) {
					result.getTips().add(tip);
				}
			}
		}
		
		// 会员卡商品返回相关字段
		if(hasPlusProduct) {
			// 默认显示的折扣
			BigDecimal discount = new BigDecimal(bConfig("xmassystem.plus_disount"));
			// 如果有活动则以活动设置为准
			PlusModelEventInfoPlus event = new PlusServiceEventPlus().getEventInfoPlus();
			if(event != null) {
				discount = event.getPrice();
			}
			
			// 把0.95转成95
			discount = discount.multiply(new BigDecimal(100));
			result.setPlusDiscount(discount.intValue()+"");
			result.setPlusDocUrl(bConfig("xmassystem.plus_doc_url"));
			
			// 562优化橙意会员卡客服电话
			String version = teslaXOrder.getUorderInfo().getAppVersion();
			String plusKfTelphone = bConfig("xmassystem.plus_kf_telphone");
			String[] split = plusKfTelphone.split("-");
			if(AppVersionUtils.compareTo(version, "5.6.2") >= 0) {
				plusKfTelphone = split[0]+"-"+split[1]+"转"+split[2];				
			}else {
				plusKfTelphone = split[0]+"-"+split[1];
			}
			result.setPlusKfTelphone(plusKfTelphone);
			
			result.setPlusBuyTips(bConfig("xmassystem.plus_buy_tips"));
		}
		
		//560增加厂商收款字段
		if(dlrType.equals("02")){
			result.getPaymentTypeAll().clear();
			result.setIntegralZh(1);
			result.setCzjZh(1);
			result.setZckZh(1);
			result.setHjycoinZh(1);
			result.setPay_type("449716200002");
			if(StringUtils.isNotBlank(appVision) && AppVersionUtils.compareTo("5.6.0", appVision) > 0){
				result.setIntegralShowFlag(0);
				result.setCzjUsableFlag(0);
				result.setZckUsableFlag(0);
			}
		}
		
		//投票换购活动 禁用优惠券
		String eventCode = teslaXOrder.getEventCode();
		if(StringUtils.isNotBlank(eventCode)) {//投票换购需要去掉银联支付
			result.setCouponUsableFlag(0);
			//投票换购活动 不支持银联支付
			if(result.getPaymentTypeAll().size() > 0) {
				Iterator<String> iterator = result.getPaymentTypeAll().iterator();
				while (iterator.hasNext()) {
					String paymentType = iterator.next();
					if("449746280014".equals(paymentType)) {
						iterator.remove();
						break;
					}
				}
			}
		}
		
		smgCoupon(teslaXOrder);
				
	    return result;
	}

	private APiOrderConfirmResult validateJJG(TeslaModelJJG jjg, APiOrderConfirmInput inputParam, APiOrderConfirmResult result) {
		// TODO Auto-generated method stub
		String eventCode = jjg.getEventCode();
		PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
		plusModelEventQuery.setCode(eventCode);
		//PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(plusModelEventQuery);
		MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code", eventCode);
		String sysDateTimeString = DateUtil.getSysDateTimeString();
		//int dataCount = DbUp.upTable("sc_event_info").dataCount("event_code='"+eventCode+"' and event_status='4497472700020002' and begin_time<='"+sysDateTimeString+"' and end_time>'"+sysDateTimeString+"'", null);
		if(!StringUtils.equals(eventInfo.get("event_status"), "4497472700020002")||!(DateUtil.compareDateTime(eventInfo.get("begin_time"),sysDateTimeString)&&DateUtil.compareDateTime(sysDateTimeString,eventInfo.get("end_time")))) {
			//换购活动失效
			if(StringUtils.isBlank(result.getJjgTips())) {
				result.setJjgTips(eventInfo.get("event_name"));
			}else {
				result.setJjgTips(result.getJjgTips()+","+eventInfo.get("event_name"));
			}

		}else{
			String skuCodes = jjg.getSkuCodes();
			String[] split = skuCodes.split(",");
			for (String skuCode : split) {		
				//int jjgStore = DbUp.upTable("sc_event_item_product").dataCount("event_code='"+eventCode+"' and flag_enable=1 and sku_code=:sku_code and sales_num>0", new MDataMap("sku_code",skuCode));
				 MDataMap one = DbUp.upTable("sc_event_item_product").one("event_code",eventCode,"sku_code",skuCode,"flag_enable","1");
					//换购商品进行并入处理
					GoodsInfoForAdd goodsInfoForAdd = new GoodsInfoForAdd();
					goodsInfoForAdd.setChooseFlag("1");
					goodsInfoForAdd.setSku_num(1);
					goodsInfoForAdd.setIsPurchase(0);
					goodsInfoForAdd.setSku_code(skuCode);
					goodsInfoForAdd.setProduct_code(one.get("product_code"));
					goodsInfoForAdd.setFlg("0");
					goodsInfoForAdd.setIfJJGFlag("1");
					inputParam.getGoods().add(goodsInfoForAdd);
				 /* int allStore = new PlusSupportStock().upAllStock(skuCode);
				 if(allStore==0||Integer.parseInt(one.get("sales_num"))==0) {
					result.setResultCode(0);
					result.setResultMessage("换购活动:"+eventInfo.getEventName()+",商品名称:"+one.get("sku_name")+"无库存!");
					
				}else {
					//换购商品进行并入处理
					GoodsInfoForAdd goodsInfoForAdd = new GoodsInfoForAdd();
					goodsInfoForAdd.setChooseFlag("1");
					goodsInfoForAdd.setSku_num(1);
					goodsInfoForAdd.setIsPurchase(0);
					goodsInfoForAdd.setSku_code(skuCode);
					goodsInfoForAdd.setProduct_code(one.get("product_code"));
					goodsInfoForAdd.setFlg("0");
					goodsInfoForAdd.setIfJJGFlag("1");
					inputParam.getGoods().add(goodsInfoForAdd);
				}*/
			}
		}
		
		return result;
	}

	/**
	 * 校验区域是否受影响
	 * @param areaCode
	 * @return
	 */
	private String checkDelay(String areaCode) {
		MDataMap area = DbUp.upTable("sc_tmp").one("code",areaCode);
		if(area == null) {
			return "";
		}
		String delay_yn = area.get("delay_yn");
		if("Y".equals(delay_yn)||"y".equals(delay_yn)) {
			return bConfig("familyhas.ncp_warning");
		}
		String a = "";
		String pCode = area.get("p_code");
		if(!StringUtils.isEmpty(pCode)) {
			a = this.checkDelay(pCode);
		}
		return a;
	}
	
	/**
	 * 校验优惠券支付类型
	 * @param inputParam
	 */
	private void checkCoupon(APiOrderConfirmInput inputParam) {
		List<String> coupons = inputParam.getCoupon_codes();
		List<String> coupons2 = new ArrayList<String>();
		if(coupons == null || coupons.isEmpty()) {
			return;
		}
		String payType = inputParam.getPayType();
		if("449716200001".equals(payType)){//在线支付
			payType = "449748290001";
		}else if("449716200002".equals(payType)){//货到付款
			payType = "449748290002";
		}
		for(String couponCode : coupons) {
			MDataMap map = DbUp.upTable("oc_coupon_info").one("coupon_code",couponCode);
			String couponTypeCode = map.get("coupon_type_code");
			MDataMap map2 = DbUp.upTable("oc_coupon_type_limit").one("coupon_type_code",couponTypeCode);
			String paymentType = "";
			if(map2 != null && !map2.isEmpty()) {
				paymentType = map2.get("payment_type");
			}
			if("".equals(paymentType)||"449748290003".equals(paymentType)||payType.equals(paymentType)) {//不限制
				coupons2.add(couponCode);
			}
		}
		inputParam.setCoupon_codes(coupons2);
		
	}

	private void checkWgs(APiOrderConfirmInput input, GroupAccountInfoResult grResult, APiOrderConfirmResult result) {
		if (grResult.upFlagTrue() && StringUtils.isNotBlank(grResult.getWithdrawMoney())) {
			// result.setWgsMoney(new
			// BigDecimal(grResult.getWithdrawMoney()).setScale(0,
			// BigDecimal.ROUND_DOWN).doubleValue());
			result.setWgsMoney(MoneyHelper.roundHalfUp(new BigDecimal(grResult.getWithdrawMoney())).doubleValue());// 兼容小数
																													// -
																													// Yangcl
		}

		if ("0".equals(grResult.getFlagEnable())
				|| (new BigDecimal(grResult.getWithdrawMoney()).setScale(2, BigDecimal.ROUND_DOWN).intValue() <= 0)) {
			input.setWgsUseMoney(0.00);
		} else if (new BigDecimal(grResult.getWithdrawMoney()).setScale(2, BigDecimal.ROUND_DOWN)
				.compareTo(BigDecimal.valueOf(input.getWgsUseMoney())) < 0) {
			input.setWgsUseMoney(Double.valueOf(grResult.getWithdrawMoney()));
		}

	}
	
	private void setPayType(APiOrderConfirmResult result, List<TeslaModelOrderDetail> detailList,APiOrderConfirmInput inputParam, TeslaXOrder teslaXOrder){
		RsyncCheckDlvPay checkDlvPay = new RsyncCheckDlvPay();
		
		for(TeslaModelOrderDetail detail : detailList){
			if (AppConst.MANAGE_CODE_HOMEHAS.equals(detail.getSmallSellerCode())
					|| AppConst.MANAGE_CODE_HPOOL.equals(detail.getSmallSellerCode())) {
				// 忽略赠品
				if(!"1".equals(detail.getGiftFlag())){
					continue;
				}
				
				RsyncRequestCheckDlvPay.GoodInfo goodInfo = new RsyncRequestCheckDlvPay.GoodInfo();
				goodInfo.setGood_id(detail.getProductCode());
				goodInfo.setGood_cnt(detail.getSkuNum()+"");
				goodInfo.setColor_id("");
				goodInfo.setStyle_id("");
				goodInfo.setSite_no("");
				checkDlvPay.upRsyncRequest().getGood_info().add(goodInfo);
			}
		}
		
		// 设置是否支持货到付款
		if(!checkDlvPay.upRsyncRequest().getGood_info().isEmpty() && StringUtils.isNotBlank(teslaXOrder.getAddress().getAreaCode())){
			checkDlvPay.upRsyncRequest().setSrgn_cd(teslaXOrder.getAddress().getAreaCode());
			if("449747430001".equals(inputParam.getChannelId())){
				checkDlvPay.upRsyncRequest().setMedi_mclss_id("34");
			}else if("449747430002".equals(inputParam.getChannelId())){
				checkDlvPay.upRsyncRequest().setMedi_mclss_id("35");
			}else if("449747430003".equals(inputParam.getChannelId())){
				checkDlvPay.upRsyncRequest().setMedi_mclss_id("42");
			}else if("449747430004".equals(inputParam.getChannelId())){
				checkDlvPay.upRsyncRequest().setMedi_mclss_id("2");
			}else{
				checkDlvPay.upRsyncRequest().setMedi_mclss_id("34");
			}
			
			
			try {
				if(checkDlvPay.doRsync() && "1".equals(checkDlvPay.upProcessResult().getResult())){
					result.setPay_type("449716200001");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Integer numHistory(String isMemberCode,String productCode) {
		String sql = "";
		if("".equals(productCode)) {
			sql = "SELECT SUM(b.sku_num) num	"
					+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
					+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
					+ "AND a.order_type = '449715200007' "
					+ "AND b.gift_flag = '1' "					
					+ "AND a.order_status != '4497153900010006'	"
					+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
		}else {
			sql = "SELECT SUM(b.sku_num) num	"
					+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
					+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
					+ "AND a.order_type = '449715200007' "
					+ "AND b.gift_flag = '1' "
					+ "AND a.order_status != '4497153900010006' "
					+ "AND b.product_code = '"+productCode+"'	"
					+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
		}
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, null);
		Integer object = 0;
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			Map<String, Object> map = dataSqlList.get(0);
			Object object2 = map.get("num");
			if(object2!=null) {
				object = ((BigDecimal)object2).intValue();
			}
		}
		return object;
	}
	
	/**
	 * 扫码购的召回预处理数据记录
	 * @param teslaXOrder
	 */
	private void smgCoupon(TeslaXOrder teslaXOrder) {
		// 暂定扫码购的来源
		String[] orderSoures = {"449715190007","449715190020","449715190027","449715190033","449715190041",
				"449715190042","449715190043","449715190044","449715190045","449715190046","449715190047"};
		
		if(!ArrayUtils.contains(orderSoures, teslaXOrder.getUorderInfo().getOrderSource())) {
			return;
		}
		
		String productCode = teslaXOrder.getOrderDetails().get(0).getProductCode();
		String memberCode = teslaXOrder.getUorderInfo().getBuyerCode();
		
		// 1小时内
		Date time60 = DateUtils.addMinutes(new Date(), -60);
		
		// 1小时内同一个商品只记录一次
		int c = DbUp.upTable("fh_smg_click_detail").dataCount("member_code = :member_code AND product_code = :product_code AND create_time > :time60", new MDataMap(
				"member_code", memberCode, 
				"product_code", productCode,
				"time60", FormatHelper.upDateTime(time60,"yyyy-MM-dd HH:mm:ss")
				));
		
		// 记录扫码购的点击明细
		if(c == 0) {
			DbUp.upTable("fh_smg_click_detail").dataInsert(new MDataMap(
						"member_code", memberCode,
						"product_code", productCode,
						"flag", "0",
						"create_time", FormatHelper.upDateTime()
					));
		}
	}
}
