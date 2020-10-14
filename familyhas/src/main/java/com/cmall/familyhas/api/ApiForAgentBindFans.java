package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForAgentBindFansInput;
import com.cmall.familyhas.api.result.ApiForAgentBindFansResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 粉丝接受分销人邀请接口
 */
public class ApiForAgentBindFans extends RootApiForVersion<ApiForAgentBindFansResult, ApiForAgentBindFansInput> {

	@Override
	public ApiForAgentBindFansResult Process(ApiForAgentBindFansInput inputParam, MDataMap mRequestMap) {
		ApiForAgentBindFansResult apiResult = new ApiForAgentBindFansResult();
		
		// 修正前端传入的用户编号mi开头是小写的问题
		inputParam.setFxrCode(StringUtils.trimToEmpty(inputParam.getFxrCode()).toUpperCase());
		
		if(!getFlagLogin()) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("登录信息过期，请重新登录！");
			return apiResult;
		}
		
		if(DbUp.upTable("mc_login_info").count("member_code",inputParam.getFxrCode()) == 0) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("分享人信息不存在！");
			return apiResult;
		}
		
		// 内部销售人员不能参与分销流程
		if(DbUp.upTable("fh_waihu_member_info").count("member_code",getOauthInfo().getUserCode()) > 0) {
			//apiResult.setResultCode(0);
			//apiResult.setResultMessage("当前账户不能绑定分享人");
			return apiResult;
		}
		
		// 内部销售人员不能参与分销流程
		if(DbUp.upTable("fh_waihu_member_info").count("member_code",inputParam.getFxrCode()) > 0) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("分享人账户不允许绑定粉丝");
			return apiResult;
		}
		
		MDataMap agentInfo = DbUp.upTable("fh_agent_member_info").one("member_code",getOauthInfo().getUserCode());
		
		if(agentInfo != null) {
			if(agentInfo.get("level_code").equals("4497484600010001")) {
				apiResult.setResultCode(916425201);
				apiResult.setResultMessage("您是特邀用户不能成为粉丝！");
				return apiResult;
			} else {
				apiResult.setResultCode(916425201);
				apiResult.setResultMessage("您已经是粉丝了！");
				return apiResult;
			}
		}
		
		String lockKey = "ApiForAgentBindFans-"+getOauthInfo().getUserCode();
		String lockResult = KvHelper.lockCodes(10, lockKey);
		if(StringUtils.isBlank(lockResult)) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("操作太快了，请稍候再试！");
			return apiResult;
		}
		
		MDataMap memberInfo = DbUp.upTable("mc_member_sync").one("member_code", getOauthInfo().getUserCode());
		String nickname = "";
		if(memberInfo != null && StringUtils.isNotBlank(memberInfo.get("nickname"))) {
			nickname = memberInfo.get("nickname");
		}
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("member_code", getOauthInfo().getUserCode());
		insertMap.put("nickname", nickname);
		insertMap.put("level_code", "4497484600010002");
		insertMap.put("gender", "");
		insertMap.put("parent_code", inputParam.getFxrCode());
		insertMap.put("create_time", FormatHelper.upDateTime());
		DbUp.upTable("fh_agent_member_info").dataInsert(insertMap);
		
		// 更新粉丝数
		String sql = "update fh_agent_member_info set fans_num = fans_num + 1 where member_code = :member_code";
		DbUp.upTable("fh_agent_member_info").dataExec(sql, new MDataMap("member_code", inputParam.getFxrCode()));
		
		KvHelper.unLockCodes(lockResult, lockKey);
		
		return apiResult;
	}
	
}
