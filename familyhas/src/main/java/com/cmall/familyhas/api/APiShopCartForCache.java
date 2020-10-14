package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.cmall.familyhas.api.input.APiShopCartForCacheInput;
import com.cmall.familyhas.api.result.APiShopCartForCacheResult;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.service.UserBehaviorsService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.CouponConst;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.xmasorder.model.ShoppingCartCache;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.ShoppingCartItem;
import com.srnpr.xmasorder.model.TeslaModelJJG;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 购物车添加商品接口|在接口测试页面中的名称为：新购物车接口
 * 
 * @author xiegj
 *
 */
public class APiShopCartForCache extends RootApiForVersion<APiShopCartForCacheResult, APiShopCartForCacheInput> {

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	public APiShopCartForCacheResult Process(APiShopCartForCacheInput inputParam,
			MDataMap mRequestMap) {
		APiShopCartForCacheResult result = new APiShopCartForCacheResult();
		Integer isPurchase = inputParam.getIsPurchase();//是否内购标示
		String isMemberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";//用户编号
		ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
		if(getFlagLogin()){
			ShoppingCartCacheInfo info = serviceForCache.queryShopCart(getOauthInfo().getUserCode(),new ArrayList<ShoppingCartGoodsInfoForAdd>());
			//添加是否满足触发发券活动,只有添加了之前从未添加过的sku才计数
			if(AppVersionUtils.compareTo("5.6.2", getApiClient().get("app_vision")) <= 0&&inputParam.getGoodsList().size()>0) {
				boolean flag = true;
				List<ShoppingCartCache> caches = info.getCaches();
				for (ShoppingCartCache shoppingCartCache : caches) {
					if(shoppingCartCache.getSku_code().equals(inputParam.getGoodsList().get(0).getSku_code())) {
						flag=false;
					}
				}
				if(flag) {
					UserBehaviorsService behaviorsService = new UserBehaviorsService();
					behaviorsService.recordUsersBeahviors(CouponConst.add_shop_car_coupon, getOauthInfo().getUserCode(), getOauthInfo().getLoginName(), getManageCode());
				}
			
			}
			serviceForCache.saveShopCart(inputParam.getGoodsList(), getOauthInfo().getUserCode());
		}
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.setChannelId(inputParam.getChannelId());
		teslaXOrder.getStatus().setExecStep(ETeslaExec.shopCart);
		teslaXOrder.setIsPurchase(isPurchase);
		teslaXOrder.setIsMemberCode(isMemberCode);
		ShoppingCartCacheInfo info = serviceForCache.queryShopCart(getOauthInfo()==null?null:getOauthInfo().getUserCode(),inputParam.getGoodsList());
	
		//记录sku加入购物车时的时间(key:sku编号;value:加入购物车的价格;)
		Map<String, BigDecimal> skuPriceMap = new HashMap<String, BigDecimal>();
		
		for (ShoppingCartCache good : info.getCaches()) {
			TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
			orderDetail.setProductCode(good.getProduct_code());
			orderDetail.setSkuCode(good.getSku_code());
			orderDetail.setSkuNum(Long.valueOf(good.getSku_num()).intValue());
			orderDetail.setChoose_flag(good.getChoose_flag());
			orderDetail.setIsSkuPriceToBuy(good.getIsSkuPriceToBuy());
			orderDetail.setFxrcode(good.getFxrcode());
			orderDetail.setTgzUserCode(good.getTgzUserCode());
			orderDetail.setTgzShowCode(good.getTgzShowCode());
			// 购物车忽略IC开头的编号
			if(!good.getSku_code().startsWith("IC")){
				teslaXOrder.getOrderDetails().add(orderDetail);
			}
			skuPriceMap.put(good.getSku_code(), good.getSku_add_shop_price());
			
		}
		teslaXOrder.getUorderInfo().setBuyerCode(getOauthInfo()==null?"":getOauthInfo().getUserCode());
		teslaXOrder.getUorderInfo().setSellerCode(getManageCode());
		TeslaXResult teslaXResult = new ApiConvertTeslaService().ConvertOrder(teslaXOrder);
		
		result.setAcount_num(teslaXOrder.getCartShow().getAcount_num());
		result.setAllDerateMoney(teslaXOrder.getCartShow().getAllDerateMoney());
		result.setAllNormalMoney(teslaXOrder.getCartShow().getAllNormalMoney());
		result.setAllPayMoney(teslaXOrder.getCartShow().getAllPayMoney());
		result.setChooseGoodsNum(teslaXOrder.getCartShow().getChooseGoodsNum());
		result.setDisable_account_num(teslaXOrder.getCartShow().getDisable_account_num());
		result.setDisableSku(teslaXOrder.getCartShow().getDisableSku());
		result.setSalesAdv(teslaXOrder.getCartShow().getSalesAdv());
		List<ShoppingCartItem> shoppingCartList = teslaXOrder.getCartShow().getShoppingCartList();
		//做是否换购条件筛查处理
		//做是否换购条件筛查处理
		if((getFlagLogin()&&AppVersionUtils.compareTo("5.5.80", getApiClient().get("app_vision")) <= 0)) {
			this.checkRepurchase(result,info,teslaXOrder);	
		}
		for (ShoppingCartItem shoppingCartItem : shoppingCartList) {
			
			List<ShoppingCartGoodsInfo> goods = shoppingCartItem.getGoods();
			for (ShoppingCartGoodsInfo shoppingCartGoodsInfo : goods) {
				
				if(skuPriceMap.isEmpty() || StringUtil.isBlank(String.valueOf(skuPriceMap.get(shoppingCartGoodsInfo.getSku_code())))) {
					continue;
				}
				//判断加入购物车的金额是否大于现在的金额（如果大于则添加降价提示）
				BigDecimal subMoney = skuPriceMap.get(shoppingCartGoodsInfo.getSku_code()).subtract(new BigDecimal(shoppingCartGoodsInfo.getSku_price()));
				subMoney = subMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
				if(subMoney.doubleValue() > 0) {
					shoppingCartGoodsInfo.setSub_price_title(FormatHelper.formatString(bConfig("familyhas.shopCart_sub_price_title"),subMoney  ));
				}
				//524:把商品分类标签加载商品列表当中比较合适 需要改为走缓存
				PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(shoppingCartGoodsInfo.getProduct_code()));
				String ssc =productInfo.getSmallSellerCode();
				shoppingCartGoodsInfo.setDlrCharge(productInfo.getDlrCharge());
				String st="";
				if("SI2003".equals(ssc)) {
					st="4497478100050000";
					// 先去查一下最小起订量,最小起订量如果＞5则不提示
					Map<String, Object> skuInfoMap = DbUp.upTable("pc_skuinfo").dataSqlOne(
							"SELECT pc_skuinfo.mini_order FROM pc_skuinfo WHERE pc_skuinfo.sku_code =:sku_code", 
								new MDataMap("sku_code", shoppingCartGoodsInfo.getSku_code()));
					int mini_order = (int) skuInfoMap.get("mini_order");
					if(mini_order <= 5) {						
						// 544需求添加:LD品一次购买最多到5件
						if(shoppingCartGoodsInfo.getLimit_order_num() > 5) {
							shoppingCartGoodsInfo.setLimit_order_num(5);
						}
					}
				}
				else {
					PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(ssc));
					st = sellerInfo.getUc_seller_type();
				}
				//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
				Map productTypeMap = WebHelper.getAttributeProductType(st);
				
				shoppingCartGoodsInfo.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
				//查询该商品用户是否收藏
				int temp = DbUp.upTable("fh_product_collection").dataCount(
						"member_code = '"+isMemberCode+"' and operate_type = '4497472000020001' and product_code = :product_code", 
							new MDataMap("product_code", shoppingCartGoodsInfo.getProduct_code()));
				if(temp>0){
					shoppingCartGoodsInfo.setIsCollect("Y");
				}
				
			}
			
		}
		result.setShoppingCartList(shoppingCartList);
		
		result.setDisableGoods(teslaXOrder.getCartShow().getDisableGoods());
		
		String maybeLove = XmasKv.upFactory(EKvSchema.ShoppingCartMaybeLove).get("maybeLove");
		result.setMaybeLove(StringUtils.isBlank(maybeLove) ? "4497480100020002" : maybeLove);  // 默认为否
//		result.setResultCode(teslaXResult.getResultCode());
//		result.setResultMessage(teslaXResult.getResultMessage());
		
		// 提取参与橙意卡专享的sku
		Map<String,String> plusEventSkuMap = new HashMap<String, String>();
		for(TeslaModelOrderActivity oa : teslaXOrder.getActivityList()) {
			if("4497472600010026".equals(oa.getActivityType())) {
				plusEventSkuMap.put(oa.getSkuCode()+oa.getOrderCode(), oa.getActivityName());
			}
		}
		
		//5.4.2新需求新加字段TagList活动标签字段
		List<ShoppingCartItem> shoppingCarItems = result.getShoppingCartList();
		LoadEventInfo load = new LoadEventInfo();
		PlusModelEventQuery query = new PlusModelEventQuery();
		for(ShoppingCartItem item : shoppingCarItems) {
			String eventCode = item.getEventCode();//获取活动编号
			query.setCode(eventCode);
			PlusModelEventInfo eventInfo = null;
			if(!StringUtils.isEmpty(eventCode)) {
				eventInfo = load.upInfoByCode(query);
			}
			String eventType = "";
			if(eventInfo != null) {
				eventType = eventInfo.getEventType();
			}
			//（秒杀：）、（闪购：4497472600010005）、（拼团：4497472600010024）、（特价：4497472600010002）、（会员日：4497472600010018）、（满减：4497472600010008）才会入List
			List<ShoppingCartGoodsInfo> goods = item.getGoods();
			for(ShoppingCartGoodsInfo good : goods) {
				// 分销商品不展示活动标签
				if(good.getFxFlag() == 1) {
					continue;
				}
				
				String skuCode = good.getSku_code();
				ProductService ps = new ProductService();
				List<String> tagList = ps.getTagListBySkuCode(skuCode,isMemberCode,inputParam.getChannelId());
				
				ps.addTagInfo(good.getTagInfoList(), TagInfo.Style.Normal, tagList.toArray(new String[0]));
				if(plusEventSkuMap.containsKey(skuCode+good.getOrder_code())) {
					ps.addTagInfo(good.getTagInfoList(), TagInfo.Style.VipCard, plusEventSkuMap.get(skuCode));
				}
				
				good.setTagList(tagList);
				good.setLabelsInfo(new ProductLabelService().getLabelInfoList(good.getProduct_code()));
				//562版本对于商品列表标签做版本兼容处理
				String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
				if(appVersion.compareTo("5.6.2")<0){
					Iterator<PlusModelProductLabel> iter = good.getLabelsInfo().iterator();
					while (iter.hasNext()) {
						PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
						if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
							iter.remove();
						}
					}
				}
			}
		}
		
		return result;
	}

	private void checkRepurchase(APiShopCartForCacheResult result,ShoppingCartCacheInfo info, TeslaXOrder teslaXOrder) {
		// TODO Auto-generated method stub
		Double allPayMoney = result.getAllPayMoney();
		result.setExcludeJJGMoney(allPayMoney);
		Double allNormalMoney = result.getAllNormalMoney();
		if(allPayMoney>0) {
			List<TeslaModelJJG> jjgList = info.getJJGList();
			List<TeslaModelJJG> temJjgList = new ArrayList<TeslaModelJJG>();
			boolean refreshFlag = false;
			String currentTime = DateUtil.getSysDateTimeString();
			List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_event_info").dataSqlList("select * from sc_event_info where event_type_code='4497472600010025' and event_status='4497472700020002' and begin_time<='"+currentTime+"' and end_time>'"+currentTime+"'", null);
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				BigDecimal allPM = new BigDecimal(allPayMoney);
				BigDecimal allPayMoneyCopy = new BigDecimal(allPayMoney);
				BigDecimal allNormalMoneyCopy = new BigDecimal(allNormalMoney);
				List<String> validList = new ArrayList<String>();
				for (Map<String, Object> map : dataSqlList) {
					RepurchaseEvent repurchaseEvent = new RepurchaseEvent();
					validList.add(map.get("event_code").toString());
					repurchaseEvent.setEvent_code(map.get("event_code").toString());
					repurchaseEvent.setEvent_name(map.get("event_name").toString());
					repurchaseEvent.setRepurchase_num(Integer.parseInt(map.get("repurchase_num").toString()));
					String repurchase_condition = map.get("repurchase_condition").toString();
					BigDecimal repurchaseCondition = new BigDecimal(Double.parseDouble(repurchase_condition));
					if(repurchaseCondition.compareTo(allPM)<=0) {
						//满足加价购条件则判断是否在该加价购活动中已选商品，若有选择则进行在总金额中加入加价购选品金额
						repurchaseEvent.setFlag("1");
						for (TeslaModelJJG jjg : jjgList) {
							if(map.get("event_code").toString().equals(jjg.getEventCode())) {
								String[] skuCodes = jjg.getSkuCodes().split(",");
								for (String skuCode : skuCodes) {
									Map<String, Object> dataSqlOne = DbUp.upTable("sc_event_item_product").dataSqlOne("select * from sc_event_item_product where event_code=:event_code and sku_code=:sku_code and flag_enable=1", new MDataMap("sku_code",skuCode,"event_code",jjg.getEventCode()));
									if(dataSqlOne!=null) {
										BigDecimal favorable_price = new BigDecimal(Double.parseDouble(dataSqlOne.get("favorable_price").toString()));
										allPayMoneyCopy = allPayMoneyCopy.add(favorable_price);
										allNormalMoneyCopy = allNormalMoneyCopy.add(favorable_price);
										//购物车中已有该活动下的的换购商品，状态置2为去修改换购商品
										repurchaseEvent.setFlag("2");
									}
								}
							}
						}
					}else {
						//不满足条件的额，换购商品清空		
						for (TeslaModelJJG jjg : jjgList) {
							if(map.get("event_code").toString().equals(jjg.getEventCode())) {
								temJjgList.add(jjg);
								refreshFlag=true;
							}
						}
						BigDecimal subtract = repurchaseCondition.subtract(allPM);
						subtract.setScale(2,BigDecimal.ROUND_CEILING);
						subtract = MoneyHelper.roundHalfUp(subtract);
						repurchaseEvent.setValue(subtract.doubleValue());
					}
					result.getRepurchaseEventList().add(repurchaseEvent);
				}
				//删除保存的过期的换购活动的商品
				for (TeslaModelJJG jjg : jjgList) {
					if(!validList.contains(jjg.getEventCode())) {
						temJjgList.add(jjg);
						refreshFlag=true;
					}
				}
				if(refreshFlag) {
					jjgList.removeAll(temJjgList);
					info.setJJGList(jjgList);
					XmasKv.upFactory(EKvSchema.ShopCart).set(getOauthInfo().getUserCode(), GsonHelper.toJson(info));
					teslaXOrder.setJJGList(jjgList);
				}
				//allPayMoneyCopy.setScale(2,BigDecimal.ROUND_CEILING);
				allPayMoneyCopy=MoneyHelper.roundHalfUp(allPayMoneyCopy);
				allNormalMoneyCopy = MoneyHelper.roundHalfUp(allNormalMoneyCopy);
				result.setAllPayMoney(allPayMoneyCopy.doubleValue());
				result.setAllNormalMoney(allNormalMoneyCopy.doubleValue());
			}
		}else {
			//清除之前已选择的换购品
			List<TeslaModelJJG> temjjgList = new ArrayList<TeslaModelJJG>();
			info.setJJGList(temjjgList);
			XmasKv.upFactory(EKvSchema.ShopCart).set(getOauthInfo().getUserCode(), GsonHelper.toJson(info));
			teslaXOrder.setJJGList(temjjgList);
		}
	}

}
