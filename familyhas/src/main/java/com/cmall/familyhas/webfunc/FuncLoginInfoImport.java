package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.groupcenter.homehas.HomehasSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 导入手机号自动注册
 */
public class FuncLoginInfoImport extends FuncAdd{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		List<MDataMap> excelDataList = null;
		try {
			excelDataList = new ExcelSupport().upExcelFromUrl(uploadFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件失败："+e);
			return mResult;
		}
		
		if(excelDataList == null || excelDataList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入文件无内容");
			return mResult;
		}
		
		String phone;
		List<String> error = new ArrayList<String>();
		Set<String> phoneList = new HashSet<String>();
		for(MDataMap data : excelDataList) {
			phone = StringUtils.trimToEmpty(data.get("手机号"));
			if(StringUtils.isBlank(phone)) continue;
			
			// 验证手机号格式，1开头的11位数字
			if(!phone.matches("1\\d{10}")) {
				error.add(phone);
				continue;
			}
			
			phoneList.add(phone);
		}
		
		if(!error.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("手机号格式错误："+StringUtils.join(error,","));
			return mResult;
		}
		
		String lockKey = KvHelper.lockCode("FuncLoginInfoImport", 60000);
		if(StringUtils.isBlank(lockKey)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("后台正在处理中，稍等后再试");
			return mResult;
		}
		
		HomehasSupport hhs = new HomehasSupport();
		String result;
		for(String val : phoneList) {
			phone = val;
			
			if(DbUp.upTable("mc_login_info").count("manage_code","SI2003","login_name", phone) > 0) {
				continue;
			}
			
			result = hhs.register(phone, RandomStringUtils.randomNumeric(8)); 
			if(StringUtils.isBlank(result)) {
				error.add(phone);
			}
		}
		
		KvHelper.unlockCode("FuncLoginInfoImport", lockKey);
		
		if(!error.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入失败手机号："+StringUtils.join(error,","));
		}
		
		return mResult;
		
	}

}