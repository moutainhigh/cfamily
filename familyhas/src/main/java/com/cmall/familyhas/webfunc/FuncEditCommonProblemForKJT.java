package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改跨境通常见问题
 * @author ligj
 *
 */
public class FuncEditCommonProblemForKJT extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		/*系统当前时间*/
		String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		if (mResult.upFlagTrue()) {
			/* 获取当前修改人 */
			String update_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", update_time);
			mDataMap.put("zw_f_update_user", update_user);
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
				//删除跨境通商品的缓存
				XmasKv.upFactory(EKvSchema.ProductCommonProblem).del("SI2003");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
