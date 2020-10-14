package com.cmall.familyhas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 批量刷新商品上下加状态
 * @author zht
 * 4497153900060001:待上架
 * 4497153900060002:已上架
 * 4497153900060003:商家下架
 * 4497153900060004:平台强制下架
 *
 */
public class BatchRefreshProductStatus {
	public static void main(String[] args) {
		Integer updated = new Integer(0);
		String path = "F:/productTax.xls";
		List<Map<String, Object>> list = readExcel(path, "xls");
		ProductJmsSupport pjs = new ProductJmsSupport();
		for (Map<String, Object> map : list) {
			String productCode = map.get("product_code") != "" ? map.get("product_code").toString() : "";
//			String smallSellerCode = map.get("small_seller_code") != "" ? map.get("small_seller_code").toString() : "";
			if(StringUtils.isNotBlank(productCode)) {
				String sql = "update pc_productinfo set product_status=:status where product_code=:productCode";
				int count = DbUp.upTable("pc_productinfo").dataExec(sql, new MDataMap("productCode", productCode, "status", "4497153900060002"));
				if(count > 0) {
					PlusHelperNotice.onChangeProductInfo(productCode);
					//触发消息队列
					pjs.onChangeForProductChangeAll(productCode);
					updated++;
				}
			} else {
				System.out.println("product_code is Null");
			}
		}
		System.out.println("Update " + updated + " products");
	}

	
	/**
	 * 
	 * 方法: readExcel <br>
	 * 描述: 读取excel文件 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年5月16日 下午2:00:59
	 * 
	 * @param file
	 * @param type
	 * @return
	 */
	private static List<Map<String, Object>> readExcel(String path, String type) {
		List<Map<String, Object>> list = null;
		Workbook wb = null;
		FileInputStream fis = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fis = new FileInputStream(file);
				if (type == "xlsx" || "xlsx".equals(type)) {
					wb = new XSSFWorkbook(fis);
				} else {
					wb = new HSSFWorkbook(fis);
				}
				list = new ArrayList<Map<String, Object>>();
//				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					// 第一个工作表;
					Sheet sheet = wb.getSheetAt(0);
					// 创建集合
					// 工作表的起始行编号
					int firstRowIndex = sheet.getFirstRowNum();
					// 工作表的结束行编号
					int lastRowIndex = sheet.getLastRowNum();
					for (int i = firstRowIndex + 1; i <= lastRowIndex; i++) {
						Row row = sheet.getRow(i);
						if (row != null) {
							Map<String, Object> map = new HashMap<String, Object>();
							// 商品编码
							map.put("product_code", getCellValue(row.getCell(0)));
//							map.put("small_seller_code", getCellValue(row.getCell(2)));
							list.add(map);
						}
					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	private static String getCellValue(org.apache.poi.ss.usermodel.Cell cell) {

		if (cell != null) {
			// excel表格中数据为字符串
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue() != null ? cell.getStringCellValue() : "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				BigDecimal big = null;
				if (cell.getColumnIndex() == 3) {
					big = new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					big = new BigDecimal(cell.getNumericCellValue());
				}

				return big.toString();
			}
		}
		return "";
	}
}
