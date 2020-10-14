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

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiBatchChangeSellPriceImport.ImportProductInput;
import com.cmall.familyhas.api.ApiBatchChangeSellPriceImport.ImportProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.webfunc.FuncChangeSkuPriceService;
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
public class ApiBatchChangeSellPriceImport extends RootApi<ImportProductResult, ImportProductInput> {
	
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

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

		
		
	}
	
	public static class ImportModel {
		private String productCode = "";

		private String skuCode = "";
	    
	    private String start_date="";
	    
	    private String end_date="";
	    
	    private String sell_price = "";
		
		private boolean flag = true;
		
		private String error_message = "";
		
		public String getProductCode() {
			return productCode;
		}

		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getSell_price() {
			return sell_price;
		}

		public void setSell_price(String sell_price) {
			this.sell_price = sell_price;
		}


	    public String getSkuCode() {
			return skuCode;
		}

		public void setSkuCode(String skuCode) {
			this.skuCode = skuCode;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
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
	    ImportProductResult result = new ImportProductResult();
	    
	    Map<String, String > errorMap = new HashMap<String, String >();
	    
	    if(!StringUtils.isEmpty(fileRemoteUrl)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream){
					List<ImportModel> rtnList = readExcel(instream);
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的产品信息!!");
						return result;
					}
					//参数集合
					List<Map<String,Object>> listMap = new ArrayList<>();
					boolean isExcute = true;
					for (ImportModel importModel : rtnList) {
						if(importModel.isFlag()) {
							Map<String,Object> map =new HashMap<>();
							map.put("start_date",importModel.getStart_date());
							map.put("end_date", importModel.getEnd_date());
							map.put("sell_price", importModel.getSell_price());
							map.put("sku_code", importModel.getSkuCode().trim());
							map.put("product_code", importModel.getProductCode());
							listMap.add(map);
						} else {
							isExcute = false;
							if(errorMap.containsKey(importModel.getError_message())) {
								String tempVal = errorMap.get(importModel.getError_message());
								errorMap.put(importModel.getError_message(), tempVal+","+importModel.getSkuCode());
							} else {
								errorMap.put(importModel.getError_message(),importModel.getSkuCode());
							}
							
						}
						
					}
					if(isExcute) {
						FuncChangeSkuPriceService service = new FuncChangeSkuPriceService();
						service.funcDo(listMap);
					}
					if(!isExcute) {
						result.setResultMessage(JSON.toJSONString(errorMap));
					}
					
				}
				
			} catch (Exception e) {
				result.setResultMessage("导入失败:sku编号不存在" /*+ e.getLocalizedMessage()*/);
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
		
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			
			String productCode ="";
			String skuCode = "";
			String start_date = "";
			String end_date = "";
			String sell_price = "";
		    
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取产品（sku）编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						skuCode = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						skuCode = new DecimalFormat("#").format(d); 
					}
				}
				if( null != row.getCell(1) && row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK){
					
					//获取修改后的价格
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						sell_price = row.getCell(1).getStringCellValue();
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(1).getNumericCellValue();
						sell_price = new DecimalFormat("#").format(a); 
					}
				} 
				//开始时间
				if(null != row.getCell(2) &&row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
					start_date = row.getCell(2).getStringCellValue();
				} else if(null != row.getCell(2) &&row.getCell(2).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(2).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					start_date = format.format(dateCellValue);
				}
				//结束时间
				if(null != row.getCell(3) &&row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
					end_date = row.getCell(3).getStringCellValue();
				} else if(null != row.getCell(3) &&row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(3).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					end_date = format.format(dateCellValue);
				}
			
				if(!StringUtils.isEmpty(skuCode)){
					
					//判断是否是ld商品
					String querSql = "select p.product_code product_code from pc_productinfo p,pc_skuinfo s where p.product_code = s.product_code and p.seller_code='SI2003' and p.small_seller_code='SI2003' and s.sku_code=:sku_code";
					Map<String,Object> resultMap = DbUp.upTable("pc_productinfo").dataSqlOne(querSql, new MDataMap("sku_code",skuCode));
					if(null != resultMap && resultMap.size() > 0) {
						//时间赋值校验
				       if (start_date == null || "".equals(start_date)) {
							 start_date = DateUtil.getSysDateString();
						 } 
				       if (end_date == null || "".equals(end_date)) {
							 end_date = "2099-12-31";
						 }
					String vertiryResult = this.verifyFormData(start_date,end_date,sell_price);
					if(StringUtils.isBlank(vertiryResult))
						{
							model.setSkuCode(skuCode);
							model.setProductCode(resultMap.get("product_code").toString());
							model.setSell_price(sell_price);
							model.setStart_date(start_date);
							model.setEnd_date(end_date);
						}
						else {
							//导入数据异常
							model.setSkuCode(skuCode);
							model.setError_message(vertiryResult);
							model.setFlag(false);
						}
						
						
					} else {
						//查询不到对应的记录
						model.setSkuCode(skuCode);
						model.setError_message("该skuCode不属于ld商品");
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
	
	
	/**
	 * 
	 * 方法: verifyFormData <br>
	 * 描述: 验证验证表单数据是否正确 <br>
	 * 
	 * @param map
	 * @return
	 */
	public static String verifyFormData(String start_date,String end_date,String sell_price ) {
		String error = "";
		String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
		//String dateRegex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|\n" + 
		//		           "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|\n" + 
		//		           "((0[48]|[2468][048]|[3579][26])00))-02-29)$";
		String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
		if (sell_price == null || "".equals(sell_price)) {
			error = "销售价格不能为空";
		} else if (!sell_price.matches(regex)) {
			error = "销售价格只能是数字";
		}

		else {
            
			if(start_date.matches(dateRegex)&&end_date.matches(dateRegex)) {
				Date startDate = DateUtil.toDate(start_date);
				Date endDate = DateUtil.toDate(end_date);
				Date nowTime = DateUtil.toDate(DateUtil.getSysDateString());
				if (startDate.compareTo(nowTime) < 0) {
					error = "开始日期必须大于等于当前日期";
				} else if (endDate.compareTo(nowTime) < 0) {
					error = "结束日期必须大于等于当前日期";
				} else if (endDate.compareTo(startDate) < 0) {
					error = "开始日期必须小于或等于结束日期";
				} 
			}
			else if(!start_date.matches(dateRegex)) {
				error = "开始日期格式不正确";
			}
			else if(!end_date.matches(dateRegex)) {
				error = "结束日期格式不正确";
			}
		
		}
		return error;
	}
}
