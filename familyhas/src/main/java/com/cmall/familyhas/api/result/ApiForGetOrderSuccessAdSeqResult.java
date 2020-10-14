package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.result.ld.ShowLinkForLDMsgPaySuccess;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/** 
* @author Angel Joy
* @Time 2020-8-26 14:13:11 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForGetOrderSuccessAdSeqResult extends RootResultWeb {
	
	@ZapcomApi(value="LD短信支付成功，广告下tab页配置内容")
	private List<ShowLinkForLDMsgPaySuccess> showLink = new ArrayList<ShowLinkForLDMsgPaySuccess>();

	public List<ShowLinkForLDMsgPaySuccess> getShowLink() {
		return showLink;
	}

	public void setShowLink(List<ShowLinkForLDMsgPaySuccess> showLink) {
		this.showLink = showLink;
	}
	
	
}
