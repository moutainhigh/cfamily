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
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiOrderListInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.familyhas.api.result.ApiOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.service.OrderDetailService;
import com.cmall.familyhas.util.MoneyFormatUtil;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderList;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrder;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelbean.ActivityAgent;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceActivityAgent;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 5.2.6 全通路订单列表接口
 * @author cc
 *
 */
public class ApiOrderListForAllChannel extends RootApiForToken<ApiOrderListResult, ApiOrderListInput>{

	/**
	 * 创建订单之后 24小时  之后  才能提醒发货
	 */
	public static final Integer CREATEORDER_LATER = 24;

	/**
	 * 上次提醒发货 12小时之后  才能提醒发货
	 */
	public static final Integer PRE_REMINDORDERSHIPMENT_LATER = 12;
	
	public static final String TelRefundVersion = "5.2.2";
	
	private static LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	@SuppressWarnings("unchecked")
	@Override
	public ApiOrderListResult Process(ApiOrderListInput inputParam, MDataMap mRequestMap) {
		ApiOrderListResult apiOrderListResult = this.getResult(inputParam, mRequestMap);
		//558需求新增商品标签自定义位置
		for(ApiSellerOrderListResult result:apiOrderListResult.getSellerOrderList()){
			for(ApiSellerListResult info:result.getApiSellerList()){
				info.setLabelsInfo(new ProductLabelService().getLabelInfoList(info.getProduct_code()));
				//562版本对于商品列表标签做版本兼容处理
				String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
				if(appVersion.compareTo("5.6.2")<0){
					Iterator<PlusModelProductLabel> iter = info.getLabelsInfo().iterator();
					while (iter.hasNext()) {
						PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
						if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
							iter.remove();
						}
					}
				}
			}
		}
		this.changeStatusOfGroupBuying(apiOrderListResult);
		this.addSupplyAfterSaleButton(apiOrderListResult);
		this.addReturnGoodsButton(apiOrderListResult);
		return apiOrderListResult;
	}
	
	/**
	 * 5.6.6 版本 新增退货详情按钮   by wangmeng
	 * 
	 */
	private void addReturnGoodsButton(ApiOrderListResult apiOrderListResult) {
		try {
			String appVersion = "";
			MDataMap apiClient = getApiClient();
			if(apiClient != null && !apiClient.isEmpty()) {
				appVersion = apiClient.get("app_vision");
			}
			if(!"".equals(appVersion) && compareAppVersion(appVersion, "5.6.6") < 0) {
				return ;
			}
			List<ApiSellerOrderListResult> sellerOrderList = apiOrderListResult.getSellerOrderList();
			for(ApiSellerOrderListResult result : sellerOrderList) {
				//过滤京东订单
				MDataMap ocOrderinfo = DbUp.upTable("oc_orderinfo").one("order_code", result.getOrder_code());
				if(ocOrderinfo==null || ocOrderinfo.get("small_seller_code").equalsIgnoreCase("SF031JDSC")) {
					continue;
				}
				if(ocOrderinfo.get("order_status").equalsIgnoreCase("4497153900010006") || ocOrderinfo.get("order_status").equalsIgnoreCase("4497153900010008")) {
					MDataMap dataSqlOne = DbUp.upTable("oc_return_money").one("order_code", result.getOrder_code());
					if(dataSqlOne!=null) {
						result.getOrderButtonList().add(new Button("4497477800080025","退款详情",1));
					}	
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 546版本新增售后功能，添加版本号控制，目前不做LD品。
	 * @param apiOrderListResult
	 */
	private void addSupplyAfterSaleButton(ApiOrderListResult apiOrderListResult) {
		AfterSaleService as  = new AfterSaleService(); 
		MDataMap apiClient = getApiClient();
		String appVersion = "";
		if(apiClient != null && !apiClient.isEmpty()) {
			appVersion = apiClient.get("app_vision");
		}
		if("".equals(appVersion)) {
			appVersion = "5.4.60";
		}
		if(compareAppVersion(appVersion, "5.4.60")<0) {
			return ;
		}
		List<ApiSellerOrderListResult> sellerOrderList = apiOrderListResult.getSellerOrderList();
		aa:for(ApiSellerOrderListResult result : sellerOrderList) {
			boolean flag = false;
			String orderCode = result.getOrder_code();
			String orderStatus = result.getOrder_status();
			if("4497153900010002".equals(orderStatus)) {//新增取消发货按钮
				//需要判断是否是京东商品或者是新团商品，JD商品与拼团商品不允许取消发货。
				boolean flag2 = this.getCheck(orderCode,appVersion);//查询订单是否能取消发货，如果能，返回true
				if(flag2) {
					result.getOrderButtonList().add(new Button("4497477800080004",bConfig("familyhas.define_4497477800080004"),1));
				}
			}
			if(orderCode.contains("DD")||orderCode.contains("HH")) {
				List<MDataMap> maps = DbUp.upTable("oc_orderdetail").queryByWhere("order_code",orderCode,"gift_flag","1");
				for(MDataMap map : maps) {
					String  sku_code = map.get("sku_code");
					flag = as.checkIfAllowAfterSale(orderCode, sku_code, "");
					if(flag) {
						result.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008"),1));
						continue aa;
					}
				}
			}else {//LD订单 加申请售后按钮
				if(compareAppVersion(appVersion, "5.5.0") >= 0) {
					boolean flagLd = as.checkAfterSaleAllow(orderCode); 
					if(flagLd) {
						result.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008"),1));
						result.getApiSellerList().get(0).setOrderSeq("1");
					}
				}
			}
		}
		/**
		 * 新增取消发货原因
		 */
		//================取消发货原因begin=================================
		List<MDataMap> reasonList = DbUp.upTable("oc_return_goods_reason").queryByWhere("after_sales_type","449747660007","status","449747660005");
		if(reasonList!=null){
			for (MDataMap mDataMap : reasonList) {
				String reason_code = mDataMap.get("return_reson_code");
				String reson_content = mDataMap.get("return_reson");
				Reason reason = new Reason(reason_code, reson_content);
				apiOrderListResult.getReasonList().add(reason);
			}
		}
	}

	/**
	 * 判断订单属性,校验是否为可以在线取消发货，如果是JD东单，或者是拼团未成功订单，则不允许在线取消发货
	 * @param orderCode
	 * @return
	 */
	private boolean getCheck(String orderCode,String appVersion) {
		MDataMap collageOrder = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		//检查是否拼团成功
		if(collageOrder != null&& !collageOrder.isEmpty()) {
			String collageCode = collageOrder.get("collage_code");
			MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
			String collageStatus = collageInfo.get("collage_status");
			if(!"449748300002".equals(collageStatus)) {//不为拼团成功，返回失败
				return false;
			}
		}
		//检查是否是JD订单
		MDataMap jdOrderDetail = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		String smallSellerCode = "";
		if(jdOrderDetail != null && !jdOrderDetail.isEmpty()) {
			smallSellerCode =  jdOrderDetail.get("small_seller_code");
			if(Constants.SMALL_SELLER_CODE_JD.equals(smallSellerCode)) {
				return false;
			}
		}
		boolean hasCancelling = DbUp.upTable("oc_order_cancel_h").count("order_code", orderCode) > 0; // 是否订单正在异步取消
		// 正在取消中的订单不再展示取消发货按钮
		if(hasCancelling) {//有取消中的订单。
			return false;
		}
		if(AppVersionUtils.compareTo("5.2.6", appVersion)>=0) {
			return false;
		}
		String uc_seller_type = WebHelper.getSellerType(smallSellerCode);
		if("".equals(uc_seller_type)) {
			if("SI2003".equals(smallSellerCode)) {
				uc_seller_type = "4497478100050001";
			}
		}
		if(StringUtils.equals(uc_seller_type, "4497478100050001") || StringUtils.equals(uc_seller_type, "4497478100050004") || StringUtils.equals(uc_seller_type, "4497478100050005")){
			return true;
		}
		return false;
	}

	/**
	 * 处理订单状态
	 * @param apiOrderListResult
	 */
	private void changeStatusOfGroupBuying(ApiOrderListResult apiOrderListResult){
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(appVersion.compareTo("5.4.0")<0){
			return;
		}
		List<ApiSellerOrderListResult> sellerOrderList = apiOrderListResult.getSellerOrderList();
		for(ApiSellerOrderListResult apiSellerOrderListResult : sellerOrderList){
			//校验订单原状态，如果交易失败的订单不做处理
			String orderStatus = apiSellerOrderListResult.getOrder_status();
			if("4497153900010006".equals(orderStatus)) {//失败订单不做处理
				continue;
			}
			String order_code = apiSellerOrderListResult.getOrder_code();
			//根据order_code去查询拼团状态表：systemcenter.sc_event_collage_item
			MDataMap orderPin = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",order_code);
			if(orderPin == null || orderPin.isEmpty()){//此单不是拼团订单
				apiSellerOrderListResult.setOrder_type("449715200025");//常规订单（目前为非拼团订单）
				continue;
			}
			apiSellerOrderListResult.setCollageCode(orderPin.get("collage_code"));
			apiSellerOrderListResult.setOrder_type("449715200026");//拼团类型订单
			//如果是拼团订单，根据拼团编码collage_code 去systemcenter.sc_event_collage中查询拼团状态
			String collage_code = orderPin.get("collage_code");
			MDataMap pinStatus = DbUp.upTable("sc_event_collage").one("collage_code",collage_code);
			//校验拼团状态表中查询是否为空
			if(pinStatus == null || pinStatus.isEmpty()){
				continue;
			}
			//collage_status 状态值 拼团中：449748300001，拼团成功：449748300002，拼团失败：449748300003
			String collage_status = pinStatus.get("collage_status");
			apiSellerOrderListResult.setGroup_buying_status(collage_status);//设置拼团状态
			List<Button> buttonList = apiSellerOrderListResult.getOrderButtonList();
			//如果状态值为拼团中，需要再加一个字段判断查几人成团。
			if("449748300001".equals(collage_status)){
				//根据event_code去表systemcenter.sc_event_info查询几人成团
				String event_code = pinStatus.get("event_code");
				MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code",event_code);
				Integer count = Integer.parseInt(eventInfo.get("collage_person_count"));
				//根据拼团编号去systemcenter.sc_event_collage_item表中查询该团确认的有几人。is_confirm = 449748320002 已确认，也就是已付款用户才是有效拼团状态
				Integer pinCount = DbUp.upTable("sc_event_collage_item").count("collage_code",collage_code,"is_confirm","449748320002");
				//
				Integer needCount = count - pinCount;
				apiSellerOrderListResult.setNeedCount(needCount);
				//拼团中订单需要提出提醒发货按钮
				for(Button button : buttonList) {
					String buttonCode = button.getButtonCode();
					if("4497477800080012".equals(buttonCode)) {
						button.setButtonStatus(3);//状态置为不可用
					}
				}
				//设置订单列表中按钮,拼团中按钮需要有邀请好友按钮。
				Button inviteFriendsButton = new Button();
				inviteFriendsButton.setButtonCode("4497477800080017");
				inviteFriendsButton.setButtonTitle("邀请好友");
				inviteFriendsButton.setButtonStatus(1);
				buttonList.add(inviteFriendsButton);
			}else if("449748300002".equals(collage_status)){
				//设置订单列表中按钮,拼团中按钮需要有拼团详情按钮。
				Button inviteFriendsButton = new Button();
				inviteFriendsButton.setButtonCode("4497477800080016");
				inviteFriendsButton.setButtonTitle("拼团详情");
				inviteFriendsButton.setButtonStatus(1);
				buttonList.add(inviteFriendsButton);
			}
			apiSellerOrderListResult.setOrderButtonList(buttonList);
		}
	}
	
	/**
	 * 5.4.0版本，需要处理订单返回结果中拼单订单状态值。次方法为原接口方法，需要外部嵌套一层，处理返回值。
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiOrderListResult getResult(ApiOrderListInput inputParam, MDataMap mRequestMap){

		
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		OrderService orderService = new OrderService();
		ProductService productService = new ProductService();
		ProductLabelService productLabelService = new ProductLabelService();
		
		ApiOrderListResult apiOrderListResult = new ApiOrderListResult();
		MDataMap paramMap = new MDataMap();
		inputParam.setBuyer_code(getUserCode());
		
		List<Map<String, Object>> ldOrderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> orderSellerList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sellerList = new ArrayList<Map<String, Object>>();
		List<MDataMap> flashSalesList = new ArrayList<MDataMap>();

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> orderSellerMap = new HashMap<String, Object>();		
		List<ApiSellerOrderListResult> sellerOrderList = new ArrayList<ApiSellerOrderListResult>();
		
		String alipayValue = null;
		String orderCode = null;
		List<String> bigOrderCodeList = new ArrayList<String>();
		List<String> bigOrderCodeNewList = new ArrayList<String>();
		boolean isGetOnOrderInfoByOrderCode = false;
		boolean isPage = false;//是否分页(5.2.6需要获取LD的订单列表，不能通过查询分页)
		
		int count = 0;  //订单总数
		int countPage = 0;  //总页数
		int orderSellerNumberDouble = 0;
						
		if (!"".equals(inputParam.getBuyer_code()) && !"".equals(inputParam.getNextPage())) {
			/**
			 * 分页计算
			 */
			count = orderService.orderCountNew(inputParam.getBuyer_code(),inputParam.getOrder_status());// 统计订单总数
			if (count != 0) {
				countPage = count / 10; // 总页数 count
				apiOrderListResult.setCountPage(countPage + 1);
			}
			
			//判断是否是获取指定单个订单信息
			if(null != inputParam.getBig_orderCode() && inputParam.getBig_orderCode().length() > 0 ) {
				isGetOnOrderInfoByOrderCode = true;
				isPage = false;
				apiOrderListResult.setCountPage(1);
			}
			/**
			 * 获取需要过滤的订单类型
			 */
			String orderTypeQueryWhere = orderService.getNotInOrderType();
			
			//=============================获取订单列表begin=======================================			
			/**
			 * 待付款的按照大订单显示(OS)
			 */
			if("4497153900010001".equals(inputParam.getOrder_status())){
				orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息								
				if(!isGetOnOrderInfoByOrderCode) {
					//订单分页
					Map<String, Object> pageOrder = setPageOrderList(appVersion, orderList, inputParam.getNextPage(), inputParam.getBuyer_code(), "01");
					if(pageOrder == null || pageOrder.get("orderList") == null) {
						apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
						apiOrderListResult.setSellerOrderList(sellerOrderList);
						return apiOrderListResult;
					}
					orderList = (List<Map<String, Object>>)pageOrder.get("orderList");
					int toNum = Integer.parseInt(pageOrder.get("toNum").toString());
					if(toNum > 0) {
						count = count + toNum;
						countPage = count / 10; // 总页数 count
						apiOrderListResult.setCountPage(countPage + 1);
					}
				}
				
				ldOrderList = new ArrayList<Map<String, Object>>();
				if (orderList != null && !orderList.isEmpty()) {
					for (int i = 0; i < orderList.size(); i++) {
						map = orderList.get(i);
						/**
						 * 如果是LD订单，big_order_code=order_code
						 */
						if(!isGetOnOrderInfoByOrderCode && map.get("source") != null && "to".equals(map.get("source"))) {
							//LD订单，需要显示大订单号，特殊处理	
							ldOrderList.add(map);
							continue;
						}
						//是否是获取指定单个订单信息
						if(isGetOnOrderInfoByOrderCode && map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
							bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
							break;
						}
						bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
					}
					bigOrderCodeNewList = removeDuplicateElement(bigOrderCodeList);//去重					
					for (String bigOrderCode : bigOrderCodeNewList) {
						//=====================LD订单end========================================
						List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
						ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
						alipayValue = "";
						// 查询大订单表 获取订单上相关信息
						Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
										"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
										new MDataMap("big_order_code",bigOrderCode));						
						setSellerOrderListByBigOrder1(apiSellerOrderListResult, bigOrderMap, alipayValue, inputParam.getOrder_status(), inputParam.getDeviceType());
						// 通过大订单号查询oc_orderinfo表, 获取相关信息     
						List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
										"select * from oc_orderinfo where big_order_code=:big_order_code and "
												+ " delete_flag=:delete_flag and order_source not in('449715190014','449715190037') ORDER BY update_time DESC",    //order_status=:order_status and
										new MDataMap("big_order_code",bigOrderCode, "delete_flag", "0"));
						
						int orderSellerNumber = 0;						
						for (Map<String, Object> mm : orderInfoList) {
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
											ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
											//商品信息
											apiSellerListResult = setSellerList(apiSellerOrderListResult, mm, sellerList.get(k), orderSellerMap, orderSellerNumberDouble, orderService, productService, productLabelService, appVersion);
											sellerResultList.add(apiSellerListResult);
										}
										apiSellerOrderListResult.setApiSellerList(sellerResultList);
									}
									apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
								}
							}							
							setIfFlashSales(apiSellerOrderListResult, flashSalesList); //设置闪购信息
						}
						if("449716200001".equals(map.get("pay_type"))){
							apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
						}
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
						sellerOrderList.add(apiSellerOrderListResult);
						
						 
					}
					//=============================按照大订单号循环end=========================================
					if(ldOrderList != null && ldOrderList.size() > 0) {
						//处理LD订单
						List<ApiSellerOrderListResult> ldSellerOrderList1 = setWaitPayLDOrder(ldOrderList, appVersion, orderService, productLabelService);
						sellerOrderList.addAll(ldSellerOrderList1);
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
			/**
			 * 待发货、待收货、交易成功都是按照小订单显示(DD)
			 */
			else if("4497153900010002".equals(inputParam.getOrder_status()) 
					|| "4497153900010003".equals(inputParam.getOrder_status())
					|| "4497153900010004".equals(inputParam.getOrder_status()) 
					|| "4497153900010005".equals(inputParam.getOrder_status()) 
					|| "4497153900010006".equals(inputParam.getOrder_status())) {
				String sql = "select count(1) as cnt from ordercenter.oc_orderinfo where buyer_code=:buyer_code  and delete_flag=:delete_flag and order_status=:order_status and seller_code=:seller_code and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') ) and order_source not in('449715190014','449715190037') and order_type not in ("+orderTypeQueryWhere+")  ";
				if(compareAppVersion(appVersion, "5.0.6") < 0 && "4497153900010005".equals(inputParam.getOrder_status())){  // 5.0.6之前的版本保持不变
					// 版本号小于5.0.6走原来的逻辑
					sql = "select count(1) as cnt from ordercenter.oc_orderinfo where"
							+ " buyer_code=:buyer_code  and delete_flag=:delete_flag and order_status=:order_status and seller_code=:seller_code and order_source not in('449715190014','449715190037') and order_type not in ("+orderTypeQueryWhere+") "
							+ " and order_code not in (select order_code from newscenter.nc_order_evaluation where order_name=:buyer_code and manage_code=:seller_code )"
							+ " and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') )";
					orderList = orderService.orderInformationSmall(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
				}else{
					// 5.0.6以后走逻辑，不再屏蔽已评价的订单
					orderList = orderService.orderInformationSmallV2(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
				}
				
				Map<String, Object> mapc=DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("buyer_code", inputParam.getBuyer_code(),"order_status", inputParam.getOrder_status(),"delete_flag", "0","seller_code", getManageCode()));
				int cc=0;	//订单总数
				if(mapc!=null&&!mapc.isEmpty()){
					cc=((Long)mapc.get("cnt")).intValue();
				}				
				if(!isGetOnOrderInfoByOrderCode) {
					//订单分页
					String order_status = "";
					if("4497153900010002".equals(inputParam.getOrder_status())){
						order_status = "02";
					} else if("4497153900010003".equals(inputParam.getOrder_status())) {
						order_status = "03";
					} else if("4497153900010005".equals(inputParam.getOrder_status())) {
						order_status = "05";
					} else if("4497153900010006".equals(inputParam.getOrder_status())) {
						order_status = "04";
					}
					Map<String, Object> pageOrder = setPageOrderList(appVersion, orderList, inputParam.getNextPage(), inputParam.getBuyer_code(), order_status);
					if(pageOrder == null || pageOrder.get("orderList") == null) {
						apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
						apiOrderListResult.setSellerOrderList(sellerOrderList);
						return apiOrderListResult;
					}
					orderList = (List<Map<String, Object>>)pageOrder.get("orderList");
					int toNum = Integer.parseInt(pageOrder.get("toNum").toString());					
					if(toNum > 0) {
						cc = cc + toNum;
					}
				} 
				//判断是否需要显示单个订单信息
				if(isGetOnOrderInfoByOrderCode ) {
					apiOrderListResult.setCountPage(1);
				} else {
					countPage = Double.valueOf(Math.ceil(Double.valueOf(cc)/10d)).intValue();
					apiOrderListResult.setCountPage(countPage);
				}
				if (orderList != null && !orderList.isEmpty()) {
					ldOrderList = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < orderList.size(); i++) {						
						int orderSellerNumber = 0;
						List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
						ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
						map = orderList.get(i);	
						//LD订单特殊处理
						if(!isGetOnOrderInfoByOrderCode && map.get("source") != null && "to".equals(map.get("source"))) {
							ldOrderList.add(map);
							continue;
						}
						//判断是否需要
						if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
							continue;
						}
						//设置订单按钮信息
						apiSellerOrderListResult = setButtonList(map, appVersion, alipayValue);
						orderSellerList = orderService.orderSellerNumber(map); // 订单商品数量(每一个订单数量加一起的总数)
						if (orderSellerList != null && !orderSellerList.isEmpty()) {
							for (int j = 0; j < orderSellerList.size(); j++) {
								orderSellerMap = orderSellerList.get(j);
								flashSalesList = orderService.flashSales(orderSellerMap.get("sku_code").toString()); // 判断是否为闪够信息
								orderSellerNumberDouble = Integer.parseInt(orderSellerMap.get("sku_num").toString());
								orderSellerNumber += orderSellerNumberDouble;
								sellerList = orderService.sellerInformation(orderSellerMap.get("sku_code").toString()); // 查询商品信息
								if (sellerList != null && !sellerList.isEmpty()) {
									for (int k = 0; k < sellerList.size(); k++) {
										ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
										apiSellerListResult = setSellerList(apiSellerOrderListResult, map, sellerList.get(k), orderSellerMap, orderSellerNumberDouble, orderService, productService, productLabelService, appVersion);
										sellerResultList.add(apiSellerListResult);										
									}
									apiSellerOrderListResult.setApiSellerList(sellerResultList);
								}
								apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
							}
						}
						setIfFlashSales(apiSellerOrderListResult, flashSalesList); //设置闪购信息
						sellerOrderList.add(apiSellerOrderListResult);
					}
					if(ldOrderList != null && ldOrderList.size() > 0) {						
						List<ApiSellerOrderListResult> ldSellerOrderList = setWaitPayLDOrder(ldOrderList, appVersion, orderService, productLabelService);
						sellerOrderList.addAll(ldSellerOrderList);						
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
				
				// 5.6.6 版本支持推广赚
				if(AppVersionUtils.compareTo(appVersion, "5.6.60") >= 0 || StringUtils.isBlank(appVersion)) {
					// 已完成的里面增加推广赚按钮
					if("4497153900010005".equals(inputParam.getOrder_status())) {
						setTgzButtonList(sellerOrderList);
					}
				}
				
				apiOrderListResult.setSellerOrderList(sellerOrderList);
				return apiOrderListResult;
			}
			/**
			 * 全部(都按照大订单号展示)
			 */
			else {
				boolean waitPay = false;   //判断此用户所有订单是否有待支付     和   外部订单号为空   的订单
				if (StringUtils.isBlank(inputParam.getOrder_status())) {
					int countPayment =  orderService.orderCountPayment(inputParam.getBuyer_code());  //统计用户下待支付订单总数
					Map<String, Object> mapc=DbUp.upTable("oc_orderinfo").dataSqlOne("select count(1) cnt from  oc_orderinfo where order_status<>'4497153900010001' and buyer_code=:buyer_code and delete_flag=:delete_flag and seller_code=:seller_code and (org_ord_id = '' or (org_ord_id != '' and order_status != '4497153900010002') ) and order_source not in('449715190014','449715190037') and order_type not in ("+orderTypeQueryWhere+") ", new MDataMap("delete_flag", "0","buyer_code",inputParam.getBuyer_code(),"seller_code",getManageCode()));
					int cc = 0;	//订单总数
					if(mapc != null && !mapc.isEmpty()){
						cc = ((Long)mapc.get("cnt")).intValue();
					}
					count = cc + countPayment;
					//判断是否需要显示单个订单信息
					if(isGetOnOrderInfoByOrderCode ) {
						apiOrderListResult.setCountPage(1);
					} else {
						countPage = Double.valueOf(Math.ceil(Double.valueOf(count)/10d)).intValue();
						apiOrderListResult.setCountPage(countPage);
					}					
				}
				orderList = orderService.orderInformation(inputParam.getBuyer_code(),inputParam.getOrder_status(), inputParam.getNextPage(),getManageCode(),isPage); // 订单信息
				if(!isGetOnOrderInfoByOrderCode) {
					//订单分页
					Map<String, Object> pageOrder = setPageOrderList(appVersion, orderList, inputParam.getNextPage(), inputParam.getBuyer_code(), "");
					if(pageOrder == null || pageOrder.get("orderList") == null) {
						apiOrderListResult.setNowPage(Integer.parseInt(inputParam.getNextPage()));
						apiOrderListResult.setSellerOrderList(sellerOrderList);
						return apiOrderListResult;
					}
					orderList = (List<Map<String, Object>>)pageOrder.get("orderList");
					int toNum = Integer.parseInt(pageOrder.get("toNum").toString());
					if(toNum > 0) {
						count = count + toNum;
						countPage = count / 10; // 总页数 count
						apiOrderListResult.setCountPage(countPage + 1);
					}
				}
				
				if (orderList != null && !orderList.isEmpty()) {
					ldOrderList = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < orderList.size(); i++) {
						map = orderList.get(i);
						//LD订单特殊处理
						if(!isGetOnOrderInfoByOrderCode && map.get("source") != null && "to".equals(map.get("source"))) {
							ldOrderList.add(map);
							continue;
						}
						//判断是否根据大订单号查询订单信息
						if(isGetOnOrderInfoByOrderCode && !map.get("big_order_code").equals(inputParam.getBig_orderCode())) {
							continue;
						}						
						if("4497153900010001".equals(map.get("order_status"))){    //待付款
							bigOrderCodeList.add(String.valueOf(map.get("big_order_code")));
							waitPay = true;
						}						
					}
					/**
					 * 处理待支付     和   外部订单号为空   的订单
					 */
					if(waitPay) {
						bigOrderCodeNewList = removeDuplicateElement(bigOrderCodeList);//去重
						// 循环所有去重的大订单号
						for (String bigOrderCode : bigOrderCodeNewList) {
							//主要用于获取小订单中的支付状态
							MDataMap  mapOrderInfo = DbUp.upTable("oc_orderinfo").one("big_order_code",bigOrderCode);
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
							//设置订单按钮信息
							apiSellerOrderListResult = setSellerOrderList(map, mapOrderInfo, appVersion, bigOrderCode, inputParam.getDeviceType(), orderCode);
							if (StringUtils.isBlank(alipayValue)) {
								alipayValue = "";
							}
							// 查询大订单表 获取订单上相关信息
							Map<String, Object> bigOrderMap = DbUp.upTable("oc_orderinfo_upper").dataSqlOne(
											"select * from oc_orderinfo_upper where big_order_code=:big_order_code and delete_flag='0'",
											new MDataMap("big_order_code",bigOrderCode));							
							//根据大订单号设置订单相关信息
							setSellerOrderListByBigOrder(apiSellerOrderListResult, mapOrderInfo, bigOrderMap, alipayValue);
							// 通过大订单号查询oc_orderinfo表, 获取相关信息
							List<Map<String, Object>> orderInfoList = DbUp.upTable("oc_orderinfo").dataSqlList(
											"select * from oc_orderinfo where big_order_code=:big_order_code and "
													+ "delete_flag=:delete_flag and order_source not in('449715190014','449715190037') ORDER BY update_time DESC",
											new MDataMap("big_order_code",bigOrderCode,"delete_flag", "0"));
							int orderSellerNumber = 0;
							for (Map<String, Object> mm : orderInfoList) {
								apiSellerOrderListResult.setOrder_status(String.valueOf(mm.get("order_status")));
								orderSellerList = orderService.orderSellerNumber(mm); // 订单商品数量(每一个订单数量加一起的总数)// 和 商品code、商品单价
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
												ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
												apiSellerListResult = setSellerList(apiSellerOrderListResult, mm, sellerList.get(k), orderSellerMap, orderSellerNumberDouble, orderService, productService, productLabelService, appVersion);
												sellerResultList.add(apiSellerListResult);
											}
											apiSellerOrderListResult.setApiSellerList(sellerResultList);
										}
										apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
									}
								}
								setIfFlashSales(apiSellerOrderListResult, flashSalesList); //设置闪购信息
							}
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					/**
					 * 此循环用于匹配
					 * 如果此订单不是"待付款(4497153900010001)、交易失败(4497153900010006)状态。外部订单号为空"时进入flag中。
					 */
					for (int i = 0; i < orderList.size(); i++) {
						boolean flag = true;
						map = orderList.get(i);
						if(!isGetOnOrderInfoByOrderCode && map.get("source") != null && "to".equals(map.get("source"))) {
							continue; //LD订单已经在上面处理过了
						}
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
						if(flag) {
							int orderSellerNumber = 0;
							List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
							ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
							//设置订单按钮信息
							apiSellerOrderListResult = setButtonList(map, appVersion, alipayValue);
							orderSellerList = orderService.orderSellerNumber(map);
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
											ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
											//商品信息
											apiSellerListResult = setSellerList(apiSellerOrderListResult, map, sellerList.get(k), orderSellerMap, orderSellerNumberDouble, orderService, productService, productLabelService, appVersion);
											sellerResultList.add(apiSellerListResult);
										}
										apiSellerOrderListResult.setApiSellerList(sellerResultList);
									}
									apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
								}
							}							
							setIfFlashSales(apiSellerOrderListResult, flashSalesList); //设置闪购信息
							sellerOrderList.add(apiSellerOrderListResult);
						}
					}
					if(ldOrderList != null && ldOrderList.size() > 0) {						
						List<ApiSellerOrderListResult> ldSellerOrderList = setWaitPayLDOrder(ldOrderList, appVersion, orderService, productLabelService);
						sellerOrderList.addAll(ldSellerOrderList);						
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
				
				// 5.6.6 版本支持推广赚
				if(AppVersionUtils.compareTo(appVersion, "5.6.60") >= 0 || StringUtils.isBlank(appVersion)) {
					// 全部的里面增加推广赚按钮
					setTgzButtonList(sellerOrderList);
				}
				
				apiOrderListResult.setSellerOrderList(sellerOrderList);
				return apiOrderListResult;
			}
			//=============================获取订单列表end=========================================
		}
		
		return null;
	}
	
	/**
	 * 设置商品区域的推广赚按钮
	 * @param sellerOrderlist
	 */
	private void setTgzButtonList(List<ApiSellerOrderListResult> sellerOrderlist) {
		HjycoinService hjycoinService = new HjycoinService();
		MDataMap tgzConfigMap = hjycoinService.getTgzTypeConfigMap(HjycoinService.TGZ_TYPE_FX);
		// 所以如果未启用则直接返回
		if(tgzConfigMap == null) {
			return;
		}
		
		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
		PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
		plusModelQuery.setChannelId(getChannelId());
		plusModelQuery.setMemberCode(getUserCode());
		
		// 如果是小程序则检查分销活动
		PlusServiceActivityAgent plusServiceActivityAgent = new PlusServiceActivityAgent();
		Map<String, String> fxProductMap = new HashMap<String, String>();
		if("449747430023".equals(plusModelQuery.getChannelId())) {
			ActivityAgent fxAct = plusServiceActivityAgent.getActivityAgent();
			if(fxAct != null) {
				fxProductMap = fxAct.getCouponTypeMap();
			}
		}
		
		PlusModelSkuInfo skuInfo = null;
		BigDecimal tgzMoney;
		PlusModelProductInfo productInfo;
		for(ApiSellerOrderListResult orderList : sellerOrderlist) {
			// 忽略非交易成功状态的订单
			if(!"4497153900010005".equals(orderList.getOrder_status())) {
				continue;
			}
			
			for(ApiSellerListResult productResult : orderList.getApiSellerList()) {
				// 忽略分销商品
				if(fxProductMap.containsKey(productResult.getProduct_code())) {
					continue;
				}
				
				plusModelQuery.setCode(productResult.getSku_code());
				skuInfo = plusSupportProduct.upSkuInfo(plusModelQuery).getSkus().get(0);
				productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(skuInfo.getProductCode()));
				
				// 忽略已下架的商品
				if(!"4497153900060002".equals(productInfo.getProductStatus())) {
					continue;
				}
				
				// 计算推广赚的金额
				tgzMoney = hjycoinService.getProductTgzMoney(tgzConfigMap,skuInfo.getSellPrice(), skuInfo.getCostPrice(), skuInfo.getSmallSellerCode(), 1, "Y".equalsIgnoreCase(productInfo.getCspsFlag()));
				if(tgzMoney.compareTo(BigDecimal.ZERO) > 0) {
					Button btn = new Button("4497477800080026", 
							"推广赚¥"+MoneyHelper.format(tgzMoney),
							1,
							FormatHelper.formatString(bConfig("familyhas.tgz_link"), productResult.getProduct_code(), getUserCode()),
							bConfig("cfamily.hjyWeiXinUrl") + productResult.getProduct_code());
					productResult.getButtonList().add(btn);
				}
			}
		}
	}
	
	/**
	 * 后台处理分页数据
	 * @param orderList
	 */
	private Map<String, Object> setPageOrderList(String appVersion, List<Map<String, Object>> orderList, String nextPage, String membercode, String order_status) {
		if(orderList == null || orderList.size() <= 0) {
			orderList = new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(compareAppVersion(appVersion, "5.2.6") >= 0 ) {
			if(new OrderService().upSyncLDOrder()) { //可以拉取LD订单
				MDataMap memberDataMap=DbUp.upTable("mc_login_info").one("member_code",membercode);
				if(memberDataMap != null) {
					/**
					 * 调取LD订单列表
					 */
					RsyncGetThirdOrderList rsyncGetThirdOrderList = new RsyncGetThirdOrderList();
					rsyncGetThirdOrderList.upRsyncRequest().setTel(memberDataMap.get("login_name").toString());
					rsyncGetThirdOrderList.upRsyncRequest().setOrd_type(order_status);
					rsyncGetThirdOrderList.doRsync();
					int ldSize = 0;
					if(rsyncGetThirdOrderList.getResponseObject() != null && rsyncGetThirdOrderList.getResponseObject().getResult() != null && rsyncGetThirdOrderList.getResponseObject().getResult().size() > 0) {
						//==============================拼订单列表begin===================================
						List<RsyncModelThirdOrder> ldList = new ArrayList<RsyncModelThirdOrder>();
						List<String> bigOrderCode = new ArrayList<String>();
						for(RsyncModelThirdOrder order : rsyncGetThirdOrderList.getResponseObject().getResult()) {
							ldList.add(order);
							bigOrderCode.add(order.getOrd_id().toString());
						}
						//相同的订单号进行合并begin
						bigOrderCode = removeDuplicateElement(bigOrderCode);
						ldSize = bigOrderCode.size();
						for(String code : bigOrderCode) {
							Map<String, Object> ldOrder = new HashMap<String, Object>(); //一个新单
							ldOrder.put("order_code", code); //订单号
							ldOrder.put("big_order_code", code); //大订单号，与订单号一致
							boolean isFirst = true;
							List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
							for(RsyncModelThirdOrder item : ldList) {
								if(code.equals(item.getOrd_id().toString())) {
									if(isFirst) {
										//归并到一个订单										
										ldOrder.put("create_time", convertDate(item.getEtr_date())); //创建时间										
										ldOrder.put("out_order_code", ""); //外部订单号
										ldOrder.put("seller_code", "SI2003"); //卖家编号
										ldOrder.put("small_seller_code", "SI2003"); //小卖家编号
										ldOrder.put("update_time", convertDate(item.getEtr_date())); //更新时间
										ldOrder.put("zid", 0);
										ldOrder.put("source", "to");	//订单来源--LD订单	
										ldOrder.put("pay_type", item.getPay_type()); //支付方式
										ldOrder.put("is_chg", item.getIs_chg()); //是否换货新单
										isFirst = false;
									}
									Map<String, Object> orderDetail = new HashMap<String, Object>();
									orderDetail.put("order_status", convertOrderStatus(item.getORD_STAT())); //订单状态
									orderDetail.put("ord_seq", item.getOrd_seq()); //订单序号
									orderDetail.put("due_money", item.getOrd_amt()); //商品总金额
									orderDetail.put("good_id", item.getGood_id());//商品编号
									orderDetail.put("good_nm", item.getGood_nm());//商品名称
									orderDetail.put("prc", item.getOrg_prc());//商品售价
									orderDetail.put("ord_amt", item.getOrd_amt());//订单金额
									orderDetail.put("ord_qty", item.getOrd_qty());//订购数量
									orderDetail.put("color_id", item.getColor_id());//商品颜色编号
									orderDetail.put("color_desc", item.getColor_desc());//商品颜色描述
									orderDetail.put("style_id", item.getStyle_id());//商品款式编号
									orderDetail.put("style_desc", item.getStyle_desc());//商品款式描述
									orderDetail.put("is_hwg", item.getIs_hwg()); //是否海外购
									orderDetail.put("accm_apply_amt", item.getAccm_apply_amt()); //使用积分
									detailList.add(orderDetail);
								}
							}
							ldOrder.put("order_detail", detailList);
							if(DbUp.upTable("oc_orderinfo").count("out_order_code",code) <= 0) {//过滤数据
								orderList.add(ldOrder);
							}
						}
						//相同的订单号进行合并end						
						//==============================拼订单列表end===================================
					}
					
					if(orderList == null || orderList.size() <= 0)
						return null;
					
					if(rsyncGetThirdOrderList.getResponseObject() == null) {
						map.put("toNum", 0);
					}else {
						map.put("toNum", ldSize);
					}
				} else {
					map.put("toNum", 0);
				}
			} else {
				map.put("toNum", 0);
			}	
		} else {
			map.put("toNum", 0);
		}
		
		//排序,根据订单更新时间和zid逆序
		Collections.sort(orderList, new Comparator<Map<String, Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String update_time1 = o1.get("update_time").toString();
				String update_time2 = o2.get("update_time").toString();
				String zid1 = o1.get("zid").toString();
				String zid2 = o2.get("zid").toString();
				if(update_time1.compareTo(update_time2) < 0) {
					return 1;
				} else if(update_time1.compareTo(update_time2) > 0) {
					return -1;
				} else {
					if(zid1.compareTo(zid2) < 0) {
						return 1;
					} else if(zid1.compareTo(zid2) > 0) {
						return -1;
					} else {
						return 0;
					}
				}
			}			
		});
		//分页
		int startNum = (Integer.parseInt(nextPage) - 1) * 10;
		int endNum = startNum + 10;
		if(endNum > orderList.size()) {
			endNum = orderList.size();
		}
		map.put("orderList", orderList.subList(startNum, endNum));		
		
		return map;
	}
	
	/**
	 * 根据sku_key获取sku编码
	 * @param product_code
	 * @param color_id
	 * @param style_id
	 * @param orderService
	 * @return
	 */
	private String getSkuCode(String product_code, String color_id, String style_id, OrderService orderService) {
		String sku_key = "color_id="+color_id+"&style_id="+style_id;
		return  orderService.getSkuCodeByColorStyle(product_code, sku_key);
	}
	
	/**
	 * LD待付款订单特殊处理
	 * @param ldOrderList
	 * @return
	 */
	private List<ApiSellerOrderListResult> setWaitPayLDOrder(List<Map<String, Object>> ldOrderList, String appVersion, OrderService orderService, ProductLabelService productLabelService){			
		List<ApiSellerOrderListResult> result = new ArrayList<ApiSellerOrderListResult>();				
		for(Map<String, Object> map : ldOrderList) {
			ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
			List<MDataMap> flashSalesList = new ArrayList<MDataMap>();
			BigDecimal due_money = BigDecimal.ZERO;
			int totalGoods = 0;
			List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();	
			//订单状态列表处理
			Set<String> orderStatusList = new HashSet<String>();
			List<Map<String, Object>> detailList = (List<Map<String, Object>>)map.get("order_detail");
			for(Map<String, Object> detail : detailList) {
				orderStatusList.add(detail.get("order_status").toString());
				String sku_code = getSkuCode(detail.get("good_id").toString(), detail.get("color_id").toString(), detail.get("style_id").toString(), orderService);
				if(!"".equals(sku_code)) {
					flashSalesList = orderService.flashSales(sku_code); // 判断是否为闪够信息
					int orderSellerNumberDouble = Integer.parseInt(detail.get("ord_qty").toString());
					totalGoods += orderSellerNumberDouble;
					due_money = due_money.add(new BigDecimal(detail.get("due_money").toString()));
					List<Map<String, Object>> sellerList = orderService.sellerInformation(sku_code); // 查询商品信息
					if (sellerList != null && !sellerList.isEmpty()) {
						for (int k = 0; k < sellerList.size(); k++) {
							List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
							ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
							Map<String, Object> sellerMap = sellerList.get(k);
							if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
								apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
							}
							apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
							apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
							apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
							apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));
							//添加图片
							apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
							/*是否生鲜*/
							PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());					
							List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();					
							apiSellerListResult.setLabelsList(labelsList);
							if("Y".equals(detail.get("is_hwg"))) {
								apiSellerListResult.setFlagTheSea("1");
							}
							//商品价格 = 商品单价 - 单个商品积分抵扣金额
							apiSellerListResult.setIntegral("0");
							apiSellerListResult.setSell_price(new BigDecimal(String.valueOf(detail.get("prc"))).toString());
							List<PcPropertyinfoForFamily> standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString());
							if (standardAndStyleList != null && standardAndStyleList.size() > 0) {
								for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
									ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
									apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
									apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
									apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
								}
								apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
							}
							apiSellerListResult.setProductType("0"); // 商品类型
							sellerResultList.add(apiSellerListResult);
						}
						apiSellerOrderListResult.setApiSellerList(sellerResultList);
					}
				} else {
					//处理商品信息
					due_money = due_money.add(new BigDecimal(detail.get("due_money").toString()));
					totalGoods += Integer.parseInt(detail.get("ord_qty").toString());
					ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
					apiSellerListResult.setMainpic_url("");
					apiSellerListResult.setProduct_code(detail.get("good_id").toString());
					apiSellerListResult.setSku_code("");
					apiSellerListResult.setProduct_name(detail.get("good_nm").toString());
					apiSellerListResult.setProduct_number(detail.get("ord_qty").toString());
					//添加图片
					apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo(detail.get("good_id").toString()).getListPic());			
					if("Y".equals(detail.get("is_hwg"))) {
						apiSellerListResult.setFlagTheSea("1");
					}
					
					/*是否生鲜*/
					PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(detail.get("good_id").toString());											
					List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();		
					apiSellerListResult.setLabelsList(labelsList);
					//商品价格 = 商品单价 - 单个商品积分抵扣金额
					apiSellerListResult.setIntegral("0");
					apiSellerListResult.setSell_price(new BigDecimal(String.valueOf(detail.get("prc")).toString()).toString());
					
					List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
					ApiSellerStandardAndStyleResult color = new ApiSellerStandardAndStyleResult();
					color.setStandardAndStyleKey("颜色");
					color.setStandardAndStyleValue(detail.get("color_desc").toString());
					apiSellerStandardAndStyleResultList.add(color);
					
					ApiSellerStandardAndStyleResult style = new ApiSellerStandardAndStyleResult();
					style.setStandardAndStyleKey("款式");
					style.setStandardAndStyleValue(detail.get("style_desc").toString());
					apiSellerStandardAndStyleResultList.add(style);
					apiSellerListResult.setProductType("0"); // 商品类型
					
					apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
					apiSellerListResult.setIsProductSync("0");
					apiSellerListResult.setProductPrompt("该商品已下架！");
					
					sellerResultList.add(apiSellerListResult);
				}
			}
			//订单状态需要特殊处理，取最大值
			String order_status = detailList.get(0).get("order_status").toString();
			if(orderStatusList.size() == 1) {
				apiSellerOrderListResult.setOrder_status(order_status);
			} else {
				//取状态最大的				
				Iterator<String> it = orderStatusList.iterator();
				while(it.hasNext()) {
					String c_status = it.next();
					if(CompareOrderStatus(order_status, c_status) == -1) {
						order_status = c_status;
					}
				}
				apiSellerOrderListResult.setOrder_status(order_status);
			}
			apiSellerOrderListResult.setApiSellerList(sellerResultList);
			String alipayValue = "";						
			apiSellerOrderListResult.setOrder_code(map.get("order_code").toString());
			apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
			apiSellerOrderListResult.setDue_money(due_money.toString()); //实付款
			apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
			apiSellerOrderListResult.setAlipaySign(alipayValue);
			apiSellerOrderListResult.setIs_comment(0); //无评价
			apiSellerOrderListResult.setOrderStatusNumber(totalGoods);//订单商品数量
			//根据订单状态设置订单按钮
			if("4497153900010001".equals(order_status) ) {
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单	
			}else if("4497153900010006".equals(order_status) ){
				apiSellerOrderListResult.setIsDeleteOrder(0);//5.0.6  fq++
				if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
				}
				if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
				}
			}else if("4497153900010002".equals(order_status) ){
				if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
					Map<String, Object> orderinfo = new HashMap<String, Object>();
					orderinfo.put("order_code", map.get("order_code").toString());
					orderinfo.put("pay_type", map.get("pay_type").toString());
					orderinfo.put("create_time", map.get("create_time").toString());
					int btnStatus = is_remind_shipment(orderinfo);
					if(3 != btnStatus) {
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
					}
				}
			}else if("4497153900010003".equals(order_status) ){
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流				
				if(AppVersionUtils.compareTo(TelRefundVersion, appVersion)<0) {
					if(AppVersionUtils.compareTo("5.4.60", appVersion)>0) {
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
					}
				}else {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
				}
			}else if("4497153900010005".equals(order_status) ){
				if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//5.0.6之后，将交易成功的订单关闭查看物流
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流
				}
				apiSellerOrderListResult.setIsDeleteOrder(0);//5.0.6  fq++
				if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
				}
			}else if("4497153900010008".equals(order_status) ) {
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
			}
			
			//换货新单增加换货标识
			if("SI2003".equals(map.get("small_seller_code").toString()) && compareAppVersion(appVersion, "5.3.2") >= 0) {
				if(AfterSaleService.isChangeGoodsOrder(map.get("order_code").toString(), map.get("is_chg").toString())) {
					apiSellerOrderListResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
				}
			}
			
			setIfFlashSales(apiSellerOrderListResult, flashSalesList);
			if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code").toString())){	
				apiSellerOrderListResult.setIsSeparateOrder("0");   //需要分包
			}
			result.add(apiSellerOrderListResult);
		}
		return result;
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
			case "06": return "4497153900010008";//取消中
			default : return "";
		}
	}
	
	/**
	 * 反向转换LD订单状态
	 * @param order_status
	 * @return
	 */
	private int revertOrderStatus(String order_status) {
		switch(order_status) {
			case "4497153900010001": return 1;
			case "4497153900010002": return 2;
			case "4497153900010003": return 3;
			case "4497153900010006": return 4;
			case "4497153900010005": return 5;
			case "4497153900010008": return 6;//取消中
			default : return 0;
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
		int status1 = revertOrderStatus(order_status1);
		int status2 = revertOrderStatus(order_status2);
		if(status1 >= status2) {
			return 1;
		} else {
			return -1;
		}
	}
	
	/**
	 * 设置闪购信息
	 * @param apiSellerOrderListResult
	 * @param flashSalesList
	 */
	private void setIfFlashSales(ApiSellerOrderListResult apiSellerOrderListResult, List<MDataMap> flashSalesList) {
		if (flashSalesList != null && !flashSalesList.isEmpty()) {
			apiSellerOrderListResult.setIfFlashSales("0"); // 闪购
		} else {
			apiSellerOrderListResult.setIfFlashSales("1"); // 非闪购
		}
	}
	
	/**
	 * 商品信息
	 * @param map
	 * @param sellerMap
	 * @param orderSellerMap
	 * @param orderSellerNumberDouble
	 * @param orderService
	 * @param productService
	 * @param productLabelService
	 * @return
	 */
	private ApiSellerListResult setSellerList(ApiSellerOrderListResult apiSellerOrderListResult, Map<String, Object> map, Map<String, Object> sellerMap, Map<String, Object> orderSellerMap, int orderSellerNumberDouble, OrderService orderService, ProductService productService, ProductLabelService productLabelService, String appVersion) {
		List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
		ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
		String orderCode = map.get("order_code").toString();
		if (!"null".equals(String.valueOf(sellerMap.get("sku_picurl")))) {
			apiSellerListResult.setMainpic_url(sellerMap.get("sku_picurl").toString());
		}
		apiSellerListResult.setProduct_code(sellerMap.get("product_code").toString());
		apiSellerListResult.setSku_code(sellerMap.get("sku_code").toString());
		apiSellerListResult.setProduct_name(sellerMap.get("sku_name").toString());
		apiSellerListResult.setProduct_number(String.valueOf(orderSellerNumberDouble));											
		//添加图片
		apiSellerListResult.setLabelsPic(productLabelService.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
		if(new PlusServiceSeller().isKJSeller(map.get("small_seller_code").toString())){													
			apiSellerListResult.setFlagTheSea("1");														
			new OrderDetailService().initCustomsStatus(orderCode, (String)map.get("order_status"), apiSellerListResult);																								
		}
		
		/*是否生鲜*/
		PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(sellerMap.get("product_code").toString());											
		List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();											
		apiSellerListResult.setLabelsList(labelsList);
		String orderType = MapUtils.getString(map, "order_type", "");
		BigDecimal show_price = new BigDecimal(MapUtils.getString(orderSellerMap, "show_price", "0"));
		if(show_price.compareTo(BigDecimal.ZERO) == 0) {
			show_price = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0"));
		}
		if("449715200024".equals(orderType)) {
			BigDecimal skuNum = new BigDecimal(MapUtils.getString(orderSellerMap, "sku_num", "0"));//商品个数
			BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSellerMap, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);//积分抵扣钱												
			//商品价格 = 商品单价 - 单个商品积分抵扣金额
			apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
			apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSellerMap, "sku_price", "0")).subtract(integralMoney).toString());
		}else {
			apiSellerListResult.setSell_price(String.valueOf(show_price));
		}
		List<PcPropertyinfoForFamily> standardAndStyleList = orderService.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString());
		if (standardAndStyleList != null && standardAndStyleList.size() > 0) {
			for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
				apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
				apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
			}
			apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
		}
		apiSellerListResult.setProductType(String.valueOf(productService.getSkuActivityTypeForOrder(sellerMap.get("sku_code").toString(),orderCode))); // 商品类型
		return apiSellerListResult;
	}
	
	/**
	 * 设置订单按钮信息
	 * @param map 订单
	 * @param appVersion app版本
	 * @param alipayValue
	 * @return
	 */
	private ApiSellerOrderListResult setButtonList(Map<String, Object> map, String appVersion, String alipayValue) {
		ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
		String orderCode = map.get("order_code").toString();
		String order_status = map.get("order_status").toString();
		String smallSellerCode = map.get("small_seller_code").toString();
		if (StringUtils.isBlank(alipayValue)) {
			alipayValue = "";
		}
		if("4497153900010006".equals(order_status) ){
			apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
			if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
			}
			if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
			}
		}else if("4497153900010002".equals(order_status) ){
			if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
				int btnStatus = is_remind_shipment(map);
				if(3 != btnStatus) {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
				}
			}
		}else if("4497153900010003".equals(order_status) ){
			// 京东商品不显示确认收货按钮，以京东状态为准
			if(!Constants.SMALL_SELLER_CODE_JD.equals(smallSellerCode)) {
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
			}
			apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流		
			
			boolean hasAfterSale = false;
			if(StringUtils.isBlank(appVersion) || compareAppVersion(appVersion, "5.4.4") >= 0 ) {
				if("SI2003".equals(smallSellerCode)) {
					// 查询LD售后申请
					if(DbUp.upTable("oc_after_sale_ld").count("order_code", map.get("out_order_code")+"") > 0) {
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080022",bConfig("familyhas.define_4497477800080022")));//售后详情
						hasAfterSale = true;
					}
				} else {
					// 有售后就展示售后详情按钮
					if(DbUp.upTable("oc_order_after_sale").count("order_code", orderCode,"show_flag", "1") > 0) {
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080022",bConfig("familyhas.define_4497477800080022")));//售后详情
						hasAfterSale = true;
					}
				}
			}
			
			// 有售后详情则不显示售后电话
			if(!hasAfterSale) {
				if(AppVersionUtils.compareTo(TelRefundVersion, appVersion)<0) {
					if(AppVersionUtils.compareTo("5.4.60", appVersion)>0) {
						apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
					}
				}else {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
				}
			}
		}else if("4497153900010005".equals(order_status) ){
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
			if(StringUtils.isBlank(appVersion) || compareAppVersion(appVersion, "5.4.4") >= 0 ) {
				// 有售后就展示售后详情按钮
				if(DbUp.upTable("oc_order_after_sale").count("order_code", orderCode,"show_flag", "1") > 0) {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080022",bConfig("familyhas.define_4497477800080022")));//售后详情
				}
				if(compareAppVersion(appVersion, "5.5.0") >= 0 && DbUp.upTable("oc_after_sale_ld").count("order_code", map.get("out_order_code")+"") > 0) {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080022",bConfig("familyhas.define_4497477800080022")));//售后详情
				}
			}
		}
		if(AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
			apiSellerOrderListResult.setIsSeparateOrder("0");
		}
		
		//换货新单增加换货标识
		if("SI2003".equals(map.get("small_seller_code").toString()) && compareAppVersion(appVersion, "5.3.2") >= 0) {
			if(AfterSaleService.isChangeGoodsOrder(orderCode, "")) {
				apiSellerOrderListResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
			}
		}
		
		apiSellerOrderListResult.setOrder_status(map.get("order_status").toString());
		apiSellerOrderListResult.setOrder_code(orderCode);
		apiSellerOrderListResult.setCreate_time(map.get("create_time").toString());
		apiSellerOrderListResult.setDue_money(map.get("due_money").toString());
		apiSellerOrderListResult.setAlipayUrl(FamilyConfig.ali_url_http);
		apiSellerOrderListResult.setAlipaySign(alipayValue);
		return apiSellerOrderListResult;
	}
	
	/**
	 * 设置订单按钮信息
	 * @param map
	 * @param mapOrderInfo
	 * @param appVersion
	 * @param bigOrderCode
	 * @param deviceType
	 * @param orderCode
	 * @return
	 */
	private ApiSellerOrderListResult setSellerOrderList(Map<String, Object> map, MDataMap  mapOrderInfo, String appVersion, String bigOrderCode, String deviceType, String orderCode) {
		ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
		if(!"4497153900010001".equals(map.get("order_status")) && AppConst.MANAGE_CODE_KJT.equals(map.get("small_seller_code"))){
			apiSellerOrderListResult.setIsSeparateOrder("0");
		}
		//待付款订单
		if("4497153900010001".equals(mapOrderInfo.get("order_status")) ) {
			String defaultPayType = new PlusSupportPay().upPayFrom(bigOrderCode);
			if(defaultPayType!=null && !"".equals(defaultPayType)){
				apiSellerOrderListResult.setDefault_Pay_type(defaultPayType);   //返回客户端  支付方式(默认支付宝)
			}
			// 非IOS设备上查询ApplePay支付的订单时，支付类型返回默认的支付宝
			if(!"IOS".equals(deviceType) && "449746280013".equals(defaultPayType)){
				apiSellerOrderListResult.setDefault_Pay_type("449746280003");
			}
			if("449716200001".equals(map.get("pay_type"))){
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080003",bConfig("familyhas.define_4497477800080003")));//取消付款
			}
			apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080002",bConfig("familyhas.define_4497477800080002")));//取消订单
		}
		//下单成功-未发货
		else if("4497153900010002".equals(mapOrderInfo.get("order_status")) ) {
			if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示提醒发货功能
				int btnStatus = is_remind_shipment(map);
				if(3 != btnStatus) {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080012",bConfig("familyhas.define_4497477800080012"),btnStatus));//提醒发货	 fq++
				}
			}
		}
		//已发货
		else if("4497153900010003".equals(mapOrderInfo.get("order_status")) ){
			apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080007",bConfig("familyhas.define_4497477800080007")));//确认收货
			apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080005",bConfig("familyhas.define_4497477800080005")));//查看物流	
			if(AppVersionUtils.compareTo(TelRefundVersion, appVersion)<0) {
				if(AppVersionUtils.compareTo("5.4.60", appVersion)>0) {
					apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080015",bConfig("familyhas.define_4497477800080015")));//售后电话
				}
			}else {
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080006",bConfig("familyhas.define_4497477800080006")));//电话退款
			}
		}
		//交易成功
		else if("4497153900010005".equals(mapOrderInfo.get("order_status")) ){
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
		//交易失败
		else if("4497153900010006".equals(mapOrderInfo.get("order_status")) ){
			apiSellerOrderListResult.setIsDeleteOrder(1);//5.0.6  fq++
			if(compareAppVersion(appVersion, "5.0.6") < 0 ) {//添加版本控制，5.0.6之后的版本  删除按钮由isDeleteOrder  字段标识，而不再使用按钮
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080010",bConfig("familyhas.define_4497477800080010")));//关闭订单(兼容app老版本，加按钮状态为3致为不可用。)
			}
			if(compareAppVersion(appVersion, "5.0.6") >= 0 ) {//添加版本控制，如果5.0.6之后的版本才展示再次购买功能
				apiSellerOrderListResult.getOrderButtonList().add(new Button("4497477800080013",bConfig("familyhas.define_4497477800080013")));//再次购买  5.0.6 fq++
			}
		}
		return apiSellerOrderListResult;
	}
	
	private void setSellerOrderListByBigOrder(ApiSellerOrderListResult apiSellerOrderListResult, MDataMap  mapOrderInfo, Map<String, Object> bigOrderMap, String alipayValue) {
		if (bigOrderMap != null && bigOrderMap.size() > 0) {
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
	}
	
	private void setSellerOrderListByBigOrder1(ApiSellerOrderListResult apiSellerOrderListResult, Map<String, Object> bigOrderMap, String alipayValue, String order_status, String deviceType) {
		if (bigOrderMap != null && bigOrderMap.size() > 0) {
			apiSellerOrderListResult.setOrder_status(order_status);
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
			if(!"IOS".equals(deviceType) && "449746280013".equals(defaultPayType)){
				apiSellerOrderListResult.setDefault_Pay_type("449746280003");
			}
		}
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
	}
	
	/**
	 * List去重
	 * @param list
	 * @return
	 */
	public List<String> removeDuplicateElement(List<String> list) {
		return new ArrayList<String>(new HashSet<String>(list));
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
	
}
