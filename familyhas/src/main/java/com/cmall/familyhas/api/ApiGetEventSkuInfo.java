package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.model.ActivitySell;
import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.PcAuthorityLogoModel;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.PropertyInfoForProtuct;
import com.cmall.familyhas.api.model.PropertyValueInfo;
import com.cmall.familyhas.api.model.Propertyinfo;
import com.cmall.familyhas.api.result.ApiGetSkuInfoResult;
import com.cmall.familyhas.model.CommonProblem;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.groupcenter.baidupush.core.utility.DismantlOrderUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.familyhas.active.ActiveForproduct;
import com.cmall.ordercenter.familyhas.active.ActiveReq;
import com.cmall.ordercenter.familyhas.active.ActiveResult;
import com.cmall.ordercenter.familyhas.active.ActiveReturn;
import com.cmall.productcenter.model.PcProductinfoForFamily;
import com.cmall.productcenter.model.PcProductpic;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.model.ProductSkuInfoForFamily;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerAuthorityLogo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelProductAuthorityLogoQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 商品详情(作废的接口，老版本使用的商品详情页接口，app已经强升)
 * 
 * @deprecated
 * @author 李国杰 
 */
public class ApiGetEventSkuInfo extends
		RootApiForMember<ApiGetSkuInfoResult, ApiGetSkuInfoInput> {
private static String highPraise = "好评";
	
	private static String headUrlAccept = "449746600001";
	//审核通过
	private static String hasAccept = "4497172100030002";
	
	static LoadSellerAuthorityLogo loadSellerAuthorityLogo = new LoadSellerAuthorityLogo();
	static LoadProductInfo loadProductInfo = new LoadProductInfo();

	public ApiGetSkuInfoResult Process(ApiGetSkuInfoInput api,
			MDataMap mRequestMap) {
		if(StringUtils.isBlank(api.getBuyerType()))
		{
			api.setBuyerType("4497469400050002");
		}
		ApiGetSkuInfoResult result = new ApiGetSkuInfoResult();
		ProductService pService = new ProductService();
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
		String productCode = api.getProductCode();
		String sellerCode = getManageCode();
		String familyPriceName = bConfig("familyhas.family_price_name");	//家有价
		String maxBuyCount = bConfig("familyhas.pt_product_num");			//最大购买数
//		String fictitiousProductPic = bConfig("familyhas.fictitions_pic");	//虚拟商品增加的描述图片
//		String fictitionsPicStartTime = bConfig("familyhas.fictitions_pic_start_time");//虚拟商品增加的描述图片开始时间
//		String fictitionsPicEndTime = bConfig("familyhas.fictitions_pic_end_time");//虚拟商品增加的描述图片结束时间
		String shareUrl = bConfig("familyhas.shareUrl");		//分享链接
		String vipSpecialTip = bConfig("familyhas.vipSpecialTip");	//内购用户提示信息
//		String userType = api.getBuyerType();
		PcProductinfoForFamily product = new PcProductinfoForFamily();
		Integer width = api.getPicWidth();
		//增加详情评论
		//获取商品详情评论
		MDataMap paramsMap = new MDataMap();
		paramsMap.put("product_code", api.getProductCode());
		paramsMap.put("manage_code", getManageCode());
		paramsMap.put("check_flag", hasAccept);
		List<MDataMap> commentList = DbUp.upTable("nc_order_evaluation").queryAll("*", "-grade,-oder_creattime", "product_code=:product_code and manage_code=:manage_code and check_flag=:check_flag", paramsMap);
		int highPraiseCount = 0;
		for (MDataMap map : commentList) {
			if(highPraise.equals(map.get("grade_type"))){
				highPraiseCount++;
			}
		}
		CdogProductComment comment = new CdogProductComment();
		if(commentList.size() != 0){
			MDataMap mDataMap = commentList.get(0);
			comment.setCommentContent(mDataMap.get("order_assessment").replaceAll("&nbsp;"," ").replaceAll("&lt", "<").replaceAll("&gt", ">").replaceAll("&amp", "&"));
			comment.setCommentTime(mDataMap.get("oder_creattime").split(" ")[0]);
			comment.setGradeType(mDataMap.get("grade_type"));
			comment.setGrade(mDataMap.get("grade"));
			comment.setSkuCode(mDataMap.get("sku_code"));
			String mobile = mDataMap.get("user_mobile");
			String userCode = mDataMap.get("order_name");
			MDataMap nickMap = DbUp.upTable("mc_extend_info_star").oneWhere("nickname,member_avatar,status", "", "", "member_code",userCode,"app_code",getManageCode());
			if(nickMap != null && nickMap.get("status").equals(headUrlAccept))
				comment.setUserFace(nickMap.get("member_avatar"));
			String nickName = nickMap == null ? "":nickMap.get("nickname");
			if(nickMap!=null && StringUtility.isNotNull(nickName) && !nickName.contains("*")){
				if(nickName.length() == 2){
					comment.setUserMobile(nickName.substring(0, 1)+"*");
				}else{
					comment.setUserMobile(nickName.substring(0, 1)+"****"+nickName.substring(nickName.length()-1, nickName.length()));
				}
			}else{
				if(mobile.length() > 7){
					comment.setUserMobile(mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length()));
				}else{
					comment.setUserMobile(nickName);
				}		
			}
			result.setCommentSumCounts(commentList.size());
			MDataMap mSkuDataMap = new MDataMap();
			//查找sku规格颜色
			mSkuDataMap = DbUp.upTable("pc_skuinfo").one("sku_code",mDataMap.get("order_skuid"));
			if(mSkuDataMap != null){
				String keyvalue = mSkuDataMap.get("sku_keyvalue");
				if(StringUtility.isNotNull(keyvalue)){
					String[] values = keyvalue.split("&");
					if(values.length > 1){
						String[] colors = values[0].split("=");
						String[] styles = values[1].split("=");
						if(colors.length > 1){
							comment.setSkuColor(colors[1].toString());
						}
						if(styles.length > 1){
							comment.setSkuStyle(styles[1].toLowerCase());
						}
					}
				}
			}
		    result.getProductComment().add(comment);
		    if(commentList.size() != 0){
		    	double x = highPraiseCount * 1.0;
		    	double y = commentList.size() * 1.0;
		    	NumberFormat nf = NumberFormat.getPercentInstance();
		        nf.setMinimumFractionDigits( 0 );
		        String prise = nf.format(x/y);
		        result.setHighPraiseRate(prise.substring(0, prise.length()-1));
		    }
		}
		// 获取商品信息
		product = pService.getProductInfoForPP(productCode, sellerCode);
		if (null == product || null == product.getProductCode()
				|| "".equals(product.getProductCode())) {
			return result;
		}
		//图片Map，key:picOldUrl，value:picNewUrl
		Map<String,PicInfo> picUrlMap = new HashMap<String, PicInfo>();
		picUrlMap.put(product.getMainPicUrl(), new PicInfo());			//主图
//		if (StringUtils.isNotEmpty(fictitiousProductPic)) {	//虚拟商品增加的描述图片
//			picUrlMap.put(fictitiousProductPic, new PicInfo());
//		}
		for (PcProductpic pic : product.getPcPicList()) {	// 轮播图
			picUrlMap.put(pic.getPicUrl(), new PicInfo());
		}
		String[] descriptionPicArr = product.getDescription().getDescriptionPic().split("\\|");
		for (String descriptionPic : descriptionPicArr) {	//描述图片
			picUrlMap.put(descriptionPic, new PicInfo());
		}
		List<String> picUrlArr = new ArrayList<String>();
		for (String picUrl: picUrlMap.keySet()) {
			picUrlArr.add(picUrl);
		}
		List<PicInfo> picInfoList = pService.getPicInfoForMulti(width,picUrlArr);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo);
		}
		String productName = product.getProdutName();
		productName = productName.replaceAll("</?[^>]+>", "");
		
		result.setFamilyPriceName(familyPriceName);		//家有价
		result.setProductCode(product.getProductCode()); // 商品编号
		result.setProductName(productName); // 商品名称
		result.setSellPrice(product.getMinSellPrice()); // 销售价
		result.setMarketPrice(product.getMarketPrice()); // 市场价
		result.setFlagSale(product.getFlagSale()); // 是否在售
		result.setProductStatus(product.getProductStatus()); // 商品上下架状态
		result.setVideoUrl(product.getVideoUrl()); // 视频地址
		result.setExitVideo(StringUtils.isEmpty(product.getVideoUrl()) ? 0 : 1);	//是否有视频
		result.setBrandCode(product.getBrandCode()); // 品牌编号
		result.setBrandName(product.getBrandName()); // 品牌名称
		result.setMainpicUrl(picUrlMap.get(product.getMainPicUrl()));		//主图
		result.setMaxBuyCount(Integer.valueOf(maxBuyCount)); // 商品的最大购买数，暂时默认为99
		result.setShareUrl(shareUrl+product.getProductCode());	//分享链接
//		if (AppConst.MANAGE_CODE_KJT.equals(product.getSmallSellerCode())
//				||AppConst.MANAGE_CODE_MLG.equals(product.getSmallSellerCode())
//				||AppConst.MANAGE_CODE_QQT.equals(product.getSmallSellerCode())
//				||AppConst.MANAGE_CODE_SYC.equals(product.getSmallSellerCode())
//				||AppConst.MANAGE_CODE_CYGJ.equals(product.getSmallSellerCode())) {
		if (new PlusServiceSeller().isKJSeller(product.getSmallSellerCode())) {
			result.setFlagTheSea("1");
			result.setCommonProblem(this.getCommonProblemForProductCode(product.getSellerCode(),product.getSmallSellerCode()));
		}
		List<ProductSkuInfoForApi> skuList = new ArrayList<ProductSkuInfoForApi>();
		
		//获取到所有sku对应的清分比例金额,返现比例
		Map<String,BigDecimal> skuScaleReckonMap = new HashMap<String, BigDecimal>();
		
		Map<String, ActiveReturn> activeInfoMap = new HashMap<String, ActiveReturn>();   //sku参加的活动
		ActiveReturn productActiveInfo = new ActiveReturn();	//此商品参加的活动
		String buyerCode = getFlagLogin() ? getOauthInfo().getUserCode() : null;
		List<String> skuCodeArr = new ArrayList<String>();
		for (ProductSkuInfoForFamily sku : product.getProductSkuInfoList()) {
			skuCodeArr.add(sku.getSkuCode());
		}
		skuScaleReckonMap = new ShopCartService().getScaleReckonMap(buyerCode,skuCodeArr, sellerCode);
		
		//特价
		PlusSupportProduct support = new PlusSupportProduct();
		String supportPriceNote = "";		//特价名称
		BigDecimal supportPrice = null;		//特价价格
		
		for (ProductSkuInfoForFamily sku : product.getProductSkuInfoList()) {
			ProductSkuInfoForApi skuObj = new ProductSkuInfoForApi();
			ActivitySell activityObj = new ActivitySell();
			skuObj.setSkuCode(sku.getSkuCode());
			skuObj.setSkuName(sku.getSkuName());
			skuObj.setStockNumSum(sku.getStockNumSum());
			skuObj.setSellPrice(sku.getSellPrice());
			skuObj.setMarketPrice(product.getMarketPrice());
			skuObj.setKeyValue(sku.getSkuKey());
			skuObj.setMiniOrder(sku.getMiniOrder());	//起订数量
			if (sku.getFkiObj() != null && null != sku.getFkiObj().getActivityName()) {			//闪购信息
				activityObj.setActivityCode(sku.getFkiObj().getActivityCode());
				activityObj.setActivityName(sku.getFkiObj().getActivityName());
				activityObj.setActivityPrice(sku.getFkiObj().getVipPrice());
				activityObj.setStartTime(sku.getFkiObj().getStartTime());
				activityObj.setEndTime(sku.getFkiObj().getEndTime());
				activityObj.setRemark(sku.getFkiObj().getRemark());
				activityObj.setFlagCheap(1);								//是否闪购
				result.setFlagCheap(1);										//标注商品是否闪购
				result.setEndTime(sku.getFkiObj().getEndTime());			//闪购结束时间
				skuObj.getActivityInfo().add(activityObj);					//促销信息  闪购
				skuObj.setSellPrice(sku.getFkiObj().getVipPrice());
				
				//获取到最低价格
				if (sku.getFkiObj().getVipPrice().compareTo(result.getSellPrice()) < 0) {
					result.setSellPrice(sku.getFkiObj().getVipPrice());			//商品的价格改成家有价
				}
				//获取到最低价格
				if (sku.getFkiObj().getVipPrice().compareTo(product.getMinSellPrice()) < 0) {
					product.setMinSellPrice(sku.getFkiObj().getVipPrice());      //商品的最低价格改为活动价//售价显示最低
				}
			}
			//特价
			if (VersionHelper.checkServerVersion("3.5.72.55")) {
				//不参加闪购
				if (1 != result.getFlagCheap()) {
					String skuCode = sku.getSkuCode();
					if(icMap.containsKey(sku.getSkuCode())){
						skuCode=icMap.get(sku.getSkuCode());
					}
					PlusModelSkuInfo skuSuppore = support.upSkuInfoBySkuCode(skuCode,buyerCode);
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
				}
			}
			if (VersionHelper.checkServerVersion("3.5.51.51")) {
					skuObj.setDisMoney(skuObj.getSellPrice().multiply(skuScaleReckonMap.get(skuObj.getSkuCode())));
					//返现取最低
					if (result.getDisMoney() == null 
							|| result.getDisMoney().compareTo(BigDecimal.ZERO) <= 0
							|| result.getDisMoney().compareTo(skuObj.getDisMoney()) > 0) {
						result.setDisMoney(skuObj.getDisMoney());
					}
			}else{
				//sku返现金额
				skuObj.setDisMoney(pService.getDisMoney(skuObj.getSellPrice(),
						getFlagLogin() ? getOauthInfo().getUserCode() : null ,sellerCode));
				result.setDisMoney(pService.getDisMoney(product.getMinSellPrice(),
						getFlagLogin() ? getOauthInfo().getUserCode() : null ,sellerCode));
			}
			
			skuList.add(skuObj);
		}
		//特价
		if (VersionHelper.checkServerVersion("3.5.72.55")) {
			//不参加闪购
			if (1 != result.getFlagCheap()) {
				if (null != supportPrice) {
					if (result.getSellPrice().compareTo(supportPrice) > 0) {
						result.setSellPrice(supportPrice);
					}
					result.setPriceLabel(supportPriceNote);
				}
			}
			if (VersionHelper.checkServerVersion("3.5.92.55")) {
				if (null != supportPrice) {
					if (result.getSellPrice().compareTo(supportPrice) > 0) {
						result.setSellPrice(supportPrice);
					}
					result.setPriceLabel(supportPriceNote);
				}
			}
		}
		if (null == skuList || skuList.size() < 1) {
			/**
			 * 如果商品下不存在sku信息则添加一个价格为product价格的skuObj
			 * add by ligj
			 * time:2015-04-15 11:43:30
			 */
			skuList = new ArrayList<ProductSkuInfoForApi>();
			ProductSkuInfoForApi skuObj = new ProductSkuInfoForApi();
			skuObj.setSellPrice(result.getSellPrice());
			skuObj.setSkuName(product.getProdutName());
			skuObj.setSkuCode(product.getProductCode());
			skuObj.setMarketPrice(result.getMarketPrice());
			skuList.add(skuObj);
		}
		
		result.setSkuList(skuList);			//sku信息
		List<PicInfo> picList = new ArrayList<PicInfo>();
		for (PcProductpic pic : product.getPcPicList()) {
			picList.add(picUrlMap.get(pic.getPicUrl()));
		}
		result.setPcPicList(picList); // 轮播图
		
		if (null != product.getDescription()) {
			// 商品描述
			result.setDiscriptInfo(product.getDescription()
					.getDescriptionInfo());
			// 内容图片
			List<String> discriptList = new ArrayList<String>();
			String[] descriptArr = product.getDescription().getDescriptionPic()
					.split("\\|");
			if (null != descriptArr && descriptArr.length > 0) {
				discriptList = Arrays.asList(descriptArr);
			}
			//虚拟商品，并且虚拟商品描述图片不为空时添加到描述图片列表中,并且在有效期内
//			SimpleDateFormat sf = new SimpleDateFormat("MM-dd hh:mm:ss");
//			String nowTime = sf.format(new Date());
//			if (nowTime.compareTo(fictitionsPicStartTime) >= 0 
//					&&nowTime.compareTo(fictitionsPicEndTime) < 0
//					&&"Y".equals(product.getValidate_flag()) 
//					&& StringUtils.isNotEmpty(fictitiousProductPic)) {
//				result.getDiscriptPicList().add(picUrlMap.get(fictitiousProductPic));
//			}
			for (String descriptPic : discriptList) {
				result.getDiscriptPicList().add(picUrlMap.get(descriptPic)); // 内容图片
			}
			// 商品标签根据逗号分割
			List<String> labelsList = new ArrayList<String>();
			String[] labls = product.getDescription().getKeyword().split(",");
			if (labls != null) {
				for (int j = 0; j < labls.length; j++) {
					labelsList.add(labls[j]);
				}
			}
			result.setLabelsList(labelsList);
		}
		result.setDiscount(product.getMarketPrice().subtract(
				product.getMinSellPrice())); // 差价

		// 商品规格List
		List<MDataMap> sInfoMap = DbUp.upTable("pc_skuinfo").queryByWhere(
				"product_code", product.getProductCode(), "seller_code",
				sellerCode,"sale_yn","Y","flag_enable","1");
		MDataMap propertyMap = new MDataMap();

		for (MDataMap mDataMap : sInfoMap) {
			String proCodeStr = mDataMap.get("sku_key"); // 属性code
			String proValueStr = mDataMap.get("sku_keyvalue"); // 属性value

			if (null == proCodeStr || null == proValueStr
					|| "".equals(proCodeStr) || "".equals(proValueStr)) {
				continue;
			}
			// 获得不重复的key-value
			String[] propertiesCodeArr = proCodeStr.split("&");
			String[] propertiesValue = proValueStr.split("&");
			for (int i = 0; i < propertiesCodeArr.length; i++) {
				propertyMap.put(propertiesCodeArr[i], propertiesValue[i]);
			}
		}
		List<PropertyInfoForProtuct> propertyList = new ArrayList<PropertyInfoForProtuct>(); // keyObjList
		MDataMap proKeyMap = new MDataMap(); // keyMap
		String[] propertyMapKey = propertyMap.convertKeysToStrings();
		for (int i = 0; i < propertyMapKey.length; i++) { // 获得不重复的规格key
			String[] codesStr = propertyMapKey[i].split("=");
			String[] valueStr = propertyMap.get(propertyMapKey[i]).split("=");
			proKeyMap.put(codesStr[0], valueStr[0]); // key
		}
		for (String keyCode : proKeyMap.convertKeysToStrings()) {
			List<PropertyValueInfo> propertyValueList = new ArrayList<PropertyValueInfo>(); // valueObjList
			PropertyInfoForProtuct proCodeObj = new PropertyInfoForProtuct(); // keyObj
			for (int i = 0; i < propertyMapKey.length; i++) {
				PropertyValueInfo proValueObj = new PropertyValueInfo(); // valueObj
				String keyCodes = propertyMapKey[i];
				String[] codesStr = keyCodes.split("=");
				String[] valueStr = propertyMap.get(keyCodes).split("=");

				if (codesStr[0].equals(keyCode)) {
					proValueObj.setPropertyValueCode(codesStr[1]); // value
					proValueObj.setPropertyValueName(valueStr[1]);
					propertyValueList.add(proValueObj);
				}
			}
			// 商品规格属性值按照key进行字典排序
			if (null != propertyValueList && propertyValueList.size() > 1) {
				  Collections.sort(propertyValueList, new Comparator<PropertyValueInfo>() {
			            public int compare(PropertyValueInfo arg0, PropertyValueInfo arg1) {
			                return arg0.getPropertyValueName().compareTo(arg1.getPropertyValueName());
			            }
			        });
			}
			//商品详情接口商品规格排序按ASCII码升序排列
			//衣服尺码特别处理，按以下方式排序：XXS、XS、S、M、L、XL、XXL、XXXL、XXXXL、XXXXXL
			String[] specialProperties = new String[]{"XXS","XS","S","M"};						//需要进行特殊排序的衣服尺码
			String[] normalProperties = new String[]{"L","XL","XXL","XXXL","XXXXL","XXXXXL"};	//不需要进行特殊排序的衣服尺码
			boolean flagExistSpecial = false;		//标志是否含有需要特殊排序的衣服尺码
			boolean flagExistNormal = false;		//标志是否含有不需要特殊排序的衣服尺码
			List<PropertyValueInfo> specialPropertyInfoList = new ArrayList<PropertyValueInfo>();		//结果集中包含的需要进行特殊排序的衣服尺码数组
			List<Integer> specialIndexArr = new ArrayList<Integer>();									//结果集中包含的需要进行特殊排序的衣服尺码下标数组
			for (int j = 0; j < propertyValueList.size(); j++) {
				for (String specialProperty : specialProperties) {
					if (propertyValueList.get(j).getPropertyValueName().equals(specialProperty)) {
						flagExistSpecial = true;
						specialPropertyInfoList.add(propertyValueList.get(j));
						specialIndexArr.add(j);
					}
				}
			}
			if (flagExistSpecial) {
				int normalStrIndex = 0;													//含有的第一个不需要特殊排序的衣服尺码下标
				for (int j = 0; j < propertyValueList.size(); j++) {
					for (String normalProperty : normalProperties) {
						if (propertyValueList.get(j).getPropertyValueName().equals(normalProperty)) {
							flagExistNormal = true;
							normalStrIndex = j;
							break;
						}
					}
					if (flagExistNormal) break;
				}
				
				//开始进行排序,有个规律，需要进行特殊排序的衣服尺码都位于不需要进行特殊排序的衣服尺码前面，插入顺序为{M、S、XS、XXS}，插入的下标为normalStrIndex
				for (int i = specialProperties.length-1; i >= 0; i--) {
					for (int j = 0;j < specialPropertyInfoList.size();j++ ) {
						if (specialPropertyInfoList.get(j).getPropertyValueName().equals(specialProperties[i])) {
							propertyValueList.add(normalStrIndex, specialPropertyInfoList.get(j));
							propertyValueList.remove(specialIndexArr.get(j)+1);
						}
					}
				}
			}
			
			proCodeObj.setPropertyKeyCode(keyCode);
			proCodeObj.setPropertyKeyName(proKeyMap.get(keyCode));
			proCodeObj.setPropertyValueList(propertyValueList);
			propertyList.add(proCodeObj);
		}
		// 商品规格属性按照key进行字典排序
		if (null != propertyList && propertyList.size() > 1) {	
			  Collections.sort(propertyList, new Comparator<PropertyInfoForProtuct>() {
		            public int compare(PropertyInfoForProtuct arg0, PropertyInfoForProtuct arg1) {
		                return arg0.getPropertyKeyCode().compareTo(arg1.getPropertyKeyCode());
		            }
		        });
		}
		result.setPropertyList(propertyList); // 商品规格值
		
		// 自定义属性
		MDataMap mWhereMapProperty = new MDataMap();
		mWhereMapProperty.put("property_type", "449736200004");
		mWhereMapProperty.put("product_code", productCode);
		
		List<MDataMap> properties = DbUp.upTable("pc_productproperty")
				.queryAll("property_key,property_value", "property_type,small_sort desc,zid asc ", "", mWhereMapProperty);
		List<Propertyinfo> propertyInfoList = new ArrayList<Propertyinfo>();
//		if (AppConst.MANAGE_CODE_KJT.equals(product.getSmallSellerCode()) && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())) {
			Propertyinfo propertyProductCode = new Propertyinfo();
			propertyProductCode.setPropertykey("商品编码");
			propertyProductCode.setPropertyValue(product.getProductCode());
			propertyInfoList.add(propertyProductCode);
//		}
		for (MDataMap mDataMap : properties) {
			Propertyinfo property = new Propertyinfo();
			property.setPropertykey(mDataMap.get("property_key"));
			property.setPropertyValue(mDataMap.get("property_value"));
			if ("内联赠品".equals(property.getPropertykey())) {
				continue;
			}
			propertyInfoList.add(property);
		}
		result.setPropertyInfoList(propertyInfoList);

		if(VersionHelper.checkServerVersion("3.5.021.507")&&(getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS))){//后台逻辑代码版本控制
			//赠品信息
			DismantlOrderUtil dis = new DismantlOrderUtil();
			Map<String,String> giftMap = dis.getProductGifts(product.getProductCode(),api.getChannelId());
			String gift = giftMap.get(product.getProductCode());
			if (StringUtils.isNotEmpty(gift)) {
				result.setFlagIncludeGift(1);
				result.setGift(gift);
			}
		}
		//权威标志
		List<PcAuthorityLogoModel> authorityLogo = new ArrayList<PcAuthorityLogoModel>();
		//List<PcAuthorityLogo> authorityLogoList = pService.getAuthorityLogo(sellerCode);
		PlusModelAuthorityLogos logos = loadSellerAuthorityLogo.upInfoByCode(new PlusModelProductAuthorityLogoQuery(sellerCode));
		String productType = "";
		if (AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSmallSellerCode()) && 
				(AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())||AppConst.MANAGE_CODE_CDOG.equals(product.getSellerCode()))) {
			productType = "4497471600150001";		//LD商品
		}else if ((AppConst.MANAGE_CODE_KJT.equals(product.getSmallSellerCode())||AppConst.MANAGE_CODE_MLG.equals(product.getSmallSellerCode())
				||AppConst.MANAGE_CODE_QQT.equals(product.getSmallSellerCode())
				||AppConst.MANAGE_CODE_SYC.equals(product.getSmallSellerCode())
				||AppConst.MANAGE_CODE_CYGJ.equals(product.getSmallSellerCode()))&& 
				(AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())||AppConst.MANAGE_CODE_CDOG.equals(product.getSellerCode()))) {
			productType = "4497471600150003";		//跨境通商品
//		}else if (product.getSmallSellerCode().startsWith("SF031") && 
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		}else if (StringUtils.isNotBlank(WebHelper.getSellerType(product.getSmallSellerCode())) &&
				(AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())||AppConst.MANAGE_CODE_CDOG.equals(product.getSellerCode()))) {
			productType = "4497471600150002";		//商户商品
		}else if((AppConst.MANAGE_CODE_CDOG.equals(product.getSmallSellerCode()))&& 
				(AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())||AppConst.MANAGE_CODE_CDOG.equals(product.getSellerCode()))){
			productType = "4497471600150004";//沙皮狗商品
		}
		if(logos != null){
			for (PlusModelAuthorityLogo modelLogo : logos.getAuthorityLogos()) {
				PcAuthorityLogoModel model = new PcAuthorityLogoModel();
				model.setLogoContent(modelLogo.getLogoContent());
				model.setLogoPic(modelLogo.getLogoPic());
				model.setLogoLocation(modelLogo.getLogoLocation());
				if ("449747110001".equals(modelLogo.getAllFlag())) {		//是否全场为否时，判断商品类型
					if (StringUtils.isNotEmpty(modelLogo.getShowProductSource())) {
						for (String channelCode : modelLogo.getShowProductSource().split(",")) {
							if (productType.equals(channelCode)) {
								authorityLogo.add(model);
								break;
							}
						}
					}
				}else {
					authorityLogo.add(model);
				}
			}
		}

		result.setAuthorityLogo(authorityLogo);
		//统计销量
		List<String> productCodesArr = new ArrayList<String>();
		productCodesArr.add(product.getProductCode());
		MDataMap salesMap = pService.getProductFictitiousSales("",productCodesArr,30);
		if (null != salesMap && !salesMap.isEmpty()) {
			if (StringUtils.isNotEmpty(salesMap.get(product.getProductCode()))) {
				result.setSaleNum(salesMap.get(product.getProductCode()));
			}
		}
		if (VersionHelper.checkServerVersion("3.5.51.53")) {
			result.setCollectionProduct(pService.getIsCollectionProduct(product.getProductCode(), getFlagLogin() ? getOauthInfo().getUserCode() : null));
		}
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
			
			result.setDiscount(product.getMarketPrice().subtract(
					productActiveInfo.getActivity_price())); // 差价
			result.setDisMoney(BigDecimal.ZERO);			//内购不参加返现
		}
		//价格标签(优先级  闪购  > 特价 > 内购)
		 if (1 == result.getFlagCheap()) {
			result.setPriceLabel(bConfig("familyhas.priceLabelFlash"));
		}else if (1 == result.getVipSpecialActivity()) {
			result.setPriceLabel(bConfig("familyhas.priceLabelVipSpecial"));
		}
		Map<String,Integer> btnMap = new HashMap<String, Integer>();
		//参加内购或闪购，只显示打电话与立即购买按钮
		if (1 == result.getFlagCheap()) {
			btnMap.put("callBtn", 1);
			btnMap.put("shopCarBtn", 0);
			btnMap.put("buyBtn", 1);
		}else if (1 == result.getVipSecKill()) {		//参加促销活动
			btnMap.put("callBtn", 1);
			btnMap.put("shopCarBtn", 1);
			btnMap.put("buyBtn", 1);
		}else if (flagVipSpecial) {
			btnMap.put("callBtn", 1);
			btnMap.put("shopCarBtn", 0);
			btnMap.put("buyBtn", 1);
		}else{
			btnMap.put("callBtn", 1);
			btnMap.put("shopCarBtn", 1);
			btnMap.put("buyBtn", 1);
		}
		
		//跨境通商品只能立即购买和加入购物车
		if (bConfig("familyhas.seller_code_KJT").equals(product.getSmallSellerCode())) {
			btnMap.put("callBtn", 0);
//					btnMap.put("shopCarBtn", 1);
//					btnMap.put("buyBtn", 1);
//		}else if (product.getSmallSellerCode().startsWith("SF03")) {
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		}else if (StringUtils.isNotBlank(WebHelper.getSellerType(product.getSmallSellerCode()))) {
			//第三方商户的商品屏蔽打电话按钮
			btnMap.put("callBtn", 0);
		}
				
		//只有惠家有的Ld商品的非虚拟商品才能有电话订购按钮见BUG #9911
		if (!(AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSmallSellerCode()) && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode()) && "N".equals(product.getValidate_flag()))) {
			btnMap.put("callBtn", 0);
		}
				
		result.setButtonMap(btnMap);
		BigDecimal minSellPriceSku = null;
		BigDecimal maxSellPriceSku = null;
		for (ProductSkuInfoForApi skuInfo : result.getSkuList()) {
			if (null == minSellPriceSku || minSellPriceSku.compareTo(skuInfo.getSellPrice()) > 0) {
				minSellPriceSku = skuInfo.getSellPrice();
			}
			if (null == maxSellPriceSku || maxSellPriceSku.compareTo(skuInfo.getSellPrice()) < 0) {
				maxSellPriceSku = skuInfo.getSellPrice();
			}
		}
		
		result.setMinSellPrice(result.getSellPrice()+"");
		result.setMaxSellPrice(result.getSellPrice()+"");
		if (StringUtils.isNotEmpty(buyerCode)) {
			List<String> productCodes = new ArrayList<String>();
			productCodes.add(productCode);
			new ApiAddBrowseHistory().addBrowseHistory(productCodes, buyerCode);
		}
		return result;
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
	/**
	 * 根据商家编号获取常见问题，暂时只有跨境通商品存在常见问题2015-09-17 11:22:00
	 * @return
	 */
	public List<CommonProblem> getCommonProblemForProductCode(String sellerCode,String smallSellerCode){
		List<CommonProblem> commonProblemList = new ArrayList<CommonProblem>();
		if (StringUtils.isBlank(sellerCode) || StringUtils.isBlank(smallSellerCode)) {
			return commonProblemList;
		}
//		List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort asc,update_time desc", "seller_code='"+sellerCode+"'and small_seller_code='"+smallSellerCode+"'", null);
		List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort asc,update_time desc", "seller_code='"+sellerCode+"'", null);
		if (null != commonProblemMapList && !commonProblemMapList.isEmpty()) {
			for (MDataMap commonProblemMap : commonProblemMapList) {
				CommonProblem commonProblem = new CommonProblem();
//				commonProblem.setProductCode(commonProblemMap.get("product_code"));
				commonProblem.setTitle(commonProblemMap.get("title"));
				commonProblem.setContent(commonProblemMap.get("content"));
//				commonProblem.setSort(commonProblemMap.get("sort"));
//				commonProblem.setCreateTime(commonProblemMap.get("create_time"));
//				commonProblem.setSellerCode(commonProblemMap.get("seller_code"));
				commonProblemList.add(commonProblem);
			}
		}
		return commonProblemList;
	}
}
