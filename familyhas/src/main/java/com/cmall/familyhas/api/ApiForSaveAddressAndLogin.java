package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.AddressInput;
import com.cmall.familyhas.api.input.ApiForSaveAddressAndLoginInput;
import com.cmall.familyhas.api.result.ApiForSaveAddressAndLoginResult;
import com.cmall.familyhas.service.ManageAddressService;
import com.cmall.groupcenter.support.GroupAccountSupport;
import com.cmall.membercenter.oauth.model.CheckUserInfoInput;
import com.cmall.membercenter.oauth.model.CheckUserInfoResult;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.systemcenter.enumer.EVerifyCodeTypeEnumer;
import com.cmall.systemcenter.support.VerifySupport;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootResultWeb;


/** 
* @ClassName: ApiForSaveAddressAndLogin 
* @Description: 保存收货地址
* @author 张海生
* @date 2015-7-2 上午11:41:34 
*  
*/
public class ApiForSaveAddressAndLogin extends RootApiForManage<ApiForSaveAddressAndLoginResult, ApiForSaveAddressAndLoginInput> {

	public ApiForSaveAddressAndLoginResult Process(ApiForSaveAddressAndLoginInput inputParam,MDataMap mRequestMap) {
		ApiForSaveAddressAndLoginResult result = new ApiForSaveAddressAndLoginResult();
		String mobile = inputParam.getMobile();
		VerifySupport verifySupport = new VerifySupport();
		result.inOtherResult(verifySupport.checkVerifyCodeByType(
				EVerifyCodeTypeEnumer.Binding, mobile,
				inputParam.getVerifyCode())//验证验证码是否正确
		);
		if(result.upFlagTrue()){
			MemberLoginSupport ms = new MemberLoginSupport();
			CheckUserInfoResult checkUserInfoResult = new CheckUserInfoResult();
			CheckUserInfoInput checkUserInfo = new CheckUserInfoInput();
			checkUserInfo.setLoginName(mobile);
			checkUserInfoResult = ms.doUserLoginByThirdParty(checkUserInfo, super.getManageCode());//注册登录获取token
			if(checkUserInfoResult.upFlagTrue()){
				result.setUserToken(checkUserInfoResult.getAccessToken());
			}else{
				result.inErrorMessage(checkUserInfoResult.getResultCode());
			}
		}
		if(result.upFlagTrue()){//绑定上下级关系
			String referrerMobile = inputParam.getReferrerMobile();
			//判断是否有绑定上下级需求
			if(!StringUtils.isEmpty(referrerMobile)){
				MDataMap userMap = DbUp.upTable("mc_login_info").one("login_name",referrerMobile);
				if(null == userMap){
					result.setResultCode(0);
					result.setResultMessage("推荐人不存在");
				}else{
					String sql = "SELECT mm.account_code FROM mc_login_info ml LEFT JOIN mc_member_info mm" +
							" ON ml.member_code = mm.member_code where ml.login_name=:loginName and ml.manage_code =:manageCode";
					MDataMap mWhereMap1 = new MDataMap();
					mWhereMap1.put("loginName", referrerMobile);
					mWhereMap1.put("manageCode", super.getManageCode());
					Map<String,Object> memMap1 = DbUp.upTable("mc_member_info").dataSqlOne(sql, mWhereMap1);
					String parentAccountCode = (String)memMap1.get("account_code");
					MDataMap mWhereMap2 = new MDataMap();
					mWhereMap2.put("loginName", mobile);
					mWhereMap2.put("manageCode", super.getManageCode());
					Map<String,Object> memMap2 = DbUp.upTable("mc_member_info").dataSqlOne(sql, mWhereMap2);
					String accountCode = (String)memMap2.get("account_code");
					GroupAccountSupport groupAccountSupport=new GroupAccountSupport();
					groupAccountSupport.createRelation(accountCode,parentAccountCode, "",FormatHelper.upDateTime());
				}
			}
		}
		if(result.upFlagTrue()){
			MDataMap mp = DbUp.upTable("mc_login_info").oneWhere("member_code",
					"", "", "login_name", mobile, "manage_code",
					super.getManageCode());//获取用户code
			ManageAddressService ms= new ManageAddressService();
			AddressInput input = new AddressInput();
			input.setReceive_person(inputParam.getReceiver());
			input.setArea_code(inputParam.getAreaCode());
			input.setAddress(inputParam.getDetailAddress());
			input.setMobilephone(inputParam.getMobile());
			input.setFlag_default(inputParam.getFlagDefault());
			RootResultWeb webresult = ms.saveAddress(input, super.getManageCode(), mp.get("member_code"));//保存收货地址
			result.inErrorMessage(webresult.getResultCode());
		}
		return result;
	}
}
