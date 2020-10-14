package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @ClassName: BrandPreferenceFuncDelete 
* @Description: 品牌特惠专题删除
* @author 张海生
* @date 2015-5-5 下午6:14:23 
*  
*/
public class BrandPreferenceFuncDelete extends FuncDelete {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MDataMap mdata = DbUp.upTable("fh_brand_preference").oneWhere(
				"flag_show", null, null, "uid", mDataMap.get("zw_f_uid"));
		if (mdata != null && "449746530001".equals(mdata.get("flag_show"))) {// 已发布
			mResult.inErrorMessage(916401232);// 已上线的专题，不允许删除
			return mResult;
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
