package com.cmall.familyhas.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.result.ApiOrderDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderKjtDetailsResult;
import com.cmall.familyhas.api.result.ApiOrderKjtParcelResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 订单详情业务实现
 * @author pang_jhui
 *
 */
public class OrderDetailService extends BaseClass {
	

	
	/**
	 * 初始化订单列表通关状态
	 * @param order_code
	 * 		订单编号
	 * @param result
	 */
	public void initCustomsStatus(String order_code,String orderStatus,ApiSellerListResult result){
		
		if(!StringUtils.equals(orderStatus, FamilyConfig.ORDER_STATUS_UNPAY)){
			
			if(StringUtils.isNotBlank(orderStatus)){
				
				int count = DbUp.upTable("oc_order_kjt_list").count("order_code",order_code,"sostatus",FamilyConfig.KJT_CUSTOMS_STATUS_NO);
				
				if(count > 0){	
					
					result.setNoPassCustom(FamilyConfig.CUSTOMS_STATUS_FAILURE);				
					
					
				}
				
			}
		
		}
		
	}
	
	/**
	 * 初始化麦乐购商品信息
	 * @param apiOrderDetailsResult
	 * 		订单详情信息
	 * @param productCodeList
	 * 		商品列表（计算返利）
	 * @param orderStatus 订单状态
	 */
	public void initMlgProductList(ApiOrderDetailsResult apiOrderDetailsResult,List<String> productCodeList,String orderStatus){
		
		String sostatus = "";
		
		OrderService orderService = new OrderService();
		
		/*设置关税*/
		apiOrderDetailsResult.setTariffMoney("0.00"); 
		
		/*查询麦乐购商品信息(总共有几个包裹)*/
		List<MDataMap> orderMlgList = getMlgOrderList(apiOrderDetailsResult.getOrder_code());
		
		List<ApiOrderKjtParcelResult> apiOrderKjtParcelList = new ArrayList<ApiOrderKjtParcelResult>();
		
		for(MDataMap mDataMap : orderMlgList){
			
			ApiOrderKjtParcelResult apiOrderKjtParcelResult = new ApiOrderKjtParcelResult();
			
			String order_code_seq = mDataMap.get("order_code_seq");
			
			apiOrderKjtParcelResult.setLocalStatus(mDataMap.get("local_status"));
			
			sostatus = mDataMap.get("sostatus")==null?"":mDataMap.get("sostatus");
			
			/*海外购商品详情信息*/
			ApiOrderKjtDetailsResult apiOrderKjtDetailsResult = null;
			
			List<ApiOrderKjtDetailsResult> apiOrderKjtDetailsList = new ArrayList<ApiOrderKjtDetailsResult>();
			
			/*查询每一个包裹内的具体信息*/
			List<MDataMap> orderKjtDetailList = getMlgOrderDetailList(order_code_seq);
			
			for(MDataMap orderKjtDetail :  orderKjtDetailList){
					
					List<Map<String, Object>> sellerList = orderService.sellerInformation(orderKjtDetail.get("sku_code")); // 查询商品信息
					
					if (sellerList != null && !sellerList.isEmpty()) {

						for (int i = 0; i < sellerList.size(); i++) {
							
							apiOrderKjtDetailsResult = new ApiOrderKjtDetailsResult();
							
							List<ApiSellerStandardAndStyleResult> apiSellerStandardAndStyleResultList = new ArrayList<ApiSellerStandardAndStyleResult>();

							Map<String,Object>mapSeller = sellerList.get(i);
							
							if (mapSeller.get("sku_picurl") != null) {
								
								apiOrderKjtDetailsResult.setMainpicUrlKJT(mapSeller.get("sku_picurl").toString());
								
							}
							
							apiOrderKjtDetailsResult.setNumberKJT(String.valueOf(orderKjtDetail.get("sku_num"))); 							
						
							MDataMap orderDetailMap = getOrderDetailInfo(orderKjtDetail.get("order_code"), orderKjtDetail.get("sku_code"));
							
							if(orderDetailMap!=null){
								apiOrderKjtDetailsResult.setPriceKJT(orderDetailMap.get("show_price").toString());
							}
							
							apiOrderKjtDetailsResult.setProductCodeKJT(String.valueOf(mapSeller.get("product_code")));
							//把商品编号单独存入一个list中  用于获取预计返利金额
							productCodeList.add(String.valueOf(mapSeller.get("product_code")));
							
							apiOrderKjtDetailsResult.setProductNameKJT(String.valueOf(mapSeller.get("sku_name")));
							
							/*是否生鲜*/
							PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(apiOrderKjtDetailsResult.getProductCodeKJT());
							
							List<String> labelsList = new LoadProductInfo().upInfoByCode(plusModelProductQuery).getLabelsList();
							
							apiOrderKjtDetailsResult.setLabelsList(labelsList);

							List<PcPropertyinfoForFamily> standardAndStyleList = orderService.sellerStandardAndStyle(mapSeller.get("sku_keyvalue").toString()); // 截取 尺码和款型
							
							if (standardAndStyleList != null) {
								for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
									ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
									apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
									apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
									apiSellerStandardAndStyleResultList.add(apiSellerStandardAndStyleResult);
								}
								apiOrderKjtDetailsResult.setStandardAndStyleList(apiSellerStandardAndStyleResultList);
							}


						}
						
						
							
							if(!StringUtils.equals(orderStatus, FamilyConfig.ORDER_STATUS_UNPAY)){
								
								if(StringUtils.equals(sostatus, FamilyConfig.KJT_CUSTOMS_STATUS_NO)){
									
									apiOrderDetailsResult.setFailureTimeReminder(bInfo(916421188));
									
									apiOrderKjtDetailsResult.setNoPassCustom(FamilyConfig.CUSTOMS_STATUS_FAILURE);
									
								}
							}
						
						apiOrderKjtDetailsList.add(apiOrderKjtDetailsResult);
					}
					
			}
			
			apiOrderKjtParcelResult.setApiOrderKjtDetailsList(apiOrderKjtDetailsList);
			
			apiOrderKjtParcelList.add(apiOrderKjtParcelResult);
			
		}

		apiOrderDetailsResult.setApiOrderKjtParcelResult(apiOrderKjtParcelList);
	
		
		
	}
	
	/**
	 * 获取麦乐订单拆单列表
	 * @param orderCode
	 * 		订单编号
	 * @return 订单信息集合
	 */
	public List<MDataMap> getMlgOrderList(String orderCode){
		
		return DbUp.upTable("oc_order_mlg_list").queryByWhere("order_code",orderCode);
		
	}
	
	/**
	 * 查询麦乐购商品信息
	 * @param order_code_seq
	 * 		订单序列号
	 * @return 商品信息集合 
	 */
	public List<MDataMap> getMlgOrderDetailList(String order_code_seq){
		
		return DbUp.upTable("oc_order_mlg_detail").queryByWhere("order_code_seq",order_code_seq);
		
	}
	
	/**
	 * 获取订单详情信息
	 * @param orderCode
	 * 		订单编号
	 * @param skuCode
	 * 		sku编码
	 * @return 订单详情信息
	 */
	public MDataMap getOrderDetailInfo(String orderCode,String skuCode){
		
		return  DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code",skuCode);
		
	}
	

}
