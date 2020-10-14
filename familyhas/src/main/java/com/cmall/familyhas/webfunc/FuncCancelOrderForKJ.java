package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.service.WxGZHService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.service.GroupPayService;
import com.cmall.groupcenter.service.OrderForLD;
import com.cmall.groupcenter.third.model.GroupRefundInput;
import com.cmall.groupcenter.third.model.GroupRefundResult;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ApiCallSupport;

/**
 * 商户后台跨境商户取消发货
 * @author jlin
 *
 */
public class FuncCancelOrderForKJ extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String order_code = mEditMaps.get("order_code");
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
		String remark = mEditMaps.get("remark");
		String remark_picurl1 = mEditMaps.get("remark_picurl1");
		
		if(StringUtils.isBlank(remark)||StringUtils.isBlank(remark_picurl1)){
			mResult.inErrorMessage(916423016);
			return mResult;
		}
		
		
		MUserInfo userInfo=UserFactory.INSTANCE.create();
		
//		OrderService orderService = new OrderService();
		MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		String small_seller_code = orderMap.get("small_seller_code");
//		String out_order_code=orderMap.get("out_order_code");
//		String seller_code = orderMap.get("seller_code");
		String buyer_code = orderMap.get("buyer_code");
		String order_status = orderMap.get("order_status");
		
		if(!StringUtils.startsWith(order_status, "4497153900010002")){
			mResult.inErrorMessage(916421187);
			return mResult;
		}
		
		//设置退款审核信息
		MDataMap remarkIMap=new MDataMap();
		remarkIMap.put("order_code", order_code);
		remarkIMap.put("remark", remark);
		remarkIMap.put("remark_picurl1", remark_picurl1);
		remarkIMap.put("create_user_code", userInfo.getUserCode());
		remarkIMap.put("create_user_name", userInfo.getRealName());
		remarkIMap.put("create_time", DateUtil.getSysDateTimeString());
		remarkIMap.put("out_order_code",orderMap.get("out_order_code")!=null?orderMap.get("out_order_code"):"");
		DbUp.upTable("oc_return_money_remark").dataInsert(remarkIMap);
		
		
		//此处留坑，不做事务
		RootResult res = cancelOrderByShop(order_code,userInfo.getUserCode());
//		if(res.getResultCode()==1&&!StringUtils.equals("SF03KJT", small_seller_code)){
		if(res.getResultCode()==1&&!StringUtils.equals("SF03KJT", small_seller_code)){
			
			
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
		
		
		mResult.setResultCode(res.getResultCode());
		mResult.setResultMessage(res.getResultMessage());
		
		return mResult;
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
		
		String flowBussinessUid = orderMap.get("uid");
		String fromStatus = orderMap.get("order_status");
		String small_seller_code = orderMap.get("small_seller_code");
		String toStatus = "4497153900010006";
		String flowType = "449715390008";
		ret = fs.ChangeFlow(flowBussinessUid, flowType, fromStatus,toStatus, operater, "update by cfamily", new MDataMap("order_code",order_code));
		
		if (ret.getResultCode() == 1) {
			
			CreateMoneyService createMoneyService = new CreateMoneyService();
			createMoneyService.creatReturnMoney(order_code);
			
		}else{
			WebHelper.errorMessage(order_code, "addorder", 1,"cancelOrderBycfamily on ChangeFlow", ret.getResultMessage(),null);
		}
		
		return ret;
	}
	
	
	//此处不牵扯运费的问题
	private void creatReturnMoney(String order_code_seq,String order_code) {
		
		BigDecimal expected_return_group_money=BigDecimal.ZERO;
		BigDecimal expected_return_money=BigDecimal.ZERO;
		
		List<MDataMap> failDetails=DbUp.upTable("oc_order_kjt_detail").queryAll("", "", "order_code_seq=:order_code_seq", new MDataMap("order_code_seq",order_code_seq));
		for (MDataMap mDataMap : failDetails) {
			String sku_code=mDataMap.get("sku_code");
			BigDecimal sku_num=new BigDecimal(mDataMap.get("sku_num"));
			MDataMap detailInfo=DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
			BigDecimal sku_price = new BigDecimal(detailInfo.get("sku_price"));
			BigDecimal group_price = new BigDecimal(detailInfo.get("group_price"));
			
			expected_return_money=expected_return_money.add(sku_num.multiply(sku_price));
			expected_return_group_money=expected_return_group_money.add(sku_num.multiply(group_price));
		}
		
		MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		
		
		String money_no = WebHelper.upCode("RTM");
		if(expected_return_money.compareTo(BigDecimal.ZERO)>0){
			//生成退款单
			MDataMap map = new MDataMap();
			map.put("return_money_code", money_no);
			map.put("return_goods_code", "");
			map.put("buyer_code", orderInfo.get("buyer_code"));
			map.put("seller_code", orderInfo.get("seller_code"));
			map.put("small_seller_code", orderInfo.get("small_seller_code"));
			map.put("contacts", "");//联系人
			map.put("status", "4497153900040003");
			map.put("return_money",expected_return_money.toString());
			map.put("mobile", (String)DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",orderInfo.get("buyer_code"))));
			map.put("create_time", DateUtil.getSysDateTimeString());
			map.put("poundage", "0");
			map.put("order_code", order_code);
			map.put("pay_method", "449716200001");
			map.put("online_money", expected_return_money.toString());
			map.put("out_order_code", orderInfo.get("out_order_code")!=null?orderInfo.get("out_order_code"):"");
			DbUp.upTable("oc_return_money").dataInsert(map);
			
			// 创建流水日志
			MDataMap logMap = new MDataMap();
			logMap.put("return_money_no", money_no);
			logMap.put("info", "跨境通订单失败，直接生成退款单");
			logMap.put("create_time", DateUtil.getSysDateTimeString());
			String create_user="";
			try {
				create_user=UserFactory.INSTANCE.create().getLoginName();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logMap.put("create_user", create_user);
			logMap.put("status", map.get("status"));
			DbUp.upTable("lc_return_money_status").dataInsert(logMap);
		}
		
		//自动退还微公社余额
		if(expected_return_group_money.compareTo(BigDecimal.ZERO)>0){
			
			//退返微公社部分
			GroupRefundInput groupRefundInput = new GroupRefundInput();
//			groupRefundInput.setTradeCode(money_no);
			groupRefundInput.setTradeCode(DbUp.upTable("oc_order_pay").one("order_code",order_code,"pay_type","449746280009").get("pay_sequenceid"));
			groupRefundInput.setMemberCode(orderInfo.get("buyer_code"));
			groupRefundInput.setRefundMoney(expected_return_group_money.toString());
			groupRefundInput.setOrderCode(order_code);
			groupRefundInput.setRefundTime(DateUtil.getSysDateTimeString());
			groupRefundInput.setRemark("跨境通自动退还微公社余额");
			groupRefundInput.setBusinessTradeCode(money_no);//一个流水值退一次
//			new GroupPayService().groupRefundSome(groupRefundInput, orderInfo.get("seller_code"));
			
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
		
	}
}
