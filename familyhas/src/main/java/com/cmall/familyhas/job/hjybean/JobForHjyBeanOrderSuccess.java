package com.cmall.familyhas.job.hjybean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.cmall.familyhas.FamilyConfig;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrInput;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrResult;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 订单交易成功时给用户送惠豆
 */
public class JobForHjyBeanOrderSuccess {

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		
		MDataMap orderInfoMap = DbUp.upTable("oc_orderinfo").one("order_code",sInfo);
		if(orderInfoMap == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单不存在");
			return mWebResult;
		}
		
		if(!FamilyConfig.ORDER_STATUS_TRADE_SUCCESS.equals(orderInfoMap.get("order_status"))){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单状态不是交易成功");
			return mWebResult;
		}
		
		// 有退货单的订单不送惠豆
		if(DbUp.upTable("oc_return_goods").dataCount("order_code =:order_code AND status NOT IN('4497153900050002','4497153900050006')", new MDataMap("order_code",sInfo)) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单退货不送惠豆");
			return mWebResult;
		}
		
		// 有退款单的订单不送惠豆
		if(DbUp.upTable("oc_return_money").dataCount("order_code =:order_code AND status != '4497153900040002'", new MDataMap("order_code",sInfo)) > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("订单退款不送惠豆");
			return mWebResult;
		}
		
		int dataCount = DbUp.upTable("oc_order_pay").dataCount(" order_code =:order_code AND pay_type IN('449746280006','449746280007') ", new MDataMap("order_code",sInfo));
		if(dataCount > 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("用了储值金和暂存款的订单不送惠豆");
			return mWebResult;
		}
		
		MDataMap changeDetail = DbUp.upTable("fh_hd_change_detail").one("change_type","449747940001","info",sInfo);
		if(changeDetail != null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("此订单已赠送惠豆");
			return mWebResult;
		}
		
		// 按商户类型查询返豆比例
		MDataMap sellerInfo = DbUp.upTable("uc_seller_info_extend").one("small_seller_code",orderInfoMap.get("small_seller_code"));
		MDataMap produceConfig = null;
		if(sellerInfo != null){
			produceConfig = DbUp.upTable("fh_hd_produce_config").one("seller_type",sellerInfo.get("uc_seller_type"));
		}else if("SI2003".equals(orderInfoMap.get("small_seller_code"))){
			produceConfig = DbUp.upTable("fh_hd_produce_config").one("seller_type","4497478100050000"); //LD 商户
		}
		
		if(produceConfig == null){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("未查到赠送惠豆比率配置");
			return mWebResult;
		}
		
		// 查询商品实付金额
		List<MDataMap> orderDetailList = DbUp.upTable("oc_orderdetail").queryByWhere("order_code",sInfo,"gift_flag","1");
		List<MDataMap> hjyBeanList = new ArrayList<MDataMap>();
		BigDecimal skuPrice = null;
		int skuNum = 0;
		int amount = 0;
		for(MDataMap orderDetail : orderDetailList){
			skuPrice = new BigDecimal(orderDetail.get("sku_price"));
			skuNum = NumberUtils.toInt(orderDetail.get("sku_num"));
			
			// 惠豆数量 = 商品价格 / 兑换比例  * 返豆率， 结果取整(如果计算的惠豆数量有小数，则向下取整)
			int sku_amount = HjybeanService.reverseRMBToHjyBean(skuPrice).multiply(new BigDecimal(produceConfig.get("percent")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(0, BigDecimal.ROUND_FLOOR).intValue();
			if(sku_amount == 0){
				continue; // 单个商品计算惠豆取整后等于0则忽略
			}
			
			// 总可送惠豆数
			amount += sku_amount * skuNum;
			
			MDataMap hjybean = new MDataMap();
			hjybean.put("order_code", sInfo);
			hjybean.put("member_code", orderInfoMap.get("buyer_code"));
			hjybean.put("product_code", orderDetail.get("product_code"));
			hjybean.put("sku_code", orderDetail.get("product_code"));
			hjybean.put("sku_price", orderDetail.get("sku_price"));
			hjybean.put("sku_num", orderDetail.get("sku_num"));
			hjybean.put("hjybean_num", sku_amount+"");
			hjyBeanList.add(hjybean);
		}
		
		if(amount <= 0){
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("商品总可送惠豆数为0");
			return mWebResult;
		}
		
		HjyBeanCtrInput input = new HjyBeanCtrInput();
		input.setAmount(amount);
		input.setTradetype(1);
		input.setOrderdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		input.setInout(1);
		input.setMemo(sInfo);
		HjyBeanCtrResult result = new HjybeanService().ctrhjyBeanByMemberCode(orderInfoMap.get("buyer_code"), input);
		
		if(result == null) {
			result = new HjyBeanCtrResult();
			result.setResultCode(0);
			result.setResultMessage("调用接口失败");
		}
		
		if(result.getResultCode() == 1){
			try {
				changeDetail = new MDataMap();
				changeDetail.put("change_type", "449747940001");
				changeDetail.put("info", sInfo);
				changeDetail.put("change_amount", ""+amount);
				changeDetail.put("remark", "By JobForHjyBeanOrderSuccess");
				changeDetail.put("serialno", result.getCenterserialno());
				changeDetail.put("trade_time", input.getOrderdate());
				changeDetail.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("fh_hd_change_detail").dataInsert(changeDetail);
				
				for(MDataMap map : hjyBeanList){
					map.put("create_time", changeDetail.get("create_time"));
					DbUp.upTable("fh_hd_produce_detail").dataInsert(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mWebResult.setResultCode(result.getResultCode());
		mWebResult.setResultMessage(result.getResultMessage());
		return mWebResult;
	}

}
