package com.cmall.familyhas.model;

import java.math.BigDecimal;

public class LiveRoomDataInfo {

	private int seePepole=0;
	private int seeTimes=0;
	private int likePeople=0;
	private int likeTimes=0;
	private int commentsPeople=0;
	private int commentsTimes=0;
	private int productClickPeople=0;
	private int productClickTimes=0;
	private int orderNum=0;
	private BigDecimal orderMoney=BigDecimal.ZERO;
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}
	public int getSeePepole() {
		return seePepole;
	}
	public void setSeePepole(int seePepole) {
		this.seePepole = seePepole;
	}
	public int getSeeTimes() {
		return seeTimes;
	}
	public void setSeeTimes(int seeTimes) {
		this.seeTimes = seeTimes;
	}
	public int getLikePeople() {
		return likePeople;
	}
	public void setLikePeople(int likePeople) {
		this.likePeople = likePeople;
	}
	public int getLikeTimes() {
		return likeTimes;
	}
	public void setLikeTimes(int likeTimes) {
		this.likeTimes = likeTimes;
	}
	public int getCommentsPeople() {
		return commentsPeople;
	}
	public void setCommentsPeople(int commentsPeople) {
		this.commentsPeople = commentsPeople;
	}
	public int getCommentsTimes() {
		return commentsTimes;
	}
	public void setCommentsTimes(int commentsTimes) {
		this.commentsTimes = commentsTimes;
	}
	public int getProductClickPeople() {
		return productClickPeople;
	}
	public void setProductClickPeople(int productClickPeople) {
		this.productClickPeople = productClickPeople;
	}
	public int getProductClickTimes() {
		return productClickTimes;
	}
	public void setProductClickTimes(int productClickTimes) {
		this.productClickTimes = productClickTimes;
	}
	
	

	
}
