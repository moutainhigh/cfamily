package com.cmall.familyhas.api;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiRecodeDistributionInfoInput;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.membercenter.model.MemberInfo;
import com.cmall.membercenter.support.MemberInfoSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webfactory.ApiFactory;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 记录分销信息接口
 * @author zhangb
 *
 */
public class ApiRecodeDistributionInfo extends RootApi<RootResultWeb, ApiRecodeDistributionInfoInput> {

	@Override
	public RootResultWeb Process(ApiRecodeDistributionInfoInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String distributionJuniorMemberId = inputParam.getDistributionJuniorMemberId();
		String distributionMemberId = inputParam.getDistributionMemberId();
		String distributionProductCode = inputParam.getDistributionProductCode();
		MDataMap mDataMap = new MDataMap();
		//排除mi开头的用户编号
		if(StringUtils.isNotBlank(distributionMemberId)) {
			distributionMemberId = "MI"+StringUtils.substring(distributionMemberId, 2);
		}if(StringUtils.isNotBlank(distributionJuniorMemberId)) {
			distributionJuniorMemberId = "MI"+StringUtils.substring(distributionJuniorMemberId, 2);
		}
		//记录分销日志
		mDataMap.put("uid", WebHelper.upUuid());
		mDataMap.put("distribution_member_id",distributionMemberId);
		mDataMap.put("distribution_product_code",distributionProductCode );
		mDataMap.put("distribution_junior_member_id",StringUtils.isBlank(distributionJuniorMemberId)?"": distributionJuniorMemberId);
		mDataMap.put("create_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("lc_distribution_info").dataInsert(mDataMap);
//		//更新分销信息表
//		Map<String, Object> dataSqlOne = DbUp.upTable("lc_distribution_info").dataSqlOne("select count(distinct uid) share_click_num,count(distinct distribution_product_code) share_product_num from lc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distributionMemberId));
//		int count = DbUp.upTable("oc_distribution_info").count("distribution_member_id",distributionMemberId);
//		if(count==0) {
//			//不存在,添加
//			MDataMap upMemberInfo = getUserInfoByCode(distributionMemberId);
//			String nickname = upMemberInfo.get("nickname");
//			String phoneNum = upMemberInfo.get("login_name");
//			DbUp.upTable("oc_distribution_info").dataInsert(new MDataMap("uid",WebHelper.upUuid(),"distribution_member_id",distributionMemberId,"distribution_member_name",nickname,"create_time",DateUtil.getSysDateTimeString(),"share_product_num",dataSqlOne.get("share_product_num").toString(),"share_click_num",dataSqlOne.get("share_click_num").toString(),"phone_num",phoneNum));
//		}else {
//			//已存在,更新
//			 DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("share_product_num",dataSqlOne.get("share_product_num").toString(),"share_click_num",dataSqlOne.get("share_click_num").toString(),"distribution_member_id",distributionMemberId), "share_click_num,share_product_num", "distribution_member_id");
//		}
		return result;
	}

	private MDataMap getUserInfoByCode(String userCode) {
		MDataMap userInfo = DbUp.upTable("mc_member_sync").one("member_code",userCode);
		if(userInfo == null || userInfo.isEmpty()) {
			return null;
		}
		String nickname = userInfo.get("nickname");
		String loginname = userInfo.get("login_name");
		if(nickname.equals(loginname)&&nickname.length() == 11) {//昵称就是手机号的时候，需要处理昵称
			nickname = nickname.substring(0, 3)+"****"+nickname.substring(8,11);
		}
		userInfo.put("nickname",nickname);
		return userInfo;
	}
	
}
