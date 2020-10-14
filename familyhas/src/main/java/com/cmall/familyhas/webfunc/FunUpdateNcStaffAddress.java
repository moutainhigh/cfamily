package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 修改内部员工收货人地址信息
 * @author wz
 *
 */
public class FunUpdateNcStaffAddress extends FuncEdit{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
//		mDataMap.put("zw_f_area_code", mDataMap.get("_area"));
		mDataMap.put("zw_f_area_code", mDataMap.get("_street"));
		
		String address_default = mDataMap.get("zw_f_address_default");
		String price = mDataMap.get("zw_f_price");
		String uid = mDataMap.get("zw_f_uid");
		
		if (price.length() > 20) {
			mResult.setResultCode(916423200);
			mResult.setResultMessage(bInfo(916423200));
			return mResult;
		}
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		FuncEdit funcEdit = new FuncEdit();
		mResult = funcEdit.funcDo(sOperateUid, mDataMap);
		//默认地址
		if ("449746250001".equals(address_default) && mResult.getResultCode() == 1) {
			String sWhere = " address_default = '449746250001' and uid <> '"+uid+"'";
			MDataMap defaultData = DbUp.upTable("nc_staff_address").oneWhere("uid","",sWhere);
			if (null!=defaultData ) {
				MDataMap mUpdateMap = new MDataMap();
				mUpdateMap.put("uid", uid);		//保留的默认地址的uid
				mUpdateMap.put("update_time", DateUtil.getSysDateTimeString());
				String sSql = "update nc_staff_address set update_time=:update_time , address_default = '449746250002' where uid <> :uid and address_default='449746250001'";
				DbUp.upTable("nc_staff_address").dataExec(sSql, mUpdateMap);
				
				mResult.setResultCode(916423201);
				mResult.setResultMessage(bInfo(916423201));
			}
		}
		return mResult;
	}
}
