package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 *品牌对象 
 * 
 */
public class HongbaoLuckyModel {
	
	private String eventAward = "";
	
	private Integer probability = 0;
	
	private Integer littleNum = 0;
	
	private Integer maxNum = 0;

	public String getEventAward() {
		return eventAward;
	}

	public void setEventAward(String eventAward) {
		this.eventAward = eventAward;
	}

	public Integer getLittleNum() {
		return littleNum;
	}

	public void setLittleNum(Integer littleNum) {
		this.littleNum = littleNum;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getProbability() {
		return probability;
	}

	public void setProbability(Integer probability) {
		this.probability = probability;
	}
	
	
}
