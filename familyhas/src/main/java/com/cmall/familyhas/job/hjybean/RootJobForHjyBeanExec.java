package com.cmall.familyhas.job.hjybean;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basehelper.LogHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.MailSupport;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webmodel.ConfigJobExec;

/**
 * 惠豆赠送和返还定时基类
 */
public abstract class RootJobForHjyBeanExec extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {

		ConfigJobExec configJobExec = getConfig();

		String sNowString = FormatHelper.upDateTime();
		// 取出三天内待执行的内容
		for (MDataMap mDataMap : DbUp
				.upTable("fh_hd_exectimer")
				.queryAll(
						"",
						"create_time",
						"exec_time<:nowtime and flag_success=0 and exec_number<:exec_number and exec_type in ("+configJobExec.getExecType()+") and create_time >= DATE_SUB(DATE(now()),INTERVAL 3 DAY)",
						new MDataMap(
								"nowtime",
								sNowString,
								"exec_number",
								String.valueOf(configJobExec.getMaxExecNumber())))) {

			String sExecCode = mDataMap.get("exec_code");
			String sExecInfo = mDataMap.get("exec_info");
			String sLockKey = WebHelper.addLock(configJobExec.getExecJobLock(),
					sExecCode, sExecInfo);

			if (StringUtils.isNotEmpty(sLockKey)) {
				IBaseResult iResult = null;
				try {
					iResult = execByInfo(mDataMap);

					LogHelper.addLog("job_exec", iResult);

				} catch (Exception e) {
					RootResult rootResult = new RootResult();

					rootResult.setResultCode(969905039);
					rootResult.setResultMessage(bInfo(969905039));
					iResult = (IBaseResult) rootResult;
					e.printStackTrace();
					rootResult.setResultMessage(rootResult.getResultMessage()
							+ e.getMessage());
				}

				if (iResult.getResultCode() != 1) {

					// 当已执行次数等于该数字时 发送报警邮件 一条记录只发送一次
					if (configJobExec.getNoticeOnce() > 0
							&& Integer.valueOf(mDataMap.get("exec_number")) == configJobExec
									.getNoticeOnce()) {

						String sErrorNotice = bConfig("zapweb.mail_notice")
								.trim();
						if (StringUtils.isNotBlank(sErrorNotice)) {

							MailSupport.INSTANCE.sendMail(sErrorNotice,
									bInfo(969912014, sExecCode + sExecInfo),
									iResult.getResultMessage());
						}
					}
				}

				DbUp.upTable("fh_hd_exectimer").dataUpdate(
						new MDataMap("begin_time", sNowString, "end_time",
								FormatHelper.upDateTime(), "flag_success",
								iResult.getResultCode() == 1 ? "1" : "0",
								"remark", mDataMap.get("remark")
										+ WebConst.CONST_SPLIT_LINE
										+ GsonHelper.toJson(iResult),
								"exec_code", sExecCode, "exec_number",
								String.valueOf(Integer.valueOf(mDataMap
										.get("exec_number")) + 1)),
						"begin_time,end_time,flag_success,remark,exec_number",
						"exec_code");

				WebHelper.unLock(sLockKey);
			}

		}
	}

	/**
	 * 根据内容执行
	 * 
	 * @param sInfo
	 * @return
	 */
	public abstract IBaseResult execByInfo(MDataMap execInfo);

	/**
	 * 获取配置
	 * 
	 * @return
	 */
	public abstract ConfigJobExec getConfig();
}
