package com.cmall.familyhas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 税率调整,在同一商户下迁移商品
 * @author zht
 * pc_productinfo(finish)	
 * pc_productinfo_ext(finish)
 * pc_productdescription(finish)
 * pc_product_authority_logo
 * pc_skuinfo(finish, sell_productcode 与product_code的区别)
 * sc_store_skunum(finish)
 * pc_productpic(finish)
 * nc_order_evaluation
 * pc_product_top50(finish)
 * pc_productcategory_rel(finish)
 * pc_productpic
 * pc_productproperty(finish)
 * pc_solr_exclude_product
 * uc_sellercategory_product_relation
 * pc_productsales_everyday
 */
public class TaxAdjust {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		FileWriter logger = null;
		try {
			logger = new FileWriter(new File("d:/product_move_flow.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter sqlLogger = null;
		try {
			sqlLogger = new FileWriter(new File("d:/product_move_sql.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String path = "F:/taxAAA.xls";
		List<Map<String, Object>> list = readExcel(path, "xls");
		if(null != list && !list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String productCode = map.get("product_code") != "" ? map.get("product_code").toString() : "";
				String newProductCode = map.get("new_product_code") != "" ? map.get("new_product_code").toString() : "";
//				moveProduct(smallSellerCode, productCode, logger, sqlLogger);
				copyFlowMain(productCode, newProductCode, logger);
			}
		}
		
		
		try {
			logger.flush();
			logger.close();
			
			sqlLogger.flush();
			sqlLogger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End!!");
	}
	
	public static void copyFlowMain(String productCode, String newProductCode, FileWriter logger) {
		String sql = "select * from sc_flow_main where outer_code=:product_code and flow_type in('449717230011','449717230016')";
		List<Map<String,Object>> flowList = DbUp.upTable("sc_flow_main").dataSqlList(sql, new MDataMap("product_code",productCode));
		if(flowList != null && !flowList.isEmpty()) {
			for(Map<String, Object> flowMap : flowList) {
				String newFlowCode = WebHelper.upCode("SF");
				
				String flow_url = flowMap.get("flow_url") == null ? "" : flowMap.get("flow_url").toString();
				flow_url = flow_url.replace(productCode, newProductCode);
				flowMap.put("flow_url", flow_url);
				
				String flow_title = flowMap.get("flow_title") == null ? "" : flowMap.get("flow_title").toString();
				flow_title = flow_title.replace(productCode, newProductCode);
				flowMap.put("flow_title", flow_title);
				
				String oldFlowCode = flowMap.get("flow_code") == null ? "" : flowMap.get("flow_code").toString();
				flowMap.put("flow_code", newFlowCode);
				flowMap.put("outer_code", newProductCode);
				flowMap.remove("uid");
				flowMap.remove("zid");
				DbUp.upTable("sc_flow_main").dataInsert(new MDataMap(flowMap));
				writeLog(logger, "#" + productCode+ "," + newProductCode + "," + oldFlowCode + "," + newFlowCode, true);
				writeLog(logger, "delete from sc_flow_main where flow_code ='" + newFlowCode + "'", true);
				
			}
		}
	}
	
	public static void moveProduct(String smallSellerCode, String productCode, FileWriter logger, FileWriter sqlLogger) {
		//复制商品基本信息
		List<Map<String,Object>> productList = DbUp.upTable("pc_productinfo")
				.dataSqlList("select * from pc_productinfo where small_seller_code=:ssc and product_code=:product_code", 
						new MDataMap("ssc",smallSellerCode, "product_code",productCode));
		if(productList != null && !productList.isEmpty()) {
			for(Map<String, Object> productMap : productList) {
				String newProductCode = WebHelper.upCode("8016");
				System.out.println("##### Gen new product:" + newProductCode);
				
				
				String product_code_old = productMap.get("product_code_old") == null ? "" : productMap.get("product_code_old").toString();
				if(StringUtils.isBlank(product_code_old)) {
					productMap.put("product_code_old", productMap.get("product_code").toString());
				} 
				String product_code_copy = productMap.get("product_code_copy") == null ? "" : productMap.get("product_code_copy").toString();
				if(StringUtils.isNotBlank(product_code_copy)) {
					productMap.put("product_code_copy", newProductCode);
				}
				
				product_code_old = productMap.get("product_code").toString();
				String old_product_status = productMap.get("product_status").toString();
				
				productMap.put("product_code", newProductCode);
				productMap.put("small_seller_code", smallSellerCode);
				productMap.put("tax_rate", "0.11");
				String newDate = sdf.format(new Date());
				productMap.put("create_time", newDate);
				productMap.put("update_time", newDate);
				//强制下架
				productMap.put("product_status", "4497153900060004");
				productMap.remove("uid");
				productMap.remove("zid");
				DbUp.upTable("pc_productinfo").dataInsert(new MDataMap(productMap));
				
				writeLog(logger, smallSellerCode+ "," + product_code_old + "," + newProductCode + "," + old_product_status, true);
				writeLog(sqlLogger, "delete from productcenter.pc_productinfo where product_code='" + newProductCode  + "';", true);
				System.out.println("delete from productcenter.pc_productinfo where product_code='" + newProductCode  + "';");
				
				//复制商品扩展信息(修改结算类型,有采购类型)
				List<Map<String,Object>> productExtList = DbUp.upTable("pc_productinfo_ext")
						.dataSqlList("select * from pc_productinfo_ext where product_code=:product_code", new MDataMap("product_code", product_code_old));
				if(productExtList != null && !productExtList.isEmpty()) {
					for(Map<String, Object> productExtMap : productExtList) {
//						//代收代付
//						productExtMap.put("purchase_type", "4497471600160003");
//						//服务费结算
//						productExtMap.put("settlement_type", "4497471600110003");
						
						String productOldSubTab = productExtMap.get("product_code_old") == null ? "" : productExtMap.get("product_code_old").toString();
						if(StringUtils.isBlank(productOldSubTab)) {
							productExtMap.put("product_code_old", product_code_old);
						}
						
//						productExtMap.put("dlr_id", newSellerCode);
						productExtMap.put("product_code", newProductCode);
						productExtMap.remove("uid");
						productExtMap.remove("zid");
						DbUp.upTable("pc_productinfo_ext").dataInsert(new MDataMap(productExtMap));
						writeLog(sqlLogger, "delete from productcenter.pc_productinfo_ext where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_productinfo_ext where product_code='" + newProductCode  + "';");
					}
				}
				
				//复制商品描述
				List<Map<String,Object>> productDescList = DbUp.upTable("pc_productdescription")
						.dataSqlList("select * from pc_productdescription where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productDescList != null && !productDescList.isEmpty()) {
					for(Map<String, Object> productDescMap : productDescList) {
						productDescMap.put("product_code", newProductCode);
						productDescMap.remove("uid");
						productDescMap.remove("zid");
						DbUp.upTable("pc_productdescription").dataInsert(new MDataMap(productDescMap));
						writeLog(sqlLogger, "delete from productcenter.pc_productdescription where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_productdescription where product_code='" + newProductCode  + "';");
					}
				}
				
				//复制商品7日退换信息
				List<Map<String,Object>> logoList = DbUp.upTable("pc_product_authority_logo")
						.dataSqlList("select * from pc_product_authority_logo where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(logoList != null && !logoList.isEmpty()) {
					for(Map<String, Object> logoMap : logoList) {
						logoMap.put("product_code", newProductCode);
						logoMap.put("create_time", newDate);
						logoMap.remove("uid");
						logoMap.remove("zid");
						DbUp.upTable("pc_product_authority_logo").dataInsert(new MDataMap(logoMap));
						writeLog(sqlLogger, "delete from productcenter.pc_product_authority_logo where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_product_authority_logo where product_code='" + newProductCode  + "';");
					}
				}
				
				
				//复制商品sku信息
				List<Map<String,Object>> skuList = DbUp.upTable("pc_skuinfo")
						.dataSqlList("select * from pc_skuinfo where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(skuList != null && !skuList.isEmpty()) {
					for(Map<String, Object> skuMap : skuList) {
						String newSkuCode = WebHelper.upCode("8019");
						
						//sell_productcode与product_code的关系
						String sku_code_old = skuMap.get("sku_code_old") == null ? "" : skuMap.get("sku_code_old").toString();
						if(StringUtils.isNotBlank(sku_code_old)) {
							skuMap.put("sku_code_old", newSkuCode);
						}
						
						sku_code_old = skuMap.get("sku_code").toString();
						skuMap.put("sku_code", newSkuCode);
						skuMap.put("product_code", newProductCode);
						skuMap.remove("uid");
						skuMap.remove("zid");
						DbUp.upTable("pc_skuinfo").dataInsert(new MDataMap(skuMap));
						writeLog(sqlLogger, "delete from productcenter.pc_skuinfo where sku_code='" + newSkuCode  + "';", true);
						System.out.println("delete from productcenter.pc_skuinfo where sku_code='" + newSkuCode  + "';");
						
						//复制sku库存表
						List<Map<String,Object>> skuStoreList = DbUp.upTable("sc_store_skunum")
								.dataSqlList("select * from sc_store_skunum where sku_code=:sku_code", new MDataMap("sku_code",sku_code_old));
						if(skuStoreList != null && !skuStoreList.isEmpty()) {
							for(Map<String, Object> skuStoreMap : skuStoreList) {
								skuStoreMap.put("sku_code", newSkuCode);
								skuStoreMap.remove("uid");
								skuStoreMap.remove("zid");
								DbUp.upTable("sc_store_skunum").dataInsert(new MDataMap(skuStoreMap));
								writeLog(sqlLogger, "delete from systemcenter.sc_store_skunum where sku_code='" + newSkuCode  + "';", true);
								System.out.println("delete from systemcenter.sc_store_skunum where sku_code='" + newSkuCode  + "';");
							}
						}
						
						//复制商品图片(有sku的情况)
						List<Map<String,Object>> productPicList = DbUp.upTable("pc_productpic")
								.dataSqlList("select * from pc_productpic where product_code=:product_code and sku_code=:sku_code", 
										new MDataMap("product_code",product_code_old, "sku_code", sku_code_old));
						if(productPicList != null && !productPicList.isEmpty()) {
							for(Map<String, Object> productPicMap : productPicList) {
								String productOldSubTab = productPicMap.get("product_code_old") == null ? "" : productPicMap.get("product_code_old").toString();
								if(StringUtils.isBlank(productOldSubTab)) {
									productPicMap.put("product_code_old", product_code_old);
								}
								
								productPicMap.put("product_code", newProductCode);
								productPicMap.put("sku_code", newSkuCode);
								productPicMap.remove("uid");
								productPicMap.remove("zid");
								DbUp.upTable("pc_productpic").dataInsert(new MDataMap(productPicMap));
								writeLog(sqlLogger, "delete from productcenter.pc_productpic where product_code='" + newProductCode  + "' and sku_code='" + newSkuCode  + "';", true);
								System.out.println("delete from productcenter.pc_productpic where product_code='" + newProductCode  + "' and sku_code='" + newSkuCode  + "';");
							}
						}
						
						//复制商品评论
						List<Map<String,Object>> commentList = DbUp.upTable("nc_order_evaluation")
								.dataSqlList("select * from nc_order_evaluation where product_code=:product_code and order_skuid=:sku_code", 
										new MDataMap("product_code",product_code_old, "sku_code", sku_code_old));
						if(commentList != null && !commentList.isEmpty()) {
							for(Map<String, Object> commentMap : commentList) {
								commentMap.put("product_code", newProductCode);
								commentMap.put("order_skuid", newSkuCode);
								commentMap.remove("uid");
								commentMap.remove("zid");
								DbUp.upTable("nc_order_evaluation").dataInsert(new MDataMap(commentMap));
								writeLog(sqlLogger, "delete from newscenter.nc_order_evaluation where product_code='" + newProductCode  + "' and order_skuid='" + newSkuCode  + "';", true);
								System.out.println("delete from newscenter.nc_order_evaluation where product_code='" + newProductCode  + "' and order_skuid='" + newSkuCode  + "';");
							}
						}
						
					}
				}
				
		
				//复制Top50
				List<Map<String,Object>> productTop50List = DbUp.upTable("pc_product_top50")
						.dataSqlList("select * from pc_product_top50 where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productTop50List != null && !productTop50List.isEmpty()) {
					for(Map<String, Object> productTop50Map : productTop50List) {
						productTop50Map.put("product_code", newProductCode);
						productTop50Map.remove("uid");
						productTop50Map.remove("zid");
						DbUp.upTable("pc_product_top50").dataInsert(new MDataMap(productTop50Map));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_product_top50 where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_product_top50 where product_code='" + newProductCode  + "';");
				}
				
				
				//复制商品和分类关联
				List<Map<String,Object>> productCategoryList = DbUp.upTable("pc_productcategory_rel")
						.dataSqlList("select * from pc_productcategory_rel where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productCategoryList != null && !productCategoryList.isEmpty()) {
					for(Map<String, Object> productCategoryMap : productCategoryList) {
						productCategoryMap.put("product_code", newProductCode);
						productCategoryMap.remove("uid");
						productCategoryMap.remove("zid");
						DbUp.upTable("pc_productcategory_rel").dataInsert(new MDataMap(productCategoryMap));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_productcategory_rel where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productcategory_rel where product_code='" + newProductCode  + "';");
				}
				
				//复制商品图片(无sku的情况)
				List<Map<String,Object>> productPicList = DbUp.upTable("pc_productpic")
						.dataSqlList("select * from pc_productpic where product_code=:product_code and sku_code = ''", new MDataMap("product_code",product_code_old));
				if(productPicList != null && !productPicList.isEmpty()) {
					for(Map<String, Object> productPicMap : productPicList) {
						String productOldSubTab = productPicMap.get("product_code_old") == null ? "" : productPicMap.get("product_code_old").toString();
						if(StringUtils.isBlank(productOldSubTab)) {
							productPicMap.put("product_code_old", product_code_old);
						}
						
						productPicMap.put("product_code", newProductCode);
						productPicMap.remove("uid");
						productPicMap.remove("zid");
						DbUp.upTable("pc_productpic").dataInsert(new MDataMap(productPicMap));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_productpic where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productpic where product_code='" + newProductCode  + "';");
				}
				
				//复制商品属性
				List<Map<String,Object>> productPropertyList = DbUp.upTable("pc_productproperty")
						.dataSqlList("select * from pc_productproperty where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productPropertyList != null && !productPropertyList.isEmpty()) {
					for(Map<String, Object> productPropertyMap : productPropertyList) {
						productPropertyMap.put("product_code", newProductCode);
						productPropertyMap.remove("uid");
						productPropertyMap.remove("zid");
						DbUp.upTable("pc_productproperty").dataInsert(new MDataMap(productPropertyMap));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_productproperty where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productproperty where product_code='" + newProductCode  + "';");
				}
				
				//复制商品搜索索引表
				List<Map<String,Object>> solrExcludeProductList = DbUp.upTable("pc_solr_exclude_product")
						.dataSqlList("select * from pc_solr_exclude_product where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(solrExcludeProductList != null && !solrExcludeProductList.isEmpty()) {
					for(Map<String, Object> excludeProductMap : solrExcludeProductList) {
						excludeProductMap.put("product_code", newProductCode);
						excludeProductMap.remove("uid");
						excludeProductMap.remove("zid");
						DbUp.upTable("pc_solr_exclude_product").dataInsert(new MDataMap(excludeProductMap));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_solr_exclude_product where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_solr_exclude_product where product_code='" + newProductCode  + "';");
				}
				
				//uc_sellercategory_product_relation
				List<Map<String,Object>> cprList = DbUp.upTable("uc_sellercategory_product_relation")
						.dataSqlList("select * from uc_sellercategory_product_relation where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(cprList != null && !cprList.isEmpty()) {
					for(Map<String, Object> cprMap : cprList) {
						cprMap.put("product_code", newProductCode);
						cprMap.remove("uid");
						cprMap.remove("zid");
						DbUp.upTable("uc_sellercategory_product_relation").dataInsert(new MDataMap(cprMap));
					}
					writeLog(sqlLogger, "delete from usercenter.uc_sellercategory_product_relation where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from usercenter.uc_sellercategory_product_relation where product_code='" + newProductCode  + "';");
				}
				
				//复制商品销量
				List<Map<String,Object>> productSaleList = DbUp.upTable("pc_productsales_everyday")
						.dataSqlList("select * from pc_productsales_everyday where product_code=:product_code", new MDataMap("product_code", product_code_old));
				if(productSaleList != null && !productSaleList.isEmpty()) {
					for(Map<String,Object> productSaleMap : productSaleList) {
						productSaleMap.put("product_code", newProductCode);
						productSaleMap.remove("uid");
						productSaleMap.remove("zid");
						DbUp.upTable("pc_productsales_everyday").dataInsert(new MDataMap(productSaleMap));
					}
					writeLog(sqlLogger, "delete from productcenter.pc_productsales_everyday where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productsales_everyday where product_code='" + newProductCode  + "';");
				}
			}
		} else {
			writeLog(logger, smallSellerCode+ "," + productCode + " Not Found", true);
		}
	}
			
	private static void writeLog(FileWriter logger, String content, boolean newLine) {
		if(logger != null) {
			try {
				logger.write(content);
				if(newLine) {
					logger.write("\r\n");
				}
				logger.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
							map.put("new_product_code", getCellValue(row.getCell(1)));
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
