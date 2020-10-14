package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.ReturnGoodsNew;
import com.cmall.familyhas.service.ReturnGoodsService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 部分退货
 * @author jlin
 *
 */
public class FuncAddReturnGoodsNew extends FuncAdd {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		ReturnGoodsNew returnGoods = new ReturnGoodsNew();
		returnGoods.setOrder_code(mAddMaps.get("order_code"));
		returnGoods.setDescription(mAddMaps.get("description"));
//		returnGoods.setFreight(mAddMaps.get("freight"));
		returnGoods.setFreight("4497476900040002");
		returnGoods.setGoods_receipt(mAddMaps.get("goods_receipt"));
		returnGoods.setReturn_reason_code(mAddMaps.get("return_reason_code"));
		returnGoods.setTransport_people(mAddMaps.get("transport_people"));
		
		returnGoods.setAddress(mAddMaps.get("address"));
		returnGoods.setContacts(mAddMaps.get("contacts"));
		returnGoods.setMobile(mAddMaps.get("mobile"));
		returnGoods.setReceiver_area_code(mAddMaps.get("receiver_area_code"));
		returnGoods.setFlag_return_goods(mAddMaps.get("flag_return_goods"));
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", returnGoods.getOrder_code());
		
		String detail=mAddMaps.get("detail");
		if(StringUtils.isBlank(detail)){
			mResult.inErrorMessage(916422112);
			return mResult;
		}
		
		for (String de : detail.split(",")) {
			String des[]=de.split("_");
			int num=Integer.valueOf(des[1]);
			if(num>0){
				returnGoods.getDetailMap().put(des[0], num);
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
		
		mResult = new ReturnGoodsService().retGoodsForPart(returnGoods);
		return mResult;
	}
}
