package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.service.MyService;
import com.cmall.systemcenter.common.AppConst;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加"我的"图片
 * @author ligj
 *
 */
public class FuncAddMyPic extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MyService myService = new MyService();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		String sellerCode = AppConst.MANAGE_CODE_HOMEHAS;
		int count = myService.availableMyPic(sellerCode);
		if (count < 1) {
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", create_user);
			mDataMap.put("zw_f_seller_code", sellerCode);
			mDataMap.put("zw_f_flag_view", "1");
			mResult = super.funcDo(sOperateUid, mDataMap);
		}else{
			mResult.setResultCode(941901132);
			mResult.setResultMessage(bInfo(941901132));
		}
		return mResult;
	}
}
