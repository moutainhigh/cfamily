package com.cmall.familyhas.webfunc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.model.CouponCheckMobile;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.util.ReadExcelUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.cmall.groupcenter.util.HttpUtil;

public class CouponCheckFuncAdd extends FuncAdd {
	
	private final String JY_URL = bConfig("groupcenter.rsync_homehas_url");

	
	 // private final String JY_URL = "http://localhost:8080/service/interface/";
	 
	
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		synchronized (CouponCheckFuncAdd.class) {
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
			String checkType = mAddMaps.get("distribute_type");
			//每天的任务数不能超过9999，否则任务编号将会重复
			String code = WebHelper.upCode("RW");
			String taskCode = "RW" + DateUtil.getNoSpSysDateString() + code.substring(10, code.length());
			insertMap.put("task_code", taskCode);
			insertMap.put("activity_code", activityCode);
			insertMap.put("distribute_type", checkType);
			insertMap.put("create_time", create_time);
			insertMap.put("create_user", create_user);
			insertMap.put("update_time", create_time);
			insertMap.put("update_user", create_user);
			insertMap.put("check_time", create_time);
			insertMap.put("check_user", create_user);
			String manageCode = UserFactory.INSTANCE.create().getManageCode();
			insertMap.put("manage_code", manageCode);
			insertMap.put("distribute_status", "4497471600250001");//待审批
			int peopleNum = 0;
			try{
				if("4497471600240001".equals(checkType)){//指定账户发放
					String mobilesUrl = mAddMaps.get("import_mobiles");
					List<CouponCheckMobile> mobileList = new ArrayList<CouponCheckMobile>();
					try {
						mobileList = this.downloadAndAnalysisFile(mobilesUrl);
					} catch (Exception e) {//上传文件内容的格式不正确！
						mResult.inErrorMessage(916422125);
						return mResult;
					}
					int totalNum = Integer.parseInt(acMap.get("provide_num"));
					MDataMap cType = DbUp
							.upTable("oc_coupon_type")
							.oneWhere(
									"coupon_type_code",
									null,
									"status='4497469400030002' and activity_code=:activity_code and (valid_type= 4497471600080001 or (valid_type= 4497471600080002 and now() < end_time))",
									"activity_code", activityCode);
					if(cType == null){
						mResult.inErrorMessage(916422130);//活动下没有符合条件的优惠券类型！
						return mResult;
					}
					int provideCount = DbUp
							.upTable("oc_coupon_provide").count(
									"coupon_type_code", cType.get("coupon_type_code"));
					List<MDataMap> cklist = DbUp
							.upTable("oc_coupon_check")
							.queryAll(
									"mobile",
									"",
									"activity_code=:activity_code and distribute_status<>4497471600250004",
									new MDataMap("activity_code", activityCode));//查询已发放的账户排除已驳回的
					int taskNum = 0;
					if(cklist != null){
						taskNum = cklist.size();
					}
					if(provideCount + taskNum + mobileList.size() > totalNum){
						mResult.inErrorMessage(916422129);
						return mResult;
					}
					peopleNum = mobileList.size();
					for (CouponCheckMobile cm : mobileList) {
						String mobile = cm.getMobile();
						if(mobile.contains(".")){
							cm.setMobile(mobile.substring(0, mobile.indexOf(".")));
						}
						insertMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
						insertMap.put("mobile", cm.getMobile());
						DbUp.upTable("oc_coupon_check").dataInsert(insertMap);//插入发放任务
					}
				}else if("4497471600240002".equals(checkType)){//按规则发放
					String mobiles[] = mAddMaps.get("mobile").split(",");
					peopleNum = mobiles.length;
					for (String mobile : mobiles) {
						insertMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
						insertMap.put("mobile", mobile);
						DbUp.upTable("oc_coupon_check").dataInsert(insertMap);//插入发放任务
					}
				}
				insertMap.remove("uid");
				insertMap.remove("mobile");
				insertMap.put("people_num", String.valueOf(peopleNum));
				DbUp.upTable("oc_coupon_task").dataInsert(insertMap);//插入发放任务总表
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
			return mResult;
		}
	}
	
	private List<CouponCheckMobile> downloadAndAnalysisFile(String fileRemoteUrl)
			throws Exception {
		List<CouponCheckMobile> huijiayouMobile = new ArrayList<>();
		
		List<CouponCheckMobile> appMobile = new ArrayList<>();
		
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
	
}
