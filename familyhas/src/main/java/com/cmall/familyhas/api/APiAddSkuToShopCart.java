package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.APiAddSkuToShopCartInput;
import com.cmall.familyhas.api.result.APiAddSkuToShopCartResult;
import com.cmall.familyhas.model.FamilyShopCart;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.familyhas.model.GoodsInfoForQuery;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.membercenter.memberdo.MemberConst;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 购物车添加商品接口
 * 
 * @author xiegj
 *
 */
public class APiAddSkuToShopCart extends RootApiForToken<APiAddSkuToShopCartResult, APiAddSkuToShopCartInput> {

	public APiAddSkuToShopCartResult Process(APiAddSkuToShopCartInput inputParam,
			MDataMap mRequestMap) {
		APiAddSkuToShopCartResult result = new APiAddSkuToShopCartResult();
		inputParam.setBuyer_code(getUserCode());
		List<GoodsInfoForAdd> inList = inputParam.getGoodsList();
		if(inList!=null&&!inList.isEmpty()){
			List<FamilyShopCart> li = new ArrayList<FamilyShopCart>();
			for (int i = 0; i < inList.size(); i++) {
				ShopCartService ss = new ShopCartService();
				FamilyShopCart cart = new FamilyShopCart();
				cart.setSku_code(ss.getSkuCodeForValue(inList.get(i).getProduct_code(), inList.get(i).getSku_code()));
				cart.setArea_code("");
				cart.setProduct_code(inList.get(i).getProduct_code());
				cart.setSku_num(Integer.valueOf(inList.get(i).getSku_num()));
				cart.setBuyer_code(inputParam.getBuyer_code());
				cart.setChooseFlag(inList.get(i).getChooseFlag());
				if(cart.getSku_code()==null||"".equals(cart.getSku_code())){
					result.setResultCode(916401102);
					result.setResultMessage(bInfo(916401102));
					return result;
				}
				li.add(cart);
			}
			ShopCartService service = new ShopCartService();
			RootResult error = service.addSkuToShopCart(li);
			if(error!=null&&!"".equals(error)){
				result.setResultCode(error.getResultCode());
				result.setResultMessage(error.getResultMessage());
			}
		}
		ShopCartService service = new ShopCartService();
		Map<String, Object> reMap= service.getSkuShopCart(getManageCode(),inputParam.getBuyer_code());
		List<GoodsInfoForQuery> list = (List<GoodsInfoForQuery>)reMap.get("list");
		int count = DbUp.upTable("oc_shopCart").count("buyer_code",inputParam.getBuyer_code());
		result.setDisableSku(count-list.size());
		
		if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){
			
			result.setSalesAdv(bConfig("familyhas.sale_Information"));
			
			result.setPostal_price(Integer.valueOf(bConfig("familyhas.full_amount")));
			
		}else {
			
			result.setSalesAdv(bConfig("familyhas.first_title"));
		}
		
		result.setAcount_num(Integer.valueOf(reMap.get("AccountAll").toString()));
		result.setDisable_account_num(Integer.valueOf(reMap.get("disableAccount").toString()));
		result.setShoppingCartList(list);
		return result;
	}

}
