package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;
import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	直播间信息
*   zhangbo
*/
public class LiveRoom  {
	
	@ZapcomApi(value = "直播间基本信息")
	LiveRoomBaseInfo liveRoomBaseInfo =new LiveRoomBaseInfo();
	
	@ZapcomApi(value = "直播间数据信息")
	private LiveRoomDataInfo liveRoomDataInfo =  new LiveRoomDataInfo();
	
	@ZapcomApi(value = "控制信息")
	private LiveControlInfo liveControlInfo =  new LiveControlInfo();

	public LiveRoomBaseInfo getLiveRoomBaseInfo() {
		return liveRoomBaseInfo;
	}

	public void setLiveRoomBaseInfo(LiveRoomBaseInfo liveRoomBaseInfo) {
		this.liveRoomBaseInfo = liveRoomBaseInfo;
	}

	public LiveRoomDataInfo getLiveRoomDataInfo() {
		return liveRoomDataInfo;
	}

	public void setLiveRoomDataInfo(LiveRoomDataInfo liveRoomDataInfo) {
		this.liveRoomDataInfo = liveRoomDataInfo;
	}

	public LiveControlInfo getLiveControlInfo() {
		return liveControlInfo;
	}

	public void setLiveControlInfo(LiveControlInfo liveControlInfo) {
		this.liveControlInfo = liveControlInfo;
	}
	
	

}

