package com.cmall.familyhas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class SellerSettleInit {
	public static void main(String[] args) {

		String path = "F:/Modify1.xls";
		List<Map<String, Object>> list = readExcel(path, "xls");
		if (list != null && list.size() > 0) {
//			int i = 0;
			for (Map<String, Object> map : list) {
				String smallSellerCode = map.get("SM") != "" ? map.get("SM").toString() : "";
				String settleCode = map.get("ST") != "" ? map.get("ST").toString() : "";
				String payTime = map.get("PT") != "" ? map.get("PT").toString() : "";
				String payMoney = map.get("PTM") != "" ? map.get("PTM").toString() : "";
				String peiodMoney = map.get("PM") != "" ? map.get("PM").toString() : "";
				String actualMoney = map.get("AM") != "" ? map.get("AM").toString() : "";
				
				DbUp.upTable("oc_bill_merchant_new_spec").dataUpdate(new MDataMap("settle_code",settleCode,"merchant_code", smallSellerCode,"period_money", peiodMoney), 
						"period_money", "settle_code,merchant_code");
				try {
					Thread.sleep(30L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Map<String, Object> payMap = DbUp.upTable("oc_bill_merchant_new_spec").dataSqlOne("select *,(income_amount-service_fee) as payable_collect_amount, "
						+ "(income_amount-service_fee-add_deduction) as settle_collect_amount, (income_amount-service_fee-add_deduction-period_money) as actual_pay_amount1 from oc_bill_merchant_new_spec "
						+ "where settle_code=:settle_code and merchant_code=:merchant_code", new MDataMap("settle_code",settleCode,"merchant_code", smallSellerCode));
				if(payMap != null) {
					String amount = payMap.get("actual_pay_amount").toString();
					String payable_collect_amount = payMap.get("payable_collect_amount").toString();
					String settle_collect_amount = payMap.get("settle_collect_amount").toString();
					String actual_pay_amount1 = payMap.get("actual_pay_amount1").toString();
					if(amount.equals(payMoney)) {
						System.out.println(smallSellerCode + ":" + settleCode + "");
						if (payTime.equals("N")) {
							int i = DbUp.upTable("oc_bill_merchant_new_spec").dataUpdate(new MDataMap("settle_code",settleCode,"merchant_code", smallSellerCode, "flag", "4497476900040008",
									"payable_collect_amount",payable_collect_amount, "settle_collect_amount", settle_collect_amount, "period_money", peiodMoney, "actual_pay_amount",actual_pay_amount1), 
									"flag,payable_collect_amount,settle_collect_amount,period_money,actual_pay_amount", "settle_code,merchant_code");
							if(i == 0) {
								System.out.println(smallSellerCode + ":" + settleCode + " Failure");
							} else {
//								System.out.println(smallSellerCode + ":" + settleCode + " Success");
							}
						} else {
							int i = DbUp.upTable("oc_bill_merchant_new_spec").dataUpdate(new MDataMap("settle_code",settleCode,"merchant_code", smallSellerCode, "flag", "4497476900040009",
									"payable_collect_amount",payable_collect_amount, "settle_collect_amount", settle_collect_amount, "period_money", peiodMoney, "actual_pay_amount",actual_pay_amount1), 
									"flag,payable_collect_amount,settle_collect_amount,period_money,actual_pay_amount", "settle_code,merchant_code");
							if(i == 0) {
								System.out.println(smallSellerCode + ":" + settleCode + " Failure");
							} else {
//								System.out.println(smallSellerCode + ":" + settleCode + " Success");
							}
						}
					} else {
						System.out.println(smallSellerCode + ":" + settleCode + " ERROR!");
					}
				} else {
					System.out.println(smallSellerCode + ":" + settleCode + " Not Found!!");
				}
				
				
				//导入后校验
//				Map<String, Object> payMap = DbUp.upTable("oc_bill_merchant_new_spec").dataSqlOne("select *,(income_amount-service_fee) as payable_collect_amount, "
//						+ "(income_amount-service_fee-add_deduction) as settle_collect_amount, (income_amount-service_fee-add_deduction-period_money) as actual_pay_amount1 from oc_bill_merchant_new_spec "
//						+ "where settle_code=:settle_code and merchant_code=:merchant_code", new MDataMap("settle_code",settleCode,"merchant_code", smallSellerCode));
//				if(payMap != null) {
//					String amount = payMap.get("actual_pay_amount").toString();
//					if(amount.equals(actualMoney)) {
//						i++;
//					} else {
//						System.out.println(smallSellerCode + ":" + settleCode + " Not Equal!");
//					}
//				}
			}
//			System.out.println(i + " equals");
		}
	}
	
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

				// 第一个工作表(平台月结;
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
						// 商户编码
						String smallSellerCode = getCellValue(row.getCell(0));
						map.put("SM", smallSellerCode);
						// 结算单编码
						String settleCode = getCellValue(row.getCell(2));
						map.put("ST", settleCode);
						// 付款时间
						String payTime = getCellValue(row.getCell(4));
						map.put("PT", payTime);
						
						String payMoney = getCellValue(row.getCell(14));
						map.put("PTM", payMoney);
						
						String periodMoney = getCellValue(row.getCell(15));
						map.put("PM", periodMoney);
						
						String actualMoney = getCellValue(row.getCell(16));
						map.put("AM", actualMoney);
						
						list.add(map);
					}
				}
				
				
				// 第一个工作表(平台半月结;
				sheet = wb.getSheetAt(1);
				// 创建集合
				// 工作表的起始行编号
				firstRowIndex = sheet.getFirstRowNum();
				// 工作表的结束行编号
				lastRowIndex = sheet.getLastRowNum();
				for (int i = firstRowIndex + 1; i <= lastRowIndex; i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 商户编码
						String smallSellerCode = getCellValue(row.getCell(0));
						map.put("SM", smallSellerCode);
						// 结算单编码
						String settleCode = getCellValue(row.getCell(2));
						map.put("ST", settleCode);
						// 付款时间
						String payTime = getCellValue(row.getCell(4));
						map.put("PT", payTime);
						
						String payMoney = getCellValue(row.getCell(14));
						map.put("PTM", payMoney);
						
						String periodMoney = getCellValue(row.getCell(15));
						map.put("PM", periodMoney);
						
						String actualMoney = getCellValue(row.getCell(16));
						map.put("AM", actualMoney);
						list.add(map);
					}
				}
				
				
				// 第一个工作表(跨保半月结;
				sheet = wb.getSheetAt(2);
				// 创建集合
				// 工作表的起始行编号
				firstRowIndex = sheet.getFirstRowNum();
				// 工作表的结束行编号
				lastRowIndex = sheet.getLastRowNum();
				for (int i = firstRowIndex + 1; i <= lastRowIndex; i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 商户编码
						String smallSellerCode = getCellValue(row.getCell(0));
						map.put("SM", smallSellerCode);
						// 结算单编码
						String settleCode = getCellValue(row.getCell(2));
						map.put("ST", settleCode);
						// 付款时间
						String payTime = getCellValue(row.getCell(4));
						map.put("PT", payTime);
						
						String payMoney = getCellValue(row.getCell(14));
						map.put("PTM", payMoney);
						
						String periodMoney = getCellValue(row.getCell(15));
						map.put("PM", periodMoney);
						
						String actualMoney = getCellValue(row.getCell(16));
						map.put("AM", actualMoney);
						list.add(map);
					}
				}
				
				
				// 第一个工作表(跨直半月结;
				sheet = wb.getSheetAt(3);
				// 创建集合
				// 工作表的起始行编号
				firstRowIndex = sheet.getFirstRowNum();
				// 工作表的结束行编号
				lastRowIndex = sheet.getLastRowNum();
				for (int i = firstRowIndex + 1; i <= lastRowIndex; i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 商户编码
						String smallSellerCode = getCellValue(row.getCell(0));
						map.put("SM", smallSellerCode);
						// 结算单编码
						String settleCode = getCellValue(row.getCell(2));
						map.put("ST", settleCode);
						// 付款时间
						String payTime = getCellValue(row.getCell(4));
						map.put("PT", payTime);
						
						String payMoney = getCellValue(row.getCell(14));
						map.put("PTM", payMoney);
						
						String periodMoney = getCellValue(row.getCell(15));
						map.put("PM", periodMoney);
						
						String actualMoney = getCellValue(row.getCell(16));
						map.put("AM", actualMoney);
						list.add(map);
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
				if (cell.getColumnIndex() == 14 || cell.getColumnIndex() == 15 || cell.getColumnIndex() == 16) {
					big = new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
				} else {
					big = new BigDecimal(cell.getNumericCellValue());
				}

				return big.toString();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				BigDecimal big = null;
				if (cell.getColumnIndex() == 14 || cell.getColumnIndex() == 15 || cell.getColumnIndex() == 16) {
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
