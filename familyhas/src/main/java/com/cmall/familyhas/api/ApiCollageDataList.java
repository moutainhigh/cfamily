package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiCollageDataListInput;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiCollageDataListResult;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiCollageDataList extends RootApiForVersion<ApiCollageDataListResult, ApiCollageDataListInput> {

	@SuppressWarnings("deprecation")
	@Override
	public ApiCollageDataListResult Process(ApiCollageDataListInput inputParam, MDataMap mRequestMap) {
		int page = inputParam.getPage() - 1;//当前页数
		int pageCount = inputParam.getPageCount();//每页记录个数
		int start = page * pageCount;
		String sellerCode = getManageCode();
		String maxWidth = inputParam.getMaxWidth();
		
		ApiCollageDataListResult apiResult = new ApiCollageDataListResult();
		ProductService productService = new ProductService();
		LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
		LoadEventItemProduct itemProduct = new LoadEventItemProduct();
		PlusModelEventItemQuery itemQuery = new PlusModelEventItemQuery();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		OrderService os = new OrderService();
		skuQuery.setChannelId(getChannelId());
		
		PlusSupportEvent plusEvent = new PlusSupportEvent();
		PlusSupportStock plusStock = new PlusSupportStock();
		MDataMap stockParams = new MDataMap();
		List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
		
		String countSql = "select count(1) from (select t.product_code from sc_event_item_product p, sc_event_info i, productcenter.pc_productinfo t "
				+ "where p.event_code = i.event_code and (i.event_status='4497472700020002' or i.event_status='4497472700020004') "
				+ "and i.event_type_code = '4497472600010024' and t.seller_code = '" + sellerCode + "' and i.begin_time <= sysdate() and i.end_time >= sysdate() and p.flag_enable = 1 "
				+ "and (i.channels = '' OR i.channels like '%"+getChannelId()+"%') "
				+ "and t.product_code = p.product_code and t.product_status = '4497153900060002' group by t.product_code) aa ";
		int count = DbUp.upTable("sc_event_info").upTemplate().queryForInt(countSql, new HashMap<String, Object>());
		int totalPage = count / pageCount;
		if(count % pageCount != 0) {
			totalPage += 1;
		}
		apiResult.setPageNum(totalPage);
		
		String sql = "select p.sku_code, p.product_code, p.seat, p.favorable_price, p.item_code, i.begin_time, i.end_time, p.sales_advertisement, t.product_name, t.min_sell_price, t.market_price, t.mainpic_url, "
				+ "p.cover_img, t.small_seller_code, i.event_code, i.collage_person_count,i.collage_type "
				+ "from sc_event_item_product p, sc_event_info i, productcenter.pc_productinfo t where p.event_code = i.event_code and (i.event_status='4497472700020002' or i.event_status='4497472700020004') "
				+ "and i.event_type_code = '4497472600010024' and t.seller_code = :sellerCode and i.begin_time <= sysdate() and i.end_time >= sysdate() and p.flag_enable = 1 "
				+ "and (i.channels = '' OR i.channels like '%"+getChannelId()+"%') "
				+ "and t.product_code = p.product_code and t.product_status = '4497153900060002' group by p.product_code order by i.collage_type desc, i.end_time asc, p.seat asc, p.zid desc "
				+ "limit " + start + ", " + pageCount + "";
		List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode", sellerCode));
		for(Map<String, Object> map : list) {
			boolean showFlag = false;
			stockParams.put("event_code", MapUtils.getString(map, "event_code", ""));
			stockParams.put("product_code", MapUtils.getString(map, "product_code", ""));
			List<Map<String, Object>> itemList = DbUp.upTable("sc_event_item_product").dataSqlList("select p.item_code, p.sku_code,p.sales_num,p.rate_of_progress from sc_event_item_product p where p.event_code = :event_code and p.flag_enable = 1 and "
					+ "p.product_code = :product_code", stockParams);
			long limitStock=0;
			long actualStock = 0;
			int allSaleNum = 0;
			int num = 0;
			int allProgress = 0;
			for(Map<String, Object> item : itemList) {
				 long sublimitStock = plusEvent.upEventItemSkuStock(MapUtils.getString(item, "item_code", ""));
				 long subactualStock = plusStock.upAllStock(MapUtils.getString(item, "sku_code", ""));
				 num=num+1;
				 allProgress = allProgress +Integer.parseInt((item.get("rate_of_progress")==null||StringUtils.isBlank(item.get("rate_of_progress").toString()))?"0":item.get("rate_of_progress").toString());
				 allSaleNum=allSaleNum + Integer.parseInt(item.get("sales_num").toString());
				 if(sublimitStock > 0 && subactualStock > 0) {
					limitStock=limitStock+sublimitStock;
					actualStock=actualStock+subactualStock;
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
			columnContent.setCollageType(MapUtils.getString(map, "collage_type"));//新增拼团类型，邀新团/普通团
			//562 添加活动进行的进度
			long minStore = Math.min(limitStock, actualStock);
			int saleNumbers = allSaleNum -Integer.valueOf(minStore+"");
			int averageProgress = (num==0?100:(allProgress/num));
			int rateOfProgress =(allSaleNum==0)?100:((int) (averageProgress!=100?((Double.parseDouble(saleNumbers+"")/Double.parseDouble(allSaleNum+""))*(100-averageProgress)+averageProgress):100));
			rateOfProgress= rateOfProgress>100?100:rateOfProgress;	
			columnContent.setRateOfProgress(rateOfProgress+"");
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
			
			//5.5.8增加所有sku实际库存 前端用于库存提示
			int allSkuRealStock = new PlusSupportStock().upAllStockForProduct(MapUtils.getString(map, "product_code", ""));
			productInfo.setAllSkuRealStock(allSkuRealStock);
			
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
			saleNumbers = os.getProductOrderNum(skuInfo.getSmallSellerCode(), skuInfo.getProductCode(), "4497153900010005");//查询已售多少件
			productInfo.setSalesNums(saleNumbers);
			productInfo.setSaveValue(MoneyHelper.round(0,BigDecimal.ROUND_FLOOR,new BigDecimal(productInfo.getMarkPrice()).subtract(new BigDecimal(productInfo.getSellPrice()))).toString());
			productInfo.setAdPicUrl(skuInfo.getDescriptionUrlHref());//赋值广告图
			columnContent.setProductInfo(productInfo);
			
			contentList.add(columnContent);
		}
		
		apiResult.setContentList(contentList);
		return apiResult;
	}
}
