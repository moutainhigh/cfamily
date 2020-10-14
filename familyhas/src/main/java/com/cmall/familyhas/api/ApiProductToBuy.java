package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.input.ApiProductToBuyInput;
import com.cmall.familyhas.api.result.ApiProductToBuyResult;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.groupcenter.homehas.model.RsyncModelThirdOrderDetail;
import com.cmall.ordercenter.service.OrderService;
import com.srnpr.xmasorder.model.ShoppingCartCache;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;


/**
 * 下单后再次购买接口
 * 将购买过的商品加入购物车
 * 
 * @author fq
 *
 */
public class ApiProductToBuy extends RootApiForMember<RootResult, ApiProductToBuyInput>{

	@Override
	public RootResult Process(ApiProductToBuyInput inputParam, MDataMap mRequestMap) {
		
		ApiProductToBuyResult result = new ApiProductToBuyResult();
		
		String order_code = inputParam.getOrder_code();//经产品确认，再次购买不支持大单号
		
		if("DD".equals(order_code.substring(0, 2)) || "HH".equals(order_code.substring(0, 2))) {//大单号再次购买
			MDataMap one = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			if(null != one) {
				String orderStatus = one.get("order_status");
				
				if("4497153900010005".equals(orderStatus) || "4497153900010006".equals(orderStatus)) {//只有订单交易成功或者订单交易失败的可以再次购买
					
					List<String> faildGoodInfo = new ArrayList<String>();//失效商品不加入购物车
					List<ShoppingCartGoodsInfoForAdd>  shoppingcartgoodslist = new ArrayList<ShoppingCartGoodsInfoForAdd>();
					//查询订单详情表里商品编号及商品数量
					MDataMap paramMap = new MDataMap();
					paramMap.put("order_code", order_code);
					List<MDataMap> queryAll = DbUp.upTable("oc_orderdetail").queryAll("product_code,sku_code,sku_num", "", "order_code=:order_code and gift_flag = '1'", paramMap);
					for (MDataMap map : queryAll) {
						
						String product_code = String.valueOf(map.get("product_code"));
						String sku_code = String.valueOf(map.get("sku_code"));
						PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(product_code) );
						// 查询商品信息
						PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(sku_code, getOauthInfo().getUserCode(),getOauthInfo().getUserCode(),0);
						//过滤失效商品
						if(!"4497153900060002".equals(upInfoByCode.getProductStatus()) || !"Y".equalsIgnoreCase(plusModelSkuInfo.getSaleYn())){//商品非上架状态，或者sku不可售  都是失效商品
							faildGoodInfo.add(plusModelSkuInfo.getProductCode()+"-"+plusModelSkuInfo.getSkuCode());
							continue;
						}
						ShoppingCartGoodsInfoForAdd shopCartGoodInfo = new ShoppingCartGoodsInfoForAdd();
						shopCartGoodInfo.setChooseFlag("1");//选中
						shopCartGoodInfo.setProduct_code(product_code);//商品编号
						shopCartGoodInfo.setSku_code(sku_code);//sku编号
						shopCartGoodInfo.setSku_num(Integer.valueOf(String.valueOf(map.get("sku_num"))));//商品数量
						
						shoppingcartgoodslist.add(shopCartGoodInfo);
					}
					
					if(shoppingcartgoodslist.size() > 0) {
						//加购买的商品加入购物车
						ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
						if(getFlagLogin()){
							ShoppingCartCacheInfo info =  serviceForCache.queryShopCart(getOauthInfo().getUserCode(),new ArrayList<ShoppingCartGoodsInfoForAdd>());
							List<ShoppingCartCache> caches = info.getCaches();
							//将要加入购物车的商品和购物车里的商品进行合并
							for (int i = 0; i < caches.size(); i++) {
								ShoppingCartCache cache = caches.get(i);
								for (int j = 0; j < shoppingcartgoodslist.size(); j++) {
									ShoppingCartGoodsInfoForAdd good = shoppingcartgoodslist.get(j);
									if(good.getSku_code().equals(cache.getSku_code())){//购物车包含此商品
										//已存在，做加操作
										BigDecimal skuNum = BigDecimal.valueOf(good.getSku_num()).add(BigDecimal.valueOf(cache.getSku_num()));
										if(skuNum.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0 ) {
											good.setSku_num(Integer.MAX_VALUE);
										} else {
											good.setSku_num(skuNum.setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
										}
										good.setChooseFlag("1");//且是选中状态
										
									} 
								}
							}
							//保存购物车
							serviceForCache.saveShopCart(shoppingcartgoodslist, getOauthInfo().getUserCode());
						} else {
							result.setResultCode(0);
							result.setResultMessage("用户未登录，请登录！！");
						}
						
					} 
					
					if(shoppingcartgoodslist.size() == 0) {//没有要加入购物车的商品信息，则认为没有有效的商品
						result.setResult_status(2);
					} else if(faildGoodInfo.size() > 0) {//失败的数据
						result.setResult_status(1);
					}
					
					
				} else {
					result.setResultCode(0);
					result.setResultMessage("此订单["+order_code+"]不支持再次购买!");
				}
				
				
			} else {
				result.setResultCode(0);
				result.setResultMessage("订单不存在");
				return result;
			}
			
			
		}
		else if(!"DD".equals(order_code.substring(0, 2)) && !"OS".equals(order_code.substring(0, 2)) && !"HH".equals(order_code.substring(0, 2))) {
			//LD订单再次购买
			RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(order_code);
			rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
			rsyncGetThirdOrderDetail.doRsync();
			if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
				RsyncModelThirdOrderDetail orderdetail = rsyncGetThirdOrderDetail.getResponseObject().getResult().get(0);
				String order_status = convertOrderStatus(orderdetail.getOrd_stat());
				if("4497153900010005".equals(order_status) || "4497153900010006".equals(order_status)) {//只有订单交易成功或者订单交易失败的可以再次购买
					List<String> faildGoodInfo = new ArrayList<String>();//失效商品不加入购物车
					List<ShoppingCartGoodsInfoForAdd>  shoppingcartgoodslist = new ArrayList<ShoppingCartGoodsInfoForAdd>();
					OrderService orderService = new OrderService();
					for(RsyncModelThirdOrderDetail detail : rsyncGetThirdOrderDetail.getResponseObject().getResult()) {
						String product_code = detail.getGood_id().toString();
						String sku_key = "color_id=" + detail.getColor_id().toString() + "&style_id=" + detail.getStyle_id().toString();
						String skuCode = orderService.getSkuCodeByColorStyle(product_code, sku_key);
						if(!"".equals(skuCode)) {
							PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(product_code) );
							// 查询商品信息
							PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(skuCode, getOauthInfo().getUserCode(),getOauthInfo().getUserCode(),0);
							//过滤失效商品
							if(!"4497153900060002".equals(upInfoByCode.getProductStatus()) || !"Y".equalsIgnoreCase(plusModelSkuInfo.getSaleYn())){//商品非上架状态，或者sku不可售  都是失效商品
								faildGoodInfo.add(plusModelSkuInfo.getProductCode()+"-"+plusModelSkuInfo.getSkuCode());
								continue;
							}
							ShoppingCartGoodsInfoForAdd shopCartGoodInfo = new ShoppingCartGoodsInfoForAdd();
							shopCartGoodInfo.setChooseFlag("1");//选中
							shopCartGoodInfo.setProduct_code(product_code);//商品编号
							shopCartGoodInfo.setSku_code(skuCode);//sku编号
							shopCartGoodInfo.setSku_num(Integer.valueOf(String.valueOf(detail.getOrd_qty())));//商品数量
							
							shoppingcartgoodslist.add(shopCartGoodInfo);
						} else {
							faildGoodInfo.add(product_code+"-");
							continue;
						}
					}
					if(shoppingcartgoodslist.size() > 0) {
						//加购买的商品加入购物车
						ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
						if(getFlagLogin()){
							ShoppingCartCacheInfo info =  serviceForCache.queryShopCart(getOauthInfo().getUserCode(),new ArrayList<ShoppingCartGoodsInfoForAdd>());
							List<ShoppingCartCache> caches = info.getCaches();
							//将要加入购物车的商品和购物车里的商品进行合并
							for (int i = 0; i < caches.size(); i++) {
								ShoppingCartCache cache = caches.get(i);
								for (int j = 0; j < shoppingcartgoodslist.size(); j++) {
									ShoppingCartGoodsInfoForAdd good = shoppingcartgoodslist.get(j);
									if(good.getSku_code().equals(cache.getSku_code())){//购物车包含此商品
										//已存在，做加操作
										BigDecimal skuNum = BigDecimal.valueOf(good.getSku_num()).add(BigDecimal.valueOf(cache.getSku_num()));
										if(skuNum.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0 ) {
											good.setSku_num(Integer.MAX_VALUE);
										} else {
											good.setSku_num(skuNum.setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
										}
										good.setChooseFlag("1");//且是选中状态
										
									} 
								}
							}
							//保存购物车
							serviceForCache.saveShopCart(shoppingcartgoodslist, getOauthInfo().getUserCode());
						} else {
							result.setResultCode(0);
							result.setResultMessage("用户未登录，请登录！！");
						}
						
					} 
					
					if(shoppingcartgoodslist.size() == 0) {//没有要加入购物车的商品信息，则认为没有有效的商品
						result.setResult_status(2);
					} else if(faildGoodInfo.size() > 0) {//失败的数据
						result.setResult_status(1);
					}
					
					
				} else {
					result.setResultCode(0);
					result.setResultMessage("此订单["+order_code+"]不支持再次购买!");
				}
			} else {
				result.setResultCode(0);
				result.setResultMessage("订单不存在");
				return result;
			}
		}
		else {
			result.setResultCode(0);
			result.setResultMessage("此单["+inputParam.getOrder_code()+"]暂不支持再次购买!");
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
			case "06": return "4497153900010008";
			default : return "";
		}
	}
}
