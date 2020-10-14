package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.APiQueryShopCartInput;
import com.cmall.familyhas.api.result.APiQueryShopCartResult;
import com.cmall.familyhas.model.Activity;
import com.cmall.familyhas.model.GoodsInfoForQuery;
import com.cmall.familyhas.service.ShopCartService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 查询购物车接口
 * 
 * @author xiegj
 *
 */
public class APiQueryShopCart extends RootApiForToken<APiQueryShopCartResult, APiQueryShopCartInput> {

	public APiQueryShopCartResult Process(APiQueryShopCartInput inputParam,
			MDataMap mRequestMap) {
		APiQueryShopCartResult result = new APiQueryShopCartResult();
		inputParam.setBuyer_code(getUserCode());
		ShopCartService service = new ShopCartService();
		Map<String, Object> reMap= service.getSkuShopCart(getManageCode(),inputParam.getBuyer_code());
		List<GoodsInfoForQuery> list = (List<GoodsInfoForQuery>)reMap.get("list");
		String error = (String)reMap.get("error");
		int count = DbUp.upTable("oc_shopCart").count("buyer_code",inputParam.getBuyer_code());
		result.setActivityList(new ArrayList<Activity>());
		result.setDisableSku(count-list.size());
		result.setSalesAdv(bConfig("familyhas.first_title"));
		result.setAcount_num(Integer.valueOf(reMap.get("AccountAll").toString()));
		result.setDisable_account_num(Integer.valueOf(reMap.get("disableAccount").toString()));
		result.setShoppingCartList(list);
		if(error!=null&&!"".equals(error)){
			result.setResultCode(2);
			result.setResultMessage(error);
		}
		return result;
	}

}
