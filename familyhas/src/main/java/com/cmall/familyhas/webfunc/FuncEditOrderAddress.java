package com.cmall.familyhas.webfunc;


import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.service.OrderEditService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改订单的收货地址
 */
public class FuncEditOrderAddress extends FuncAdd{
	
	static OrderEditService editService = new OrderEditService();

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String orderCode = StringUtils.trimToEmpty(mDataMap.get("orderCode"));
		String mobilephone = StringUtils.trimToEmpty(mDataMap.get("mobilephone"));
		String receive_person = StringUtils.trimToEmpty(mDataMap.get("receive_person"));
		String address = StringUtils.trimToEmpty(mDataMap.get("address"));
		String areaCode = StringUtils.trimToEmpty(mDataMap.get("town"));
		String idcardNumber = StringUtils.trimToEmpty(mDataMap.get("auth_idcard_number")).toUpperCase();
		RootResult rs = editService.checkOrderEditLimit(orderCode);
		if(rs.getResultCode() != 1) {
			mResult.setResultCode(0);
			mResult.setResultMessage(rs.getResultMessage());
			return mResult;
		}
		
		if(StringUtils.isBlank(mobilephone)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("收货人手机号不能为空");
			return mResult;
		}
		
		if(!mobilephone.matches("1\\d{10}")) {
			mResult.setResultCode(0);
			mResult.setResultMessage("收货人手机号格式错误");
			return mResult;
		}
		
		if(StringUtils.isBlank(receive_person)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("收货人不能为空");
			return mResult;
		}
		
		if(StringUtils.isBlank(address)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("详细地址不能为空");
			return mResult;
		}
		
		if(StringUtils.isBlank(areaCode)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("所在地区4级地址必须选择");
			return mResult;
		}
		
		if(mDataMap.containsKey("auth_idcard_number") && !idcardNumber.matches("\\d{17}[0-9X]") && !idcardNumber.matches("\\d{1}[0-9X]")) {
			mResult.setResultCode(0);
			mResult.setResultMessage("身份证号码错误");
			return mResult;
		}
		
		// 原收货地址
		MDataMap adrNewMap = DbUp.upTable("oc_orderadress").one("order_code", orderCode);
		// 判断收货地址是否有修改
		boolean needUpdate = false;
		if(!mobilephone.equals(adrNewMap.get("mobilephone"))
				|| !receive_person.equals(adrNewMap.get("receive_person"))
				|| !address.equals(adrNewMap.get("address"))
				|| !areaCode.equals(adrNewMap.get("area_code"))
				|| (mDataMap.containsKey("auth_idcard_number") && !idcardNumber.equals(adrNewMap.get("auth_idcard_number")))) {
			needUpdate = true;
			adrNewMap.put("mobilephone", mobilephone);
			adrNewMap.put("address", address);
			adrNewMap.put("receive_person", receive_person);
			adrNewMap.put("area_code", areaCode);
			
			if(mDataMap.containsKey("auth_idcard_number")) {
				adrNewMap.put("auth_idcard_number", idcardNumber);
			}
		}
		
		if(needUpdate) {
			RootResult updateRes = editService.updateOrderAddress(adrNewMap);
			mResult.inOtherResult(updateRes);
		}
		
		return mResult;
	}
}
