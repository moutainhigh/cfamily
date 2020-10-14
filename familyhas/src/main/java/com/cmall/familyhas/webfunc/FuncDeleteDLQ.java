package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDeleteDLQ extends RootFunc {

	/**
	 * 大陆桥专题内容   -列表(fh_dlq_content)、
	 * 大陆桥轮播广告图-列表(fh_dlq_picture)、
	 * 以上共用此删除功能
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if (mResult.upFlagTrue()) {
			if (mDelMaps.containsKey("uid")) {
				MDataMap mThisMap=null;
				// 循环所有结构
				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {
						if(mThisMap==null)
						{
							mThisMap=DbUp.upTable(mPage.getPageTable()).one("uid",mDelMaps.get("uid"));
						}
						WebUp.upComponent(mField.getSourceCode()).inDelete(mField,mThisMap);
					}
				}
				MDataMap updataMap = new MDataMap();
				updataMap.put("delete_state", "1000");//1000为删除，1001为未删除
				updataMap.put("uid", mDataMap.get("zw_f_uid"));
				if(mPage.getPageTable().equals("fh_dlq_content")){
					//物理删除
					DbUp.upTable(mPage.getPageTable()).dataUpdate(updataMap, "delete_state", "uid");
				}else if (mPage.getPageTable().equals("fh_dlq_picture")) {
					//物理删除
					DbUp.upTable(mPage.getPageTable()).dataUpdate(updataMap, "delete_state", "uid");
				}
				//记录日志
				String content = "在表《"+mPage.getPageTable()+"》 删除一条记录:"+JSON.toJSONString(mDataMap);
				TempletePageLog.upLog(content);
			}
		}
		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}
	
	
}
