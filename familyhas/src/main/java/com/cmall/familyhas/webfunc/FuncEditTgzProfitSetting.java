package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditTgzProfitSetting extends FuncEdit {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		try {
			String loginName = UserFactory.INSTANCE.create().getLoginName();// 获取登录人
			String uid = mDataMap.get("zw_f_uid");
			Integer companyRate = Integer.parseInt(mDataMap.get("zw_f_company_rate").replaceAll("\\.\\d*$", ""));// 公司利润比
			Integer tgzRate = Integer.parseInt(mDataMap.get("zw_f_tgz_rate").replaceAll("\\.\\d*$", ""));// 推广利润比例
			// 参数校验合法性
			if (!isNumeric(mDataMap.get("zw_f_tgz_rate"))) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("推广利润必须为正整数");
				return mWebResult;
			}
			if (tgzRate <= 0 || tgzRate > 100) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("推广利润比例 必须为大于0小于100之间的整数");
				return mWebResult;
			}
			if ((companyRate + tgzRate) != 100) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("公司利润比例+推广利润比例  不等于100");
				return mWebResult;
			}
			String nowTiem = DateHelper.upNow();
			MDataMap tgzProfitSetting = DbUp.upTable("fh_tgz_profit_setting").one("uid", uid);
			if (null == tgzProfitSetting) {
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("当前数据不存在");
				return mWebResult;
			}
			// 修改数据
			MDataMap updateMDataMap = new MDataMap();
			updateMDataMap.put("tgz_rate", new BigDecimal(tgzRate).toString());
			updateMDataMap.put("company_rate", new BigDecimal(companyRate).toString());
			updateMDataMap.put("update_user", loginName);
			updateMDataMap.put("update_time", nowTiem);
			updateMDataMap.put("uid", uid);
			int dataUpdate = DbUp.upTable("fh_tgz_profit_setting").dataUpdate(updateMDataMap,
					"tgz_rate,company_rate,update_time,update_user", "uid");
			// 插入日志
			if (dataUpdate > 0) {// 修改成功 插入操作日志
				MDataMap saveMDataMap = new MDataMap();
				saveMDataMap.put("status", tgzProfitSetting.get("status"));// 当前状态
				saveMDataMap.put("tgz_type", tgzProfitSetting.get("tgz_type"));// 推广类型： 推广收益、买家秀
				saveMDataMap.put("tgz_rate", new BigDecimal(tgzRate).toString());
				saveMDataMap.put("company_rate", new BigDecimal(companyRate).toString());
				saveMDataMap.put("update_time", nowTiem);
				saveMDataMap.put("update_user", loginName);
				DbUp.upTable("lc_tgz_profit_setting_log").dataInsert(saveMDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("数据格式错误，请检查！");
		}
		return mWebResult;
	}

	public boolean isNumeric(String string) {
		if (string.length() > 3) {
			String result = string.substring(string.length() - 3, string.length());
			if (result.equals(".00")) {
				return true;
			}
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(string).matches();
	}

}
