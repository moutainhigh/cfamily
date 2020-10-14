package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import com.cmall.familyhas.api.ApiTemplateJifenImport.ImportProductInput;
import com.cmall.familyhas.api.ApiTemplateJifenImport.ImportProductResult;
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
public class ApiTemplateJifenImport extends RootApi<ImportProductResult, ImportProductInput> {
	
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
		
		private String channel_uid="";
		
		

		public String getChannel_uid() {
			return channel_uid;
		}

		public void setChannel_uid(String channel_uid) {
			this.channel_uid = channel_uid;
		}

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
		
		private String jf_cost ="";
		
		
		private String addprice = "";
		
		private String stocknum = "";
		
		private String limitnum = "";
		
		/**
		 * 只为返回错误的商品编号或者sku编号
		 */
		private String product_code = "";
		
		private String start_time = "";
		
		private String end_time = "";
		
		private String title = "";
		
		private boolean flag = true;
		
		private String error_message = "";

		

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getJf_cost() {
			return jf_cost;
		}

		public void setJf_cost(String jf_cost) {
			this.jf_cost = jf_cost;
		}

		public String getAddprice() {
			return addprice;
		}

		public void setAddprice(String addprice) {
			this.addprice = addprice;
		}

		public String getStocknum() {
			return stocknum;
		}

		public void setStocknum(String stocknum) {
			this.stocknum = stocknum;
		}

		public String getLimitnum() {
			return limitnum;
		}

		public void setLimitnum(String limitnum) {
			this.limitnum = limitnum;
		}

		

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
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
	    String channel_uid = input.getChannel_uid();
	    MDataMap mWhereMap = new MDataMap();
	    mWhereMap.put("uid", channel_uid);
	    Map<String, Object> dataSqlOne = DbUp.upTable("fh_apphome_channel").dataSqlOne("select * from fh_apphome_channel where uid =:uid", mWhereMap);
	    String channel_type = dataSqlOne.get("channel_type").toString();
	    ImportProductResult result = new ImportProductResult();
	    
	    Map<String, String > errorMap = new HashMap<String, String >();
	    
	    if(!StringUtils.isEmpty(fileRemoteUrl) && !StringUtils.isEmpty(templete_number)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream){
					List<ImportModel> rtnList = readExcel(instream,channel_type,channel_uid);
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的商品信息!!");
						return result;
					}
					StringBuffer sql = new StringBuffer();
					String execSql = "";
					for (ImportModel importModel : rtnList) {
						
						if(importModel.isFlag()) {
							String addprice = importModel.getAddprice();
							
							String sUid = UUID.randomUUID().toString().replace("-", "");
							sql.append("('"+sUid+"',");
							sql.append("'"+importModel.getProduct_code().replace("'", "\\'")+"',");
							sql.append("'"+importModel.getTitle().replace("'", "\\'")+"',");
							sql.append("'"+importModel.getStart_time()+"',");
							sql.append("'"+importModel.getEnd_time()+"',");
							sql.append("'"+importModel.getJf_cost()+"',");
							if(StringUtils.isNotBlank(addprice)) {
								execSql = "INSERT INTO familyhas.fh_apphome_channel_details(uid,product_code,title,start_time,end_time,jf_cost,extra_charges,allow_count,channel_uid,one_allowed,product_info)  VALUES";
								sql.append("'"+addprice+"',");
							}else {
								execSql = "INSERT INTO familyhas.fh_apphome_channel_details(uid,product_code,title,start_time,end_time,jf_cost,allow_count,channel_uid,one_allowed,product_info)  VALUES";
							}	
							sql.append("'"+importModel.getStocknum()+"',");
							sql.append("'"+channel_uid+"',");
							sql.append("'"+importModel.getLimitnum()+"',");
							sql.append("'"+1+"'),");
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
						DbUp.upTable("fh_apphome_channel_details").dataExec(execSql, new MDataMap());
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
	public List<ImportModel> readExcel(InputStream input,String channel_type,String channel_uid) {
		
		@SuppressWarnings("unused")
		String result = "";
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			resultModel = importProduct(sheet,channel_type,channel_uid);
		} catch (FileNotFoundException e) {
			result = "导入商品失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入商品失败！" /*+ e.getLocalizedMessage()*/;
			e.printStackTrace();
		}
		return resultModel;
	}
	
	private List<ImportModel> importProduct(Sheet sheet,String channel_type,String channel_uid) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
		List<String> product_code_list = new ArrayList<String>();
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			int tishiindex = rIndex+1;
			ImportModel model = new ImportModel();
			//商品编号
			String product_code = "";
			String product_name = "";
			String start_time = "";
			String end_time = "";
			String jf_cost = "";
			String addprice = "";
			String stocknum = "";
			String limitnum = "";
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						product_code = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(0).getNumericCellValue();
						product_code = new DecimalFormat("#").format(a);
					}
					if(product_code_list.contains(product_code)) {
						model.setError_message("商品编号为"+product_code+"的商品数据重复!");
						model.setFlag(false);
					}else {
						product_code_list.add(product_code);
					}
				} else {
					//没有商品编号或者sku编号，则终止循环
					break;
				}
				if( null != row.getCell(1) && row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						product_name = row.getCell(1).getStringCellValue();
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(1).getNumericCellValue();
						product_name = new DecimalFormat("#").format(a); 
					}
				}
				//开始时间
				if(row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
					start_time = row.getCell(2).getStringCellValue();
				} else if(row.getCell(2).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(2).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					start_time = format.format(dateCellValue);
				}
				//结束时间
				if(row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
					end_time = row.getCell(3).getStringCellValue();
				} else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
					Date dateCellValue = row.getCell(3).getDateCellValue();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					end_time = format.format(dateCellValue);
				}
				Date currentDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long l = 0l;
				long l2 = 0l;
				try {
					Date startDate = formatter.parse(start_time);
					Date endDate = formatter.parse(end_time);
					l2 = endDate.getTime() - currentDate.getTime();
					l = endDate.getTime() - startDate.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l2<0){
					model.setError_message("第"+tishiindex+"行,结束时间小于当前时间，不合法，请重新填写");
					model.setFlag(false);
				}
				if(l<0){
					model.setError_message("第"+tishiindex+"行,开始时间大于结束时间，不合法，请重新填写");
					model.setFlag(false);
				}
				//兑换积分
				if(null != row.getCell(4) && StringUtils.isNotBlank(row.getCell(4).toString())){
					if(row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
						jf_cost = null != row.getCell(4)?row.getCell(4).getStringCellValue():"";
					} else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(4).getNumericCellValue();
						jf_cost = new DecimalFormat("#").format(b); 
					}else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(4).getErrorCellValue();
						jf_cost = new DecimalFormat("#").format(c);
					}
				}
				//加价价格
				if(null != row.getCell(5) && StringUtils.isNotBlank(row.getCell(5).toString())){
					if(row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING) {
						addprice = null != row.getCell(5)?row.getCell(5).getStringCellValue():"";
					} else if(row.getCell(5).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(5).getNumericCellValue();
						addprice = new DecimalFormat("##.##").format(b); 
					}else if(row.getCell(5).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(5).getErrorCellValue();
						addprice = new DecimalFormat("##.##").format(c);
					}
				}
				
				
				if("449748130001".equals(channel_type)){//类型为加价的时候校验加价价格不能为空和0
					double extraC = Double.parseDouble(addprice);
					if(0 == extraC){
						model.setError_message("第"+tishiindex+"行,加价金额不能为0");
						model.setFlag(false);
					}
				}
				//最大库存数
				if(null != row.getCell(6) && StringUtils.isNotBlank(row.getCell(6).toString())){
					if(row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING) {
						stocknum = null != row.getCell(6)?row.getCell(6).getStringCellValue():"";
					} else if(row.getCell(6).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(6).getNumericCellValue();
						stocknum = new DecimalFormat("#").format(b); 
					}else if(row.getCell(6).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(6).getErrorCellValue();
						stocknum = new DecimalFormat("#").format(c);
					}
				}
				if(Integer.parseInt(stocknum) == 0){
					model.setError_message("第"+tishiindex+"行,活动允许最大库存不能为0");
					model.setFlag(false);
				}
				//限制兑换数量
				if(null != row.getCell(7) && StringUtils.isNotBlank(row.getCell(7).toString())){
					if(row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING) {
						limitnum = null != row.getCell(7)?row.getCell(7).getStringCellValue():"";
					} else if(row.getCell(7).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double b = row.getCell(7).getNumericCellValue();
						limitnum = new DecimalFormat("#").format(b); 
					}else if(row.getCell(7).getCellType()==Cell.CELL_TYPE_ERROR) {
						byte c = row.getCell(7).getErrorCellValue();
						limitnum = new DecimalFormat("#").format(c);
					}
				}
				
				if(!StringUtils.isEmpty(product_code)){//积分兑换或是加价购
					String sql = "select * from fh_apphome_channel_details where product_code = "+"\'"+product_code+"\'"+" and channel_uid = "+"\'"+channel_uid+"\' and"
							+ " end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\'";
					List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
					if(list.size()>0){
						model.setError_message("第"+tishiindex+"行,该栏目相同时间内已存在商品编号为："+product_code+",商品名称为："+product_name+"的商品！！！");
						model.setFlag(false);
					}
				}
					
				/**
				 * 只拼接成功导入,未找到商品,重复导入的商品编号
				 */
				if(!StringUtils.isEmpty(product_code)) {
					
					Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select product_code, product_name from "
							+ "pc_productinfo where product_code='" + product_code + "'", null);
					if(null != map && map.size() > 0) {
						model.setAddprice(addprice);
						model.setJf_cost(jf_cost);
						model.setLimitnum(limitnum);
						model.setStocknum(stocknum);
						model.setEnd_time(StringUtils.trimToEmpty(end_time));
						model.setStart_time(StringUtils.trimToEmpty(start_time));
						model.setProduct_code(product_code);
						model.setTitle(product_name);
					} else {
						//查询不到对应的记录
						model.setProduct_code(product_code);//只为返回错误的商品编号或者sku编号
						model.setError_message("第"+tishiindex+"行,无对应商品信息");
						model.setFlag(false);
					}
						
				}
				if(!StringUtils.isEmpty(product_code)){//积分兑换或是加价购
					String sql = "select * from fh_apphome_channel_details where product_code = "+"\'"+product_code+"\'"+" and channel_uid = "+"\'"+channel_uid+"\' and"
							+ " end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\'";
					List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
					if(list.size()>0){
						model.setProduct_code(product_code);//只为返回错误的商品编号或者sku编号
						model.setError_message("第"+tishiindex+"行,该栏目相同时间内已存在商品编号为："+product_code+",商品名称为："+product_name+"的商品！！！");
						model.setFlag(false);
					}
				}
				
				if(model.flag) {
					// 检查日期格式
					if(!datePattern.matcher(model.getStart_time()).matches() || !datePattern.matcher(model.getStart_time()).matches()) {
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
