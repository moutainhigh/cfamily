package com.cmall.familyhas.model;




/**   
 * 	活动
*    xiegj
*/
public class NewsNotificationModel  {
	
	private String noticeType;
	private String noticeTopic;
	private String publishTime;  
	private String readFlag =  "0";//0为未读  1为已读
	private String newsCode = "";
	private String newsUid = "";
	
	
	
	public String getNewsUid() {
		return newsUid;
	}
	public void setNewsUid(String newsUid) {
		this.newsUid = newsUid;
	}
	public String getNewsCode() {
		return newsCode;
	}
	public void setNewsCode(String newsCode) {
		this.newsCode = newsCode;
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
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	
	
	
}

