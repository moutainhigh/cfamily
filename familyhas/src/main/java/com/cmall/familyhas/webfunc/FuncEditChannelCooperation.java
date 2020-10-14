package com.cmall.familyhas.webfunc;

import java.util.Map;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncEditChannelCooperation
 * @Description: 修改渠道商合作状态
 * @author lgx
 * 
 */
public class FuncEditChannelCooperation extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String uidAndStatus = mDataMap.get("zw_f_uid");
		String[] split = uidAndStatus.split(",");
		String uid = split[0];
		String toStatus = split[1];
		
		Map<String, Object> channel_seller = DbUp.upTable("uc_channel_sellerinfo").dataSqlOne("SELECT * FROM uc_channel_sellerinfo WHERE uid = '"+uid+"'", new MDataMap());
		if(channel_seller==null) {
			result.setResultCode(-1);
			result.setResultMessage("查不到该条数据,操作失败");
			return result;
		}
		String before = "";
		String now = "";
		String nowTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		MDataMap updateDataMap = new MDataMap();
		updateDataMap.put("uid", uid);
		updateDataMap.put("update_time", nowTime);
		String cooperation_status = (String) channel_seller.get("cooperation_status");
		if("4497471600560001".equals(cooperation_status)) {
			// 等待合作,点击确认合作变为合作中
			if("4497471600560002".equals(toStatus)) {
				updateDataMap.put("cooperation_status", toStatus);
				before = "等待合作";
				now = "合作中";
			}
		}else if("4497471600560002".equals(cooperation_status)){
			// 合作中,点击冻结变为已冻结
			if("4497471600560004".equals(toStatus)) {
				updateDataMap.put("cooperation_status", toStatus);
				before = "合作中";
				now = "已冻结";
			}
		}else if("4497471600560003".equals(cooperation_status)){
			// 终止合作,点击开启合作变为等待合作
			if("4497471600560001".equals(toStatus)) {
				updateDataMap.put("cooperation_status", toStatus);
				before = "终止合作";
				now = "等待合作";
			}
		}else if("4497471600560004".equals(cooperation_status)){
			if("4497471600560002".equals(toStatus)) {
				// 已冻结,点击解冻变为合作中
				updateDataMap.put("cooperation_status", toStatus);
				before = "已冻结";
				now = "合作中";
			}else if("4497471600560005".equals(toStatus)) {
				// 已冻结,点击取消合作变为清算中
				updateDataMap.put("cooperation_status", toStatus);
				before = "已冻结";
				now = "清算中";
			}
		}else {
			result.setResultCode(-1);
			result.setResultMessage("操作失败,请刷新重试");
			return result;
		}
		int dataUpdate = DbUp.upTable("uc_channel_sellerinfo").dataUpdate(updateDataMap, "cooperation_status,update_time", "uid");
		
		if(dataUpdate > 0) {
			// 往 lc_channel_freeze_cooperation_log 插入日志数据
			MDataMap map = new MDataMap();
			map.put("channel_seller_code", (String) channel_seller.get("channel_seller_code"));
			map.put("channel_seller_name", (String) channel_seller.get("channel_seller_name"));
			map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map.put("register_time", nowTime);
			map.put("remark", UserFactory.INSTANCE.create().getLoginName()+"修改了渠道合作商合作状态:由"+before+"变为"+now);
			DbUp.upTable("lc_channel_freeze_cooperation_log").dataInsert(map);
		}else {
			result.setResultCode(-1);
			result.setResultMessage("操作失败");
			return result;
		}
		
		return result;
	}
	
}