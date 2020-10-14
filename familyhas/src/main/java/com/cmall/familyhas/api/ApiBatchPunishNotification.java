package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.familyhas.api.ApiBatchPunishNotification.UnshelveProductInput;
import com.cmall.familyhas.api.ApiBatchPunishNotification.UnshelveProductResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

public class ApiBatchPunishNotification extends RootApi<UnshelveProductResult, UnshelveProductInput> {

	private boolean orderAndSkuError = false;
	private String orderAndSkuErrorCode = "";
	private boolean repeatError = false;
	private String repeatErrorCode = "";
	private boolean punishedError = false;
	private String  punishedErrorCode  = "";
	private boolean emptyError = false;
	private String emptyErrorInfo = "";
	
	@Override
	public UnshelveProductResult Process(UnshelveProductInput input, MDataMap mRequestMap) {
		UnshelveProductResult result = new UnshelveProductResult();
		String fileRemoteUrl = input.getUpload_show();
		//判断 文件格式
		if(!StringUtils.endsWith(fileRemoteUrl, ".xls")){
			result.setResultMessage("文件格式不对，请核实！");
			return result;
		}
	    if(!StringUtils.isEmpty(fileRemoteUrl)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream){
					List<ImportModel> rtnList = readExcel(instream);
					//判断单元格内是否有空数据，不允许为空
					if(emptyError) {
						result.setResultCode(0);
						result.setResultMessage("导入表格有字段为空,明细为:"+emptyErrorInfo.substring(0, emptyErrorInfo.length()-1));
						return result;
					}

					//判断是否有错误码或重复码或已经处罚过
					if(orderAndSkuError) {
						result.setResultCode(0);
						result.setResultMessage("导入表格有错误订单号商品号,明细为:"+orderAndSkuErrorCode.substring(0, orderAndSkuErrorCode.length()-1));
						return result;
					}
					if(repeatError) {
						result.setResultCode(0);
						result.setResultMessage("导入表格有重复订单号商品号,明细为:"+repeatErrorCode.substring(0, repeatErrorCode.length()-1));
						return result;
					}
					if(punishedError) {
						result.setResultCode(0);
						result.setResultMessage("导入表格有已处罚过的订单号商品号,明细为:"+punishedErrorCode.substring(0, punishedErrorCode.length()-1));
						return result;
					}
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的商品信息!!");
						return result;
					}
					
					//将插入数据库数据重新按商户编号分类，同一商户处罚归为一条处罚消息
					Map<String,List<ImportModel>> maps = new HashMap<>();
					for (ImportModel importModel : rtnList) {
						String company_code = importModel.getCompany_code();
						if(maps.containsKey(company_code)) {
							maps.get(company_code).add(importModel);
						}else {
							List<ImportModel> list = new ArrayList<ImportModel>();
							list.add(importModel);
							maps.put(company_code, list);
						}
					}
					
					//插入消息通知表
					String execSql = " INSERT INTO familyhas.fh_news_notification(uid,news_code,company_code,company_name,notice_topic,notice_type)  VALUES(?,?,?,?,?,?) ";
					//插入处罚明细表 
					String execsql1 = " INSERT INTO familyhas.fh_news_punish_detail(uid,news_code,order_code,order_time,product_code,product_name,product_cost,product_sell_price,punish_money,punish_reason)  VALUES(?,?,?,?,?,?,?,?,?,?) ";
					
					List<Object[]> batchArgsNotifi = new ArrayList<Object[]>();
					List<Object[]> batchArgsDetail = new ArrayList<Object[]>();
					
					Set<String> keySet = maps.keySet();
					for(String str : keySet) {
						List<ImportModel> list = maps.get(str);
						String tzCode = WebHelper.upCode("TZ");
						ImportModel importModel = list.get(0);
						//消息通知表
						batchArgsNotifi.add(new Object[]{
							UUID.randomUUID().toString().replace("-", ""),
							tzCode,
							importModel.getCompany_code(),
							importModel.getCompany_name(),
							importModel.getNews_notification_topic(),
							importModel.getType()
						});
						
						for(ImportModel model : list) {
							//处罚明细表
							batchArgsDetail.add(new Object[]{
								UUID.randomUUID().toString().replace("-", ""),
								tzCode,
								model.getOrder_code(),
								model.getOrder_time(),
								model.getProdcut_code(),
								model.getProduct_name(),
								model.getProduct_cost(),
								model.getProduct_sellprice(),
								model.getPunish_money(),
								model.getPunish_reason()
							});
						}
					}
					
					if(!batchArgsNotifi.isEmpty()) {
						DbUp.upTable("fh_news_notification").upTemplate().getJdbcOperations().batchUpdate(execSql, batchArgsNotifi);
					}
					if(!batchArgsDetail.isEmpty()) {
						DbUp.upTable("fh_news_punish_detail").upTemplate().getJdbcOperations().batchUpdate(execsql1, batchArgsDetail);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != instream) try { instream.close(); } catch (IOException e) {}
			}
	    }
		return result;
	}
	
	
	
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
	
	public static class UnshelveProductInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;
		

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}
	}
	
	public static class UnshelveProductResult extends RootResult {

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
		String orderAndProduct = "";//判断excel里有无重复订单和商品
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			//消息通知标题
			String newsNotificationTopic =  "";
			//订单编号
			String orderCode =  "";
			//商品编号
			String productCode = "";
			//处罚金额
			String punishMoney = "";
			//处罚原因
			String  punishReason = "";
			try {
				//读取当前行的几列数据
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						newsNotificationTopic = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						newsNotificationTopic = new DecimalFormat("#").format(d); 
					}
				} else {
					emptyError = true;
					emptyErrorInfo += "第"+(rIndex+1)+"行第1列为空,";
				}
				
				if( null != row.getCell(1) && row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						orderCode = row.getCell(1).getStringCellValue();
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(1).getNumericCellValue();
						orderCode = new DecimalFormat("#").format(d); 
					}
				} else {
					emptyError = true;
					emptyErrorInfo += "第"+(rIndex+1)+"行第2列为空,";
				}
				
				if( null != row.getCell(2) && row.getCell(2).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
						productCode = row.getCell(2).getStringCellValue();
					} else if(row.getCell(2).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(2).getNumericCellValue();
						productCode = new DecimalFormat("#").format(d); 
					}
				} else {
					emptyError = true;
					emptyErrorInfo += "第"+(rIndex+1)+"行第3列为空,";
				}
				
				if( null != row.getCell(3) && row.getCell(3).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
						punishMoney = row.getCell(3).getStringCellValue();
					} else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(3).getNumericCellValue();
						punishMoney = new DecimalFormat("#.##").format(d); 
					}
				} else {
					emptyError = true;
					emptyErrorInfo += "第"+(rIndex+1)+"行第4列为空,";
				}
				
				if( null != row.getCell(4) && row.getCell(4).getCellType() != Cell.CELL_TYPE_BLANK) {
					if(row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
						punishReason = row.getCell(4).getStringCellValue();
					} else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(4).getNumericCellValue();
						punishReason = new DecimalFormat("#").format(d); 
					}
				} else {
					emptyError = true;
					emptyErrorInfo += "第"+(rIndex+1)+"行第5列为空,";
				}
				
				if(emptyError) {
					continue;
				}
				//---------------------------------------------------------------
				//判断有无此订单下的商品，如果查到 完善 对象信息，如果没有加入错误编号	
				
				if(!StringUtils.isEmpty(orderCode)&&!StringUtils.isEmpty(productCode)) {
					
					String sql = "SELECT b.small_seller_code,c.seller_name,a.order_code,a.sku_name,a.cost_price,a.show_price,b.create_time FROM ordercenter.oc_orderdetail a  LEFT JOIN ordercenter.oc_orderinfo b  ON a.order_code = b.order_code LEFT JOIN usercenter.uc_sellerinfo c " + 
							"ON b.small_seller_code = c.small_seller_code WHERE a.sku_code = '"+productCode+"' AND a.order_code = '"+orderCode+"';";
					Map<String,Object> map = DbUp.upTable("oc_orderdetail").dataSqlOne(sql, null);
					String ssss = orderCode+"-"+productCode;
					
					//判断此订单下商品是否已有过处罚
					String sql1 = "SELECT * FROM familyhas.fh_news_punish_detail WHERE order_code  = '"+orderCode+"' AND product_code = '"+productCode+"';";
					Map<String,Object> map1 = DbUp.upTable("fh_news_punish_detail").dataSqlOne(sql1, null);
					if(map1!=null&&map1.size()>0) {
						punishedError = true;
						punishedErrorCode  = punishedErrorCode + ssss +",";
					}
					
					if(map==null||map.size()==0) {
						//无此订单下的商品
						orderAndSkuError = true;
						orderAndSkuErrorCode = orderAndSkuErrorCode +ssss+",";
					}else {
						//判断是否重复
						if(orderAndProduct.indexOf(ssss)!=-1) {
							//有重复订单下的sku
							repeatError  = true;
							repeatErrorCode = repeatErrorCode + ssss +",";
						}else {
							//完善当前商品信息    small_seller_code,seller_name,order_code,sku_name,cost_price,show_price,create_time
							model.setCompany_code(map.get("small_seller_code").toString());;
							model.setCompany_name("SI2003".equals(map.get("small_seller_code").toString())?"LD":map.get("seller_name").toString());
							model.setNews_notification_topic(newsNotificationTopic);
							model.setOrder_code(map.get("order_code").toString());
							model.setProdcut_code(productCode);
							model.setProduct_name(map.get("sku_name").toString());
							model.setProduct_cost( new  BigDecimal(map.get("cost_price").toString()) );
							model.setProduct_sellprice(  new  BigDecimal(map.get("show_price").toString())     );
							model.setPunish_money(new  BigDecimal(punishMoney));
							model.setPunish_reason(punishReason);
							model.setOrder_time(map.get("create_time").toString());
							
							orderAndProduct = orderAndProduct +ssss +",";
						}
						
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
		
		
		private String prodcut_code = "";
		
		private String product_name = "";
		
		private BigDecimal product_cost;
		
		private BigDecimal  product_sellprice;
		
		private String company_name;
		
		private String company_code;
		
		private String type =  "4497471600370002";//处罚
		
		private String error_message = "";
		
		private String news_notification_topic = "";
		
		private String order_code  ="";
		
		private BigDecimal punish_money = new BigDecimal(0.00);
		
		private String  punish_reason =  "";
		
		private  String order_time = "";
		

		private  boolean  flag ;
		
		
		
		public String getOrder_time() {
			return order_time;
		}

		public void setOrder_time(String order_time) {
			this.order_time = order_time;
		}

		public BigDecimal getProduct_cost() {
			return product_cost;
		}

		public void setProduct_cost(BigDecimal product_cost) {
			this.product_cost = product_cost;
		}

		public BigDecimal getProduct_sellprice() {
			return product_sellprice;
		}

		public void setProduct_sellprice(BigDecimal product_sellprice) {
			this.product_sellprice = product_sellprice;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getCompany_code() {
			return company_code;
		}

		public void setCompany_code(String company_code) {
			this.company_code = company_code;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public String getProdcut_code() {
			return prodcut_code;
		}

		public void setProdcut_code(String prodcut_code) {
			this.prodcut_code = prodcut_code;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getError_message() {
			return error_message;
		}

		public void setError_message(String error_message) {
			this.error_message = error_message;
		}

		public String getNews_notification_topic() {
			return news_notification_topic;
		}

		public void setNews_notification_topic(String news_notification_topic) {
			this.news_notification_topic = news_notification_topic;
		}

		public String getOrder_code() {
			return order_code;
		}

		public void setOrder_code(String order_code) {
			this.order_code = order_code;
		}

		public BigDecimal getPunish_money() {
			return punish_money;
		}

		public void setPunish_money(BigDecimal punish_money) {
			this.punish_money = punish_money;
		}

		public String getPunish_reason() {
			return punish_reason;
		}

		public void setPunish_reason(String punish_reason) {
			this.punish_reason = punish_reason;
		}
		
		

	}
	
	public static class ImportProductResult extends RootResult {

	}
}
