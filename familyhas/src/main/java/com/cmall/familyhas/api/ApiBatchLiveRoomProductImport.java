package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.cmall.familyhas.api.ApiBatchLiveRoomProductImport.ImportProductInput;
import com.cmall.familyhas.api.ApiBatchLiveRoomProductImport.ImportProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;


/**
 * 
 * @author zb 直播间商品批量导入
 *
 */
public class ApiBatchLiveRoomProductImport extends RootApi<ImportProductResult, ImportProductInput> {

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
		// 上传excel文件名(先传到文件服务器)
		private String upload_show;
		private String liveRoomId;

		public String getLiveRoomId() {
			return liveRoomId;
		}

		public void setLiveRoomId(String liveRoomId) {
			this.liveRoomId = liveRoomId;
		}

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

	}

	public static class ImportModel {
		private String productCode = "";
		private boolean flag = true;
		private String error_message = "";

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

		public String getProductCode() {
			return productCode;
		}

		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}

	}

	public static class ImportProductResult extends RootResult {

	}

	@Override
	public ImportProductResult Process(ImportProductInput input, MDataMap mRequestMap) {
		String fileRemoteUrl = input.getUpload_show();
		String liveRoomId = input.getLiveRoomId();
		ImportProductResult result = new ImportProductResult();
		int maxNum = 200;
		if (!StringUtils.isEmpty(fileRemoteUrl)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if (null != instream) {
					List<ImportModel> rtnList = readExcel(instream, liveRoomId);
					if (rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的数据!");
						return result;
					}
					// 参数集合
					boolean isExcute = true;
					for (ImportModel importModel : rtnList) {
						if (!importModel.isFlag()) {
							isExcute = false;
							result.setResultCode(0);
							result.setResultMessage(importModel.getError_message());
							break;
						}
					}
					if (isExcute) {
						int count = DbUp.upTable("lv_live_room_product").count("live_room_id", liveRoomId,
								"delete_flag", "1");
						if ((rtnList.size() + count) > maxNum) {
							result.setResultCode(0);
							result.setResultMessage("添加商品总数超过200个,请重新导入!");
							return result;
						} else {
							List<String> list = new ArrayList<>();
							for (ImportModel simportModel : rtnList) {
								String productCode = simportModel.getProductCode();
								list.add(productCode);
							}
							String sWhere = " product_code in ('" + StringUtils.join(list, "','") + "') ";
							List<MDataMap> mapList = DbUp.upTable("pc_productinfo").queryAll("", "", sWhere, null);
							// 查询最大顺序
							List<Map<String, Object>> dataSqlList = DbUp.upTable("lv_live_room_product").dataSqlList(
									"select IFNULL(MAX(sort),0) maxsort from lv_live_room_product where live_room_id='"
											+ liveRoomId + "' and delete_flag='1' ",
									null);
							int startSort = 1;
							if (dataSqlList != null && dataSqlList.size() > 0) {
								startSort = Integer.parseInt(dataSqlList.get(0).get("maxsort").toString()) + 1;
							}
							for (MDataMap mDataMap : mapList) {
								MDataMap insertMap = new MDataMap();
								insertMap.put("uid", WebHelper.upUuid());
								insertMap.put("live_room_id", liveRoomId);
								insertMap.put("product_code", mDataMap.get("product_code"));
								insertMap.put("product_name", mDataMap.get("product_name"));
								insertMap.put("product_picture", mDataMap.get("mainpic_url"));
								insertMap.put("product_price", mDataMap.get("min_sell_price"));
								insertMap.put("product_market_price", mDataMap.get("market_price"));
								insertMap.put("sort", startSort + "");
								DbUp.upTable("lv_live_room_product").dataInsert(insertMap);
								startSort++;
							}

							result.setResultCode(1);
							result.setResultMessage("导入成功！");
						}

					}
				}

			} catch (Exception e) {
				result.setResultMessage("导入失败" /* + e.getLocalizedMessage() */);
				e.printStackTrace();
			} finally {
				if (null != instream)
					try {
						instream.close();
					} catch (IOException e) {
					}
			}
		}
		return result;
	}

	/**
	 * 读取Excel商品数据
	 * 
	 * @param file
	 */
	public List<ImportModel> readExcel(InputStream input, String liveRoomId) {

		@SuppressWarnings("unused")
		String result = "";
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			resultModel = importProduct(sheet, liveRoomId);
		} catch (FileNotFoundException e) {
			result = "商品数据失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "商品信息数据失败！" /* + e.getLocalizedMessage() */;
			e.printStackTrace();
		}
		return resultModel;
	}

	private List<ImportModel> importProduct(Sheet sheet, String liveRoomId) {

		List<ImportModel> resultModel = new ArrayList<ImportModel>();

		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();

		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			ImportModel model = new ImportModel();
			String productCode = "";
			boolean flag = true;
			try {
				Row row = sheet.getRow(rIndex);
				if (null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					// 商品编号
					if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						productCode = row.getCell(0).getStringCellValue();
					} else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						productCode = new DecimalFormat("#").format(d);
					}
				}
				if (!StringUtils.isEmpty(productCode)) {
					int count = DbUp.upTable("pc_productinfo").count("product_code", productCode);
					int count1 = DbUp.upTable("lv_live_room_product").count("product_code", productCode, "live_room_id",
							liveRoomId, "delete_flag", "1");

					if (count == 0) {
						model.setProductCode(productCode);
						model.setError_message("第" + (rIndex + 1) + "行,存在无效的或者重复的商品编码，请检查！");
						model.setFlag(false);
						flag = false;
					} else if (count1 > 0) {
						model.setProductCode(productCode);
						model.setError_message("第" + (rIndex + 1) + "行,直播间已存在该商品，请检查！");
						model.setFlag(false);
						flag = false;
					}
				}
				if (flag) {
					model.setProductCode(productCode);
					model.setFlag(true);
				}
				resultModel.add(model);
			} catch (Exception e) {
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
	public static String verifyFormData(String start_date, String end_date, String sell_price) {
		String error = "";
		String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
		String dateRegex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|\n"
				+ "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|\n"
				+ "((0[48]|[2468][048]|[3579][26])00))-02-29)$";
		if (sell_price == null || "".equals(sell_price)) {
			error = "销售价格不能为空";
		} else if (!sell_price.matches(regex)) {
			error = "销售价格只能是数字";
		}

		else {

			if (start_date.matches(dateRegex) && end_date.matches(dateRegex)) {
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
			} else if (!start_date.matches(dateRegex)) {
				error = "开始日期格式不正确";
			} else if (!end_date.matches(dateRegex)) {
				error = "结束日期格式不正确";
			}

		}
		return error;
	}
}
