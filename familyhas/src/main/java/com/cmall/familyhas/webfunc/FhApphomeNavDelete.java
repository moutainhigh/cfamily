package com.cmall.familyhas.webfunc;

import com.cmall.productcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @description: 删除导航信息
 *
 * @author Yangcl
 * @date 2017年5月3日 下午2:10:33 
 * @version 1.0.0
 */
public class FhApphomeNavDelete extends FuncEdit {  
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String update_user = UserFactory.INSTANCE.create().getLoginName();
		
		MDataMap um = new MDataMap();
		um.put("is_delete", "01"); 
		um.put("update_user", update_user);
		um.put("update_time", DateUtil.getSysDateTimeString());
		um.put("uid", mDataMap.get("zw_f_uid")); 
		DbUp.upTable("fh_apphome_nav").dataUpdate(um, "is_delete,update_user,update_time", "uid");
		
		return mResult;
	}
}








