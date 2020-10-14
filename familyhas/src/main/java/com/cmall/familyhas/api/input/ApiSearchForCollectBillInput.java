package com.cmall.familyhas.api.input;

import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

import java.math.BigDecimal;

import com.cmall.familyhas.api.result.PriceChooseObj;

/**
 * 搜索输入参数
 * @author zhangbo
 *
 */
public class ApiSearchForCollectBillInput extends RootInput{
	
	@ZapcomApi(value = "品类编号")
	private String categoryCode = "";
	
	@ZapcomApi(value="排序字段",remark="0、默认；1、销量；2、上架时间；3、价格；4、人气;5、top50,默认为：0")
	private int sortType=0;
	
	@ZapcomApi(value="正序倒序",remark="1、正序；2、倒序，默认为：2")
	private int sortFlag=0;
	
	@ZapcomApi(value="价格区间对象")
	private PriceChooseObj PriceChooseObj = new PriceChooseObj();
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value="每页读取记录数",remark="默认为:10")
	private int pageSize=20;
	
	@ZapcomApi(value="读取页码",remark="默认为：1")
	private int pageNo=1;

	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value="换购活动")
	private RepurchaseEvent repurchaseEvent=new RepurchaseEvent();
	
	@ZapcomApi(value = "购物车总金额")
	private BigDecimal allPay =new BigDecimal(0);
	
	
	public BigDecimal getAllPay() {
		return allPay;
	}

	public void setAllPay(BigDecimal allPay) {
		this.allPay = allPay;
	}

	public RepurchaseEvent getRepurchaseEvent() {
		return repurchaseEvent;
	}

	public void setRepurchaseEvent(RepurchaseEvent repurchaseEvent) {
		this.repurchaseEvent = repurchaseEvent;
	}

	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public int getSortType() {
		return sortType;
	}

	public void setSortType(int sortType) {
		this.sortType = sortType;
	}

	public int getSortFlag() {
		return sortFlag;
	}

	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}

	public PriceChooseObj getPriceChooseObj() {
		return PriceChooseObj;
	}

	public void setPriceChooseObj(PriceChooseObj priceChooseObj) {
		PriceChooseObj = priceChooseObj;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


}
	
