package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer.Req;

import com.cmall.familyhas.api.input.ApiPersonHbInput;
import com.cmall.familyhas.api.result.ApiPersonHbResult;
import com.cmall.groupcenter.homehas.RsyncGetCustRelHb;
import com.cmall.groupcenter.homehas.model.RsyncModelCustRelHb;
import com.cmall.groupcenter.homehas.model.RsyncResponseGetCustRelHb;
import com.cmall.systemcenter.util.AESUtil;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
/**
 * 个人惠币账户查询接口
 */
public class ApiPersonHb extends RootApiForToken<ApiPersonHbResult, ApiPersonHbInput> {
	@Override
	public ApiPersonHbResult Process(ApiPersonHbInput inputParam, MDataMap mRequestMap) {
		ApiPersonHbResult result = new ApiPersonHbResult();
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		AESUtil aesutil = new AESUtil();
		aesutil.initialize();
		String loginName = URLEncoder.encode(aesutil.encrypt(getOauthInfo().getLoginName()));
		String accessToken = URLEncoder.encode(aesutil.encrypt(getOauthInfo().getAccessToken()));
		String id = bConfig("familyhas.hbid");
		String path = bConfig("familyhas.hbpath");
		result.setMiniprogram_id(id);
		result.setMiniprogram_path(String.format(path, loginName,accessToken));
		String custId = levelInfo.getCustId();
		String sql = "select * from fh_tgz_withdraw_info where member_code =:member_code limit 0,1";
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", getUserCode());
		Map<String, Object> dataSqlOne2 = DbUp.upTable("fh_tgz_withdraw_info").dataSqlOne(sql, mWhereMap);
		if(dataSqlOne2 != null && dataSqlOne2.size() > 0) {
			result.setName(MapUtils.getString(dataSqlOne2, "name", ""));
			result.setIdcard_number(MapUtils.getString(dataSqlOne2, "idcard_number", ""));
		}
		List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_tgz_withdraw_info").dataSqlList("select * from fh_tgz_withdraw_info WHERE create_time > date_format(now(),'%Y-%m-%d') and `status` != '4497471600620003'and member_code =:member_code", mWhereMap);
		List<Map<String,Object>> dataSqlList1 = DbUp.upTable("fh_tgz_withdraw_info").dataSqlList("select * from fh_tgz_withdraw_info WHERE `status` = '4497471600620001'and member_code =:member_code", mWhereMap);
		
		if (dataSqlList.size() > 0) {
			result.setWithdraw("0");
			result.setReason("今天已有待提现或者已提现记录");
		}
		
		if(dataSqlList1.size() > 0) {
			result.setWithdraw("0");
			result.setReason("已有待提现记录");
		}
		
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 25) {
			result.setWithdraw("0");
			result.setReason("每月的25~31号才可以提现,现在不能提现");
		}
		DecimalFormat df = new DecimalFormat("#.###");
		/**
		 * 查询积分数量、储值金、暂存款
		 */
		if (StringUtils.isNotEmpty(custId)) {
			GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(custId);
			if (amtRef != null) {
				//添加惠币
				BigDecimal possHcoinAmt = amtRef.getPossHcoinAmt();
				if (possHcoinAmt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
					result.setHbTotal(df.format(possHcoinAmt.doubleValue()));
					if(possHcoinAmt.doubleValue() < 100.0) {
						result.setWithdraw("0");
						result.setReason("惠币余额小于100不能提现");
					}
				}else {
					result.setHbTotal("100000+");
				}
				BigDecimal preHcoinAmt = amtRef.getPreHcoinAmt();
				if (preHcoinAmt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
					result.setYghbTotal(df.format(preHcoinAmt.doubleValue()));
				}else {
					result.setYghbTotal("100000+");
				}
				RsyncGetCustRelHb rsync = new RsyncGetCustRelHb();
				rsync.upRsyncRequest().setCust_id(custId);
				rsync.doRsync();
				
				RsyncResponseGetCustRelHb upProcessResult = rsync.upProcessResult();
				if(upProcessResult != null) {
					BigDecimal ljdzhb = upProcessResult.getResult().getLjdzhb();
					result.setYtxhbTotal(df.format(ljdzhb.doubleValue()));
				}
				
//				Map<String, Object> dataSqlOne = DbUp.upTable("fh_tgz_withdraw_info").dataSqlOne("select SUM(apply_money) as total from fh_tgz_withdraw_info where member_code =:member_code and `status` in ('4497471600620002','4497471600620001')", mWhereMap);
//				Object objTotal = dataSqlOne.get("total");
//				BigDecimal ytxhbbTotal = new BigDecimal(0.00);
//				if(objTotal != null) {
//					ytxhbbTotal = (BigDecimal)objTotal;
//				}
//				if (ytxhbbTotal.compareTo(BigDecimal.valueOf(100000)) <= 0 && possHcoinAmt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
//					result.setYtxhbTotal(df.format(ytxhbbTotal.doubleValue()+possHcoinAmt.doubleValue()));
//				}else {
//					result.setYtxhbTotal("100000+");
//				}
			}
		}
		return result;
	}

}
