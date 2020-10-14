package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddDataPage  extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		/*获取当前登录人*/
		String create_user = UserFactory.INSTANCE.create().getRealName();
		mDataMap.put("zw_f_page_number", WebHelper.upCode("PNM"));
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		mResult = super.funcDo(sOperateUid, mDataMap);
		/*
		 * 添加日志
		 */
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String content = "在表《"+mPage.getPageTable()+"》 添加一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		
		return mResult;
	}

}
