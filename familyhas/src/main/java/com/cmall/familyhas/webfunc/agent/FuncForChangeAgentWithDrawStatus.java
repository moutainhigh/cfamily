package com.cmall.familyhas.webfunc.agent;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
 * 4497484600050005 ==> 4497484600050007   付款
 */
public class FuncForChangeAgentWithDrawStatus  extends RootFunc {

	@Transactional
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		MDataMap insert = new MDataMap();
		String uid = mDataMap.get("zw_f_uid");
		String operate = mDataMap.get("zw_f_operate");
		String type = mDataMap.get("zw_f_type");
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
		String apply_status = applyInfo.get("apply_status");
		//是否执行的判断
		boolean flag = true;
		if("yunying".equals(operate)&&"4497484600050001".equals(apply_status)) {//运营可以审核的状态
			if("access".equals(type)) {
				applyInfo.put("apply_status", "4497484600050002");//运营审核通过
				remark = "运营审批通过";
				flag = checkRealyMoney(applyInfo.get("member_code"));
			}else if("refuse".equals(type)) {//拒绝
				applyInfo.put("apply_status", "4497484600050006");//运营驳回
				remark = "运营审批拒绝";
			}
		}else if("caiwu".equals(operate)&&("4497484600050002".equals(apply_status)||"4497484600050004".equals(apply_status)||"4497484600050005".equals(apply_status))){
			if("4497484600050002".equals(apply_status)) {
				if("access".equals(type)) {
					applyInfo.put("apply_status", "4497484600050004");//财务审核通过
					flag = checkRealyMoney(applyInfo.get("member_code"));
				}else if("refuse".equals(type)) {//拒绝
					applyInfo.put("apply_status", "4497484600050001");//财务驳回
				}
			}
			if("4497484600050004".equals(apply_status)) {
				if("access".equals(type)) {
					applyInfo.put("apply_status", "4497484600050005");//财务确认
					flag = checkRealyMoney(applyInfo.get("member_code"));
				}else if("refuse".equals(type)) {//拒绝
					applyInfo.put("apply_status", "4497484600050002");//财务反审核
				}
			}
			if("4497484600050005".equals(apply_status)) {
				if("access".equals(type)) {
					//需要确定是否有付款时间
					String pay_time = applyInfo.get("pay_time");
					if(StringUtils.isEmpty(pay_time)) {
						result.setResultCode(0);
						result.setResultMessage("付款时间为空，请先确认打款时间再修改状态！");
						return result;
					}
					applyInfo.put("apply_status", "4497484600050007");//财务确认付款
				}
			}
		}else {
			result.setResultCode(0);
			result.setResultMessage("运营不能审批此状态，请核查后再提交");
			return result;
			
		}
		if(!flag) {//可提现金额为负数，不可审批通过提现单
			result.setResultCode(0);
			result.setResultMessage("可提现金额为负数，不可审批通过提现单");
			return result;
		}
		DbUp.upTable("fh_agent_withdraw").dataUpdate(applyInfo, "apply_status", "uid");
		if("4497484600050006".equals(applyInfo.get("apply_status"))) {//运营驳回，此申请单需要返还金额到待提现金额
			BigDecimal withdraw_money = new  BigDecimal(applyInfo.get("withdraw_money"));
			String member_code = applyInfo.get("member_code");
			MDataMap memberInfo = DbUp.upTable("fh_agent_member_info").one("member_code",member_code);
			BigDecimal realMoneyOld = 	new BigDecimal(memberInfo.get("real_money"));
			BigDecimal realMoney = realMoneyOld.add(withdraw_money);
			memberInfo.put("real_money", realMoney.toString());
			DbUp.upTable("fh_agent_member_info").dataUpdate(memberInfo, "real_money", "uid");
		}
		//财务付款，需要回写会员信息表已支付金额。
		if("4497484600050007".equals(applyInfo.get("apply_status"))) {
			String memberCode = applyInfo.get("member_code");
			String sql = "UPDATE familyhas.fh_agent_member_info SET pay_money = pay_money+"+applyInfo.get("withdraw_money")+" WHERE member_code = '"+memberCode+"'";
			DbUp.upTable("fh_agent_member_info").dataExec(sql, null);
		}
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

	/**
	 * 如果该用户的可提现金额为负数，则不准通过该用户的提现申请
	 * @param member_code
	 * @return
	 */
	public boolean checkRealyMoney(String member_code) {
		MDataMap memberInfo = DbUp.upTable("fh_agent_member_info").one("member_code",member_code);
		if(memberInfo == null || memberInfo.isEmpty()) {
			return false;
		}
		BigDecimal realMoney = new BigDecimal(memberInfo.get("real_money"));
		if(realMoney.compareTo(BigDecimal.ZERO)>=0) {
			return true;
		}
		return false;
	}

}
