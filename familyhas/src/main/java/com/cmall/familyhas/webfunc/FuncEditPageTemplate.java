package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.xmassystem.support.PlusSupportWebTemplete;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditPageTemplate extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		// 550添加,当是"拼团模板"时,活动编号非空，但不是拼团活动，提示“请绑定拼团活动”
		String event_code = mDataMap.get("zw_f_event_code");
		if(null != event_code && !"".equals(event_code)) {
			List<MDataMap> eventList = DbUp.upTable("sc_event_info").queryByWhere("event_code",event_code,"event_type_code","4497472600010024");
			if(null != eventList && eventList.size() > 0) {
				
			}else {
				mResult.setResultCode(-1);
				mResult.setResultMessage("请绑定拼团活动");
				return mResult;
			}
		}
		
		// 550添加,新增时如果专题页已经有悬浮模板，则提示“专题页已经配置了悬浮模板”
		String template_number = mDataMap.get("zw_f_template_number");
		if(null != template_number && !"".equals(template_number)) {
			List<MDataMap> dataTemplateList = DbUp.upTable("fh_data_template").queryByWhere("template_number",template_number);
			if(null != dataTemplateList && dataTemplateList.size() > 0) {
				if("449747500024".equals(dataTemplateList.get(0).get("template_type"))) {
					// 如果是"悬浮模板",看该专题页是否已经配置了"悬浮模板”
					List<Map<String, Object>> pageTemplateList = DbUp.upTable("fh_page_template").dataSqlList("SELECT pt.*,dt.template_type FROM fh_page_template pt LEFT JOIN fh_data_template dt ON pt.template_number = dt.template_number " + 
							" WHERE pt.page_number = '"+ mDataMap.get("zw_f_page_number") +"' AND pt.dal_status = '1001' AND dt.template_type = '449747500024' AND pt.template_number <> '"+template_number+"'", new MDataMap());
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
		
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		
		//应测试需求 兑换码兑换模板增加活动编号校验
		if("449747500022".equals(mDataMap.get("zw_f_template_type"))) {
			String activityCode = mDataMap.get("zw_f_activity_code");
			if(StringUtils.isEmpty(activityCode)) {
				mResult.setResultCode(11);
				mResult.setResultMessage("活动编号不能为空！");
				return mResult;
			}
			MDataMap activity = DbUp.upTable("oc_activity").one("activity_code", activityCode);
			if(null==activity || activity.isEmpty()) {
				mResult.setResultCode(22);
				mResult.setResultMessage("无效的活动编号！");
				return mResult;
			}
			if(DateUtil.compareDateTime(activity.get("end_time"),DateUtil.getSysDateTimeString())) {
				mResult.setResultCode(33);
				mResult.setResultMessage("活动已过期！");
				return mResult;
			}
		}
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		
		if(mResult.upFlagTrue()) {
			String pageNumber = mDataMap.get("zw_f_page_number");
			
			// 如果是专题页面修改则查询专题编号
			if("page_edit_v_fh_data_page".equals(mOperate.getPageCode())) {
				pageNumber = (String)DbUp.upTable("fh_data_page").dataGet("page_number", "", new MDataMap("uid", mDataMap.get("zw_f_uid")));
			}
			
			if(StringUtils.isNotBlank(pageNumber)) {
				new PlusSupportWebTemplete().onDataPageChanged(pageNumber);
			} else if(StringUtils.isNotBlank(template_number)) {
				new PlusSupportWebTemplete().onDataTemplateChanged(template_number);
			}
		}
		
		String content = "在表《"+mPage.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		return mResult;
	}
	
	
	

}
