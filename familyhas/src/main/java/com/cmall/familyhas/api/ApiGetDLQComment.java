package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetDLQCommentInput;
import com.cmall.familyhas.api.model.DLQCommentModel;
import com.cmall.familyhas.api.result.ApiGetDLQCommentResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 获取评论列表
 * @author fq
 *
 */
public class ApiGetDLQComment extends RootApiForManage<ApiGetDLQCommentResult, ApiGetDLQCommentInput> {

	@Override
	public ApiGetDLQCommentResult Process(ApiGetDLQCommentInput inputParam, MDataMap mRequestMap) {

		ApiGetDLQCommentResult result = new  ApiGetDLQCommentResult();
		result.setCommentList(this.getDLQCommentList(inputParam.getSource()));
		
		
		return result;
		
	}
	
	/**
	 * 转换ip
	 * @param ip
	 * @return
	 */
	private String IPRevert (String ip ) {
		
		if(StringUtils.isNotBlank(ip)) {
			
			String[] split = ip.split("\\.");
			
			return split[0]+"****"+split[split.length-1];
			
		} else {
			return "";
		}
		
	}
	
	private String MobileRevert(String mobile) {
		
		String returnStr = "";
		
		if(StringUtils.isNotBlank(mobile)) {
			returnStr = mobile.substring(0, 3)+"****"+mobile.substring(mobile.length()-4);
		} 
		
		return returnStr;
		
		
	}
	/**
	 * 时间格式化
	 * @return
	 */
	private String timeSplit (String time) {
		
		if(StringUtils.isNotBlank(time)) {
			return time.split(" ")[0];
		} else {
			return "";
		}
		
	}
	
	public List<DLQCommentModel> getDLQCommentList(String source) {
		
		List<DLQCommentModel> commentList = new ArrayList<DLQCommentModel>();
		List<MDataMap> queryAll = DbUp.upTable("fh_dlq_comment").queryAll("mobile,u_ip,content,create_time,rtn_content,rtn_time",
				"-create_time", " c_status = '1001' and source = :source ", new MDataMap("source",source));

		Map<String,String> userInfo = new HashMap<String, String>();
		StringBuffer mobileBuffer = new StringBuffer();
		for (MDataMap mDataMap : queryAll) {
			if(StringUtils.isNotBlank(mDataMap.get("mobile"))) {
				mobileBuffer.append("'"+mDataMap.get("mobile")+"',");
			}
		}
		if(queryAll.size() > 0) {
			String allMobile = "";
			List<Map<String, Object>> dataSqlList = new ArrayList<Map<String, Object>>();
			if (mobileBuffer.length() > 0) {
				allMobile = mobileBuffer.substring(0, mobileBuffer.length()-1);
				String sSql = " SELECT l.member_avatar as member_avatar,r.login_name as login_name FROM membercenter.mc_extend_info_star l JOIN membercenter.mc_login_info r ON l.member_code = r.member_code AND r.manage_code = 'SI2003' AND l.app_code = 'SI2003' AND r.login_name IN ("+allMobile+") ";
				
				dataSqlList = DbUp.upTable("mc_extend_info_star").dataSqlList(sSql, new MDataMap());
			}
			for (Map<String, Object> map : dataSqlList) {
				userInfo.put(String.valueOf(map.get("login_name")), String.valueOf(map.get("member_avatar")));
			}
			
			for (MDataMap mDataMap : queryAll) {
				DLQCommentModel commentModel = new DLQCommentModel();
				
				commentModel.setC_ip(this.IPRevert(mDataMap.get("u_ip")));
				commentModel.setMobile(this.MobileRevert(mDataMap.get("mobile")));
				commentModel.setContent(mDataMap.get("content"));
				commentModel.setC_time(this.timeSplit(mDataMap.get("create_time")));
				
				if(userInfo.containsKey(mDataMap.get("mobile"))) {
					commentModel.setHead_photo(userInfo.get(mDataMap.get("mobile")));
				}
				/*
				 * 添加追评内容
				 */
				commentModel.setRtn_content(mDataMap.get("rtn_content"));
				commentModel.setRtn_time(mDataMap.get("rtn_time").split(" ")[0]);
				commentModel.setRtn_user(bConfig("familyhas.dlq_rtn_name"));
				
				commentList.add(commentModel);
				
			}
		}
		
		return commentList;
		
	}
	
}
