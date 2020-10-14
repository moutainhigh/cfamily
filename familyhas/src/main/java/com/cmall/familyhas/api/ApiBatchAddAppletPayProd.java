package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.familyhas.api.input.ApiBatchAddAppletPayProdInput;
import com.cmall.familyhas.api.result.ApiBatchAddAppletPayProdResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 批量添加小程序支付完成配置商品
 * @author LGX
 *
 */
public class ApiBatchAddAppletPayProd extends RootApi<ApiBatchAddAppletPayProdResult, ApiBatchAddAppletPayProdInput> {

	@Override
	public ApiBatchAddAppletPayProdResult Process(ApiBatchAddAppletPayProdInput inputParam, MDataMap mRequestMap) {
		ApiBatchAddAppletPayProdResult result = new ApiBatchAddAppletPayProdResult();
		String fileRemoteUrl = inputParam.getUpload_show();
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
					String readExcelResult = readExcel(instream);
					if(!"操作成功".equals(readExcelResult)) {
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
			if(row != null) {
				// 商品编码
				String product_code = StringUtils.trimToEmpty(getCellValue(row.getCell(0)));
				// 位置
				String positionStr = StringUtils.trimToEmpty(getCellValue(row.getCell(1)));
				positionStr = positionStr.equals("")?"0":positionStr;
				boolean matches = (Pattern.compile("[0-9]*")).matcher(String.valueOf(positionStr)).matches();
				int position = 0;
				if(matches) {
					position = Integer.parseInt(positionStr);
				}
				
				if(!"".equals(product_code)) {
					MDataMap productinfo = DbUp.upTable("pc_productinfo").one("product_code",product_code,"seller_code","SI2003","if_delete","0");
					if(null != productinfo) {
						if(allProd.toString().contains(product_code)) {
							chongfuProd.append(product_code+",");
						}else {
							MDataMap one = DbUp.upTable("pc_applet_pay_success").one("product_code",product_code);
							if(null != one) {
								cunzaiProd.append(product_code+",");
							}else {
								MDataMap prodMap = new MDataMap();
								prodMap.put("product_code", product_code);
								prodMap.put("position", position+"");
								listMap.add(prodMap);
							}
						}
					}else {
						noProd.append(product_code+",");
					}
					allProd.append(product_code+",");
				}
			}
		}
		
		if(noProd.toString().trim().length()>0) {
			// 导入的product_code查不到商品（格式、编号错误）
			sb.append("以下商品编码查不到对应商品:"+noProd.toString().trim().substring(0,noProd.toString().trim().length()-1)+";");
		}
		if(chongfuProd.toString().trim().length()>0) {
			// 导入商品的product_code重复
			sb.append("以下商品编码在文件中重复:"+chongfuProd.toString().trim().substring(0,chongfuProd.toString().trim().length()-1)+";");
		}
		if(cunzaiProd.toString().trim().length()>0) {
			// 导入的product_code已经存在
			sb.append("以下商品编码已经存在:"+cunzaiProd.toString().trim().substring(0,cunzaiProd.toString().trim().length()-1)+";");
		}
		
		String create_time = FormatHelper.upDateTime();
		if(sb.toString().trim().length()==0) {
			if(listMap.size() > 0) {				
				for (MDataMap mDataMap : listMap) {
					String create_user = UserFactory.INSTANCE.create().getLoginName();
					mDataMap.put("create_time", create_time);
					mDataMap.put("create_user", create_user);
					DbUp.upTable("pc_applet_pay_success").dataInsert(mDataMap);
				}
				sb.append("操作成功");
			}else {
				sb.append("导入商品为空");
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
