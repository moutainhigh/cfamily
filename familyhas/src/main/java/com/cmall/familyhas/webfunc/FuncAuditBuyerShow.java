package com.cmall.familyhas.webfunc;

import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 审核买家秀
 * @author lgx
 *
 */
public class FuncAuditBuyerShow extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("uid",mMaps.get("uid"));
		if(buyer_show==null) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("查不到该条数据,删除失败");
			return mResult;
		}
		String check_status = mMaps.get("check_status");
		if("已驳回".equals(check_status)) {
			check_status = "449748580001";
		}else {
			check_status = "449748580002";
		}
		String now_status = MapUtils.getString(buyer_show, "check_status");
		if(now_status.equals(check_status)) {
			// 已经审批到该状态
			String status = "";
			if("449748580002".equals(check_status)) {
				status = "驳回";
			}else {
				status = "通过";
			}
			mResult.setResultCode(1);
			mResult.setResultMessage("该买家秀已经是"+status+"状态");
			return mResult;
		}else {			
			MDataMap delDataMap = new MDataMap();
			delDataMap.put("uid", mMaps.get("uid"));
			delDataMap.put("check_status", check_status);
			DbUp.upTable("nc_buyer_show_info").dataUpdate(delDataMap, "check_status", "uid");
		}
		
		return mResult;
	}

}
