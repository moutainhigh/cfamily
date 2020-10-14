package com.cmall.familyhas.webfunc.agent;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 审批提现单
 * @author Angel Joy
 * @params zw_f_operate :yunying/caiwu  = 运营/财务
 * 		   zw_f_type:access/refuse = 通过/拒绝
 * 运营流程，也就是 operate  == yunying 时，
 * 4497484600050001 ==>  4497484600050002  通过
 * 4497484600050001 ==>  4497484600050006  驳回
 * 财务，也就是 operate  == caiwu 时，
 * 4497484600050002 ==> 4497484600050004   通过
 * 4497484600050002 ==> 4497484600050001   驳回
 * 4497484600050004 ==> 4497484600050005   确认
 * 4497484600050004 ==> 4497484600050002   反审核
 */
public class FuncForFinanceAccessAgentWithDraw  extends RootFunc {

	@Transactional
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		String remark = mDataMap.get("zw_f_remark");
		if(remark == null) {
			remark = "";
		}
		MDataMap applyInfo = DbUp.upTable("fh_agent_withdraw").one("uid",uid);
		if(applyInfo == null) {
			result.setResultCode(0);
			result.setResultMessage("该提现单不存在，请核查后再做操作");
			return result;
		}
		boolean flag =  new FuncForChangeAgentWithDrawStatus().checkRealyMoney(applyInfo.get("member_code"));
		if(!flag) {//可提现金额为负数，不可审批通过提现单
			result.setResultCode(0);
			result.setResultMessage("可提现金额为负数，不可审批通过提现单");
			return result;
		}
		String apply_status = applyInfo.get("apply_status");
		applyInfo.put("apply_status", "4497484600050004");//财务审核通过
		DbUp.upTable("fh_agent_withdraw").dataUpdate(applyInfo, "apply_status", "uid");
		//操作日志添加
		MDataMap  logMap = new MDataMap();
		logMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
		logMap.put("apply_code", applyInfo.get("apply_code"));
		logMap.put("old_status", apply_status);
		logMap.put("now_status",applyInfo.get("apply_status") );
		logMap.put("remark", remark);
		logMap.put("create_user", UserFactory.INSTANCE.create().getLoginName());
		logMap.put("create_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("lc_agent_withdraw_status_log").dataInsert(logMap);
		result.setResultCode(1);
		result.setResultMessage("操作成功！");
		return result;
	}

}
