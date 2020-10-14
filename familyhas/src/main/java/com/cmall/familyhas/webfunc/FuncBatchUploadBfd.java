package com.cmall.familyhas.webfunc;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 百分点屏蔽商品导入
 * @author zhaojunling
 */
public class FuncBatchUploadBfd extends RootFunc {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String day = mDataMap.get("zw_f_day");
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		HttpURLConnection conn = null;
		InputStream in = null;
		Set<String> dataList = new HashSet<String>(); 
		try {
			conn = (HttpURLConnection)new URL(uploadFileUrl).openConnection();
			if(conn.getResponseCode() != 200){
				throw new RuntimeException("上传文件未找到");
			}
			
			in = conn.getInputStream();
			
			Workbook wb = new HSSFWorkbook(in);
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum();
			int end = sheet.getLastRowNum();
			dataList = new HashSet<String>((int)(end/0.75+1));
			
			Row row;
			String productCode = null;
			while((start++) <= end){
				row = sheet.getRow(start);
				if(row == null || row.getCell(0) == null) continue;
				
				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				productCode = row.getCell(0,Row.RETURN_NULL_AND_BLANK) == null ? "" : row.getCell(0,Row.RETURN_NULL_AND_BLANK).getStringCellValue();
				
				// 忽略退款单号为空的行
				if(StringUtils.isBlank(productCode)){
					continue;
				}
				
				dataList.add(productCode);
			}
		} catch (Exception e) {
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件异常："+e);
			return mResult;
		} finally {
			if(in != null) IOUtils.closeQuietly(in);
			if(conn != null) conn.disconnect();
		}
		
		if(StringUtils.isBlank(day)){
			mResult.setResultCode(0);
			mResult.setResultMessage("请选择日期");
			return mResult;
		}
		
		// 每次导入先删除旧数据
		DbUp.upTable("pc_product_bfd").delete("day", day);
		
		int size = 0;
		String productCode;
		MDataMap productMap;
		MDataMap insertMap;
		for(String val : dataList){
			if(StringUtils.isBlank(val)) continue;
			productCode = StringUtils.trimToEmpty(val);

			productMap = DbUp.upTable("pc_productinfo").oneWhere("product_name", "", "", "product_code",productCode);
			insertMap = new MDataMap();
			insertMap.put("day", day);
			insertMap.put("product_code", productCode);
			insertMap.put("product_name", productMap == null ? "" : StringUtils.left(productMap.get("product_name"), 255));
			insertMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("pc_product_bfd").dataInsert(insertMap);
			
			size++;
		}
		
		mResult.setResultMessage("成功导入数："+size);
		return mResult;
	}
	
}
