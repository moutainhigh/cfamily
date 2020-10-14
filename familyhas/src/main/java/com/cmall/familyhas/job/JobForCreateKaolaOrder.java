package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cmall.groupcenter.service.KaolaOrderService;
import com.cmall.ordercenter.model.OrderItemList;
import com.cmall.ordercenter.model.UserInfo;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 如果考拉同步下单失败，则进行异步下单
 * @author cc
 *
 */
public class JobForCreateKaolaOrder extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		
		//根据订单号查询是否是拼团单。
		MDataMap groupOrderMap = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(groupOrderMap != null && !groupOrderMap.isEmpty()){//不为空时，证明是拼团单，然后检查是否已经拼团成功。
			String collageCode = groupOrderMap.get("collage_code");
			MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
			//判断此团是否拼团成功
			String collageStatus = collageInfo.get("collage_status");
			if(!"449748300002".equals(collageStatus)){//非拼团成功的订单做以下操作
				//操作失败标识
				mWebResult.setResultCode(99);
				return mWebResult;
			}
		}
		
		// 异步下单时锁定一下订单，防止订单在下单成功的同时订单又被用户取消的情况
		String lockKey = KvHelper.lockCodes(20, Constants.LOCK_ORDER_UPDATE + orderCode);
		if(StringUtils.isBlank(lockKey)) {
			// 订单正在操作中，请稍候重试！
			mWebResult.setResultCode(918590001);
			mWebResult.setResultMessage(TopUp.upLogInfo(918590001));
			return mWebResult;
		}
		
		MDataMap orderInfoMap = DbUp.upTable("oc_orderinfo").one("order_code", orderCode);
		if("4497153900010006".equals(orderInfoMap.get("order_status"))){
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("订单已经取消");
			return mWebResult;
		}
		
		MDataMap kaolaOrder = DbUp.upTable("oc_order_kaola_list").one("order_code", orderCode);
		List<MDataMap> skuItemList = DbUp.upTable("oc_order_kaola_list_detail").queryByWhere("order_code", orderCode);
		MDataMap orderAddress = DbUp.upTable("oc_orderadress").one("order_code", orderCode);
		
		if(kaolaOrder == null || skuItemList.isEmpty() || orderAddress == null) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("缺失订单相关数据");
			return mWebResult;
		}
		
		List<OrderItemList> orderItemList = new ArrayList<OrderItemList>();//考拉订单信息
		for(MDataMap map : skuItemList) {
			MDataMap detailMap = DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code",map.get("sku_code"));
			if(detailMap == null){
				mWebResult.setResultCode(0);
				mWebResult.setResultMessage("未查到订单明细");
				return mWebResult;
			}
			
			OrderItemList item = new OrderItemList();
			item.setGoodsId(map.get("goods_id").toString());
			item.setSkuId(map.get("sku_id").toString());
			item.setBuyAmount(Integer.parseInt(map.get("goods_buy_number").toString()));
			item.setChannelSalePrice(new BigDecimal(detailMap.get("cost_price").toString())); // 渠道售价需要传成本价（考拉供货价）
			item.setWarehouseId(kaolaOrder.get("warehouse_id").toString());
			orderItemList.add(item);
		}
		
		UserInfo userInfo = new UserInfo();
		userInfo.setAccountId(orderInfoMap.get("buyer_code"));
		userInfo.setName(orderAddress.get("receive_person"));//收货人
		userInfo.setMobile(orderAddress.get("mobilephone"));//手机号码					
		userInfo.setAddress(orderAddress.get("address"));
		
		KaolaOrderService.setOrderAddress(userInfo, orderAddress.get("area_code"));
		
		if(!new KaolaOrderService().upKaolaBookOrderInterface(orderItemList, userInfo, orderCode)) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("异步下单失败");
		} else {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("异步下单成功");	
		}
		
		// 操作执行完成解除锁定
		KvHelper.unLockCodes(lockKey, Constants.LOCK_ORDER_UPDATE + orderCode);
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990014");
		config.setMaxExecNumber(30);
		return config;
	}

}
