package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.cmall.productcenter.common.DateUtil;
import com.cmall.productcenter.service.BrandService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd; 
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 网站编辑批量审批上架
 */
public class FuncBatchUploadBrand extends FuncAdd{
	
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
		
		
		ArrayList<String> errors = new ArrayList<String>();
		String cnName;
		String EnName;
		String isUse;
		String brandSc;
		for(MDataMap _mDataMap : excelDataList) {
			cnName = StringUtils.trimToEmpty(_mDataMap.get("中文名称"));
			if(StringUtils.isBlank(cnName)) {
				continue;
			}
			isUse = StringUtils.trimToEmpty(_mDataMap.get("是否启用(启用:Y,启用:N)"));
			if(StringUtils.isBlank(isUse)) {
				continue;
			}else {
				if((!"Y".equalsIgnoreCase(isUse))&&(!"N".equalsIgnoreCase(isUse))) {
					continue;
				}
			}
			if("Y".equalsIgnoreCase(isUse)) {
				isUse = "1";
			}
			if("N".equalsIgnoreCase(isUse)) {
				isUse = "0";
			}
			EnName = StringUtils.trimToEmpty(_mDataMap.get("英文名称"));
			brandSc = StringUtils.trimToEmpty(_mDataMap.get("品牌描述"));
			//根据brandName与uid校验在数据库中是否有相同品牌名称存在,如果存在则返回提示信息
			int atCount = DbUp.upTable("pc_brandinfo").count("brand_name", cnName);		//判断数据库中是否存在品牌名称
			if(atCount >= 1){
				//返回提示信息
				mResult.setResultCode(0);
				mResult.setResultMessage("品牌名称:"+cnName+"与现有品牌名字重复!");
				return mResult;
			}
			MDataMap data = new MDataMap();
			data.put("brand_name",cnName);
			data.put("brand_name_en",EnName);
			data.put("brand_note",brandSc);
			data.put("flag_enable",isUse);
			
			MUserInfo userInfo = UserFactory.INSTANCE.create();
			String create_usercode = "";
			String create_usenm = "";
			if (null != userInfo) {
				/*获取当前登录人*/
				create_usercode = userInfo.getUserCode();
				create_usenm = userInfo.getRealName();
			}
			data.put("create_time", DateUtil.getSysDateTimeString());
			data.put("create_usernm", create_usenm);
			data.put("create_usercode", create_usercode);
			
			BrandService service = new BrandService();
			boolean flag = service.addBrand(data);
			
			if (flag == false) {
				// 异常处理待定
				mResult.inErrorMessage(939301099);
			}else {
				mResult.setResultMessage(bInfo(969909001));
			}
			
		}
		
		if(!errors.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("问题商品："+StringUtils.join(errors,","));
			return mResult;
		}
		
		return mResult;
	}
}
