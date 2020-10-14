package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiGetMyCollectionProductInput;
import com.cmall.familyhas.api.model.CollectionProductModel;
import com.cmall.familyhas.api.result.APiGetMyCollectionProductResult;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.model.PageResults;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.service.ProductStoreService;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmasorder.model.TagInfo;
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
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠家有我的收藏
 * 
 * @author ligj
 * 
 */
public class APiGetMyCollectionProduct extends
		RootApiForToken<APiGetMyCollectionProductResult, APiGetMyCollectionProductInput> {

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	private final String SOLDOUT = "4497471600050002";			//售罄 
	public APiGetMyCollectionProductResult Process(
			APiGetMyCollectionProductInput inputParam, MDataMap mRequestMap) {
		APiGetMyCollectionProductResult result = new APiGetMyCollectionProductResult();
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType("4497469400050002");
		}
		List<CollectionProductModel> collectionProductList = new ArrayList<CollectionProductModel>();
		ProductService pService = new ProductService();
		ProductStoreService pStoreService = new ProductStoreService();
		Integer isPurchase =  inputParam.getIsPurchase();
		String channelId = getChannelId();
		// 获取分页每页条数
		int count = Integer.valueOf(Integer
				.parseInt(bConfig("familyhas.page_myCollection_limit")));
		PageOption paging = new PageOption();
		if (inputParam.getPageNum() <= 0) {
			return result;
		}
		String userType = inputParam.getBuyerType();
		paging.setOffset(inputParam.getPageNum() - 1);
		paging.setLimit(count);
		MPageData mPageData = new MPageData();
		
		
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("operate_type", "4497472000020001");
		mWhereMap.put("member_code", getUserCode());
		
		if(1 == inputParam.getIsReduce()) {
			List<MDataMap> queryAll = DbUp.upTable("fh_product_collection").queryAll("product_code,sku_code,sku_price", "operate_time desc,zid desc", "operate_type=:operate_type and member_code=:member_code", mWhereMap);
			mPageData.setListData(queryAll);
			PageResults pageResults = new PageResults();
			pageResults.setCount(queryAll.size());
			pageResults.setTotal(queryAll.size());
			pageResults.setMore(0);
			mPageData.setPageResults(pageResults);
		}
		if(0 == inputParam.getIsReduce()) {
			mPageData = DataPaging.upPageData("fh_product_collection",
					"product_code,sku_code,sku_price", "operate_time desc,zid desc", "operate_type=:operate_type and member_code=:member_code", mWhereMap,paging);
		}
		if (null != mPageData) {
			List<String> productCodeArr = new ArrayList<String>();
			//用于534我的收藏需求
			Map<String,Model1> map = new HashMap<>();
			for (MDataMap collectionMap : mPageData.getListData()) {
				productCodeArr.add(collectionMap.get("product_code"));
				Model1 model1 = new Model1();
				model1.setSkuCode(collectionMap.get("sku_code"));
				model1.setSkuPrice(collectionMap.get("sku_price"));
				map.put(collectionMap.get("product_code"), model1);
			}
			String sFileds = "product_code,product_name,market_price,product_status,mainpic_url,min_sell_price,small_seller_code";
			String sWhere = "product_code in ('"+StringUtils.join(productCodeArr,"','")+"')";
			List<MDataMap> productList = DbUp.upTable("pc_productinfo").queryAll(sFileds,"",sWhere,null);
			
			if (null != productList && productList.size() > 0) {
//				获取商品最低价格
//				Map<String,BigDecimal> productPriceMap = pService.getMinProductActivity(productCodeArr);
				// 558需求修改,我的收藏参加活动商品添加下划线价格
				//Map<String,BigDecimal> productPriceMap = new HashMap<String, BigDecimal>();
				Map<String,PlusModelSkuInfo> productPriceMap = new HashMap<String, PlusModelSkuInfo>();
				if(null!=productCodeArr && productCodeArr.size()>0){
//					if (VersionHelper.checkServerVersion("3.5.62.55")) {
//						productPriceMap = pService.getMinProductActivityNew(productCodeArr,userType);
//						if (VersionHelper.checkServerVersion("3.5.95.55")) {
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(StringUtils.join(productCodeArr,","));
					skuQuery.setMemberCode(getUserCode());
					skuQuery.setIsPurchase(isPurchase);
					skuQuery.setChannelId(channelId);
					// 558需求修改,我的收藏参加活动商品添加下划线价格
					//productPriceMap = new ProductPriceService().getProductMinPrice(skuQuery);// 获取商品最低销售价格
					productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);// 获取商品最低销售价格和对应的划线价
//						}else{
//							productPriceMap = new ProductService().
//									getMinProductActivityNew(productCodeArr, userType);// 获取商品最低销售价格
//							productPriceMap = new ProductService().
//									getMinProductActivityNew2(productCodeArr, isPurchase);// 获取商品最低销售价格
//						}
//					}else{
//						productPriceMap = pService.getMinProductActivity(productCodeArr);
//					}
				}
				
				//获取商品是否有库存
				Map<String,Integer> productStockMap = pStoreService.getStockNumAll("", StringUtils.join(productCodeArr,","),1);
				
				MDataMap salesMap = new MDataMap();
				if(null!=productCodeArr && productCodeArr.size()>0){
					salesMap = pService.getProductFictitiousSales("SI2003",productCodeArr,30);
				}
				
				Map<String,MDataMap> productObjMap = new HashMap<String,MDataMap>();
				
				for (MDataMap productMap : productList) {
					productObjMap.put(productMap.get("product_code"), productMap);
				}
				ProductLabelService labelService = new ProductLabelService();
				//Iterator<String> it = productCodeArr.iterator();
				
				  
				for (String productCode : productCodeArr) {
				//while(it.hasNext()){
				//	String productCode = it.next();
					CollectionProductModel model = new CollectionProductModel();
					MDataMap productMap = productObjMap.get(productCode);
					if (null == productMap || productMap.isEmpty()) {
						continue;
					}
					model.setProductCode(productCode);
					model.setProductName(productMap.get("product_name"));
					model.setMarketPrice(productMap.get("market_price"));
					model.setPicture(productMap.get("mainpic_url"));
					
					model.setProductStatus(productMap.get("product_status"));
					//524:添加商品分类标签
					PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
					
					// 558需求修改,浏览历史参加活动商品添加下划线价格
					//model.setSellPrice(productPriceMap.get(productCode));
					if(null != productPriceMap.get(productCode)) {						
						BigDecimal sellPrice1 = productPriceMap.get(productCode).getSellPrice();
						model.setSellPrice(sellPrice1);
						String eventType = productPriceMap.get(productCode).getEventType();
						if("4497472600010001".equals(eventType) || "4497472600010002".equals(eventType) || "4497472600010005".equals(eventType) 
								 || "4497472600010018".equals(eventType) || "4497472600010024".equals(eventType)) {						
							BigDecimal skuPrice1 = productPriceMap.get(productCode).getSkuPrice();
							if(skuPrice1.compareTo(sellPrice1) > 0) {
								model.setSkuPrice(skuPrice1);
							}
						}
					}else {
						model.setSellPrice(productInfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP));
					}
					String ssc =productInfo.getSmallSellerCode();
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
					
					model.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
					
					//只有上架的时候才会去判断库存数
					if ("4497153900060002".equals(productMap.get("product_status"))) {
						if(null == productStockMap || null == productStockMap.get(productCode) || 1 != productStockMap.get(productCode)){
							model.setProductStatus(SOLDOUT);
						}
					}
					
					//销量
					if (null != salesMap && !salesMap.isEmpty()) {
						if (StringUtils.isNotEmpty(salesMap.get(productCode))) {
							model.setSaleNum(Integer.parseInt(salesMap.get(productCode)));
						}
					}
					if (pService.checkProductKjt(productCode)) {
						model.setFlagTheSea("1");
					}
					// 缓存获取商品信息
					PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
					model.setLabelsList(plusModelProductinfo.getLabelsList());
					
					model.setLabelsPic(labelService.getLabelInfo(productCode).getListPic());
					model.setLabelsInfo(labelService.getLabelInfoList(productCode));
					//562版本对于商品列表标签做版本兼容处理
					String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
					if(appVersion.compareTo("5.6.2")<0){
						Iterator<PlusModelProductLabel> iter = model.getLabelsInfo().iterator();
						while (iter.hasNext()) {
							PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
							if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
								iter.remove();
							}
						}
					}
					
					//5.3.4版本后判断是否展示降价提醒信息
					if(AppVersionUtils.compareTo(inputParam.getApp_vision(), "5.3.2") > 0) {
						Model1 model1 = map.get(productCode);
						if(null!=model1&&("4497153900060002".equals(model.getProductStatus())||"4497471600050002".equals(model.getProductStatus()))) {
							String skuCode = model1.getSkuCode();
							String skuPrice = model1.getSkuPrice();
							if(!"".equals(skuCode)&&!"".equals(skuPrice)) {
								BigDecimal dbPrice = new BigDecimal(skuPrice);
								PlusModelSkuInfo upSkuInfoBySkuCode = new PlusSupportProduct().upSkuInfoBySkuCode(skuCode,getUserCode(),"",1);
								BigDecimal sellPrice = upSkuInfoBySkuCode.getSellPrice();
								if(dbPrice.compareTo(sellPrice)>0) {
									model.setIsShowDepreciate("1");
									model.setDepreciateInfo("比关注时降价"+(dbPrice.subtract(sellPrice))+"元");
								} 
								else { if(1 == inputParam.getIsReduce()) { continue; } }
							}else {
								if(1 == inputParam.getIsReduce()) {
									continue;
								
								}
							}
						}else {
							if(1 == inputParam.getIsReduce()) {
								continue;
							}
						}
					}
					
					//5.5.8增加sku实际库存 前端用于库存提示
					Model1 model1 = map.get(productCode);
					String skuCode = model1.getSkuCode();
					int skuStock = new PlusSupportStock().upAllStockForProduct(productCode);
					model.setSkuStock(skuStock);
					
					collectionProductList.add(model);
				}
				
			}
		}
		result.setCollectionProductList(collectionProductList);
		// 总页数
		int pagination1 = 0;
		// 查询数据库中优惠劵总条数
		int page1 = mPageData.getPageResults().getTotal();

		if (page1 % count == 0) {
			pagination1 = page1 / count;
		} else {
			pagination1 = page1 / count + 1;
		}
		if(1 == inputParam.getIsReduce()) {
			pagination1 = 1;
		}
		result.setPagination(pagination1);
		//新增拼团标识
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		appVersion = "5.4.0";
		if(appVersion.compareTo("5.4.0")>=0){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
			List<CollectionProductModel> items = result.getCollectionProductList();
			for(CollectionProductModel itemEntity : items){
				String productCode = itemEntity.getProductCode();
				//根据商品编号查询商品所参与的活动
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(productCode);
				skuQuery.setChannelId(channelId);
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
				List<String> tagList = service.getTagListByProductCode(productCode,getUserCode(),channelId);
				itemEntity.setTagList(tagList);
				
				if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
					List<TagInfo> tagInfoList = service.getProductTagInfoList(productCode, getUserCode(), channelId);
					itemEntity.setTagInfoList(tagInfoList);
				}
			}
		}
		return result;
	}
	
	
	class Model1{
		private String skuCode;
		private String skuPrice;
		public String getSkuCode() {
			return skuCode;
		}
		public void setSkuCode(String skuCode) {
			this.skuCode = skuCode;
		}
		public String getSkuPrice() {
			return skuPrice;
		}
		public void setSkuPrice(String skuPrice) {
			this.skuPrice = skuPrice;
		}
		
	}
	
}
