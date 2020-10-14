package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 我的积分、礼金卷、储值金、暂存款输入类
 * @author jlin
 *
 */
public class ApiForAssetResult extends RootResultWeb {
	
	@ZapcomApi(value = "可用积分")
	private String availablePoint="0";
	
	@ZapcomApi(value = "可用礼金卷")
	private String availableGiftCard="0";
	
	@ZapcomApi(value = "可用储值金")
	private String availableStoreGold="0";
	
	@ZapcomApi(value = "可用暂存款")
	private String availableTemporaryStore="0";

	public String getAvailablePoint() {
		return availablePoint;
	}

	public void setAvailablePoint(String availablePoint) {
		this.availablePoint = availablePoint;
	}

	public String getAvailableGiftCard() {
		return availableGiftCard;
	}

	public void setAvailableGiftCard(String availableGiftCard) {
		this.availableGiftCard = availableGiftCard;
	}

	public String getAvailableStoreGold() {
		return availableStoreGold;
	}

	public void setAvailableStoreGold(String availableStoreGold) {
		this.availableStoreGold = availableStoreGold;
	}

	public String getAvailableTemporaryStore() {
		return availableTemporaryStore;
	}

	public void setAvailableTemporaryStore(String availableTemporaryStore) {
		this.availableTemporaryStore = availableTemporaryStore;
	}
	
}
