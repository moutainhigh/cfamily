package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 促销活动
 * @author 李国杰
 *
 */
public class BusinessLicenseModel {

	@ZapcomApi(value = "企业名称",remark="企业名称")
	private String sellerCompanyName = "";

	@ZapcomApi(value = "营业执照注册号",remark="营业执照注册号")
	private String registrationNumber = "";
	
	@ZapcomApi(value = "法定代表人姓名",remark="法定代表人姓名")
	private String legalPerson = "";
	
	@ZapcomApi(value = "企业注册资金",remark="企业注册资金")
	private String registerMoney = "";
	
	@ZapcomApi(value = "营业执照所在地",remark="营业执照所在地")
	private String registerAddress = "";
	
	@ZapcomApi(value = "营业执照图片",remark="营业执照图片")
	private String uploadBusinessLicense = "";

	public String getSellerCompanyName() {
		return sellerCompanyName;
	}

	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getRegisterMoney() {
		return registerMoney;
	}

	public void setRegisterMoney(String registerMoney) {
		this.registerMoney = registerMoney;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getUploadBusinessLicense() {
		return uploadBusinessLicense;
	}

	public void setUploadBusinessLicense(String uploadBusinessLicense) {
		this.uploadBusinessLicense = uploadBusinessLicense;
	}
	
	
	
}
