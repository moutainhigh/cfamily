package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditDLQShareInfo extends FuncEdit{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		//{"zw_f_ad_name":"1","zw_f_share_content":"3","zw_f_share_pic":"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/2722d/dbecbe1b794a440c80ea7315fb9b62b8.jpg","zw_f_share_title":"2","zw_f_uid":""}
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String zw_f_uid = mDataMap.get("zw_f_uid");
		if (null != zw_f_uid && !"".equals(zw_f_uid)) {
			
			result = super.funcDo(sOperateUid, mDataMap);
			String content = "在表《"+mPage.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(mDataMap);
			TempletePageLog.upLog(content);
			
		} else {//没有uid则为添加
			String zw_f_ad_name = mDataMap.get("zw_f_ad_name");
			String zw_f_share_content = mDataMap.get("zw_f_share_content");
			String zw_f_share_pic = mDataMap.get("zw_f_share_pic");
			String zw_f_share_title = mDataMap.get("zw_f_share_title");
			String zw_f_p_type = mDataMap.get("zw_f_p_type");
			String zw_f_tv_number = mDataMap.get("zw_f_tv_number");
			
			DbUp.upTable("fh_dlq_share").insert(
					"ad_name",zw_f_ad_name,
					"share_title",zw_f_share_title,
					"share_content",zw_f_share_content,
					"share_pic",zw_f_share_pic,
					"p_type",zw_f_p_type,
					"tv_number",zw_f_tv_number
					);
			//日志
			String content = "在表《"+mPage.getPageTable()+"》 添加一条记录:"+JSON.toJSONString(mDataMap);
			TempletePageLog.upLog(content);
			
		}
		return result;
	}
}
