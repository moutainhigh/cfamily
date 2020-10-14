package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.ChangeGoods;
import com.cmall.familyhas.model.ChangeGoods.ChangeGoodsDetail;
import com.cmall.familyhas.service.ChangeGoodsService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加换货单
 * @author jlin
 *
 */
public class FuncAddChangeGoods extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String order_code=mAddMaps.get("order_code").trim();
		String detail=mAddMaps.get("detail");
		String exchange_reason=mAddMaps.get("exchange_reason");
		String transport_money=mAddMaps.get("transport_money");
		String contacts=mAddMaps.get("contacts");
		String mobile=mAddMaps.get("mobile");
		String address=mAddMaps.get("address");
		String description=mAddMaps.get("description");
		String transportPeople = mAddMaps.get("transport_people");
		
		ChangeGoods changeGoods = new ChangeGoods();
		changeGoods.setAddress(address);
		changeGoods.setContacts(contacts);
		changeGoods.setDescription(description);
		changeGoods.setExchange_reason(exchange_reason);
		changeGoods.setMobile(mobile);
		changeGoods.setOrder_code(order_code);
		changeGoods.setTransport_money(transport_money==null?"0":transport_money);
		changeGoods.setTransport_people(transportPeople);
		
		
		//验证订单详情
		if(StringUtils.isBlank(detail)){
			mResult.inErrorMessage(916411239, order_code);
			return mResult;
		}
		
		
		String details[]=detail.split(",");
		for (String ss : details) {
			String sku_code=ss.substring(0, ss.indexOf("_"));
			int sku_num=Integer.valueOf(ss.substring(ss.indexOf("_")+1));
			
			ChangeGoodsDetail changeGoodsDetail = new ChangeGoodsDetail();
			changeGoodsDetail.setCount(sku_num);
			changeGoodsDetail.setSku_code(sku_code);
			changeGoods.getGoodsDetailList().add(changeGoodsDetail);
		}
		
		ChangeGoodsService changeGoodsService = new ChangeGoodsService();
		mResult=changeGoodsService.addExchangegoods(changeGoods);
		
		return mResult;
	}
	
}
