package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddDLQPicture extends FuncAdd{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		mDataMap.put("zw_f_id_number", "1001");
		mDataMap.put("zw_f_delete_state", "1001");
		MWebResult funcDo = super.funcDo(sOperateUid, mDataMap);
		
		/*
		 * 添加日志
		 */
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String content = "在表《"+mPage.getPageTable()+"》 添加一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		
		return funcDo;
		
		
	}
}
