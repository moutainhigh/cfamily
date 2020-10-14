package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webexport.RootExport;
import com.srnpr.zapweb.webmodel.MPageData;

/**
 * 导出银行信息
 */
public class FuncBankInfoExportService extends RootExport {

	@Override
	public void export(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		List<MDataMap> bankInfoList = DbUp.upTable("uc_bankinfo").queryAll("", "", "", new MDataMap());
		
		MPageData data = new MPageData();
		data.getPageHead().add("LD银行类别代码");
		data.getPageHead().add("NC银行类别代码");
		data.getPageHead().add("银行名称");
		data.getPageHead().add("冻结状态");
		
		List<String> vals;
		data.setPageData(new ArrayList<List<String>>());
		for(int i = 0; i < bankInfoList.size(); i++){
			vals = new ArrayList<String>();
			vals.add(StringUtils.trimToEmpty(bankInfoList.get(i).get("ld_code")));
			vals.add(StringUtils.trimToEmpty(bankInfoList.get(i).get("nc_code")));
			vals.add(StringUtils.trimToEmpty(bankInfoList.get(i).get("bank_name")));
			
			if("449746250001".equals(bankInfoList.get(i).get("bank_status"))){
				vals.add("是");
			}else if("449746250002".equals(bankInfoList.get(i).get("bank_status"))){
				vals.add("否");
			}else{
				vals.add("");
			}
			data.getPageData().add(vals);
		}
		
		setExportName("bankinfo");
		exportExcelFile(data, response);
	}

}
