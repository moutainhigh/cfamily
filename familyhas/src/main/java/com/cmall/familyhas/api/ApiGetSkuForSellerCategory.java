package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;

import com.cmall.familyhas.api.input.ApiGetSkuForSellerCategoryInput;
import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.model.SaleProduct;
import com.cmall.familyhas.api.result.ApiGetSkuForSellerCategoryResult;
import com.cmall.productcenter.model.PcProductPrice;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.PcProductinfoPage;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
/**
 * 按虚类查询商品
 * @author 李国杰
 *
 */
public class ApiGetSkuForSellerCategory extends RootApiForMember<ApiGetSkuForSellerCategoryResult,ApiGetSkuForSellerCategoryInput>  {

	public ApiGetSkuForSellerCategoryResult Process(ApiGetSkuForSellerCategoryInput inputParam,MDataMap mRequestMap) {
		ApiGetSkuForSellerCategoryResult result = new ApiGetSkuForSellerCategoryResult();
		
		// 设置相关信息
		if (result.upFlagTrue()) {
			
			String category_code = inputParam.getCategory();
			ProductService productService = BeansHelper.upBean("bean_com_cmall_productcenter_service_ProductService");
			
			PcProductinfoPage productsPage = productService.getProductInfoForC(category_code, inputParam.getSort(),
									bConfig("familyhas.app_code"),inputParam.getPaging().getOffset(),inputParam.getPaging().getLimit());
			
			List<PcProductinfo> subList = productsPage.getPcProducinfoList();
			for (PcProductinfo pcProductinfo : subList) {
				SaleProduct saleObj = new SaleProduct();
				String [] labls = pcProductinfo.getLabels().split(",");			//商品标签根据逗号分割
				if(labls!=null){
					for (int j = 0; j < labls.length; j++) {
						saleObj.getLabels().add(labls[j]);
					}
				}
				saleObj.setProductCode(pcProductinfo.getProductCode());
				saleObj.setProductName(pcProductinfo.getProdutName());
				saleObj.setMarketPrice(pcProductinfo.getMarketPrice());
				saleObj.setSellPrice(pcProductinfo.getMinSellPrice());
				saleObj.setMainpicUrl(pcProductinfo.getMainPicUrl());
				saleObj.setVipuserPrice(pcProductinfo.getMinSellPrice());
				saleObj.setCategoryCode(pcProductinfo.getCategory().getCategoryCode());
				
				
				//如果存在闪购活动则商品的最低售价为闪购价
				MDataMap skuCodeMap = DbUp.upTable("pc_skuinfo").oneWhere("sku_code","","","product_code" ,pcProductinfo.getProductCode());
				
				//判断商品的类型  0：普通商品 1：限购商品 2：试用商品
				int flagFlash = productService.getSkuActivityType(skuCodeMap.get("sku_code"), pcProductinfo.getSellerCode());
				
				if (flagFlash == 1) {
					PcProductPrice productPrice = productService.getSkuPrice(skuCodeMap.get("sku_code"), pcProductinfo.getSellerCode());
					//活动价和售价都设为闪购价格
					saleObj.setVipuserPrice(
							new BigDecimal(Double.valueOf(productPrice.getVipPrice())));
					saleObj.setSellPrice(
							new BigDecimal(Double.valueOf(productPrice.getVipPrice())));
				}
				result.getProducts().add(saleObj);
			}
			//分页信息
			PageResults pageResults = new PageResults();
			pageResults.setTotal(productsPage.getTotal());
			pageResults.setCount(productsPage.getCount());
			pageResults.setMore(productsPage.getMore());
			result.setPaged(pageResults);			//给结果集设置分页信息
		}
		return result;
}
}
