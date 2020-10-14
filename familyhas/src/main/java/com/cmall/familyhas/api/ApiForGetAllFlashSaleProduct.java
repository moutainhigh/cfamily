package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetAllFlashSaleProductInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.ApiForGetAllFlashSaleProductResult;
import com.cmall.familyhas.api.result.FlashSaleProduct;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webmodel.MOauthInfo;
import com.srnpr.zapweb.websupport.OauthSupport;

/**
 * 获取所有秒杀商品接口
 * @author lgx
 *
 */
public class ApiForGetAllFlashSaleProduct extends RootApi<ApiForGetAllFlashSaleProductResult, ApiForGetAllFlashSaleProductInput> {

	@Override
	public ApiForGetAllFlashSaleProductResult Process(ApiForGetAllFlashSaleProductInput inputParam, MDataMap mRequestMap) {
		ApiForGetAllFlashSaleProductResult result = new ApiForGetAllFlashSaleProductResult();
		ProductService ps = new ProductService();
		PlusSupportProduct psp = new PlusSupportProduct();
		String memberCode = "";
		if (StringUtils.isNotEmpty(mRequestMap.get("api_token"))) {
			MOauthInfo oauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token"));
			if (oauthInfo != null) {
				memberCode = oauthInfo.getUserCode();
			}
		}
		List<FlashSaleProduct> items = new ArrayList<FlashSaleProduct>();
		
		String nowTime = FormatHelper.upDateTime();
		// 查询当前时间已发布且可用的秒杀活动
		String eventInfoSql = "SELECT * FROM sc_event_info WHERE event_type_code = '4497472600010001' AND event_status = '4497472700020002' AND flag_enable = '1' "
				+ "AND seller_code = 'SI2003' AND begin_time <= '"+nowTime+"' AND end_time > '"+nowTime+"'";
		List<Map<String, Object>> eventInfoList = DbUp.upTable("sc_event_info").dataSqlList(eventInfoSql, new MDataMap());
		if(null != eventInfoList && eventInfoList.size() > 0) {
			for (Map<String, Object> map : eventInfoList) {
				String event_code = (String) map.get("event_code");
				// 根据活动编号查询可用的秒杀商品
				//String itemProdSql = "SELECT * FROM sc_event_item_product WHERE event_code = '"+event_code+"' AND flag_enable = '1'";
				String itemProdSql = "SELECT s.* FROM (SELECT p.* FROM sc_event_item_product p WHERE p.event_code = '"+event_code+"' AND p.flag_enable = '1' ORDER BY p.favorable_price ASC ) s GROUP BY s.product_code ";
				List<Map<String, Object>> itemProdList = DbUp.upTable("sc_event_item_product").dataSqlList(itemProdSql, new MDataMap());
				if(null != itemProdList && itemProdList.size() > 0) {
					for (Map<String, Object> map2 : itemProdList) {
						MDataMap m = new MDataMap(map2);
						FlashSaleProduct product = new FlashSaleProduct();
						product.setProductCode(m.get("product_code"));
						product.setProductName(m.get("sku_name"));
						String imgUrl = m.get("mainpic_url");
						PicInfo imgInfo = ps.getPicInfoOprBig(400,imgUrl);//压缩主图
						product.setProductUrl(imgInfo.getPicNewUrl());
						product.setSkuCode(m.get("sku_code"));
						PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(m.get("sku_code"), memberCode, "", 1);
						// 已经开始秒杀。实时计算当前商品售价
						product.setKillPrice(new BigDecimal(m.get("favorable_price")));
						product.setOrgSellPrice(skuInfo.getSkuPrice());
						Button button = new Button();
						button.setButtonCode("4497477800080020");
						button.setButtonTitle("去抢购");
						product.getButtons().add(button);
						
						List<String> tags = new ArrayList<String>();//ps.getTagListByProductCode(m.get("product_code"), memberCode);
						tags.add("秒杀");
						//524:添加返回商品分类标签
						String smallSellerCode = skuInfo.getSmallSellerCode();
						String st="";
						if("SI2003".equals(smallSellerCode)) {
							st="4497478100050000";
						}
						else {
							LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
							PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(smallSellerCode));
							if(sellerInfo != null){
								st = sellerInfo.getUc_seller_type();
							}
						}
						//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
						product.setTagList(tags);
						//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
						Map<String,String> productTypeMap = WebHelper.getAttributeProductType(st);
						
						product.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
						//562 添加活动进行的进度
						PlusSupportEvent plusEvent = new PlusSupportEvent();
						PlusSupportStock plusStock = new PlusSupportStock();
						MDataMap stockParams = new MDataMap();
						stockParams.put("event_code", MapUtils.getString(map2, "event_code", ""));
						stockParams.put("product_code", MapUtils.getString(map2, "product_code", ""));
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
							if(sublimitStock > 0 && subactualStock > 0) {
								limitStock=limitStock+sublimitStock;
								actualStock=actualStock+subactualStock;
								allProgress = allProgress +Integer.parseInt((item.get("rate_of_progress")==null||StringUtils.isBlank(item.get("rate_of_progress").toString()))?"0":item.get("rate_of_progress").toString());
								num=num+1;
								allSaleNum=allSaleNum + Integer.parseInt(item.get("sales_num").toString());
							}
						}
						long minStore = Math.min(limitStock, actualStock);
						int saleNumbers = allSaleNum -Integer.valueOf(minStore+"");
						int averageProgress = (num==0?100:(allProgress/num));
						int rateOfProgress =(allSaleNum==0||skuInfo.getBuyStatus()==6)?100:((int) (averageProgress!=100?((Double.parseDouble(saleNumbers+"")/Double.parseDouble(allSaleNum+""))*(100-averageProgress)+averageProgress):100));
						rateOfProgress= rateOfProgress>100?100:rateOfProgress;	
						product.setRateOfProgress(rateOfProgress+"");
						items.add(product);
					}
				}
			}
		}
		
		result.setItems(items);
		
		return result;
	}

	
}
