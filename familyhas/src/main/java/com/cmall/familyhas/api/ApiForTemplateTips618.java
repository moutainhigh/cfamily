package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 618活动金额提示
 */
public class ApiForTemplateTips618 extends RootApiForVersion<RootResult, RootInput> {

	@Override
	public RootResult Process(RootInput inputParam,
			MDataMap mRequestMap) {
		RootResult result = new RootResult();

		if(!getFlagLogin()) {
			return result;
		}
		
		String startDate = bConfig("cfamily.zt_618_start_date");
		String endDate = bConfig("cfamily.zt_618_end_date");
		
		// 判断是否活动过期
		if(FormatHelper.upDateTime().compareTo(endDate) > 0) {
			return result;
		}
		
		// 商品金额加上运费
		// 屏蔽橙意卡商品、屏蔽导入订单
		String sql = "SELECT SUM(t.orderMoney + t.transport_money) orderMoney, COUNT(*) orderNum FROM "
						+ " ("
						+ " SELECT SUM(d.sku_price*d.sku_num) orderMoney,o.order_code,o.transport_money FROM ordercenter.oc_orderinfo o, ordercenter.oc_orderdetail d WHERE "
						+ " o.order_code = d.order_code AND d.gift_flag = '1' and o.order_status NOT IN('4497153900010001','4497153900010006') AND o.pay_type = '449716200001'"
						+ " AND o.buyer_code = :buyer_code AND o.create_time > :start_date AND o.create_time < :end_date"
						+ " AND d.product_code != :plus_product_code"
						+ " GROUP BY o.order_code"
						+ " ) t";

		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("buyer_code", getOauthInfo().getUserCode());
		mWhereMap.put("start_date", startDate);
		mWhereMap.put("end_date", endDate);
		mWhereMap.put("plus_product_code", bConfig("xmassystem.plus_product_code"));
		Map<String, Object> dataMap = DbUp.upTable("oc_orderinfo").dataSqlOne(sql, mWhereMap);
		Object money = dataMap.get("orderMoney");
		int orderNum = NumberUtils.toInt(dataMap.get("orderNum") + "");
		BigDecimal orderMoney = BigDecimal.ZERO;
		if(money != null) {
			orderMoney = new BigDecimal(money.toString());
		}
		
		if(orderMoney.compareTo(BigDecimal.ZERO) == 0) {
			return result;
		}
		
		if(orderMoney.compareTo(new BigDecimal(350)) < 0) {
			result.setResultMessage("您已消费<span>"+MoneyHelper.format(orderMoney)+"元</span>，再消费<span>"+MoneyHelper.format(new BigDecimal(350).subtract(orderMoney))+"元</span>可获得<span>30元</span>无门槛优惠券");
		} else if(orderMoney.compareTo(new BigDecimal(700)) < 0) {
			result.setResultMessage("您已消费<span>"+MoneyHelper.format(orderMoney)+"元</span>，再消费<span>"+MoneyHelper.format(new BigDecimal(700).subtract(orderMoney))+"元</span>可获得<span>70元</span>无门槛优惠券");
		} else if(orderMoney.compareTo(new BigDecimal(1200)) < 0) {
			result.setResultMessage("您已消费<span>"+MoneyHelper.format(orderMoney)+"元</span>，再消费<span>"+MoneyHelper.format(new BigDecimal(1200).subtract(orderMoney))+"元</span>可获得<span>200元</span>无门槛优惠券");
		} else if(orderMoney.compareTo(new BigDecimal(1618)) < 0) {
			result.setResultMessage("您已消费<span>"+MoneyHelper.format(orderMoney)+"元</span>，再消费<span>"+MoneyHelper.format(new BigDecimal(1618).subtract(orderMoney))+"元</span>可获得<span>300元</span>无门槛优惠券");
		} else if(orderNum < 2) {
			result.setResultMessage("您已消费<span>"+MoneyHelper.format(orderMoney)+"元</span>，再消费任意元一单</span>可获得<span>300元</span>无门槛优惠券");
		} else {
			result.setResultMessage("恭喜您，购物金额已满<span>1618元</span>，商品妥收后即可获得<span>300元</span>无门槛优惠券");
		}
		
		return result;
	}
	
}
