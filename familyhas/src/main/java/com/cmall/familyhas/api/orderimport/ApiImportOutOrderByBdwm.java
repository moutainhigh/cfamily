package com.cmall.familyhas.api.orderimport;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiImportOrderInput;
import com.cmall.familyhas.api.result.ApiImportOrderResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 
 * 类: ApiImportOutOrder <br>
 * 描述: 导入外部订单 <br>
 * 作者: zhy<br>
 * 时间: 2016年12月20日 上午11:37:31
 */
public class ApiImportOutOrderByBdwm extends BaseImportOrder {
	@SuppressWarnings("unchecked")
	@Override
	public ApiImportOrderResult Process(ApiImportOrderInput inputParam, MDataMap mRequestMap) {
		ApiImportOrderResult result = new ApiImportOrderResult();
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
				lock = WebHelper.addLock(60 * 60 * 3, "import_order_bdwm");
				if (StringUtils.isBlank(lock)) {
					result.setResultCode(-1);
					result.setResultMessage("正在执行导入订单操作");
				} else {
					resourceUrl = new java.net.URL(fileRemoteUrl);
					instream = (InputStream) resourceUrl.getContent();
					result = readData(instream, excelType, inputParam);
					if (result.getResultCode() == 1) {
						list = result.getList();
						/**
						 * 验证外部导入订单编码是否为空
						 */
						RootResult verifyOrderNumberResult = beforeVerifyImporOrderList(list);
						if (verifyOrderNumberResult.getResultCode() == 1) {
							/**
							 * 验证总金额是否超过10万
							 */
							result = beforeImportTotalMoney(list);
							if (result.getResultCode() == 1) {
								if (list != null && list.size() > 0) {
									list = verifyImporOrderList(list, "oc_orderinfo_bdwm");
									if (list != null && list.size() > 0) {

										/**
										 * 添加导入数据到导入订单表
										 */
										RootResult insertOutOrderResult = insertImportData(list, "oc_orderinfo_bdwm");
										if (insertOutOrderResult.getResultCode() == 1) {
											/**
											 * 分组查询导入订单
											 */
											Map<String, Object> keyMap = groupImportOrderDataKey(list);
											List<Map<String, Object>> keys = (List<Map<String, Object>>) keyMap
													.get("keys");
											String orderNumbers = keyMap.get("orderNumbers").toString().substring(0,
													keyMap.get("orderNumbers").toString().lastIndexOf(","));
											if (keys != null && keys.size() > 0) {
												/**
												 * 开启线程池
												 */
												ExecutorService fixedThreadPool = null;
												try {
													fixedThreadPool = Executors.newFixedThreadPool(50);
													for (int i = 0; i < keys.size(); i++) {
														Map<String, Object> key = keys.get(i);
														MDataMap importOrder = new MDataMap(key);
														List<Map<String, Object>> importData = DbUp
																.upTable("oc_orderinfo_bdwm").dataSqlList(
																		"select * from oc_orderinfo_bdwm where import_status='4497479500010001' and order_source=:order_source and buyer_mobile=:buyer_mobile and order_number in("
																				+ orderNumbers + ")",
																		importOrder);
														if (importData != null) {
															fixedThreadPool
																	.execute(new ImportOutOrderByBdwm(importData));
														}
													}
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													/**
													 * 关闭线程池
													 */
													if (fixedThreadPool != null) {
														fixedThreadPool.shutdown();
													}
												}
											}
										}
									} else {
										result.setResultCode(-1);
										result.setResultMessage("订单重复或正在执行导入操作，请稍后查询订单列表信息");
									}
								} else {
									result.setResultCode(-1);
									result.setResultMessage("导入数据错误，请检查导入订单数据信息");
								}
							}
						} else {
							result.setResultCode(verifyOrderNumberResult.getResultCode());
							result.setResultMessage(verifyOrderNumberResult.getResultMessage());
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setResultCode(-1);
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
}