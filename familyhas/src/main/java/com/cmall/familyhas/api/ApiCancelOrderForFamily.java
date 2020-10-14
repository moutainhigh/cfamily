package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiCancelOrderForFamilyInput;
import com.cmall.familyhas.api.result.ApiCancelOrderForFamilyResult;
import com.cmall.familyhas.service.WxGZHService;
import com.cmall.groupcenter.homehas.RsyncRecordTvOrderStat;
import com.cmall.groupcenter.homehas.model.RsyncModelRecordTvOrderStat;
import com.cmall.familyhas.service.CouponService;
import com.cmall.ordercenter.helper.OrderHelper;
import com.cmall.ordercenter.model.api.ApiCancelOrderResult;
import com.cmall.ordercenter.model.api.GiftVoucherInfo;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.helper.WebHelper;

/**
 *取消订单接口
 *xiegj 
 * 
 */
public class ApiCancelOrderForFamily extends RootApiForToken<ApiCancelOrderForFamilyResult,ApiCancelOrderForFamilyInput> {

	static Object lock = new Object();
	
	public ApiCancelOrderForFamilyResult Process(ApiCancelOrderForFamilyInput input, MDataMap mRequestMap) {
		synchronized (lock) {
		ApiCancelOrderForFamilyResult result = new ApiCancelOrderForFamilyResult();
			input.setBuyer_code(getUserCode());
			OrderService os = new OrderService();
			
			try {
				if(input.getOrder_code().startsWith("OS")){
					
					//取消之前添加判断逻辑
//					if(DbUp.upTable("oc_orderinfo").count("big_order_code",input.getOrder_code(),"out_order_code","")>0){
//						result.setResultCode(916401231);
//						result.setResultMessage(bInfo(916401231));
//						return result;
//					}
					
					List<MDataMap> orderCodes = DbUp.upTable("oc_orderinfo").queryAll("order_code,order_status", "", "", new MDataMap("big_order_code",input.getOrder_code()));
					if(orderCodes==null||orderCodes.isEmpty()){
						result.setResultCode(916401109);
						result.setResultMessage(bInfo(916401109));
					}else {
						
						boolean status_flag=false;
						for (MDataMap mDataMap : orderCodes) {
							String order_status=mDataMap.get("order_status");
							if(!StringUtils.startsWithAny(order_status, "4497153900010001","4497153900010006")){
								status_flag=true;
								break;
							}
						}
						
						if(status_flag){
							result.setResultCode(916421180);
							result.setResultMessage(bInfo(916421180));
							return result;
						}
						
						List<GiftVoucherInfo> reWriteLD = new ArrayList<GiftVoucherInfo>();
						for (int i = 0; i < orderCodes.size(); i++) {
							String od = orderCodes.get(i).get("order_code");
							String order_status = orderCodes.get(i).get("order_status"); //订单状态
							ApiCancelOrderResult rr = os.CancelOrderForList(OrderHelper.upOrderCodeByOutCode(od),getUserCode());
							for(GiftVoucherInfo giftVoucher : rr.getReWriteLD()) {
								reWriteLD.add(giftVoucher);
							}
							String sql = "SELECT oc_orderinfo.order_code,oc_orderinfo.buyer_code,oc_orderinfo.out_order_code,oc_orderinfo.small_seller_code," +
									 "oc_orderinfo.order_source,oc_orderinfo.order_money, oc_orderadress.address, oc_orderinfo.product_name " +
									 " from oc_orderinfo left join oc_orderadress " +
									 " on oc_orderinfo.order_code = oc_orderadress.order_code " +
									 " where oc_orderinfo.order_code=:order_code ";
							Map<String, Object> map= DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code",OrderHelper.upOrderCodeByOutCode(od)));							
							String order_code= (String)map.get("order_code");
							String buyer_code= (String)map.get("buyer_code");
							String out_order_code= (String)map.get("out_order_code");
							String small_seller_code = (String)map.get("small_seller_code");
							String order_money = map.get("order_money").toString(); //订单金额
							String address = (String) map.get("address"); //收货信息	
							String product_name = (String) map.get("product_name"); //所有商品名称
							String now=DateUtil.getSysDateTimeString();
							DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",out_order_code,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode());
							//未付款的订单不发通知
							if(!"4497153900010001".equals(order_status)) {
								//向商家发送微信通知
								new WxGZHService().sendWxGZHCancelOrder(small_seller_code, order_code, order_money, address, product_name);
								//做分销人金额信息统计更新
								//updateDistributionInfos(order_code); 
							}
							//取消的是网易考拉订单的时候，需要同步上传网易考拉
							if(AppConst.MANAGE_CODE_WYKL.equals(small_seller_code)) {
								cancelKaolaOrder(order_code);
							}
							
							//判断如果是拼团单，则修改拼团状态
							updateCollageStatus(order_code);
						}
						new CouponService().reWriteGiftVoucherToLD(reWriteLD, "R");
					}
				}				
				else if("DD".equals(input.getOrder_code().substring(0, 2)) || "HH".equals(input.getOrder_code().substring(0, 2))){
					
					MDataMap orderMap=DbUp.upTable("oc_orderinfo").one("order_code",input.getOrder_code());
					if(orderMap==null||orderMap.size()<1){
						result.setResultCode(916421179);
						result.setResultMessage(bInfo(916421179));
						return result;
					}
					
					String order_status=orderMap.get("order_status");
					if(!"4497153900010001".equals(order_status)){
						result.setResultCode(916421180);
						result.setResultMessage(bInfo(916421180));
						return result;
					}
					
					if(StringUtils.isBlank(orderMap.get("out_order_code"))){
//					if(DbUp.upTable("oc_orderinfo").count("order_code",input.getOrder_code(),"out_order_code","")>0){
						if(!AppConst.MANAGE_CODE_WYKL.equals(orderMap.get("small_seller_code"))) {
							result.setResultCode(916401231);
							result.setResultMessage(bInfo(916401231));
							return result;
						}						
					}
					
					String orderCode=OrderHelper.upOrderCodeByOutCode( input.getOrder_code());
					ApiCancelOrderResult rr = os.CancelOrderForList(orderCode,getUserCode());					

					if(rr.getResultCode()==1){  //取消成功,向取消表中添加一条记录
						String sql = "SELECT oc_orderinfo.order_code,oc_orderinfo.buyer_code,oc_orderinfo.out_order_code,oc_orderinfo.small_seller_code," +
								 "oc_orderinfo.order_source,oc_orderinfo.order_money, oc_orderadress.address, oc_orderinfo.product_name " +
								 " from oc_orderinfo left join oc_orderadress " +
								 " on oc_orderinfo.order_code = oc_orderadress.order_code " +
								 " where oc_orderinfo.order_code=:order_code ";
						Map<String, Object> map= DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code",orderCode));
						String order_code= (String)map.get("order_code");
						String buyer_code= (String)map.get("buyer_code");
						String out_order_code= (String)map.get("out_order_code");
						String small_seller_code = (String)map.get("small_seller_code");
						String order_money = map.get("order_money").toString(); //订单金额
						String address = (String) map.get("address"); //收货信息
						String product_name = (String) map.get("product_name"); //所有商品名称
						String now=DateUtil.getSysDateTimeString();
						DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",out_order_code,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode());
						if(!"4497153900010001".equals(order_status)){
							//向商家发送微信通知
							new WxGZHService().sendWxGZHCancelOrder(small_seller_code, order_code, order_money, address, product_name);
							//做分销人金额信息统计更新
							//updateDistributionInfos(order_code); 
						}
						//取消的是网易考拉订单的时候，需要同步上传网易考拉
						if(AppConst.MANAGE_CODE_WYKL.equals(small_seller_code)) {
							cancelKaolaOrder(order_code);
						}
						new CouponService().reWriteGiftVoucherToLD(rr.getReWriteLD(), "R"); //取消发货回写礼金券给LD						
					}
					
					result.setResultCode(rr.getResultCode());
					result.setResultMessage(rr.getResultMessage());
				}
				//LD订单取消需要走专用的接口
				else {
					String sOrderCode = input.getOrder_code();					
					RsyncRecordTvOrderStat rsyncRecordTvOrderStat = new RsyncRecordTvOrderStat();
					rsyncRecordTvOrderStat.upRsyncRequest().setOrd_id(sOrderCode);
					rsyncRecordTvOrderStat.upRsyncRequest().setOrd_seq("0");
					rsyncRecordTvOrderStat.doRsync();
					if(rsyncRecordTvOrderStat.getResponseObject() != null && rsyncRecordTvOrderStat.getResponseObject().getResult() != null && rsyncRecordTvOrderStat.getResponseObject().getResult().size() > 0 ) {
						//插入表 oc_order_cancel_h
						RsyncModelRecordTvOrderStat orderinfo = rsyncRecordTvOrderStat.getResponseObject().getResult().get(0);
						MDataMap cancelOrder = DbUp.upTable("oc_order_cancel_h").one("order_code",orderinfo.getORD_ID().toString(),"buyer_code",getUserCode(),"out_order_code",orderinfo.getORD_ID().toString());
						if(cancelOrder == null || cancelOrder.isEmpty()) {
							String order_code = orderinfo.getORD_ID().toString();
							String now=DateUtil.getSysDateTimeString();
							DbUp.upTable("oc_order_cancel_h").insert("order_code",input.getOrder_code(),"buyer_code",getUserCode(),"out_order_code",order_code,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode());
							//updateDistributionInfos(input.getOrder_code());
						}							
					}					
				}
			} catch (Exception e) {
				result.setResultCode(916401109);
				result.setResultMessage(bInfo(916401109));
			}
			
		return result;
	}
	}
	
	private void updateDistributionInfos(String order_code) {
		// TODO Auto-generated method stub
		MDataMap one = DbUp.upTable("oc_orderdetail").one("order_code",order_code);
		String distribution_member_id = one.get("distribution_member_id");
		if(one!=null&&StringUtils.isNotBlank(distribution_member_id)) {
		    BigDecimal sku_price =BigDecimal.valueOf(Double.parseDouble(one.get("sku_price").toString()));
		    String sku_num = one.get("sku_num").toString();
			Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
		    if(dataSqlOne!=null) {
				Integer  order_fail_num = Integer.parseInt(dataSqlOne.get("order_fail_num").toString());
				BigDecimal order_fail_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("order_fail_value").toString()));
				order_fail_num = order_fail_num+1;
		    	BigDecimal allSkuPrice = sku_price.multiply(BigDecimal.valueOf(Double.parseDouble(sku_num)));
				order_fail_value=order_fail_value.add(allSkuPrice);
				order_fail_value.setScale(2,BigDecimal.ROUND_HALF_DOWN);
				DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("order_fail_num",order_fail_num.toString(),"order_fail_value",order_fail_value.toString(),"distribution_member_id",distribution_member_id), "order_fail_num,order_fail_value", "distribution_member_id"); 
		    }
		}
	}

	private void updateCollageStatus(String orderCode) {
		//判断是否为拼团单
		MDataMap ptMap = DbUp.upTable("sc_event_collage_item").onePriLib("collage_ord_code", orderCode);
		if(ptMap != null) {
			String collageCode = ptMap.get("collage_code");
			String memberType = ptMap.get("collage_member_type");
			if("449748310001".equals(memberType)) {//拼团单为团长创建
				//修改拼团主表状态为拼团失败
				DbUp.upTable("sc_event_collage").dataUpdate(new MDataMap("collage_code", collageCode, "collage_status", "449748300003"), "collage_status", "collage_code");
			}
		}
	}
	
	/**
	 * 将网易考拉取消订单的任务写入定时
	 * @param order_code
	 */
	private void cancelKaolaOrder(String order_code) {
		//写入定时任务，定时执行返还微公社金额
		MDataMap jobMap = new MDataMap();
		jobMap.put("uid", WebHelper.upUuid());
		jobMap.put("exec_code", WebHelper.upCode("ET"));
		jobMap.put("exec_type", "449746990011");
		jobMap.put("exec_info", order_code);
		jobMap.put("create_time", DateUtil.getSysDateTimeString());
		jobMap.put("begin_time", "");
		jobMap.put("end_time", "");
		jobMap.put("exec_time", DateUtil.getSysDateTimeString());
		jobMap.put("flag_success","0");
		jobMap.put("remark", "ApiCancelOrderForFamily line 204");
		jobMap.put("exec_number", "0");
		DbUp.upTable("za_exectimer").dataInsert(jobMap);
	}
}
