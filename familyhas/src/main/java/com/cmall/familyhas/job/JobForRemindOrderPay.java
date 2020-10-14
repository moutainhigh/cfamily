package com.cmall.familyhas.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.cmall.systemcenter.util.SmsUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
import com.srnpr.zapweb.webwx.WxGateSupport;

public class JobForRemindOrderPay extends RootJob{

	private final String api_key = "appfamilyhas";
	private final String MD5key = "amiauhsnehnujiauhz";
	
	@Override
	public void doExecute(JobExecutionContext context) {
		List<NeedPayOrder> userAllNums = getUserAllNums();
		StringBuilder sb =  new StringBuilder();
		int countPs = 0;
		//一批去重手机号
		List<String> nums = new ArrayList<String>();
		StringBuffer str  = new StringBuffer();//辅助去重
		List<NeedPayOrder> paramAllNums =  new ArrayList<NeedPayOrder>();
		for(NeedPayOrder npo :userAllNums) {
			String num = npo.getNum();
			if(str.toString().indexOf(num)>0) {
				
			}else {
				str.append(num);
				nums.add(num);
				paramAllNums.add(npo);
			}
		}
		
		for(int i = 0;i<nums.size();i++) {
			String num = nums.get(i);
			if(i!=0 && i%999==0) {
				sb.append(num);
				//处理这100条数据
				String processNums = processNums(sb.toString());
				if(processNums.contains("\"resultCode\":1")) {
					insertNumLog(userAllNums);
					countPs++;
					if(countPs==9) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
							countPs = 0;
						}
					}
				};
				//重置nums
				sb =  new StringBuilder();
			}else {
				if(i<nums.size()-1) {
					sb.append(num+",");
				}else {
					sb.append(num);
				}
			}
		}
		if(!"".equals(sb.toString())) {
			//处理不足1000条的数据
			String processNums = processNums(sb.toString());
			if(processNums.contains("\"resultCode\":1")) {
				insertNumLog(userAllNums);
			};
		}
	    //添加小程序推送通知和短息通知
		if(paramAllNums.size()>0) {
			sendExpiredOrderNoticeForXCX(paramAllNums);
			
		}
	}
	
	
	private String processNums(String nums) {
		MDataMap map = new MDataMap();
		map.put("api_project", "jyhapi");
		String api_target =  "com_cmall_familyhas_api_APIBaiDuPush";
		map.put("api_target", api_target);
		String api_timespan = DateUtil.getNowTime();
		map.put("api_timespan",api_timespan );
		
		MDataMap mDataMap = new MDataMap();
		//新增参数
		mDataMap.put("toPage", "11");
		String str = "您看中的这款商品非常热销，库存不多了，建议尽快付款哦~";
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

	/**
	 * 小程序消息推送逻辑处理
	 * @param paramAllNums
	 */
	private void sendExpiredOrderNoticeForXCX(List<NeedPayOrder> paramAllNums) {
		
		Map<String,String> bigOrderCodeMap = new HashMap<String, String>();
		
		for (NeedPayOrder needPayOrder : paramAllNums) {
			
			// 排重
			if(bigOrderCodeMap.containsKey(needPayOrder.getBig_order_code())) {
				continue;
			}
			bigOrderCodeMap.put(needPayOrder.getBig_order_code(), "");
			
			if(StringUtils.isNotBlank(needPayOrder.getOpenidGzh())) {
				String open_id = needPayOrder.getOpenidGzh();
				String receivers  = open_id+ "|11||"+FormatHelper.formatString(bConfig("familyhas.sms_order_pay_wx"), needPayOrder.getBig_order_code());
				String orderCode = "订单编号:"+needPayOrder.getBig_order_code();
				String message = "{\"first\":{\"color\":\"#336699\",\"value\":\""+orderCode+"\"},\"keyword1\":{\"color\":\"#336699\",\"value\":\"待付款\"},\"keyword2\":{\"color\":\"#336699\",\"value\":\""+needPayOrder.getOrderMoney()+"元\"},\"remark\":{\"color\":\"#336699\",\"value\":\"您看中的这款商品非常热销，库存不多了，建议尽快付款哦~\"}}";
				WxGateSupport wxGateSupport = new WxGateSupport();
				wxGateSupport.sendMsgForNotice(receivers, message,"");
			}
			
			//添加短信通知
			SmsUtil smsUtil=new SmsUtil();
			StringBuffer error= new StringBuffer();
			//内容违规：亲，您拍下的宝贝还未付款哦，需要帮助请联系客服哦。活动马上就截止了，请及时付款享受优惠，http://r.jyh.com/4i
			//smsUtil.sendSms6(needPayOrder.getNum(),"您的订单:"+needPayOrder.getBig_order_code()+"即将关闭，其中的商品库存不多了。http://r.jyh.com/4i",error);
			smsUtil.sendSmsForYX(needPayOrder.getNum(), FormatHelper.formatString(bConfig("familyhas.sms_order_pay_content"), needPayOrder.getBig_order_code()));
		}
	}
	
	
	private List<NeedPayOrder> getUserAllNums() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentTime = System.currentTimeMillis() - 40 * 60 * 1000;
		Date date = new Date(currentTime);
		String format = sdf.format(date);
		String sql = "SELECT a.big_order_code,a.buyer_code,a.order_code,a.create_time,c.login_name,c.openid_gzh,a.order_money FROM ordercenter.oc_orderinfo a JOIN membercenter.mc_login_info c ON a.buyer_code = c.member_code  WHERE a.order_status = '4497153900010001' AND a.pay_type='449716200001' AND a.create_time > '" + format +"' AND a.seller_code = 'SI2003' AND NOT EXISTS (SELECT b.member_code FROM logcenter.lc_remind_order_pay b WHERE b.member_code = a.buyer_code AND b.order_code = a.order_code) ";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, null);
		List<NeedPayOrder> allNums = new ArrayList<NeedPayOrder>();
		
		for(Map<String, Object> map : dataSqlList) {
			String createTime = map.get("create_time").toString();
			String orderCode = map.get("order_code").toString();
			String memberCode = map.get("buyer_code").toString();
			String num = map.get("login_name").toString();
			String openidGzh = map.get("openid_gzh").toString();
			String orderMoney = map.get("order_money").toString();
			String big_order_code = map.get("big_order_code").toString();
			if(!"".equals(orderCode)&&XmasKv.upFactory(EKvSchema.TimeCancelOrder).exists(orderCode)) {
				String expireTime = XmasKv.upFactory(EKvSchema.TimeCancelOrder).get(orderCode);
				try {
					if(sdf.parse(expireTime).getTime()-sdf.parse(createTime).getTime()<= 1800*1000 ) {
						if(new Date().getTime() - sdf.parse(createTime).getTime() >= 300*1000) {
							NeedPayOrder model = new NeedPayOrder();
							model.setMemberCode(memberCode);
							model.setOrderCode(orderCode);
							model.setNum(num);
							model.setOpenidGzh(openidGzh);
							model.setOrderMoney(orderMoney);
							model.setBig_order_code(big_order_code);
							allNums.add(model);
						}
					}else {
						if(new Date().getTime() - sdf.parse(createTime).getTime() >= 1800*1000) {
							NeedPayOrder model = new NeedPayOrder();
							model.setMemberCode(memberCode);
							model.setOrderCode(orderCode);
							model.setNum(num);
							model.setOpenidGzh(openidGzh);
							model.setOrderMoney(orderMoney);
							model.setBig_order_code(big_order_code);
							allNums.add(model);
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return allNums;
	}
	private void insertNumLog(List<NeedPayOrder> processNums) {
		StringBuffer sql = new StringBuffer();
		String execSql = " INSERT INTO logcenter.lc_remind_order_pay(uid,member_code,order_code,rec_time)  VALUES ";
		for (NeedPayOrder model : processNums) {
			String sUid = UUID.randomUUID().toString().replace("-", "");
			sql.append("('"+sUid+"',");
			sql.append("'"+model.getMemberCode().replace("'", "\\'")+"',");
			sql.append("'"+model.getOrderCode().replace("'", "\\'")+"',");
			sql.append("'"+DateUtil.getSysDateTimeString()+"'),");
		}
		if(sql.length() > 0) {
			execSql += sql.substring(0, sql.length()-1);
			DbUp.upTable("lc_remind_order_pay").dataExec(execSql, new MDataMap());
		}
	}

	
	class NeedPayOrder{
		private String num;
		private String memberCode;
		private String orderCode;
		private String orderCreateTime;
		private String openidGzh;
		private String orderMoney;
		private String big_order_code;
		
		public String getBig_order_code() {
			return big_order_code;
		}
		public void setBig_order_code(String big_order_code) {
			this.big_order_code = big_order_code;
		}
		public String getOpenidGzh() {
			return openidGzh;
		}
		public void setOpenidGzh(String openidGzh) {
			this.openidGzh = openidGzh;
		}
		public String getOrderMoney() {
			return orderMoney;
		}
		public void setOrderMoney(String orderMoney) {
			this.orderMoney = orderMoney;
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
