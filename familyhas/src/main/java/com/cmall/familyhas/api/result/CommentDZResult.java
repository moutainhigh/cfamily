package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class CommentDZResult extends RootResultWeb{
	
	@ZapcomApi(value="评论赞成总数",require=1)
	private int approveNum = 0;

	@ZapcomApi(value="评论反对总数",require=1)
	private int opposeNum = 0 ;
	
	@ZapcomApi(value="",remark="Y赞成 N 无操作",demo="Y",require=1)
	private String approveFlag = "";
	
	@ZapcomApi(value="",remark="Y反对 N 无操作",demo="Y",require=1)
	private String opposeFlag = "";
	
	

	public int getApproveNum() {
		return approveNum;
	}

	public void setApproveNum(int approveNum) {
		this.approveNum = approveNum;
	}

	public int getOpposeNum() {
		return opposeNum;
	}

	public void setOpposeNum(int opposeNum) {
		this.opposeNum = opposeNum;
	}

	public String getApproveFlag() {
		return approveFlag;
	}

	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}

	public String getOpposeFlag() {
		return opposeFlag;
	}

	public void setOpposeFlag(String opposeFlag) {
		this.opposeFlag = opposeFlag;
	}


	
	
	
}
