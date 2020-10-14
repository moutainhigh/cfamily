package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForResetMerchantPwdInput;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;


/** 
* @ClassName: ApiForChangeMerchantPwd 
* @Description: 冲着商户密码
* @author 张海生
* @date 2015-6-15 上午11:33:39 
*  
*/
public class ApiForResetMerchantPwd extends
		RootApi<RootResult, ApiForResetMerchantPwdInput> {

	public RootResult Process(ApiForResetMerchantPwdInput input,
			MDataMap mRequestMap) {
		RootResult result = new RootResult();
		try {
			MDataMap updataMap = new MDataMap();
			updataMap.put("user_name", input.getUserName());
			updataMap.put("user_password", SecrurityHelper.MD5Customer(bConfig("familyhas.dsf_password")));//默认密码重置成88888888
			DbUp.upTable("za_userinfo").dataUpdate(updataMap, "user_password", "user_name");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
