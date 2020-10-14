package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForCancelReturnInput;
import com.cmall.familyhas.api.result.ApiForCancelReturnResult;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 取消申请退货\换货接口
 * @author cc
 *
 */
public class ApiForCancelReturn extends RootApiForToken<ApiForCancelReturnResult, ApiForCancelReturnInput>{

	@Transactional
	@Override
	public ApiForCancelReturnResult Process(ApiForCancelReturnInput inputParam,
			MDataMap mRequestMap) {
		ApiForCancelReturnResult result = new ApiForCancelReturnResult();
		String afterSaleCode = inputParam.getAfterSaleCode();
		if(afterSaleCode.contains("LD")||afterSaleCode.contains("A")||afterSaleCode.contains("P")){//LD 退货单\换货单
			result = this.cancelLd(inputParam,mRequestMap);
		}else{
			result = this.cancelApp(inputParam,mRequestMap);
			//取消成功是，需要校验是否是分销商品售后单，如果时，写入定时
			boolean flag  = this.checkIfAgent(afterSaleCode);
			if(result.getResultCode() == 1 && flag) {
				JobExecHelper.createExecInfo("449746990026", afterSaleCode, DateUtil.addMinute(5));
			}
		}
		return result;
		
	}

	/**
	 * 根据售后单号校验是否是分销单
	 * @param afterSaleCode
	 * @return
	 */
	public boolean checkIfAgent(String afterSaleCode) {
		MDataMap returnInfo = DbUp.upTable("oc_return_goods").one("return_code",afterSaleCode);
		String orderCode = "";
		if(returnInfo != null && !returnInfo.isEmpty()) {
			orderCode = returnInfo.get("order_code");
		}
		if(StringUtils.isEmpty(orderCode)) {
			return false;
		}
		if(DbUp.upTable("fh_agent_order_detail").count("order_code",orderCode) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * APP订单取消并记录日志
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 * @author AngelJoy
	 */
	private ApiForCancelReturnResult cancelApp(
			ApiForCancelReturnInput inputParam, MDataMap mRequestMap) {
		ApiForCancelReturnResult result = new ApiForCancelReturnResult();
		String afterSaleCode = inputParam.getAfterSaleCode();
		MDataMap afterSale = DbUp.upTable("oc_order_after_sale").one("asale_code",afterSaleCode);
		if(afterSale == null || afterSale.isEmpty()){
			result.setResultCode(10000);
			result.setResultMessage("售后单不存在");
			return result;
		}
		String asale_type = afterSale.get("asale_type");
		if(!"4497477800030001".equals(asale_type) && !"4497477800030003".equals(asale_type)){//不为退货退款和换货的
			result.setResultCode(10000);
			result.setResultMessage("目前只有退货退款和换货的售后单允许取消");
			return result;
		}
		
		String asale_status = afterSale.get("asale_status");
		if(!"4497477800050003".equals(asale_status)&&!"4497477800050005".equals(asale_status)&&!"4497477800050010".equals(asale_status)&&!"4497477800050004".equals(asale_status)){
			result.setResultCode(10000);
			result.setResultMessage("该单不允许取消");
			return result;
		}
		
		// 京东商品
		if(Constants.SMALL_SELLER_CODE_JD.equals(afterSale.get("small_seller_code"))) {
			MDataMap jdAfterSale = DbUp.upTable("oc_order_jd_after_sale").one("asale_code", afterSaleCode);
			// 检查一下京东售后单是否允许取消
			if(jdAfterSale != null && StringUtils.isNotBlank(jdAfterSale.get("afs_service_id"))
					&& !(","+jdAfterSale.get("allow_operations")+",").contains(",1,")
					) {
				result.setResultCode(10000);
				result.setResultMessage("该单不允许取消");
				return result;
			}
			
			// 同步取消京东的售后单，排除状态： 审核不通过(20) 、取消 60;
			if(jdAfterSale != null 
					&& StringUtils.isNotBlank(jdAfterSale.get("afs_service_id"))
					&& !ArrayUtils.contains(new String[]{"60","20"}, jdAfterSale.get("afs_service_step"))){
				RootResult cancelResult = new JdAfterSaleSupport().execAuditCancelQuery(afterSaleCode, "");
				if(cancelResult.getResultCode() != 1) {
					result.setResultCode(10000);
					result.setResultMessage(cancelResult.getResultMessage());
					return result;
				}
			}
		}
		RootResult cancelRootResult = new RootResult();
		if("4497477800030001".equals(asale_type)) {//退货退款
			cancelRootResult = cancelReturnGoods(afterSaleCode);
		} else if("4497477800030003".equals(asale_type)) { //换货
			cancelRootResult = cancelChangeGoods(afterSaleCode);
		}
		
		result.setResultCode(cancelRootResult.getResultCode());
		if(cancelRootResult.getResultCode()==1) {
			result.setResultMessage("取消成功");
		}else {
			result.setResultMessage(cancelRootResult.getResultMessage());
		}
		return result;	
	}
	
	/**
	 * 取消退货退款流程
	 * @param afterSaleCode
	 * @param asale_status
	 * @param toStatus
	 */
	private RootResult cancelReturnGoods(String afterSaleCode) {
		MDataMap info = DbUp.upTable("oc_return_goods").one("return_code", afterSaleCode);
		String flowBussinessUid = info.get("uid");
		String fromStatus = info.get("status");
		String toStatus = "4497153900050007";
		String flowType = "449715390005";
		
		RootResult result = new FlowBussinessService().ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, "system", "取消申请", new MDataMap());
		return result;
	}
	
	/**
	 * 取消换货流程
	 * @param afterSaleCode
	 * @param asale_status
	 * @param toStatus
	 */
	private RootResult cancelChangeGoods(String afterSaleCode) {
		MDataMap info = DbUp.upTable("oc_exchange_goods").one("exchange_no", afterSaleCode);
		String flowBussinessUid = info.get("uid");
		String fromStatus = info.get("status");
		String toStatus = "4497153900020007";
		String flowType = "449715390002";
		
		RootResult result = new FlowBussinessService().ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, "system", "取消申请", new MDataMap());
		return result;
	}

	private ApiForCancelReturnResult cancelLd(
			ApiForCancelReturnInput inputParam, MDataMap mRequestMap) {
		ApiForCancelReturnResult result = new ApiForCancelReturnResult();
		String afterSaleCode = inputParam.getAfterSaleCode();
		MDataMap afterSaleOrder = new MDataMap();
		if(afterSaleCode.contains("LD")){
			afterSaleOrder = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_app",afterSaleCode);
		}else{
			afterSaleOrder = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_ld",afterSaleCode);
		}
		int if_exit = 1;
		if(afterSaleOrder == null || afterSaleOrder.isEmpty()){//不存在的时候有可能是LD发起的售后申请
			//获取LD售后单
			Map ldAfterSaleMap = this.getLdAfterSaleOrder(afterSaleCode);//根据售后单号获取LD售后单
			if(ldAfterSaleMap == null){
				result.setResultCode(10000); 
				result.setResultMessage("该售后单不存在，请核查后再执行操作或联系客服人员");
				return result;
			}
			MDataMap mmap = new MDataMap(ldAfterSaleMap);
			mmap.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
			mmap.put("good_cnt", "1");//默认售后数量为1
			mmap.put("is_get_product", "4497476900040001");//默认是收到货的
			DbUp.upTable("oc_after_sale_ld").dataInsert(mmap);
			if_exit = 0;
			afterSaleOrder = new MDataMap("after_sale_type", ldAfterSaleMap.get("after_sale_type").toString());
		}
		String status = "";
		if(if_exit == 1){
			String after_sale_status = afterSaleOrder.get("after_sale_status");			
			if("06".equals(after_sale_status)){
				result.setResultCode(10000); 
				result.setResultMessage("该售后单已经在取消售后中。请勿频繁操作");
				return result;
			}
			if("04".equals(after_sale_status)){
				result.setResultCode(10000); 
				result.setResultMessage("该售后单已经取消了。请勿频繁操作");
				return result;
			}
			if("05".equals(after_sale_status)){
				result.setResultCode(10000); 
				if("1".equals(afterSaleOrder.get("after_sale_type"))) {//退货
					result.setResultMessage("该售后单已经退货完成。请勿频繁操作");
				} else {//换货
					result.setResultMessage("该售后单已经换货完成。请勿频繁操作");
				}				
				return result;
			}
			String if_post = afterSaleOrder.get("if_post");
			if("01".equals(after_sale_status)&&"2".equals(if_post)){//審核中未推送的單子
				status = "04";
				afterSaleOrder.put("after_sale_status", status);//改为取消成功
				afterSaleOrder.put("if_post", "3");//改为不推送
				this.changeDetailStatus(afterSaleOrder.get("order_code"),afterSaleOrder.get("sku_code"));//是否变更可再次售后状态
			}else{
				status = "06";
				afterSaleOrder.put("after_sale_status", status);//改为取消中
				afterSaleOrder.put("if_post", "2");//改为未推送
			}
			DbUp.upTable("oc_after_sale_ld").dataUpdate(afterSaleOrder, "after_sale_status,if_post", "uid");
		}
		//记录日志
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MDataMap log = new MDataMap();
		log.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
		log.put("after_sale_code_app", afterSaleCode);
		log.put("after_sale_status", status);
		log.put("create_time", sdf.format(new Date()));
		if("1".equals(afterSaleOrder.get("after_sale_type"))) {//退货
			log.put("remark", "用户取消申请退货");
		} else {//换货
			log.put("remark", "用户取消申请换货");
		}
				
		log.put("operator", "APP申请");
		DbUp.upTable("lc_after_sale_ld_return").dataInsert(log);
		return result;
	}
	
	/**
	 * 获取LD售后信息
	 * @return
	 */
	public Map<String,Object> getLdAfterSaleOrder(String after_sale_code_ld){
		String member_code = getUserCode();
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if("N".equals(isSyncLd)){//添加开关
			return null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("saleCodeLd",after_sale_code_ld);
		String url = bConfig("groupcenter.rsync_homehas_url")+"getAfterServiceList";
		String result = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
		if(StringUtils.isEmpty(result)){
			return null;
		}
		try{
			JSONObject jo = JSONObject.parseObject(result);
			Map<String,Object> map = new HashMap<String,Object>();
			String codeStr = jo.getString("code");
			if(StringUtils.isEmpty(codeStr)){
				return null;
			}
			if(jo.getInteger("code")!= 0){
				return null;
			}
			String jsonArrayStr = jo.getString("result");
			JSONArray ja = JSONArray.parseArray(jsonArrayStr);
			Iterator it = ja.iterator();
			while(it.hasNext()){
				JSONObject oo = (JSONObject) it.next();
				String cd = "06";//oo.getString("AFTER_SALE_CD");
				map.put("order_code",oo.getInteger("ORD_ID"));//LD 订单编号
				map.put("member_code", member_code);
				map.put("product_code", oo.getInteger("GOOD_ID"));
//				map.put("sku_code", "color_id="+oo.getInteger("COLOR_ID")+"style_id="+oo.getInteger("COLOR_ID"));
				map.put("order_seq", oo.getInteger("ORD_SEQ"));
				map.put("after_sale_code_app", oo.getString("AFTER_SALE_CODE_LD"));//无APP售后单号，此处填充LD售后单号
				map.put("after_sale_code_ld", oo.getString("AFTER_SALE_CODE_LD"));
				map.put("return_code", oo.getString("RTN_ID"));//退货号
				map.put("chg_ord", oo.getString("NEW_ORD_ID"));//换货新单号
				map.put("after_sale_status", cd);
				String etr_date = oo.getString("ETR_DATE");
				String create_time = timeStamp2Date(etr_date, "yyyy-MM-dd HH:mm:ss");
				map.put("create_time", create_time);
				String mdf_date = oo.getString("MDF_DATE");
				String modif_time = timeStamp2Date(mdf_date, "yyyy-MM-dd HH:mm:ss");
				map.put("modif_time", modif_time);
				map.put("if_post", "2");
				String as_type = StringUtils.isEmpty(oo.getString("AFTER_SALE_TYPE")) ? "T" : oo.getString("AFTER_SALE_TYPE");//售后工单类型 T：退货 H:换货
				if("T".equals(as_type)) {
					map.put("after_sale_type", "1");
					map.put("reason", "".equals(oo.getString("RTN_REASON"))?"LD退货":oo.getString("RTN_REASON"));
				} else {
					map.put("after_sale_type", "2");
					map.put("reason", "".equals(oo.getString("RTN_REASON"))?"LD换货":oo.getString("RTN_REASON"));
				}								
				String prc = this.getPrcByLd(oo.getInteger("ORD_ID"), oo.getInteger("ORD_SEQ"));
				map.put("return_money", prc);
				map.put("good_cnt", 1);
				return map;//只去第一条，理论上只有一条
			}
		}catch(Exception e){
			e.getStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String getPrcByLd(Integer ordId,Integer ordSeq){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ordId", ordId);
		params.put("ordSeq", ordSeq);
		String url = bConfig("groupcenter.rsync_homehas_url")+ "getOrderDetailById";
		String orderStr = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
		if(StringUtils.isEmpty(orderStr)){
			return "";
		}
		JSONObject jo = JSONObject.parseObject(orderStr);
		Integer code = jo.getInteger("code");
		if(code != 0){
			return "";
		}
		String orderListStr = jo.getString("result");
		if(StringUtils.isEmpty(orderListStr)){
			return "";
		}
		JSONArray ja = JSONArray.parseArray(orderListStr);
		Iterator it = ja.iterator();
		while(it.hasNext()){
			JSONObject oo = (JSONObject)it.next();
			return oo.getString("PRC");
		}
		return "";
	}
	
	 /** 
     * 时间戳转换成日期格式字符串 
     * @param seconds 精确到毫秒的字符串 
     * @param formatStr 
     * @return 
    */  
   public static String timeStamp2Date(String seconds,String format) {  
       if(format == null || format.isEmpty()){
           format = "yyyy-MM-dd HH:mm:ss";
       }   
       SimpleDateFormat sdf = new SimpleDateFormat(format);  
       if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
    	   return sdf.format(new Date());  
       }  
       return sdf.format(new Date(Long.valueOf(seconds)));  
   }  

	/**
	 * 根据售后编号查询是否是APP下单，如果是，变更oc_orderdetail中状态值
	 * @param string
	 */
	private void changeDetailStatus(String order_code,String sku_code) {
		MDataMap orderinfo = DbUp.upTable("oc_orderinfo").one("out_order_code",order_code);
		if(orderinfo == null){
			return;
		}
		order_code = orderinfo.get("order_code");
		MDataMap orderDetail = DbUp.upTable("oc_orderdetail").one("order_code",order_code,"sku_code",sku_code);
		if(orderDetail != null && !orderDetail.isEmpty()){
			DbUp.upTable("oc_orderdetail").dataUpdate(new MDataMap("order_code",order_code,"sku_code",sku_code,"flag_asale","0"), "flag_asale", "order_code,sku_code");
		}
	}
	

}
