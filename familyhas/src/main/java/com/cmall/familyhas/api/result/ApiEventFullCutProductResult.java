package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.EventListGoodsEntity;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/***
 * 满减活动返回参数
 * @author zhouguohui
 *
 */
public class ApiEventFullCutProductResult extends RootResult{
	
	@ZapcomApi(value="价格区间列表")
	private List<PriceChooseObj> priceList=new ArrayList<PriceChooseObj>();
	@ZapcomApi(value="满减说明",remark="满减金额全部说明")
	private String fullCutDescription="";
	@ZapcomApi(value="总页数",remark="返回商品总页数")
	private int pageNum;
	@ZapcomApi(value="总条数",remark="返回商品总条数")
	private int recordNum;
	@ZapcomApi(value="商品列表",remark="返回当前满减活动编号对应的商品信息")
	private List<EventListGoodsEntity> fullCutProduct = new ArrayList<EventListGoodsEntity>();
	@ZapcomApi(value="满减凑单还需购买的金额/件数")
	private String addFullMoney = "";
	
	@ZapcomApi(value="满减凑单类型",remark="1 金额、2 件数")
	private int addFullMoneyType = 0;
	/**
	 * @return the fullCutDescription
	 */
	public String getFullCutDescription() {
		return fullCutDescription;
	}
	/**
	 * @param fullCutDescription the fullCutDescription to set
	 */
	public void setFullCutDescription(String fullCutDescription) {
		this.fullCutDescription = fullCutDescription;
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
	/**
	 * @return the fullCutProduct
	 */
	public List<EventListGoodsEntity> getFullCutProduct() {
		return fullCutProduct;
	}
	/**
	 * @param fullCutProduct the fullCutProduct to set
	 */
	public void setFullCutProduct(List<EventListGoodsEntity> fullCutProduct) {
		this.fullCutProduct = fullCutProduct;
	}
	public List<PriceChooseObj> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<PriceChooseObj> priceList) {
		this.priceList = priceList;
	}
	public String getAddFullMoney() {
		return addFullMoney;
	}
	public void setAddFullMoney(String addFullMoney) {
		this.addFullMoney = addFullMoney;
	}
	public int getAddFullMoneyType() {
		return addFullMoneyType;
	}
	public void setAddFullMoneyType(int addFullMoneyType) {
		this.addFullMoneyType = addFullMoneyType;
	}
	
}
