package com.cmall.familyhas.api;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.job.JobUserCoupon;
import com.cmall.groupcenter.recommend.RecommendUtil;
import com.cmall.groupcenter.support.GroupAccountSupport;
import com.cmall.membercenter.model.RecommendUserRegisterInput;
import com.cmall.membercenter.model.RecommendUserRegisterResult;
import com.cmall.membercenter.support.MemberRegisterSupport;
import com.cmall.ordercenter.service.CouponsService;
import com.cmall.systemcenter.bill.HexUtil;
import com.cmall.systemcenter.bill.MD5Util;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.EVerifyCodeTypeEnumer;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.cmall.systemcenter.support.VerifySupport;
import com.ctc.wstx.util.DataUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webmodel.MWebResult;


/** 
* @ClassName: GroupUserRegisterApi 
* @Description: 推荐用户注册惠家有送券
* @author 张海生
* @date 2015-8-13 下午4:48:14 
*  
*/
public class RecommendUserRegisterApi extends
	RootApiForManage<RecommendUserRegisterResult, RecommendUserRegisterInput> {

	public RecommendUserRegisterResult Process(
			RecommendUserRegisterInput inputParam, MDataMap mRequestMap) {
		
		RecommendUserRegisterResult result = new RecommendUserRegisterResult();
		String mobile = inputParam.getMobile();
		String referrerMobile = inputParam.getReferrerMobile().trim();
	    //添加验证码验证
		String verifyCode = inputParam.getVerifyCode().trim();
	    VerifySupport verifySupport = new VerifySupport();  
	    @SuppressWarnings("static-access")
		String verifyType=verifySupport.upVerifyTypeByEnumer(EVerifyCodeTypeEnumer.MemberReginster);
	    MWebResult mWebResult  = verifySupport.checkVerifyCodeByMoreType(verifyType, mobile, verifyCode);
	    if(mWebResult.upFlagTrue()){
	    	result.setReturnStatus(true);
	    	
			Map<?, ?> loginInfoMap = null;
			//判断是否有绑定上下级需求
			if(!StringUtils.isEmpty(referrerMobile)){
				loginInfoMap = DbUp.upTable("mc_login_info").one("login_name",referrerMobile);
				if(null == loginInfoMap){
					result.setResultCode(0);
					result.setResultMessage("推荐人不存在");
				}
			}
			if (result.upFlagTrue()){
				//result = new MemberRegisterSupport().doHomHasRegister(inputParam, getManageCode());
				
				//更改注册方法为  调动会员中心的用户注册接口
				JSONObject obj = new JSONObject();
				obj.put("loginName", mobile);
				obj.put("loginPass", RandomStringUtils.randomNumeric(6));
				obj.put("version", "1");
				try {
					String code = "";
					String responseStr = getHttps(obj.toJSONString());
					JSONObject resultJson = JSON.parseObject(responseStr);
					if (resultJson.getInteger("resultCode") == 1 && resultJson.getString("accessToken") != null
							&& !"".equals(resultJson.getString("accessToken"))) {
						
						// 不再检查用户表，避免同步不及时造成的查询异常
//						Map<String, Object> map = DbUp.upTable("za_oauth").dataSqlOne(
//								"select user_code from zapdata.za_oauth where access_token=:access_token",
//								new MDataMap("access_token", resultJson.getString("accessToken")));
//						if (map != null && map.get("user_code") != null) {
//							code = map.get("user_code").toString();
//						}
//						
//						MDataMap mc_member_info = DbUp.upTable("mc_member_info").one("member_code",code);
//						MDataMap mc_login_info = DbUp.upTable("mc_login_info").one("member_code",code);
//						
//						
//						result.setAccountCode(mc_member_info.get("account_code"));
//						result.setFlagRelation(Integer
//								.parseInt(DbUp
//										.upTable("gc_member_relation")
//										.dataGet(
//												" count(1) ",
//												"account_code=:account_code or parent_code=:account_code",
//												new MDataMap("account_code",
//														mc_member_info.get("account_code"))).toString()) > 0 ? 0
//								: 1);
//						result.setIsNoPassword(StringUtils.isBlank(mc_login_info.get("login_pass").trim()) ? "1" : "0");
//						result.setLoginName(mobile);
//						result.setMemberCode(code);
//						result.setResultCode(1);
//						result.setSerialNumber("");
//						result.setUserToken(resultJson.getString("accessToken"));
						
					}
					else {
						result.setResultCode(0);
						result.setResultMessage("领取失败");
						LogFactory.getLog(getClass()).warn("邀请注册失败：" + responseStr);;
					}
					
					
				} catch (Exception e) {
					result.setResultCode(0);
					result.setResultMessage("领取失败");
					e.printStackTrace();
				}
			}
			if (!result.upFlagTrue()){
				return result;
			}
		
			String sql = "SELECT ol.activity_code, op.coupon_type_code,op.money,op.money_type,op.start_time,op.end_time,op.valid_type,op.valid_day" +
					" FROM oc_coupon_relative  ol inner join oc_coupon_type op on ol.activity_code = op.activity_code" +
					" and op.status='4497469400030002' where ol.relative_type = '1' and ol.manage_code=:manage_code";
			//只是查出一种优惠券
			//Map<String, Object> couponTypeMap =  DbUp.upTable("oc_coupon_type").dataSqlOne(sql, new MDataMap("manage_code",getManageCode()));//查询优惠券相关信息	
			//需要改为多种券中的情况
			List<Map<String,Object>>  couponTypeListMap= new ArrayList<Map<String,Object>>();
			couponTypeListMap = DbUp.upTable("oc_coupon_type").dataSqlList(sql, new MDataMap("manage_code",getManageCode()));//查询优惠券相关信息	
			//有绑定需求的，开始绑定上下级
			if (result.upFlagTrue() && !StringUtils.isEmpty(referrerMobile)) {
				 //String memberCode=result.getMemberCode();
				 //String parentMemberCode=DbUp.upTable("mc_login_info").one("login_name",referrerMobile).get("member_code");
				 //String accountCode=DbUp.upTable("mc_member_info").one("member_code",memberCode).get("account_code");
				 //String parentAccountCode=DbUp.upTable("mc_member_info").one("member_code",parentMemberCode).get("account_code");
				 
				 //GroupAccountSupport groupAccountSupport=new GroupAccountSupport();
				 //groupAccountSupport.createRelation(accountCode,parentAccountCode, "",FormatHelper.upDateTime());
				 
				 RecommendUtil ru = new RecommendUtil();
				 String uid = new SimpleDateFormat("MMddHHmmss").format(new Date()) + ru.getRandom(7);
				 String sBaseString="abcdefghijklmnopqrstuvwxyz0123456789";
				 String convert = RecommendUtil.convertFormatNumberBack(uid, sBaseString);
				
				 DbUp.upTable("gc_recommend_info").insert("uqcode",convert,"app_code",getManageCode(),"mobile",referrerMobile,
						 "recommended_mobile",mobile,"recommend_time",DateUtil.getNowTime(),"source","1");//插入推荐信息表
				 
				 //邀请人获取优惠券 这个添加的是oc_coupon_provide 和oc_coupon_info 表  
				 JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,
						CouponConst.referees__coupon,new MDataMap("mobile", referrerMobile, "manage_code", "SI2003"));
					
				 
				 //添加专门的邀请人邀请的优惠信息统计表，下面代码放在消息中不好使，等找到原因了可以迁移
				 //这么写有不同步的风险
		/*		 String temSql = "SELECT ol.activity_code, op.coupon_type_code,op.money,op.money_type,op.create_time,op.start_time,op.end_time,op.valid_type,op.valid_day,op.limit_money,op.surplus_money" +
							" FROM oc_coupon_relative  ol inner join oc_coupon_type op on ol.activity_code = op.activity_code" +
							" and op.status='4497469400030002' where ol.relative_type = '7' and ol.manage_code=:manage_code";
					List<Map<String, Object>> listMap =  DbUp.upTable("oc_coupon_type").dataSqlList(temSql, new MDataMap("manage_code",getManageCode()));
					 if(listMap!=null){
						 String dateParam="";
						 String startTime="";
						 String endTime="";
						 for (Map<String, Object> map : listMap) {
							 //String dataParam = DateUtil.toString(new Date(),DateUtil.DATE_FORMAT_DATETIME);
							  dateParam =DateUtil.getNowTime();
							  startTime = map.get("start_time").toString();
							  endTime = map.get("end_time").toString();
							  String temStartTime="";
							 if(StringUtils.isBlank(startTime)&&StringUtils.isBlank(endTime)) {
								 startTime = DateUtil.getNowTime();
								 Integer validDay = Integer.valueOf(map.get("valid_day").toString());
								 endTime = DateUtil.getTimeCompareSomeDay(validDay);
							 }
							 String coupon_code=WebHelper.upCode("CP");
							 DbUp.upTable("oc_coupon_member").insert("uid",WebHelper.upUuid(),"relative_type","7","member_code",parentMemberCode,
									 "coupon_type_code",map.get("coupon_type_code").toString(),"coupon_code",coupon_code,"initial_money",map.get("money").toString(),
									 "limit_money",map.get("limit_money").toString(),"create_time",dateParam,"start_time",startTime,"end_time",endTime,"money_type",map.get("money_type").toString(),
									 "manage_code",getManageCode());
						} 
					 }*/

			} else {
				//判断是否有有效的推荐记录
				MDataMap isHasRcd = DbUp.upTable("gc_recommend_info").one("is_usable_send_link","1","flag_establishment_rel","0","recommended_mobile",inputParam.getMobile());
				if(null != isHasRcd) {
					result.setFlagRelation(0);
				}
			}
			if(result.upFlagTrue()&&(AppConst.MANAGE_CODE_HOMEHAS.equals(getManageCode()) || AppConst.MANAGE_CODE_CDOG.equals(getManageCode()))){
				JmsNoticeSupport.INSTANCE.sendQueue(JmsNameEnumer.OnDistributeCoupon,//惠家有或沙皮狗注册送优惠券
						CouponConst.register_coupon,new MDataMap("mobile", inputParam.getMobile(),"manage_code", getManageCode()));
			}
			if(result.upFlagTrue() && couponTypeListMap.size()>0){
				StringBuffer sb = new StringBuffer();
				for(Map<String,Object> map:couponTypeListMap) {
					Map paramMap = new HashMap<String,Object>();
					paramMap.put("money", map.get("money").toString());//面额
					paramMap.put("money_type", map.get("money_type").toString());//类型
					
					sb.append("money_type#"+map.get("money_type").toString());
					sb.append(",money#"+map.get("money").toString());
					if("4497471600080002".equals((String)map.get("valid_type"))){//有效类型为日期范围
						paramMap.put("start_time",(String)map.get("start_time"));
						paramMap.put("end_time",(String)map.get("end_time"));
						
						sb.append(",start_time#"+map.get("start_time").toString());
						sb.append(",end_time#"+map.get("end_time").toString());
					}else if("4497471600080001".equals((String)map.get("valid_type"))){//有效类型为天数
						paramMap.put("start_time",DateUtil.getNowTime());
						paramMap.put("end_time",DateUtil.getTimeCompareSomeDay(Integer.valueOf(map.get("valid_day").toString())));
						sb.append(",start_time#"+DateUtil.getNowTime());
						sb.append(",end_time#"+DateUtil.getTimeCompareSomeDay(Integer.valueOf(map.get("valid_day").toString()))+";");
					}
				}
				result.setCouponInfo(sb.toString().substring(0,sb.toString().length()-1));
			}
     	}   	
	    		
	    else{
	    	result.setResultCode(0);
	    	result.setResultMessage("验证码有误");
	    	result.setReturnStatus(false);
	    }
		return result;
	}

	/**
	 * 
	 * 方法: getHttps <br>
	 * 描述: 获取url访问地址 <br>
	 * 作者: fq fuqiang@huijiayou.cn<br>
	 * 时间: 2016年9月1日 下午1:39:04
	 * 
	 * @param sUrl
	 * @param sRequestString
	 * @return
	 * @throws Exception
	 */
	private String getHttps(String sRequestString) throws Exception {

		MDataMap mrequest = getsignMap(sRequestString);

		String sResponseString = WebClientSupport.upPost(bConfig("groupcenter.chcekedUserInfo_api_url"), mrequest);

		return sResponseString;
	}

	private MDataMap getsignMap(String requestStr) {
		MDataMap dataMap = new MDataMap();
		dataMap.put("api_target", bConfig("groupcenter.checkedUserInfo_api_target"));
		dataMap.put("api_key", bConfig("groupcenter.checkedUserInfo_api_key"));
		dataMap.put("api_input", requestStr);
		dataMap.put("api_timespan", DateUtil.getSysDateTimeString());
		dataMap.put("api_project", bConfig("groupcenter.checkedUserInfo_api_project"));
		StringBuffer str = new StringBuffer();
		str.append(dataMap.get("api_target")).append(dataMap.get("api_key")).append(dataMap.get("api_input"))
				.append(dataMap.get("api_timespan")).append(bConfig("groupcenter.checkedUserInfo_api_pass"));
		dataMap.put("api_secret", HexUtil.toHexString(MD5Util.md5(str.toString())));
		return dataMap;
	}
}
