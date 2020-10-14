package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiRegIsUserResult extends RootResult{
	
	@ZapcomApi(value="是否是惠家有成员")
	private boolean flagUser = false;
	
	@ZapcomApi(value="是否有密码")
	private boolean hasPwd = false;
	
	@ZapcomApi(value="是否有上级")
	private boolean hasPMember = false;

	public boolean isFlagUser() {
		return flagUser;
	}

	public void setFlagUser(boolean flagUser) {
		this.flagUser = flagUser;
	}

	public boolean isHasPwd() {
		return hasPwd;
	}

	public void setHasPwd(boolean hasPwd) {
		this.hasPwd = hasPwd;
	}

	public boolean isHasPMember() {
		return hasPMember;
	}

	public void setHasPMember(boolean hasPMember) {
		this.hasPMember = hasPMember;
	}
	
	
}
