package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetRelAmtInput;
import com.cmall.familyhas.api.result.APiForGetRelAmtResult;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ApiCallSupport;


public class FuncSelectMember extends RootFunc{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String phonenumber = mDataMap.get("zw_f_phonenumber");
		String member_code = mDataMap.get("zw_f_member_code");
		MDataMap one = null;
		if(StringUtils.isBlank(member_code)) {
			one = DbUp.upTable("mc_login_info").one("login_name",phonenumber,"manage_code","SI2003");
		}else {
			one = DbUp.upTable("mc_login_info").one("member_code",member_code,"manage_code","SI2003");
			
		}
		if(one == null) {
			mResult.setResultCode(0);
			mResult.setResultMessage("无电话号码记录");
			return mResult;
		}
		member_code = one.get("member_code");
		phonenumber = one.get("login_name");
		String create_time = one.get("create_time");
		String nickname = DbUp.upTable("mc_member_sync").one("member_code",member_code).get("nickname");
		CouponUtil coupon = new CouponUtil();
		// 计算可用优惠券数量
		int number = coupon.availableCouponList("", member_code,
				new BigDecimal(-1), true, "SI2003","5.6.2");
		//int number = DbUp.upTable("oc_coupon_info").count("member_code",member_code,"status","0");
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", member_code);
		Map<String, Object> dataSqlOne = DbUp.upTable("za_oauth").dataSqlOne("select * from za_oauth where user_code =:member_code order by create_time desc limit 0,1", mWhereMap);
		String access_token = MapUtils.getString(dataSqlOne, "access_token");
		ApiCallSupport<ApiForGetRelAmtInput, APiForGetRelAmtResult> apiCallSupport = new ApiCallSupport<ApiForGetRelAmtInput, APiForGetRelAmtResult>();
		APiForGetRelAmtResult amtResult = new APiForGetRelAmtResult();
		try {
			amtResult = apiCallSupport.doCallApiForToken(
					"http://localhost:8080/cfamily/jsonapi/", 
					"com_cmall_familyhas_api_ApiForGetRelAmt", 
					"appfamilyhas", 
					/* bConfig("familyhas.apiPass") == null?"":bConfig("familyhas.apiPass"), */
					"amiauhsnehnujiauhz",
					access_token, 
					new ApiForGetRelAmtInput(), 
					amtResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(amtResult.getResultCode() != 1) {
			mResult.setResultCode(0);
			mResult.setResultMessage("查询数据失败,请重试!");
			return mResult;
		}
		Map<String,Object> re = new HashMap<String, Object>();
		re.put("phonenumber", phonenumber);
		re.put("nickname", nickname);
		re.put("member_code", member_code);
		re.put("create_time", create_time);
		re.put("number", number);
		re.put("jifen", amtResult.getPossAccmAmt());
		re.put("zancunkuan", amtResult.getPossCrdtAmt());
		re.put("chuzhijin", amtResult.getPossPpcAmt());
		mResult.setResultObject(re);
		
		List<Object> data = mResult.getResultList();
		List<Map<String,Object>> dataSqlList = DbUp.upTable("nc_address").dataSqlList("select * from nc_address where address_code =:member_code order by create_time desc", mWhereMap);
		for(Map<String,Object> map : dataSqlList) {
			Map<String,Object> dataMap = new HashMap<String,Object>();
			String uid = MapUtils.getString(map,"uid");
			String address = MapUtils.getString(map,"address_province")+MapUtils.getString(map,"address_street");
			String address_name = MapUtils.getString(map,"address_name");
			String address_mobile = MapUtils.getString(map,"address_mobile");
			Integer address_default = MapUtils.getInteger(map,"address_default");
			dataMap.put("uid", uid);
			dataMap.put("address", address);
			dataMap.put("address_name", address_name);
			dataMap.put("address_mobile", address_mobile);
			if(1 == address_default) {
				dataMap.put("address_default", "是");
			}else if(0 == address_default){
				dataMap.put("address_default", "否");
			}
			data.add(dataMap);
		}
		return mResult;
	}
}
