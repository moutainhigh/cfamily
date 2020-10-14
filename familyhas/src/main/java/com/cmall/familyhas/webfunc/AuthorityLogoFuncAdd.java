package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class AuthorityLogoFuncAdd extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String logoContent = mDataMap.get("zw_f_logo_content");
		if(StringUtils.isEmpty(logoContent) || logoContent.length() > 30){
			mResult.inErrorMessage(916401205);//权威字符说明不能为空且不能超过30个字符
			return mResult;
		}
		String manage_code = UserFactory.INSTANCE.create().getManageCode();
		int count = DbUp.upTable("pc_authority_logo").count("logo_content",
				logoContent, "manage_code", manage_code);
		if (count > 0) {
			mResult.inErrorMessage(916401204);
			return mResult;
		}
		/* 系统当前时间 */
		String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();

		mDataMap.put("zw_f_create_time", create_time);
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_update_time", create_time);
		mDataMap.put("zw_f_update_user", create_user);
		mDataMap.put("zw_f_manage_code", manage_code);
		if("449747110002".equals(mDataMap.get("zw_f_all_flag"))){//是否全场为是
			mDataMap.put("zw_f_show_product_source", "");
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		
		//删除商品权威标志的缓存
		XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003");
		XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003-ProductLog");
		return mResult;
	}

}
