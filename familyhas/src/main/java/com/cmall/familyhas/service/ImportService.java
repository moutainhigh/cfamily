package com.cmall.familyhas.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ImportService {

	/**
	 * 导入excel文件内容
	 * @param fileUrl
	 * @param sheetIndex 第几个工作表
	 * @return
	 * @throws IOException
	 */
	public List<Row> importExcel (String fileUrl,int sheetIndex) throws IOException {
		List<Row> rtnList = new ArrayList<Row>();
		
		if(!StringUtils.isEmpty(fileUrl) ) {
			java.net.URL resourceUrl = new java.net.URL(fileUrl);
			InputStream instream = (InputStream) resourceUrl.getContent();
			if(null != instream){
				rtnList = readExcel(instream,sheetIndex);
			}
		}
		return rtnList;
	}
	/**
	 * 获取excel数据(会过滤掉excel第一行数据，从第二行数据开始读取)
	 * @param input
	 * @param sheetIndex 第几个工作表
	 * @return
	 * @throws IOException
	 */
	public List<Row> readExcel(InputStream input,int sheetIndex) throws IOException {
		
		List<Row> result = new ArrayList<Row>();
		Workbook wb = null;
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(sheetIndex);
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			result.add(sheet.getRow(rIndex));
		}
		
		return result;
	}
	
	
	
}
