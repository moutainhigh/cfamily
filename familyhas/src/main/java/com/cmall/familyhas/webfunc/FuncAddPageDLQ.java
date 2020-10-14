package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddPageDLQ  extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		//页面编号
		mDataMap.put("zw_f_page_number", WebHelper.upCode("DLQ"));
		//创建人
        String createUserName = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_creater", createUserName);
		//创建时间
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		//发布状态初始值(0为未发布，1为发布)
//		mDataMap.put("zw_f_state", "0");
		/*
		 * 更改发布状态
		 */
		DbUp.upTable("fh_dlq_status").insert(
				"page_number",mDataMap.get("zw_f_page_number"),
				"tv_number",mDataMap.get("zw_f_tv_number"),
				"page_status","0",
				"page_sort",mDataMap.get("zw_f_page_sort"),
				"update_time",mDataMap.get("zw_f_create_time")
				);
		
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
