package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiUserIdentityInfoInput;
import com.cmall.familyhas.api.result.ApiUserIdentityInfoResult;
import com.cmall.familyhas.util.OperFlagEnum;
import com.cmall.ordercenter.service.MemberAuthInfoSupport;
import com.cmall.systemcenter.util.AESUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 用户身份信息业务处理
 * @author pang_jhui
 *
 */
public class UserIdentityInfoService extends BaseClass {
	
	/**
	 * 用户信息业务处理入口
	 * @param input
	 * 		输入参数
	 * @param mDataMap
	 * 		扩展参数
	 * @return ApiUserIdentityInfoResult
	 * 		用户身份信息
	 */
	public ApiUserIdentityInfoResult doProcess(ApiUserIdentityInfoInput input,MDataMap mDataMap){
		
		if(StringUtils.equalsIgnoreCase(input.getOperFlag(), OperFlagEnum.CHECK.name())){
			
			return doCheck(input, mDataMap);
			
		}
		
		if(StringUtils.equalsIgnoreCase(input.getOperFlag(), OperFlagEnum.UPDATE.name())){
			
			return doUpdate(input, mDataMap);
			
		}
		
		
		return null;
		
		
	}
	
	/**
	 * 校验身份证信息是否通关成功
	 * @param input
	 * 		输入参数
	 * @param mDataMap
	 * 		扩展参数
	 * @return ApiUserIdentityInfoResult
	 * 		返回结果信息
	 */
	public ApiUserIdentityInfoResult doCheck(ApiUserIdentityInfoInput input, MDataMap mDataMap) {

		ApiUserIdentityInfoResult result = new ApiUserIdentityInfoResult();

		MemberAuthInfoSupport support = new MemberAuthInfoSupport();

		String aesIdNumber = support.deIdNumber(input.getIdNumber());
		
		String custoemStatus = support.getCustomStatus(aesIdNumber);

		if (StringUtils.equals(custoemStatus, FamilyConfig.CUSTOMS_STATUS_FAILURE)) {

			result.inErrorMessage(916421182);

		}

		return result;

	}
	
	/**
	 * 更新身份证信息
	 * @param input
	 * 		输入参数
	 * @param mDataMap
	 * 		扩展参数
	 * @return ApiUserIdentityInfoResult
	 * 		返回结果
	 */
	public ApiUserIdentityInfoResult doUpdate(ApiUserIdentityInfoInput input, MDataMap mDataMap){
		
		ApiUserIdentityInfoResult result = new ApiUserIdentityInfoResult();
			
		
		return result;
		
	}

}
