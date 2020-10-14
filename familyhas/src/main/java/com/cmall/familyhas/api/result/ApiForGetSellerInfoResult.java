package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/***
 * 查询商户信息输出参数
 */
public class ApiForGetSellerInfoResult extends RootResultWeb {

	@ZapcomApi(value = "商户编码",remark="")
	private String smallSellerCode;
	@ZapcomApi(value = "商户类型",remark="普通4497478100050001,跨境4497478100050002,直邮4497478100050003,平台4497478100050004")
	private String ucSellerType;
	@ZapcomApi(value = "组织机构代码照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/27507/ccc.jpg")
	private String orgCodePic;
	@ZapcomApi(value = "税务登记证副本照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/rrr.jpg")
	private String taxRegCertCopy;
	@ZapcomApi(value = "开户行影印证件",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/ttt.jpg")
	private String bankAccountCertPhotoCopy;
	@ZapcomApi(value = "营业执照照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/27507/bbb.jpg")
	private String bizLicensePic;
	@ZapcomApi(value = "法人身份证反面照片",remark="http://image-family.huijiayou.cn/cfiles/staticfiles/upload/24ebe/yyy.jpg")
	private String legalPersonIDCardOppPic;
	
	public String getSmallSellerCode() {
		return smallSellerCode;
	}
	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}
	public String getUcSellerType() {
		return ucSellerType;
	}
	public void setUcSellerType(String ucSellerType) {
		this.ucSellerType = ucSellerType;
	}
	public String getOrgCodePic() {
		return orgCodePic;
	}
	public void setOrgCodePic(String orgCodePic) {
		this.orgCodePic = orgCodePic;
	}
	public String getTaxRegCertCopy() {
		return taxRegCertCopy;
	}
	public void setTaxRegCertCopy(String taxRegCertCopy) {
		this.taxRegCertCopy = taxRegCertCopy;
	}
	public String getBankAccountCertPhotoCopy() {
		return bankAccountCertPhotoCopy;
	}
	public void setBankAccountCertPhotoCopy(String bankAccountCertPhotoCopy) {
		this.bankAccountCertPhotoCopy = bankAccountCertPhotoCopy;
	}
	public String getBizLicensePic() {
		return bizLicensePic;
	}
	public void setBizLicensePic(String bizLicensePic) {
		this.bizLicensePic = bizLicensePic;
	}
	public String getLegalPersonIDCardOppPic() {
		return legalPersonIDCardOppPic;
	}
	public void setLegalPersonIDCardOppPic(String legalPersonIDCardOppPic) {
		this.legalPersonIDCardOppPic = legalPersonIDCardOppPic;
	}
	
}
