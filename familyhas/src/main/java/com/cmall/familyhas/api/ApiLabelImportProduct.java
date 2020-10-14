package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiLabelImportProduct.ImportProductInput;
import com.cmall.familyhas.api.ApiLabelImportProduct.ImportProductResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 标签 -批量导入商品管理
 * @author hw
 *
 */
public class ApiLabelImportProduct extends RootApi<ImportProductResult, ImportProductInput> {
	
	private static class JsonResult {
		private List<String> success = new LinkedList<String>();
		private String notFound = "";
		private String same = "";
		
		public List<String> getSuccess() {
			return success;
		}
		public void setSuccess(List<String> success) {
			this.success = success;
		}
		public String getNotFound() {
			return notFound;
		}
		public void setNotFound(String notFound) {
			this.notFound = notFound;
		}
		public String getSame() {
			return same;
		}
		public void setSame(String same) {
			this.same = same;
		}
	}
	
	public static class ImportProductInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}
	}
	
	public static class ImportProductResult extends RootResult {
//		private String msg;
//
//		public String getMsg() {
//			return msg;
//		}
//
//		public void setMsg(String msg) {
//			this.msg = msg;
//		}
	}

	@Override
	public ImportProductResult Process(ImportProductInput input, MDataMap mRequestMap) {
	
	    String fileRemoteUrl = input.getUpload_show();
	    ImportProductResult result = new ImportProductResult();
	    if(!StringUtils.isEmpty(fileRemoteUrl)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream)
					result.setResultMessage(readExcel(instream));
			} catch (Exception e) {
				result.setResultMessage("导入商品失败:" + e.getLocalizedMessage());
				e.printStackTrace();
			} finally {
				if(null != instream) try { instream.close(); } catch (IOException e) {}
			}
	    }
		return result;
	}
	
	/**
	 * 读取Excel商品数据
	 * 
	 * @param file
	 */
	public String readExcel(InputStream input) {
		String result = "";
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			result = importProduct(sheet);
		} catch (FileNotFoundException e) {
			result = "导入商品失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入商品失败！" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}
	
	private String importProduct(Sheet sheet) {
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		//过滤重复导入的商品编号
		List<String> productList = new LinkedList<String>();
//		StringBuilder sbSuccess = new StringBuilder();
		StringBuilder sbSame = new StringBuilder();
		StringBuilder sbNotFound = new StringBuilder();
		JsonResult jr = new JsonResult();
		
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			//商品编号
			String productCode = "";
			try {
				Row row = sheet.getRow(rIndex);
				if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
					productCode = row.getCell(0).getStringCellValue();
				} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Double d = row.getCell(0).getNumericCellValue();
					productCode = new DecimalFormat("#").format(d); 
				}
				//只拼接成功导入,未找到商品,重复导入的商品编号
				//cfamily/js/couponTypeLimit_product_select.js的result
				//方法用传递的商品编号(逗号分隔)反查商品编号和商品名称,回填界面上的productTable表
				if(!StringUtils.isEmpty(productCode)) {
					if(!productList.contains(productCode)) {
						Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select product_code, product_name from "
								+ "pc_productinfo where product_code='" + productCode + "'", null);
						if(null != map && map.size() > 0) {
							String product_code = isEmpty(map.get("product_code")) ? "" : map.get("product_code").toString();
							String product_name = isEmpty(map.get("product_name")) ? "" : map.get("product_name").toString();
							jr.getSuccess().add(product_code + "," + product_name);
//							sbSuccess.append(productCode).append(",");
						} else {
							//查询不到对应的记录
							sbNotFound.append(productCode).append(",");
						}
					} else {
						//重复导入
						sbSame.append(productCode).append(",");
					}					
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		

//		String tmp = sbSuccess.toString();
//		if(tmp.length() > 0 && tmp.endsWith(",")) {
//			jr.setSuccess(tmp.substring(0, tmp.length() -1));
//		}
		String tmp = sbNotFound.toString();
		if(tmp.length() > 0 && tmp.endsWith(",")) {
			jr.setNotFound(tmp.substring(0, tmp.length() -1));
		}
		tmp = sbSame.toString();
		if(tmp.length() > 0 && tmp.endsWith(",")) {
			jr.setSame(tmp.substring(0, tmp.length() -1));
		}
		return JSON.toJSONString(jr);
	}
	
	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
}
