package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改sku是否可卖
 * @author ligj
 *
 */
public class FuncChangeChannelStatus extends RootFunc {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

//		String sale_yn = mDataMap.get("zw_f_sale_yn");
		String uid = mDataMap.get("zw_f_uid");
		Integer status = Integer.parseInt(mDataMap.get("zw_f_status"));
		int count = DbUp.upTable("fh_apphome_channel_details").count("channel_uid",uid);
		MDataMap channel = DbUp.upTable("fh_apphome_channel").one("uid",uid);
		String zid = channel.get("zid");
		String channel_type = channel.get("channel_type");
		if(status == 2){//已发布状态变更为取消发布
			status = 1;
		}else{
			status = 2;
		}
		if(!"449748130003".equals(channel_type)){
			if(status == 2){
				if(count <= 0){
					mResult.setResultCode(1000);
					mResult.setResultMessage("该栏目中栏目信息为空，不能发布");
					return mResult;
				}
			}
		}else{
			String start_time = channel.get("start_time");
			String end_time = channel.get("end_time");
			//event_type_code = '4497472600010020' 为 会员专享-闪购 活动，为积分商城会员专享使用。event_status = '4497472700020002'此状态值为已发布。
			String sSql = "select * from systemcenter.sc_event_info where begin_time <= "+"\'"+end_time+"\'"+"and end_time >= "+"\'"+start_time+"\' and event_type_code = '4497472600010020' and event_status = '4497472700020002'";
			List<Map<String,Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sSql, null);
			if(status == 2){
				if(list.size() <= 0){
					mResult.setResultCode(1000);
					mResult.setResultMessage("该栏目中栏目信息为空，不能发布");
					return mResult;
				}
			}
		}
		MDataMap params = new MDataMap();
		params.put("status", status+"");
		params.put("uid", uid);
		params.put("zid", zid);
		DbUp.upTable("fh_apphome_channel").update(params);
		return mResult;
	}
	
}
