package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.PhoneNumberFilterInput;
import com.cmall.familyhas.api.result.PhoneNumberFilterResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 新增浏览历史
 * @author liqt
 *
 */
public class ApiForPhoneNumberFilter extends RootApiForVersion<PhoneNumberFilterResult, PhoneNumberFilterInput>{
	public PhoneNumberFilterResult Process(PhoneNumberFilterInput input,MDataMap mDataMap){
		PhoneNumberFilterResult result = new PhoneNumberFilterResult();
		String nums = input.getNums();
		String app_version =  input.getCompareVersion();
		if(!"".equals(nums)&&!"".equals(app_version)) {
			String sql = "SELECT mobile,CASE WHEN app_version < '"+app_version+"' THEN 'Y'  ELSE 'N' END flag FROM  logcenter.lc_push_device_info WHERE mobile in ("+nums+");";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_push_device_info").dataSqlList(sql, null);
			String lowNums = "";
			String highNums = "";
			for(Map<String, Object> map : dataSqlList) {
				String flag = map.get("flag").toString();
				String mobile = map.get("mobile").toString();
				if("Y".equals(flag)) {
					lowNums  = lowNums + mobile +",";
				}else {
					highNums =highNums +mobile +",";
				}
			}
			if(lowNums.length()>0) {
				lowNums = lowNums.substring(0, lowNums.length()-1);
			}
			result.setLowNums(lowNums);
			result.setHighNums(highNums);
		}
		return result;
	}
	
}
