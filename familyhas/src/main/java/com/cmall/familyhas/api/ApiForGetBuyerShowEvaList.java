package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForGetBuyerShowEvaListInput;
import com.cmall.familyhas.api.model.BuyerShowEvaluation;
import com.cmall.familyhas.api.result.ApiForGetBuyerShowEvaListResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 分页获取单个买家秀评论列表数据
 * @author lgx
 * 
 */
public class ApiForGetBuyerShowEvaList extends RootApiForVersion<ApiForGetBuyerShowEvaListResult, ApiForGetBuyerShowEvaListInput> {
	
	static final int pageSize = 20;
	
	public ApiForGetBuyerShowEvaListResult Process(ApiForGetBuyerShowEvaListInput inputParam, MDataMap mRequestMap) {
		ApiForGetBuyerShowEvaListResult result = new ApiForGetBuyerShowEvaListResult();//返回结果
		HomeColumnService hcService = new HomeColumnService();
		
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		// 第几页
		int page = inputParam.getPage();
		String buyerShowUid = inputParam.getBuyerShowUid();
		// 起始索引
		int index = (page-1) * pageSize;
		
		int totalPage = 1;
		String evaluationNum = "0";
		List<BuyerShowEvaluation> buyerShowList = new ArrayList<BuyerShowEvaluation>();
		
		String sql = "SELECT * FROM nc_buyer_show_evaluation WHERE buyer_show_uid = '"+buyerShowUid+"' ORDER BY create_time DESC LIMIT "+index+"," + pageSize;
		List<Map<String, Object>> showEvaMapList = DbUp.upTable("nc_buyer_show_evaluation").dataSqlList(sql, new MDataMap());
		if(showEvaMapList != null && showEvaMapList.size() > 0) {
			for (Map<String, Object> map : showEvaMapList) {
				BuyerShowEvaluation buyerShowEvaluation = new BuyerShowEvaluation();
				String buyerShowEvaUid = (String) map.get("uid");
				String memberCode = (String) map.get("member_code");
				String contentEvaluation = (String) map.get("content_evaluation");
				String createTime = (String) map.get("create_time");
				// 晒单人头像
				String avatar = "";
				// 晒单人昵称
				String nickname = "";
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"' ORDER BY last_update_time DESC LIMIT 1", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003'", new MDataMap());
					nickname = (String) login_info.get("login_name");
					if(hcService.isPhone(nickname)) {
						nickname = nickname.substring(0, 3) + "****" + nickname.substring(7);
					}
				}else { // 如果昵称不是空
					nickname = (String) member_sync.get("nickname");
				}
				// 头像
				if(null != member_sync && null != member_sync.get("avatar") && !"".equals(member_sync.get("avatar"))){
					avatar = (String) member_sync.get("avatar");
				}
				// 买家秀评论点赞量
				String evaApproveNum = "0";
				int approveCount = DbUp.upTable("nc_buyer_show_approve").count("buyer_show_or_buyer_show_eva_uid",buyerShowEvaUid);
				if(approveCount > 999) {
					evaApproveNum = "999+";
				}else {
					evaApproveNum = approveCount+"";
				}
				// 当前用户是否点赞
				String isApprove = "0";
				MDataMap one = DbUp.upTable("nc_buyer_show_approve").one("buyer_show_or_buyer_show_eva_uid",buyerShowEvaUid,"member_code",userCode);
				if(one != null) {
					isApprove = "1";
				}
				
				buyerShowEvaluation.setAvatar(avatar);
				buyerShowEvaluation.setBuyerShowEvaUid(buyerShowEvaUid);
				buyerShowEvaluation.setContentEvaluation(contentEvaluation);
				buyerShowEvaluation.setCreateTime(createTime);
				buyerShowEvaluation.setEvaApproveNum(evaApproveNum);
				buyerShowEvaluation.setMemberCode(memberCode);
				buyerShowEvaluation.setNickname(nickname);
				buyerShowEvaluation.setIsApprove(isApprove);
				
				buyerShowList.add(buyerShowEvaluation);
			}
		}
		
		int evaluationCount = DbUp.upTable("nc_buyer_show_evaluation").count("buyer_show_uid",buyerShowUid);
		if(evaluationCount > 999) {
			evaluationNum = "999+";
		}else {
			evaluationNum = evaluationCount+"";
		}
		
		String countSql = "SELECT count(1) num FROM nc_buyer_show_evaluation WHERE buyer_show_uid = '"+buyerShowUid+"' ";
		Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_evaluation").dataSqlOne(countSql, new MDataMap());
		double num = MapUtils.getDoubleValue(countMap, "num");
		// 总页数
		totalPage = (int) Math.ceil(num/pageSize);
		
		result.setBuyerShowList(buyerShowList);
		result.setEvaluationNum(evaluationNum);
		result.setTotalPage(totalPage);
		
		return result;
	}
	
	
}
