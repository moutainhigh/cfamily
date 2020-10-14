package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.result.ApiGetTvChannelAreaListResult.AreaChannelInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetTvChannelAreaInfoResult extends RootResult{
	
	@ZapcomApi(value="地区名称")
	private String areaName = "";
	
	@ZapcomApi(value="频道名称")
	private String channelName = "";
	
	@ZapcomApi(value="所有地区的频道")
	private List<AreaChannelInfo> area_channel = new ArrayList<ApiGetTvChannelAreaListResult.AreaChannelInfo>();
		
	
	public List<AreaChannelInfo> getArea_channel() {
		return area_channel;
	}


	public void setArea_channel(List<AreaChannelInfo> area_channel) {
		this.area_channel = area_channel;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}	
	
}
