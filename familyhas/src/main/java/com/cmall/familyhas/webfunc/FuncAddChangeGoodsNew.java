package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.ChangeGoodsNew;
import com.cmall.familyhas.service.ChangeGoodsService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加换货单
 * @author jlin
 *
 */
public class FuncAddChangeGoodsNew extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		ChangeGoodsNew changeGoods = new ChangeGoodsNew();
		
		changeGoods.setOrder_code(mAddMaps.get("order_code"));
		changeGoods.setDescription(mAddMaps.get("description"));
		changeGoods.setMobile(mAddMaps.get("buyer_mobile"));
		changeGoods.setTransport_people(mAddMaps.get("transport_people"));
		changeGoods.setAfter_sale_person(mAddMaps.get("after_sale_person"));
		changeGoods.setAfter_sale_mobile(mAddMaps.get("after_sale_mobile"));
		changeGoods.setAfter_sale_address(mAddMaps.get("after_sale_address"));
		changeGoods.setAfter_sale_postcode(mAddMaps.get("after_sale_postcode"));
		changeGoods.setAsale_reason(mAddMaps.get("asale_reason"));
		changeGoods.setGoods_receipt(mAddMaps.get("goods_receipt"));
		changeGoods.setFreight(mAddMaps.get("freight"));
		changeGoods.setFlag_return_goods(mAddMaps.get("flag_return_goods"));
		
		String detail=mAddMaps.get("detail");
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", changeGoods.getOrder_code());
		
		// 多货主订单不支持换货
		if("4497471600430002".equals(orderInfo.get("delivery_store_type"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("多货主订单不支持换货");
			return mResult;
		}
		
		//验证订单详情
		if(StringUtils.isBlank(detail)){
			mResult.inErrorMessage(916411239, changeGoods.getOrder_code());
			return mResult;
		}
		
		for (String de : detail.split(",")) {
			String des[]=de.split("_");
			int num=Integer.valueOf(des[1]);
			if(num>0){
				changeGoods.getDetailMap().put(des[0], num);
			}
			
			// 京东商品目前只允许一次申请一件售后
			if(Constants.SMALL_SELLER_CODE_JD.equals(orderInfo.get("small_seller_code"))) {
				if(num > 1) {
					mResult.setResultCode(0);
					mResult.setResultMessage("京东商品每次只支持申请1件商品售后");
					return mResult;
				}
			}
		}
		
		ChangeGoodsService changeGoodsService = new ChangeGoodsService();
		mResult=changeGoodsService.addExchangegoodsPart(changeGoods);
		
		return mResult;
	}
	
}
