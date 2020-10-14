package com.cmall.familyhas.api.input;

import com.cmall.familyhas.api.model.PriceRange;
import com.cmall.familyhas.api.result.PriceChooseObj;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 满减活动输入 参数
 * @author zhouguohui
 *
 */
public class ApiEventFullCutProductInput extends RootInput{
	@ZapcomApi(value="活动编号",require=1,remark="当前页面的活动编号，如果当前活动编号不是满减返回为空")
	private String activityCode="";
	@ZapcomApi(value="当前页面",require=1,remark="当前页  默认为第一页")
	private int pageNo=1;
	@ZapcomApi(value="每页多少条",require=1,remark="每页显示多少条数据    默认显示10条")
	private int pageSize=10;
	@ZapcomApi(value="活动排序",require=1,remark="1: 默认 2: 销量  3:价格升序 4:价格降序  默认是按系统的zid排序         默认都是倒叙排列")
	private int sortType=1;
	@ZapcomApi(value = "活动开始时间", require=1,remark="活动的开始时间")
	private String beginTime = "";
	@ZapcomApi(value = "活动结束时间",require=1,remark="活动的结束时间")
	private String endTime = "";
	@ZapcomApi(value="价格区间对象")
	private PriceRange priceChooseObj = new PriceRange();
	@ZapcomApi(value = "品类编号")
	private String categoryCode = "";
	/**
	 * @return the activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}
	/**
	 * @param activityCode the activityCode to set
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}
	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the sortType
	 */
	public int getSortType() {
		return sortType;
	}
	/**
	 * @param sortType the sortType to set
	 */
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public PriceRange getPriceChooseObj() {
		return priceChooseObj;
	}
	public void setPriceChooseObj(PriceRange priceChooseObj) {
		this.priceChooseObj = priceChooseObj;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	
}
