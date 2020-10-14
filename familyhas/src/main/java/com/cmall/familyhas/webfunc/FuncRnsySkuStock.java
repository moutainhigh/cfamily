package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.groupcenter.homehas.RsyncGetStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 手工同步商品库存
 * @author jlin
 *
 */
public class FuncRnsySkuStock  extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		if (mResult.upFlagTrue()) {
			
			String uid=mAddMaps.get("uid");
			String product_code_old=(String)DbUp.upTable("pc_productinfo").dataGet("product_code_old", "uid=:uid", new MDataMap("uid",uid));
			if(StringUtils.isBlank(product_code_old)){
				mResult.inErrorMessage(916401206);
				return mResult;
			}
			
			//开始执行同步
			RsyncGetStock rsyncGetStock=new RsyncGetStock();
			rsyncGetStock.upRsyncRequest().setGood_id(product_code_old);
			rsyncGetStock.doRsync();
			
			if(!rsyncGetStock.responseSucc()){
				mResult.inErrorMessage(916401207);
				return mResult;
			}
			
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;

	}
	
}
