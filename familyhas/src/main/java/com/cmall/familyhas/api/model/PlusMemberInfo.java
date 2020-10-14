package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
 * 橙意卡中心信息实体
* @author Angel Joy
* @Time 2020年6月28日 下午2:40:55 
* @Version 1.0
* <p>Description:</p>
*/
public class PlusMemberInfo {
	
	@ZapcomApi(value="头像",remark="头像")
	private String avatar;
	
	@ZapcomApi(value="昵称",remark="昵称")
	private String nickName;
	
	@ZapcomApi(value="是否是橙意卡会员",remark="1：是，0：否")
	private Integer privilegeflag = 1;
	
	@ZapcomApi(value="橙意卡会员到期时间",remark="2021-02-12 12:00:00,非橙意卡会员为空")
	private String expireDate = "";
	
	@ZapcomApi(value="累积节约金额",remark="1000.00")
	private BigDecimal saveMoney = BigDecimal.ZERO;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getPrivilegeflag() {
		return privilegeflag;
	}

	public void setPrivilegeflag(Integer privilegeflag) {
		this.privilegeflag = privilegeflag;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getSaveMoney() {
		return saveMoney;
	}

	public void setSaveMoney(BigDecimal saveMoney) {
		this.saveMoney = saveMoney;
	}
	
}
