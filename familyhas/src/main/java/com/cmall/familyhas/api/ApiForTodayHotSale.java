package com.cmall.familyhas.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.api.input.ApiForTodayHotSaleInput;
import com.cmall.familyhas.api.model.ProductHotSale;
import com.cmall.familyhas.api.result.ApiForTodayHotSaleResult;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 大转盘今日热卖接口
 */
public class ApiForTodayHotSale extends RootApiForVersion<ApiForTodayHotSaleResult, ApiForTodayHotSaleInput>{

	int pageSize = 20;
	String baseSql = "SELECT #field# FROM pc_productsales_everyday e, pc_productinfo p "
					+ " WHERE e.`day` = :day and e.product_code = p.product_code and e.zid > :startZid "
					+ " and p.product_status = '4497153900060002' "
					+ " ORDER BY sales desc "
					+ " #limit#";
	
	ProductLabelService productLabelService = new ProductLabelService();
	LoadEventInfo loadEventInfo = new LoadEventInfo();
	
	@Override
	public ApiForTodayHotSaleResult Process(ApiForTodayHotSaleInput inputParam, MDataMap mRequestMap) {
		ApiForTodayHotSaleResult result = new ApiForTodayHotSaleResult();
		result.setBrowserTime(Integer.parseInt(bConfig("familyhas.browser_time")));//互动游戏浏览商品任务，新增倒计时时间。NG++ 20190925
		String sSql = baseSql.replace("#field#", "count(1) num").replace("#limit#", "");
		MDataMap paraMap = new MDataMap();
		// 查询昨日销量
		paraMap.put("day", FormatHelper.upDateTime(DateUtils.addDays(new Date(), -1),"yyyy-MM-dd"));
		paraMap.put("startZid", bConfig("familyhas.start_zid"));
		Map<String, Object> countMap = DbUp.upTable("pc_productinfo").dataSqlOne(sSql, paraMap);
		
		int totalCount = NumberUtils.toInt(countMap.get("num")+"");
		int totalPage = totalCount / pageSize;
		if(totalCount > 0 && (totalCount % pageSize > 0)) {
			totalPage++;
		}
		
		result.setPagination(totalPage);
		
		int startIndex = (inputParam.getPageIndex() - 1)*pageSize;
		if(inputParam.getPageIndex() < 1) {
			startIndex = 0;
		}
		
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String channelId = getChannelId();
		
		//根据商品编号查询商品所参与的活动
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		ProductPriceService productPriceService = new ProductPriceService();
		Map<String,PlusModelSkuInfo> map;
		ProductHotSale hotSale;
		LoadProductInfo loadProductInfo = new LoadProductInfo();
		PlusModelProductInfo productInfo;
		PlusModelSkuInfo skuInfo = null;
		PlusModelEventQuery eventQuery = new PlusModelEventQuery();
		PlusModelEventInfo eventInfo;
		
		sSql = baseSql.replace("#field#", "e.product_code").replace("#limit#", "limit " + startIndex + "," + pageSize);
		List<Map<String, Object>> productCodeList = DbUp.upTable("pc_productinfo").dataSqlList(sSql, paraMap);
		for(Map<String, Object> pcMap : productCodeList) {
			skuQuery.setCode(pcMap.get("product_code").toString());
			skuQuery.setMemberCode(memberCode);
			skuQuery.setChannelId(channelId);
			map = productPriceService.getProductMinPriceSkuInfo(skuQuery);
			if(map.isEmpty()) {
				continue;
			}
			
			skuInfo = map.get(skuQuery.getCode());
			
			productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(skuQuery.getCode()));
			hotSale = new ProductHotSale();
			hotSale.setProcuctCode(productInfo.getProductCode());
			hotSale.setProductName(productInfo.getProductName());
			hotSale.setSellPrice(skuInfo.getSellPrice().toString());
			hotSale.setSkuPrice(skuInfo.getSkuPrice().toString());
			hotSale.setMainpic_url(productInfo.getMainpicUrl());
			hotSale.setSmallSellerCode(productInfo.getSmallSellerCode());
			hotSale.setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
			
			String sellerTypeLabel = "";
			if("SI2003".equals(productInfo.getSmallSellerCode())) {
				sellerTypeLabel = "4497478100050000";
			} else {
				sellerTypeLabel = WebHelper.getSellerType(productInfo.getSmallSellerCode());
			}
			
			//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
			Map<String,String> productTypeMap = WebHelper.getAttributeProductType(sellerTypeLabel);
			hotSale.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
			
			if(StringUtils.isNotBlank(skuInfo.getEventCode())) {
				eventQuery.setCode(skuInfo.getEventCode());
				eventInfo = loadEventInfo.upInfoByCode(eventQuery);
				if(StringUtils.isNotBlank(eventInfo.getEventType())) {
					hotSale.setEventTypeCode(eventInfo.getEventType());
					hotSale.setEventTypeName(eventInfo.getEventNote());
					
					// 拼团设置人数
					if(eventInfo.getEventType().equals("4497472600010024")) {
						hotSale.setCollagePersonCount(skuInfo.getCollagePersonCount());
					}
				}
			}
			
			result.getProductList().add(hotSale);
		}
		
		return result;
	}

}
