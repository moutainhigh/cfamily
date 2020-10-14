package com.cmall.familyhas.mtmanager.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.mtmanager.inputresult.ApiForStockPriceInput;
import com.cmall.familyhas.mtmanager.inputresult.ApiForStockPriceResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
/**
 *@author xiegj 
 * 
 * sku库存及价格
 */
public class ApiForStockPrice extends RootApiForManage<ApiForStockPriceResult, ApiForStockPriceInput> {

	public ApiForStockPriceResult Process(ApiForStockPriceInput inputParam, MDataMap mRequestMap) {
		ApiForStockPriceResult result = new ApiForStockPriceResult();
		if(StringUtils.isNotBlank(inputParam.getSkuCode())){
			MDataMap dataMap = DbUp.upTable("pc_skuinfo").one("sku_code",inputParam.getSkuCode());
			if(!dataMap.isEmpty()){
				PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(dataMap.get("product_code"));
				PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
				result.getStockPriceInfo().setFlag("Y".equals(dataMap.get("sale_yn"))?"1":"0");
				if(!"4497153900060002".equals(plusModelProductinfo.getProductStatus())){
					result.getStockPriceInfo().setFlag("0");
				}
				result.getStockPriceInfo().setPrice(Double.valueOf(dataMap.get("sell_price")));
				result.getStockPriceInfo().setSkuCode(inputParam.getSkuCode());
				result.getStockPriceInfo().setStock(new PlusSupportStock().upSalesStock(inputParam.getSkuCode()));
			}else {
				result.setResultCode(916401000);
				result.setResultMessage(bInfo(916401000));
			}
		}
		return result;
	}

}
