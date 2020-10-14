package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForAftermarketListInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.ApiForAftermarketListResult;
import com.cmall.familyhas.api.result.ApiForAftermarketListResult.AfterSale;
import com.cmall.familyhas.api.result.ApiForAftermarketListResult.Product;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 售后详情
 * 包含处理中和已完成页签
 * LD 订单状态：01:待审核 02:受理退货 03:驳回退货 04:退货取消 05:退货完成 06:取消异常 07:已出库 08:换货失败 09:退款中 10:退款完成
 * @author jlin
 *
 */
public class ApiForAftermarketList
		extends
		RootApiForToken<ApiForAftermarketListResult, ApiForAftermarketListInput> {

	@Override
	public ApiForAftermarketListResult Process(
			ApiForAftermarketListInput inputParam, MDataMap mRequestMap) {
		
		ApiForAftermarketListResult result = new ApiForAftermarketListResult();
		String asale_status_detail = inputParam.getAsale_status();
		String order_code = inputParam.getOrder_code();
		MDataMap appClient = getApiClient();
		String appVersion = appClient.get("app_vision");
		if (StringUtils.isEmpty(asale_status_detail)
				&& StringUtils.isEmpty(order_code)) {
			result = this.getOldList(inputParam, mRequestMap,appVersion); //旧的售后详情逻辑，主要是TV自营品
		} else {
			Long l = System.currentTimeMillis();
			result = this.getNewList(inputParam, mRequestMap,appVersion); //新的售后详情逻辑，包括TV自营品和LD订单
			System.out.println("总耗时："+(System.currentTimeMillis()-l));
		}

		return result;

	}

	/**
	 * 获取新版本的售后列表
	 * 
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiForAftermarketListResult getNewList(
			ApiForAftermarketListInput inputParam, MDataMap mRequestMap,String appVersion) {
		ApiForAftermarketListResult result = new ApiForAftermarketListResult();
		int page = inputParam.getPage();
		String order_code_detail = inputParam.getOrder_code();//订单号  非必填项，如果用户从订单详情进入，则需要传入订单号
		String asale_status_detail = inputParam.getAsale_status();//售后单状态 0：处理中，1：已完成
		String buyer_code = getUserCode();
		// 查询所有订单
		String sql = "";
		List<Map<String, Object>> ldList = new ArrayList<Map<String, Object>>();
		List<MDataMap> allList = new ArrayList<MDataMap>();
		if (StringUtils.isEmpty(order_code_detail)) {// 入参订单号为空的时候（“我的”->“退款/售后”列表）
			List<MDataMap> appList = new ArrayList<MDataMap>();
			if ("0".equals(asale_status_detail)) {// 未完成
				sql = "select * from ordercenter.oc_order_after_sale where buyer_code = '"
						+ buyer_code
						+ "' and show_flag = 1 and asale_status in('4497477800050001','4497477800050003','4497477800050005','4497477800050010','4497477800050013') order by zid desc";
				//4497477800050001 退款中 4497477800050003 等待审核 4497477800050005 退货中 4497477800050010 待完善物流4497477800050013换货中
			} else {
				sql = "select * from ordercenter.oc_order_after_sale where buyer_code = '"
						+ buyer_code
						+ "' and show_flag = 1 and asale_status in('4497477800050002','4497477800050004','4497477800050006','4497477800050007','4497477800050008','4497477800050009','4497477800050011') order by zid desc";
				//4497477800050002 退款成功 4497477800050004 拒绝申请 4497477800050006 退货成功 4497477800050007 退货失败 4497477800050008 换货成功 4497477800050009 换货失败 4497477800050011 取消申请
			}
			List<Map<String, Object>> app = DbUp.upTable("oc_order_after_sale").dataSqlList(sql, null);//获取处理中或者已完成的售后单列表
			if (app != null) {
				for (Map<String, Object> map : app) {
					MDataMap changeMap = new MDataMap(map);
					appList.add(changeMap);
				}
			}
			allList.addAll(appList);//自营品的售后订单
			Long l = System.currentTimeMillis();
			ldList = this.getLdList(buyer_code, order_code_detail,asale_status_detail,appVersion);//取出LD的售后订单
			System.out.println("获取LD售后单耗时："+(System.currentTimeMillis()-l));
			for (Map<String, Object> map : ldList) {
				MDataMap mmap = new MDataMap();
				String order_code = map.get("order_code") != null ? map.get("order_code").toString() : "";
				String asale_code = map.get("asale_code") != null ? map.get("asale_code").toString() : "";
				String asale_status = map.get("asale_status") != null ? map.get("asale_status").toString() : "";
				String create_time = map.get("create_time") != null ? map.get("create_time").toString() : "";
				String after_sale_type = map.get("after_sale_type") != null ? map.get("after_sale_type").toString() : "1";
				String chg_ord = map.get("chg_ord") != null ? map.get("chg_ord").toString() : "";
				mmap.put("order_code", order_code);
				mmap.put("asale_code", asale_code);
				mmap.put("asale_status", asale_status);
				mmap.put("after_sale_type", after_sale_type);
				mmap.put("chg_ord", chg_ord);
				mmap.put("ld", "ld");
				mmap.put("create_time", create_time);
				allList.add(mmap);
			}
		} else {//根据订单号查询与订单号关联的售后订单
			MDataMap orderinfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code_detail);
			if (orderinfo == null || orderinfo.isEmpty() || "SI2003".equals(orderinfo.get("small_seller_code"))) {// 此订单为LD订单
				ldList = this.getLdList(buyer_code, order_code_detail,asale_status_detail,appVersion);
				for (Map<String, Object> map : ldList) {
					MDataMap mmap = new MDataMap();
					String order_code = map.get("order_code") != null ? map.get("order_code").toString() : "";
					String asale_code = map.get("asale_code") != null ? map.get("asale_code").toString() : "";
					String asale_status = map.get("asale_status") != null ? map.get("asale_status").toString() : "";
					String create_time = map.get("create_time") != null ? map.get("create_time").toString() : "";
					String after_sale_type = map.get("after_sale_type") != null ? map.get("after_sale_type").toString() : "1";
					String chg_ord = map.get("chg_ord") != null ? map.get("chg_ord").toString() : "";
					mmap.put("order_code", order_code);
					mmap.put("asale_code", asale_code);
					mmap.put("asale_status", asale_status);
					mmap.put("after_sale_type", after_sale_type);
					mmap.put("chg_ord", chg_ord);
					mmap.put("ld", "ld");
					mmap.put("create_time", create_time);
					allList.add(mmap);
				}
			} else {//不是LD订单，取本地的订单数据
				if ("0".equals(asale_status_detail)) {// 未完成
					sql = "select * from ordercenter.oc_order_after_sale where buyer_code = '"
							+ buyer_code
							+ "' and show_flag = 1 and order_code = '"
							+ order_code_detail
							+ "' and  asale_status in ('4497477800050001','4497477800050003','4497477800050005','4497477800050010','4497477800050013') order by zid desc";
				} else {
					sql = "select * from ordercenter.oc_order_after_sale where buyer_code = '"
							+ buyer_code
							+ "' and show_flag = 1 and order_code = '"
							+ order_code_detail
							+ "' and asale_status in ('4497477800050002','4497477800050004','4497477800050006','4497477800050007','4497477800050008','4497477800050009','4497477800050011') order by zid desc";
				}
				List<Map<String, Object>> app = DbUp.upTable("oc_order_after_sale").dataSqlList(sql, null);
				if (app != null) {
					for (Map<String, Object> map : app) {
						MDataMap changeMap = new MDataMap(map);
						allList.add(changeMap);
					}
				}
			}
		}
		// 分页开始前做排序
		List<MDataMap> allList2 = this.changeSeq(allList);
		// 集合做分页开始
		List<MDataMap> list = new ArrayList<MDataMap>();
		int currIdx = (page > 1 ? (page - 1) * 10 : 0);
		for (int i = 0; i < 10 && i < allList2.size() - currIdx; i++) {
			MDataMap map = allList2.get(currIdx + i);
			list.add(map);
		}
		// 集合做分页结束
		if (list != null && !list.isEmpty()) {
			for (MDataMap mDataMap : list) {
				String ld = mDataMap.get("ld");
				String order_code = mDataMap.get("order_code");
				String asale_code = mDataMap.get("asale_code");
				String asale_status = mDataMap.get("asale_status");
				String after_sale_type = mDataMap.get("after_sale_type");//1:退货退款，2：换货
				if (StringUtils.isEmpty(ld)) {// APP售后单
					AfterSale afterSale = new AfterSale();
					afterSale.setAfterCode(asale_code);
					String saleStatusStr = (String) DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code", asale_status));
					String asale_type = "";
					try {
						asale_type = DbUp.upTable("oc_order_after_sale").one("asale_code", afterSale.getAfterCode()).get("asale_type");
					} catch (Exception e) {
						e.getStackTrace();
					}
					if ("拒绝申请".equals(saleStatusStr)) {
						if ("4497477800030001".equals(asale_type)) {
							saleStatusStr = "退货失败";
						} else if ("4497477800030003".equals(asale_type)) {//换货
							saleStatusStr = "换货失败";
						}
					}
					afterSale.setAfterStatus(saleStatusStr);
					afterSale.setOrderCode(order_code);

					List<MDataMap> adList = DbUp.upTable("oc_order_after_sale_dtail").queryByWhere("asale_code", asale_code);

					for (MDataMap ad : adList) {

						String sku_code = ad.get("sku_code");
						String sku_price = ad.get("sku_price");
						String mainpic_url = ad.get("product_picurl");
						String sku_num = ad.get("sku_num");
						String product_code = ad.get("product_code");

						PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(product_code);
						// 524:添加商品分类标签
						PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
						String ssc = productInfo.getSmallSellerCode();
						String st = "";
						if ("SI2003".equals(ssc)) {
							st = "4497478100050000";
						} else {
							st = WebHelper.getSellerType(ssc);
						}
						// 获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
						Map productTypeMap = WebHelper.getAttributeProductType(st);

						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						skuQuery.setCode(sku_code);
						PlusModelSkuInfo skuInfo = new LoadSkuInfo().upInfoByCode(skuQuery);

						Product product = new Product();
						product.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());

						List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService().sellerStandardAndStyle(skuInfo.getSkuKeyvalue()); // 截取 尺码和款型
						if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
							for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
								ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
								apiSellerStandardAndStyleResult
										.setStandardAndStyleKey(pcPropertyinfoForFamily
												.getPropertyKey());
								apiSellerStandardAndStyleResult
										.setStandardAndStyleValue(pcPropertyinfoForFamily
												.getPropertyValue());
								product.getStandardAndStyleList().add(
										apiSellerStandardAndStyleResult);
							}
						}

						product.setLabelsList(productInfo.getLabelsList());
						product.setProduct_name(productInfo.getProductName());
						product.setLablesPic("");
						product.setMainpic_url(mainpic_url);
						product.setProduct_code(product_code);
						product.setProduct_number(sku_num);

						// 积分兑换
						Map<String, Object> map = DbUp.upTable("oc_orderinfo")
										.dataSqlOne(
												"select i.order_type, d.show_price, d.sku_num, d.integral_money from oc_orderinfo i, oc_orderdetail d where i.order_code = d.order_code"
												+ " and i.order_code = '" + order_code + "' and d.sku_code = '" + sku_code + "'", new MDataMap());
						if (map != null) {
							String orderType = MapUtils.getString(map, "order_type", "");
							if ("449715200024".equals(orderType)) {
								BigDecimal showPrice = new BigDecimal(MapUtils.getString(map, "show_price", "0"));
								BigDecimal skuNum = new BigDecimal(MapUtils.getString(map, "sku_num", "0"));
								BigDecimal integralMoney = new BigDecimal(MapUtils.getString(map, "integral_money", "0")).divide(skuNum, BigDecimal.ROUND_HALF_UP);

								product.setSell_price(showPrice.subtract(integralMoney).toString());
								product.setIntegral(integralMoney.multiply(new BigDecimal(200)).toString());
							} else {
								product.setSell_price(sku_price);
							}
						}
						afterSale.getProductList().add(product);
					}

					if ("4497477800050010".equals(asale_status)) {// 填写物流
						Button button = new Button();
						button.setButtonCode("4497477800080001");
						button.setButtonTitle(bConfig("familyhas.define_4497477800080001"));
						afterSale.getOrderButtonList().add(button);
					}
					if ("4497477800030001".equals(asale_type)) {// 新增按钮，取消申请
						if ("4497477800050003".equals(asale_status)||"4497477800050005".equals(asale_status)||"4497477800050010".equals(asale_status)) {// 等待审核、待完善物流、待商户审核 可以取消		
						Button button = new Button();
							button.setButtonCode("4497477800050011");
							button.setButtonTitle("取消申请");
							if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){
								afterSale.getOrderButtonList().add(button);
							}
						}
					}else if ("4497477800030003".equals(asale_type)) {//换货
						if ("4497477800050003".equals(asale_status)) {// 等待审核可以取消						
							Button button = new Button();
							button.setButtonCode("4497477800050011");
							button.setButtonTitle("取消申请");
							if(AppVersionUtils.compareTo("5.3.6", appVersion) < 0){
								afterSale.getOrderButtonList().add(button);
							}
						}
					}
					result.getOrderList().add(afterSale);
				} else { //LD订单的售后详情
					AfterSale afterSale = new AfterSale();
					afterSale.setAfterCode(asale_code);
					// 退货：售后状态 01 ：待审核，02 ：退货中 03：退货失败 04：取消申请 05：退货完成 06：取消中 09:退款中,10:退款完成
					// 换货：售后状态 01：待审核 ，02 ：换货中 03：未通过审核 04：取消售后单 05：换货完成 06:取消失败（取消中/换货中）07：已出库
					// 00：异常订单
					if("1".equals(after_sale_type)) {//退货的状态
						if ("01".equals(asale_status)) {
							afterSale.setAfterStatus("待审核");
						} else if ("02".equals(asale_status)) {
							afterSale.setAfterStatus("退货中");
						} else if ("03".equals(asale_status)) {
							afterSale.setAfterStatus("退货失败");
						} else if ("04".equals(asale_status)) {
							afterSale.setAfterStatus("取消申请");
						} else if ("05".equals(asale_status)) {
							afterSale.setAfterStatus("退货完成");
						} else if ("06".equals(asale_status)) {
							afterSale.setAfterStatus("取消退货中");
						}else if ("09".equals(asale_status)) {
							afterSale.setAfterStatus("退款处理中");
						}else if ("10".equals(asale_status)) {
							afterSale.setAfterStatus("退款完成");
						}
					} else {
						if ("01".equals(asale_status)) {
							afterSale.setAfterStatus("待审核");
						} else if ("02".equals(asale_status)) {
							afterSale.setAfterStatus("换货中");
						} else if ("03".equals(asale_status)) {
							afterSale.setAfterStatus("换货关闭");
						} else if ("04".equals(asale_status)) {
							afterSale.setAfterStatus("换货取消");
						} else if ("05".equals(asale_status)) {
							afterSale.setAfterStatus("换货完成");
						} else if ("06".equals(asale_status)) {
							afterSale.setAfterStatus("取消换货中");
						} else if ("07".equals(asale_status)) {
							afterSale.setAfterStatus("已出库");
						} else if ("08".equals(asale_status)) {
							afterSale.setAfterStatus("换货失败");
						}
					}
					
					afterSale.setOrderCode(order_code);
					afterSale.setIfShowDetail("1");// 不显示
					List<Button> orderButtonList = new ArrayList<Button>();
					List<Product> productList = new ArrayList<Product>();
					for (Map<String, Object> map : ldList) {
						String asale_code_ld = map.get("asale_code").toString();
						String asale_code_change = mDataMap.get("asale_code");
						if (asale_code_ld.equals(asale_code_change)) {
							orderButtonList = (List<Button>) map.get("orderButtonList");
							productList = (List<Product>) map.get("productList");
						}
					}
					afterSale.setOrderButtonList(orderButtonList);
					// LD 售後单添加商品信息
					for (Product productInfo : productList) {
						this.setProductInfo(productInfo, asale_code, order_code);
					}
					afterSale.setProductList(productList);
					result.getOrderList().add(afterSale);
				}
			}
			result.setCountPage(Double.valueOf(Math.ceil(allList2.size() / 10d)).intValue());
			result.setNowPage(page);
		}
		return result;

	}
	
	/**
	 * 获取商品详情
	 * 
	 * @param productInfo
	 * @param asale_code
	 */
	private void setProductInfo(Product productInfo, String asale_code, String order_code) {
		MDataMap ldAfterSale = new MDataMap();
		if (asale_code.contains("LD")) {
			ldAfterSale = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_app", asale_code);
		} else {
			ldAfterSale = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_ld", asale_code);
		}
		if (ldAfterSale == null || ldAfterSale.isEmpty()) {
			//根据售后单号查询售后详情
			ApiForCancelReturn afr = new ApiForCancelReturn();
			ldAfterSale = new MDataMap(afr.getLdAfterSaleOrder(asale_code));
		}
		String skuCode = ldAfterSale.get("sku_code");
		productInfo.setSell_price(ldAfterSale.get("return_money"));// 赋值当时售价
		String productCode = productInfo.getProduct_code();
		MDataMap skuInfo = null;
		try{
			if(!StringUtils.isEmpty(skuCode)){
				skuInfo = DbUp.upTable("pc_skuinfo").one("sku_code", skuCode, "product_code", productCode);
			}
		}catch(Exception e){
			e.getStackTrace();
		}
		MDataMap productMap = DbUp.upTable("pc_productinfo").one("product_code", productCode);
		if (productMap != null && !productMap.isEmpty()) {
			productInfo.setProduct_name(productMap.get("product_name"));
			productInfo.setMainpic_url(productMap.get("mainpic_url"));
		}
		
		productInfo.setLablesPic("");
		productInfo.setProduct_number(ldAfterSale.get("good_cnt"));
		MDataMap orderinfo = DbUp.upTable("oc_orderinfo").one("out_order_code", order_code);
		if (orderinfo != null && !orderinfo.isEmpty()) {// APP订单
			if (skuInfo == null || skuInfo.isEmpty()) {
				//LD 下的退货，没有sku 编号
				//需要获取颜色，款式
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ordId", Integer.parseInt(ldAfterSale.get("order_code")));
				if(!StringUtils.isEmpty(ldAfterSale.get("order_seq"))){
					params.put("ordSeq", Integer.parseInt(ldAfterSale.get("order_seq")));
				}
				String url = bConfig("groupcenter.rsync_homehas_url")+ "getOrderDetailById";
				String orderStr = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey("颜色");
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult2 = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult2.setStandardAndStyleKey("款式");
				if(StringUtils.isEmpty(orderStr)){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				JSONObject jo = JSONObject.parseObject(orderStr);
				Integer code = jo.getInteger("code");
				if(code != 0){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				String orderListStr = jo.getString("result");
				if(StringUtils.isEmpty(orderListStr)){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				JSONArray ja = JSONArray.parseArray(orderListStr);
				Iterator it = ja.iterator();
				while(it.hasNext()){
					JSONObject oo = (JSONObject)it.next();
					apiSellerStandardAndStyleResult.setStandardAndStyleValue(oo.getString("COLOR_DESC"));
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue(oo.getString("STYLE_DESC"));
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					productInfo.setProduct_name(oo.getString("GOOD_NM"));
					Integer styleId = oo.getInteger("STYLE_ID")!=null?oo.getInteger("STYLE_ID"):0;
					Integer colorId = oo.getInteger("COLOR_ID")!=null?oo.getInteger("COLOR_ID"):0;
					String sku_key = "color_id="+colorId+"&style_id="+styleId;
					try{
						skuInfo = DbUp.upTable("pc_skuinfo").one("product_code",productCode,"sku_key",sku_key);
					}catch(Exception e){
						e.getStackTrace();
					}
					if(skuInfo == null){
						return;
					}
					MDataMap orderDetail = null;
					try{
						orderDetail = DbUp.upTable("oc_orderdetail").one("order_code",orderinfo.get("order_code"),"sku_code",skuInfo.get("sku_code"));
					}catch(Exception e){
						e.getStackTrace();
					}
					if(orderDetail == null||orderDetail.isEmpty()){
						return;
					}
					productInfo.setSell_price(orderDetail.get("sku_price"));
					return;
				}
			}
			List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService().sellerStandardAndStyle(skuInfo.get("sku_keyvalue")); // 截取
			// 尺码和款型																
			if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
				for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
					ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
					apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
					apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
				}
			}
		} else {
			//APP查询不到商品详情的时候，需要获取商品颜色款式，通过LD获取
			if (skuInfo == null || skuInfo.isEmpty()) {
				//需要获取颜色，款式
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ordId", Integer.parseInt(ldAfterSale.get("order_code")));
				if(!StringUtils.isEmpty(ldAfterSale.get("order_seq"))){
					params.put("ordSeq", Integer.parseInt(ldAfterSale.get("order_seq")));
				}
				String url = bConfig("groupcenter.rsync_homehas_url")+ "getOrderDetailById";
				String orderStr = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey("颜色");
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult2 = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult2.setStandardAndStyleKey("款式");
				if(StringUtils.isEmpty(orderStr)){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				JSONObject jo = JSONObject.parseObject(orderStr);
				Integer code = jo.getInteger("code");
				if(code != 0){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				String orderListStr = jo.getString("result");
				if(StringUtils.isEmpty(orderListStr)){
					apiSellerStandardAndStyleResult.setStandardAndStyleValue("共同");
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue("共同");
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					return;
				}
				JSONArray ja = JSONArray.parseArray(orderListStr);
				Iterator it = ja.iterator();
				while(it.hasNext()){
					JSONObject oo = (JSONObject)it.next();
					apiSellerStandardAndStyleResult.setStandardAndStyleValue(oo.getString("COLOR_DESC"));
					apiSellerStandardAndStyleResult2.setStandardAndStyleValue(oo.getString("STYLE_DESC"));
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult2);
					productInfo.setProduct_name(oo.getString("GOOD_NM"));
					return;
				}
			}
			List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService().sellerStandardAndStyle(skuInfo.get("sku_keyvalue")); // 截取
			// 尺码和款型
			if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
				for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
					ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
					apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
					apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
					productInfo.getStandardAndStyleList().add(apiSellerStandardAndStyleResult);
				}
			}

		}
	}

	/**
	 * 给已有List按照时间做排序
	 * 
	 * @param allList
	 * @return
	 */
	private List<MDataMap> changeSeq(List<MDataMap> allList) {
		Collections.sort(allList, new Comparator<MDataMap>() {
			public int compare(MDataMap o1, MDataMap o2) {
				String hits0 = o1.get("create_time");
				String hits1 = o2.get("create_time");
				if (compareTime(hits0, hits1) < 0) {
					return 1;
				} else if (hits0.equals(hits1)) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		return allList;
	}

	/**
	 * 比较两个时间 时间格式：2014-12-02 20:14:10 <br>
	 * 大于结束时间返回正数，等于 0，小于 负数
	 * 
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public synchronized static int compareTime(String start_time,
			String end_time) {
		try {
			Date date1 = DateUtil.sdfDateTime.parse(start_time);
			Date date2 = DateUtil.sdfDateTime.parse(end_time);
			return date1.compareTo(date2);
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * 通过接口获取LD订单
	 * 
	 * @param buyer_code
	 * @param order_code_detail
	 * @return
	 */
	private List<Map<String, Object>> getLdList(String buyer_code,
			String order_code_detail, String asale_status_detail,String appVersion) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if ("N".equals(isSyncLd)) {// 添加开关
			return list;
		}
		String url = bConfig("groupcenter.rsync_homehas_url") + "getAfterServiceList";
		if (StringUtils.isEmpty(buyer_code) && StringUtils.isEmpty(order_code_detail)) {
			return list;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		Integer ordId = 0;
		try {
			ordId = Integer.parseInt(order_code_detail);
		} catch (Exception e) {
		}
		if (ordId == 0 && !StringUtils.isEmpty(order_code_detail)) {// 入参订单号没有转换过来，需要用三方订单号去查
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", order_code_detail);
			if (orderInfo != null && !orderInfo.isEmpty()) {
				try {
					ordId = Integer.parseInt(orderInfo.get("out_order_code"));//取外部订单号
				} catch (Exception e) {
				}
			}
		}
		String phoneNo = "";
		if (!StringUtils.isEmpty(order_code_detail)) {
			params.put("ordId", ordId);
		} else {
			MDataMap member = DbUp.upTable("mc_login_info").one("member_code", buyer_code);
			phoneNo = member.get("login_name");
			params.put("mobile", phoneNo);
		}
		Long l= System.currentTimeMillis();
		String result = HttpUtil.post(url, JSONObject.toJSONString(params), "UTF-8");
		System.out.println("接口获取数据耗时："+(System.currentTimeMillis()-l));
		if (StringUtils.isEmpty(result)) {
			return list;
		}
		JSONObject jo = JSONObject.parseObject(result);
		String codeStr = jo.getString("code");
		if (StringUtils.isEmpty(codeStr)) {
			return list;
		}
		if (jo.getInteger("code") != 0) {
			return list;
		}
		String jsonArrayStr = jo.getString("result");
		if (StringUtils.isEmpty(jsonArrayStr)) {
			return list;
		}
		//==========查询本地未推送的售后单begin===========
		String sql = "";
		if (ordId != 0) { //根据订单编号查询该订单的售后详情
			sql = "SELECT * FROM ordercenter.oc_after_sale_ld WHERE if_post != 1 and order_code = '" + ordId + "' and after_sale_status != '00'";//00：异常订单
		} else { //根据买家编号查询该用户所有订单的售后详情
			sql = "SELECT * FROM ordercenter.oc_after_sale_ld WHERE if_post != 1 and member_code = '" + buyer_code + "' and after_sale_status != '00'";
		}
		List<Map<String, Object>> listLocal = DbUp.upTable("oc_after_sale_ld").dataSqlList(sql, null);
		List<Map<String, Object>> listOne = new ArrayList<Map<String, Object>>();// 本地的售后单
		for (Map<String, Object> map : listLocal) {
			Map<String, Object> afterSale = new HashMap<String, Object>();
			String cd = map.get("after_sale_status") != null ? map.get("after_sale_status").toString() : "01";//01：待审核
			afterSale.put("order_code", map.get("order_code"));// LD 订单编号
			afterSale.put("asale_status", cd);// LD 售后状态
			afterSale.put("after_sale_type", map.get("after_sale_type"));// 1：退货退款 2：换货
			afterSale.put("asale_code", map.get("after_sale_code_app") != null ? map.get("after_sale_code_app").toString() : "");
			afterSale.put("chg_ord", map.get("chg_ord") == null ? "" : map.get("chg_ord").toString());// 换货新单号
			afterSale.put("create_time", map.get("create_time"));
			List<Button> orderButtonList = new ArrayList<Button>();
			List<Product> productList = new ArrayList<Product>();
			Product product = new Product();
			product.setProduct_code(map.get("product_code") != null ? map.get("product_code").toString() : "");
			productList.add(product);
			if ("0".equals(asale_status_detail)) {// 处理中订单
				if ("04".equals(cd) || "03".equals(cd) || "08".equals(cd)||"10".equals(cd)) {// 已完成订单
					continue;
				}
				if("1".equals(map.get("after_sale_type"))) {
					Button cancelApply = new Button();
					cancelApply.setButtonCode("4497477800050011");
					cancelApply.setButtonTitle("取消申请");
					if ("01".equals(cd)||"02".equals(cd)) {
						if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){
							orderButtonList.add(cancelApply);
						}
					}
				} else {
					Button cancelApply = new Button();
					cancelApply.setButtonCode("4497477800050011");
					cancelApply.setButtonTitle("取消申请");
					if (!"06".equals(cd) && !"07".equals(cd) && !"05".equals(cd)) {
						if(AppVersionUtils.compareTo("5.3.0", appVersion)<0){
							orderButtonList.add(cancelApply);
						}
					}
				}
				
			} else {// 处理完成的单子
				if ("05".equals(cd) ||"01".equals(cd) || "02".equals(cd) || "06".equals(cd) || "07".equals(cd)|| "09".equals(cd)) {
					continue;
				}
			}
			afterSale.put("orderButtonList", orderButtonList);
			afterSale.put("productList", productList);
			listOne.add(afterSale);
		}
		//==========查询本地未推送的售后单end===========
		JSONArray ja = JSONArray.parseArray(jsonArrayStr);
		Iterator it = ja.iterator();
		List<Map<String, Object>> listTwo = new ArrayList<Map<String, Object>>();// 接口售后单
		while (it.hasNext()) {
			JSONObject oo = (JSONObject) it.next();
			Map<String, Object> afterSale = new HashMap<String, Object>();
			String cd = oo.getString("AFTER_SALE_CD");
			afterSale.put("order_code", oo.getString("ORD_ID"));// LD 订单编号
			if (StringUtils.isEmpty(oo.getString("AFTER_SALE_CODE_APP"))) {// APP售后单号为空，LD发起的
				afterSale.put("asale_code", oo.getString("AFTER_SALE_CODE_LD"));// LD 售后编号
			} else {
				afterSale.put("asale_code", oo.getString("AFTER_SALE_CODE_APP"));// LD 售后编号
			}
			afterSale.put("asale_status", cd);// LD 售后状态
			String after_sale_type = StringUtils.isEmpty(oo.getString("AFTER_SALE_TYPE")) ? "T" : oo.getString("AFTER_SALE_TYPE");
			afterSale.put("after_sale_type", "T".equals(after_sale_type) ? "1" : "2");// T：退货 H:换货
			afterSale.put("chg_ord", oo.getString("NEW_ORD_ID"));// 换货新单号
			String etr_date = oo.getString("ETR_DATE");
			String create_time = timeStamp2Date(etr_date, "yyyy-MM-dd HH:mm:ss");
			afterSale.put("create_time", create_time);
			List<Button> orderButtonList = new ArrayList<Button>();
			List<Product> productList = new ArrayList<Product>();
			Product product = new Product();
			product.setProduct_code(oo.getString("GOOD_ID"));
			productList.add(product);
			if ("0".equals(asale_status_detail)) {// 处理中订单
				if ("04".equals(cd) || "03".equals(cd) || "08".equals(cd)||"10".equals(cd)) {// 已完成订单与处理中的单子不允许再次取消
					continue;
				}
				if("T".equals(after_sale_type)) {
					Button cancelApply = new Button();
					cancelApply.setButtonCode("4497477800050011");
					cancelApply.setButtonTitle("取消申请");
					if ("01".equals(cd)||"02".equals(cd)) {
						if(AppVersionUtils.compareTo("5.2.7", appVersion)<0){
							orderButtonList.add(cancelApply);
						}
					}
				} else {
					Button cancelApply = new Button();
					cancelApply.setButtonCode("4497477800050011");
					cancelApply.setButtonTitle("取消申请");
					if (!"06".equals(cd) && !"07".equals(cd) && !"05".equals(cd)) {
						if(AppVersionUtils.compareTo("5.3.0", appVersion)<0){
							orderButtonList.add(cancelApply);
						}
					}
				}
				
			} else {// 处理完成的单子
				if ("05".equals(cd) ||"01".equals(cd) || "02".equals(cd) || "06".equals(cd) || "07".equals(cd)|| "09".equals(cd)) {
					continue;
				}
			}
			afterSale.put("orderButtonList", orderButtonList);
			afterSale.put("productList", productList);
			listTwo.add(afterSale);
		}
		aa: for (Map<String, Object> map : listTwo) {
			String asale_code = map.get("asale_code") != null ? map.get("asale_code").toString() : "";
			for (Map<String, Object> mapOne : listOne) {
				String asale_code1 = mapOne.get("asale_code") != null ? mapOne.get("asale_code").toString() : "";
				if (asale_code1.equals(asale_code)) {// 本地有，取本地，不要接口数据
					continue aa;
				}
			}
			list.add(map);
		}
		for (Map<String, Object> mapOne : listOne) {
			list.add(mapOne);
		}

		return list;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到毫秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds)));
	}

	/**
	 * 老逻辑的获取订单售后详情
	 * 
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiForAftermarketListResult getOldList(
			ApiForAftermarketListInput inputParam, MDataMap mRequestMap,String appVersion) {
		ApiForAftermarketListResult result = new ApiForAftermarketListResult();
		int page = inputParam.getPage();
		String buyer_code = getUserCode();
		List<MDataMap> list = DbUp.upTable("oc_order_after_sale").query("",
				"zid desc ",
				"buyer_code=:buyer_code and asale_source='4497477800060001'",
				new MDataMap("buyer_code", buyer_code), (page - 1) * 10, 10);
		if (list != null && !list.isEmpty()) {

			for (MDataMap mDataMap : list) {

				String order_code = mDataMap.get("order_code");
				String asale_code = mDataMap.get("asale_code");
				String asale_status = mDataMap.get("asale_status");
				AfterSale afterSale = new AfterSale();
				afterSale.setAfterCode(asale_code);
				String saleStatusStr = (String) DbUp.upTable("sc_define").dataGet("define_name", "define_code=:define_code", new MDataMap("define_code", asale_status));
				String asale_type = "";
				try {
					asale_type = DbUp.upTable("oc_order_after_sale").one("asale_code", afterSale.getAfterCode()).get("asale_type");
				} catch (Exception e) {
					e.getStackTrace();
				}
				if ("拒绝申请".equals(saleStatusStr)) {
					if ("4497477800030001".equals(asale_type)) {
						saleStatusStr = "退货失败";
					} else if("4497477800030003".equals(asale_type)) {
						saleStatusStr = "换货失败";
					}
				}
				afterSale.setAfterStatus(saleStatusStr);
				afterSale.setOrderCode(order_code);

				List<MDataMap> adList = DbUp.upTable(
						"oc_order_after_sale_dtail").queryByWhere("asale_code",
						asale_code);

				for (MDataMap ad : adList) {

					String sku_code = ad.get("sku_code");
					String sku_price = ad.get("sku_price");
					String mainpic_url = ad.get("product_picurl");
					String sku_num = ad.get("sku_num");
					String product_code = ad.get("product_code");

					PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(
							product_code);
					// 524:添加商品分类标签
					PlusModelProductInfo productInfo = new LoadProductInfo()
							.upInfoByCode(plusModelProductQuery);
					String ssc = productInfo.getSmallSellerCode();
					String st = "";
					if ("SI2003".equals(ssc)) {
						st = "4497478100050000";
					} else {
						st = WebHelper.getSellerType(ssc);
					}
					// 获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
					Map productTypeMap = WebHelper.getAttributeProductType(st);

					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(sku_code);
					PlusModelSkuInfo skuInfo = new LoadSkuInfo()
							.upInfoByCode(skuQuery);

					Product product = new Product();

					product.setProClassifyTag(productTypeMap.get(
							"proTypeListPic").toString());

					List<PcPropertyinfoForFamily> standardAndStyleList = new OrderService()
							.sellerStandardAndStyle(skuInfo.getSkuKeyvalue()); // 截取
																				// 尺码和款型
					if (standardAndStyleList != null
							&& !"".equals(standardAndStyleList)) {
						for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
							ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
							apiSellerStandardAndStyleResult
									.setStandardAndStyleKey(pcPropertyinfoForFamily
											.getPropertyKey());
							apiSellerStandardAndStyleResult
									.setStandardAndStyleValue(pcPropertyinfoForFamily
											.getPropertyValue());
							product.getStandardAndStyleList().add(
									apiSellerStandardAndStyleResult);
						}
					}

					product.setLabelsList(productInfo.getLabelsList());
					product.setProduct_name(productInfo.getProductName());
					product.setLablesPic("");
					product.setMainpic_url(mainpic_url);
					product.setProduct_code(product_code);
					product.setProduct_number(sku_num);

					// 积分兑换
					Map<String, Object> map = DbUp
							.upTable("oc_orderinfo")
							.dataSqlOne(
									"select i.order_type, d.show_price, d.sku_num, d.integral_money from oc_orderinfo i, oc_orderdetail d where i.order_code = d.order_code"
											+ " and i.order_code = '"
											+ order_code
											+ "' and d.sku_code = '"
											+ sku_code
											+ "'", new MDataMap());
					if (map != null) {
						String orderType = MapUtils.getString(map,
								"order_type", "");
						if ("449715200024".equals(orderType)) {
							BigDecimal showPrice = new BigDecimal(
									MapUtils.getString(map, "show_price", "0"));
							BigDecimal skuNum = new BigDecimal(
									MapUtils.getString(map, "sku_num", "0"));
							BigDecimal integralMoney = new BigDecimal(
									MapUtils.getString(map, "integral_money",
											"0")).divide(skuNum,
									BigDecimal.ROUND_HALF_UP);

							product.setSell_price(showPrice.subtract(
									integralMoney).toString());
							product.setIntegral(integralMoney.multiply(
									new BigDecimal(200)).toString());
						} else {
							product.setSell_price(sku_price);
						}
					}

					afterSale.getProductList().add(product);

				}

				if ("4497477800050010".equals(asale_status)) {// 填写物流
					Button button = new Button();
					button.setButtonCode("4497477800080001");
					button.setButtonTitle(bConfig("familyhas.define_4497477800080001"));
					afterSale.getOrderButtonList().add(button);
				}
				if ("4497477800030001".equals(asale_type)) {// 新增按钮，取消申请
					if ("4497477800050003".equals(asale_status)||"4497477800050010".equals(asale_status)||"4497477800050005".equals(asale_status)) {
						Button button = new Button();
						button.setButtonCode("4497477800050011");
						button.setButtonTitle("取消申请");
						if(AppVersionUtils.compareTo("5.2.7", appVersion) < 0){
							afterSale.getOrderButtonList().add(button);
						}
					}
				}else if("4497477800030003".equals(asale_type)) {
					if ("4497477800050003".equals(asale_status)) {// 等待审核				
						Button button = new Button();
						button.setButtonCode("4497477800050011");
						button.setButtonTitle("取消申请");
						if(AppVersionUtils.compareTo("5.3.6", appVersion) < 0){
							afterSale.getOrderButtonList().add(button);
						}
					}
				}

				result.getOrderList().add(afterSale);
			}

			result.setCountPage(Double.valueOf(
					Math.ceil(DbUp.upTable("oc_order_after_sale").count(
							"buyer_code", buyer_code, "asale_source",
							"4497477800060001") / 10d)).intValue());
			result.setNowPage(page);
		}
		return result;
	}
	
}