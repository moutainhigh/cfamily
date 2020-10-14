package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.InitMyCollectionInput;
import com.cmall.familyhas.api.result.InitMyCollectionResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForInitMyCollection extends RootApi< InitMyCollectionResult,  InitMyCollectionInput>{

	@Override
	public InitMyCollectionResult Process(InitMyCollectionInput inputParam, MDataMap mRequestMap) {
		InitMyCollectionResult result = new  InitMyCollectionResult();
		//此接口只允许成功调用一次
		String pwd = inputParam.getPwd();
		if(!"cptbtptp".equals(pwd)) {
			result.setResultCode(0);
			result.setResultMessage("密码错误");
			return result;
		};
		Map<String,Model1> map1 = new HashMap<>();
		String sql = "SELECT zid,product_code FROM fh_product_collection WHERE operate_type = '4497472000020001' AND sku_code = '';";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_product_collection").dataSqlList(sql, null);
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			for(Map<String, Object> map  :  dataSqlList) {
				String productCode = map.get("product_code")==null?"":map.get("product_code").toString();
				String zid = map.get("zid").toString();
				if(!"".equals(productCode)) {
					String skuCode = "";
					String skuPrice = "";
					if(null==map1.get(productCode)) {
						PlusModelSkuQuery query = new PlusModelSkuQuery();
						query.setCode(new PlusSupportProduct().upProductSku(productCode));
						query.setIsPurchase(1);
						PlusModelSkuResult plusModelSkuResult = new PlusSupportProduct().upSkuInfo(query);
						PlusModelSkuInfo plusModelSkuInfo = plusModelSkuResult.getSkus().get(0);
						skuCode = plusModelSkuInfo.getSkuCode();
						skuPrice= plusModelSkuInfo.getSellPrice().toString();
						Model1 model1 = new Model1();
						model1.setSkuCode(skuCode);
						model1.setSkuPrice(skuPrice);
						map1.put(productCode, model1);
					}else {
						Model1 model1 = map1.get(productCode);
						skuCode = model1.getSkuCode();
						skuPrice = model1.getSkuPrice();
					}
					
					if(!"".equals(skuCode)) {
						MDataMap mDataMap =  new MDataMap();
						mDataMap.put("zid", zid);
						mDataMap.put("sku_code",skuCode );
						mDataMap.put("sku_price", skuPrice);
						DbUp.upTable("fh_product_collection").dataUpdate(mDataMap, "sku_code,sku_price", "zid");
					}
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
