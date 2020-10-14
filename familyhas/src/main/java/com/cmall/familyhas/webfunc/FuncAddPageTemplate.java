package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.xmassystem.support.PlusSupportWebTemplete;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddPageTemplate extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		// 550添加,新增时如果专题页已经有悬浮模板，则提示“专题页已经配置了悬浮模板”
		String template_number = mDataMap.get("zw_f_template_number");
		if(null != template_number && !"".equals(template_number)) {
			List<MDataMap> dataTemplateList = DbUp.upTable("fh_data_template").queryByWhere("template_number",template_number);
			if(null != dataTemplateList && dataTemplateList.size() > 0) {
				if("449747500024".equals(dataTemplateList.get(0).get("template_type"))) {
					// 如果是"悬浮模板",看该专题页是否已经配置了"悬浮模板”
					List<Map<String, Object>> pageTemplateList = DbUp.upTable("fh_page_template").dataSqlList("SELECT pt.*,dt.template_type FROM fh_page_template pt LEFT JOIN fh_data_template dt ON pt.template_number = dt.template_number " + 
							" WHERE pt.page_number = '"+ mDataMap.get("zw_f_page_number") +"' AND pt.dal_status = '1001' AND dt.template_type = '449747500024'", new MDataMap());
					if(null != pageTemplateList && pageTemplateList.size() > 0) {
						// 如果配置了"悬浮模板”,则提示“专题页已经配置了悬浮模板”
						mResult.setResultCode(-1);
						mResult.setResultMessage("专题页已经配置了悬浮模板");
						return mResult;
					}
				}
			}else {
				mResult.setResultCode(-1);
				mResult.setResultMessage("所选模板不存在,请重新选择");
				return mResult;
			}
		}
		
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		String sql = "select creator from fh_data_template where template_number=:template_number";
		Map<String,Object> reMap = DbUp.upTable("fh_data_template").dataSqlOne(sql, new MDataMap("template_number",mDataMap.get("zw_f_template_number")));
		mDataMap.put("zw_f_creator", reMap.get("creator").toString());
		mResult = super.funcDo(sOperateUid, mDataMap);
		
		new PlusSupportWebTemplete().onDataPageChanged(mDataMap.get("zw_f_page_number"));
		
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
