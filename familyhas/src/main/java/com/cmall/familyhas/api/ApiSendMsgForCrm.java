package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.SendMsgInput;
import com.cmall.systemcenter.message.SendMessageBase;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiSendMsgForCrm extends RootApi<RootResult,SendMsgInput>{

	@Override
	public RootResult Process(SendMsgInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult msgSendResult = new RootResult();
		String[] mobiles = inputParam.getMobileList().split(",");
		String content = inputParam.getContent();
		String send_source = inputParam.getSend_source();
		
		//发送短信
		for(String mobile:mobiles){
			SendMessageBase messageBase=new SendMessageBase();
			messageBase.sendMessage(mobile, content,send_source);
		}
		

		return msgSendResult;
	}
	
	

}
