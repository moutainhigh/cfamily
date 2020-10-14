package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * @title: com.cmall.familyhas.api.input.QuestionOnlineInput.java 
 * @description: 请求参数设置
 			449747890001:客服与帮助
			449747890002:跨境通商品详情
			449747890003:平台入驻 
 * @author Yangcl
 * @date 2016年9月21日 下午5:16:11 
 * @version 1.0.0
 */
public class QuestionOnlineInput extends RootInput{
	@ZapcomApi(value="449747890001:客服与帮助|449747890002:跨境通商品详情|449747890003:平台入驻")
	private String type = "";

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
