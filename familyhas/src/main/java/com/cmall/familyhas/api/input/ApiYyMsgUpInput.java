package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiYyMsgUpInput extends RootInput {
	@ZapcomApi(value="留言类型", require = 1, remark="449748250001:文字留言,449748250002:语音留言")
	private String msgType = "449748250001";
	@ZapcomApi(value="留言内容", require = 1, remark="文字留言为文字,语音留言时为语音文件路径")
	private String msgContent = "";
	@ZapcomApi(value="图片路径", remark="上传图片的路径")
	private List<String> imagePath = new ArrayList<String>();
	
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	public List<String> getImagePath() {
		return imagePath;
	}
	public void setImagePath(List<String> imagePath) {
		this.imagePath = imagePath;
	}
}
