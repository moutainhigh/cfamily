package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.EventListGoodsEntity;
import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/***
 * 满减活动返回参数
 * @author zhangbo
 *
 */
public class ApiSearchForCollectBillResult extends RootResult{
	@ZapcomApi(value="价格区间列表")
	private List<PriceChooseObj> priceList=new ArrayList<PriceChooseObj>();

	@ZapcomApi(value="商品列表",remark="返回商品列表（按照活动列表格式进行数据封装）")
	private List<EventListGoodsEntity> productList = new ArrayList<EventListGoodsEntity>();
	
	@ZapcomApi(value="总页数",remark="返回商品总页数")
	private int pageNum;
	
	@ZapcomApi(value="总条数",remark="返回商品总条数")
	private int recordNum;

	@ZapcomApi(value="换购活动")
	private RepurchaseEvent repurchaseEvent=new RepurchaseEvent();
	
	@ZapcomApi(value="总金额")
	private String totalMoney="";
	
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public List<EventListGoodsEntity> getProductList() {
		return productList;
	}
	public void setProductList(List<EventListGoodsEntity> productList) {
		this.productList = productList;
	}
	public List<PriceChooseObj> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<PriceChooseObj> priceList) {
		this.priceList = priceList;
	}
	public RepurchaseEvent getRepurchaseEvent() {
		return repurchaseEvent;
	}
	public void setRepurchaseEvent(RepurchaseEvent repurchaseEvent) {
		this.repurchaseEvent = repurchaseEvent;
	}
	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * @return the recordNum
	 */
	public int getRecordNum() {
		return recordNum;
	}
	/**
	 * @param recordNum the recordNum to set
	 */
	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}

	
	
}
