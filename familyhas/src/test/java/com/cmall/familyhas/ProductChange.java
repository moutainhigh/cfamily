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

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopTest;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 类: ProductChange <br>
 * 描述: 商品信息更改 <br>
 * 作者: zhy<br>
 * 时间: 2017年5月17日 上午10:05:33
 */
public class ProductChange extends TopTest {

	public static void main(String[] args) {
		ProductChange.updateProduct();
	}

	/**
	 * 
	 * 方法: updateProduct <br>
	 * 描述: 修改商品的结算类型，采购类型，税率 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年5月16日 下午3:03:58
	 */
	public static void updateProduct() {
		String path = "E:/ichsy/惠家有/商品相关/product_20170609.xlsx";
		List<Map<String, Object>> list = readExcel(path, "xlsx");
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String product_code = map.get("product_code") != "" ? map.get("product_code").toString() : "";
				String settlement_type = map.get("settlement_type") != "" ? map.get("settlement_type").toString() : "";
				String purchase_type = map.get("purchase_type") != "" ? map.get("purchase_type").toString() : "";
				String tax_rate = map.get("tax_rate") != "" ? map.get("tax_rate").toString() : "";
				if (StringUtils.isNotBlank(product_code)) {
					if (StringUtils.isNotBlank(settlement_type) || StringUtils.isNotBlank(purchase_type)) {
						String sql = "update productcenter.pc_productinfo_ext set ";
						if (StringUtils.isNotBlank(settlement_type)) {
							sql += "settlement_type = '" + settlement_type + "' ";
						}
						if (StringUtils.isNotBlank(purchase_type)) {
							if (StringUtils.isNotBlank(settlement_type)) {
								sql += ",";
							}
							sql += " purchase_type='" + purchase_type + "'";
						}
						sql += " where product_code='" + product_code + "'";
						System.out.println(sql);
						DbUp.upTable("pc_productinfo_ext").dataExec(sql, new MDataMap());
					}
					if (StringUtils.isNotBlank(tax_rate)) {
						String sql = "update productcenter.pc_productinfo set tax_rate='" + tax_rate
								+ "' where product_code='" + product_code + "'";
						System.out.println(sql);
						DbUp.upTable("pc_productinfo").dataExec(sql, new MDataMap());
					}
				}
			}
		}
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
				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					// 第一个工作表;
					Sheet sheet = wb.getSheetAt(k);
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
							// 结算类型
							if (StringUtils.isNotBlank(getCellValue(row.getCell(1)))) {
								String settlement_type = getSettlementType(getCellValue(row.getCell(1)));
								map.put("settlement_type", settlement_type);
							} else {
								map.put("settlement_type", "");
							}
							// 采购类型
							if (StringUtils.isNotBlank(getCellValue(row.getCell(2)))) {
								String purchase_type = getPurchaseType(getCellValue(row.getCell(2)));
								map.put("purchase_type", purchase_type);
							} else {
								map.put("purchase_type", "");
							}
							// 税率
							map.put("tax_rate", getCellValue(row.getCell(3)));
							list.add(map);
						}
					}
				}
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

	/**
	 * 
	 * 方法: getSettlementType <br>
	 * 描述: 根据名称获取结算类型编码 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年5月16日 下午3:20:29
	 * 
	 * @param name
	 * @return
	 */
	private static String getSettlementType(String name) {
		/**
		 * 4497471600110001 常规结算<br>
		 * 4497471600110002 特殊结算<br>
		 * 4497471600110003 服务费结算<br>
		 * 
		 */
		String code = "";
		if (StringUtils.equals(name, "常规结算")) {
			code = "4497471600110001";
		} else if (StringUtils.equals(name, " 特殊结算")) {
			code = "4497471600110002";
		} else if (StringUtils.equals(name, "服务费结算")) {
			code = "4497471600110003";
		}
		return code;
	}

	/**
	 * 
	 * 方法: getPurchaseType <br>
	 * 描述: 根据名称获取采购类型编码 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年5月16日 下午3:21:33
	 * 
	 * @param name
	 * @return
	 */
	private static String getPurchaseType(String name) {
		/**
		 * 4497471600160001 代销<br>
		 * 4497471600160002 经销<br>
		 * 4497471600160003 代收代付<br>
		 */
		String code = "";
		if (StringUtils.equals(name, "代销")) {
			code = "4497471600160001";
		} else if (StringUtils.equals(name, "经销")) {
			code = "4497471600160002";
		} else if (StringUtils.equals(name, "代收代付")) {
			code = "4497471600160003";
		}
		return code;
	}
}
