package com.cmall.familyhas.api.result;

import java.util.List;
import com.cmall.ordercenter.model.ActivityInfos;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiUserSignResult extends RootResultWeb{

	private String userCode;
	
	@ZapcomApi(value="连续签到天数",remark="连续签到天数,默认为0")
	private Integer signSeqDays = 0;//连续签到天数
	
	@ZapcomApi(value="今日签到标记",remark="1为已签 0为未签")
	private Integer flagSignToday;//今日签到标记
	
	private String signTime;
	
	private String signGetCouponCode;
	
	@ZapcomApi(value="今日签到获取优惠券名称",remark="如果有值，代表今天签到获得优惠券，已拼接为 n元优惠券,m张折扣券 格式")
	private String signGetCouponName;
	
	@ZapcomApi(value="获取下一优惠券所需天数",remark="获取下一优惠券所需天数")
	private Integer getNextCouponDays;
	
	private String nextCouponCode;
	
	@ZapcomApi(value="下一优惠券名称",remark="已拼接为 n元优惠券,m张优惠券 格式")
	private String nextCouponName;
	
	private String nextActivityCode;

	@ZapcomApi(value="天数对应活动",remark="仅用于查询*活动集合  days为天数(格式:3)，jinEQuan为天数对应金额券总金额(格式:5),zheKouQuan为天数对应折扣券张数(格式:1)")
	private List<ActivityInfos> activityInfos;

	@ZapcomApi(value="当前用户是否打开签到提醒",remark="1为提醒，0为不提醒")
	private String signRemindFlag;
	
	
	
	
	
	public String getSignRemindFlag() {
		return signRemindFlag;
	}
	public void setSignRemindFlag(String signRemindFlag) {
		this.signRemindFlag = signRemindFlag;
	}
	public List<ActivityInfos> getActivityInfos() {
		return activityInfos;
	}
	public void setActivityInfos(List<ActivityInfos> activityInfos) {
		this.activityInfos = activityInfos;
	}
	public String getNextActivityCode() {
		return nextActivityCode;
	}
	public void setNextActivityCode(String nextActivityCode) {
		this.nextActivityCode = nextActivityCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getSignSeqDays() {
		return signSeqDays;
	}
	public void setSignSeqDays(Integer signSeqDays) {
		this.signSeqDays = signSeqDays;
	}
	public Integer getFlagSignToday() {
		return flagSignToday;
	}
	public void setFlagSignToday(Integer flagSignToday) {
		this.flagSignToday = flagSignToday;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getSignGetCouponCode() {
		return signGetCouponCode;
	}
	public void setSignGetCouponCode(String signGetCouponCode) {
		this.signGetCouponCode = signGetCouponCode;
	}
	public String getSignGetCouponName() {
		return signGetCouponName;
	}
	public void setSignGetCouponName(String signGetCouponName) {
		this.signGetCouponName = signGetCouponName;
	}
	public Integer getGetNextCouponDays() {
		return getNextCouponDays;
	}
	public void setGetNextCouponDays(Integer getNextCouponDays) {
		this.getNextCouponDays = getNextCouponDays;
	}
	public String getNextCouponCode() {
		return nextCouponCode;
	}
	public void setNextCouponCode(String nextCouponCode) {
		this.nextCouponCode = nextCouponCode;
	}
	public String getNextCouponName() {
		return nextCouponName;
	}
	public void setNextCouponName(String nextCouponName) {
		this.nextCouponName = nextCouponName;
	}
	
}
