package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.cmall.familyhas.api.ApiBatchUpshelveProductKL.UnshelveProductInput;
import com.cmall.familyhas.api.ApiBatchUpshelveProductKL.UnshelveProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;

public class ApiBatchUpshelveProductKL extends RootApi<UnshelveProductResult, UnshelveProductInput> {

	@Override
	public UnshelveProductResult Process(UnshelveProductInput input, MDataMap mRequestMap) {
		UnshelveProductResult result = new UnshelveProductResult();
		String fileRemoteUrl = input.getUpload_show();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
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
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的商品信息!!");
						return result;
					}
					
					//去重,并且记录是否有错误码
					List<String> productCodes =  new ArrayList<>();
					String errorProductCode  = "";
					Iterator<ImportModel> it = rtnList.iterator();
					while(it.hasNext()) {
						ImportModel  importModel =  it.next();
						//如果当前对象有错误编码代表商品编号错误，添加到错误码中 
						if(!"".equals(importModel.getError_message())) {
							String prodcut_code = importModel.getProdcut_code();
							errorProductCode += prodcut_code+",";
							continue;
						}
						//如果没有错误码才进，有错误码一会会返回 错误信息，全部不处理
						if("".equals(errorProductCode)) {
							//第一步去重，去掉提交数据中的重复信息，如果productCodes不包含 当前商品码，添加进去，如果包含了，代表有 重复商品码，去除
							if(!productCodes.contains(importModel.getProdcut_code())) {
								productCodes.add(importModel.getProdcut_code());
							}else {
								it.remove();
							}
						}
					}

					//判断errorProductCode是否为""，如果为""代表没有错误码 ，同时rtnList已去重；否则全部不处理返回错误商品码
					if(!"".equals(errorProductCode)) {
						result.setResultCode(-1);
						result.setResultMessage("导入数据错误，请检查导入数据信息:"+errorProductCode);
						return result;
					}
					//直接修改已经上架的商品的状态，不用走审批流程
					MDataMap paramMap = new MDataMap();
                    if(productCodes.size()>0) {
                    	for (String pCode : productCodes) {
                    		paramMap.put("product_code", pCode);
                    		paramMap.put("product_status", "4497153900060002");
    						int dataUpdate = DbUp.upTable("pc_productinfo").dataUpdate(paramMap,
    								"product_status",
    								"product_code");
    						if(dataUpdate>0) {
    							//其他操作 是否有必要
//    							PlusHelperNotice.onChangeProductInfo(pCode);
//    							ProductJmsSupport productJmsSupport = new  ProductJmsSupport();
//    							productJmsSupport.updateSolrData(pCode);
    						}
						}
                    }

				}
				
			} catch (Exception e) {
				result.setResultMessage("导入商品失败:商品编号不存在" /*+ e.getLocalizedMessage()*/);
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
		
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			//商品编号
			String productCode = "";
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						productCode = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						productCode = new DecimalFormat("#").format(d); 
					}
				} else {
					//没有商品编号或者sku编号，则终止循环
					break;
				}
					
				/**
				 * 只拼接成功导入,未找到商品,重复导入的商品编号
				 */
				if(!StringUtils.isEmpty(productCode)) {
					
					Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select * from "
							+ "pc_productinfo where product_code='" + productCode + "'", null);
					if(null != map && map.size() > 0) {
						String productStatus = map.get("product_status").toString();
						if("4497153900060001".equals(productStatus)) {
							//查询不到对应的记录
							model.setProdcut_code(productCode);//只为返回错误的商品编号或者sku编号
							model.setError_message("商品处于待上架，不能操作");
							model.setFlag(false);
						}else if("4497153900060003".equals(productStatus)||"4497153900060004".equals(productStatus)) {
							//商品处于下架状态
							String product_name = isEmpty(map.get("product_name")) ? "" : map.get("product_name").toString();
							BigDecimal min_sell_price = new BigDecimal(map.get("min_sell_price").toString());
							BigDecimal max_sell_price = new BigDecimal(map.get("max_sell_price").toString());
							String brand_code = isEmpty(map.get("brand_code")) ? "" : map.get("brand_code").toString();
							String small_seller_code = isEmpty(map.get("small_seller_code")) ? "" : map.get("small_seller_code").toString();
							String validate_flag = isEmpty(map.get("validate_flag")) ? "" : map.get("validate_flag").toString();
							model.setProdcut_code(productCode);
							model.setProduct_name(product_name);
							model.setMin_sell_price(min_sell_price);
							model.setMax_sell_price(max_sell_price);
							model.setBrand_code(brand_code);
							model.setSmall_seller_code(small_seller_code);
							model.setValidate_flag(validate_flag);
						}else if("4497153900060002".equals(productStatus)) {
							//已上架，忽略
							continue;
						}
						
					} else {
						//查询不到对应的记录
						model.setProdcut_code(productCode);//只为返回错误的商品编号或者sku编号
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

	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
	
public static class ImportModel {
		
		private String uid = "";
		
		/**
		 * 只为返回错误的商品编号或者sku编号
		 */
		private String prodcut_code = "";
		
		
		private String product_status =  "";//4497153900060001待上架   4497153900060002已上架  4497153900060003商家下架 4497153900060004平台强制下架
		
		
		private String product_name = "";
		
		private boolean flag = true;
		
		private String error_message = "";
		
//		min_sell_price,max_sell_price,brand_code,small_seller_code	
		private BigDecimal min_sell_price;
		
		private BigDecimal max_sell_price;
		
		private String brand_code;
		
		private String small_seller_code;
		
		private String validate_flag;
		
		
		
		public String getValidate_flag() {
			return validate_flag;
		}

		public void setValidate_flag(String validate_flag) {
			this.validate_flag = validate_flag;
		}

		public BigDecimal getMin_sell_price() {
			return min_sell_price;
		}

		public void setMin_sell_price(BigDecimal min_sell_price) {
			this.min_sell_price = min_sell_price;
		}

		public BigDecimal getMax_sell_price() {
			return max_sell_price;
		}

		public void setMax_sell_price(BigDecimal max_sell_price) {
			this.max_sell_price = max_sell_price;
		}

		public String getBrand_code() {
			return brand_code;
		}

		public void setBrand_code(String brand_code) {
			this.brand_code = brand_code;
		}

		public String getSmall_seller_code() {
			return small_seller_code;
		}

		public void setSmall_seller_code(String small_seller_code) {
			this.small_seller_code = small_seller_code;
		}

		public String getProduct_status() {
			return product_status;
		}

		public void setProduct_status(String product_status) {
			this.product_status = product_status;
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
}
