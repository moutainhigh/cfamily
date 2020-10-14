package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForReturnGoodsInput;
import com.cmall.familyhas.api.result.ApiForReturnGoodsResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.service.ReturnGoodsService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport;
import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.srnpr.xmasorder.service.TeslaCrdtService;
import com.srnpr.xmasorder.service.TeslaPpcService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 售后退/换货处理
 * <p>
 * 目前只支持第三方商户的订单
 * <p>
 * <p>
 * 	AngelJoy 变更 
 * 	除了跨境通商户，均允许在线申请售后。  
 * <p>
 * <p>
 * 	AngelJoy 变更 
 * 	分销相关逻辑新增，如果是分销单，需要写入分销定时系统，LD品写入走LD定时通知。
 * <p>
 * @author jlin
 *
 */
public class ApiForReturnGoods extends RootApiForToken<ApiForReturnGoodsResult, ApiForReturnGoodsInput> {

	@Override
	public ApiForReturnGoodsResult Process(ApiForReturnGoodsInput inputParam, MDataMap mRequestMap) {
		String order_code = inputParam.getOrderCode();
		ApiForReturnGoodsResult result = new ApiForReturnGoodsResult();
		MDataMap mmap = DbUp.upTable("oc_orderinfo").one("order_code", order_code);
		if(mmap != null&&!"SI2003".equals(mmap.get("small_seller_code"))){
			result = mainReCha(inputParam,mRequestMap);
			if(result.getResultCode() == 1&&DbUp.upTable("fh_agent_order_detail").count("order_code",order_code)>0) {//退货申请成功并且是分销订单，需要写入定时
				JobExecHelper.createExecInfo("449746990025",result.getAfterCode(), DateUtil.addMinute(5));
			}
		}else{
			//LD退货逻辑
			result = ldReturn(inputParam,mRequestMap);
		}
		return result;
	}
	
	public ApiForReturnGoodsResult ldReturn(ApiForReturnGoodsInput inputParam, MDataMap mRequestMap){
		
		ApiForReturnGoodsResult result = new ApiForReturnGoodsResult();
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if ("N".equals(isSyncLd)) {// 添加开关
			return result;
		}
		String order_code = inputParam.getOrderCode();
		String sku_code = inputParam.getSkuCode();
		String buyer_code = getUserCode();
		List<String> certificatePicList =  inputParam.getCertificatePic();
		String certificatePic = "";
		for(String pic : certificatePicList){
			certificatePic += "|"+pic;
		}
		String reimburseTips = inputParam.getReimburseTips();
		String reimburseReasonCode = inputParam.getReimburseReason();
		String reimburseMoney = inputParam.getReimburseMoney();//退款金额
		String isGetProduce = inputParam.getIsGetProduce();//是否收到货
		BigDecimal totalMoney = new BigDecimal(reimburseMoney);
		int produceNum = inputParam.getProduceNum();
		String reimburseType = inputParam.getReimburseType();
		//校验售后单数量
		String reimburseReason = "";
		try{
			//after_sales_type : 449747660003  退货 ，449747660004 换货
			MDataMap reasonMap = new MDataMap();
			if("4497477800030003".equals(reimburseType)){//换货原因
				reasonMap = DbUp.upTable("oc_return_goods_reason").one("return_reson_code",reimburseReasonCode,"after_sales_type","449747660004");
			}else{//退货原因
				reasonMap = DbUp.upTable("oc_return_goods_reason").one("return_reson_code",reimburseReasonCode,"after_sales_type","449747660003");
			}
			reimburseReason = reasonMap.get("return_reson");
		}catch(Exception e){
			e.getStackTrace();
		}
		String ordSeq = inputParam.getOrderSeq();
		Integer orderCode = 0,goodId = 0;//orderCode 为LD订单号
		Integer allowCount = 0;
		if(order_code.contains("DD")||order_code.contains("HH")){//APP订单
			Integer allowCountLd = new AfterSaleService().getAllowCount(order_code,sku_code);//接口允许申请的售后数量
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			String sql = "SELECT SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+orderInfo.get("out_order_code")+"' AND after_sale_status = '01' AND if_post = '2'";
			Integer localCount = 0;
			try{
				Map<String,Object> afterSaleLd = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
				localCount = Integer.parseInt(afterSaleLd.get("good_cnt").toString());
			}catch(Exception e){
				e.getStackTrace();
			}
			allowCount = allowCountLd - localCount;
			if(allowCount < produceNum){
				result.setResultCode(100000);
				result.setResultMessage("填写数量超过了可允许售后数量，请核查后提交。");
				return result;
			}
			orderCode = Integer.parseInt(orderInfo.get("out_order_code"));
		}else{
			orderCode = Integer.parseInt(order_code);
			Integer localCount = 0;
			String sql = "SELECT SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+order_code+"' AND order_seq = "+ordSeq+" AND after_sale_status = '01' AND if_post = '2'";
			try{
				Map<String,Object> afterSaleLd = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
				localCount = Integer.parseInt(afterSaleLd.get("good_cnt").toString());
			}catch(Exception e){
				e.getStackTrace();
			}
			allowCount = 1 - localCount;
			if(allowCount < produceNum){
				result.setResultCode(100000);
				result.setResultMessage("填写数量超过了可允许售后数量，请核查后提交。");
				return result;
			}
		}
		try{
			MDataMap productInfo = DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
			if(productInfo == null || productInfo.isEmpty()){
				goodId = Integer.parseInt(sku_code);//LD 订单在APP没有商品时，skuCode默认为productCode
			}else{
				goodId = Integer.parseInt(productInfo.get("product_code"));
			}
		}catch(Exception e){
			
		}
		if(orderCode == 0){//没有LD订单号
			result.setResultCode(10000);
			result.setResultMessage("系统异常，请联系客服");
			return result;
		}
		if(goodId == 0){//没有查询到商品
			result.setResultCode(10000);
			result.setResultMessage("系统异常，请联系客服");
			return result;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String afterSaleCode = "";
		String return_money = totalMoney.divide(new BigDecimal(produceNum)).toString();
		for(int i = 0;i<produceNum;i++){
			MDataMap afterSaleLd = new MDataMap();
			String after_sale_code_app ="";
			afterSaleLd.put("uid", UUID.randomUUID().toString().replace("-", ""));
			afterSaleLd.put("order_code", orderCode+"");
			afterSaleLd.put("product_code", goodId+"");
			afterSaleLd.put("sku_code", sku_code);
			afterSaleLd.put("member_code", buyer_code);
			afterSaleLd.put("good_cnt", "1");
			afterSaleLd.put("return_money", return_money);
			afterSaleLd.put("is_get_product", isGetProduce);
			if(!StringUtils.isEmpty(ordSeq)){
				afterSaleLd.put("order_seq", ordSeq);
			}
			if("4497477800030003".equals(reimburseType)){//4497477800030003 == 换货
				afterSaleLd.put("after_sale_type","2");//换货
				after_sale_code_app = WebHelper.upCode("CGS")+"LD";//生成售后单号
			}
			if("4497477800030001".equals(reimburseType)){//4497477800030001 == 退货退款
				afterSaleLd.put("after_sale_type","1");//退货退款
				after_sale_code_app = WebHelper.upCode("RGS")+"LD";//生成售后单号
			}
			afterSaleLd.put("after_sale_code_app", after_sale_code_app);
			afterSaleLd.put("after_sale_status", "01");//待审核
			afterSaleLd.put("if_post", "2");//待审核
			afterSaleLd.put("create_time", sdf.format(new Date()));
			afterSaleLd.put("reason", reimburseReason);
			afterSaleLd.put("after_image", certificatePic);
			afterSaleLd.put("remark", reimburseTips);
			DbUp.upTable("oc_after_sale_ld").dataInsert(afterSaleLd);
			//记录日志
			MDataMap log = new MDataMap();
			log.put("after_sale_code_app", after_sale_code_app);
			log.put("after_sale_status", "01");
			log.put("remark", reimburseReason);
			log.put("create_time", sdf.format(new Date()));
			log.put("operator", "App申请");
			log.put("uid", UUID.randomUUID().toString().replace("-", ""));
			DbUp.upTable("lc_after_sale_ld_return").dataInsert(log);
			afterSaleCode += after_sale_code_app+",";
		}
		result.setAfterCode(afterSaleCode.substring(0, afterSaleCode.length()-1));
		//变更是否可申请售后按钮
		Integer count = 0;
		if(order_code.contains("DD")||order_code.contains("HH")){
			String sql = "SELECT SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+orderCode+"' AND after_sale_status != '04' AND after_sale_status != '03'";
			Map<String,Object> ldAsale = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
			if(ldAsale != null){
				String countStr = ldAsale.get("good_cnt")!=null?ldAsale.get("good_cnt").toString():"0";
				count = Integer.parseInt(countStr);
			}
			MDataMap orderDetail = DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
			
			if(orderDetail != null && !orderDetail.isEmpty()){
				String orderCount = orderDetail.get("sku_num");
				Integer skuNum = Integer.parseInt(orderCount);
				if(count >= skuNum){
					orderDetail.put("flag_asale", "1");
					DbUp.upTable("oc_orderdetail").dataUpdate(orderDetail, "flag_asale", "uid");
				}
			}
		}
		return result;
	}	
	
	/**
	 * 原有逻辑：只允许第三方商户退换货
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	public ApiForReturnGoodsResult mainReCha(ApiForReturnGoodsInput inputParam, MDataMap mRequestMap){
		ApiForReturnGoodsResult result = new ApiForReturnGoodsResult();
		
		String order_code = inputParam.getOrderCode();
		String sku_code = inputParam.getSkuCode();
		String reimburseType = inputParam.getReimburseType();
		String buyer_code = getUserCode();
		String reimburseReason = inputParam.getReimburseReason();
		String reimburseMoney = inputParam.getReimburseMoney();
		String isGetProduce = inputParam.getIsGetProduce();
		int produceNum = inputParam.getProduceNum();
		String reimburseTips = inputParam.getReimburseTips();
		List<String> certificatePic = inputParam.getCertificatePic();
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", order_code, "buyer_code", buyer_code);
		if (orderInfo == null || orderInfo.isEmpty()) {
			result.setResultCode(916422141);
			result.setResultMessage(bInfo(916422141, order_code));
			return result;
		}
		
		String small_seller_code = orderInfo.get("small_seller_code");
		String order_status = orderInfo.get("order_status");
		String success_time = orderInfo.get("update_time");// 交易成功的时间
		
		// 判断订单状态
		if (!"4497153900010003".equals(order_status)
				&& !"4497153900010005".equals(order_status)) {
			result.setResultCode(916422162);
			result.setResultMessage(bInfo(916422162));
			return result;
		}
		
		// 验证退换货数量
//		int maxReturnNum = new ReturnGoodsService().getAchangeNum(order_code, sku_code);
		
		int count=0;
		Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available", new MDataMap("order_code",order_code,"sku_code",sku_code,"available","0"));
		if(map1!=null&&!map1.isEmpty()){
			Object obj=map1.get("count");
			if(obj!=null){
				count=((BigDecimal) obj).intValue();
			}
		}
		
		MDataMap detail=DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
		
		if (produceNum > (Integer.valueOf(detail.get("sku_num"))-count)) {
			result.setResultCode(916422151);
			result.setResultMessage(bInfo(916422151, sku_code));
			return result;
		}
		boolean flag = false;//这次换货结束后已换货+退货数量
		//如果等于此次购买的数量，则订单不可以再进行售后
		if((produceNum + count) == Integer.valueOf(detail.get("sku_num"))){
			flag = true;
		}
		
		// 京东商品目前只允许一次申请一件售后
		if(Constants.SMALL_SELLER_CODE_JD.equals(orderInfo.get("small_seller_code"))) {
			if(produceNum > 1) {
				// 当前商品每次仅支持申请一件进行售后，请修改数量后重试!
				result.setResultCode(916423800);
				result.setResultMessage(bInfo(916423800));
				return result;
			}
		}
		
		if(StringUtils.isBlank(reimburseTips)){
			reimburseTips="无";
		}
		
		// 判断是否为第三方商户的订单
//		if (StringUtils.startsWith(small_seller_code, "SF031")) {
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		if(StringUtils.isNotBlank(WebHelper.getSellerType(small_seller_code))){
			// 15天可以换货 ，7天可以退货 //目前没有 仅退款 逻辑
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// 退货流程
			if ("4497477800030001".equals(reimburseType)) {
				if (differ < 604800000l) {
					
					
					//正在进行的换货操作，不允许退货
//					if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030003","flow_end","0")>0){
//						result.setResultCode(916422155);
//						result.setResultMessage(bInfo(916422155));
//						return result;
//					}
					
					
					// 进入退货流程
					MWebResult webResult= retGoodsForPart(orderInfo, order_code, sku_code, buyer_code, reimburseReason, reimburseMoney, isGetProduce, produceNum, reimburseTips, certificatePic,flag);
						result.setResultCode(webResult.getResultCode());
						result.setResultMessage(webResult.getResultMessage());
						if(webResult.upFlagTrue()){
							result.setAfterCode(webResult.getResultMessage());
						}
				} else {
					result.setResultCode(916422148);
					result.setResultMessage(FormatHelper.formatString(bInfo(916422148, order_code)));
					return result;
				}
			} else if ("4497477800030003".equals(reimburseType)) {

				if (differ < 1296000000l) {
					
//					MDataMap return_goods = DbUp.upTable("oc_return_goods").oneWhere("", "", "order_code=:order_code and status in ('4497153900050001','4497153900050003') ", "order_code",order_code);
//					if (return_goods != null) {
//						result.setResultCode(916422156);
//						result.setResultMessage(bInfo(916422156));
//						return result;
//					}
					//正在进行退货操作，不允许再进行换货操作
					if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030001","flow_end","0")>0){
						result.setResultCode(916422156);
						result.setResultMessage(bInfo(916422156));
						return result;
					}
					//正在进行换货操作，不允许再进行换货
					if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030003","flow_end","0")>0){
						result.setResultCode(916422155);
						result.setResultMessage("已经申请过换货，不可以再次申请换货，如有疑问请致电客服");
						return result;
					}
					
					MWebResult webResult= ChaGoodsForPart(orderInfo, order_code, sku_code, buyer_code, reimburseReason, reimburseMoney, isGetProduce, produceNum, reimburseTips, certificatePic,flag);
					result.setResultCode(webResult.getResultCode());
					result.setResultMessage(webResult.getResultMessage());
					if(webResult.upFlagTrue()){
						result.setAfterCode(webResult.getResultMessage());
					}
				} else {
					result.setResultCode(916422149);
					result.setResultMessage(FormatHelper.formatString(bInfo(916422149, order_code)));
				}
			} else {
				result.setResultCode(916422150);
				result.setResultMessage(bInfo(916422150, order_code));
			}
			
		} else {
			result.setResultCode(916422146);
			result.setResultMessage(bInfo(916422146, order_code));
		}

		return result;

	}
	
	/***
	 * 时间差
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public long differTimeNow(String time) throws ParseException {
		return new Date().getTime() - DateUtil.sdfDateTime.parse(time).getTime();
	}
	
	
	/**
	 * 添加部分退货单
	 * 
	 * @param returnGoods
	 * @return
	 */
	public MWebResult retGoodsForPart(MDataMap orderInfo,String order_code, String sku_code, String buyer_code, String reimburseReason, String reimburseMoney,
			String isGetProduce, int produceNum, String reimburseTips, List<String> certificatePic,boolean flag) {
		
		MWebResult result = new MWebResult();
		
		//正在进行的换货操作，不允许退货
		//现在允许退货
//		if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030003","flow_end","0")>0){
//			result.inErrorMessage(916422114);
//			return result;
//		}
		
		// 生成退货编号
		String returnNo = WebHelper.upCode("RGS");
		
		MDataMap detail=DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
		
		BigDecimal expected_return_money = (new BigDecimal(produceNum)).multiply(new BigDecimal(detail.get("sku_price")));
		BigDecimal expected_return_group_money = (new BigDecimal(produceNum)).multiply(new BigDecimal(detail.get("group_price")));
		//退款公式 支付金额+微公社支付金额+运费    目前运费都为0
		
//		if(!((new BigDecimal(reimburseMoney)).compareTo(expected_return_money.add(expected_return_group_money))==0)){
		if(!((new BigDecimal(reimburseMoney)).compareTo(expected_return_money)==0)){
			result.inErrorMessage(916422152);
			return result;
		}
		
		MDataMap return_reason=DbUp.upTable("oc_return_goods_reason").one("return_reson_code",reimburseReason);
		if(return_reason==null||return_reason.isEmpty()){
			result.inErrorMessage(916422117);
			return result;
		}
		
		MDataMap addr = new MDataMap();
		MDataMap product = DbUp.upTable("pc_productinfo").one("product_code",detail.get("product_code"));
		if(product.get("after_sale_address_uid") != null && !"".equals(product.get("after_sale_address_uid"))) {
			addr = DbUp.upTable("oc_address_info").one("uid",product.get("after_sale_address_uid"),"small_seller_code",orderInfo.get("small_seller_code"));
		}else {
			addr = DbUp.upTable("oc_address_info").oneWhere("", "zid desc ", "small_seller_code=:small_seller_code", "small_seller_code",orderInfo.get("small_seller_code"));
		}
		//多货主售后地址特殊处理
		if("4497471600430002".equals(orderInfo.get("delivery_store_type"))) {
			addr = new DuohzAfterSaleSupport().getDuohzAfterSaleAddr(orderInfo.get("order_code"));
		}
		if(addr==null||addr.isEmpty()){
			result.inErrorMessage(916422116);
			return result;
		}
		
		ReturnGoodsService goodsService = new ReturnGoodsService();
		
		// 预期退还的积分金额
		BigDecimal returnIntegralMoney = BigDecimal.ZERO;
		// 此SKU使用的积分金额
		BigDecimal integralMoney = new BigDecimal(detail.get("integral_money"));
		// 商品明细上面记录积分金额则按照明细记录计算退还积分金额
		if(integralMoney.compareTo(BigDecimal.ZERO) > 0){
			// 此SKU已经退还的积分金额
			BigDecimal alreadyReturnIntegralMoneyForSku = goodsService.getAlreadyReturnIntegralMoney(order_code, sku_code);
			// 此SKU已经退货的商品数量
			int alreadyReturnSkuNumForSku = goodsService.getAlreadyReturnSkuNum(order_code, sku_code);
			// 退货SKU总金额
			BigDecimal returnSkuMoney = new BigDecimal(produceNum).multiply(new BigDecimal(detail.get("sku_price")));
			// SKU总金额
			BigDecimal skuMoney = new BigDecimal(detail.get("sku_num")).multiply(new BigDecimal(detail.get("sku_price")));
			
			// 如果已经全部退还则把剩余积分都退还
			if((alreadyReturnSkuNumForSku + produceNum) >= goodsService.getOrderSkuNum(order_code, sku_code)){
				returnIntegralMoney = integralMoney.subtract(alreadyReturnIntegralMoneyForSku);
			}else{
				// 按金额占比计算退还的积分金额
				returnIntegralMoney = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(integralMoney).setScale(0, BigDecimal.ROUND_UP);
				// 不能超过总使用积分
				if(alreadyReturnIntegralMoneyForSku.add(returnIntegralMoney).compareTo(integralMoney) > 0){
					returnIntegralMoney = integralMoney.subtract(alreadyReturnIntegralMoneyForSku);
				}
				// 不能超过退货商品金额
				if(returnIntegralMoney.compareTo(returnSkuMoney) > 0){
					returnIntegralMoney = returnSkuMoney;
				}
			}
			
			if(returnIntegralMoney.compareTo(BigDecimal.ZERO) < 0){
				returnIntegralMoney = BigDecimal.ZERO;
			}
		}
		
		//预计退还的惠币金额
		BigDecimal returnHjycoin = BigDecimal.ZERO;
		//此SKU使用的惠币
		BigDecimal hjycoin = new BigDecimal(detail.get("hjycoin"));
		if(hjycoin.compareTo(BigDecimal.ZERO)>0) {

			// 此SKU已经退还的惠币金额
			BigDecimal alreadyReturnHjycoinForSku = goodsService.getAlreadyReturnHjyCoin(order_code, sku_code);
			// 此SKU已经退货的商品数量
			int alreadyReturnSkuNumForSku = goodsService.getAlreadyReturnSkuNum(order_code, sku_code);
			// 退货SKU总金额
			BigDecimal returnSkuMoney = new BigDecimal(produceNum).multiply(new BigDecimal(detail.get("sku_price")));
			// SKU总金额
			BigDecimal skuMoney = new BigDecimal(detail.get("sku_num")).multiply(new BigDecimal(detail.get("sku_price")));
			
			// 如果已经全部退还则把剩余惠币都退还
			if((alreadyReturnSkuNumForSku + produceNum) >= goodsService.getOrderSkuNum(order_code, sku_code)){
				returnHjycoin = hjycoin.subtract(alreadyReturnHjycoinForSku);
			}else{
				// 按金额占比计算退还的积分金额
				returnHjycoin = returnSkuMoney.divide(skuMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(hjycoin).setScale(0, BigDecimal.ROUND_UP);
				// 不能超过总使用积分
				if(alreadyReturnHjycoinForSku.add(returnHjycoin).compareTo(hjycoin) > 0){
					returnHjycoin = hjycoin.subtract(alreadyReturnHjycoinForSku);
				}
				// 不能超过退货商品金额
				if(returnHjycoin.compareTo(returnSkuMoney) > 0){
					returnHjycoin = returnSkuMoney;
				}
			}
			
			if(returnHjycoin.compareTo(BigDecimal.ZERO) < 0){
				returnHjycoin = BigDecimal.ZERO;
			}
		
		}
		
		// 积分支付的钱
		BigDecimal usedMoney = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280008"));
		// 商品明细未记录积分金额的情况下按照整单占比拆分积分
		if(usedMoney != null && usedMoney.compareTo(BigDecimal.ZERO) > 0 && goodsService.getOrderDetailIntegralMoney(order_code).compareTo(BigDecimal.ZERO) == 0){
			// 已经退还的积分金额
			BigDecimal alreadyReturnIntegralMoney = goodsService.getAlreadyReturnIntegralMoney(order_code);
			// 已经退货的商品数量
			int alreadyReturnSkuNum = goodsService.getAlreadyReturnSkuNum(order_code);
			// 订单总的商品数量
			int orderAllSkuNum = goodsService.getOrderSkuNum(order_code);
			// 订单总商品金额
			BigDecimal productMoney = goodsService.getOrderProductMoney(order_code);

			// 订单中的商品已经全部退货则退还剩余的全部积分
			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
				returnIntegralMoney = usedMoney.subtract(alreadyReturnIntegralMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			// 还有商品没有退货则根据比例拆分退货的积分
			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
				// 根据商品金额和总单商品金额的占比计算退还积分数量
				returnIntegralMoney = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(usedMoney).setScale(0, BigDecimal.ROUND_UP);
				// 退还积分不能超过退货商品金额
				if (expected_return_money.compareTo(returnIntegralMoney) < 0) {
					returnIntegralMoney = expected_return_money;
				}
				// 总的退还积分不能大于总使用的积分
				if(usedMoney.compareTo(alreadyReturnIntegralMoney.add(returnIntegralMoney)) < 0){
					returnIntegralMoney = usedMoney.subtract(alreadyReturnIntegralMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
			
			if(returnIntegralMoney.compareTo(BigDecimal.ZERO) < 0){
				returnIntegralMoney = BigDecimal.ZERO;
			}
		}
		//惠币支付的金额
		// 积分支付的钱
		BigDecimal usedHjycoin = (BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "", new MDataMap("order_code",order_code,"pay_type","449746280025"));
		// 商品明细未记录积分金额的情况下按照整单占比拆分积分
		if(usedHjycoin != null && usedHjycoin.compareTo(BigDecimal.ZERO) > 0 && goodsService.getOrderDetailHjycoin(order_code).compareTo(BigDecimal.ZERO) == 0){
			// 已经退还的积分金额
			BigDecimal alreadyReturnHjycoin = goodsService.getAlreadyReturnHjycoin(order_code);
			// 已经退货的商品数量
			int alreadyReturnSkuNum = goodsService.getAlreadyReturnSkuNum(order_code);
			// 订单总的商品数量
			int orderAllSkuNum = goodsService.getOrderSkuNum(order_code);
			// 订单总商品金额
			BigDecimal productMoney = goodsService.getOrderProductMoney(order_code);

			// 订单中的商品已经全部退货则退还剩余的全部积分
			if(orderAllSkuNum <= (alreadyReturnSkuNum + produceNum)){
				returnHjycoin = usedMoney.subtract(alreadyReturnHjycoin).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			// 还有商品没有退货则根据比例拆分退货的积分
			if(orderAllSkuNum > (alreadyReturnSkuNum + produceNum)){
				// 根据商品金额和总单商品金额的占比计算退还积分数量
				returnHjycoin = expected_return_money.divide(productMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(usedHjycoin).setScale(0, BigDecimal.ROUND_UP);
				// 退还积分不能超过退货商品金额
				if (expected_return_money.compareTo(returnHjycoin) < 0) {
					returnHjycoin = expected_return_money;
				}
				// 总的退还积分不能大于总使用的积分
				if(usedHjycoin.compareTo(alreadyReturnHjycoin.add(returnHjycoin)) < 0){
					returnHjycoin = usedHjycoin.subtract(alreadyReturnHjycoin).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
			
			if(returnHjycoin.compareTo(BigDecimal.ZERO) < 0){
				returnHjycoin = BigDecimal.ZERO;
			}
		}
		// 退货时应扣除的赋予积分数
		BigDecimal return_give_accm_money = new BigDecimal(detail.get("give_integral_money"));
		return_give_accm_money = return_give_accm_money.divide(new BigDecimal(detail.get("sku_num")), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(produceNum));
		
		// 退货时应扣除的赋予惠币数
		BigDecimal return_give_hjycoin = new BigDecimal(detail.get("give_hjycoin"));
		return_give_hjycoin = return_give_hjycoin.divide(new BigDecimal(detail.get("sku_num")), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(produceNum));
		
		BigDecimal returnCzjMoney = new TeslaPpcService().handlerReturnCzjMoney(order_code, sku_code, produceNum);
		// 已经退还的积分加上储值金超过商品金额的情况下，以商品的金额为准
		if(returnCzjMoney.add(returnIntegralMoney).add(returnHjycoin).compareTo(expected_return_money) > 0) {
			returnCzjMoney = expected_return_money.subtract(returnIntegralMoney).subtract(returnHjycoin);
			if(returnCzjMoney.compareTo(BigDecimal.ZERO) < 0) {
				returnCzjMoney = BigDecimal.ZERO;
			}
		}
		
		BigDecimal returnZckMoney = new TeslaCrdtService().handlerRetrunZckMoney(order_code, sku_code, produceNum);
		// 已经退还的积分和储值金加上暂存款超过商品金额的情况下，以商品的金额为准
		if(returnZckMoney.add(returnIntegralMoney).add(returnCzjMoney).add(returnHjycoin).compareTo(expected_return_money) > 0) {
			returnZckMoney = expected_return_money.subtract(returnIntegralMoney).subtract(returnCzjMoney).subtract(returnHjycoin);
			if(returnZckMoney.compareTo(BigDecimal.ZERO) < 0) {
				returnZckMoney = BigDecimal.ZERO;
			}
		}
		
		MDataMap addReturnGoodsDetailMap = new MDataMap();
		addReturnGoodsDetailMap.put("return_code", returnNo);
		addReturnGoodsDetailMap.put("sku_code", sku_code);
		addReturnGoodsDetailMap.put("count", String.valueOf(produceNum));
		addReturnGoodsDetailMap.put("sku_name", detail.get("sku_name"));
		addReturnGoodsDetailMap.put("current_price", detail.get("sku_price"));
		addReturnGoodsDetailMap.put("url", detail.get("product_picurl"));
		addReturnGoodsDetailMap.put("return_price", detail.get("sku_price"));
		addReturnGoodsDetailMap.put("return_accm_money",returnIntegralMoney.toString());
		addReturnGoodsDetailMap.put("return_hjycoin_money",returnHjycoin.toString());
		addReturnGoodsDetailMap.put("return_give_accm_money", return_give_accm_money.toString());
		addReturnGoodsDetailMap.put("return_give_hjycoin_money", return_give_hjycoin.toString());
		addReturnGoodsDetailMap.put("return_ppc_money",returnCzjMoney.toString());
		addReturnGoodsDetailMap.put("return_crdt_money",returnZckMoney.toString());
		DbUp.upTable("oc_return_goods_detail").dataInsert(addReturnGoodsDetailMap);
		
		String now =DateUtil.getSysDateTimeString();
		// 退货数据入库处理
		MDataMap addReturnGoodsMap = new MDataMap();
		// 退货主信息设置
		addReturnGoodsMap.put("return_code", returnNo);
		addReturnGoodsMap.put("buyer_code", buyer_code);
		addReturnGoodsMap.put("order_code", order_code);
		addReturnGoodsMap.put("return_reason", return_reason.get("return_reson"));
		addReturnGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addReturnGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addReturnGoodsMap.put("create_time", now);
		addReturnGoodsMap.put("contacts", addr.get("after_sale_person"));
		addReturnGoodsMap.put("status", "4497153900050003");
		addReturnGoodsMap.put("mobile", addr.get("after_sale_mobile"));
		addReturnGoodsMap.put("address", addr.get("after_sale_address"));
		addReturnGoodsMap.put("receiver_area_code", addr.get("after_sale_postcode"));
		addReturnGoodsMap.put("description", reimburseTips);
		addReturnGoodsMap.put("pic_url", "");
		addReturnGoodsMap.put("third_order_code", orderInfo.get("out_order_code"));
		addReturnGoodsMap.put("transport_people", "");
		addReturnGoodsMap.put("return_reason_code", reimburseReason);
		addReturnGoodsMap.put("buyer_mobile",(String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code",new MDataMap("member_code", orderInfo.get("buyer_code"))));
		addReturnGoodsMap.put("goods_receipt", isGetProduce);
		addReturnGoodsMap.put("freight", "4497476900040002");//运费不退
		addReturnGoodsMap.put("transport_money", "0");
		addReturnGoodsMap.put("expected_return_money",StringUtils.equals(orderInfo.get("pay_type"), "449716200001") ? String.valueOf(expected_return_money.add(new BigDecimal(addReturnGoodsMap.get("transport_money")))) : "0");
		addReturnGoodsMap.put("expected_return_group_money", expected_return_group_money.toString());
		addReturnGoodsMap.put("expected_return_accm_money", returnIntegralMoney.toString());
		addReturnGoodsMap.put("expected_return_hjycoin_money", returnHjycoin.toString());
		addReturnGoodsMap.put("expected_return_give_accm_money", return_give_accm_money.toString());
		addReturnGoodsMap.put("expected_return_give_hjycoin_money", return_give_hjycoin.toString());
		addReturnGoodsMap.put("expected_return_ppc_money", returnCzjMoney.toString());
		addReturnGoodsMap.put("expected_return_crdt_money", returnZckMoney.toString());
		addReturnGoodsMap.put("flag_return_goods", "4497477800090001");
		addReturnGoodsMap.put("delivery_store_type", DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code))+"");
		
		
		// 退货主信息入库
		DbUp.upTable("oc_return_goods").dataInsert(addReturnGoodsMap);
		
		MDataMap achangeInfo=new MDataMap();
		achangeInfo.put("order_code", order_code);
		achangeInfo.put("sku_code", sku_code);
		achangeInfo.put("oac_num", String.valueOf(produceNum));
		achangeInfo.put("oac_type", "4497477800030001");//退货
		achangeInfo.put("oac_status", "4497477800040001");
		achangeInfo.put("create_time", now);
		achangeInfo.put("update_time", now);
		achangeInfo.put("available", "0");
		achangeInfo.put("asale_source", "4497477800060001");
		achangeInfo.put("asale_code", returnNo);
		DbUp.upTable("oc_order_achange").dataInsert(achangeInfo);
		
		MDataMap addLogMap = new MDataMap();
		// 退货日志信息设置
		addLogMap.put("return_no", returnNo);
		addLogMap.put("info", "[用户提交售后]"+addReturnGoodsMap.get("return_reason"));
		addLogMap.put("create_time", now);
		addLogMap.put("create_user", buyer_code);
		addLogMap.put("status", addReturnGoodsMap.get("status"));
		// 插入新的换货状态日志信息
		DbUp.upTable("lc_return_goods_status").dataInsert(addLogMap);
		
		//****************************过渡逻辑********************************
		MDataMap oasMap=new MDataMap();
		oasMap.put("asale_code", returnNo);
		oasMap.put("order_code", order_code);
		oasMap.put("buyer_code", buyer_code);
		oasMap.put("buyer_mobile", addReturnGoodsMap.get("buyer_mobile"));
		oasMap.put("seller_code", addReturnGoodsMap.get("seller_code"));
		oasMap.put("small_seller_code", addReturnGoodsMap.get("small_seller_code"));
		oasMap.put("asale_reason", reimburseReason);
		oasMap.put("asale_status", "4497477800050003");//等待审核
		oasMap.put("asale_remark", reimburseTips);
		oasMap.put("asale_type", "4497477800030001");//退货
		oasMap.put("asale_source", "4497477800060001");
		oasMap.put("create_time", now);
		oasMap.put("update_time", now);
		oasMap.put("flow_end", "0");
		DbUp.upTable("oc_order_after_sale").dataInsert(oasMap);
		
		MDataMap oasdMap=new MDataMap();
		oasdMap.put("asale_code", returnNo);
		oasdMap.put("sku_code", sku_code);
		oasdMap.put("sku_num", String.valueOf(produceNum));
		oasdMap.put("sku_name", detail.get("sku_name"));
		oasdMap.put("sku_price", detail.get("sku_price"));
		oasdMap.put("product_code", detail.get("product_code"));
		oasdMap.put("product_picurl", detail.get("product_picurl"));
		DbUp.upTable("oc_order_after_sale_dtail").dataInsert(oasdMap);
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", returnNo);
		loasMap.put("create_user", buyer_code);
		loasMap.put("create_time", now);
		loasMap.put("asale_status", oasMap.get("asale_status"));
		loasMap.put("remark", addLogMap.get("info"));
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", returnNo);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070002");
		lsasMap.put("create_time", now);
		
		
		int integarl = new PlusServiceAccm().moneyToAccmAmt(returnIntegralMoney, 0, BigDecimal.ROUND_DOWN).intValue();
		BigDecimal money = expected_return_money.subtract(returnIntegralMoney).subtract(returnHjycoin).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		if("rejection".equals(reimburseReason)){
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100001");
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), return_reason.get("return_reson"), money, integarl,MoneyHelper.format(returnHjycoin),reimburseTips));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("template_code", templateMap.get("template_code"));
		}else{
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100007");
			String order_status=(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",orderInfo.get("order_status")));
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), order_status,return_reason.get("return_reson"),"4497476900040001".equals(isGetProduce)?"是":"否", money, integarl,MoneyHelper.format(returnHjycoin),reimburseTips));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("template_code", templateMap.get("template_code"));
		}
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		//添加备注图片
		if(certificatePic!=null){
			
			MDataMap orderRemark=new MDataMap();
			orderRemark.put("order_code",order_code);
			orderRemark.put("remark","[用户提交售后]"+addReturnGoodsMap.get("return_reason"));
			orderRemark.put("create_time",now);
			orderRemark.put("create_user_code",buyer_code);
			orderRemark.put("create_user_name","用户");
			orderRemark.put("remark_type","1");
			orderRemark.put("asale_code",returnNo);
			
			for (int i = 0; i < certificatePic.size(); i++) {
				if(i>4){
					break;
				}
				orderRemark.put("remark_picurl"+(i+1),certificatePic.get(i));
			}
			
			DbUp.upTable("oc_order_remark").dataInsert(orderRemark);
			
		}
		
		if(Constants.SMALL_SELLER_CODE_JD.equals(DbUp.upTable("oc_orderinfo").dataGet("small_seller_code", "", new MDataMap("order_code",order_code)))) {
			if(new JdAfterSaleSupport().initJdAfterSale(returnNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new JdAfterSaleSupport().createAfterSaleServiceTask(returnNo);
			}
		}
		
		if("4497471600430002".equals(DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code)))) {
			if(new DuohzAfterSaleSupport().initDuohzAfterSale(returnNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new DuohzAfterSaleSupport().createAfterSaleServiceTask(returnNo);
			}
		}
		
		//申请售后的商品数量 == 订单下单数量后
		if(flag){
			DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","1","sku_code",sku_code,"asale_code",returnNo), "", "order_code,sku_code");
		}
		
		// 如果赋予了积分再退货则需要调用积分的退货接口
		MDataMap changeMap = DbUp.upTable("mc_member_integral_change").one("member_code",orderInfo.get("buyer_code"),"change_type","449748080004","remark",order_code);
		if(changeMap != null){
			new PlusServiceAccm().addExecInfoForReturnGiveAccmAmt(returnNo);
		}
		//申请退货，需要写入惠币定时任务
		HjycoinService.addExecTimer("449746990037", returnNo);
		//判断是否有推广人，如果有，需要扣除推广人预估收益。
		if (DbUp.upTable("fh_tgz_order_detail").count("order_code",order_code)>0) {
			//申请退货，需要写入惠币定时任务
			HjycoinService.addExecTimer("449746990043", returnNo);
		}
		result.setResultMessage(returnNo);
		return result;
	}
	
	/**
	 * 部分换货
	 * @param orderInfo
	 * @param order_code
	 * @param sku_code
	 * @param buyer_code
	 * @param reimburseReason
	 * @param reimburseMoney
	 * @param isGetProduce
	 * @param produceNum
	 * @param reimburseTips
	 * @param certificatePic
	 * @return
	 */
	public MWebResult ChaGoodsForPart(MDataMap orderInfo,String order_code, String sku_code, String buyer_code, String reimburseReason, String reimburseMoney,
			String isGetProduce, int produceNum, String reimburseTips, List<String> certificatePic,boolean flag) {
		
		MWebResult result = new MWebResult();
		
		if(DbUp.upTable("oc_order_after_sale").count("order_code",order_code,"asale_type","4497477800030001","flow_end","0")>0){
			result.inErrorMessage(916421253,order_code);
			return result;
		}
		
		String exchangeNo = WebHelper.upCode("CGS");
		
		MDataMap detail=DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
		
		MDataMap return_reason=DbUp.upTable("oc_return_goods_reason").one("return_reson_code",reimburseReason);
		if(return_reason==null||return_reason.isEmpty()){
			result.inErrorMessage(916422117);
			return result;
		}
		
		MDataMap addr = new MDataMap();
		MDataMap product = DbUp.upTable("pc_productinfo").one("product_code",detail.get("product_code"));
		if(product.get("after_sale_address_uid") != null && !"".equals(product.get("after_sale_address_uid"))) {
			addr = DbUp.upTable("oc_address_info").one("uid",product.get("after_sale_address_uid"),"small_seller_code",orderInfo.get("small_seller_code"));
		}else {			
			addr = DbUp.upTable("oc_address_info").oneWhere("", "zid desc ", "small_seller_code=:small_seller_code", "small_seller_code",orderInfo.get("small_seller_code"));
		}
		//多货主售后地址特殊处理
		if("4497471600430002".equals(orderInfo.get("delivery_store_type"))) {
			addr = new DuohzAfterSaleSupport().getDuohzAfterSaleAddr(orderInfo.get("order_code"));
		}
		if(addr==null||addr.isEmpty()){
			result.inErrorMessage(916422116);
			return result;
		}
		
		MDataMap addChangeGoodsDetailMap=new MDataMap();
		addChangeGoodsDetailMap.put("exchange_no", exchangeNo);
		addChangeGoodsDetailMap.put("sku_code", sku_code);
		addChangeGoodsDetailMap.put("sku_name", detail.get("sku_name"));
		addChangeGoodsDetailMap.put("count", String.valueOf(produceNum));
		addChangeGoodsDetailMap.put("current_price", detail.get("sku_price"));
		addChangeGoodsDetailMap.put("product_picurl", detail.get("product_picurl"));
		DbUp.upTable("oc_exchange_goods_detail").dataInsert(addChangeGoodsDetailMap);
		
		
		
		MDataMap orderadress=DbUp.upTable("oc_orderadress").one("order_code",order_code);
		String now =DateUtil.getSysDateTimeString();
		
		//换货数据的插入处理
		MDataMap addChangeGoodsMap=new MDataMap();
		addChangeGoodsMap.put("exchange_no", exchangeNo);
		addChangeGoodsMap.put("order_code", order_code);
		addChangeGoodsMap.put("buyer_code", orderInfo.get("buyer_code"));
		addChangeGoodsMap.put("seller_code", orderInfo.get("seller_code"));
		addChangeGoodsMap.put("exchange_reason", return_reason.get("return_reson"));
		addChangeGoodsMap.put("status", "4497153900020002");
		addChangeGoodsMap.put("transport_money", "0");//运费为0
		addChangeGoodsMap.put("contacts", orderadress.get("receive_person"));
		addChangeGoodsMap.put("mobile", orderadress.get("mobilephone"));
		addChangeGoodsMap.put("buyer_mobile", (String)DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code", new MDataMap("member_code",orderInfo.get("buyer_code"))));
		addChangeGoodsMap.put("address", orderadress.get("address"));
		addChangeGoodsMap.put("pic_url", "");
		addChangeGoodsMap.put("description", reimburseTips);
		addChangeGoodsMap.put("small_seller_code", orderInfo.get("small_seller_code"));
		addChangeGoodsMap.put("transport_people", "");//暂无运费承担方
		addChangeGoodsMap.put("create_time", now);
		
		addChangeGoodsMap.put("after_sale_person", addr.get("after_sale_person"));
		addChangeGoodsMap.put("after_sale_mobile", addr.get("after_sale_mobile"));
		addChangeGoodsMap.put("after_sale_address", addr.get("after_sale_address"));
		addChangeGoodsMap.put("after_sale_postcode", addr.get("after_sale_postcode"));
		addChangeGoodsMap.put("goods_receipt", "isGetProduce");
		addChangeGoodsMap.put("freight", "4497476900040002");
		addChangeGoodsMap.put("flag_return_goods", "4497477800090001");
		addChangeGoodsMap.put("delivery_store_type", DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code))+"");
		
		DbUp.upTable("oc_exchange_goods").dataInsert(addChangeGoodsMap);
		
		MDataMap achangeInfo=new MDataMap();
		achangeInfo.put("order_code", order_code);
		achangeInfo.put("sku_code", sku_code);
		achangeInfo.put("oac_num", String.valueOf(produceNum));
		achangeInfo.put("oac_type", "4497477800030003");//换货
		achangeInfo.put("oac_status", "4497477800040001");
		achangeInfo.put("create_time", now);
		achangeInfo.put("update_time", now);
		achangeInfo.put("available", "0");
		achangeInfo.put("asale_source", "4497477800060001");
		achangeInfo.put("asale_code", exchangeNo);
		DbUp.upTable("oc_order_achange").dataInsert(achangeInfo);
		
		MDataMap addLogMap = new MDataMap();
		addLogMap.put("exchange_no", exchangeNo);
		addLogMap.put("info", "[用户提交售后]"+addChangeGoodsMap.get("exchange_reason"));
		addLogMap.put("create_time", now);
		addLogMap.put("create_user", buyer_code);
		addLogMap.put("now_status", addChangeGoodsMap.get("status"));
		// 插入新的换货状态日志信息
		DbUp.upTable("lc_exchangegoods").dataInsert(addLogMap);
		
		//****************************过渡逻辑********************************
		MDataMap oasMap=new MDataMap();
		oasMap.put("asale_code", exchangeNo);
		oasMap.put("order_code", order_code);
		oasMap.put("buyer_code", buyer_code);
		oasMap.put("buyer_mobile", addChangeGoodsMap.get("mobile"));
		oasMap.put("seller_code", addChangeGoodsMap.get("seller_code"));
		oasMap.put("small_seller_code", addChangeGoodsMap.get("small_seller_code"));
		oasMap.put("asale_reason", reimburseReason);
		oasMap.put("asale_status", "4497477800050003");//等待审核
		oasMap.put("asale_remark", reimburseTips);
		oasMap.put("asale_type", "4497477800030003");//换货
		oasMap.put("asale_source", "4497477800060001");
		oasMap.put("create_time", now);
		oasMap.put("update_time", now);
		oasMap.put("flow_end", "0");
		DbUp.upTable("oc_order_after_sale").dataInsert(oasMap);
		
		MDataMap oasdMap=new MDataMap();
		oasdMap.put("asale_code", exchangeNo);
		oasdMap.put("sku_code", sku_code);
		oasdMap.put("sku_num", String.valueOf(produceNum));
		oasdMap.put("sku_name", detail.get("sku_name"));
		oasdMap.put("sku_price", detail.get("sku_price"));
		oasdMap.put("product_code", detail.get("product_code"));
		oasdMap.put("product_picurl", detail.get("product_picurl"));
		DbUp.upTable("oc_order_after_sale_dtail").dataInsert(oasdMap);
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", exchangeNo);
		loasMap.put("create_user", buyer_code);
		loasMap.put("create_time", now);
		loasMap.put("asale_status", oasMap.get("asale_status"));
		loasMap.put("remark", addLogMap.get("info"));
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", exchangeNo);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070002");
		lsasMap.put("create_time", now);
		
		
		String order_status=(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",orderInfo.get("order_status")));
		MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100013");
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"), order_status,return_reason.get("return_reson"),"4497476900040001".equals(isGetProduce)?"是":"否",reimburseTips));
		lsasMap.put("serial_title", templateMap.get("template_title"));
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		//添加备注图片
		if(certificatePic!=null){
			
			MDataMap orderRemark=new MDataMap();
			orderRemark.put("order_code",order_code);
			orderRemark.put("remark","[用户提交售后]"+return_reason.get("return_reson"));
			orderRemark.put("create_time",now);
			orderRemark.put("create_user_code",buyer_code);
			orderRemark.put("create_user_name","用户");
			orderRemark.put("remark_type","1");
			orderRemark.put("asale_code",exchangeNo);
			for (int i = 0; i < certificatePic.size(); i++) {
				if(i>4){
					break;
				}
				orderRemark.put("remark_picurl"+(i+1),certificatePic.get(i));
			}
			
			DbUp.upTable("oc_order_remark").dataInsert(orderRemark);
			
		}
		
		if(Constants.SMALL_SELLER_CODE_JD.equals(DbUp.upTable("oc_orderinfo").dataGet("small_seller_code", "", new MDataMap("order_code",order_code)))) {
			if(new JdAfterSaleSupport().initJdAfterSale(exchangeNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new JdAfterSaleSupport().createAfterSaleServiceTask(exchangeNo);
			}
		}
		
		if("4497471600430002".equals(DbUp.upTable("oc_orderinfo").dataGet("delivery_store_type", "", new MDataMap("order_code",order_code)))) {
			if(new DuohzAfterSaleSupport().initDuohzAfterSale(exchangeNo).getResultCode() != 1) {
				// 初始化失败时加到定时任务里面进行重试
				new DuohzAfterSaleSupport().createAfterSaleServiceTask(exchangeNo);
			}
		}
		
		//禁止退换货操作
		if(flag){
			DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code", order_code,"flag_asale","1","sku_code",sku_code,"asale_code",exchangeNo), "", "order_code,sku_code");
		}
		result.setResultMessage(exchangeNo);
		return result;
	}
}
