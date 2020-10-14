package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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

public class FuncAddDataTemplate  extends FuncAdd{
	
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
		
		String userCode = UserFactory.INSTANCE.create().getUserCode();
		String sql ="select real_name from zapdata.za_userinfo where user_code='"+userCode+"'";
		Map<String,Object> map =DbUp.upTable("za_userinfo").dataSqlOne(sql, null);
		mDataMap.put("zw_f_template_number", WebHelper.upCode("PTN"));
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		mDataMap.put("zw_f_creator", map.get("real_name").toString());
		
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
