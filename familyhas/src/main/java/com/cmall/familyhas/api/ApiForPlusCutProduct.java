package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.result.ApiForPlusCutProductResult;
import com.cmall.familyhas.api.result.PlusSaleProduct;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadPlusSaleProduct;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusSaleProductModelQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/** 
* @author Angel Joy
* @Time 2020年5月8日 下午2:08:50 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusCutProduct extends RootApiForToken<ApiForPlusCutProductResult, RootInput> {

	@Override
	public ApiForPlusCutProductResult Process(RootInput inputParam, MDataMap mRequestMap) {
		String sql = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010032' AND event_status = '4497472700020002' AND begin_time <= sysdate() AND end_time >= sysdate() limit 1";
		Map<String,Object> eventInfo = DbUp.upTable("sc_event_info").dataSqlOne(sql, new MDataMap());
		ApiForPlusCutProductResult result = new ApiForPlusCutProductResult();
		if(eventInfo == null || eventInfo.isEmpty()) {
			result.setResultCode(0);
			result.setResultMessage("当前无活动！");
			return result;
		}
		String channelId = getChannelId();
		MDataMap eventInfoMap = new MDataMap(eventInfo);
		PlusSaleProductModelQuery tQuery = new PlusSaleProductModelQuery();
		tQuery.setCode(eventInfoMap.get("event_code"));
		PlusModelFlashSaleProduct items = new LoadPlusSaleProduct().upInfoByCode(tQuery);
		List<Map<String,Object>> maps = items.getItems();
		List<PlusSaleProduct> products = new ArrayList<PlusSaleProduct>();
		ProductService ps = new ProductService();
		PlusSupportProduct psp = new PlusSupportProduct();
		for(Map<String,Object> map : maps) {
			MDataMap m = new MDataMap(map);
			PlusSaleProduct product = new PlusSaleProduct();
			product.setProductCode(m.get("product_code"));
			product.setProductName(m.get("sku_name"));
			String imgUrl = m.get("mainpic_url");
			PicInfo imgInfo = ps.getPicInfoOprBig(400,imgUrl);//压缩主图
			product.setProductUrl(imgInfo.getPicNewUrl());
			product.setSkuCode(m.get("sku_code"));
			PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(m.get("sku_code"));
			//这边直接查库
			MDataMap skuInfoDb = DbUp.upTable("sc_event_item_product").one("event_code",eventInfoMap.get("event_code"),"sku_code",product.getSkuCode(),"flag_enable","1");
			if(skuInfoDb == null || skuInfoDb.isEmpty()) {
				continue;
			}
			product.setPlusVipPrice(new BigDecimal(skuInfoDb.get("favorable_price")));
			PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
			skuQuery.setCode(product.getProductCode());
			skuQuery.setChannelId(channelId);
			Map<String,BigDecimal> priceMap = new ProductPriceService().getProductMinPrice(skuQuery);
			if(priceMap != null) {
				product.setOrgSellPrice(priceMap.get(product.getProductCode()));
			}
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
			Map<String,String> productTypeMap = WebHelper.getAttributeProductType(st);
			product.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
			products.add(product);
		}
		result.setEventCode(eventInfoMap.get("event_code"));
		result.setProducts(products);
		result.setPlusProductCode(bConfig("xmassystem.plus_product_code"));
		result.setPlusSkuCode(bConfig("xmassystem.plus_sku_code"));
		return result;
	}

}
