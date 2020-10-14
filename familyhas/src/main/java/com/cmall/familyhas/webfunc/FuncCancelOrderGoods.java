package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.service.WxGZHService;
import com.cmall.familyhas.service.CouponService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.duohuozhu.support.OrderForDuohuozhuSupport;
import com.cmall.groupcenter.homehas.RsyncKaoLaSupport;
import com.cmall.groupcenter.service.OrderForLD;
import com.cmall.groupcenter.third.model.GroupRefundInput;
import com.cmall.groupcenter.third.model.GroupRefundResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.ordercenter.service.money.ReturnMoneyResult;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmasorder.channel.service.PorscheOrderService;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ApiCallSupport;

/**
 * 商户后台取消发货
 * @author jlin
 *
 */
public class FuncCancelOrderGoods extends RootFunc {
	
	// 小卖家编号为空的时候也默认是LD
	String[] ldSellerCodes = new String[]{"SI2003","SI2009",""};

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String order_code = mEditMaps.get("uid");
		/**
		 * 需要校验是否是拼团订单，如果是拼团订单需要校验是否拼团成功，（拼团未成功的订单不允许取消发货，20190428 NG 新增逻辑）
		 */
		MDataMap pinOrder = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",order_code);
		if(pinOrder != null && !pinOrder.isEmpty()) {
			String collage_code = pinOrder.get("collage_code");//获取拼团编码，查询拼团状态
			MDataMap collageMap = DbUp.upTable("sc_event_collage").one("collage_code",collage_code);
			if(collageMap != null && !collageMap.isEmpty()) {
				String collage_status = collageMap.get("collage_status");
				if(!"449748300002".equals(collage_status)) {//449748300002 是拼团成功状态，此处去非，非拼团成功的，均返回不可取消发货
					mResult.setResultCode(0);
					mResult.setResultMessage("拼团未成功订单不允许取消发货！！！");
					return mResult;
				}
			}
		}
		
		OrderService orderService = new OrderService();
		
		String loginname=UserFactory.INSTANCE.create().getUserCode();
		
		MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		String small_seller_code = orderMap.get("small_seller_code");
		String out_order_code=orderMap.get("out_order_code");
		String seller_code = orderMap.get("seller_code");
		String buyer_code = orderMap.get("buyer_code");
		String order_status = orderMap.get("order_status");
		
		if(!StringUtils.startsWith(order_status, "4497153900010002")){
			mResult.inErrorMessage(916421187);
			return mResult;
		}
		
		// 多货主订单同步调用取消接口
		if("4497471600430002".equals(orderMap.get("delivery_store_type"))) {
			MWebResult cancelRes = new OrderForDuohuozhuSupport().cancelOrder(order_code);
			if(!cancelRes.upFlagTrue()) {
				MDataMap orderDuohz = DbUp.upTable("oc_order_duohz").one("order_code",order_code);
				if("P01".equals(orderDuohz.get("cod_status"))){
					cancelRes.setResultMessage("您的订单当前正在出库中，不支持取消订单！");
				}
				return cancelRes;
			}
		}
		
//		//若是LD订单，需取消LD订单信息
		if(ArrayUtils.contains(ldSellerCodes,small_seller_code) && StringUtils.isNotBlank(out_order_code)) {
			
			OrderForLD orderForLD = new OrderForLD();
			mResult=orderForLD.cancelOrderForLD(out_order_code, loginname, MemberConst.MANAGE_CODE_HOMEHAS);
			if(!mResult.upFlagTrue()){
				return mResult;
			}
		}
		//此处留坑，不做事务
		RootResult res = cancelOrderByShop(order_code,loginname);
//		if(res.getResultCode()==1&&!StringUtils.equals("SF03KJT", small_seller_code)){
		if(res.getResultCode()==1&&!new PlusServiceSeller().isKJSeller(small_seller_code)){
			// 取消订单时同步处理三方异常订单的数据
			List<MDataMap> list = DbUp.upTable("oc_order_sanfang_exception").queryByWhere("hjy_order_code", order_code, "delete_flag", "0");
			for(MDataMap m : list) {
				m.put("delete_flag", "1");
				m.put("update_time", FormatHelper.upDateTime());
				m.put("updator", UserFactory.INSTANCE.create().getUserCode());
				DbUp.upTable("oc_order_sanfang_exception").dataUpdate(m, "delete_flag,update_time,updator", "zid");
			}
			
			//退返微公社部分
			MDataMap payInfo=DbUp.upTable("oc_order_pay").one("order_code",order_code,"pay_type","449746280009");
			if(payInfo!=null&&!payInfo.isEmpty()){
				GroupRefundInput groupRefundInput = new GroupRefundInput();
				groupRefundInput.setTradeCode(payInfo.get("pay_sequenceid"));
				groupRefundInput.setMemberCode(buyer_code);
				groupRefundInput.setRefundMoney(payInfo.get("payed_money"));
				groupRefundInput.setOrderCode(order_code);
				groupRefundInput.setRefundTime(DateUtil.getSysDateTimeString());
				groupRefundInput.setRemark("取消发货");
				groupRefundInput.setBusinessTradeCode(payInfo.get("pay_sequenceid"));//一个流水值退一次
//				new GroupPayService().groupRefundSome(groupRefundInput, seller_code);
				
				ApiCallSupport<GroupRefundInput, GroupRefundResult> apiCallSupport=new ApiCallSupport<GroupRefundInput, GroupRefundResult>();
				GroupRefundResult refundResult = null;
				try {
					refundResult=apiCallSupport.doCallApi(
							bConfig("xmassystem.group_pay_url"),
							bConfig("xmassystem.group_pay_refund_face"),
							bConfig("xmassystem.group_pay_key"),
							bConfig("xmassystem.group_pay_pass"), groupRefundInput,
							new GroupRefundResult());
				} catch (Exception e) {
					//此处暂时流程，退款失败，不影响总流程
					e.printStackTrace();
				}
				
			}
			
			//微信通知商户有用户取消订单
			if(!"4497153900010001".equals(order_status)){
				String sql = "SELECT oc_orderinfo.order_code,oc_orderinfo.small_seller_code," +
						 " oc_orderinfo.order_money, oc_orderadress.address, oc_orderinfo.product_name " +
						 " from oc_orderinfo left join oc_orderadress " +
						 " on oc_orderinfo.order_code = oc_orderadress.order_code " +
						 " where oc_orderinfo.order_code=:order_code ";
				Map<String, Object> map= DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code",order_code));
				String order_money = map.get("order_money").toString(); //订单金额
				String address = (String) map.get("address"); //收货信息
				String product_name = (String) map.get("product_name"); //所有商品名称
				new WxGZHService().sendWxGZHCancelOrder(small_seller_code, order_code, order_money, address, product_name);
			}
		}
		//添加分销统计处理
		
		//updateDistributionInfos(order_code);
		mResult.setResultCode(res.getResultCode());
		mResult.setResultMessage(res.getResultMessage());
		
		return mResult;
	}
	//添加分销统计处理
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
	public RootResult cancelOrderByShop(String order_code,String operater) {
		RootResult ret = new RootResult();

		FlowBussinessService fs = new FlowBussinessService();

		MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		
		if(orderMap==null||"4497153900010006".equals(orderMap.get("order_status"))||"4497153900010005".equals(orderMap.get("order_status"))){
			ret.setResultCode(939302001);
			ret.setResultMessage(bInfo(939302001, order_code));
			return ret;
		}
		
		// 如果是考拉订单并且考拉方面未取消则调用考拉的取消订单接口
		MDataMap kaola_order = DbUp.upTable("oc_order_kaola_list").one("order_code",order_code);
		if(kaola_order != null && !"".equals(kaola_order.get("out_order_code")) && !"7".equals(kaola_order.get("status")) && !"6".equals(kaola_order.get("status"))) {
			try {
				TreeMap<String, String> params = new TreeMap<String, String>();
				//reasonId 枚举如下 1 收货人信息有误 2 商品数量或款式需调整 3 有更优惠的购买方案 4 考拉一直未发货 5 商品缺货 6 我不想买了 7 其他原因
				params.put("thirdpartOrderId", order_code);
				params.put("reasonId", "6");
				params.put("remark", "");
				String result = RsyncKaoLaSupport.doPostRequest("cancelOrder", "channelId", params);
				JSONObject resultJson = JSON.parseObject(result);
				if(resultJson.getInteger("recCode") != 200) {
					ret.setResultCode(0);
					ret.setResultMessage("取消考拉订单失败，错误码："+resultJson.getInteger("recCode"));
					return ret;
				}
			} catch (Exception e) {
				ret.setResultCode(0);
				ret.setResultMessage("取消考拉订单失败");
				return ret;
			}
		}
		
		String flowBussinessUid = orderMap.get("uid");
		String fromStatus = orderMap.get("order_status");
		String small_seller_code = orderMap.get("small_seller_code");
		String toStatus = "4497153900010006";
		String flowType = "449715390008";
//		ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, small_seller_code, "update by shop", new MDataMap("order_code",order_code));
		
		if(kaola_order != null && !"".equals(kaola_order.get("out_order_code")) && !"7".equals(kaola_order.get("status")) && !"6".equals(kaola_order.get("status"))) {
			// 考拉订单取消时更新为待审核的中间状态，最终以考拉订单状态同步定时为准
			// 退款单以考拉订单状态变更为订单交易失败时为准
			toStatus = "4497153900010008";
			ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, "update by shop", new MDataMap("order_code",order_code));
			return ret;
		} else {
			ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, "update by shop", new MDataMap("order_code",order_code));
		}
		
		// 考拉订单取消暂不生成退款单，以定时任务为准
		if (ret.getResultCode() == 1) {
			//根据状态判断，如果是到4497153900010006
			if("4497153900010006".equals(toStatus)&&DbUp.upTable("fh_agent_order_detail").count("order_code",orderMap.get("order_code"))>0 && DbUp.upTable("za_exectimer").count("exec_info",orderMap.get("order_code"),"exec_type","449746990029")<=0) {
				JobExecHelper.createExecInfo("449746990029", orderMap.get("order_code"), DateUtil.getSysDateTimeString());
			}
			
			//生成退款单
			CreateMoneyService createMoneyService = new CreateMoneyService();
			ReturnMoneyResult rm; 
			if(orderMap.containsKey("order_source") && "449715190014".equals(orderMap.get("order_source"))){
				//多彩生成退款单走另一个方法
				rm = createMoneyService.creatReturnMoney(order_code,operater,"客服取消多彩订单");
			}else{
				rm = createMoneyService.creatReturnMoney(order_code);
			}
			if(rm.getList() != null && rm.getList().size() > 0) {
				new CouponService().reWriteGiftVoucherToLD(rm.getList(), "R"); //取消发货回写礼金券给LD
			}
			
			//取消成功 返还渠道商预存款
			//判断 如果为渠道商订单 则加预存款
			if("449715190034".equals(orderMap.get("order_source"))) {
				MDataMap orderChannel = DbUp.upTable("oc_order_channel").one("order_code", order_code);
				MWebResult mWebResult = new PorscheOrderService().cancelOrderReturnAdvanceBalance(orderChannel.get("channel_seller_code"), orderMap.get("order_money"));
				if(mWebResult.upFlagTrue()) {
					MDataMap one = DbUp.upTable("uc_channel_sellerinfo").one("channel_seller_code", orderChannel.get("channel_seller_code"));
					//记录日志
					String logRemark = "取消订单还原，第三方订单号：" + orderMap.get("out_order_code");
					new PorscheOrderService().insertChannelMoneyLog(orderChannel.get("channel_seller_code"), "449748420002", 
							orderMap.get("order_money"), one.get("advance_balance"), order_code, logRemark);
				}
			}
		}else{
			WebHelper.errorMessage(order_code, "addorder", 1,"cancelOrderByShop on ChangeFlow", ret.getResultMessage(),null);
		}
		
		return ret;
	}
}
