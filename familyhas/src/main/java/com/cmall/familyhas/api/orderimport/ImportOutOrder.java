package com.cmall.familyhas.api.orderimport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 类: ImportOutOrder <br>
 * 描述: 导入外部订单 <br>
 * 作者: zhy<br>
 * 时间: 2016年12月20日 上午10:53:43
 */
public class ImportOutOrder implements Runnable {

	/**
	 * 处理数据
	 */
	private List<Map<String, Object>> list;

	public ImportOutOrder(List<Map<String, Object>> list) {
		this.list = list;
	}

	@Override
	public void run() {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				MDataMap data = new MDataMap(list.get(i));
				createHjyOrder(data);
				try {
					Thread.sleep(12000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void createHjyOrder(MDataMap map) {
		String error = "";
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.getStatus().setExecStep(ETeslaExec.Create);
		// ======基本信息
		// 订单基本信息
		// 外部订单编号 2016-06-01添加
		teslaXOrder.getOrderOther().setOut_order_code(map.get("order_number"));
		// 买家编号
		teslaXOrder.getUorderInfo().setBuyerCode(map.get("buyer_code"));
		// 收货人手机号
		teslaXOrder.getUorderInfo().setBuyerMobile(map.get("buyer_mobile"));
		teslaXOrder.getUorderInfo().setSellerCode("SI2003");
		// 订单类型
		teslaXOrder.getUorderInfo().setOrderType(map.get("order_type"));
		// 订单来源
		teslaXOrder.getUorderInfo().setOrderSource(map.get("order_source"));
		// 支付方式
		teslaXOrder.getUorderInfo().setPayType(map.get("pay_type"));
		// 交订单的app的版本
		teslaXOrder.getUorderInfo().setAppVersion("1.0.0");

		// ======商品信息

		TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
		// 商品编号
		// 根据产品编号查询商品编号
		MDataMap producParam = new MDataMap();
		producParam.put("sku_code", map.get("sku_code").toString());
		// 查询商品信息
		PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(map.get("sku_code").toString(),
				map.get("buyer_code").toString(), "", 0);
		orderDetail.setProductCode(plusModelSkuInfo.getProductCode());
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
		// 渠道编号
		teslaXOrder.setChannelId(map.get("order_channel"));
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
			detail.put("sku_code", map.get("sku_code").toString());
			detail.put("sku_price",
					BigDecimal.valueOf(Double.parseDouble(map.get("product_money").toString())).toString());
			detail.put("show_price",
					BigDecimal.valueOf(Double.parseDouble(map.get("product_money").toString())).toString());
			//做结算成本判断处理2019-08-22
			Map<String, Object> newMap = DbUp.upTable("oc_import_define").dataSqlOne("select settlement_cost from oc_import_define where code=:code", new MDataMap("code",map.get("order_source").toString()));
		     if("44975014002".equals(newMap.get("settlement_cost"))){//当前成本价
				 String nowTime = DateUtil.getNowTime();
		    	 List<Map<String, Object>> resuList = DbUp.upTable("pc_skuprice_change_flow").dataSqlList("select * from pc_skuprice_change_flow where start_time<='"+nowTime+"' and end_time>'"+nowTime+"' and sku_code=:sku_code and status='4497172300130002' order by zid desc", new MDataMap("sku_code",map.get("sku_code").toString()));
		    	 if(resuList!=null&&resuList.size()>0) {//存在则取最近的数据
		    		 Map<String, Object> subMap = resuList.get(0);
		    		 detail.put("cost_price",subMap.get("cost_price").toString());	
		    	 }
		    	 else {//不存在取档案成本价
		    		 detail.put("cost_price", DbUp.upTable("pc_skuinfo").dataGet("cost_price", "", new MDataMap("sku_code", map.get("sku_code").toString())).toString());	
		    	 }
			} else {//档案成本价
				detail.put("cost_price", DbUp.upTable("pc_skuinfo").dataGet("cost_price", "", new MDataMap("sku_code", map.get("sku_code").toString())).toString());	
			}
		
			DbUp.upTable("oc_orderdetail").dataUpdate(detail, "sku_price,show_price,cost_price", "order_code,sku_code");
			map.put("import_status", "4497479500010002");
			
			// 淘宝店铺订单导入支付流水
			if(TopConfig.Instance.bConfig("cfamily.importOrderTaobao").equals(map.get("order_source"))) {
				MDataMap payMap = new MDataMap();
				payMap.put("order_code", detail.get("order_code"));
				payMap.put("pay_sequenceid", map.get("pay_sequenceid"));
				payMap.put("payed_money", new BigDecimal(map.get("product_total_money")).add(new BigDecimal(map.get("freight"))).toString());
				payMap.put("create_time", FormatHelper.upDateTime());
				payMap.put("pay_type", "449746280003");
				payMap.put("pay_remark", "");
				DbUp.upTable("oc_order_pay").dataInsert(payMap);
			}
		} else {
			// 如果失败获取失败原因，存入oc_orderinfo_import
			error = reTeslaXResult.getResultMessage();
		}
		// 将错误信息添加到oc_orderinfo_import的error
		// 如果错误日志不为空，修改执行状态为执行失败
		if (StringUtils.isNotBlank(error)) {
			map.put("import_status", "4497479500010003");
		}
		map.put("error", error);
		// 根据订单编号查询是否在百度外卖订单表中已存在数据
		// 如果存在，更新数据信息，不存在执行添加操作
		MDataMap updateParam = new MDataMap();
		updateParam.put("order_number", map.get("order_number").toString());
		updateParam.put("product_code", map.get("product_code").toString());
		updateParam.put("sku_code", map.get("sku_code").toString());
		updateParam.put("order_source", map.get("order_source").toString());
		String updateSql = "select zid,uid from oc_orderinfo_import where order_number=:order_number and product_code=:product_code and sku_code=:sku_code and order_source=:order_source";
		Map<String, Object> orderInfo = DbUp.upTable("oc_orderinfo_import").dataSqlOne(updateSql, updateParam);
		if (orderInfo != null) {
			MDataMap updateMap = map;
			updateMap.put("zid", orderInfo.get("zid").toString());
			updateMap.put("uid", orderInfo.get("uid").toString());
			updateMap.put("update_user", map.get("update_user"));
			updateMap.put("update_time", DateUtil.getSysDateTimeString());
			updateMap.remove("create_user");
			updateMap.remove("create_date");
			DbUp.upTable("oc_orderinfo_import").update(updateMap);
		}
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
}
