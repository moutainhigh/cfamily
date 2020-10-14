package com.cmall.familyhas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 为商品加标签
 * @author zht
 *
 */
public class LabelChange {
	public static void main(String[] args) {
		LabelChange change = new LabelChange();
		change.changeLabel();
	}

	
	public void changeLabel() {
		String labelCode = "LB170629100001";
		String path = "f:/allproduct.xls";
		List<Map<String, Object>> list = readExcel(path, "xls");
		if (list != null && list.size() > 0) {
			List<Future<Integer>> taskList = new ArrayList<>();
			
			ExecutorService executor = Executors.newCachedThreadPool();
			int multiple = list.size() / 2000;
			int count = 0;
			for (int i = 0; i < multiple; i++) {
				List<Map<String, Object>> subList = list.subList(count * 2000, (count + 1) * 2000 - 1);
				count++;
				LabelChangeTask subTask = new LabelChangeTask(subList, labelCode);
				taskList.add(executor.submit(subTask));
			}
			if(list.size() % 2000 > 0) {
				List<Map<String, Object>> remainderList = list.subList(count * 2000, list.size());
				LabelChangeTask subTask = new LabelChangeTask(remainderList, labelCode);
				taskList.add(executor.submit(subTask));
			}
			
			Integer total = 0;
			for(Future<Integer> task : taskList){
				while(!task.isDone()){
				}
				try {
					total += task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println("total:" + total);
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
}


