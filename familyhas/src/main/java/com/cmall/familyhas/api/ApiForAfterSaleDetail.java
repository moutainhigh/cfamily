package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForAfterSaleInfoInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.ApiForAfterSaleDetailResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.service.AfterSaleOrderForGetTime;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.util.HttpUtil;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 订单售后详情接口
 * @author cc
 *
 */
public class ApiForAfterSaleDetail extends RootApiForToken<ApiForAfterSaleDetailResult, ApiForAfterSaleInfoInput>{
	
	@Override
	public ApiForAfterSaleDetailResult Process(ApiForAfterSaleInfoInput inputParam, MDataMap mRequestMap) {
		ApiForAfterSaleDetailResult result = new ApiForAfterSaleDetailResult();
		String afterSaleCode = inputParam.getAfterCode();
		if(afterSaleCode.contains("CGS")) {//换货暂无
			result.setResultCode(0);
			result.setResultMessage("暂无售后信息。");
			return result;
		}
		if(afterSaleCode.contains("LD")||afterSaleCode.contains("P") || afterSaleCode.contains("A")) {
			result = this.ldAfterSaleDetail(inputParam, mRequestMap);
			return result;
		}
		if(afterSaleCode.contains("RGS")&&!afterSaleCode.contains("LD")) {
			result = this.appAfterSaleDetail(inputParam, mRequestMap);
			return result;
		}
		return result;
	}
	
	/**
	 * LD品售后走接口查。
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiForAfterSaleDetailResult ldAfterSaleDetail(ApiForAfterSaleInfoInput inputParam, MDataMap mRequestMap) {
		String url = bConfig("groupcenter.rsync_homehas_url")+"getAfterServiceDetail";
		ApiForAfterSaleDetailResult result = new ApiForAfterSaleDetailResult();
		String afterSaleCode = inputParam.getAfterCode();
		if(afterSaleCode.contains("RGS")) {//APP发起的申请售后。需要查询LD申请售后的单号是否已有，如果没有，则证明LD还未受理
			MDataMap afterSaleLD = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_app",afterSaleCode);
			if(afterSaleLD == null || afterSaleLD.isEmpty()) {//没有查询到数据
				result.setResultCode(0);
				result.setResultMessage("未查询到数据");
				return result;
			}
			afterSaleCode = afterSaleLD.get("after_sale_code_ld");
			if(StringUtils.isEmpty(afterSaleCode)) {
				result.setResultCode(0);
				result.setResultMessage("暂无售后信息。");
				return result;
			}
		}
		if(afterSaleCode.contains("A")||afterSaleCode.contains("P")) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("saleCodeLd", afterSaleCode);
			String response = HttpUtil.postJson(url,JSONArray.toJSONString(map));
			if(StringUtils.isEmpty(response)) {//返回结果为空
				result.setResultCode(0);
				result.setResultMessage("暂无售后信息。");
				return result;
			}
			JSONObject jo = JSONObject.parseObject(response);
			if(jo.getString("success").equals("false")) {
				result.setResultCode(0);
				result.setResultMessage("信息查询失败，请联系客服");
				return result;
			}
			String dataStr = jo.getString("result");
			if(StringUtils.isEmpty(dataStr)) {
				result.setResultCode(0);
				result.setResultMessage("信息查询失败，请联系管理员");
				return result;
			}
			JSONObject afterSaleObject = JSONObject.parseObject(dataStr);
			String sale_code_app = afterSaleObject.getString("SALE_CODE_APP");
			result.setAfterSaleCode(sale_code_app == null || "".equals(sale_code_app)?afterSaleObject.getString("SALE_CODE_LD"):afterSaleObject.getString("SALE_CODE_APP"));
			result.setAfterSaleReason(StringUtils.trimToEmpty(afterSaleObject.getString("RTN_REASON")));
			String afterSaleType = afterSaleObject.getString("AFTER_SALE_TYPE");
			if(afterSaleType == null ||afterSaleType.equals("T")) {
				result.setAfterSaleType("退货");
			}else {
				result.setAfterSaleType("换货");
			}
			result.setGoodsCount(afterSaleObject.getIntValue("GOOD_CNT"));
			String status = afterSaleObject.getString("AFTER_SALE_CD");
			String checkInfo = "";
			List<Button> list = new ArrayList<Button>();
			Button kefuCall = new Button();
			kefuCall.setButtonCode("4497477800080014");
			kefuCall.setButtonStatus(1);
			kefuCall.setButtonTitle("客服电话");
			Button cancel = new Button();
			cancel.setButtonCode("4497477800050011");
			cancel.setButtonStatus(1);
			cancel.setButtonTitle("取消申请");
			Button wuliu = new Button();
			wuliu.setButtonCode("4497477800080001");
			wuliu.setButtonStatus(1);
			wuliu.setButtonTitle("填写退换货物流信息");
			Button applyAgian = new Button();
			applyAgian.setButtonCode("4497477800080008");
			applyAgian.setButtonStatus(1);
			applyAgian.setButtonTitle("申请售后");
			if("01".equals(status)) {
				status = "申请已提交，等待审核";
				checkInfo = "您的申请已提交成功，等待售后审核";
				list.add(cancel);
			}else if("02".equals(status)) {
				status = "客服同意申请，退货中";
				checkInfo = "客服同意申请";
				list.add(kefuCall);
				list.add(cancel);
			}else if("03".equals(status)) {
				status = "客服关闭了申请";
				checkInfo = "";
				list.add(kefuCall);
				list.add(applyAgian);
			}else if("04".equals(status)) {
				status = "申请已取消";
				checkInfo = "您已取消了此次售后申请，若问题未解决，您可以在有效期内再次申请售后";
				list.add(kefuCall);
				list.add(applyAgian);
			}else if("05".equals(status)) {
				status = "退货成功";
				checkInfo = "";//TODO
				list.add(kefuCall);
			}else if("06".equals(status)) {
				status = "退货成功";
				checkInfo = "";//TODO
				list.add(kefuCall);
			}else if("09".equals(status)) {
				status = "办理退款中";
				checkInfo = "您的退货已经入库，我们正在为您办理退款，预计1-5个工作日，退还到您的支付账号";
				list.add(kefuCall);
			}else if("10".equals(status)) {
				status = "退款完成";
				checkInfo = "您的退款已退还到您的支付账号，请注意查收";
				list.add(kefuCall);
			}
			result.setButtons(list);
			result.setCheckInfo(checkInfo);
			result.setCheckStatus(status);
			result.setGoodsPrice(afterSaleObject.getBigDecimal("GOOD_PRC"));
			result.setIfGetGoods(afterSaleObject.getString("IS_RCV").equals("Y")?"是":"否");
			String orderCode = afterSaleObject.getString("ORD_ID");
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("out_order_code",orderCode);
			if(orderInfo != null && !orderInfo.isEmpty()) {
				orderCode = orderInfo.get("order_code");
			}
			result.setOrderCode(orderCode);
			result.setOrderSeq(afterSaleObject.getString("ORD_SEQ"));
			result.setReturnCZJ(afterSaleObject.getBigDecimal("RTN_PPC")!=null?afterSaleObject.getBigDecimal("RTN_PPC"):BigDecimal.ZERO);//储值金
			result.setReturnJF(afterSaleObject.getBigDecimal("RTN_ACCM")!=null?afterSaleObject.getBigDecimal("RTN_ACCM"):BigDecimal.ZERO);//积分
			result.setReturnMoney(afterSaleObject.getBigDecimal("RTN_MONEY")!=null?afterSaleObject.getBigDecimal("RTN_MONEY"):BigDecimal.ZERO);//金钱
			result.setReturnZCK(afterSaleObject.getBigDecimal("RTN_CRDT")!=null?afterSaleObject.getBigDecimal("RTN_CRDT"):BigDecimal.ZERO);//暂存款
			result.setReturnHjycoin(afterSaleObject.getBigDecimal("RTN_HCOIN")!=null?afterSaleObject.getBigDecimal("RTN_HCOIN"):BigDecimal.ZERO);//惠币
			String productCode = afterSaleObject.getString("GOOD_ID");
			String colorId = afterSaleObject.getString("COLOR_ID");
			String styleId = afterSaleObject.getString("STYLE_ID");
			String skuKey = "color_id="+colorId+"&style_id="+styleId;
			MDataMap skuInfo = DbUp.upTable("pc_skuinfo").one("product_code",productCode,"sku_key",skuKey);
			if(skuInfo != null && !skuInfo.isEmpty()) {
				result.setSkuCode(skuInfo.get("sku_code"));
				result.setGoodsPic(skuInfo.get("sku_picurl"));
				result.setIsChangeGoods("");
				result.setGoodsName(skuInfo.get("sku_name"));
				skuKey = skuInfo.get("sku_keyvalue");
			}
			List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService().sellerStandardAndStyle(skuKey); // 截取尺码和款型
			if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
				for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
					ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
					apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
					apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
					result.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
				}
			}
			result.setSupplyTime(DateUtil.toString(afterSaleObject.getLong("ETR_DATE"),DateUtil.DATE_FORMAT_DATETIME));
		}
		return result;
	}

	/**
	 * APP售后单
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiForAfterSaleDetailResult appAfterSaleDetail(ApiForAfterSaleInfoInput inputParam, MDataMap mRequestMap) {
		ApiForAfterSaleDetailResult result = new ApiForAfterSaleDetailResult();
		String afterSaleCode = inputParam.getAfterCode();
		MDataMap returnGoods = DbUp.upTable("oc_return_goods").one("return_code",afterSaleCode);
		MDataMap afterSale = DbUp.upTable("oc_order_after_sale").one("asale_code",afterSaleCode);
		MDataMap afterSaleDetail = DbUp.upTable("oc_order_after_sale_dtail").one("asale_code",afterSaleCode);
		String skuCode =  afterSaleDetail.get("sku_code");
		PlusModelSkuQuery tQuery = new PlusModelSkuQuery();
		tQuery.setCode(skuCode);
		PlusModelSkuInfo skuInfo = new LoadSkuInfo().upInfoByCode(tQuery);
		List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService().sellerStandardAndStyle(skuInfo.getSkuKeyvalue()); // 截取尺码和款型
		if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
			for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
				apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
				result.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
			}
		}
		String status = afterSale.get("asale_status");
		String checkInfo = "";
		List<Button> buttons = new ArrayList<Button>();
		Button kefuCall = new Button();
		kefuCall.setButtonCode("4497477800080014");
		kefuCall.setButtonStatus(1);
		kefuCall.setButtonTitle("客服电话");
		buttons.add(kefuCall);
		Button cancel = new Button();
		cancel.setButtonCode("4497477800050011");
		cancel.setButtonStatus(1);
		cancel.setButtonTitle("取消申请");
		Button wuliu = new Button();
		wuliu.setButtonCode("4497477800080001");
		wuliu.setButtonStatus(1);
		wuliu.setButtonTitle("填写退换货物流信息");
		Button applyAgian = new Button();
		applyAgian.setButtonCode("4497477800080008");
		applyAgian.setButtonStatus(1);
		applyAgian.setButtonTitle("申请售后");
		if("4497477800050001".equals(status)) {
			status = "退款中";
			checkInfo = "您的申请已经通过，我们正在为您办理退款，预计1~5个工作日，退还到您的支付账号";
		}
		if("4497477800050002".equals(status)) {
			status = "退款成功";
			checkInfo = "您的退款已退还到您的支付账号，请注意查收";
		}
		if("4497477800050003".equals(status)) {
			status = "等待审核";
			checkInfo = "您的申请已提交成功，等待售后审核中";
			buttons.add(cancel);
		}
		if("4497477800050004".equals(status)) {
			status = "客服关闭了申请";
			checkInfo = "客服关闭了您的售后申请，如有问题请联系客服";
			buttons.add(applyAgian);
		}
		if("4497477800050005".equals(status)) {
			if("4497153900050002".equals(returnGoods.get("status"))) {//商户否决审核
				status = "4497477800050014";
			}else {
				status = "退货中，待商家确认";
				checkInfo = "退货中，待商家审核通过";
				buttons.add(cancel);
			}
		}
		if("4497477800050010".equals(status)) {
			status = "客服同意申请，待完善物流";
			checkInfo = "您的申请已经通过，请按照客服提供的地址回寄，保存快递存单，并在7日内填写物流信息";
			buttons.add(wuliu);
			buttons.add(cancel);
			AfterSaleOrderForGetTime getTime = new AfterSaleOrderForGetTime();
			MDataMap deadLineMap = getTime.getDeadLineForFillShipments(afterSaleCode);
			String deadLine = deadLineMap.get("deadLine");
			result.setDeadLine(deadLine);
		}
		if("4497477800050011".equals(status)) {
			status = "申请已取消";
			checkInfo = "您已经取消了此次售后申请，若问题未解决您可以在有效期内再次申请售后";
			buttons.add(applyAgian);
		}
		if("4497477800050014".equals(status)) {
			status = "商户驳回，等待客服审核";
			checkInfo = "商户驳回了您的售后申请，正在等待客服审核";
			buttons.add(cancel);
		}
		result.setAfterSaleCode(afterSaleCode);
		result.setAfterSaleReason(returnGoods.get("return_reason"));
		result.setButtons(buttons);
		result.setCheckInfo(checkInfo);
		result.setCheckStatus(status);
		result.setGoodsCount(Integer.parseInt(afterSaleDetail.get("sku_num")));
		result.setGoodsName(skuInfo.getSkuName());
		result.setGoodsPic(skuInfo.getProductPicUrl());
		result.setGoodsPrice(new BigDecimal(afterSaleDetail.get("sku_price")));
		if("4497476900040001".equals(returnGoods.get("goods_receipt"))) {
			result.setIfGetGoods("是");//4497476900040001 是，4497476900040002 否
		}else if("4497476900040002".equals(returnGoods.get("goods_receipt"))){
			result.setIfGetGoods("否");
		}else {
			result.setIfGetGoods("否");
		}
		result.setOrderCode(returnGoods.get("order_code"));
		result.setResultCode(1);
		result.setResultMessage("");
		result.setReturnCZJ(new BigDecimal(returnGoods.get("expected_return_ppc_money")));
		result.setReturnJF(new BigDecimal(returnGoods.get("expected_return_accm_money")).multiply(new BigDecimal("200")));
		result.setReturnHjycoin(new BigDecimal(returnGoods.get("expected_return_hjycoin_money")));
		BigDecimal money = new BigDecimal(0);
		money = new BigDecimal(returnGoods.get("expected_return_money")).subtract(new BigDecimal(returnGoods.get("expected_return_crdt_money"))).subtract(new BigDecimal(returnGoods.get("expected_return_ppc_money"))).subtract(new BigDecimal(returnGoods.get("expected_return_accm_money"))).subtract(new BigDecimal(returnGoods.get("expected_return_hjycoin_money")));
		if(money.intValue() < 0) {
			money = new BigDecimal(0);
		}
		result.setReturnMoney(money);
		result.setReturnZCK(new BigDecimal(returnGoods.get("expected_return_crdt_money")));
		result.setSkuCode(afterSaleDetail.get("sku_code"));
		result.setSupplyTime(afterSale.get("create_time")!=null&&afterSale.get("create_time").length()>=19?afterSale.get("create_time").substring(0, 19):"");
		result.setOrderSeq("-1");//LD 拓展字段
		String afterSaleType = "";
		if("4497477800030001".equals(afterSale.get("asale_type"))) {
			afterSaleType = "退货退款";
		}
		if("4497477800030003".equals(afterSale.get("asale_type"))) {
			afterSaleType = "换货";
		}
		if("4497477800030002".equals(afterSale.get("asale_type"))) {
			afterSaleType = "仅退款";
		}
		if("4497477800030004".equals(afterSale.get("asale_type"))) {
			afterSaleType = "取消发货";
		}
		if("4497477800030005".equals(afterSale.get("asale_type"))) {
			afterSaleType = "拒收";
		}
		result.setAfterSaleType(afterSaleType);//售后类型：（4497477800030001 退货退款）（4497477800030003 换货）（4497477800030002 仅退款）（4497477800030004 取消发货）（4497477800030005 拒收）
		return result;
	}
	
}
