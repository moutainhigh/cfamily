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
import com.cmall.familyhas.api.ApiPPTHProductImport.ImportProductInput;
import com.cmall.familyhas.service.ImportService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiPPTHProductImport extends RootApi<RootResult, ImportProductInput>{
	
	@Override
	public RootResult Process(ImportProductInput inputParam, MDataMap mRequestMap) {
		
		
		RootResult result = new RootResult();
		String infoCode = inputParam.getInfoCode();
		
		/**
		 * 存放导入有问题的商品信息
		 */
		Map<String, String > errorMap = new HashMap<String, String >();
		
	
		try {
			List<Row> importExcel = new ImportService().importExcel(inputParam.getUpload_show(),0);
			List<ImportModel> importProduct = importProduct(importExcel);
			if(importProduct.size() <= 0) {
				result.setResultCode(0);
				result.setResultMessage("没有需要导入的商品信息!!");
				return result;
			}
			StringBuffer sql = new StringBuffer();
			String execSql = " INSERT INTO productcenter.pc_brand_rel_product(uid,info_code,product_code,product_status,product_sort)  VALUES ";
			for (ImportModel importModel : importProduct) {
				if(importModel.isFlag()) {
					
					String sUid = UUID.randomUUID().toString().replace("-", "");
					sql.append("('"+sUid+"',");
					sql.append("'"+infoCode+"',");
					sql.append("'"+importModel.getProduct_code()+"',");
					sql.append("'"+importModel.getProduct_status()+"',");
					sql.append("'"+importModel.getLocation()+"'),");
				} else {
					
					if(errorMap.containsKey(importModel.getError_message())) {
						String tempVal = errorMap.get(importModel.getError_message());
						errorMap.put(importModel.getError_message(), tempVal+","+importModel.getProduct_code());
					} else {
						errorMap.put(importModel.getError_message(),importModel.getProduct_code());
					}
					
				}
			}
			if(sql.length() > 0) {
				execSql += sql.substring(0, sql.length()-1);
				DbUp.upTable("pc_brand_rel_product").dataExec(execSql, new MDataMap());
			}
			if(errorMap.size() > 0) {
				result.setResultMessage(JSON.toJSONString(errorMap));
			}
			
		} catch (IOException e) {
			result.setResultCode(0);
			result.setResultMessage("导入商品失败");
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public static class ImportProductInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;
		
		private String infoCode="";

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

		public String getInfoCode() {
			return infoCode;
		}

		public void setInfoCode(String infoCode) {
			this.infoCode = infoCode;
		}

		
	}
	
	//获取商品数据
	private List<ImportModel> importProduct(List<Row> rowlist) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		
		for (Row row : rowlist) {
		
			ImportModel model = new ImportModel();
			//商品编号
			String productCode = "";
			String commodity_location = "";
			try {
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						productCode = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(0).getNumericCellValue();
						productCode = new DecimalFormat("#").format(a); 
					}
				}  else {
					//没有商品编号，则终止循环
					break;
				}
				//位置 默认为99
				if(null != row.getCell(1) && StringUtils.isNotBlank(row.getCell(1).toString())){
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						commodity_location = null != row.getCell(1)?row.getCell(1).getStringCellValue():"";
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(1).getNumericCellValue();
						commodity_location = new DecimalFormat("#").format(b); 
					}else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(1).getErrorCellValue();
						commodity_location = new DecimalFormat("#").format(c);
					}
				}else {
					commodity_location = "99";
				}
				
					
				/**
				 * 只拼接成功导入,未找到商品,重复导入的商品编号
				 */
				if(!StringUtils.isEmpty(productCode)) {
					
					Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select product_code, product_name from "
							+ "pc_productinfo where product_code='" + productCode + "'", null);
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher matcher = pattern.matcher(productCode);
					if(!matcher.find()) {
						//查询不到对应的记录
						model.setProduct_code(productCode);//只为返回错误的商品编号或者sku编号
						model.setError_message("商品编号异常");
						model.setFlag(false);
						continue;
					}
					
					if(null != map && map.size() > 0) {
						model.setLocation(commodity_location);
						model.setProduct_code(productCode);
						model.setProduct_status("2");//初始导入为不可用
					} else {
						//查询不到对应的记录
						model.setProduct_code(productCode);//只为返回错误的商品编号或者sku编号
						model.setError_message("无对应商品信息");
						model.setFlag(false);
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
		private String product_code = "";
		
		private String product_status = "";
		
		private String location = "";
		
		private boolean flag = true;
		
		private String error_message = "";

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

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

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public String getProduct_status() {
			return product_status;
		}

		public void setProduct_status(String product_status) {
			this.product_status = product_status;
		}
		
	}

	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
	
}
