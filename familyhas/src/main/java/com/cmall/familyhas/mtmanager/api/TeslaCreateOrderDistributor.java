package com.cmall.familyhas.mtmanager.api;

import java.math.BigDecimal;
import java.util.List;

import com.cmall.familyhas.mtmanager.inputresult.APiCreateOrderDistributorInput;
import com.cmall.familyhas.mtmanager.inputresult.APiCreateOrderDistributorResult;
import com.cmall.familyhas.mtmanager.model.MTGood;
import com.cmall.familyhas.mtmanager.model.MTOrder;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.period.TeslaPeriodOrder;
import com.srnpr.xmasorder.period.TeslaPeriodOrderForDistributor;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 分销系统下单-MT系统接口
 * @author xiegj
 * 
 */
public class TeslaCreateOrderDistributor extends
		RootApiForManage<APiCreateOrderDistributorResult, APiCreateOrderDistributorInput> {

	public APiCreateOrderDistributorResult Process(APiCreateOrderDistributorInput inputParam,
			MDataMap mRequestMap) {

		APiCreateOrderDistributorResult result = new APiCreateOrderDistributorResult();
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.getStatus().setExecStep(ETeslaExec.Distributor);
		//先校验微公社余额
		// 应付款
		teslaXOrder.setCheck_pay_money(BigDecimal.valueOf(inputParam
				.getPay_money()));
		// 订单基本信息
		teslaXOrder.getUorderInfo().setBuyerCode(bConfig("groupcenter.MT_CODE"));
		teslaXOrder.getUorderInfo().setSellerCode(getManageCode());
		teslaXOrder.getUorderInfo().setOrderType("449715200011");
		teslaXOrder.getUorderInfo().setOrderSource("449715190009");
		teslaXOrder.getUorderInfo().setPayType("449716200001");
		teslaXOrder.getOrderOther().setCarriageFXMoney(BigDecimal.valueOf(inputParam.getFreight()));
		// 商品信息
		List<MTGood> infoForAdds = inputParam.getGoods();
		// 订单详细信息
		for (MTGood goodsInfo : infoForAdds) {
			TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
			orderDetail.setSkuCode(goodsInfo.getSku_code());
			orderDetail.setSkuNum(goodsInfo.getSku_num());
			teslaXOrder.getOrderDetails().add(orderDetail);
			teslaXOrder.getShowGoods().add(new TeslaModelShowGoods());
		}
		// 订单地址
		teslaXOrder.getAddress().setAddress(inputParam.getBuyer_address());
		teslaXOrder.getAddress().setAreaCode(inputParam.getBuyer_address_code());
		teslaXOrder.getAddress().setMobilephone(inputParam.getBuyer_mobile());
		teslaXOrder.getAddress().setReceivePerson(inputParam.getBuyer_name());
		teslaXOrder.getAddress().setRemark(inputParam.getRemark());
		teslaXOrder.getAddress().setInvoiceTitle(inputParam.getBillInfo().getBill_title());
		teslaXOrder.getAddress().setInvoiceType(inputParam.getBillInfo().getBill_Type());
		teslaXOrder.getAddress().setInvoiceContent(inputParam.getBillInfo().getBill_detail());
		// 用户支付相关
		//执行创建订单
		TeslaXResult reTeslaXResult = new TeslaPeriodOrderForDistributor().doRefresh(teslaXOrder);
		if (reTeslaXResult.upFlagTrue()) {
			for (int i = 0; i < teslaXOrder.getOrderDetails().size(); i++) {
				MTOrder order = new MTOrder();
				order.setOrder_code(teslaXOrder.getOrderDetails().get(i).getOrderCode());
				order.setProduct_code(teslaXOrder.getOrderDetails().get(i).getProductCode());
				order.setSku_code(teslaXOrder.getOrderDetails().get(i).getSkuCode());
				result.getOrder().add(order);
			}
		}else {
			result.setResultCode(reTeslaXResult.getResultCode());
			result.setResultMessage(reTeslaXResult.getResultMessage());
		}
		return result;
	}
}
