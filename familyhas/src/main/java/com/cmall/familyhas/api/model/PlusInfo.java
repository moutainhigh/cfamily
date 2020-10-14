package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
 * 橙意卡中心，橙意卡相关信息实体类
* @author Angel Joy
* @Time 2020年6月28日 下午2:52:15 
* @Version 1.0
* <p>Description:</p>
*/
public class PlusInfo {
	
	@ZapcomApi(value="橙意卡特权",remark="橙意卡特权")
	private List<PlusPrivilageInfo> plusPrivilageInfos = new ArrayList<PlusPrivilageInfo>();
	
	@ZapcomApi(value="橙意卡展示价格",remark="219.00")
	private BigDecimal showPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="橙意卡展示价格",remark="199.00")
	private BigDecimal sellPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="橙意卡协议描述",remark="")
	private String privilegeDesc = "";
	
	@ZapcomApi(value="橙意卡名称协议描述",remark="")
	private String plusName = "橙意卡年卡";
	
	@ZapcomApi(value="橙意卡商品编号",remark="")
	private String plusProductCode = "";
	
	@ZapcomApi(value="橙意卡SKU编号",remark="")
	private String plusSkuCode = "";

	public String getPlusName() {
		return plusName;
	}

	public String getPlusProductCode() {
		return plusProductCode;
	}

	public void setPlusProductCode(String plusProductCode) {
		this.plusProductCode = plusProductCode;
	}

	public String getPlusSkuCode() {
		return plusSkuCode;
	}

	public void setPlusSkuCode(String plusSkuCode) {
		this.plusSkuCode = plusSkuCode;
	}

	public void setPlusName(String plusName) {
		this.plusName = plusName;
	}

	public List<PlusPrivilageInfo> getPlusPrivilageInfos() {
		return plusPrivilageInfos;
	}

	public void setPlusPrivilageInfos(List<PlusPrivilageInfo> plusPrivilageInfos) {
		this.plusPrivilageInfos = plusPrivilageInfos;
	}

	public BigDecimal getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(BigDecimal showPrice) {
		this.showPrice = showPrice;
	}

	public String getPrivilegeDesc() {
		return privilegeDesc;
	}

	public void setPrivilegeDesc(String privilegeDesc) {
		this.privilegeDesc = privilegeDesc;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	
}

