package com.cmall.familyhas.webfunc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.model.CouponCheckMobile;
import com.cmall.groupcenter.util.HttpUtil;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.util.ReadExcelUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 兑换码兑换 发放
 * @remark 
 * @author 任宏斌
 * @date 2019年7月4日
 */
public class CouponRedeemFuncAdd extends FuncAdd {
	
	private final String letterStr="ABCDEFGHJKLMNPQRSTUVWXYZ";
	private final String numnrtStr="123456789";
	
	private final String JY_URL = bConfig("groupcenter.rsync_homehas_url");
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		synchronized (CouponRedeemFuncAdd.class) {
			MWebResult mResult = new MWebResult();
			MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			MDataMap insertMap = new MDataMap();
			String activityCode = mAddMaps.get("activity_code");
			if(StringUtils.isEmpty(activityCode)){
				mResult.inErrorMessage(916422124);//活动不能为空
				return mResult;
			}
			MDataMap acMap = DbUp.upTable("oc_activity").oneWhere(
					"flag,begin_time,end_time,provide_num", "", "",
					"activity_code", activityCode);//查询活动信息
			if(acMap != null){
				String nowTime = DateUtil.getNowTime();
				String startTime = acMap.get("begin_time");
				String endTime = acMap.get("end_time");
				if (DateUtil.compareTime(startTime, nowTime,
						"yyyy-MM-dd HH:mm:ss") > 0) {
					mResult.inErrorMessage(916422126);// 活动未开始
					return mResult;
				}
				if (DateUtil.compareTime(nowTime, endTime,
						"yyyy-MM-dd HH:mm:ss") > 0) {
					mResult.inErrorMessage(916422127);// 活动已结束
					return mResult;
				}
				if ("0".equals(acMap.get("flag"))) {
					mResult.inErrorMessage(916422128);//活动未发布
					return mResult;
				}
			}else{
				mResult.inErrorMessage(916422124);//活动不能为空
				return mResult;
			}
			/* 系统当前时间 */
			String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
			/* 获取当前登录人 */
			String create_user = UserFactory.INSTANCE.create().getLoginName();

			insertMap.put("activity_code", activityCode);
			insertMap.put("is_redeem", "0");
			
			String sSql = "select ifnull(max(import_count),0) import_count from oc_coupon_redeem where activity_code=:activity_code";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_redeem").dataSqlList(sSql, new MDataMap("activity_code",activityCode));
			if(null!= dataSqlList && !dataSqlList.isEmpty()) {
				insertMap.put("import_count", String.valueOf(Integer.parseInt(dataSqlList.get(0).get("import_count")+"")+1));
			}else {
				insertMap.put("import_count", "1");
			}
			insertMap.put("create_time", create_time);
			insertMap.put("create_user", create_user);
			insertMap.put("update_time", create_time);
			insertMap.put("update_user", create_user);
			
			int successCount = 0;
			int failureCount = 0;
			int totalCount = 0;
			List<String> successMobile = new ArrayList<String>();
			List<String> failureMobile = new ArrayList<String>();
			try{
				String mobilesUrl = mAddMaps.get("member_code");
				List<CouponCheckMobile> mobileList = new ArrayList<CouponCheckMobile>();
				try {
					mobileList = this.downloadAndAnalysisFile(mobilesUrl);
				} catch (Exception e) {//上传文件内容的格式不正确！
					mResult.inErrorMessage(916422125);
					return mResult;
				}
				totalCount = mobileList.size();
				for (CouponCheckMobile cm : mobileList) {
					String mobile = cm.getMobile();
					if(mobile.contains(".")){
						cm.setMobile(mobile.substring(0, mobile.indexOf(".")));
					}
					String sql = "select ifnull(member_code,'') member_code from mc_login_info where login_name=:login_name";
					List<Map<String, Object>> sqlList = DbUp.upTable("mc_login_info").dataSqlList(sql, new MDataMap("login_name",cm.getMobile()));
					if(sqlList!=null && !sqlList.isEmpty()) {
						insertMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
						insertMap.put("activity_cdkey", getCdkeyString());
						insertMap.put("mobile", cm.getMobile());
						insertMap.put("member_code", sqlList.get(0).get("member_code")+"");
						DbUp.upTable("oc_coupon_redeem").dataInsert(insertMap);
						successCount++;
						successMobile.add(cm.getMobile());
					}else {
						failureCount++;
						failureMobile.add(cm.getMobile());
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
			
			if(successCount>0) {
				int reCount = DbUp.upTable("oc_coupon_redeem").count("activity_code", activityCode);
				DbUp.upTable("oc_activity").dataUpdate(new MDataMap("provide_num", 
						String.valueOf(reCount), "activity_code", activityCode), "provide_num", "activity_code");
			}
			
			if(mResult.upFlagTrue()) {
				if(successCount==totalCount) {
					mResult.setResultMessage("成功发放"+successCount+"个账户");
				}else {
					mResult.setResultMessage("99");
					mResult.setResultMessage("共上传"+totalCount+"个账户<br/>成功发放"+successCount+"个账户<br/>失败"+failureCount+"个账户<br/>失败手机号码为:<br/>"+StringUtils.join(failureMobile, "<br/>"));
				}
			}else {
				if(successCount>0) {
					mResult.setResultMessage(mResult.getResultMessage()+"<br/>但以下账户保存成功:<br/>"+StringUtils.join(successMobile, "<br/>"));
				}
			}
			return mResult;
		}
	}
	
	private List<CouponCheckMobile> downloadAndAnalysisFile(String fileRemoteUrl)
			throws Exception {
		List<CouponCheckMobile> mobile = new ArrayList<>();
		String extension = fileRemoteUrl.lastIndexOf(".") == -1 ? ""
				: fileRemoteUrl.substring(fileRemoteUrl.lastIndexOf(".") + 1);
		java.net.URL resourceUrl = new java.net.URL(fileRemoteUrl);
		InputStream content = (InputStream) resourceUrl.getContent();
		ReadExcelUtil<CouponCheckMobile> readExcelUtil = new ReadExcelUtil<CouponCheckMobile>();
		List<CouponCheckMobile> readExcel =
				readExcelUtil.readExcel(false, null, content, new String[] {"mobile"}, new Class[] {String.class}, CouponCheckMobile.class, extension);
		for (CouponCheckMobile couponCheckMobile : readExcel) {
			String copMobile = couponCheckMobile.getMobile();
			copMobile = copMobile.replace(".00", "");
			if(copMobile.substring(0, 1).equalsIgnoreCase("1") && copMobile.length()==11) {//正常手机号
				mobile.add(couponCheckMobile);
			}else if(copMobile.substring(0, 1).equalsIgnoreCase("M")) {//惠家有用户
				Map<String, Object> dataSqlOne =
						DbUp.upTable("mc_login_info").dataSqlOne("select login_name from mc_login_info where member_code = '"+copMobile+"'",new MDataMap());
				if(dataSqlOne!=null) {
					CouponCheckMobile couponCheckMobile2 = new CouponCheckMobile();
					couponCheckMobile2.setMobile(dataSqlOne.get("login_name").toString());
					mobile.add(couponCheckMobile2);
				}
			}else {
				Map<String, String> postMap = new HashMap<String, String>();
				postMap.put("custId", copMobile);
				String response = HttpUtil.postJson(JY_URL + "getMobilesById", JSONArray.toJSONString(postMap));
				// logger.info("response---------->>>:" + response);
				System.out.println("response---------->>>:" + response);
				Map<String, Object> maps = JSONObject.parseObject(response);
				if ((Boolean)maps.get("success")) {
					CouponCheckMobile couponCheckMobile3 = new CouponCheckMobile();
					couponCheckMobile3.setMobile(maps.get("mobile").toString());
					mobile.add(couponCheckMobile3);
				}
			}
		}
		return mobile;
		
	}
	
	private String getCdkeyString(){
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<16;i++){
	    	 if((i>=0&&i<4) || (i>=8&&i<12)) {
	    		 int number=random.nextInt(24);
	    		 sb.append(letterStr.charAt(number));
	    	 }else {
	    		 int number=random.nextInt(9);
	    		 sb.append(numnrtStr.charAt(number));
	    	 }
	     }
	     return sb.toString();
	 }
}
