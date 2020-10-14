package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

public class AuthorityLogoFuncEdit extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String logoContent = mDataMap.get("zw_f_logo_content");
		String uid = mDataMap.get("zw_f_uid");
		MDataMap mdata = new MDataMap();
		
		mdata.put("uid", uid);
		mdata.put("logoContent", logoContent);
		if(StringUtils.isEmpty(logoContent) || logoContent.length() > 30){
			mResult.inErrorMessage(916401205);//权威字符说明不能为空且不能超过30个字符
			return mResult;
		}
		/*dataCount 权威标识说明不能重复，参数一位查询条件二为传进来的键*/
		int count = DbUp.upTable("pc_authority_logo").dataCount(
				"logo_content=:logoContent and uid<>:uid", mdata);
		
		/*判断标识说明是否唯一*/
		if (count > 0) {
			mResult.inErrorMessage(916401204);
			return mResult;
		}

		/* 系统更新时间 */
		String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();

		/* 获取当前修改人 */
		String update_user = UserFactory.INSTANCE.create().getLoginName();

		mDataMap.put("update_time", update_time);
		mDataMap.put("update_user", update_user);
		if("449747110002".equals(mDataMap.get("zw_f_all_flag"))){//是否全场为是
			mDataMap.put("zw_f_show_product_source", "");
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		//删除商品权威标志的缓存
		XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003");
		XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003-ProductLog");
		return mResult;
	}

}
