package com.cmall.familyhas.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.systemcenter.util.AESUtil;
import com.srnpr.zapcom.baseclass.BaseClass;

public class TemplateService extends BaseClass{

	public Map<String, String> decodeMobileAndToken(String mobile, String token){
		Map<String, String> result = new HashMap<String, String>();
		
		AESUtil aesUtil = new AESUtil();
		aesUtil.initialize();
		result.put("mobile", StringUtils.isNotEmpty(mobile) ? aesUtil.decrypt(mobile) : "");
		result.put("token", StringUtils.isNotEmpty(token) ? aesUtil.decrypt(token) : "");
		
		return result;
	}
}
