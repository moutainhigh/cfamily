package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiYyMsgUpInput;
import com.cmall.familyhas.api.result.ApiYyMsgUpResult;
import com.srnpr.xmassystem.util.XSSUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiYyMsgUp extends RootApiForToken<ApiYyMsgUpResult, ApiYyMsgUpInput> {
	private final String MSG_WORD = "449748250001";//文字留言
	private final String MSG_YY = "449748250002";//语音留言
	
	@Override
	public ApiYyMsgUpResult Process(ApiYyMsgUpInput inputParam, MDataMap mRequestMap) {
		ApiYyMsgUpResult apiResult = new ApiYyMsgUpResult();
		
		String msgCode = WebHelper.upCode("LY");
		String userCode = getUserCode();
		String msgType = inputParam.getMsgType();
		String msgContent = inputParam.getMsgContent();
		String userMobile = getOauthInfo().getLoginName();
		List<String> pathList = inputParam.getImagePath();
		
		if(!MSG_WORD.equals(msgType) && !MSG_YY.equals(msgType)) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("参数有误!");
			return apiResult;
		}
		if("".equals(msgContent)) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("参数有误!");
			return apiResult;
		}
		if(msgContent.length() > 300) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("文字留言过长!");
			return apiResult;
		}
		
		if(XSSUtils.hasXSS(msgContent)) {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("文字留言包含非法字符!");
			return apiResult;
		}
		
		MDataMap mDataMap = new MDataMap();
		mDataMap.put("msg_code", msgCode);
		mDataMap.put("msg_type", msgType);
		mDataMap.put("msg_content", msgContent);
		mDataMap.put("user_mobile", userMobile);
		mDataMap.put("user_code", userCode);
		mDataMap.put("create_time", FormatHelper.upDateTime());
		String infoId = DbUp.upTable("uc_user_words").dataInsert(mDataMap);
		
		for(String path : pathList) {
			MDataMap image = new MDataMap();
			image.put("word_id", infoId);
			image.put("image_path", path);
			image.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("uc_user_words_img").dataInsert(image);
		}
		return apiResult;
	}
}
