package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.webfunc.FuncEdit;
public class FuncEditSellerFaPiaoInfos  extends FuncEdit{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		   
		MDataMap logMap = new MDataMap();
		MWebResult mResult = new MWebResult();
		String createTime = DateUtil.getNowTime();
		String user = UserFactory.INSTANCE.create().getRealName();
		logMap.put("operate_time", createTime);
		logMap.put("operator", user);
		logMap.put("remark", "发票信息维护修改提交");
		logMap.put("small_seller_code",mDataMap.get("zw_f_small_seller_code") );
		logMap.put("uid", WebHelper.upUuid());
		
		MDataMap paramMap = new MDataMap();
		paramMap.put("taxpayer_certificate_select", mDataMap.get("zw_f_taxpayer_certificate_select").toString());
		paramMap.put("taxpayer_certificate_input", mDataMap.get("zw_f_taxpayer_certificate_input").toString());
		paramMap.put("bank_account", mDataMap.get("zw_f_bank_account").toString());
		paramMap.put("address", mDataMap.get("zw_f_address").toString());
		paramMap.put("phone", mDataMap.get("zw_f_phone").toString());
		paramMap.put("receiver_address", mDataMap.get("zw_f_receiver_address").toString());
		paramMap.put("receiver_name", mDataMap.get("zw_f_receiver_name").toString());
		paramMap.put("mail", mDataMap.get("zw_f_mail").toString());
		paramMap.put("telphone_num", mDataMap.get("zw_f_telphone_num").toString());
		paramMap.put("small_seller_code", mDataMap.get("zw_f_small_seller_code").toString());
		
		
		int dataUpdate = DbUp.upTable("uc_seller_invoice_info").dataUpdate(paramMap, "telphone_num,mail,receiver_name,receiver_address,phone,address,bank_account,taxpayer_certificate_input,taxpayer_certificate_select", "small_seller_code");
		if(dataUpdate==0) {
			Map<String, Object> map = DbUp.upTable("v_uc_sellers_invoice_info").dataSqlOne("select * from v_uc_sellers_invoice_info where small_seller_code=:small_seller_code", new MDataMap("small_seller_code",mDataMap.get("zw_f_small_seller_code")));
			paramMap.put("tax_identification_number", map.get("tax_identification_number").toString());
			paramMap.put("account_clear_type", map.get("account_clear_type").toString());
			paramMap.put("uc_seller_type", map.get("uc_seller_type").toString());
			paramMap.put("small_seller_name", map.get("seller_company_name").toString());
			paramMap.put("uid", WebHelper.upUuid());
			DbUp.upTable("uc_seller_invoice_info").dataInsert(paramMap);
		}
		DbUp.upTable("lc_fapiao_seller_infos_log").dataInsert(logMap);
		return mResult;
	}

}
