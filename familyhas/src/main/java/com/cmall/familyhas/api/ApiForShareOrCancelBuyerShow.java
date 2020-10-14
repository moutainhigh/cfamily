package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForShareOrCancelBuyerShowInput;
import com.cmall.familyhas.api.result.ApiForShareOrCancelBuyerShowResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 评价详情页分享或者取消分享到买家秀接口
 * @author lgx
 * 
 */
public class ApiForShareOrCancelBuyerShow extends RootApiForVersion<ApiForShareOrCancelBuyerShowResult, ApiForShareOrCancelBuyerShowInput> {
	
	
	public ApiForShareOrCancelBuyerShowResult Process(ApiForShareOrCancelBuyerShowInput inputParam, MDataMap mRequestMap) {
		ApiForShareOrCancelBuyerShowResult result = new ApiForShareOrCancelBuyerShowResult();//返回结果
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String evaluationUid = inputParam.getEvaluationUid();
		String shareOrCancel = inputParam.getShareOrCancel();
		MDataMap order_evaluation = DbUp.upTable("nc_order_evaluation").one("uid",evaluationUid);
		if(order_evaluation==null) {
			result.setResultCode(-1);
			result.setResultMessage("查不到该条评论,操作失败");
			return result;
		}
		MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("evaluation_uid",evaluationUid);
		if(shareOrCancel.equals("0")) {
			// 取消,修改评价表状态,删除买家秀表
			MDataMap updateMap = new MDataMap();
			updateMap.put("uid", evaluationUid);
			updateMap.put("is_cancel", "1");
			DbUp.upTable("nc_order_evaluation").dataUpdate(updateMap,"is_cancel", "uid");
			if(buyer_show != null) {				
				MDataMap delDataMap = new MDataMap();
				delDataMap.put("uid", buyer_show.get("uid"));
				delDataMap.put("is_delete", "1");
				DbUp.upTable("nc_buyer_show_info").dataUpdate(delDataMap, "is_delete", "uid");
				result.setResultMessage("买家秀取消成功！");
			}else {
				result.setResultMessage("该买家秀不存在或者已取消！");
			}
		}else if(shareOrCancel.equals("1")) {
			// 分享,修改评价表状态,新增买家秀表
			if((StringUtils.isNotBlank(order_evaluation.get("oder_photos")) || StringUtils.isNotBlank(order_evaluation.get("ccvids"))) && Integer.parseInt(order_evaluation.get("grade"))>=4 && StringUtils.isNotBlank(order_evaluation.get("order_assessment"))) {
				MDataMap updateMap = new MDataMap();
				updateMap.put("uid", evaluationUid);
				updateMap.put("is_share", "1");
				DbUp.upTable("nc_order_evaluation").dataUpdate(updateMap,"is_share", "uid");
				if(buyer_show == null) {
					MDataMap insertMap = new MDataMap();
					insertMap.put("evaluation_uid", evaluationUid);
					insertMap.put("create_time", DateUtil.getSysDateTimeString());
					insertMap.put("check_status", "449748580001");
					insertMap.put("is_delete", "0");
					insertMap.put("member_code", userCode);
					DbUp.upTable("nc_buyer_show_info").dataInsert(insertMap);
					result.setResultMessage("买家秀分享成功！");
				}else {
					result.setResultMessage("该评论已经分享到买家秀！");
				}
			}else {
				//result.setResultMessage("不满足分享到买家秀条件！");
			}
		}else {
			result.setResultCode(-1);
			result.setResultMessage("系统异常,请刷新重试！");
			return result;
		}
		
		return result;
	}
	
	
}
