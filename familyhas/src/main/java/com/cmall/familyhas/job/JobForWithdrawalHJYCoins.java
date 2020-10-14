package com.cmall.familyhas.job;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.process.prepare.WechatPrepareCashProcess;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;

/**
 * 
 * <p>
 * Description:惠币提现定时 <／p>
 * 
 * @author zb
 * @date 2020年7月8日
 *
 */
public class JobForWithdrawalHJYCoins extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String sInfo) {
		String applyCode = sInfo;
		if (StringUtils.isNotBlank(applyCode)) {
			// 入参不能为空
			String lockKey = KvHelper.lockCodes(20, Constants.LOCK_CASH + applyCode);
			if (StringUtils.isNotBlank(lockKey)) {
				MDataMap one = DbUp.upTable("fh_tgz_withdraw_info").one("apply_code", applyCode);
				// 查询此申请编号是否为待提现状态
				if ("4497471600620001".equals(one.get("status"))) {
					WechatPrepareCashProcess.PaymentInput input = new WechatPrepareCashProcess.PaymentInput();
					input.apply_code = applyCode;
					input.c_transfer_order = input.apply_code;
					input.receiveName = one.get("name");
					input.transfer_amount = one.get("apply_money");
					input.wxOpenId = one.get("openid");
					input.cust_id = one.get("member_code");
					// 进行提现操作
					PayServiceFactory.getInstance().getWechatPrepareCashProcess().process(input);
				}

				// 不管成功与否都不再做第二次操作
				DbUp.upTable("za_exectimer").dataUpdate(
						new MDataMap("exec_info", sInfo, "exec_type", "449746990034", "exec_number", "1"),
						"exec_number", "exec_type,exec_info");

			}
			KvHelper.unlockCode(lockKey, Constants.LOCK_CASH + applyCode);
		}
		return new RootResult();
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990034");
		config.setMaxExecNumber(1);
		return config;
	}

}
