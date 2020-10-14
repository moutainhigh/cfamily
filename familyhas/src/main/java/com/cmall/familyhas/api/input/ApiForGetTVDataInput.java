package com.cmall.familyhas.api.input;

import com.cmall.familyhas.api.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForGetTVDataInput extends RootInput {

	@ZapcomApi(value = "日期", remark = "可为空，默认查询当日数据", demo = "2014-08-08")
	private String date = "";
	
	@ZapcomApi(value = "会员编号",remark = "输入会员的编号")
	private String vipNo = "";
	
	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10")
	private PageOption paging = new PageOption();
	
	@ZapcomApi(value = "品牌编号" ,demo= "467703130008000100070001",require = 1,verify={ "in=467703130008000100070001" })
	private String activity = "467703130008000100070001";
	
	@ZapcomApi(value = "排序",remark = "排列顺序，正序/倒序,0:正序,1:倒序,其余(惠家有为倒序，其余为正序)")
	private String sort = "";
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value="图片宽度",demo="600")
	private Integer picWidth = new Integer(0);
	
	@ZapcomApi(value="预播档数编码",demo="4497471600350001",remark="预播的商品场数")
	private String futureProgramCode = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * 获取  sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * 设置 
	 * @param sort 
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public String getFutureProgramCode() {
		return futureProgramCode;
	}

	public void setFutureProgramCode(String futureProgramCode) {
		this.futureProgramCode = futureProgramCode;
	}

	
	
}
