package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 删除内部员工收货人地址
 * @author wz
 *
 */
public class FuncDeleteNcStaffAddress extends FuncDelete{
	/**
	 * 删除
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		MDataMap map =  DbUp.upTable("nc_staff_address").one("uid",mDataMap.get("zw_f_uid"));
		if("449746250001".equals(map.get("address_default"))){
			mResult.setResultCode(916423202);
			mResult.setResultMessage(bInfo(916423202));
		}else{
			mResult = super.funcDo(sOperateUid, mDataMap);
		}
		return mResult;
	}
	
}
