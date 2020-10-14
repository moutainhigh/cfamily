package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForCancelShipmentsInput;
import com.cmall.familyhas.api.result.ApiForCancelShipmentsResult;
import com.cmall.familyhas.service.WxGZHService;
import com.cmall.familyhas.service.CouponService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.FamilyConfig;
import com.cmall.groupcenter.duohuozhu.support.OrderForDuohuozhuSupport;
import com.cmall.groupcenter.homehas.RsyncRecordTvOrderStat;
import com.cmall.groupcenter.homehas.model.RsyncModelRecordTvOrderStat;
import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.ordercenter.service.money.ReturnMoneyResult;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.message.SendMessageBase;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 取消发货
 * <p>
 * 目前只支持第三方商户的订单
 * <p>
 * 
 * @author jlin
 *
 */
public class ApiForCancelShipments extends RootApiForToken<ApiForCancelShipmentsResult, ApiForCancelShipmentsInput> {

	static Object lock = new Object();
	public ApiForCancelShipmentsResult Process(ApiForCancelShipmentsInput inputParam, MDataMap mRequestMap) {
		synchronized (lock) {
		ApiForCancelShipmentsResult result = new ApiForCancelShipmentsResult();
		
		String order_code = inputParam.getOrderCode();
		String reason = inputParam.getReason();
		String buyer_code=getUserCode();
		String remark=(String)DbUp.upTable("oc_return_goods_reason").dataGet("return_reson", "return_reson_code=:return_reson_code", new MDataMap("return_reson_code",reason));
		remark="[取消发货]"+remark;
		
		//处理TV品订单		
		if(!"OS".equals(order_code.substring(0, 2)) && !"DD".equals(order_code.substring(0, 2)) && !"HH".equals(order_code.substring(0, 2))) {			
			MDataMap cancelOrder = DbUp.upTable("oc_order_cancel_h").one("order_code",order_code,"buyer_code",buyer_code,"out_order_code",order_code);
			if(cancelOrder == null || cancelOrder.isEmpty()) {
				RsyncRecordTvOrderStat rsyncRecordTvOrderStat = new RsyncRecordTvOrderStat();
				rsyncRecordTvOrderStat.upRsyncRequest().setOrd_id(order_code);
				rsyncRecordTvOrderStat.upRsyncRequest().setOrd_seq("0");
				rsyncRecordTvOrderStat.doRsync();
				if(rsyncRecordTvOrderStat.getResponseObject() != null && rsyncRecordTvOrderStat.getResponseObject().getResult() != null && rsyncRecordTvOrderStat.getResponseObject().getResult().size() > 0 ) {
					//插入表 oc_order_cancel_h
					RsyncModelRecordTvOrderStat orderinfo = rsyncRecordTvOrderStat.getResponseObject().getResult().get(0);
					String orderCode = orderinfo.getORD_ID().toString();					
					String now=DateUtil.getSysDateTimeString();					
					DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",orderCode,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode(),"reason_code","C0T");
					//做分销人金额信息统计更新
					//updateDistributionInfos(order_code);		
				}
			} else {
				result.setResultCode(916401143);
				result.setResultMessage(bInfo(916401143));
			}
			//需要校验该订单是否是分销商品订单，如果是，需要写入定时
			if(result.getResultCode() == 1 && DbUp.upTable("fh_agent_order_detail").count("order_code",order_code)>0 && DbUp.upTable("za_exectimer").count("exec_info",order_code,"exec_type","449746990029") <= 0) {
				JobExecHelper.createExecInfo("449746990029", order_code, DateUtil.addMinute(5));//插入定时任务，五分钟后执行
			}
			return result;
		}
		
		MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",order_code,"buyer_code",buyer_code);
		if(orderInfo==null || orderInfo.isEmpty()){
			result.setResultCode(916422141);
			result.setResultMessage(bInfo(916422141, order_code));
			return result;
		}
		
		// 取消时锁定一下订单，防止订单同时又在做异步下单逻辑
		String lockKey = KvHelper.lockCodes(20, Constants.LOCK_ORDER_UPDATE + order_code);
		if(StringUtils.isBlank(lockKey)) {
			// 订单正在操作中，请稍候重试！
			result.setResultCode(918590001);
			result.setResultMessage(TopUp.upLogInfo(918590001));
			return result;
		}
		
		String small_seller_code=orderInfo.get("small_seller_code");
		String order_status=orderInfo.get("order_status");
		String out_order_code=orderInfo.get("out_order_code"); 
		boolean directCancel = true;
		if("SI2003".equals(small_seller_code) && !"".equals(out_order_code)) {
			directCancel = false;
		}
		//判断订单状态
		if(!"4497153900010002".equals(order_status)){
			result.setResultCode(916422142);
			result.setResultMessage(bInfo(916422142, order_code));
			return result;
		}
		
		//判断是否为第三方商户的订单
		
//		if(StringUtils.startsWith(small_seller_code, "SF031")){
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		if(!AppConst.MANAGE_CODE_WYKL.equals(small_seller_code)) {
			if(StringUtils.isNotBlank(WebHelper.getSellerType(small_seller_code)) || directCancel){
				// 多货主订单同步调用取消接口
				if("4497471600430002".equals(orderInfo.get("delivery_store_type"))) {
					MWebResult cancelRes = new OrderForDuohuozhuSupport().cancelOrder(order_code);
					if(!cancelRes.upFlagTrue()) {
						MDataMap orderDuohz = DbUp.upTable("oc_order_duohz").one("order_code",order_code);
						if("P01".equals(orderDuohz.get("cod_status"))){
							cancelRes.setResultMessage("您的订单当前正在出库中，不支持取消订单！");
						}
						
						result.setResultCode(cancelRes.getResultCode());
						result.setResultMessage(cancelRes.getResultMessage());
						return result;
					}
				}
				
				//生成退款单和退还微公社余额
				//此处留坑，不做事务
				RootResult res = cancelOrder(orderInfo,remark);
				//做分销人金额信息统计更新
				//updateDistributionInfos(order_code);
//				if(res.getResultCode()==1){
//					//退返微公社部分
//					MDataMap payInfo=DbUp.upTable("oc_order_pay").one("order_code",order_code,"pay_type","449746280009");
//					if(payInfo!=null&&!payInfo.isEmpty()){
//						GroupRefundInput groupRefundInput = new GroupRefundInput();
//						groupRefundInput.setTradeCode(payInfo.get("pay_sequenceid"));
//						groupRefundInput.setMemberCode(buyer_code);
//						groupRefundInput.setRefundMoney(payInfo.get("payed_money"));
//						groupRefundInput.setOrderCode(order_code);
//						groupRefundInput.setRefundTime(DateUtil.getSysDateTimeString());
//						groupRefundInput.setRemark("取消发货");
//						groupRefundInput.setBusinessTradeCode(payInfo.get("pay_sequenceid"));//一个流水值退一次
//						ApiCallSupport<GroupRefundInput, GroupRefundResult> apiCallSupport=new ApiCallSupport<GroupRefundInput, GroupRefundResult>();
//						try {
//							apiCallSupport.doCallApi(bConfig("xmassystem.group_pay_url"),bConfig("xmassystem.group_pay_refund_face"),bConfig("xmassystem.group_pay_key"),bConfig("xmassystem.group_pay_pass"), groupRefundInput,new GroupRefundResult());
//						} catch (Exception e) {
//							//此处暂时流程，退款失败，不影响总流程
//							e.printStackTrace();
//						}
//					}
//				}
				
				//LD订单需要调用LD取消订单接口
				if("SI2003".equals(small_seller_code)) {
					String now=DateUtil.getSysDateTimeString();
					MDataMap reasonMap=DbUp.upTable("oc_return_goods_reason").one("after_sales_type","449747660007","status","449747660005","return_reson",reason);
					if(reasonMap != null && reasonMap.get("return_reson_code") != null) {
						DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",orderInfo.get("out_order_code"),"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode(),"reason_code",reasonMap.get("return_reson_code").toString());
					} else {
						DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",orderInfo.get("out_order_code"),"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode(),"reason_code",reason);
					}				
				}
				
				result.setResultMessage("取消发货成功");
				//发送提醒短信
				String mobile=(String)DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",buyer_code));
				String smsStr=FormatHelper.formatString(bConfig("familyhas.sms_cancel_shipments"), order_code);
				new SendMessageBase().sendMessage(mobile, smsStr,"4497467200020006");
				//微信通知商户有用户取消订单
				if(!"4497153900010001".equals(order_status)){
					String sql = "SELECT oc_orderinfo.order_code,oc_orderinfo.small_seller_code," +
							 " oc_orderinfo.order_money, oc_orderadress.address, oc_orderinfo.product_name " +
							 " from oc_orderinfo left join oc_orderadress " +
							 " on oc_orderinfo.order_code = oc_orderadress.order_code " +
							 " where oc_orderinfo.order_code=:order_code ";
					Map<String, Object> map = DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code",order_code));
					String order_money = map.get("order_money").toString(); //订单金额
					String address = (String) map.get("address"); //收货信息
					String product_name = (String) map.get("product_name"); //所有商品名称
					new WxGZHService().sendWxGZHCancelOrder(small_seller_code, order_code, order_money, address, product_name);
				}
			} else if("SI2003".equals(small_seller_code) && !directCancel) { //App下的TV品订单，取消发货时将订单状态改为取消中
				String now=DateUtil.getSysDateTimeString();
				MDataMap cancelOrder = DbUp.upTable("oc_order_cancel_h").one("order_code",order_code,"buyer_code",buyer_code,"out_order_code",out_order_code);
				if(cancelOrder == null || cancelOrder.isEmpty()) {
					RsyncRecordTvOrderStat rsyncRecordTvOrderStat = new RsyncRecordTvOrderStat();
					rsyncRecordTvOrderStat.upRsyncRequest().setOrd_id(out_order_code);
					rsyncRecordTvOrderStat.upRsyncRequest().setOrd_seq("0");
					rsyncRecordTvOrderStat.doRsync();
					if(rsyncRecordTvOrderStat.getResponseObject() != null && rsyncRecordTvOrderStat.getResponseObject().getResult() != null && rsyncRecordTvOrderStat.getResponseObject().getResult().size() > 0 ) {
						MDataMap reasonMap=DbUp.upTable("oc_return_goods_reason").one("after_sales_type","449747660007","status","449747660005","return_reson",reason);
						if(reasonMap != null && reasonMap.get("return_reson_code") != null) {
							DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",out_order_code,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode(),"reason_code",reasonMap.get("return_reson_code").toString());
						} else {
							DbUp.upTable("oc_order_cancel_h").insert("order_code",order_code,"buyer_code",buyer_code,"out_order_code",out_order_code,"call_flag","1","create_time",now,"update_time",now,"canceler",getUserCode(),"reason_code",reason);
						}
						
						RootResult ret = new RootResult();
						FlowBussinessService fs = new FlowBussinessService();
						String flowBussinessUid = orderInfo.get("uid");
						String fromStatus = orderInfo.get("order_status");
						String operater = orderInfo.get("buyer_code");		
						String toStatus = "4497153900010008";//状态改为取消中
						String flowType = "449715390008";
						
						ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, remark, new MDataMap("order_code",order_code));
						if (ret.getResultCode() == 1) {	
							
						} else {
							WebHelper.errorMessage(order_code, "cancelOrder", 1,"cancelOrder on ChangeFlow", ret.getResultMessage(),null);
						}
					} else {
						result.setResultCode(916422160);
						result.setResultMessage(bInfo(916422160));
					}
				} else {
					result.setResultCode(916422161);
					result.setResultMessage(bInfo(916422161));
				}									
			} else{
				result.setResultCode(916422142);
				result.setResultMessage(bInfo(916422142, order_code));
			}
		} else {
			MDataMap kaola_order = DbUp.upTable("oc_order_kaola_list").one("order_code",order_code);
			if(kaola_order != null && !"".equals(kaola_order.get("out_order_code")) && !"7".equals(kaola_order.get("status")) && !"6".equals(kaola_order.get("status"))) {
				RootResult ret = new RootResult();
				FlowBussinessService fs = new FlowBussinessService();
				String flowBussinessUid = orderInfo.get("uid");
				String fromStatus = orderInfo.get("order_status");
				String operater = orderInfo.get("buyer_code");		
				String toStatus = "4497153900010008";//状态改为取消中
				String flowType = "449715390008";
				
				ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, remark, new MDataMap("order_code",order_code));
				if (ret.getResultCode() == 1) {	
					
				} else {
					WebHelper.errorMessage(order_code, "cancelOrder", 1,"cancelOrder on ChangeFlow", ret.getResultMessage(),null);
				}
				cancelShipmentsKaolaOrder(order_code);
			} else {
				//考拉订单下单失败的话，直接取消该订单
				RootResult res = cancelOrder(orderInfo,"网易考拉订单取消发货");
//				if(res.getResultCode()==1){
//					//退返微公社部分
//					MDataMap payInfo=DbUp.upTable("oc_order_pay").one("order_code",order_code,"pay_type","449746280009");
//					if(payInfo!=null&&!payInfo.isEmpty()){
//						GroupRefundInput groupRefundInput = new GroupRefundInput();
//						groupRefundInput.setTradeCode(payInfo.get("pay_sequenceid"));
//						groupRefundInput.setMemberCode(orderInfo.get("buyer_code"));
//						groupRefundInput.setRefundMoney(payInfo.get("payed_money"));
//						groupRefundInput.setOrderCode(order_code);
//						groupRefundInput.setRefundTime(DateUtil.getSysDateTimeString());
//						groupRefundInput.setRemark("取消发货");
//						groupRefundInput.setBusinessTradeCode(payInfo.get("pay_sequenceid"));//一个流水值退一次
//						ApiCallSupport<GroupRefundInput, GroupRefundResult> apiCallSupport=new ApiCallSupport<GroupRefundInput, GroupRefundResult>();
//						try {
//							apiCallSupport.doCallApi(bConfig("xmassystem.group_pay_url"),bConfig("xmassystem.group_pay_refund_face"),bConfig("xmassystem.group_pay_key"),bConfig("xmassystem.group_pay_pass"), groupRefundInput,new GroupRefundResult());
//						} catch (Exception e) {
//							//此处暂时流程，退款失败，不影响总流程
//							e.printStackTrace();
//						}
//					}
//				}
			}
		}
		
		// 操作执行完成解除锁定
		KvHelper.unLockCodes(lockKey, Constants.LOCK_ORDER_UPDATE + orderInfo.get("order_code"));
		
		return result;
		}
	}
	
	/***
	 * 取消发货
	 * @param order_code
	 * @param operater
	 * @param remark
	 * @return
	 */
	public RootResult cancelOrder(MDataMap orderInfo,String remark) {
		
		RootResult ret = new RootResult();
		FlowBussinessService fs = new FlowBussinessService();
		String flowBussinessUid = orderInfo.get("uid");
		String fromStatus = orderInfo.get("order_status");
		String operater = orderInfo.get("buyer_code");
		String order_code = orderInfo.get("order_code");		
		String toStatus = "4497153900010006";
		String flowType = "449715390008";
		ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, remark, new MDataMap("order_code",order_code));
		
		if (ret.getResultCode() == 1) {			
			//生成退款单
			CreateMoneyService createMoneyService = new CreateMoneyService();
			ReturnMoneyResult rm = createMoneyService.creatReturnMoney(order_code,operater,remark);
			if(rm.getList() != null && rm.getList().size() > 0) {
				new CouponService().reWriteGiftVoucherToLD(rm.getList(), "R"); //取消发货回写礼金券给LD
			}
			//此处新增取消订单定时任务。仅限APP自营品取消
			if(DbUp.upTable("fh_agent_order_detail").count("order_code",order_code)>0 && DbUp.upTable("za_exectimer").count("exec_info",order_code,"exec_type","449746990029") <= 0) {
				JobExecHelper.createExecInfo("449746990029", order_code, DateUtil.addMinute(5));//插入定时任务，五分钟后执行
			}
		}else{
			WebHelper.errorMessage(order_code, "cancelOrder", 1,"cancelOrder on ChangeFlow", ret.getResultMessage(),null);
		}
		return ret;
	}
	
	/**
	 * 将网易考拉取消发货订单的任务写入定时
	 * @param order_code
	 */
	private void cancelShipmentsKaolaOrder(String order_code) {
		//写入定时任务，定时执行返还微公社金额
		MDataMap jobMap = new MDataMap();
		jobMap.put("uid", WebHelper.upUuid());
		jobMap.put("exec_code", WebHelper.upCode("ET"));
		jobMap.put("exec_type", "449746990012");
		jobMap.put("exec_info", order_code);
		jobMap.put("create_time", DateUtil.getSysDateTimeString());
		jobMap.put("begin_time", "");
		jobMap.put("end_time", "");
		jobMap.put("exec_time", DateUtil.getSysDateTimeString());
		jobMap.put("flag_success","0");
		jobMap.put("remark", "ApiForCancelShipments line 207");
		jobMap.put("exec_number", "0");
		DbUp.upTable("za_exectimer").dataInsert(jobMap);
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
}
