package com.cmall.familyhas.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.familyhas.util.MD5Util;
import com.cmall.ordercenter.model.FlashsalesSkuInfo;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.service.DefineService;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusActiveProduct;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;

public class HomeColumnServiceforYuLan extends BaseClass{
	private ProductLabelService productLabelService = new ProductLabelService();
	public Map<String, HomeColumnContentProductInfo> getProductInfo(List<String> produtCodeArr, String userType,String userCode,String channelId){
		return getProductInfo(produtCodeArr, userType,userCode,0,channelId);
	}
	public Map<String, HomeColumnContentProductInfo> getProductInfo(List<String> produtCodeArr, String userType,String userCode,Integer isPurchase,String channelId) {
		ProductService pService = new ProductService();
		Map<String, HomeColumnContentProductInfo> resultMap = new HashMap<String, HomeColumnContentProductInfo>();
		if (null == produtCodeArr || produtCodeArr.size() < 1) {
			return resultMap;
		}
		
		// 获取商品价格
		Map<String, BigDecimal> minSellPriceProduct = new HashMap<String, BigDecimal>();
//		if (VersionHelper.checkServerVersion("3.5.62.55")) {
//			minSellPriceProduct = pService.getMinProductActivityNew(
//					produtCodeArr, userType); 
//			if (VersionHelper.checkServerVersion("3.5.95.55")) {
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(StringUtils.join(produtCodeArr,","));
				skuQuery.setMemberCode(userCode);
				skuQuery.setIsPurchase(isPurchase);
				skuQuery.setChannelId(channelId);
				// 获取商品最低销售价格
				minSellPriceProduct = new ProductPriceService().getProductMinPrice(skuQuery);
//			}else{
//				minSellPriceProduct = new ProductService().getMinProductActivityNew(produtCodeArr, userType);// 获取商品最低销售价格
//			}
//		} else {
//			minSellPriceProduct = pService.getMinProductActivity(produtCodeArr);
//		}
		
		String sFields = "product_code,product_name,min_sell_price,market_price,mainpic_url,small_seller_code,adpic_url,tv_tips";
		String sWhere = " product_code in ('" + StringUtils.join(produtCodeArr, "','") + "')"; 
		List<MDataMap> productInfoMapList = DbUp.upTable("pc_productinfo").queryAll(sFields, "", sWhere, null);
		
		for (MDataMap productInfoMap : productInfoMapList) {
			HomeColumnContentProductInfo productInfo = new HomeColumnContentProductInfo();
			String min_sell_price = productInfoMap.get("min_sell_price");
			String market_price = productInfoMap.get("market_price");

			String discount = "";
			BigDecimal sellPrice = minSellPriceProduct.get(productInfoMap.get("product_code"));
			BigDecimal marketPrice = BigDecimal.ZERO;
			if (null!=sellPrice) {
//				sellPrice = sellPrice.setScale(0,BigDecimal.ROUND_DOWN);
				sellPrice = MoneyHelper.roundHalfUp(sellPrice); // 兼容小数 - Yangcl  
			}else{
				sellPrice = BigDecimal.ZERO;
			}
			if (StringUtils.isNotEmpty(market_price)) {
//				marketPrice = BigDecimal.valueOf(Double.parseDouble(market_price)).setScale(0,BigDecimal.ROUND_DOWN);
				marketPrice = MoneyHelper.roundHalfUp(BigDecimal.valueOf(Double.parseDouble(market_price)));  // 兼容小数 - Yangcl  
			}
			
			min_sell_price = sellPrice.toString();
			market_price = marketPrice.toString();
			if (sellPrice.compareTo(BigDecimal.ZERO) > 0
					&& marketPrice.compareTo(BigDecimal.ZERO) > 0
					&& sellPrice.compareTo(marketPrice) < 0) {
				discount = ("" + sellPrice.multiply(new BigDecimal(10)).divide(marketPrice, 1, BigDecimal.ROUND_HALF_UP));
				if (discount.indexOf(".") > 0) {
					// 正则表达
					discount = discount.replaceAll("0+?$", "");// 去掉后面无用的零
					discount = discount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
				}
				if (StringUtils.isNotBlank(discount) && Double.parseDouble(discount) <= 0) {
					discount = "";
				}
			}
			productInfo.setProductCode(productInfoMap.get("product_code"));
			productInfo.setProductName(productInfoMap.get("product_name"));
			productInfo.setMainpicUrl(productInfoMap.get("mainpic_url"));
			if("SI2003".equals(productInfoMap.get("small_seller_code"))) {
				//取productcenter.pc_productadpic中的图片
				productInfo.setAdPicUrl(getPcProductAdpic(productInfo.getProductCode()));
			} else {
				productInfo.setAdPicUrl(productInfoMap.get("adpic_url"));
			}			
			productInfo.setSellPrice(min_sell_price);
			productInfo.setMarkPrice(market_price);
			productInfo.setDiscount(discount);
			productInfo.setTvTips(productInfoMap.get("tv_tips"));
//			if (AppConst.MANAGE_CODE_KJT.equals(productInfoMap
//					.get("small_seller_code"))||AppConst.MANAGE_CODE_MLG.equals(productInfoMap
//							.get("small_seller_code"))||AppConst.MANAGE_CODE_QQT.equals(productInfoMap
//									.get("small_seller_code"))||AppConst.MANAGE_CODE_SYC.equals(productInfoMap
//											.get("small_seller_code"))||AppConst.MANAGE_CODE_CYGJ.equals(productInfoMap
//													.get("small_seller_code"))) {
				
			if(new PlusServiceSeller().isKJSeller(productInfoMap.get("small_seller_code"))){
				productInfo.setFlagTheSea("1");
			}
			// 缓存获取商品信息
			PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
			//加上tryCatch可以防止首页接口因为商品缓存报错
			try {
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productInfo.getProductCode()));
			} catch (Exception e) {
				XmasKv.upFactory(EKvSchema.Product).del(productInfo.getProductCode());
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productInfo.getProductCode()));
			}
			productInfo.setLabelsList(plusModelProductinfo.getLabelsList());
			productInfo.setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
			resultMap.put(productInfo.getProductCode(), productInfo);
		}
		return resultMap;
	}

	public List<HomeColumnContent> getFlashActivity(String maxWidth) {
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		Map<String, FlashsalesSkuInfo> retMap = new HashMap<String, FlashsalesSkuInfo>();
		String systemTime = DateUtil.getSysDateTimeString();
		ProductService pService = new ProductService();
		String flashSql = " SELECT k.sell_price,k.vip_price,k.sku_name,k.sku_code,k.start_time,k.end_time, k.on_status,k.location from ( "
				+ "(SELECT  s.sell_price,s.vip_price,s.sku_name,s.sku_code,a.start_time,a.end_time,0 as on_status,s.location from oc_flashsales_skuInfo s LEFT JOIN oc_activity_flashsales a ON s.activity_code=a.activity_code where "
				+ " a.activity_code=(select activity_code from oc_activity_flashsales where start_time<='"
				+ systemTime
				+ "' and end_time>='"
				+ systemTime
				+ "' and  status='449746740002' and app_code='SI2003' order by end_time desc limit 0,1 )  "
				+ " and s.status!='449746810002' and a.app_code='SI2003')) k ";
		List<Map<String, Object>> list = DbUp.upTable("oc_activity_flashsales")
				.dataSqlList(flashSql, null);

		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				FlashsalesSkuInfo skuInfo = new FlashsalesSkuInfo();
				skuInfo.setEnd_time((String) map.get("end_time"));
				skuInfo.setStart_time((String) map.get("start_time"));
				skuInfo.setSku_code((String) map.get("sku_code"));
				skuInfo.setSku_name((String) map.get("sku_name"));
				skuInfo.setVip_price((BigDecimal) map.get("vip_price"));
				skuInfo.setSell_price((BigDecimal) map.get("sell_price"));
				skuInfo.setOn_status(Integer.valueOf(map.get("on_status") + ""));
				skuInfo.setLocation((Integer) map.get("location"));

				if (skuInfo.getSell_price().compareTo(BigDecimal.ZERO) > 0
						&& skuInfo.getVip_price().compareTo(BigDecimal.ZERO) > 0
						&& skuInfo.getVip_price().compareTo(skuInfo.getSell_price()) < 0) {
					String discountStr = ""
							+ skuInfo
									.getVip_price()
									.multiply(new BigDecimal(10))
									.divide(skuInfo.getSell_price(), 1,
											BigDecimal.ROUND_HALF_UP);
					if (discountStr.indexOf(".") > 0) {
						// 正则表达
						discountStr = discountStr.replaceAll("0+?$", "");// 去掉后面无用的零
						discountStr = discountStr.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
					}
					if (StringUtils.isNotBlank(discountStr) && Double.parseDouble(discountStr) <= 0) {
						discountStr = "";
					}
					skuInfo.setDiscountRate(discountStr);
				}
				// 通过sku_code 查询商品信息
				String product_status = "";
				List<Map<String, Object>> list1 = DbUp
						.upTable("pc_skuinfo")
						.dataSqlList(
								"SELECT p.video_url,p.mainpic_url,p.product_status,p.market_price,p.product_code,p.product_name,p.cost_price from pc_skuinfo s LEFT JOIN pc_productinfo p on s.product_code=p.product_code where s.sku_code=:sku_code",
								new MDataMap("sku_code", skuInfo.getSku_code()));
				if (list1 != null && list1.size() > 0) {
					// 设置商品信息
					Map<String, Object> pc = list1.get(0);
					// String video_url=(String)pc.get("video_url");
					String mainpic_url = (String) pc.get("mainpic_url");
					product_status = (String) pc.get("product_status");
					BigDecimal market_price = (BigDecimal) pc
							.get("market_price");
					// 过滤市场价为0的商品 ligj
					if (market_price.compareTo(new BigDecimal(0)) == 0) {
						continue;
					}
					if (!"4497153900060002".equals(product_status)) { // 只要已上架的商品
						continue;
					}
					skuInfo.setSell_price(market_price);// 这里把销售价格替换成市场价格
					skuInfo.setImg_url(mainpic_url);

					// 由原SKU信息变为商品信息 若需还原，将以下代码注掉即可
					// start
					String product_code = (String) pc.get("product_code");
					if (retMap.containsKey(product_code + "_"
							+ skuInfo.getOn_status())) { // 如果已经存在此商品，就不再添加,然后把sku的最小价格添加的商品属性中
						BigDecimal vip_price_new = skuInfo.getVip_price();
						FlashsalesSkuInfo sku_old = retMap.get(product_code
								+ "_" + skuInfo.getOn_status());
						if (vip_price_new.compareTo(sku_old.getVip_price()) < 0) {
							sku_old.setVip_price(vip_price_new);
						}
						continue;
					}

					skuInfo.setSku_code(product_code);
					skuInfo.setSku_name((String) pc.get("product_name"));

					if (skuInfo.getSell_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(skuInfo.getSell_price()) < 0) {
						String discountStr = ""+ skuInfo.getVip_price().multiply(new BigDecimal(10)).divide(skuInfo.getSell_price(), 1,
												BigDecimal.ROUND_HALF_UP);
						if (discountStr.indexOf(".") > 0) {
							// 正则表达
							discountStr = discountStr.replaceAll("0+?$", "");// 去掉后面无用的零
							discountStr = discountStr.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
						}
						if (StringUtils.isNotBlank(discountStr) && Double.parseDouble(discountStr) <= 0) {
							discountStr = "";
						}
						skuInfo.setDiscountRate(discountStr);
					} else {
						skuInfo.setDiscountRate("");
					}
					retMap.put(
							skuInfo.getSku_code() + "_"
									+ skuInfo.getOn_status(), skuInfo);
				}
			}
		}
		List<FlashsalesSkuInfo> flashsalesList = new ArrayList<FlashsalesSkuInfo>();

		for (Map.Entry<String, FlashsalesSkuInfo> entry : retMap.entrySet()) {
			FlashsalesSkuInfo skuInfo = entry.getValue();
			skuInfo.setSell_count(0);
			skuInfo.setSales_num(0);
			flashsalesList.add(skuInfo);
		}
		sortSkuList(flashsalesList);
		List<String> productCodes = new ArrayList<String>();
		List<String> picArrFlashsales = new ArrayList<String>();
		Map<String,String> picMapFlashsales = new HashMap<String, String>();
		for (int i = 0; i < flashsalesList.size(); i++) {
			if (i >= 3) {
				break;
			}
			productCodes.add(flashsalesList.get(i).getSku_code());
			picArrFlashsales.add(flashsalesList.get(i).getImg_url());
		}

		//闪购压缩图片
		List<PicInfo> picList = pService.getPicInfoOprBigForMulti(
				Integer.parseInt(maxWidth), picArrFlashsales);
		for (PicInfo picInfo : picList) {
			picMapFlashsales.put(picInfo.getPicOldUrl(),picInfo.getPicNewUrl());
		}
		
		for (int i = 0; i < flashsalesList.size(); i++) {
			if (i >= 3) {
				break;
			}
			FlashsalesSkuInfo flashObj = flashsalesList.get(i);
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.setEndTime(flashObj.getEnd_time());
			columnContent.setStartTime(flashObj.getStart_time());
			// 到后面会改成product_code
			columnContent.getProductInfo().setProductCode(
					flashObj.getSku_code());
			columnContent.getProductInfo().setProductName(
					flashObj.getSku_name());
			columnContent.getProductInfo().setMarkPrice(
					flashObj.getSell_price().toString());
			columnContent.getProductInfo().setMainpicUrl(flashObj.getImg_url());
			columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
			columnContent.setShowmoreLinkvalue(flashObj.getSku_code()); // 商品Code
			columnContent.setPicture(picMapFlashsales.get(flashObj.getImg_url()));
			columnContent.getProductInfo().setSellPrice(
					flashObj.getVip_price().toString());
			columnContent.getProductInfo().setDiscount(
					flashObj.getDiscountRate());
			if (new ProductService().checkProductKjt(flashObj.getSku_code())) {
				columnContent.getProductInfo().setFlagTheSea("1");
			}
			columnContentList.add(columnContent);
		}
		return columnContentList;
	}

	// 闪购最新方法，走促销活动
	public List<HomeColumnContent> getFlashActivityNew(String sellerCode,String maxWidth,String picType,String channelId) {
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		Map<String, FlashsalesSkuInfo> retMap = new HashMap<String, FlashsalesSkuInfo>();
		PlusActiveProduct activeProduct = new PlusActiveProduct();
		ProductService pService = new ProductService();
		List<MDataMap> activeProductList = activeProduct
				.getActiveProduct(sellerCode,channelId);
		if (activeProductList != null && activeProductList.size() > 0) {
			for (MDataMap mDataMap : activeProductList) {
				FlashsalesSkuInfo skuInfo = new FlashsalesSkuInfo();
				skuInfo.setEnd_time(mDataMap.get("end_time"));
				skuInfo.setStart_time(mDataMap.get("begin_time"));
				skuInfo.setSku_code(mDataMap.get("sku_code"));
				skuInfo.setVip_price(new BigDecimal(mDataMap
						.get("favorable_price")));
				skuInfo.setLocation(Integer.parseInt(StringUtils
						.isEmpty(mDataMap.get("seat")) ? "0" : mDataMap
						.get("seat")));
				// 通过sku_code 查询商品信息
				List<Map<String, Object>> list1 = DbUp
						.upTable("pc_skuinfo")
						.dataSqlList(
								"SELECT p.video_url,p.mainpic_url,p.product_status,p.market_price,p.product_code,p.product_name,p.cost_price,s.sku_code from pc_skuinfo s LEFT JOIN pc_productinfo p on s.product_code=p.product_code where s.sku_code=:sku_code",
								new MDataMap("sku_code", skuInfo.getSku_code()));
				if (list1 != null && list1.size() > 0) {
					// 设置商品信息
					Map<String, Object> pc = list1.get(0);
					String mainpic_url = (String) pc.get("mainpic_url");
					BigDecimal market_price = (BigDecimal) pc
							.get("market_price");
					// 过滤市场价为0的商品 ligj
					// if (market_price.compareTo(new BigDecimal(0)) == 0) {
					// continue;
					// }
					// if(!"4497153900060002".equals(product_status)){
					// //只要已上架的商品
					// continue;
					// }
					skuInfo.setSell_price(market_price);// 这里把销售价格替换成市场价格
					skuInfo.setImg_url(mainpic_url);
					skuInfo.setSku_name((String) pc.get("product_name"));
					if (skuInfo.getSell_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(skuInfo.getSell_price()) < 0) {
						String discountStr = ""
								+ skuInfo
										.getVip_price()
										.multiply(new BigDecimal(10))
										.divide(skuInfo.getSell_price(), 1,
												BigDecimal.ROUND_HALF_UP);
						if (discountStr.indexOf(".") > 0) {
							// 正则表达
							discountStr = discountStr.replaceAll("0+?$", "");// 去掉后面无用的零
							discountStr = discountStr.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
						}
						if (StringUtils.isNotBlank(discountStr) && Double.parseDouble(discountStr) <= 0) {
							discountStr = "";
						}
						skuInfo.setDiscountRate(discountStr);
					} else {
						skuInfo.setDiscountRate("");
					}
					//打开商品详情页需要传入product_code,为了兼容，再次把product_code放到sku_code上
					skuInfo.setSku_code((String) pc.get("product_code"));
					retMap.put((String) pc.get("sku_code"), skuInfo);
				}
			}
		}
		List<FlashsalesSkuInfo> flashsalesList = new ArrayList<FlashsalesSkuInfo>();

		for (MDataMap mDataMap :  activeProductList) {
			FlashsalesSkuInfo skuInfo =retMap.get(mDataMap.get("sku_code"));
			skuInfo.setSell_count(0);
			skuInfo.setSales_num(0);
			flashsalesList.add(skuInfo);
		}
		List<String> productCodes = new ArrayList<String>();
		List<String> picArrFlashsales = new ArrayList<String>();
		Map<String,String> picMapFlashsales = new HashMap<String, String>();
		for (int i = 0; i < flashsalesList.size(); i++) {
			if (i >= 3) {
				break;
			}
			productCodes.add(flashsalesList.get(i).getSku_code());
			picArrFlashsales.add(flashsalesList.get(i).getImg_url());
		}

		//闪购压缩图片
		List<PicInfo> picList = pService.getPicInfoOprBigForMulti(
				Integer.parseInt(maxWidth), picArrFlashsales,picType);
		for (PicInfo picInfo : picList) {
			picMapFlashsales.put(picInfo.getPicOldUrl(),picInfo.getPicNewUrl());
		}
		for (int i = 0; i < flashsalesList.size(); i++) {
			if (i >= 3) {
				break;
			}
			FlashsalesSkuInfo flashObj = flashsalesList.get(i);
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.setEndTime(flashObj.getEnd_time());
			columnContent.setStartTime(flashObj.getStart_time());
			// flashObj.getSku_code()在上面已经被替换成product_code
			columnContent.getProductInfo().setProductCode(
					flashObj.getSku_code());
			columnContent.getProductInfo().setProductName(
					flashObj.getSku_name());
			columnContent.getProductInfo().setMarkPrice(
					flashObj.getSell_price().toString());
			columnContent.getProductInfo().setMainpicUrl(flashObj.getImg_url());
			columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
			columnContent.setShowmoreLinkvalue(flashObj.getSku_code()); // 商品Code
			columnContent.setPicture(picMapFlashsales.get(flashObj.getImg_url()));
			columnContent.getProductInfo().setSellPrice(
					flashObj.getVip_price().toString());
			columnContent.getProductInfo().setDiscount(
					flashObj.getDiscountRate());
			if (new ProductService().checkProductKjt(flashObj.getSku_code())) {
				columnContent.getProductInfo().setFlagTheSea("1");
			}
			// 缓存获取商品信息
			PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
			try {
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(flashObj.getSku_code()));
			} catch (Exception e) {
				XmasKv.upFactory(EKvSchema.Product).del(flashObj.getSku_code());
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(flashObj.getSku_code()));
			}
			columnContent.getProductInfo().setLabelsList(plusModelProductinfo.getLabelsList());
			columnContent.getProductInfo().setLabelsPic(productLabelService.getLabelInfo(flashObj.getSku_code()).getListPic());
			columnContentList.add(columnContent);
		}
		return columnContentList;
	}
	
	// 闪购pc用
	public List<HomeColumnContent> getFlashActivityPc(String sellerCode,String maxWidth,String picType) {
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		Map<String, FlashsalesSkuInfo> retMap = new HashMap<String, FlashsalesSkuInfo>();
		PlusActiveProduct activeProduct = new PlusActiveProduct();
		ProductService pService = new ProductService();
		List<MDataMap> activeProductList = activeProduct
				.getActiveProductPc(sellerCode);
		if (activeProductList != null && activeProductList.size() > 0) {
			for (MDataMap mDataMap : activeProductList) {
				FlashsalesSkuInfo skuInfo = new FlashsalesSkuInfo();
				skuInfo.setEnd_time(mDataMap.get("end_time"));
				skuInfo.setStart_time(mDataMap.get("begin_time"));
				skuInfo.setSku_code(mDataMap.get("sku_code"));
				skuInfo.setVip_price(new BigDecimal(mDataMap
						.get("favorable_price")));
				skuInfo.setLocation(Integer.parseInt(StringUtils
						.isEmpty(mDataMap.get("seat")) ? "0" : mDataMap
						.get("seat")));
				// 通过sku_code 查询商品信息
				List<Map<String, Object>> list1 = DbUp
						.upTable("pc_skuinfo")
						.dataSqlList(
								"SELECT p.video_url,p.mainpic_url,p.product_status,p.market_price,p.product_code,p.product_name,p.cost_price,s.sku_code from pc_skuinfo s LEFT JOIN pc_productinfo p on s.product_code=p.product_code where s.sku_code=:sku_code",
								new MDataMap("sku_code", skuInfo.getSku_code()));
				if (list1 != null && list1.size() > 0) {
					// 设置商品信息
					Map<String, Object> pc = list1.get(0);
					String mainpic_url = (String) pc.get("mainpic_url");
					BigDecimal market_price = (BigDecimal) pc
							.get("market_price");
					skuInfo.setSell_price(market_price);// 这里把销售价格替换成市场价格
					skuInfo.setImg_url(mainpic_url);
					skuInfo.setSku_name((String) pc.get("product_name"));
					if (skuInfo.getSell_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(BigDecimal.ZERO) > 0
							&& skuInfo.getVip_price().compareTo(skuInfo.getSell_price()) < 0) {
						String discountStr = ""
								+ skuInfo
										.getVip_price()
										.multiply(new BigDecimal(10))
										.divide(skuInfo.getSell_price(), 1,
												BigDecimal.ROUND_HALF_UP);
						if (discountStr.indexOf(".") > 0) {
							// 正则表达
							discountStr = discountStr.replaceAll("0+?$", "");// 去掉后面无用的零
							discountStr = discountStr.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
						}
						if (StringUtils.isNotBlank(discountStr) && Double.parseDouble(discountStr) <= 0) {
							discountStr = "";
						}
						skuInfo.setDiscountRate(discountStr);
					} else {
						skuInfo.setDiscountRate("");
					}
					//打开商品详情页需要传入product_code,为了兼容，再次把product_code放到sku_code上
					skuInfo.setSku_code((String) pc.get("product_code"));
					retMap.put((String) pc.get("sku_code"), skuInfo);
				}
			}
		}
		List<FlashsalesSkuInfo> flashsalesList = new ArrayList<FlashsalesSkuInfo>();

		for (MDataMap mDataMap :  activeProductList) {
			FlashsalesSkuInfo skuInfo =retMap.get(mDataMap.get("sku_code"));
			skuInfo.setSell_count(0);
			skuInfo.setSales_num(0);
			flashsalesList.add(skuInfo);
		}
		List<String> productCodes = new ArrayList<String>();
		List<String> picArrFlashsales = new ArrayList<String>();
		Map<String,String> picMapFlashsales = new HashMap<String, String>();
		for (int i = 0; i < flashsalesList.size(); i++) {
			productCodes.add(flashsalesList.get(i).getSku_code());
			picArrFlashsales.add(flashsalesList.get(i).getImg_url());
		}

		//闪购压缩图片
		List<PicInfo> picList = pService.getPicInfoOprBigForMulti(
				Integer.parseInt(maxWidth), picArrFlashsales,picType);
		for (PicInfo picInfo : picList) {
			picMapFlashsales.put(picInfo.getPicOldUrl(),picInfo.getPicNewUrl());
		}
		for (int i = 0; i < flashsalesList.size(); i++) {
			FlashsalesSkuInfo flashObj = flashsalesList.get(i);
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.setEndTime(flashObj.getEnd_time());
			columnContent.setStartTime(flashObj.getStart_time());
			// flashObj.getSku_code()在上面已经被替换成product_code
			columnContent.getProductInfo().setProductCode(
					flashObj.getSku_code());
			columnContent.getProductInfo().setProductName(
					flashObj.getSku_name());
			columnContent.getProductInfo().setMarkPrice(
					flashObj.getSell_price().toString());
			columnContent.getProductInfo().setMainpicUrl(flashObj.getImg_url());
			columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
			columnContent.setShowmoreLinkvalue(flashObj.getSku_code()); // 商品Code
			columnContent.setPicture(picMapFlashsales.get(flashObj.getImg_url()));
			columnContent.getProductInfo().setSellPrice(
					flashObj.getVip_price().toString());
			columnContent.getProductInfo().setDiscount(
					flashObj.getDiscountRate());
			if (new ProductService().checkProductKjt(flashObj.getSku_code())) {
				columnContent.getProductInfo().setFlagTheSea("1");
			}
			// 缓存获取商品信息
			PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
			try {
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(flashObj.getSku_code()));
			} catch (Exception e) {
				XmasKv.upFactory(EKvSchema.Product).del(flashObj.getSku_code());
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(flashObj.getSku_code()));
			}
			columnContent.getProductInfo().setLabelsList(plusModelProductinfo.getLabelsList());
			columnContent.getProductInfo().setLabelsPic(productLabelService.getLabelInfo(flashObj.getSku_code()).getListPic());
			columnContentList.add(columnContent);
		}
		return columnContentList;
	}

	public List<HomeColumnContent> getTVDataList(String userType,String maxWidth,String picType,String userCode , String viewType,String appVersion,String systemTime,String channelId){
		return getTVDataList(userType,maxWidth,picType,userCode , viewType,appVersion,0,systemTime,channelId);
	} 
	
	public List<HomeColumnContent> getTVDataList(String userType,String maxWidth,String picType,String userCode , String viewType,String appVersion,Integer isPurchase,String systemTime,String channelId) {
		systemTime = StringUtils.isBlank(systemTime)?DateUtil.getSysDateTimeString():systemTime;
		String videoUrlTV = bConfig("familyhas.video_url_TV");
		ProductService pService = new ProductService();
		// TV直播链接
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		
		String swhere = "form_fr_date<='" + systemTime
					+ "' and form_end_date >= '" + systemTime + "' and so_id='1000001'";
		
		List<MDataMap> TVlistI = DbUp.upTable("pc_tv").query("",
				"form_fr_date", swhere, new MDataMap(),-1,-1);
		//过滤下架商品，只取15条-->add 2016-06-13
		List<MDataMap> TVlist = new ArrayList<MDataMap>();
		if (TVlistI != null && TVlistI.size() > 0) {
			// 获取商品信息
			PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
			for (MDataMap mDataMap : TVlistI) {
				String productCode = mDataMap.get("good_id");
				if (StringUtils.isNotEmpty(productCode)) {
					plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
					if ("4497153900060002".equals(plusModelProductinfo.getProductStatus())) {
						TVlist.add(mDataMap);
					}
				}
				if (TVlist.size()>=15) {
					break;
				}
			}
		}
		
		
		if (TVlist != null && TVlist.size() > 0) {
			// 获取所有要查询商品信息的商品编号
			MDataMap productCodeMap = new MDataMap();
			for (MDataMap mDataMap : TVlist) {
				String productCode = mDataMap.get("good_id");
				if (StringUtils.isNotEmpty(productCode)) {
					productCodeMap.put(productCode, "");
				}
			}
			List<String> productCodeArr = new ArrayList<String>();
			for (String productCode : productCodeMap.keySet()) {
				productCodeArr.add(productCode);
			}
			Map<String, HomeColumnContentProductInfo> productInfoMap = this
					.getProductInfo(productCodeArr, userType,userCode,isPurchase,channelId);
			Map<String,String> picUrlMap = new HashMap<String, String>();
			List<String> picUrlArr = new ArrayList<String>();
			//获取商品信息中的商品图片，用来进行压缩
			for (String productCode : productInfoMap.keySet()) {
				picUrlArr.add(productInfoMap.get(productCode).getMainpicUrl());
				picUrlArr.add(productInfoMap.get(productCode).getAdPicUrl());
			}
			//压缩图片
			List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(
					Integer.parseInt(maxWidth), picUrlArr,picType);
			for (PicInfo picInfo : picInfoList) {
				picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			}
			for (String productCode : productInfoMap.keySet()) {
				String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
				String adpicUrl = productInfoMap.get(productCode).getAdPicUrl();
				if (StringUtils.isNotBlank(mainpicUrl)) {
					productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
				}	
				if (StringUtils.isNotBlank(adpicUrl)) {
					productInfoMap.get(productCode).setAdPicUrl(picUrlMap.get(adpicUrl));
				}
			
//				if (StringUtils.isNotBlank(adpicUrl)) {
//					if (appVersion.compareTo("3.9.6") >= 0) {
//						productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
//					}else{
//						productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(adpicUrl));
//					}
//					
//				}
			}
			for (MDataMap map : TVlist) {
				HomeColumnContent columnContent = new HomeColumnContent();
				columnContent.getProductInfo().setProductCode(map.get("good_id"));
				String startTime_ = map.get("form_fr_date");
				String endTime_ = map.get("form_end_date");
				columnContent.setStartTime(startTime_);// 播出时间
				columnContent.setEndTime(endTime_);// 结束时间
				columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
				columnContent.setShowmoreLinkvalue(map.get("good_id")); // 商品Code
				// 如果当前时间不在时间段内则不显示videoUrlTv - Yangcl
				if(this.compareDate(systemTime, startTime_) && this.compareDate(endTime_, systemTime)){
					columnContent.setVideoUrlTV(videoUrlTV);
				}else{
					columnContent.setVideoUrlTV("");  
				}

				// 根据商品编码查询sku信息
				// MDataMap productInfo =
				// DbUp.upTable("pc_productinfo").one("product_code",columnContent.getProductInfo().getProductCode());
				HomeColumnContentProductInfo productInfo = productInfoMap
						.get(columnContent.getProductInfo().getProductCode());
				if (null != productInfo) {
//					BigDecimal sellPrice = new BigDecimal( productInfo.getSellPrice()).setScale(0, BigDecimal.ROUND_DOWN);
					BigDecimal sellPrice = MoneyHelper.roundHalfUp(new BigDecimal( productInfo.getSellPrice()));  // 兼容小数 - Yangcl  
					columnContent.getProductInfo().setProductName(productInfo.getProductName());
					columnContent.getProductInfo().setSellPrice(sellPrice.toString());
					
//					columnContent.getProductInfo().setMarkPrice(new BigDecimal(productInfo.getMarkPrice()).setScale(0, BigDecimal.ROUND_DOWN).toString());
					columnContent.getProductInfo().setMarkPrice(MoneyHelper.format(new BigDecimal(productInfo.getMarkPrice())));  // 兼容小数 - Yangcl  
					
					
					columnContent.getProductInfo().setMainpicUrl(
							productInfo.getMainpicUrl());
					columnContent.getProductInfo().setFlagTheSea(
							productInfo.getFlagTheSea());
					columnContent.getProductInfo().setLabelsList(productInfo.getLabelsList());
					columnContent.getProductInfo().setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
					columnContent.setPicture(productInfo.getMainpicUrl());
					
					columnContent.getProductInfo().setAdPicUrl(
							productInfo.getAdPicUrl());
					//columnContent.setPicUrl(productInfo.getAdPicUrl());
					columnContent.setPicUrl(getPcProductAdpic(productInfo.getProductCode()));

//					BigDecimal marketPrice = new BigDecimal( Double.valueOf(columnContent.getProductInfo().getMarkPrice())).setScale(0, BigDecimal.ROUND_DOWN);
					BigDecimal marketPrice = MoneyHelper.roundHalfUp(new BigDecimal( Double.valueOf(columnContent.getProductInfo().getMarkPrice()))); // 兼容小数 - Yangcl 
					
					// BigDecimal sellPrice = new
					// BigDecimal(Double.valueOf(columnContent.getProductInfo().getSellPrice()));
					if (marketPrice.compareTo(BigDecimal.ZERO) > 0
							&& sellPrice.compareTo(BigDecimal.ZERO) > 0
							&& sellPrice.compareTo(marketPrice) < 0) {
						String discount = ""+ sellPrice.multiply(new BigDecimal(10)).divide(marketPrice,1,BigDecimal.ROUND_HALF_UP);
						if (discount.indexOf(".") > 0) {
							// 正则表达
							discount = discount.replaceAll("0+?$", "");// 去掉后面无用的零
							discount = discount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
						}
						if (StringUtils.isNotBlank(discount) && Double.parseDouble(discount) <= 0) {
							discount = "";
						}
						columnContent.getProductInfo().setDiscount(discount);
					} else {
						columnContent.getProductInfo().setDiscount("");
					}
					columnContentList.add(columnContent);
				}
			}
		}
		return columnContentList;
	}

	
	public List<HomeColumnContent> getTVDataListNew(String userType,String maxWidth,String picType,String userCode , String viewType,String appVersion,int futureProgram,String systemTime,String channelId) {
		return getTVDataListNew( userType, maxWidth, picType, userCode , viewType,appVersion,futureProgram,0,systemTime,channelId);
	}
	
	/**
	 * 取节目单列表，只返回：已结束、正在播放、未开始的三个节目数据<br>
	 * 如果当前没有直播的节目或者直播商品已下架则返回空列表
	 * @return
	 */
	public List<HomeColumnContent> getTVDataListNew(String userType,String maxWidth,String picType,String userCode , String viewType,String appVersion,int futureProgram,Integer isPurchase,String systemTime,String channelId) {
		systemTime = StringUtils.isBlank(systemTime)?DateUtil.getSysDateTimeString():systemTime;
		String videoUrlTV = bConfig("familyhas.video_url_TV");
		ProductService pService = new ProductService();
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		
		// 500版本不查询未开始的节目，固定返回一个默认数据
		if("5.0.0".equals(appVersion)){
			futureProgram = 0;
		}
		
		List<MDataMap> tvlist = new ArrayList<MDataMap>();
		List<MDataMap> nextList = new ArrayList<MDataMap>();
		// 已结束的节目
		tvlist.add(DbUp.upTable("pc_tv").oneWhere("", "form_end_date desc", "form_end_date <= :systemTime and so_id='1000001'", "systemTime",systemTime));
		// 正在开始的节目
		tvlist.addAll(DbUp.upTable("pc_tv").queryAll("", "form_fr_date asc", "form_fr_date <= :systemTime AND form_end_date > :systemTime and so_id='1000001'", new MDataMap("systemTime",systemTime)));
		if(futureProgram > 0){
			// 先查询出未开始的节目编号
			List<Map<String, Object>> formIdList = DbUp.upTable("pc_tv").dataSqlList("SELECT DISTINCT form_id FROM productcenter.pc_tv WHERE form_fr_date > :systemTime and so_id='1000001' ORDER BY form_fr_date limit "+futureProgram, new MDataMap("systemTime",systemTime));
			if(!formIdList.isEmpty()){
				List<String> formIds = new ArrayList<String>();
				for(Map<String, Object> map : formIdList){
					formIds.add("'"+map.get("form_id")+"'");
				}
				// 根据节目编号查询节目商品数据
				nextList = DbUp.upTable("pc_tv").queryAll("", "form_fr_date asc", "form_fr_date > :systemTime AND form_id IN("+StringUtils.join(formIds, ",")+") and so_id='1000001'", new MDataMap("systemTime",systemTime));
				tvlist.addAll(nextList);
			}
		}
		
		Set<String> productCodeArr = new HashSet<String>();
		
		// 排除掉下架商品
		PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
		Iterator<MDataMap> ita = tvlist.iterator();
		MDataMap mDataMap;
		LoadProductInfo load = new LoadProductInfo();
		while(ita.hasNext()){
			mDataMap = ita.next();
			// 排除查询的结果是null的
			if(mDataMap == null || StringUtils.isBlank(mDataMap.get("good_id"))){
				ita.remove();
				continue;
			}
			
			plusModelProductinfo = load.upInfoByCode(new PlusModelProductQuery(mDataMap.get("good_id")));
			if(plusModelProductinfo == null || !"4497153900060002".equals(plusModelProductinfo.getProductStatus())){
				ita.remove();
				continue;
			}
			
			productCodeArr.add(mDataMap.get("good_id"));
		}
		
		Map<String, HomeColumnContentProductInfo> productInfoMap = getProductInfo(new ArrayList<String>(productCodeArr), userType,userCode,isPurchase,channelId);
		Map<String,String> picUrlMap = new HashMap<String, String>();
		
		List<String> picUrlArr = new ArrayList<String>();
		//获取商品信息中的商品图片，用来进行压缩
		for (String productCode : productInfoMap.keySet()) {
			picUrlArr.add(productInfoMap.get(productCode).getMainpicUrl());
			picUrlArr.add(productInfoMap.get(productCode).getAdPicUrl());
		}
		//压缩图片
		List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), picUrlArr,picType);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
		}
		
		for (String productCode : productInfoMap.keySet()) {
			String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
			String adpicUrl = productInfoMap.get(productCode).getAdPicUrl();
			if (StringUtils.isNotBlank(mainpicUrl)) {
				productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
			}	
			if (StringUtils.isNotBlank(adpicUrl)) {
				productInfoMap.get(productCode).setAdPicUrl(picUrlMap.get(adpicUrl));
			}
		}
		
		boolean hasShowing = false; // 是否有当前正在直播的节目
		for (MDataMap map : tvlist) {
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.getProductInfo().setProductCode(map.get("good_id"));
			String startTime_ = map.get("form_fr_date");
			String endTime_ = map.get("form_end_date");
			columnContent.setStartTime(startTime_);// 播出时间
			columnContent.setEndTime(endTime_);// 结束时间
			columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
			columnContent.setShowmoreLinkvalue(map.get("good_id")); // 商品Code
			// 如果当前时间不在时间段内则不显示videoUrlTv - Yangcl
			if(this.compareDate(systemTime, startTime_) && this.compareDate(endTime_, systemTime)){
				columnContent.setVideoUrlTV(videoUrlTV);
				hasShowing = true;
			}else{
				columnContent.setVideoUrlTV("");  
			}
			
			// 根据商品编码查询sku信息
			// MDataMap productInfo =
			// DbUp.upTable("pc_productinfo").one("product_code",columnContent.getProductInfo().getProductCode());
			HomeColumnContentProductInfo productInfo = productInfoMap.get(columnContent.getProductInfo().getProductCode());
			if (null != productInfo) {
				BigDecimal sellPrice = MoneyHelper.roundHalfUp(new BigDecimal( productInfo.getSellPrice()));  // 兼容小数 - Yangcl  
				columnContent.getProductInfo().setProductName(productInfo.getProductName());
				columnContent.getProductInfo().setSellPrice(sellPrice.toString());
				columnContent.getProductInfo().setMarkPrice(MoneyHelper.format(new BigDecimal(productInfo.getMarkPrice())));  // 兼容小数 - Yangcl  
				columnContent.getProductInfo().setMainpicUrl(productInfo.getMainpicUrl());
				columnContent.getProductInfo().setFlagTheSea(productInfo.getFlagTheSea());
				columnContent.getProductInfo().setLabelsList(productInfo.getLabelsList());
				columnContent.getProductInfo().setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
				columnContent.getProductInfo().setTvTips(productInfo.getTvTips());
				columnContent.setPicture(productInfo.getMainpicUrl());
				//columnContent.getProductInfo().setAdPicUrl(productInfo.getAdPicUrl());
				columnContent.getProductInfo().setAdPicUrl(getPcProductAdpic(productInfo.getProductCode()));
				columnContent.setPicUrl(productInfo.getAdPicUrl());
				BigDecimal marketPrice = MoneyHelper.roundHalfUp(new BigDecimal( Double.valueOf(columnContent.getProductInfo().getMarkPrice()))); // 兼容小数 - Yangcl 
				
				// BigDecimal sellPrice = new
				// BigDecimal(Double.valueOf(columnContent.getProductInfo().getSellPrice()));
				if (marketPrice.compareTo(BigDecimal.ZERO) > 0
						&& sellPrice.compareTo(BigDecimal.ZERO) > 0
						&& sellPrice.compareTo(marketPrice) < 0) {
					String discount = ""+ sellPrice.multiply(new BigDecimal(10)).divide(marketPrice,1,BigDecimal.ROUND_HALF_UP);
					BigDecimal bd = new BigDecimal(discount);
					if(bd.compareTo(BigDecimal.ZERO) > 0){
						discount = MoneyHelper.roundHalfUp(bd).toString();
					}else{
						discount = "";
					}
					columnContent.getProductInfo().setDiscount(discount);
				} else {
					columnContent.getProductInfo().setDiscount("");
				}
				columnContentList.add(columnContent);
			}
		}
		
		// 如果没有未开始的节目则默认一个
		if(nextList.isEmpty()){
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.setStartTime("2099-01-01 00:00:00");// 播出时间
			columnContent.setEndTime("2099-12-01 00:00:00");// 结束时间
			columnContent.setShowmoreLinktype(""); // 商品详情类型
			columnContent.setShowmoreLinkvalue(""); // 商品Cod
			columnContent.setPicUrl(bConfig("familyhas.tv_to_begin_img"));
			columnContentList.add(columnContent);
		}
		
		// 如果当前没有直播的节目，则不返回节目单列表
		return hasShowing ? columnContentList : new ArrayList<HomeColumnContent>();
	}

	/**
	 * 获取分类名称
	 * 
	 * @param categoryCodeArr
	 * @return
	 */
	public MDataMap getCategoryName(List<String> categoryCodeArr,String sellerCode) {
		MDataMap resultMap = new MDataMap();
		if (null == categoryCodeArr || categoryCodeArr.size() < 1) {
			return resultMap;
		}
		String sFields = "category_code,category_name";
		String sWhere = " seller_code='" + sellerCode + "' and category_code in ('"	+ StringUtils.join(categoryCodeArr, "','") + "')";
		List<MDataMap> categoryNameMapList = DbUp.upTable("uc_sellercategory").queryAll(sFields, "", sWhere, null);
		for (MDataMap categoryNameMap : categoryNameMapList) {
			String categoryCode = categoryNameMap.get("category_code");
			String categoryName = categoryNameMap.get("category_name");
			resultMap.put(categoryCode, categoryName);
		}
		return resultMap;
	}

	// 闪购排序用
	public void sortSkuList(List<FlashsalesSkuInfo> list) {

		Collections.sort(list, new Comparator<FlashsalesSkuInfo>() {
			public int compare(FlashsalesSkuInfo o1, FlashsalesSkuInfo o2) {
				String hits0 = lpad(4, o1.getLocation()) + o1.getSku_code();
				String hits1 = lpad(4, o2.getLocation()) + o2.getSku_code();
				if (hits1.compareTo(hits0) < 0) {
					return 1;
				} else if (hits1.compareTo(hits0) == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

	}

	public String lpad(int length, int number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}
	
	/**
	 * 首页板式栏目维护内容批量删除功能
	 * @param uid
	 * @return
	 */
	public int batchDelColumnContent(List<String> uids) {
		int delNum = 0;
		if (null != uids) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("is_delete", "449746250001");
			delNum = DbUp.upTable("fh_apphome_column_content").dataExec("update fh_apphome_column_content set is_delete=:is_delete where uid in ('"+StringUtils.join(uids,"','")+"')", mDataMap);
		}
		return delNum;
	}
	
	/**
	 * @descriptions 比较两个时间的大小
	 *  	如果两个时间相等则返回0
	 * @param a not null
	 * @param b not null 
	 * @return boolean 
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-5-下午2:52:13
	 * @version 1.0.0.1
	 */
	private boolean compareDate(String a, String b) {
	    return a.compareTo(b) > 0;
	}
	/**
	 * 根据首页推荐分类UID获取分类编号
	 * @param uid
	 * @return
	 */
	public String getHomeRecoomendCategoryByUid(String uid){
		if (StringUtils.isNotBlank(uid)) {
			String sWhere ="uid=:uid";
			MDataMap map = DbUp.upTable("fh_home_recommend").oneWhere("category_code", "", sWhere,"uid",uid);
			if (null != map && !map.isEmpty()) {
				return map.get("category_code");
			}
		}
		return "";
	}
	
	/**
	 * 将惠家有的栏目复制一份到微信商城栏目
	 * @param uid  惠家有app首页栏目Uid
	 * 
	 * @author fq
	 */
	public Map<String , String> copyColumnToWx (String uid) {
		String desc = "【复制】";
		Map<String , String> result = new HashMap<String ,String>();
		if(StringUtils.isNotBlank(uid)) {
			
			MDataMap one = DbUp.upTable("fh_apphome_column").one("uid",uid);
			
			if(null != one) {
				
				String nowTime = DateUtil.getNowTime();
				/* 获取当前登录人 */
				String create_user = UserFactory.INSTANCE.create().getLoginName();

				String old_column_code = one.get("column_code");
				String new_column_code = WebHelper.upCode("COL");
				
				
				//修改 字段值  
				one.put("release_flag", "449746250002");//将发布状态致为未发布
				one.put("view_type", "4497471600100002");
				one.put("create_time", nowTime);
				one.put("create_user", create_user+desc);
				one.put("update_time", nowTime);
				one.put("update_user", create_user);
				one.put("column_code", new_column_code);
				
				one.remove("zid");
				one.remove("uid");
				DbUp.upTable("fh_apphome_column").dataInsert(one);
				List<Map<String, Object>> columnContent = DbUp.upTable("fh_apphome_column_content").listByWhere("column_code",old_column_code);
				StringBuffer apphomeColumnContent_inserSql = new StringBuffer();
				String contentExecSql = "  INSERT INTO familyhas.fh_apphome_column_content(uid,column_code,picture,start_time,end_time,position,title,title_color,description,description_color,showmore_linktype,showmore_linkvalue,is_share,is_delete,create_time,create_user,update_time,update_user,delete_time,delete_user,share_pic,share_title,share_content,share_link,skip_place,place_time,floor_model)  VALUES ";
				for (Map<String, Object> map : columnContent) {
					map.remove("zid");
					map.put("uid", WebHelper.upUuid());
					map.put("column_code", new_column_code);
					map.put("create_user", create_user+desc);
					map.put("update_user", create_user);
					map.put("create_time", nowTime);
					map.put("update_time", nowTime);
					
					apphomeColumnContent_inserSql.append("(");
						apphomeColumnContent_inserSql.append("'" + WebHelper.upUuid() + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("column_code") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("picture")     + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("start_time")  + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("end_time")    + "',");
						apphomeColumnContent_inserSql.append(      map.get("position")    +","     );
						apphomeColumnContent_inserSql.append("'" + map.get("title") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("title_color") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("description") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("description_color") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("showmore_linktype") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("showmore_linkvalue") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("is_share") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("is_delete") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("create_time") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("create_user") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("update_time") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("update_user") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("delete_time") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("delete_user") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("share_pic") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("share_title") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("share_content") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("share_link") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("skip_place") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("place_time") + "',");
						apphomeColumnContent_inserSql.append("'" + map.get("floor_model") + "'");
					apphomeColumnContent_inserSql.append("),");
					
				}
				
				if(apphomeColumnContent_inserSql.length() > 0) {
					String data = apphomeColumnContent_inserSql.substring(0, apphomeColumnContent_inserSql.length()-1);
					DbUp.upTable("fh_apphome_column_content").dataExec( contentExecSql + data , new MDataMap());
				}
				
				result.put("sql", JSON.toJSONString(apphomeColumnContent_inserSql.toString()));
				result.put("columnInfo", JSON.toJSONString(one));
				result.put("columnContent", JSON.toJSONString(columnContent));
				result.put("resultCode", "1");
				
				
			}
			
		}
		return result;
		
		
		
	}
	
	/**
	 * 根据规则取商品的广告图信息
	 * @param productCode
	 * @return
	 */
	private static String getPcProductAdpic(String productCode) {
		String ret = "";
		//取得商品广告图信息
		MDataMap pcAdpicListMapParam = new MDataMap();
		pcAdpicListMapParam.put("product_code", productCode);
		pcAdpicListMapParam.put("now", DateUtil.getSysDateTimeString());
		List<MDataMap> pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "start_date desc",
				"product_code=:product_code  and (sku_code='' or sku_code is null) and start_date <=:now and end_date >=:now", pcAdpicListMapParam, -1, -1);
		if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
			ret = pcAdpicListMap.get(0).get("pic_url");
		} else {
			pcAdpicListMapParam = new MDataMap();
			pcAdpicListMapParam.put("product_code", productCode);
			pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "",
					"product_code=:product_code  and (sku_code='' or sku_code is null) and (start_date='' or start_date is null) and (end_date='' or end_date is null)", pcAdpicListMapParam, -1, -1);
			if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
				ret = pcAdpicListMap.get(0).get("pic_url");
			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		String now = DateUtil.getSysDateTimeString();
		System.out.println(now);//2018-10-29 16:43:49
	}
	
	/**
	 * 获取define 定义参数
	 * @param parentDefineCode
	 * @return
	 */
	public Map<String, String> getColumnTypeCodeRELName(String parentDefineCode) {
		
		Map<String, String> defineInfo = new HashMap<String, String>();
		if(StringUtils.isNotBlank(parentDefineCode)) {
			defineInfo = new DefineService().getDefineCodeRELName(parentDefineCode);
		}
		
		return defineInfo;
		
	}
	
	public String getVideoLinkUrlFromApi(String ccvid) {
		String apiKey = "2PTwX2QNHSghSm8UoagS32s5bQG7I1hR";
        String userId = "7408202D39A123FF";
        String videoid = ccvid;
        Date date = new Date();
        String time = date.getTime()+"";
        String params2 = "userid="+userId;
        String params3 = "videoid="+videoid;
        String totalStr = params2+"&"+params3;
        String params1 = "time="+time;
        String md5Parmas = MD5Util.MD5(totalStr+"&"+params1+"&salt="+apiKey);
        String videoHost = "http://union.bokecc.com/api/mobile";
        String allurl = videoHost.concat("?").concat(totalStr).concat("&").concat(params1).concat("&").concat("hash=").concat(md5Parmas);
        String result = HttpUtil.post(allurl, "{}", "UTF-8");
		String videoLink = this.xmlElements(result);
		return videoLink;
	}
	
	private String xmlElements(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String linkvalue = "";
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
//            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;            
            for(int i=0;i<jiedian.size();i++){
                et = (Element) jiedian.get(i);//循环依次得到子元素
                if(linkvalue.equals("")){
                	linkvalue = et.getText();
                }
                if(et.getAttribute("quality").getValue().equals("20")){
                	linkvalue = et.getText();
                }
            }          
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return linkvalue;
    }
	public List<HomeColumnContent> getSeckillDataList(String userType, String maxWidth, String picType, String userCode,
			String viewType, String appVersion, Integer isPurchase,String systemTime,String channelId) {
		
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		
		 systemTime = (StringUtils.isBlank(systemTime)?DateUtil.getSysDateTimeString():systemTime);
		ProductService pService = new ProductService();
		
		String swhere = "event_type_code='4497472600010001' and " + "begin_time<='" + systemTime + "' and end_time >= '" + systemTime + "' and event_status='4497472700020002'";

		List<MDataMap> seckillEvent = DbUp.upTable("sc_event_info").query("", "begin_time desc", swhere, new MDataMap(),-1,-1);
		
		if(null == seckillEvent || seckillEvent.isEmpty()) {
			//当前档没有 则判断是否有下一档
			String endTime = systemTime.substring(0,11) + "23:59:59";
			swhere = "event_type_code='4497472600010001' and " + "begin_time>='" + systemTime + "' and begin_time <= '" + endTime + "' and event_status='4497472700020002'";

			seckillEvent = DbUp.upTable("sc_event_info").query("", "begin_time", swhere, new MDataMap(),-1,-1);
			if(null == seckillEvent || seckillEvent.isEmpty()) {
				return columnContentList;
			}
		}
		
		String event_code = seckillEvent.get(0).get("event_code")+"";
		
		String sSql = "select * from sc_event_item_product where event_code=:event_code and flag_enable=1 group by product_code";
		List<Map<String, Object>> seckillEventItem = DbUp.upTable("sc_event_item_product").dataSqlList(sSql, new MDataMap("event_code",event_code));
		
		if(null == seckillEventItem || seckillEventItem.isEmpty()) {
			return columnContentList;
		}
		
		// 获取商品信息
		List<Map<String, Object>> seckillGoodList = new ArrayList<Map<String, Object>>();
		PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
		for (Map<String, Object> map : seckillEventItem) {
			String productCode = map.get("product_code")+"";
			if (StringUtils.isNotEmpty(productCode)) {
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
				if ("4497153900060002".equals(plusModelProductinfo.getProductStatus())) {
					seckillGoodList.add(map);
				}
			}
			if (seckillGoodList.size()>=6) {
				break;
			}
		}
		
		// 获取所有要查询商品信息的商品编号
		Map<String, Map<String, Object>> productCodeMap = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : seckillGoodList) {
			String productCode = map.get("product_code")+"";
			if (StringUtils.isNotEmpty(productCode)) {
				productCodeMap.put(productCode, map);
			}
		}
		List<String> productCodeArr = new ArrayList<String>();
		for (String productCode : productCodeMap.keySet()) {
			productCodeArr.add(productCode);
		}
		Map<String, HomeColumnContentProductInfo> productInfoMap = this
				.getProductInfo(productCodeArr, userType,userCode,isPurchase,channelId);
		Map<String,String> picUrlMap = new HashMap<String, String>();
		List<String> picUrlArr = new ArrayList<String>();
		//获取商品信息中的商品图片，用来进行压缩
		for (String productCode : productInfoMap.keySet()) {
			picUrlArr.add(productInfoMap.get(productCode).getMainpicUrl());
			picUrlArr.add(productInfoMap.get(productCode).getAdPicUrl());
		}
		//压缩图片
		List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(
				Integer.parseInt(maxWidth), picUrlArr,picType);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
		}
		for (String productCode : productInfoMap.keySet()) {
			String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
			String adpicUrl = productInfoMap.get(productCode).getAdPicUrl();
			if (StringUtils.isNotBlank(mainpicUrl)) {
				productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
			}	
			if (StringUtils.isNotBlank(adpicUrl)) {
				productInfoMap.get(productCode).setAdPicUrl(picUrlMap.get(adpicUrl));
			}
		}
		
		
		for (Map<String, Object> map : seckillEventItem) {
			HomeColumnContent columnContent = new HomeColumnContent();
			columnContent.getProductInfo().setProductCode(map.get("product_code")+"");
			columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
			columnContent.setShowmoreLinkvalue(map.get("product_code")+""); // 商品Code
			columnContent.setStartTime(seckillEvent.get(0).get("begin_time"));
			columnContent.setEndTime(seckillEvent.get(0).get("end_time"));

			HomeColumnContentProductInfo productInfo = productInfoMap
					.get(columnContent.getProductInfo().getProductCode());
			if (null != productInfo) {
				BigDecimal sellPrice = MoneyHelper.roundHalfUp(new BigDecimal( productCodeMap.get(productInfo.getProductCode()).get("favorable_price")+""));  // 兼容小数 - Yangcl  
				columnContent.getProductInfo().setProductName(productInfo.getProductName());
				columnContent.getProductInfo().setSellPrice(sellPrice.toString());
				
				columnContent.getProductInfo().setMarkPrice(MoneyHelper.format(new BigDecimal(productCodeMap.get(productInfo.getProductCode()).get("selling_price")+"")));  // 兼容小数 - Yangcl  
				
				
				columnContent.getProductInfo().setMainpicUrl(
						productInfo.getMainpicUrl());
				columnContent.getProductInfo().setFlagTheSea(
						productInfo.getFlagTheSea());
				columnContent.getProductInfo().setLabelsList(productInfo.getLabelsList());
				columnContent.getProductInfo().setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
				columnContent.setPicture(productInfo.getMainpicUrl());
				
				columnContent.getProductInfo().setAdPicUrl(
						productInfo.getAdPicUrl());
				columnContent.setPicUrl(getPcProductAdpic(productInfo.getProductCode()));

				BigDecimal marketPrice = MoneyHelper.roundHalfUp(new BigDecimal( productCodeMap.get(productInfo.getProductCode()).get("selling_price")+"")); // 兼容小数 - Yangcl 
				
				if (marketPrice.compareTo(BigDecimal.ZERO) > 0
						&& sellPrice.compareTo(BigDecimal.ZERO) > 0
						&& sellPrice.compareTo(marketPrice) < 0) {
					String discount = ""+ sellPrice.multiply(new BigDecimal(10)).divide(marketPrice,1,BigDecimal.ROUND_HALF_UP);
					if (discount.indexOf(".") > 0) {
						// 正则表达
						discount = discount.replaceAll("0+?$", "");// 去掉后面无用的零
						discount = discount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
					}
					if (StringUtils.isNotBlank(discount) && Double.parseDouble(discount) <= 0) {
						discount = "";
					}
					columnContent.getProductInfo().setDiscount(discount);
				} else {
					columnContent.getProductInfo().setDiscount("");
				}
				columnContentList.add(columnContent);
			}
		}
		return columnContentList;
	}
	
	public int getCollageDataPageCount(String sellerCode, String eventCode, String showNum,String systemTime) {
		systemTime = StringUtils.isBlank(systemTime)?DateUtil.getSysDateTimeString():systemTime;
		String sql = "select count(1) from (select p.product_code "
				+ "from sc_event_item_product p, sc_event_info i, productcenter.pc_productinfo t where p.event_code = i.event_code and (i.event_status='4497472700020002' or i.event_status='4497472700020004') "
				+ "and i.event_type_code = '4497472600010024' and t.seller_code = :sellerCode and i.begin_time <= '"+systemTime+"' and i.end_time >= '"+systemTime+"'  and p.event_code = :event_code and p.flag_enable = 1 "
				+ "and t.product_code = p.product_code and t.product_status = '4497153900060002' group by p.product_code) t";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sellerCode", sellerCode);
		params.put("event_code", eventCode);
		int count = DbUp.upTable("sc_event_info").upTemplate().queryForInt(sql, params);
		int pageSize = Integer.parseInt(showNum);
		int pageCount = (count - 1 / pageSize) + 1;
		return pageCount;
	}
	
	
	public List<HomeColumnContent> getCollageDataList(String sellerCode, String eventCode, int startPage, String showNum, int pageCount, String maxWidth,String systemTime) {
		systemTime = StringUtils.isBlank(systemTime)?DateUtil.getSysDateTimeString():systemTime;
		ProductService productService = new ProductService();
		LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
		LoadEventItemProduct itemProduct = new LoadEventItemProduct();
		PlusModelEventItemQuery itemQuery = new PlusModelEventItemQuery();
		PlusSupportProduct plusProduct = new PlusSupportProduct();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		PlusSupportEvent plusEvent = new PlusSupportEvent();
		MDataMap stockParams = new MDataMap();
		
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		if(eventCode != null && !"".equals(eventCode) && showNum != null && !"".equals(showNum)) {
			String sql = "select p.sku_code, p.product_code, p.seat, p.favorable_price, p.item_code, i.begin_time, i.end_time, p.sales_advertisement, t.product_name, t.min_sell_price, t.market_price, t.mainpic_url, "
					+ "p.cover_img, t.small_seller_code, i.event_code, i.collage_person_count "
					+ "from sc_event_item_product p, sc_event_info i, productcenter.pc_productinfo t where p.event_code = i.event_code and (i.event_status='4497472700020002' or i.event_status='4497472700020004') "
					+ "and i.event_type_code = '4497472600010024' and t.seller_code = :sellerCode and i.begin_time <= '"+systemTime+"' and i.end_time >= '"+systemTime+"' and p.event_code = :event_code and p.flag_enable = 1 "
					+ "and t.product_code = p.product_code and t.product_status = '4497153900060002' group by p.product_code order by p.seat asc, p.zid desc "
					+ "limit " + startPage + ", " + Integer.parseInt(showNum) + "";
			List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode", sellerCode, "event_code", eventCode));
			for(Map<String, Object> map : list) {
				boolean showFlag = false;
				stockParams.put("event_code", MapUtils.getString(map, "event_code", ""));
				stockParams.put("product_code", MapUtils.getString(map, "product_code", ""));
				List<Map<String, Object>> itemList = DbUp.upTable("sc_event_item_product").dataSqlList("select p.item_code from sc_event_item_product p where p.event_code = :event_code and p.flag_enable = 1 and "
						+ "p.product_code = :product_code", stockParams);
				for(Map<String, Object> item : itemList) {
					long limitStock = plusEvent.upEventItemSkuStock(MapUtils.getString(item, "item_code", ""));
					if(limitStock > 0) {
						showFlag = true;
					}
				}
				if(!showFlag) {//库存小于0不让前端显示
					continue;
				}
				
				HomeColumnContent columnContent = new HomeColumnContent();
				columnContent.setStartTime(MapUtils.getString(map, "begin_time", ""));//开始时间
				columnContent.setEndTime(MapUtils.getString(map, "end_time", ""));//结束时间
				columnContent.setShowmoreLinktype("4497471600020004");//商品详情类型
				columnContent.setShowmoreLinkvalue(MapUtils.getString(map, "product_code", ""));//商品编码
				columnContent.setDescription(MapUtils.getString(map, "sales_advertisement", ""));//促销活动广告语
				
				HomeColumnContentProductInfo productInfo = new HomeColumnContentProductInfo();
				productInfo.setProductCode(MapUtils.getString(map, "product_code", ""));//商品编码
				productInfo.setProductName(MapUtils.getString(map, "product_name", ""));//商品名称
				
				skuQuery.setCode(MapUtils.getString(map, "product_code", ""));
				Map<String, PlusModelSkuInfo> priceMap = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
				PlusModelSkuInfo skuInfo = priceMap.get(MapUtils.getString(map, "product_code", ""));
				productInfo.setSellPrice(skuInfo.getGroupBuyingPrice().toString());//销售价
				productInfo.setMarkPrice(skuInfo.getSkuPrice().toString());//市场价
				
				//优先走精修图片，如果没有精修图片，则返回商品图
				String cover_img = MapUtils.getString(map, "cover_img", "");
				if(!"".equals(cover_img)) {
					productInfo.setMainpicUrl(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), cover_img).getPicNewUrl());//商品主图
				}else {
					productInfo.setMainpicUrl(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), MapUtils.getString(map, "mainpic_url", "")).getPicNewUrl());//商品主图
				}
				
				//获取自营标签
				String stChar = "";
				if("SI2003".equals(MapUtils.getString(map, "small_seller_code", ""))) {
					stChar = "4497478100050000";
				}else {
					PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(MapUtils.getString(map, "small_seller_code", "")));
					stChar = sellerInfo.getUc_seller_type();
				}
				Map<String, String> productTypeMap = WebHelper.getAttributeProductType(stChar);
				productInfo.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
				
				//获取是否抢光
				itemQuery.setCode(MapUtils.getString(map, "item_code", ""));
				PlusModelEventItemProduct eventItemProduct = itemProduct.upInfoByCode(itemQuery);
				long saleStock = eventItemProduct.getSalesStock();
				if(saleStock > 0) {
					columnContent.setIsLoot("449746250002");
				}else {
					columnContent.setIsLoot("449746250001");
				}
				
				//获取几人团
				String collagePersonCount = MapUtils.getString(map, "collage_person_count", "0");
				columnContent.setManyCollage(collagePersonCount);
				
				columnContent.setProductInfo(productInfo);
				
				columnContentList.add(columnContent);
			}
		}
		if(columnContentList.size() <= 0 && startPage < (pageCount - 1)) {
			startPage += 1;
			columnContentList = getCollageDataList(sellerCode, eventCode, startPage, showNum, pageCount, maxWidth,systemTime);
		}
		return columnContentList;
	}
	
}
