package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;

import com.cmall.familyhas.api.input.ApiGetShopCarAndCustAmtForCrmInput;
import com.cmall.familyhas.api.result.ApiGetShopCarAndCustAmtForCrmResult;
import com.srnpr.xmasorder.model.ShoppingCartCache;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * crm系统获取购物车商品数和用户的积分、储值金、暂存款
 * @author lgx
 *
 */
public class ApiGetShopCarAndCustAmtForCrm extends RootApiForVersion<ApiGetShopCarAndCustAmtForCrmResult, ApiGetShopCarAndCustAmtForCrmInput> {

	public ApiGetShopCarAndCustAmtForCrmResult Process(ApiGetShopCarAndCustAmtForCrmInput inputParam, MDataMap mRequestMap) {
		ApiGetShopCarAndCustAmtForCrmResult result = new ApiGetShopCarAndCustAmtForCrmResult();
		
		String custId = inputParam.getCustId();
		String memberCode = inputParam.getMemberCode();
		if(!"".equals(custId) && !"".equals(memberCode)) {			
			// 根据客代号查询积分、暂存款、储值金
			PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
			GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
			if(plusModelCustAmt != null) {
				// 积分
				BigDecimal accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
				result.setPossAccmAmt(accm.intValue()>0?accm.intValue():0);
				// 暂存款
				result.setPossCrdtAmt(plusModelCustAmt.getPossCrdtAmt().intValue());
				// 储值金
				result.setPossPpcAmt(plusModelCustAmt.getPossPpcAmt().intValue());
			}else {
				result.setResultCode(-1);
				return result;
			}
			
			// 查询购物车商品 
			int shoppingCarNum = 0;
			String json = XmasKv.upFactory(EKvSchema.ShopCart).get(memberCode);
			ShoppingCartCacheInfo info = new GsonHelper().fromJson(json, new ShoppingCartCacheInfo());
			if(info != null) {
				List<ShoppingCartCache> caches = info.getCaches();
				if(caches != null && caches.size() > 0) {
					for (ShoppingCartCache shoppingCartCache : caches) {
						shoppingCarNum += shoppingCartCache.getSku_num();
					}
				}else {
					result.setResultCode(-1);
					return result;
				}
				result.setShoppingCarNum(shoppingCarNum);
			}else {
				result.setResultCode(-1);
				return result;
			}
		}else {
			result.setResultCode(-1);
		}
		
		return result;
	}

}
