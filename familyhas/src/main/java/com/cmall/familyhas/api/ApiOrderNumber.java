package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiOrderNumberInput;
import com.cmall.familyhas.api.result.ApiOrderNumberResult;
import com.cmall.familyhas.api.result.ApiOrderStateNumberResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderNumber;
import com.cmall.groupcenter.homehas.model.RsyncModelOrderNumber;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.xmassystem.util.TimeCostUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.enumer.EKvTop;
import com.srnpr.zapdata.kvdo.KvTop;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 个人中心订单数量
 * 
 * @author wz
 * 
 */

public class ApiOrderNumber extends RootApiForToken<ApiOrderNumberResult, ApiOrderNumberInput> {

	private static String orderSuccess = "4497153900010005";

	private static String hasAccept = "4497172100030002";

	public ApiOrderNumberResult Process(ApiOrderNumberInput inputParam, MDataMap mRequestMap) {
		ApiOrderNumberResult apiOrderNumberResult = new ApiOrderNumberResult();
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		inputParam.setBuyer_code(getUserCode());
		OrderService orderService = new OrderService();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new MObjMap<String, Object>();
		List<ApiOrderStateNumberResult> list = new ArrayList<ApiOrderStateNumberResult>();
		MDataMap mDataMap = new MDataMap();
		
		Date apiStart = new Date();

		// APP版本号
		String version = getApiClient().get("app_vision");
				
		int dfkCount = 0;//LD待付款数量
		int dfhCount = 0;//LD待发货数量
		int dshCount = 0;//LD待收货数量
		int dpjCount = 0;//LD待评价数量
		int jysbCount = 0;//LD交易失败数量
		
		TimeCostUtils tc = TimeCostUtils.create();
		
		boolean isCountLd = false;
		tc.begin("s1");
		if(AfterSaleService.compareAppVersion(version, "5.2.6") >= 0 ) {
			MDataMap memberDataMap=DbUp.upTable("mc_login_info").one("member_code",getUserCode());
			RsyncGetThirdOrderNumber rsyncGetThirdOrderNumber = new RsyncGetThirdOrderNumber();
			rsyncGetThirdOrderNumber.upRsyncRequest().setTel(memberDataMap.get("login_name").toString());
			rsyncGetThirdOrderNumber.doRsync();
			if(rsyncGetThirdOrderNumber.getResponseObject() != null && rsyncGetThirdOrderNumber.getResponseObject().getResult() != null && rsyncGetThirdOrderNumber.getResponseObject().getResult().size() > 0) {
				isCountLd = true;
				for(RsyncModelOrderNumber on : rsyncGetThirdOrderNumber.getResponseObject().getResult()) {
					switch(on.getORD_STAT()) {
						case "01":dfkCount = on.getCNT();break;
						case "02":dfhCount = on.getCNT();break;
						case "03":dshCount = on.getCNT();break;
						case "05":dpjCount = on.getCNT();break;
						case "04":jysbCount = on.getCNT();break;
						default: break;
					}
				}
			}
		}
		tc.end("s1");
		
		tc.begin("s2");
		if(AfterSaleService.compareAppVersion(version, "5.5.0") >= 0 ) {
			PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
			apiOrderNumberResult.setCustLvl(this.custLvlEx(levelInfo.getLevel()));
		}		
		tc.end("s2");
		
		tc.begin("s3");
		// 统计待付款总数
		int countPayment = orderService.orderCountPayment(inputParam.getBuyer_code());

		mapList = orderService.personagetOrderNumber(inputParam.getBuyer_code());
		tc.end("s3");
		// 查找交易成功的订单\
		MDataMap paramMap = new MDataMap();
		paramMap.put("order_status", orderSuccess);
		paramMap.put("buyer_code", inputParam.getBuyer_code());
		paramMap.put("manage_code", getManageCode());
		paramMap.put("delete_flag", "0");
		paramMap.put("check_flag", hasAccept);
		
		
		tc.begin("s4");
		/*
		 * 获取需要过滤的订单类型
		 */
		String orderTypeQueryWhere = orderService.getNotInOrderType();

		List<MDataMap> orderCodesMap = DbUp.upTable("oc_orderinfo").queryAll("order_code", "",
				"buyer_code=:buyer_code and order_status=:order_status and order_source not in('449715190014') and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') ) and seller_code=:manage_code and delete_flag=:delete_flag and order_type not in ("
						+ orderTypeQueryWhere + ") ",
				paramMap);
		// 根据订单号查找评价过的商品
		String orderCodes = "";
		List<Map<String, Object>> commentList = new ArrayList<Map<String, Object>>();
		for (MDataMap m : orderCodesMap) {
			orderCodes += "'" + m.get("order_code") + "',";
		}
		if (orderCodes.length() != 0) {
			orderCodes = orderCodes.substring(0, orderCodes.length() - 1);
			commentList = DbUp.upTable("nc_order_evaluation").dataSqlList(
					"select count(1) from nc_order_evaluation where order_code in (" + orderCodes + ")"
							+ " and  manage_code=:manage_code and order_name=:buyer_code group by order_code",
					paramMap);
		}
		tc.end("s4");

		Integer commentNum = commentList.size();
		Map<String, String> resu = new HashMap<String, String>();
		resu.put("4497153900010001", "");
		resu.put("4497153900010002", "");
		resu.put("4497153900010003", "");
		resu.put("4497153900010004", "");
		resu.put("4497153900010005", "");
		resu.put("4497153900010006", "");

		if (mapList != null) {
			Iterator<Map<String, Object>> ite = mapList.iterator();
			while (ite.hasNext()) {
				ApiOrderStateNumberResult apiOrderStateNumberResult = new ApiOrderStateNumberResult();
				map = (Map<String, Object>) ite.next();
				if (map.get("order_status").equals(orderSuccess)) {
					if (commentNum > Integer.parseInt(map.get("number").toString()))
						apiOrderStateNumberResult.setNumber("0");
					else
						apiOrderStateNumberResult
								.setNumber((Integer.parseInt(map.get("number").toString()) - commentNum) + "");
				} else
					apiOrderStateNumberResult.setNumber(map.get("number").toString());
				apiOrderStateNumberResult.setOrderStatus(map.get("order_status").toString());

				// 如果状态为待付款，重新进行统计待付款信息，并且覆盖上述统计出来的待付款数量
				if ("4497153900010001".equals(map.get("order_status").toString())) {
					apiOrderStateNumberResult.setNumber(String.valueOf(countPayment));
					apiOrderStateNumberResult.setOrderStatus(map.get("order_status").toString());
				}

				if (resu.containsKey(map.get("order_status").toString())) {
					resu.remove(map.get("order_status").toString());
				}

				List<MDataMap> listOrderStateValue = orderService
						.orderStateValue(apiOrderStateNumberResult.getOrderStatus());
				if (listOrderStateValue != null && !listOrderStateValue.isEmpty()) {
					for (int i = 0; i < listOrderStateValue.size(); i++) {
						mDataMap = listOrderStateValue.get(i);
						apiOrderStateNumberResult.setStatusCode(mDataMap.get("define_name"));
					}
				}
				list.add(apiOrderStateNumberResult);
			}

		}

		Iterator<String> iterator = resu.keySet().iterator();
		while (iterator.hasNext()) {
			String sts = (String) iterator.next();
			ApiOrderStateNumberResult apiOrderStateNumberResult = new ApiOrderStateNumberResult();
			apiOrderStateNumberResult.setOrderStatus(sts);
			apiOrderStateNumberResult.setNumber("0");
			list.add(apiOrderStateNumberResult);
		}
		
		
		tc.begin("s5");
		/**
		 * 可用优惠券数量
		 */
		if (VersionHelper.checkServerVersion("3.5.41.51")) {
			CouponUtil coupon = new CouponUtil();
			// 计算可用优惠券数量
			int couponCount = coupon.availableCouponList(inputParam.getChannelId(), inputParam.getBuyer_code(),
					new BigDecimal(-1), true, getManageCode(),version);
			apiOrderNumberResult.setCouponCount(couponCount);
			// 1.生成优惠券快到期提醒
			// 2.判断是否有新券,生新券红点标识
			MDataMap couponMap = coupon.personalCouponPrompt(inputParam.getChannelId(), inputParam.getBuyer_code(),
					true, getManageCode(),version);
			couponCount = Integer.parseInt(couponMap.get("couponToDead"));
			if (couponCount > 0)
				apiOrderNumberResult.setCouponPrompt(couponCount + "张即将过期");
			apiOrderNumberResult.setNewCoupon(couponMap.get("newCoupon"));
		}
		tc.end("s5");

		// 售后的数量
		ApiOrderStateNumberResult apiOrderStateNumberResult7 = new ApiOrderStateNumberResult();
		apiOrderStateNumberResult7.setOrderStatus("4497153900010107");
		Map map1 = this.getMainAfterSaleCount(version);
		String countStr1 = String.valueOf(map1.get("COUNT(*)"));//查询本地售后数量
		//查询LD售后单数量开始
		Integer ldCount = this.getLdAfterSaleCount(getUserCode(),version);
		if(!StringUtils.isEmpty(countStr1)){
			ldCount += Integer.parseInt(countStr1);
		}
		//LD售后单数量结束
		apiOrderStateNumberResult7.setNumber(ldCount.toString());
		apiOrderStateNumberResult7.setStatusCode("售后");
		list.add(apiOrderStateNumberResult7);
		
		tc.begin("s6");
		// 待评价数量
		String sql = "SELECT COUNT(DISTINCT oi.order_code, od.sku_code) total FROM ordercenter.oc_orderinfo oi"
				   + " LEFT JOIN ordercenter.oc_orderdetail od ON oi.order_code = od.order_code"
				   + " LEFT JOIN productcenter.pc_skuinfo sku ON sku.sku_code = od.sku_code"
				   + " WHERE od.gift_flag = '1' AND oi.delete_flag = '0' AND order_source not in('449715190014') AND sku.sku_code IS NOT NULL AND oi.buyer_code = :buyer_code AND oi.order_status = '4497153900010005'"
				   + " AND oi.order_type NOT IN(" + orderService.getNotInOrderType() + ")"
				   + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) = 0";
		map = DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("buyer_code", inputParam.getBuyer_code()));
		ApiOrderStateNumberResult apiOrderStateNumberResult8 = new ApiOrderStateNumberResult();
		apiOrderStateNumberResult8.setOrderStatus("4497153900010108");
		apiOrderStateNumberResult8.setNumber(map.get("total")+"");
		apiOrderStateNumberResult8.setStatusCode("待评价");
		list.add(apiOrderStateNumberResult8);
		
		tc.end("s6");

		if(isCountLd) {
			for(ApiOrderStateNumberResult item : list) {
				if(item.getOrderStatus().equals("4497153900010001")) {
					if(dfkCount > 0) {
						dfkCount = Integer.parseInt(item.getNumber()) + dfkCount;
						item.setNumber(String.valueOf(dfkCount));
					}
				} else if(item.getOrderStatus().equals("4497153900010002")) {
					if(dfhCount > 0) {
						dfhCount = Integer.parseInt(item.getNumber()) + dfhCount;
						item.setNumber(String.valueOf(dfhCount));
					}
				} else if(item.getOrderStatus().equals("4497153900010003")) {
					if(dshCount > 0) {
						dshCount = Integer.parseInt(item.getNumber()) + dshCount;
						item.setNumber(String.valueOf(dshCount));
					}
				} else if(item.getOrderStatus().equals("4497153900010005")) {
					if(dpjCount > 0) {
						dpjCount = Integer.parseInt(item.getNumber()) + dpjCount;
						item.setNumber(String.valueOf(dpjCount));
					}
				} else if(item.getOrderStatus().equals("4497153900010006")) {
					if(jysbCount > 0) {
						jysbCount = Integer.parseInt(item.getNumber()) + jysbCount;
						item.setNumber(String.valueOf(jysbCount));
					}
				}
			}
		}
		
		apiOrderNumberResult.setList(list);
		/*GroupAccountService accountService = new GroupAccountService();
		// GroupAccountInfoResult groupResult =
		// accountService.getAccountInfo(accountService.getAccountCodeByMemberCode(getUserCode()));
		GroupAccountInfoResult groupResult = accountService.getAccountInfoByApi(getUserCode());
		if (StringUtils.isBlank(groupResult.getWithdrawMoney())) {
			groupResult.setWithdrawMoney("0");
		}

		*//**
		 * 微公社是否显示
		 *//*
		String microCommuneOpenFlag = XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", "withdraw");
		apiOrderNumberResult.setMicroCommuneOpenFlag(StringUtils.isEmpty(microCommuneOpenFlag) ? "0" : microCommuneOpenFlag);
		
		apiOrderNumberResult.setMicroCommuneBalance("返利余额：" + groupResult.getWithdrawMoney());*/
		
		apiOrderNumberResult.setMicroCommuneOpenFlag( "0" );
		apiOrderNumberResult.setMicroCommuneBalance("返利余额：0");
		
		apiOrderNumberResult.setOtherOrderNumber(String.valueOf(commentNum));
		
		tc.begin("s7");
		/**
		 * 获取我的惠豆<br>
		 * 2016-12-07 zhy <br>
		 */
		Integer beans = new HjybeanService().uphjyBeanByMemberCode(getUserCode());
		apiOrderNumberResult.setMyBeans(String.valueOf(beans));
		
		tc.end("s7");
		/**
		 * 是否显示我的惠豆
		 */
		String beanOpenFlag = XmasKv.upFactory(EKvSchema.HomehasBeanConfig).get("switch");
		apiOrderNumberResult.setBeanUsableFlag(StringUtils.isBlank(beanOpenFlag) ? "0" : beanOpenFlag);
		
		tc.begin("s8");
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
		tc.end("s8");
		
		tc.begin("s9");
		/**
		 * 查询积分数量、储值金、暂存款
		 */
		String custId = levelInfo.getCustId();
		if (StringUtils.isNotEmpty(custId)) {
			GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(custId);
			if (amtRef != null) {
				BigDecimal accm = plusServiceAccm.moneyToAccmAmt(amtRef.getPossAccmAmt(),1);
				BigDecimal expireAccm = plusServiceAccm.moneyToAccmAmt(amtRef.getExpireAccm(),1);
				if (accm.intValue() < 1000000) {
					DecimalFormat df = new DecimalFormat("#.#");
					apiOrderNumberResult.setIntegralTotal(df.format(plusServiceAccm.moneyToAccmAmt(amtRef.getPossAccmAmt(),1).doubleValue()));
				}else {
					apiOrderNumberResult.setIntegralTotal("1000000+");
				}
				if(expireAccm.intValue() >= 200){
					DecimalFormat df = new DecimalFormat("#.#");
					apiOrderNumberResult.setExpireIntegral(df.format(expireAccm));
				}
				//添加储值金、暂存款 2018-05-18 rhb
				BigDecimal ppc = amtRef.getPossPpcAmt();
				if (ppc.compareTo(BigDecimal.valueOf(100000)) <= 0) {
					DecimalFormat df = new DecimalFormat("#.##");
					apiOrderNumberResult.setCzjTotal(df.format(ppc.doubleValue()));
				}else {
					apiOrderNumberResult.setCzjTotal("100000+");
				}
				BigDecimal crdt = amtRef.getPossCrdtAmt();
				if (crdt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
					DecimalFormat df = new DecimalFormat("#.##");
					apiOrderNumberResult.setZckTotal(df.format(crdt.doubleValue()));
				}else {
					apiOrderNumberResult.setZckTotal("100000+");
				}
				//添加惠币
				BigDecimal possHcoinAmt = amtRef.getPossHcoinAmt();
				if (possHcoinAmt.compareTo(BigDecimal.valueOf(100000)) <= 0) {
					DecimalFormat df = new DecimalFormat("#.###");
					apiOrderNumberResult.setHbTotal(df.format(possHcoinAmt.doubleValue()));
				}else {
					apiOrderNumberResult.setHbTotal("100000+");
				}
			}
			
			if(StringUtils.isNotBlank(levelInfo.getPlusEndDate())) {
				apiOrderNumberResult.getPlusInfo().setStat(1);
				apiOrderNumberResult.getPlusInfo().setExpireDate(levelInfo.getPlusEndDate());
				apiOrderNumberResult.getPlusInfo().setName(bConfig("xmassystem.plus_name"));
			}
			
		}
		
		tc.end("s9");
		
		apiOrderNumberResult.getPlusInfo().setProductCode(bConfig("xmassystem.plus_product_code"));
		apiOrderNumberResult.getPlusInfo().setSkuCode(bConfig("xmassystem.plus_sku_code"));
		apiOrderNumberResult.setPlusShowFlag(NumberUtils.toInt(bConfig("xmassystem.plus_flag")));
		
		tc.begin("s10");
		// 是否外呼分销人员
		boolean waihuFxFlag = DbUp.upTable("fh_waihu_member_info").count("member_code",getOauthInfo().getUserCode()) > 0;
		// 普通分销入口需要排除外呼人员
		if(!waihuFxFlag) {
			/**
			 * 分销收益查询
			 */
			//Map<String, Object> dataSqlOne = DbUp.upTable("fh_agent_member_info").dataSqlOne("select (predict_money+real_money) sumProfit from fh_agent_member_info where member_code=:member_code ", new MDataMap("member_code",getUserCode()));
			Map<String, Object> sqlOne = DbUp.upTable("fh_agent_member_info").dataSqlOne("select level_code from fh_agent_member_info where member_code=:member_code ",  new MDataMap("member_code",getUserCode()));
		    if(sqlOne!=null) {
		    	apiOrderNumberResult.setLevelCode(sqlOne.get("level_code").toString());
		    }
			Map<String, Object> dataSqlOne = DbUp.upTable("fh_agent_profit_detail").dataSqlOne("select IFNULL(sum(profit),0) sumProfit from fh_agent_profit_detail where member_code=:member_code  and (profit_type='4497484600030001' or profit_type='4497484600030002')", new MDataMap("member_code",getOauthInfo().getUserCode()));
			if(dataSqlOne!=null) {
				String pm = MoneyHelper.roundHalfUp(new BigDecimal(dataSqlOne.get("sumProfit").toString())).toString();
				apiOrderNumberResult.setFxAmount(pm);
			}	
		}
		tc.end("s10");
		
		Date apiEnd = new Date();
		if("1".equals(KvTop.upFactory(EKvTop.TempCache).get("api_log_ApiOrderNumber"))) {
			MDataMap reqLogMap = new MDataMap();
			reqLogMap.put("api_class", getClass().getName());
			reqLogMap.put("token", StringUtils.trimToEmpty(mRequestMap.get("api_token")));
			reqLogMap.put("request_time", DateFormatUtils.format(apiStart, "yyyy-MM-ss HH:mm:ss.SSS"));
			reqLogMap.put("response_time", DateFormatUtils.format(apiEnd, "yyyy-MM-ss HH:mm:ss.SSS"));
			reqLogMap.put("cost_content", tc.toString());
			DbUp.upTable("lc_api_req_cost_log").dataInsert(reqLogMap);
		}
		
		return apiOrderNumberResult;
	}
	
	/**
	 * 版本兼容
	 * @param appVersion
	 * @return
	 */
	private Map<String,Object> getMainAfterSaleCount(String appVersion){
		Map<String,Object> map1 = new HashMap<String,Object>();
		if(StringUtils.isEmpty(appVersion)){
			appVersion = "5.2.7";//入参为空默认为之前版本
		}
		if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){
			map1 = DbUp.upTable("oc_order_after_sale").dataSqlOne(
					"SELECT	COUNT(*) FROM ordercenter.oc_order_after_sale " + 
					"WHERE buyer_code =:buyer_code AND show_flag = 1 AND asale_status in ('4497477800050001','4497477800050003','4497477800050005','4497477800050010','4497477800050013')", new MDataMap("buyer_code", getUserCode()));
		}else{
			map1= DbUp.upTable("oc_order_after_sale").dataSqlOne(
					"SELECT	COUNT(*) FROM ordercenter.oc_order_after_sale " + 
							"WHERE buyer_code =:buyer_code AND show_flag = 1 ", new MDataMap("buyer_code", getUserCode()));
		}		
		return map1;
	}
	
	/**
	 * 转换LD订单状态
	 * @param order_status
	 * @return
	 */
	private String convertOrderStatus(String order_status) {
		switch(order_status) {
			case "01": return "4497153900010001";//待付款
			case "02": return "4497153900010002";//待发货
			case "03": return "4497153900010003";//待收货
			case "04": return "4497153900010006";//交易关闭
			case "05": return "4497153900010005";//已完成
			case "06": return "4497153900010008";//取消中
			default : return "";
		}
	}
	
	private Integer getLdAfterSaleCount(String buyer_code,String appVersion){
		if(StringUtils.isEmpty(appVersion)){
			return 0;
		}
		if(AppVersionUtils.compareTo("5.2.7", appVersion)>=0){//版本小于等于5.2.7返回0，不查LD售后单数量
			return 0;
		}
		if(StringUtils.isEmpty(buyer_code)){
			return 0;
		}
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		String phoneNo = "";
		if("N".equals(isSyncLd)){//添加开关
			return 0;
		}
		String url = bConfig("groupcenter.rsync_homehas_url")+"getAfterServiceList";//访问LD
		Map<String,Object> params = new HashMap<String,Object>();
		MDataMap member = DbUp.upTable("mc_login_info").one("member_code",buyer_code);
		phoneNo = member.get("login_name");
		params.put("mobile", phoneNo);
		String result = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
		if(StringUtils.isEmpty(result)){
			return 0;
		}
		JSONObject jo = JSONObject.parseObject(result);
		String codeStr = jo.getString("code");
		if(StringUtils.isEmpty(codeStr)){
			return 0;
		}
		if(jo.getInteger("code")!= 0){
			return 0;
		}
		String jsonArrayStr = jo.getString("result");
		if(StringUtils.isEmpty(jsonArrayStr)){
			return 0;
		}
		String sql = "SELECT * FROM ordercenter.oc_after_sale_ld WHERE if_post != 1 and member_code = '"+buyer_code+"' AND after_sale_status in ('01','02','06') ";
		List<Map<String,Object>> listLocal = DbUp.upTable("oc_after_sale_ld").dataSqlList(sql, null);//本地的售后单
		List<Map<String,Object>>  list = new ArrayList<Map<String,Object>>();//接口的售后单
		JSONArray ja = JSONArray.parseArray(jsonArrayStr);
		Iterator it = ja.iterator();
		while(it.hasNext()){
			JSONObject oo = (JSONObject) it.next();
			Map<String,Object> afterSale = new HashMap<String,Object>();
			String cd = oo.getString("AFTER_SALE_CD");
			afterSale.put("order_code",oo.getString("ORD_ID"));//LD 订单编号
			afterSale.put("asale_code",oo.getString("AFTER_SALE_CODE_APP"));//LD 售后编号
			afterSale.put("asale_status",cd);//LD 售后状态
			afterSale.put("asale_code_ld", oo.getString("AFTER_SALE_CODE_LD"));
			if("10".equals(cd)||"04".equals(cd) || "03".equals(cd) || "08".equals(cd)){//已完成订单
				continue;
			}
			list.add(afterSale);
		}
		/**
		 * 去重（本地单子跟LD记录单子去重）
		 */
		List<Map<String,Object>> reaultList = new ArrayList<Map<String,Object>>();
		aa: for (Map<String, Object> map : list) {
			String asale_code = map.get("asale_code") != null ? map.get(
					"asale_code").toString() : "";
			for (Map<String, Object> mapOne : listLocal) {
				String asale_code1 = mapOne.get("after_sale_code_app") != null ? mapOne
						.get("after_sale_code_app").toString() : "";
				if (asale_code1.equals(asale_code)) {// 本地有，取本地，不要接口数据
					continue aa;
				}
			}
			reaultList.add(map);
		}
		for (Map<String, Object> mapOne : listLocal) {
			reaultList.add(mapOne);
		}
		return reaultList.size();
	}
	
	public String custLvlEx (String cust_lvl){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "注册会员");                         
		map.put("20", "1星会员");    
		map.put("25", "2星会员");
		map.put("30", "3星会员");  
		map.put("40", "4星会员");  
		map.put("50", "5星会员");
		map.put("60", "终身VIP");  
		map.put("70", "家有员工");
		map.put("80", "注册会员");
		map.put("90", "注册会员");
		map.put("100", "注册会员");                     
		return map.get(cust_lvl);
	}
}
