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
import com.cmall.familyhas.api.ApiBatchChangeCooperationSta.ImportProductInput;
import com.cmall.familyhas.api.ApiBatchChangeCooperationSta.ImportProductResult;
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
public class ApiBatchChangeCooperationSta extends RootApi<ImportProductResult, ImportProductInput> {
	
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
		private String small_seller_code;
		private boolean flag = true;
		private String error_message;
		private String userName;
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getError_message() {
			return error_message;
		}

		public void setError_message(String error_message) {
			this.error_message = error_message;
		}
		
		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public String getSmall_seller_code() {
			return small_seller_code;
		}

		public void setSmall_seller_code(String small_seller_code) {
			this.small_seller_code = small_seller_code;
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
						result.setResultMessage("没有需要导入的公司编号!");
						return result;
					}
					//参数集合
					List<Map<String,Object>> listMap = new ArrayList<>();
					boolean isExcute = true;
					for (ImportModel importModel : rtnList) {
						if(importModel.isFlag()) {
							Map<String,Object> map =new HashMap<>();
							map.put("small_seller_code", importModel.getSmall_seller_code());
							map.put("user_name", importModel.getUserName());
							listMap.add(map);
						} else {
							isExcute = false;
							if(errorMap.containsKey(importModel.getError_message())) {
								String tempVal = errorMap.get(importModel.getError_message());
								errorMap.put(importModel.getError_message(), tempVal+","+importModel.getSmall_seller_code());
							} else {
								errorMap.put(importModel.getError_message(),importModel.getSmall_seller_code());
							}
							
						}
						
					}
					if(isExcute) {
						String outUserSql = "update za_userinfo set flag_enable = '2',cookie_user='' where user_name=:user_name ";
						for (Map<String, Object> map : listMap) {
							DbUp.upTable("za_userinfo").dataExec(outUserSql, new MDataMap("user_name",map.get("user_name").toString()));
						}
						
					}
					if(!isExcute) {
						result.setResultMessage(JSON.toJSONString(errorMap));
					}
					
				}
				
			} catch (Exception e) {
				result.setResultMessage("导入失败:公司编号编号不存在" /*+ e.getLocalizedMessage()*/);
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
			result = "导入公司编号信息失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入公司编号信息失败！" /*+ e.getLocalizedMessage()*/;
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
			
			String small_seller_code ="";
		    
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//公司编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						small_seller_code = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						small_seller_code = new DecimalFormat("#").format(d); 
					}
				}
				if(!StringUtils.isEmpty(small_seller_code)){
					Map<String,Object> resutlMap = DbUp.upTable("za_userinfo").dataSqlOne("select zu.flag_enable as flag,zu.user_name as user_name from zapdata.za_userinfo zu,usercenter.uc_seller_info_extend ue where zu.user_name=ue.user_name and ue.small_seller_code=:small_seller_code", new MDataMap("small_seller_code",small_seller_code));
					if(resutlMap==null) {
						//查询不到对应的记录
						model.setSmall_seller_code(small_seller_code);
						model.setError_message("该公司编号不存在");
						model.setFlag(false);
					}
					else if("1".equals(resutlMap.get("flag").toString())) {
						
						model.setSmall_seller_code(small_seller_code);
						model.setError_message("商户非冻结状态,不能进行结束合作操作");
						model.setFlag(false);
					}
					else if("2".equals(resutlMap.get("flag").toString())) {
						
						model.setSmall_seller_code(small_seller_code);
						model.setError_message("商户已为结束合作状态");
						model.setFlag(false);
					}
					else {
						
						model.setSmall_seller_code(small_seller_code);
						model.setUserName(resutlMap.get("user_name").toString());
						model.setFlag(true);
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
		String dateRegex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|\n" + 
				           "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|\n" + 
				           "((0[48]|[2468][048]|[3579][26])00))-02-29)$";
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
