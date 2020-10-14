package com.cmall.familyhas.api;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.APIBDWMExportOrderInput;
import com.cmall.familyhas.api.input.AddressInput;
import com.cmall.familyhas.api.result.APIBDWMExportOrderResult;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.service.ManageAddressService;
import com.cmall.systemcenter.bill.HexUtil;
import com.cmall.systemcenter.bill.MD5Util;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 导入百度外卖订单信息api
 * 
 * @author zhy
 * @date 2016-05-13
 *
 */
public class APIBDWMExportOrder extends RootApi<APIBDWMExportOrderResult, APIBDWMExportOrderInput> {

	@Override
	public APIBDWMExportOrderResult Process(APIBDWMExportOrderInput inputParam, MDataMap mRequestMap) {

		APIBDWMExportOrderResult result = new APIBDWMExportOrderResult();
		// 判断导入文件是否成功
		String fileRemoteUrl = inputParam.getUpload_show();
		String excelType = fileRemoteUrl.substring(fileRemoteUrl.lastIndexOf(".") + 1, fileRemoteUrl.length());
		if (!StringUtils.isEmpty(fileRemoteUrl)) {
			String lock = null;
			// 读取文件中的数据
			java.net.URL resourceUrl;
			InputStream instream = null;
			List<Map<String, Object>> list = null;
			try {
				lock = WebHelper.addLock(60 * 60 * 3, "import_bdwm_order");
				if (StringUtils.isBlank(lock)) {
					result.setResultCode(-1);
					result.setResultMessage("正在执行导入订单操作");
				} else {
					resourceUrl = new java.net.URL(fileRemoteUrl);
					instream = (InputStream) resourceUrl.getContent();
					list = readData(instream, excelType);
					StringBuffer repeateOrder = new StringBuffer();
					if (list != null && list.size() > 0) {
						result = beforeImportVerify(list);
						if (result.getResultCode() == 1) {
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> map = list.get(i);
								if (map != null) {
									// 获取用户编号
									JSONObject member = registerBuyerCode(map.get("buyer_mobile").toString());
									if (member.getInteger("resultCode") == 1) {

										String buyer_code = member.getString("member_code");
										if (buyer_code != null && !"".equals(buyer_code)) {
											map.put("buyer_code", buyer_code);
											String repeat_order_num = insertOrderInfoData(map);
											if (repeat_order_num != null && !"".equals(repeat_order_num)) {
												repeateOrder.append(repeat_order_num + "  ");
											}
										} else {
											result.setResultCode(0);
											result.setResultMessage("用户编号为空！");
											break;
										}
									} else {
										result.setResultCode(member.getIntValue("resultCode"));
										result.setResultMessage(member.getString("resultMessage"));
										break;
									}
								} else {
									result.setResultCode(0);
									result.setResultMessage("导入失败，失败原因：导入文件数据错误！");
									break;
								}
							}
							if (repeateOrder != null && repeateOrder.length() > 0) {
								result.setResultMessage(repeateOrder.toString());
							}
						}
					} else {
						result.setResultCode(0);
						result.setResultMessage("导入失败，失败原因：导入文件数据错误！");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setResultCode(0);
				result.setResultMessage("导入失败，失败原因：文件读取失败！");
			} finally {
				if (StringUtils.isNotBlank(lock)) {
					WebHelper.unLock(lock);
				}
			}
		} else {
			result.setResultCode(0);
			result.setResultMessage("导入失败，失败原因：文件不存在！");
		}
		return result;
	}

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
	private static List<Map<String, Object>> readData(InputStream file, String type) {
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
			// 创建集合
			list = new ArrayList<Map<String, Object>>();
			// 工作表的起始行编号
			int firstRowIndex = sheet.getFirstRowNum();
			// 工作表的结束行编号
			int lastRowIndex = sheet.getLastRowNum();
			// 遍历数据表数据
			for (int i = firstRowIndex + 1; i <= lastRowIndex; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					if (row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
						// 订单编号
						map.put("order_number", getCellVal(row.getCell(0)));
						// 商品sku编号
						map.put("product_code", getCellVal(row.getCell(1)));
						// 商品数量
						if (row.getCell(2) != null && !"".equals(row.getCell(2).toString())) {
							map.put("product_sum", getCellVal(row.getCell(2)));
						} else {
							map.put("product_sum", 0);
						}
						// 商品金额
						if (row.getCell(3) != null && !"".equals(row.getCell(3).toString())) {
							map.put("product_money", getCellVal(row.getCell(3)));
							if (getCellVal(row.getCell(2)) != null && !"".equals(getCellVal(row.getCell(2)))
									&& getCellVal(row.getCell(3)) != null && !"".equals(getCellVal(row.getCell(3)))) {
								// 商品总金额
								BigDecimal product_total_money = BigDecimal
										.valueOf(Double.parseDouble(getCellVal(row.getCell(2))))
										.multiply(BigDecimal.valueOf(Double.parseDouble(getCellVal(row.getCell(3)))));
								map.put("product_total_money", product_total_money.toString());
							}
						} else {
							map.put("product_money", 0);
							map.put("product_total_money", 0);
						}
						// 用户名称
						map.put("buyer_name", getCellVal(row.getCell(4)));
						// 手机号
						map.put("buyer_mobile", getCellVal(row.getCell(5)));
						// 地区编码
						map.put("area_code", getCellVal(row.getCell(6)));
						// 详细地址
						map.put("address", getCellVal(row.getCell(7)));
						// 备注
						map.put("remark", getCellVal(row.getCell(8)));
						// 下单时间
						map.put("order_create_time", getCellVal(row.getCell(9)));
						// 是否开发票 1 开， 0 不开 2016-06-01 zhy
						if (getCellVal(row.getCell(10)) == "是" || "是".equals(getCellVal(row.getCell(10)))) {
							// 开具发票
							map.put("is_invoice", "449746250001");
							// 发票抬头
							map.put("invoice_title", getCellVal(row.getCell(11)));
							// 发票明细
							map.put("invoice_content", getCellVal(row.getCell(12)));
						} else {
							// 开具发票
							map.put("is_invoice", "449746250002");
							// 发票抬头
							map.put("invoice_title", "");
							// 发票明细
							map.put("invoice_content", "");
						}
						// 运费
						if (getCellVal(row.getCell(13)) != null && !"".equals(getCellVal(row.getCell(13)))) {
							map.put("freight", BigDecimal.valueOf(Double.parseDouble(getCellVal(row.getCell(13)))));
						} else {
							map.put("freight", 0);
						}
						// 导入数据创建人
						String create_user = UserFactory.INSTANCE.create().getLoginName();
						map.put("create_user", create_user);
						// 创建时间
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						map.put("create_time", sdf.format(new Date()));
						String update_user = UserFactory.INSTANCE.create().getLoginName();
						map.put("update_user", update_user);
						// 创建时间
						map.put("update_time", sdf.format(new Date()));
						list.add(map);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @descriptions 整理存储到oc_orderinfo表中的数据集合
	 * 
	 * @param list
	 * @return
	 * 
	 * @author zhy
	 * @date 2016年5月13日-下午6:38:05
	 */
	private static String insertOrderInfoData(Map<String, Object> map) {
		String repeat_order_num = "";
		if (map != null) {
			// 根据订单编号查询是否已生成惠家有订单编号，如果生成不重复导入
			// 新添加查询条件product_code
			Map<String, Object> order = DbUp.upTable("oc_orderinfo_bdwm").dataSqlOne(
					"select oc_order_code from oc_orderinfo_bdwm where order_number=:order_number and product_code=:product_code and error=''",
					new MDataMap("order_number", map.get("order_number").toString(), "product_code",
							map.get("product_code").toString()));
			// 如果未生成惠家有订单，执行添加操作
			if (order == null || order.get("oc_order_code") == null
					|| "".equals(order.get("oc_order_code").toString())) {
				// 验证信息是否填写正确
				String error = "";
				if (verifyData(map) == null || "".equals(verifyData(map))) {
					// 添加地址到nc_address，读取nc_address的address_code
					if (map.get("address_code") == null || "".equals(map.get("address_code").toString())) {
						String address_code = getAddressCode(map);
						// 移除已存在的address_code
						map.remove("address_code");
						// 重新赋值
						map.put("address_code", address_code);
					}
					TeslaXOrder teslaXOrder = new TeslaXOrder();
					teslaXOrder.getStatus().setExecStep(ETeslaExec.Create);
					// 获取订单地址编码信息
					String address_code = getAddressCode(map);
					map.put("address_code", address_code);
					// ======基本信息
					// 订单基本信息
					// 外部订单编号 2016-06-01添加
					teslaXOrder.getOrderOther().setOut_order_code(map.get("order_number").toString());
					// 买家编号
					teslaXOrder.getUorderInfo().setBuyerCode(map.get("buyer_code").toString());
					// 收货人手机号
					teslaXOrder.getUorderInfo().setBuyerMobile(map.get("buyer_mobile").toString());
					teslaXOrder.getUorderInfo().setSellerCode("SI2003");
					// 订单类型：449715200015 百度外卖订单
					teslaXOrder.getUorderInfo().setOrderType("449715200015");
					// 订单来源：449715190012 百度外卖订单
					teslaXOrder.getUorderInfo().setOrderSource("449715190012");
					// 支付方式：449716200005：百度外卖代收货款
					teslaXOrder.getUorderInfo().setPayType("449716200005");
					// 交订单的app的版本
					teslaXOrder.getUorderInfo().setAppVersion("1.0.0");

					// ======商品信息

					TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
					// 商品编号
					// 根据产品编号查询商品编号
					MDataMap producParam = new MDataMap();
					producParam.put("sku_code", map.get("product_code").toString());
					// 查询商品信息
					PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(
							map.get("product_code").toString(), map.get("buyer_code").toString(), "", 0);
					orderDetail.setProductCode(map.get("product_code").toString());
					// 产品编号
					orderDetail.setSkuCode(plusModelSkuInfo.getSkuCode());
					// 产品单品金额
					orderDetail.setSkuPrice(plusModelSkuInfo.getSellPrice());
					orderDetail.setShowPrice(plusModelSkuInfo.getSellPrice());
					// 产品数量
					orderDetail.setSkuNum(Integer.parseInt(map.get("product_sum").toString()));
					teslaXOrder.getOrderDetails().add(orderDetail);

					// ======收货地址

					// 地址信息:收货地址
					teslaXOrder.getAddress().setAddress(map.get("address").toString());
					// 地址编号:收货人地址编号
					teslaXOrder.getAddress().setAddressCode(map.get("address_code").toString());
					// 地区编码:收货人地区编码
					teslaXOrder.getAddress().setAreaCode(map.get("area_code").toString());
					// 电话:收货人手机号
					teslaXOrder.getAddress().setMobilephone(map.get("buyer_mobile").toString());
					// 收货人:收货人姓名
					teslaXOrder.getAddress().setReceivePerson(map.get("buyer_name").toString());
					// 订单备注
					teslaXOrder.getAddress().setRemark(map.get("remark").toString());
					// ======发票
					// 是否开发票 1 开， 0 不开
					// 449746250001 是 449746250002 否
					if ("449746250001".equals(map.get("is_invoice").toString())) {
						teslaXOrder.getAddress().setFlagInvoice(1);
					} else {
						teslaXOrder.getAddress().setFlagInvoice(0);
					}
					// 发票抬头
					teslaXOrder.getAddress().setInvoiceTitle(map.get("invoice_title").toString());
					/*
					 * 发票类型 449746310001 普通发票 449746310002 增值税发票
					 */
					if ("449746250001".equals(map.get("is_invoice").toString())) {
						teslaXOrder.getAddress().setInvoiceType("449746310001");
					} else {
						teslaXOrder.getAddress().setInvoiceType("");
					}
					// 发票明细
					teslaXOrder.getAddress().setInvoiceContent(map.get("invoice_content").toString());
					// 渠道编号：百度外卖 449747430005
					teslaXOrder.setChannelId("449747430005");
					// 调用添加订单接口将输入添加到订单表oc_orderinfo
					// 执行创建订单
					TeslaXResult reTeslaXResult = new ApiConvertTeslaService().ConvertOrder(teslaXOrder);
					if (reTeslaXResult.upFlagTrue()) {
						// 如果添加到oc_orderinfo成功，读取订单code值，存入map
						// 由取大订单号修改为取小订单号 2016-07-21 zhy
						// map.put("oc_order_code",
						// teslaXOrder.getUorderInfo().getBigOrderCode());
						map.put("oc_order_code", teslaXOrder.getSorderInfo().get(0).getOrderCode());
						// 修改订单详情信息表，将详情表中的sku_price更新为百度导入订单的sku价格 2016-08-25
						// zhy
						MDataMap detail = new MDataMap();
						detail.put("order_code", teslaXOrder.getSorderInfo().get(0).getOrderCode());
						detail.put("sku_code", map.get("product_code").toString());
						detail.put("sku_price",
								BigDecimal.valueOf(Double.parseDouble(map.get("product_money").toString())).toString());
						detail.put("show_price",
								BigDecimal.valueOf(Double.parseDouble(map.get("product_money").toString()))
										.toEngineeringString());
						DbUp.upTable("oc_orderdetail").dataUpdate(detail, "sku_price,show_price",
								"order_code,sku_code");
					} else {
						// 如果失败获取失败原因，存入oc_orderinfo_bdwm
						error = JSONObject.toJSON(reTeslaXResult).toString();
					}
				} else {
					error = verifyData(map);
				}
				// 将错误信息添加到oc_orderinfo_bdwm的error
				map.put("error", error);
				// 根据订单编号查询是否在百度外卖订单表中已存在数据
				// 如果存在，更新数据信息，不存在执行添加操作
				Map<String, Object> orderInfo = DbUp.upTable("oc_orderinfo_bdwm").dataSqlOne(
						"select zid,uid from oc_orderinfo_bdwm where order_number=:order_number and product_code=:product_code",
						new MDataMap("order_number", map.get("order_number").toString(), "product_code",
								map.get("product_code").toString()));
				if (orderInfo != null) {
					map.remove("create_user");
					map.remove("create_time");
					map.put("zid", orderInfo.get("zid"));
					map.put("uid", orderInfo.get("uid"));
					DbUp.upTable("oc_orderinfo_bdwm").update(new MDataMap(map));
				} else {
					DbUp.upTable("oc_orderinfo_bdwm").dataInsert(new MDataMap(map));
				}
			} else {
				// 记录重复订单号
				repeat_order_num = map.get("order_number").toString();
			}
		}
		return repeat_order_num;
	}

	private JSONObject registerBuyerCode(String mobile) {
		// String code = "";
		JSONObject response = null;
		try {
			// ----- 修改用户获取接口 start 2016-09-01 zhy
			JSONObject obj = new JSONObject();
			obj.put("loginName", mobile);
			obj.put("loginPass", "000000");
			obj.put("version", "1");
			String responseStr = getHttps(obj.toJSONString());
			response = JSONObject.parseObject(responseStr);
			if (response.getInteger("resultCode") == 1 && response.getString("accessToken") != null
					&& !"".equals(response.getString("accessToken"))) {
				// Map<String, Object> map =
				// DbUp.upTable("za_oauth").dataSqlOne(
				// "select user_code from zapdata.za_oauth where
				// access_token=:access_token",
				// new MDataMap("access_token",
				// response.getString("accessToken")));
				// if (map != null && map.get("user_code") != null) {
				// code = map.get("user_code").toString();
				// response.put("member_code", code);
				// }
				response.put("member_code", response.getString("memberCode"));
			}
			// ----- end
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @descriptions 获取用户地址表nc_address中的用户编号address_code
	 * 
	 * @param map
	 * @return
	 * 
	 * @author zhy
	 * @date 2016年5月18日-上午10:49:05
	 */
	private static String getAddressCode(Map<String, Object> map) {
		String address_code = "";
		// 根据地址信息查询地址是否已存在nc_address表中
		// 添加地址查询，由根据详细信息查询改为根据详细信息及手机号查询 2016-06-01 zhy
		Map<String, Object> address = DbUp.upTable("nc_address").dataSqlOne(
				"select address_id from nc_address where address_street=:address_street and address_mobile=:address_mobile",
				new MDataMap("address_street", map.get("address").toString(), "address_mobile",
						map.get("buyer_mobile").toString()));
		// 如果存在直接获取nc_address的address_id字段
		if (address != null) {
			address_code = address.get("address_id").toString();
		} else {
			// 根据手机号查询是否存在nc_address的地址信息大于20条
			Map<String, Object> addressSum = DbUp.upTable("nc_address").dataSqlOne(
					"select count(1) AS address_sum from nc_address where address_mobile=:address_mobile",
					new MDataMap("address_mobile", map.get("buyer_mobile").toString()));
			// 如果大于20，删除最早的一条信息
			Integer sum = Integer.valueOf(addressSum.get("address_sum").toString());
			if (sum > 20) {
				// 删除最早的一条地址信息
				DbUp.upTable("nc_address").dataDelete("address_mobile=:address_mobile ORDER BY update_time ASC LIMIT 1",
						new MDataMap("address_mobile", map.get("buyer_mobile").toString()), "address_mobile");
			}
			// 如果不存在添加新的地址信息到nc_address
			ManageAddressService ms = new ManageAddressService();
			AddressInput input = new AddressInput();
			// 收货人
			input.setReceive_person(map.get("buyer_name").toString());
			// 地区编码
			input.setArea_code(map.get("area_code").toString());
			// 详细地址
			input.setAddress(map.get("address").toString());
			// 电话
			input.setMobilephone(map.get("buyer_mobile").toString());
			// 标记：1:默认, 0不默认
			// input.setFlag_default("0");
			RootResultWeb webresult = ms.saveAddress(input, "SI2003", map.get("buyer_code").toString());// 保存收货地址
			if (webresult.getResultCode() == 1) {
				// 查询nc_address的用户编号address_code
				Map<String, Object> result = DbUp.upTable("nc_address").dataSqlOne(
						"select address_id from nc_address where address_mobile=:address_mobile order by update_time desc",
						new MDataMap("address_mobile", map.get("buyer_mobile").toString()));
				if (result != null && !"".equals(result)) {
					address_code = result.get("address_id").toString();
				}
			}
		}
		return address_code;
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
	private static String getCellVal(Cell cell) {
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

	private static String verifyData(Map<String, Object> map) {
		String error = "";
		// 验证商品是否存在
		if (map.get("product_code") == null || "".equals(map.get("product_code").toString())) {
			error = "SKU编码不能为空，请填写商品编码";
			return error;
		} else {
			Map<String, Object> skuInfo = DbUp.upTable("pc_skuinfo").dataSqlOne(
					"select zid from pc_skuinfo where sku_code=:sku_code",
					new MDataMap("sku_code", map.get("product_code").toString()));
			if (skuInfo == null) {
				error = "SKU编码填写错误，商品不存在";
				return error;
			}
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
			Map<String, Object> areaInfo = DbUp.upTable("v_sc_gov_area3").dataSqlOne(
					"select area_code from systemcenter.v_sc_gov_area3 where area_code=:area_code",
					new MDataMap("area_code", map.get("area_code").toString()));
			if (areaInfo == null) {
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
				Map<String, Object> areaInfo = DbUp.upTable("v_sc_gov_area3").dataSqlOne(
						"select `name` from systemcenter.v_sc_gov_area3 where area_code=:area_code",
						new MDataMap("area_code", map.get("area_code").toString()));
				if (map.get("address").toString().indexOf(areaInfo.get("name").toString()) < 0) {
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
	 * 导入前验证
	 * 
	 * @param map
	 * @return
	 */
	private static APIBDWMExportOrderResult beforeImportVerify(List<Map<String, Object>> list) {
		APIBDWMExportOrderResult result = new APIBDWMExportOrderResult();
		if (list != null && list.size() > 0) {
			List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			StringBuffer sb = new StringBuffer();
			// 订单总金额验证
			double totalMoney = 0;
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					if (map != null) {
						double product_money = Double.valueOf(map.get("product_money").toString());
						int product_sum = Integer.valueOf(map.get("product_sum").toString());
						totalMoney += product_money * product_sum;
						// 如果新的集合中不包含map，存入到newList
						Map<String, Object> newMap = new HashMap<String, Object>();
						newMap.put("order_number", map.get("order_number"));
						newMap.put("product_code", map.get("product_code"));
						if (newList != null && newList.size() > 0) {
							if (newList.contains(newMap)) {
								sb.append(map.get("order_number")).append(",");
							}
						} else {
							newList.add(newMap);
						}
					}
				}
			}
			if (sb.length() > 0) {
				result.setResultCode(-1);
				result.setResultMessage("导入文件中" + sb.toString() + "订单编号和商品sku编号重复!");
			} else {
				if (totalMoney > 100000) {
					result.setResultCode(-1);
					result.setResultMessage("导入订单总金额为" + totalMoney + "，最高限额为10万，请重新导入!");
				}
			}
		}
		return result;
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
	private String getHttps(String sRequestString) throws Exception {

		MDataMap mrequest = getsignMap(sRequestString);

		String url = bConfig("groupcenter.chcekedUserInfo_api_url");

		String sResponseString = WebClientSupport.upPost(url, mrequest);
		return sResponseString;
	}

	private MDataMap getsignMap(String requestStr) {
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
}