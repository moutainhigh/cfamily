package com.cmall.familyhas.api;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiCommoidtyBookImportDelete.ImportProductInput;
import com.cmall.familyhas.service.ImportService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiCommoidtyBookImportDelete extends RootApi<RootResult, ImportProductInput>{
	
	@Override
	public RootResult Process(ImportProductInput inputParam, MDataMap mRequestMap) {
		
		
		RootResult result = new RootResult();
		String templateCode = inputParam.getTemplateCode().trim();
		String fileRemoteUrl = inputParam.getUpload_show();
		if(!StringUtils.endsWith(fileRemoteUrl, ".xls")){
			result.setResultMessage("文件格式不对，请核实！");
			return result;
		}
		/**
		 * 存放专题模板编号
		 */
		Map<String, String > errorMap = new HashMap<String, String >();
		
		if(StringUtils.isBlank(templateCode)) {
			result.setResultMessage("模板编码值获取为空异常");
			return result;
		}
		try {
			List<Row> importExcel = new ImportService().importExcel(inputParam.getUpload_show(),0);
			List<ImportModel> importProduct = importProduct(importExcel,templateCode);
			StringBuffer sql = new StringBuffer();
			String execSql  = "delete  from fh_data_commodity where template_number = '"+templateCode+"' and good_number in ";
		    boolean excuteFlag = true;
		    if(importProduct.size()==0||importProduct.get(importProduct.size()-1).getError_message()!=null&&"导入数据为空".equals(importProduct.get(importProduct.size()-1).getError_message())) {
		    	result.setResultCode(0);
				result.setResultMessage("导入数据为空");
				return result;
		    }
		    int temNum = 1;
		    StringBuffer sb= new StringBuffer();
			for (ImportModel importModel : importProduct) {
				temNum++;
				if(importModel.isFlag()) {
					if(!"".equals(importModel.getGood_number())) 
					sql.append("'"+importModel.getGood_number()+"',");
				} else {
					excuteFlag = false;
					sb.append("第"+temNum+"行:"+importModel.getError_message()+"\r\n");
					break;
				/*	if(errorMap.containsKey(importModel.getError_message())) {
						String tempVal = errorMap.get(importModel.getError_message());
						errorMap.put(importModel.getError_message(), tempVal+","+importModel.getGood_number());
					} else {
						errorMap.put(importModel.getError_message(),importModel.getGood_number());
					}*/
					//result.setResultMessage("导入数据错误，请检查导入数据信息");
				}
			}
			if(sql.length() > 0&&excuteFlag) {
				String sql2 = "("+sql.toString().substring(0,sql.toString().length()-1)+")";
				execSql += sql2;
				DbUp.upTable("fh_data_commodity").dataExec(execSql, new MDataMap());
			}
			if(sb.toString().length()>0) {
				result.setResultMessage(sb.toString());
			}
			
		} catch (IOException e) {
			result.setResultCode(0);
			result.setResultMessage("导入数据错误，请检查导入数据信息");
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public static class ImportProductInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;
		
		private String templateCode="";

		public String getTemplateCode() {
			return templateCode;
		}

		public void setTemplateCode(String templateCode) {
			this.templateCode = templateCode;
		}

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

	

		
	}
	
	//获取商品数据
	private List<ImportModel> importProduct(List<Row> rowlist,String template_number) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		int num = 0;
		
		for (Row row : rowlist) {
		
			ImportModel model = new ImportModel();
			//商品编号
			String good_number = "";

			try {
				
				if( row!=null&&null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						good_number = row.getCell(0).getStringCellValue().trim();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(0).getNumericCellValue();
						good_number = new DecimalFormat("#").format(a).trim(); 
					}
				}  else {
					num++;
					if(num==rowlist.size())
					{
						model.setError_message("导入数据为空");
						model.setFlag(false);
					}
				}
						
				/**
				 * 只拼接成功导入,未找到商品,重复导入的商品编号
				 */
				if(!StringUtils.isEmpty(good_number)) {
					
					Map<String,Object> map = DbUp.upTable("fh_data_commodity").dataSqlOne("select good_number from "
							+ "fh_data_commodity where good_number='" + good_number + "' and template_number = '"+template_number+"'", null);
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher matcher = pattern.matcher(good_number);
					if(!matcher.matches()) {
						//查询不到对应的记录
						model.setGood_number(good_number);
						model.setError_message("商品编号异常");
						model.setFlag(false);
						resultModel.add(model);
						continue;
						//break;
						
					}
					if(null != map && map.size() > 0) {
						model.setGood_number(good_number);
					} else {
						//查询不到对应的记录
						model.setGood_number(good_number);
						model.setError_message("无对应商品信息");
						model.setFlag(false);
						resultModel.add(model);
						continue;
					}
					
				} 
				resultModel.add(model);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return resultModel;
	}
	
public static class ImportModel {
		
		/**
		 * 只为返回错误的商品编号编号
		 */
		private String good_number = "";
			
		public String getGood_number() {
			return good_number;
		}

		public void setGood_number(String good_number) {
			this.good_number = good_number;
		}

		private boolean flag = true;
		
		private String error_message = "";

	

		/**
		 * 默认为true
		 * @return
		 */
		public boolean isFlag() {
			return flag;
		}

		/**
		 * 默认为true
		 * @param flag
		 */
		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public String getError_message() {
			return error_message;
		}

		public void setError_message(String error_message) {
			this.error_message = error_message;
		}

		
	}

	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
	
}
