package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cmall.familyhas.api.input.ApiGetProductInfoInput;
import com.cmall.familyhas.api.model.MediaProductInfo;
import com.cmall.familyhas.api.result.ApiGetProductInfoResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 批量查询商品信息
 * 供媒体站项目批量查询商品信息
 * @author fq
 *
 */
public class ApiGetProductInfo extends RootApiForManage<ApiGetProductInfoResult, ApiGetProductInfoInput>{

	@Override
	public ApiGetProductInfoResult Process(ApiGetProductInfoInput inputParam, MDataMap mRequestMap) {
		
		ApiGetProductInfoResult result = new ApiGetProductInfoResult();
		
		String productCodes = inputParam.getProductCodes();
		String[] productCodeArr = productCodes.split(",");
		
		PlusSupportProduct psp = new PlusSupportProduct();
		Integer isPurchase = 0;//不展示内购
		for(int j=0;j<productCodeArr.length;j++){
			
			MediaProductInfo pcModel = new MediaProductInfo();
			
			PlusModelProductInfo productInfo = new LoadProductInfo().topInitInfo(new PlusModelProductQuery(productCodeArr[j]));
			List<PlusModelProductSkuInfo> list = productInfo.getSkuList();
			List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
			for(PlusModelProductSkuInfo model : list ){
				PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(model.getSkuCode(),inputParam.getMemberCode(),"",isPurchase);
				if(skuInfo!=null){
					listPrice.add(skuInfo.getSellPrice());
				}
				
			}
			pcModel.setMainPicUrl(productInfo.getMainpicUrl());
			pcModel.setMarkPrice(productInfo.getMarketPrice());
			pcModel.setProductCode(productInfo.getProductCode());
			pcModel.setProductName(productInfo.getProductName());
			BigDecimal curentSellPrice = BigDecimal.ZERO;
			if(listPrice.size() > 0) {
				Collections.sort(listPrice);
				curentSellPrice = listPrice.get(0).setScale(2, RoundingMode.HALF_UP);
			} else {
				curentSellPrice = productInfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP);
			}
			pcModel.setSellprice(curentSellPrice);
			result.getProductInfoList().add(pcModel);
		}
		
		
		return result;
	}

}
