package com.cmall.familyhas.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.cmall.familyhas.api.input.ApiDgUserBehaviourInput;
import com.cmall.familyhas.api.model.DgUserBehaviourModel;
import com.cmall.familyhas.api.result.ApiDgUserBehaviourResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.HttpClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiDgUserBehaviour extends RootApiForVersion<ApiDgUserBehaviourResult, ApiDgUserBehaviourInput> {

	@Override
	public ApiDgUserBehaviourResult Process(ApiDgUserBehaviourInput inputParam, MDataMap mRequestMap) {
		ApiDgUserBehaviourResult apiResult = new ApiDgUserBehaviourResult();

		// 判断配置参数是否要屏蔽
		if (!StringUtils.contains(bConfig("familyhas.recommend_up_config"), "dg_up")) {
			return apiResult;
		}

		String userid = "";
		if (getOauthInfo() != null) {
			userid = getOauthInfo().getUserCode();
		}
		
		JSONObject content;
		JSONArray contents = new JSONArray();
		int recShowSize = 0;
		List<DgUserBehaviourModel> modelList = inputParam.getModelList();
		for (DgUserBehaviourModel model : modelList) {
			content = new JSONObject();
			content.put("cmd", "add");
			
			String action_type = model.getUserAction();
			int action_num = model.getActionNum();
			long timestamp = new Date().getTime() / 1000;
			String rec_requestid = model.getDgRequestId();
			String keyword = model.getSearchTxt();
			String productCode = model.getProductCode();
			int playTime = Integer.parseInt(StringUtils.isNotBlank(model.getPlayTime()) ? model.getPlayTime() : "0");
			// 校验关键字段action_type是否合规
			if (!"view".equals(action_type) && !"play".equals(action_type) && !"rec_click".equals(action_type) && !"rec_show".equals(action_type)
					&& !"collect".equals(action_type) && !"comment".equals(action_type) && !"share".equals(action_type) && !"cart".equals(action_type)
					&& !"buy".equals(action_type) && !"search".equals(action_type)) {
				//apiResult.setResultCode(0);
				//apiResult.setResultMessage("调用接口参数有误,请联系管理员!");
				continue;
			}
			if ("search".equals(action_type) && "".equals(keyword)) {
				//apiResult.setResultCode(0);
				//apiResult.setResultMessage("调用接口参数有误,请联系管理员!");
				continue;
			}
			
			// 忽略商品编号值是链接的情况
			if (productCode != null && productCode.contains("http")) {
				continue;
			}
			
			// 忽略橙意会员卡商品
			if(productCode != null && productCode.equals(bConfig("xmassystem.plus_product_code"))) {
				continue;
			}

			JSONObject fields = new JSONObject();
			fields.put("userid", userid);
			fields.put("action_type", action_type);
			fields.put("action_num", action_num);
			fields.put("timestamp", timestamp);
			if (!"".equals(inputParam.getImei())) {
				fields.put("imei", inputParam.getImei());
			}
			if (!"".equals(inputParam.getcId())) {
				fields.put("cid", inputParam.getcId());
			}
			if ("rec_click".equals(action_type) || "rec_show".equals(action_type)) {
				fields.put("rec_requestid", rec_requestid);
			}

			if ("search".equals(action_type)) {
				fields.put("keyword", keyword);
			} else {
				fields.put("itemid", productCode);
				if ("view".equals(action_type)) {
					fields.put("play_time", playTime);
				}
				
				// 忽略没有itemid的数据
				if(StringUtils.isBlank(productCode)) {
					continue;
				}
			}

			content.put("fields", fields);
			contents.add(content);
			
			// 仅记录rec_show用于排查问题
			if("rec_show".equals(action_type)) {
				recShowSize++;
			}
		}
		
		if(contents.isEmpty()) {
			return apiResult;
		}
		
		String os = StringUtils.trimToEmpty(getApiClient().get("os"));
		String app_vision = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("api_type", "dg_up");
		mDataMap.put("app_platform", os);
		mDataMap.put("app_version", app_vision);
		mDataMap.put("member_code", userid);
		mDataMap.put("data_size", recShowSize+"");
		mDataMap.put("request_time", FormatHelper.upDateTime());
		mDataMap.put("response_time", "");
		mDataMap.put("fail_content", "");
		
		try {
			String resultChar = doDgUp(contents);
			mDataMap.put("response_time", FormatHelper.upDateTime());
			
			JSONObject resultObject = JSONObject.fromObject(resultChar);
			String status = resultObject.getString("status");
			if ("FAIL".equals(status)) {
				apiResult.setResultCode(0);
				apiResult.setResultMessage("调用达观返回失败!");
			}
			
			if(!"OK".equals(status)) {
				mDataMap.put("fail_content", resultChar);
			}
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setResultCode(0);
			apiResult.setResultMessage("调用接口报错,请联系管理员!");
			// e.printStackTrace();
			mDataMap.put("fail_content", e+"");
			mDataMap.put("response_time", FormatHelper.upDateTime());
		}
		
		// 记录达观接口调用日志
		if(StringUtils.isNotBlank(mDataMap.get("fail_content"))) {
			DbUp.upTable("lc_dg_api_log").dataInsert(mDataMap);
		}
		
		return apiResult;
	}

	private String doDgUp(JSONArray contents) throws Exception {
		String url = bConfig("productcenter.dg_up_url") + bConfig("productcenter.dg_app_name");
		String appId = bConfig("productcenter.dg_app_id");

		JSONObject goodParams = new JSONObject();
		goodParams.put("appid", appId);
		goodParams.put("table_name", "user_action");
		goodParams.put("table_content", contents);

		String result = HttpClientSupport.doPostDg(url, goodParams.toString());
		return result;
	}
}
