package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.model.Activity;
import com.cmall.familyhas.model.BillInfo;
import com.cmall.familyhas.model.FamilyShopCart;
import com.cmall.familyhas.model.Gift;
import com.cmall.familyhas.model.GoodsInfoForConfirm;
import com.cmall.familyhas.model.GoodsInfoForQuery;
import com.cmall.familyhas.model.GoodsInfoForQueryDisable;
import com.cmall.familyhas.model.OrderSort;
import com.cmall.groupcenter.baidupush.core.utility.DismantlOrderUtil;
import com.cmall.groupcenter.groupdo.GroupConst;
import com.cmall.groupcenter.homehas.GetGoodGiftList;
import com.cmall.groupcenter.homehas.model.ModelGoodGiftInfo;
import com.cmall.groupcenter.model.GroupLevelInfo;
import com.cmall.groupcenter.service.GroupPayService;
import com.cmall.groupcenter.support.ReckonOrderSupport;
import com.cmall.groupcenter.third.model.GroupPayInput;
import com.cmall.groupcenter.third.model.GroupPayResult;
import com.cmall.groupcenter.third.model.GroupRefundInput;
import com.cmall.groupcenter.third.model.GroupRefundResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.common.OrderConst;
import com.cmall.ordercenter.familyhas.active.product.ActiveForSource;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.model.CouponInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.OcOrderActivity;
import com.cmall.ordercenter.model.OcOrderPay;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.model.OrderAddress;
import com.cmall.ordercenter.model.OrderDetail;
import com.cmall.ordercenter.model.api.GiftVoucherInfo;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.ordercenter.service.FlashsalesSkuInfoService;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.util.CouponUtil;
import com.cmall.productcenter.model.FlashsalesSkuInfo;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.service.ProductStoreService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.service.StoreService;
import com.cmall.systemcenter.util.StringUtility;
import com.srnpr.xmasorder.model.DistributionInfoModel;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


/**
 * 
 * 项目名称：familyhas 类名称：ShopCartService 类描述： 创建人：xiegj 创建时间：2014-09-16 上午11:03:25
 * 
 * @version 1.0
 * 
 */
public class ShopCartService extends BaseClass {

	/**
	 * 添加商品进入购物车
	 * 
	 */
	public RootResult addSkuToShopCart(List<FamilyShopCart> list) {
		RootResult rootResult = new RootResult();
		try {
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					FamilyShopCart cart = list.get(i);
					ProductService service = new ProductService();
					// 判断此商品是否存在
					PcProductinfo product = service.getskuinfo(
							cart.getSku_code(), cart.getProduct_code());
					if (product == null) {
						deleteSkuForShopCart(cart.getBuyer_code(),
								cart.getSku_code());// 删除已不存在的商品
						continue;
					}
					// 判断购物车中是否存在本商品
					if (cart != null) {
						MDataMap insert = new MDataMap();
						insert.put("sku_num", String.valueOf(cart.getSku_num()));
						insert.put("shop_type",
								String.valueOf(cart.getShop_type()));
						insert.put("buyer_code", cart.getBuyer_code());
						insert.put("sku_code", cart.getSku_code());
						MDataMap one = DbUp.upTable("oc_shopCart").one(
								"buyer_code", cart.getBuyer_code(), "sku_code",
								cart.getSku_code());
						insert.put("choose_flag", cart.getChooseFlag());
						if (one == null && cart.getSku_num() > 0) {// 不存在本商品的话新增
							insert.put("account_flag",
									String.valueOf(cart.getAccount_flag()));
							insert.put("add_time",
									DateUtil.getSysDateTimeString());
							insert.put("create_time",
									DateUtil.getSysDateTimeString());
							DbUp.upTable("oc_shopCart").dataInsert(insert);
						} else if (one != null && cart.getSku_num() > 0) {// 已存在本商品的话更新本商品
							insert.put("account_flag", one.get("account_flag"));
							insert.put("sku_num",
									String.valueOf(cart.getSku_num()));
							insert.put("update_time",
									DateUtil.getSysDateTimeString());
							DbUp.upTable("oc_shopCart").dataUpdate(insert, "",
									"buyer_code,sku_code");
						} else if (cart.getSku_num() == 0 && one != null) {
							deleteSkuForShopCart(cart.getBuyer_code(),
									cart.getSku_code());// 删除的商品
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			rootResult.setResultCode(916401102);
			rootResult.setResultMessage(bInfo(916401102));
		}
		return rootResult;
	}

	/**
	 * 获取用户购物车中的所有商品
	 * 
	 */
	public Map<String, Object> getSkuShopCart(String sellerCode,
			String buyerCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<GoodsInfoForQuery> list = new ArrayList<GoodsInfoForQuery>();
		int AccountAll = 0;// 商品总个数
		int disableAccount = 0;// 无效商品总个数
		List<MDataMap> skuLi = DbUp.upTable("oc_shopCart").queryAll("",
				"create_time DESC", "", new MDataMap("buyer_code", buyerCode));
		if (skuLi != null && !skuLi.isEmpty()) {
			for (int i = 0; i < skuLi.size(); i++) {
				MDataMap map = skuLi.get(i);
				GoodsInfoForQuery gifq = new GoodsInfoForQuery();
				ProductService service = new ProductService();
				PcProductinfo product = service.getskuinfo(map.get("sku_code"),
						"");
				if (product == null) {
					deleteSkuForShopCart(buyerCode, map.get("sku_code"));// 删除已不存在的商品
					continue;
				}
				AccountAll = AccountAll + Integer.valueOf(map.get("sku_num"));
				// 判断是否下架
				if (product.getProductStatus() == null
						|| !"4497153900060002".equals(product
								.getProductStatus())) {// 已下架
					disableAccount = disableAccount
							+ Integer.valueOf(map.get("sku_num"));
					gifq.setFlag_product("0");
				}
				// 判断库存是否足够
				int maxStock = (new StoreService()).getStockNumByStore(map
						.get("sku_code"));

				if (VersionHelper.checkServerVersion("3.5.11.31")
						&& (sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS) || sellerCode
								.equals(MemberConst.MANAGE_CODE_HPOOL))) {// 后台逻辑代码版本控制
					maxStock = (new ProductStoreService()).getStockNumBySku(map
							.get("sku_code"));
				}

				if (Integer.valueOf(map.get("sku_num")) > maxStock) {
					gifq.setFlag_stock("0");
				}
				gifq.setSku_stock(maxStock);
				gifq.setProduct_code(product.getProductCode());
				gifq.setArea_code("");
				gifq.setPic_url(product.getMainPicUrl());
				gifq.setSku_code(map.get("sku_code"));
				gifq.setSku_name(product.getProductSkuInfoList().get(0)
						.getSkuName());
				gifq.setSku_num(Integer.valueOf(map.get("sku_num")));
				boolean ngFlag = new ActiveForSource()
						.checkIsVipSpecialForFamilyhas(buyerCode);
				boolean ngsjFlag = new ActiveForSource()
						.checkIsVipSpecialDayForFamilyhas();
				boolean sgFlag = service.checkSkuIsFlash(gifq.getSku_code())
						.get(gifq.getSku_code()) > 0;
				PlusModelSkuInfo skuSupport = new PlusSupportProduct()
						.upSkuInfoBySkuCode(gifq.getSku_code(), buyerCode);
				if (sgFlag) {
					// 判断此商品是否参加闪购活动
					Map<String, Object> kc = service.getSkuActivity(
							map.get("sku_code"), sellerCode);
					FlashsalesSkuInfo flashsalesSkuInfo = (FlashsalesSkuInfo) kc
							.get("flashsalesObj");
					gifq.setLimit_order_num(flashsalesSkuInfo
							.getPurchase_limit_order_num().intValue());// 闪购商品的限购数量
					gifq.setSku_price(flashsalesSkuInfo.getVipPrice()
							.doubleValue());// 闪购商品价格
					gifq.setSales_info(bConfig("familyhas.ActivitySgInfo"));
					gifq.setSales_type(bConfig("familyhas.ActivitySgName"));
					Activity activity = new Activity();
					activity.setActivity_name(bConfig("familyhas.ActivitySgName"));
					activity.setActivity_info(bConfig("familyhas.ActivitySgInfo"));
					gifq.getActivitys().add(activity);
				} else if (skuSupport != null
						&& StringUtility.isNotNull(skuSupport.getEventCode())) {// 参加促销活动
					gifq.setSku_price(skuSupport.getSellPrice().doubleValue());
					if (StringUtility.isNotNull(skuSupport.getSellNote())) {
						Activity activity = new Activity();
						activity.setActivity_name(skuSupport.getSellNote());
						gifq.getActivitys().add(activity);
					}
					gifq.setLimit_order_num(new Long(skuSupport.getMaxBuy()).intValue());
				} else if (ngFlag && ngsjFlag) {
					gifq.setLimit_order_num(Integer
							.valueOf(bConfig("familyhas.ng_product_num")));// 内购商品限购数量
					List<String> prorr = new ArrayList<String>();
					prorr.add(gifq.getProduct_code());
//					gifq.setSku_price(service
//							.getMinProductActivityNew(prorr, "4497469400050001")
//							.get(gifq.getProduct_code()).doubleValue());// 商品价格
					
					if (VersionHelper.checkServerVersion("3.5.95.55")) {		//ligj获取商品最低价格方法
						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						skuQuery.setCode(StringUtils.join(prorr,","));
						skuQuery.setMemberCode(buyerCode);
						gifq.setSku_price(new ProductPriceService().
								getProductMinPrice(skuQuery).get(gifq.getProduct_code()).doubleValue());// 获取商品最低销售价格
					}else{
						gifq.setSku_price(service
								.getMinProductActivityNew(prorr, "4497469400050001")
								.get(gifq.getProduct_code()).doubleValue());// 商品价格
					}
				} else {
					gifq.setLimit_order_num(Integer
							.valueOf(bConfig("familyhas.pt_product_num")));// 普通商品的限购数量
					gifq.setSku_price(product.getProductSkuInfoList().get(0)
							.getSellPrice().doubleValue());// 普通商品价格
				}
				gifq.setSku_property(reProperties(map.get("sku_code"), product
						.getProductSkuInfoList().get(0).getSkuValue()));
				int isIncludeGift = Integer.valueOf((new ProductService())
						.getProductSomeInfo(product.getProductCode())
						.get(product.getProductCode()).get("isIncludeGift"));
				if (isIncludeGift == 1) {
					gifq.getOtherShow()
							.add(bConfig("familyhas.ActivityZpName"));
				}
				gifq.setMini_order(product.getProductSkuInfoList().get(0).getMiniOrder());
//				gifq.setFlagTheSea(AppConst.MANAGE_CODE_KJT.equals(product.getSmallSellerCode())||AppConst.MANAGE_CODE_MLG.equals(product.getSmallSellerCode())
//						||AppConst.MANAGE_CODE_QQT.equals(product.getSmallSellerCode())
//						||AppConst.MANAGE_CODE_SYC.equals(product.getSmallSellerCode())
//						||AppConst.MANAGE_CODE_CYGJ.equals(product.getSmallSellerCode())?"1":"0");
				gifq.setFlagTheSea(new PlusServiceSeller().isKJSeller(product.getSmallSellerCode())?"1":"0");
				gifq.setChooseFlag(map.get("choose_flag"));
				list.add(gifq);
			}
		}
		result.put("list", list);
		result.put("AccountAll", AccountAll);
		result.put("disableAccount", disableAccount);
		return result;
	}

	/**
	 * 获取用户购物车中的失效商品
	 * 
	 */
	public Map<String, Object> getDisableSkuShopCart(String sellerCode,
			String buyerCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<GoodsInfoForQueryDisable> goodsList = new ArrayList<GoodsInfoForQueryDisable>();
		StringBuffer error = new StringBuffer();
		List<MDataMap> li = DbUp.upTable("oc_shopCart").queryByWhere(
				"buyer_code", buyerCode);
		if (li == null || li.isEmpty()) {
			result.put("error", error.toString());
			result.put("list", goodsList);
			return result;
		}
		for (int i = 0; i < li.size(); i++) {
			String skuCode = li.get(i).get("sku_code");
			// String area_code = li.get(i).get("area_code");
			// 判断是否已下架商品
			ProductService service = new ProductService();
			// 判断此商品是否存在
			PcProductinfo product = service.getskuinfo(skuCode, "");
			if (product != null && product.getProductSkuInfoList() != null
					&& product.getProductSkuInfoList().get(0) != null) {
				// 判断是否下架
				String productStatus = product.getProductStatus();
				// 判断库存是否不足
				Map<String, Object> kc = service.getSkuActivity(skuCode,
						sellerCode);
				if (productStatus == null
						|| !"4497153900060002".equals(productStatus)
						|| (kc.containsKey("flashsalesObj") && kc
								.get("flashsalesObj") != null)) {// 已下架或者是闪购商品
					GoodsInfoForQueryDisable gfd = new GoodsInfoForQueryDisable();
					gfd.setSku_code(skuCode);
					gfd.setSku_name(product.getProductSkuInfoList().get(0)
							.getSkuName());
					gfd.setSku_num(Integer.valueOf(li.get(i).get("sku_num")
							.toString()));
					gfd.setPic_url(product.getMainPicUrl());
					// gfd.setArea_code(area_code);
					gfd.setSku_property(reProperties(skuCode, product
							.getProductSkuInfoList().get(0).getSkuValue()));
					goodsList.add(gfd);
				}
			}
		}
		result.put("error", error.toString());
		result.put("list", goodsList);
		return result;
	}

	/**
	 * 删除购物车中的商品
	 * 
	 */
	public boolean deleteSkuForShopCart(String buyer_code, String skuCode) {
		boolean flag = false;
		try {
			if (buyer_code != null && skuCode != null && !"".equals(skuCode)
					&& !"".equals(buyer_code)) {
				DbUp.upTable("oc_shopCart").delete("buyer_code", buyer_code,
						"sku_code", skuCode);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 统一确认订单
	 * 
	 */

	public Map<String, Object> orderConfirm(MDataMap things,
			List<GoodsInfoForAdd> goods, OrderAddress address, List<String> couponCodes) {
		String buyerCode = things.get("buyerCode");
		String sellerCode = things.get("sellerCode");
		String orderType = things.get("orderType");
		Map<String, Object> result = new HashMap<String, Object>();
		List<GoodsInfoForConfirm> confirms = new ArrayList<GoodsInfoForConfirm>();
		BigDecimal payMoney = new BigDecimal(0.00);// 实付款
		BigDecimal cashBack = new BigDecimal(0.00);// 返现
		BigDecimal costMoney = new BigDecimal(0.00);// 商品总金额
		BigDecimal firstCheap = new BigDecimal(0.00);// 首单优惠
		BigDecimal sentMoney = new BigDecimal(0.00);// 运费
		BigDecimal subMoney = new BigDecimal(0.00);// 满减金额
		BigDecimal phoneMoney = new BigDecimal(0.00);// 手机下单减少金额
		BigDecimal couponAmount = new BigDecimal(0.00);// 优惠券实用金额
		String sourceFlag = "0";
		List<OrderSort> sorts = new ArrayList<OrderSort>();
		String payType = "";// 库存地支持的支付方式
		int KJTNum = 0;//跨境通商品数量
		int xgNum = 0;// 闪购商品数量
		int ptNum = 0;// 普通商品数量
		// 拆单*************************************start****************************************拆单
		List<String> skuForQF = new ArrayList<String>();
		Map<String, BigDecimal> priceForQF = new HashMap<String, BigDecimal>();// 用户清分的sku交易价格
		Map<String, Integer> numMap = new HashMap<String, Integer>();
		MDataMap icMap = new MDataMap();
		for (int i = 0; i < goods.size(); i++) {
			GoodsInfoForAdd forAdd = goods.get(i);
			numMap.put(forAdd.getSku_code(), forAdd.getSku_num());
			icMap.put(forAdd.getSku_code(), "");
		}
		ProductService service = new ProductService();
		if ((VersionHelper.checkServerVersion("3.5.11.31")
				&& sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS))||sellerCode.equals(MemberConst.MANAGE_CODE_SPDOG)) {// 后台逻辑代码版本控制
			MDataMap other = new MDataMap();
			other.put("buyerCode", buyerCode);//
			other.put("orderType", orderType);
			other.put("sellerCode", sellerCode);
//			other.put("coupon_codes", things.get("coupon_codes"));
			if (things.containsKey("payType")) {
				other.put("payType", things.get("payType"));
			}
			if(things.containsKey("wgsUseMoney")){
				other.put("wgsUseMoney", things.get("wgsUseMoney"));
			}
			if(things.containsKey("order_souce")&&!"".equals(things.get("order_souce"))){
				other.put("orderSouce", things.get("order_souce"));
			}else {
				other.put("orderSouce", "449715190001");
			}
			other.put("channelId", things.get("channelId"));
			//一元购参数添加
			other.put("yygOrderNo", things.get("yygOrderNo"));
			other.put("yygPayMoney", things.get("yygPayMoney"));
			
			RootResultWeb resultWeb = new RootResultWeb();
			List<Order> orders = (new DismantlOrderUtil()).DismantlOrder(
					numMap, address, other, resultWeb, couponCodes);
			//回写ld使用的礼金券明细（hjy订单DD编号、礼金券编码）
			List<GiftVoucherInfo> reWriteLD = new ArrayList<GiftVoucherInfo>();			
			for(Order order : orders) {
				List<OcOrderPay> pay = order.getOcOrderPayList();
				if(pay != null && pay.size() > 0) {
					for(OcOrderPay orderpay : pay) {
						String order_code = orderpay.getOrderCode();
						String coupon_code = orderpay.getPaySequenceid();
						String[] codes = coupon_code.split(",");
						for(String code : codes) {
							GiftVoucherInfo info = new GiftVoucherInfo();
							info.setHjy_ord_id(order_code);
							info.setLj_code(code);
							reWriteLD.add(info);
						}
					}
				}
			}
			result.put("reWriteLD", reWriteLD);
			BigDecimal preMon = new BigDecimal(0.00);// 449715400006类型活动的优惠金额
			
			Map<String, BigDecimal> couponMap = new HashMap<String, BigDecimal>();// 优惠券使用
			BigDecimal couponMoney = new BigDecimal(0.00);// 优惠券实用金额
			BigDecimal wgsMoney = new BigDecimal(0.00);// 微公社实用金额
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				sentMoney = sentMoney.add(order.getTransportMoney());
				payMoney = payMoney.add(order.getDueMoney());
				costMoney = costMoney.add(order.getOrderMoney());
				payType = order.getPayType();
				phoneMoney = phoneMoney.add(order.getPromotionMoney());
				if (!"1".equals(sourceFlag)
						&& "449715200007".equals(order.getOrderType())) {
					sourceFlag = "1";
				}
				OrderSort orderSort = new OrderSort();
				ProductLabelService productLabelService = new ProductLabelService();
				for (int j = 0; j < order.getProductList().size(); j++) {// 商品
					OrderDetail detail = order.getProductList().get(j);
					if ("0".equals(detail.getGiftFlag())) {
						continue;
					}
					GoodsInfoForConfirm confirm = new GoodsInfoForConfirm();
					confirm.setSku_code(detail.getSkuCode());
					if (!icMap.containsKey(detail.getSkuCode())) {// 非本系统正常sku编号
						for (int f = 0; f < goods.size(); f++) {
							if (PlusHelperEvent.checkEventItem(goods.get(f)
									.getSku_code())) {
								PlusModelSkuInfo info = new PlusSupportProduct()
										.upSkuInfoBySkuCode(goods.get(f)
												.getSku_code(), buyerCode);
								if (info.getSkuCode().equals(
										detail.getSkuCode())) {
									confirm.setSku_code(goods.get(f)
											.getSku_code());
									break;
								}
							}
						}
					}
					confirm.setFlagTheSea(service.checkProductKjt(detail.getProductCode())?"1":"0");
					KJTNum=confirm.getFlagTheSea()=="1"?KJTNum+1:KJTNum;
					confirm.setSku_name(detail.getSkuName());
					confirm.setPic_url(detail.getProductPicUrl());
					confirm.setSku_num(detail.getSkuNum());
					confirm.setProduct_code(detail.getProductCode());
					confirm.setSku_property(reProperties(detail.getSkuCode(),
							detail.getSkuValue()));
					PlusModelSkuInfo icinfo = new PlusSupportProduct().upSkuInfoBySkuCode(confirm.getSku_code(), buyerCode);
					confirm.setSku_price(detail.getSkuPrice().add(detail.getGroupPrice()).add(detail.getCouponPrice()).doubleValue());
					orderSort.getSkuCodes().add(detail.getSkuCode());
					confirm.setNow_stock((new StoreService())
							.getStockNumByStore(detail.getSkuCode()));
					Map<String, Object> kc = service.getSkuActivity(
							confirm.getSku_code(), sellerCode);
					if (kc.containsKey("flashsalesObj")
							&& kc.get("flashsalesObj") != null) {
						xgNum = xgNum + 1;
						FlashsalesSkuInfo fssi = (FlashsalesSkuInfo) kc
								.get("flashsalesObj");
						confirm.setSales_info(bConfig("familyhas.ActivitySgInfo"));
						confirm.setSales_type(bConfig("familyhas.ActivitySgName"));
						confirm.setSales_code(fssi.getActivityCode());
						Activity activity = new Activity();
						activity.setActivity_name(bConfig("familyhas.ActivitySgName"));
						activity.setActivity_info(bConfig("familyhas.ActivitySgInfo"));
						confirm.getActivitys().add(activity);
					} else if(StringUtility.isNotNull(icinfo.getEventCode())&&icinfo.getBuyStatus()==1){
						Activity activity = new Activity();
						activity.setActivity_name(icinfo.getSellNote());
						confirm.getActivitys().add(activity);
					}else {
						ptNum = ptNum + 1;
					}
					priceForQF.put(
							confirm.getSku_code(),
							detail.getSkuPrice().multiply(
									new BigDecimal(detail.getSkuNum())));
					skuForQF.add(confirm.getSku_code());
					int isIncludeGift = Integer.valueOf((new ProductService())
							.getProductSomeInfo(detail.getProductCode())
							.get(detail.getProductCode()).get("isIncludeGift"));
					if (isIncludeGift == 1) {
						confirm.getOtherShow().add(
								bConfig("familyhas.ActivityZpName"));
					}
					
					
					//添加图片
					confirm.setLabelsPic(productLabelService.getLabelInfo(detail.getProductCode()).getListPic());
					confirms.add(confirm);
				}
				orderSort.setTranMoney(order.getTransportMoney().doubleValue());
				sorts.add(orderSort);
				for (int j = 0; j < order.getActivityList().size(); j++) {
					OcOrderActivity aa = order.getActivityList().get(j);
					if ("449715400006".equals(aa.getActivityType())) {
						preMon = new BigDecimal(aa.getPreferentialMoney());
					}
				}
				if (order.getOcOrderPayList() != null
						&& !order.getOcOrderPayList().isEmpty()) {
					for (int j = 0; j < order.getOcOrderPayList().size(); j++) {
						String yhqType = order.getOcOrderPayList().get(j)
								.getPayType();
						String yhqCode = order.getOcOrderPayList().get(j)
								.getPaySequenceid();
						float yhqMon = order.getOcOrderPayList().get(j)
								.getPayedMoney();
						if ("449746280002".equals(yhqType)) {
							couponMoney = couponMoney
									.add(new BigDecimal(yhqMon));
							
							couponAmount=couponMoney;
							
							if (couponMap.containsKey(yhqCode)) {
								couponMap.get(yhqCode).add(
										new BigDecimal(yhqMon));
							} else {
								couponMap.put(yhqCode, new BigDecimal(yhqMon));
							}
						}else if ("449746280009".equals(yhqType)) {
							wgsMoney = new BigDecimal(order.getOcOrderPayList().get(j).getPayedMoney()).add(wgsMoney);
						}
					}
				}
			}
			List<TeslaModelDiscount> disList = new ArrayList<TeslaModelDiscount>();// 折扣显示
			TeslaModelDiscount rpjl = new TeslaModelDiscount();
			if (VersionHelper.checkServerVersion("3.5.43.51")
					&& couponMoney.compareTo(new BigDecimal(0.00)) > 0) {// 优惠券使用显示
				TeslaModelDiscount discount = new TeslaModelDiscount();
				//需要考虑礼金券叠加使用的情况下，出现多张礼金券
				for(String coupon_code : couponCodes) {
					CouponInfo ci = new CouponUtil().getCouponInfo(coupon_code);
					discount.setDis_name(bConfig("familyhas.coupon_name"));
					if(ci.getSurplusMoney().compareTo(couponMoney)>=0){
						discount.setDis_price(couponMoney.doubleValue());
					}else {
						discount.setDis_price(ci.getSurplusMoney().doubleValue());
					}
					discount.setDis_type("0");
					if (couponMoney.compareTo(ci.getSurplusMoney()) > 0) {
						rpjl.setDis_name(bConfig("familyhas.coupon_name_limit"));
						rpjl.setDis_price((couponMoney.subtract(ci
								.getSurplusMoney())).doubleValue());
						rpjl.setDis_type("0");
					}
				}				
				disList.add(discount);
			}
			result.put("wgsalUseMoney", "0.00");
			if (things.containsKey("wgsUseMoney")&&wgsMoney.compareTo(new BigDecimal(things.get("wgsUseMoney")))>0) {
				rpjl.setDis_price(new BigDecimal(rpjl.getDis_price()).add(wgsMoney).subtract(new BigDecimal(things.get("wgsUseMoney"))).doubleValue());
				rpjl.setDis_name(bConfig("familyhas.coupon_name_limit"));
				rpjl.setDis_type("0");
				result.put("wgsalUseMoney", things.get("wgsUseMoney"));
			}else if(things.containsKey("wgsUseMoney")&&wgsMoney.compareTo(new BigDecimal(things.get("wgsUseMoney")))<=0) {
				result.put("wgsalUseMoney", String.valueOf(wgsMoney.doubleValue()));
			}
			if(rpjl.getDis_price()>0){
				disList.add(rpjl);
			}
			if(KJTNum>0){
				TeslaModelDiscount discount = new TeslaModelDiscount();
				discount.setDis_name("关税");
				discount.setDis_type("1");
				disList.add(discount);
			}
			if (!disList.isEmpty()) {
				result.put("disList", disList);
			}
			List<CouponInfo> couponInfos = new ArrayList<CouponInfo>();
			Iterator<String> couIte = couponMap.keySet().iterator();
			while (couIte.hasNext()) {
				CouponInfo ci = new CouponInfo();
				ci.setCouponCode((String) couIte.next());
				ci.setSurplusMoney(new CouponUtil().getCouponInfo(
						ci.getCouponCode()).getSurplusMoney());
				couponInfos.add(ci);
			}
			result.put("coupons", couponInfos);
			result.put("orders", orders);
			if (!resultWeb.upFlagTrue()) {
				result.put("resultCode", resultWeb.getResultCode());
				result.put("resultMessage", resultWeb.getResultMessage());
			}
			// 拆单**************************************end***************************************拆单
		} else {
			if (goods != null && !goods.isEmpty()) {
				for (int i = 0; i < goods.size(); i++) {
					String skuCode = goods.get(i).getSku_code();
					int skuNum = goods.get(i).getSku_num();
					PcProductinfo productInfo = service.getskuinfo(skuCode, "");
					payType = "449716200002";// 货到付款
					GoodsInfoForConfirm confirm = new GoodsInfoForConfirm();
					confirm.setSku_code(skuCode);
					confirm.setSku_name(productInfo.getProductSkuInfoList()
							.get(0).getSkuName());
					confirm.setPic_url(productInfo.getMainPicUrl());
					confirm.setSku_num(skuNum);
					confirm.setProduct_code(productInfo.getProductCode());
					confirm.setGiftList(new ArrayList<Gift>());
					confirm.setSku_property(reProperties(skuCode, productInfo
							.getProductSkuInfoList().get(0).getSkuValue()));
					confirm.setSku_price(productInfo.getProductSkuInfoList()
							.get(0).getSellPrice().doubleValue());
					confirm.setNow_stock((new ProductStoreService())
							.getStockNumBySku(skuCode));
					Map<String, Object> kc = service.getSkuActivity(skuCode,
							sellerCode);
					BigDecimal tmp_price = productInfo.getProductSkuInfoList()
							.get(0).getSellPrice();// 获取价格
					if (kc.containsKey("flashsalesObj")
							&& kc.get("flashsalesObj") != null) {
						xgNum = xgNum + 1;
						FlashsalesSkuInfo fssi = (FlashsalesSkuInfo) kc
								.get("flashsalesObj");
						confirm.setSales_info(bConfig("familyhas.ActivitySgInfo"));
						confirm.setSales_type(bConfig("familyhas.ActivitySgName"));
						confirm.setSales_code(fssi.getActivityCode());
						confirm.setSku_price(fssi.getVipPrice().doubleValue());
						payMoney = payMoney.add(fssi.getVipPrice().multiply(
								new BigDecimal(skuNum)));// 实付款
						costMoney = costMoney.add((new BigDecimal(fssi
								.getVipPrice().doubleValue()))
								.multiply(new BigDecimal(skuNum)));// 商品总金额
					} else {
						ptNum = ptNum + 1;
						payMoney = payMoney.add(tmp_price
								.multiply(new BigDecimal(skuNum)));
						costMoney = costMoney.add(productInfo
								.getProductSkuInfoList().get(0).getSellPrice()
								.multiply(new BigDecimal(skuNum)));// 商品总金额
					}
					confirms.add(confirm);
				}
			}
			if (VersionHelper.checkServerVersion("2.0.1.1") && xgNum == 0
					&& sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS)) {// 后台逻辑代码版本控制
				payMoney = payMoney.subtract(new BigDecimal(30));
				TeslaModelDiscount discount = new TeslaModelDiscount();
				List<TeslaModelDiscount> disList = new ArrayList<TeslaModelDiscount>();
				discount.setDis_name(bConfig("familyhas.eveActivityName"));
				discount.setDis_price(new Double(
						bConfig("familyhas.eveActivityMoney")));
				discount.setDis_type("0");
				disList.add(discount);
				result.put("disList", disList);
			}
		}
		result.put("sorts", sorts);
		if (VersionHelper.checkServerVersion("2.0.1.1") && xgNum > 0
				&& ptNum > 0
				&& sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS)) {// 后台逻辑代码版本控制
			result.put("prompt", bConfig("familyhas.eveActivityalert"));
		}
		if ((VersionHelper.checkServerVersion("3.5.51.51")
				&& sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS))||sellerCode.equals(MemberConst.MANAGE_CODE_SPDOG)) {
			if ("1".equals(sourceFlag)) {
				cashBack = new BigDecimal(0.00);
			} else {
				Map<String, BigDecimal> qfBlMap = getScaleReckonMap(buyerCode,
						skuForQF, sellerCode);
				if (qfBlMap != null && !qfBlMap.isEmpty()) {
					for (int i = 0; i < skuForQF.size(); i++) {
						cashBack = cashBack.add(qfBlMap.get(skuForQF.get(i))
								.multiply(priceForQF.get(skuForQF.get(i))));
					}
				}
			}
		}else {
			Double fxbl = getDiscountForUserCode(buyerCode, sellerCode);
			cashBack = payMoney.multiply(new BigDecimal(fxbl));
		}
		result.put("confirms", confirms);
		
		// 在这里去掉小数位
		if ("SI2013".equals(sellerCode)) {
			result.put("payMoney",
					payMoney.setScale(2, BigDecimal.ROUND_HALF_UP));
			result.put("costMoney",
					costMoney.setScale(2, BigDecimal.ROUND_HALF_UP));
			
		} else {
			if(things.containsKey("wgsUseMoney")&&Double.valueOf(things.get("wgsUseMoney"))>0&&payMoney.doubleValue()<0){//当且仅当存在微公社支付的时候出现的0元订单才正常.
				result.put("payMoney",0.00);
			}else {
//				result.put("payMoney",payMoney.setScale(0, BigDecimal.ROUND_HALF_UP));
				result.put("payMoney", MoneyHelper.roundHalfUp(payMoney));	// 兼容小数 - Yangcl 
			}
//			result.put("costMoney",costMoney.setScale(0, BigDecimal.ROUND_HALF_UP));
			result.put("costMoney", MoneyHelper.roundHalfUp(costMoney)); // 兼容小数 - Yangcl  
			
		}
		
		// 返现金额保持小数点
		if(cashBack.doubleValue()<=0){
			cashBack=new BigDecimal(0.00);
		}
		result.put("cashBack", cashBack.setScale(2, BigDecimal.ROUND_HALF_UP));
//		result.put("firstCheap", firstCheap.setScale(0, BigDecimal.ROUND_HALF_UP));
//		result.put("sentMoney", sentMoney.setScale(0, BigDecimal.ROUND_HALF_UP));
//		result.put("subMoney", subMoney.setScale(0, BigDecimal.ROUND_HALF_UP));
//		result.put("phoneMoney", phoneMoney.setScale(0, BigDecimal.ROUND_HALF_UP));
		result.put("firstCheap",  MoneyHelper.roundHalfUp(firstCheap)); // 兼容小数 - Yangcl  
		result.put("sentMoney", MoneyHelper.roundHalfUp(sentMoney)); // 兼容小数 - Yangcl  
		result.put("subMoney", MoneyHelper.roundHalfUp(subMoney)); // 兼容小数 - Yangcl   
		result.put("phoneMoney", MoneyHelper.roundHalfUp(phoneMoney)); // 兼容小数 - Yangcl    
		result.put("payType", payType);
		result.put("sourceFlag", sourceFlag);
		return result;
	}

	/**
	 * 确认订单判断库存 和是否下架
	 */
	public RootResult checkGoodsStock(String sellerCode, String buyerCode,
			List<GoodsInfoForAdd> goods) {
		RootResult rootResult = new RootResult();
		String error = "";
		int maxStock = 0;
		if (goods != null && !goods.isEmpty()) {
			for (int i = 0; i < goods.size(); i++) {
				String skuCode = goods.get(i).getSku_code();
				int skuNum = goods.get(i).getSku_num();
				ProductService service = new ProductService();
				PcProductinfo product = null;
				if (PlusHelperEvent.checkEventItem(skuCode)) {
					PlusModelSkuInfo pms = new PlusSupportProduct()
							.upSkuInfoBySkuCode(skuCode, buyerCode);
					if (pms.getBuyStatus() != 1 || skuNum > pms.getMaxBuy()) {
						product = service.getskuinfo(
								pms.getSkuCode(), "");
						error = product.getProductSkuInfoList().get(0)
								.getSkuName();
						break;
					}
				} else {
					// 判断此商品是否存在
					product = service.getskuinfo(skuCode, "");
					if (product == null) {
						deleteSkuForShopCart(buyerCode, skuCode);// 删除已不存在的商品
						continue;
					}
					// 判断是否下架
					String productStatus = product.getProductStatus();
					if (productStatus == null
							|| !"4497153900060002".equals(productStatus)) {// 已下架
						error = product.getProductSkuInfoList().get(0)
								.getSkuName();
						break;
					}
					// 判断本地区所在库是否存在本商品的库存或者存在库存是否不足 如果是闪购商品提示删除
					StoreService storeService = new StoreService();
					maxStock = storeService.getStockNumByMax(skuCode);

					if (VersionHelper.checkServerVersion("3.5.11.31")
							&& (sellerCode
									.equals(MemberConst.MANAGE_CODE_HOMEHAS) || sellerCode
									.equals(MemberConst.MANAGE_CODE_HPOOL))) {// 后台逻辑代码版本控制
						maxStock = (new ProductStoreService())
								.getStockNumBySku(skuCode);
					}

					if (maxStock < skuNum) {
						error = product.getProductSkuInfoList().get(0)
								.getSkuName();
						break;
					}
				}
				if(product!=null&&"".equals(error.toString())&&product.getProductSkuInfoList().get(0).getMiniOrder()>skuNum){
					rootResult.setResultCode(916421262);
					rootResult.setResultMessage(bInfo(916421262, product.getProductSkuInfoList().get(0).getSkuName(),product.getProductSkuInfoList().get(0).getMiniOrder()));
					break;
				}
			}
		}
		if (!"".equals(error.toString()) && maxStock == 0) {
			rootResult.setResultCode(916401131);
			rootResult.setResultMessage(bInfo(916401131, "[" + error + "]"));
		} else if (!"".equals(error.toString()) && maxStock > 0) {
			rootResult.setResultCode(916421255);
			rootResult.setResultMessage(bInfo(916421255, "[" + error + "]"));
		}
		return rootResult;
	}

	/**
	 * 确认订单判断库存 和是否下架
	 */
	public RootResult checkGoodsStock(String sellerCode, String buyerCode,
			List<GoodsInfoForAdd> goods, String areaCode) {
		RootResult rootResult = new RootResult();
		String error = "";
		if (goods != null && !goods.isEmpty()) {
			for (int i = 0; i < goods.size(); i++) {
				String skuCode = goods.get(i).getSku_code();
				int skuNum = goods.get(i).getSku_num();
				ProductService service = new ProductService();
				// 判断此商品是否存在
				PcProductinfo product = service.getskuinfo(skuCode, "");
				if (product == null) {
					deleteSkuForShopCart(buyerCode, skuCode);// 删除已不存在的商品
					continue;
				}
				// 判断是否下架
				String productStatus = product.getProductStatus();
				if (productStatus == null
						|| !"4497153900060002".equals(productStatus)) {// 已下架
					error = product.getProductSkuInfoList().get(0).getSkuName();
					break;
				}
				// 判断本地区所在库是否存在本商品的库存或者存在库存是否不足 如果是闪购商品提示删除
				StoreService storeService = new StoreService();
				int maxStock = storeService.getStockNumByMax(skuCode);

				if (maxStock < skuNum) {
					error = product.getProductSkuInfoList().get(0).getSkuName();
					break;
				}
			}
		}
		if (!"".equals(error.toString())) {
			rootResult.setResultCode(916401131);
			rootResult.setResultMessage(bInfo(916401131, "[" + error + "]"));
		}
		return rootResult;
	}

	/**
	 * 确认订单判断限购数量
	 */
	public RootResult checkGoodsLimit(String sellerCode, String buyerCode,
			List<GoodsInfoForAdd> goods) {
		RootResult rootResult = new RootResult();
		String error = "";
		if (goods != null && !goods.isEmpty()) {

			FlashsalesSkuInfoService flashsalesSkuInfoService = new FlashsalesSkuInfoService();// 闪购剩余促销库存

			for (int i = 0; i < goods.size(); i++) {

				String skuCode = goods.get(i).getSku_code();
				int skuNum = goods.get(i).getSku_num();
				if (!PlusHelperEvent.checkEventItem(skuCode)) {
					ProductService service = new ProductService();
					// 判断是否闪购商品 库存是否不足
					Map<String, Object> kc = service.getSkuActivity(skuCode,
							sellerCode);

					// ----------------------------update by jlin 2015-04-02
					// 19:59:32--------------------------------------------
					int number = skuNum;
					if (kc.get("flashsalesObj") != null) {
						number += service.getUsedPurchaseLimitVipNum3(skuCode,
								buyerCode, ((FlashsalesSkuInfo) kc
										.get("flashsalesObj"))
										.getActivityCode());
					}
					// ---------------------------------------------------------------

					PcProductinfo product = service.getskuinfo(skuCode, "");
					if (product == null) {
						rootResult.setResultCode(916401107);
						rootResult.setResultMessage(bInfo(916401107, skuCode));
						return rootResult;
					}
					String skuName = product.getProductSkuInfoList().get(0)
							.getSkuName();
					if (product.getProductStatus() == null
							|| !"4497153900060002".equals(product
									.getProductStatus())) {
						rootResult.setResultCode(916401104);
						rootResult.setResultMessage(bInfo(916401104, skuName));
						return rootResult;
					}
					if (!kc.containsKey("flashsalesObj")
							|| kc.get("flashsalesObj") == null) {// 普通商品
						if (skuNum > Integer
								.valueOf(bConfig("familyhas.pt_product_num"))) {// 判断普通商品是否超过限购数量
							error = bInfo(916401132, "“" + skuName + "”",
									bConfig("familyhas.pt_product_num"));
							break;
						}
					} else {
						FlashsalesSkuInfo fhlo = (FlashsalesSkuInfo) kc
								.get("flashsalesObj");
						// 判断是否超过每日限购数
						String swhere = "select sum(sku_num) as num from oc_orderdetail where order_code in ("
								+ " select o.order_code from oc_orderinfo o LEFT JOIN oc_order_activity a on o.order_code=a.order_code where"
								+ " o.order_status in ('4497153900010001','4497153900010002','4497153900010003','4497153900010004','4497153900010005')"
								+ " and o.buyer_code = :buyer_code and a.activity_code=:activity_code and a.activity_type=:activity_type "
								+ ") and sku_code=:sku_code ";
						Map<String, Object> alTodaySkuNum = DbUp.upTable(
								"oc_orderinfo").dataSqlOne(
								swhere,
								new MDataMap("buyer_code", buyerCode,
										"activity_type", "449715400004",
										"sku_code", skuCode, "activity_code",
										((FlashsalesSkuInfo) kc
												.get("flashsalesObj"))
												.getActivityCode()));
						if (alTodaySkuNum != null
								&& !alTodaySkuNum.isEmpty()
								&& alTodaySkuNum.get("num") != null
								&& !"".equals(alTodaySkuNum.get("num")
										.toString())
								&& Integer.valueOf(alTodaySkuNum.get("num")
										.toString()) + skuNum > fhlo
										.getPurchase_limit_day_num().intValue()) {
							error = bInfo(
									916401132,
									"“" + skuName + "”",
									(fhlo).getPurchase_limit_day_num()
											.intValue()
											- Integer.valueOf(alTodaySkuNum
													.get("num").toString()));
							break;
						} else if (skuNum > fhlo.getPurchase_limit_day_num()
								.intValue()) {
							error = bInfo(916401132, "“" + skuName + "”",
									(fhlo).getPurchase_limit_day_num()
											.intValue());
							break;
						} else if (number > (fhlo).getPurchaseLimitVipNum()
								.intValue()) {// 判断每会员限购数量
							error = bInfo(916401132, "“" + skuName + "”",
									(fhlo).getPurchaseLimitVipNum().intValue()
											- number
											+ goods.get(i).getSku_num());
							break;
						} else if (skuNum > fhlo.getPurchase_limit_order_num()
								.intValue()) {// 判断是否超过每单限购数量
							error = bInfo(916401132, "“" + skuName + "”",
									(fhlo).getPurchase_limit_order_num()
											.intValue());
							break;
						} else {
							// 判断剩余促销库存 update by jlin 2014-11-07 15:55:41
							int surplus = flashsalesSkuInfoService
									.salesNumSurplus(skuCode,
											(fhlo).getActivityCode());
							if (skuNum > surplus && surplus > 0) {
								error = bInfo(916421255, "“" + skuName + "”");
								rootResult.setResultCode(916421255);
								rootResult.setResultMessage(error);
								return rootResult;
							} else if (skuNum > surplus && surplus == 0) {
								error = bInfo(916421256, "“" + skuName + "”");
								rootResult.setResultCode(916421256);
								rootResult.setResultMessage(error);
								return rootResult;
							}
						}
					}
				} else {
					PlusModelSkuInfo info = new PlusSupportProduct()
							.upSkuInfoBySkuCode(skuCode, buyerCode);
					if (StringUtility.isNull(info.getEventCode())
							|| info.getBuyStatus() != 1) {
						rootResult.setResultCode(916421256);
						rootResult.setResultMessage(bInfo(
								916421256,
								"“"
										+ DbUp.upTable("pc_skuinfo")
												.one("sku_code",
														info.getSkuCode())
												.get("sku_name") + "”"));
						break;
					} else if (StringUtility.isNotNull(info.getEventCode())
							&& info.getBuyStatus() == 1
							&& skuNum > info.getMaxBuy()) {
						error = bInfo(
								916401132,
								"“"
										+ DbUp.upTable("pc_skuinfo")
												.one("sku_code",
														info.getSkuCode())
												.get("sku_name") + "”",
								info.getMaxBuy());
						break;
					}
				}
			}
		}
		if (!"".equals(error.toString())) {
			rootResult.setResultCode(916401132);
			rootResult.setResultMessage(error);
		}
		return rootResult;
	}

	/**
	 * 解析skuValue
	 */
	public List<PcPropertyinfoForFamily> reProperties(String skuCode,
			String skuValue) {
		List<PcPropertyinfoForFamily> piffList = new ArrayList<PcPropertyinfoForFamily>();
		if (skuValue == null || "".equals(skuValue)) {
			return piffList;
		}
		String[] pro = skuValue.split("&");
		for (int j = 0; j < pro.length; j++) {
			PcPropertyinfoForFamily piff = new PcPropertyinfoForFamily();
			String[] va = pro[j].split("=");
			piff.setSku_code(skuCode);
			piff.setPropertyKey(va[0]);
			piff.setPropertyValue(va[1]);
			piffList.add(piff);
		}
		return piffList;
	}
	
	/**
	 * 解析skuValue
	 */
	public List<PcPropertyinfoForFamily> rePropertiesForTesla(String skuCode,
			String skuValue) {
		List<PcPropertyinfoForFamily> piffList = new ArrayList<PcPropertyinfoForFamily>();
		if (skuValue == null || "".equals(skuValue)) {
			return piffList;
		}
		String[] pro = skuValue.split("&");
		for (int j = 0; j < pro.length; j++) {
			PcPropertyinfoForFamily piff = new PcPropertyinfoForFamily();
			String[] va = pro[j].split("=");
			piff.setSku_code(skuCode);
			piff.setPropertyKey(va[0]);
			piff.setPropertyValue(va[1]);
			piffList.add(piff);
		}
		return piffList;
	}

	/**
	 * 创建订单
	 */
	public Map<String, Object> createOrder(Map<String, Object> inputMap,List<String> couponCodes) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 判断是否能进行添加订单操作 先进行订单确认校验
		ShopCartService service = new ShopCartService();
		String sellerCode = inputMap.get("seller_code").toString();
		// 发货地址信息
		OrderAddress address = new OrderAddress();
		if (inputMap.containsKey("buyer_address_id")
				&& inputMap.get("buyer_address_id") != null
				&& !"".equals(inputMap.get("buyer_address_id").toString())) {
			AddressInformation ad = (new AddressService()).getAddressOne(
					inputMap.get("buyer_address_id").toString(),
					inputMap.get("buyer_code").toString());
			address.setAddress(ad.getAddress_street());
			address.setPostCode(ad.getAddress_postalcode());
			address.setMobilephone(ad.getAddress_mobile());
			address.setAreaCode(ad.getArea_code());
			address.setReceivePerson(ad.getAddress_name());
		} else {
			address.setAddress(inputMap.get("buyer_address").toString());
			address.setAreaCode(inputMap.get("buyer_address_code").toString());
			address.setMobilephone(inputMap.get("buyer_mobile").toString());
			address.setReceivePerson(inputMap.get("buyer_name").toString());
		}
		if (StringUtility.isNull(address.getAddress())
				|| StringUtility.isNull(address.getAreaCode())
				|| StringUtility.isNull(address.getMobilephone())) {// 地址三级编号、手机号、详细地址都不能为空
			result.put("resultCode", 916401216);
			result.put("resultMessage", bInfo(916401216));
			return result;
		}
		address.setInvoiceType(((BillInfo) inputMap.get("billInfo"))
				.getBill_Type());
		address.setInvoiceTitle(((BillInfo) inputMap.get("billInfo"))
				.getBill_title());
		address.setInvoiceContent(((BillInfo) inputMap.get("billInfo"))
				.getBill_detail());
		if ((address.getInvoiceContent() == null || "".equals(address
				.getInvoiceContent()))
				&& (address.getInvoiceTitle() == null || "".equals(address
						.getInvoiceTitle()))
				&& (address.getInvoiceType() == null || "".equals(address
						.getInvoiceType()))) {
			address.setFlagInvoice("0");
		} else {
			address.setFlagInvoice("1");
		}
		List<GoodsInfoForAdd> infoForAdds = (List<GoodsInfoForAdd>) inputMap.get("goods");
		if (address.getAreaCode() != null && !"".equals(address.getAreaCode())) {
			RootResult rtr = service.checkGoodsStock(inputMap
					.get("seller_code").toString(), inputMap.get("buyer_code")
					.toString(), infoForAdds);
			if (rtr.getResultCode() > 1) {
				result.put("resultCode", rtr.getResultCode());
				result.put("resultMessage", rtr.getResultMessage());
				return result;
			}
		}
		MDataMap oth = new MDataMap();
		oth.put("sellerCode", inputMap.get("seller_code").toString());
		oth.put("buyerCode", inputMap.get("buyer_code").toString());
		oth.put("orderType", inputMap.get("order_type").toString());
		oth.put("payType", inputMap.get("pay_type").toString());
		//coupon_codes 类型是List<String>, 此处需要修改
//		oth.put("coupon_codes", inputMap.get("coupon_codes").toString());
		oth.put("wgsUseMoney", inputMap.get("wgsUseMoney").toString());
		oth.put("order_souce", inputMap.get("order_souce").toString());
		oth.put("channelId", inputMap.get("channelId").toString());
		//一元购参数添加
		oth.put("yygOrderNo", inputMap.get("yygOrderNo").toString());
		oth.put("yygPayMoney", inputMap.get("yygPayMoney").toString());
		
		Map<String, Object> checkMe = service.orderConfirm(oth, infoForAdds, address, couponCodes);
		if (checkMe.containsKey("resultCode")) {
			result.put("resultCode", checkMe.get("resultCode").toString());
			result.put("resultMessage", checkMe.get("resultMessage").toString());
			return result;
		}
		if (((BigDecimal) checkMe.get("payMoney")).doubleValue() != Double
				.valueOf(inputMap.get("check_pay_money").toString())) {
			result.put("check_pay_money_error",
					((BigDecimal) checkMe.get("payMoney")).doubleValue());
			return result;
		}
		if(checkMe.containsKey("reWriteLD")) {
			result.put("reWriteLD", checkMe.get("checkMe"));
		}
		if (sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS) 
				|| sellerCode.equals(MemberConst.MANAGE_CODE_HPOOL)
				||sellerCode.equals(MemberConst.MANAGE_CODE_SPDOG)) {// 后台逻辑代码版本控制
			// 拆单*************************************start****************************************拆单
			OrderService orderService = new OrderService();
			RootResultWeb rootResult = new RootResultWeb();
			Map<String, String> other = new HashMap<String, String>();
			other.put("buyerCode", inputMap.get("buyer_code").toString());
			other.put("orderType", inputMap.get("order_type").toString());
			other.put("orderSouce", inputMap.get("order_souce").toString());
			other.put("payType", inputMap.get("pay_type").toString());
			other.put("sellerCode", inputMap.get("seller_code").toString());
			List<Order> orders = (List<Order>) checkMe.get("orders");
			if (sellerCode.equals(MemberConst.MANAGE_CODE_HOMEHAS)||sellerCode.equals(MemberConst.MANAGE_CODE_SPDOG)) {
				for (int i = 0; i < orders.size(); i++) {
//					orders.get(i).setTransportMoney(new BigDecimal(0.00));
					if (orders.get(i).getDueMoney()
							.compareTo(new BigDecimal(0.00)) <= 0&&Double.valueOf(inputMap.get("wgsUseMoney").toString())==0&&"".equals(inputMap.get("coupon_codes").toString())) {
						rootResult.setResultCode(916401118);
						rootResult.setResultMessage(bInfo(916401118));
						break;
					}
				}
			}
			if (rootResult.upFlagTrue()) {
				// 下单前减库存~~特殊的促销系统
				beforeCreateOrder(orders,infoForAdds, rootResult,
						inputMap.get("buyer_code").toString());
			}
			if (rootResult.upFlagTrue()) {
				String bigOrderCode = orderService.AddOrderListForSupper(
						orders, rootResult);
				//this.updateDistributionInfos(inputMap,orders);
				if (rootResult.upFlagTrue()) {
					if (VersionHelper.checkServerVersion("3.5.41.51")) {
						(new CouponUtil()).useCoupon(bigOrderCode);// 优惠券占用
					}
					result.put("order_code", bigOrderCode);
				}
			} else {
				result.put("resultCode", rootResult.getResultCode());
				result.put("resultMessage", rootResult.getResultMessage());
				return result;
			}
			if (rootResult.upFlagTrue()) {
				// 下单后~~特殊的促销系统
				afterCreateOrder(result.get("order_code").toString(), inputMap
						.get("pay_type").toString());
			}else {
				for(int i = 0; i < orders.size(); i++){
					Order order = orders.get(i);
					if(order.getOcOrderPayList()==null){
						continue;
					}
					for(int k = 0; k < order.getOcOrderPayList().size(); k++){
						OcOrderPay pay = order.getOcOrderPayList().get(k);
						if("449746280009".equals(pay.getPayType())){
							GroupRefundInput gri = new GroupRefundInput();
							gri.setMemberCode(order.getBuyerCode());
							gri.setOrderCode(order.getOrderCode());
							gri.setRefundMoney(pay.getPayRemark());
							gri.setRefundTime(DateUtil.getSysDateTimeString());
							gri.setTradeCode(pay.getPaySequenceid());
							GroupRefundResult grr = new GroupPayService().groupRefund(gri, order.getSellerCode());
							if(!grr.upFlagTrue()){
								rootResult.setResultCode(grr.getResultCode());
								rootResult.setResultMessage(grr.getResultMessage());
								break;
							}
						}
					}
				}
			}
			// 拆单*************************************end****************************************拆单
		} else {
			// 创建订单
			Order order = new Order();
			order.setOrderCode(WebHelper.upCode(OrderConst.OrderHead));
			address.setOrderCode(order.getOrderCode());
			order.setAddress(address);
			List<GoodsInfoForConfirm> confirms = (List<GoodsInfoForConfirm>) checkMe
					.get("confirms");
			List<OcOrderActivity> actiList = new ArrayList<OcOrderActivity>();
			order.setAppVersion(inputMap.get("app_vision").toString());
			order.setBuyerCode(inputMap.get("buyer_code").toString());
			order.setCreateTime(DateUtil.getSysDateTimeString());
			if ("449716200002".equals(order.getPayType())) {
				order.setOrderStatus("4497153900010002");
			} else {
				order.setOrderStatus("4497153900010001");
			}
			order.setOrderSource(inputMap.get("order_souce").toString());
			order.setOrderType(inputMap.get("order_type").toString());
			order.setSellerCode(inputMap.get("seller_code").toString());
			order.setPayType(inputMap.get("pay_type").toString());
			order.setOrderMoney((BigDecimal) checkMe.get("costMoney"));
			order.setDueMoney((BigDecimal) checkMe.get("payMoney"));
			order.setPromotionMoney((BigDecimal) checkMe.get("firstCheap"));
			if (checkMe.containsKey("disList")
					&& checkMe.get("disList") != null
					&& !"".equals(checkMe.get("disList"))) {
				List<TeslaModelDiscount> discounts = (List<TeslaModelDiscount>) checkMe
						.get("disList");
				for (int z = 0; z < discounts.size(); z++) {
					OcOrderActivity acti = new OcOrderActivity();
					if (new BigDecimal(
							bConfig("familyhas.fullSubActivityMoney"))
							.compareTo(new BigDecimal(50.00)) == 0) {
						acti.setActivityCode(bConfig("familyhas.fullSubActivityCode"));// 满399减50活动编号
						acti.setOutActiveCode(bConfig("familyhas.fullSubActivityCode"));// 满399减50活动编号
						acti.setActivityName(bConfig("familyhas.fullSubActivityName"));// 活动名称
						acti.setActivityType("449715400006");
						acti.setPreferentialMoney(Float
								.valueOf(bConfig("familyhas.fullSubActivityMoney")));// 优惠金额
						acti.setOrderCode(order.getOrderCode());
						acti.setTicketCode(bConfig("familyhas.fullSubActivityquan"));
					} else {
						acti.setActivityCode(bConfig("familyhas.eveActivity"));// 每单立减30元活动编号
						acti.setOutActiveCode(bConfig("familyhas.eveActivity"));// 每单立减30元活动编号
						acti.setActivityName(bConfig("familyhas.eveActivityName"));// 活动名称
						acti.setActivityType("449715400006");
						acti.setPreferentialMoney(Float
								.valueOf(bConfig("familyhas.eveActivityMoney")));// 优惠金额
						acti.setOrderCode(order.getOrderCode());
						acti.setTicketCode(bConfig("familyhas.eveActivityquan"));
					}
					actiList.add(acti);
				}
			}
			List<OrderDetail> odList = new ArrayList<OrderDetail>();
			for (int i = 0; i < confirms.size(); i++) {
				GoodsInfoForConfirm firm = confirms.get(i);
				OrderDetail od = new OrderDetail();
				od.setSkuCode(firm.getSku_code());
				od.setProductCode(firm.getProduct_code());
				od.setOrderCode(order.getOrderCode());
				od.setProductPicUrl(firm.getPic_url());
				od.setSkuName(firm.getSku_name());
				od.setSkuNum(firm.getSku_num());
				od.setSkuPrice(new BigDecimal(firm.getSku_price()));
				od.setStoreCode(firm.getArea_code());
				od.setGiftFlag("1");
				odList.add(od);

				if (Double.valueOf(checkMe.get("firstCheap").toString()) > 0) {// 如果首单优惠大于0则为首单优惠活动
					// 活动相关的
					OcOrderActivity acti = new OcOrderActivity();
					// acti.setActivityCode(bConfig("familyhas.firstActivity"));//
					// 首单88折活动编号
					// acti.setActivityName(bConfig("familyhas.firstActivityName"));
					acti.setPreferentialMoney(firm.getSku_price().floatValue());// 优惠金额
					acti.setSkuCode(firm.getSku_code());
					acti.setOrderCode(order.getOrderCode());
					actiList.add(acti);
				}

				if (firm.getSales_code() != null
						&& !"".equals(firm.getSales_code())
						&& firm.getSales_code().startsWith("SG")) {
					order.setOrderType("449715200004");// 闪购订单
					OcOrderActivity sgOcOrderActivity = new OcOrderActivity();
					sgOcOrderActivity.setSkuCode(firm.getSku_code());
					sgOcOrderActivity.setActivityCode(firm.getSales_code());
					sgOcOrderActivity.setActivityName(firm.getSales_info());
					sgOcOrderActivity.setActivityType("AT140820100002");
					sgOcOrderActivity.setOutActiveCode(DbUp
							.upTable("oc_activity_flashsales")
							.one("activity_code", firm.getSales_code())
							.get("outer_activity_code"));
					String sprice = DbUp.upTable("pc_skuinfo")
							.one("sku_code", firm.getSku_code())
							.get("sell_price");
					BigDecimal pprice = (new BigDecimal(sprice)).subtract(
							new BigDecimal(firm.getSku_price())).setScale(2,
							BigDecimal.ROUND_HALF_UP);
					sgOcOrderActivity.setPreferentialMoney(pprice.floatValue());
					actiList.add(sgOcOrderActivity);
				}

				// 获取赠品数据
				/*StoreService storeService = new StoreService();
				List<String> stores = storeService.getStores(firm
						.getArea_code());*/
				GetGoodGiftList ggft = new GetGoodGiftList();
				List<ModelGoodGiftInfo> gifts = ggft.doRsync(
						firm.getProduct_code(), address.getAreaCode());
				if (gifts != null && !gifts.isEmpty()) {
					for (int j = 0; j < gifts.size(); j++) {
						ModelGoodGiftInfo mlgi = gifts.get(j);
						OcOrderActivity giftActi = new OcOrderActivity();
						giftActi.setActivityCode(mlgi.getEvent_id());
						giftActi.setOrderCode(order.getOrderCode());
						giftActi.setSkuCode(firm.getSku_code());
						actiList.add(giftActi);

						OrderDetail odG = new OrderDetail();
						odG.setSkuCode(mlgi.getGood_id());
						odG.setOrderCode(order.getOrderCode());
						odG.setSkuName(mlgi.getGood_nm());
						odG.setSkuNum(1);
						odG.setSkuPrice(new BigDecimal(0));
//						odG.setStoreCode(stores.get(0));
						odG.setGiftFlag("0");
//						odG.setStoreCode(stores.get(0).toString());
						odList.add(odG);
					}
				}
			}
			order.setProductList(odList);
			order.setActivityList(actiList);

			OrderService ser = new OrderService();
			List<Order> orderLists = new ArrayList<Order>();
			orderLists.add(order);
			StringBuffer error = new StringBuffer();
			if (AppConst.MANAGE_CODE_CAPP.equals(inputMap.get("seller_code")
					.toString())) {
				ser.AddOrderListTx(orderLists, error, AppConst.CAPP_STORE_CODE);
			} else {
				ser.AddOrderListTx(orderLists, error,
						inputMap.get("buyer_address_code").toString());
			}
			if (error != null && !"".equals(order.getOrderCode())) {
				result.put("resultCode", 916401118);
				result.put("resultMessage", error);
			}
			result.put("order_code", order.getOrderCode());
			//this.updateDistributionInfos(inputMap,orderLists);
		}
		return result;
	}

	/**
	 * 更新下单商品绑定的分销信息
	 * @param inputMap
	 * @param orders
	 */
	private static void updateDistributionInfos(Map<String, Object> inputMap, List<Order> orders) {
		// TODO Auto-generated method stub
		Object object = inputMap.get("productSharedInfos");		
		if(object!=null) {
			@SuppressWarnings("unchecked")
			List<DistributionInfoModel> list = JSONArray.toList(JSONArray.fromObject(object), new DistributionInfoModel(),new JsonConfig());
			if(list!=null&&list.size()>0) {
				Long nowTime = System.currentTimeMillis();
				//获取订单详情信息
				Map<String,OrderDetail> paraMap  = new ConcurrentHashMap<String,OrderDetail>();
				for (Order order : orders) {
					List<OrderDetail> productList = order.getProductList();
					for (OrderDetail orderDetail : productList) {
						paraMap.put(orderDetail.getProductCode(), orderDetail);
					}
				}
				//更新分销信息
	        	for (DistributionInfoModel distributionInfoModel : list) {
	        		String productCode = distributionInfoModel.getPid();
	        		String distribution_member_id = distributionInfoModel.getFxrMemberCode();
	        		Long startTime = Long.parseLong(distributionInfoModel.getStartTime());
	        		Long endTime = Long.parseLong(distributionInfoModel.getEndTime());
	        	
	        		if(Long.compare(nowTime, startTime)>0&&Long.compare(endTime, nowTime)>0&&paraMap.containsKey(productCode)) {
	        			DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("distribution_member_id",distribution_member_id,"order_code",paraMap.get("order_code").getOrderCode()), "distribution_member_id", "order_code");
	        			Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
	        		    if(dataSqlOne!=null) {
	        		    	Integer share_order_num = Integer.parseInt(dataSqlOne.get("share_order_num").toString());
	        		    	BigDecimal share_sales_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("share_sales_value").toString()));
	        		    	share_order_num=share_order_num+1;
	        		    	share_sales_value.add(paraMap.get("order_code").getSkuPrice());
	        		    	BigDecimal average_share_order_price = share_sales_value.divide(BigDecimal.valueOf(Double.valueOf(share_order_num)), BigDecimal.ROUND_HALF_DOWN);
	        		    	share_sales_value.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	        		    	average_share_order_price.setScale(2,BigDecimal.ROUND_HALF_DOWN);
	        		    	DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("share_order_num",share_order_num.toString(),"share_sales_value",share_sales_value.toString(),"average_share_order_price",average_share_order_price.toString(),"distribution_member_id",distribution_member_id), "average_share_order_price,share_order_num,share_sales_value", "distribution_member_id");
	        		    }
	        		}
	        		
				}
	        }
		}
	}

	
	private void beforeCreateOrder(List<Order> orders,List<GoodsInfoForAdd> goods,
			RootResultWeb resultWeb, String buyerCode) {

		for(int i = 0; i < orders.size(); i++){
			Order order = orders.get(i);
			if(!resultWeb.upFlagTrue()){
				break;
			}
			if(order.getOcOrderPayList()==null){
				break;
			}
			for(int k = 0; k < order.getOcOrderPayList().size(); k++){
				OcOrderPay pay = order.getOcOrderPayList().get(k);
				if("449746280009".equals(pay.getPayType())&&StringUtility.isNotNull(pay.getPayRemark())
						&&!"0".equals(pay.getPayRemark())&&Double.valueOf(pay.getPayRemark())>0){
					GroupPayInput gi = new GroupPayInput();
					gi.setMemberCode(order.getBuyerCode());
					gi.setOrderCode(order.getOrderCode());
					gi.setOrderCreateTime(order.getCreateTime());
					gi.setTradeMoney(pay.getPayRemark());
					GroupPayResult gr = new GroupPayService().GroupPay(gi, order.getSellerCode());
					if(gr.upFlagTrue()){
						orders.get(i).getOcOrderPayList().get(k).setPaySequenceid(gr.getTradeCode());
					}else {
						resultWeb.setResultCode(gr.getResultCode());
						resultWeb.setResultMessage(gr.getResultMessage());
						break;
					}
				}
			}
		}
		if(resultWeb.upFlagTrue()){
			for (int i = 0; i < goods.size(); i++) {
				GoodsInfoForAdd good = goods.get(i);
				String icCode = "";
				if (PlusHelperEvent.checkEventItem(good.getSku_code())) {
					icCode = good.getSku_code();
				} else {
					PlusModelSkuInfo info = new PlusSupportProduct()
							.upSkuInfoBySkuCode(good.getSku_code(), buyerCode);
					if (StringUtility.isNotNull(info.getEventCode())
							&& info.getBuyStatus() == 1) {
						icCode = info.getItemCode();
					}
				}
				if (!"".equals(icCode)
						&& new PlusSupportEvent().subtractSkuStock(icCode,
								Long.valueOf(good.getSku_num())) < 0) {
					resultWeb.setResultCode(916401118);
					resultWeb.setResultMessage(bInfo(916401118));
					break;
				}
			}
		}
	}

	private void afterCreateOrder(String bigOrderCode, String pay_type) {
		try {
			if (StringUtility.isNotNull(bigOrderCode)) {
				// 缓存记录支付方式
				new PlusSupportPay().fixPayFrom(bigOrderCode, pay_type);
				String sql = "select a.order_code,a.buyer_code,b.out_active_code,c.sku_num,c.sku_code "
						+ "from oc_orderinfo a,oc_order_activity b ,oc_orderdetail c "
						+ "where a.order_code=b.order_code and a.order_code=c.order_code "
						+ "AND b.out_active_code like 'IC%' and a.big_order_code=:big_order_code";
				List<Map<String, Object>> orders = DbUp.upTable("oc_orderinfo")
						.dataSqlList(sql,
								new MDataMap("big_order_code", bigOrderCode));
				for (int j = 0; j < orders.size(); j++) {
					Map<String, Object> mm = orders.get(j);
					
				

					String sIcCode = mm.get("out_active_code").toString();

					if (StringUtils.isNotBlank(sIcCode)
							&& new PlusSupportEvent()
									.upItemProductByIcCode(sIcCode)
									.getSkuCode()
									.equals(mm.get("sku_code").toString())) {

						new PlusHelperNotice().onIcOrder(mm.get("order_code")
								.toString(), mm.get("buyer_code").toString(),
								mm.get("out_active_code").toString(), Long
										.valueOf(mm.get("sku_num").toString()));
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public void updateAccountFlag(String buyerCode, String skuCode) {
		try {
			if (buyerCode != null && skuCode != null && !"".equals(skuCode)
					&& !"".equals(buyerCode)) {
				MDataMap map = new MDataMap();
				map.put("buyer_code", buyerCode);
				map.put("sku_code", skuCode);
				map.put("account_flag", "1");
				DbUp.upTable("oc_shopCart").dataUpdate(map, "account_flag",
						"buyer_code,sku_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户编号，获取折扣
	 * 
	 * @param userCode
	 *            登录用户编号
	 * @param sellerCode
	 *            app编号
	 * @return
	 */
	public double getDiscountForUserCode(String userCode, String sellerCode) {
		if (null == userCode || null == sellerCode || "".equals(userCode)
				|| "".equals(sellerCode)) {
			return 0.05;
		}
		try {
			// 根据member_code获取account_code
			MDataMap accountCodeMap = DbUp.upTable("mc_member_info").oneWhere(
					"account_code", "", "", "member_code", userCode,
					"manage_code", sellerCode);

			// 根据account_code获取清分比例
			MDataMap scaleReckonMap = DbUp.upTable("gc_group_account")
					.oneWhere("scale_reckon", "", "", "account_code",
							accountCodeMap.get("account_code"));

			double scaleReckon = Double.parseDouble(scaleReckonMap
					.get("scale_reckon"));

			// 清分比例不足5%的设置为5%
			return (scaleReckon > 0.05 ? scaleReckon : 0.05); // 返现金额

		} catch (Exception e) {
			// 清分比例不足5%的设置为5%
			return 0.05;
		}
	}

	/**
	 * 解析skuValue 获取sku编号
	 * 
	 */
	public String getSkuCodeForValue(String productCode, String skuKeyValue) {
		if (skuKeyValue != null && !"".equals(skuKeyValue)
				&& !skuKeyValue.contains("&") && !skuKeyValue.contains("=")) {
			return skuKeyValue;
		}
		List<String> skuCode = new ArrayList<String>();
		ProductService service = new ProductService();
		PcProductinfo pc = service.getProduct(productCode);
		if (skuKeyValue != null && !"".equals(skuKeyValue)
				&& productCode != null && !"".equals(productCode)) {
			String skuKey[] = null;
			if (skuKeyValue.contains("&")) {
				skuKey = skuKeyValue.split("&");
			} else {
				skuKey = new String[1];
				skuKey[0] = skuKeyValue;
			}
			if (pc.getProductSkuInfoList() != null
					&& !pc.getProductSkuInfoList().isEmpty()) {
				for (int i = 0; i < pc.getProductSkuInfoList().size(); i++) {
					String ss = pc.getProductSkuInfoList().get(i).getSkuKey();
					if (ss != null && !"".equals(ss)) {
						String ssMap[] = ss.split("&");
						MDataMap mm = new MDataMap();
						for (int d = 0; d < ssMap.length; d++) {
							mm.put(ssMap[d], "");
						}
						int cc = 0;
						for (int j = 0; j < skuKey.length; j++) {
							if (mm.containsKey(skuKey[j])) {
								cc = cc + 1;
							}
						}
						if (cc == skuKey.length) {
							skuCode.add(pc.getProductSkuInfoList().get(i)
									.getSkuCode());
						}
					}
				}
			}
		}
		if (skuCode.size() == 1) {
			return skuCode.get(0);
		} else {
			return "";
		}
	}

	public void saveClienInfo(MDataMap map) {
		try {
			DbUp.upTable("lc_client_info").dataInsert(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 调取微公社返现
	 * 
	 * @param userCode
	 * @param skuCodeArr
	 * @param sellerCode
	 * @return
	 */
	public Map<String, BigDecimal> getScaleReckonMap(String userCode,
			List<String> skuCodeArr, String sellerCode) {
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		ReckonOrderSupport ac = new ReckonOrderSupport();
		String sLevelCode = GroupConst.DEFAULT_LEVEL_CODE; // 默认等级
		if (StringUtils.isNotEmpty(userCode)) {
			// 根据member_code获取account_code
			MDataMap accountCodeMap = DbUp.upTable("mc_member_info").oneWhere(
					"account_code", "", "", "member_code", userCode,
					"manage_code", sellerCode);
			MDataMap accountLevelMap = null;
			if (null != accountCodeMap && !accountCodeMap.isEmpty()) {
				// 根据account_code获取用户等级
				accountLevelMap = DbUp.upTable("gc_group_account").oneWhere(
						"account_level", "", "", "account_code",
						accountCodeMap.get("account_code"));
			}
			if (null != accountLevelMap && !accountLevelMap.isEmpty()) {
				sLevelCode = accountLevelMap.get("account_level");
			}
		}
		for (String sSkuCode : skuCodeArr) {
			GroupLevelInfo levelInfo = ac.upLevelInfoForThird(sLevelCode,
					sellerCode, sSkuCode, FormatHelper.upDateTime(), "0");
			if (null != levelInfo) {
				resultMap.put(sSkuCode, levelInfo.getScaleReckon());
			} else {
				resultMap.put(sSkuCode, BigDecimal.ZERO);
			}
		}
		return resultMap;
	}
	
	/**
	 * 调取微公社返现(为缓存订单准备流程准备的返现方法)
	 * @author xiegj
	 * 
	 * @param userCode
	 * @param skuCodeArr
	 * @param sellerCode
	 * @return
	 */
	public BigDecimal getScaleReckonMapTesla(TeslaXOrder order) {
		BigDecimal result = BigDecimal.ZERO;
		ReckonOrderSupport ac = new ReckonOrderSupport();
		String sLevelCode = GroupConst.DEFAULT_LEVEL_CODE; // 默认等级
		if (StringUtils.isNotEmpty(order.getUorderInfo().getBuyerCode())) {
			// 根据member_code获取account_code
			MDataMap accountCodeMap = DbUp.upTable("mc_member_info").oneWhere(
					"account_code", "", "", "member_code", order.getUorderInfo().getBuyerCode(),
					"manage_code", order.getUorderInfo().getSellerCode());
			MDataMap accountLevelMap = null;
			if (null != accountCodeMap && !accountCodeMap.isEmpty()) {
				// 根据account_code获取用户等级
				accountLevelMap = DbUp.upTable("gc_group_account").oneWhere(
						"account_level", "", "", "account_code",
						accountCodeMap.get("account_code"));
			}
			if (null != accountLevelMap && !accountLevelMap.isEmpty()) {
				sLevelCode = accountLevelMap.get("account_level");
			}
		}
		for (TeslaModelOrderDetail detail : order.getOrderDetails()) {
			GroupLevelInfo levelInfo = ac.upLevelInfoForThird(sLevelCode,
					order.getUorderInfo().getSellerCode(), detail.getSkuCode(), FormatHelper.upDateTime(), "0");
			if (null != levelInfo) {
				result = result.add(levelInfo.getScaleReckon().multiply(detail.getSkuPrice()).multiply(new BigDecimal(detail.getSkuNum())));
			}
		}
		return result;
	}
}
