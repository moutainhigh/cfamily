package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 更新退货原因状态
 * @author zmm
 *
 */
public class FuncAddReturnReasonUpdateStatus extends RootFunc{
	/**
	 * 449747660005可用
	 * 449747660006禁用
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid=mSubMap.get("uid");
		Map<String, String> statusmap = DbUp.upTable("oc_return_goods_reason").oneWhere("uid,status", "","uid=:uid", "uid", uid);
		String status = statusmap.get("status").toString();
		if(status.equals("449747660005")){
			DbUp.upTable("oc_return_goods_reason").dataUpdate(new MDataMap("uid",uid,"status","449747660006"),"status", "uid");
		}else{
			DbUp.upTable("oc_return_goods_reason").dataUpdate(new MDataMap("uid",uid,"status","449747660005"),"status", "uid");
		}
		mResult.setResultMessage("操作成功");
		return mResult;
	}

}
