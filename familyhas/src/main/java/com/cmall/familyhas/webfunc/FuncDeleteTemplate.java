package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.xmassystem.support.PlusSupportWebTemplete;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDeleteTemplate extends RootFunc {

	/**
	 * 专题模版页面-列表(fh_data_page)、
	 * 专题模版商品-列表(fh_data_commodity)、
	 * 专题模版关联-列表(fh_page_template)、
	 * 专题模版-列表(fh_data_template) 
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
				updataMap.put("dal_status", "1000");//1000为删除，1001为未删除
				updataMap.put("uid", mDataMap.get("zw_f_uid"));
				String zw_f_uid = mDataMap.get("zw_f_uid");
				if(mPage.getPageTable().equals("fh_data_template")){
					String sSql = "SELECT t.template_number,p_t.dal_status,p_t.page_number FROM familyhas.fh_data_template t " +
							"JOIN familyhas.fh_page_template p_t  ON t.uid = '"+zw_f_uid+"' AND p_t.template_number = t.template_number " +
									"AND p_t.dal_status != '1000'  GROUP BY p_t.page_number";
					List<Map<String, Object>> list = DbUp.upTable("fh_page_template").dataSqlList(sSql, new MDataMap());
					if(list==null||list.size()==0){
						
						String sSql2 = "SELECT t.template_number FROM familyhas.fh_data_template t " +
								"JOIN familyhas.fh_data_commodity d_c  " +
								"ON t.uid = '"+zw_f_uid+"' AND d_c.template_number = t.template_number AND d_c.dal_status != '1000'";
						List<Map<String, Object>> commodityList = DbUp.upTable("fh_page_template").dataSqlList(sSql2, new MDataMap());
						if(commodityList.size() > 0) {
							Object object = commodityList.get(0).get("template_number");
							MDataMap comoditydataMap = new MDataMap();
							comoditydataMap.put("dal_status", "1000");
							comoditydataMap.put("template_number", String.valueOf(object));
							DbUp.upTable("fh_data_commodity").dataUpdate(comoditydataMap, "dal_status", "template_number");
						} 
						DbUp.upTable(mPage.getPageTable()).dataUpdate(updataMap, "dal_status", "uid");
						
					}else{
						mResult.setResultCode(0);
						int templete_size = list.size();
						String[] numArr = new String[templete_size];
						for (int i = 0; i < templete_size; i++) {
							Map<String, Object> templete = list.get(i);
							numArr[i] = templete.get("page_number").toString();
						}
						mResult.setResultMessage("该模板存在于页面编号 "+JSON.toJSONString(numArr)+" 页面中，不可删除");
					}		
				}else if (mPage.getPageTable().equals("fh_data_page")) {
					String sSql = "SELECT p.page_number , p_t.dal_status FROM familyhas.fh_data_page p " +
							" JOIN familyhas.fh_page_template p_t  ON p.uid = '"+zw_f_uid+"' AND p_t.page_number = p.page_number " +
									"AND p_t.dal_status != '1000'";
					List<Map<String, Object>> list = DbUp.upTable("fh_page_template").dataSqlList(sSql, new MDataMap());
					if(list==null||list.size()==0){
						//物理删除
						DbUp.upTable(mPage.getPageTable()).dataUpdate(updataMap, "dal_status", "uid");
					}else{
						mResult.setResultCode(0);
						mResult.setResultMessage("该页面中存在维护内容，不可删除");
					}	
				}else {
					if(mPage.getPageTable().equals("fh_page_template")) {
						MDataMap m = DbUp.upTable("fh_page_template").oneWhere("page_number", "", "", "uid", zw_f_uid);
						if(m != null) {
							new PlusSupportWebTemplete().onDataPageChanged(m.get("page_number"));
						}
					}
					
					if(mPage.getPageTable().equals("fh_data_commodity")) {
						MDataMap m = DbUp.upTable("fh_data_commodity").oneWhere("template_number", "", "", "uid", zw_f_uid);
						if(m != null) {
							new PlusSupportWebTemplete().onDataTemplateChanged(m.get("template_number"));
						}
					}
					
					//物理删除
					DbUp.upTable(mPage.getPageTable()).dataUpdate(updataMap, "dal_status", "uid");
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
