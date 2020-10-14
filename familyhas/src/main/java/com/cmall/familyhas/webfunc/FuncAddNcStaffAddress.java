package com.cmall.familyhas.webfunc;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 内部员工收货人地址添加
 * @author wz
 *
 */
public class FuncAddNcStaffAddress extends FuncAdd{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		mDataMap.put("zw_f_address_id", WebHelper.upCode("NGDZ"));
		mDataMap.put("zw_f_area_code", mDataMap.get("_street"));
		//如果添加的是默认地址，把其他所有的地址都写成非默认
		if("449746250001".equals(mDataMap.get("zw_f_address_default"))){
			DbUp.upTable("nc_staff_address").dataUpdate(new MDataMap("address_default","449746250002"), "address_default", "");
		}
		String price = mDataMap.get("zw_f_price");
		if (price.length() > 20) {
			mResult.setResultCode(916423200);
			mResult.setResultMessage(bInfo(916423200));
			return mResult;
		}
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		FuncAdd funcAdd = new FuncAdd();
		mResult = funcAdd.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
}
