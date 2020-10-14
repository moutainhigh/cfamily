package com.cmall.familyhas.api;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.LogFactory;

import com.cmall.familyhas.api.input.ApiForAgentQueryFansInput;
import com.cmall.familyhas.api.result.ApiForAgentQueryFansInfo;
import com.cmall.familyhas.api.result.ApiForAgentQueryFansResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 查询粉丝接口
 */
public class ApiForAgentQueryFans extends RootApiForVersion<ApiForAgentQueryFansResult, ApiForAgentQueryFansInput> {

	static int pageSize = 10;
	
	@Override
	public ApiForAgentQueryFansResult Process(ApiForAgentQueryFansInput inputParam, MDataMap mRequestMap) {
		ApiForAgentQueryFansResult apiResult = new ApiForAgentQueryFansResult();
		
		if(!getFlagLogin()) {
			return apiResult;
		}
		
		int nextPage = NumberUtils.toInt(inputParam.getNextPage(), 1);
		if(nextPage < 1) nextPage = 1;
		
		MDataMap mQueryMap = new MDataMap();
		mQueryMap.put("user_code", getOauthInfo().getUserCode());
		
		String whereSql = " AND ami.parent_code = :user_code ";
		
		Date startDate = null,endDate = null;
		try {
			startDate = DateUtils.parseDate(inputParam.getStartDate(), "yyyy-MM-dd");
			endDate = DateUtils.parseDate(inputParam.getEndDate(), "yyyy-MM-dd");
		} catch (ParseException e) {
			startDate = null;
			endDate = null;
			LogFactory.getLog(getClass()).warn("日期格式错误:["+inputParam.getStartDate()+", "+inputParam.getEndDate()+"]");
		}
		
		// 开始时间包含当天
		if(startDate != null) {
			whereSql += " AND ami.create_time >= '"+DateFormatUtils.format(startDate, "yyyy-MM-dd")+"'";
		}
		
		// 结束时间也包含当天
		if(endDate != null) {
			whereSql += " AND ami.create_time < '"+DateFormatUtils.format(DateUtils.addDays(endDate, 1), "yyyy-MM-dd")+"'";
		}
		
		// 查询总数
		String countSql = "SELECT count(DISTINCT ms.member_code) num FROM `fh_agent_member_info` ami, membercenter.mc_member_sync ms WHERE ami.member_code = ms.member_code ";
		countSql += whereSql;
		Map<String, Object> totalMap = DbUp.upTable("fh_agent_member_info").dataSqlOne(countSql, mQueryMap);
		int totalNum = NumberUtils.toInt(totalMap.get("num").toString(), 0);
		
		// 计算总页数
		int totalPage = totalNum/pageSize;
		if(totalNum % pageSize > 0) {
			totalPage++;
		}
		apiResult.setCountPage(totalPage);
		apiResult.setNowPage(nextPage);
		
		// 查询明细
		String memberSql = "SELECT ms.login_name,ms.nickname,avatar,ami.create_time FROM `fh_agent_member_info` ami, membercenter.mc_member_sync ms WHERE ami.member_code = ms.member_code ";
		memberSql += whereSql;
		memberSql += " ORDER BY ami.create_time desc LIMIT "+(nextPage-1)*pageSize+", "+pageSize;
		List<Map<String, Object>> dataList = DbUp.upTable("fh_agent_member_info").dataSqlList(memberSql, mQueryMap);
		
		ApiForAgentQueryFansInfo fansInfo;
		for(Map<String, Object> item : dataList) {
			fansInfo = new ApiForAgentQueryFansInfo();
			fansInfo.setAvatar(StringUtils.trimToEmpty((String)item.get("avatar")));
			fansInfo.setNickname(StringUtils.trimToEmpty((String)item.get("nickname")));
			fansInfo.setRegisterTime((String)item.get("create_time"));
			fansInfo.setPhone(getShowPhone((String)item.get("login_name")));
			apiResult.getFansList().add(fansInfo);
		}
		
		return apiResult;
	}
	
	private String getShowPhone(String phone) {
		if(phone.length() < 6) return phone;
		if(phone.length() < 11) return phone.substring(0, 3)+"******";
		return phone.substring(0, 3)+"****"+phone.substring(7, 11);
	}
	
}
