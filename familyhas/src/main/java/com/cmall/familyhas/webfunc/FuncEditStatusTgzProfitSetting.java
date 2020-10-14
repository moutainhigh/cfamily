package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改推广状态
 * 
 * @author wm
 *
 */
public class FuncEditStatusTgzProfitSetting extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		try {
			String loginName = UserFactory.INSTANCE.create().getLoginName();// 获取登录人
			String uid = mDataMap.get("zw_f_uid");
			String nowTiem = DateHelper.upNow();
			String status = "4497473700010001";// 开启状态
			MDataMap tgzProfitSetting = DbUp.upTable("fh_tgz_profit_setting").one("uid", uid);
			if (null == tgzProfitSetting) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("当前数据不存在");
				return mWebResult;
			}
			if (tgzProfitSetting.get("status").equals("4497473700010001")) {
				status = "4497473700010002";// 关闭状态
			}
			// 修改数据
			MDataMap updateMDataMap = new MDataMap();
			updateMDataMap.put("update_user", loginName);
			updateMDataMap.put("update_time", nowTiem);
			updateMDataMap.put("status", status);
			updateMDataMap.put("uid", uid);
			int dataUpdate = DbUp.upTable("fh_tgz_profit_setting").dataUpdate(updateMDataMap,
					"update_time,update_user,status", "uid");
			// 插入日志
			if (dataUpdate > 0) {// 修改成功 插入操作日志
				MDataMap saveMDataMap = new MDataMap();
				saveMDataMap.put("status", status);// 当前状态
				saveMDataMap.put("tgz_type", tgzProfitSetting.get("tgz_type"));// 推广类型： 推广收益、买家秀
				saveMDataMap.put("tgz_rate", tgzProfitSetting.get("tgz_rate"));
				saveMDataMap.put("company_rate", tgzProfitSetting.get("company_rate"));
				saveMDataMap.put("update_time", nowTiem);
				saveMDataMap.put("update_user", loginName);
				DbUp.upTable("lc_tgz_profit_setting_log").dataInsert(saveMDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("数据错误，请检查！");
		}

		return mWebResult;
	}

}
