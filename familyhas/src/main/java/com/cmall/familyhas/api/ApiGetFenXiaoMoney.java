package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetFenXiaoMoneyDetailInput;
import com.cmall.familyhas.api.input.ApiGetFenXiaoMoneyInput;
import com.cmall.familyhas.api.result.ApiGetFenXiaoMoneyDetailResult;
import com.cmall.familyhas.api.result.FXDetail;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * 提现接口
 * 
 */

public class ApiGetFenXiaoMoney extends RootApiForVersion<RootResultWeb, ApiGetFenXiaoMoneyInput> {

	@Override
	public RootResultWeb Process(ApiGetFenXiaoMoneyInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResultWeb rootResultWeb = new RootResultWeb();
		int monthDay = DateUtil.getMonthDay();
		int money = inputParam.getMoney();
		//升级的时候再改回规定日期
		if (!(monthDay>=25&&monthDay<=31)){
		rootResultWeb.setResultMessage("每个月25号-31号可提现！");
		rootResultWeb.setResultCode(0);
		return rootResultWeb;
	    } 
		int dataCount = DbUp.upTable("fh_agent_withdraw").dataCount("member_code=:member_code and apply_status in('4497484600050001','4497484600050002','4497484600050003','4497484600050004') ",new MDataMap("member_code",getOauthInfo().getUserCode()));
		if(dataCount>0) {
			rootResultWeb.setResultMessage("您有正在进行中的提现申请！");
			rootResultWeb.setResultCode(0);
			return rootResultWeb;
		}
		if(money<=0||money%100!=0) {
			rootResultWeb.setResultMessage("请输入大于0且为100的整数倍");
			rootResultWeb.setResultCode(0);
			return rootResultWeb;
		}else {
			List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_agent_member_info").dataSqlList("select * from fh_agent_member_info where member_code=:member_code order by zid desc", new MDataMap("member_code",getOauthInfo().getUserCode()));
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				String realMoney = dataSqlList.get(0).get("real_money").toString();
				 BigDecimal rlMoney = new BigDecimal(realMoney);
				 BigDecimal my = new BigDecimal(money);
				if(rlMoney.compareTo(my)>=0) {
					String sysDateTimeString = DateUtil.getSysDateTimeString();
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("uid",WebHelper.upUuid() );
					mDataMap.put("apply_code", WebHelper.upCode("TX"));
					mDataMap.put("member_code",getOauthInfo().getUserCode() );
					mDataMap.put("withdraw_money",money+"");
					mDataMap.put("apply_status", "4497484600050001");
					mDataMap.put("creator_source","4497484600060002" );
					mDataMap.put("create_time",sysDateTimeString);
					mDataMap.put("update_time",sysDateTimeString);
					DbUp.upTable("fh_agent_withdraw").dataInsert(mDataMap);
					//修改可提现金额
					BigDecimal newRealMoney = rlMoney.subtract(my);
					DbUp.upTable("fh_agent_member_info").dataUpdate(new MDataMap("real_money",newRealMoney.toString(),"member_code",getOauthInfo().getUserCode()), "real_money", "member_code");
				
					rootResultWeb.setResultCode(1);
					rootResultWeb.setResultMessage("提现申请成功,等待审核!");
					
				}else {
					rootResultWeb.setResultMessage("输入金额大于可提现金额！");
					rootResultWeb.setResultCode(0);
					return rootResultWeb;
				}
			}else {
				rootResultWeb.setResultMessage("无可提现金额！");
				rootResultWeb.setResultCode(0);
				return rootResultWeb;
			}

		}
		return rootResultWeb;
	}

}
