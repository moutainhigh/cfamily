package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.CommentCodeCf;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class commentPictureStatusCf extends RootFunc{

	
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		mResult.setResultCode(1);
		
		String tplUid = mDataMap.get("zw_f_uid");
		MDataMap dataMap = new MDataMap();
		dataMap.put("uid", tplUid);
		String isDisable = mDataMap.get("zw_f_isDisable");
		if(CommentCodeCf.pictureAccept.equals(isDisable)){
			mResult.setResultCode(934205104);
			mResult.setResultMessage("状态已为图片通过！");
		}else{
			dataMap.put("pic_status", CommentCodeCf.pictureAccept	);
			DbUp.upTable("nc_order_evaluation").dataUpdate(dataMap, "pic_status", "uid");
		}
		return mResult;
	}

	
}
