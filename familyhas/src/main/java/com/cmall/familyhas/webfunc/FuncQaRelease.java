package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * @remark 客服与帮助-问题 发布/取消发布
 * @author 任宏斌
 * @date 2018年12月3日
 */
public class FuncQaRelease extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String qa_code = mDataMap.get("zw_f_qa_code");
		MDataMap mdata = DbUp.upTable("fh_common_problem_new").one("qa_code", qa_code);
		String qa_status = mdata.get("qa_status");

		MDataMap dataMap = new MDataMap();
		if ("449748270001".equals(qa_status)) {// 已发布就设为未发布
			dataMap.put("qa_status", "449748270002");
			dataMap.put("publish_time", "");
		} else if ("449748270002".equals(qa_status)) {// 未发布就设为已发布
			dataMap.put("qa_status", "449748270001");
			dataMap.put("publish_time", DateUtil.getSysDateTimeString());
		}

		dataMap.put("update_user", UserFactory.INSTANCE.create().getLoginName());
		dataMap.put("update_time", DateUtil.getSysDateTimeString());
		dataMap.put("qa_code", qa_code);
		DbUp.upTable("fh_common_problem_new").dataUpdate(dataMap, "qa_status,publish_time,update_user,update_time", "qa_code");
		return mResult;
	}

}
