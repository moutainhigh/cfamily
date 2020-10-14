package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.familyhas.api.input.ApiImportTemplateProductInput;
import com.cmall.familyhas.api.result.ApiImportTemplateProductResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 一行多栏(横滑)、一行三栏专题模板批量导入商品
 * @author LGX
 *
 */
public class ApiImportTemplateProduct extends RootApi<ApiImportTemplateProductResult, ApiImportTemplateProductInput> {

	@Override
	public ApiImportTemplateProductResult Process(ApiImportTemplateProductInput inputParam, MDataMap mRequestMap) {
		ApiImportTemplateProductResult result = new ApiImportTemplateProductResult();
		String fileRemoteUrl = inputParam.getUpload_show();
		if(!StringUtils.endsWith(fileRemoteUrl, ".xls")){
			result.setResultMessage("文件格式不对，请核实！");
			return result;
		}
		String templete_number = inputParam.getTemplete_number();
	    if(!StringUtils.isEmpty(fileRemoteUrl) || !StringUtils.isEmpty(templete_number)) {
	    	java.net.URL resourceUrl;
			InputStream instream = null;
			
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream) {
					String readExcelResult = readExcel(instream, templete_number);
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
	    	result.setResultMessage("请选择文件!");
	    }
		return result;
	}
	
	/**
	 * 读取Excel商品数据
	 * 
	 * @param file
	 */
	public String readExcel(InputStream input, String templete_number) {
		String result = "";
		MDataMap template = DbUp.upTable("fh_data_template").one("template_number", templete_number, "dal_status", "1001");
		if(template == null) {
			result = "模板不存在！";
			return result;
		}
		
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			result = importProduct(sheet, templete_number);
		} catch (FileNotFoundException e) {
			result = "导入商品失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入商品失败！" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}
	
	
	private String importProduct(Sheet sheet, String templete_number) {
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		
		// 返回字符串
		StringBuffer sb =  new StringBuffer();
		// 导入的product_code和sku_code查不到对应商品（格式、编号错误）
		StringBuffer noSku =  new StringBuffer();
		// 导入商品的sku_code重复
		//StringBuffer chongfuSku =  new StringBuffer();
		// 导入的product_code和sku_code已经存在
		//StringBuffer cunzaiSku =  new StringBuffer();
		// 导入的sku_code日期格式错误
		StringBuffer wrongDateSku =  new StringBuffer();
		// 全部的sku_code
		StringBuffer allSku =  new StringBuffer();
		// 可以导入的商品map集合
		List<MDataMap> listMap = new ArrayList<MDataMap>();
				
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			Row row = sheet.getRow(rIndex);
			if(row != null) {
				// SKU编码
//				String sku_code = getCellValue(row.getCell(0)).trim();
				// 开始时间
				String start_time = getCellValue(row.getCell(0)).trim();
				// 结束时间
				String end_time = getCellValue(row.getCell(1)).trim();
				// 位置
				String positionStr = getCellValue(row.getCell(2)).trim();
				positionStr = positionStr.equals("")?"99":positionStr;
				boolean matches = (Pattern.compile("[0-9]*")).matcher(String.valueOf(positionStr)).matches();
				int position = 99;
				if(matches) {
					position = Integer.parseInt(positionStr);
				}
				// 商品编码
				String product_code = getCellValue(row.getCell(3)).trim();
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(product_code);
				PlusModelSkuInfo plusModelSkuInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery).get(product_code);
				String sku_code = plusModelSkuInfo.getSkuCode();
				
				if(!"".equals(product_code) && !"".equals(sku_code)) {
					// 检查product_code和sku_code是否有对应商品
					Map<String, Object> skuMap = DbUp.upTable("pc_skuinfo").dataSqlOne("SELECT * FROM pc_skuinfo WHERE product_code = '"+product_code+"' AND sku_code = '"+sku_code+"'",new MDataMap());
					if(skuMap == null) {
						noSku.append(sku_code+",");
					}else {
						// 检查导入商品的sku_code是否有重复
						/*if(allSku.toString().contains(sku_code)) {
							chongfuSku.append(sku_code+",");
						}else {
							MDataMap one = DbUp.upTable("fh_data_commodity").one("template_number", templete_number,"good_number",product_code,"commodity_number",sku_code,"dal_status","1001");
							if(null != one) {
								cunzaiSku.append(sku_code+",");
							}else {*/
								// 检查日期格式
								Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
								if(!datePattern.matcher(start_time).matches() || !datePattern.matcher(end_time).matches()) {
									wrongDateSku.append(sku_code+",");
								}else {
									MDataMap prodMap = new MDataMap();
									prodMap.put("template_number", templete_number);
									prodMap.put("skip", "449747550002");
									prodMap.put("commodity_name", MapUtils.getString(skuMap, "sku_name"));
									prodMap.put("start_time", start_time);
									prodMap.put("end_time", end_time);
									prodMap.put("commodity_location", position+"");
									prodMap.put("commodity_number", sku_code);
									prodMap.put("good_number", product_code);
									listMap.add(prodMap);
								}
							/*}
						}*/
					}
				}
				// 全部的sku_code
				allSku.append(sku_code+",");
			}
		}
		
		if(noSku.toString().trim().length()>0) {
			// 导入的product_code和sku_code查不到商品（格式、编号错误）
			sb.append("以下SKU编码根据商品编码查不到对应商品:"+noSku.toString().trim().substring(0,noSku.toString().trim().length()-1)+";");
		}
		/*if(chongfuSku.toString().trim().length()>0) {
			// 导入商品的sku_code重复
			sb.append("以下SKU编码在文件中重复:"+chongfuSku.toString().trim().substring(0,chongfuSku.toString().trim().length()-1)+";");
		}
		if(cunzaiSku.toString().trim().length()>0) {
			// 导入的product_code和sku_code已经存在
			sb.append("以下SKU编码在模板中已经存在:"+cunzaiSku.toString().trim().substring(0,cunzaiSku.toString().trim().length()-1)+";");
		}*/
		if(wrongDateSku.toString().trim().length()>0) {
			// 导入的sku_code日期格式错误
			sb.append("以下SKU编码的日期格式填写有误:"+wrongDateSku.toString().trim().substring(0,wrongDateSku.toString().trim().length()-1)+";");
		}
		
		String createTime = FormatHelper.upDateTime();
		if(sb.toString().trim().length()==0) {
			if(listMap.size() > 0) {				
				for (MDataMap mDataMap : listMap) {
					mDataMap.put("create_time", createTime);
					DbUp.upTable("fh_data_commodity").dataInsert(mDataMap);
					
				}
				sb.append("操作成功");
			}else {
				sb.append("导入商品不能为空");
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
