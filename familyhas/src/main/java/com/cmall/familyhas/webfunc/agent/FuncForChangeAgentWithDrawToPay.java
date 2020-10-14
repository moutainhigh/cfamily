package com.cmall.familyhas.webfunc.agent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 提现单确认付款
 * @author Angel Joy
 * @params zw_f_operate :yunying/caiwu  = 运营/财务
 * 		   zw_f_type:access/refuse = 通过/拒绝
 * 财务，也就是 operate  == caiwu 时，
 * 4497484600050005 ==> 4497484600050007   付款
 */
public class FuncForChangeAgentWithDrawToPay  extends RootFunc {

	@Transactional
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String sql = "SELECT * FROM familyhas.fh_agent_withdraw WHERE apply_status = '4497484600050005' AND pay_time != ''";
		List<Map<String,Object>> mapList = DbUp.upTable("fh_agent_withdraw").dataSqlList(sql, null);
		for(Map<String,Object> map : mapList) {
			if(map == null) {
				continue;
			}
			MDataMap applyInfo = new MDataMap(map);
			if(applyInfo == null || applyInfo.isEmpty()) {
				continue;
			}
			applyInfo.put("apply_status", "4497484600050007");
			DbUp.upTable("fh_agent_withdraw").dataUpdate(applyInfo, "apply_status", "apply_code");
			//财务付款，需要回写会员信息表已支付金额。
			if("4497484600050007".equals(applyInfo.get("apply_status"))) {
				String memberCode = applyInfo.get("member_code");
				String sqlpay = "UPDATE familyhas.fh_agent_member_info SET pay_money = pay_money+"+applyInfo.get("withdraw_money")+" WHERE member_code = '"+memberCode+"'";
				DbUp.upTable("fh_agent_member_info").dataExec(sqlpay, null);
			}
			//操作日志添加
			MDataMap  logMap = new MDataMap();
			logMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
			logMap.put("apply_code", applyInfo.get("apply_code"));
			logMap.put("old_status", "4497484600050005");
			logMap.put("now_status",applyInfo.get("apply_status") );
			logMap.put("remark", "财务人员批量付款");
			logMap.put("create_user", UserFactory.INSTANCE.create().getLoginName());
			logMap.put("create_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("lc_agent_withdraw_status_log").dataInsert(logMap);
		}
		result.setResultCode(1);
		result.setResultMessage("操作成功！");
		return result;
	}

}
