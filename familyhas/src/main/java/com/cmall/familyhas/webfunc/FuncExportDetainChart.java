package com.cmall.familyhas.webfunc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srnpr.zapweb.webexport.ExportChart;
import com.srnpr.zapweb.webmodel.MPageData;

/**
 * 退货挽留列表导出
 */
public class FuncExportDetainChart extends ExportChart {

	@Override
	public void exportExcel(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		super.exportExcel(sOperateId, request, response);
		
		MPageData pageData = getPageData();
//		int productIndex = pageData.getPageField().indexOf("product_code");
		
		if(pageData.getPageData() == null) {
			return;
		}
		
		pageData.getPageHead().remove(0);
		pageData.getPageField().remove(0);
		for(List<String> dataList : pageData.getPageData()) {
			dataList.remove(0);
		}
		
		setPageData(pageData);
	}
	
}
