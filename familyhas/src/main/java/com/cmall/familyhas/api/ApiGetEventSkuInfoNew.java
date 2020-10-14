package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.model.BusinessLicenseModel;
import com.cmall.familyhas.api.model.CcVideo;
import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.CityInfo;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.ProvinceInfo;
import com.cmall.familyhas.api.result.ApiGetSkuInfoNewResult;
import com.cmall.familyhas.service.cc.CCVideoService;
import com.cmall.familyhas.util.ProCityLoader;
import com.cmall.groupcenter.baidupush.core.utility.DismantlOrderUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.familyhas.active.ActiveForproduct;
import com.cmall.ordercenter.familyhas.active.ActiveReq;
import com.cmall.ordercenter.familyhas.active.ActiveResult;
import com.cmall.ordercenter.familyhas.active.ActiveReturn;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.model.ReminderContent;
import com.cmall.productcenter.service.CategoryService;
import com.cmall.productcenter.service.MyService;
import com.cmall.productcenter.service.ProductLogoService;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadEventExcludeProduct;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadProductSales;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadTemplateAreaCode;
import com.srnpr.xmassystem.modelevent.PlusModelEventExclude;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventOnlinePay;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.service.PlusServiceEventOnlinePay;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 新商品详情接口（基础数据走缓存390版本调用）
 * 
 * @author 李国杰 
 */
public class ApiGetEventSkuInfoNew extends
		RootApiForVersion<ApiGetSkuInfoNewResult, ApiGetSkuInfoInput> {
	
	private static String highPraise = "好评";
	private static String headUrlAccept = "449746600001";
	//审核通过
	private static String hasAccept = "4497172100030002";
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	static LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
	

	public ApiGetSkuInfoNewResult Process(ApiGetSkuInfoInput api, MDataMap mRequestMap) {
		if(StringUtils.isBlank(api.getBuyerType()))
		{
			api.setBuyerType("4497469400050002");
		}
		
		String eventProductCode = api.getProductCode();			//IC开头的商品编号
		ApiGetSkuInfoNewResult result = new ApiGetSkuInfoNewResult();
		ProductService pService = new ProductService();
		ProductLogoService plService = new ProductLogoService();
		Map<String, String> icMap = new HashMap<String, String>();
		if (PlusHelperEvent.checkEventItem(api.getProductCode())) {
			PlusModelEventItemProduct eventItemtInfo = new PlusSupportEvent().upItemProductByIcCode(api.getProductCode());
			if (null != eventItemtInfo) {
				api.setProductCode(eventItemtInfo.getProductCode());
				if(StringUtils.isNotBlank(eventItemtInfo.getSkuCode())&&StringUtils.isNotBlank(eventItemtInfo.getItemCode())){
					icMap.put(eventItemtInfo.getSkuCode(), eventItemtInfo.getItemCode());
				}
			}
		}
		
		if(StringUtils.isNotBlank(getApiClientValue("channelId"))) {
			api.setChannelId(getApiClientValue("channelId"));
		}
		
		Integer isPurchase = api.getIsPurchase();
		String productCode = getProductCode(api.getProductCode());
		String sellerCode = getManageCode();
		String maxBuyCount = bConfig("familyhas.pt_product_num");			//最大购买数
//		String fictitiousProductPic = bConfig("familyhas.fictitions_pic");	//虚拟商品增加的描述图片
//		String fictitionsPicStartTime = bConfig("familyhas.fictitions_pic_start_time");//虚拟商品增加的描述图片开始时间
//		String fictitionsPicEndTime = bConfig("familyhas.fictitions_pic_end_time");//虚拟商品增加的描述图片结束时间
		String vipSpecialTip = bConfig("familyhas.vipSpecialTip");	//内购用户提示信息
		
		String tuwenUrl = FormatHelper.formatString(bConfig("familyhas.tuwenUrl"),productCode);	//图文详情的页面链接
		String guigeUrl = FormatHelper.formatString(bConfig("familyhas.guigeUrl"),productCode);	//规格参数的页面链接
		String FQAUrl = FormatHelper.formatString(bConfig("familyhas.FQAUrl"),productCode);		//常见问题的页面链接
		
		// 橙意会员卡商品标识
		result.setPlusFlag(productCode.equals(bConfig("xmassystem.plus_product_code")) ? 1 : 0);
		
//		String userType = api.getBuyerType();
		
//		PcProductinfoForFamily product = new PcProductinfoForFamily();
		// 橙意会员卡商品屏蔽评论
		if(result.getPlusFlag() != 1) {
			//获取商品详情评论
			MDataMap paramsMap = new MDataMap();
			paramsMap.put("product_code", api.getProductCode());
			paramsMap.put("manage_code", getManageCode());
			paramsMap.put("check_flag", hasAccept);
			
			// 好评数和总评论数
			String sSql = "SELECT SUM(1) total, SUM(IF(grade_type = '好评',1,0)) highPraiseSize FROM	newscenter.nc_order_evaluation WHERE check_flag = '4497172100030002' AND product_code = :product_code";
			Map<String, Object> totalMap = DbUp.upTable("nc_order_evaluation").dataSqlOne(sSql, new MDataMap("product_code", productCode));
			if(totalMap != null && totalMap.get("total") != null) {
				result.setCommentSumCounts(new BigDecimal(totalMap.get("total")+"").intValue());
				// 好评率  = 好评数 / 总数 * 100  然后取整
				result.setHighPraiseRate(new BigDecimal(totalMap.get("highPraiseSize")+"").divide(new BigDecimal(totalMap.get("total")+""),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue()+"");
			}
			
			// 查询最近的一条好评，用户好评的排前面
			sSql = "SELECT * FROM newscenter.nc_order_evaluation WHERE pic_status = '449747510001' AND check_flag = '4497172100030002' and grade_type = '好评' AND product_code = :product_code AND ccvids != '' AND ccpics != '' ORDER BY  is_down, auto_good_evaluation_flag, oder_creattime desc limit 2";
			List<Map<String, Object>> commentMapList = DbUp.upTable("nc_order_evaluation").dataSqlList(sSql, new MDataMap("product_code", productCode));
			Integer limit = 2;
			if(commentMapList == null || commentMapList.size()<2) {
				if(commentMapList != null) {
					limit = 2-commentMapList.size();
				}
				sSql = "SELECT * FROM newscenter.nc_order_evaluation WHERE pic_status = '449747510001' AND  check_flag = '4497172100030002' and grade_type = '好评' AND product_code = :product_code AND ccvids = '' AND ccpics = '' ORDER BY  is_down, auto_good_evaluation_flag, oder_creattime desc limit "+limit;
				List<Map<String, Object>> commentMapList2 = DbUp.upTable("nc_order_evaluation").dataSqlList(sSql, new MDataMap("product_code", productCode));
				commentMapList.addAll(commentMapList2);
			}
			CCVideoService ccservice = new CCVideoService();
			for(Map<String,Object> commentMap : commentMapList) {
				CdogProductComment comment = new CdogProductComment();
				MDataMap mDataMap = new MDataMap(commentMap);
				comment.setCommentContent(mDataMap.get("order_assessment").replaceAll("&nbsp;"," ").replaceAll("&lt", "<").replaceAll("&gt", ">").replaceAll("&amp", "&"));
				comment.setCommentTime(mDataMap.get("oder_creattime").split(" ")[0]);
				comment.setGradeType(mDataMap.get("grade_type"));
				comment.setGrade(mDataMap.get("grade"));
				comment.setSkuCode(mDataMap.get("sku_code"));
				String ccvids = mDataMap.get("ccvids");
				String ccpics = mDataMap.get("ccpics");
				String duration = mDataMap.get("duration");
				if(StringUtils.isNotEmpty(ccvids)) {//添加视频信息
					String args[] = ccvids.split("\\|");
					String pics[] = ccpics.split("\\|");
					String dus[] = duration.split("\\|");
					if(args.length == pics.length && args.length == dus.length) {
						for(int i = 0;i<args.length;i++) {
							CcVideo cc = new CcVideo();
							cc.setCcvid(args[i]);
							cc.setImg(pics[i]);
							cc.setTime(Integer.parseInt(dus[i]));
							if(!StringUtils.isEmpty(cc.getImg())) {
								comment.getVideoList().add(cc);
							}
						}
					}
				}
				String commentPhotos = commentMap.get("oder_photos") != null?commentMap.get("oder_photos").toString():"";
				comment.setCommentPhotoList(pService.getPicForProduct(api.getPicWidth(), commentPhotos));
				String mobile = mDataMap.get("user_mobile");
				if(mobile != null) {
					MDataMap commentUser = DbUp.upTable("mc_member_sync").one("login_name", StringUtils.trimToEmpty(mobile));
					if(commentUser != null) {
						comment.setUserFace(StringUtils.trimToEmpty(commentUser.get("avatar")));
						comment.setUserMobile(StringUtils.trimToEmpty(commentUser.get("nickname")));
					} else {
						if(mobile.length() > 7){
							comment.setUserMobile(mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length()));
						}
					}
				}
				
				//MDataMap mSkuDataMap = new MDataMap();
				//查找sku规格颜色
				//mSkuDataMap = DbUp.upTable("pc_skuinfo").one("sku_code",mDataMap.get("order_skuid"));
				PlusModelSkuQuery query = new PlusModelSkuQuery();
				query.setCode(mDataMap.get("order_skuid"));
				PlusModelSkuInfo skuInfoModel = loadSkuInfo.upInfoByCode(query);
				
				if(skuInfoModel != null){
					String keyvalue = skuInfoModel.getSkuKeyvalue();
					if(StringUtility.isNotNull(keyvalue)){
						String[] values = keyvalue.split("&");
						if(values.length > 1){
							String[] colors = values[0].split("=");
							String[] styles = values[1].split("=");
							if(colors.length > 1 && colors[1].toString().indexOf("共同") < 0){
								comment.setSkuColor(colors[1].toString());
							}
							if(styles.length > 1 && styles[1].toString().indexOf("共同") < 0){
								comment.setSkuStyle(styles[1].toLowerCase());
							}
						}
					}
				}
			    result.getProductComment().add(comment);
			}
		}
		
		PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(productCode);
		PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();

		plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
		if (null == plusModelProductinfo || null == plusModelProductinfo.getProductCode()
				|| "".equals(plusModelProductinfo.getProductCode())) {
			return result;
		}
		
		// 追加默认服务卡图片
		if(StringUtils.isNotBlank(plusModelProductinfo.getDescription().getDescriptionPic())) {
			String serviceCardBlacklist = bConfig("familyhas.productdetail_service_card_blacklist");
			if(StringUtils.isEmpty(serviceCardBlacklist) || !serviceCardBlacklist.contains(plusModelProductinfo.getSmallSellerCode())) {
				plusModelProductinfo.getDescription().setDescriptionPic(plusModelProductinfo.getDescription().getDescriptionPic()+"|"+bConfig("familyhas.productdetail_service_card"));
			}
		}
		
		//524:添加返回商品分类标签
		String ssc =plusModelProductinfo.getSmallSellerCode();
		String st="";
		if("SI2003".equals(ssc)) {
			st="4497478100050000";
		}
		else {
			PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(ssc));
			if(sellerInfo != null){
				st = sellerInfo.getUc_seller_type();
			}
		}
		//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
		Map productTypeMap = WebHelper.getAttributeProductType(st);
		
		result.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
		//524:获取缓存中商户信息
		String smallSellerCode = plusModelProductinfo.getSmallSellerCode();
		if(StringUtils.isNotBlank(smallSellerCode)) {
			if(smallSellerCode.equals("SI2003")) {
				//LD商户
				smallSellerCode = bConfig("familyhas.small_seller_code_ld");
			} 
		
			PlusModelSellerQuery pmsq = new PlusModelSellerQuery(smallSellerCode);
			PlusModelSellerInfo sellerInfo = new LoadSellerInfo().upInfoByCode(pmsq);
			if(sellerInfo != null) {
				result.setSmallSellerCode(smallSellerCode);
				result.setSellerCompanyName(sellerInfo.getSellerCompanyName());
			}
		}
		
		//图片Map，key:picOldUrl，value:picNewUrl
		Map<String,PicInfo> picUrlMap = new HashMap<String, PicInfo>();
		picUrlMap.put(plusModelProductinfo.getMainpicUrl(), new PicInfo());			//主图
//		if (StringUtils.isNotEmpty(fictitiousProductPic)) {	//虚拟商品增加的描述图片
//			picUrlMap.put(fictitiousProductPic, new PicInfo());
//		}
		
		for (String pic : plusModelProductinfo.getPcPicList()) {	// 轮播图
			picUrlMap.put(pic, new PicInfo());
		}
		String[] descriptionPicArr = plusModelProductinfo.getDescription().getDescriptionPic().split("\\|");
		for (String descriptionPic : descriptionPicArr) {	//描述图片
			picUrlMap.put(descriptionPic, new PicInfo());
		}
		PlusModelProductLabel pmpl = new ProductLabelService().getLabelInfo(plusModelProductinfo.getProductCode());
		picUrlMap.put(pmpl.getInfoActivityPic(), new PicInfo());
		List<String> picUrlArr = new ArrayList<String>();
		for (String picUrl: picUrlMap.keySet()) {
			picUrlArr.add(picUrl);
		}
		List<PicInfo> picInfoList = pService.getPicInfoForMulti(Constants.IMG_WIDTH_SP01,picUrlArr);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo);
		}
		String productName = plusModelProductinfo.getProductName();
		productName = productName.replaceAll("</?[^>]+>", "");
		
		result.setProductCode(plusModelProductinfo.getProductCode()); // 商品编号
		result.setProductName(productName); // 商品名称
		result.setProductStatus(plusModelProductinfo.getProductStatus()); // 商品上下架状态
		//商品的录播视频
		result.setVideoUrl(plusModelProductinfo.getVideoUrl()); // 视频地址
		result.setVideoMainPic(plusModelProductinfo.getVideoMainPic()); // 介绍视频封面图地址
		result.setProductDescVideo(plusModelProductinfo.getProductDescVideo());//介绍视频地址。
		result.setExitVideo(StringUtils.isEmpty(plusModelProductinfo.getVideoUrl()) ? 0 : 1);	//是否有视频
		//当前商品如果是直播商品则返回直播链接.added by zht.20170619 
		if("SI2003".equals(plusModelProductinfo.getSmallSellerCode()) && isLiveStreaming(api.getProductCode())) {
			result.setExitVideo(1);
			result.setLiveUrl(bConfig("familyhas.video_url_TV"));
		}
		result.setBrandCode(plusModelProductinfo.getBrandCode()); // 品牌编号
		result.setBrandName(plusModelProductinfo.getBrandName()); // 品牌名称
		result.setMainpicUrl(picUrlMap.get(plusModelProductinfo.getMainpicUrl()));		//主图
		result.setMaxBuyCount(Integer.valueOf(maxBuyCount)); // 商品的最大购买数，暂时默认为99
		result.setCommonProblem(plService.getProductCommonProblem(plusModelProductinfo.getSellerCode(), plusModelProductinfo.getSmallSellerCode()));
		
		result.setFlagTheSea(plusModelProductinfo.getFlagTheSea()); 			//是否海外购
		result.setLabelsList(plusModelProductinfo.getLabelsList());          //商品标签
		result.setLabelsPic(pmpl.getInfoPic());
		
//		result.setIapic(pmpl.getInfoActivityPic());
//		result.setPumap(picUrlMap);
		
		result.setEventLabelPic( picUrlMap.get(pmpl.getInfoActivityPic()) );                     // 3.9.6新增 - Yangcl
		result.setEventLabelPicSkip(pmpl.getEventLabelPicSkip());     // 3.9.6新增 - Yangcl   
		
		result.setCategoryList(new CategoryService().getCategoryBaseInfoList(plusModelProductinfo.getCategorys(), plusModelProductinfo.getSellerCode()));
		result.setSellingPoint(plusModelProductinfo.getProductAdv());//商品广告语（卖点信息）
		result.setTuwenUrl(tuwenUrl);	//图文详情的页面链接
		result.setGuigeUrl(guigeUrl);	//规格参数的页面链接
		result.setFQAUrl(FQAUrl);		//常见问题的页面链接
		
		List<ProductSkuInfoForApi> skuList = new ArrayList<ProductSkuInfoForApi>();
		
		//获取到所有sku对应的清分比例金额,返现比例
		//Map<String,BigDecimal> skuScaleReckonMap = new HashMap<String, BigDecimal>();
		
		Map<String, ActiveReturn> activeInfoMap = new HashMap<String, ActiveReturn>();   //sku参加的活动
		ActiveReturn productActiveInfo = new ActiveReturn();	//此商品参加的活动
		String buyerCode = getFlagLogin() ? getOauthInfo().getUserCode() : null;
		List<String> skuCodeArr = new ArrayList<String>();
		for (PlusModelProductSkuInfo sku : plusModelProductinfo.getSkuList()) {
			skuCodeArr.add(sku.getSkuCode());
		}
		//skuScaleReckonMap = new ShopCartService().getScaleReckonMap(buyerCode,skuCodeArr, sellerCode);
		
		//特价
		PlusSupportProduct support = new PlusSupportProduct();
		String supportPriceNote = "";		//特价名称
		BigDecimal supportPrice = null;		//特价价格
		String eventCode = "",ldEventCode = "",eventType="";
		for (PlusModelProductSkuInfo sku : plusModelProductinfo.getSkuList()) {
			ProductSkuInfoForApi skuObj = new ProductSkuInfoForApi();
			skuObj.setSkuCode(sku.getSkuCode());
			skuObj.setSkuName(sku.getSkuName());
			skuObj.setStockNumSum(0);
			skuObj.setSellPrice(sku.getSellPrice());
			skuObj.setMarketPrice(plusModelProductinfo.getMarketPrice());
			skuObj.setKeyValue(sku.getSkuKey());
			skuObj.setMiniOrder(sku.getMiniOrder());	//起订数量
			//特价
			if (1 != result.getFlagCheap()) {
				String skuCode = sku.getSkuCode();
				if(icMap.containsKey(sku.getSkuCode())){
					skuCode=icMap.get(sku.getSkuCode());
				}
				PlusModelSkuQuery pq = new PlusModelSkuQuery();
				pq.setCode(skuCode);
				pq.setMemberCode(StringUtils.trimToEmpty(buyerCode));
				pq.setIsPurchase(isPurchase);
				pq.setChannelId(api.getChannelId());
				PlusModelSkuInfo skuSuppore = support.upSkuInfo(pq).getSkus().get(0);
				if (StringUtils.isNotEmpty(skuSuppore.getEventCode()) || PlusHelperEvent.checkEventItem(api.getProductCode())) {
					if (null != supportPrice && supportPrice.compareTo(skuSuppore.getSellPrice()) > 0 ) {
						supportPrice = skuSuppore.getSellPrice();
						supportPriceNote = skuSuppore.getSellNote();
					}else if (null == supportPrice ) {
						supportPrice = skuSuppore.getSellPrice();
						supportPriceNote = skuSuppore.getSellNote();
					}
					int maxBuy = (int)skuSuppore.getMaxBuy();	//最大购买数
					skuObj.setSkuMaxBuy(maxBuy);
					skuObj.setSellPrice(supportPrice);
					skuObj.setLimitBuy((int)skuSuppore.getLimitBuy());
					result.setVipSecKill(1);		//商品设置为参加促销活动
					
					skuObj.setStockNumSum((int)skuSuppore.getLimitStock());
				}
				
				// 取惠家有活动编号已经对应的LD活动编号
				if(StringUtils.isBlank(eventCode) && StringUtils.isNotBlank(skuSuppore.getEventCode())) {
					eventCode = skuSuppore.getEventCode();
					eventType = skuSuppore.getEventType();
					PlusModelEventQuery q = new PlusModelEventQuery();
					q.setCode(eventCode);
					PlusModelEventInfo evi = new LoadEventInfo().upInfoByCode(q);
					
					if(evi != null && StringUtils.isNotBlank(evi.getEventCode())) {
						ldEventCode = evi.getOutActiveCode();
					}
				}
			}
			//skuObj.setDisMoney(skuObj.getSellPrice().multiply(skuScaleReckonMap.get(skuObj.getSkuCode())));
			//返现取最低
			//if (result.getDisMoney() == null 
			//		|| result.getDisMoney().compareTo(BigDecimal.ZERO) <= 0
			//		|| result.getDisMoney().compareTo(skuObj.getDisMoney()) > 0) {
			//	result.setDisMoney(skuObj.getDisMoney());
			//}
			
			skuList.add(skuObj);
		}
		result.setSkuList(skuList);			//sku信息
		if (null != supportPrice) {
			result.setPriceLabel(supportPriceNote);
		}
		
		result.setEventCode(eventCode);
		result.setLdEventCode(ldEventCode);
		
		List<PicInfo> picList = new ArrayList<PicInfo>();
		for (String pic : plusModelProductinfo.getPcPicList()) {
			picList.add(picUrlMap.get(pic));
		}
		result.setPcPicList(picList); // 轮播图
		
		if (null != plusModelProductinfo.getDescription()) {
			// 内容图片
			List<String> discriptList = new ArrayList<String>();
			String[] descriptArr = plusModelProductinfo.getDescription().getDescriptionPic()
					.split("\\|");
			if (null != descriptArr && descriptArr.length > 0) {
				discriptList = Arrays.asList(descriptArr);
			}
			//虚拟商品，并且虚拟商品描述图片不为空时添加到描述图片列表中,并且在有效期内
//			SimpleDateFormat sf = new SimpleDateFormat("MM-dd hh:mm:ss");
//			String nowTime = sf.format(new Date());
//			if (nowTime.compareTo(fictitionsPicStartTime) >= 0 
//					&&nowTime.compareTo(fictitionsPicEndTime) < 0
//					&&"Y".equals(plusModelProductinfo.getValidateFlag()) 
//					&& StringUtils.isNotEmpty(fictitiousProductPic)) {
//				result.getDiscriptPicList().add(picUrlMap.get(fictitiousProductPic));
//			}
			for (String descriptPic : discriptList) {
				result.getDiscriptPicList().add(picUrlMap.get(descriptPic)); // 内容图片
			}
//			划线价价格隐藏，屏蔽划线价的图片说明
//			if(result.getDiscriptPicList().size() != 0){
//				PicInfo p = result.getDiscriptPicList().get(0);
//				PicInfo e = new PicInfo();
//				e.setHeight(p.getHeight());
//				e.setOldHeight(p.getOldHeight());
//				e.setOldWidth(p.getOldWidth());
//				e.setWidth(p.getWidth());  
//				e.setPicNewUrl(this.bConfig("familyhas.price_url_pic"));
//				e.setPicOldUrl(this.bConfig("familyhas.price_url_pic")); 
//				result.getDiscriptPicList().add(e);
//			}
			
		}
		result.setPropertyList(plusModelProductinfo.getPropertyList()); // 商品规格值
		

		//自定义属性,过滤不展示内联赠品
		for (PlusModelPropertyInfo plusModelPropertyInfo : plusModelProductinfo.getPropertyInfoList()) {
			if ("内联赠品".equals(plusModelPropertyInfo.getPropertykey())) {
				continue;
			}
			result.getPropertyInfoList().add(plusModelPropertyInfo);	
		}
		
		if(getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS)){
				//赠品信息
				DismantlOrderUtil dis = new DismantlOrderUtil();
				Map<String,String> giftMap = dis.getProductGifts(eventProductCode,api.getChannelId());
				String gift = giftMap.get(plusModelProductinfo.getProductCode());
				if (StringUtils.isNotEmpty(gift)) {
					result.setFlagIncludeGift(1);
					result.setGift(gift);
				}
		}
		//权威标志
//		result.setAuthorityLogo(plusModelProductinfo.getAuthorityLogo());
		result.setAuthorityLogo(plService.getProductAuthorityLogo(plusModelProductinfo.getSellerCode(), plusModelProductinfo.getSmallSellerCode() , plusModelProductinfo.getProductCode()));
		//统计销量
//		List<String> productCodesArr = new ArrayList<String>();
//		productCodesArr.add(plusModelProductinfo.getProductCode());
//		MDataMap salesMap = pService.getProductFictitiousSales("",productCodesArr,30);
//		if (null != salesMap && !salesMap.isEmpty()) {
//			if (StringUtils.isNotEmpty(salesMap.get(plusModelProductinfo.getProductCode()))) {
//				result.setSaleNum(salesMap.get(plusModelProductinfo.getProductCode()));
//			}
//		}
		PlusModelProductSales productSales = new LoadProductSales().upInfoByCode(plusModelProductQuery);
		result.setSaleNum(productSales.getFictitionSales30()+"");
		result.setCollectionProduct(pService.getIsCollectionProduct(plusModelProductinfo.getProductCode(), getFlagLogin() ? getOauthInfo().getUserCode() : null));
		
		boolean flagVipSpecial = false;
		//不参加闪购与不参加特价时才去判断内购价格(暂时只有惠家有走的逻辑，等内购走促销系统后这些代码可以删除了呗)
		if (AppConst.MANAGE_CODE_HOMEHAS.equals(sellerCode)&&VersionHelper.checkServerVersion("3.5.62.55")) {
			if (1 != result.getFlagCheap() && 1 != result.getVipSecKill()) {
				if (getFlagLogin()) {
					activeInfoMap = this.getVipSpecialPriceForFamilyhas(skuCodeArr, productCode, buyerCode);
				}
				if (null != activeInfoMap && !activeInfoMap.isEmpty()) {
					for (String string : activeInfoMap.keySet()) {
						productActiveInfo = activeInfoMap.get(string);
						break;
					}
				}
				//参加的活动为内购活动
				if (bConfig("familyhas.vipSpecialActivityTypeCode").equals(productActiveInfo.getActivity_type())) {
					flagVipSpecial = true;
				}
			}
		}
				
		//商品内购相关在这里
		if (flagVipSpecial) {
			result.setVipSpecialTip(vipSpecialTip);				//内购提示信息
			result.setVipSpecialActivity(1);					//标志是否参加内购
			result.setVipSpecialPrice(productActiveInfo.getActivity_price()+"");	//内购价格
			
			for (int i = 0; i <  result.getSkuList().size(); i++) {
				result.getSkuList().get(i).setDisMoney(BigDecimal.ZERO);
				result.getSkuList().get(i).setVipSpecialPrice(productActiveInfo.getActivity_price()+"");
			}
			
			result.setDisMoney(BigDecimal.ZERO);			//内购不参加返现
		}
		//价格标签(优先级  闪购  > 特价 > 内购)
		 if (1 == result.getFlagCheap()) {
			result.setPriceLabel(bConfig("familyhas.priceLabelFlash"));
		}else if (1 == result.getVipSpecialActivity()) {
			result.setPriceLabel(bConfig("familyhas.priceLabelVipSpecial"));
		}
//		Map<String,Integer> btnMap = new HashMap<String, Integer>();
		
		if (StringUtils.isNotEmpty(buyerCode)) {
			List<String> productCodes = new ArrayList<String>();
			productCodes.add(productCode);
			new ApiAddBrowseHistory().addBrowseHistory(productCodes, buyerCode);
		}
		
		
		//商品详情页地址相关
		MDataMap pcgovMap = new MDataMap();
		List<MDataMap> pcgovList= ProCityLoader.queryAll(); //DbUp.upTable("sc_tmp").queryAll("code,name", "", "", new MDataMap());//查出所有的省市区信息
		for (MDataMap mDataMap : pcgovList) {
			pcgovMap.put(mDataMap.get("code"), mDataMap.get("name"));
		}
		CityInfo city = new CityInfo();
		if (StringUtils.isNotEmpty(buyerCode)) {
			//按当前用户编号查询该用户的缺省收货地址
			AddressInformation addressInfomation = new AddressService().getAddressOne("",buyerCode,plusModelProductinfo.getSellerCode());
			String areaCode = "";
			if (null != addressInfomation) {
				areaCode = addressInfomation.getArea_code();
			}
			if (StringUtils.isNotBlank(areaCode) && areaCode.length() >= 6 ) {		//区域编码应该为6位
				String cityCode = (areaCode.substring(0, areaCode.length()-2)+"00");
				String cityName = pcgovMap.get(cityCode);
				if (StringUtils.isNotBlank(cityName)) {
					city.setCityID(cityCode);
					city.setCityName(cityName);
					String provinceCode = (cityCode.substring(0, cityCode.length()-4)+"0000");
					String provinceName = pcgovMap.get(provinceCode);
					city.setProvinceID(provinceCode);
					city.setProvinceName(provinceName);
				}
			}
		}
		PlusModelTemplateAeraCode plusAeraCode = new PlusModelTemplateAeraCode();
		if(StringUtils.isNotBlank(plusModelProductinfo.getAreaTemplate())){
			PlusModelTemplateAreaQuery taq = new PlusModelTemplateAreaQuery();
			taq.setCode(plusModelProductinfo.getAreaTemplate());
			plusAeraCode = new LoadTemplateAreaCode().upInfoByCode(taq);
		}
		List<ProvinceInfo> addressList = new ArrayList<ProvinceInfo>();		//可配送区域地址
		if (null != plusAeraCode) {
			List<String> areaCodes = plusAeraCode.getAreaCodes();
			if (null != areaCodes) {
				Map<String,Map<String,String>> provinceInfoMap = new HashMap<String, Map<String,String>>();		//key:省级编号，value(key:市级编号，value:市级名称)
				for (String area_code : areaCodes) {
					String cityCode = (area_code.substring(0, area_code.length()-2)+"00");
					String cityName = pcgovMap.get(cityCode);
					
					String provinceCode = (area_code.substring(0, area_code.length()-4)+"0000");
				
					Map<String,String> cityMap = new HashMap<String, String>();			//key:市级编号，value:市级名称
					if (provinceInfoMap.containsKey(provinceCode)) {
						cityMap = provinceInfoMap.get(provinceCode);
					}
					cityMap.put(cityCode, cityName);
					provinceInfoMap.put(provinceCode, cityMap);
				}
				for (String provinceCodeMapKey : provinceInfoMap.keySet()) {
					ProvinceInfo provinceInfo = new ProvinceInfo();
					List<CityInfo> cityList = new ArrayList<CityInfo>();
					Map<String,String> cityMap = provinceInfoMap.get(provinceCodeMapKey);
					for (String cityCodeMapKey : cityMap.keySet()) {
						CityInfo city_ = new CityInfo();
						city_.setCityID(cityCodeMapKey);
						city_.setCityName(cityMap.get(cityCodeMapKey));
						cityList.add(city_);
					}
					provinceInfo.setProvinceID(provinceCodeMapKey);
					provinceInfo.setProvinceName(pcgovMap.get(provinceCodeMapKey));
					provinceInfo.setCityList(cityList);
					addressList.add(provinceInfo);
				}
				
				//省份ID升序排列
				Collections.sort(addressList, new Comparator<ProvinceInfo>() {
		            public int compare(ProvinceInfo arg0, ProvinceInfo arg1) {
		                return arg0.getProvinceID().compareTo(arg1.getProvinceID());
		            }
		        });
				//市ID升序排列
				for (int i = 0; i < addressList.size(); i++) {
					ProvinceInfo provinceInfo_ = addressList.get(i);
					List<CityInfo> cityInfoList = provinceInfo_.getCityList();
					Collections.sort(cityInfoList, new Comparator<CityInfo>() {
			            public int compare(CityInfo arg0, CityInfo arg1) {
			                return arg0.getCityID().compareTo(arg1.getCityID());
			            }
			        });
					addressList.get(i).setCityList(cityInfoList);
				}
				
			}
		}
		if (StringUtils.isBlank(city.getCityID()) && addressList.size() > 0 && addressList.get(0).getCityList().size() > 0) {
			city = addressList.get(0).getCityList().get(0);
			ProvinceInfo provinceInfo = addressList.get(0);
			city.setProvinceID(provinceInfo.getProvinceID());
			city.setProvinceName(provinceInfo.getProvinceName());
		}
		result.setDefaultAddress(city);				//默认地址
		result.setAddressList(addressList); 		//可配送地址列表
		
		List<String> smallSellers = new ArrayList<String>();
		smallSellers.add(plusModelProductinfo.getSmallSellerCode());
		List<ReminderContent> listr= new MyService().getReminderList(smallSellers,"4497471600270003");
		if(listr!=null&&!listr.isEmpty()){
			for (ReminderContent content :listr) {
				result.getTips().add(content.getContent());
			}
		}
		
//		String isShowDisMoney = XmasKv.upFactory(EKvSchema.CgroupMoney).get("view");
		// modify by zht.
		String isShowDisMoney = XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", "view");
		result.setIsShowDisMoney(StringUtils.isBlank(isShowDisMoney) ? 0 : Integer.parseInt(isShowDisMoney));
		
		
		//534版本返回商品所属商户营业执照信息
		String app_version = api.getApp_version();
		MDataMap map1 = DbUp.upTable("pc_productinfo").one("product_code",productCode);
		String seller1 = map1.get("small_seller_code");
		if(!"".equals(app_version)&&AppVersionUtils.compareTo(app_version, "5.3.2") > 0) {
			if("SI2003".equals(seller1)) {
				
			}else {
				result.setIsShowBusinessLicense("1");
				String sql = "SELECT b.seller_company_name,b.registration_number,b.legal_person,b.register_money,b.register_address,b.upload_business_license,b.uc_seller_type FROM productcenter.pc_productinfo a LEFT JOIN usercenter.uc_seller_info_extend b ON a.small_seller_code = b.small_seller_code WHERE a.product_code  =  '"+productCode+"';";
				List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productinfo").dataSqlList(sql, null);
				if(null!=dataSqlList) {
					Map<String, Object> map = dataSqlList.get(0);
					String sellerType = null==map.get("uc_seller_type")?"":map.get("uc_seller_type").toString();
					if(!"".equals(sellerType)) {
						if("4497478100050002".equals(sellerType)||"4497478100050003".equals(sellerType)||"4497478100050004".equals(sellerType)) {
							//跨境商户、跨境直邮商户、平台入驻商户 用商户上传的营业执照信息
							BusinessLicenseModel model = new BusinessLicenseModel();
							model.setSellerCompanyName(null==map.get("seller_company_name")?"":map.get("seller_company_name").toString());
							model.setRegistrationNumber(null==map.get("registration_number")?"":map.get("registration_number").toString());
							model.setLegalPerson(null==map.get("legal_person")?"":map.get("legal_person").toString());
							model.setRegisterMoney(null==map.get("register_money")?"":map.get("register_money").toString());
							model.setRegisterAddress(null==map.get("register_address")?"":map.get("register_address").toString());
							model.setUploadBusinessLicense(null==map.get("upload_business_license")?"":map.get("upload_business_license").toString());
							result.setBusinessLicense(model);
						}else if("4497478100050001".equals(sellerType)||"4497478100050005".equals(sellerType)){
							//普通商户（代销），缤纷商户供应商商品营业执照统一展示为“惠家有的营业执照”
							
							String sql1 = "SELECT b.seller_company_name,b.registration_number,b.legal_person,b.register_money,b.register_address,b.upload_business_license,b.uc_seller_type FROM  usercenter.uc_seller_info_extend b WHERE b.small_seller_code = 'SF03100135' ;";
							List<Map<String, Object>> dataSqlList1 = DbUp.upTable("uc_seller_info_extend").dataSqlList(sql1, null);
							if(null!=dataSqlList1) {
								Map<String, Object> map2 = dataSqlList1.get(0);
								BusinessLicenseModel model = new BusinessLicenseModel();
								model.setSellerCompanyName(null==map2.get("seller_company_name")?"":map2.get("seller_company_name").toString());
								model.setRegistrationNumber(null==map2.get("registration_number")?"":map2.get("registration_number").toString());
								model.setLegalPerson(null==map2.get("legal_person")?"":map2.get("legal_person").toString());
								model.setRegisterMoney(null==map2.get("register_money")?"":map2.get("register_money").toString());
								model.setRegisterAddress(null==map2.get("register_address")?"":map2.get("register_address").toString());
								model.setUploadBusinessLicense(null==map2.get("upload_business_license")?"":map2.get("upload_business_license").toString());
								result.setBusinessLicense(model);
							}
						}
					}
				}
				
			}
		}
		//新增字段，是否支持在线支付立减。
		this.checkSupportOnlinePayCut(smallSellerCode, productCode,api.getChannelId(),eventType,result);
		
		// 如果是从app直播间进入的商品详情,记录商品点击数据
		String liveRoomId = api.getLiveRoomId();
		if(StringUtils.isNotBlank(liveRoomId)) {
			rememberLiveRoomProd(liveRoomId,eventProductCode,StringUtils.trimToEmpty(buyerCode));
		}
		
		// 扫码购添加特殊参数
		if(eventProductCode.startsWith("IC_SMG")) {
			// 农场入口
			if("Y".equalsIgnoreCase(bConfig("familyhas.nc_show_flag"))) {
				result.getHhFarm().setLogo(bConfig("familyhas.nc_show_logo"));
				result.getHhFarm().setWebURL(bConfig("familyhas.nc_show_url"));
			}
			
			result.getTopGuide().setShow(Boolean.parseBoolean((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","46992333", "define_dids", "469923330001"))));
			result.getTopGuide().setGuideType(NumberUtils.toInt(((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","46992333", "define_dids", "469923330002")))));
			result.getTopGuide().setGuideValue(StringUtils.trimToEmpty((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","46992333", "define_dids", "469923330003"))));
			result.getTopGuide().setGuideImage(StringUtils.trimToEmpty((String)DbUp.upTable("zw_define").dataGet("define_note", "", new MDataMap("parent_did","46992333", "define_dids",  "469923330004"))));
		}
		
		// 判断是否为小程序推广赚分享
		String shortCode = api.getShortCode();
		if(!"".equals(shortCode)) {
			// 有小程序推广赚分享参数唯一编号,说明是小程序推广赚分享,取出分享参数返回
			String shortContent = "";
			MDataMap one = DbUp.upTable("fh_short_code").one("code",shortCode);
			if(one != null) {
				shortContent = MapUtils.getString(one, "content");
			}
			result.setShortContent(shortContent);
		}
		
		return result;
	}
	
	/**
	 * 记录app直播间进入的商品详情点击数据
	 * @param liveRoomId
	 * @param eventProductCode
	 * @param buyerCode
	 */
	private void rememberLiveRoomProd(String liveRoomId, String eventProductCode, String buyerCode) {
		MDataMap liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId,"is_delete","0");
		if(liveRoom != null) {
			// 先查询用户是否已经在该直播间看过这款商品
			MDataMap one = DbUp.upTable("lv_live_room_product_statistics").one("live_room_id",liveRoomId,"product_code",eventProductCode,"member_code",buyerCode,"behavior_type", "449748620001");
			if(one != null) {
				// 看过,更新点击数
				int num = MapUtils.getIntValue(one, "num");
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", MapUtils.getString(one, "uid"));
				mDataMap.put("num", num+1+"");
				DbUp.upTable("lv_live_room_product_statistics").dataUpdate(mDataMap, "num", "uid");
			}else {		
				// 没看过,记录商品点击信息
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("live_room_id", liveRoomId);
				mDataMap.put("product_code", eventProductCode);
				mDataMap.put("member_code", buyerCode);
				mDataMap.put("behavior_type", "449748620001");
				mDataMap.put("num", "1");
				DbUp.upTable("lv_live_room_product_statistics").dataInsert(mDataMap);
			}
		}
	}

	/**
	 * 校验是否支持在线支付立减。
	 * @return
	 * 2020年4月23日
	 * Angel Joy
	 * String
	 */
	private void checkSupportOnlinePayCut(String smallSellerCode,String productCode,String channelId,String eventType,ApiGetSkuInfoNewResult result) {
		if(!bConfig("familyhas.small_seller_code_ld").equals(smallSellerCode)) {//非LD商品不支持。
			return;
		}
		// 橙意会员卡商品不参与
		if(productCode.equals(TopConfig.Instance.bConfig("xmassystem.plus_product_code"))) {
			return ;
		}
		//过滤是否是拼团商品
		if("4497472600010024".equals(eventType)) {
			return;
		}
		PlusServiceEventOnlinePay plusServiceEventOnlinePay = new PlusServiceEventOnlinePay();
		PlusModelEventOnlinePay event = plusServiceEventOnlinePay.getEventOnlinePay(channelId);
		//当前不存在在线支付立减活动，返回不支持。
		if(event == null) {
			return;
		}
		BigDecimal favorablePrice = BigDecimal.ZERO;
		try {
			favorablePrice = new BigDecimal(event.getFavorablePrice()).setScale(2,BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(favorablePrice == null || favorablePrice.compareTo(BigDecimal.ZERO) <= 0){
			return;
		}
		//校验是否是排除商品，在活动排除列表则不参与活动
		LoadEventExcludeProduct loadEventExcludeProduct = new LoadEventExcludeProduct();
		PlusModelEventExclude excludeProduct = loadEventExcludeProduct.upInfoByCode(new PlusModelQuery(event.getEventCode()));
		if(excludeProduct != null&&excludeProduct.getProductCodeList().contains(productCode)) {
			return;
		}
		result.setSupplyOnlinePayCut("Y");
		result.setOnlinePayCut(favorablePrice);
		
	}
	
	/**
	 * 获取商品编号
	 * @return
	 */
	public String getProductCode(String code){
		if (PlusHelperEvent.checkEventItem(code)) {
			PlusModelEventItemProduct eventItemtInfo = new PlusSupportEvent().upItemProductByIcCode(code);
			if (null != eventItemtInfo) {
				return eventItemtInfo.getProductCode();
			}
		}
		return code;
	}
	/**
	 * 获取惠家有商品内购价格
	 * @return
	 */
	public Map<String, ActiveReturn> getVipSpecialPriceForFamilyhas(List<String> skuCodeArr,String productCode,String userCode){
		Map<String, ActiveReturn> activeInfoMap = new HashMap<String, ActiveReturn>();
		
		if (null == skuCodeArr || skuCodeArr.size() == 0 || StringUtils.isEmpty(userCode) || StringUtils.isEmpty(productCode)) {
			return activeInfoMap;
		}
		ActiveForproduct activeInfo = new ActiveForproduct();
		List<ActiveReq> activeRequests = new ArrayList<ActiveReq>();
		for (String skuCode : skuCodeArr) {
			ActiveReq activeReq = new ActiveReq();
			activeReq.setBuyer_code(userCode);
			activeReq.setSku_code(skuCode);
			activeReq.setProduct_code(productCode);
			activeReq.setSku_num(0);
			activeRequests.add(activeReq);
		}
		ActiveResult activeResult = new ActiveResult();
		activeInfoMap = activeInfo.activeGallery(activeRequests, activeResult);		//判断sku参加的各种活动
		return activeInfoMap;
	}
	
	private boolean isLiveStreaming(String productCode) {
		String now = dateFormater.format(new Date());
		String swhere = "form_fr_date<='"+now+"' and form_end_date>='"+now+"' and good_id='" + productCode + "' and so_id='1000001'";
		List<MDataMap> liveList = DbUp.upTable("pc_tv").queryAll("", "form_fr_date", swhere, new MDataMap());
		return liveList != null && liveList.size() > 0 ? true : false;
	}
}