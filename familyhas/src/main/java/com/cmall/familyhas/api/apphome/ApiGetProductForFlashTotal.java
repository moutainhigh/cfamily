package com.cmall.familyhas.api.apphome;

import java.util.List;

import com.cmall.familyhas.api.input.apphome.ApiGetProductForFlashTotalInput;
import com.cmall.familyhas.api.result.apphome.ApiGetProductForFlashTotalResult;
import com.cmall.productcenter.service.ProductStoreService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 积分商城中获取商品库存，多SKU获取库存总和
 * @author AngelJoy
 * @Email zhouenzhi@jiayougo.com
 * @Date 2018-06-23 14:48:00
 *
 */
public class ApiGetProductForFlashTotal extends RootApi<ApiGetProductForFlashTotalResult,ApiGetProductForFlashTotalInput> {
	
	public ApiGetProductForFlashTotalResult Process(ApiGetProductForFlashTotalInput input, MDataMap mRequestMap) {
		
		ApiGetProductForFlashTotalResult result = new ApiGetProductForFlashTotalResult();
		String product_code=input.getProduct_code();
		
		MDataMap productMap=DbUp.upTable("pc_productinfo").one("product_code",product_code);
		if(productMap==null){
			result.setResultCode(941901018);
			result.setResultMessage(bInfo(941901018, product_code));
			return result;
		}
		
		result.setProduct_code(product_code);
		result.setProduct_name(productMap.get("product_name"));
		result.setProduct_price(productMap.get("market_price"));
		result.setProduct_status(DbUp.upTable("sc_define").one("parent_code","449715390006","define_code",productMap.get("product_status")).get("define_name"));
		
		List<MDataMap> skuList=DbUp.upTable("pc_skuinfo").queryAll("sku_code,sell_price,market_price", "", "product_code=:product_code and flag_enable=1 and sale_yn=:sale_yn", new MDataMap("product_code",product_code,"sale_yn","Y"));
		int stoc_num = 0;
		if(skuList!=null&&skuList.size()>0){
			
			ProductStoreService productStoreService = new ProductStoreService();
			
			for (MDataMap mDataMap : skuList) {
				String sku_code=mDataMap.get("sku_code");
				
				int stock=productStoreService.getStockNumBySku(sku_code);
				stoc_num += stock;
			}
			
		}
		result.setProduct_stock(stoc_num);
		
		return result;
	}
}
