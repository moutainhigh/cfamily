package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetLiveRoomProdListInput;
import com.cmall.familyhas.api.model.LiveProd;
import com.cmall.familyhas.api.result.ApiForGetLiveRoomProdListResult;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取直播间商品列表接口
 * @author lgx
 *
 */
public class ApiForGetLiveRoomProdList extends RootApiForVersion<ApiForGetLiveRoomProdListResult, ApiForGetLiveRoomProdListInput> {

	@Override
	public ApiForGetLiveRoomProdListResult Process(ApiForGetLiveRoomProdListInput inputParam, MDataMap mRequestMap) {
		ApiForGetLiveRoomProdListResult result = new ApiForGetLiveRoomProdListResult();
		ProductService pService = new ProductService();
		
		String channelId = getChannelId();
		String appVersion = "";
		if(this.getApiClient() != null && StringUtils.isNotBlank(this.getApiClient().get("app_vision"))){
			appVersion = this.getApiClient().get("app_vision");
		}
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String liveRoomId = inputParam.getLiveRoomId();
		int page = inputParam.getPage();
		// 起始索引
		int index = (page-1) * 20;
		
		int totalPage = 0;
		List<LiveProd> liveProdList = new ArrayList<LiveProd>();
		
		MDataMap liveRoom = DbUp.upTable("lv_live_room").one("live_room_id",liveRoomId,"is_delete","0");
		if(liveRoom != null) {
			String liveProdSql = "SELECT l.* FROM lv_live_room_product l LEFT JOIN productcenter.pc_productinfo p ON p.product_code = l.product_code " + 
					" WHERE l.live_room_id = '"+liveRoomId+"' AND l.delete_flag = '1' AND p.product_status = '4497153900060002'  ORDER BY l.sort DESC LIMIT "+index+",20";
			List<Map<String, Object>> liveProdMapList = DbUp.upTable("lv_live_room_product").dataSqlList(liveProdSql, new MDataMap());
			if(liveProdMapList != null && liveProdMapList.size() > 0) {
				for (Map<String, Object> map : liveProdMapList) {
					LiveProd liveProd = new LiveProd();
					String productCode = MapUtils.getString(map, "product_code");
					
					PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
					
					liveProd.setProductCode(productCode);
					liveProd.setProductName(productInfo.getProductName());
					liveProd.setMainpicUrl(productInfo.getMainpicUrl());
					liveProd.setProductStatus(productInfo.getProductStatus());
					
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					List<String> productCodeArr = new ArrayList<String>();
					productCodeArr.add(productCode);
					skuQuery.setCode(StringUtils.join(productCodeArr, ","));
					skuQuery.setMemberCode(memberCode);
					skuQuery.setIsPurchase(1);
					skuQuery.setChannelId(channelId);
					// 获取商品最低销售价格和对应的划线价
					Map<String, PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
					if (null != productPriceMap.get(productCode)) {
						BigDecimal sellPrice1 = productPriceMap.get(productCode).getSellPrice();
						// 销售价
						liveProd.setSellPrice(MoneyHelper.format(sellPrice1));
						BigDecimal skuPrice1 = productPriceMap.get(productCode).getSkuPrice();
						String eventType = productPriceMap.get(productCode).getEventType();
						if (skuPrice1.compareTo(sellPrice1) > 0) {
							// 划线价
							liveProd.setMarkPrice(MoneyHelper.format(skuPrice1));
						}
						if("4497472600010024".equals(eventType)) {
							// 拼团商品
							liveProd.setProductType("4497472000050001");
							liveProd.setGroupBuying("4497472600010024");
							String eventCode = productPriceMap.get(productCode).getEventCode();
							PlusModelEventQuery tQuery = new PlusModelEventQuery();
							tQuery.setCode(eventCode);
							PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(tQuery);
							liveProd.setCollagePersonCount(eventInfo.getCollagePersonCount());
						}else {
							liveProd.setProductType("4497472000050002");
						}
					} else {
						// 销售价
						liveProd.setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
						liveProd.setProductType("4497472000050002");
					}
					
					int stockNum = 0;
					String stockNumSql = "SELECT ifnull(sum(ss.stock_num),0) num FROM systemcenter.sc_store_skunum ss WHERE ss.sku_code in " + 
							"(SELECT s.sku_code FROM productcenter.pc_skuinfo s WHERE s.product_code = '"+productCode+"')";
					Map<String, Object> stockNumMap = DbUp.upTable("sc_store_skunum").dataSqlOne(stockNumSql, new MDataMap());
					if(stockNumMap != null) {
						stockNum = MapUtils.getIntValue(stockNumMap, "num");
					}
					liveProd.setAllSkuRealStock(stockNum);
					liveProd.setStockNum(stockNum==0?"抢光了":"有货");
					
					// 自营标签
					LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
					String stChar = "";
					if("SI2003".equals(productInfo.getSmallSellerCode())) {
						stChar = "4497478100050000";
					}else {
						PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(productInfo.getSmallSellerCode()));
						stChar = sellerInfo.getUc_seller_type();
					}
					Map<String, String> productTypeMap = WebHelper.getAttributeProductType(stChar);
					liveProd.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
					
					// 带样式的活动标签
					if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
						List<TagInfo> tagInfoList = pService.getProductTagInfoList(productCode, memberCode,channelId);
						liveProd.setTagInfoList(tagInfoList);
					}
					
					// 活动文字标签
					//5.4.2版本之后新增TagList商品活动标签
					List<String> tagList = pService.getTagListByProductCode(productCode, memberCode, channelId);
					liveProd.setTagList(tagList);
					
					// 四角标签
					ProductLabelService productLabelService = new ProductLabelService();
					List<PlusModelProductLabel> labelInfoList = productLabelService.getLabelInfoList(productCode);
					//562版本对于商品列表标签做版本兼容处理
					if(appVersion.compareTo("5.6.2")<0){
						Iterator<PlusModelProductLabel> iter = labelInfoList.iterator();
						while (iter.hasNext()) {
							PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
							if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
								iter.remove();
							}
						}
					}
					liveProd.setLabelsInfo(labelInfoList);
					
					liveProdList.add(liveProd);
				}
			}
			
			String countSql = "SELECT count(1) num FROM lv_live_room_product l LEFT JOIN productcenter.pc_productinfo p ON p.product_code = l.product_code " + 
					" WHERE l.live_room_id = '"+liveRoomId+"' AND l.delete_flag = '1' AND p.product_status = '4497153900060002' ";
			Map<String, Object> countMap = DbUp.upTable("lv_live_room_product").dataSqlOne(countSql, new MDataMap());
			double num = MapUtils.getDoubleValue(countMap, "num");
			// 总页数
			totalPage = (int) Math.ceil(num/20);
			
		}else {
			result.setResultCode(-1);
			result.setResultMessage("该直播间不存在!");
		}
		
		result.setTotalPage(totalPage);
		result.setLiveProdList(liveProdList);
		
		return result;
	}

}
