package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiOrderListInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.ApiOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.service.OrderDetailService;
import com.cmall.ordercenter.service.ApiAlipayMoveProcessService;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 订单列表接口
 * 
 * @author wz
 * 业务逻辑： 
 * 待付款  、  交易失败、外部订单号为空的(只要有一条为空就算)显示大订单号，其余显示外部订单号
 * 
 */
public class ApiOrderList extends
		RootApiForToken<ApiOrderListResult, ApiOrderListInput> {
	
	
	/**
	 * 创建订单之后 24小时  之后  才能提醒发货
	 */
	public static final Integer CREATEORDER_LATER = 24;

	/**
	 * 上次提醒发货 12小时之后  才能提醒发货
	 */
	public static final Integer PRE_REMINDORDERSHIPMENT_LATER = 12;
	
	static final String TelRefundVersion = "5.2.2";
	

	public ApiOrderListResult Process(ApiOrderListInput inputParam,
			MDataMap mRequestMap) {
		
		String versionApp = inputParam.getApp_vision();
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		
		OrderService orderService = new OrderService();
		ProductService productService = new ProductService();
		ProductLabelService productLabelService = new ProductLabelService();
		ApiAlipayMoveProcessService apiAlipayMoveProcessService = new ApiAlipayMoveProcessService();
		ApiOrderListResult apiOrderListResult = new ApiOrderListResult();
		MDataMap paramMap = new MDataMap();
		inputParam.setBuyer_code(getUserCode());

		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderSellerList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sellerList = new ArrayList<Map<String, Object>>();
		List<MDataMap> flashSalesList = new ArrayList<MDataMap>();

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> orderSellerMap = new HashMap<String, Object>();
		Map<String, Object> sellerMap = new HashMap<String, Object>();
		Map<String, String> alipaySignMap = new HashMap<String, String>();

		List<ApiSellerOrderListResult> sellerOrderList = new ArrayList<ApiSellerOrderListResult>();
		// List<ApiSellerListResult> sellerResultList = new
		// ArrayList<ApiSellerListResult>();
		List<PcPropertyinfoForFamily> standardAndStyleList = new ArrayList<PcPropertyinfoForFamily>();

		String alipayValue = null;
		String orderCode = null;
		String sellerCode = null;
		List<String> bigOrderCodeList = new ArrayList<String>();
		List<String> bigOrderCodeNewList = new ArrayList<String>();
		boolean isGetOnOrderInfoByOrderCode = false;
		boolean isPage = true;//是否分页

		int count = 0;
		int countPage = 0;
		int orderSellerNumberDouble = 0;

		if (!"".equals(inputParam.getBuyer_code())
				&& !"".equals(inputParam.getNextPage())) {
			
			if(VersionHelper.checkServerVersion("3.5.44.51")){
				count = orderService.orderCountNew(inputParam.getBuyer_code(),inputParam.getOrder_status());// 统计订单总数
				if (count != 0) {
					countPage = count / 10; // 总页数 count
					apiOrderListResult.setCountPage(countPage + 1);
				}
			}else{
				count = orderService.orderCount(inputParam.getBuyer_code(),
						inputParam.getOrder_status()); // 统计订单总数
				
				if (count != 0) {
					countPage = count / 10; // 总页数 count
					apiOrderListResult.setCountPage(countPage + 1);
				}
			}
			
			//判断是否是获取指定单个订单信息
			if(null != inputParam.getBig_orderCode() && inputParam.getBig_orderCode().length() > 0 ) {
				isGetOnOrderInfoByOrderCode = true;
				isPage = false;
				apiOrderListResult.setCountPage(1);
			}
			/*
			 * 获取需要过滤的订单类型
			 */
			String orderTypeQueryWhere = orderService.getNotInOrderType();
					
			/*
			 * 此版本是：  
			 * 多不考虑外部订单号是否为空
			 * 待付款的按照大订单显示(OS)
			 * 待发货、待收货、交易成功都是按照小订单显示(DD)
			 * 在全部的显示中，除了待付款 的  其他都按照小订单号显示
			 */
			if(VersionHelper.checkServerVersion("3.5.63.55")){
				if("4497153900010001".equals(inputParam.getOrder_status())){
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息

					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							
							//判断是否需要
							if(isGetOnOrderInfoByOrderCode && map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								break;
							}
							
							bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
							
						}
						// 去掉相同大订单号
						if (bigOrderCodeList != null) {
							Iterator it = bigOrderCodeList.iterator();
							while (it.hasNext()) {
								String bigO = (String) it.next();
								if (bigOrderCodeNewList.contains(bigO)) {
									it.remove();
								} else {
									bigOrderCodeNewList.add(bigO);
								}
							}
						}

						// 循环所有去重的大订单号
						for (String bigOrderCode : bigOrderCodeNewList) {
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
							
							
							//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
							//if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							//}

							// 查询大订单表 获取订单上相关信息
							Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
											"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
											new MDataMap("big_order_code",bigOrderCode)); 
							
							if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
								apiSellerOrderListResult.setOrder_status(inputParam.getOrder_status());
								apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
								apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
								
								apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);
								
								String defaultPayType = new PlusSupportPay().upPayFrom(String.valueOf(bigOrderMap.get("big_order_code")));
								if(defaultPayType!=null && !"".equals(defaultPayType)){
									apiSellerOrderListResult.setDefault_Pay_type(defaultPayType);   //返回客户端  支付方式(默认支付宝)
								}
								
								// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
								if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
									apiSellerOrderListResult.setDefault_Pay_type("449746280003");
								}
								
							}

							// 通过大订单号查询oc_orderinfo表, 获取相关信息     
							List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
											"select * from oc_orderinfo where big_order_code=:big_order_code and "
													+ " delete_flag=:delete_flag and order_source not in('449715190014') ORDER BY update_time DESC",    //order_status=:order_status and
											new MDataMap("big_order_code",bigOrderCode, "delete_flag", "0"));
							
							int orderSellerNumber = 0;
							
							for (Map<String, Object> mm : orderInfoList) {
								// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));

								orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

								if (orderSellerList != null&& !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));

										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);
												
												//判断此商品是否为海外购的商品
//												if(AppConst.MANAGE_CODE_KJT.equals(mm.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_MLG.equals(mm.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_QQT.equals(mm.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_SYC.equals(mm.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_CYGJ.equals(mm.get("small_seller_code"))){
												if(new PlusServiceSeller().isKJSeller(mm.get("small_seller_code").toString())){
													apiSellerListResult.setFlagTheSea("1");
														
														new OrderDetailService().initCustomsStatus((String)mm.get("order_code"), (String)mm.get("order_status"), apiSellerListResult);
														
													
													
												}												
	
												
												/**
												 * 商品价格
												 */
												//BigDecimal sell_price = new BigDecimal(orderSellerMap.get("sku_price").toString()).add(new BigDecimal(orderSellerMap.get("group_price").toString()).add(new BigDecimal(orderSellerMap.get("coupon_price").toString())));
												
												//积分兑换
												String orderType = MapUtils.getString(mm, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
												
												
//												apiSellerListResult.setSell_price(orderSellerMap.get("sku_price").toString());

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}
								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
							}
							
							if("449716200001".equals(map.get("pay_type"))){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
							}
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					
					
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
				
				}else if("4497153900010002".equals(inputParam.getOrder_status()) 
						|| "4497153900010003".equals(inputParam.getOrder_status())
						|| "4497153900010004".equals(inputParam.getOrder_status()) 
						|| "4497153900010005".equals(inputParam.getOrder_status())|| "4497153900010006".equals(inputParam.getOrder_status())){  //按照小订单显示
					
					
					String sql = "select count(1) as cnt from ordercenter.oc_orderinfo where buyer_code=:buyer_code and delete_flag=:delete_flag and order_status=:order_status and seller_code=:seller_code and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') ) and order_source not in('449715190014') and order_type not in ("+orderTypeQueryWhere+")  ";
					if(compareAppVersion(appVersion, "5.0.6") < 0 && "4497153900010005".equals(inputParam.getOrder_status())){  // 5.0.6之前的版本保持不变
						// 版本号小于5.0.6走原来的逻辑
						sql = "select count(1) as cnt from ordercenter.oc_orderinfo where"
								+ " buyer_code=:buyer_code and delete_flag=:delete_flag and order_status=:order_status and seller_code=:seller_code and order_source not in('449715190014') and order_type not in ("+orderTypeQueryWhere+") "
								+ " and order_code not in (select order_code from newscenter.nc_order_evaluation where order_name=:buyer_code and manage_code=:seller_code )"
								+ " and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') )";
						orderList = orderService.orderInformationSmall(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					}else{
						// 5.0.6以后走逻辑，不再屏蔽已评价的订单
						orderList = orderService.orderInformationSmallV2(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					}
					
					Map<String, Object> mapc=DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("buyer_code", inputParam.getBuyer_code(),"order_status", inputParam.getOrder_status(),"delete_flag", "0","seller_code", getManageCode()));
					
					int cc=0;
					if(mapc!=null&&!mapc.isEmpty()){
						cc=((Long)mapc.get("cnt")).intValue();
					}
					//判断是否需要
					if(isGetOnOrderInfoByOrderCode ) {
						apiOrderListResult.setCountPage(1);
					} else {
						countPage = Double.valueOf(Math.ceil(Double.valueOf(cc)/10d)).intValue();
						apiOrderListResult.setCountPage(countPage);
					}
					
					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							int orderSellerNumber = 0;
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

							map = orderList.get(i);
							
							//判断是否需要
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							orderCode = map.get("order_code").toString();
							sellerCode = map.get("seller_code").toString();

							//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode); // 获取支付宝移动支付链接地址
							// alipaySignMap =
							// apiAlipayMoveProcessService.alipaySign(orderCode);
							// //获取签名后的sign
							if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							}
							apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
							apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("order_code")));
							apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
							apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
							apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
							apiSellerOrderListResult.setAlipaySign(alipayValue);

							
							
							///////////////////////////////////////////////////
							if("4497153900010006".equals(map.get("order_status")) ){
								//在订单下方展示的删除订单按钮屏蔽，由订单是否删除的标识代替
								apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
								if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
								}
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
								}
							}else if("4497153900010002".equals(map.get("order_status")) ){
//								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货	
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
									int btnStatus = is_remind_shipment(map);
									if(btnStatus != 3) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
									}
								}
							}else if("4497153900010003".equals(map.get("order_status")) ){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
								if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
								}else {
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
								}
							}else if("4497153900010005".equals(map.get("order_status")) ){
								if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
								}
								if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
								}
								
								apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
								}
							}
							
							
							
							orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																					// 和
																					// 商品code、商品单价

							if (orderSellerList != null && !orderSellerList.isEmpty()) {
								for (int j = 0; j < orderSellerList.size(); j++) {

									orderSellerMap = orderSellerList.get(j);
									flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

									orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
									orderSellerNumber += orderSellerNumberDouble;

									sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
									if (sellerList != null && !sellerList.isEmpty()) {
										for (int k = 0; k < sellerList.size(); k++) {
											List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
											ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
											sellerMap = sellerList.get(k);
											//过滤评价后的商品
										
											if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
												apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
											}
											apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
											apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
											apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
											apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
											//524:添加分类标签
								
											PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
											String ssc =productInfo.getSmallSellerCode();
											String st="";
											if("SI2003".equals(ssc)) {
												st="4497478100050000";
											}
											else {
												st = WebHelper.getSellerType(ssc);
											}
											//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
											Map productTypeMap = WebHelper.getAttributeProductType(st);
											apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
											
											//添加图片
											apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
											/*是否生鲜*/
											PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
											
											List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
											
											apiSellerListResult.setLabelsList(labelsList);
											
											//判断此商品是否为海外购的商品
//											if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//													|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//													|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//													|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//													|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
											if(new PlusServiceSeller().isKJSeller(map.get("small_seller_code").toString())){	
												apiSellerListResult.setFlagTheSea("1");
													
												new OrderDetailService().initCustomsStatus(orderCode, (String)map.get("order_status"), apiSellerListResult);
													
												
											}
											
											/**
											 * 商品价格
											 */
											//BigDecimal sell_price = new BigDecimal(orderSellerMap.get("sku_price").toString()).add(new BigDecimal(orderSellerMap.get("group_price").toString()).add(new BigDecimal(orderSellerMap.get("coupon_price").toString())));
											
											//积分兑换
											String orderType = MapUtils.getString(map, "order_type", "");
											if("449715200024".equals(orderType)) {
												BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
												BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
												
												//商品价格 = 商品单价 - 单个商品积分抵扣金额
												apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
												apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
											}else {
												apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
											}
										

											standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																			// 款型
											if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
												for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
													ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
													apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
													apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
													apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
												}
												apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
											}

											apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

											sellerResultList.add(apiSellerListResult);
										}
										apiSellerOrderListResult.setApiSellerList(sellerResultList);
									}
									apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

								}
							}

							if (flashSalesList != null && !flashSalesList.isEmpty()) {
								apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
							} else {
								apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
							}
							
							
//							if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//									|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
							if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){	
								apiSellerOrderListResult.setIsSeparateOrder("0");   //需要分包
							}
								sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
				
				}else {   //order_state为全部(都按照大订单号展示)   if("".equals(inputParam.getOrder_status()))
					boolean waitPay = false;   //判断此用户所有订单是否有待支付     和   外部订单号为空   的订单
					String orderStatue = "";
					//String orderState = "";
					//查询此用户下所有的订单
					
					if (StringUtils.isBlank(inputParam.getOrder_status())) {
						
						int countPayment =  orderService.orderCountPayment(inputParam.getBuyer_code());
						
						
						
						Map<String, Object> mapc=DbUp.upTable("oc_orderinfo").dataSqlOne("select count(1) cnt from  oc_orderinfo where order_status<>'4497153900010001' and buyer_code=:buyer_code and delete_flag=:delete_flag and seller_code=:seller_code and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') ) and order_source not in('449715190014') and order_type not in ("+orderTypeQueryWhere+") ", new MDataMap("delete_flag", "0","buyer_code",inputParam.getBuyer_code(),"seller_code",getManageCode()));
						
						int cc=0;
						if(mapc!=null&&!mapc.isEmpty()){
							cc=((Long)mapc.get("cnt")).intValue();
						}
						
						count=cc+countPayment;
						
						//判断是否需要
						if(isGetOnOrderInfoByOrderCode ) {
							apiOrderListResult.setCountPage(1);
						} else {
							countPage = Double.valueOf(Math.ceil(Double.valueOf(count)/10d)).intValue();
							apiOrderListResult.setCountPage(countPage);
						}
					}
				
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					
					if (orderList != null && !orderList.isEmpty()) {
						
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							if("4497153900010001".equals(map.get("order_status"))){    //待付款    交易失败
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								//orderState = "4497153900010001";
								waitPay = true;
							}
//							else if("".equals(map.get("out_order_code")) || map.get("out_order_code")==null){   //外部订单为空
//								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
//								//orderState = String.valueOf(map.get("order_status"));
//								waitPay = true;
//							}

						}
						
						if(waitPay){
							// 去掉相同大订单号
							if (bigOrderCodeList != null) {
								Iterator it = bigOrderCodeList.iterator();
								while (it.hasNext()) {
									String bigO = (String) it.next();
									if (bigOrderCodeNewList.contains(bigO)) {
										it.remove();
									} else {
										bigOrderCodeNewList.add(bigO);
									}
								}
							}
							

							// 循环所有去重的大订单号
							for (String bigOrderCode : bigOrderCodeNewList) {
								//主要用于获取小订单中的支付状态
								MDataMap  mapOrderInfo = DbUp.upTable("oc_orderinfo").one("big_order_code",bigOrderCode);
								
								
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
								
								//是否需要分包
//								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
//									apiSellerOrderListResult.setIsSeparateOrder("0");
//								}
//								
//								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))){
//									apiSellerOrderListResult.setIsSeparateOrder("0");
//								}
//								
//								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))){
//									apiSellerOrderListResult.setIsSeparateOrder("0");
//								}
//								
//								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))){
//									apiSellerOrderListResult.setIsSeparateOrder("0");
//								}
//								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
								if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
									apiSellerOrderListResult.setIsSeparateOrder("0");
								}
								if("4497153900010001".equals(mapOrderInfo.get("order_status")) ){
									//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
									
									String defaultPayType = new PlusSupportPay().upPayFrom(bigOrderCode);
									if(defaultPayType!=null && !"".equals(defaultPayType)){
										apiSellerOrderListResult.setDefault_Pay_type(defaultPayType);   //返回客户端  支付方式(默认支付宝)
									}
									
									// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
									if(!"IOS".equals(inputParam.getDeviceType()) && "449746280013".equals(defaultPayType)){
										apiSellerOrderListResult.setDefault_Pay_type("449746280003");
									}
									
									if("449716200001".equals(map.get("pay_type"))){
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
									}
//									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款	
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
								}
								
								if("4497153900010006".equals(mapOrderInfo.get("order_status")) ){
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
									}
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}else if("4497153900010002".equals(mapOrderInfo.get("order_status")) ){
//									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
										int btnStatus = is_remind_shipment(map);
										if(3 != btnStatus) {
											apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
										}
									}
								}else if("4497153900010003".equals(mapOrderInfo.get("order_status")) ){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
									if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
									}else {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
									}
								}else if("4497153900010005".equals(mapOrderInfo.get("order_status")) ){
									if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
									}
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
									}
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}
								
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}

								// 查询大订单表 获取订单上相关信息
								Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
												"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
												new MDataMap("big_order_code",bigOrderCode));
								
								if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
									apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
									apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
									
									if("4497153900010001".equals(mapOrderInfo.get("order_status")) ){
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
									}else if("4497153900010002".equals(mapOrderInfo.get("order_status")) || "4497153900010006".equals(mapOrderInfo.get("order_status"))){
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("order_money").toString());
									}else{
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("payed_money").toString());
									}
									
									apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
									apiSellerOrderListResult.setAlipaySign(alipayValue);
								}

								// 通过大订单号查询oc_orderinfo表, 获取相关信息
								List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
												"select * from oc_orderinfo where big_order_code=:big_order_code and "
														+ "delete_flag=:delete_flag and order_source not in('449715190014') ORDER BY update_time DESC",
												new MDataMap("big_order_code",bigOrderCode,"delete_flag", "0"));
								
								int orderSellerNumber = 0;
								for (Map<String, Object> mm : orderInfoList) {
									// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));
									apiSellerOrderListResult.setOrder_status(String.valueOf(mm.get("order_status")));
									
									orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

									if (orderSellerList != null&& !orderSellerList.isEmpty()) {
										for (int j = 0; j < orderSellerList.size(); j++) {

											orderSellerMap = orderSellerList.get(j);
											flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

											orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
											orderSellerNumber += orderSellerNumberDouble;

											sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
											//查询商品是否有评论
											apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										
											if (sellerList != null && !sellerList.isEmpty()) {
												for (int k = 0; k < sellerList.size(); k++) {
													List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
													ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
													sellerMap = sellerList.get(k);
													if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
														apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
													}
													apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
													apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
													apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
													apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
													//524:添加分类标签
													PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
													String ssc =productInfo.getSmallSellerCode();
													String st="";
													if("SI2003".equals(ssc)) {
														st="4497478100050000";
													}
													else {
														st = WebHelper.getSellerType(ssc);
													}
													//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
													Map productTypeMap = WebHelper.getAttributeProductType(st);
													apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
													//添加图片
													apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
													
													//判断此商品是否为海外购的商品
//													if(AppConst.MANAGE_CODE_KJT.equals(mm.get("small_seller_code"))
//															|| AppConst.MANAGE_CODE_MLG.equals(mm.get("small_seller_code"))
//															|| AppConst.MANAGE_CODE_QQT.equals(mm.get("small_seller_code"))
//															|| AppConst.MANAGE_CODE_SYC.equals(mm.get("small_seller_code"))
//															|| AppConst.MANAGE_CODE_CYGJ.equals(mm.get("small_seller_code"))){
													if(new PlusServiceSeller().isKJSeller(mm.get("small_seller_code").toString())){
														apiSellerListResult.setFlagTheSea("1");
															
														new OrderDetailService().initCustomsStatus((String)mm.get("order_code"), (String)mm.get("order_status"), apiSellerListResult);
														
													}
													
													/*是否生鲜*/
													PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
													
													List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
													
													apiSellerListResult.setLabelsList(labelsList);
													/**
													 * 商品价格
													 */
													//BigDecimal sell_price = new BigDecimal(orderSellerMap.get("sku_price").toString()).add(new BigDecimal(orderSellerMap.get("group_price").toString()).add(new BigDecimal(orderSellerMap.get("coupon_price").toString())));
													
													String orderType = MapUtils.getString(mm, "order_type", "");
													if("449715200024".equals(orderType)) {
														BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
														BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
														
														//商品价格 = 商品单价 - 单个商品积分抵扣金额
														apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
														apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
													}else {
														apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
													}
//													apiSellerListResult.setSell_price(orderSellerMap.get("sku_price").toString());

													standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
													if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
														for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
															ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
															apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
															apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
															apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
														}
														apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
													}

													apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

													sellerResultList.add(apiSellerListResult);
												}
												apiSellerOrderListResult.setApiSellerList(sellerResultList);
											}
											apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

										}
									}
									if (flashSalesList != null && !flashSalesList.isEmpty()) {
										apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
									} else {
										apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
									}
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
						
						
						
						/*
						 * 此循环用于匹配
						 * 如果此订单不是"待付款(4497153900010001)、交易失败(4497153900010006)状态。外部订单号为空"时进入flag中。
						 */
						for (int i = 0; i < orderList.size(); i++) {
							boolean flag = true;
							map = orderList.get(i);
							
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							for(String bigOrderCode : bigOrderCodeList){
								if(bigOrderCode.equals(map.get("big_order_code"))){
									flag = false;
									break;
								}
							}
							
							if(flag){
								int orderSellerNumber = 0;
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

								orderCode = map.get("order_code").toString();
								sellerCode = map.get("seller_code").toString();
								
								if("4497153900010001".equals(map.get("order_status"))){
									//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode,true); // 获取支付宝移动支付链接地址
									// alipaySignMap =
									// apiAlipayMoveProcessService.alipaySign(orderCode);
									// //获取签名后的sign
								}
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}
								
								
								if("4497153900010006".equals(map.get("order_status")) ){
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
									}
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}else if("4497153900010002".equals(map.get("order_status")) ){
//									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货	
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
										int btnStatus = is_remind_shipment(map);
										if(3 != btnStatus) {
											apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
										}
									}
								}else if("4497153900010003".equals(map.get("order_status")) ){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
									if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
									}else {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
									}
								}else if("4497153900010005".equals(map.get("order_status")) ){
									if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
									}
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
									}
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}
								
								
								//判断此订单是否需要分包
//								if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//										|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
								
								if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
									apiSellerOrderListResult.setIsSeparateOrder("0");
								}
								
								apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
								apiSellerOrderListResult.setOrder_code(orderCode);
							//	apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
							//	apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("big_order_code")));
								apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
								apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);

								orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																						// 和
																						// 商品code、商品单价

								if (orderSellerList != null && !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												//判断此商品是否为海外购的商品
//												if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_MLG.equals(map.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_QQT.equals(map.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_SYC.equals(map.get("small_seller_code"))
//														|| AppConst.MANAGE_CODE_CYGJ.equals(map.get("small_seller_code"))){
												if(new PlusServiceSeller().isKJSeller(map.get("small_seller_code").toString())){	
													
													apiSellerListResult.setFlagTheSea("1");		
													
													new OrderDetailService().initCustomsStatus(orderCode, (String)map.get("order_status"), apiSellerListResult);
													
													
												}
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);
												
												/**
												 * 商品价格
												 */
												//BigDecimal sell_price = new BigDecimal(orderSellerMap.get("sku_price").toString()).add(new BigDecimal(orderSellerMap.get("group_price").toString()).add(new BigDecimal(orderSellerMap.get("coupon_price").toString())));
												
												String orderType = MapUtils.getString(map, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
//												apiSellerListResult.setSell_price(orderSellerMap.get("sku_price").toString());

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																				// 款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}

								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					
					// 按点击数倒序
					Collections.sort(sellerOrderList, new Comparator<ApiSellerOrderListResult>() {
						public int compare(ApiSellerOrderListResult o1, ApiSellerOrderListResult o2) {
							String hits0 = o1.getCreate_time();
							String hits1 = o2.getCreate_time();
							if (compareTime(hits0, hits1)<0) {
								return 1;
							} else if (hits0.equals(hits1)) {
								return 0;
							} else {
								return -1;
							}
						}
					});
					
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
					
				}
			}else if(VersionHelper.checkServerVersion("3.5.51.52")){   //此版本所有的状态都是按照大订单号显示的
				//VersionHelper.checkServerVersion("3.5.44.51")
				
				//待付款 、交易失败  进入此判断
				if ("4497153900010001".equals(inputParam.getOrder_status()) || "4497153900010006".equals(inputParam.getOrder_status())) {   //order_state为待支付
					
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息

					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							//判断是否需要
							if(isGetOnOrderInfoByOrderCode && map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								break;
							}
							
							bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
						}
						// 去掉相同大订单号
						if (bigOrderCodeList != null) {
							Iterator it = bigOrderCodeList.iterator();
							while (it.hasNext()) {
								String bigO = (String) it.next();
								if (bigOrderCodeNewList.contains(bigO)) {
									it.remove();
								} else {
									bigOrderCodeNewList.add(bigO);
								}
							}
						}

						// 循环所有去重的大订单号
						for (String bigOrderCode : bigOrderCodeNewList) {
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();;
							
							//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
							if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							}

							// 查询大订单表 获取订单上相关信息
							Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
											"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
											new MDataMap("big_order_code",bigOrderCode)); 
							
							if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
								apiSellerOrderListResult.setOrder_status(inputParam.getOrder_status());
								apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
								apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
								
								apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);
							}

							// 通过大订单号查询oc_orderinfo表, 获取相关信息
							List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
											"select * from oc_orderinfo where big_order_code=:big_order_code and "
													+ "order_status=:order_status and delete_flag=:delete_flag ORDER BY update_time DESC",
											new MDataMap("big_order_code",bigOrderCode, "order_status",inputParam.getOrder_status(),"delete_flag", "0"));
							
							int orderSellerNumber = 0;
							
							for (Map<String, Object> mm : orderInfoList) {
								// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));

								orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

								if (orderSellerList != null&& !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												
												String orderType = MapUtils.getString(mm, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
												
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}
								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
							}
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
				
				} else {   //order_state为全部(都按照大订单号展示)   if("".equals(inputParam.getOrder_status()))
					boolean waitPay = false;   //判断此用户所有订单是否有待支付     和   外部订单号为空   的订单
					String orderStatue = "";
					//String orderState = "";
					//查询此用户下所有的订单
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					
					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							if("4497153900010001".equals(map.get("order_status")) || "4497153900010006".equals(map.get("order_status"))){    //待付款    交易失败
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								//orderState = "4497153900010001";
								waitPay = true;
							}else if("".equals(map.get("out_order_code")) || map.get("out_order_code")==null){   //外部订单为空
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								//orderState = String.valueOf(map.get("order_status"));
								waitPay = true;
							}
//							else{
//								int orderSellerNumber = 0;
//								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
//								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
//
//								orderCode = map.get("order_code").toString();
//								sellerCode = map.get("seller_code").toString();
//
//								alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode); // 获取支付宝移动支付链接地址
//								// alipaySignMap =
//								// apiAlipayMoveProcessService.alipaySign(orderCode);
//								// //获取签名后的sign
//								if (StringUtils.isBlank(alipayValue)) {
//									alipayValue = "";
//								}
//								apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
//								apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
//								apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
//								apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
//								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
//								apiSellerOrderListResult.setAlipaySign(alipayValue);
//
//								orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
//																						// 和
//																						// 商品code、商品单价
//
//								if (orderSellerList != null && !orderSellerList.isEmpty()) {
//									for (int j = 0; j < orderSellerList.size(); j++) {
//
//										orderSellerMap = orderSellerList.get(j);
//										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息
//
//										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
//										orderSellerNumber += orderSellerNumberDouble;
//
//										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
//										if (sellerList != null && !sellerList.isEmpty()) {
//											for (int k = 0; k < sellerList.size(); k++) {
//												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
//												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
//												sellerMap = sellerList.get(k);
//												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
//													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
//												}
//												apiSellerListResult.setProduct_code(sellerMap.get("sku_code").toString());
//												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
//												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
//												apiSellerListResult.setSell_price(orderSellerMap.get("sku_price").toString());
//
//												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
//																				// 款型
//												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
//													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
//														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
//														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
//														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
//														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
//													}
//													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
//												}
//
//												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型
//
//												sellerResultList.add(apiSellerListResult);
//											}
//											apiSellerOrderListResult.setApiSellerList(sellerResultList);
//										}
//										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
//
//									}
//								}
//
//								if (flashSalesList != null && !flashSalesList.isEmpty()) {
//									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
//								} else {
//									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
//								}
//								sellerOrderList.add(apiSellerOrderListResult);
//							}
						}
						
						if(waitPay){
							// 去掉相同大订单号
							if (bigOrderCodeList != null) {
								Iterator it = bigOrderCodeList.iterator();
								while (it.hasNext()) {
									String bigO = (String) it.next();
									if (bigOrderCodeNewList.contains(bigO)) {
										it.remove();
									} else {
										bigOrderCodeNewList.add(bigO);
									}
								}
							}
							

							// 循环所有去重的大订单号
							for (String bigOrderCode : bigOrderCodeNewList) {
								//主要用于获取小订单中的支付状态
								MDataMap  mapOrderInfo = DbUp.upTable("oc_orderinfo").one("big_order_code",bigOrderCode);
								
								
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();;
								
								//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}

								// 查询大订单表 获取订单上相关信息
								Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
												"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
												new MDataMap("big_order_code",bigOrderCode));
								
								if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
									apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
									apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
									
									if("4497153900010001".equals(mapOrderInfo.get("order_status")) ){
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
									}else if("4497153900010002".equals(mapOrderInfo.get("order_status")) || "4497153900010006".equals(mapOrderInfo.get("order_status"))){
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("order_money").toString());
									}else{
										apiSellerOrderListResult.setDue_money(bigOrderMap.get("payed_money").toString());
									}
									
									apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
									apiSellerOrderListResult.setAlipaySign(alipayValue);
								}

								// 通过大订单号查询oc_orderinfo表, 获取相关信息
								List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
												"select * from oc_orderinfo where big_order_code=:big_order_code and "
														+ "delete_flag=:delete_flag ORDER BY update_time DESC",
												new MDataMap("big_order_code",bigOrderCode,"delete_flag", "0"));
								
								int orderSellerNumber = 0;
								for (Map<String, Object> mm : orderInfoList) {
									// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));
									apiSellerOrderListResult.setOrder_status(String.valueOf(mm.get("order_status")));
									
									orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

									if (orderSellerList != null&& !orderSellerList.isEmpty()) {
										for (int j = 0; j < orderSellerList.size(); j++) {

											orderSellerMap = orderSellerList.get(j);
											flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

											orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
											orderSellerNumber += orderSellerNumberDouble;

											sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
											//查询商品是否有评论
											apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
											if (sellerList != null && !sellerList.isEmpty()) {
												for (int k = 0; k < sellerList.size(); k++) {
													List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
													ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
													sellerMap = sellerList.get(k);
													if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
														apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
													}
													apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
													apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
													apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
													apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
													//524:添加分类标签
													PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
													String ssc =productInfo.getSmallSellerCode();
													String st="";
													if("SI2003".equals(ssc)) {
														st="4497478100050000";
													}
													else {
														st = WebHelper.getSellerType(ssc);
													}
													//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
													Map productTypeMap = WebHelper.getAttributeProductType(st);
													apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
													
													String orderType = MapUtils.getString(mm, "order_type", "");
													if("449715200024".equals(orderType)) {
														BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
														BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
														
														//商品价格 = 商品单价 - 单个商品积分抵扣金额
														apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
														apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
													}else {
														apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
													}
													
													//添加图片
													apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
													
													/*是否生鲜*/
													PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
													
													List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
													
													apiSellerListResult.setLabelsList(labelsList);

													standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
													if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
														for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
															ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
															apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
															apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
															apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
														}
														apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
													}

													apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

													sellerResultList.add(apiSellerListResult);
												}
												apiSellerOrderListResult.setApiSellerList(sellerResultList);
											}
											apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

										}
									}
									if (flashSalesList != null && !flashSalesList.isEmpty()) {
										apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
									} else {
										apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
									}
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
						
						
						
						/*
						 * 此循环用于匹配
						 * 如果此订单不是"待付款(4497153900010001)、交易失败(4497153900010006)状态。外部订单号为空"时进入flag中。
						 */
						for (int i = 0; i < orderList.size(); i++) {
							boolean flag = true;
							map = orderList.get(i);
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							for(String bigOrderCode : bigOrderCodeList){
								if(bigOrderCode.equals(map.get("big_order_code"))){
									flag = false;
									break;
								}
							}
							
							if(flag){
								int orderSellerNumber = 0;
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

								orderCode = map.get("order_code").toString();
								sellerCode = map.get("seller_code").toString();

								//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode,true); // 获取支付宝移动支付链接地址
								// alipaySignMap =
								// apiAlipayMoveProcessService.alipaySign(orderCode);
								// //获取签名后的sign
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}
								apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
								apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
						//		apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("big_order_code")));
								apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
								apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);

								
								if("4497153900010006".equals(map.get("order_status")) ){
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
									}
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}else if("4497153900010002".equals(map.get("order_status")) ){
//									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
										int btnStatus = is_remind_shipment(map);
										if(3 != btnStatus) {
											apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
										}
									}
								}else if("4497153900010003".equals(map.get("order_status")) ){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
									if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
									}else {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
									}
								}else if("4497153900010005".equals(map.get("order_status")) ){
									if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
									}
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
									}
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}
								
								
								orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																						// 和
																						// 商品code、商品单价

								if (orderSellerList != null && !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
			
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												
												String orderType = MapUtils.getString(map, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
												
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																				// 款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}

								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					
					// 按点击数倒序
					Collections.sort(sellerOrderList, new Comparator<ApiSellerOrderListResult>() {
						public int compare(ApiSellerOrderListResult o1, ApiSellerOrderListResult o2) {
							String hits0 = o1.getCreate_time();
							String hits1 = o2.getCreate_time();
							if (compareTime(hits0, hits1)<0) {
								return 1;
							} else if (hits0.equals(hits1)) {
								return 0;
							} else {
								return -1;
							}
						}
					});
					
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
					
				}
//				else {  //待发货、待收货、交易成功进入此判断
//				    //之前使用
//					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode()); // 订单信息
//					if (orderList != null && !orderList.isEmpty()) {
//						for (int i = 0; i < orderList.size(); i++) {
//							int orderSellerNumber = 0;
//							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
//							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
//
//							map = orderList.get(i);
//							orderCode = map.get("order_code").toString();
//							sellerCode = map.get("seller_code").toString();
//
//							alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode); // 获取支付宝移动支付链接地址
//							// alipaySignMap =
//							// apiAlipayMoveProcessService.alipaySign(orderCode);
//							// //获取签名后的sign
//							if (StringUtils.isBlank(alipayValue)) {
//								alipayValue = "";
//							}
//							apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
//							if("".equals(map.get("out_order_code")) || map.get("out_order_code")==null){
//								apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("big_order_code")));
//							}else{
//								apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
//							}
//							apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
//							apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
//							apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
//							apiSellerOrderListResult.setAlipaySign(alipayValue);
//
//							orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
//																					// 和
//																					// 商品code、商品单价
//
//							if (orderSellerList != null && !orderSellerList.isEmpty()) {
//								for (int j = 0; j < orderSellerList.size(); j++) {
//
//									orderSellerMap = orderSellerList.get(j);
//									flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息
//
//									orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
//									orderSellerNumber += orderSellerNumberDouble;
//
//									sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
//									if (sellerList != null && !sellerList.isEmpty()) {
//										for (int k = 0; k < sellerList.size(); k++) {
//											List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
//											ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
//											sellerMap = sellerList.get(k);
//											if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
//												apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
//											}
//											apiSellerListResult.setProduct_code(sellerMap.get("sku_code").toString());
//											apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
//											apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
//											apiSellerListResult.setSell_price(orderSellerMap.get("sku_price").toString());
//
//											standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
//																			// 款型
//											if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
//												for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
//													ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
//													apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
//													apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
//													apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
//												}
//												apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
//											}
//
//											apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型
//
//											sellerResultList.add(apiSellerListResult);
//										}
//										apiSellerOrderListResult.setApiSellerList(sellerResultList);
//									}
//									apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
//
//								}
//							}
//
//							if (flashSalesList != null && !flashSalesList.isEmpty()) {
//								apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
//							} else {
//								apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
//							}
//							sellerOrderList.add(apiSellerOrderListResult);
//						}
//					}
//					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
//					apiOrderListResult.setSellerOrderList(sellerOrderList);
//					return apiOrderListResult;
//				
//				}
			}else if(VersionHelper.checkServerVersion("3.5.44.51")){

				if ("4497153900010001".equals(inputParam.getOrder_status())) {   //order_state为待支付
					
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息

					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							//判断是否需要
							if(isGetOnOrderInfoByOrderCode && map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								break;
							}
							
							bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
						}
						// 去掉相同大订单号
						if (bigOrderCodeList != null) {
							Iterator it = bigOrderCodeList.iterator();
							while (it.hasNext()) {
								String bigO = (String) it.next();
								if (bigOrderCodeNewList.contains(bigO)) {
									it.remove();
								} else {
									bigOrderCodeNewList.add(bigO);
								}
							}
						}

						// 循环所有去重的大订单号
						for (String bigOrderCode : bigOrderCodeNewList) {
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();;
							
							//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
							if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							}

							// 查询大订单表 获取订单上相关信息
							Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
											"select * from oc_orderinfo_upper where big_order_code=:big_order_code",
											new MDataMap("big_order_code",bigOrderCode));
							
							if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
								apiSellerOrderListResult.setOrder_status(inputParam.getOrder_status());
								apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
								apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
								apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);
							}
							
							if("449716200001".equals(map.get("pay_type"))){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
							}
//							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
							
							// 通过大订单号查询oc_orderinfo表, 获取相关信息
							List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
											"select * from oc_orderinfo where big_order_code=:big_order_code and "
													+ "order_status=:order_status and delete_flag=:delete_flag ORDER BY update_time DESC",
											new MDataMap("big_order_code",bigOrderCode, "order_status","4497153900010001","delete_flag", "0"));
							
							int orderSellerNumber = 0;
							
							for (Map<String, Object> mm : orderInfoList) {
								// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));

								orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

								if (orderSellerList != null&& !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												
												String orderType = MapUtils.getString(mm, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
												
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}
								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
							}
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
				
				}else if("".equals(inputParam.getOrder_status())){   //order_state为全部
					boolean waitPay = false;   //判断此用户所有订单是否有待支付的订单
					String orderState = "";
					//查询此用户下所有的订单
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					
					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							map = orderList.get(i);
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							if("4497153900010001".equals(map.get("order_status"))){
								bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
								orderState = "4497153900010001";
								waitPay = true;
							}else{
								int orderSellerNumber = 0;
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

								orderCode = map.get("order_code").toString();
								sellerCode = map.get("seller_code").toString();

								//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode,true); // 获取支付宝移动支付链接地址
								// alipaySignMap =
								// apiAlipayMoveProcessService.alipaySign(orderCode);
								// //获取签名后的sign
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}
								apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
								apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
								apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
								apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
								apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
								apiSellerOrderListResult.setAlipaySign(alipayValue);

								orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																						// 和
																						// 商品code、商品单价

								if (orderSellerList != null && !orderSellerList.isEmpty()) {
									for (int j = 0; j < orderSellerList.size(); j++) {

										orderSellerMap = orderSellerList.get(j);
										flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

										orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
										orderSellerNumber += orderSellerNumberDouble;

										sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
										//查询商品是否有评论
										apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
										if (sellerList != null && !sellerList.isEmpty()) {
											for (int k = 0; k < sellerList.size(); k++) {
												List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												sellerMap = sellerList.get(k);
												if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
													apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
												}
												apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
												apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
												apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
												apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
												//524:添加分类标签
												PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
												String ssc =productInfo.getSmallSellerCode();
												String st="";
												if("SI2003".equals(ssc)) {
													st="4497478100050000";
												}
												else {
													st = WebHelper.getSellerType(ssc);
												}
												//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
												Map productTypeMap = WebHelper.getAttributeProductType(st);
												apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
												
												String orderType = MapUtils.getString(map, "order_type", "");
												if("449715200024".equals(orderType)) {
													BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
													BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
													
													//商品价格 = 商品单价 - 单个商品积分抵扣金额
													apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
													apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
												}else {
													apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
												}
												
												//添加图片
												apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
												
												/*是否生鲜*/
												PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
												
												List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
												
												apiSellerListResult.setLabelsList(labelsList);

												standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																				// 款型
												if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
													for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
														ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
														apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
														apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
														apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
													}
													apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
												}

												apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

									}
								}

								if (flashSalesList != null && !flashSalesList.isEmpty()) {
									apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
								} else {
									apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
						
						if(waitPay){
							// 去掉相同大订单号
							if (bigOrderCodeList != null) {
								Iterator it = bigOrderCodeList.iterator();
								while (it.hasNext()) {
									String bigO = (String) it.next();
									if (bigOrderCodeNewList.contains(bigO)) {
										it.remove();
									} else {
										bigOrderCodeNewList.add(bigO);
									}
								}
							}


							// 循环所有去重的大订单号
							for (String bigOrderCode : bigOrderCodeNewList) {
								List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
								ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();;
								
								//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(bigOrderCode,true); // 获取支付宝移动支付链接地址
								if (StringUtils.isBlank(alipayValue)) {
									alipayValue = "";
								}

								// 查询大订单表 获取订单上相关信息
								Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
												"select * from oc_orderinfo_upper where big_order_code=:big_order_code",
												new MDataMap("big_order_code",bigOrderCode));
								
								if (bigOrderMap != null && !"".equals(bigOrderMap)&& bigOrderMap.size() > 0) {
									apiSellerOrderListResult.setOrder_status(orderState);
									apiSellerOrderListResult.setOrder_code(String.valueOf(bigOrderMap.get("big_order_code")));
									apiSellerOrderListResult.setCreate_time(bigOrderMap.get("create_time").toString());
									apiSellerOrderListResult.setDue_money(bigOrderMap.get("due_money").toString());
									apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
									apiSellerOrderListResult.setAlipaySign(alipayValue);
								}

								if("4497153900010006".equals(map.get("order_status")) ){
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
									}
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}else if("4497153900010002".equals(map.get("order_status")) ){
//									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
										int btnStatus = is_remind_shipment(map);
										if(3 != btnStatus) {
											apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
										}
									}
								}else if("4497153900010003".equals(map.get("order_status")) ){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
									if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
									}else {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
									}
								}else if("4497153900010005".equals(map.get("order_status")) ){
									if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
									}
									if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
									}
									apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
									if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能	
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
									}
								}
								
								
								// 通过大订单号查询oc_orderinfo表, 获取相关信息
								List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
												"select * from oc_orderinfo where big_order_code=:big_order_code and "
														+ "order_status=:order_status and delete_flag=:delete_flag ORDER BY update_time DESC",
												new MDataMap("big_order_code",bigOrderCode, "order_status","4497153900010001","delete_flag", "0"));
								
								int orderSellerNumber = 0;
								for (Map<String, Object> mm : orderInfoList) {
									// apiSellerOrderListResult.setOrder_code(String.valueOf(mm.get("out_order_code")));

									orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价

									if (orderSellerList != null&& !orderSellerList.isEmpty()) {
										for (int j = 0; j < orderSellerList.size(); j++) {

											orderSellerMap = orderSellerList.get(j);
											flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

											orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
											orderSellerNumber += orderSellerNumberDouble;

											sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
											//查询商品是否有评论
											apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
											if (sellerList != null && !sellerList.isEmpty()) {
												for (int k = 0; k < sellerList.size(); k++) {
													List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
													ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
													sellerMap = sellerList.get(k);
													if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
														apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
													}
													apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
													apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
													apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
													apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
													//524:添加分类标签
													PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
													String ssc =productInfo.getSmallSellerCode();
													String st="";
													if("SI2003".equals(ssc)) {
														st="4497478100050000";
													}
													else {
														st = WebHelper.getSellerType(ssc);
													}
													//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
													Map productTypeMap = WebHelper.getAttributeProductType(st);
													apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
													
													String orderType = MapUtils.getString(mm, "order_type", "");
													if("449715200024".equals(orderType)) {
														BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
														BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
														
														//商品价格 = 商品单价 - 单个商品积分抵扣金额
														apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
														apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
													}else {
														apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
													}
													
													//添加图片
													apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
													
													/*是否生鲜*/
													PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
													
													List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
													
													apiSellerListResult.setLabelsList(labelsList);

													standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取// 尺码  和  款型
													if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
														for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
															ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
															apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
															apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
															apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
														}
														apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
													}

													apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

													sellerResultList.add(apiSellerListResult);
												}
												apiSellerOrderListResult.setApiSellerList(sellerResultList);
											}
											apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

										}
									}
									if (flashSalesList != null && !flashSalesList.isEmpty()) {
										apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
									} else {
										apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
									}
								}
								sellerOrderList.add(apiSellerOrderListResult);
							}
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					
					// 按点击数倒序
					Collections.sort(sellerOrderList, new Comparator<ApiSellerOrderListResult>() {
						public int compare(ApiSellerOrderListResult o1, ApiSellerOrderListResult o2) {
							String hits0 = o1.getCreate_time();
							String hits1 = o2.getCreate_time();
							if (compareTime(hits0, hits1)<0) {
								return 1;
							} else if (hits0.equals(hits1)) {
								return 0;
							} else {
								return -1;
							}
						}
					});
					
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
					
				} else{   //待发货、待收货、交易成功、交易失败进入此判断
				    //之前使用
					orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
					if (orderList != null && !orderList.isEmpty()) {
						for (int i = 0; i < orderList.size(); i++) {
							int orderSellerNumber = 0;
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

							map = orderList.get(i);
							//判断是否根据大订单号查询订单信息
							if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
								continue;
							}
							
							orderCode = map.get("order_code").toString();
							sellerCode = map.get("seller_code").toString();

							//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode,true); // 获取支付宝移动支付链接地址
							// alipaySignMap =
							// apiAlipayMoveProcessService.alipaySign(orderCode);
							// //获取签名后的sign
							if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							}
							apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
							apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
							apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
							apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
							apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
							apiSellerOrderListResult.setAlipaySign(alipayValue);

							
							if("4497153900010006".equals(map.get("order_status")) ){
								apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
								if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
								}
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
								}
							}else if("4497153900010002".equals(map.get("order_status")) ){
//								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
									int btnStatus = is_remind_shipment(map);
									if(3 != btnStatus) {
										apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
									}
								}
							}else if("4497153900010003".equals(map.get("order_status")) ){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
								if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
								}else {
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
								}
							}else if("4497153900010005".equals(map.get("order_status")) ){
								if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
								}
								if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
								}
								apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
								if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
								}
								
							}
							
							
							orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																					// 和
																					// 商品code、商品单价

							if (orderSellerList != null && !orderSellerList.isEmpty()) {
								for (int j = 0; j < orderSellerList.size(); j++) {

									orderSellerMap = orderSellerList.get(j);
									flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

									orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
									orderSellerNumber += orderSellerNumberDouble;

									sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
									//查询商品是否有评论
									apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
									if (sellerList != null && !sellerList.isEmpty()) {
										for (int k = 0; k < sellerList.size(); k++) {
											List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
											ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
											sellerMap = sellerList.get(k);
											if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
												apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
											}
											apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
											apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
											apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
											apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
											//524:添加分类标签
											PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
											String ssc =productInfo.getSmallSellerCode();
											String st="";
											if("SI2003".equals(ssc)) {
												st="4497478100050000";
											}
											else {
												st = WebHelper.getSellerType(ssc);
											}
											//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
											Map productTypeMap = WebHelper.getAttributeProductType(st);
											apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
											
											String orderType = MapUtils.getString(map, "order_type", "");
											if("449715200024".equals(orderType)) {
												BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
												BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
												
												//商品价格 = 商品单价 - 单个商品积分抵扣金额
												apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
												apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
											}else {
												apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
											}
											
											//添加图片
											apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
											
											/*是否生鲜*/
											PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
											
											List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
											
											apiSellerListResult.setLabelsList(labelsList);

											standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																			// 款型
											if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
												for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
													ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
													apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
													apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
													apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
												}
												apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
											}

											apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

											sellerResultList.add(apiSellerListResult);
										}
										apiSellerOrderListResult.setApiSellerList(sellerResultList);
									}
									apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

								}
							}

							if (flashSalesList != null && !flashSalesList.isEmpty()) {
								apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
							} else {
								apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
							}
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
					apiOrderListResult.setSellerOrderList(sellerOrderList);
					return apiOrderListResult;
				
				}
			} else {    //之前使用
				orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
				if (orderList != null && !orderList.isEmpty()) {
					for (int i = 0; i < orderList.size(); i++) {
						int orderSellerNumber = 0;
						List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
						ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();

						map = orderList.get(i);
						
						//判断是否根据大订单号查询订单信息
						if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
							continue;
						}
						
						
						orderCode = map.get("order_code").toString();
						sellerCode = map.get("seller_code").toString();

						//alipayValue = apiAlipayMoveProcessService.alipayMoveParameter(orderCode,true); // 获取支付宝移动支付链接地址
						// alipaySignMap =
						// apiAlipayMoveProcessService.alipaySign(orderCode);
						// //获取签名后的sign
						if (StringUtils.isBlank(alipayValue)) {
							alipayValue = "";
						}
						apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
						apiSellerOrderListResult.setOrder_code(String.valueOf(map.get("out_order_code")));
						apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
						apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
						apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
						apiSellerOrderListResult.setAlipaySign(alipayValue);

						if("4497153900010006".equals(map.get("order_status")) ){
							apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
							if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
							}
							if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
							}
						}else if("4497153900010002".equals(map.get("order_status")) ){
//							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004")));//取消发货
							if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
								int btnStatus = is_remind_shipment(map);
								if(3 != btnStatus) {
									apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
								}
							}
						}else if("4497153900010003".equals(map.get("order_status")) ){
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
							if(AppVersionUtils.compareTo(TelRefundVersion, versionApp)<0) {
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
							}else {
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
							}
						}else if("4497153900010005".equals(map.get("order_status")) ){
							
							if(DbUp.upTable("nc_order_evaluation").count("order_code",orderCode)<1){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080009",bConfig("familyhas.define_4497477800080009")));//评价晒单
							}
							if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
							}
							apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
							if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
							}
						}else if("4497153900010001".equals(map.get("order_status")) ){
							
							if("449716200001".equals(map.get("pay_type"))){
								apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
							}
							
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
						}
						
						
						
						orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
																				// 和
																				// 商品code、商品单价

						if (orderSellerList != null && !orderSellerList.isEmpty()) {
							for (int j = 0; j < orderSellerList.size(); j++) {

								orderSellerMap = orderSellerList.get(j);
								flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息

								orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
								orderSellerNumber += orderSellerNumberDouble;

								sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
								//查询商品是否有评论
								apiSellerOrderListResult.setIs_comment(Is_comment(paramMap,apiSellerOrderListResult.getOrder_code()));
								if (sellerList != null && !sellerList.isEmpty()) {
									for (int k = 0; k < sellerList.size(); k++) {
										List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
										ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
										sellerMap = sellerList.get(k);
										if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
											apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
										}
										apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
										apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
										apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
										apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
										//524:添加分类标签
										PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(sellerMap.get("product_code").toString()));
										String ssc =productInfo.getSmallSellerCode();
										String st="";
										if("SI2003".equals(ssc)) {
											st="4497478100050000";
										}
										else {
											st = WebHelper.getSellerType(ssc);
										}
										//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
										Map productTypeMap = WebHelper.getAttributeProductType(st);
										apiSellerListResult.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
										
										String orderType = MapUtils.getString(map, "order_type", "");
										if("449715200024".equals(orderType)) {
											BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
											BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱
											
											//商品价格 = 商品单价 - 单个商品积分抵扣金额
											apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
											apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
										}else {
											apiSellerListResult.setSell_price(String.valueOf(orderSellerMap.get("show_price")));
										}
										
										//添加图片
										apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
										
										/*是否生鲜*/
										PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());
										
										List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
										
										apiSellerListResult.setLabelsList(labelsList);

										standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString()); // 截取 尺码 和
																		// 款型
										if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
											for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
												ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
												apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
												apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
												apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
											}
											apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
										}

										apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型

										sellerResultList.add(apiSellerListResult);
									}
									apiSellerOrderListResult.setApiSellerList(sellerResultList);
								}
								apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);

							}
						}

						if (flashSalesList != null && !flashSalesList.isEmpty()) {
							apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
						} else {
							apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
						}
						sellerOrderList.add(apiSellerOrderListResult);
					}
				}
				apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
				apiOrderListResult.setSellerOrderList(sellerOrderList);
				return apiOrderListResult;
			}

		}
		// https://wappaygw.alipay.com/service/rest.htm

		return null;
	}
	/**
	 * 查询订单是否评价
	 * @param paramMap
	 * @param order
	 * @return
	 */
	private Integer Is_comment(MDataMap paramMap,String order) {
		paramMap.put("manage_code", getManageCode());
		paramMap.put("order_code", order);
		paramMap.put("order_name", getUserCode());
		int dataCount = DbUp.upTable("nc_order_evaluation").dataCount("manage_code=:manage_code and order_code=:order_code and order_name=:order_name", paramMap);
		return dataCount;
	}
	
	/**
	 * 比较两个时间
	 * 时间格式：2014-12-02 20:14:10
	 * <br>大于结束时间返回正数，等于 0，小于 负数
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public synchronized static int compareTime(String start_time,String end_time){
		try {
			Date date1=DateUtil.sdfDateTime.parse(start_time);
			Date date2=DateUtil.sdfDateTime.parse(end_time);
			return date1.compareTo(date2);
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**
	 * 用户是否可以提醒发货
	 * @return
	 */
	private Integer is_remind_shipment(Map<String, Object> orderinfo) {
		Integer isRemind = 3;//按钮展示，但不可使用
		String orderCode  = String.valueOf(orderinfo.get("order_code"));
		String pay_type = String.valueOf(orderinfo.get("pay_type"));
		String orderCreateTime  = String.valueOf(orderinfo.get("create_time"));
		if(StringUtils.isNotBlank(orderCode)) {
			MDataMap one = DbUp.upTable("oc_order_remind_shipment").one("order_code",orderCode);
			if(null != one) {
				String remindTime = one.get("remind_time");
				try {
					int hoursBetween = DateHelper.hoursBetween(remindTime, DateHelper.upNow());
					if(hoursBetween >= PRE_REMINDORDERSHIPMENT_LATER) {
						isRemind = 1;
					} else {
						isRemind = 2;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				//如果没有数据（用户没有点击过提醒发货），拿订单创建时间判断，订单创建时间24小时之后才可以提醒发货
				try {
					String compareTime = "";
					//判断支付类型，除货到付款外都当做在线支付
					if ("449716200002".equals(pay_type)) {//货到付款
						compareTime = orderCreateTime;
					} else {
						MDataMap orderPayInfo = DbUp.upTable("oc_order_pay").one("order_code",orderCode);
						if(null != orderPayInfo) {
							compareTime = orderPayInfo.get("create_time");//订单支付时间
						} else {//如果订单状态是代发货，且是在线支付的订单，如果没有支付信息，则以订单创建时间为准
							compareTime = orderCreateTime;//订单创建时间
						}
					}
					int hoursBetween = DateHelper.hoursBetween(compareTime, DateHelper.upNow());
					if(hoursBetween >= CREATEORDER_LATER) {
						isRemind = 1;
					} 
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return isRemind;
	}
	
	/**
	 * 对比版本
	 * appVersion > compareVersion   返回正数
	 * appVersion = compareVersion   返回0
	 * appVersion < compareVersion   返回负数
	 * @param appVersion
	 * @param compareVersion
	 */
	public static Integer compareAppVersion ( String appVersion ,String compareVersion) {
		if(StringUtils.isBlank(appVersion)) {
			appVersion = "";
		}
		if(StringUtils.isBlank(compareVersion)) {
			compareVersion = "";
		}
		return AppVersionUtils.compareTo(appVersion, compareVersion);
		//return appVersion.compareTo(compareVersion);
	}
}
