package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiOrderConfirmInput;
import com.cmall.familyhas.api.result.TeslaOrderAvailableCouponResult;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.service.AddressService;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠家有订单可用优惠券
 */
public class TeslaOrderAvailableCoupon extends RootApiForToken<TeslaOrderAvailableCouponResult, APiOrderConfirmInput> {

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	public TeslaOrderAvailableCouponResult Process(APiOrderConfirmInput inputParam,
			MDataMap mRequestMap) {
		TeslaOrderAvailableCouponResult result = new TeslaOrderAvailableCouponResult();
		
	    TeslaXOrder teslaXOrder = new TeslaXOrder();
	    //订单基本信息
	    TeslaModelOrderInfoUpper orderInfo =  teslaXOrder.getUorderInfo();
	    orderInfo.setBuyerCode(getUserCode());
	    orderInfo.setSellerCode(getManageCode());
	    orderInfo.setOrderType(inputParam.getOrder_type());
	    orderInfo.setPayType(inputParam.getPayType());
	    teslaXOrder.setUorderInfo(orderInfo);
	    teslaXOrder.setIsOriginal(inputParam.getIsOriginal());
	    teslaXOrder.setCollageFlag(inputParam.getCollageFlag());
	    teslaXOrder.setCollageCode(inputParam.getCollageCode());
	    teslaXOrder.setActivityCode(inputParam.getActivityCode());
	    teslaXOrder.setRedeemCode(inputParam.getRedeemCode());
	    teslaXOrder.setTreeCode(inputParam.getTreeCode());
	    teslaXOrder.setEventCode(inputParam.getEventCode());
	    teslaXOrder.setHuDongCode(inputParam.getHuDongCode());
	    
	    if(getApiClient() != null ){
	    	orderInfo.setAppVersion(getApiClient().get("app_vision"));
	    }
	    
		List<TeslaModelOrderDetail> orderDetails = new ArrayList<TeslaModelOrderDetail>();
		List<GoodsInfoForAdd> infoForAdds = inputParam.getGoods();
		for(GoodsInfoForAdd goodsInfo :infoForAdds) {
			//加价购商品不参与优惠券的使用
			if("0".equals(goodsInfo.getIfJJGFlag())) {
				TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail(); 
				orderDetail.setProductCode(goodsInfo.getProduct_code());
				orderDetail.setSkuCode(goodsInfo.getSku_code());
				orderDetail.setSkuNum(goodsInfo.getSku_num());
				orderDetail.setIsSkuPriceToBuy(goodsInfo.getFlg());
				orderDetails.add(orderDetail);	
			}
		}
		
		teslaXOrder.setOrderDetails(orderDetails);
		
		//三级区域编码
		teslaXOrder.getAddress().setAreaCode(inputParam.getArea_code());
		//优惠券 清空优惠券列表
		if (inputParam.getCoupon_codes() != null
				&& !inputParam.getCoupon_codes().isEmpty()) {
			teslaXOrder.getUse().setCoupon_codes(new ArrayList<String>());
		}

		// 渠道来源
		teslaXOrder.setChannelId(inputParam.getChannelId());
		// 只走优惠券逻辑
		teslaXOrder.getStatus().setExecStep(ETeslaExec.AvailableCoupon);
		// 用户编号
		teslaXOrder.setIsMemberCode(inputParam.getIsMemberCode());
		// 是否内购
		teslaXOrder.setIsPurchase(inputParam.getIsPurchase());

		AddressInformation address = (new AddressService()).getAddressOne(inputParam.getBuyer_address_id(), getUserCode(), getManageCode());
		if (StringUtils.isBlank(teslaXOrder.getAddress().getAreaCode())) {
			teslaXOrder.getAddress().setAreaCode(address.getArea_code());
		}		
		
		TeslaXResult reTeslaXResult = new ApiConvertTeslaService().ConvertOrder(teslaXOrder);
		if (reTeslaXResult.upFlagTrue()) {
			List<TeslaModelCouponInfo> list = teslaXOrder.getCouponInfoList();
			
			List<TeslaModelCouponInfo> availableList = new ArrayList<TeslaModelCouponInfo>();
			List<TeslaModelCouponInfo> disableList = new ArrayList<TeslaModelCouponInfo>();
			
			for(TeslaModelCouponInfo ci : list) {
				if("1".equals(ci.getSelectLimit())) {
					availableList.add(ci);
				} else {
					disableList.add(ci);
				}
			}
			
			result.setDisableCouponCount(disableList.size());
			result.setCouponCount(availableList.size());
			result.setCouponList(availableList);
			result.setDisableCouponList(disableList);
		}

	    return result;
	}

}
