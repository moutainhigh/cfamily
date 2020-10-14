package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.service.CollageService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 客服拼团。
 * @author Angel Joy
 * @date 2020年4月2日 下午2:39:23
 * @version 
 * @desc TODO
 */
public class FuncCollageComplateByKefu extends RootFunc {

	
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String order_code = mDataMap.get("zw_f_uid");
		MDataMap collageItemInfo = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",order_code);
		if(collageItemInfo == null || collageItemInfo.isEmpty()) {
			result.setResultCode(0);
			result.setResultMessage("非拼团单，请联系技术");
			return result;
		}
		String collage_code = collageItemInfo.get("collage_code");
		MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collage_code);
		if(collageInfo == null || collageInfo.isEmpty()) {
			result.setResultCode(0);
			result.setResultMessage("非拼团单，请联系技术");
			return result;
		}
		String collage_status = collageInfo.get("collage_status");
		if(!"449748300001".equals(collage_status)) {
			result.setResultCode(0);
			result.setResultMessage("该团非拼团中，请刷新页面确认拼团状态，如有问题请联系技术人员排查");
			return result;
		}
		CollageService cs = new CollageService();
		cs.robotComplateCollage(collage_code);
		result.setResultCode(1);
		result.setResultMessage("操作成功！");
		return result;
	}
	
}
