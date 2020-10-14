package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForGetMd5SecretInput;
import com.cmall.familyhas.util.MD5Util;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiForGetMd5Secret extends RootApiForVersion<RootResult, ApiForGetMd5SecretInput> {

	@Override
	public RootResult Process(ApiForGetMd5SecretInput inputParam, MDataMap mRequestMap) {

		String a = inputParam.getStr() + "dd41e5fb0d0342f5b17c4847bc1e181a";
		String secret = MD5Util.md5Hex(a);
		RootResult result = new RootResult();
		result.setResultCode(1);
		result.setResultMessage(secret);
		return result;
	}

	public static void main(String[] args) {
		String a = "1151788019607749159913161366600KLUK7816ALUM8254AC190704100001dd41e5fb0d0342f5b17c4847bc1e181a";
		String secret = MD5Util.md5Hex(a);
		System.out.println(secret);
	}

}
