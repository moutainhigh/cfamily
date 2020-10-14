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

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiAfterSaleListInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.ApiAfterSaleListResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.service.AfterSaleService;
import com.cmall.familyhas.service.OrderDetailService;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderList;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrder;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 售后申请列表<br/>
 * 只保留创建订单45天内<br/>
 * 订单状态：待收货和签收(签收时间在15天内)的订单数据，分页做内存分页
 * @remark 
 * @author 任宏斌
 * @date 2019年11月18日
 */
public class ApiAfterSaleList extends RootApiForToken<ApiAfterSaleListResult, ApiAfterSaleListInput>{

	private static final String HJY_ORDER_STATUS = "'4497153900010003','4497153900010005'";
	private static final String LD_ORDER_STATUS = "03,05";
	
	private OrderService os = new OrderService();
	private ProductService ps = new ProductService();
	private AfterSaleService as  = new AfterSaleService();
	private ProductLabelService pls = new ProductLabelService();
	
	@Override
	public ApiAfterSaleListResult Process(ApiAfterSaleListInput inputParam, MDataMap mRequestMap) {
		ApiAfterSaleListResult result = new ApiAfterSaleListResult();
		
		//获取惠家有订单
		List<Map<String, Object>> hjyOrderList = getHjyOrderList();
		//获取LD订单
		List<Map<String, Object>> ldOrderList = getLDOrderList();
		//计算分页、总页数 返回分页后订单列表
		List<Map<String, Object>> orderList = calculatePaging(result, hjyOrderList, ldOrderList, inputParam.getNextPage());
		
		//没有订单则直接返回
		if(null == orderList || orderList.isEmpty()) return result;
		
		//转换订单列表 添加商品信息
		dealOrderList(result, orderList);
		//设置拼团信息
		changeStatusOfGroupBuying(result);
		//设置申请售后按钮
		addSupplyAfterSaleButton(result);
		
		return result;
	}
	
	/**
	 * 转换订单列表 添加商品信息
	 * @param result
	 * @param orderList
	 */
	private void dealOrderList(ApiAfterSaleListResult result, List<Map<String, Object>> orderList) {
		//ld原始订单信息
		List<Map<String, Object>> ldOrderList = new ArrayList<Map<String, Object>>(); 
		//原始订单信息
		Map<String, Object> order = new HashMap<String, Object>(); 
		//最终订单信息
		ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult(); 
		//订单中的商品列表
		List<Map<String, Object>> orderSellerList = new ArrayList<Map<String, Object>>(); 
		//订单中的商品信息
		Map<String, Object> orderSeller = new HashMap<String, Object>();
		//商品的sku信息
		List<Map<String, Object>> skuList = new ArrayList<Map<String, Object>>();
		//闪购信息
		List<MDataMap> flashSalesList = new ArrayList<MDataMap>();
		
		if (orderList != null && !orderList.isEmpty()) {
			ldOrderList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < orderList.size(); i++) {						
				int orderSellerNumber = 0;
				List<ApiSellerListResult> sellerResultList = new ArrayList<ApiSellerListResult>();
				order = orderList.get(i);	
				//LD订单特殊处理
				if(order.get("source") != null && "to".equals(order.get("source"))) {
					ldOrderList.add(order);
					continue;
				}
				//设置订单信息
				apiSellerOrderListResult = setHjyOrderInfo(order);
				// 订单商品数量(每一个订单数量加一起的总数)
				orderSellerList = os.orderSellerNumber(order); 
				if (orderSellerList != null && !orderSellerList.isEmpty()) {
					for (int j = 0; j < orderSellerList.size(); j++) {
						orderSeller = orderSellerList.get(j);
						// 判断是否为闪够信息
						flashSalesList = os.flashSales(orderSeller.get("sku_code").toString()); 
						int skuNum = Integer.parseInt(orderSeller.get("sku_num").toString());
						orderSellerNumber += skuNum;
						// 查询商品信息
						skuList = os.sellerInformation(orderSeller.get("sku_code").toString());
						if (skuList != null && !skuList.isEmpty()) {
							for (int k = 0; k < skuList.size(); k++) {
								sellerResultList.add(setSellerListHjy(apiSellerOrderListResult, order, skuList.get(k), orderSeller, skuNum));										
							}
							apiSellerOrderListResult.setApiSellerList(sellerResultList);
						}
						apiSellerOrderListResult.setOrderStatusNumber(orderSellerNumber);
					}
				}
				//设置闪购信息
				setIfFlashSales(apiSellerOrderListResult, flashSalesList);
				result.getSellerOrderList().add(apiSellerOrderListResult);
			}
			
			if(ldOrderList != null && ldOrderList.size() > 0) {						
				List<ApiSellerOrderListResult> ldSellerOrderList = setSellerListLd(ldOrderList);
				result.getSellerOrderList().addAll(ldSellerOrderList);						
			}
		}
		// 按点击数倒序
		Collections.sort(result.getSellerOrderList(), new Comparator<ApiSellerOrderListResult>() {
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
	}

	/**
	 * 转换惠家有订单信息
	 * @param order
	 * @return
	 */
	private ApiSellerOrderListResult setHjyOrderInfo(Map<String, Object> order) {
		ApiSellerOrderListResult apiSellerOrderListResult = new ApiSellerOrderListResult();
		String orderCode = order.get("order_code").toString();
		///跨境通标识
		if(AppConst.MANAGE_CODE_KJT.equals(order.get("small_seller_code"))){
			apiSellerOrderListResult.setIsSeparateOrder("0");
		}
		//换货新单增加换货标识
		if("SI2003".equals(order.get("small_seller_code").toString()) && AfterSaleService.isChangeGoodsOrder(orderCode, "")) {
			apiSellerOrderListResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
		}
		apiSellerOrderListResult.setOrder_status(order.get("order_status").toString());
		apiSellerOrderListResult.setOrder_code(orderCode);
		apiSellerOrderListResult.setCreate_time(order.get("create_time").toString());
		apiSellerOrderListResult.setDue_money(order.get("due_money").toString());
		return apiSellerOrderListResult;
	}

	/**
	 * 计算分页、总页数
	 * @param result
	 * @param hjyOrderList
	 * @param ldOrderList
	 * @param nextPage
	 * @return
	 */
	private List<Map<String, Object>> calculatePaging(ApiAfterSaleListResult result, List<Map<String, Object>> hjyOrderList,
			List<Map<String, Object>> ldOrderList, String nextPage) {
		int hjyOrderCount = 0;
		int ldOrderCount = 0;
		List<Map<String, Object>> allOrderList = new ArrayList<Map<String, Object>>();
		if(null != hjyOrderList) {
			hjyOrderCount = hjyOrderList.size();
			allOrderList.addAll(hjyOrderList);
		}
		if(null != ldOrderList) {
			ldOrderCount = ldOrderList.size();
			allOrderList.addAll(ldOrderList);
		}
		
		if(allOrderList.isEmpty()) return allOrderList;
		
		//排序,根据订单更新时间和zid逆序
		Collections.sort(allOrderList, new Comparator<Map<String, Object>>(){
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
		if(endNum > allOrderList.size()) endNum = allOrderList.size();
		result.setNowPage(Integer.parseInt(nextPage));
		result.setCountPage(Double.valueOf(Math.ceil(Double.valueOf(hjyOrderCount+ldOrderCount)/10d)).intValue());
		if(startNum > (hjyOrderCount+ldOrderCount)) return new ArrayList<Map<String, Object>>();
		
		return allOrderList.subList(startNum, endNum);
	}

	/**
	 * 获取LD订单
	 * @return
	 */
	private List<Map<String, Object>> getLDOrderList() {
		List<Map<String, Object>> ldOrderList = new ArrayList<Map<String, Object>>();
		if(os.upSyncLDOrder()) { //可以拉取LD订单
			MDataMap memberDataMap=DbUp.upTable("mc_login_info").one("member_code",getUserCode());
			RsyncGetThirdOrderList rsyncGetThirdOrderList = new RsyncGetThirdOrderList();
			rsyncGetThirdOrderList.upRsyncRequest().setTel(memberDataMap.get("login_name").toString());
			rsyncGetThirdOrderList.upRsyncRequest().setOrd_type(LD_ORDER_STATUS);
			rsyncGetThirdOrderList.upRsyncRequest().setSource("sqshlb");//申请售后列表
			rsyncGetThirdOrderList.doRsync();
			
			if(rsyncGetThirdOrderList.getResponseObject() != null 
					&& rsyncGetThirdOrderList.getResponseObject().getResult() != null 
					&& rsyncGetThirdOrderList.getResponseObject().getResult().size() > 0) {
				
				List<RsyncModelThirdOrder> ldList = new ArrayList<RsyncModelThirdOrder>();
				List<String> bigOrderCode = new ArrayList<String>();
				for(RsyncModelThirdOrder order : rsyncGetThirdOrderList.getResponseObject().getResult()) {
					if(as.checkAfterSaleAllow(order.getOrd_id().toString())) {
						ldList.add(order);
						bigOrderCode.add(order.getOrd_id().toString());
					}
				}
				
				bigOrderCode = removeDuplicateElement(bigOrderCode);
				
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
					ldOrderList.add(ldOrder);
				}
			}
		}
		
		return ldOrderList;
	}

	/**
	 * 获取惠家有订单
	 * @return
	 */
	private List<Map<String, Object>> getHjyOrderList() {
		List<Map<String, Object>> result = new ArrayList<Map<String ,Object>>();
		
		List<Map<String, Object>> orderInfoList = os.orderInformationSmallV3(getUserCode(), getManageCode());
		
		if(null != orderInfoList && !orderInfoList.isEmpty()) {
			Iterator<Map<String, Object>> iterator = orderInfoList.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> orderInfo = iterator.next();
				String orderCode = orderInfo.get("order_code")+"";
				List<MDataMap> maps = DbUp.upTable("oc_orderdetail").queryByWhere("order_code",orderCode);
				for(MDataMap map1 : maps) {
					if(as.checkIfAllowAfterSale(orderCode, map1.get("sku_code"), "")) {
						result.add(orderInfo);
						break;
					}
				}
			}
		}
		
		return result;
	}

	/**
	 * 添加【申请售后】按钮
	 * @param apiOrderListResult
	 */
	private void addSupplyAfterSaleButton(ApiAfterSaleListResult apiOrderListResult) {
		List<ApiSellerOrderListResult> sellerOrderList = apiOrderListResult.getSellerOrderList();
		for(ApiSellerOrderListResult result : sellerOrderList) {
			String orderCode = result.getOrder_code();
			
			result.getOrderButtonList().add(new Button("4497477800080008",bConfig("familyhas.define_4497477800080008"),1));
			
			if(!orderCode.contains("DD") && !orderCode.contains("HH")) {
				result.getApiSellerList().get(0).setOrderSeq("1");
			}
		}
	}

	/**
	 * 拼团订单处理
	 * @param apiOrderListResult
	 */
	private void changeStatusOfGroupBuying(ApiAfterSaleListResult apiOrderListResult){
		List<ApiSellerOrderListResult> sellerOrderList = apiOrderListResult.getSellerOrderList();
		for(ApiSellerOrderListResult apiSellerOrderListResult : sellerOrderList){
			//校验订单原状态，如果交易失败的订单不做处理
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
		}
	}
	
	
	
	/**
	 * 根据sku_key获取sku编码
	 * @param product_code
	 * @param color_id
	 * @param style_id
	 * @return
	 */
	private String getSkuCode(String product_code, String color_id, String style_id) {
		String sku_key = "color_id="+color_id+"&style_id="+style_id;
		return  os.getSkuCodeByColorStyle(product_code, sku_key);
	}
	
	@SuppressWarnings("unchecked")
	private List<ApiSellerOrderListResult> setSellerListLd(List<Map<String, Object>> ldOrderList){			
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
				String sku_code = getSkuCode(detail.get("good_id").toString(), detail.get("color_id").toString(), detail.get("style_id").toString());
				if(!"".equals(sku_code)) {
					flashSalesList = os.flashSales(sku_code); // 判断是否为闪够信息
					int orderSellerNumberDouble = Integer.parseInt(detail.get("ord_qty").toString());
					totalGoods += orderSellerNumberDouble;
					due_money = due_money.add(new BigDecimal(detail.get("due_money").toString()));
					List<Map<String, Object>> sellerList = os.sellerInformation(sku_code); // 查询商品信息
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
							apiSellerListResult.setLabelsPic(pls.getLabelInfo((String)sellerMap.get("product_code")).getListPic());
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
							List<PcPropertyinfoForFamily> standardAndStyleList = os.sellerStandardAndStyle(sellerMap.get("sku_keyvalue").toString());
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
					apiSellerListResult.setLabelsPic(pls.getLabelInfo(detail.get("good_id").toString()).getListPic());			
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

			//换货新单增加换货标识
			if("SI2003".equals(map.get("small_seller_code").toString()) 
					&& AfterSaleService.isChangeGoodsOrder(map.get("order_code").toString(), map.get("is_chg").toString())) {
				apiSellerOrderListResult.setIsChangeGoods(bConfig("familyhas.change_good_pic"));
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
	private ApiSellerListResult setSellerListHjy(ApiSellerOrderListResult apiSellerOrderListResult, Map<String, Object> order, Map<String, Object> skuInfo, Map<String, Object> orderSeller, int skuNum) {
		List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();
		ApiSellerListResult apiSellerListResult = new ApiSellerListResult();
		String orderCode = order.get("order_code").toString();
		if (!"null".equals(String.valueOf(skuInfo.get("sku_picurl")))) {
			apiSellerListResult.setMainpic_url(skuInfo.get("sku_picurl").toString());
		}
		apiSellerListResult.setProduct_code(skuInfo.get("product_code").toString());
		apiSellerListResult.setSku_code(skuInfo.get("sku_code").toString());
		apiSellerListResult.setProduct_name(skuInfo.get("sku_name").toString());
		apiSellerListResult.setProduct_number(String.valueOf(skuNum));											
		//添加图片
		apiSellerListResult.setLabelsPic(pls.getLabelInfo((String)skuInfo.get("product_code")).getListPic());
		if(new PlusServiceSeller().isKJSeller(order.get("small_seller_code").toString())){													
			apiSellerListResult.setFlagTheSea("1");														
			new OrderDetailService().initCustomsStatus(orderCode, (String)order.get("order_status"), apiSellerListResult);																								
		}
		
		//是否生鲜
		PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(skuInfo.get("product_code").toString());											
		List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();											
		apiSellerListResult.setLabelsList(labelsList);
		String orderType = MapUtils.getString(order, "order_type", "");
		BigDecimal show_price = new BigDecimal(MapUtils.getString(orderSeller, "show_price", "0"));
		if(show_price.compareTo(BigDecimal.ZERO) == 0) {
			show_price = new BigDecimal(MapUtils.getString(orderSeller, "sku_price", "0"));
		}
		if("449715200024".equals(orderType)) {
			BigDecimal skuNum1 = new BigDecimal(MapUtils.getString(orderSeller, "sku_num", "0"));//商品个数
			BigDecimal integralMoney = new BigDecimal(MapUtils.getString(orderSeller, "integral_money", "0")).divide(skuNum1, BigDecimal.ROUND_HALF_UP);//积分抵扣钱												
			//商品价格 = 商品单价 - 单个商品积分抵扣金额
			apiSellerListResult.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
			apiSellerListResult.setSell_price(new BigDecimal(MapUtils.getString(orderSeller, "sku_price", "0")).subtract(integralMoney).toString());
		}else {
			apiSellerListResult.setSell_price(String.valueOf(show_price));
		}
		List<PcPropertyinfoForFamily> standardAndStyleList = os.sellerStandardAndStyle(skuInfo.get("sku_keyvalue").toString());
		if (standardAndStyleList != null && standardAndStyleList.size() > 0) {
			for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
				apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
				apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
			}
			apiSellerListResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
		}
		apiSellerListResult.setProductType(String.valueOf(ps.getSkuActivityTypeForOrder(skuInfo.get("sku_code").toString(),orderCode))); // 商品类型
		return apiSellerListResult;
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
	
	/**
	 * 计算时差
	 * @param time
	 * @return
	 */
	public long differTimeNow(String time) {
		long now = new Date().getTime();
		long old = 0L;
		try {
			old = DateUtil.sdfDateTime.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (now - old);
	}
}
