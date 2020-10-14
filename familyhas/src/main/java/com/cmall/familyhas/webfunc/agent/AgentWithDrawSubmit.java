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
 * 新增分销人提现单
 * @author Angel Joy
 *
 */
public class AgentWithDrawSubmit  extends RootFunc {

	@Transactional
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		MDataMap insert = new MDataMap();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String apply_code = WebHelper.upCode("TX");
		String member_code = mDataMap.get("zw_f_member_code");
		String withdraw_money = mDataMap.get("zw_f_withdraw_money");
		String apply_status = "4497484600050002";//待财务审核状态
		String creator_source = "4497484600060001";//后台
		String creator_code = UserFactory.INSTANCE.create().getRealName();
		String create_time = DateUtil.getSysDateTimeString();
		insert.put("uid", uuid);
		insert.put("apply_code", apply_code);
		insert.put("member_code", member_code);
		insert.put("withdraw_money", withdraw_money);
		insert.put("apply_status", apply_status);
		insert.put("creator_source", creator_source);
		insert.put("creator_code", creator_code);
		insert.put("create_time", create_time);
		MDataMap agentInfo = DbUp.upTable("fh_agent_member_info").one("member_code",member_code);
		if(agentInfo == null || agentInfo.isEmpty()) {
			result.setResultCode(987654321);
			result.setResultMessage("提现用户不存在，请核查后提交！");
			return result;
		}
		BigDecimal realMoney = new BigDecimal(agentInfo.get("real_money"));
		if(new BigDecimal(withdraw_money).compareTo(new BigDecimal("0")) <1) {//异常提现数据
			result.setResultCode(987654321);
			result.setResultMessage("异常提现数据，请核查后提交！");
			return result;
		}
		if(realMoney.compareTo(new BigDecimal(withdraw_money)) > -1) {//可以提现
			agentInfo.put("real_money", realMoney.subtract(new BigDecimal(withdraw_money)).toString());
			DbUp.upTable("fh_agent_member_info").dataUpdate(agentInfo, "real_money","uid");
			DbUp.upTable("fh_agent_withdraw").dataInsert(insert);
			//插入日志
			MDataMap  logMap = new MDataMap();
			logMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
			logMap.put("apply_code", apply_code);
			logMap.put("old_status", "");
			logMap.put("now_status", "4497484600050002");
			logMap.put("remark", "后台创建提现单");
			logMap.put("create_user", UserFactory.INSTANCE.create().getLoginName());
			logMap.put("create_time", create_time);
			DbUp.upTable("lc_agent_withdraw_status_log").dataInsert(logMap);
			result.setResultCode(1);
			result.setResultMessage("操作成功");
			return result;
		}else {
			result.setResultCode(987654321);
			result.setResultMessage("提现金额超过可提现金额，请校对提现数据！");
			return result;
		}
	}

}
