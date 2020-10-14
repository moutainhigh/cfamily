package com.cmall.familyhas.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.DateUtils;
import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 查询惠家有所有帐户的微公社余额,不考虑黑名单或不可用的帐户
 * @author zht
 *
 */
public class JobMicroCommunityBalance extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		String sql = "select member_code, manage_code from mc_login_info";
		List<Map<String, Object>> list = DbUp.upTable("mc_login_info").dataSqlList(sql, new MDataMap());
		if(null != list && list.size() > 0) {
			GroupAccountService accountService = new GroupAccountService();
			for(Map<String, Object> map: list) {
				String memberCode = null == map.get("member_code") ? "" : map.get("member_code") .toString();
				String manageCode = null == map.get("manage_code") ? "" : map.get("manage_code") .toString();
				GroupAccountInfoResult groupResult= accountService.getAccountInfoByApi(memberCode);
				if(StringUtils.isBlank(groupResult.getWithdrawMoney())){
					groupResult.setWithdrawMoney("0");
				}
				
				Double withdrawMoney = 0.0;
				try {
					withdrawMoney = StringUtils.isEmpty(groupResult.getWithdrawMoney()) ? 0.0 : Double.parseDouble(groupResult.getWithdrawMoney());
				} catch(Exception e) {
					e.printStackTrace();
				}
				MDataMap insertMap = new MDataMap();
				insertMap.put("balance", String.valueOf(withdrawMoney));
				insertMap.put("member_code", memberCode);
				insertMap.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				insertMap.put("result_message", groupResult.getResultMessage());
				int count = DbUp.upTable("gc_micro_community_balance").dataCount("member_code=:member_code", insertMap);
				if(count > 0) {
					DbUp.upTable("gc_micro_community_balance").dataUpdate(insertMap, "balance,update_time,result_message", "member_code");
				} else {
					DbUp.upTable("gc_micro_community_balance").dataInsert(insertMap);
				}
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		JobMicroCommunityBalance jb = new JobMicroCommunityBalance();
		jb.doExecute(null);
	}
}
