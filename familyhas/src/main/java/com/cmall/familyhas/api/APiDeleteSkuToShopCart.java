package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.APiDeleteSkuToShopCartInput;
import com.cmall.familyhas.api.result.APiDeleteSkuToShopCartResult;
import com.cmall.familyhas.service.ShopCartService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 购物车删除商品接口
 * 
 * @author xiegj
 *
 */
public class APiDeleteSkuToShopCart extends RootApiForToken<APiDeleteSkuToShopCartResult, APiDeleteSkuToShopCartInput> {

	public APiDeleteSkuToShopCartResult Process(APiDeleteSkuToShopCartInput inputParam,
			MDataMap mRequestMap) {
		APiDeleteSkuToShopCartResult result = new APiDeleteSkuToShopCartResult();
		inputParam.setBuyer_code(getUserCode());
		ShopCartService service = new ShopCartService();
		List<String> skuCodes = inputParam.getSkuCodes();
		StringBuffer error = new StringBuffer();
		if(!skuCodes.isEmpty()){
			for (int i = 0; i < skuCodes.size(); i++) {
				String skuCode = skuCodes.get(i);
				boolean flag = service.deleteSkuForShopCart(inputParam.getBuyer_code(), skuCode);
				if(!flag){
					error.append(bInfo(916401108, skuCode));
				}
			}
		}
		if(error.toString()!=null&&!"".equals(error.toString())){
			result.setResultCode(2);
			result.setResultMessage(error.toString());
		}
		return result;
	}
}
