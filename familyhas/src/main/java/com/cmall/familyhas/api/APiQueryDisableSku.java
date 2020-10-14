package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.APiQueryDisableSkuInput;
import com.cmall.familyhas.api.result.APiQueryDisableSkuResult;
import com.cmall.familyhas.model.GoodsInfoForQueryDisable;
import com.cmall.familyhas.service.ShopCartService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 查询购物车失效商品列表接口
 * 
 * @author xiegj
 *
 */
public class APiQueryDisableSku extends RootApiForToken<APiQueryDisableSkuResult, APiQueryDisableSkuInput> {

	public APiQueryDisableSkuResult Process(APiQueryDisableSkuInput inputParam,
			MDataMap mRequestMap) {
		APiQueryDisableSkuResult result = new APiQueryDisableSkuResult();
		inputParam.setBuyer_code(getUserCode());
		ShopCartService service = new ShopCartService();
		Map<String, Object> reMap = service.getDisableSkuShopCart(getManageCode(),inputParam.getBuyer_code());
		List<GoodsInfoForQueryDisable> list = (List<GoodsInfoForQueryDisable>)reMap.get("list");
		String error = (String)reMap.get("error");
		result.setLoseEfficacyList(list);
		if(error!=null&&!"".equals(error)){
			result.setResultCode(2);
			result.setResultMessage(error);
		}
		return result;
	}

}
