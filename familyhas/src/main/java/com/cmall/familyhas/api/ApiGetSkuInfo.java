package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.dborm.txmodel.PcAuthorityLogo;
import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.model.ActivitySell;
import com.cmall.familyhas.api.model.PcAuthorityLogoModel;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.PropertyInfoForProtuct;
import com.cmall.familyhas.api.model.PropertyValueInfo;
import com.cmall.familyhas.api.model.Propertyinfo;
import com.cmall.familyhas.api.result.ApiGetSkuInfoResult;
import com.cmall.familyhas.model.CommonProblem;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.groupcenter.baidupush.core.utility.DismantlOrderUtil;
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
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 商品详情
 * 
 * @author 李国杰 
 */
public class ApiGetSkuInfo extends
		RootApiForMember<ApiGetSkuInfoResult, ApiGetSkuInfoInput> {

	public ApiGetSkuInfoResult Process(ApiGetSkuInfoInput api,
			MDataMap mRequestMap) {
		if(StringUtils.isBlank(api.getBuyerType()))
		{
			api.setBuyerType("4497469400050002");
		}
		ApiGetSkuInfoResult result = new ApiGetSkuInfoResult();
		ProductService pService = new ProductService();
		String productCode = pService.getProductCodeForICcode(api.getProductCode());
		String sellerCode = bConfig("familyhas.app_code"); // 获取appCode
		String familyPriceName = bConfig("familyhas.family_price_name");
		String maxBuyCount = bConfig("familyhas.pt_product_num");			//最大购买数
//		String fictitiousProductPic = bConfig("familyhas.fictitions_pic");	//虚拟商品增加的描述图片
//		String fictitionsPicStartTime = bConfig("familyhas.fictitions_pic_start_time");//虚拟商品增加的描述图片开始时间
//		String fictitionsPicEndTime = bConfig("familyhas.fictitions_pic_end_time");//虚拟商品增加的描述图片结束时间
		String shareUrl = bConfig("familyhas.shareUrl");		//分享链接
		String vipSpecialTip = bConfig("familyhas.vipSpecialTip");	//内购用户提示信息
//		String userType = api.getBuyerType();
		//分销模板进入则的进行分销价的详情展示
		String fxFlag = api.getFxFlag();
		RootResult rootResult = new RootResult();
		//校验分销活动是否有效
		validateFXEvent(productCode,fxFlag,rootResult);
		if(1==rootResult.getResultCode()) {fxFlag="1";}
		else {fxFlag="0";}
		PcProductinfoForFamily product = new PcProductinfoForFamily();
		Integer width = api.getPicWidth();
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
			if ("0".equals(fxFlag)&&sku.getFkiObj() != null && null != sku.getFkiObj().getActivityName()) {			//闪购信息
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
				if (1 != result.getFlagCheap()&&"0".equals(fxFlag)) {
					PlusModelSkuInfo skuSuppore = support.upSkuInfoBySkuCode(sku.getSkuCode(),buyerCode);
					if (StringUtils.isNotEmpty(skuSuppore.getEventCode())) {
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
						skuObj.setShowLimitNum(skuSuppore.getShowLimitNum());		//是否显示限购数
						result.setVipSecKill(1);		//商品设置为参加促销活动
					
						skuObj.setStockNumSum((int)skuSuppore.getLimitStock());
					}
				}
			}
			if (VersionHelper.checkServerVersion("3.5.51.51")) {
					skuObj.setDisMoney(skuObj.getSellPrice().multiply(skuScaleReckonMap.get(skuObj.getSkuCode())));
					result.setDisMoney(skuObj.getSellPrice().multiply(skuScaleReckonMap.get(skuObj.getSkuCode())));
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
			if (1 != result.getFlagCheap()&&"0".equals(fxFlag)) {
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
		List<String> otherShow = new ArrayList<String>();				//其他相关显示语
		if(VersionHelper.checkServerVersion("3.5.021.507")&&getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS)){//后台逻辑代码版本控制
			//赠品信息
			DismantlOrderUtil dis = new DismantlOrderUtil();
			Map<String,String> giftMap = dis.getProductGifts(product.getProductCode(),api.getChannelId());
			String gift = giftMap.get(product.getProductCode());
			if (StringUtils.isNotEmpty(gift)) {
				result.setFlagIncludeGift(1);
				result.setGift(gift);
				otherShow.add(bConfig("familyhas.ActivityZpName"));
			}
			result.setOtherShow(otherShow);
			//权威标志
			List<PcAuthorityLogoModel> authorityLogo = new ArrayList<PcAuthorityLogoModel>();
			List<PcAuthorityLogo> authorityLogoList = pService.getAuthorityLogo(sellerCode);
			String productType = "";
			if (AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSmallSellerCode()) && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())) {
				productType = "4497471600150001";		//LD商品
			}else if ((AppConst.MANAGE_CODE_KJT.equals(product.getSmallSellerCode())||AppConst.MANAGE_CODE_MLG.equals(product.getSmallSellerCode())||AppConst.MANAGE_CODE_QQT.equals(product.getSmallSellerCode())||AppConst.MANAGE_CODE_SYC.equals(product.getSmallSellerCode())
					||AppConst.MANAGE_CODE_CYGJ.equals(product.getSmallSellerCode())) && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())) {
				productType = "4497471600150003";		//跨境通商品
//			}else if (product.getSmallSellerCode().startsWith("SF03") && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())) {
			/**
			 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
			 */
			}else if (StringUtils.isNotBlank(WebHelper.getSellerType(product.getSmallSellerCode())) && AppConst.MANAGE_CODE_HOMEHAS.equals(product.getSellerCode())) {
				productType = "4497471600150002";		//商户商品
			}
			for (PcAuthorityLogo modelLogo : authorityLogoList) {
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
			result.setAuthorityLogo(authorityLogo);
		}
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
		//不参加闪购与不参加特价时才去判断内购价格
		if (VersionHelper.checkServerVersion("3.5.62.55")) {
			if (1 != result.getFlagCheap() && 1 != result.getVipSecKill()&&"0".equals(fxFlag)) {
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
//			btnMap.put("shopCarBtn", 1);
//			btnMap.put("buyBtn", 1);
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
		if("1".equals(fxFlag)) {
			String coupon_money = rootResult.getResultMessage();
		    BigDecimal subtract = result.getSellPrice().subtract(new BigDecimal(coupon_money));
		    result.setSellPrice(subtract);
		    result.setShowFXPrice("1");
		}
		result.setMinSellPrice(minSellPriceSku+"");
		result.setMaxSellPrice(maxSellPriceSku+"");
		if (StringUtils.isNotEmpty(buyerCode)) {
			List<String> productCodes = new ArrayList<String>();
			productCodes.add(productCode);
			new ApiAddBrowseHistory().addBrowseHistory(productCodes, buyerCode);
		}
		return result;
	}
	private void validateFXEvent(String productCode, String fxFlag, RootResult rootResult) {
		// TODO Auto-generated method stub
		rootResult.setResultCode(0);
		if("1".equals(fxFlag)) {
			List<Map<String, Object>> listMap= DbUp.upTable("oc_activity").dataSqlList("select * from oc_activity where activity_type='449715400008' and flag=1 and begin_time<=now() and end_time>now() order by zid desc", null);
		    if(listMap!=null&&listMap.size()>0) {
		    	Map<String, Object> map = listMap.get(0);
		    	 Map<String, Object> dataSqlOne = DbUp.upTable("oc_activity_agent_product").dataSqlOne("select * from oc_activity_agent_product where activity_code=:activity_code and produt_code=:produt_code and flag_enable=1 ",new MDataMap("activity_code",map.get("activity_code").toString(),"produt_code",productCode));
			     if(dataSqlOne!=null) {
			    	 String coupon_money = dataSqlOne.get("coupon_money").toString();
			    	 rootResult.setResultCode(1);
			    	 rootResult.setResultMessage(coupon_money);
			     }
		    }
		}
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
		List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort desc,update_time desc", "seller_code='"+sellerCode+"'and small_seller_code='"+smallSellerCode+"'", null);
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
