package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import com.cmall.familyhas.api.input.ApiGetBrowesHistoryInput;
import com.cmall.familyhas.api.model.GetBrowseHistory;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiGetBrowesHistoryResult;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.productcenter.model.Item;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.service.ProductStoreService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MFileItem;

/**
 * 获取浏览历史记录
 * @author liqt
 *
 */
public class ApiGetBrowesHistory extends RootApiForToken<ApiGetBrowesHistoryResult, ApiGetBrowesHistoryInput>{
	
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	static PlusVeryImage plusVeryImage = new PlusVeryImage();
	
	public ApiGetBrowesHistoryResult Process(ApiGetBrowesHistoryInput input, MDataMap mDataMap){
		ApiGetBrowesHistoryResult result = new ApiGetBrowesHistoryResult();
		
		//获取用户编号
		String memberCode = getUserCode();
		String channelId = getChannelId();
		int count = Integer.parseInt(bConfig("familyhas.page_everypage"));
		
		Integer isPurchase = input.getIsPurchase();
		
		String buyerType = input.getBuyerType();
		
		//534需求，用户最多只能查看200条记录
		int pageNum = input.getPageNum();//当前页
		int totalCount = pageNum*count;
		int thisCount = count;
		if(totalCount>200) {
			if((pageNum-1)*count>=200) {
				result.setResultCode(0);
				result.setResultMessage("用户最多展示200条浏览记录");
			}else {
				thisCount = 200 - (pageNum-1)* count;
			}
		}
		
		//分页
		PageOption pageOption = new PageOption();
		pageOption.setLimit(count);
		pageOption.setOffset(input.getPageNum()-1);
		
		MPageData mPageData = new MPageData();
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", memberCode);
		mPageData = DataPaging.upPageData("pc_browse_history", "product_code,create_time,zid", "create_time desc,zid desc", mWhereMap, pageOption);
		
		//总条数
		int page = mPageData.getPageResults().getTotal();
		//总页数
		int pagination = 0;
		//534需求，每个用户展示浏览记录不超过200条
		page = page>200?200:page;
		if (page % count == 0) {
			pagination = page / count;
		} else {
			pagination = page / count + 1;
		}
		
		Map<String, Integer> productCodesMap = new HashMap<String, Integer>();
		Map<String, MDataMap> productInfosMap = new HashMap<String, MDataMap>();
		Map<String, MDataMap> skuInfosMap = new HashMap<String, MDataMap>();
		Map<String, Integer> skuCodeMap = new HashMap<String, Integer>();
		Map<String, String> getSkuMap = new HashMap<String, String>();
		for(MDataMap map : mPageData.getListData()){
			productCodesMap.put(map.get("product_code"), 1);
		}
		ProductStoreService pStoreService = new ProductStoreService();
		List<MDataMap> mProductList = new ArrayList<MDataMap>();
		List<MDataMap> sellPriceList = new ArrayList<MDataMap>();
		List<MDataMap> skuList = new ArrayList<MDataMap>();
		List<MDataMap> pruductList = new ArrayList<MDataMap>();
//		List<MDataMap> stockNumList = new ArrayList<MDataMap>();
		List<String> productCodeArr = new ArrayList<String>();
		if(null!=productCodesMap && !productCodesMap.isEmpty()){
			mProductList = DbUp.upTable("pc_productinfo").queryAll("product_code,product_name,market_price,product_status,mainpic_url,small_seller_code", "", "product_code in ('"+StringUtil.join(productCodesMap.keySet(), "','")+"')", null);
			sellPriceList = DbUp.upTable("pc_skuinfo").queryAll("product_code,sku_code,min(sell_price) AS sell_price", "", "product_code in ('"+StringUtil.join(productCodesMap.keySet(), "','")+"') group by product_code", null);
			skuList = DbUp.upTable("pc_skuinfo").queryAll("product_code, sku_code", "", "product_code in ('"+StringUtil.join(productCodesMap.keySet(), "','")+"')",null);
		}
		for(MDataMap map : mProductList){
			productInfosMap.put(map.get("product_code"), map);
		}
		for(MDataMap map : sellPriceList){
			skuInfosMap.put(map.get("product_code"), map);
			skuCodeMap.put(map.get("sku_code"), 1);
		}
		//根据sku_code分组获得每个sku_code对应的付存数
//		if(null!=skuCodeMap && !skuCodeMap.isEmpty()){
//			stockNumList = DbUp.upTable("sc_store_skunum").queryAll("sku_code,sum(stock_num) AS stock_num", "", "sku_code in ('"+StringUtil.join(skuCodeMap.keySet(), "','")+"') group by sku_code", null);
//		}
		
		for(MDataMap map : mProductList){
			productCodeArr.add(map.get("product_code"));
		}
		//获取商品是否有库存
		Map<String,Integer> productStockMap = pStoreService.getStockNumAll("", StringUtils.join(productCodeArr,","),1);
		
		for(MDataMap map : skuList){
			if(null != getSkuMap.get(map.get("product_code"))){
				getSkuMap.put(map.get("product_code"),getSkuMap.get(map.get("product_code"))+","+map.get("sku_code"));
			}else {
				getSkuMap.put(map.get("product_code"), map.get("sku_code"));
			}
		}
		
		
//		ProductService productService = new ProductService();
		// 558需求修改,浏览历史参加活动商品添加下划线价格
		//Map<String,BigDecimal> productPriceMap = new HashMap<String, BigDecimal>();
		Map<String,PlusModelSkuInfo> productPriceMap = new HashMap<String, PlusModelSkuInfo>();
		if(null!=productCodeArr && productCodeArr.size()>0){	
//			productPriceMap = productService.getMinProductActivityNew(productCodeArr, buyerType);
//			Map<String, BigDecimal> productPriceMap = new HashMap<String,BigDecimal>();
			//if (VersionHelper.checkServerVersion("3.5.95.55")) {
			PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
			skuQuery.setCode(StringUtils.join(productCodeArr,","));
			skuQuery.setMemberCode(memberCode);
			//新加内购属性设置
			skuQuery.setIsPurchase(isPurchase);
			skuQuery.setChannelId(channelId);
			// 558需求修改,浏览历史参加活动商品添加下划线价格
			//productPriceMap = new ProductPriceService().getProductMinPrice(skuQuery);// 获取商品最低销售价格
			productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);// 获取商品最低销售价格和对应的划线价
//			}else{
//				productPriceMap = new ProductService().
//						getMinProductActivityNew(productCodeArr, buyerType);// 获取商品最低销售价格
//				productPriceMap = new ProductService().
//						getMinProductActivityNew2(productCodeArr, isPurchase);// 获取商品最低销售价格
//			}
			//现在使用的逻辑，插入新增数据时需要进行手动控制
			for(int i = 0;i < mPageData.getListData().size();i++){
				MDataMap map = mPageData.getListData().get(i);
				GetBrowseHistory getBrowseHistory = new GetBrowseHistory();
				MDataMap plMap = productInfosMap.get(map.get("product_code"));
				MDataMap skuMap = skuInfosMap.get(map.get("product_code"));
				if(null==plMap) {
					//此商品数据库中没有，数据库出现错误审判官
					continue;
				}
				getBrowseHistory.setProductCode(map.get("product_code"));
				getBrowseHistory.setBrowseTime(map.get("create_time"));
				getBrowseHistory.setMainpicUrl(plMap.get("mainpic_url"));
				getBrowseHistory.setMarketPrice(plMap.get("market_price"));
				getBrowseHistory.setProductName(plMap.get("product_name"));
				
				// 主图压缩
				Map<String, MFileItem> fileItemMap = plusVeryImage.upImageZoom(plMap.get("mainpic_url"), Constants.IMG_WIDTH_SP02);
				if(!fileItemMap.values().isEmpty()) {
					String url = fileItemMap.values().iterator().next().getFileUrl();
					if(StringUtils.isNotBlank(url)) {
						getBrowseHistory.setMainpicUrl(url);
					}
				}
				
				//524:添加商品分类标签
				PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(map.get("product_code")));
				String ssc =plusModelProductinfo.getSmallSellerCode();
				String st="";
				if("SI2003".equals(ssc)) {
					st="4497478100050000";
				}
				else {
					PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(ssc));
					st = sellerInfo.getUc_seller_type();
				}
				//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
				Map productTypeMap = WebHelper.getAttributeProductType(st);
				
				getBrowseHistory.setLabelsList(plusModelProductinfo.getLabelsList());
				getBrowseHistory.setLabelsPic(new ProductLabelService().getLabelInfo(map.get("product_code")).getListPic());
				getBrowseHistory.setProClassifyTag(productTypeMap.get("proTypeListPic").toString() );
				
//				if (AppConst.MANAGE_CODE_KJT.equals(plMap.get("small_seller_code"))
//						||AppConst.MANAGE_CODE_MLG.equals(plMap.get("small_seller_code"))
//						||AppConst.MANAGE_CODE_QQT.equals(plMap.get("small_seller_code"))
//						||AppConst.MANAGE_CODE_SYC.equals(plMap.get("small_seller_code"))
//						||AppConst.MANAGE_CODE_CYGJ.equals(plMap.get("small_seller_code"))) {
				if (new PlusServiceSeller().isKJSeller(plMap.get("small_seller_code"))) {
					getBrowseHistory.setFlagTheSea("1");
				}
//				getBrowseHistory.setSellPrice(skuMap.get("sell_price"));
				// 558需求修改,浏览历史参加活动商品添加下划线价格
				//getBrowseHistory.setSellPrice(productPriceMap.get(map.get("product_code")).toString());
				if(null != productPriceMap.get(map.get("product_code"))) {
					BigDecimal sellPrice = productPriceMap.get(map.get("product_code")).getSellPrice();
					getBrowseHistory.setSellPrice(sellPrice.toString());
					String eventType = productPriceMap.get(map.get("product_code")).getEventType();
					if("4497472600010001".equals(eventType) || "4497472600010002".equals(eventType) || "4497472600010005".equals(eventType) 
							 || "4497472600010018".equals(eventType) || "4497472600010024".equals(eventType)) {						
						BigDecimal skuPrice = productPriceMap.get(map.get("product_code")).getSkuPrice();
						if(skuPrice.compareTo(sellPrice) > 0) {
							getBrowseHistory.setSkuPrice(skuPrice);
						}
					}
				}else {
					getBrowseHistory.setSellPrice(plusModelProductinfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP).toString());
				}
				
				//判断状态
				if(plMap.get("product_status").equals("4497153900060002")){//判断是否已上架
					if(null == productStockMap || null == productStockMap.get(map.get("product_code")) || 1 != productStockMap.get(map.get("product_code"))){
						getBrowseHistory.setProductStatus("4497471600050002");//标记状态为售罄
					}else {
						getBrowseHistory.setProductStatus("4497153900060002");//标记状态为上架
					}
				}else {
					getBrowseHistory.setProductStatus(plMap.get("product_status"));//其他
				}
				result.getGetBrowseHistory().add(getBrowseHistory);
				if(thisCount!=count) {
					if(thisCount==result.getGetBrowseHistory().size()) {
						break;
					}
				}
			}
		}
		result.setPagination(pagination);
		result.setNowPage(input.getPageNum());
		//新增拼团标识
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(appVersion.compareTo("5.4.0")>=0){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
			List<GetBrowseHistory> items = result.getGetBrowseHistory();
			for(GetBrowseHistory itemEntity : items){
				String productCode = itemEntity.getProductCode();
				//根据商品编号查询商品所参与的活动
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(productCode);
				Map<String,PlusModelSkuInfo> map = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
				PlusModelSkuInfo skuInfo = map.get(productCode);
				if("4497472600010024".equals(skuInfo.getEventType())){//拼团单
					itemEntity.setProductType("4497472000050001");
					itemEntity.setGroupBuying("4497472600010024");
					itemEntity.setGroupBuyingPrice(skuInfo.getGroupBuyingPrice());//设置拼团价
					itemEntity.setSkuPrice(skuInfo.getSkuPrice());//拼团前的价格
					String eventCode = skuInfo.getEventCode();
					PlusModelEventInfo eventInfo = new PlusSupportEvent().upEventInfoByCode(eventCode);
					String collagePersonCount = eventInfo.getCollagePersonCount();//拼团人数
					itemEntity.setCollagePersonCount(collagePersonCount);
				}else{
					itemEntity.setProductType("4497472000050002");//不是拼团单
				}
				//5.4.2版本之后新增TagList商品活动标签
				ProductService service = new ProductService();
				List<String> tagList = service.getTagListByProductCode(productCode,memberCode,channelId);
				itemEntity.setTagList(tagList);
				//558需求新增商品标签自定义位置
				itemEntity.setLabelsInfo(new ProductLabelService().getLabelInfoList(productCode));
				if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
					List<TagInfo> tagInfoList = service.getProductTagInfoList(productCode, memberCode,channelId);
					itemEntity.setTagInfoList(tagInfoList);
				}
				//562版本对于商品列表标签做版本兼容处理
				if(appVersion.compareTo("5.6.2")<0){
					Iterator<PlusModelProductLabel> iter = itemEntity.getLabelsInfo().iterator();
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
}
