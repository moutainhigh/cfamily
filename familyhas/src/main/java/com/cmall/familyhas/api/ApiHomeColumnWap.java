package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiHomeColumnWapInput;
import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiHomeColumnWapResult;
import com.cmall.familyhas.model.TopThreeColumn;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.FlashsalesSkuInfo;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusActiveProduct;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有首页版式栏目(WAP页调用)(2015-10-29准备作废，以后请使用ApiHomeColumn)
 * 
 * @author ligj
 * 
 */
public class ApiHomeColumnWap extends
		RootApiForVersion<ApiHomeColumnWapResult, ApiHomeColumnWapInput> {
	private final String PRODUCT_VIEW = "4497471600020004"; // 链接类型为商品详情
	private final String CATEGORY = "4497471600020003"; // 链接类型为分类
	private final String URL = "4497471600020001"; // 链接类型为URL
	private String systemTime = DateUtil.getSysDateTimeString();
	private ProductService pService = BeansHelper
			.upBean("bean_com_cmall_productcenter_service_ProductService");
	private String userType = "";
	private String maxWidth = "0";
	public ApiHomeColumnWapResult Process(ApiHomeColumnWapInput inputParam,
			MDataMap mRequestMap) {
		if (StringUtils.isBlank(inputParam.getBuyerType())) {
			inputParam.setBuyerType("4497469400050002");
		}
		ApiHomeColumnWapResult result = new ApiHomeColumnWapResult();
		List<HomeColumn> columnList = new ArrayList<HomeColumn>(); // 其他栏目List
		List<HomeColumn> topThreeColumnList = new ArrayList<HomeColumn>(); // 前三个栏目List
		TopThreeColumn topThreeColumn = new TopThreeColumn(); // 前三个栏目

		maxWidth = StringUtils.isEmpty(inputParam.getMaxWidth()) ? "0"
				: inputParam.getMaxWidth(); // 最大宽度
		userType = inputParam.getBuyerType();
		String sFields = "column_code,column_name,column_type,start_time,end_time,is_showmore,showmore_title,showmore_linktype,showmore_linkvalue,show_name,pic_url";
		String sOrders = "position asc , create_time desc";
		String sWhere = " start_time <= '"
				+ systemTime
				+ "' and end_time > '"
				+ systemTime
				+ "' and is_delete='449746250002' and release_flag='449746250001' and seller_code='SI2003' and view_type='4497471600100002'";
		List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column")
				.queryAll(sFields, sOrders, sWhere, null);
		if (null != columnMapList) {
			String sFieldsContent = "";
			String sWhereContent = " start_time <='" + systemTime
					+ "' and end_time > '" + systemTime
					+ "' and is_delete='449746250002'";
			List<MDataMap> columnContentMapList = DbUp.upTable(
					"fh_apphome_column_content").queryAll(sFieldsContent,
					"position asc,start_time desc", sWhereContent, null);
			// 获取所有要查询商品信息的商品编号
			MDataMap productCodeMap = new MDataMap();
			// 获取所有要查询的分类编号，key:categoryCode,value:0
			MDataMap categoryCodeMap = new MDataMap();
			// 图片Map，key:picOldUrl，value:picNewUrl
			MDataMap picUrlMap = new MDataMap();

			// 图片Map，key:picOldUrl，value:picNewUrl
			Map<String,PicInfo> picUrlObjMap = new HashMap<String,PicInfo>();
			for (int i = 0; i < columnContentMapList.size(); i++) {
				MDataMap mDataMap = columnContentMapList.get(i);
				if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
					String productCode = mDataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(productCode)) {
						productCodeMap.put(productCode, "");
					}
				} else if (CATEGORY.equals(mDataMap.get("showmore_linktype"))) {
					String categoryCode = mDataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(categoryCode)) {
						categoryCodeMap.put(categoryCode, "");
					}
				}
				if (StringUtils.isNotEmpty(mDataMap.get("picture"))) {
					picUrlMap.put(mDataMap.get("picture"), "");
				}

			}
			for (int i = 0; i < columnMapList.size(); i++) {
				MDataMap mDataMap = columnMapList.get(i);
				if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
					String productCode = mDataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(productCode)) {
						productCodeMap.put(productCode, "");
					}
				} else if (CATEGORY.equals(mDataMap.get("showmore_linktype"))) {
					String categoryCode = mDataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(categoryCode)) {
						categoryCodeMap.put(categoryCode, "");
					}
				}
			}

			List<String> productCodeArr = new ArrayList<String>();
			for (String productCode : productCodeMap.keySet()) {
				productCodeArr.add(productCode);
			}
			List<String> categoryCodeArr = new ArrayList<String>();
			for (String categoryCode : categoryCodeMap.keySet()) {
				categoryCodeArr.add(categoryCode);
			}
			
			Map<String, HomeColumnContentProductInfo> productInfoMap = this.getProductInfo(productCodeArr, userType);
			MDataMap categoryNameMap = this.getCategoryName(categoryCodeArr);
			
			
			//获取商品信息中的商品图片，用来进行压缩
			for (String productCode : productInfoMap.keySet()) {
				picUrlMap.put(productInfoMap.get(productCode).getMainpicUrl(),"");
			}
			
			List<String> picUrlArr = new ArrayList<String>();
			for (String picUrl : picUrlMap.keySet()) {
				picUrlArr.add(picUrl);
			}
			//压缩图片
			List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(
					Integer.parseInt(maxWidth), picUrlArr);
			for (PicInfo picInfo : picInfoList) {
				picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
				picUrlObjMap.put(picInfo.getPicNewUrl(), picInfo);
			}
			
			for (String productCode : productInfoMap.keySet()) {
				String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
				if (StringUtils.isNotBlank(mainpicUrl)) {
					productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
				}
			}
			
			for (int i = 0; i < columnMapList.size(); i++) {
				MDataMap columnMap = columnMapList.get(i);

				HomeColumn homeColumn = new HomeColumn();
				List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();

				homeColumn.setShowName(columnMap.get("show_name")); // 是否显示栏目名称
				if (!"449746250002".equals(columnMap.get("show_name"))) {
					homeColumn.setColumnName(columnMap.get("column_name"));
				}
				homeColumn.setColumnType(columnMap.get("column_type"));
				homeColumn.setStartTime(columnMap.get("start_time"));
				homeColumn.setEndTime(columnMap.get("end_time"));
				homeColumn.setIsShowmore(columnMap.get("is_showmore")); // 是否显示更多
				homeColumn.setShowmoreLinktype(columnMap
						.get("showmore_linktype"));
				if (CATEGORY.equals(columnMap.get("showmore_linktype"))) {
					homeColumn.setShowmoreLinkvalue(categoryNameMap
							.get(columnMap.get("showmore_linkvalue")));
				} else {
					homeColumn.setShowmoreLinkvalue(columnMap
							.get("showmore_linkvalue"));
				}
				homeColumn.setShowmoreTitle(columnMap.get("showmore_title"));
				// 不是“闪购”与“今日直播”时
				if (null != columnContentMapList
						&& !"4497471600010011".equals(homeColumn
								.getColumnType())
						&& !"4497471600010010".equals(homeColumn
								.getColumnType())) {
					for (MDataMap columnContentMap : columnContentMapList) {
						if (columnMap.get("column_code").equals(
								columnContentMap.get("column_code"))) {
							HomeColumnContent columnContent = new HomeColumnContent();
							// 压缩图片
							String pic = picUrlMap.get(columnContentMap.get("picture"));
							columnContent.setPicture(StringUtils.isEmpty(pic) ? "" : pic);
							columnContent.setStartTime(columnContentMap.get("start_time"));
							columnContent.setEndTime(columnContentMap.get("end_time"));
							columnContent.setTitle(columnContentMap.get("title"));
							columnContent.setTitleColor(columnContentMap.get("title_color"));
							columnContent.setDescription(columnContentMap.get("description"));
							columnContent.setDescriptionColor(columnContentMap.get("description_color"));
							columnContent.setShowmoreLinktype(columnContentMap.get("showmore_linktype"));
							
							String showmore_linkvalue = columnContentMap.get("showmore_linkvalue");
							if (StringUtils.isNotBlank(showmore_linkvalue) && 
									"449746250001".equals(columnContentMap.get("skip_place")) && 
									showmore_linkvalue.endsWith("=")) {
								String place_time = columnContentMap.get("place_time");
								if (StringUtils.isNotBlank(place_time)) {
										try {
											Long timeLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(place_time).getTime();
											String timeParam = "";
											if (showmore_linkvalue.indexOf("?") > 0) {
												timeParam = "&time";
											}else{
												timeParam = "?time=";
											}
											showmore_linkvalue += (timeParam+timeLong.toString());
										} catch (ParseException e) {
											e.printStackTrace();
										}
								}
							}
							
							if (CATEGORY.equals(columnContentMap.get("showmore_linktype"))) {
								columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue));
							} else {
								columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(
										showmore_linkvalue) ? "" : showmore_linkvalue);
							}
							columnContent.setPosion(Integer.parseInt(StringUtils.isEmpty(
									columnContentMap.get("position")) ? "0" : columnContentMap.get("position")));
							columnContent.setIsShare(columnContentMap.get("is_share"));
							
							//栏目类型为一栏广告时，返回图片高度
							if ("4497471600010002".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())) {
									PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
									columnContent.setPicHeight(null == oneColumnPic ? 0 : oneColumnPic.getHeight());
							}
							// 链接类型为“商品详情”时查询商品信息
							if (PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))) {
								HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
								// 注释掉下面这个过滤，见bug#5756
								// if (null == productInfo ||
								// StringUtils.isEmpty(productInfo.getProductCode()))
								// {
								// //加这个if为了过滤掉已下架的商品
								// continue;
								// }
								columnContent.setProductInfo(productInfo);
							} else if (URL.equals(columnContentMap
									.get("showmore_linktype"))
									&& "449746250001".equals(columnContentMap
											.get("is_share"))) {
								if (VersionHelper
										.checkServerVersion("3.5.51.54")) {
									// 链接类型为"URL"时并且是否分享为"是"时,设置“分享图片”，“分享标题”，“分享内容”，“分享链接”
									columnContent.setShareTitle(columnContentMap.get("share_title"));
									columnContent.setShareContent(columnContentMap.get("share_content"));
									columnContent.setShareLink(columnContentMap.get("share_link"));
									columnContent.setSharePic(columnContentMap.get("share_pic"));

									// “分享链接需要进行拼接重写”
									// http://share-static.sycdn.ichsy.com/cfamily/web/cfamilypage/specialshare?link=http://share.ichsy.com/index/20150525mr.html&alt=%E6%97%85%E8%A1%8C%E5%A6%86%E5%A4%87
									String shareUrlHead = bConfig("familyhas.share_url_head");
									String alt = "&alt=";
									String shareTitle = columnContentMap.get("share_title");
									if (StringUtils.isEmpty(shareTitle)) {
										shareTitle = bConfig("familyhas.share_title");
									}
									String shareURL = columnContentMap.get("share_link");
									// 为了兼容历史数据分享链接为空的情况
									if (StringUtils.isEmpty(shareURL)) {
										shareURL = showmore_linkvalue;
									}
									String shareLink = shareUrlHead;
									try {
										shareLink += (URLEncoder.encode((shareURL.trim()),"utf8")+alt+URLEncoder.encode(shareTitle,"utf8"));
									} catch (UnsupportedEncodingException e) {
									}
									columnContent.setShareLink(shareLink);
								}
							}
							contentList.add(columnContent);
						}
					}
				} else {
					// 闪购
					if ("4497471600010011".equals(homeColumn.getColumnType())) {
						if (VersionHelper.checkServerVersion("3.5.92.55")) {
							contentList = this.getFlashActivityNew("SI2003",getChannelId());
						}else{
							contentList = this.getFlashActivity();
						}
						// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
						if (null == contentList
								|| contentList.size() < 1
								|| systemTime.compareTo(contentList.get(0)
										.getEndTime()) > 0) {
							continue;
						} else {
							// 闪购栏目的开始结束时间设置为闪购活动的开始结束时间
							homeColumn.setStartTime(contentList.get(0)
									.getStartTime());
							homeColumn.setEndTime(contentList.get(0)
									.getEndTime());
						}
					}
					// TV直播
					if ("4497471600010010".equals(homeColumn.getColumnType())) {
						contentList = this.getTVDataList();
					}

				}
				homeColumn.setContentList(contentList); // 内容List
				// 轮播图，二栏广告，导航栏时插入到前三个
				if ("4497471600010001".equals(homeColumn.getColumnType())
						|| "4497471600010003"
								.equals(homeColumn.getColumnType())
						|| "4497471600010004"
								.equals(homeColumn.getColumnType())) {
					if (null != homeColumn.getContentList()
							&& homeColumn.getContentList().size() > 0) {
						topThreeColumnList.add(homeColumn);
					}
				} else {
					if (null != homeColumn.getContentList()
							&& homeColumn.getContentList().size() > 0) {
						columnList.add(homeColumn);
					}
				}
			}
		}
		// topThreeColumnList数组里第一个元素一定是轮播图，第二个元素一定是二栏广告，第三个元素一定是导航栏
		for (int i = 0; i < 3; i++) {
			boolean flagExist = false; // 标志是否存在特定位置特定类型的栏目，如果不存在，则放入new
										// HomeColumn();
			for (int k = 0; k < topThreeColumnList.size(); k++) {
				if (i == 0
						&& "4497471600010001".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 轮播广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
				if (i == 1
						&& "4497471600010003".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 二栏广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
				if (i == 2
						&& "4497471600010004".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 二栏广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
			}
			if (!flagExist) {
				topThreeColumnList.add(i, new HomeColumn());
			}
		}
		topThreeColumn.setTopThreeColumnList(topThreeColumnList);
		result.setSysTime(systemTime);
		result.setColumnList(columnList);
		result.setTopThreeColumn(topThreeColumn);
		return result;
	}

	private Map<String, HomeColumnContentProductInfo> getProductInfo(
			List<String> produtCodeArr, String userType) {
		Map<String, HomeColumnContentProductInfo> resultMap = new HashMap<String, HomeColumnContentProductInfo>();
		if (null == produtCodeArr || produtCodeArr.size() < 1) {
			return resultMap;
		}
		// 获取商品价格
		Map<String, BigDecimal> minSellPriceProduct = new HashMap<String, BigDecimal>();
		if (VersionHelper.checkServerVersion("3.5.62.55")) {
//			minSellPriceProduct = pService.getMinProductActivityNew(
//					produtCodeArr, userType);
			if (VersionHelper.checkServerVersion("3.5.95.55")) {
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(StringUtils.join(produtCodeArr,","));
				skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
				minSellPriceProduct = new ProductPriceService().
						getProductMinPrice(skuQuery);// 获取商品最低销售价格
			}else{
				minSellPriceProduct = new ProductService().
						getMinProductActivityNew(produtCodeArr, userType);// 获取商品最低销售价格
			}
		} else {
			minSellPriceProduct = pService.getMinProductActivity(produtCodeArr);
		}
		String sFields = "product_code,product_name,min_sell_price,market_price,mainpic_url,small_seller_code";
		String sWhere = " product_code in ('"
				+ StringUtils.join(produtCodeArr, "','") + "')";
		List<MDataMap> productInfoMapList = DbUp.upTable("pc_productinfo")
				.queryAll(sFields, "", sWhere, null);
		for (MDataMap productInfoMap : productInfoMapList) {
			HomeColumnContentProductInfo productInfo = new HomeColumnContentProductInfo();
			String min_sell_price = productInfoMap.get("min_sell_price");
			String market_price = productInfoMap.get("market_price");

			String discount = "";
			BigDecimal sellPrice = minSellPriceProduct.get(productInfoMap.get("product_code"));
			BigDecimal marketPrice = BigDecimal.ZERO;
			if (null!=sellPrice) {
//				sellPrice = sellPrice.setScale(0,BigDecimal.ROUND_DOWN);
				sellPrice = MoneyHelper.roundHalfUp(sellPrice);  // 兼容小数 - Yangcl
			}else{
				sellPrice = BigDecimal.ZERO;
			}
			if (StringUtils.isNotEmpty(market_price)) {
//				marketPrice = BigDecimal.valueOf(Double.parseDouble(market_price)).setScale(0,BigDecimal.ROUND_DOWN);
				marketPrice = MoneyHelper.roundHalfUp( BigDecimal.valueOf(Double.parseDouble(market_price)) ) ;  // 兼容小数 - Yangcl
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
			productInfo.setSellPrice(min_sell_price);
			productInfo.setMarkPrice(market_price);
			productInfo.setDiscount(discount);
//			if (AppConst.MANAGE_CODE_KJT.equals(productInfoMap
//					.get("small_seller_code"))||AppConst.MANAGE_CODE_MLG.equals(productInfoMap
//							.get("small_seller_code"))||AppConst.MANAGE_CODE_QQT.equals(productInfoMap
//									.get("small_seller_code"))
//									||AppConst.MANAGE_CODE_SYC.equals(productInfoMap.get("small_seller_code"))
//									||AppConst.MANAGE_CODE_CYGJ.equals(productInfoMap.get("small_seller_code"))) {
			if(new PlusServiceSeller().isKJSeller(productInfoMap.get("small_seller_code"))){
				productInfo.setFlagTheSea("1");
			}
			resultMap.put(productInfo.getProductCode(), productInfo);
		}
		return resultMap;
	}

	private List<HomeColumnContent> getFlashActivity() {
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		Map<String, FlashsalesSkuInfo> retMap = new HashMap<String, FlashsalesSkuInfo>();

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
	private List<HomeColumnContent> getFlashActivityNew(String sellerCode,String channelId) {
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		Map<String, FlashsalesSkuInfo> retMap = new HashMap<String, FlashsalesSkuInfo>();
		PlusActiveProduct activeProduct = new PlusActiveProduct();
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
								"SELECT p.video_url,p.mainpic_url,p.product_status,p.market_price,p.product_code,p.product_name,p.cost_price from pc_skuinfo s LEFT JOIN pc_productinfo p on s.product_code=p.product_code where s.sku_code=:sku_code",
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
					retMap.put(skuInfo.getSku_code(), skuInfo);
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
			// 到后面会该成product_code
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

	private List<HomeColumnContent> getTVDataList() {
//		String videoUrlTV = bConfig("familyhas.video_url_TV");
		String videoUrlTV = "";
		; // TV直播链接
		List<HomeColumnContent> columnContentList = new ArrayList<HomeColumnContent>();
		String swhere = "form_fr_date<='" + systemTime
				+ "' and form_end_date >= '" + systemTime + "' and so_id='1000001'";
		List<MDataMap> TVlist = DbUp.upTable("pc_tv").queryAll("",
				"form_fr_date", swhere, new MDataMap());
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
					.getProductInfo(productCodeArr, userType);
			Map<String,String> picUrlMap = new HashMap<String, String>();
			List<String> picUrlArr = new ArrayList<String>();
			//获取商品信息中的商品图片，用来进行压缩
			for (String productCode : productInfoMap.keySet()) {
				picUrlArr.add(productInfoMap.get(productCode).getMainpicUrl());
			}
			for (MDataMap map : TVlist) {
				picUrlArr.add(map.get("pic_url"));
			}
			//压缩图片
			List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(
					Integer.parseInt(maxWidth), picUrlArr);
			for (PicInfo picInfo : picInfoList) {
				picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			}
			for (String productCode : productInfoMap.keySet()) {
				String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
				if (StringUtils.isNotBlank(mainpicUrl)) {
					productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
				}
			}
			for (MDataMap map : TVlist) {
				HomeColumnContent columnContent = new HomeColumnContent();
				columnContent.getProductInfo().setProductCode(
						map.get("good_id"));
				columnContent.setStartTime(map.get("form_fr_date"));// 播出时间
				columnContent.setEndTime(map.get("form_end_date"));// 结束时间
				columnContent.setShowmoreLinktype("4497471600020004"); // 商品详情类型
				columnContent.setShowmoreLinkvalue(map.get("good_id")); // 商品Code
				columnContent.setVideoUrlTV(videoUrlTV);
				columnContent.setPicUrl(picUrlMap.get(map.get("pic_url")));

				// 根据商品编码查询sku信息
				// MDataMap productInfo =
				// DbUp.upTable("pc_productinfo").one("product_code",columnContent.getProductInfo().getProductCode());
				HomeColumnContentProductInfo productInfo = productInfoMap
						.get(columnContent.getProductInfo().getProductCode());
				if (null != productInfo) {
//					BigDecimal sellPrice = new BigDecimal(productInfo.getSellPrice()).setScale(0, BigDecimal.ROUND_DOWN); 
					BigDecimal sellPrice = MoneyHelper.roundHalfUp(new BigDecimal(productInfo.getSellPrice())); // 兼容小数 - Yangcl

					columnContent.getProductInfo().setProductName( productInfo.getProductName());
					columnContent.getProductInfo().setSellPrice( sellPrice.toString());
//					columnContent.getProductInfo().setMarkPrice( new BigDecimal(productInfo.getMarkPrice()).setScale(0, BigDecimal.ROUND_DOWN).toString());
					columnContent.getProductInfo().setMarkPrice(MoneyHelper.format(new BigDecimal(productInfo.getMarkPrice()))); // 兼容小数 - Yangcl
					columnContent.getProductInfo().setMainpicUrl( productInfo.getMainpicUrl());
					columnContent.getProductInfo().setFlagTheSea( productInfo.getFlagTheSea());
					columnContent.setPicture(productInfo.getMainpicUrl());

//					BigDecimal marketPrice = new BigDecimal(Double.valueOf(columnContent.getProductInfo().getMarkPrice())).setScale(0,BigDecimal.ROUND_DOWN);
					BigDecimal marketPrice = MoneyHelper.roundHalfUp(new BigDecimal(Double.valueOf(columnContent.getProductInfo().getMarkPrice()))); // 兼容小数 - Yangcl
					
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

	/**
	 * 获取分类名称
	 * 
	 * @param categoryCodeArr
	 * @return
	 */
	private MDataMap getCategoryName(List<String> categoryCodeArr) {
		MDataMap resultMap = new MDataMap();
		if (null == categoryCodeArr || categoryCodeArr.size() < 1) {
			return resultMap;
		}
		String sFields = "category_code,category_name";
		String sWhere = " seller_code='SI2003' and category_code in ('"
				+ StringUtils.join(categoryCodeArr, "','") + "')";
		List<MDataMap> categoryNameMapList = DbUp.upTable("uc_sellercategory")
				.queryAll(sFields, "", sWhere, null);
		for (MDataMap categoryNameMap : categoryNameMapList) {
			String categoryCode = categoryNameMap.get("category_code");
			String categoryName = categoryNameMap.get("category_name");
			resultMap.put(categoryCode, categoryName);
		}
		return resultMap;
	}

	// 闪购排序用
	private void sortSkuList(List<FlashsalesSkuInfo> list) {

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

	private String lpad(int length, int number) {
		String f = "%0" + length + "d";
		return String.format(f, number);
	}
}
