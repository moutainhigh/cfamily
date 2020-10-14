package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiCreateHmlOrderInput;
import com.cmall.familyhas.api.result.APiCreateHmlOrderResult;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.familyhas.service.ShopCartServiceForBeauty;
import com.cmall.groupcenter.groupdo.GroupConst;
import com.cmall.groupcenter.support.GroupReckonSupport;
import com.cmall.ordercenter.alipay.process.WechatProcessRequest;
import com.cmall.ordercenter.service.ApiAlipayMoveProcessService;
import com.cmall.ordercenter.service.OrderShoppingService;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠美丽—提交订单Api
 * 
 * @author houwen date: 2014-10-08
 * @version1.0
 */
public class APiCreateOrderForBeautiful extends
		RootApiForToken<APiCreateHmlOrderResult, APiCreateHmlOrderInput> {

	static String regEx = "[\u4e00-\u9fa5]";

	static Pattern pat = Pattern.compile(regEx);

	public APiCreateHmlOrderResult Process(APiCreateHmlOrderInput inputParam,
			MDataMap mRequestMap) {
		APiCreateHmlOrderResult result = new APiCreateHmlOrderResult();

		// 开始锁定用户
		String sLockString = WebHelper.addLock(8, getUserCode());

		if (StringUtils.isNotEmpty(sLockString)) {

			if (result.upFlagTrue()) {

				List<GoodsInfoForAdd> goodF = new ArrayList<GoodsInfoForAdd>();

				if (inputParam.getGoods() != null
						&& !inputParam.getGoods().isEmpty()) {
					// --ProductService pService = BeansHelper
					// --
					// .upBean("bean_com_cmall_productcenter_service_ProductService");
					// --String orderType = inputParam.getOrder_type(); // 订单类型
					// 449715200003试用订单、449715200004闪购订单、449715200005普通订单
					// -Map<String,Integer> skuMap = new HashMap<String,
					// Integer>();
					for (int i = 0; i < inputParam.getGoods().size(); i++) {
						// 如果是活动订单并且订单里商品的活动已结束 add by ligj
						// 此map计算总金额试用
						// ---skuMap.put(inputParam.getGoods().get(i).getSku_code(),
						// inputParam.getGoods().get(i).getSku_num());
						// int type = pService.getSkuActivityType(inputParam
						// .getGoods().get(i).getSku_code(),
						// getManageCode());
						// if (("449715200003".equals(orderType) && 2 != type)
						// || // 试用活动过期
						// ("449715200004".equals(orderType) && 1 != type)) { //
						// 闪购活动过期
						// result.setResultCode(939301029);
						// result.setResultMessage(bInfo(939301029));
						// return result;
						// }
						ShopCartService se = new ShopCartService();
						GoodsInfoForAdd add = inputParam.getGoods().get(i);
						add.setSku_code(se.getSkuCodeForValue(
								add.getProduct_code(), add.getSku_code()));
						goodF.add(add);
					}
					// 订单总金额
					// --BigDecimal totalMoney =
					// pService.getSkuTotalManeyForBeauty(skuMap,getManageCode(),orderType);
					// --inputParam.setCheck_pay_money(totalMoney.doubleValue());

					inputParam.setGoods(goodF);
				}
			}

			// 判断付邮订单的库存是否有效
			if (result.upFlagTrue()) {
				if (inputParam.getOrder_type().equals("449715200003")) {
					MDataMap map2 = new MDataMap();
					map2.put("sku_code", inputParam.getGoods().get(0)
							.getSku_code());

					String sWhere = " start_time <= now() and end_time > now() and sku_code =:sku_code  ";

					List<MDataMap> map = DbUp.upTable("oc_tryout_products")
							.queryAll("tryout_inventory", "", sWhere, map2);

					int iStock = 0;

					if ((map.size() != 0)) {
						iStock = Integer.valueOf(map.get(0).get(
								"tryout_inventory"));
					}else {
						
						result.inErrorMessage(939301301);
						
					}

					if (iStock <= 0) {
						result.inErrorMessage(916401139);
					}

				}
			}

			if (result.upFlagTrue()) {

				inputParam.setBuyer_code(getUserCode());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_type", inputParam.getOrder_type());// 订单类型
				map.put("order_souce", inputParam.getOrder_souce());// 订单来源
				map.put("buyer_code", getUserCode());// 买家编号
				map.put("buyer_name", inputParam.getBuyer_name());// 收货人姓名
				map.put("buyer_address", inputParam.getBuyer_address());// 收件人地址

				/* 判断是否存在中文字符 */
				Matcher matcher = pat.matcher(inputParam
						.getBuyer_address_code());

				if (matcher.find()) {

					MDataMap mDataMap = DbUp.upTable("nc_order_area").one(
							"area_name", inputParam.getBuyer_address_code());

					if (mDataMap != null) {
						map.put("buyer_address_code", mDataMap.get("area_code"));// 收货人地址第三极编号
					}

				} else {

					map.put("buyer_address_code",
							inputParam.getBuyer_address_code());// 收货人地址第三极编号
				}
				map.put("buyer_mobile", inputParam.getBuyer_mobile());// 收件人手机号
				map.put("pay_type", inputParam.getPay_type());// 支付类型
				map.put("billInfo", inputParam.getBillInfo());// 发票信息
																// bill_Type发票类型
																// bill_title发票抬头
																// bill_detail发票内容
				map.put("goods", inputParam.getGoods());// 商品列表
				map.put("seller_code", getManageCode());
				map.put("app_vision", inputParam.getVersion());
				map.put("check_pay_money", inputParam.getCheck_pay_money());
				map.put("remark", inputParam.getRemark());

				if (inputParam.getGoods() == null
						|| inputParam.getGoods().isEmpty()) {
					result.setResultCode(916401113);
					result.setResultMessage(bInfo(916401113));
					return result;
				}
				// 校验购买条件是否符合
				ShopCartServiceForBeauty service = new ShopCartServiceForBeauty();
				RootResult rootResult = service.checkGoodsStock(getUserCode(),
						inputParam.getGoods());
				if (rootResult.getResultCode() == 1) {
					rootResult = service.checkGoodsLimit(getManageCode(),
							getUserCode(), inputParam.getGoods());
					if (rootResult.getResultCode() > 1) {
						result.setResultCode(rootResult.getResultCode());
						result.setResultMessage(rootResult.getResultMessage());
						return result;
					}
				} else {
					result.setResultCode(rootResult.getResultCode());
					result.setResultMessage(rootResult.getResultMessage());
					return result;
				}
				Map<String, Object> re = service.createOrder(map);

				if (re.containsKey("check_pay_money_error")) {
					result.setResultCode(916401133);
					result.setResultMessage(re.get("check_pay_money_error")
							.toString());
					return result;
				}
				/* 判断是否零元支付 */
				if (re.get("flag").equals(1)) {

					result.setFlag(1);

				}

				if (re.containsKey("error")
						&& re.get("error") != null
						&& !"".equals(re.get("error").toString())
						&& (re.containsKey("order_code") && !re
								.get("order_code").toString()
								.equals(re.get("error").toString()))) {
					result.setResultCode(2);
					result.setResultMessage(re.get("error").toString());
				} else {
					ApiAlipayMoveProcessService se = new ApiAlipayMoveProcessService();

					GroupReckonSupport groupReckonSupport = new GroupReckonSupport();

					groupReckonSupport.initByErpOrder(re.get("order_code")
							.toString(), "");

					groupReckonSupport.checkCreateStep(re.get("order_code")
							.toString(), GroupConst.REBATE_ORDER_EXEC_TYPE_IN);

					if (re.get("order_code") != null
							&& !re.get("order_code").equals("")) {
						result.setOrder_code(re.get("order_code").toString());
					}

					if (inputParam.getPay_type() != null
							&& "449716200002".equals(inputParam.getPay_type())) {
						result.setPay_url("");
						result.setSign_detail("");
					} else {
						/**
						 * 判断金额是否是0元，0元不走任何支付
						 */
						if (result.getFlag() != 1) {
							if ("449716200001".equals(inputParam.getPay_type())) {
								result.setPay_url(se.alipayMoveParameter(re
										.get("order_code").toString(),true));
								result.setSign_detail(se.alipaySign(
										re.get("order_code").toString()).get(
										"sign"));
							} else if ("449716200004".equals(inputParam
									.getPay_type())) {

								RootResult wxResult = new RootResult();

								WechatProcessRequest wechatprocress = new WechatProcessRequest();

								MDataMap mDataMap = wechatprocress.wechatMove(
										re.get("order_code").toString(),
										inputParam.getBrowserUrl(), wxResult);

								if (mDataMap != null) {

									result.getMicoPayment().setAppid(
											mDataMap.get("appid"));

									result.getMicoPayment().setNonceStr(
											mDataMap.get("noncestr"));

									result.getMicoPayment().setPackageValue(
											mDataMap.get("package"));

									result.getMicoPayment().setPartnerid(
											mDataMap.get("partnerid"));

									result.getMicoPayment().setPrepayid(
											mDataMap.get("prepayid"));

									result.getMicoPayment().setSign(
											mDataMap.get("sign"));

									result.getMicoPayment().setTimeStamp(
											mDataMap.get("timestamp"));

								} else {

									result.setResultCode(wxResult
											.getResultCode());

									result.setResultMessage(wxResult
											.getResultMessage());

								}
							}
						}

					}
					if ("449716200002".equals(inputParam.getPay_type())) {
						OrderShoppingService orderShoppingService = new OrderShoppingService();
						orderShoppingService.deleteSkuToShopCart(re.get(
								"order_code").toString());
					}
					if (inputParam.getGoods() != null
							&& !inputParam.getGoods().isEmpty()) {
						for (int i = 0; i < inputParam.getGoods().size(); i++) {// 更新购物车数据为已结算
							GoodsInfoForAdd add = new GoodsInfoForAdd();
							ShopCartService ss = new ShopCartService();
							ss.updateAccountFlag(getUserCode(),
									add.getSku_code());
						}
					}
					if (inputParam.getOrder_type().equals("449715200003")) {
						MDataMap map2 = new MDataMap();
						map2.put("sku_code", inputParam.getGoods().get(0)
								.getSku_code());
						// map2.put("tryout_inventory", );
						String sql = "update oc_tryout_products set tryout_inventory = tryout_inventory - 1 where start_time <= now() and end_time > now() and sku_code = '"+ inputParam.getGoods().get(0).getSku_code()+"'";
						DbUp.upTable("oc_tryout_products").dataExec(sql, map2);

					}

				}

			}
			WebHelper.unLock(sLockString);

		} else {
			// result.setResultCode(resultCode);
			result.inErrorMessage(939301119);
		}

		return result;
	}
}
