package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.model.ReturnGoods;
import com.cmall.familyhas.service.ReturnGoodsService;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加退货单
 * 
 * @author zmm
 *
 */
public class FuncAddReturnGoods extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String order_code = mAddMaps.get("order_code").trim();
		String return_reason = mAddMaps.get("return_reason");
		String contacts = mAddMaps.get("contacts");
		String transport_money = mAddMaps.get("transport_money");
		String mobile = mAddMaps.get("mobile");
		String address = mAddMaps.get("address");
		String description = mAddMaps.get("description");
		String transportPeople = mAddMaps.get("transport_people");
		ReturnGoods returnGoods = new ReturnGoods();
		returnGoods.setAddress(address);
		returnGoods.setContacts(contacts);
		returnGoods.setReturn_reason(return_reason);
		returnGoods.setMobile(mobile);
		returnGoods.setOrder_code(order_code);
		returnGoods.setTransport_money(transport_money);
		returnGoods.setDescription(description);
		returnGoods.setTransport_people(transportPeople);
		ReturnGoodsService returnGoodsService = new ReturnGoodsService();
		mResult=returnGoodsService.addReturngoods(returnGoods);

		return mResult;

	}
}
