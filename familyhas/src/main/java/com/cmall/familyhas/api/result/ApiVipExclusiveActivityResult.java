package com.cmall.familyhas.api.result;

import java.util.List;

import com.cmall.familyhas.model.VipActivity;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiVipExclusiveActivityResult extends RootResult {
	
	@ZapcomApi(value="总数据数")
	private int totalPage;
	@ZapcomApi(value="列表数据")
	private List<VipActivity> itemList;
	@ZapcomApi(value="分享标题")
	private String shareTitle;
	@ZapcomApi(value="分享详情")
	private String shareDesc;
	@ZapcomApi(value="分享封面")
	private String sharePic;
	@ZapcomApi(value="是否分享",remark="是：‘449746250001’，否：‘449746250002’")
	private String isShare;
	@ZapcomApi(value="频道名称")
	private String channelName;
	
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public List<VipActivity> getItemList() {
		return itemList;
	}
	public void setItemList(List<VipActivity> itemList) {
		this.itemList = itemList;
	}
	
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	
	public String getShareDesc() {
		return shareDesc;
	}
	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}
	
	public String getSharePic() {
		return sharePic;
	}
	public void setSharePic(String sharePic) {
		this.sharePic = sharePic;
	}
	
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
