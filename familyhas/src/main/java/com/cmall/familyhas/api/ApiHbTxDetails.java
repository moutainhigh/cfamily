package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiHbTxDetailsInput;
import com.cmall.familyhas.api.model.HbTxDetailInfo;
import com.cmall.familyhas.api.result.ApiHbTxDetailsResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
/**
 * 惠币提现明细查询接口
 */
public class ApiHbTxDetails extends RootApiForToken<ApiHbTxDetailsResult, ApiHbTxDetailsInput> {

	
	static int pageSize = 10;

	@Override
	public ApiHbTxDetailsResult Process(ApiHbTxDetailsInput inputParam, MDataMap mRequestMap) {
		ApiHbTxDetailsResult result = new ApiHbTxDetailsResult();
		List<HbTxDetailInfo> hbs = new ArrayList<HbTxDetailInfo>();
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
		String custId = levelInfo.getCustId();
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", getUserCode());
		DecimalFormat df = new DecimalFormat("#.##");
		int nextPage = NumberUtils.toInt(inputParam.getNowPage(), 1);
		if(nextPage < 1) nextPage = 1;
		
		
		// 查询总数
		String sql = "select count(*) as num from fh_tgz_withdraw_info where member_code =:member_code";
		Map<String, Object> totalMap = DbUp.upTable("fh_tgz_order_detail").dataSqlOne(sql, mWhereMap);
		int totalNum = NumberUtils.toInt(totalMap.get("num").toString(), 0);
		
		// 计算总页数
		int totalPage = totalNum/pageSize;
		if(totalNum % pageSize > 0) {
			totalPage++;
		}
		result.setTotalPage(String.valueOf(totalPage));
		result.setNowPage(String.valueOf(nextPage));
		
		// 查询订单明细
		sql = "select * from fh_tgz_withdraw_info where member_code =:member_code";
		sql += " ORDER BY zid DESC LIMIT "+(nextPage-1)*pageSize+", "+pageSize;
		
		
		List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_tgz_withdraw_info").dataSqlList(sql, mWhereMap);
		for(Map<String,Object> map : dataSqlList) {
			HbTxDetailInfo detailInfo = new HbTxDetailInfo();
			detailInfo.setApply_money(df.format((BigDecimal)map.get("apply_money")));
			detailInfo.setCreate_time(map.get("create_time").toString());
			detailInfo.setStatus(map.get("status").toString());
			hbs.add(detailInfo);
		}
		result.setHbs(hbs);
		return result;
	}

}
