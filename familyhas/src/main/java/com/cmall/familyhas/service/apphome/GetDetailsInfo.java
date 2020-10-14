package com.cmall.familyhas.service.apphome;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.AppHomeChannelDetailsInfo;
import com.cmall.systemcenter.systemface.IFlowFunc;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

public class GetDetailsInfo extends BaseClass implements IFlowFunc{

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
	public AppHomeChannelDetailsInfo getPrchType(String uid) {
		AppHomeChannelDetailsInfo details = new AppHomeChannelDetailsInfo();
		if (StringUtils.isBlank(uid)) {
			return details;
		}
		MDataMap appMap = DbUp.upTable("fh_apphome_channel_details").one("uid", uid);
		details.start_time = appMap.get("start_time");
		details.end_time = appMap.get("end_time");
		details.product_info = appMap.get("product_info");
		details.product_code = appMap.get("product_code");
		details.title = appMap.get("title");
		details.jf_cost = Integer.parseInt(appMap.get("jf_cost"));
		if(!StringUtils.isEmpty(appMap.get("extra_charges"))){
			details.extra_charges =appMap.get("extra_charges");
		}
		details.allow_count = Integer.parseInt(appMap.get("allow_count"));
		details.one_allowed = Integer.parseInt(appMap.get("one_allowed"));
		details.channel_uid = appMap.get("channel_uid");
		return details;
	}


}
