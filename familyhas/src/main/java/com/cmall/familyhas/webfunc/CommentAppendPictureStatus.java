package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 追评的图片审核
 */
public class CommentAppendPictureStatus extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		mResult.setResultCode(1);

		String zid = mDataMap.get("zw_f_zid");
		MDataMap dataMap = new MDataMap();
		dataMap.put("zid", zid);
		String status = mDataMap.get("zw_f_status");
		if (StringUtils.isNotBlank(status)) {
			dataMap.put("pic_status", status);
			DbUp.upTable("nc_order_evaluation_append").dataUpdate(dataMap, "pic_status", "zid");
		}
		return mResult;
	}

}
