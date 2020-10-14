package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.util.HttpUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时捞取LD售后单变更状态轨迹，给用户推送消息以及记录审核详情
 * @author Angel Joy
 * @desc 售后状态 01:待审核 02:受理退货 03:驳回退货 04:退货取消 05:退货完成 06:取消异常 07:已出库 08:换货失败
 */
public class JobForGetLdAfterSaleStatusChange extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		String jobClass = "com.cmall.familyhas.job.JobForGetLdAfterSaleStatusChange";
		String url = bConfig("groupcenter.rsync_homehas_url")+"getAfterServiceChange";
		MDataMap jobInfo = DbUp.upTable("za_job").one("job_class",jobClass);
		if(jobInfo == null || jobInfo.isEmpty()) {
			return;
		}
		String startTime = jobInfo.get("begin_time");
		String endTime = jobInfo.get("next_time");
		if(StringUtils.isEmpty(startTime)) {//开始时间为空时，取当前时间
			startTime = DateUtil.getSysDateTimeString();
			endTime =DateUtil.addMinute(1);
		}
		Map<String,Object> postMap = new HashMap<String,Object>();
		postMap.put("beginTime", startTime);
		postMap.put("endTime", endTime);
		MDataMap logMap = new MDataMap();
		logMap.put("push_time", DateUtil.getSysDateTimeString());
		logMap.put("push_param", JSONArray.toJSONString(postMap));
		String response = HttpUtil.postJson(url,JSONArray.toJSONString(postMap));
		logMap.put("result_time", DateUtil.getSysDateTimeString());
		logMap.put("result_param", response);
		logMap.put("http_url", url);
		logMap.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
		DbUp.upTable("lc_ld_aftersale_http_log").dataInsert(logMap);
		boolean flag = false;
		String result = "";
		if(StringUtils.isEmpty(response)) {
			return;
		}
		JSONObject jo = JSONObject.parseObject(response);
		if(jo != null) {
			flag = jo.getBooleanValue("success");
		}
		result = jo.getString("result");
		if(flag) {
			logMap.put("code", "0");
		}else {
			logMap.put("code", "-1");
		}
		if(StringUtils.isEmpty(result)) {
			return;
		}
		JSONArray datas = JSONArray.parseArray(result);
		Iterator it = datas.iterator();
		while(it.hasNext()) {
			JSONObject data = (JSONObject)it.next();
			this.exData(data);
		}
	}
	
	/*
	 * 处理数据
	 */
	private void exData(JSONObject data) {
		String afterSaleCode =StringUtils.trimToEmpty(data.getString("SALE_CODE_LD"));
		String org_status = StringUtils.trimToEmpty(data.getString("ORG_STATUS"));
		String end_status = StringUtils.trimToEmpty(data.getString("END_STATUS"));
		String status_desc =  StringUtils.trimToEmpty(data.getString("STATUS_DESC"));
		Long etr_date = data.getLong("ETR_DATE");
		String money = StringUtils.trimToEmpty(data.getString("RTN_MONEY"));
		String integarl = StringUtils.trimToEmpty(data.getString("RTN_ACCM"));
		String hjycoin = StringUtils.trimToEmpty(data.getString("RTN_HCOIN"));
		if(StringUtils.isEmpty(hjycoin)) {
			hjycoin = "0";
		}
		String phone = data.getString("mobile");
		String reason = StringUtils.trimToEmpty(data.getString("RTN_REASON"));
		String isGetProduce = "Y".equals(data.getString("IS_RCV"))?"是":"否";
		MDataMap lsasMap = new MDataMap();
		lsasMap.put("lac_code",  WebHelper.upCode("LAC"));
		lsasMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
		String template_code = "";
		lsasMap.put("create_source", "4497477800070001");//卖家
		if("06".equals(end_status)) {
			end_status = org_status; 
		}
		if("01".equals(end_status)) {//待审核，用户发起申请
			template_code = "OST160312100007";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), "交易完成",reason,isGetProduce, money, integarl,MoneyHelper.format(new BigDecimal(hjycoin)),"无"));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("create_source", "4497477800070002");//买家
		}else if("02".equals(end_status)) {//受理退货
			template_code = "OST160312100002";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", templateMap.get("template_context"));
			lsasMap.put("serial_title", templateMap.get("template_title"));
		}else if("03".equals(end_status)) {
			template_code = "OST160312100009";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), ""));
			lsasMap.put("serial_title", templateMap.get("template_title"));
		}else if("04".equals(end_status)) {
			template_code = "OST160312100023";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", templateMap.get("template_context"));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("create_source", "4497477800070002");//买家
		}else if("05".equals(end_status)||"06".equals(end_status)||"09".equals(end_status)) {
			template_code = "OST160312100010";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", templateMap.get("template_context"));
			lsasMap.put("serial_title", "办理退款中");
		}else if("10".equals(end_status)) {
			template_code = "OST160312100012";
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code",template_code);
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), money));
			lsasMap.put("serial_title", templateMap.get("template_title"));
		}else {
			//换货节点，暂不处理
			return;
		}
		lsasMap.put("create_time", DateUtil.toString(etr_date, DateUtil.DATE_FORMAT_DATETIME));
		lsasMap.put("asale_code", afterSaleCode);
		lsasMap.put("template_code", template_code);
		Integer count = DbUp.upTable("lc_serial_after_sale").count("asale_code",afterSaleCode,"template_code",template_code);
		MDataMap pusgNewsMap = new MDataMap();
		pusgNewsMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
		String memberCode = "";
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("login_name",phone); 
		if(loginInfo!= null && !loginInfo.isEmpty()) {
			memberCode = loginInfo.get("member_code");
		}
		pusgNewsMap.put("member_code", memberCode);
		pusgNewsMap.put("phone_no", phone);//TODO
		pusgNewsMap.put("title", lsasMap.get("serial_title"));
		pusgNewsMap.put("message", lsasMap.get("serial_msg"));
		pusgNewsMap.put("create_time", DateUtil.toString(etr_date, DateUtil.DATE_FORMAT_DATETIME));
		pusgNewsMap.put("after_sale_code", afterSaleCode);
		pusgNewsMap.put("after_sale_status", end_status);
		pusgNewsMap.put("to_page", "13");
		pusgNewsMap.put("if_read", "0");
		if(count == 0) {
			DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);//审核详情表插入数据
			if(lsasMap.get("create_source").equals("4497477800070001")) {//卖家
				DbUp.upTable("nc_aftersale_push_news").dataInsert(pusgNewsMap);//消息推送表插入数据
			}
		}
	}

}
