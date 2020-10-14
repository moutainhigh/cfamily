package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.APiUpdateJJGProductForShopCartInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 更新换购商品到购物车
 *
 */
public class APiUpdateJJGProductForShopCart extends RootApiForVersion<RootResult, APiUpdateJJGProductForShopCartInput> {

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	public RootResult Process(APiUpdateJJGProductForShopCartInput inputParam,
			MDataMap mRequestMap) {
		RootResult result = new RootResult();
		ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
		String jjgEventCode = inputParam.getJjgEventCode();
		String skuCodes = inputParam.getSkuCodes();
		String sumRepurchaseMoney = inputParam.getSumRepurchaseMoney();
		BigDecimal sumMoney = BigDecimal.valueOf(Double.valueOf(sumRepurchaseMoney));
        result = this.validateData(skuCodes,sumMoney,jjgEventCode);
        if(0==result.getResultCode()) {return result;}
		List<ShoppingCartGoodsInfoForAdd> goodsList = new ArrayList<ShoppingCartGoodsInfoForAdd>();
		ShoppingCartGoodsInfoForAdd shoppingCartGoodsInfoForAdd = new ShoppingCartGoodsInfoForAdd();
		shoppingCartGoodsInfoForAdd.setJjgEventCode(jjgEventCode);
		//此处是存储加价购活动下的多个sku编号，用","隔开的字符串，而正常的购物车商品的添加存放的是一个sku编号
		shoppingCartGoodsInfoForAdd.setSku_code(skuCodes);
		goodsList.add(shoppingCartGoodsInfoForAdd);
		serviceForCache.saveShopCart(goodsList, getOauthInfo().getUserCode());
		return result;
	}

	private RootResult validateData(String skuCodes,BigDecimal sumMoney,String jjgEventCode) {
		RootResult result = new RootResult();
		//做总价格的校验
		if(StringUtils.isNotBlank(skuCodes)) {
			String[] split = StringUtils.split(skuCodes, ",");
			List<String> asList = Arrays.asList(split);
			//添加换购总数校验
			Map<String, Object> dataSqlOne = DbUp.upTable("sc_event_info").dataSqlOne("select * from sc_event_info where event_code=:event_code and flag_enable=1 and begin_time<now() and end_time>now() ", new MDataMap("event_code",jjgEventCode));
			if(dataSqlOne!=null&&dataSqlOne.size()>0) {
				if(asList.size()>Integer.valueOf(dataSqlOne.get("repurchase_num").toString())) {
					result.setResultMessage("最多换购"+dataSqlOne.get("repurchase_num").toString()+"件！");
					result.setResultCode(0);
					return result;
				}
				String join = StringUtils.join(asList, "','");
				join = "('"+join+"')";
				String sSql="select * from sc_event_item_product where event_code='"+jjgEventCode+"' and flag_enable=1 and sku_code in "+join;
				List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_event_item_product").dataSqlList(sSql, null);
				if(dataSqlList!=null&&dataSqlList.size()>0) {
					BigDecimal sum = new BigDecimal(0);
					for (Map<String, Object> map : dataSqlList) {
						PlusModelSkuQuery query = new PlusModelSkuQuery();
						query.setCode(map.get("sku_code").toString());
						PlusModelSkuInfo upInfoByCode = new LoadSkuInfo().upInfoByCode(query);
						long upEventItemSkuStock = new PlusSupportEvent().upEventItemSkuStock(map.get("item_code").toString());
						if("Y".equals(upInfoByCode.getSaleYn())) {
							if(upInfoByCode.getBuyStatus()!=6) {
								long upSkuStockBySkuCode = new PlusSupportStock().upSkuStockBySkuCode(map.get("sku_code").toString());
								if(Math.min(upSkuStockBySkuCode, upEventItemSkuStock)>0) {
									sum = sum.add(new BigDecimal(Double.valueOf(map.get("favorable_price").toString())));
								}else {
									result.setResultMessage("您所选的商品:["+map.get("sku_name")+"]无货,请重新选择！");
									result.setResultCode(0);
									return result;
								}
							}else {
								result.setResultMessage("您所选的商品:["+map.get("sku_name")+"]无货,请重新选择！");
								result.setResultCode(0);
								return result;
							}
						}else {
							result.setResultMessage("您所选的商品:["+map.get("sku_name")+"无货,请重新选择！");
							result.setResultCode(0);
							return result;
						}	

					}
					sum =MoneyHelper.roundHalfUp(sum);
					if(sum.compareTo(sumMoney)!=0) {
						result.setResultMessage("换购总价校验失败");
						result.setResultCode(0);
						return result;
					}
				}
			}else {
				result.setResultMessage("活动结束，请重新选择！");
				result.setResultCode(0);
				return result;
			}
		}
		return result;
	}
}
