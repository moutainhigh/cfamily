package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiSyncYYGActivityInfoInput;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.util.MD5Code;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 同步一元购活动信息
 * @author fq
 *
 */
public class ApiSyncYYGActivityInfo extends
		RootApiForManage<RootResult, ApiSyncYYGActivityInfoInput> {

	public RootResult Process(ApiSyncYYGActivityInfoInput inputParam,
			MDataMap mRequestMap) {
		RootResult result = new RootResult();

		/*
		 * 加密字符串
		 */
		String mac = inputParam.getMac();
		String orderNo = inputParam.getOrderNo();
		String payMoney = inputParam.getPayMoney();
		String periodNum = inputParam.getPeriodNum();
		String productCode = inputParam.getProductCode();
		String productID = inputParam.getProductID();
		String skuCode = inputParam.getSkuCode();
		
		String key = bConfig("familyhas.sync_yyg_key");
		
		/*
		 * 对比加密字符串
		 */
		String str = orderNo + payMoney + periodNum + productCode + productID + skuCode + key;
		try {
			String encode = MD5Code.encode(str);
			if(StringUtils.isNotBlank(mac) && mac.equals(encode)) {
				MDataMap one = DbUp.upTable("lc_sync_yyg_orderInfo").one("order_no",orderNo);
				if(null == one) {
					DbUp.upTable("lc_sync_yyg_orderInfo").insert(
							"product_id", productID,
							"product_code", productCode,
							"sku_code", skuCode, 
							"period_num", periodNum, 
							"order_no", orderNo, 
							"pay_money", payMoney,
							"period_key",productID+"-"+periodNum,
							"sync_time",DateUtil.getNowTime());
				} else {
					result.setResultCode(0);
					result.setResultMessage("已经有同步记录");
				}
				
			} else {
				result.setResultCode(0);
				result.setResultMessage("同步失败,请重试");
			}
			
		} catch (UnsupportedEncodingException e) {
			result.setResultCode(0);
			result.setResultMessage("推送失败，参数格式错误");
//			e.printStackTrace();
		}
		
		return result;
	}

}
