package com.cmall.familyhas.api.orderimport;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.AddressInput;
import com.cmall.familyhas.api.input.ApiImportOrderInput;
import com.cmall.familyhas.api.result.ApiImportOrderResult;
import com.cmall.familyhas.service.ManageAddressService;
import com.cmall.systemcenter.bill.HexUtil;
import com.cmall.systemcenter.bill.MD5Util;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webfactory.UserFactory;

public abstract class BaseImportOrder extends RootApi<ApiImportOrderResult, ApiImportOrderInput> {

	/**
	 * @descriptions 读取文件流，输出百度外卖订单信息数据集合
	 * 
	 * @param file
	 *            文件流
	 * @param type
	 *            excel后缀
	 * @return 百度外卖订单信息数据集合
	 * @author zhy
	 * @date 2016年5月13日-下午5:08:00
	 */
	public static ApiImportOrderResult readData(InputStream file, String type, ApiImportOrderInput inputParam) {
		ApiImportOrderResult result = new ApiImportOrderResult();
		List<Map<String, Object>> list = null;
		Workbook wb = null;
		try {
			if (type == "xlsx" || "xlsx".equals(type)) {
				wb = new XSSFWorkbook(file);
			} else {
				wb = new HSSFWorkbook(file);
			}
			// 第一个工作表;
			Sheet sheet = wb.getSheetAt(0);

			if (!StringUtils.equals(inputParam.getOrderSource(), "449715190012")
					&& !StringUtils.equals(inputParam.getOrderSource(), "449715190013")
					&& !StringUtils.equals(inputParam.getOrderSource(), "4497151900140001")) {
				if (wb.getNumberOfSheets() == 2) {
					boolean equalsSource = equalsSouce(inputParam.getOrderSource(), wb.getSheetAt(1));
					if (!equalsSource) {
						result.setResultCode(-1);
						result.setResultMessage("excel文档与所选来源不匹配，请重新上传或下载最新模板编辑");
						return result;
					}
				} else {
					result.setResultCode(-1);
					result.setResultMessage("excel文档错误，请重新下载模板文件编写");
					return result;
				}
			}
			// 创建集合
			list = new ArrayList<Map<String, Object>>();
			// 工作表的起始行编号
			int firstRowIndex = sheet.getFirstRowNum();
			// 工作表的结束行编号
			int lastRowIndex = sheet.getLastRowNum();
			// 遍历数据表数据
			for (int i = firstRowIndex + 2; i <= lastRowIndex; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
						// 订单编号
						String order_number = getCellVal(row.getCell(0));
						/**
						 * 判断如果订单编号有="002112321" 去除多余的=",";
						 */
						order_number = StringUtils.replace(order_number, "=", "").replace("\"", "");
						map.put("order_number", order_number);
						// 商品编码
						map.put("product_code", getCellVal(row.getCell(1)));
						// 商品sku编号
						map.put("sku_code", getCellVal(row.getCell(2)));
						// 商品sku名称
						map.put("sku_name", getCellVal(row.getCell(3)));
						// 商品数量
						String product_sum = "0";
						if (row.getCell(4) != null && !"".equals(row.getCell(4).toString())) {
							product_sum = getCellVal(row.getCell(4));
						}
						map.put("product_sum", product_sum);

						String product_money = "0";
						// 商品金额
						if (row.getCell(5) != null && !"".equals(row.getCell(5).toString())) {
							product_money = getCellVal(row.getCell(5));
						}
						map.put("product_money", product_money);
						// 商品总金额=商品数量*商品金额
						String product_total_money = "0";
						if (StringUtils.isNotBlank(product_sum) && StringUtils.isNotBlank(product_money)) {
							BigDecimal product_total_money_ = BigDecimal
									.valueOf(Double.parseDouble(getCellVal(row.getCell(4))))
									.multiply(BigDecimal.valueOf(Double.parseDouble(getCellVal(row.getCell(5)))));
							product_total_money = product_total_money_.toString();
						}
						map.put("product_total_money", product_total_money);
						// 用户名称
						map.put("buyer_name", getCellVal(row.getCell(6)));
						// 手机号
						map.put("buyer_mobile", getCellVal(row.getCell(7)).trim());
						// 地区编码
						map.put("area_code", getCellVal(row.getCell(8)));
						// 详细地址
						map.put("address", getCellVal(row.getCell(9)));
						// 备注
						map.put("remark", getCellVal(row.getCell(10)));
						// 支付流水
						map.put("pay_sequenceid", getCellVal(row.getCell(16)));	
						
						// 下单时间
						/**
						 * 新增判断如果是时间格式，转换为系统默认的时间格式存储，如果不是存储字符串
						 */
						Cell createTimeCell = row.getCell(11);
						String order_create_time = "";
						try {
							order_create_time = DateUtil.toString(createTimeCell.getDateCellValue(),
									DateUtil.DATE_FORMAT_DATETIME);
						} catch (Exception e) {
							order_create_time = getCellVal(row.getCell(11));
						}
						map.put("order_create_time", order_create_time);

						// 是否开发票 1 开， 0 不开 2016-06-01 zhy
						if (StringUtils.equals(getCellVal(row.getCell(12)), "是")) {
							// 开具发票
							map.put("is_invoice", "449746250001");
							// 发票抬头
							map.put("invoice_title", getCellVal(row.getCell(13)));
							// 发票明细
							map.put("invoice_content", getCellVal(row.getCell(14)));
						} else {
							// 开具发票
							map.put("is_invoice", "449746250002");
							// 发票抬头
							map.put("invoice_title", "");
							// 发票明细
							map.put("invoice_content", "");
						}
						// 运费
						if (getCellVal(row.getCell(15)) != null && !"".equals(getCellVal(row.getCell(15)))) {
							map.put("freight", BigDecimal.valueOf(Double.parseDouble(getCellVal(row.getCell(15)))));
						} else {
							map.put("freight", 0);
						}
						// 导入数据创建人
						String create_user = UserFactory.INSTANCE.create().getLoginName();
						map.put("create_user", create_user);
						// 创建时间
						map.put("create_time", DateUtil.getSysDateTimeString());
						String update_user = UserFactory.INSTANCE.create().getLoginName();
						map.put("update_user", update_user);
						// 创建时间
						map.put("update_time", DateUtil.getSysDateTimeString());

						/**
						 * 百度，民生商城，电视宝直接读取传入参数
						 */
						if (StringUtils.equals(inputParam.getOrderSource(), "449715190012")
								|| StringUtils.equals(inputParam.getOrderSource(), "449715190013")
								|| StringUtils.equals(inputParam.getOrderSource(), "4497151900140001")) {
							// 订单来源
							map.put("order_source", inputParam.getOrderSource());
							// 订单类型
							map.put("order_type", inputParam.getOrderType());
							// 订单渠道
							map.put("order_channel", inputParam.getOrderChannel());
							// 支付方式
							map.put("pay_type", inputParam.getPayType());
						} else {
							/**
							 * 读取订单导入配置信息获取订单来源，订单类型，订单渠道，支付方式
							 */
							MDataMap define = getDefine(inputParam.getOrderSource());
							// 订单来源
							map.put("order_source", define.get("order_source"));
							// 订单类型
							map.put("order_type", define.get("order_type"));
							// 订单渠道
							map.put("order_channel", define.get("order_channel"));
							// 支付方式
							map.put("pay_type", define.get("pay_type"));
						}

						list.add(map);
					}
				}
			}
			if (list != null && list.size() > 0) {
				result.setList(list);
			} else {
				result.setResultCode(-1);
				result.setResultMessage("读取列表为空");
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.setResultCode(-1);
			result.setResultMessage("读取列表错误，错误原因：" + e.getMessage());
		}
		/**
		 * 如果导入订单信息不为空，进行订单排序
		 */
		if (list != null && list.size() > 0) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> m1, Map<String, Object> m2) {
					int buyer_mobile = m1.get("buyer_mobile").toString().compareTo(m2.get("buyer_mobile").toString());
					int order_number = m1.get("order_number").toString().compareTo(m2.get("order_number").toString());
					int product_code = m1.get("product_code").toString().compareTo(m2.get("product_code").toString());
					int sku_code = m1.get("sku_code").toString().compareTo(m2.get("sku_code").toString());
					if (buyer_mobile == 0) {
						/**
						 * 排序规则：如果外部订单编号相同，根据商品编码排序；如果商品编码相同，根据商品sku编码排序
						 */
						if (order_number == 0) {
							if (product_code == 0) {
								return sku_code;
							}
							return product_code;
						}
						return order_number;
					}
					return buyer_mobile;
				}
			});
		}
		return result;
	}

	/**
	 * @descriptions 判断表格中数据类型将类型转换为字符串类型
	 * 
	 * @param cell
	 * @return
	 * 
	 * @author zhy
	 * @date 2016年5月13日-下午6:32:49
	 */
	public static String getCellVal(Cell cell) {
		if (cell != null) {
			// excel表格中数据为字符串
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue() != null ? cell.getStringCellValue() : "";
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				BigDecimal big = new BigDecimal(cell.getNumericCellValue());
				return big.toString();
			}
		}
		return "";
	}

	/**
	 * 
	 * 方法: verifyImporOrderOrderNumber <br>
	 * 描述: 验证外部导入订单编号是否为空 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月21日 下午3:19:53
	 * 
	 * @param list
	 * @return
	 */
	public static RootResult beforeVerifyImporOrderList(List<Map<String, Object>> list) {
		RootResult result = new RootResult();
		StringBuffer error = new StringBuffer();
		for (Map<String, Object> map : list) {
			if (StringUtils.isBlank(map.get("order_number").toString())) {
				error.append("sku编码为" + map.get("sku_code") + "的订单编码为空，请检查导入订单中的订单编号是否为文本格式<br>");
			} else if (StringUtils.isBlank(map.get("product_code").toString())) {
				error.append("订单编码为" + map.get("order_number") + "的商品编码为空，请检查导入订单中的商品编码是否正确<br>");
			} else if (StringUtils.isBlank(map.get("sku_code").toString())) {
				error.append("订单编码为" + map.get("order_number") + "的商品SKU编码为空，请检查导入订单中的商品SKU编码是否正确<br>");
			} else if (StringUtils.isNotBlank(map.get("product_code").toString())
					&& StringUtils.isNotBlank(map.get("sku_code").toString())) {
				MDataMap sku = new MDataMap();
				sku.put("sku_code", map.get("sku_code").toString());
				sku.put("product_code", map.get("product_code").toString());
				Map<String, Object> skuInfo = DbUp.upTable("pc_skuinfo").dataSqlOne(
						"select zid from pc_skuinfo where sku_code=:sku_code and product_code=:product_code", sku);
				if (skuInfo == null) {
					error.append("订单编码为" + map.get("order_number") + "商品编码为" + map.get("product_code") + "，商品SKU编码为"
							+ map.get("sku_code") + "商品不存在，请检查导入订单中的商品编码和商品sku编码是否正确<br>");
				}
			} else if (DateUtil.toDate(map.get("order_create_time").toString()) == null) {
				error.append("订单编码为" + map.get("order_number") + "的时间格式不是文本格式，请检查导入订单中的时间格式是否为文本格式<br>");
			}
		}
		/**
		 * 验证导入订单列表是否有重复数据
		 */
		StringBuffer orderNumber = new StringBuffer();
		List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Map<String, Object> _map = new HashMap<String, Object>();
			_map.put("order_number", map.get("order_number"));
			_map.put("sku_code", map.get("sku_code"));
			if (_list.contains(_map)) {
				orderNumber.append("订单编码为").append(map.get("order_number")).append(",sku编码为")
						.append(map.get("sku_code")).append("，订单编码和sku编码重复<br>");
			} else {
				_list.add(_map);
			}
		}
		if (error.length() > 0) {
			result.setResultCode(-1);
			result.setResultMessage(error.toString());
		}
		return result;
	}

	/**
	 * 
	 * 方法: verifyImporOrderList <br>
	 * 描述: 验证集合数据是否在tableName已存在 <br>
	 * 忽略执行状态为执行中和执行成功的<br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午3:40:29
	 * 
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> verifyImporOrderList(List<Map<String, Object>> list, String tableName) {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Map<String, Object> flag = DbUp.upTable(tableName).dataSqlOne(
						"select order_number from ordercenter." + tableName
								+ " where  order_number=:order_number and order_source=:order_source and product_code=:product_code and sku_code=:sku_code and import_status in('4497479500010001','4497479500010002')",
						new MDataMap(map));
				if (flag == null) {
					// 错误日志
					String error = verifyImporOrder(map, getAreaMap(map.get("area_code")+""));
					if (StringUtils.isNotBlank(error)) {
						map.put("import_status", "4497479500010003");
					} else {
						map.put("import_status", "4497479500010001");
					}
					map.put("error", error);
					newList.add(map);
				}
			}
		}
		return newList;
	}

	/**
	 * 
	 * 方法: verifyData <br>
	 * 描述: 验证导入订单信息 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午3:32:34
	 * 
	 * @param map
	 * @param areaList
	 *            地址编码集合
	 * @return
	 */
	public static String verifyImporOrder(Map<String, Object> map, List<Map<String, Object>> areaList) {
		String error = "";
		// 验证商品编号是否为空
		if (map.get("product_code") == null || "".equals(map.get("product_code").toString())) {
			error = "商品编号不能为空，请填写商品编号";
			return error;
		}
		// 验证商品sku编号是否为空
		if (map.get("sku_code") == null || "".equals(map.get("sku_code").toString())) {
			error = "商品SKU编号不能为空，请填写商品SKU编号";
			return error;
		}

		// 验证商品是否存在
		if (StringUtils.isNotBlank(map.get("product_code").toString())
				&& StringUtils.isNotBlank(map.get("sku_code").toString())) {
			MDataMap sku = new MDataMap();
			sku.put("sku_code", map.get("sku_code").toString());
			sku.put("product_code", map.get("product_code").toString());
			Map<String, Object> skuInfo = DbUp.upTable("pc_skuinfo").dataSqlOne(
					"select zid from pc_skuinfo where sku_code=:sku_code and product_code=:product_code", sku);
			if (skuInfo == null) {
				error = "商品编号或商品SKU编号填写错误，商品不存在";
				return error;
			}
		}

		// 验证商品sku名称是否为空
		if (map.get("sku_name") == null || "".equals(map.get("sku_name").toString())) {
			error = "商品SKU名称不能为空，请填写商品sku名称";
			return error;
		}
		// 商品数量是否为整数
		if (map.get("product_sum") == null || "".equals(map.get("product_sum").toString())
				|| Integer.valueOf(map.get("product_sum").toString()) == 0) {
			error = "商品数量填写错误，请填写整数";
			return error;
		}
		// 单品金额是否为数字
		if (map.get("product_money") == null || "".equals(map.get("product_money").toString())
				|| Double.valueOf(map.get("product_money").toString()) == 0) {
			error = "商品的单品金额填写错误，请填写数字";
			return error;
		}
		// 用户名称是否为空
		if (map.get("buyer_name") == null || "".equals(map.get("buyer_name").toString())) {
			error = "用户名称不能为空";
			return error;
		}
		// 手机号是否正确
		if (map.get("buyer_mobile") == null || "".equals(map.get("buyer_mobile").toString())) {
			error = "用户手机号不能为空";
			return error;
		} else {
			String mobile = map.get("buyer_mobile").toString();
			String regx = "^1[0-9]{10}$";
			Pattern p = Pattern.compile(regx);
			Matcher m = p.matcher(mobile);
			if (!m.matches()) {
				error = "用户手机号格式错误";
				return error;
			}
		}
		// 地区编码是否存在
		if (map.get("area_code") == null || "".equals(map.get("area_code").toString())) {
			error = "地区编码不能为空，请填写地区编码";
			return error;
		} else {
			boolean flag = false;
			for (Map<String, Object> area : areaList) {
				if (StringUtils.equals(map.get("area_code").toString(), area.get("area_code").toString())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				error = "地区编码填写错误";
				return error;
			}
		}
		// 详细地址不得超过60个字符 2016-05-31
		if (map.get("address") == null || "".equals(map.get("address").toString())) {
			error = "详细地址不能为空,请重新填写";
			return error;
		} else {
			if (map.get("address").toString().length() > 60) {
				error = "详细地址不得超过60个字符,请重新填写";
				return error;
			} else {
				// 如果收货地址不包含地址编码地区名称，提示错误信息 2016-07-21 zhy
				boolean flag = false;
				for (Map<String, Object> area : areaList) {
					if (map.get("address").toString().indexOf(area.get("name").toString()) >= 0) {
						flag = true;
						break;
					}
				}
				
				if (!flag) {
					error = "详细地址与地区编码所属区域不同，请重新填写";
					return error;
				}
			}
		}
		// 下单时间是否为空
		if (map.get("order_create_time") == null || "".equals(map.get("order_create_time").toString())) {
			error = "下单时间填写错误，请重新填写";
			return error;
		}
		return error;
	}

	/**
	 * 
	 * 方法: insertImportData <br>
	 * 描述: 添加数据到导入订单表 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 上午10:56:54
	 * 
	 * @param list
	 */
	public RootResult insertImportData(List<Map<String, Object>> list, String tableName) {
		RootResult result = new RootResult();
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					MDataMap data = new MDataMap(list.get(i));
					JSONObject member = registerBuyerCode(data.get("buyer_mobile").toString());
					if (member.getInteger("resultCode") == 1) {
						String buyer_code = member.getString("member_code");
						if (buyer_code != null && !"".equals(buyer_code)) {
							data.put("buyer_code", buyer_code);
						} else {
							result.setResultCode(0);
							result.setResultMessage(data.get("buyer_mobile") + ":" + "用户编号为空！");
							break;
						}
					} else {
						result.setResultCode(member.getIntValue("resultCode"));
						result.setResultMessage(data.get("buyer_mobile") + ":" + member.getString("resultMessage"));
						break;
					}
					/**
					 * 获取地址编码
					 */
					String address_code = getAddressCode(data);
					if (StringUtils.isBlank(address_code)) {
						result.setResultCode(0);
						result.setResultMessage(data.get("buyer_mobile") + ":" + "地址编码生成失败！");
						break;
					} else {
						data.put("address_code", address_code);
					}
					/**
					 * 判断导入订单是否已存在,如果存在更新订单信息，如果不存在添加订单信息
					 */
					MDataMap importOrder = DbUp.upTable(tableName).one("order_number", data.get("order_number"),
							"order_source", data.get("order_source"), "product_code", data.get("product_code"),
							"sku_code", data.get("sku_code"));
					if (importOrder != null) {
						data.put("zid", importOrder.get("zid"));
						data.put("uid", importOrder.get("uid"));
						data.put("update_user", UserFactory.INSTANCE.create().getLoginName());
						data.put("update_time", DateUtil.getSysDateTimeString());
						data.remove("create_user");
						data.remove("create_time");
						DbUp.upTable(tableName).update(data);
					} else {
						DbUp.upTable(tableName).dataInsert(data);
					}
				}
			}
			result.setResultCode(1);
			result.setResultMessage("导入成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(-1);
			result.setResultMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * 方法: groupImportOrderDataKey <br>
	 * 描述: 根据导入数据查询分组识别码订单编码和用户手机号 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午3:01:31
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Object> groupImportOrderDataKey(List<Map<String, Object>> list) {
		List<Map<String, Object>> keys = new ArrayList<Map<String, Object>>();
		StringBuffer orderNumbers = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i > 0) {
					Map<String, Object> m1 = list.get(i - 1);
					Map<String, Object> m2 = list.get(i);
					int order_number = m1.get("order_number").toString().compareTo(m2.get("order_number").toString());
					if (order_number != 0) {
						orderNumbers.append("'").append(m2.get("order_number")).append("',");
					}
					int buyer_mobile = m1.get("buyer_mobile").toString().compareTo(m2.get("buyer_mobile").toString());
					int order_source = m1.get("order_source").toString().compareTo(m2.get("order_source").toString());
					if (buyer_mobile == 0 && order_source == 0) {
						continue;
					} else {
						if (!keys.contains(keys)) {
							Map<String, Object> key = new HashMap<String, Object>();
							key.put("buyer_mobile", m2.get("buyer_mobile"));
							key.put("order_source", m2.get("order_source"));
							keys.add(key);
						}
					}
				} else {
					Map<String, Object> m1 = list.get(i);
					Map<String, Object> key = new HashMap<String, Object>();
					key.put("buyer_mobile", m1.get("buyer_mobile"));
					key.put("order_source", m1.get("order_source"));
					keys.add(key);
					orderNumbers.append("'").append(m1.get("order_number")).append("',");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keys", keys);
		map.put("orderNumbers", orderNumbers);
		return map;
	}

	/**
	 * 
	 * 方法: beforeImportVerify <br>
	 * 描述: 导入前验证导入订单总金额 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午5:04:21
	 * 
	 * @param list
	 * @return
	 */
	public static ApiImportOrderResult beforeImportTotalMoney(List<Map<String, Object>> list) {
		ApiImportOrderResult result = new ApiImportOrderResult();
		if (list != null && list.size() > 0) {
			// 订单总金额验证
			double totalMoney = 0;
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					if (map != null) {
						double product_money = Double.valueOf(map.get("product_money").toString());
						int product_sum = Integer.valueOf(map.get("product_sum").toString());
						totalMoney += product_money * product_sum;
					}
				}
			}
			if (totalMoney > 100000) {
				result.setResultCode(-1);
				result.setResultMessage("导入订单总金额为" + totalMoney + "，最高限额为10万，请重新导入!");
			}
		}
		return result;
	}

	/**
	 * 注册用户，获取用户的用户编码
	 * 
	 * @date 2016-09-21 zhy
	 * @param mobile
	 * @return
	 */
	public JSONObject registerBuyerCode(String mobile) {
		JSONObject response = null;
		try {
			JSONObject obj = new JSONObject();
			obj.put("loginName", mobile);
			obj.put("loginPass", RandomStringUtils.randomNumeric(8));
			obj.put("version", "1");
			String responseStr = getHttps(obj.toJSONString());
			response = JSONObject.parseObject(responseStr);
			if (response.getInteger("resultCode") == 1 && response.getString("memberCode") != null
					&& !"".equals(response.getString("memberCode"))) {
				response.put("member_code", response.getString("memberCode"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 
	 * 方法: getHttps <br>
	 * 描述: 获取url访问地址 <br>
	 * 作者: 张海宇 zhanghaiyu@huijiayou.cn<br>
	 * 时间: 2016年9月1日 下午1:39:04
	 * 
	 * @param sUrl
	 * @param sRequestString
	 * @return
	 * @throws Exception
	 */
	public String getHttps(String sRequestString) throws Exception {

		MDataMap mrequest = getsignMap(sRequestString);
		String url = bConfig("groupcenter.chcekedUserInfo_api_url");
		String sResponseString = WebClientSupport.upPost(url, mrequest);
		return sResponseString;
	}

	public MDataMap getsignMap(String requestStr) {
		MDataMap dataMap = new MDataMap();
		dataMap.put("api_target", bConfig("groupcenter.checkedUserInfo_api_target"));
		dataMap.put("api_key", bConfig("groupcenter.checkedUserInfo_api_key"));
		dataMap.put("api_input", requestStr);
		dataMap.put("api_timespan", DateUtil.getSysDateTimeString());
		dataMap.put("api_project", bConfig("groupcenter.checkedUserInfo_api_project"));
		StringBuffer str = new StringBuffer();
		str.append(dataMap.get("api_target")).append(dataMap.get("api_key")).append(dataMap.get("api_input"))
				.append(dataMap.get("api_timespan")).append(bConfig("groupcenter.checkedUserInfo_api_pass"));
		dataMap.put("api_secret", HexUtil.toHexString(MD5Util.md5(str.toString())));
		return dataMap;
	}

	/**
	 * 
	 * 方法: getAddressCode <br>
	 * 描述: 获取用户地址表nc_address中的用户编号address_code <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午5:16:09
	 * 
	 * @param map
	 * @return
	 */
	public static String getAddressCode(MDataMap map) {
		String address_code = "";
		String address = map.get("address");
		
		// 保存收货地址时把详细地址中的省市区替换掉
		String name = (String)getAreaMap(map.get("area_code")).get(0).get("name");
		int index = address.indexOf(name);
		if(index >= 0 && address.length() > name.length()) {
			address = address.substring(index+name.length());
		}
		
		// 根据地址信息查询地址是否已存在nc_address表中
		// 添加地址查询，由根据详细信息查询改为根据详细信息及手机号查询 2016-06-01 zhy
		Map<String, Object> addressMap = DbUp.upTable("nc_address").dataSqlOne(
				"select address_id from nc_address where address_street=:address_street and address_mobile=:address_mobile and address_code = :address_code and address_name = :address_name and area_code = :area_code",
				new MDataMap("address_street", address, "address_mobile", map.get("buyer_mobile"), "address_code",map.get("buyer_code"),"address_name",map.get("buyer_name"),"area_code",map.get("area_code")));
		// 如果存在直接获取nc_address的address_id字段
		if (addressMap != null) {
			address_code = addressMap.get("address_id").toString();
		} else {
			// 根据手机号查询是否存在nc_address的地址信息大于20条
			Map<String, Object> addressSum = DbUp.upTable("nc_address").dataSqlOne(
					"select count(1) AS address_sum from nc_address where address_code=:address_code",
					new MDataMap("address_code", map.get("buyer_code")));
			// 如果大于20，删除最早的一条信息
			Integer sum = Integer.valueOf(addressSum.get("address_sum").toString());
			if (sum > 20) {
				// 删除最早的一条地址信息
				DbUp.upTable("nc_address").dataDelete("address_code=:address_code ORDER BY update_time ASC LIMIT 1",
						new MDataMap("address_code", map.get("buyer_code")), "address_code");
			}
			// 如果不存在添加新的地址信息到nc_address
			ManageAddressService ms = new ManageAddressService();
			AddressInput input = new AddressInput();
			// 收货人
			input.setReceive_person(map.get("buyer_name"));
			// 地区编码
			input.setArea_code(map.get("area_code"));
			// 详细地址
			input.setAddress(address);
			// 电话
			input.setMobilephone(map.get("buyer_mobile"));
			
			// 标记：1:默认, 0不默认
			// input.setFlag_default("0");
			RootResultWeb webresult = ms.saveAddress(input, "SI2003", map.get("buyer_code"));// 保存收货地址
			if (webresult.getResultCode() == 1) {
				address_code = webresult.getResultMessage();
				// 查询nc_address的用户编号address_code
//				Map<String, Object> result = DbUp.upTable("nc_address").upTemplate().queryForMap(
//						"select address_id from nc_address where address_code=:address_code order by update_time desc limit 1",
//						new MDataMap("address_code", map.get("buyer_code")));
//				if (result != null && !"".equals(result)) {
//					address_code = result.get("address_id").toString();
//				}
			}
		}
		return address_code;
	}

	/**
	 * 
	 * 方法: equalsSouce <br>
	 * 描述: 判断订单来源是否正确 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年4月12日 上午10:57:00
	 * 
	 * @param source
	 * @param sheet
	 * @return
	 */
	private static boolean equalsSouce(String source, Sheet sheet) {
		boolean flag = false;
		try {
			Cell cell = sheet.getRow(1999).getCell(199);
			if (StringUtils.equals(cell.getStringCellValue(), source)) {
				flag = true;
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	private static MDataMap getDefine(String code) {
		MDataMap map = DbUp.upTable("oc_import_define").oneWhere("order_source,order_type,order_channel,pay_type", "",
				"", "code", code);
		return map;
	}
	
	// 仅查询订单指定的区域信息，提交导入速度
	private static List<Map<String,Object>> getAreaMap(String areaCode) {
		String sSql = "SELECT t3.code AS area_code, CONCAT(ifnull(t1.name, ''),IF(t2.show_yn != 'Y' OR t2.`name` is null,'',t2.name),IF(t3.show_yn != 'Y' OR t3.`name` is null,'',t3.name)) name"+
					" FROM sc_tmp t3"+
					" LEFT JOIN sc_tmp t2 ON t3.p_code = t2.code"+
					" LEFT JOIN sc_tmp t1 ON t2.p_code = t1.code"+
					" WHERE (t3.code_lvl = 3) and t3.code =:code"+
					
					" UNION ALL"+
					
					" SELECT t4.code AS area_code, CONCAT(ifnull(t1.name, ''),IF(t2.show_yn != 'Y' OR t2.`name` is null,'',t2.name),IF(t3.show_yn != 'Y' OR t3.`name` is null,'',t3.name),IF(t4.show_yn != 'Y' OR t4.`name` is null,'',t4.name)) name"+
					" FROM sc_tmp t4"+
					" LEFT JOIN sc_tmp t3 ON t4.p_code = t3.code"+
					" LEFT JOIN sc_tmp t2 ON t3.p_code = t2.code"+
					" LEFT JOIN sc_tmp t1 ON t2.p_code = t1.code"+
					" WHERE (t4.code_lvl = 4) and t4.code =:code";
		return DbUp.upTable("v_sc_gov_area3").dataSqlList(sSql, new MDataMap("code",areaCode));
	}
}
