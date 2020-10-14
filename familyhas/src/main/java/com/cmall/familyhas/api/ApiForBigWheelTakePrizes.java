package com.cmall.familyhas.api;

import java.util.Map;

import com.cmall.familyhas.api.input.ApiForBigWheelTakePrizesInput;
import com.cmall.familyhas.api.result.ApiForBigWheelTakePrizesResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 大转盘-领取奖品接口
 * @author lgx
 *
 */
public class ApiForBigWheelTakePrizes extends RootApiForVersion<ApiForBigWheelTakePrizesResult, ApiForBigWheelTakePrizesInput> {

	
	public ApiForBigWheelTakePrizesResult Process(ApiForBigWheelTakePrizesInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelTakePrizesResult result = new ApiForBigWheelTakePrizesResult();
		
		String jpCodeSeq = inputParam.getJpCodeSeq();
		String addressId = inputParam.getAddressId();
		
		String sql1 = "SELECT * FROM nc_address WHERE address_id = '"+addressId+"'";
		Map<String, Object> addressMap = DbUp.upTable("nc_address").dataSqlOne(sql1, new MDataMap());
		if(addressMap != null) {
			MDataMap mDataMap = new MDataMap();
			String address_name = addressMap.get("address_name").toString();
			String address_mobile = addressMap.get("address_mobile").toString();
			String address_province = addressMap.get("address_province").toString();
			String address_street = addressMap.get("address_street").toString();
			
			mDataMap.put("jp_code_seq", jpCodeSeq);
			mDataMap.put("lj_yn", "Y");
			mDataMap.put("rcv_address", "收货人:"+address_name+";收货电话:"+address_mobile+";收货地址:"+address_province+address_street);
			int dataUpdate = DbUp.upTable("lc_huodong_event_jl").dataUpdate(mDataMap , "lj_yn,rcv_address", "jp_code_seq");
			if(dataUpdate > 0) {
				
			}else {
				result.setResultCode(-1);
				result.setResultMessage("奖品领取失败!");
			}
		}else {
			result.setResultCode(-1);
			result.setResultMessage("收货地址信息获取失败!");
		}
		
		return result;
	}

}
