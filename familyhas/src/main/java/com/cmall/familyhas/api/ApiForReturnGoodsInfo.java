package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForReturnGoodsInfoInput;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.familyhas.api.result.ApiForReturnGoodsInfoResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrderDetail;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 退换货前置信息
 * <p>
 * 目前只支持第三方商户的订单
 * <p>
 * <p>
 * 支持除跨境直邮以外的所有商品
 * <p>
 * @author jlin
 * @update 2018-11-23 新增换货，过期时间点将7天拓展到十五天，如果是十五天内则只支持退货
 * 
 */
public class ApiForReturnGoodsInfo extends RootApiForToken<ApiForReturnGoodsInfoResult, ApiForReturnGoodsInfoInput> {

	public ApiForReturnGoodsInfoResult Process(ApiForReturnGoodsInfoInput inputParam, MDataMap mRequestMap) {
		ApiForReturnGoodsInfoResult result = new ApiForReturnGoodsInfoResult();
		String sku_code = inputParam.getProductCode();
		String order_code = inputParam.getOrderCode();
		if(order_code.contains("DD")||order_code.contains("HH")) {//惠家有订单，需校验skuCode不为空
			if(StringUtils.isEmpty(sku_code)) {
				result.setResultCode(-1);
				result.setResultMessage("【sku_code】不能为空");
				return result;
			}
		}
		MDataMap apiClient = getApiClient();//获取版本号信息
		//MDataMap apiClient = new MDataMap("app_vision","5.3.2");
		MDataMap mmap = DbUp.upTable("oc_orderinfo").one("order_code", order_code);
		if(mmap != null&&!mmap.isEmpty()&&!"SI2003".equals(mmap.get("small_seller_code"))){
			result = mainInfo(inputParam,mRequestMap);
		}else{
			if(apiClient==null||apiClient.isEmpty()){//老版本
				result = ldInfo(inputParam,mRequestMap);
			}else{
				String appVersion = apiClient.get("app_vision");
				if(AppVersionUtils.compareTo("5.3.2", appVersion)<=0){
					result = ldNewInfo(inputParam,mRequestMap);
				}else{
					//530版本之前走老接口
					result = ldInfo(inputParam,mRequestMap);
				}
			}
		}
		/**
		 * LD回之前页面
		 */
		String smallSellerCode = "";
		if(mmap != null && !mmap.isEmpty()) {
			smallSellerCode = mmap.get("small_seller_code");
		}else {
			smallSellerCode = "SI2003";
		}
		if("SI2003".equals(smallSellerCode)) {
			result.setTurnToAfterSale("0");
		}else {
			result.setTurnToAfterSale("1");
		}
		return result;
	}
	
	/**
	 * 5.3.2之后版本的TV品在线退换货前置信息
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiForReturnGoodsInfoResult ldNewInfo(
			ApiForReturnGoodsInfoInput inputParam, MDataMap mRequestMap) {


		ApiForReturnGoodsInfoResult result = new ApiForReturnGoodsInfoResult();
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if("N".equals(isSyncLd)){//关闭状态
			result.setResultCode(10000);
			result.setResultMessage("LD订单异常，请联系客服");
			return result;
		}
		String order_code = inputParam.getOrderCode();
		String sOrderCode = order_code;//LD订单用
		String sku_code = inputParam.getProductCode();
		String ordSeq = inputParam.getOrderSeq();
		if(order_code.contains("DD")||order_code.contains("HH")){//APP通路LD品处理逻辑
			MDataMap map = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			String success_time = map.get("update_time");// 交易成功的时间
			String out_order_code = map.get("out_order_code");
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//校验时间之前先校验是否是换货新单，如果是换货新单则判断时间不能超过七天。
			String orgOrdId = map.get("org_ord_id");
			if(!StringUtils.isEmpty(orgOrdId)){//换货新单
				if (differ > 604800000l) {// 大于7天，过期了
					result.setOutDateFlag("1");
					result.setOutDateMsg("该订单已过允许退货时间（7天以内允许退货）");
					result.setResultCode(100000);
					result.setResultMessage("该订单已过允许退货时间（7天以内允许退货）");
					return result;
				}
				result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
			}else{
				if (differ > 1296000000) {// 大于15天，过期了
					result.setOutDateFlag("1");
					result.setOutDateMsg("该订单已过允许退货时间【7天以内允许退货，7-15天允许换货】");
					result.setResultCode(100000);
					result.setResultMessage("该订单已过允许退货时间【7天以内允许退货，7-15天允许换货】");
					return result;
				}else if(differ <= 1296000000l&&differ>604800000l){//介于7-15天之间只能换货
					// 多货主订单不支持换货
					if("4497471600430002".equals(map.get("delivery_store_type"))) {
						result.setOutDateFlag("1");
						result.setOutDateMsg("该订单已过允许退货时间【7天以内允许退货】");
						result.setResultCode(100000);
						result.setResultMessage("该订单已过允许退货时间【7天以内允许退货】");
						return result;
					} else {
						result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
					}
				}else if(differ <= 604800000l){//小于7天的订单既可以换货也可以退货
					// 多货主订单不支持换货
					if(!"4497471600430002".equals(map.get("delivery_store_type"))) {
						result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
						
					}
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
				}
			}
			if (!result.getReimburseType().isEmpty()) {
				
				MDataMap odetail = DbUp.upTable("oc_orderdetail").one("order_code", order_code, "sku_code", sku_code);
				if (odetail == null || odetail.isEmpty()) {
					result.setResultCode(916422147);
					result.setResultMessage(bInfo(916422147, sku_code));
				} else {
					result.setSkuPrice(odetail.get("sku_price"));
					result.setWgsPrise(odetail.get("group_price"));
					int count=0;
					String sql = "select SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+out_order_code+"' AND after_sale_status = '01' AND if_post = 2";
					Map<String,Object> mapCount = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
					if(mapCount != null){
						String countLocal = mapCount.get("good_cnt")!=null?mapCount.get("good_cnt").toString():"0";
						count += Integer.parseInt(countLocal);
					}
					//接口获取允许售后数量
					Integer total = new AfterSaleService().getAllowCount(order_code,sku_code);
					result.setMaxReturnNum(total - count);
					List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryAll("", "", "after_sales_type in ('449747660003','449747660004') and status=:status and app_code='SI2003'", new MDataMap("status","449747660005"));
					for (MDataMap mDataMap : reasonList) {
						String after_sales_type=mDataMap.get("after_sales_type");
						if("449747660003".equals(after_sales_type)){
							result.getReimburseReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}else{
							result.getChangeGoodsReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}
					}
				}
			}
//			Double money = Double.valueOf(result.getWgsPrise()) * Double.valueOf(result.getMaxReturnNum()) ;  
			// 含运费{0}元  4.1.0需求 - Yangcl
			result.setWgsReturnAlert(bInfo(916423216 , result.getFreightPrise()));
			MDataMap notice= DbUp.upTable("fh_notice").one("notice_show_place","4497477800010001","notice_status", "4497477800020001");
			result.setNotice_content(notice.get("notice_content"));
			//添加储值金判断 20180615 -rhb
			String sql = "select count(1) con from oc_order_pay where order_code=:order_code and pay_type in ('449746280006','449746280008')";
			Map<String, Object> countMap = DbUp.upTable("oc_order_pay").dataSqlOne(sql, new MDataMap("order_code", order_code));
			if(null != countMap && Integer.parseInt(countMap.get("con")+"") > 0) {
				result.setIntegralReturnAlert(this.bConfig("xmassystem.integralReturnAlert"));
			}
			return result;
			
		}else{//LD 订单处理逻辑
			String isChangeGoods = inputParam.getIsChangeGoods();
			Integer ord_seq = 0;
			if(!StringUtils.isEmpty(ordSeq)){
				ord_seq = Integer.parseInt(ordSeq) - 1;
			}
			RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(sOrderCode);
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
			rsyncGetThirdOrderDetail.doRsync();
			RsyncModelThirdOrderDetail orderdetail = null;
			if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
				orderdetail = rsyncGetThirdOrderDetail.getResponseObject().getResult().get(ord_seq);
				if (orderdetail == null) {
					result.setResultCode(916422141);
					result.setResultMessage(bInfo(916422141, order_code));
					return result;
				}
				//只要有一个是海外购，那整个订单就都是海外购，海外购目前不允许在线申请售后
				if("Y".equals(orderdetail.getIs_hwg())) {
					result.setResultCode(916422146);
					result.setResultMessage(bInfo(916422146, order_code));
					return result;
				}
			}
			if (orderdetail == null) {
				result.setResultCode(916422147);
				result.setResultMessage(bInfo(916422147, sku_code));
				return result;
			}
			String order_status = orderdetail.getOrd_stat();
			Long success_time = orderdetail.getRcv_date();
			if(success_time == null){//如果签收时间为空
				success_time = DateUtil.getSysDateLong();
			}
			
			if(order_status.length() == 2) {//LD订单
				order_status = this.convertOrderStatus(order_status);
			}
			if (!"4497153900010003".equals(order_status)
					&& !"4497153900010005".equals(order_status)) {
				result.setResultCode(916422162);
				result.setResultMessage(bInfo(916422162));
				return result;
			}
			
			/**
			 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
			 */
			// 15天可以换货 ，7天可以退货 //目前没有 仅退款 逻辑//目前LD没有换货逻辑
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 4497477800030001-退货退款 4497477800030002-仅退款 4497477800030003-换货
			if("1".equals(isChangeGoods)){//换货新单
				if (differ < 604800000l) {// 小于7天，可以退货
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
				} else {
					result.setOutDateFlag("1");
					result.setOutDateMsg("已过允许退货时间");
					
					result.setResultCode(916422148);
					result.setResultMessage("已过允许退货时间");
					return result;
				}
			}else{
				if (differ < 604800000l) {// 小于7天，可以退货,也可以换货
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
				} else if(differ>=604800000l&&differ<1296000000l ){//大于七天，小于十五天的时间
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
				}else{
					result.setOutDateFlag("1");
					result.setOutDateMsg("已过允许退货时间");
					
					result.setResultCode(916422148);
					result.setResultMessage("已过允许退货时间");
					return result;
				}
			}
			
			if (!result.getReimburseType().isEmpty()) {
				result.setSkuPrice(orderdetail.getPrc().toString());
				//TODO
				result.setWgsPrise("0.00");
				String sql = "SELECT SUM(good_cnt) goodCnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+sOrderCode+"' and order_seq = "+ordSeq+" and  after_sale_status = '01' and  if_post = '2'";
				Map<String, Object> map1=DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
				Integer applyForLocal = 0;
				if(map1 == null){
					applyForLocal = 0;
				}else{
					applyForLocal = Integer.parseInt(map1.get("goodCnt")!=null?map1.get("goodCnt").toString():"0");
				}
				Integer total = new AfterSaleService().getAllowCount(sOrderCode,Integer.parseInt(ordSeq));
				result.setMaxReturnNum((total-applyForLocal)>0?(total-applyForLocal):0);//目前确定LD订单明细只有一条
				
				List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryAll("", "", "after_sales_type in ('449747660003','449747660004') and status=:status and app_code='SI2003'", new MDataMap("status","449747660005"));
				for (MDataMap mDataMap : reasonList) {
					String after_sales_type=mDataMap.get("after_sales_type");
					if("449747660003".equals(after_sales_type)){
						result.getReimburseReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
					}else{
						result.getChangeGoodsReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
					}
				}
			}
			
//		Double money = Double.valueOf(result.getWgsPrise()) * Double.valueOf(result.getMaxReturnNum()) ;  
			// 含运费{0}元  4.1.0需求 - Yangcl
			result.setWgsReturnAlert(bInfo(916423216 , result.getFreightPrise()));
			
			MDataMap notice= DbUp.upTable("fh_notice").one("notice_show_place","4497477800010001","notice_status", "4497477800020001");
			result.setNotice_content(notice.get("notice_content"));
			return result;
		}
	
	}

	/**
	 * LD退货信息5.3.2之前仅允许退货版本。
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	public ApiForReturnGoodsInfoResult ldInfo(ApiForReturnGoodsInfoInput inputParam, MDataMap mRequestMap){


		ApiForReturnGoodsInfoResult result = new ApiForReturnGoodsInfoResult();
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if("N".equals(isSyncLd)){//关闭状态
			result.setResultCode(10000);
			result.setResultMessage("LD订单异常，请联系客服");
			return result;
		}
		String order_code = inputParam.getOrderCode();
		String sOrderCode = order_code;//LD订单用
		String sku_code = inputParam.getProductCode();
		String ordSeq = inputParam.getOrderSeq();
		if(order_code.contains("DD")||order_code.contains("HH")){//APP通路LD品处理逻辑
			MDataMap map = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			String success_time = map.get("update_time");// 交易成功的时间
			String out_order_code = map.get("out_order_code");
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//校验时间之前先校验是否是换货新单，如果是换货新单则判断时间不能超过七天。
			if (differ > 604800000l) {// 大于7天，过期了
				result.setOutDateFlag("1");
				result.setOutDateMsg("该订单已过允许退货时间（7天以内允许退货）");
				result.setResultCode(100000);
				result.setResultMessage("该订单已过允许退货时间（7天以内允许退货）");
				return result;
			}
			result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
			if (!result.getReimburseType().isEmpty()) {
				
				MDataMap odetail = DbUp.upTable("oc_orderdetail").one("order_code", order_code, "sku_code", sku_code);
				if (odetail == null || odetail.isEmpty()) {
					result.setResultCode(916422147);
					result.setResultMessage(bInfo(916422147, sku_code));
				} else {
					result.setSkuPrice(odetail.get("sku_price"));
					result.setWgsPrise(odetail.get("group_price"));
					int count=0;
					String sql = "select SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+out_order_code+"' AND after_sale_status = '01' AND if_post = 2";
					Map<String,Object> mapCount = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
					if(mapCount != null){
						String countLocal = mapCount.get("good_cnt")!=null?mapCount.get("good_cnt").toString():"0";
						count += Integer.parseInt(countLocal);
					}
					//接口获取允许售后数量
					Integer total = new AfterSaleService().getAllowCount(order_code,sku_code);
					result.setMaxReturnNum(total - count);
					List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryAll("", "", "after_sales_type in ('449747660003','449747660004') and status=:status and app_code='SI2003'", new MDataMap("status","449747660005"));
					for (MDataMap mDataMap : reasonList) {
						String after_sales_type=mDataMap.get("after_sales_type");
						if("449747660003".equals(after_sales_type)){
							result.getReimburseReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}else{
							result.getChangeGoodsReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}
					}
				}
			}
//			Double money = Double.valueOf(result.getWgsPrise()) * Double.valueOf(result.getMaxReturnNum()) ;  
			// 含运费{0}元  4.1.0需求 - Yangcl
			result.setWgsReturnAlert(bInfo(916423216 , result.getFreightPrise()));
			MDataMap notice= DbUp.upTable("fh_notice").one("notice_show_place","4497477800010001","notice_status", "4497477800020001");
			result.setNotice_content(notice.get("notice_content"));
			//添加储值金判断 20180615 -rhb
			String sql = "select count(1) con from oc_order_pay where order_code=:order_code and pay_type in ('449746280006','449746280008')";
			Map<String, Object> countMap = DbUp.upTable("oc_order_pay").dataSqlOne(sql, new MDataMap("order_code", order_code));
			if(null != countMap && Integer.parseInt(countMap.get("con")+"") > 0) {
				result.setIntegralReturnAlert(this.bConfig("xmassystem.integralReturnAlert"));
			}
			return result;
			
		}else{//LD 订单处理逻辑
			Integer ord_seq = 0;
			if(!StringUtils.isEmpty(ordSeq)){
				ord_seq = Integer.parseInt(ordSeq) - 1;
			}
			RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(sOrderCode);
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
			rsyncGetThirdOrderDetail.doRsync();
			RsyncModelThirdOrderDetail orderdetail = null;
			if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
				orderdetail = rsyncGetThirdOrderDetail.getResponseObject().getResult().get(ord_seq);
				if (orderdetail == null) {
					result.setResultCode(916422141);
					result.setResultMessage(bInfo(916422141, order_code));
					return result;
				}
				//只要有一个是海外购，那整个订单就都是海外购，海外购目前不允许在线申请售后
				if("Y".equals(orderdetail.getIs_hwg())) {
					result.setResultCode(916422146);
					result.setResultMessage(bInfo(916422146, order_code));
					return result;
				}
			}
			if (orderdetail == null) {
				result.setResultCode(916422147);
				result.setResultMessage(bInfo(916422147, sku_code));
				return result;
			}
			String order_status = orderdetail.getOrd_stat();
			Long success_time = orderdetail.getRcv_date();
			if(success_time == null){//如果签收时间为空
				result.setResultCode(100000);
				result.setResultMessage("该订单商品尚未签收，如有疑问请联系客服");
				return result;
			}
			
			if(order_status.length() == 2) {//LD订单
				order_status = this.convertOrderStatus(order_status);
			}
			if (!"4497153900010005".equals(order_status)) {
				result.setResultCode(916422162);
				result.setResultMessage(bInfo(916422162));
				return result;
			}
			/**
			 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
			 */
			// 15天可以换货 ，7天可以退货 //目前没有 仅退款 逻辑//目前LD没有换货逻辑
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 4497477800030001-退货退款 4497477800030002-仅退款 4497477800030003-换货
			if (differ < 604800000l) {// 小于7天，可以退货
				result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
			} else {
				result.setOutDateFlag("1");
				result.setOutDateMsg("已过允许退货时间");
				
				result.setResultCode(916422148);
				result.setResultMessage("已过允许退货时间");
				return result;
			}
			
			if (!result.getReimburseType().isEmpty()) {
				result.setSkuPrice(orderdetail.getPrc().toString());
				result.setWgsPrise("0.00");
				String sql = "SELECT SUM(good_cnt) goodCnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+sOrderCode+"' and order_seq = "+ordSeq+" and  after_sale_status = '01' and  if_post = '2'";
				Map<String, Object> map1=DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
				Integer applyForLocal = 0;
				if(map1 == null){
					applyForLocal = 0;
				}else{
					applyForLocal = Integer.parseInt(map1.get("goodCnt")!=null?map1.get("goodCnt").toString():"0");
				}
				Integer total = new AfterSaleService().getAllowCount(sOrderCode,Integer.parseInt(ordSeq));
				result.setMaxReturnNum((total-applyForLocal)>0?(total-applyForLocal):0);//目前确定LD订单明细只有一条
				
				List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryAll("", "", "after_sales_type in ('449747660003','449747660004') and status=:status and app_code='SI2003'", new MDataMap("status","449747660005"));
				for (MDataMap mDataMap : reasonList) {
					String after_sales_type=mDataMap.get("after_sales_type");
					if("449747660003".equals(after_sales_type)){
						result.getReimburseReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
					}else{
						result.getChangeGoodsReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
					}
				}
			}
			
//		Double money = Double.valueOf(result.getWgsPrise()) * Double.valueOf(result.getMaxReturnNum()) ;  
			// 含运费{0}元  4.1.0需求 - Yangcl
			result.setWgsReturnAlert(bInfo(916423216 , result.getFreightPrise()));
			
			MDataMap notice= DbUp.upTable("fh_notice").one("notice_show_place","4497477800010001","notice_status", "4497477800020001");
			result.setNotice_content(notice.get("notice_content"));
			return result;
		}
	
	}

	/**
	 * 第三方商户申请退换货前置信息
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	public ApiForReturnGoodsInfoResult mainInfo(ApiForReturnGoodsInfoInput inputParam, MDataMap mRequestMap){


		ApiForReturnGoodsInfoResult result = new ApiForReturnGoodsInfoResult();

		String order_code = inputParam.getOrderCode();
		String sku_code = inputParam.getProductCode();
		String buyer_code = getUserCode();
		
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

		// 判断是否为第三方商户的订单
//		if (StringUtils.startsWith(small_seller_code, "SF031")) {
		/**
		 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
		 */
		if(StringUtils.isNotBlank(WebHelper.getSellerType(small_seller_code))|| "SI2003".equals(small_seller_code)){
			// 15天可以换货 ，7天可以退货 //目前没有 仅退款 逻辑
			long differ = 0l;
			try {
				differ = differTimeNow(success_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// 4497477800030001-退货退款 4497477800030002-仅退款 4497477800030003-换货
			if (differ < 604800000l) {// 小于7天，可以退货
				result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030001", "退货退款"));
				// 屏蔽京东订单换货
				// 多货主订单不支持换货
				if(!"SI2003".equals(small_seller_code) && !Constants.SMALL_SELLER_CODE_JD.equals(small_seller_code)
						&& !"4497471600430002".equals(orderInfo.get("delivery_store_type"))){//如果是LD品，现在只支持退货退款
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
				}
			} else if (differ < 1296000000) {// 小于15天，可以换货
				// 屏蔽京东订单换货
				// 多货主订单不支持换货
				if(!"SI2003".equals(small_seller_code) && !Constants.SMALL_SELLER_CODE_JD.equals(small_seller_code)
						&& !"4497471600430002".equals(orderInfo.get("delivery_store_type"))){//如果是LD品，现在只支持退货退款
					result.getReimburseType().add(new ApiForReturnGoodsInfoResult.Reimburse("4497477800030003", "换货"));
				}else{
					result.setOutDateFlag("1");
//					result.setOutDateMsg(bConfig("familyhas.returnGoodsInfo_outDateMsg"));
					result.setOutDateMsg("已过允许退货时间");
					
					result.setResultCode(916422148);
					result.setResultMessage("已过允许退货时间");
					return result;
				}
			} else {
				// 过期
				result.setOutDateFlag("1");
//				result.setOutDateMsg(bConfig("familyhas.returnGoodsInfo_outDateMsg"));
				result.setOutDateMsg(bInfo(916422148));
				
				result.setResultCode(916422148);
				result.setResultMessage(bInfo(916422148));
				return result;
			}
			
			if (!result.getReimburseType().isEmpty()) {
				
				MDataMap odetail = DbUp.upTable("oc_orderdetail").one("order_code", order_code, "sku_code", sku_code);
				if (odetail == null || odetail.isEmpty()) {
					result.setResultCode(916422147);
					result.setResultMessage(bInfo(916422147, sku_code));
				} else {

					result.setSkuPrice(odetail.get("sku_price"));
					result.setWgsPrise(odetail.get("group_price"));
					
					

					int count=0;
					Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available", new MDataMap("order_code",order_code,"sku_code",sku_code,"available","0"));
					if(map1!=null&&!map1.isEmpty()){
						Object obj=map1.get("count");
						if(obj!=null){
							count=((BigDecimal) obj).intValue();
						}
					}
					
					result.setMaxReturnNum(Integer.valueOf(odetail.get("sku_num"))-count);

					List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryAll("", "", "after_sales_type in ('449747660003','449747660004') and status=:status and app_code='SI2003'", new MDataMap("status","449747660005"));
					for (MDataMap mDataMap : reasonList) {
						String after_sales_type=mDataMap.get("after_sales_type");
						if("449747660003".equals(after_sales_type)){
							result.getReimburseReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}else{
							result.getChangeGoodsReason().add(new Reason(mDataMap.get("return_reson_code"), mDataMap.get("return_reson")));
						}
					}
				}
			}
		} else {
			result.setResultCode(916422146);
			result.setResultMessage(bInfo(916422146, order_code));
		}

//		Double money = Double.valueOf(result.getWgsPrise()) * Double.valueOf(result.getMaxReturnNum()) ;  
		// 含运费{0}元  4.1.0需求 - Yangcl
		result.setWgsReturnAlert(bInfo(916423216 , result.getFreightPrise()));
		
		MDataMap notice= DbUp.upTable("fh_notice").one("notice_show_place","4497477800010001","notice_status", "4497477800020001");
		result.setNotice_content(notice.get("notice_content"));
		//添加储值金判断 20180615 -rhb
		String sql = "select count(1) con from oc_order_pay where order_code=:order_code and pay_type in ('449746280006','449746280008')";
		Map<String, Object> countMap = DbUp.upTable("oc_order_pay").dataSqlOne(sql, new MDataMap("order_code", order_code));
		if(null != countMap && Integer.parseInt(countMap.get("con")+"") > 0) {
			result.setIntegralReturnAlert(this.bConfig("xmassystem.integralReturnAlert"));
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
	
	/***
	 * 时间差
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public long differTimeNow(Long time) throws ParseException {
		return new Date().getTime() - time;
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

}
