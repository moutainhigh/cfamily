package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.familyhas.api.input.ApiNewCategoryImportProductInput;
import com.cmall.familyhas.api.result.ApiNewCategoryImportProductResult;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 新建子分类商品导入
 * @author lgx
 *
 */
public class ApiNewCategoryImportProduct extends RootApi<ApiNewCategoryImportProductResult, ApiNewCategoryImportProductInput> {

	public ApiNewCategoryImportProductResult Process(ApiNewCategoryImportProductInput inputParam,
			MDataMap mRequestMap) {
		
		ApiNewCategoryImportProductResult result = new ApiNewCategoryImportProductResult();
		String fileRemoteUrl = inputParam.getUpload_show();
		String category_code = inputParam.getCategory_code();
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
				if(null != instream) {
					String readExcelResult = readExcel(instream,category_code);
					if(!readExcelResult.contains("操作成功")) {
						result.setResultCode(-1);
					}
					result.setResultMessage(readExcelResult);
				}
			} catch (Exception e) {
				result.setResultCode(-1);
				result.setResultMessage("导入商品失败:" + e.getLocalizedMessage());
				e.printStackTrace();
			} finally {
				if(null != instream) try { instream.close(); } catch (IOException e) {}
			}
			
	    }else {
	    	result.setResultCode(-1);
	    	result.setResultMessage("商品导入失败");
	    }
		return result;
	}
	
	
	/**
	 * 读取Excel商品数据
	 * 
	 * @param file
	 */
	public String readExcel(InputStream input, String category_code) {
		String result = "";
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			result = importProduct(sheet,category_code);
		} catch (FileNotFoundException e) {
			result = "导入商品失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入商品失败！" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}
	
	
	private String importProduct(Sheet sheet,String category_code) {
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		// 返回字符串
		StringBuffer sb =  new StringBuffer();
		// 导入的product_code查不到商品（格式、编号错误）
		StringBuffer noProd =  new StringBuffer();
		// 导入商品的product_code重复
		StringBuffer chongfuProd =  new StringBuffer();
		// 导入的product_code已经存在
		StringBuffer cunzaiProd =  new StringBuffer();
		// 全部的product_code
		StringBuffer allProd =  new StringBuffer();
		// 可以导入的商品map集合
		List<MDataMap> listMap = new ArrayList<MDataMap>();
				
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			Row row = sheet.getRow(rIndex);
			// 商品编码
			String product_code = getCellValue(row.getCell(0));
			
			if(!"".equals(product_code)) {
				// 检查product_code是否有对应商品
				MDataMap prod = DbUp.upTable("pc_productinfo").one("product_code",product_code,"seller_code","SI2003");
				if(null == prod) {
					noProd.append(product_code+",");
				}else {
					// 检查导入商品的product_code是否有重复
					if(allProd.toString().contains(product_code)) {
						if(chongfuProd.toString().contains(product_code)) {
							
						}else {							
							chongfuProd.append(product_code+",");
						}
					}else {
						// 检查导入的product_code是否已经存在
						MDataMap categoryProd = DbUp.upTable("uc_sellercategory_pre_product").one("category_code",category_code,"product_code",product_code);
						if(null != categoryProd) {
							if(cunzaiProd.toString().contains(product_code)) {
								
							}else {							
								cunzaiProd.append(product_code+",");
							}
						}else {
							MDataMap categoryProdMap = new MDataMap();
							categoryProdMap.put("category_code", category_code);
							categoryProdMap.put("product_code", product_code);
							listMap.add(categoryProdMap);
						}
					}
				}
			}
			allProd.append(product_code+",");
		}
		
		if(noProd.toString().trim().length()>0) {
			// 导入的product_code查不到商品（格式、编号错误）
			sb.append("以下商品编码查不到对应商品:"+noProd.toString().trim().substring(0,noProd.toString().trim().length()-1)+";");
			return sb.toString();
		}
		if(chongfuProd.toString().trim().length()>0) {
			// 导入商品的sku_code重复
			sb.append("以下商品编码在文件中重复:"+chongfuProd.toString().trim().substring(0,chongfuProd.toString().trim().length()-1)+";");
		}
		if(cunzaiProd.toString().trim().length()>0) {
			// 导入的product_code和sku_code已经存在
			sb.append("以下商品编码已经添加:"+cunzaiProd.toString().trim().substring(0,cunzaiProd.toString().trim().length()-1)+";");
		}
		
		String upDateTime = FormatHelper.upDateTime();
		ProductJmsSupport productJmsSupport = new ProductJmsSupport();
		if(listMap.size() > 0) {
			for (MDataMap mDataMap : listMap) {
				mDataMap.put("create_time", upDateTime);
				DbUp.upTable("uc_sellercategory_pre_product").dataInsert(mDataMap);
				// 刷新solr缓存
				productJmsSupport.updateSolrData(mDataMap.get("product_code"));
			}
			sb.append("操作成功");
		}else {
			if(sb.toString().trim().length()==0) {
				sb.append("导入文件为空");
			}
		}
		
		return sb.toString();
	}
	
	
	private String getCellValue(Cell cell) {
        String cellValue = "";
        DataFormatter formatter = new DataFormatter();
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = formatter.formatCellValue(cell);
                    } else {
                        BigDecimal value = new BigDecimal(cell.getNumericCellValue());
                       
                        cellValue = value.toString();
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }
	
}