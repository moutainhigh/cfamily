package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.model.ChangeGoods;
import com.cmall.familyhas.model.ChangeGoods.ChangeGoodsDetail;
import com.cmall.familyhas.model.ChangeGoodsNew;
import com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport;
import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ChangeGoodsService {

	public MWebResult addExchangegoods(ChangeGoods changeGoods){
		
		MWebResult result = new MWebResult();
		
		String order_code=changeGoods.getOrder_code();
		List<ChangeGoodsDetail> detailList=changeGoods.getGoodsDetailList();
		
		//验证订单
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		if(orderInfo==null||orderInfo.size()<1){
			result.inErrorMessage(916411237, order_code);
			return result;
		}
		
		String order_status=orderInfo.get("order_status");
		if(!"4497153900010005".equals(order_status)&&!"4497153900010003".equals(order_status)&&!"4497153900010004".equals(order_status)){
//		if(!"4497153900010005".equals(order_status)){
			result.inErrorMessage(916421252, order_code);
			return result;
		}
		
//		MDataMap egm=DbUp.upTable("oc_exchange_goods").one("order_code",order_code);
//		if(egm!=null){
//			String status=egm.get("status");
//			if(!"4497153900020004".equals(status)&&!"4497153900020003".equals(status)){
//				result.inErrorMessage(916401237, order_code);
//				return result;
//			}
//		}
		
		MDataMap return_goods = DbUp.upTable("oc_return_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900050001','4497153900050003') ", "order_code",changeGoods.getOrder_code());
		if (return_goods != null) {
			result.inErrorMessage(916421253, order_code);
			return result;
		}
		
//		MDataMap egm=DbUp.upTable("oc_exchange_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900020001','4497153900020002') ", "order_code",changeGoods.getOrder_code());
//		if(egm!=null){
//			result.inErrorMessage(916421250, order_code);
//			return result;
//		}
		
		//生成换后编号
		String exchangeNo = WebHelper.upCode("CGS");
		
		//验证订单详情
		List<MDataMap> list = DbUp.upTable("oc_orderdetail").queryAll("", "", "", new MDataMap("order_code",order_code));
		Map<String, MDataMap> orderDetailMap=new HashMap<String, MDataMap>(list.size());
		for (MDataMap mDataMap2 : list) {
			orderDetailMap.put(mDataMap2.get("sku_code"), mDataMap2);
		}
		
		MDataMap addChangeGoodsDetailMap=new MDataMap();
		List<MDataMap> detailMapList=new ArrayList<MDataMap>();
		for (ChangeGoodsDetail detail : detailList) {
			
			MDataMap orderDetail=orderDetailMap.get(detail.getSku_code());
			if(orderDetail==null){
				result.inErrorMessage(916411240, order_code);
				break;
			}
			
			if(Integer.valueOf(orderDetail.get("sku_num"))<detail.getCount()||detail.getCount()<1){
				result.inErrorMessage(916411240, order_code);
				break;
			}
			
			addChangeGoodsDetailMap.put("exchange_no", exchangeNo);
			addChangeGoodsDetailMap.put("sku_code", detail.getSku_code());
			addChangeGoodsDetailMap.put("sku_name", orderDetail.get("sku_name"));
			addChangeGoodsDetailMap.put("count", String.valueOf(detail.getCount()));
			addChangeGoodsDetailMap.put("current_price", orderDetail.get("sku_price"));
			detailMapList.add(addChangeGoodsDetailMap);
		}
		
		if(!result.upFlagTrue()){
			return result;
		}
		
		//换货数据的插入处理
		MDataMap addChangeGoodsMap=new MDataMap();
		
		//换货主信息设置
		addChangeGoodsMap.put("exchange_no", exchangeNo);
		addChangeGoodsMap.put("order_code", order_code);
		addChangeGoodsMap.put("buyer_code", orderInfo.get("buyer_code"));
		addChangeGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addChangeGoodsMap.put("exchange_reason", changeGoods.getExchange_reason());
		addChangeGoodsMap.put("status", "4497153900020002");//默认换货流转状态初始值
		addChangeGoodsMap.put("transport_money", changeGoods.getTransport_money());
		addChangeGoodsMap.put("contacts", changeGoods.getContacts());
		addChangeGoodsMap.put("mobile", changeGoods.getMobile());
		addChangeGoodsMap.put("address", changeGoods.getAddress());
		addChangeGoodsMap.put("pic_url", "");
		addChangeGoodsMap.put("description", changeGoods.getDescription());
		addChangeGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addChangeGoodsMap.put("transport_people", changeGoods.getTransport_people());
		//设置日期格式
		addChangeGoodsMap.put("create_time", DateUtil.getSysDateTimeString());
		//换货主信息增加
		DbUp.upTable("oc_exchange_goods").dataInsert(addChangeGoodsMap);
		
		//换货明细表信息设置
		for (MDataMap mDataMap : detailMapList) {
			DbUp.upTable("oc_exchange_goods_detail").dataInsert(mDataMap);
		}
		
		
		MDataMap addLogMap=new MDataMap();
		//换货日志信息设定
		addLogMap.put("exchange_no", exchangeNo);
		addLogMap.put("info", "");
		addLogMap.put("create_time", DateUtil.getSysDateTimeString());
		addLogMap.put("create_user", UserFactory.INSTANCE.create().getUserCode());
		addLogMap.put("old_status", "");
		addLogMap.put("now_status", "4497153900020002");
		
		//插入新的换货状态日志信息
		DbUp.upTable("lc_exchangegoods").dataInsert(addLogMap);
		
		return result;
	}
	
	
	public MWebResult addExchangegoodsPart(ChangeGoodsNew changeGoods){
		
		MWebResult result = new MWebResult();
		
		String order_code=changeGoods.getOrder_code();
		
		//验证订单
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		
		//验证订单状态
		if (!StringUtils.startsWithAny(orderInfo.get("order_status"),"4497153900010005","4497153900010004","4497153900010003")) {
			result.inErrorMessage(916422113);
			return result;
		}
		
		//验证订单类型
//		if (!StringUtils.startsWithAny(orderInfo.get("small_seller_code"),"SF03")) {
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		if (!StringUtils.isNotBlank(WebHelper.getSellerType(orderInfo.get("small_seller_code")))) {
			result.inErrorMessage(916422157);
			return result;
		}
		
		if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030001","flow_end","0")>0){
			result.inErrorMessage(916421253,order_code);
			return result;
		}
		
		
		boolean num_b=false;
		
		MObjMap<String, Integer> redetails = changeGoods.getDetailMap();
		
		for (MObjMap.Entry<String, Integer> map : redetails.entrySet()) {
			String sku_code=map.getKey();
			int num=map.getValue();
			if(num>new com.cmall.ordercenter.service.goods.ReturnGoodsService().getAchangeNumC(order_code, sku_code)){
				num_b=true;
				break;
			}
		}
		
		if(num_b){
			result.inErrorMessage(916422158);
			return result;
		}
		
		MDataMap return_reason=DbUp.upTable("oc_return_goods_reason").one("return_reson_code",changeGoods.getAsale_reason());
		if(return_reason==null||return_reason.isEmpty()){
			result.inErrorMessage(916422117);
			return result;
		}
		
		
		//生成换后编号
		String exchangeNo = WebHelper.upCode("CGS");
		String now=DateUtil.getSysDateTimeString();
		
		// 换货明细表信息设置
		for (MObjMap.Entry<String, Integer> map : redetails.entrySet()) {
			String sku_code=map.getKey();
			int num=map.getValue();
			
			MDataMap mDataMap = DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
			
			MDataMap addChangeGoodsDetailMap = new MDataMap();
			addChangeGoodsDetailMap.put("exchange_no", exchangeNo);
			addChangeGoodsDetailMap.put("sku_code", sku_code);
			addChangeGoodsDetailMap.put("sku_name", mDataMap.get("sku_name"));
			addChangeGoodsDetailMap.put("count", String.valueOf(num));
			addChangeGoodsDetailMap.put("current_price", mDataMap.get("sku_price"));
			addChangeGoodsDetailMap.put("product_picurl", mDataMap.get("product_picurl"));
			DbUp.upTable("oc_exchange_goods_detail").dataInsert(addChangeGoodsDetailMap);
			
			//****************************过渡逻辑**************
			MDataMap achangeInfo=new MDataMap();
			achangeInfo.put("order_code", order_code);
			achangeInfo.put("sku_code", sku_code);
			achangeInfo.put("oac_num", addChangeGoodsDetailMap.get("count"));
			achangeInfo.put("oac_type", "4497477800030003");//退货
			achangeInfo.put("oac_status", "4497477800040001");
			achangeInfo.put("create_time", now);
			achangeInfo.put("update_time", now);
			achangeInfo.put("available", "0");
			achangeInfo.put("asale_source", "4497477800060002");
			achangeInfo.put("asale_code", exchangeNo);
			DbUp.upTable("oc_order_achange").dataInsert(achangeInfo);
			
			MDataMap oasdMap=new MDataMap();
			oasdMap.put("asale_code", exchangeNo);
			oasdMap.put("sku_code", sku_code);
			oasdMap.put("sku_num", addChangeGoodsDetailMap.get("count"));
			oasdMap.put("sku_name", mDataMap.get("sku_name"));
			oasdMap.put("sku_price", mDataMap.get("sku_price"));
			oasdMap.put("product_code", mDataMap.get("product_code"));
			oasdMap.put("product_picurl", mDataMap.get("product_picurl"));
			DbUp.upTable("oc_order_after_sale_dtail").dataInsert(oasdMap);
			
			
			
			
			
			int count=0;
			Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available", new MDataMap("order_code",order_code,"sku_code",sku_code,"available","0"));
			if(map1!=null&&!map1.isEmpty()){
				Object obj=map1.get("count");
				if(obj!=null){
					count=((BigDecimal) obj).intValue();
				}
			}
			
			int all_count=Integer.valueOf(mDataMap.get("sku_num"));
			if(all_count<=count){
				DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","1","sku_code",sku_code), "flag_asale", "order_code,sku_code");
			}
			
//			int count=DbUp.upTable("oc_order_achange").count("order_code",order_code,"sku_code",sku_code,"available","0","oac_type","4497477800030001");
//			if(all_count>count){//还有可以售后的商品
//				if(DbUp.upTable("oc_order_achange").count("order_code",order_code,"sku_code",sku_code,"available","0","asale_source","4497477800060001")<1){
//					DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","0","sku_code",sku_code), "flag_asale", "order_code,sku_code");
//				}
//			}
		}
		
		
		MDataMap orderadress=DbUp.upTable("oc_orderadress").one("order_code",order_code);
		
		//换货数据的插入处理
		MDataMap addChangeGoodsMap=new MDataMap();
		
		// 默认客服确认
		String exchangeGoodsStatus = "4497153900020005";
		// 默认换货中
		String afterSaleStatus = "4497477800050013";
		
		// 需要寄回状态变更为待完善物流
		if("4497477800090001".equals(changeGoods.getFlag_return_goods())) {
			afterSaleStatus = "4497477800050010";
		}
		
		if(Constants.SMALL_SELLER_CODE_JD.equals(orderInfo.get("small_seller_code"))) {
			MDataMap jdOrderInfo = DbUp.upTable("oc_order_jd").one("order_code", order_code);
			MWebResult typeResult = new JdAfterSaleSupport().getAfsPickwareType(jdOrderInfo.get("jd_order_id"), jdOrderInfo.get("sku_id"));
			// 如果支持上门取件则没有待完善物流这一步
			if(typeResult.upFlagTrue() && typeResult.getResultList().contains("4")) {
				afterSaleStatus = "4497477800050013";
			}
		}
		
		//换货主信息设置
		addChangeGoodsMap.put("exchange_no", exchangeNo);
		addChangeGoodsMap.put("order_code", order_code);
		addChangeGoodsMap.put("buyer_code", orderInfo.get("buyer_code"));
		addChangeGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addChangeGoodsMap.put("exchange_reason", return_reason.get("return_reson"));
		addChangeGoodsMap.put("status", exchangeGoodsStatus);//默认换货流转状态初始值
		addChangeGoodsMap.put("transport_money",  StringUtils.equals(changeGoods.getFreight(), "4497476900040001")?orderInfo.get("transport_money"):"0");
		addChangeGoodsMap.put("contacts", orderadress.get("receive_person"));
		addChangeGoodsMap.put("mobile", orderadress.get("mobilephone"));
		addChangeGoodsMap.put("buyer_mobile", (String)DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",orderInfo.get("buyer_code"))));
		addChangeGoodsMap.put("address", orderadress.get("address"));
		addChangeGoodsMap.put("pic_url", "");
		addChangeGoodsMap.put("description", changeGoods.getDescription());
		addChangeGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addChangeGoodsMap.put("transport_people", changeGoods.getTransport_people());
		addChangeGoodsMap.put("create_time", DateUtil.getSysDateTimeString());
		addChangeGoodsMap.put("after_sale_person", changeGoods.getAfter_sale_person());
		addChangeGoodsMap.put("after_sale_mobile", changeGoods.getAfter_sale_mobile());
		addChangeGoodsMap.put("after_sale_address", changeGoods.getAfter_sale_address());
		addChangeGoodsMap.put("after_sale_postcode", changeGoods.getAfter_sale_postcode());
		addChangeGoodsMap.put("goods_receipt", changeGoods.getGoods_receipt());
		addChangeGoodsMap.put("freight", changeGoods.getFreight());
		addChangeGoodsMap.put("flag_return_goods", changeGoods.getFlag_return_goods());
		
		//换货主信息增加
		DbUp.upTable("oc_exchange_goods").dataInsert(addChangeGoodsMap);
		
		MDataMap addLogMap=new MDataMap();
		addLogMap.put("exchange_no", exchangeNo);
		addLogMap.put("info", changeGoods.getDescription());
		addLogMap.put("create_time", now);
		addLogMap.put("create_user", UserFactory.INSTANCE.create().getUserCode());
		addLogMap.put("old_status", "");
		addLogMap.put("now_status", exchangeGoodsStatus);
		DbUp.upTable("lc_exchangegoods").dataInsert(addLogMap);
		
		
		
		//****************************过渡逻辑********************************
		MDataMap oasMap=new MDataMap();
		oasMap.put("asale_code", exchangeNo);
		oasMap.put("order_code", order_code);
		oasMap.put("buyer_code", orderInfo.get("buyer_code"));
		oasMap.put("buyer_mobile", addChangeGoodsMap.get("mobile"));
		oasMap.put("seller_code", addChangeGoodsMap.get("seller_code"));
		oasMap.put("small_seller_code", addChangeGoodsMap.get("small_seller_code"));
		oasMap.put("asale_reason", changeGoods.getAsale_reason());
		oasMap.put("asale_status",afterSaleStatus);
		oasMap.put("asale_remark", changeGoods.getDescription());
		oasMap.put("asale_type", "4497477800030003");//退货
		oasMap.put("asale_source", "4497477800060002");
		oasMap.put("create_time", now);
		oasMap.put("update_time", now);
		oasMap.put("flow_end", "0");
		DbUp.upTable("oc_order_after_sale").dataInsert(oasMap);
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", exchangeNo);
		loasMap.put("create_user", addLogMap.get("create_user"));
		loasMap.put("create_time", now);
		loasMap.put("asale_status", oasMap.get("asale_status"));
		loasMap.put("remark", addLogMap.get("info"));
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", exchangeNo);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070001");
		lsasMap.put("create_time", now);
		
		String order_status=(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",orderInfo.get("order_status")));
		MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100013");
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), order_status,return_reason.get("return_reson"),"4497476900040001".equals(changeGoods.getGoods_receipt())?"是":"否",changeGoods.getDescription()));
		lsasMap.put("serial_title", "客服发起了申请");
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100014");
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), addChangeGoodsMap.get("after_sale_address"),addChangeGoodsMap.get("after_sale_person"),addChangeGoodsMap.get("after_sale_mobile"),addChangeGoodsMap.get("after_sale_postcode")));
		lsasMap.put("serial_title", templateMap.get("template_title"));
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		// 待完善物流
		if("4497477800050010".equals(afterSaleStatus)) {
			templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100018");
			lsasMap.put("serial_msg", templateMap.get("template_context"));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("template_code", templateMap.get("template_code"));
			DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		}
		
		if("4497477800090001".equals(changeGoods.getFlag_return_goods())){
			MDataMap shipmentsMap = new MDataMap();
			shipmentsMap.put("order_code", exchangeNo);
			shipmentsMap.put("creator", "system");
			shipmentsMap.put("create_time", now);
			shipmentsMap.put("is_send100_flag", "0");
			shipmentsMap.put("send_count", "0");
			shipmentsMap.put("update_time", now);
			shipmentsMap.put("update_user", "system");
			shipmentsMap.put("shipments_code", "");
			shipmentsMap.put("is_send100_flag", "1");
			DbUp.upTable("oc_order_shipments").dataInsert(shipmentsMap);
		}
		
		if (Constants.SMALL_SELLER_CODE_JD.equals(orderInfo.get("small_seller_code"))) {
			if(new JdAfterSaleSupport().initJdAfterSale(exchangeNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new JdAfterSaleSupport().createAfterSaleServiceTask(exchangeNo);
			}
		}
		
		if("4497471600430002".equals(DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code)))) {
			DuohzAfterSaleSupport duohzAfterSaleSupport = new DuohzAfterSaleSupport();
			if(duohzAfterSaleSupport.initDuohzAfterSale(exchangeNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				duohzAfterSaleSupport.createAfterSaleServiceTask(exchangeNo);
			}
			
			duohzAfterSaleSupport.createApplyAfterSaleTask(exchangeNo);
		}
		
		return result;
	}
	
}
