package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiOrderDetailsInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.familyhas.api.result.ApiOrderActivityDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderActivityRemarkDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderDetailsBalancePayResult;
import com.cmall.familyhas.api.result.ApiOrderDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderDonationDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderKjtDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderKjtParcelResult;
import com.cmall.familyhas.api.result.ApiOrderSellerDetailsResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.api.result.InvoiceInformationResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.service.OrderDetailService;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.familyhas.util.MoneyFormatUtil;
import com.cmall.groupcenter.account.model.ApiHomeOrderTrackingListResult;
import com.cmall.groupcenter.homehas.RsyncGetOrderTracking;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.groupcenter.homehas.model.ResponseGetOrderTrackingList;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrderDetail;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.helper.OrderHelper;
import com.cmall.ordercenter.model.OcOrderActivity;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.model.OrderDetail;
import com.cmall.ordercenter.service.ApiAlipayMoveProcessService;
import com.cmall.ordercenter.service.FlashsalesService;
import com.cmall.ordercenter.service.MemberAuthInfoSupport;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.service.TryoutProductsService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.util.StringUtility;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 订单详情
 * 
 * @author wz
 * 
 */
public class ApiOrderDetails extends
		RootApiForToken<ApiOrderDetailsResult, ApiOrderDetailsInput> {
	
	private static LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	public ApiOrderDetailsResult Process(ApiOrderDetailsInput inputParam,
			MDataMap mRequestMap) {
		//根据参数获取订单详情参数。(主流程)
		ApiOrderDetailsResult result = this.originalFlow(inputParam,mRequestMap);
		//处理按钮
		this.handleButton(result);
		//京东商品订单屏蔽取消发货按钮
		this.handleJdOrderButton(result);
		//根据订单号查询
		return result;
	}
	
	/**
	 * 处理按钮
	 * @param result
	 */
	private void handleButton(ApiOrderDetailsResult result) {
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(StringUtils.isEmpty(appVersion)) {
			appVersion = "5.5.0";
		}
		if(appVersion.compareTo("5.4.0")<0){
			return;
		}
		//获取订单编号
		String orderCode = result.getOrder_code();
		//根据订单编号去查询是否是拼团单
		if(orderCode.contains("OS")){
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("big_order_code",orderCode);
			orderCode = orderInfo.get("order_code");
		}
		MDataMap map = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(map == null || map.isEmpty()){//如果为空则证明不是拼团单
			return;
		}
		//如果是拼团单，此处应该有拼团编码。
		String collageCode = map.get("collage_code");
		//根据collageCode码去查询拼团状态
		MDataMap collage = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
		String status = collage.get("collage_status");
		result.setCollageCode(collageCode);
		List<Button> buttons = result.getOrderButtonList();
		for(Button button : buttons){
			String code = button.getButtonCode();
			if("4497477800080004".equals(code)){//【取消发货】按钮需要删除
				if(!"449748300002".equals(status)) {//拼团不成功订单需要隐藏取消发货按钮，449748300002=成功，boolean值取非
					button.setButtonStatus(3);
				}
			}
			if("449748300001".equals(status)) {//拼团中订单
				if("4497477800080012".equals(code)) {
					button.setButtonStatus(3);//提醒发货按钮状态置为不可用
				}
			}
		}
		result.setOrderButtonList(buttons);
	}
	

	private ApiOrderDetailsResult originalFlow(ApiOrderDetailsInput inputParam,
			MDataMap mRequestMap){

		ApiOrderDetailsResult apiOrderDetailsResult = new ApiOrderDetailsResult();
		apiOrderDetailsResult.setLink("0");//先赋值不显示。
		String sOrderCode = "";
		String appVersion = inputParam.getApp_vision();
		//惠家有大订单
		if("OS".equals(inputParam.getOrder_code().substring(0, 2))){
			sOrderCode = inputParam.getOrder_code();
			
			if(DbUp.upTable("oc_orderinfo").count("big_order_code",sOrderCode) == 0) {
				return apiOrderDetailsResult;
			}
			
			if(DbUp.upTable("oc_orderinfo").count("big_order_code",sOrderCode) > 0
					&& DbUp.upTable("oc_orderinfo").count("big_order_code",sOrderCode,"buyer_code",getUserCode())==0){
				apiOrderDetailsResult.setResultCode(0);
				apiOrderDetailsResult.setResultMessage("当前登录用户和下单人不是同一个，不能查询订单！");
				return apiOrderDetailsResult;
			}
			
		} 		
		//惠家有小订单
		else if("DD".equals(inputParam.getOrder_code().substring(0, 2)) || "HH".equals(inputParam.getOrder_code().substring(0, 2))){
			//根据订单 号查询是否有售后单
			sOrderCode = OrderHelper.upOrderCodeByOutCode(inputParam.getOrder_code());
			Integer count = DbUp.upTable("oc_order_after_sale").count("order_code",sOrderCode);
			if(count > 0){
				apiOrderDetailsResult.setLink("1");//有售后赋值显示
			}
			if(DbUp.upTable("oc_orderinfo").count("order_code",sOrderCode,"buyer_code",getUserCode())==0){
				apiOrderDetailsResult.setLink("1");//有售后赋值显示 LD 订单
				return apiOrderDetailsResult;
			}else{
				MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",sOrderCode);
				String small_seller_code = orderMap.get("small_seller_code");
				
				// 544 已发货和交易成功时都展示售后
				if("SI2003".equals(small_seller_code) 
						&& ArrayUtils.contains(new String[]{"4497153900010003","4497153900010005"}, orderMap.get("order_status"))){
					apiOrderDetailsResult.setLink("1");// LD 订单
				} 
				//换货新单增加换货标识
				if("SI2003".equals(small_seller_code) && AppVersionUtils.compareTo("5.3.2", appVersion)<=0) {
					if(AfterSaleService.isChangeGoodsOrder(sOrderCode, "")) {
						apiOrderDetailsResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
					}
				}
			}
		}
		//LD订单--只有小订单号
		else{
			apiOrderDetailsResult.setLink("1");//LD直接赋值显示	
			sOrderCode = inputParam.getOrder_code();
		}
		
		InvoiceInformationResult invoiceInformationResult = new InvoiceInformationResult();
		inputParam.setBuyer_code(getUserCode());
		
		Order order = new Order();
		OrderService orderService = new OrderService();
		ApiAlipayMoveProcessService apiAlipayMoveProcessService = new ApiAlipayMoveProcessService();
		TryoutProductsService tryoutProductsService = new TryoutProductsService();
		String alipayValue = null;
		String prov_name = null;
		String city_name = null;
		String area_name = null;
		String street_name = null;
		String area_code_address = null;
		String activityType = "";
		String activityCode = "";
		String balancePayMoneyStart = "0.00";
		BigDecimal bigDecimal = BigDecimal.ZERO;
		BigDecimal balancePayMoneyBigDecimal = BigDecimal.ZERO;
		boolean showTipsForZhiYou = false;  // 是否展示直邮商品的温馨提示
		
		double freightSpecial = 0.00;   //特殊地区  运费金额
		double freight = 0.00; //运费金额
		boolean flag = true;
		boolean freightFlag = true;
		boolean activityFlag = true;		
		List<MDataMap> list = new ArrayList<MDataMap>();
		List<Map<String, Object>> sellerList = new ArrayList<Map<String, Object>>();
		List<PcPropertyinfoForFamily> standardAndStyleList = new ArrayList<PcPropertyinfoForFamily>();
		List<Map<String, Object>> buyerList = new ArrayList<Map<String, Object>>();
		List<ApiOrderActivityDetailsResult> orderActivityDetailsResult = new ArrayList<ApiOrderActivityDetailsResult>();
		List<ApiOrderActivityRemarkDetailsResult> orderActivityRemarkDetailsResult = new ArrayList<ApiOrderActivityRemarkDetailsResult>();
		List<String> productCodeList = new ArrayList<String>();
		List<String> productCodeListNew = new ArrayList<String>();
		List<ApiOrderDetailsBalancePayResult> orderDetailsBalancePay = new ArrayList<ApiOrderDetailsBalancePayResult>();
		
		MDataMap dm = new MDataMap();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapSeller = new HashMap<String, Object>();
		List<Map<String,Object>> mapOrderPay = new ArrayList<Map<String,Object>>();
		
		LoadSkuInfoSpread loadSkuInfoSpread = new LoadSkuInfoSpread();
		PlusModelSkuInfoSpread plusModelSkuInfoSpread;
		
		ApiOrderActivityDetailsResult apiOrderActivityDetailsResultNew = new ApiOrderActivityDetailsResult();
		ApiOrderDetailsBalancePayResult apiOrderDetailsBalancePayResult = new ApiOrderDetailsBalancePayResult();
		BigDecimal fullMoney = BigDecimal.ZERO;//满减金额
		if(VersionHelper.checkServerVersion("3.5.44.51")){
			//大订单
			if("OS".equals(sOrderCode.substring(0, 2))){
				// 判断是否是闪购
				List<ApiOrderSellerDetailsResult> orderSellerList = new ArrayList<ApiOrderSellerDetailsResult>();
				
				//int deleteFlagCount = orderService.bigCountDeleteFlag(sOrderCode); // 判断此订单是否为以删除订单
				int deleteFlagCount = 1;  // 以删除订单可以正常查询，兼容橙意卡会员下单情况。
				
				if (deleteFlagCount != 0) { // 不等于0 为 未删除订单
					dm.put("big_order_code", sOrderCode);
					//查询大订单信息
					Map<String, Object> mapBig = DbUp.upTable("oc_orderinfo_upper").dataSqlOne("select * from oc_orderinfo_upper " +
							"where big_order_code=:big_order_code", new MDataMap("big_order_code", sOrderCode));
					
					
					buyerList = DbUp.upTable("oc_orderinfo")
							.dataSqlList(
									"select out_order_code,order_type,order_code,due_money,order_status,small_seller_code from ordercenter.oc_orderinfo where big_order_code=:big_order_code order by order_status ",
									dm);
					
					
					if(mapBig!=null && !"".equals(mapBig) && mapBig.size()>0){
						
						/**
						 * 把支付类型放入缓存中。  但在此段代码之后又访问了支付宝的方法，又会把此缓存类型给更新成支付宝
						 */
						if(VersionHelper.checkServerVersion("3.5.72.55")){
							//返回客户端  支付方式(默认支付宝)
							String defaultPayType = new PlusSupportPay().upPayFrom(String.valueOf(mapBig.get("big_order_code")));
							if(defaultPayType!=null && !"".equals(defaultPayType)){
								apiOrderDetailsResult.setDefault_Pay_type(defaultPayType);  
							}
							
							// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
							if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
								apiOrderDetailsResult.setDefault_Pay_type("449746280003");
							}
						}
						
						alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(sOrderCode,true); // 获取支付宝移动支付参数
						apiOrderDetailsResult.setAlipayUrl(FamilyConfig.ali_url_http);
						apiOrderDetailsResult.setAlipaySign(alipayValue);
						BigDecimal czjAmt = BigDecimal.ZERO;
						BigDecimal zckAmt = BigDecimal.ZERO;
						BigDecimal hjyBeanMoney = BigDecimal.ZERO;
						BigDecimal integralMoney = BigDecimal.ZERO;
						BigDecimal eventOnlinePayMoney = BigDecimal.ZERO;  // 在线支付立减
						BigDecimal plusPayMoney = BigDecimal.ZERO;  // plus会员折扣
						BigDecimal huDongMoney = BigDecimal.ZERO;  // 互动活动优惠
						BigDecimal hjycoinMoney = BigDecimal.ZERO;  // 互动活动优惠
						//判断订单中是否有网易考拉的商品
						List<Map<String, Object>> kaolalist = DbUp.upTable("oc_orderinfo").dataSqlList(
										"select order_code,small_seller_code from ordercenter.oc_orderinfo where big_order_code=:big_order_code and small_seller_code=:small_seller_code order by order_status ",
										new MDataMap("big_order_code", sOrderCode,"small_seller_code",AppConst.MANAGE_CODE_WYKL,"delete_flag", "0"));
						if(kaolalist != null && kaolalist.size() > 0) {
							apiOrderDetailsResult.setFlagKaola("1");
						}
						for(Map<String, Object> mapOrder : buyerList){
							order = orderService.getOrder(String.valueOf(mapOrder.get("order_code"))); // 获取与订单相关内容的信息
							
							/*初始化暂存款，储备金 begin*/
							
							String amtSql = "order_code='"+order.getOrderCode()+"' and pay_type in ('449746280006','449746280007','449746280008','449746280015','449746280019','449746280022','449746280024','449746280025')";
							
							List<MDataMap> amtDataMaps = DbUp.upTable("oc_order_pay").queryAll("pay_type,payed_money", "", amtSql,new MDataMap());
							
							for(MDataMap amtDataMap : amtDataMaps){					
								
								if(amtDataMap != null){
									
									String amt_pay_type = amtDataMap.get("pay_type");
									
									String amtStr = amtDataMap.get("payed_money");
									
									/*储备金*/
									if(StringUtils.equals(amt_pay_type, "449746280006")){						
										
										
										if(StringUtils.isNotBlank(amtStr)){
											
											czjAmt = czjAmt.add(new BigDecimal(amtStr));
											
											continue;
											
										}
										
									}
									
									/*暂存款*/
									if(StringUtils.equals(amt_pay_type, "449746280007")){						
										
										
										if(StringUtils.isNotBlank(amtStr)){
											
											zckAmt = zckAmt.add(new BigDecimal(amtStr));
											
											continue;
											
										}
										
									}
									
									/*
									 * 惠豆   fq++
									 */
									if(StringUtils.equals(amt_pay_type, "449746280015")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											hjyBeanMoney = hjyBeanMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
									/*
									 * 积分   zf++
									 */
									if(StringUtils.equals(amt_pay_type, "449746280008")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											integralMoney = integralMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
									/*
									 * plus折扣
									 */
									if(StringUtils.equals(amt_pay_type, "449746280022")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											plusPayMoney = plusPayMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
									/*
									 * 在线支付立减
									 */
									if(StringUtils.equals(amt_pay_type, "449746280019")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											eventOnlinePayMoney = eventOnlinePayMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
									/*
									 * 互动活动优惠
									 */
									if(StringUtils.equals(amt_pay_type, "449746280024")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											huDongMoney = huDongMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
									/*
									 * 互动活动优惠
									 */
									if(StringUtils.equals(amt_pay_type, "449746280025")) {
										if(StringUtils.isNotBlank(amtStr)){
											
											hjycoinMoney = hjycoinMoney.add(new BigDecimal(amtStr));
											
											continue;
											
										}
									}
									
								}
								
							}
							
							/*初始化暂存款，储备金 end*/
							
							/**
							 * 如果一个大订单下面包含多个小订单,如果有一个小订单为海外购的订单,那么此大订单就返回此订单为海外购的
							 */
//							if (AppConst.MANAGE_CODE_KJT.equals(order.getSmallSellerCode())
//									|| AppConst.MANAGE_CODE_MLG.equals(order.getSmallSellerCode())
//									|| AppConst.MANAGE_CODE_QQT.equals(order.getSmallSellerCode())
//									|| AppConst.MANAGE_CODE_SYC.equals(order.getSmallSellerCode())
//									|| AppConst.MANAGE_CODE_CYGJ.equals(order.getSmallSellerCode())) {
//							if (new PlusServiceSeller().isKJSeller(order.getSmallSellerCode())) {
							if (AppConst.MANAGE_CODE_KJT.equals(order.getSmallSellerCode())) {
								apiOrderDetailsResult.setFlagTheSea("1");

							}
							//让其在无限的循环中指进入一次，因为以下数据，只需根据某一个小订单号返回一次即可
							if(flag){
								if (order.getAddress() != null && !"".equals(order.getAddress())) {
//									//为兼容旧版本做相应处理
									String areaCode = order.getAddress().getAreaCode();
									int isFour = DbUp.upTable("sc_tmp").count("code", areaCode, "code_lvl", "4");
									if(isFour > 0) {//当前编码为四级编码
										//加入查询区域的代码
										String area_sql="SELECT d.`name` AS prov_name,d.`code` AS prov_code,IF (c.show_yn = 'Y', c.`name`, '') AS city_name,c.`code` AS city_code, "
												+ "IF(b.show_yn = 'Y',b.name,'') AS area_name,b.code AS area_code,IF(a.show_yn = 'Y',a.`name`,'') AS street_name,a.`code` AS street_code "
												+ "from sc_tmp a  LEFT JOIN sc_tmp b on b.`code` = a.p_code "
												+ "LEFT JOIN sc_tmp c on c.`code` = b.p_code "
												+ "left join sc_tmp d on d. code = c.p_code "
												+ "where a.`code`=:area_code";
										Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",order.getAddress().getAreaCode()));
										prov_name = MapUtils.getString(areaMap, "prov_name", "");
										city_name = MapUtils.getString(areaMap, "city_name", "");
										area_name = MapUtils.getString(areaMap, "area_name", "");
										street_name = MapUtils.getString(areaMap, "street_name", "");
										apiOrderDetailsResult.setConsigneeAddress(prov_name + city_name + area_name + street_name + " " + order.getAddress().getAddress());
									}else {//当前编码为三级编码
										String area_sql="SELECT a.`code` AS area_code,c.`name` as prov_name,b.`name` as city_name,a.`name` AS area_name,b.`code` as city_code,c.`code` as prov_code "
												+ "from sc_tmp a  LEFT JOIN sc_tmp b on b.`code`=CONCAT(LEFT(a.`code`,4),'00') "
												+ "LEFT JOIN sc_tmp c on c.`code`=CONCAT(LEFT(a.`code`,2),'0000') "
												+ "where a.`code`=:area_code";
										Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",order.getAddress().getAreaCode()));
										prov_name=(String)areaMap.get("prov_name");
										city_name=(String)areaMap.get("city_name");
										area_name=(String)areaMap.get("area_name");
										apiOrderDetailsResult.setConsigneeAddress((prov_name.equals(city_name)?prov_name:(prov_name+city_name))+area_name+" "+order
												.getAddress().getAddress());
									}
									
									String idNumber = new MemberAuthInfoSupport().enIdNumber(order.getAddress().getAuthIdcardNumber());
									
									apiOrderDetailsResult.setIdNumber(idNumber);
									
									apiOrderDetailsResult.setConsigneeName(order.getAddress().getReceivePerson());
									apiOrderDetailsResult.setConsigneeTelephone(order.getAddress().getMobilephone()); // 收货人电话
									invoiceInformationResult.setInvoiceInformationTitle(order.getAddress().getInvoiceTitle());
									invoiceInformationResult.setInvoiceInformationType(order.getAddress().getInvoiceType());
									invoiceInformationResult.setInvoiceInformationValue(order.getAddress().getInvoiceContent());

									apiOrderDetailsResult.setInvoiceInformation(invoiceInformationResult);
								}
								
								apiOrderDetailsResult.setCreate_time(String.valueOf(mapBig.get("create_time")));
								
							
								//积分兑换
								String orderType = MapUtils.getString(mapOrder, "order_type", "");
								if("449715200024".equals(orderType)) {
									BigDecimal allMoney = MoneyFormatUtil.moneyFormat(mapBig.get("all_money").toString());
									for(OrderDetail orderDetail : order.getProductList()) {
										allMoney = allMoney.subtract(orderDetail.getIntegralMoney());
									}
									apiOrderDetailsResult.setDue_money(allMoney);
								}else {
									apiOrderDetailsResult.setDue_money(MoneyFormatUtil.moneyFormat(mapBig.get("all_money").toString()));
								}
											
								
								
								apiOrderDetailsResult.setFirstFavorable(""); // 不知道怎么获取值(收单优惠)
								
								
								apiOrderDetailsResult.setFullSubtraction(0.00); // 满减目前是"0"
								apiOrderDetailsResult.setTelephoneSubtraction(0.00); // 手机下单减少目前为"0"

								apiOrderDetailsResult.setOrder_code(String.valueOf(mapBig.get("big_order_code")));
								
								
								if("4497153900010001".equals(order.getOrderStatus())){
									apiOrderDetailsResult.setOrder_money(String.valueOf(mapBig.get("due_money")));
								}else if("4497153900010002".equals(order.getOrderStatus()) || "4497153900010006".equals(order.getOrderStatus())){
									apiOrderDetailsResult.setOrder_money(String.valueOf(mapBig.get("order_money")));
								}else{
									apiOrderDetailsResult.setOrder_money(String.valueOf(mapBig.get("payed_money")));
								}
								
								
								apiOrderDetailsResult.setOrder_status(order.getOrderStatus());
								apiOrderDetailsResult.setPay_type(String.valueOf(mapBig.get("pay_type"))); // 支付方式
								
								flag=false;
							}
							
							/*
							 * transport_template(运费) 、validate_flag(是否是虚拟商品  Y：是  N：否)
							 * 查询有运费  并且  不是虚拟商品的   商品信息          (只返回了一次   看最后  按照freightFlag来找)
							 */
							String sql = "select uid from pc_productinfo where product_code in " +
									"(select product_code from ordercenter.oc_orderdetail where order_code=:order_code) " +
									"and LENGTH(transport_template)=32 and validate_flag='N'";
							
							
							MDataMap productinfoMDataMap = new MDataMap();
							productinfoMDataMap.put("order_code", order.getOrderCode());
							
							Map<String, Object> productinfoMap = DbUp.upTable("pc_productinfo").dataSqlOne(sql, productinfoMDataMap);
							/*
							 * 如果此订单有收货人地址并且  地址是   新疆（65） 或 西藏(54)  运费返回 24.00
							 */
							if(VersionHelper.checkServerVersion("3.5.93.55")){
								
								freight = freight + order.getTransportMoney().doubleValue();
								
							}else if(VersionHelper.checkServerVersion("3.5.22.51") && 
									productinfoMap!=null && !"".equals(productinfoMap) && productinfoMap.size()>0 &&
									("65".equals(area_code_address.substring(0, 2)) || "54".equals(area_code_address.substring(0, 2)))){
								
								freightFlag = false;   //在大循环外面  会用到此标示
								freightSpecial = freightSpecial + 24.00;
								//apiOrderDetailsResult.setFreight(24.00);
							}else{
								freight = freight + order.getTransportMoney().doubleValue();
								
								//apiOrderDetailsResult.setFreight(freight);
							}
							
							/**
							 * 此段代码用于判断，大订单下的每一个小订单是否为跨径通的订单，
							 * flagKJT用于判断是否跨径通的订单，如果大订单下的某一个小订单为跨径通订单，那么，此小订单下的
							 * 商品也为跨径通的商品。
							 */
							boolean flagKJT = false;
//							if (AppConst.MANAGE_CODE_KJT.equals(mapOrder.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_MLG.equals(mapOrder.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_QQT.equals(mapOrder.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_SYC.equals(mapOrder.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_CYGJ.equals(mapOrder.get("small_seller_code"))) {
//							if (new PlusServiceSeller().isKJSeller(mapOrder.get("small_seller_code").toString())) {
							if (AppConst.MANAGE_CODE_KJT.equals(mapOrder.get("small_seller_code"))){
								flagKJT = true;
							}							
							ProductLabelService productLabelService = new ProductLabelService();
							for (OrderDetail orderDetail : order.getProductList()) {
								/*
								 * 只显示非赠品商品
								 */
								if("1".equals(orderDetail.getGiftFlag())){
									list = orderService.flashSales(orderDetail.getSkuCode()); // 存在为闪购信息
									sellerList = orderService.sellerInformation(orderDetail.getSkuCode()); // 查询商品信息
									if (sellerList != null && !sellerList.isEmpty()) {

										for (int i = 0; i < sellerList.size(); i++) {
											ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();
											List<ApiOrderDonationDetailsResult> apiOrderDonationDetailsResultList = new ArrayList<ApiOrderDonationDetailsResult>();
											List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
											
											map = sellerList.get(i);
											apiOrderSellerDetailsResult.setSmallSellerCode(map.get("small_seller_code")+"");
											if (!"null".equals(String.valueOf(map.get("sku_picurl")))) {
												apiOrderSellerDetailsResult.setMainpicUrl(map.get("sku_picurl").toString());
											}
											apiOrderSellerDetailsResult.setNumber(String.valueOf(orderDetail.getSkuNum()));
											
											//flagKJT为true，说明此商品是海外购商品
											if(flagKJT){
												apiOrderSellerDetailsResult.setFlagTheSea("1");
											}else{
												apiOrderSellerDetailsResult.setFlagTheSea("0");
											}
											
											/**
											 * 商品价格
											 */
											BigDecimal sell_price = new BigDecimal(String.valueOf(orderDetail.getSkuPrice())).add(new BigDecimal(String.valueOf(orderDetail.getGroupPrice())).add(new BigDecimal(String.valueOf(orderDetail.getCouponPrice()))));
											apiOrderSellerDetailsResult.setDeal_price(orderDetail.getSkuPrice().doubleValue());
											apiOrderSellerDetailsResult.setProductCode(String.valueOf(map.get("product_code")));
											apiOrderSellerDetailsResult.setSkutCode(orderDetail.getSkuCode());
											
											//积分兑换
											String orderType = MapUtils.getString(mapOrder, "order_type", "");
											if("449715200024".equals(orderType)) {
												int skuNum = orderDetail.getSkuNum();
												BigDecimal integralMoneyTmp = orderDetail.getIntegralMoney().divide(new BigDecimal(skuNum), BigDecimal.ROUND_HALF_UP);
												
												//商品单价 = 商品售价(sku_price) - (integral_money / sku_num);
												apiOrderSellerDetailsResult.setIntegral(integralMoneyTmp.multiply(new BigDecimal(200)).toString());
												apiOrderSellerDetailsResult.setPrice(orderDetail.getShowPrice().subtract(integralMoneyTmp).doubleValue());
											}else {
												apiOrderSellerDetailsResult.setPrice(orderDetail.getShowPrice().doubleValue());
											}
											
											//添加图片
											apiOrderSellerDetailsResult.setLabelsPic(productLabelService.getLabelInfo(String.valueOf(map.get("product_code"))).getListPic());
											
											//把商品编号单独存入一个list中  用于获取预计返利金额
											productCodeList.add(String.valueOf(map.get("product_code")));
											
											apiOrderSellerDetailsResult.setProductName(orderDetail.getSkuName());
											
											/*是否生鲜*/
											PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(apiOrderSellerDetailsResult.getProductCode());
											
											List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
											
											apiOrderSellerDetailsResult.setLabelsList(labelsList);
											
											apiOrderSellerDetailsResult.setRegion(orderDetail.getStoreCode());

											standardAndStyleList = orderService.sellerStandardAndStyle(map.get("sku_keyvalue").toString()); // 截取 尺码和款型
											
											if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
												for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
													ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
													apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
													apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
													apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
												}
												apiOrderSellerDetailsResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
											}
											/*计算商品总金额*/
											//积分兑换
											if("449715200024".equals(orderType)) {
												BigDecimal integralMoneyTmp = orderDetail.getIntegralMoney();
												BigDecimal avgIntegralMoney = integralMoneyTmp.divide(new BigDecimal(orderDetail.getSkuNum()), BigDecimal.ROUND_HALF_UP);
												
												//商品单价 = 商品售价(sku_price) - (integral_money / sku_num);
												apiOrderDetailsResult.setProductMoney(apiOrderDetailsResult.getProductMoney().add(orderDetail.getShowPrice().subtract(avgIntegralMoney).
														multiply(new BigDecimal(orderDetail.getSkuNum()))));
												apiOrderDetailsResult.setProductIntegral(apiOrderDetailsResult.getProductIntegral().add(integralMoneyTmp.multiply(new BigDecimal(200))));
												apiOrderDetailsResult.setOrderType(orderType);
											}else {
												apiOrderDetailsResult.setProductMoney(apiOrderDetailsResult.getProductMoney().add(sell_price));		
											}
											orderSellerList.add(apiOrderSellerDetailsResult);
											
											apiOrderSellerDetailsResult.setDetailsList(apiOrderDonationDetailsResultList); // 赠品信息(目前为空)
											
											//添加按钮
											MDataMap orderInfo1 = DbUp.upTable("oc_orderinfo").one("order_code", orderDetail.getOrderCode());
											
											//获取跨境商户Code
//											List<String> kuajingShopList = getKJTShopList();
											
											//if (StringUtils.startsWith(orderInfo1.get("small_seller_code"), "SF031"))
//											  if(!kuajingShopList.contains((orderInfo1.get("small_seller_code")))){
//												if(!new PlusServiceSeller().isKJSeller(orderInfo1.get("small_seller_code"))){
											/**
											 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
											 */
											String uc_seller_type = WebHelper.getSellerType(orderInfo1.get("small_seller_code"));
											if(StringUtils.isEmpty(uc_seller_type)&&"SI2003".equals(orderInfo1.get("small_seller_code"))){
												PlusSupportLD ld = new PlusSupportLD();
												String isSyncLd = ld.upSyncLdOrder();
												if("Y".equals(isSyncLd)){
													uc_seller_type = "4497478100050001";
												}
												if(AppVersionUtils.compareTo("5.2.7", appVersion)>=0){//版本低于527
													uc_seller_type = "";
												}
											}
											if(StringUtils.equals(uc_seller_type, "4497478100050001") || StringUtils.equals(uc_seller_type, "4497478100050005")|| StringUtils.equals(uc_seller_type, "4497478100050004")){
												MDataMap orderInfo=DbUp.upTable("oc_orderdetail").one("order_code",orderDetail.getOrderCode(),"sku_code",orderDetail.getSkuCode());
												String flag_asale=orderInfo.get("flag_asale");
												String asale_code=orderInfo.get("asale_code");
												//校验是否显示售后按钮方法 ture 显示，false 不显示
												boolean ifAllowAfterSaleFlag = new AfterSaleService().checkIfAllowAfterSale(orderDetail.getOrderCode(),orderDetail.getSkuCode(),"0");
												if(ifAllowAfterSaleFlag){
													//售后按钮解决冲突
													apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008")));//售后
													
												}else{
													//
													if(StringUtils.isNotBlank(asale_code)){
														MDataMap afterSaleInfo=DbUp.upTable("oc_order_after_sale").one("asale_code",asale_code);
														String asale_status=afterSaleInfo.get("asale_status");
														apiOrderSellerDetailsResult.setAfterCode(asale_code);
														
//														apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080011",(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",asale_status))));
//														apiOrderSellerDetailsResult.setAfterSaleStatusCode(asale_status);
//														apiOrderSellerDetailsResult.setAfterSaleStatusName((String)DbUp.upTable("oc_order_after_sale").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",asale_status)));
													}
												}
											}
											
											
										}
									}

								}
							}
							
							String remark = null;  //此字段用于判断所有活动号都不满足是  只显示一条活动批注(remark)
							ApiOrderActivityRemarkDetailsResult apiOrderActivityRemarkDetailsResult = null;
							List<OcOrderActivity> ocOrderActivities = order.getActivityList();
							
							if (ocOrderActivities != null && !ocOrderActivities.isEmpty()) {
								for (int i = 0; i < ocOrderActivities.size(); i++) {
									apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
									/*
									 * 折扣(活动信息)
									 */
									if(ocOrderActivities.get(i).getActivityCode()!=null && !"".equals(ocOrderActivities.get(i).getActivityCode()) &&
											ocOrderActivities.get(i).getOrderCode()!=null && !"".equals(ocOrderActivities.get(i).getOrderCode())){
											
										    ApiOrderActivityDetailsResult apiOrderActivityDetailsResult = null;
										   
											/*
											 *活动备注(oc_order_activity表中"activity_code为TA : 代表使用商品的活动；SG：为闪够商品的活动；1002317：为首单88折") 
											 */
											if("TA".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))
													|| "449715400006".equals(String.valueOf(ocOrderActivities.get(i).getActivityType()))){  //查询TA(代表使用商品的活动)的活动备注
												 // 立减30  都需显示活动名称(已在属于闪够 和 立减30的判断中赋值了)，  其他显示活动编号
												apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
												if("449715400006".equals(String.valueOf(ocOrderActivities.get(i).getActivityType()))){
													if(new BigDecimal(bConfig("familyhas.fullSubActivityMoney")).compareTo(new BigDecimal(ocOrderActivities.get(i).getPreferentialMoney()))==0){
														apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.fullSubActivityName"));
													}else {
														apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.eveActivityName"));
													}
												}else {
													apiOrderActivityDetailsResult.setActivityType(ocOrderActivities.get(i).getActivityType()); 
												}
												apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
												apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
												apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
												
												/*
												 * 此活动备注显示  新疆、西藏运费24.00等等
												 */
												if(remark == null && activityFlag){
													apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));	
													if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
													remark = "remarkValueExist";
												}
												
												//查询试用商品(目前没用到，年前上线屏蔽有可能会有问题，目前先不屏蔽)
												List<Map<String, Object>> tryOutProductsList = tryoutProductsService.getTryoutProducts(String.valueOf(order.getCreateTime()), 
														String.valueOf(ocOrderActivities.get(i).getSkuCode()), 
														String.valueOf(ocOrderActivities.get(i).getActivityCode()));   
												if(tryOutProductsList!=null && !"".equals(tryOutProductsList) && !tryOutProductsList.isEmpty()){
													for(Map<String, Object> mapTryOut : tryOutProductsList){
														apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
														apiOrderActivityRemarkDetailsResult.setRemark(String.valueOf(mapTryOut.get("notice")));
													}
												}
												
											}else if("SG".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))){  //查询SG(为闪够商品的活动)的活动备注
												
												FlashsalesService flashsalesService = new FlashsalesService();
												Map<String, Object> mapFlashsales = flashsalesService.getActivityFlashsales(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
												
												if(mapFlashsales!=null && !"".equals(mapFlashsales) && mapFlashsales.size()>0){
//													apiOrderActivityDetailsResult.setActivityType(String.valueOf(mapFlashsales.get("activity_name")));   //闪够是显示闪够的活动名称
//													apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
//													apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
//													apiOrderActivityRemarkDetailsResult.setRemark(String.valueOf(mapFlashsales.get("remark")));
													
												}
												/*
												 * 此活动备注显示  新疆、西藏运费24.00等等
												 */
												if(remark == null && activityFlag){
													apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));	
													if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
													remark = "remarkValueExist";
												}
												
											}else if("1002317".equals(ocOrderActivities.get(i).getActivityCode())){   //查询1002317(为首单88折)的活动备注
												//apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.firstActivityRemark"));
											}else if("1003037".equals(ocOrderActivities.get(i).getActivityCode())){    //立减30元
												
												// 立减30  都需显示活动名称(已在属于闪够 和 立减30的判断中赋值了)，  其他显示活动编号
												apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
												apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.eveActivityName"));    //立减30元 活动类型需要显示成  活动名称
												apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
												apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
												apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
												
												//apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
												apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.eveActivityRemark"));
											}else if ("1003358".equals(ocOrderActivities.get(i).getActivityCode())) {//满399减50
												
												apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
												apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.fullSubActivityName"));    //立减30元 活动类型需要显示成  活动名称
												apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
												apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
												apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
											}else if(VersionHelper.checkServerVersion("3.5.22.51") && remark == null &&activityFlag){   //所有活动都不满足显示： （新疆、西藏地区每单收24元运费）
												
												activityFlag = false;
												//apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
												apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));
												if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
												remark = "remarkValueExist";
											}
											
											/**
											 * 由于闪够不需要放备注，所以判断下是否为空，否则返回前台会是null
											 */
											if(apiOrderActivityRemarkDetailsResult!=null && !"".equals(apiOrderActivityRemarkDetailsResult) && 
													apiOrderActivityRemarkDetailsResult.getRemark()!=null){
												activityFlag = false;
												orderActivityRemarkDetailsResult.add(apiOrderActivityRemarkDetailsResult);
											}
											if(apiOrderActivityDetailsResult!=null && !"".equals(apiOrderActivityDetailsResult)){
												orderActivityDetailsResult.add(apiOrderActivityDetailsResult);
											}
											
									}
									
									/*
									 * 首单88折
									 */
									if (bConfig("familyhas.firstActivity").equals(ocOrderActivities.get(i).getActivityCode())) {  
										
										//apiOrderDetailsResult.setFirstFavorable(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
										//apiOrderDetailsResult.setFirstFavorable(orderService.discountSku(sOrderCode));
										
									} else {
										//下单成功 未付款 且  是闪够活动的   ，提示15分钟后失效！
										if ("4497153900010001".equals(order.getOrderStatus())&& (!"".equals(ocOrderActivities.get(i).getActivityCode()) || !""
														.equals(ocOrderActivities.get(i).getActivityName())) && 
														"SG".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))) {
											apiOrderDetailsResult.setIfFlashSales("0");
											apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后15分钟内不付款，系统将自动取消订单！");
										}
									}
								}
							}
//							else if(VersionHelper.checkServerVersion("3.5.22.51")){ //此订单没有活动时显示： （新疆、西藏地区每单收24元运费）
//								apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
//								apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));
//								orderActivityRemarkDetailsResult.add(apiOrderActivityRemarkDetailsResult);
//								
//							}
							
							//优惠劵       pay_type = '449746280002'类型为优惠券
							List<Map<String, Object>> orderPayList = DbUp.upTable("oc_order_pay").dataSqlList("select p.pay_type,p.pay_sequenceid,c.coupon_code,a.activity_code,a.activity_name,c.initial_money,p.payed_money " +
									"FROM oc_order_pay p LEFT JOIN oc_coupon_info c on p.pay_sequenceid = c.coupon_code " +
									"LEFT JOIN oc_activity a on c.activity_code = a.activity_code  " +
									"where  p.order_code =:order_code and p.pay_type in ('449746280002','449746280021','449746280023')", new MDataMap("order_code",String.valueOf(mapOrder.get("order_code"))));
							//String.valueOf(map.get("order_code"))
							
							for(Map<String, Object> orderPayMap : orderPayList){
								String activityTypeTemp = "";
								if("449746280021".equals(String.valueOf(orderPayMap.get("pay_type")))) {
									activityTypeTemp = bConfig("xmasorder.redeem_name");
								}else if("449746280023".equals(String.valueOf(orderPayMap.get("pay_type")))){
									activityTypeTemp = bConfig("xmasorder.farm_name");
								}else {
									activityTypeTemp = String.valueOf(orderPayMap.get("activity_name"));
								}
								activityType = String.valueOf(activityTypeTemp);
								activityCode = String.valueOf(orderPayMap.get("activity_code"));
								//apiOrderActivityDetailsResult.setActivityType(String.valueOf(orderPayMap.get("activity_name")));
								//apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
								bigDecimal = new BigDecimal(String.valueOf(orderPayMap.get("payed_money"))).add(bigDecimal);
								
								//moneyActivity +=  Integer.parseInt(String.valueOf(orderPayMap.get("payed_money")));
								//apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(moneyActivity)+".00");
								
//								if(apiOrderActivityDetailsResult!=null && !"".equals(apiOrderActivityDetailsResult)){
//									orderActivityDetailsResult.add(apiOrderActivityDetailsResult);
//								}
							}
							//促销系统商品订单自动取消订单提示  (扫码购)
							if("4497153900010001".equals(order.getOrderStatus())&&order.getActivityList()!=null&&!order.getActivityList().isEmpty()
									){
								List<MDataMap> ics = DbUp.upTable("oc_order_activity").queryAll("activity_code,out_active_code", "", " activity_code like 'CX%' and  activity_type!='4497472600010025'  and order_code=:order_code", new MDataMap("order_code",order.getOrderCode()));
								if(ics!=null&&!ics.isEmpty()){
									List<String> cxCodes = new ArrayList<String>();
									for (int jj = 0; jj < ics.size(); jj++) {
										cxCodes.add(ics.get(jj).get("activity_code"));
									}
									String icTs = new PlusSupportProduct().getOrderRemind(cxCodes);
									if(StringUtility.isNotNull(icTs)){
										if("1".equals(apiOrderDetailsResult.getFlagKaola())) {
											if(new PlusSupportProduct().compareOrderRemind(cxCodes, 1200L) == 1) {
												apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后20分钟内不付款，系统将自动取消订单！");
											} else {
												apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
											}
										} else {
											apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
										}
									}
								}
							}
							

							if ("4497153900010001".equals(order.getOrderStatus())
									&& (apiOrderDetailsResult.getFailureTimeReminder() == null || "".equals(apiOrderDetailsResult.getFailureTimeReminder()))) {
								apiOrderDetailsResult.setIfFlashSales("1");
								if("1".equals(apiOrderDetailsResult.getFlagKaola())) {
									apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后20分钟内不付款，系统将自动取消订单！");
								} else {
									apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后24小时内不付款，系统将自动取消订单！");
								}								
							}
							
							//查询订单轨迹
							MDataMap dateMap = new MDataMap();
							dateMap.put("order_code", order.getOrderCode());
							List<MDataMap> trackAll = DbUp.upTable("oc_order_tracking").queryAll("*", "-outgo_time", " order_code = :order_code", dateMap);
							if(trackAll.size() > 0) {
								MDataMap mDataMap = trackAll.get(0);
								apiOrderDetailsResult.setYc_express_num(mDataMap.get("yc_express_num"));
								apiOrderDetailsResult.setYc_delivergoods_user_name(mDataMap.get("yc_delivergoods_user_name"));
							}
							for (MDataMap mDataMap : trackAll) {
								ApiHomeOrderTrackingListResult trackingListResult = new ApiHomeOrderTrackingListResult();
								
								trackingListResult.setOrderTrackContent(mDataMap.get("outgo_no"));
								trackingListResult.setOrderTrackTime(mDataMap.get("outgo_time"));
								trackingListResult.setYc_dis_time(mDataMap.get("yc_dis_time"));
								trackingListResult.setYc_update_time(mDataMap.get("yc_update_time"));
								
								apiOrderDetailsResult.getApiHomeOrderTrackingListResult().add(trackingListResult);
							}
							
							
							/**
							 * 获取微公社余额支付金额
							 */
//							mapOrderPay = DbUp.upTable("oc_order_pay").dataSqlOne("select * from oc_order_pay where order_code=:order_code and pay_type=:pay_type", 
//									new MDataMap("order_code",String.valueOf(mapOrder.get("order_code")),"pay_type","449746280009"));
							mapOrderPay = DbUp.upTable("oc_order_pay").
									dataSqlList("select * from oc_order_pay where order_code=:order_code and pay_type in ('449746280009','449746280012','449746280015')", 
											new MDataMap("order_code",String.valueOf(mapOrder.get("order_code"))));
							if(mapOrderPay!=null && !"".equals(mapOrderPay) && mapOrderPay.size()>0){
								for (int i = 0; i < mapOrderPay.size(); i++) {
									if(mapOrderPay.get(i)!=null&&"449746280009".equals(mapOrderPay.get(i).get("pay_type"))){
										balancePayMoneyBigDecimal = new BigDecimal(balancePayMoneyStart).add(new BigDecimal(String.valueOf(mapOrderPay.get(i).get("payed_money"))));
										balancePayMoneyStart = String.valueOf(balancePayMoneyBigDecimal);
									}else if (mapOrderPay.get(i)!=null&&"449746280012".equals(mapOrderPay.get(i).get("pay_type"))) {
										fullMoney = fullMoney.add(new BigDecimal(String.valueOf(mapOrderPay.get(i).get("payed_money"))));
									}
								}
							}
						}
						

						
						apiOrderDetailsResult.setCzjAmt(czjAmt);
						
						apiOrderDetailsResult.setZckAmt(zckAmt);
						
						apiOrderDetailsResult.setHjyBean(hjyBeanMoney);
						
						apiOrderDetailsResult.setIntegralMoney(integralMoney);
						
						//返回运费
						if(freightFlag){
							apiOrderDetailsResult.setFreight(freight);
						}else{
							apiOrderDetailsResult.setFreight(freightSpecial);	//收货人地址是   新疆（65） 或 西藏(54)  运费返回 24.00			
						}
						
						// 互动活动优惠
						if(huDongMoney.compareTo(BigDecimal.ZERO) > 0){
							ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
							act.setActivityType((String)DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code","449746280024")));
							act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
							act.setPreferentialMoney(huDongMoney.toString());
							orderActivityDetailsResult.add(act);
						}
						
						// plus折扣
						if(plusPayMoney.compareTo(BigDecimal.ZERO) > 0){
							ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
							act.setActivityType(bConfig("xmassystem.plus_order_pay_name"));
							act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
							act.setPreferentialMoney(plusPayMoney.toString());
							orderActivityDetailsResult.add(act);
						}
						
						// 在线支付立减
						if(eventOnlinePayMoney.compareTo(BigDecimal.ZERO) > 0){
							ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
							act.setActivityType(bConfig("familyhas.online_pay_name"));
							act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
							act.setPreferentialMoney(eventOnlinePayMoney.toString());
							orderActivityDetailsResult.add(act);
						}
						
						// 惠币支付
						if(hjycoinMoney.compareTo(BigDecimal.ZERO) > 0) {
							ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
							act.setActivityType("惠币抵扣");
							act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
							act.setPreferentialMoney(hjycoinMoney.toString());
							orderActivityDetailsResult.add(act);
						}
						
						//返回无活动时的备注
						if(activityFlag){
							if(VersionHelper.checkServerVersion("3.5.22.51")){ //此订单没有活动时显示： （新疆、西藏地区每单收24元运费）
								ApiOrderActivityRemarkDetailsResult apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
								apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));
								if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
								orderActivityRemarkDetailsResult.add(apiOrderActivityRemarkDetailsResult);
								
							}
						}
						
						//返回优惠劵信息
						if(!"".equals(activityType)){
							apiOrderActivityDetailsResultNew.setActivityType(activityType);
							apiOrderActivityDetailsResultNew.setActivityCode(activityCode);
							apiOrderActivityDetailsResultNew.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
							
							//moneyActivity = moneyActivity + Integer.parseInt(String.valueOf(orderPayMap.get("payed_money")));
							apiOrderActivityDetailsResultNew.setPreferentialMoney(bigDecimal.toString());
							
							if(apiOrderActivityDetailsResultNew!=null && !"".equals(apiOrderActivityDetailsResultNew)){
								boolean isIn = false;
								for(ApiOrderActivityDetailsResult oc : orderActivityDetailsResult) {
									if(oc.getActivityCode().equals(apiOrderActivityDetailsResultNew.getActivityCode())) {
										BigDecimal bd1 = new BigDecimal(oc.getPreferentialMoney());
										BigDecimal bd2 = new BigDecimal(apiOrderActivityDetailsResultNew.getPreferentialMoney());
										oc.setPreferentialMoney(bd1.add(bd2).setScale(2).toString());
										isIn = true;
										break;
									}
								}
								if(!isIn) {
									orderActivityDetailsResult.add(apiOrderActivityDetailsResultNew);
								}
							}
						}
						
						apiOrderDetailsResult.setCashBackMoney(new BigDecimal(0.00));
						
						
						/**
						 * 微公社余额支付
						 */
						if(!"".equals(balancePayMoneyStart)){
							apiOrderDetailsBalancePayResult.setPayType("449746280009");
							apiOrderDetailsBalancePayResult.setSumDueMoney(balancePayMoneyBigDecimal.toString());
							
							orderDetailsBalancePay.add(apiOrderDetailsBalancePayResult);
						}
						
						//返回微公社余额支付信息
						apiOrderDetailsResult.setOrderDetailsBalancePay(orderDetailsBalancePay);
						//返回所有商品
						apiOrderDetailsResult.setOrderSellerList(orderSellerList);
						//活动备注信息
						apiOrderDetailsResult.setOrderActivityRemarkDetailsResult(orderActivityRemarkDetailsResult); 
						//折扣信息(活动信息)，排除加价购的活动
						apiOrderDetailsResult.setOrderActivityDetailsResult(orderActivityDetailsResult);  
						List<MDataMap> ics = DbUp.upTable("oc_order_activity").queryAll("activity_code,out_active_code", "", " activity_code like 'CX%' and  activity_type!='4497472600010025'  and order_code in (select order_code from oc_orderinfo where big_order_code=:order_code)", new MDataMap("order_code",inputParam.getOrder_code()));
						if(ics!=null&&!ics.isEmpty()){
							List<String> cxCodes = new ArrayList<String>();
							for (int jj = 0; jj < ics.size(); jj++) {
							cxCodes.add(ics.get(jj).get("activity_code"));
							}
							String icTs = new PlusSupportProduct().getOrderRemind(cxCodes);
							if(StringUtility.isNotNull(icTs)){
								if("1".equals(apiOrderDetailsResult.getFlagKaola())) {
									if(new PlusSupportProduct().compareOrderRemind(cxCodes, 1200L) == 1) {
										apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后20分钟内不付款，系统将自动取消订单！");
									} else {
										apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
									}
								} else {
									apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
								}
							}
						}
					}					
					apiOrderDetailsResult.setOrder_code(sOrderCode);
					apiOrderDetailsResult.setIsShowPay("1");
					apiOrderDetailsResult.setIsShowInvoice("1");
				}								
			}
			//DD小订单
			else if("DD".equals(inputParam.getOrder_code().substring(0, 2)) || "HH".equals(inputParam.getOrder_code().substring(0, 2))){    //小订单								
				int deleteFlagCount = orderService.orderCountDeleteFlag(sOrderCode); // 判断此订单是否为以删除订单
				
				/*初始化暂存款，储备金 begin*/
				
				String amtSql = "order_code='"+sOrderCode+"' and pay_type in ('449746280006','449746280007','449746280008','449746280015','449746280019','449746280022','449746280024','449746280025')";
				
				List<MDataMap> amtDataMaps = DbUp.upTable("oc_order_pay").queryAll("pay_type,payed_money", "", amtSql,new MDataMap());
				
				BigDecimal eventOnlinePayMoney = BigDecimal.ZERO;  // 在线支付立减
				BigDecimal plusPayMoney = BigDecimal.ZERO;  // plus折扣
				BigDecimal huDongMoney = BigDecimal.ZERO;  // 互动活动优惠
				BigDecimal hjycoinMoney = BigDecimal.ZERO;  // 惠币
				for(MDataMap amtDataMap : amtDataMaps){					
					
					if(amtDataMap != null){
						
						String amt_pay_type = amtDataMap.get("pay_type");
						
						String amtStr = amtDataMap.get("payed_money");
						
						/*储备金*/
						if(StringUtils.equals(amt_pay_type, "449746280006")){						
							
							
							if(StringUtils.isNotBlank(amtStr)){
								
								BigDecimal czjAmt = new BigDecimal(amtStr);
								
								apiOrderDetailsResult.setCzjAmt(czjAmt);
								
								continue;
								
							}
							
						}
						
						/*暂存款*/
						if(StringUtils.equals(amt_pay_type, "449746280007")){						
							
							
							if(StringUtils.isNotBlank(amtStr)){
								
								BigDecimal zckAmt = new BigDecimal(amtStr);
								
								apiOrderDetailsResult.setZckAmt(zckAmt);
								
								continue;
								
							}
							
						}
						
						/*
						 * 惠豆金额
						 */
						if(StringUtils.equals(amt_pay_type, "449746280015")){						
							
							
							if(StringUtils.isNotBlank(amtStr)){
								
								BigDecimal hjybeanMoney = new BigDecimal(amtStr);
								
								apiOrderDetailsResult.setHjyBean(hjybeanMoney);
								
								continue;
								
							}
							
						}
						
						
						/*
						 * 积分金额
						 */
						if(StringUtils.equals(amt_pay_type, "449746280008")){						
							
							
							if(StringUtils.isNotBlank(amtStr)){
								
								BigDecimal integralMoney = new BigDecimal(amtStr);
								
								apiOrderDetailsResult.setIntegralMoney(integralMoney);
								
								continue;
								
							}
							
						}
						
						/*
						 * 积分金额
						 */
						if(StringUtils.equals(amt_pay_type, "449746280022")){						
							
							
							if(StringUtils.isNotBlank(amtStr)){
								
								plusPayMoney = plusPayMoney.add(new BigDecimal(amtStr));
								
								continue;
								
							}
							
						}
						
						/*
						 * 在线支付立减
						 */
						if(StringUtils.equals(amt_pay_type, "449746280019")) {
							if(StringUtils.isNotBlank(amtStr)){
								
								eventOnlinePayMoney = eventOnlinePayMoney.add(new BigDecimal(amtStr));
								
								continue;
								
							}
						}
						
						/*
						 * 互动活动优惠
						 */
						if(StringUtils.equals(amt_pay_type, "449746280024")) {
							if(StringUtils.isNotBlank(amtStr)){
								huDongMoney = huDongMoney.add(new BigDecimal(amtStr));
								continue;
							}
						}
						
						/*
						 * 惠币优惠
						 */
						if(StringUtils.equals(amt_pay_type, "449746280025")) {
							if(StringUtils.isNotBlank(amtStr)){
								hjycoinMoney = hjycoinMoney.add(new BigDecimal(amtStr));
								continue;
							}
						}
					}
					
				}
				
				// 互动活动优惠
				if(huDongMoney.compareTo(BigDecimal.ZERO) > 0){
					ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
					act.setActivityType((String)DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code","449746280024")));
					act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
					act.setPreferentialMoney(huDongMoney.toString());
					orderActivityDetailsResult.add(act);
				}
				
				// plus折扣
				if(plusPayMoney.compareTo(BigDecimal.ZERO) > 0){
					ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
					act.setActivityType(bConfig("xmassystem.plus_order_pay_name"));
					act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
					act.setPreferentialMoney(plusPayMoney.toString());
					orderActivityDetailsResult.add(act);
				}
				
				// 在线支付立减
				if(eventOnlinePayMoney.compareTo(BigDecimal.ZERO) > 0){
					ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
					act.setActivityType(bConfig("familyhas.online_pay_name"));
					act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
					act.setPreferentialMoney(eventOnlinePayMoney.toString());
					orderActivityDetailsResult.add(act);
				}
				
				// 惠币支付
				if(hjycoinMoney.compareTo(BigDecimal.ZERO) > 0) {
					ApiOrderActivityDetailsResult act = new ApiOrderActivityDetailsResult();
					act.setActivityType("惠币");
					act.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
					act.setPreferentialMoney(hjycoinMoney.toString());
					orderActivityDetailsResult.add(act);
				}
				
				/*初始化暂存款，储备金 end*/

				if (deleteFlagCount != 0) { // 不等于0 为 未删除订单
					dm.put("order_code", sOrderCode);
					
					buyerList = DbUp.upTable("oc_orderinfo")
							.dataSqlList(
									"select * from ordercenter.oc_orderinfo where order_code=:order_code or out_order_code=:order_code",
									dm);

					if (buyerList != null && !buyerList.isEmpty()) {
						
						for (int j = 0; j < buyerList.size(); j++) {
							map = buyerList.get(j);
							
							order = orderService.getOrder(String.valueOf(map.get("order_code"))); // 获取与订单相关内容的信息
							
							//判断此订单是否为海外购的订单
//							if (AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))) {
							if (AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code")) ){
								apiOrderDetailsResult.setFlagTheSea("1");
							}
							if (AppConst.MANAGE_CODE_WYKL.equals(map.get("small_seller_code")) ){
								apiOrderDetailsResult.setFlagKaola("1");
							}
							
							/*
							 *目前待付款的只有OS开头的订单，但怕以后改成DD的,在这里加上，如果DD有代付款的订单，需返回给客户端  支付方式
							 */
							/*
							if(VersionHelper.checkServerVersion("3.5.72.55") && j==0){
								String defaultPayType = new PlusSupportPay().upPayFrom(String.valueOf(map.get("big_order_code")));
								if(defaultPayType!=null && !"".equals(defaultPayType)){
									apiOrderDetailsResult.setDefault_Pay_type(defaultPayType);   //返回客户端  支付方式(默认支付宝)
								}
								
								// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
								if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
									apiOrderDetailsResult.setDefault_Pay_type("449746280003");
								}
							}
							*/
							// 直接给客户端返回实际的支付方式
							MDataMap bigOrderPayTypeMap = DbUp.upTable("oc_orderinfo_upper").oneWhere("pay_type", "", "", "big_order_code",order.getBigOrderCode());
							if(bigOrderPayTypeMap != null){
								apiOrderDetailsResult.setDefault_Pay_type(bigOrderPayTypeMap.get("pay_type"));
							}
							
							if (order != null) {
								//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(order.getOrderCode(),true); // 获取支付宝移动支付参数
								//apiOrderDetailsResult.setAlipayUrl(FamilyConfig.ali_url_http);
								//apiOrderDetailsResult.setAlipaySign(alipayValue);

								if (order.getAddress() != null && !"".equals(order.getAddress())) {
									//为兼容旧版本做相应处理
									String areaCode = order.getAddress().getAreaCode();
									int isFour = DbUp.upTable("sc_tmp").count("code", areaCode, "code_lvl", "4");
									if(isFour > 0) {//当前编码为四级编码
										//加入查询区域的代码
										String area_sql="SELECT d.`name` AS prov_name,d.`code` AS prov_code,IF (c.show_yn = 'Y', c.`name`, '') AS city_name,c.`code` AS city_code, "
												+ "IF(b.show_yn = 'Y',b.name,'') AS area_name,b.code AS area_code,IF(a.show_yn = 'Y',a.`name`,'') AS street_name,a.`code` AS street_code "
												+ "from sc_tmp a  LEFT JOIN sc_tmp b on b.`code` = a.p_code "
												+ "LEFT JOIN sc_tmp c on c.`code` = b.p_code "
												+ "left join sc_tmp d on d. code = c.p_code "
												+ "where a.`code`=:area_code";										
										Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",order.getAddress().getAreaCode()));
										prov_name = MapUtils.getString(areaMap, "prov_name", "");
										city_name = MapUtils.getString(areaMap, "city_name", "");
										area_name = MapUtils.getString(areaMap, "area_name", "");
										street_name = MapUtils.getString(areaMap, "street_name", "");
										apiOrderDetailsResult.setConsigneeAddress(prov_name + city_name + area_name + street_name + " " + order.getAddress().getAddress());
									}else {//当前编码为三级编码
										//加入查询区域的代码
										String area_sql="SELECT a.`code` AS area_code,c.`name` as prov_name,b.`name` as city_name,a.`name` AS area_name,b.`code` as city_code,c.`code` as prov_code "
												+ "from sc_tmp a  LEFT JOIN sc_tmp b on b.`code`=CONCAT(LEFT(a.`code`,4),'00') "
												+ "LEFT JOIN sc_tmp c on c.`code`=CONCAT(LEFT(a.`code`,2),'0000') "
												+ "where a.`code`=:area_code";
										Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",order.getAddress().getAreaCode()));
										prov_name=(String)areaMap.get("prov_name");
										city_name=(String)areaMap.get("city_name");
										area_name=(String)areaMap.get("area_name");
										apiOrderDetailsResult.setConsigneeAddress((prov_name.equals(city_name)?prov_name:(prov_name+city_name))+area_name+" "+order
												.getAddress().getAddress());
									}
									
									/* 这是被坑了，我也只能暂时这么做了,要骂就骂产品吧 */
									
									String idNumber = new MemberAuthInfoSupport().enIdNumber(order.getAddress().getAuthIdcardNumber());
									
									apiOrderDetailsResult.setIdNumber(idNumber);
									
									apiOrderDetailsResult.setConsigneeName(order.getAddress().getReceivePerson());
									apiOrderDetailsResult.setConsigneeTelephone(order.getAddress().getMobilephone()); // 收货人电话
									invoiceInformationResult.setInvoiceInformationTitle(order.getAddress().getInvoiceTitle());
									invoiceInformationResult.setInvoiceInformationType(order.getAddress().getInvoiceType());
									invoiceInformationResult.setInvoiceInformationValue(order.getAddress().getInvoiceContent());

									apiOrderDetailsResult.setInvoiceInformation(invoiceInformationResult);
								}
								apiOrderDetailsResult.setCreate_time(order.getCreateTime());
								
								if(VersionHelper.checkServerVersion("3.5.93.55")){
									
									if("449715200024".equals(order.getOrderType())) {
										BigDecimal allMoney = order.getProductMoney();
										for(OrderDetail orderDetail : order.getProductList()) {
											allMoney = allMoney.subtract(orderDetail.getIntegralMoney());
										}
										apiOrderDetailsResult.setDue_money(allMoney);
									}else {
										apiOrderDetailsResult.setDue_money(order.getProductMoney());
									}
								}else{
									if("449715200024".equals(order.getOrderType())) {
										BigDecimal allMoney = MoneyFormatUtil.moneyFormat(order.getOrderMoney().toString());
										for(OrderDetail orderDetail : order.getProductList()) {
											allMoney = allMoney.subtract(orderDetail.getIntegralMoney());
										}
										apiOrderDetailsResult.setDue_money(allMoney);
									}else {
										apiOrderDetailsResult.setDue_money(MoneyFormatUtil.moneyFormat(order.getOrderMoney().toString()));
									}
								}
								
								//积分兑换
								if("449715200024".equals(order.getOrderType())) {
									for(OrderDetail orderDetail : order.getProductList()) {
										BigDecimal integralMoneyTmp = orderDetail.getIntegralMoney();
										BigDecimal avgIntegralMoney = integralMoneyTmp.divide(new BigDecimal(orderDetail.getSkuNum()), BigDecimal.ROUND_HALF_UP);
										
										//商品单价 = 商品售价(sku_price) - (integral_money / sku_num);
										apiOrderDetailsResult.setProductMoney(apiOrderDetailsResult.getProductMoney().add(orderDetail.getSkuPrice().subtract(avgIntegralMoney).
												multiply(new BigDecimal(orderDetail.getSkuNum()))));
										apiOrderDetailsResult.setProductIntegral(apiOrderDetailsResult.getProductIntegral().add(integralMoneyTmp.multiply(new BigDecimal(200))));
										apiOrderDetailsResult.setOrderType(order.getOrderType());
									}
								}else {
									apiOrderDetailsResult.setProductMoney(order.getProductMoney());	
								}
								
								apiOrderDetailsResult.setFirstFavorable(""); // 不知道怎么获取值(收单优惠)
								
								/*
								 * transport_template(运费) 、validate_flag(是否是虚拟商品  Y：是  N：否)
								 * 查询有运费  并且  不是虚拟商品的   商品信息
								 */
								String sql = "select uid from pc_productinfo where product_code in " +
										"(select product_code from ordercenter.oc_orderdetail where order_code=:order_code) " +
										"and LENGTH(transport_template)=32 and validate_flag='N'";
								
								MDataMap productinfoMDataMap = new MDataMap();
								productinfoMDataMap.put("order_code", order.getOrderCode());
								
								Map<String, Object> productinfoMap = DbUp.upTable("pc_productinfo").dataSqlOne(sql, productinfoMDataMap);
								/*
								 * 如果此订单有收货人地址并且  地址是   新疆（65） 或 西藏(54)  运费返回 24.00
								 */
								if(VersionHelper.checkServerVersion("3.5.93.55")){
									
									apiOrderDetailsResult.setFreight(order.getTransportMoney().doubleValue());
									
								}else if(VersionHelper.checkServerVersion("3.5.22.51") && 
										productinfoMap!=null && !"".equals(productinfoMap) && productinfoMap.size()>0 &&
										("65".equals(area_code_address.substring(0, 2)) || "54".equals(area_code_address.substring(0, 2)))
										){
									
									apiOrderDetailsResult.setFreight(24.00);
								}else{
									apiOrderDetailsResult.setFreight(order.getTransportMoney().doubleValue());
								}
								apiOrderDetailsResult.setFullSubtraction(0.00); // 满减目前是"0"
								apiOrderDetailsResult.setTelephoneSubtraction(0.00); // 手机下单减少目前为"0"

								apiOrderDetailsResult.setOrder_code(order.getOrderCode());
								apiOrderDetailsResult.setOrder_money(order.getDueMoney().toString());
								apiOrderDetailsResult.setOrder_status(order.getOrderStatus());
								apiOrderDetailsResult.setPay_type(order.getPayType()); // 支付方式
								
								//此表示用于判断是否是快精通的订单，false为否，true为是
								boolean flagKJT = false;
								List<ApiOrderSellerDetailsResult> orderSellerList = new ArrayList<ApiOrderSellerDetailsResult>();
								
								List<ApiOrderKjtParcelResult> apiOrderKjtParcelList = new ArrayList<ApiOrderKjtParcelResult>();
								/**
								 * 判断商品是否属于快境通商品(快精通商品涉及到包裹)
								 * 
								 * 
								 */
								
//								if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
								if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
									flagKJT =true;
									
									apiOrderDetailsResult.setIsSeparateOrder("0");   //0为是分包的商品
									
								}
								/*跨境通商品通关状态*/
								String sostatus = "";
								if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
									
									apiOrderDetailsResult.setTariffMoney("0.00"); //关税
									
									//查询快精通商品信息(总共有几个包裹)
									List<Map<String,Object>> orderKjtList = DbUp.upTable("oc_order_kjt_list").dataSqlList("select * from oc_order_kjt_list where order_code=:order_code", new MDataMap("order_code",sOrderCode));
									for(Map<String,Object> orderKjt : orderKjtList){
//										ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();
										ApiOrderKjtParcelResult apiOrderKjtParcelResult = new ApiOrderKjtParcelResult();
										String order_code_seq = String.valueOf(orderKjt.get("order_code_seq"));
										apiOrderKjtParcelResult.setLocalStatus(String.valueOf(orderKjt.get("local_status")));
										
										sostatus = orderKjt.get("sostatus")==null?"":String.valueOf(orderKjt.get("sostatus"));
										
										ApiOrderKjtDetailsResult apiOrderKjtDetailsResult = null;
										List<ApiOrderKjtDetailsResult> apiOrderKjtDetailsList = new ArrayList<ApiOrderKjtDetailsResult>();
										ProductLabelService productLabelService = new ProductLabelService();
										//查询每一个包裹内的具体信息
										List<Map<String,Object>> orderKjtDetailList = DbUp.upTable("oc_order_kjt_detail").dataSqlList("select * from oc_order_kjt_detail where order_code_seq=:order_code_seq", new MDataMap("order_code_seq",order_code_seq));
										for(Map<String,Object> orderKjtDetail :  orderKjtDetailList){
											
//											for (OrderDetail orderDetail : order.getProductList()) {
												list = orderService.flashSales(String.valueOf(orderKjtDetail.get("sku_code"))); // 存在为闪购信息
												sellerList = orderService.sellerInformation(String.valueOf(orderKjtDetail.get("sku_code"))); // 查询商品信息
												
												if (sellerList != null && !sellerList.isEmpty()) {

													for (int i = 0; i < sellerList.size(); i++) {
														apiOrderKjtDetailsResult = new ApiOrderKjtDetailsResult();
//														ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();
														List<ApiOrderDonationDetailsResult> apiOrderDonationDetailsResultList = new ArrayList<ApiOrderDonationDetailsResult>();
														List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();

														mapSeller = sellerList.get(i);
														if (!"null".equals(String.valueOf(mapSeller.get("sku_picurl")))) {
															apiOrderKjtDetailsResult.setMainpicUrlKJT(mapSeller.get("sku_picurl").toString());
														}
														apiOrderKjtDetailsResult.setNumberKJT(String.valueOf(orderKjtDetail.get("sku_num"))); 
//														apiOrderSellerDetailsResult.setNumber(String.valueOf(orderDetail.getSkuNum()));
														
														
														/**
														 * 商品价格
														 */
//														BigDecimal sell_price = new BigDecimal(String.valueOf(orderDetail.getSkuPrice())).add(new BigDecimal(String.valueOf(orderDetail.getGroupPrice())).add(new BigDecimal(String.valueOf(orderDetail.getCouponPrice()))));
														Map<String,Object> orderDetailMap = DbUp.upTable("oc_orderdetail").dataSqlOne("select * from oc_orderdetail where sku_code=:sku_code and order_code=:order_code", new MDataMap("sku_code",String.valueOf(orderKjtDetail.get("sku_code")),"order_code",String.valueOf(orderKjtDetail.get("order_code"))));
														if(orderDetailMap!=null && !"".equals(orderDetailMap) && orderDetailMap.size()>0){
															apiOrderKjtDetailsResult.setPriceKJT(orderDetailMap.get("show_price").toString());
														}
														apiOrderKjtDetailsResult.setProductCodeKJT(String.valueOf(mapSeller.get("product_code")));
														//把商品编号单独存入一个list中  用于获取预计返利金额
														productCodeList.add(String.valueOf(mapSeller.get("product_code")));
														
														apiOrderKjtDetailsResult.setProductNameKJT(String.valueOf(mapSeller.get("sku_name")));
//														
														/*是否生鲜*/
														PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(apiOrderKjtDetailsResult.getProductCodeKJT());
														
														List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
														
														apiOrderKjtDetailsResult.setLabelsList(labelsList);
														apiOrderKjtDetailsResult.setLabelsPic(productLabelService.getLabelInfo(String.valueOf(mapSeller.get("product_code"))).getListPic());
														standardAndStyleList = orderService.sellerStandardAndStyle(mapSeller.get("sku_keyvalue").toString()); // 截取 尺码和款型
														
														if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
															for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
																ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
																apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
																apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
																apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
															}
															apiOrderKjtDetailsResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
														}

//														apiOrderSellerDetailsResult.setDetailsList(apiOrderDonationDetailsResultList); // 赠品信息(目前为空)
													}
													
													
														
														if(!StringUtils.equals((String)map.get("order_status"), FamilyConfig.ORDER_STATUS_UNPAY)){
															
															if(StringUtils.equals(sostatus, FamilyConfig.KJT_CUSTOMS_STATUS_NO)){
																
																apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421188));
																
																apiOrderKjtDetailsResult.setNoPassCustom(FamilyConfig.CUSTOMS_STATUS_FAILURE);
																
															}
														}
													
													apiOrderKjtDetailsList.add(apiOrderKjtDetailsResult);
//													apiOrderDetailsResult.setOrderSellerList(orderSellerList);
												}
//											}
										}
										
										apiOrderKjtParcelResult.setApiOrderKjtDetailsList(apiOrderKjtDetailsList);
										
										apiOrderKjtParcelList.add(apiOrderKjtParcelResult);
										
									}
									apiOrderDetailsResult.setApiOrderKjtParcelResult(apiOrderKjtParcelList);
								}
								
								/*麦乐购商品*/
								if(AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))){
									
									new OrderDetailService().initMlgProductList(apiOrderDetailsResult, productCodeList, (String)map.get("order_status"));
									
								}
								
								
								/**
								 *   此循环里的代码，是为了兼容以前的老版本，无论是否为KJT的订单都要放入原有不分包裹的商品列表里
								 */
								//======================
								ProductLabelService productLabelService = new ProductLabelService();
								for (OrderDetail orderDetail : order.getProductList()) {
									/*
									 * 只显示非赠品商品
									 */
									if("1".equals(orderDetail.getGiftFlag())){
										list = orderService.flashSales(orderDetail.getSkuCode()); // 存在为闪购信息
										sellerList = orderService.sellerInformation(orderDetail.getSkuCode()); // 查询商品信息
										if (sellerList != null && !sellerList.isEmpty()) {

											for (int i = 0; i < sellerList.size(); i++) {
												ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();
												List<ApiOrderDonationDetailsResult> apiOrderDonationDetailsResultList = new ArrayList<ApiOrderDonationDetailsResult>();
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();

												mapSeller = sellerList.get(i);
												apiOrderSellerDetailsResult.setSmallSellerCode(mapSeller.get("small_seller_code")+"");
												if (!"null".equals(String.valueOf(mapSeller.get("sku_picurl")))) {
													apiOrderSellerDetailsResult.setMainpicUrl(mapSeller.get("sku_picurl").toString());
												}
												
												apiOrderSellerDetailsResult.setNumber(String.valueOf(orderDetail.getSkuNum()));
												
												MDataMap orderInfo1 = DbUp.upTable("oc_orderinfo").one("order_code", orderDetail.getOrderCode());
												//获取跨境商户Code
//												List<String> kuajingShopList = getKJTShopList();
												
												//if (StringUtils.startsWith(orderInfo1.get("small_seller_code"), "SF031"))
//												  if(!kuajingShopList.contains((orderInfo1.get("small_seller_code")))){
//												if(!new PlusServiceSeller().isKJSeller(orderInfo1.get("small_seller_code"))){
												/**
												 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
												 */
												String uc_seller_type = WebHelper.getSellerType(orderInfo1.get("small_seller_code"));
												String small_seller_code = orderInfo1.get("small_seller_code");
												//如果uc_seller_type = ""，目前是惠家有商户，5.2.8需求需要放开在线申请售后功能。
												if(StringUtils.isEmpty(uc_seller_type)&&"SI2003".equals(small_seller_code)){
													PlusSupportLD ld = new PlusSupportLD();
													String isSyncLd = ld.upSyncLdOrder();
													if("Y".equals(isSyncLd)){
														uc_seller_type = "4497478100050001";
													}
													if(AppVersionUtils.compareTo("5.2.7", appVersion)>=0){//版本低于527
														uc_seller_type = "";
													}
												}
												//4497478100050004 = 平台入驻商户、 4497478100050001 = 普通商户、4497478100050005 = 缤纷商户
												if(StringUtils.equals(uc_seller_type, "4497478100050001") || StringUtils.equals(uc_seller_type, "4497478100050005") || StringUtils.equals(uc_seller_type, "4497478100050004")){
													MDataMap orderInfo=DbUp.upTable("oc_orderdetail").one("order_code",orderDetail.getOrderCode(),"sku_code",orderDetail.getSkuCode());
													String flag_asale=orderInfo.get("flag_asale");
													String asale_code=orderInfo.get("asale_code");
													//校验是否显示售后按钮方法
													boolean flagAllow = new AfterSaleService().checkIfAllowAfterSale(orderDetail.getOrderCode(), orderDetail.getSkuCode(), "0");
													if("SI2003".equals(small_seller_code)){
														if(flagAllow){
															//售后按钮
															apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008")));//售后
														}
													}else{ 
														if(flagAllow){
															//售后按钮
															apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008")));//售后
														}else{
															//
															if(StringUtils.isNotBlank(asale_code)){
																MDataMap afterSaleInfo=DbUp.upTable("oc_order_after_sale").one("asale_code",asale_code);
																String asale_status=afterSaleInfo.get("asale_status");
																apiOrderSellerDetailsResult.setAfterCode(asale_code);
																
	//															apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080011",(String)DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",asale_status))));
	//															apiOrderSellerDetailsResult.setAfterSaleStatusCode(asale_status);
	//															apiOrderSellerDetailsResult.setAfterSaleStatusName((String)DbUp.upTable("oc_order_after_sale").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code",asale_status)));
															}
														}
													}
												}
												
												
												/**
												 * 商品价格
												 */
												//积分兑换
												String orderType = MapUtils.getString(map, "order_type", "");
												BigDecimal show_price = orderDetail.getShowPrice();
												if(show_price.compareTo(BigDecimal.ZERO) == 0) {
													show_price = orderDetail.getSkuPrice();
												}
												if("449715200024".equals(orderType)) {
													int skuNum = orderDetail.getSkuNum();
													BigDecimal integralMoneyTmp = orderDetail.getIntegralMoney().divide(new BigDecimal(skuNum), BigDecimal.ROUND_HALF_UP);
													
													//商品单价 = 商品售价(sku_price) - (integral_money / sku_num);
													apiOrderSellerDetailsResult.setIntegral(integralMoneyTmp.multiply(new BigDecimal(200)).toString());
													apiOrderSellerDetailsResult.setPrice(show_price.subtract(integralMoneyTmp).doubleValue());
												}else {
													apiOrderSellerDetailsResult.setPrice(show_price.doubleValue());
												}
												
												apiOrderSellerDetailsResult.setDeal_price(orderDetail.getSkuPrice().doubleValue());
												apiOrderSellerDetailsResult.setProductCode(String.valueOf(mapSeller.get("product_code")));
												apiOrderSellerDetailsResult.setSkutCode(orderDetail.getSkuCode());
												

												//添加图片
												apiOrderSellerDetailsResult.setLabelsPic(productLabelService.getLabelInfo(String.valueOf(mapSeller.get("product_code"))).getListPic());
												//把商品编号单独存入一个list中  用于获取预计返利金额
												productCodeList.add(String.valueOf(mapSeller.get("product_code")));
												
												apiOrderSellerDetailsResult.setProductName(orderDetail.getSkuName());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(apiOrderSellerDetailsResult.getProductCode());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiOrderSellerDetailsResult.setLabelsList(labelsList);
												
												apiOrderSellerDetailsResult.setRegion(orderDetail.getStoreCode());

												standardAndStyleList = orderService.sellerStandardAndStyle(mapSeller.get("sku_keyvalue").toString()); // 截取 尺码和款型
												
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiOrderSellerDetailsResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}
												
												/*通关状态*/
												
													
													if(!StringUtils.equals((String)map.get("order_status"), FamilyConfig.ORDER_STATUS_UNPAY)){
														
														if(StringUtils.equals(apiOrderDetailsResult.getFlagTheSea(), FamilyConfig.OVER_SEAS_FLAG_YES)){
															
															if(StringUtils.equals(sostatus, FamilyConfig.KJT_CUSTOMS_STATUS_NO)){
																
																apiOrderSellerDetailsResult.setNoPassCustom(FamilyConfig.CUSTOMS_STATUS_FAILURE);
																
															}
															
															
														}
														
													}
											  if(flagKJT){apiOrderSellerDetailsResult.setFlagTheSea("1");}else{apiOrderSellerDetailsResult.setFlagTheSea("0");};
												orderSellerList.add(apiOrderSellerDetailsResult);	

												apiOrderSellerDetailsResult.setDetailsList(apiOrderDonationDetailsResultList); // 赠品信息(目前为空)
											}
											apiOrderDetailsResult.setOrderSellerList(orderSellerList);
										}

										// 只对未发货或已发货状态下的订单展示
										if(!showTipsForZhiYou){
											if(("4497153900010002".equals(order.getOrderStatus()) || "4497153900010003".equals(order.getOrderStatus()))){
												PlusModelSkuQuery sq = new PlusModelSkuQuery();
												sq.setCode(orderDetail.getProductCode());
												plusModelSkuInfoSpread = loadSkuInfoSpread.upInfoByCode(sq);
												
												// 是否展示直邮商品温馨提示判断
												showTipsForZhiYou = "1".equals(plusModelSkuInfoSpread.getProductTradeType());
											}
										}
									}
								}									

								String remark = null;  //此字段用于判断所有活动号都不满足是  只显示一条活动批注(remark)
								ApiOrderActivityRemarkDetailsResult apiOrderActivityRemarkDetailsResult = null;
								List<OcOrderActivity> ocOrderActivities = order.getActivityList();
								
								
								if (ocOrderActivities != null && !ocOrderActivities.isEmpty()) {
									for (int i = 0; i < ocOrderActivities.size(); i++) {
										apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
										/*
										 * 折扣(活动信息)
										 */
										if(ocOrderActivities.get(i).getActivityCode()!=null && !"".equals(ocOrderActivities.get(i).getActivityCode()) &&
												ocOrderActivities.get(i).getOrderCode()!=null && !"".equals(ocOrderActivities.get(i).getOrderCode())){
												
											    ApiOrderActivityDetailsResult apiOrderActivityDetailsResult = null;
											   
												/*
												 *活动备注(oc_order_activity表中"activity_code为TA : 代表使用商品的活动；SG：为闪够商品的活动；1002317：为首单88折") 
												 */
												if("TA".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))
														|| "449715400006".equals(String.valueOf(ocOrderActivities.get(i).getActivityType()))){  //查询TA(代表使用商品的活动)的活动备注
													
													 // 立减30  都需显示活动名称(已在属于闪够 和 立减30的判断中赋值了)，  其他显示活动编号
													apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
													if("449715400006".equals(String.valueOf(ocOrderActivities.get(i).getActivityType()))){
														if(new BigDecimal(bConfig("familyhas.fullSubActivityMoney")).compareTo(new BigDecimal(ocOrderActivities.get(i).getPreferentialMoney()))==0){
															apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.fullSubActivityName"));
														}else {
															apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.eveActivityName"));
														}
													}else {
														apiOrderActivityDetailsResult.setActivityType(ocOrderActivities.get(i).getActivityType()); 
													}
													apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
													apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
													apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
													
													/*
													 * 此活动备注显示  新疆、西藏运费24.00等等
													 */
													if(remark == null){
														apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));	
														if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
														remark = "remarkValueExist";
													}
													
													//查询试用商品(目前没用到，年前上线屏蔽有可能会有问题，目前先不屏蔽)
													List<Map<String, Object>> tryOutProductsList = tryoutProductsService.getTryoutProducts(String.valueOf(order.getCreateTime()), 
															String.valueOf(ocOrderActivities.get(i).getSkuCode()), 
															String.valueOf(ocOrderActivities.get(i).getActivityCode()));   
													if(tryOutProductsList!=null && !"".equals(tryOutProductsList) && !tryOutProductsList.isEmpty()){
														for(Map<String, Object> mapTryOut : tryOutProductsList){
															apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
															apiOrderActivityRemarkDetailsResult.setRemark(String.valueOf(mapTryOut.get("notice")));
														}
													}
													
												}else if("SG".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))){  //查询SG(为闪够商品的活动)的活动备注
													FlashsalesService flashsalesService = new FlashsalesService();
													Map<String, Object> mapFlashsales = flashsalesService.getActivityFlashsales(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
													
													if(mapFlashsales!=null && !"".equals(mapFlashsales) && mapFlashsales.size()>0){
//														apiOrderActivityDetailsResult.setActivityType(String.valueOf(mapFlashsales.get("activity_name")));   //闪够是显示闪够的活动名称
//														apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
//														apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
//														apiOrderActivityRemarkDetailsResult.setRemark(String.valueOf(mapFlashsales.get("remark")));
														
													}
													/*
													 * 此活动备注显示  新疆、西藏运费24.00等等
													 */
													if(remark == null){
														apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));	
														if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
														remark = "remarkValueExist";
													}
													
												}else if("1002317".equals(ocOrderActivities.get(i).getActivityCode())){   //查询1002317(为首单88折)的活动备注
													//apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.firstActivityRemark"));
												}else if("1003037".equals(ocOrderActivities.get(i).getActivityCode())){    //立减30元
													 // 立减30  都需显示活动名称(已在属于闪够 和 立减30的判断中赋值了)，  其他显示活动编号
													apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
													apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.eveActivityName"));    //立减30元 活动类型需要显示成  活动名称
													apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
													apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
													apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
													
													//apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
													apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.eveActivityRemark"));
												}else if ("1003358".equals(ocOrderActivities.get(i).getActivityCode())) {//满399减50
													apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
													apiOrderActivityDetailsResult.setActivityType(bConfig("familyhas.fullSubActivityName"));    //立减30元 活动类型需要显示成  活动名称
													apiOrderActivityDetailsResult.setActivityCode(String.valueOf(ocOrderActivities.get(i).getActivityCode()));
													apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
													apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
												}else if(VersionHelper.checkServerVersion("3.5.22.51") && remark == null){   //所有活动都不满足显示： （新疆、西藏地区每单收24元运费）
													//apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
													apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));
													if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
													remark = "remarkValueExist";
												}
												
												/**
												 * 由于闪够不需要放备注，所以判断下是否为空，否则返回前台会是null
												 */
												if(apiOrderActivityRemarkDetailsResult!=null && !"".equals(apiOrderActivityRemarkDetailsResult) && 
														apiOrderActivityRemarkDetailsResult.getRemark()!=null){
													orderActivityRemarkDetailsResult.add(apiOrderActivityRemarkDetailsResult);
												}
												if(apiOrderActivityDetailsResult!=null && !"".equals(apiOrderActivityDetailsResult)){
													orderActivityDetailsResult.add(apiOrderActivityDetailsResult);
												}
												
										}
										
										/*
										 * 首单88折
										 */
										if (bConfig("familyhas.firstActivity").equals(ocOrderActivities.get(i).getActivityCode())) {  
											
											//apiOrderDetailsResult.setFirstFavorable(String.valueOf(ocOrderActivities.get(i).getPreferentialMoney()));
											//apiOrderDetailsResult.setFirstFavorable(orderService.discountSku(sOrderCode));
											
										} else {
											//下单成功 未付款 且  是闪够活动的   ，提示15分钟后失效！
											if ("4497153900010001".equals(order.getOrderStatus())&& (!"".equals(ocOrderActivities.get(i).getActivityCode()) || !""
															.equals(ocOrderActivities.get(i).getActivityName())) && 
															"SG".equals(String.valueOf(ocOrderActivities.get(i).getActivityCode()).substring(0, 2))) {
												apiOrderDetailsResult
														.setIfFlashSales("0");
												apiOrderDetailsResult
														.setFailureTimeReminder("提示:下单成功后15分钟内不付款，系统将自动取消订单！");
											}
										}
									}
								}else if(VersionHelper.checkServerVersion("3.5.22.51")){ //此订单没有活动时显示： （新疆、西藏地区每单收24元运费）
									apiOrderActivityRemarkDetailsResult = new ApiOrderActivityRemarkDetailsResult();
									apiOrderActivityRemarkDetailsResult.setRemark(bConfig("familyhas.fullSubActivityOtherRemark"));
									if(getManageCode().equals(MemberConst.MANAGE_CODE_SPDOG)){apiOrderActivityRemarkDetailsResult.setRemark(bConfig("sharpei.fullSubActivityOtherRemark"));	}
									orderActivityRemarkDetailsResult.add(apiOrderActivityRemarkDetailsResult);
									
								}
								
								
								//优惠劵       pay_type = '449746280002'类型为优惠券
								List<Map<String, Object>> orderPayList = DbUp.upTable("oc_order_pay").dataSqlList("select p.pay_sequenceid,p.pay_type,c.coupon_code,a.activity_code,a.activity_name,c.initial_money,p.payed_money " +
										"FROM oc_order_pay p LEFT JOIN oc_coupon_info c on p.pay_sequenceid = c.coupon_code " +
										"LEFT JOIN oc_activity a on c.activity_code = a.activity_code  " +
										"where  p.order_code =:order_code and p.pay_type in ('449746280002','449746280021','449746280023')", new MDataMap("order_code",sOrderCode));
								
								for(Map<String, Object> orderPayMap : orderPayList){
									ApiOrderActivityDetailsResult apiOrderActivityDetailsResult = new ApiOrderActivityDetailsResult();
									String activityTypeTemp = "";
									if("449746280021".equals(String.valueOf(orderPayMap.get("pay_type")))) {
										activityTypeTemp = bConfig("xmasorder.redeem_name");
									}else if("449746280023".equals(String.valueOf(orderPayMap.get("pay_type")))){
										activityTypeTemp = bConfig("xmasorder.farm_name");
									}else {
										activityTypeTemp = String.valueOf(orderPayMap.get("activity_name"));
									}
									apiOrderActivityDetailsResult.setActivityType(activityTypeTemp);
									apiOrderActivityDetailsResult.setActivityCode(String.valueOf(orderPayMap.get("activity_code")));
									apiOrderActivityDetailsResult.setPlusOrMinus(0);   //目前默认都是“0 : 减钱”
									apiOrderActivityDetailsResult.setPreferentialMoney(String.valueOf(orderPayMap.get("payed_money")));
									
									if(apiOrderActivityDetailsResult!=null && !"".equals(apiOrderActivityDetailsResult)){
										boolean isIn = false;
										for(ApiOrderActivityDetailsResult oc : orderActivityDetailsResult) {
											if(oc.getActivityCode().equals(apiOrderActivityDetailsResult.getActivityCode())) {
												BigDecimal bd1 = new BigDecimal(oc.getPreferentialMoney());
												BigDecimal bd2 = new BigDecimal(apiOrderActivityDetailsResult.getPreferentialMoney());
												oc.setPreferentialMoney(bd1.add(bd2).setScale(2).toString());
												isIn = true;
												break;
											}
										}
										if(!isIn) {
											orderActivityDetailsResult.add(apiOrderActivityDetailsResult);
										}
									}
								}
								
								
								apiOrderDetailsResult.setOrderActivityDetailsResult(orderActivityDetailsResult);  //折扣信息(活动信息)
								apiOrderDetailsResult.setOrderActivityRemarkDetailsResult(orderActivityRemarkDetailsResult);  //活动备注信息
								
								
								//促销系统商品订单自动取消订单提示
								if("4497153900010001".equals(order.getOrderStatus())&&order.getActivityList()!=null&&!order.getActivityList().isEmpty()
										&&StringUtility.isNull(apiOrderDetailsResult.getFailureTimeReminder())){

									List<MDataMap> ics = DbUp.upTable("oc_order_activity").queryAll("activity_code,out_active_code", "", " activity_code like 'CX%' and order_code=:order_code", new MDataMap("order_code",order.getOrderCode()));
									if(ics!=null&&!ics.isEmpty()){
										List<String> cxCodes = new ArrayList<String>();
										for (int jj = 0; jj < ics.size(); jj++) {
											cxCodes.add(ics.get(jj).get("activity_code"));
										}
										String icTs = new PlusSupportProduct().getOrderRemind(cxCodes);
										if(StringUtility.isNotNull(icTs)){
											if("1".equals(apiOrderDetailsResult.getFlagKaola())) {
												if(new PlusSupportProduct().compareOrderRemind(cxCodes, 1200L) == 1) {
													apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后20分钟内不付款，系统将自动取消订单！");
												} else {
													apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
												}
											} else {
												apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421258, icTs));
											}
										}
									}
								}
								
								if ("4497153900010001".equals(order.getOrderStatus())
										&& (apiOrderDetailsResult.getFailureTimeReminder() == null || "".equals(apiOrderDetailsResult.getFailureTimeReminder()))) {
									apiOrderDetailsResult.setIfFlashSales("1");
									if("1".equals(apiOrderDetailsResult.getFlagKaola())) {
										apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后20分钟内不付款，系统将自动取消订单！");
									} else {
										apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后24小时内不付款，系统将自动取消订单！");
									}
								}
															
								if(DbUp.upTable("oc_express_detail").count("order_code", inputParam.getOrder_code())==0){
									//查询订单轨迹
									MDataMap dateMap = new MDataMap();
									dateMap.put("order_code", order.getOrderCode());
									List<MDataMap> trackAll = DbUp.upTable("oc_order_tracking").queryAll("*", "-outgo_time", " order_code = :order_code", dateMap);
									
									if(trackAll!=null && !"".equals(trackAll) && trackAll.size() > 0) {
										MDataMap mDataMap = trackAll.get(0);
										apiOrderDetailsResult.setYc_express_num(mDataMap.get("yc_express_num"));
										apiOrderDetailsResult.setYc_delivergoods_user_name(mDataMap.get("yc_delivergoods_user_name"));
										
										for (MDataMap mDataMapTrack : trackAll) {
											ApiHomeOrderTrackingListResult trackingListResult = new ApiHomeOrderTrackingListResult();
											
											trackingListResult.setOrderTrackContent(mDataMapTrack.get("outgo_no"));
											trackingListResult.setOrderTrackTime(mDataMapTrack.get("outgo_time"));
											trackingListResult.setYc_dis_time(mDataMapTrack.get("yc_dis_time"));
											trackingListResult.setYc_update_time(mDataMapTrack.get("yc_update_time"));
											
											apiOrderDetailsResult.getApiHomeOrderTrackingListResult().add(trackingListResult);
										}
									}
									
								}else {
									
									MDataMap md = new MDataMap();
									md.put("order_code", inputParam.getOrder_code());
									List<MDataMap> lm = DbUp.upTable("oc_express_detail").queryAll("", "time desc", "", md); // 查询订单跟踪信息表
									
									
									if(lm!=null && !"".equals(lm) && lm.size()>0){
										List<ApiHomeOrderTrackingListResult> aor = new ArrayList<ApiHomeOrderTrackingListResult>();
										for(MDataMap trackingMap : lm){
											ApiHomeOrderTrackingListResult apiHomeOrderTrackingListResult = new ApiHomeOrderTrackingListResult();
											apiHomeOrderTrackingListResult.setOrderTrackContent(trackingMap.get("context"));
											apiHomeOrderTrackingListResult.setOrderTrackTime(trackingMap.get("time"));
											apiHomeOrderTrackingListResult.setYc_dis_time(trackingMap.get("time"));
											apiHomeOrderTrackingListResult.setYc_update_time(trackingMap.get("time"));
											aor.add(apiHomeOrderTrackingListResult);
										}
										apiOrderDetailsResult.setApiHomeOrderTrackingListResult(aor);
									}
								}
								
								/**
								 * 此段代码目的：
								 * 由于跨径通涉及到包裹，但预返利的金额是针对于商品来说的，所以不能通过包裹去获取预返利所需相关信息，
								 * 所以在此加入此段代码
								 * 
								 */
								List<ApiOrderSellerDetailsResult> apiOrderSellerDetailsResultNewList = new ArrayList<ApiOrderSellerDetailsResult>();
								if(flagKJT){
									for (OrderDetail orderDetail : order.getProductList()) {
//										list = orderService.flashSales(orderDetail.getSkuCode()); // 存在为闪购信息
										sellerList = orderService.sellerInformation(orderDetail.getSkuCode()); // 查询商品信息
											/*
											 * 只显示非赠品商品
											 */
											if("1".equals(orderDetail.getGiftFlag())){
												if (sellerList != null && !sellerList.isEmpty()) {

													for (int i = 0; i < sellerList.size(); i++) {
														ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();

														mapSeller = sellerList.get(i);
														
														apiOrderSellerDetailsResult.setNumber(String.valueOf(orderDetail.getSkuNum()));
														
														/**
														 * 商品价格
														 */
														apiOrderSellerDetailsResult.setDeal_price(orderDetail.getSkuPrice().doubleValue());
														
														//积分兑换
														String orderType = MapUtils.getString(map, "order_type", "");
														BigDecimal show_price = orderDetail.getShowPrice();
														if(show_price.compareTo(BigDecimal.ZERO) == 0) {
															show_price = orderDetail.getSkuPrice();
														}
														if("449715200024".equals(orderType)) {
															int skuNum = orderDetail.getSkuNum();
															BigDecimal integralMoneyTmp = orderDetail.getIntegralMoney().divide(new BigDecimal(skuNum), BigDecimal.ROUND_HALF_UP);
															
															//商品单价 = 商品售价(sku_price) - (integral_money / sku_num);
															apiOrderSellerDetailsResult.setIntegral(integralMoneyTmp.multiply(new BigDecimal(200)).toString());
															apiOrderSellerDetailsResult.setPrice(show_price.subtract(integralMoneyTmp).doubleValue());
														}else {
															apiOrderSellerDetailsResult.setPrice(show_price.doubleValue());
														}
														
														apiOrderSellerDetailsResult.setProductCode(String.valueOf(mapSeller.get("product_code")));
														apiOrderSellerDetailsResult.setSkutCode(orderDetail.getSkuCode());
														
														apiOrderSellerDetailsResult.setLabelsPic(productLabelService.getLabelInfo(String.valueOf(mapSeller.get("product_code"))).getListPic());
														//把商品编号单独存入一个list中  用于获取预计返利金额
														productCodeListNew.add(String.valueOf(mapSeller.get("product_code")));
														
														apiOrderSellerDetailsResult.setProductName(orderDetail.getSkuName());
														apiOrderSellerDetailsResult.setRegion(orderDetail.getStoreCode());
														
														apiOrderSellerDetailsResultNewList.add(apiOrderSellerDetailsResult);

													}
													//apiOrderDetailsResult.setOrderSellerList(orderSellerList);
												}
											}
									}
									BigDecimal cashBack = new BigDecimal(0.00);
									//惠家有新版获取返现折扣
									Map<String, BigDecimal> qfBlMap = (new ShopCartService()).getScaleReckonMap(inputParam.getBuyer_code(), productCodeListNew, "SI2003");
									if(qfBlMap!=null&&!qfBlMap.isEmpty()){
										for(ApiOrderSellerDetailsResult orderSeller : apiOrderSellerDetailsResultNewList){
											//把所有商品的返现折扣相加
											cashBack = cashBack.add(qfBlMap.get(orderSeller.getProductCode()).multiply(new BigDecimal(orderSeller.getNumber())).multiply(new BigDecimal(orderSeller.getDeal_price())));
										}
										//获取预返利信息
										apiOrderDetailsResult.setCashBackMoney(cashBack);
									}
								}else{
									BigDecimal cashBack = new BigDecimal(0.00);
									//惠家有新版获取返现折扣
									Map<String, BigDecimal> qfBlMap = (new ShopCartService()).getScaleReckonMap(inputParam.getBuyer_code(), productCodeList, "SI2003");
									if(qfBlMap!=null&&!qfBlMap.isEmpty()){
										for(ApiOrderSellerDetailsResult orderSeller : orderSellerList){
											//把所有商品的返现折扣相加
											cashBack = cashBack.add(qfBlMap.get(orderSeller.getProductCode()).multiply(new BigDecimal(orderSeller.getNumber())).multiply(new BigDecimal(orderSeller.getDeal_price())));
										}
										//获取预返利信息
										apiOrderDetailsResult.setCashBackMoney(cashBack);
									}
								}
							}
							
							/**
							 * 获取微公社余额支付金额
							 */
//							mapOrderPay = DbUp.upTable("oc_order_pay").dataSqlOne("select * from oc_order_pay where order_code=:order_code and pay_type=:pay_type", 
//							new MDataMap("order_code",String.valueOf(mapOrder.get("order_code")),"pay_type","449746280009"));
							mapOrderPay = DbUp.upTable("oc_order_pay").
									dataSqlList("select * from oc_order_pay where order_code=:order_code and pay_type in ('449746280009','449746280012')", 
											new MDataMap("order_code",String.valueOf(inputParam.getOrder_code())));
							if(mapOrderPay!=null && !"".equals(mapOrderPay) && mapOrderPay.size()>0){
								for (int i = 0; i < mapOrderPay.size(); i++) {
									if(mapOrderPay.get(i)!=null&&"449746280009".equals(mapOrderPay.get(i).get("pay_type"))){
										balancePayMoneyBigDecimal = new BigDecimal(balancePayMoneyStart).add(new BigDecimal(String.valueOf(mapOrderPay.get(i).get("payed_money"))));
										balancePayMoneyStart = String.valueOf(balancePayMoneyBigDecimal);
										apiOrderDetailsBalancePayResult.setPayType("449746280009");
										apiOrderDetailsBalancePayResult.setSumDueMoney(balancePayMoneyBigDecimal.toString());
										orderDetailsBalancePay.add(apiOrderDetailsBalancePayResult); 
										//返回微公社余额支付信息
										apiOrderDetailsResult.setOrderDetailsBalancePay(orderDetailsBalancePay);
									}else if (mapOrderPay.get(i)!=null&&"449746280012".equals(mapOrderPay.get(i).get("pay_type"))) {
										fullMoney = fullMoney.add(new BigDecimal(String.valueOf(mapOrderPay.get(i).get("payed_money"))));
									}
								}
							}
							
							
						}
						
						// 0元单的情况下支付类型分： 余额支付(449746280009)、优惠支付(449746280017)
						if(order.getDueMoney().compareTo(new BigDecimal("0.00")) == 0){
							if(balancePayMoneyBigDecimal.compareTo(new BigDecimal("0.00")) > 0){
								apiOrderDetailsResult.setDefault_Pay_type("449746280009");
							}else{
								apiOrderDetailsResult.setDefault_Pay_type("449746280017");
							}
						}
					}

				}
				apiOrderDetailsResult.setOrder_code(inputParam.getOrder_code()); // 仅用于显示	
			}
			//LD订单
			else {
				RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
				rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(sOrderCode);
				rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
				rsyncGetThirdOrderDetail.doRsync();
				if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
					RsyncModelThirdOrderDetail orderdetail = rsyncGetThirdOrderDetail.getResponseObject().getResult().get(0);
					alipayValue = "";
					apiOrderDetailsResult.setOrder_code(inputParam.getOrder_code());//订单编号
					apiOrderDetailsResult.setAlipayUrl(FamilyConfig.ali_url_http);//支付宝移动支付链接
					apiOrderDetailsResult.setAlipaySign(alipayValue);//支付宝Sign
										
					apiOrderDetailsResult.setConsigneeAddress(orderdetail.getRec_addr() == null ? "" : orderdetail.getRec_addr().toString());//收货人地址
					apiOrderDetailsResult.setConsigneeTelephone(orderdetail.getTel() == null ? "" : orderdetail.getTel().toString());//收货人电话
					apiOrderDetailsResult.setIdNumber("");//身份证号码
					apiOrderDetailsResult.setConsigneeName(orderdetail.getRcver_nm() == null ? "" : orderdetail.getRcver_nm().toString());//收货人姓名
					apiOrderDetailsResult.setInvoiceInformation(new InvoiceInformationResult());//发票信息(不显示)
					apiOrderDetailsResult.setCreate_time(convertDate(orderdetail.getEtr_date()));//下单时间
					
					apiOrderDetailsResult.setFirstFavorable("");//首单优惠					
					apiOrderDetailsResult.setFullSubtraction(0d);//满减
					apiOrderDetailsResult.setTelephoneSubtraction(0d);//手机下单减少
					apiOrderDetailsResult.setDefault_Pay_type("449746280003");//默认支付方式--支付宝
					apiOrderDetailsResult.setPay_type(orderdetail.getPay_type());//支付方式	
					
					apiOrderDetailsResult.setTariffMoney("");//关税
					
					
					apiOrderDetailsResult.setFailureTimeReminder("");//失效时间提示
					apiOrderDetailsResult.setIfFlashSales("1");//是否为闪购订单
					
					apiOrderDetailsResult.setIsSeparateOrder("1");//是否为需要分包的订单
					
					//只要有一个是海外购，那整个订单就都是海外购
					if("Y".equals(orderdetail.getIs_hwg())) {
						apiOrderDetailsResult.setFlagTheSea("1");//是否为海外购
					} else {
						apiOrderDetailsResult.setFlagTheSea("0");//是否为海外购
					}
					
					apiOrderDetailsResult.setOrderActivityDetailsResult(new ArrayList<ApiOrderActivityDetailsResult>());//活动信息(不显示)
					apiOrderDetailsResult.setOrderActivityRemarkDetailsResult(new ArrayList<ApiOrderActivityRemarkDetailsResult>());//活动备注(不显示)
					
					apiOrderDetailsResult.setYc_express_num(orderdetail.getInvc_id() == null ? "" : orderdetail.getInvc_id().toString());//运单号
					apiOrderDetailsResult.setYc_delivergoods_user_name(orderdetail.getDl_cd_desc() == null ? "" : orderdetail.getDl_cd_desc().toString());//送货商名称
					
					apiOrderDetailsResult.setCashBackMoney(BigDecimal.ZERO);//预计返利金额
					apiOrderDetailsResult.setNewuserFlag("0");//是否为新用户
					
					apiOrderDetailsResult.setOrderType("");//订单类型
					
					apiOrderDetailsResult.setHjyBean(BigDecimal.ZERO);//惠豆金额
					
					BigDecimal due_money = BigDecimal.ZERO;//应付金额
					BigDecimal order_money = BigDecimal.ZERO;//商品总金额
					BigDecimal czjAmt = BigDecimal.ZERO;/*储备金*/
					BigDecimal zckAmt = BigDecimal.ZERO;/*暂存款*/
					BigDecimal freght = BigDecimal.ZERO;//运费
					BigDecimal integralMoney = BigDecimal.ZERO;//积分金额
					BigDecimal disAmt = BigDecimal.ZERO;//优惠金额	
					
					//订单状态列表处理
					Set<String> orderStatusList = new HashSet<String>();
					
					for(RsyncModelThirdOrderDetail item : rsyncGetThirdOrderDetail.getResponseObject().getResult()) {
						order_money = order_money.add(item.getOrd_amt());
						due_money = due_money.add(item.getOrg_prc().multiply(new BigDecimal(item.getOrd_qty())));
						czjAmt = czjAmt.add(item.getPpc_apply_amt());
						zckAmt = zckAmt.add(item.getCrdt_apply_amt());
						freght = freght.add(item.getDely_fee());
						integralMoney = integralMoney.add(item.getAccm_apply_amt());
						disAmt = disAmt.add(item.getDis_amt());
						
						orderStatusList.add(item.getOrd_stat().toString());
					}											
					
					//订单状态需要特殊处理，取最大值
					if(orderStatusList.size() == 1) {
						apiOrderDetailsResult.setOrder_status(convertOrderStatus(orderdetail.getOrd_stat()));//订单状态
					} else {
						String order_status = orderdetail.getOrd_stat();
						Iterator<String> it = orderStatusList.iterator();
						while(it.hasNext()) {
							String c_status = it.next();
							if(CompareOrderStatus(order_status, c_status) == -1) {
								order_status = c_status;
							}
						}
						apiOrderDetailsResult.setOrder_status(convertOrderStatus(order_status));//订单状态
					}
					
					//优惠活动
					if(disAmt.compareTo(BigDecimal.ZERO) > 0) {
						ApiOrderActivityDetailsResult active = new ApiOrderActivityDetailsResult();
						active.setActivityCode("TV0000000000000001");
						active.setActivityType("TV下单优惠");
						active.setPlusOrMinus(0);
						active.setPreferentialMoney(disAmt.toString());
						apiOrderDetailsResult.getOrderActivityDetailsResult().add(active);
					}
					apiOrderDetailsResult.setDue_money(due_money);//应付金额
					apiOrderDetailsResult.setProductMoney(due_money);//商品总金额
					apiOrderDetailsResult.setCzjAmt(czjAmt); /*储备金*/
					apiOrderDetailsResult.setZckAmt(zckAmt);/*暂存款*/
					apiOrderDetailsResult.setFreight(freght.doubleValue());//运费
					apiOrderDetailsResult.setIntegralMoney(integralMoney);//积分金额
					apiOrderDetailsResult.setProductIntegral(BigDecimal.ZERO);//商品总积分	
					apiOrderDetailsResult.setOrder_money(order_money.toString());//订单金额
					apiOrderDetailsResult.setOrderDetailsBalancePay(orderDetailsBalancePay);//余额支付												
					apiOrderDetailsResult.setApiHomeOrderTrackingListResult(new ArrayList<ApiHomeOrderTrackingListResult>());//订单跟踪信息
					apiOrderDetailsResult.setApiOrderKjtParcelResult(new ArrayList<ApiOrderKjtParcelResult>());//包裹信息
					
					String versionApp = inputParam.getApp_vision();
					apiOrderDetailsResult.setOrderSellerList(getOrderSellerList(apiOrderDetailsResult,rsyncGetThirdOrderDetail.getResponseObject().getResult(),versionApp));//订单商品列表
					
					if("4497153900010001".equals(apiOrderDetailsResult.getOrder_status())) {
						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
					} else if("4497153900010002".equals(apiOrderDetailsResult.getOrder_status())) {
						boolean hasCancelling = DbUp.upTable("oc_order_cancel_h").count("order_code", sOrderCode) > 0; // 是否订单正在异步取消
						// 正在取消中的订单不再展示取消发货按钮
						if(!hasCancelling && AppVersionUtils.compareTo("5.2.6", versionApp)<0) {
							apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
						}
					} else if("4497153900010003".equals(apiOrderDetailsResult.getOrder_status())) {
						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
					} else if("4497153900010005".equals(apiOrderDetailsResult.getOrder_status())) {
						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流						
					} 
					if(!"4497153900010001".equals(apiOrderDetailsResult.getOrder_status())) {
						if("4497153900010005".equals(apiOrderDetailsResult.getOrder_status())) {//交易成功显示客服电话
							apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080014",bConfig("familyhas.define_4497477800080014")));//客服电话
						}else if("4497153900010006".equals(apiOrderDetailsResult.getOrder_status())) {//交易失败不显示此按钮
							
						}else {//4497153900010002/3/4
							apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
						}
					}

					if ("4497153900010001".equals(order.getOrderStatus())
							&& (apiOrderDetailsResult.getFailureTimeReminder() == null || "".equals(apiOrderDetailsResult.getFailureTimeReminder()))) {
						apiOrderDetailsResult.setIfFlashSales("1");
						apiOrderDetailsResult.setFailureTimeReminder("提示:下单成功后24小时内不付款，系统将自动取消订单！");
					}
					
					String is_chg = orderdetail.getIs_chg() == null ? "" : orderdetail.getIs_chg();
					//换货新单增加换货标识
					if(AppVersionUtils.compareTo("5.3.2", appVersion)<=0) {
						if(AfterSaleService.isChangeGoodsOrder(sOrderCode, is_chg)) {
							apiOrderDetailsResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
						}
					}
					
					//LD订单不展示支付方式和发票信息
					apiOrderDetailsResult.setIsShowPay("0");
					apiOrderDetailsResult.setIsShowInvoice("0");
					
					//================取消发货原因begin=================================
					List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryByWhere("after_sales_type","449747660007","status","449747660005");
					if(reasonList!=null){
						for (MDataMap mDataMap : reasonList) {
							String reason_code = mDataMap.get("return_reson_code");
							String reson_content = mDataMap.get("return_reson");
							Reason reason = new Reason(reason_code, reson_content);
							apiOrderDetailsResult.getReasonList().add(reason);
						}
					}
					//================取消发货原因end===================================
					
					RsyncGetOrderTracking  rsyncGetOrderTracking = new RsyncGetOrderTracking();
					rsyncGetOrderTracking.upRsyncRequest().setOrd_id(sOrderCode);
					rsyncGetOrderTracking.doRsync();
					if(rsyncGetOrderTracking.getResponseObject() != null && rsyncGetOrderTracking.getResponseObject().getResult() != null && rsyncGetOrderTracking.getResponseObject().getResult().size() > 0 ) {
						List<ResponseGetOrderTrackingList> result = rsyncGetOrderTracking.getResponseObject().getResult();
						Collections.sort(result, new Comparator<ResponseGetOrderTrackingList>() {
							@Override
							public int compare(ResponseGetOrderTrackingList t1, ResponseGetOrderTrackingList t2) {
								String yc_update_time1 = t1.getYc_update_time();
								String yc_update_time2 = t2.getYc_update_time();
								if(yc_update_time1.compareTo(yc_update_time2) < 0)
									return 1;
								else 
									return -1;
							}							
						});
						List<ApiHomeOrderTrackingListResult> aor = new ArrayList<ApiHomeOrderTrackingListResult>();
						for(ResponseGetOrderTrackingList track : result){
							ApiHomeOrderTrackingListResult apiHomeOrderTrackingListResult = new ApiHomeOrderTrackingListResult();
							apiHomeOrderTrackingListResult.setOrderTrackContent(track.getOutgo_no());
							apiHomeOrderTrackingListResult.setOrderTrackTime(track.getOutgo_time());
							apiHomeOrderTrackingListResult.setYc_dis_time(track.getYc_dis_time());
							apiHomeOrderTrackingListResult.setYc_update_time(track.getYc_update_time());
							aor.add(apiHomeOrderTrackingListResult);
						}
						apiOrderDetailsResult.setApiHomeOrderTrackingListResult(aor);
					}
					
					// 直邮商品的物流提示
					if(("4497153900010002".equals(order.getOrderStatus()) 
							|| ("4497153900010003".equals(order.getOrderStatus())) && apiOrderDetailsResult.getApiHomeOrderTrackingListResult().isEmpty())){
						LoadSkuInfoSpread loadSkuInfoSpreadLD = new LoadSkuInfoSpread();
						PlusModelSkuQuery sq = new PlusModelSkuQuery();
						
						// 理论上只查询一个商品就行，因为直邮的商品已经被拆为单独的订单了
						if(StringUtils.isNotBlank(orderdetail.getGood_id().toString()) && !"".equals(orderdetail.getGood_id())){
							sq.setCode(orderdetail.getGood_id().toString());
							PlusModelSkuInfoSpread plusModelSkuInfoSpreadLD = loadSkuInfoSpreadLD.upInfoByCode(sq);
							if(plusModelSkuInfoSpreadLD != null && "1".equals(plusModelSkuInfoSpreadLD.getProductTradeType())){
								apiOrderDetailsResult.setLogisticsTips(bConfig("familyhas.product_tips_zhiyou"));
							}
						}
					}
					
					if(StringUtils.isBlank(apiOrderDetailsResult.getLogisticsTips())
							&& apiOrderDetailsResult.getApiHomeOrderTrackingListResult().isEmpty()
							&& "4497153900010003".equals(order.getOrderStatus())){
						apiOrderDetailsResult.setLogisticsTips("暂无物流信息!");
					}
				}
				return apiOrderDetailsResult;
			}
		}
		
		// 直邮商品的温馨提示   在未发货或已发货且无物流信息的情况下才展示
		if(showTipsForZhiYou && apiOrderDetailsResult.getApiHomeOrderTrackingListResult().isEmpty()){
			apiOrderDetailsResult.setLogisticsTips(bConfig("familyhas.product_tips_zhiyou"));
		}
		
		// 只在已发货的情况下显示默认的物流提示
		if(StringUtils.isBlank(apiOrderDetailsResult.getLogisticsTips())
				&& apiOrderDetailsResult.getApiHomeOrderTrackingListResult().isEmpty()
				&& "4497153900010003".equals(order.getOrderStatus())){
			apiOrderDetailsResult.setLogisticsTips("暂无物流信息!");
		}
		
		if(VersionHelper.checkServerVersion("3.5.44.51")){
			//是否为新用户
			int iCount = DbUp.upTable("mc_login_info").count("member_code",
					getUserCode(), "login_pass", "");
			if (iCount > 0) {
				apiOrderDetailsResult.setNewuserFlag("1");
			}
		}
		if(fullMoney.compareTo(BigDecimal.ZERO)>0){
			apiOrderDetailsResult.setFullSubtraction(fullMoney.doubleValue());
		}	
		
		//取消发货原因
		/**
		 * 5.2.6 使用新的取消发货原因
		 */
		List<MDataMap> reasonList=DbUp.upTable("oc_return_goods_reason").queryByWhere("after_sales_type","449747660007","status","449747660005");
		if(reasonList!=null){
			for (MDataMap mDataMap : reasonList) {
				String reason_code = mDataMap.get("return_reson_code");
				String reson_content = mDataMap.get("return_reson");
				Reason reason = new Reason(reason_code, reson_content);
				apiOrderDetailsResult.getReasonList().add(reason);
			}
		}
		
		//按钮
		MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",sOrderCode);
		if(orderInfo==null||orderInfo.isEmpty()) {
			
			orderInfo=DbUp.upTable("oc_orderinfo").one("big_order_code",sOrderCode);
			if(orderInfo!=null&&!orderInfo.isEmpty()){
				
				if("449716200001".equals(orderInfo.get("pay_type"))){
					apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
				}
			}
			apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
			
		}else{
			String versionApp = inputParam.getApp_vision();
			//订单详情页的按钮，取消发货和售后按钮
			String order_status=orderInfo.get("order_status");
//					String flag_asale=orderInfo.get("flag_asale");
			if("4497153900010001".equals(order_status)){//大订单号都为代付款订单
				if("449716200001".equals(orderInfo.get("pay_type"))){
					apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
				}
				apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
			}else if("4497153900010002".equals(order_status)){//取消发货
//						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
				
				//if (StringUtils.startsWith(orderInfo.get("small_seller_code"), "SF031")) {
				//获取跨境商户Code
//						List<String> kuajingShopList = getKJTShopList();
//						  if(!kuajingShopList.contains((orderInfo.get("small_seller_code")))){
//						if(!new PlusServiceSeller().isKJSeller(orderInfo.get("small_seller_code"))&&StringUtils.startsWith(orderInfo.get("small_seller_code"), "SF031")){
				/**
				 * 根据商户编码查询商户类型，判断商户是否为第三方商户 2016-11-30 zhy
				 */
				String uc_seller_type = WebHelper.getSellerType(orderInfo.get("small_seller_code"));
				if("".equals(uc_seller_type)) {
					if("SI2003".equals(orderInfo.get("small_seller_code"))) {
						if(AppVersionUtils.compareTo("5.2.6", versionApp)<0) {
							apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
						}
					}
				}
				else if(StringUtils.equals(uc_seller_type, "4497478100050001") || StringUtils.equals(uc_seller_type, "4497478100050004") || StringUtils.equals(uc_seller_type, "4497478100050005")){
					apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
				}
				
			}else if("4497153900010003".equals(order_status)){//取消发货
				apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
				apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
			}else if("4497153900010005".equals(order_status)){//取消发货
				
				if(DbUp.upTable("nc_order_evaluation").count("order_code",orderInfo.get("order_code"))<1){
					apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
				}
				apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流				
			}else if("4497153900010006".equals(order_status)){//取消发货
				apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//删除订单
//						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080011",bConfig("familyhas.define_4497477800080011")));//售后进度
			}
			
			if(!"4497153900010001".equals(order_status)){				
				if(AppVersionUtils.compareTo("5.2.2", versionApp)<0) {
					if("4497153900010005".equals(order_status)) {//交易成功显示客服电话
						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080014",bConfig("familyhas.define_4497477800080014")));//客服电话
					}else if("4497153900010006".equals(order_status)) {//交易失败不显示此按钮
						
					}else {//4497153900010002/3/4
						apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
					}
				}else {
					apiOrderDetailsResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
				}
			}
		}
		apiOrderDetailsResult.setIsShowPay("1");
		apiOrderDetailsResult.setIsShowInvoice("1");
		if("OS".equals(inputParam.getOrder_code().substring(0, 2))){
			MDataMap mWhereMap = new MDataMap();
			mWhereMap.put("big_order_code", inputParam.getOrder_code());
			List<Map<String,Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList("select * from oc_orderinfo where big_order_code =:big_order_code", mWhereMap);
			if(dataSqlList.size() > 0) {
				String order_code = dataSqlList.get(0).get("order_code").toString();
				String cachetime = XmasKv.upFactory(EKvSchema.TimeCancelOrder).get(order_code);
				try {
					String time = "";
					if(StringUtils.isNotBlank(cachetime)) {
						time = cachetime; 
					}else {
						time = FormatHelper.upDateTime(new Date(FormatHelper.parseDate(dataSqlList.get(0).get("create_time").toString()).getTime() + 24*60*60*1000),"yyyy-MM-dd HH:mm:ss");
					}
					apiOrderDetailsResult.setRemainTime(time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} 
		return apiOrderDetailsResult;		
	}
	

	/**
	 * 取商品信息列表
	 * @param detailList
	 * @return
	 */
	private List<ApiOrderSellerDetailsResult> getOrderSellerList(ApiOrderDetailsResult apiOrderDetailsResult,List<RsyncModelThirdOrderDetail> detailList,String appVersion){
		List<ApiOrderSellerDetailsResult> orderSellerList = new ArrayList<ApiOrderSellerDetailsResult>();
		OrderService orderService = new OrderService();
		ProductLabelService productLabelService = new ProductLabelService();
		List<PcPropertyinfoForFamily> standardAndStyleList = new ArrayList<PcPropertyinfoForFamily>();
		for(RsyncModelThirdOrderDetail detail : detailList) {
			ApiOrderSellerDetailsResult orderSeller = new ApiOrderSellerDetailsResult();
			String product_code = detail.getGood_id().toString();
			String sku_key = "color_id=" + detail.getColor_id().toString() + "&style_id=" + detail.getStyle_id().toString();
			String skuCode = orderService.getSkuCodeByColorStyle(product_code, sku_key);
			String orderStatus =detail.getOrd_stat();
			if(orderStatus.length()==2) {
				orderStatus = this.convertOrderStatus(detail.getOrd_stat());
			}
			if(!"".equals(skuCode)) {
				//flashSalesList = orderService.flashSales(sku_code); // 判断是否为闪够信息
				List<Map<String, Object>> sellerList = orderService.sellerInformation(skuCode); // 查询商品信息
				if (sellerList != null && !sellerList.isEmpty()) {
					for (int k = 0; k < sellerList.size(); k++) {
						//新增售后按钮开始
						ApiOrderSellerDetailsResult apiOrderSellerDetailsResult = new ApiOrderSellerDetailsResult();
						if(orderStatus.equals("4497153900010005")||orderStatus.equals("4497153900010003")){
							if(!"1".equals(detail.getIs_hwg())){//非海外购商品
								//校验是否显示售后按钮方法
								boolean flag = new AfterSaleService().checkIfAllowAfterSale(detail.getOrd_id().toString(), detail.getGood_id().toString(), detail.getOrd_seq().toString());
								if(flag){//是否已经申请过售后且状态不为取消。
									if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){//过滤掉app版本为 526/527的
										//售后按钮
										apiOrderSellerDetailsResult.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008")));//售后
									}
								}
								
							}
						}
						//新增售后按钮结束
						List<ApiOrderDonationDetailsResult> apiOrderDonationDetailsResultList = new ArrayList<ApiOrderDonationDetailsResult>();
						List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
						
						Map<String, Object> map = sellerList.get(k);
						if (!"null".equals(String.valueOf(map.get("sku_picurl")))) {
							apiOrderSellerDetailsResult.setMainpicUrl(map.get("sku_picurl").toString());
						}
						apiOrderSellerDetailsResult.setNumber(String.valueOf(detail.getOrd_qty()));
						
						/**
						 * 商品价格
						 */
						apiOrderSellerDetailsResult.setDeal_price(detail.getOrg_prc().doubleValue());
						apiOrderSellerDetailsResult.setProductCode(String.valueOf(map.get("product_code")));
						apiOrderSellerDetailsResult.setSkutCode(skuCode);
						apiOrderSellerDetailsResult.setIntegral("0");
						apiOrderSellerDetailsResult.setPrice(detail.getOrg_prc().doubleValue());
						apiOrderSellerDetailsResult.setOrderSeq(detail.getOrd_seq().toString());//订单明细增加小订单序号
						//添加图片
						apiOrderSellerDetailsResult.setLabelsPic(productLabelService.getLabelInfo(String.valueOf(map.get("product_code"))).getListPic());												
						apiOrderSellerDetailsResult.setProductName(detail.getGood_nm());						
						/*是否生鲜*/
						PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(apiOrderSellerDetailsResult.getProductCode());						
						List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();						
						apiOrderSellerDetailsResult.setLabelsList(labelsList);
						apiOrderSellerDetailsResult.setRegion("");
						standardAndStyleList = orderService.sellerStandardAndStyle(map.get("sku_keyvalue").toString()); // 截取 尺码和款型
						if (standardAndStyleList != null && standardAndStyleList.size() > 0) {
							for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
								ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
								apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
								apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
								apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
							}
							apiOrderSellerDetailsResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
						}						
						orderSellerList.add(apiOrderSellerDetailsResult);
					}
				}
			} else {
				//新增售后按钮开始
				String orderStatusLd = this.convertOrderStatus(detail.getOrd_stat());
				if(orderStatusLd.equals("4497153900010005")||orderStatusLd.equals("4497153900010003")){
					if(!"1".equals(detail.getIs_hwg())){//非海外购商品
						boolean flag = new AfterSaleService().checkIfAllowAfterSale(detail.getOrd_id().toString(), "", detail.getOrd_seq().toString());
						if(flag){//是否已经申请过售后且状态不为取消。
							if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){//过滤掉app版本为 526/527的
								orderSeller.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008")));//售后
							}
						}
						
					}
				}
				//新增售后按钮结束
				orderSeller.setMainpicUrl("");
				orderSeller.setProductCode(product_code);
				orderSeller.setSkutCode(product_code);//当skuCode为空时，默认补productCode
				orderSeller.setNumber(String.valueOf(detail.getOrd_qty()));
				orderSeller.setDeal_price(detail.getOrg_prc().doubleValue());
				orderSeller.setIntegral("0");
				orderSeller.setPrice(detail.getOrg_prc().doubleValue());
				orderSeller.setOrderSeq(detail.getOrd_seq().toString());//订单明细增加小订单序号
				//添加图片
				orderSeller.setLabelsPic(productLabelService.getLabelInfo(product_code).getListPic());
				orderSeller.setProductName(detail.getGood_nm());						
				/*是否生鲜*/
				PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(orderSeller.getProductCode());						
				List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();						
				orderSeller.setLabelsList(labelsList);
				orderSeller.setRegion("");
				
				List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
				
				ApiSellerStandardAndStyleResult color = new ApiSellerStandardAndStyleResult();
				color.setStandardAndStyleKey("颜色");
				color.setStandardAndStyleValue(detail.getColor_desc());
				apiSellerStandardAndStyleResultList.add(color);
				
				ApiSellerStandardAndStyleResult style = new ApiSellerStandardAndStyleResult();
				style.setStandardAndStyleKey("款式");
				style.setStandardAndStyleValue(detail.getStyle_desc());
				apiSellerStandardAndStyleResultList.add(style);
				
				orderSeller.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
				orderSeller.setIsProductSync("0");
				orderSeller.setProductPrompt("该商品已下架！");
				orderSellerList.add(orderSeller);
			}
		}
		
		return orderSellerList;
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
	 * 比较状态
	 * @param order_status1
	 * @param order_status2
	 * @return
	 */
	private int CompareOrderStatus(String order_status1, String order_status2) {
		if(order_status1.equals(order_status2)) {
			return 1;
		}
		int status1 = Integer.parseInt(order_status1.substring(1));
		int status2 = Integer.parseInt(order_status2.substring(1));
		if(status1 >= status2) {
			return 1;
		} else {
			return -1;
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
	
	/**
	 * 京东商品订单 屏蔽取消发货按钮
	 * @param result
	 */
	private void handleJdOrderButton(ApiOrderDetailsResult result) {
		boolean flag = false;//京东商品标识
		List<ApiOrderSellerDetailsResult> orderSellerList = result.getOrderSellerList();
		for (ApiOrderSellerDetailsResult apiOrderSellerDetailsResult : orderSellerList) {
			if(Constants.SMALL_SELLER_CODE_JD.equals(apiOrderSellerDetailsResult.getSmallSellerCode())) {
				flag = true;
				break;
			}
		}
		if(flag) {
			List<Button> orderButtonList = result.getOrderButtonList();
			for (Button button : orderButtonList) {
				if("4497477800080004".equals(button.getButtonCode())) {
					button.setButtonStatus(3);
				}
			}
		}
	}
}
