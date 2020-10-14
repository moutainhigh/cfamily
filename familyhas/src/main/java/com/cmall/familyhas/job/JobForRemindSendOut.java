package com.cmall.familyhas.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.quartz.JobExecutionContext;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForRemindSendOut extends RootJob{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	
	@Override
	public void doExecute(JobExecutionContext context) {
		List<NeedPayOrder> userAllNums = getUserAllNums();
		List<NeedPayOrder> successNums = new ArrayList<>();
		for(NeedPayOrder model : userAllNums) {
			//处理这条数据
			String processNums = processNums(model);
			if(processNums.contains("\"resultCode\":1")) {
				successNums.add(model);
			};
		}
		insertNumLog(successNums);
	}
	
	
	private String processNums(NeedPayOrder model) {
		String nums = model.getNum();
		String orderCode = model.getOrderCode();
		String app_version = model.getApp_version();
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		
		MDataMap mDataMap = new MDataMap();
		//新增参数
		if(AppVersionUtils.compareTo(app_version, "5.3.8") >= 0) {
			mDataMap.put("toPage", "12");
		}else {
			mDataMap.put("toPage", "7");
		}
		String str = "您好，您的订单"+orderCode+"已经发货，请及时关注和查收";
		try {
			mDataMap.put("msgContent", URLEncoder.encode(str, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		mDataMap.put("phone",nums);
		ObjectMapper om = new ObjectMapper();
		String api_input = "";
		try {
			api_input = om.writeValueAsString(mDataMap);
			map.put("api_input", api_input);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String apiSecret =  api_target +api_key+ api_input + api_timespan +  MD5key;
		apiSecret = SecrurityHelper.MD5(apiSecret);
		map.put("api_secret", apiSecret.toLowerCase());
		String  baiduPushUrl = bConfig("familyhas.baidu_push_url");
		String result = Http_Request_Post.doPost(baiduPushUrl, map, "utf-8");
		return result;
	}

	private List<NeedPayOrder> getUserAllNums() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentTime = System.currentTimeMillis() - 40 * 60 * 1000;
		Date date = new Date(currentTime);
		String format = sdf.format(date);
		String sql = "SELECT a.buyer_code,a.order_code,a.app_version,c.login_name FROM ordercenter.oc_orderinfo a JOIN membercenter.mc_login_info c ON a.buyer_code = c.member_code  WHERE a.order_status = '4497153900010003' AND a.update_time > '" + format +"' AND a.seller_code = 'SI2003' AND NOT EXISTS (SELECT b.member_code FROM logcenter.lc_send_out_remind b WHERE b.member_code = a.buyer_code AND b.order_code = a.order_code) ";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, null);
		List<NeedPayOrder> allNums = new ArrayList<NeedPayOrder>();
		
		for(Map<String, Object> map : dataSqlList) {
			String orderCode = map.get("order_code").toString();
			String memberCode = map.get("buyer_code").toString();
			String num = map.get("login_name").toString();
			String app_version = map.get("app_version").toString();
			NeedPayOrder model = new NeedPayOrder();
			model.setMemberCode(memberCode);
			model.setOrderCode(orderCode);
			model.setNum(num);
			model.setApp_version(app_version);
			allNums.add(model);
		}
		
		return allNums;
	}
	private void insertNumLog(List<NeedPayOrder> processNums) {
		StringBuffer sql = new StringBuffer();
		String execSql = " INSERT INTO logcenter.lc_send_out_remind(uid,member_code,order_code,rec_time)  VALUES ";
		for (NeedPayOrder model : processNums) {
			String sUid = UUID.randomUUID().toString().replace("-", "");
			sql.append("('"+sUid+"',");
			sql.append("'"+model.getMemberCode().replace("'", "\\'")+"',");
			sql.append("'"+model.getOrderCode().replace("'", "\\'")+"',");
			sql.append("'"+DateUtil.getSysDateTimeString()+"'),");
		}
		if(sql.length() > 0) {
			execSql += sql.substring(0, sql.length()-1);
			DbUp.upTable("lc_send_out_remind").dataExec(execSql, new MDataMap());
		}
	}

	
	class NeedPayOrder{
		private String num;
		private String memberCode;
		private String orderCode;
		private String orderCreateTime;
		private String app_version;
		
		public String getApp_version() {
			return app_version;
		}
		public void setApp_version(String app_version) {
			this.app_version = app_version;
		}
		public String getMemberCode() {
			return memberCode;
		}
		public void setMemberCode(String memberCode) {
			this.memberCode = memberCode;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getOrderCode() {
			return orderCode;
		}
		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}
		public String getOrderCreateTime() {
			return orderCreateTime;
		}
		public void setOrderCreateTime(String orderCreateTime) {
			this.orderCreateTime = orderCreateTime;
		}
		
	}
	
}
