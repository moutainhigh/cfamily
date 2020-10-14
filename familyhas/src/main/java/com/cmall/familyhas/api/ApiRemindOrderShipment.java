package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cmall.familyhas.api.input.ApiRemindOrderShipmentInput;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrderDetail;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户提醒发货接口
 * @author fq
 *
 */
public class ApiRemindOrderShipment extends RootApiForToken<RootResult,ApiRemindOrderShipmentInput>{
	
	/**
	 * 创建订单之后 24小时  之后  才能提醒发货
	 */
	public static final Integer CREATEORDER_LATER = 24;

	/**
	 * 上次提醒发货 12小时之后  才能提醒发货
	 */
	public static final Integer PRE_REMINDORDERSHIPMENT_LATER = 12;
	
	

	@Override
	public RootResult Process(ApiRemindOrderShipmentInput inputParam, MDataMap mRequestMap) {
		
		RootResult result = new RootResult();
		String order_code = inputParam.getOrder_code();
		MDataMap info = DbUp.upTable("oc_order_remind_shipment").one("order_code",order_code);
		String sysTime = DateHelper.upNow();
		if("DD".equals(order_code.substring(0, 2)) || "HH".equals(order_code.substring(0, 2))) {
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			//判断用户是否操作自己的订单
			if(getUserCode().equals( orderInfo.get("buyer_code")) ) {				
				if( "4497153900010002".equals(orderInfo.get("order_status"))) {
					if(null != info) {
						//已经提醒过。判断之前的提醒时间是否在12小时之后
						String remindTime = info.get("remind_time");
						try {
							int hoursBetween = DateHelper.hoursBetween(remindTime, sysTime);
							if(hoursBetween >= PRE_REMINDORDERSHIPMENT_LATER ) {
								//如果在上次提醒12小时后，则更新提醒发货的时间
								info.put("remind_time", sysTime);
								DbUp.upTable("oc_order_remind_shipment").update(info);
							} else {
								result.setResultCode(0);
								result.setResultMessage("已经提醒过，"+(PRE_REMINDORDERSHIPMENT_LATER-hoursBetween)+"小时后再试");
							}
						} catch (ParseException e) {
							result.setResultCode(0);
							result.setResultMessage("提醒失败！！");
							e.printStackTrace();
						}
					} else{
						//如果没有提醒过。则判断提醒的时间是否在创建订单24小时之后（创建订单24小时后才能提醒发货）
						
						if(null != orderInfo) {
							/*
							 * 449716200001	在线支付
							 * 449716200002	货到付款
							 * 获取支付类型，如果是在线支付，则以支付时间为准，如果是货到付款，则以创建订单的时间为准
							 */
							String compareTime = "";
							String pay_type = orderInfo.get("pay_type");
							if ("449716200002".equals(pay_type)) {//货到付款
								compareTime = orderInfo.get("create_time");
							} else {//非货到付款的订单，都认为是在线支付
								MDataMap orderPayInfo = DbUp.upTable("oc_order_pay").one("order_code",order_code);
								if(null != orderPayInfo) {
									compareTime = orderPayInfo.get("create_time");//订单支付时间
								} else {//如果订单状态是代发货，且是在线支付的订单，如果没有支付信息，则以订单创建时间为准
									compareTime = orderInfo.get("create_time");//订单创建时间
								}
							}
							
							try {
								int hoursBetween = DateHelper.hoursBetween(compareTime, sysTime);
								if(hoursBetween >= CREATEORDER_LATER ) {
									// 546需求添加,提醒发货时增加   账户,收货人姓名,发货商家名称,订单状态  四个字段,方便客服使用
									// 查询下单账户
									String login_name = "";
									MDataMap memberInfo = DbUp.upTable("mc_login_info").one("member_code",orderInfo.get("buyer_code"));
									if(null != memberInfo) {
										login_name = memberInfo.get("login_name");
									}
									// 收货人姓名
									String receive_person = "";
									MDataMap orderadressInfo = DbUp.upTable("oc_orderadress").one("order_code",order_code);
									if(null != orderadressInfo) {
										receive_person = orderadressInfo.get("receive_person");
									}
									// 发货商家名称
									String seller_company_name = "";
									MDataMap sellerInfo = DbUp.upTable("uc_sellerinfo").one("small_seller_code",orderInfo.get("small_seller_code"));
									if(null != sellerInfo) {
										seller_company_name = sellerInfo.get("seller_name");
									}
									
									DbUp.upTable("oc_order_remind_shipment").insert(
											"order_code",orderInfo.get("order_code"),
											"remind_time",sysTime,
											"create_time",sysTime,
											"login_name",login_name,
											"receive_person",receive_person,
											"seller_company_name",seller_company_name,
											"order_status",orderInfo.get("order_status")
											);
								} else {
									result.setResultCode(0);
									result.setResultMessage("客官，请"+(CREATEORDER_LATER-hoursBetween)+"小时后再提醒");
								}
							} catch (ParseException e) {
								result.setResultCode(0);
								result.setResultMessage("提醒失败！！");
								e.printStackTrace();
							}
							
						} else {
							result.setResultCode(0);
							result.setResultMessage("订单不存在");
						}						
					}
				} else {
					//订单状态不匹配
					result.setResultCode(0);
					result.setResultMessage("订单状态不匹配");
				}
			} else {
				//非本人订单
				result.setResultCode(0);
				result.setResultMessage("非本人订单，请勿操作");				
			}
		} else {
			//LD订单
			RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(order_code);
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
			rsyncGetThirdOrderDetail.doRsync();
			if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
				RsyncModelThirdOrderDetail orderdetail = rsyncGetThirdOrderDetail.getResponseObject().getResult().get(0);
				String order_status = convertOrderStatus(orderdetail.getOrd_stat());
				String create_time = convertDate(orderdetail.getEtr_date());
				if( "4497153900010002".equals(order_status)) {
					if(null != info) {
						//已经提醒过。判断之前的提醒时间是否在12小时之后
						String remindTime = info.get("remind_time");
						try {
							int hoursBetween = DateHelper.hoursBetween(remindTime, sysTime);
							if(hoursBetween >= PRE_REMINDORDERSHIPMENT_LATER ) {
								//如果在上次提醒12小时后，则更新提醒发货的时间
								info.put("remind_time", sysTime);
								DbUp.upTable("oc_order_remind_shipment").update(info);
							} else {
								result.setResultCode(0);
								result.setResultMessage("已经提醒过，"+(PRE_REMINDORDERSHIPMENT_LATER-hoursBetween)+"小时后再试");
							}
						} catch (ParseException e) {
							result.setResultCode(0);
							result.setResultMessage("提醒失败！！");
							e.printStackTrace();
						}
					} else {
						//如果没有提醒过。则判断提醒的时间是否在创建订单24小时之后（创建订单24小时周才能提醒发货）
						try {
							int hoursBetween = DateHelper.hoursBetween(create_time, sysTime);
							if(hoursBetween >= CREATEORDER_LATER ) {
								DbUp.upTable("oc_order_remind_shipment").insert(
										"order_code",order_code,
										"remind_time",sysTime,
										"create_time",sysTime											
										);
							} else {
								result.setResultCode(0);
								result.setResultMessage("客官，请"+(CREATEORDER_LATER-hoursBetween)+"小时后再提醒");
							}
						} catch (ParseException e) {
							result.setResultCode(0);
							result.setResultMessage("提醒失败！！");
							e.printStackTrace();
						}
					}
				} else {
					//订单状态不匹配
					result.setResultCode(0);
					result.setResultMessage("订单状态不匹配");
				}
			} else {
				result.setResultCode(0);
				result.setResultMessage("订单不存在");
			}				
		}		
			
		return result;
	}

	/**
	 * 转换LD订单状态
	 * @param order_status
	 * @return
	 */
	private String convertOrderStatus(String order_status) {
		switch(order_status) {
			case "01": return "4497153900010001";
			case "02": return "4497153900010002";
			case "03": return "4497153900010003";
			case "04": return "4497153900010006";
			case "05": return "4497153900010005";
			case "06": return "4497153900010008";
			default : return "";
		}
	}
	
	/**
	 * 日期格式转换
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	private String convertDate(Long date) {
		SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		if(date == null) {
			return format.format(new Date());
		}
		return format.format(date);
	}
}
