package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除"我的"图片
 * 
 * @author 李国杰
 * 
 */
public class FuncDeleteNewsNotification extends RootFunc {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		if (mResult.upFlagTrue()) {
			if (mDelMaps.containsKey("uid")) {
				
				MDataMap dataMap = new MDataMap();
				dataMap.put("uid",mDelMaps.get("uid"));
				Object proclamation_code = DbUp.upTable("fh_news_notification").dataGet("proclamation_code", "uid =:uid", dataMap);
				
				MDataMap mThisMap=null;
				

				// 循环所有结构
				for (MWebField mField : mPage.getPageFields()) {

					if (mField.getFieldTypeAid().equals("104005003")) {
						
						if(mThisMap==null)
						{
							mThisMap=DbUp.upTable(mPage.getPageTable()).one("uid",mDelMaps.get("uid"));
						}
						
						
						WebUp.upComponent(mField.getSourceCode()).inDelete(mField,
								mThisMap);
						
					}
				}
				  
				
				String uid = mDelMaps.get("uid");
				String sql = "select news_code,notice_type from fh_news_notification where uid = '"+uid+"'";
				List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_news_notification").dataSqlList(sql, null);
				if(null!=dataSqlList&&dataSqlList.size()>0) {
					Map<String, Object> map = dataSqlList.get(0);
					String notice_type = map.get("notice_type").toString();
					if("4497471600370002".equals(notice_type)) {
						String news_code = map.get("news_code").toString();
						DbUp.upTable("fh_news_punish_detail").delete("news_code",
								news_code);
					}
				}
				

				DbUp.upTable(mPage.getPageTable()).delete("uid",
						mDelMaps.get("uid"));
				
				
				DbUp.upTable("fh_proclamation_manage").delete("proclamation_code",proclamation_code.toString());
			}
		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}

		return mResult;
	}
}
