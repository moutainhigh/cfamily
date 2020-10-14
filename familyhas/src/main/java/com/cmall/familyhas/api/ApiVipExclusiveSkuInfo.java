package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.CityInfo;
import com.cmall.familyhas.api.model.ProductSkuInfoForApi;
import com.cmall.familyhas.api.model.ProvinceInfo;
import com.cmall.familyhas.api.result.ApiGetSkuInfoNewResult;
import com.cmall.familyhas.util.ProCityLoader;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
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
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadTemplateAreaCode;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;
import com.srnpr.xmassystem.modelproduct.PlusModelPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusiveSkuInfo extends RootApiForToken<ApiGetSkuInfoNewResult, ApiGetSkuInfoInput> {
	
	private static String highPraise = "好评";
	private static String headUrlAccept = "449746600001";
	//审核通过
	private static String hasAccept = "4497172100030002";
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("deprecation")
	@Override
	public ApiGetSkuInfoNewResult Process(ApiGetSkuInfoInput api, MDataMap mRequestMap) {
		if(StringUtils.isBlank(api.getBuyerType()))
		{
			api.setBuyerType("4497469400050002");
		}
		
		//String eventProductCode = api.getProductCode();			//IC开头的商品编号
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
		String productCode = api.getProductCode();
		String sellerCode = getManageCode();
		String maxBuyCount = bConfig("familyhas.pt_product_num");			//最大购买数
		String vipSpecialTip = bConfig("familyhas.vipSpecialTip");	//内购用户提示信息
		
		String tuwenUrl = FormatHelper.formatString(bConfig("familyhas.tuwenUrl"),productCode);	//图文详情的页面链接
		String guigeUrl = FormatHelper.formatString(bConfig("familyhas.guigeUrl"),productCode);	//规格参数的页面链接
		String FQAUrl = FormatHelper.formatString(bConfig("familyhas.FQAUrl"),productCode);		//常见问题的页面链接
		
		Integer width = api.getPicWidth();
		// 好评数和总评论数
		String sSql = "SELECT SUM(1) total, SUM(IF(grade_type = '好评',1,0)) highPraiseSize FROM	newscenter.nc_order_evaluation WHERE check_flag = '4497172100030002' AND product_code = :product_code";
		Map<String, Object> totalMap = DbUp.upTable("nc_order_evaluation").dataSqlOne(sSql, new MDataMap("product_code", productCode));
		if(totalMap != null && totalMap.get("total") != null) {
			result.setCommentSumCounts(new BigDecimal(totalMap.get("total")+"").intValue());
			// 好评率  = 好评数 / 总数 * 100  然后取整
			result.setHighPraiseRate(new BigDecimal(totalMap.get("highPraiseSize")+"").divide(new BigDecimal(totalMap.get("total")+""),0, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue()+"");
		}
		// 查询最近的一条好评，用户好评的排前面
		sSql = "SELECT * FROM newscenter.nc_order_evaluation WHERE check_flag = '4497172100030002' and grade_type = '好评' AND product_code = :product_code ORDER BY auto_good_evaluation_flag, oder_creattime desc LIMIT 2";
		List<Map<String, Object>> commentList = DbUp.upTable("nc_order_evaluation").dataSqlList(sSql, new MDataMap("product_code", productCode));
		for(Map<String,Object> map : commentList){
			CdogProductComment comment = new CdogProductComment();
			MDataMap mDataMap = new MDataMap(map);
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
		
		//获取商户信息
		Map<String, Object> sellProrMap = DbUp.upTable("pc_productinfo").dataSqlOne("select p.product_name, p.seller_code, p.small_seller_code, p.mainpic_url, p.product_status, p.video_url, i.seller_company_name, "
				+ "p.brand_code, (select b.brand_name from pc_brandinfo b where b.brand_code = p.brand_code) brand_name, p.labels, e.uc_seller_type, p.product_adv, p.area_template "
				+ "from pc_productinfo p left join usercenter.uc_sellerinfo i on p.small_seller_code = i.small_seller_code "
				+ "left join usercenter.uc_seller_info_extend e on i.small_seller_code = e.small_seller_code "
				+ "where p.product_code = '" + productCode + "'", new MDataMap());
		result.setSmallSellerCode(MapUtils.getString(sellProrMap, "small_seller_code"));
		result.setSellerCompanyName(MapUtils.getString(sellProrMap, "seller_company_name"));
		
		//主图
		Map<String,PicInfo> picUrlMap = new HashMap<String, PicInfo>();
		picUrlMap.put(MapUtils.getString(sellProrMap, "mainpic_url", ""), new PicInfo());

		//轮播图
		List<Map<String, Object>> picList = DbUp.upTable("pc_productpic").dataSqlList("select p.pic_url from pc_productpic p where p.product_code = '" + productCode + "'", new MDataMap());
		for(Map<String, Object> pic : picList) {
			picUrlMap.put(MapUtils.getString(pic, "pic_url", ""), new PicInfo()); 
		}
		
		//描述图片
		List<Map<String, Object>> desList = DbUp.upTable("pc_productdescription").dataSqlList("select p.description_pic from pc_productdescription p where p.product_code = '" + productCode + "'", new MDataMap());
		for(Map<String, Object> des : desList) {
			String[] descriptionPicArr = MapUtils.getString(des, "description_pic", "").split("\\|");
			for(String descriptionPic : descriptionPicArr) {
				picUrlMap.put(descriptionPic, new PicInfo());
			}
		}
		
		//压缩图片
		PlusModelProductLabel pmpl = new ProductLabelService().getLabelInfo(productCode);
		picUrlMap.put(pmpl.getInfoActivityPic(), new PicInfo());
		List<String> picUrlArr = new ArrayList<String>();
		for (String picUrl: picUrlMap.keySet()) {
			picUrlArr.add(picUrl);
		}
		List<PicInfo> picInfoList = pService.getPicInfoForMulti(width,picUrlArr);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo);
		}
		
		String productName = MapUtils.getString(sellProrMap, "product_name", "");
		productName = productName.replaceAll("</?[^>]+>", "");
		result.setProductCode(productCode); // 商品编号
		result.setProductName(productName); // 商品名称
		result.setProductStatus(MapUtils.getString(sellProrMap, "product_status", ""));//商品上下架状态
		//商品的录播视频
		result.setVideoUrl(MapUtils.getString(sellProrMap, "video_url", "")); // 视频地址
		result.setExitVideo(StringUtils.isEmpty(MapUtils.getString(sellProrMap, "video_url", "")) ? 0 : 1);//是否有视频
		//当前商品如果是直播商品则返回直播链接.added by zht.20170619 
		if(isLiveStreaming(api.getProductCode())) {
			result.setExitVideo(1);
			result.setLiveUrl(bConfig("familyhas.video_url_TV"));
		}
		result.setBrandCode(MapUtils.getString(sellProrMap, "brand_code", "")); // 品牌编号
		result.setBrandName(MapUtils.getString(sellProrMap, "brand_name", "")); // 品牌名称
		result.setMainpicUrl(picUrlMap.get(MapUtils.getString(sellProrMap, "mainpic_url", "")));		//主图
		result.setMaxBuyCount(Integer.valueOf(maxBuyCount)); // 商品的最大购买数，暂时默认为99
		result.setCommonProblem(plService.getProductCommonProblem(MapUtils.getString(sellProrMap, "seller_code", ""), MapUtils.getString(sellProrMap, "small_seller_code", "")));
		String isSea = "0";
		String ucSellerType = MapUtils.getString(sellProrMap, "uc_seller_type", "");
		if("4497478100050002".equals(ucSellerType) || "4497478100050003".equals(ucSellerType)) {
			isSea = "1";
		}
		result.setFlagTheSea(isSea);//是否海外购
		result.setLabelsPic(pmpl.getInfoPic());
		//获取商品标签
		List<String> labelList = new ArrayList<String>();
		String labels = MapUtils.getString(sellProrMap, "labels", "");
		for(String label : labels.split(",")) {
			labelList.add(label);
		}
		
		//积分商城不显示活动，标签
//		result.setLabelsList(labelList);//商品标签
		result.setEventLabelPic( picUrlMap.get(pmpl.getInfoActivityPic()) );                     // 3.9.6新增 - Yangcl
//		result.setEventLabelPicSkip(pmpl.getEventLabelPicSkip());     // 3.9.6新增 - Yangcl 
		//获取品牌类型
		List<String> categoryStrs = new ArrayList<String>();
		List<Map<String, Object>> categoryList = DbUp.upTable("uc_sellercategory_product_relation").dataSqlList("select c.category_name from uc_sellercategory_product_relation r, uc_sellercategory c "
				+ "where r.product_code = '" + productCode + "' and r.seller_code = '" + MapUtils.getString(sellProrMap, "seller_code", "")
				+ "' and c.category_code = r.category_code and c.seller_code = r.seller_code", new MDataMap());
		for(Map<String, Object> category : categoryList) {
			categoryStrs.add(MapUtils.getString(category, "category_name", ""));
		}
		result.setCategoryList(new CategoryService().getCategoryBaseInfoList(categoryStrs, MapUtils.getString(sellProrMap, "seller_code", "")));
		result.setSellingPoint(MapUtils.getString(sellProrMap, "product_adv", ""));//商品广告语（卖点信息）
		result.setTuwenUrl(tuwenUrl);	//图文详情的页面链接
		result.setGuigeUrl(guigeUrl);	//规格参数的页面链接
		result.setFQAUrl(FQAUrl);		//常见问题的页面链接
		
		Map<String, ActiveReturn> activeInfoMap = new HashMap<String, ActiveReturn>();   //sku参加的活动
		ActiveReturn productActiveInfo = new ActiveReturn();	//此商品参加的活动
		String buyerCode = getFlagLogin() ? getOauthInfo().getUserCode() : null;
		
		List<ProductSkuInfoForApi> skuList = new ArrayList<ProductSkuInfoForApi>();//存储sku信息
		List<MDataMap> skuMapList = new ArrayList<MDataMap>();//记录sku规格信息
		List<String> skuCodeArr = new ArrayList<String>();
		List<Map<String, Object>> skuInfoList = DbUp.upTable("pc_skuinfo").dataSqlList("select i.sku_code, i.sku_name, i.market_price, i.sell_price, i.mini_order, i.sku_key, i.sku_keyvalue, d.extra_charges, "
				+ "(d.jf_cost * 200) jf_cost, d.one_allowed, d.allow_count, "
				+ "(case when d.start_time > sysdate() then '0' else '1' end) is_start, "
				+ "(case when d.end_time < sysdate() then '1' else '0' end) is_end "
				+ "from pc_skuinfo i left join familyhas.fh_apphome_channel_details d on i.product_code = d.product_code where i.product_code = '" + productCode + "' and d.uid = '" + api.getIntegralDetailId() + "'", new MDataMap());
		for(Map<String, Object> skuInfo : skuInfoList) {
			skuCodeArr.add(MapUtils.getString(skuInfo, "sku_code", ""));
			
			ProductSkuInfoForApi skuObj = new ProductSkuInfoForApi();
			skuObj.setSkuCode(MapUtils.getString(skuInfo, "sku_code", ""));
			skuObj.setSkuName(MapUtils.getString(skuInfo, "sku_name", ""));
			
			String jf_cost = MapUtils.getString(skuInfo, "jf_cost", "");
			String extra_charges = MapUtils.getString(skuInfo, "extra_charges", "");
			if(!"".equals(jf_cost)) {
				skuObj.setSellPrice(new BigDecimal("".equals(extra_charges) ? "0" : extra_charges));
				skuObj.setIntegral(new BigDecimal(jf_cost));
			}else {
				skuObj.setSellPrice(new BigDecimal(MapUtils.getString(skuInfo, "sell_price", "")));
				skuObj.setIntegral(new BigDecimal(0));
			}
			
			skuObj.setMarketPrice(new BigDecimal(MapUtils.getString(skuInfo, "market_price", "")));
			skuObj.setKeyValue(MapUtils.getString(skuInfo, "sku_key", ""));
			skuObj.setMiniOrder(MapUtils.getIntValue(skuInfo, "mini_order", 0));//起订数量
			skuObj.setDisMoney(new BigDecimal(0));
			
			int stockNumSum = MapUtils.getIntValue(skuInfo, "allow_count", 0);
			skuObj.setStockNumSum(stockNumSum);
			
			
			int limitBuy = 0;
			int maxLimit = 0;
			int one_allowed = MapUtils.getIntValue(skuInfo, "one_allowed", 0);
			if(one_allowed == 0) {
				limitBuy = 99;
				maxLimit = 99;
			}else if(one_allowed != 0) {
				int buyedCount = DbUp.upTable("oc_orderinfo").upTemplate().queryForInt("select ifnull(sum(d.sku_num), 0) from oc_orderinfo i, oc_orderdetail d, oc_order_activity a where i.order_code = d.order_code "
						+ "and a.order_code = d.order_code and a.product_code = d.product_code and a.sku_code = d.sku_code and i.buyer_code = '" + getUserCode() + "' and d.product_code = '" + productCode + "' "
						+ "and a.activity_code = '" + api.getIntegralDetailId() + "' and i.order_status != '4497153900010006'", 
						new HashMap<String, Object>());
				if(one_allowed > buyedCount) {
					limitBuy = one_allowed - buyedCount;
					
				}
				maxLimit = one_allowed;
			}
			skuObj.setMaxLimit(maxLimit);
			skuObj.setLimitBuy(limitBuy);//每个用户限购数
			
			if("0".equals(MapUtils.getString(skuInfo, "is_start", ""))) {//未开始
				skuObj.setBuyStatus("2");
			}else if("1".equals(MapUtils.getString(skuInfo, "is_end", ""))) {//已结束
				skuObj.setBuyStatus("3");
//			}else if(limitBuy == 0 || stockNumSum <= 0) {
			}else if(stockNumSum <= 0) {
				skuObj.setBuyStatus("4");
			}else {
				skuObj.setBuyStatus("1");
			}
			
			//返现取最低
			if(result.getDisMoney() == null || result.getDisMoney().compareTo(BigDecimal.ZERO) <= 0 || result.getDisMoney().compareTo(skuObj.getDisMoney()) > 0) {
				result.setDisMoney(skuObj.getDisMoney());
			}
			skuList.add(skuObj);
			
			MDataMap skuMap = new MDataMap();
			skuMap.put("sku_key", MapUtils.getString(skuInfo, "sku_key", ""));
			skuMap.put("sku_keyvalue", MapUtils.getString(skuInfo, "sku_keyvalue", ""));
			skuMapList.add(skuMap);
		} 
		result.setSkuList(skuList);	//sku信息
		if(skuList.size() > 0) {
			result.setBuyStatus(skuList.get(0).getBuyStatus());
		}
		
		List<PicInfo> picInfoist = new ArrayList<PicInfo>();
		for(Map<String, Object> pic : picList) {
			picInfoist.add(picUrlMap.get(MapUtils.getString(pic, "pic_url", "")));
		}
		result.setPcPicList(picInfoist); // 轮播图
		
		if(desList.size() > 0) {
			List<String> discriptList = new ArrayList<String>();
			for(Map<String, Object> des : desList) {
				String[] descriptionPicArr = MapUtils.getString(des, "description_pic", "").split("\\|");
				for(String pic : descriptionPicArr) {
					discriptList.add(pic);
				}
			}
			for(String descriptPic : discriptList) {
				result.getDiscriptPicList().add(picUrlMap.get(descriptPic));//内容图片
			}
			if(result.getDiscriptPicList().size() != 0){
				PicInfo p = result.getDiscriptPicList().get(0);
				PicInfo e = new PicInfo();
				e.setHeight(p.getHeight());
				e.setOldHeight(p.getOldHeight());
				e.setOldWidth(p.getOldWidth());
				e.setWidth(p.getWidth());  
				e.setPicNewUrl(this.bConfig("familyhas.price_url_pic"));
				e.setPicOldUrl(this.bConfig("familyhas.price_url_pic")); 
				result.getDiscriptPicList().add(e);
			}
		}
		
		PlusSupportProduct product = new PlusSupportProduct();
		result.setPropertyList(product.propertyListSku(skuMapList)); // 商品规格值
		
		//自定义属性
		List<PlusModelPropertyInfo> propertyInfoList = new ArrayList<PlusModelPropertyInfo>();//商品的自定义属性
		MDataMap mWhereMapProperty = new MDataMap();
		mWhereMapProperty.put("property_type", "449736200004");
		mWhereMapProperty.put("product_code", productCode);
		List<MDataMap> properties = DbUp.upTable("pc_productproperty").queryAll("property_key,property_value,start_date,end_date", "property_type,small_sort desc,zid asc ", "", mWhereMapProperty);
		PlusModelPropertyInfo propertyProductCode = new PlusModelPropertyInfo();
		propertyProductCode.setPropertykey("商品编码");
		propertyProductCode.setPropertyValue(productCode);
		propertyInfoList.add(propertyProductCode);
		for(MDataMap mDataMap : properties) {
			if(!StringUtils.contains(mDataMap.get("property_key"), "内联赠品")){ 
				PlusModelPropertyInfo property = new PlusModelPropertyInfo();
				property.setPropertykey(mDataMap.get("property_key"));
				property.setPropertyValue(mDataMap.get("property_value"));
				propertyInfoList.add(property);
			}
		}
		result.setPropertyInfoList(propertyInfoList);
		
		//积分兑换，不赠送赠品
		/*if(getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS)){
				//赠品信息
				DismantlOrderUtil dis = new DismantlOrderUtil();
				Map<String,String> giftMap = dis.getProductGifts(eventProductCode,api.getChannelId());
				String gift = giftMap.get(productCode);
				if(StringUtils.isNotEmpty(gift)) {
					result.setFlagIncludeGift(1);
					result.setGift(gift);
				}
		}*/
		
		result.setAuthorityLogo(plService.getProductAuthorityLogo(MapUtils.getString(sellProrMap, "seller_code", ""), MapUtils.getString(sellProrMap, "small_seller_code", ""), productCode));
		
		
		PlusModelProductSales productSales = new PlusSupportProduct().getProductSales(productCode);
		result.setSaleNum(productSales.getFictitionSales30() + "");
		result.setCollectionProduct(pService.getIsCollectionProduct(productCode, getFlagLogin() ? getOauthInfo().getUserCode() : null));
		
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
			AddressInformation addressInfomation = new AddressService().getAddressOne("",buyerCode, MapUtils.getString(sellProrMap, "seller_code", ""));
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
		if(!"".equals(MapUtils.getString(sellProrMap, "area_template", ""))){
			PlusModelTemplateAreaQuery taq = new PlusModelTemplateAreaQuery();
			taq.setCode(MapUtils.getString(sellProrMap, "area_template", ""));
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
		smallSellers.add(MapUtils.getString(sellProrMap, "small_seller_code", ""));
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
	
	private boolean isLiveStreaming(String productCode) {
		String now = dateFormater.format(new Date());
		String swhere = "form_fr_date<='"+now+"' and form_end_date>='"+now+"' and good_id='" + productCode + "' and so_id='1000001'";
		List<MDataMap> liveList = DbUp.upTable("pc_tv").queryAll("", "form_fr_date", swhere, new MDataMap());
		return liveList != null && liveList.size() > 0 ? true : false;
	}
}
