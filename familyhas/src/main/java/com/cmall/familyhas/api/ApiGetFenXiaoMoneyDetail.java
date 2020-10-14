package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetFenXiaoMoneyDetailInput;
import com.cmall.familyhas.api.result.ApiGetFenXiaoMoneyDetailResult;
import com.cmall.familyhas.api.result.FXDetail;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 提现明细接口
 * 
 */

public class ApiGetFenXiaoMoneyDetail extends RootApiForVersion<ApiGetFenXiaoMoneyDetailResult, ApiGetFenXiaoMoneyDetailInput> {

	@Override
	public ApiGetFenXiaoMoneyDetailResult Process(ApiGetFenXiaoMoneyDetailInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiGetFenXiaoMoneyDetailResult result = new ApiGetFenXiaoMoneyDetailResult();
		int pageNum = inputParam.getPageNum();
		int dataCount = DbUp.upTable("fh_agent_withdraw").dataCount("member_code=:member_code", new MDataMap("member_code",getOauthInfo().getUserCode()));
		int pageSize = 20;
		int totalPage = 0;
		int start = (pageNum-1)*pageSize;
		if(dataCount%pageSize!=0) {
			totalPage=dataCount/pageSize+1;
		}else {
			totalPage=dataCount/pageSize;
		}
		result.setTotalPage(totalPage);
		List<Map<String,Object>> list = DbUp.upTable("fh_agent_withdraw").dataSqlList("select * from fh_agent_withdraw where member_code=:member_code order by zid desc limit "+start+","+pageSize, new MDataMap("member_code",getOauthInfo().getUserCode()));
		if(list!=null&&list.size()>0) {
			for (Map<String, Object> map : list) {
				String status = map.get("apply_status").toString();
				if("4497484600050007".equals(status)) {
					FXDetail detail = new FXDetail();
					detail.setCreateime(map.get("create_time").toString());
					detail.setState("1");
					detail.setWithdrawMoney(map.get("withdraw_money").toString());
					result.getFxDetailList().add(detail);
				}else if("4497484600050006".equals(status)) {
					FXDetail detail = new FXDetail();
					detail.setCreateime(map.get("create_time").toString());
					detail.setState("2");
					detail.setWithdrawMoney(map.get("withdraw_money").toString());
					result.getFxDetailList().add(detail);
				}else {
					FXDetail detail = new FXDetail();
					detail.setCreateime(map.get("create_time").toString());
					detail.setState("0");
					detail.setWithdrawMoney(map.get("withdraw_money").toString());
					result.getFxDetailList().add(detail);
				}
			}
		}
		return result;
	}

}
