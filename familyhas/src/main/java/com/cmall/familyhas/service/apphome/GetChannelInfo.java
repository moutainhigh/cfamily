package com.cmall.familyhas.service.apphome;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.AppHomeChannelInfo;
import com.cmall.systemcenter.systemface.IFlowFunc;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

public class GetChannelInfo extends BaseClass implements IFlowFunc{

	@Override
	public RootResult BeforeFlowChange(String flowCode, String outCode,
			String fromStatus, String toStatus, MDataMap mSubMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RootResult afterFlowChange(String flowCode, String outCode,
			String fromStatus, String toStatus, MDataMap mSubMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获取商品入库类型以及入库仓库编号,虚拟销售量基数(商品总览页面掉用) 获取供应商编号与供应商名称
	 * 
	 */
	public AppHomeChannelInfo getPrchType(String uid) {
		AppHomeChannelInfo appHomeChannelInfo = new AppHomeChannelInfo();
		if (StringUtils.isBlank(uid)) {
			return appHomeChannelInfo;
		}
		MDataMap appMap = DbUp.upTable("fh_apphome_channel").one("uid", uid);
		appHomeChannelInfo.channel_name = appMap.get("channel_name");
		appHomeChannelInfo.channel_type = appMap.get("channel_type");
		appHomeChannelInfo.start_time = appMap.get("start_time");
		appHomeChannelInfo.end_time = appMap.get("end_time");
		appHomeChannelInfo.seq = Integer.parseInt(appMap.get("seq"));
		int status = Integer.parseInt(appMap.get("status"));
		appHomeChannelInfo.status = status;
		if(status == 1){
			appHomeChannelInfo.status_str = "未发布";
		}else{
			appHomeChannelInfo.status_str = "已发布";
		}
		if("449748130001".equals(appHomeChannelInfo.channel_type)){
			appHomeChannelInfo.type_name = "加价";
		}
		if("449748130002".equals(appHomeChannelInfo.channel_type)){
			appHomeChannelInfo.type_name = "兑换";
		}
		if("449748130003".equals(appHomeChannelInfo.channel_type)){
			appHomeChannelInfo.type_name = "会员专享";
		}
		if("449748130004".equals(appHomeChannelInfo.channel_type)){
			appHomeChannelInfo.type_name = "活动";
		}
		if("449748130005".equals(appHomeChannelInfo.channel_type)){
			appHomeChannelInfo.type_name = "视频";
		}
		return appHomeChannelInfo;
	}


}
