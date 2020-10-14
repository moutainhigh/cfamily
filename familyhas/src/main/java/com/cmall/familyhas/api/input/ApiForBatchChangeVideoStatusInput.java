package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBatchChangeVideoStatusInput extends RootInput{

	private String videoCodes ="";
	private String nextStatus ="";
	private String remark ="";
	private String fromStatus ="";
	
	
	public String getFromStatus() {
		return fromStatus;
	}
	public void setFromStatus(String fromStatus) {
		this.fromStatus = fromStatus;
	}
	public String getVideoCodes() {
		return videoCodes;
	}
	public void setVideoCodes(String videoCodes) {
		this.videoCodes = videoCodes;
	}
	public String getNextStatus() {
		return nextStatus;
	}
	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	
	


}
