package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiOrderConfirmInput;
import com.cmall.familyhas.api.result.APiOrderConfirmResult;
import com.cmall.familyhas.model.GoodsInfoForConfirm;
import com.cmall.familyhas.model.OrderSort;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.groupcenter.service.GroupAccountService;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.familyhas.active.ActiveReq;
import com.cmall.ordercenter.familyhas.active.product.ActiveForSource;
import com.cmall.ordercenter.model.CouponInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.model.OrderAddress;
import com.cmall.ordercenter.service.AddressService;
import com.cmall.ordercenter.service.CouponsService;
import com.cmall.productcenter.model.ReminderContent;
import com.cmall.productcenter.service.MyService;
import com.cmall.systemcenter.service.InvoiceService;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 惠家有订单确认接口
 * 
 * @author xiegj
 *
 */
public class APiOrderConfirm extends RootApiForToken<APiOrderConfirmResult, APiOrderConfirmInput> {

	public APiOrderConfirmResult Process(APiOrderConfirmInput inputParam,
			MDataMap mRequestMap) {
		APiOrderConfirmResult result = new APiOrderConfirmResult();
		try {
			List<GoodsInfoForAdd> goodF = new ArrayList<GoodsInfoForAdd>();
			List<ActiveReq> activeRequests = new ArrayList<ActiveReq>();
			if(inputParam.getGoods()!=null&&!inputParam.getGoods().isEmpty()){
				for (int i = 0; i < inputParam.getGoods().size(); i++) {
					ShopCartService se = new ShopCartService();
					GoodsInfoForAdd add = inputParam.getGoods().get(i);
					add.setSku_code(se.getSkuCodeForValue(add.getProduct_code(), add.getSku_code()));
					if(add.getSku_code()==null||"".equals(add.getSku_code())||add.getSku_num()<=0){
						result.setResultCode(916401128);
						result.setResultMessage(bInfo(916401128));
						return result;
					}
					goodF.add(add);
					ActiveReq activeReq = new ActiveReq();
					activeReq.setBuyer_code(getUserCode());
					activeReq.setProduct_code(add.getProduct_code());
					activeReq.setSku_code(add.getSku_code());
					activeReq.setSku_num(add.getSku_num());
					activeRequests.add(activeReq);
				}
			}
			
			
			PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
			List<String> smallSellerCodes = new ArrayList<String>();
			for (GoodsInfoForAdd goods : inputParam.getGoods()) {
				plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(goods.getProduct_code()));
				if (null != plusModelProductinfo) {
					smallSellerCodes.add(plusModelProductinfo.getSmallSellerCode());
				}
			}
			List<ReminderContent> listr= new MyService().getReminderList(smallSellerCodes,"4497471600270001");
			if(listr!=null&&!listr.isEmpty()){
				for (ReminderContent content :listr) {
					result.getTips().add(content.getContent());
				}
			}
			
			inputParam.setGoods(goodF);
			inputParam.setBuyer_code(getUserCode());
			ShopCartService service = new ShopCartService();
			RootResult rootResult = service.checkGoodsStock(getManageCode(),getUserCode(), inputParam.getGoods());
			if(rootResult.getResultCode()==1){
				rootResult = service.checkGoodsLimit(getManageCode(),getUserCode(), inputParam.getGoods());
				if(rootResult.getResultCode()>1){
					result.setResultCode(rootResult.getResultCode());
					result.setResultMessage(rootResult.getResultMessage());
					return result;
				}
				RootResultWeb ww = new RootResultWeb();
				new ActiveForSource().checkVipSpecialLimitCount(activeRequests, ww);
				if(ww.getResultCode()>1){
					result.setResultCode(ww.getResultCode());
					result.setResultMessage(ww.getResultMessage());
					return result;
				}
			}else {
				result.setResultCode(rootResult.getResultCode());
				result.setResultMessage(rootResult.getResultMessage());
				return result;
			}
			result.setBills((new InvoiceService()).getInvoiceList(getManageCode()));
			result.setInformation((new AddressService()).getAddressOne("", getUserCode(),getManageCode()));
			// 发货地址信息
			OrderAddress address = new OrderAddress();
			if(inputParam.getArea_code()!=null&&!"".equals(inputParam.getArea_code())){
				address.setAreaCode(inputParam.getArea_code());
			}else {
				address.setAreaCode(result.getInformation().getArea_code());
			}
			
			MDataMap oo = new MDataMap();
			oo.put("buyerCode", getUserCode());
			oo.put("sellerCode", getManageCode());
			oo.put("orderType", inputParam.getOrder_type());
			List<String> couponCodes = new ArrayList<String>();
			if(inputParam.getCoupon_codes()!=null&&!inputParam.getCoupon_codes().isEmpty()){
				couponCodes = inputParam.getCoupon_codes();
			}
			oo.put("channelId", inputParam.getChannelId());
			if(Double.valueOf(inputParam.getWgsUseMoney())>0){
				oo.put("wgsUseMoney", String.valueOf(inputParam.getWgsUseMoney()));
			}
			Map<String, Object> map = service.orderConfirm(oo, inputParam.getGoods(),address,couponCodes);
			result.setResultGoodsInfo((List<GoodsInfoForConfirm>)map.get("confirms"));
			result.setPay_money(new BigDecimal(String.valueOf(map.get("payMoney"))).doubleValue());
			result.setCash_back(new BigDecimal(String.valueOf(map.get("cashBack"))).doubleValue());
			result.setCost_money(new BigDecimal(String.valueOf(map.get("costMoney"))).doubleValue());
			result.setFirst_cheap(new BigDecimal(String.valueOf(map.get("firstCheap"))).doubleValue());
			result.setSent_money(new BigDecimal(String.valueOf(map.get("sentMoney"))).doubleValue());
			result.setSub_money(new BigDecimal(String.valueOf(map.get("subMoney"))).doubleValue());
			result.setPhone_money(new BigDecimal(String.valueOf(map.get("phoneMoney"))).doubleValue());
			result.setOrders((List<OrderSort>)map.get("sorts"));
			result.setBills((new InvoiceService()).getInvoiceList(getManageCode()));
			result.setCoupons((List<CouponInfo>)map.get("coupons"));
			result.setSourceFlag((String)map.get("sourceFlag"));
			result.setBillRemark(bConfig("familyhas.billrenark"));
			result.setWgsalUseMoney(Double.valueOf(map.get("wgsalUseMoney").toString()));
//			if(result.getCoupons().size()>0){//使用优惠券必须在线支付(拆单和此处共同控制)
//				result.setPay_type("449716200001");
//			}else {}
			if(inputParam.getWgsUseMoney()>0&&result.getPay_money()==0)
			{
				result.setPay_type("449746280009");//微公社支付方式
			}else{
				result.setPay_type(map.get("payType").toString());
			}
			if(map.containsKey("resultCode")){
				result.setResultCode(Integer.valueOf(map.get("resultCode").toString()));
				result.setResultMessage(map.get("resultMessage").toString());
			}
			if(result.getResultCode()==1){
				if(VersionHelper.checkServerVersion("3.5.22.51")&&getManageCode().equals(MemberConst.MANAGE_CODE_HOMEHAS)){//后台逻辑代码版本控制
					result.setDisList((List<TeslaModelDiscount>)map.get("disList"));
					List<String> disRemarks = new ArrayList<String>();
//					disRemarks.add(bConfig("familyhas.remarkDoubleTwelve"));
					disRemarks.add(bConfig("familyhas.fullSubActivityOtherRemark"));
					result.setDisRemarks(disRemarks);
				}else if(map.containsKey("disList")){
					result.setDisList((List<TeslaModelDiscount>)map.get("disList"));
					List<String> disRemarks = new ArrayList<String>();
					disRemarks.add(bConfig("familyhas.eveActivityRemark"));
					result.setDisRemarks(disRemarks);
				}
				if(map.containsKey("prompt")){
					result.setPrompt(map.get("prompt").toString());
				}
				if(result.getPay_money()<=0&&result.getResultCode()==1&&inputParam.getWgsUseMoney()==0&&(inputParam.getCoupon_codes()==null||inputParam.getCoupon_codes().isEmpty())){//当且仅当存在微公社支付或者优惠券的时候出现的0元订单才正常.
					result.setResultCode(916401128);
					result.setResultMessage(bInfo(916401128));
				}
				if (result.getResultCode()==1) {
					String skuCodes = "";
					for (int i = 0; i < inputParam.getGoods().size(); i++) {
						GoodsInfoForAdd gf = inputParam.getGoods().get(i);
						if("".equals(skuCodes)){
							skuCodes=gf.getSku_code();
						}else {
							skuCodes=skuCodes+","+gf.getSku_code();
						}
					}
					if(VersionHelper.checkServerVersion("3.5.43.51")){
						result.setCouponAbleNum(new CouponsService().couponList(getUserCode(), String.valueOf(result.getPay_money()), inputParam.getGoods(),getManageCode(),inputParam.getChannelId(),inputParam.getPayType()).get("available").size());
					}
					GroupAccountService accountService = new GroupAccountService();
					GroupAccountInfoResult groupResult = accountService.getAccountInfo(accountService.getAccountCodeByMemberCode(getUserCode()));
					if(groupResult.upFlagTrue()&&StringUtils.isNotBlank(groupResult.getWithdrawMoney())&&"1".equals(groupResult.getFlagEnable())){
//						result.setWgsMoney(new BigDecimal(groupResult.getWithdrawMoney()).setScale(0, BigDecimal.ROUND_DOWN).doubleValue());
						result.setWgsMoney(MoneyHelper.roundHalfUp(new BigDecimal(groupResult.getWithdrawMoney())).doubleValue());  // 兼容小数 - Yangcl 
					}
					if(inputParam.getWgsUseMoney()>0){
						result.setWgsMaxMoney((result.getPay_money()+result.getWgsalUseMoney())>result.getWgsMoney()?result.getWgsMoney():(result.getPay_money()+result.getWgsalUseMoney()));
					}else {
						result.setWgsMaxMoney(result.getPay_money()>result.getWgsMoney()?result.getWgsMoney():result.getPay_money());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(916401128);
			result.setResultMessage(bInfo(916401128));
		}
		
		
		result.setRoomId(inputParam.getRoomId());
		result.setAnchorId(inputParam.getAnchorId());
		
		return result;
	}
	
	public static void main(String[] args) {
		Integer usableBeanTotal = (int) ((Double.valueOf(String.valueOf("3.32")))*15/100*1000);
		System.out.println(usableBeanTotal);
		APiOrderConfirmInput apiOrderConfirmInput = new APiOrderConfirmInput();
		apiOrderConfirmInput.setAnchorId("");
		apiOrderConfirmInput.setArea_code("");
		apiOrderConfirmInput.setBuyer_code("MI171026700455");
		apiOrderConfirmInput.setIsPurchase(0);
		apiOrderConfirmInput.setOrder_type("449715200005");
		apiOrderConfirmInput.setUsedCzjTotal(12.34);
		apiOrderConfirmInput.setUsedZckTotal(56.78);
		new APiOrderConfirm().Process(apiOrderConfirmInput, new MDataMap());
	}
}