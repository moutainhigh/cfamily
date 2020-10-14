package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.MyGiftCard;
import com.cmall.familyhas.api.model.MyPoints;
import com.cmall.familyhas.api.model.MyStoreGold;
import com.cmall.familyhas.api.model.MyTemporaryStore;
import com.cmall.familyhas.api.model.PageResults;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 我的积分、礼金卷、储值金、暂存款输入类
 * @author jlin
 *
 */
public class ApiUserInfoQueryFromHomeResult extends RootResultWeb {
	
	@ZapcomApi(value = "积分LIST")
	private List<MyPoints> points = new ArrayList<MyPoints>();
	
	@ZapcomApi(value = "礼金卷LIST")
	private List<MyGiftCard> giftCard = new ArrayList<MyGiftCard>();
	
	@ZapcomApi(value = "储值金LIST")
	private List<MyStoreGold> storeGold = new ArrayList<MyStoreGold>();
	
	@ZapcomApi(value = "暂存款LIST")
	private List<MyTemporaryStore> temporaryStore = new ArrayList<MyTemporaryStore>();
	
	@ZapcomApi(value = "可用积分")
	private String availablePoint="0";
	
	@ZapcomApi(value = "积分提示",demo="其中888积分有效期至xxxx-xx-xx，111积分有效期至xxxx-xx-xx")
	private String pointTip="";
	
	@ZapcomApi(value = "可用礼金卷")
	private String availableGiftCard="0";
	
	@ZapcomApi(value = "可用储值金")
	private String availableStoreGold="0";
	
	@ZapcomApi(value = "可用暂存款")
	private String availableTemporaryStore="0";
	
	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();

	public List<MyPoints> getPoints() {
		return points;
	}

	public void setPoints(List<MyPoints> points) {
		this.points = points;
	}

	public List<MyGiftCard> getGiftCard() {
		return giftCard;
	}

	public void setGiftCard(List<MyGiftCard> giftCard) {
		this.giftCard = giftCard;
	}

	public List<MyStoreGold> getStoreGold() {
		return storeGold;
	}

	public void setStoreGold(List<MyStoreGold> storeGold) {
		this.storeGold = storeGold;
	}

	public List<MyTemporaryStore> getTemporaryStore() {
		return temporaryStore;
	}

	public void setTemporaryStore(List<MyTemporaryStore> temporaryStore) {
		this.temporaryStore = temporaryStore;
	}

	public String getAvailablePoint() {
		return availablePoint;
	}

	public void setAvailablePoint(String availablePoint) {
		this.availablePoint = availablePoint;
	}

	public String getPointTip() {
		return pointTip;
	}

	public void setPointTip(String pointTip) {
		this.pointTip = pointTip;
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

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}
	
	
	
}
