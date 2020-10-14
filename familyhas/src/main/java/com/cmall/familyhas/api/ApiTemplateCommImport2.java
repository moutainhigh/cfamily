package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiTemplateCommImport2.ImportProductInput;
import com.cmall.familyhas.api.ApiTemplateCommImport2.ImportProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * @author Administrator
 *
 */
public class ApiTemplateCommImport2 extends RootApi<ImportProductResult, ImportProductInput> {
	
	@SuppressWarnings("unused")
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
		
		private String templete_number="";

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

		public String getTemplete_number() {
			return templete_number;
		}

		public void setTemplete_number(String templete_number) {
			this.templete_number = templete_number;
		}
		
		
	}
	
	public static class ImportModel {
		
		private String uid = "";
		
		private String templete_number ="";
		
		/**
		 * 只为返回错误的商品编号或者sku编号
		 */
		private String prodcut_code = "";
		
		private String sku_code = "";
		
		private String start_time = "";
		
		private String end_time = "";
		
		private String location = "";
		
		private String product_desc = "";
		
		private String product_name = "";
		
		private boolean flag = true;
		
		private String error_message = "";
		
		public String getProdcut_code() {
			return prodcut_code;
		}

		public void setProdcut_code(String prodcut_code) {
			this.prodcut_code = prodcut_code;
		}

		public String getSku_code() {
			return sku_code;
		}

		public void setSku_code(String sku_code) {
			this.sku_code = sku_code;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getProduct_desc() {
			return product_desc;
		}

		public void setProduct_desc(String product_desc) {
			this.product_desc = product_desc;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getTemplete_number() {
			return templete_number;
		}

		public void setTemplete_number(String templete_number) {
			this.templete_number = templete_number;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public boolean isFlag() {
			return flag;
		}

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
	
	public static class ImportProductResult extends RootResult {

	}

	@Override
	public ImportProductResult Process(ImportProductInput input, MDataMap mRequestMap) {
	    String fileRemoteUrl = input.getUpload_show();
	    String templete_number = input.getTemplete_number();
	    ImportProductResult result = new ImportProductResult();
	    
	    Map<String, String > errorMap = new HashMap<String, String >();
	    
	    if(!StringUtils.isEmpty(fileRemoteUrl) && !StringUtils.isEmpty(templete_number)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream){
					List<ImportModel> rtnList = readExcel(instream);
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的商品信息!!");
						return result;
					}
					StringBuffer sql = new StringBuffer();
					String execSql = " INSERT INTO familyhas.fh_data_commodity(uid,template_number,commodity_name,commodity_describe,start_time,end_time,commodity_location,commodity_number,create_time,is_dis,good_number)  VALUES ";
					for (ImportModel importModel : rtnList) {
						
						if(importModel.isFlag()) {
							
							String sUid = UUID.randomUUID().toString().replace("-", "");
							sql.append("('"+sUid+"',");
							sql.append("'"+templete_number+"',");
							sql.append("'"+importModel.getProduct_name().replace("'", "\\'")+"',");
							sql.append("'"+importModel.getProduct_desc().replace("'", "\\'")+"',");
							sql.append("'"+importModel.getStart_time()+"',");
							sql.append("'"+importModel.getEnd_time()+"',");
							sql.append("'"+importModel.getLocation()+"',");
							sql.append("'"+importModel.getSku_code()+"',");
							sql.append("'"+DateUtil.getSysDateTimeString()+"',");
							sql.append("'449746250001',");
							sql.append("'"+importModel.getProdcut_code()+"'),");
						} else {
							
							if(errorMap.containsKey(importModel.getError_message())) {
								String tempVal = errorMap.get(importModel.getError_message());
								errorMap.put(importModel.getError_message(), tempVal+","+importModel.getProdcut_code());
							} else {
								errorMap.put(importModel.getError_message(),importModel.getProdcut_code());
							}
							
						}
						
					}
					if(sql.length() > 0) {
						execSql += sql.substring(0, sql.length()-1);
						DbUp.upTable("fh_data_commodity").dataExec(execSql, new MDataMap());
					}
					if(errorMap.size() > 0) {
						result.setResultMessage(JSON.toJSONString(errorMap));
					}
					
				}
				
			} catch (Exception e) {
				result.setResultMessage("导入商品失败:sku编号不存在" /*+ e.getLocalizedMessage()*/);
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
	public List<ImportModel> readExcel(InputStream input) {
		
		@SuppressWarnings("unused")
		String result = "";
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			resultModel = importProduct(sheet);
		} catch (FileNotFoundException e) {
			result = "导入商品失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入商品失败！" /*+ e.getLocalizedMessage()*/;
			e.printStackTrace();
		}
		return resultModel;
	}
	
	private List<ImportModel> importProduct(Sheet sheet) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			//商品编号
			String productCode = "";
			String start_time = "";
			String end_time = "";
			String commodity_location = "";
			String sku = "";
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						sku = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						sku = new DecimalFormat("#").format(d); 
					}
				} else {
					//没有商品编号或者sku编号，则终止循环
					break;
				}
				if( null != row.getCell(4) && row.getCell(4).getCellType() != Cell.CELL_TYPE_BLANK){
					
					//取SKU编号
					if(row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
						productCode = row.getCell(4).getStringCellValue();
					} else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(4).getNumericCellValue();
						productCode = new DecimalFormat("#").format(a); 
					}
				} else {
					//没有商品编号或者sku编号，则终止循环
					break;
				}
				//开始时间
				if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
					start_time = row.getCell(1).getStringCellValue();
				} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(1).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					start_time = format.format(dateCellValue);
				}
				//结束时间
				if(row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
					end_time = row.getCell(2).getStringCellValue();
				} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(2).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					end_time = format.format(dateCellValue);
				}
				//位置
				if(null != row.getCell(3) && StringUtils.isNotBlank(row.getCell(3).toString())){
					if(row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
						commodity_location = null != row.getCell(3)?row.getCell(3).getStringCellValue():"";
					} else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(3).getNumericCellValue();
						commodity_location = new DecimalFormat("#").format(b); 
					}else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(3).getErrorCellValue();
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
					if(null != map && map.size() > 0) {
						String product_name = isEmpty(map.get("product_name")) ? "" : map.get("product_name").toString();
						
						model.setEnd_time(StringUtils.trimToEmpty(end_time));
						model.setLocation(commodity_location);
						model.setStart_time(StringUtils.trimToEmpty(start_time));
						model.setProduct_desc(product_name);
						model.setProdcut_code(productCode);
						PlusModelProductQuery plus = new PlusModelProductQuery(productCode);
//						PlusSupportProduct psp = new PlusSupportProduct();
						PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
						List<PlusModelProductSkuInfo> skuList = upInfoByCode.getSkuList();
						model.setSku_code(skuList.get(0).getSkuCode());
						model.setProduct_name(product_name);
						
					} else {
						//查询不到对应的记录
						model.setProdcut_code(productCode);//只为返回错误的商品编号或者sku编号
						model.setError_message("无对应商品信息");
						model.setFlag(false);
					}
						
				}
				if(!StringUtils.isEmpty(sku)){
					
					Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("SELECT pc.product_code, pc.product_name,sku.sku_name FROM pc_productinfo pc JOIN pc_skuinfo sku " +
							"ON pc.product_code = sku.product_code AND sku.sku_code = '"+sku+"'", null);
					if(null != map && map.size() > 0) {
						String product_name = isEmpty(map.get("product_name")) ? "" : map.get("product_name").toString();
						
						model.setEnd_time(StringUtils.trimToEmpty(end_time));
						model.setLocation(commodity_location);
						model.setProdcut_code(map.get("product_code").toString());
						model.setStart_time(StringUtils.trimToEmpty(start_time));
						model.setProduct_desc(product_name);
						model.setProduct_name(product_name);
						model.setSku_code(sku);
					} else {
						//查询不到对应的记录
						model.setProdcut_code(sku);//只为返回错误的商品编号或者sku编号
						model.setError_message("无对应sku信息");
						model.setFlag(false);
					}
				}
				
				if(model.flag) {
					// 检查日期格式
					if(!datePattern.matcher(model.getStart_time()).matches() || !datePattern.matcher(model.getEnd_time()).matches()) {
						model.setError_message("日期格式错误");
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
	
	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
}
