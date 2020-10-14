package com.cmall.familyhas.service;


import java.util.ArrayList;
import java.util.List;
import com.srnpr.zapcom.baseclass.BaseClass;
/**
 * 转换输入类
 * @author shiyz
 *
 */
public class NewsNotificationResult extends BaseClass {
	
	
	private String noticeType;
	private String noticeTopic;
	private String noticeContent;
	private String publishTime;
	private List<PunishModel> list = new ArrayList<>();
	private String nextUid  = "";
	private String preUid =  "";
	private String noReadCounts = "-1";
	
	
	public String getNoReadCounts() {
		return noReadCounts;
	}
	public void setNoReadCounts(String noReadCounts) {
		this.noReadCounts = noReadCounts;
	}
	public String getNextUid() {
		return nextUid;
	}
	public void setNextUid(String nextUid) {
		this.nextUid = nextUid;
	}
	public String getPreUid() {
		return preUid;
	}
	public void setPreUid(String preUid) {
		this.preUid = preUid;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getNoticeTopic() {
		return noticeTopic;
	}
	public void setNoticeTopic(String noticeTopic) {
		this.noticeTopic = noticeTopic;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public List<PunishModel> getList() {
		return list;
	}
	public void setList(List<PunishModel> list) {
		this.list = list;
	}
	
}




