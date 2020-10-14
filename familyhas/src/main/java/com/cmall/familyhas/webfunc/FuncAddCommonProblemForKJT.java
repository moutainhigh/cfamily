package com.cmall.familyhas.webfunc;

import com.cmall.systemcenter.common.AppConst;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 跨境通常见问题
 * @author ligj
 *
 */
public class FuncAddCommonProblemForKJT extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			MUserInfo create_user = UserFactory.INSTANCE.create();
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_create_user", create_user.getLoginName());
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", create_user.getLoginName());
			mDataMap.put("zw_f_seller_code", create_user.getManageCode());
			mDataMap.put("zw_f_small_seller_code", AppConst.MANAGE_CODE_KJT);
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
				//删除常见问题的缓存
				XmasKv.upFactory(EKvSchema.ProductCommonProblem).del("SI2003");
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
