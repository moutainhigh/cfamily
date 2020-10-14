package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils; 

import org.apache.commons.lang3.StringUtils;
import java.math.RoundingMode;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.cmall.familyhas.api.input.APiCreateOrderInput;
import com.cmall.familyhas.api.result.APiCreateOrderResult;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.service.CouponService;
import com.cmall.familyhas.service.MemberLevelService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.service.KaolaOrderService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.cmall.groupcenter.util.StringHelper;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.model.OrderDetail;
import com.cmall.ordercenter.model.OrderItemList;
import com.cmall.ordercenter.model.UserInfo;
import com.cmall.ordercenter.model.api.GiftVoucherInfo;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.DistributionInfoModel;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.TeslaModelJJG;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.xmassystem.util.XSSUtils;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.enumer.EKvTop;
import com.srnpr.zapdata.kvdo.KvTop;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 惠家有创建订单接口 shiyz
 * @Edit :zhouenzhi
 * @date :2019-05-16 14:19:00
 * @desc :拼团订单创建时，团中带该团失效时间。需要从拼团活动中查询出该拼团活动的拼团时效。根据当前时间算出失效时间
 */
public class TeslaCreateOrder extends
		RootApiForToken<APiCreateOrderResult, APiCreateOrderInput> {

	public APiCreateOrderResult Process(APiCreateOrderInput inputParam,
			MDataMap mRequestMap) {
		
		APiCreateOrderResult result = new APiCreateOrderResult();

		String userCod = getUserCode();
		
		// 删除表情符号
		inputParam.setRemark(StringHelper.deleteEmoji(inputParam.getRemark()));
		// 如果包含非法字符则清空内容
		if(XSSUtils.hasXSS(inputParam.getRemark())) {
			inputParam.setRemark("");
		}
		
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.setCpsCode(inputParam.getCpsCode());
		teslaXOrder.setOutChannelId(inputParam.getOutChannelId());
		teslaXOrder.setLiveRoomID(inputParam.getLiveRoomID());
		
		//添加是否内购
		teslaXOrder.setIsPurchase(inputParam.getIsPurchase());
		//添加分销信息
		teslaXOrder.setProductSharedInfos(inputParam.getProductSharedInfos());
		teslaXOrder.getStatus().setExecStep(ETeslaExec.Create);
		teslaXOrder.setIsOriginal(inputParam.getIsOriginal());
	    teslaXOrder.setCollageFlag(inputParam.getCollageFlag());
	    teslaXOrder.setCollageCode(inputParam.getCollageCode());
	    teslaXOrder.setActivityCode(inputParam.getActivityCode());
	    teslaXOrder.setRedeemCode(inputParam.getRedeemCode());
	    teslaXOrder.setTreeCode(inputParam.getTreeCode());
	    teslaXOrder.setEventCode(inputParam.getEventCode());
	    teslaXOrder.setHuDongCode(inputParam.getHuDongCode());
	    if(StringUtils.isNotBlank(inputParam.getBlockSign())){
	    	teslaXOrder.setBlockSign(inputParam.getBlockSign());
	    }
	    teslaXOrder.setOrderPageSouce(inputParam.getOrderPageSouce());
		
		//先校验微公社余额
//		GroupAccountInfoResult groupResult = new GroupAccountService().getAccountInfo(new GroupAccountService().getAccountCodeByMemberCode(getUserCode()));
		if(inputParam.getWgsUseMoney()>0){
			// 使用了微公社余额的情况下才去调用接口
			GroupAccountInfoResult groupResult= new GroupAccountService().getAccountInfoByApi(getUserCode());
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
		
		// 商品信息
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
		
		teslaXOrder.setQrcode(StringUtils.trimToEmpty(inputParam.getQrcode()));
		
		checkOrderSource(teslaXOrder);
		
		// 订单详细信息
		List<OrderItemList> orderItemList = new ArrayList<OrderItemList>();//考拉订单信息		
		String sSql = "select pc_productinfo.product_name,pc_productinfo.product_code,pc_skuinfo.sku_code,pc_productinfo.sell_productcode as kaola_productcode,pc_skuinfo.sell_productcode as kaola_skucode,pc_skuinfo.cost_price from pc_productinfo" + 
					  " left join pc_skuinfo on pc_productinfo.product_code = pc_skuinfo.product_code" +
					  " where pc_productinfo.product_code =:product_code and pc_skuinfo.sku_code =:sku_code and pc_productinfo.small_seller_code =:small_seller_code";
		String kaolaProduct = "";
		
		//558添加加价购活动有效校验提示
		ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
		ShoppingCartCacheInfo info = serviceForCache.queryShopCart(getOauthInfo()==null?null:getOauthInfo().getUserCode(),new ArrayList<ShoppingCartGoodsInfoForAdd>());
	    boolean noStoreFlag = false;
		if("1".equals(inputParam.getIfFromShopCar())&&info.getJJGList().size()>0) {
	    	for (TeslaModelJJG jjg : info.getJJGList()) {
	    		result = validateJJG(jjg,inputParam,result,teslaXOrder);
	    		if(result.getResultCode()==916425001) {noStoreFlag=true;}
			}
	    	if(!StringUtils.equals("", result.getJjgTips())) {
	    		//针对加价购校验的提示错误码
	    		result.setResultCode(973900001);
	    		result.setJjgTips(result.getJjgTips());
	    	}
	    }
		
		for (GoodsInfoForAdd goodsInfo : infoForAdds) {
			//544需求 根据商品编号查询商品是否是LD品
			Map<String, Object> prodInfoMap = DbUp.upTable("pc_productinfo").dataSqlOne(
					"SELECT pc_productinfo.small_seller_code,dlr_charge FROM pc_productinfo WHERE pc_productinfo.product_code =:product_code", 
						new MDataMap("product_code", goodsInfo.getProduct_code()));
			//560需求  厂商收款商品不能在线支付
			if("Y".equals(prodInfoMap.get("dlr_charge"))&&inputParam.getPay_type().equals("449716200001")){
				result.setResultCode(-1);
				result.setResultMessage("商品["+goodsInfo.getProduct_code()+"]不支持在线支付");
				return result;
			}
			if("SI2003".equals(prodInfoMap.get("small_seller_code"))) {
				// 先去查一下最小起订量,最小起订量如果＞5则不提示
				Map<String, Object> skuInfoMap = DbUp.upTable("pc_skuinfo").dataSqlOne(
						"SELECT pc_skuinfo.mini_order FROM pc_skuinfo WHERE pc_skuinfo.sku_code =:sku_code", 
							new MDataMap("sku_code", goodsInfo.getSku_code()));
				if(null != skuInfoMap) {					
					int mini_order = (int) skuInfoMap.get("mini_order");
					if(mini_order <= 5) {					
						//LD品一次购买最多到5件
						if(goodsInfo.getSku_num() > 5) {
							result.setResultCode(-1);
							result.setResultMessage("商品["+goodsInfo.getProduct_code()+"]在线最多可以够买5件，如需购买更多数量请打电话购买");
							return result;
						}
					}
				}
			}
			
			TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
			orderDetail.setProductCode(goodsInfo.getProduct_code());
			orderDetail.setSkuCode(goodsInfo.getSku_code());
			orderDetail.setSkuNum(goodsInfo.getSku_num());
			orderDetail.setIsSkuPriceToBuy(goodsInfo.getFlg());
			orderDetail.setIntegralDetailId(goodsInfo.getIntegralDetailId());
			orderDetail.setIfJJGFlag(goodsInfo.getIfJJGFlag());
			orderDetail.setFxrcode(goodsInfo.getFxrcode());
			orderDetail.setShareCode(goodsInfo.getShareCode());
			orderDetail.setTgzShowCode(goodsInfo.getTgzShowCode());
			orderDetail.setTgzUserCode(goodsInfo.getTgzUserCode());
			//设置积分商城标示
			if(!"".equals(goodsInfo.getIntegralDetailId())) {
				teslaXOrder.setPointShop(true);
			}			
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
			teslaXOrder.getUse().setCoupon_codes(new ArrayList<String>(new HashSet<String>(inputParam.getCoupon_codes())));
		}
		
		teslaXOrder.getUse().setHjyBean(BigDecimal.valueOf(inputParam.getUsedBeanTotal()));//添加惠豆
		teslaXOrder.getUse().setIntegral(inputParam.getUsedIntegralTotal());
		teslaXOrder.getUse().setCzj_money(inputParam.getCzj_money());//添加储值金
		//添加版本兼容5.2.0（不含）以上版本支出暂存款  -rhb
		if(getApiClient() != null && AppVersionUtils.compareTo(getApiClient().get("app_vision"), "5.2.0") > 0) {
			teslaXOrder.getUse().setZck_money(inputParam.getZck_money());//添加暂存款
		}
		// 使用惠币
		teslaXOrder.getUse().setHjycoin(inputParam.getUsedhjycoinTotal());
		
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
		
		//一元购订单信息
		teslaXOrder.getYyg().setYygMac(inputParam.getYygMac());
		teslaXOrder.getYyg().setYygOrderNo(inputParam.getYygOrderNo());
		teslaXOrder.getYyg().setYygPayMoney(inputParam.getYygPayMoney());
		
		//直播订单参数
		teslaXOrder.setRoomId(inputParam.getRoomId());
		teslaXOrder.setAnchorId(inputParam.getAnchorId());
		
		if("449715200016".equals(inputParam.getOrder_type())&&(StringUtils.isBlank(inputParam.getRoomId())||StringUtils.isBlank(inputParam.getAnchorId()))){
			result.setResultCode(916421165);
			result.setResultMessage(bInfo(916421165));
			return result;
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
		//存在失效的加价购活动，则再次进行确认处理
		if(result.getResultCode()==973900001) {
			String[] invalidatejjgEventNameCode = result.getJjgTips().split(",");
			String eventNames = "";
			for (String string : invalidatejjgEventNameCode) {
				String[] split = string.split("_");
				if(StringUtils.isBlank(eventNames)) {
					eventNames = split[0];
				}else {
					eventNames = eventNames+","+split[0];
				}
			}
			result.setResultMessage("因活动["+eventNames+"]已结束，该活动下已选商品已失效，请重新选择!");
			return result;
		}
		//执行创建订单
		TeslaXResult reTeslaXResult = new ApiConvertTeslaService()
				.ConvertOrder(teslaXOrder);
		if (reTeslaXResult.upFlagTrue()) {
			createKaolaOrder(teslaXOrder);
			createCollageTeam(teslaXOrder);
            clearShopCarJJGProducts(inputParam);
			doOthershing(teslaXOrder);
			//同步统计分销额
			updateDistributionInfos(teslaXOrder);
			
			// 下单成功判断是不是app直播间下单,如果是记录下单数据
			String appLiveRoomId = inputParam.getAppLiveRoomId();
			if(StringUtils.isNotBlank(appLiveRoomId)) {
				createLiveRoomOrder(appLiveRoomId,userCod,teslaXOrder);
			}
			
			Map weChatMap = new HashMap();
			result.setOrder_code(teslaXOrder.getUorderInfo().getBigOrderCode());
			
		}else {
			//订单创建失败要解锁 下单太频繁的问题除外 -rhb 20181113
			if(reTeslaXResult.getResultCode()!=963902124) {
				KvTop.upFactory(EKvTop.LockCode).del(teslaXOrder.getUorderInfo().getBuyerCode());
			}
			//存在无库存商品 需要将无库存商品信息返回
			if(reTeslaXResult.getResultCode() == 916425001) {
				result.getNoStockOrFailureGoods().addAll(teslaXOrder.getNoStockOrFailureGoods());
			}

			if(noStoreFlag&&"449747430023".equals(inputParam.getChannelId())) {
				result.setResultCode(916425001);
				result.setResultMessage("部分商品无货，请重新结算");
				return result;
			}
			
				result.setResultCode(reTeslaXResult.getResultCode());
				result.setResultMessage(reTeslaXResult.getResultMessage());
			
		}
		return result;
	}
	
	/**
	 * 记录app直播间下单数据
	 * @param appLiveRoomId
	 * @param userCod
	 * @param teslaXOrder
	 */
	private void createLiveRoomOrder(String appLiveRoomId, String userCod, TeslaXOrder teslaXOrder) {
		MDataMap liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",appLiveRoomId,"is_delete","0");
		if(liveRoom != null) {
			List<TeslaModelOrderInfo> sorderInfo = teslaXOrder.getSorderInfo();
			if(sorderInfo != null && sorderInfo.size() > 0) {
				for (TeslaModelOrderInfo teslaModelOrderInfo : sorderInfo) {
					// 记录直播间下单
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("live_room_id", appLiveRoomId);
					mDataMap.put("order_code", teslaModelOrderInfo.getOrderCode());
					mDataMap.put("order_money", teslaModelOrderInfo.getOrderMoney().toString());
					DbUp.upTable("lv_live_room_orders").dataInsert(mDataMap);
				}
			}
			List<TeslaModelOrderDetail> orderDetails = teslaXOrder.getOrderDetails();
			if(orderDetails != null && orderDetails.size() > 0) {
				for (TeslaModelOrderDetail teslaModelOrderDetail : orderDetails) {
					// 先查询用户是否已经在该直播间买过这款商品
					MDataMap one = DbUp.upTable("lv_live_room_product_statistics").one("live_room_id",appLiveRoomId,"product_code",teslaModelOrderDetail.getProductCode(),"member_code",userCod,"behavior_type", "449748620002");
					if(one != null) {
						// 买过,更新下单数
						int num = MapUtils.getIntValue(one, "num");
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("uid", MapUtils.getString(one, "uid"));
						mDataMap.put("num", num+1+"");
						DbUp.upTable("lv_live_room_product_statistics").dataUpdate(mDataMap, "num", "uid");
					}else {		
						// 没买过,记录下单信息
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("live_room_id", appLiveRoomId);
						mDataMap.put("product_code", teslaModelOrderDetail.getProductCode());
						mDataMap.put("member_code", userCod);
						mDataMap.put("behavior_type", "449748620002");
						mDataMap.put("num", "1");
						DbUp.upTable("lv_live_room_product_statistics").dataInsert(mDataMap);
					}
				}
			}
		}
	}

	private void clearShopCarJJGProducts(APiCreateOrderInput inputParam) {
		// TODO Auto-generated method stub
		if("1".equals(inputParam.getIfFromShopCar())) {
			ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
			serviceForCache.clearJJGForShopCart(getOauthInfo().getUserCode());	
		}
		
	}

	
	private APiCreateOrderResult validateJJG(TeslaModelJJG jjg, APiCreateOrderInput inputParam,
			APiCreateOrderResult result, TeslaXOrder teslaXOrder) {
		// TODO Auto-generated method stub
		String eventCode = jjg.getEventCode();
		PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
		plusModelEventQuery.setCode(eventCode);
		MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code", eventCode);
		String sysDateTimeString = DateUtil.getSysDateTimeString();
		//int dataCount = DbUp.upTable("sc_event_info").dataCount("event_code='"+eventCode+"' and event_status='4497472700020002' and begin_time<='"+sysDateTimeString+"' and end_time>'"+sysDateTimeString+"'", null);
		if(!StringUtils.equals(eventInfo.get("event_status"), "4497472700020002")||!(DateUtil.compareDateTime(eventInfo.get("begin_time"),sysDateTimeString)&&DateUtil.compareDateTime(sysDateTimeString,eventInfo.get("end_time")))) {
			//换购活动失效
			if(StringUtils.isBlank(result.getJjgTips())) {
				result.setJjgTips(eventInfo.get("event_name")+"_"+eventInfo.get("event_code"));
			}else {
				result.setJjgTips(result.getJjgTips()+","+eventInfo.get("event_name")+"_"+eventInfo.get("event_code"));
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
					PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCodeForJJG(skuCode, 
							teslaXOrder.getUorderInfo().getBuyerCode(),teslaXOrder.getIsMemberCode());
					if( plusModelSkuInfo.getMaxBuy()>0&& (StringUtils.isBlank(plusModelSkuInfo.getSaleYn()) || plusModelSkuInfo.getSaleYn().equals("Y"))) {
						continue;
					}else{
						result.setResultCode(916425001);
					};
			}
		}
		
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
		
		//回写ld使用的礼金券明细（hjy订单DD编号、礼金券编码）
		List<TeslaModelOrderPay> couponPayInfo = teslaXOrder.getOcOrderPayList();
		List<GiftVoucherInfo> reWriteLD = new ArrayList<GiftVoucherInfo>();
		for(TeslaModelOrderPay coupon : couponPayInfo) {
			if(coupon.getPayType().equals("449746280002")) {
				String couponCode = coupon.getPaySequenceid().toString();
				//如果是礼金券，取消订单则退还使用的礼金券
				Map<String, Object> couponmap = DbUp.upTable("oc_coupon_info").dataSqlOne("select ct.money_type from oc_coupon_type ct,oc_coupon_info ci where ci.coupon_type_code = ct.coupon_type_code and ci.coupon_code = :couponCode", new MDataMap("couponCode",couponCode));
				if(couponmap != null && "449748120003".equals(couponmap.get("money_type"))) {
					GiftVoucherInfo info = new GiftVoucherInfo();
					info.setLj_code(couponCode);
					info.setHjy_ord_id(coupon.getOrderCode());
					reWriteLD.add(info);
				}
			}			
		}
		if(reWriteLD != null && reWriteLD.size() > 0) {
			new CouponService().reWriteGiftVoucherToLD(reWriteLD, "U");
		}		
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
					jobMap.put("remark", "TeslaCreateOrder -> createKaolaOrder");
					jobMap.put("exec_number", "0");
					DbUp.upTable("za_exectimer").dataInsert(jobMap);
				}
			}
		}
	}
	
	/**
	 * 判断是否为拼团单，如果为拼团单，则创建团信息
	 * @param teslaXOrder
	 */
	private void createCollageTeam(TeslaXOrder teslaXOrder) {
		if("1".equals(teslaXOrder.getCollageFlag())) {
			String memberType = "449748310002";
			String collageCode = teslaXOrder.getCollageCode();//拼团编码
			
			String eventCode = "";
			// 取拼团活动编号
			for(TeslaModelOrderActivity act : teslaXOrder.getActivityList()) {
				if("4497472600010024".equals(act.getActivityType())){
					eventCode = act.getActivityCode();
					break;
				}
			}
			
			// 未取到活动编号则忽略拼团标识
			if(StringUtils.isBlank(eventCode)) {
				return;
			}
			
			if("".equals(collageCode)) {//当前用户为发起人
				memberType = "449748310001";
				collageCode = WebHelper.upCode("PT");
				String expireTime = "";
				if(!StringUtils.isEmpty(eventCode)) {
					/**
					 * 查询拼团时效以及活动结束时间
					 */
					LoadEventInfo load = new LoadEventInfo();
					PlusModelEventQuery tQuery = new PlusModelEventQuery();
					tQuery.setCode(eventCode);
					PlusModelEventInfo eventInfo = load.upInfoByCode(tQuery);
					String endTime = eventInfo.getEndTime();//获取活动结束时间
					Integer timeliness = eventInfo.getCollageTimeLiness();//获取拼团时效
					if(timeliness == -1||timeliness==null) {
						expireTime = endTime;
					}else {
						expireTime = DateUtil.addDateMinut(DateUtil.getSysDateTimeString(), timeliness);
					}
					if(!DateUtil.compareDateTime(expireTime, endTime)) {
						expireTime = endTime;
					}
					DbUp.upTable("sc_event_collage").dataInsert(new MDataMap("collage_code", collageCode, "event_code", eventCode, "collage_status", "449748300001", 
							"create_time", DateUtil.getSysDateTimeString(),"expire_time",expireTime));
				}
			}
			List<TeslaModelShowGoods> showGoods = teslaXOrder.getShowGoods();
			String productCode = "";
			if(showGoods != null && showGoods.size()>0) {
				productCode = showGoods.get(0).getProductCode();
			}
			MDataMap itemMap = new MDataMap();
			itemMap.put("collage_code", collageCode);//团编码
			itemMap.put("collage_member", getUserCode());//member_code
			itemMap.put("collage_ord_code", teslaXOrder.getOrderDetails().get(0).getOrderCode());//订单编号
			itemMap.put("collage_member_type", memberType);//拼团人类型
			itemMap.put("is_confirm", "449748320001");//待确认
			itemMap.put("collage_time", DateUtil.getSysDateTimeString());//拼团时间
			itemMap.put("product_code", productCode);//商品编号
			DbUp.upTable("sc_event_collage_item").dataInsert(itemMap);
			
			for(TeslaModelOrderInfo teslaModelOrderInfo : teslaXOrder.getSorderInfo()) {
				if(teslaModelOrderInfo.getDueMoney().compareTo(BigDecimal.ZERO) <= 0 || "4497153900010002".equals(teslaModelOrderInfo.getOrderStatus())) {
					doCollageSomething(teslaModelOrderInfo.getOrderCode());
				}
			}
		}
	}
	
	private void doCollageSomething(String orderCode) {
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
						String expireTime = "";
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
								"create_time", DateHelper.upNow(),"expire_time",expireTime));
						DbUp.upTable("sc_event_collage_item").dataUpdate(new MDataMap("zid", MapUtils.getString(collageItemMap, "zid", ""), "collage_code", newCollageCode, "is_confirm", "449748320002", 
								"re_collage", "1", "collage_member_type", "449748310001"), "collage_code,is_confirm,re_collage,collage_member_type", "zid");
					}
				}
			}
		}
	}
	/**
	 * 更新下单商品绑定的分销信息
	 * @param teslaOrder
	 */
	private static void updateDistributionInfos(TeslaXOrder teslaOrder) {
		// TODO Auto-generated method stub
		    String memberCode=teslaOrder.getUorderInfo().getBuyerCode();
		    List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();
		    List<TeslaModelOrderInfo> sorderInfo = teslaOrder.getSorderInfo();
		    //记录订单状态
			Map<String,String> paraMap  = new ConcurrentHashMap<String,String>();
		    for (TeslaModelOrderInfo teslaModelOrderInfo : sorderInfo) {
		    	paraMap.put(teslaModelOrderInfo.getOrderCode(), teslaModelOrderInfo.getOrderStatus());
			}
			String currentTime = DateUtil.getSysDateTimeString();
			for (TeslaModelOrderDetail orderDetail : orderDetails) {
				//获取最新关联的分销人
				List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_distribution_info").dataSqlList("select * from lc_distribution_info where distribution_junior_member_id='"+memberCode+"' and distribution_junior_member_id!=distribution_member_id and distribution_product_code='"+orderDetail.getProductCode()+"' order by zid desc", null);
				if(dataSqlList!=null&&dataSqlList.size()>0) {
				//更新分销信息
				Map<String, Object> map = dataSqlList.get(0);
				String create_time = map.get("create_time").toString();
				String distribution_member_id = map.get("distribution_member_id").toString();
				String endTime = DateUtil.addDateHour(create_time, 24);
				//String endTime = DateUtil.addDateSec(create_time, 10*60);
        		if(DateUtil.compareDateTime(currentTime, endTime)&&paraMap.containsKey(orderDetail.getOrderCode())) {
    				//直接在订单明细表里进行统计,不再在订单表里关联分销人信息了
        			DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("distribution_member_id",distribution_member_id,"order_code",orderDetail.getOrderCode()), "distribution_member_id", "order_code");
        			/*Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
        		    if(dataSqlOne!=null&&"4497153900010002".equals(paraMap.get(orderDetail.getOrderCode()))) {
        		    	Integer share_order_num = Integer.parseInt(dataSqlOne.get("share_order_num").toString());
        		    	BigDecimal share_sales_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("share_sales_value").toString()));
        		    	share_order_num=share_order_num+1;
        		    	BigDecimal skuPrice = orderDetail.getSkuPrice();
        		    	int skuNum = orderDetail.getSkuNum();
        		    	BigDecimal allSkuPrice = skuPrice.multiply(BigDecimal.valueOf(Double.parseDouble(skuNum+"")));
        		    	share_sales_value=share_sales_value.add(allSkuPrice);
        		    	share_sales_value.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        		    	DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("share_order_num",share_order_num.toString(),"share_sales_value",share_sales_value.toString(),"distribution_member_id",distribution_member_id), "share_order_num,share_sales_value", "distribution_member_id");
            		  }*/
        			//此处做一个下单的明细记录
        			 if("4497153900010002".equals(paraMap.get(orderDetail.getOrderCode()))) {
        				 MDataMap mDataMap = new MDataMap();
        				 mDataMap.put("uid", WebHelper.upUuid());
        				 mDataMap.put("distribution_member_id", distribution_member_id);
        				 mDataMap.put("product_code", orderDetail.getProductCode());
        				 mDataMap.put("distribution_junior_member_id", memberCode);
        				 mDataMap.put("order_code",orderDetail.getOrderCode() );
        				 mDataMap.put("create_time", currentTime);
        				 DbUp.upTable("oc_distribution_order").dataInsert(mDataMap);
        			 }
	        	}
			}	
		}
	}
	
	/**
	 * 兼容IPTV扫码，检查一下订单来源
	 * @param teslaXOrder
	 */
	private void checkOrderSource(TeslaXOrder teslaXOrder) {
		String s = StringUtils.trimToEmpty(teslaXOrder.getUorderInfo().getOrderSource());
		// 未知来源渠道统一设置为微信商城
		if(DbUp.upTable("sc_define").count("define_code", s, "parent_code", "44971519") == 0) {
			teslaXOrder.getUorderInfo().setOrderSource("449715190006");
		}
		
		
	}
	
}
