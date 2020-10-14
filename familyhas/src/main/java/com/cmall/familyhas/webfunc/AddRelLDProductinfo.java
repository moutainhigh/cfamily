package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;


public class AddRelLDProductinfo extends FuncAdd{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		MDataMap mWhereMap = new MDataMap();
		String sSql = " SELECT * FROM productcenter.pc_hjyLD_rel_LD_productinfo WHERE ( product_code = :product_code AND sku_code = :sku_code ) OR ( ld_product_code = :ld_product_code AND ld_sku_code = :ld_sku_code ) ";
		mWhereMap.put("product_code", mDataMap.get("zw_f_product_code"));
		mWhereMap.put("sku_code", mDataMap.get("zw_f_sku_code"));
		mWhereMap.put("ld_product_code", mDataMap.get("zw_f_ld_product_code"));
		mWhereMap.put("ld_sku_code", mDataMap.get("zw_f_ld_sku_code"));
		
		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_hjyLD_rel_LD_productinfo").dataSqlList(sSql, mWhereMap);
		
		if(dataSqlList.size() > 0) {
			mResult.setResultCode(0);
			Map<String, Object> hadInfo = dataSqlList.get(0);
			mResult.setResultMessage("已存在对应关系[惠家有商品:"+hadInfo.get("product_code")+"-"+hadInfo.get("sku_code")+"<==>LD商品："+hadInfo.get("ld_product_code")+"-"+hadInfo.get("ld_sku_code")+"]");
		} else {
			String loginname=UserFactory.INSTANCE.create().getLoginName();
			if(loginname==null||"".equals(loginname)){
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			mDataMap.put("zw_f_create_user", loginname);
			mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
			
			mResult = super.funcDo(sOperateUid, mDataMap);
		}
		
		return mResult;
	}
	
}
