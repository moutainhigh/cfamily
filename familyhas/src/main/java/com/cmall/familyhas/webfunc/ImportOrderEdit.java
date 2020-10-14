package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 描述: 添加修改按钮事件：外部导入订单收款状态修改操作函数 <br>
 * @author zhy
 * @date 2016-09-23
 */
public class ImportOrderEdit extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		/*
		 * 整理参数集合
		 */
		MDataMap editMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		editMap.put("update_user", UserFactory.INSTANCE.create().getLoginName());
		editMap.put("update_time", DateUtil.getSysDateTimeString());
		/*
		 * 修改百度订单表的收款状态
		 */
		int result = DbUp.upTable("oc_orderinfo_import").dataUpdate(editMap, "", "uid");
		if (result >= 0) {
			/*
			 * 将订单金额导入到惠家有订单表中的已付款金额中
			 */
			MDataMap bdwmOrderMap = DbUp.upTable("oc_orderinfo_import")
					.oneWhere("oc_order_code,product_total_money,freight", "", "", "uid", editMap.get("uid"));
			MDataMap orderMap = new MDataMap();
			orderMap.put("order_code", bdwmOrderMap.get("oc_order_code"));
			/*
			 * 计算订单已付金额=订单总金额+运费金额
			 */
			BigDecimal product_total_money = BigDecimal
					.valueOf(Double.valueOf(bdwmOrderMap.get("product_total_money")));
			BigDecimal freight = BigDecimal.valueOf(Double.valueOf(bdwmOrderMap.get("freight")));
			orderMap.put("payed_money", product_total_money.add(freight).toString());
			int resultOrder = DbUp.upTable("oc_orderinfo").dataUpdate(orderMap, "payed_money", "order_code");
			if (resultOrder >= 0) {
				/*
				 * 修改订单信息表oc_orderinfo_upper
				 */
				MDataMap upper = new MDataMap();
				// 订单编码
				upper.put("order_code", bdwmOrderMap.get("oc_order_code"));
				// 订单金额
				upper.put("payed_money", product_total_money.add(freight).toString());
				upper.put("due_money", BigDecimal.ZERO.toString());
				upper.put("update_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("oc_orderinfo_upper").dataExec(
						"UPDATE ordercenter.oc_orderinfo_upper SET payed_money=:payed_money,due_money=:due_money,update_time=:update_time where big_order_code = (select big_order_code from ordercenter.oc_orderinfo where order_code=:order_code)",
						upper);
				mResult.setResultCode(0);
				mResult.setResultMessage(bInfo(969909001));
			} else {
				mResult.setResultCode(1);
				mResult.setResultMessage("修改百度外卖收款状态成功，同步已支付金额失败");
			}
		} else {
			mResult.setResultCode(1);
			mResult.setResultMessage("修改百度外卖收款状态失败");
		}
		return mResult;
	}

}
