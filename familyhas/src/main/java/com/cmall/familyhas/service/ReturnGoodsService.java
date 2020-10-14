package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.model.ReturnGoods;
import com.cmall.familyhas.model.ReturnGoodsNew;
import com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport;
import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ReturnGoodsService {

	public MWebResult addReturngoods(ReturnGoods returnGoods) {
		MWebResult result = new MWebResult();
		String order_code = returnGoods.getOrder_code();
		//验证订单是否已存在
//		1、同一个订单号已经退货完成，不可以再次发起申请
//		2、和同一个订单号，如果正在退货进行中，不可以发起申请 
		MDataMap return_goods = DbUp.upTable("oc_return_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900050001','4497153900050003') ", "order_code",returnGoods.getOrder_code());
		if (return_goods != null) {
			result.inErrorMessage(916421249, order_code);
			return result;
		}
		
//		订单号正在走换货的流程，如果发起退货申请，就不让提交
		MDataMap egm=DbUp.upTable("oc_exchange_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900020001','4497153900020002') ", "order_code",returnGoods.getOrder_code());
		if(egm!=null){
			result.inErrorMessage(916421250, order_code);
			return result;
		}
		
		// 验证订单
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",	order_code, "delete_flag", "0");
		if (orderInfo == null || orderInfo.size() < 1) {
			result.inErrorMessage(916421241, order_code);
			return result;
		}
		
		//验证订单状态
		String order_status = orderInfo.get("order_status");
		if (!"4497153900010005".equals(order_status)&& !"4497153900010003".equals(order_status)	&& !"4497153900010004".equals(order_status)) {
//		if (!"4497153900010005".equals(order_status)) {
			result.inErrorMessage(916421251, order_code);
			return result;
		}
		
		
		// 生成退货编号
		String returnNo = WebHelper.upCode("RGS");
		// 验证订单详情
		List<MDataMap> list = DbUp.upTable("oc_orderdetail").queryAll("", "","", new MDataMap("order_code", order_code));
		
		// 退货数据入库处理
		MDataMap addReturnGoodsMap = new MDataMap();
		// 退货主信息设置
		addReturnGoodsMap.put("return_code", returnNo);
		addReturnGoodsMap.put("order_code", order_code);
		addReturnGoodsMap.put("buyer_code", orderInfo.get("buyer_code"));
		addReturnGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addReturnGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addReturnGoodsMap.put("return_reason", returnGoods.getReturn_reason());
		addReturnGoodsMap.put("status", "4497153900050003");// 默认换货流转状态初始值
		addReturnGoodsMap.put("transport_money", (returnGoods.getTransport_money() == null ? "0" :returnGoods.getTransport_money().toString()));
		addReturnGoodsMap.put("contacts", returnGoods.getContacts());
		addReturnGoodsMap.put("mobile", returnGoods.getMobile());
		addReturnGoodsMap.put("address", returnGoods.getAddress());
		addReturnGoodsMap.put("pic_url", "");
		addReturnGoodsMap.put("description", returnGoods.getDescription());
		addReturnGoodsMap.put("transport_people", returnGoods.getTransport_people());
		// 设置日期格式
		addReturnGoodsMap.put("create_time", DateUtil.getSysDateTimeString());
		// 退货主信息入库
		DbUp.upTable("oc_return_goods").dataInsert(addReturnGoodsMap);
		
		
		// 换货明细表信息设置
		for (MDataMap mDataMap : list) {
			
			MDataMap addReturnGoodsDetailMap = new MDataMap();
			addReturnGoodsDetailMap.put("return_code", returnNo);
			addReturnGoodsDetailMap.put("sku_code", mDataMap.get("sku_code"));
			addReturnGoodsDetailMap.put("count", mDataMap.get("sku_num"));
			addReturnGoodsDetailMap.put("sku_name",mDataMap.get("sku_name"));
			addReturnGoodsDetailMap.put("current_price",mDataMap.get("sku_price"));
			addReturnGoodsDetailMap.put("url",mDataMap.get("product_picurl"));
			addReturnGoodsDetailMap.put("return_price",mDataMap.get("sku_price"));
			
			DbUp.upTable("oc_return_goods_detail").dataInsert(addReturnGoodsDetailMap);
		}
		
		MDataMap addLogMap = new MDataMap();
		// 退货日志信息设置
		addLogMap.put("return_no", returnNo);
		addLogMap.put("info", "");
		addLogMap.put("create_time", DateUtil.getSysDateTimeString());
		addLogMap.put("create_user", UserFactory.INSTANCE.create().getUserCode());
		addLogMap.put("status", "4497153900050003");
		// 插入新的换货状态日志信息
		DbUp.upTable("lc_return_goods_status").dataInsert(addLogMap);
		if(result.getResultCode() == 1 && DbUp.upTable("fh_agent_order_detail").count("order_code",order_code)>0 && DbUp.upTable("za_exectimer").count("exec_info",returnNo,"exec_type","449746990025")<=0) {//退货申请成功并且是分销订单，需要写入定时
			JobExecHelper.createExecInfo("449746990025",returnNo, DateUtil.addMinute(5));
		}
		return result;
	}
	
	/**
	 * 添加部分退货单
	 * @param returnGoods
	 * @return
	 */
	public MWebResult retGoodsForPart(ReturnGoodsNew returnGoods){
		
		MWebResult result = new MWebResult();
		
		String order_code = returnGoods.getOrder_code();
		
		String lock_code=WebHelper.addLock("order_change_"+order_code, 180);
		if(StringUtils.isBlank(lock_code)){
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422118);
			return result;
		}
		
		// 验证订单
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",	order_code);
		if (orderInfo == null || orderInfo.isEmpty()) {
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916421241, order_code);
			return result;
		}
		
		//验证订单状态
		if (!StringUtils.startsWithAny(orderInfo.get("order_status"),"4497153900010005","4497153900010004","4497153900010003")) {
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422113);
			return result;
		}
		
		//验证订单类型
//		if (!StringUtils.startsWithAny(orderInfo.get("small_seller_code"),"SF03")) {
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		if (!StringUtils.isNotBlank(WebHelper.getSellerType(orderInfo.get("small_seller_code")))) {
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422119);
			return result;
		}
		
		//正在进行的换货操作，不允许退货
//		MDataMap egm=DbUp.upTable("oc_exchange_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900020001','4497153900020002') ", "order_code",order_code);
//		if(egm!=null){
//			WebHelper.unLock(lock_code);
//			result.inErrorMessage(916422114);
//			return result;
//		}
		if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030003","flow_end","0")>0){
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422114);
			return result;
		}
		
		
//		List<String> sku_codes=new ArrayList<String>(retDetailMap.size());
//		for (String str : retDetailMap.keySet()) {
//			sku_codes.add("'"+str+"'");
//		}
//		String sku_codes_str=StringUtils.join(sku_codes,",");
//		
//		Map<String, MDataMap> orderDetailMap=new HashMap<String, MDataMap>();
//		List<MDataMap> details=DbUp.upTable("oc_orderdetail").queryAll("", "", "order_code=:order_code and sku_code in ("+sku_codes_str+") ", new MDataMap("order_code",order_code));
//		for (MDataMap data : details) {
//			orderDetailMap.put(data.get("sku_code"), data);
//		}
//		
//		MObjMap<String, Integer> retNum=new MObjMap<String, Integer>();
//		List<Map<String, Object>> rgs=DbUp.upTable("oc_return_goods").dataSqlList("SELECT d.sku_code,d.count from oc_return_goods g RIGHT JOIN  oc_return_goods_detail d on g.return_code=d.return_code where g.`status` in ('4497153900050001','4497153900050003') and d.sku_code in ("+sku_codes_str+") and g.order_code=:order_code", new MDataMap("order_code",order_code));
//		for (Map<String, Object> map : rgs) {
//			String sku_code=(String)map.get("sku_code");
//			int oac_num=(Integer)map.get("count");
//			int num=retNum.get(sku_code)==null?0:retNum.get(sku_code);
//			retNum.put(sku_code, num+oac_num);
//		}
		
		////退货超出下单量，不允许退货
		boolean num_b=false;
//		for (MObjMap.Entry<String, Integer> mm : retDetailMap.entrySet()) {
//			String sku_code=mm.getKey();
//			int num=mm.getValue();//本次退货数
//			int numr=retNum.get(sku_code)==null?0:retNum.get(sku_code);//已经退货数
//			int sku_num=Integer.valueOf(orderDetailMap.get(sku_code).get("sku_num"));//买的件数
//			if(sku_num<(num+numr)){
//				break;
//			}
//		}
		
		MObjMap<String, Integer> redetails = returnGoods.getDetailMap();
		
		for (MObjMap.Entry<String, Integer> map : redetails.entrySet()) {
			String sku_code=map.getKey();
			int num=map.getValue();
			if(num>new com.cmall.ordercenter.service.goods.ReturnGoodsService().getAchangeNum(order_code, sku_code)){
				num_b=true;
				break;
			}
		}
		
		if(num_b){
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422115);
			return result;
		}
		
		
//		MDataMap addr=DbUp.upTable("oc_address_info").one("small_seller_code",orderInfo.get("small_seller_code"));
//		if(addr==null||addr.isEmpty()){
//			WebHelper.unLock(lock_code);
//			result.inErrorMessage(916422116);
//			return result;
//		}
		
		MDataMap return_reason=DbUp.upTable("oc_return_goods_reason").one("return_reson_code",returnGoods.getReturn_reason_code());
		if(return_reason==null||return_reason.isEmpty()){
			WebHelper.unLock(lock_code);
			result.inErrorMessage(916422117);
			return result;
		}
		
		
		// 生成退货编号
		String returnNo = WebHelper.upCode("RGS");
		String now=DateUtil.getSysDateTimeString();
		
		BigDecimal expected_return_money=BigDecimal.ZERO;
		BigDecimal expected_return_group_money=BigDecimal.ZERO;
		BigDecimal expected_return_accm_money = BigDecimal.ZERO; //预期退还积分金额
		BigDecimal expected_return_give_accm_money = BigDecimal.ZERO; //回收赋予的积分总金额
		BigDecimal expected_return_ppc_money = BigDecimal.ZERO; //预期退还储值金金额
		BigDecimal expected_return_crdt_money = BigDecimal.ZERO; //预期退还暂存款金额
		BigDecimal expected_return_hjycoin = BigDecimal.ZERO; //预期退还惠币金额
		BigDecimal expected_return_give_hjycoin = BigDecimal.ZERO; //回收赋予的惠币积分总金额
		
		// 总退货数量
		int produceNum = 0;
		// 换货明细表信息设置
		for (MObjMap.Entry<String, Integer> map : redetails.entrySet()) {
			String sku_code=map.getKey();
			int num=map.getValue();
			
			produceNum += num;
			
			MDataMap mDataMap = DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
			
			// 此SKU使用的积分金额
			BigDecimal integralMoney = new BigDecimal(mDataMap.get("integral_money"));
			// 此SKU使用的惠币金额
			BigDecimal hjycoin = new BigDecimal(mDataMap.get("hjycoin"));
			// 此SKU已经退还的积分金额
			BigDecimal alreadyReturnIntegralMoneyForSku = getAlreadyReturnIntegralMoney(order_code,mDataMap.get("sku_code"));
			// 此SKU已经退还的惠币
			BigDecimal alreadyReturnHjycoinForSku = getAlreadyReturnHjyCoin(order_code,mDataMap.get("sku_code"));
			// 此SKU已经退货的商品数量
			int alreadyReturnSkuNumForSku = getAlreadyReturnSkuNum(order_code,mDataMap.get("sku_code"));
			// SKU总金额
			BigDecimal skuMoney = new BigDecimal(mDataMap.get("sku_num")).multiply(new BigDecimal(mDataMap.get("sku_price")));
			// 退货SKU总金额
			BigDecimal returnSkuMoney = new BigDecimal(num).multiply(new BigDecimal(mDataMap.get("sku_price")));
			// 预期退还的积分金额
			BigDecimal returnIntegralMoney = BigDecimal.ZERO;
			
			// 预期退还的惠币金额
			BigDecimal returnHjycoin = BigDecimal.ZERO;
			
			// 有使用积分且仍有积分未退还
			if(integralMoney.compareTo(BigDecimal.ZERO) > 0 && integralMoney.compareTo(alreadyReturnIntegralMoneyForSku) > 0){
				// 如果已经全部退还则把剩余积分都退还
				if((alreadyReturnSkuNumForSku + num) >= getOrderSkuNum(order_code, mDataMap.get("sku_code"))){
					returnIntegralMoney = integralMoney.subtract(alreadyReturnIntegralMoneyForSku);
				}else{
					// 按金额占比计算退还的积分金额
					returnIntegralMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(integralMoney).setScale(0, BigDecimal.ROUND_UP);
					// 不能超过总使用积分
					if(alreadyReturnIntegralMoneyForSku.add(returnIntegralMoney).compareTo(integralMoney) > 0){
						returnIntegralMoney = integralMoney.subtract(alreadyReturnIntegralMoneyForSku);
					}
					// 退还积分金额不能大于退货商品金额
					if(returnIntegralMoney.compareTo(returnSkuMoney) > 0){
						returnIntegralMoney = returnSkuMoney;
					}
				}
				
				if(returnIntegralMoney.compareTo(BigDecimal.ZERO) < 0){
					returnIntegralMoney = BigDecimal.ZERO;
				}
			}
			
			// 有使用惠币且仍有惠币积分未退还
			if(hjycoin.compareTo(BigDecimal.ZERO) > 0 && hjycoin.compareTo(alreadyReturnHjycoinForSku) > 0){
				// 如果已经全部退还则把剩余积分都退还
				if((alreadyReturnSkuNumForSku + num) >= getOrderSkuNum(order_code, mDataMap.get("sku_code"))){
					returnHjycoin = hjycoin.subtract(alreadyReturnHjycoinForSku);
				}else{
					// 按金额占比计算退还的积分金额
					returnHjycoin = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(hjycoin).setScale(0, BigDecimal.ROUND_UP);
					// 不能超过总使用积分
					if(alreadyReturnHjycoinForSku.add(returnHjycoin).compareTo(hjycoin) > 0){
						returnHjycoin = hjycoin.subtract(alreadyReturnHjycoinForSku);
					}
					// 退还积分金额不能大于退货商品金额
					if(returnHjycoin.compareTo(returnSkuMoney) > 0){
						returnHjycoin = returnSkuMoney;
					}
				}
				
				if(returnHjycoin.compareTo(BigDecimal.ZERO) < 0){
					returnHjycoin = BigDecimal.ZERO;
				}
			}
			
			// 此SKU使用的储值金金额
			BigDecimal czjMoney = new BigDecimal(mDataMap.get("czj_money"));
			// 此SKU已经退还的储值金金额
			BigDecimal alreadyReturnCzjMoneyForSku = getAlreadyReturnCzjMoney(order_code,mDataMap.get("sku_code"));
			// 预期退还的储值金金额
			BigDecimal returnCzjMoney = BigDecimal.ZERO;
			
			// 有使用储值金且仍有储值金未退还
			if(czjMoney.compareTo(BigDecimal.ZERO) > 0 && czjMoney.compareTo(alreadyReturnCzjMoneyForSku) > 0){
				// 如果已经全部退还则把剩余储值金都退还
				if((alreadyReturnSkuNumForSku + num) >= getOrderSkuNum(order_code, mDataMap.get("sku_code"))){
					returnCzjMoney = czjMoney.subtract(alreadyReturnCzjMoneyForSku);
				}else{
					if(orderInfo.get("small_seller_code").equals("SI2003") || orderInfo.get("small_seller_code").equals("SI2009")) {
						//tv品向上取整
						// 按金额占比计算退还的储值金金额
						returnCzjMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(czjMoney).setScale(0, BigDecimal.ROUND_UP);
					}else {
						//自营品精确到分
						// 按金额占比计算退还的储值金金额
						// 必须向上取整，不然就会出现  102/306*306 结果不等于102的情况
						returnCzjMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(czjMoney).setScale(0, BigDecimal.ROUND_UP);
					}
					
					// 不能超过总使用储值金
					if(alreadyReturnCzjMoneyForSku.add(returnCzjMoney).compareTo(czjMoney) > 0){
						returnCzjMoney = czjMoney.subtract(alreadyReturnCzjMoneyForSku);
					}
					// 退还储值金金额不能大于退货商品金额
					if(returnCzjMoney.compareTo(returnSkuMoney) > 0){
						returnCzjMoney = returnSkuMoney;
					}
				}
				
				if(returnCzjMoney.compareTo(BigDecimal.ZERO) < 0){
					returnCzjMoney = BigDecimal.ZERO;
				}
			}
			
			// 已经退还的积分加上储值金超过商品金额的情况下，以商品的金额为准
			if(returnCzjMoney.add(returnIntegralMoney).add(returnHjycoin).compareTo(returnSkuMoney) > 0) {
				returnCzjMoney = returnSkuMoney.subtract(returnIntegralMoney).subtract(returnHjycoin);
				if(returnCzjMoney.compareTo(BigDecimal.ZERO) < 0) {
					returnCzjMoney = BigDecimal.ZERO;
				}
			}
			
			// 此SKU使用的暂存款金额
			BigDecimal zckMoney = new BigDecimal(mDataMap.get("zck_money"));
			// 此SKU已经退还的暂存款金额
			BigDecimal alreadyReturnZckMoneyForSku = getAlreadyReturnZckMoney(order_code,mDataMap.get("sku_code"));
			// 预期退还的暂存款金额
			BigDecimal retrunZckMoney = BigDecimal.ZERO;
			
			// 有使用暂存款且仍有暂存款未退还
			if(zckMoney.compareTo(BigDecimal.ZERO) > 0 && zckMoney.compareTo(alreadyReturnZckMoneyForSku) > 0){
				// 如果已经全部退还则把剩余暂存款都退还
				if((alreadyReturnSkuNumForSku + num) >= getOrderSkuNum(order_code, mDataMap.get("sku_code"))){
					retrunZckMoney = zckMoney.subtract(alreadyReturnZckMoneyForSku);
				}else{
					if(orderInfo.get("small_seller_code").equals("SI2003") || orderInfo.get("small_seller_code").equals("SI2009")) {
						//tv品向上取整
						// 按金额占比计算退还的暂存款金额
						retrunZckMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(zckMoney).setScale(0, BigDecimal.ROUND_UP);
					}else {
						//自营品精确到分
						// 按金额占比计算退还的暂存款金额
						// 必须向上取整，不然就会出现  102/306*306 结果不等于102的情况
						retrunZckMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(zckMoney).setScale(0, BigDecimal.ROUND_UP);
					}
					// 不能超过总使用暂存款
					if(alreadyReturnZckMoneyForSku.add(retrunZckMoney).compareTo(zckMoney) > 0){
						retrunZckMoney = zckMoney.subtract(alreadyReturnZckMoneyForSku);
					}
					// 退还暂存款金额不能大于退货商品金额
					if(retrunZckMoney.compareTo(returnSkuMoney) > 0){
						retrunZckMoney = returnSkuMoney;
					}
				}
				
				if(retrunZckMoney.compareTo(BigDecimal.ZERO) < 0){
					retrunZckMoney = BigDecimal.ZERO;
				}
			}
			
			// 已经退还的积分和储值金加上暂存款超过商品金额的情况下，以商品的金额为准
			if(retrunZckMoney.add(returnIntegralMoney).add(returnCzjMoney).add(returnHjycoin).compareTo(returnSkuMoney) > 0) {
				retrunZckMoney = returnSkuMoney.subtract(returnIntegralMoney).subtract(returnCzjMoney).subtract(returnHjycoin);
				if(retrunZckMoney.compareTo(BigDecimal.ZERO) < 0) {
					retrunZckMoney = BigDecimal.ZERO;
				}
			}
			
			// 累加此次退款的总积分金额、总储值金金额、总暂存款金额
			expected_return_accm_money = expected_return_accm_money.add(returnIntegralMoney);
			expected_return_ppc_money = expected_return_ppc_money.add(returnCzjMoney);
			expected_return_crdt_money = expected_return_crdt_money.add(retrunZckMoney);
			expected_return_hjycoin = expected_return_hjycoin.add(returnHjycoin);
			
			// 退货时应扣除的赋予积分数
			BigDecimal return_give_accm_money = new BigDecimal(mDataMap.get("give_integral_money"));
			return_give_accm_money = return_give_accm_money.divide(new BigDecimal(mDataMap.get("sku_num")), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(num));
			
			// 退货时应扣除的赋予惠币数
			BigDecimal return_give_hjycoin = new BigDecimal(mDataMap.get("give_hjycoin"));
			return_give_hjycoin = return_give_hjycoin.divide(new BigDecimal(mDataMap.get("sku_num")), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(num));
			
			MDataMap addReturnGoodsDetailMap = new MDataMap();
			addReturnGoodsDetailMap.put("return_code", returnNo);
			addReturnGoodsDetailMap.put("sku_code", mDataMap.get("sku_code"));
			addReturnGoodsDetailMap.put("count", String.valueOf(num));
			addReturnGoodsDetailMap.put("sku_name",mDataMap.get("sku_name"));
			addReturnGoodsDetailMap.put("current_price",mDataMap.get("sku_price"));
			addReturnGoodsDetailMap.put("url",mDataMap.get("product_picurl"));
			addReturnGoodsDetailMap.put("return_price",mDataMap.get("sku_price"));
			addReturnGoodsDetailMap.put("return_accm_money",returnIntegralMoney.toString());
			addReturnGoodsDetailMap.put("return_give_accm_money",return_give_accm_money.toString());
			addReturnGoodsDetailMap.put("return_hjycoin_money",returnHjycoin.toString());
			addReturnGoodsDetailMap.put("return_give_hjycoin_money",return_give_hjycoin.toString());
			addReturnGoodsDetailMap.put("return_ppc_money",returnCzjMoney.toString());
			addReturnGoodsDetailMap.put("return_crdt_money",retrunZckMoney.toString());
			
			DbUp.upTable("oc_return_goods_detail").dataInsert(addReturnGoodsDetailMap);
			expected_return_money=expected_return_money.add((new BigDecimal(num)).multiply(new BigDecimal(mDataMap.get("sku_price"))));
			expected_return_group_money=expected_return_group_money.add((new BigDecimal(num)).multiply(new BigDecimal(mDataMap.get("group_price"))));
			expected_return_give_accm_money = expected_return_give_accm_money.add(return_give_accm_money);
			expected_return_give_hjycoin = expected_return_give_hjycoin.add(return_give_hjycoin);
		
			
			//****************************过渡逻辑**************
			MDataMap achangeInfo=new MDataMap();
			achangeInfo.put("order_code", order_code);
			achangeInfo.put("sku_code", mDataMap.get("sku_code"));
			achangeInfo.put("oac_num", addReturnGoodsDetailMap.get("count"));
			achangeInfo.put("oac_type", "4497477800030001");//退货
			achangeInfo.put("oac_status", "4497477800040001");
			achangeInfo.put("create_time", now);
			achangeInfo.put("update_time", now);
			achangeInfo.put("available", "0");
			achangeInfo.put("asale_source", "4497477800060002");
			achangeInfo.put("asale_code", returnNo);
			DbUp.upTable("oc_order_achange").dataInsert(achangeInfo);
			
			MDataMap oasdMap=new MDataMap();
			oasdMap.put("asale_code", returnNo);
			oasdMap.put("sku_code", mDataMap.get("sku_code"));
			oasdMap.put("sku_num", addReturnGoodsDetailMap.get("count"));
			oasdMap.put("sku_name", mDataMap.get("sku_name"));
			oasdMap.put("sku_price", mDataMap.get("sku_price"));
			oasdMap.put("product_code", mDataMap.get("product_code"));
			oasdMap.put("product_picurl", mDataMap.get("product_picurl"));
			DbUp.upTable("oc_order_after_sale_dtail").dataInsert(oasdMap);
			
			
			
			
			
			int count=0;
			Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available and oac_type=:oac_type", new MDataMap("order_code",order_code,"sku_code",sku_code,"available","0","oac_type","4497477800030001"));
			if(map1!=null&&!map1.isEmpty()){
				Object obj=map1.get("count");
				if(obj!=null){
					count=((BigDecimal) obj).intValue();
				}
			}
//			int count=DbUp.upTable("oc_order_achange").count("order_code",order_code,"sku_code",sku_code,"available","0","oac_type","4497477800030001");
			int all_count=Integer.valueOf(mDataMap.get("sku_num"));
			if(all_count<=count){
				DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","1","sku_code",sku_code), "flag_asale", "order_code,sku_code");
			}
			
//			int count=DbUp.upTable("oc_order_achange").count("order_code",order_code,"sku_code",sku_code,"available","0","oac_type","4497477800030001");
//			if(all_count>count){//还有可以售后的商品
//				if(DbUp.upTable("oc_order_achange").count("order_code",order_code,"sku_code",sku_code,"available","0","asale_source","4497477800060001")<1){
//					DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","0","sku_code",sku_code), "flag_asale", "order_code,sku_code");
//				}
//			}else{
//				DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","1","sku_code",sku_code,"asale_code",returnNo), "flag_asale", "order_code,sku_code");
//			}
		}
		
		// 积分支付的钱
		BigDecimal usedMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280008"));
		
		// 商品明细中未记录积分的情况走原来的逻辑
		if(usedMoney != null && usedMoney.compareTo(BigDecimal.ZERO) > 0 && getOrderDetailIntegralMoney(order_code).compareTo(BigDecimal.ZERO) == 0){
			// 订单总商品金额
			BigDecimal productMoney = getOrderProductMoney(order_code);
			// 已经退还的积分金额
			BigDecimal alreadyReturnIntegralMoney = getAlreadyReturnIntegralMoney(order_code);
			// 已经退货的商品数量
			int alreadyReturnSkuNum = getAlreadyReturnSkuNum(order_code);
			// 订单总的商品数量
			int orderAllSkuNum = getOrderSkuNum(order_code);

			// 订单中的商品已经全部退货则退还剩余的全部积分
			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
				expected_return_accm_money = usedMoney.subtract(alreadyReturnIntegralMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			// 还有商品没有退货则根据比例拆分退货的积分
			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
				// 根据商品金额和总单商品金额的占比计算退还积分数量
				expected_return_accm_money = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(usedMoney).setScale(0, BigDecimal.ROUND_UP);
				// 退还积分金额不能大于退货商品金额
				if (expected_return_money.compareTo(expected_return_accm_money) < 0) {
					expected_return_accm_money = expected_return_money;
				}
				// 总的退还积分不能大于总使用的积分
				if(usedMoney.compareTo(alreadyReturnIntegralMoney.add(expected_return_accm_money)) < 0){
					expected_return_accm_money = usedMoney.subtract(alreadyReturnIntegralMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
			
			if(expected_return_accm_money.compareTo(BigDecimal.ZERO) < 0){
				expected_return_accm_money = BigDecimal.ZERO;
			}
		}
		
		// 惠币支付的钱
		BigDecimal hjycoinMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280025"));
		// 商品明细中未记录惠币的情况走原来的逻辑
		if(hjycoinMoney != null && hjycoinMoney.compareTo(BigDecimal.ZERO) > 0 && getOrderDetailHjycoin(order_code).compareTo(BigDecimal.ZERO) == 0){
			// 订单总商品金额
			BigDecimal productMoney = getOrderProductMoney(order_code);
			// 已经退还的积分金额
			BigDecimal alreadyReturnHjycoin = getAlreadyReturnHjycoin(order_code);
			// 已经退货的商品数量
			int alreadyReturnSkuNum = getAlreadyReturnSkuNum(order_code);
			// 订单总的商品数量
			int orderAllSkuNum = getOrderSkuNum(order_code);

			// 订单中的商品已经全部退货则退还剩余的全部积分
			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
				expected_return_hjycoin = hjycoinMoney.subtract(alreadyReturnHjycoin).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			// 还有商品没有退货则根据比例拆分退货的积分
			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
				// 根据商品金额和总单商品金额的占比计算退还积分数量
				expected_return_hjycoin = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(hjycoinMoney).setScale(0, BigDecimal.ROUND_UP);
				// 退还积分金额不能大于退货商品金额
				if (expected_return_money.compareTo(expected_return_hjycoin) < 0) {
					expected_return_hjycoin = expected_return_money;
				}
				// 总的退还积分不能大于总使用的积分
				if(hjycoinMoney.compareTo(alreadyReturnHjycoin.add(expected_return_hjycoin)) < 0){
					expected_return_hjycoin = hjycoinMoney.subtract(alreadyReturnHjycoin).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
			
			if(expected_return_hjycoin.compareTo(BigDecimal.ZERO) < 0){
				expected_return_hjycoin = BigDecimal.ZERO;
			}
		}
		
//		// 储值金支付的钱
//		BigDecimal czjUsedMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280006"));
//		
//		// 商品明细中未记录储值金的情况走原来的逻辑
//		if(czjUsedMoney != null && czjUsedMoney.compareTo(BigDecimal.ZERO) > 0 && getOrderDetailCzjMoney(order_code).compareTo(BigDecimal.ZERO) == 0){
//			// 订单总商品金额
//			BigDecimal productMoney = getOrderProductMoney(order_code);
//			// 已经退还的储值金金额
//			BigDecimal alreadyReturnCzjMoney = getAlreadyReturnCzjMoney(order_code);
//			// 已经退货的商品数量
//			int alreadyReturnSkuNum = getAlreadyReturnSkuNum(order_code);
//			// 订单总的商品数量
//			int orderAllSkuNum = getOrderSkuNum(order_code);
//			
//			// 订单中的商品已经全部退货则退还剩余的全部储值金
//			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
//				expected_return_ppc_money = czjUsedMoney.subtract(alreadyReturnCzjMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//			}
//			
//			// 还有商品没有退货则根据比例拆分退货的储值金
//			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
//				// 根据商品金额和总单商品金额的占比计算退还储值金
//				expected_return_ppc_money = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(czjUsedMoney).setScale(0, BigDecimal.ROUND_UP);
//				// 退还储值金金额不能大于退货商品金额
//				if (expected_return_money.compareTo(expected_return_ppc_money) < 0) {
//					expected_return_ppc_money = expected_return_money;
//				}
//				// 总的退还储值金不能大于总使用的储值金
//				if(czjUsedMoney.compareTo(alreadyReturnCzjMoney.add(expected_return_ppc_money)) < 0){
//					expected_return_ppc_money = czjUsedMoney.subtract(alreadyReturnCzjMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//				}
//			}
//			
//			if(expected_return_ppc_money.compareTo(BigDecimal.ZERO) < 0){
//				expected_return_ppc_money = BigDecimal.ZERO;
//			}
//		}
//		
//		// 暂存款支付的钱
//		BigDecimal zckUsedMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280007"));
//		
//		// 商品明细中未记录暂存款的情况走原来的逻辑
//		if(zckUsedMoney != null && zckUsedMoney.compareTo(BigDecimal.ZERO) > 0 && getOrderDetailZckMoney(order_code).compareTo(BigDecimal.ZERO) == 0){
//			// 订单总商品金额
//			BigDecimal productMoney = getOrderProductMoney(order_code);
//			// 已经退还的暂存款金额
//			BigDecimal alreadyReturnZckMoney = getAlreadyReturnZckMoney(order_code);
//			// 已经退货的商品数量
//			int alreadyReturnSkuNum = getAlreadyReturnSkuNum(order_code);
//			// 订单总的商品数量
//			int orderAllSkuNum = getOrderSkuNum(order_code);
//			
//			// 订单中的商品已经全部退货则退还剩余的全部暂存款
//			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
//				expected_return_crdt_money = zckUsedMoney.subtract(alreadyReturnZckMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//			}
//			
//			// 还有商品没有退货则根据比例拆分退货的暂存款
//			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
//				// 根据商品金额和总单商品金额的占比计算退还暂存款数量
//				expected_return_crdt_money = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(zckUsedMoney).setScale(0, BigDecimal.ROUND_UP);
//				// 退还暂存款金额不能大于退货商品金额
//				if (expected_return_money.compareTo(expected_return_crdt_money) < 0) {
//					expected_return_crdt_money = expected_return_money;
//				}
//				// 总的退还暂存款不能大于总使用的暂存款
//				if(zckUsedMoney.compareTo(alreadyReturnZckMoney.add(expected_return_crdt_money)) < 0){
//					expected_return_crdt_money = zckUsedMoney.subtract(alreadyReturnZckMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
//				}
//			}
//			
//			if(expected_return_crdt_money.compareTo(BigDecimal.ZERO) < 0){
//				expected_return_crdt_money = BigDecimal.ZERO;
//			}
//		}
		
		// 退货数据入库处理
		MDataMap addReturnGoodsMap = new MDataMap();
		// 退货主信息设置
		addReturnGoodsMap.put("return_code", returnNo);
		addReturnGoodsMap.put("buyer_code", orderInfo.get("buyer_code"));
		addReturnGoodsMap.put("order_code", order_code);
		addReturnGoodsMap.put("return_reason", return_reason.get("return_reson"));
		addReturnGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addReturnGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addReturnGoodsMap.put("create_time", now);
		// 需要寄回状态变更为待完善物流
		if("4497477800090001".equals(returnGoods.getFlag_return_goods())) {
			addReturnGoodsMap.put("status", "4497153900050005");//待完善物流状态
		}else {
			addReturnGoodsMap.put("status", "4497153900050004");// 商户审核状态
		}
//		addReturnGoodsMap.put("contacts", addr.get("after_sale_person"));
//		addReturnGoodsMap.put("mobile", addr.get("after_sale_mobile"));
//		addReturnGoodsMap.put("address", addr.get("after_sale_address"));
//		addReturnGoodsMap.put("receiver_area_code", addr.get("after_sale_postcode"));
		
		addReturnGoodsMap.put("contacts", returnGoods.getContacts());
		addReturnGoodsMap.put("mobile", returnGoods.getMobile());
		addReturnGoodsMap.put("address", returnGoods.getAddress());
		addReturnGoodsMap.put("receiver_area_code", returnGoods.getReceiver_area_code());
		
		addReturnGoodsMap.put("description", returnGoods.getDescription());
		addReturnGoodsMap.put("pic_url", "");
		addReturnGoodsMap.put("third_order_code", orderInfo.get("out_order_code"));
		addReturnGoodsMap.put("transport_people", returnGoods.getTransport_people());
		addReturnGoodsMap.put("return_reason_code", returnGoods.getReturn_reason_code());
		addReturnGoodsMap.put("buyer_mobile", (String)DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",orderInfo.get("buyer_code"))));
		addReturnGoodsMap.put("goods_receipt", returnGoods.getGoods_receipt());
		addReturnGoodsMap.put("freight", returnGoods.getFreight());
		addReturnGoodsMap.put("transport_money", StringUtils.equals(returnGoods.getFreight(), "4497476900040001")?orderInfo.get("transport_money"):"0");
		addReturnGoodsMap.put("expected_return_money", String.valueOf(expected_return_money.add(new BigDecimal(addReturnGoodsMap.get("transport_money")))));
		addReturnGoodsMap.put("expected_return_group_money", expected_return_group_money.toString());
		addReturnGoodsMap.put("expected_return_accm_money", expected_return_accm_money.toString());
		addReturnGoodsMap.put("expected_return_give_accm_money", expected_return_give_accm_money.toString());
		addReturnGoodsMap.put("expected_return_hjycoin_money", expected_return_hjycoin.toString());
		addReturnGoodsMap.put("expected_return_give_hjycoin_money", expected_return_give_hjycoin.toString());
		addReturnGoodsMap.put("expected_return_ppc_money", expected_return_ppc_money.toString());
		addReturnGoodsMap.put("expected_return_crdt_money", expected_return_crdt_money.toString());
		addReturnGoodsMap.put("flag_return_goods", returnGoods.getFlag_return_goods());
		// 退货主信息入库
		DbUp.upTable("oc_return_goods").dataInsert(addReturnGoodsMap);
		
		MDataMap addLogMap = new MDataMap();
		// 退货日志信息设置
		addLogMap.put("return_no", returnNo);
		addLogMap.put("info", returnGoods.getDescription());
		addLogMap.put("create_time", now);
		addLogMap.put("create_user", UserFactory.INSTANCE.create().getUserCode());
		addLogMap.put("status", "4497153900050004");
		// 插入新的换货状态日志信息
		DbUp.upTable("lc_return_goods_status").dataInsert(addLogMap);
		
		
		//****************************过渡逻辑********************************
		String afterSaleStatus = "4497477800050005";
		// 需要寄回状态变更为待完善物流
		if("4497477800090001".equals(returnGoods.getFlag_return_goods())) {
			afterSaleStatus = "4497477800050010";
		}
		
		if(Constants.SMALL_SELLER_CODE_JD.equals(addReturnGoodsMap.get("small_seller_code"))) {
			MDataMap jdOrderInfo = DbUp.upTable("oc_order_jd").one("order_code", order_code);
			MWebResult typeResult = new JdAfterSaleSupport().getAfsPickwareType(jdOrderInfo.get("jd_order_id"), jdOrderInfo.get("sku_id"));
			// 如果支持上门取件则没有待完善物流这一步
			if(typeResult.upFlagTrue() && typeResult.getResultList().contains("4")) {
				afterSaleStatus = "4497477800050005";
			}
		}
		
		MDataMap oasMap=new MDataMap();
		oasMap.put("asale_code", returnNo);
		oasMap.put("order_code", order_code);
		oasMap.put("buyer_code", orderInfo.get("buyer_code"));
		oasMap.put("buyer_mobile", addReturnGoodsMap.get("buyer_mobile"));
		oasMap.put("seller_code", addReturnGoodsMap.get("seller_code"));
		oasMap.put("small_seller_code", addReturnGoodsMap.get("small_seller_code"));
		oasMap.put("asale_reason", addReturnGoodsMap.get("return_reason_code"));
		oasMap.put("asale_status", afterSaleStatus);
		oasMap.put("asale_remark", returnGoods.getDescription());
		oasMap.put("asale_type", "4497477800030001");//退货
		oasMap.put("asale_source", "4497477800060002");
		oasMap.put("create_time", now);
		oasMap.put("update_time", now);
		oasMap.put("flow_end", "0");
		DbUp.upTable("oc_order_after_sale").dataInsert(oasMap);
		
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", returnNo);
		loasMap.put("create_user", addLogMap.get("create_user"));
		loasMap.put("create_time", now);
		loasMap.put("asale_status", oasMap.get("asale_status"));
		loasMap.put("remark", addLogMap.get("info"));
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", returnNo);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070001");
		lsasMap.put("create_time", now);
		
		int integarl = new PlusServiceAccm().moneyToAccmAmt(expected_return_accm_money, 0, BigDecimal.ROUND_DOWN).intValue();
		BigDecimal money = expected_return_money.subtract(expected_return_accm_money).subtract(expected_return_hjycoin).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		// 发起售后申请
		MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100007");
		String order_status=(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",orderInfo.get("order_status")));
		String isGetProduceName = "4497476900040001".equals(returnGoods.getGoods_receipt())?"是":"否";
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), order_status,return_reason.get("return_reson"),isGetProduceName,money,integarl,MoneyHelper.format(expected_return_hjycoin),returnGoods.getDescription()));
		lsasMap.put("serial_title", "客服发起了申请");
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		// 客服审核
		templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100008");
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), addReturnGoodsMap.get("address"),addReturnGoodsMap.get("contacts"),addReturnGoodsMap.get("mobile"),addReturnGoodsMap.get("receiver_area_code")));
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
		
		/**
		 * 客服申请退货暂时注掉状态
		 */
//		if("4497477800090001".equals(returnGoods.getFlag_return_goods())){
//			MDataMap shipmentsMap = new MDataMap();
//			shipmentsMap.put("order_code", returnNo);
//			shipmentsMap.put("creator", "system");
//			shipmentsMap.put("create_time", now);
//			shipmentsMap.put("is_send100_flag", "0");
//			shipmentsMap.put("send_count", "0");
//			shipmentsMap.put("update_time", now);
//			shipmentsMap.put("update_user", "system");
//			shipmentsMap.put("shipments_code", "");
//			shipmentsMap.put("is_send100_flag", "1");
//			DbUp.upTable("oc_order_shipments").dataInsert(shipmentsMap);
//		}
		
		
		WebHelper.unLock(lock_code);
		
		if(Constants.SMALL_SELLER_CODE_JD.equals(DbUp.upTable("oc_orderinfo").dataGet("small_seller_code", "", new MDataMap("order_code",order_code)))) {
			if(new JdAfterSaleSupport().initJdAfterSale(returnNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new JdAfterSaleSupport().createAfterSaleServiceTask(returnNo);
			}
		}
		
		if("4497471600430002".equals(DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code)))) {
			DuohzAfterSaleSupport duohzAfterSaleSupport = new DuohzAfterSaleSupport();
			if(duohzAfterSaleSupport.initDuohzAfterSale(returnNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				duohzAfterSaleSupport.createAfterSaleServiceTask(returnNo);
			}
			duohzAfterSaleSupport.createApplyAfterSaleTask(returnNo);
		}
		
		
		// 如果赋予了积分再退货则需要调用积分的退货接口
		MDataMap changeMap = DbUp.upTable("mc_member_integral_change").one("member_code",orderInfo.get("buyer_code"),"change_type","449748080004","remark",order_code);
		if(changeMap != null){
			new PlusServiceAccm().addExecInfoForReturnGiveAccmAmt(returnNo);
		}
		/**
		 * 后台发起退货申请，需要校验该单是否是分销商品订单，如果是，写入定时，计算冻结金额
		 */
		if(result.getResultCode() == 1 && DbUp.upTable("fh_agent_order_detail").count("order_code",order_code)>0 && DbUp.upTable("za_exectimer").count("exec_info",returnNo,"exec_type","449746990025")<=0) {//退货申请成功并且是分销订单，需要写入定时
			JobExecHelper.createExecInfo("449746990025",returnNo, DateUtil.addMinute(5));
		}
		HjycoinService.addExecTimer("449746990037", returnNo);
		//判断是否有推广人，如果有，需要扣除推广人预估收益。
		if (DbUp.upTable("fh_tgz_order_detail").count("order_code",order_code)>0) {
			//申请退货，需要写入惠币定时任务
			HjycoinService.addExecTimer("449746990043", returnNo);
		}
		return result;
	}
	
	// 已经退还的积分金额
	public BigDecimal getAlreadyReturnIntegralMoney(String order_code){
		Object allReturnMoney = DbUp.upTable("oc_return_goods").dataGet("IFNULL(sum(expected_return_accm_money),0.0)", "order_code = :order_code AND `status` NOT IN('4497153900050006','4497153900050007')", new MDataMap("order_code", order_code));
		return allReturnMoney == null ? BigDecimal.ZERO : new BigDecimal(allReturnMoney.toString());
	}
	
	// 已经退还的储值金金额
	public BigDecimal getAlreadyReturnCzjMoney(String order_code){
		Object allReturnMoney = DbUp.upTable("oc_return_goods").dataGet("IFNULL(sum(expected_return_ppc_money),0.0)", "order_code = :order_code AND `status` != '4497153900050006'", new MDataMap("order_code", order_code));
		return allReturnMoney == null ? BigDecimal.ZERO : new BigDecimal(allReturnMoney.toString());
	}
	
	// 已经退还的暂存款金额
	public BigDecimal getAlreadyReturnZckMoney(String order_code){
		Object allReturnMoney = DbUp.upTable("oc_return_goods").dataGet("IFNULL(sum(expected_return_crdt_money),0.0)", "order_code = :order_code AND `status` != '4497153900050006'", new MDataMap("order_code", order_code));
		return allReturnMoney == null ? BigDecimal.ZERO : new BigDecimal(allReturnMoney.toString());
	}
	
	// 按SKU查询已经退还的积分金额
	public BigDecimal getAlreadyReturnIntegralMoney(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(rgd.return_accm_money),0) money FROM oc_return_goods rg, oc_return_goods_detail rgd"
				+ " WHERE rg.return_code = rgd.return_code"
				+ " AND rg.order_code = :order_code "
				+ " AND rgd.sku_code = :sku_code"
				+ " AND rg.`status` NOT IN('4497153900050006','4497153900050007')";
		Map<String, Object> result = DbUp.upTable("oc_return_goods").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? BigDecimal.ZERO : new BigDecimal(result.get("money").toString());
	}
	
	// 按SKU查询已经退还的储值金金额
	public BigDecimal getAlreadyReturnCzjMoney(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(rgd.return_ppc_money),0) money FROM oc_return_goods rg, oc_return_goods_detail rgd"
				+ " WHERE rg.return_code = rgd.return_code"
				+ " AND rg.order_code = :order_code "
				+ " AND rgd.sku_code = :sku_code"
				+ " AND rg.`status` NOT IN('4497153900050006','4497153900050007')";
		Map<String, Object> result = DbUp.upTable("oc_return_goods").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? BigDecimal.ZERO : new BigDecimal(result.get("money").toString());
	}
	
	// 按SKU查询已经退还的暂存款金额
	public BigDecimal getAlreadyReturnZckMoney(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(rgd.return_crdt_money),0) money FROM oc_return_goods rg, oc_return_goods_detail rgd"
				+ " WHERE rg.return_code = rgd.return_code"
				+ " AND rg.order_code = :order_code "
				+ " AND rgd.sku_code = :sku_code"
				+ " AND rg.`status` NOT IN('4497153900050006','4497153900050007')";
		Map<String, Object> result = DbUp.upTable("oc_return_goods").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? BigDecimal.ZERO : new BigDecimal(result.get("money").toString());
	}
	
	// 已经退货的商品数量
	public int getAlreadyReturnSkuNum(String order_code){
		Map<String, Object> result = DbUp.upTable("oc_return_goods_detail").dataSqlOne("SELECT IFNULL(SUM(`count`),0) skuNum FROM oc_return_goods_detail WHERE return_code IN (SELECT rg.return_code FROM oc_return_goods rg WHERE rg.order_code = :order_code AND status NOT IN('4497153900050006','4497153900050007'))", new MDataMap("order_code", order_code));
		return result == null ? 0 : Integer.parseInt(result.get("skuNum").toString());
	}
	
	// 按SKU查询已经退货的商品数量
	public int getAlreadyReturnSkuNum(String order_code,String skuCode){
		String sql = "SELECT IFNULL(SUM(`count`),0) skuNum FROM oc_return_goods_detail WHERE"
				+ " return_code IN (SELECT rg.return_code FROM oc_return_goods rg WHERE rg.order_code = :order_code AND status NOT IN('4497153900050006','4497153900050007')) "
				+ " AND sku_code = :sku_code";
		Map<String, Object> result = DbUp.upTable("oc_return_goods_detail").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", skuCode));
		return result == null ? 0 : Integer.parseInt(result.get("skuNum").toString());
	}
	
	// 订单总商品数量
	public int getOrderSkuNum(String order_code){
		Object result = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return result == null ? 0 : Integer.parseInt(result.toString());
	}
	
	// 按SKU查询总商品数量
	public int getOrderSkuNum(String order_code,String skuCode){
		Object result = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num)", "", new MDataMap("order_code", order_code, "sku_code", skuCode, "gift_flag", "1"));
		return result == null ? 0 : Integer.parseInt(result.toString());
	}
	
	// 统计商品明细上面的积分总金额
	public BigDecimal getOrderDetailIntegralMoney(String order_code){
		Object money = DbUp.upTable("oc_orderdetail").dataGet("sum(integral_money)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return money == null ? BigDecimal.ZERO : new BigDecimal(money.toString());
	}
	
	// 统计商品明细上面的储值金总金额
	public BigDecimal getOrderDetailCzjMoney(String order_code){
		Object money = DbUp.upTable("oc_orderdetail").dataGet("sum(czj_money)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return money == null ? BigDecimal.ZERO : new BigDecimal(money.toString());
	}
	
	// 统计商品明细上面的暂存款总金额
	public BigDecimal getOrderDetailZckMoney(String order_code){
		Object money = DbUp.upTable("oc_orderdetail").dataGet("sum(zck_money)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return money == null ? BigDecimal.ZERO : new BigDecimal(money.toString());
	}
	
	// 订单总商品金额
	public BigDecimal getOrderProductMoney(String order_code){
		Object productMoney = DbUp.upTable("oc_orderdetail").dataGet("sum(sku_num*sku_price)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
		return productMoney == null ? BigDecimal.ZERO : new BigDecimal(productMoney.toString());
	}
	
	/**
	  * 查询已经退还的惠币数
	  * @param order_code
	  * @param sku_code
	  * @return
	  * 2020-7-15
	  * Angel Joy
	  * BigDecimal
	  */
	 public BigDecimal getAlreadyReturnHjyCoin(String order_code, String sku_code) {
	  String sql = "SELECT IFNULL(SUM(rgd.return_hjycoin_money),0) money FROM oc_return_goods rg, oc_return_goods_detail rgd"
	    + " WHERE rg.return_code = rgd.return_code"
	    + " AND rg.order_code = :order_code "
	    + " AND rgd.sku_code = :sku_code"
	    + " AND rg.`status` NOT IN('4497153900050006','4497153900050007')";
	  Map<String, Object> result = DbUp.upTable("oc_return_goods").dataSqlOne(sql, new MDataMap("order_code", order_code, "sku_code", sku_code));
	  return result == null ? BigDecimal.ZERO : new BigDecimal(result.get("money").toString());
	 }

	 /**
	  * 获取订单使用的惠币
	  * @param order_code
	  * @return
	  * 2020-7-15
	  * Angel Joy
	  * BigDecimal
	  */
	 public BigDecimal getOrderDetailHjycoin(String order_code) {
	  Object money = DbUp.upTable("oc_orderdetail").dataGet("sum(hjycoin)", "", new MDataMap("order_code", order_code, "gift_flag", "1"));
	  return money == null ? BigDecimal.ZERO : new BigDecimal(money.toString());
	 }

	 /**
	  * 获取订单已经退还的惠币
	  * @param order_code
	  * @return
	  * 2020-7-15
	  * Angel Joy
	  * BigDecimal
	  */
	 public BigDecimal getAlreadyReturnHjycoin(String order_code) {
	  Object allReturnMoney = DbUp.upTable("oc_return_goods").dataGet("IFNULL(sum(expected_return_hjycoin_money),0.0)", "order_code = :order_code AND `status` NOT IN('4497153900050006','4497153900050007')", new MDataMap("order_code", order_code));
	  return allReturnMoney == null ? BigDecimal.ZERO : new BigDecimal(allReturnMoney.toString());
	 }
}
