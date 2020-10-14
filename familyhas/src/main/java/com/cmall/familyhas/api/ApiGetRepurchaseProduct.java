package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmall.familyhas.api.input.ApiGetRepurchaseProductInput;
import com.cmall.familyhas.api.input.ApiGetRepurchaseProductResult;
import com.cmall.familyhas.api.input.ApiGetRepurchaseProductResult.ActiceProduct;
import com.cmall.familyhas.service.ApiConvertTeslaService;
import com.cmall.productcenter.model.PcProductinfoForFamily;
import com.cmall.productcenter.model.ProductSkuInfoForFamily;
import com.cmall.productcenter.model.SolrData;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.xmasorder.model.ShoppingCartCache;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.service.ShopCartServiceForCache;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;


/**
 * @author zhangbo
 * 获取换购商品
 */
public class ApiGetRepurchaseProduct extends
		RootApiForVersion<ApiGetRepurchaseProductResult, ApiGetRepurchaseProductInput> {

	@Override
	public ApiGetRepurchaseProductResult Process(ApiGetRepurchaseProductInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiGetRepurchaseProductResult result = new ApiGetRepurchaseProductResult();
		String eventCode = inputParam.getEventCode();
		String selectedSkuCodes = inputParam.getSelectedSkuCodes();
		ShopCartServiceForCache serviceForCache = new ShopCartServiceForCache();
		String skuCodes = serviceForCache.queryJJGForShopCart(getOauthInfo().getUserCode(),eventCode);

		String[] split  = {};
		List<String> list = new ArrayList<>();

		if(StringUtils.isNotBlank(selectedSkuCodes)) {
			split = StringUtils.split(selectedSkuCodes, ",");
			 list = Arrays.asList(split);
			 list = new ArrayList<String>(list);
		}
		if(StringUtils.isNotBlank(skuCodes)) {
			split = StringUtils.split(skuCodes, ",");
			List<String> temList = new ArrayList<>();
			for (String string : split) {
				if(!list.contains(string)) {
					temList.add(string);
				}
			}
			
			list.addAll(temList);
		}
		
		/*int page = inputParam.getPage();
		int pageSize = 2000;
		int start = (page-1)*pageSize;*/
		
		List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
		int dataCount = DbUp.upTable("sc_event_item_product").dataCount("event_code='"+eventCode+"' and flag_enable=1", null);
		if(dataCount>0) {
			/*if(dataCount%pageSize==0) {
				result.setPageNum(dataCount/pageSize);
			}else {
				result.setPageNum((dataCount/pageSize)+1);
			}*/
			result.setPageNum(1);
			String sql = "select si.*,se.event_name,se.repurchase_num,se.channel_limit from sc_event_item_product si,sc_event_info se where si.flag_enable=1 and si.event_code=se.event_code and se.event_code=:event_code order by seat asc,zid desc ";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_event_item_product").dataSqlList(sql, new MDataMap("event_code",eventCode));
			BigDecimal bigDecimal = new BigDecimal(0);
			List< String> invalidProductList = new ArrayList<String>();
			LOOP:for (Map<String, Object> map : dataSqlList) {
				 ActiceProduct acticeProduct = new ActiceProduct();
				 ProductService pService = new ProductService();
				 PcProductinfoForFamily productInfoForPP = pService.getProductInfoForPP(map.get("product_code").toString(),getManageCode());
				 
                //MDataMap  productInfo=DbUp.upTable("pc_productinfo").one("product_code",map.get("product_code").toString());
				 int upEventItemSkuStock = 0;
				 int upAllStock = 0;
				 if(!"4497153900060002".equals(productInfoForPP.getProductStatus())){ 
					invalidProductList.add(map.get("product_code").toString());
            		continue;
    			}else {
    				//在架状态
    				//判断sku是否可售
    				List<ProductSkuInfoForFamily> productSkuInfoList = productInfoForPP.getProductSkuInfoList();
    				boolean flag = false;
    				for (ProductSkuInfoForFamily productSkuInfoForFamily : productSkuInfoList) {
						if(productSkuInfoForFamily.getSkuCode().equals(map.get("sku_code").toString())) {flag = true; break;}
					}
    				if(!flag) {
						continue LOOP;
					}
    				upEventItemSkuStock =(int) new PlusSupportEvent().upEventItemSkuStock(map.get("item_code").toString());
    			    upAllStock = new PlusSupportStock().upAllStock(map.get("sku_code").toString());
    				if(upEventItemSkuStock==0||upAllStock==0) {
    					//售罄
    					acticeProduct.setProductStatus("4497471600050002");
    				}else {
    					acticeProduct.setProductStatus("4497153900060002");
    					acticeProduct.setSales_num(Math.min(upEventItemSkuStock, upAllStock));
    				}
    			}
                acticeProduct.setProduct_name(productInfoForPP.getProductName());            
				acticeProduct.setJjg_price(new BigDecimal(map.get("favorable_price").toString()));
				acticeProduct.setProduct_code(map.get("product_code").toString());
				//获取商品当前售价
				{   MDataMap dataMap = new MDataMap();
				
					try {
						BigDecimal minSellPrice = productInfoForPP.getMinSellPrice();
						if(minSellPrice.compareTo(new BigDecimal(0))>0) {
							acticeProduct.setSell_price(minSellPrice);
						}else {
							acticeProduct.setSell_price(null);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						acticeProduct.setSell_price(null);
					}
				}
				acticeProduct.setSku_code(map.get("sku_code").toString());

                acticeProduct.setMainpic_url(map.get("mainpic_url").toString());
                //5.5.8增加所有sku实际库存 前端用于库存提示,这里是以sku展示的，所以不用获取全部的sku总库存了
				/*int allSkuRealStock = new PlusSupportStock().upAllStockForProduct(map.get("product_code").toString());
				acticeProduct.setAllSkuRealStoc(allSkuRealStock);*/
                if(!"颜色=共同&款式=共同".equals(map.get("standard").toString())) {
                	if(map.get("standard").toString().contains("颜色=共同")) {
                		String[] split2 = StringUtils.split(map.get("standard").toString(),"&");
                		String replace2 = StringUtils.replace(split2[1],"=",":");
                   	    acticeProduct.setStandard(replace2);
                	}else if(map.get("standard").toString().contains("款式=共同")) {
                		String[] split2 = StringUtils.split(map.get("standard").toString(),"&");
                		String replace2 = StringUtils.replace(split2[0],"=",":");
                   	    acticeProduct.setStandard(replace2);
                	}else {
                		String replace = StringUtils.replace(map.get("standard").toString(), "&", " ");
                   	    String replace2 = StringUtils.replace(replace,"=",":");
                   	    acticeProduct.setStandard(replace2);
                	} 
                }
                acticeProduct.setSku_name(map.get("sku_name").toString());
                acticeProduct.setProClassifyTag(getProClassifyTay(map.get("product_code").toString(),productInfoForPP.getSmallSellerCode()));
                if(list.contains(map.get("sku_code").toString())&&upEventItemSkuStock!=0&&upAllStock!=0) {
                	acticeProduct.setSelectedFlag("1");	
                }else if(list.contains(map.get("sku_code").toString())&&(upEventItemSkuStock==0||upAllStock==0)){
                	list.remove(map.get("sku_code").toString());
                }
               // acticeProduct.setGoods_link(FormatHelper.formatString(bConfig("ordercenter.product_detail"),map.get("product_code").toString(),map.get("product_code").toString()));
                productList.add(acticeProduct);
			}
			if(list.size()>0) {
				String join = StringUtils.join(list, "','");
				join = "('"+join+"')";
				String sql2="select si.*,se.event_name,se.repurchase_num,se.channel_limit from sc_event_item_product si,sc_event_info se where si.sku_code in "+join+" and si.flag_enable=1 and si.event_code=se.event_code and se.event_code=:event_code";
				List<Map<String, Object>> list2 = DbUp.upTable("sc_event_item_product").dataSqlList(sql2, new MDataMap("event_code",eventCode));
				int count = 0;
				for (Map<String, Object> map : list2) {
					if(invalidProductList.contains(map.get("product_code"))) {continue;}
					bigDecimal = bigDecimal.add(BigDecimal.valueOf(Double.parseDouble(map.get("favorable_price").toString())));
					count++;
				}
				result.setSelectCount(count);
			}
			bigDecimal=MoneyHelper.roundHalfUp(bigDecimal);
			result.setSumRepurchaseMoney(bigDecimal.toString());
		}
		result.setProductList(productList);

		//再次校验购物车是否满足条件
		Double shopCarAllMoney= getShopCarAllMoney(serviceForCache.queryShopCart(getOauthInfo()==null?null:getOauthInfo().getUserCode(), new ArrayList<ShoppingCartGoodsInfoForAdd>()));
		MDataMap mapResult = DbUp.upTable("sc_event_info").one("event_code",eventCode);
		RepurchaseEvent repurchaseEvent = new  RepurchaseEvent();
		repurchaseEvent.setEvent_code(mapResult.get("event_code"));
		repurchaseEvent.setEvent_name(mapResult.get("event_name"));
		repurchaseEvent.setRepurchase_num(Integer.parseInt(mapResult.get("repurchase_num")));
		repurchaseEvent.setLimit_money(new BigDecimal(mapResult.get("repurchase_condition")));
		if(shopCarAllMoney>=Double.valueOf(mapResult.get("repurchase_condition"))) {
			repurchaseEvent.setFlag("1");
		}else {
			BigDecimal subtract = repurchaseEvent.getLimit_money().subtract(BigDecimal.valueOf(shopCarAllMoney));
			subtract=MoneyHelper.roundHalfUp(subtract);
			//subtract.setScale(2,BigDecimal.ROUND_CEILING);
			repurchaseEvent.setValue(subtract.doubleValue());
		}
		result.setRepurchaseEvent(repurchaseEvent);
		return result;
	}

	private String getProClassifyTay(String proCode, String smallerCode) {
		// TODO Auto-generated method stub
		String st="";
		if("SI2003".equals(smallerCode)) {
			st="4497478100050000";
		}
		else {
			st = WebHelper.getSellerType(smallerCode);
		}
		//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
		Map productTypeMap = WebHelper.getAttributeProductType(st);
		return productTypeMap.get("proTypeListPic").toString()==null?"":productTypeMap.get("proTypeListPic").toString();
	}

	private Double getShopCarAllMoney(ShoppingCartCacheInfo shoppingCartCacheInfo) {
		// TODO Auto-generated method stub
		TeslaXOrder teslaXOrder = new TeslaXOrder();
		teslaXOrder.getStatus().setExecStep(ETeslaExec.shopCart);
		teslaXOrder.setIsPurchase(1);
		teslaXOrder.setIsMemberCode(getOauthInfo().getUserCode());
		for (ShoppingCartCache good : shoppingCartCacheInfo.getCaches()) {
			TeslaModelOrderDetail orderDetail = new TeslaModelOrderDetail();
			orderDetail.setProductCode(good.getProduct_code());
			orderDetail.setSkuCode(good.getSku_code());
			orderDetail.setSkuNum(Long.valueOf(good.getSku_num()).intValue());
			orderDetail.setChoose_flag(good.getChoose_flag());
			orderDetail.setIsSkuPriceToBuy(good.getIsSkuPriceToBuy());
			// 购物车忽略IC开头的编号
			if(!good.getSku_code().startsWith("IC")){
				teslaXOrder.getOrderDetails().add(orderDetail);
			}
			
		}
		teslaXOrder.getUorderInfo().setBuyerCode(getOauthInfo()==null?"":getOauthInfo().getUserCode());
		teslaXOrder.getUorderInfo().setSellerCode(getManageCode());
		new ApiConvertTeslaService().ConvertOrder(teslaXOrder);
		return teslaXOrder.getCartShow().getAllPayMoney();
	}

	
	public static void main(String []args) {
		 MDataMap dataMap = new MDataMap();
		dataMap.put("keyWord", "8016827349");
		dataMap.put("sortType",0+"");
		dataMap.put("sortFlag",1+"");
		dataMap.put("pageNo", 0+"");
		dataMap.put("pageSize", 1+"");
		dataMap.put("sellercode","SI2003");
		dataMap.put("key","key");
		try {
			String pro = WebClientSupport.upPost(TopUp.upConfig("productcenter.webclienturlselect"), dataMap);
			System.out.println(pro);
			System.out.println("============");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
