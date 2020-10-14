package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class CommentDZInput extends RootInput{

	@ZapcomApi(value="用户编号",remark="用户编号",demo="MI151125101955")
	private String userCode = "";

	@ZapcomApi(value="评论uid",remark="评论uid")
	private String evalutionUid = "" ;
	
	@ZapcomApi(value="点赞标识",remark="Y+赞成 ,Y-取消赞成, N+反对,N-取消反对",demo="Y+",require=1)
	private String flag = "";

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getEvalutionUid() {
		return evalutionUid;
	}

	public void setEvalutionUid(String evalutionUid) {
		this.evalutionUid = evalutionUid;
	}


	
	
	
	
}
