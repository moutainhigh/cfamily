package com.cmall.familyhas.webfunc.ldmsgpay;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 批量导入橙意卡限制商品
 */
public class FuncBatchImportProduct extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		InputStream in = null;
		List<String> dataList = new ArrayList<String>(); 
		try {
			in = new URL(uploadFileUrl).openStream();
			Workbook wb = new HSSFWorkbook(in);
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum();
			int end = sheet.getLastRowNum();
			dataList = new ArrayList<String>((int)(end/0.75+1));
			
			DecimalFormat df = new DecimalFormat("#.#");
			
			start++; // 忽略第一行标题
			Row row;
			String cols = null;
			Cell cell;
			while(start <= end){
				row = sheet.getRow(start);
				start++;
				
				// 第一个单元格是商品编号
				cell = row.getCell(0,Row.RETURN_NULL_AND_BLANK);
				if(cell == null) continue;
				
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					cols = df.format(cell.getNumericCellValue());
				} else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
					cols = cell.getStringCellValue();
				} else {
					mResult.setResultCode(0);
					mResult.setResultMessage("单元格格式错误，行："+start);
					return mResult;
				}
				
				// 忽略空的行
				if(StringUtils.isBlank(cols)){
					continue;
				}
				
				cols = StringUtils.trimToEmpty(cols);
				dataList.add(cols);
			}
			
		} catch (IOException e) {
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件异常："+e);
			return mResult;
		}
		
		StringBuilder error = new StringBuilder();
		for(String code : dataList) {
			if(DbUp.upTable("pc_productinfo").count("product_code",code) == 0) {
				if(error.length() > 0) {
					error.append(",");
				}
				error.append(code);
			}
		}
		
		if(error.length() > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("商品编号错误："+error);
			return mResult;
		}
		
		mResult.getResultList().addAll(dataList);
		
		return mResult;
	}
	
}
