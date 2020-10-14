package com.cmall.familyhas.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiKJTOrderTraceInput;
import com.cmall.familyhas.api.model.KJTExpressDetail;
import com.cmall.familyhas.api.model.KJTOrderDetail;
import com.cmall.familyhas.api.model.KJTOrderTraceInfo;
import com.cmall.familyhas.api.result.ApiKJTOrderTraceResult;
import com.cmall.familyhas.util.OutBindFacade;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 跨境通订单轨迹相关信息
 * @author pangjh
 *
 */
public class KJTOrderTraceService extends BaseClass {
	
	/**
	 * 根据订单编号查询运单信息
	 * @param order_code
	 * 		订单编号
	 * @param order_code_seq
	 * 		拆单后的序列号
	 * @return
	 * 		运单信息
	 */
	public MDataMap getOrderTraceInfo(String order_code,String order_code_seq){
		
		return DbUp.upTable("oc_order_shipments").one("order_code" , order_code,"order_code_seq",order_code_seq);
		
	}
	
	/**
	 * 根据订单编号查询跨境通拆包后的订单信息集合
	 * @param order_code
	 * 		订单编号
	 * @return List<MDataMap>
	 */
	public List<MDataMap> getOrderKjtListData(String order_code){
		
		return DbUp.upTable("oc_order_kjt_list").queryAll("order_code,order_code_seq", "", "", new MDataMap("order_code",order_code));
		
	}
	
	/**
	 * 根据订单编号查询麦乐购拆包后的订单信息集合
	 * @param order_code
	 * 		订单编号
	 * @return List<MDataMap>
	 */
	public List<MDataMap> getOrderMlgListData(String order_code){
		
		return DbUp.upTable("oc_order_mlg_list").queryAll("order_code,order_code_seq", "", "", new MDataMap("order_code",order_code));
		
	}
	
	/**
	 * 处理接口相关业务
	 * @param input
	 * 		输入参数
	 * @return ApiKJTOrderTraceResult
	 * 		响应结果
	 */
	public ApiKJTOrderTraceResult doProcess(ApiKJTOrderTraceInput input){
		
		List<MDataMap> kjtOrderInfos = getOrderKjtListData(input.getOrder_code());
		
		List<MDataMap> mlgOrderInfos = getOrderMlgListData(input.getOrder_code());
		
		kjtOrderInfos.addAll(mlgOrderInfos);
		
		List<KJTOrderTraceInfo> orderTraceInfos = new ArrayList<KJTOrderTraceInfo>();
		
		ApiKJTOrderTraceResult apiKJTOrderTraceResult = new ApiKJTOrderTraceResult();
		
		boolean hasTraceInfo = false;
		try {
			
			KJTOrderTraceInfo info;
			for (MDataMap mDataMap : kjtOrderInfos) {
				
				MDataMap orderTraceInfo = getOrderTraceInfo(mDataMap.get("order_code"), mDataMap.get("order_code_seq"));
				
				if(orderTraceInfo != null){
					info = convertOrderTraceInfo(orderTraceInfo);
					orderTraceInfos.add(convertOrderTraceInfo(orderTraceInfo));
					
					hasTraceInfo = hasTraceInfo || info.getExpressList().isEmpty();
					
				}else{
					
					orderTraceInfos.add( new KJTOrderTraceInfo());
					
				}
				
			}
			
			apiKJTOrderTraceResult.setOrderTraceInfos(orderTraceInfos);
			
		} catch (Exception e) {
			bLogError(0, "跨境通订单轨迹："+e.getMessage());
			apiKJTOrderTraceResult.setResultCode(-1);
			apiKJTOrderTraceResult.setResultMessage(e.getMessage());
		}
		
		
		// 直邮商品的物流提示
		String orderStatus = (String)DbUp.upTable("oc_orderinfo").dataGet("order_status", "", new MDataMap("order_code",input.getOrder_code()));
		if(("4497153900010002".equals(orderStatus) || ("4497153900010003".equals(orderStatus) && !hasTraceInfo))){
			LoadSkuInfoSpread loadSkuInfoSpread = new LoadSkuInfoSpread();
			PlusModelSkuQuery sq = new PlusModelSkuQuery();
			
			// 理论上只查询一个商品就行，因为直邮的商品已经被拆为单独的订单了
			MDataMap m = DbUp.upTable("oc_orderdetail").oneWhere("product_code", "", "", "order_code",input.getOrder_code(),"gift_flag","1");
			if(m != null && StringUtils.isNotBlank(m.get("product_code"))){
				sq.setCode(m.get("product_code"));
				PlusModelSkuInfoSpread plusModelSkuInfoSpread = loadSkuInfoSpread.upInfoByCode(sq);
				if(plusModelSkuInfoSpread != null && "1".equals(plusModelSkuInfoSpread.getProductTradeType())){
					apiKJTOrderTraceResult.setLogisticsTips(bConfig("familyhas.product_tips_zhiyou"));
				}
			}
		}
		
		// 只在已发货的情况显示默认的提示
		if(StringUtils.isBlank(apiKJTOrderTraceResult.getLogisticsTips()) && !hasTraceInfo && "4497153900010003".equals(orderStatus)){
			apiKJTOrderTraceResult.setLogisticsTips("暂无物流信息!");
		}
		
		return apiKJTOrderTraceResult;
		
	}
	
	/**
	 * 组装跨境通运单实体信息
	 * @param mDataMap
	 * 		运单信息
	 * @return KJTOrderTraceInfo
	 * 		跨境通运单信息
	 * @throws Exception 
	 */
	public KJTOrderTraceInfo convertOrderTraceInfo(MDataMap mDataMap) throws Exception{
		
		KJTOrderTraceInfo kjtOrderTraceInfo = 
				(KJTOrderTraceInfo) OutBindFacade.getInstance().invoke(KJTOrderTraceInfo.class, mDataMap);
		
		kjtOrderTraceInfo.setExpressList(convertExpressInfo(mDataMap));
		
		kjtOrderTraceInfo.setProductList(convertOrderDetail(mDataMap));
		
		return kjtOrderTraceInfo;
		
	}
	
	/**
	 * 转转定案详情信息
	 * @param order_code
	 * 		订单编号
	 * @param order_code_seq
	 * 		订单序列号
	 * @return List<OrderDetail>
	 * 		订单详情集合
	 * @throws Exception
	 */
	public List<KJTOrderDetail> convertOrderDetail(MDataMap params) throws Exception{
		
		List<KJTOrderDetail> orderDetails = new ArrayList<KJTOrderDetail>();
		
		List<MDataMap> mdataMaps = getOrderDetailList(params.get("order_code"), params.get("order_code_seq"));
		
		for (MDataMap mDataMap : mdataMaps) {
			
			orderDetails.add((KJTOrderDetail) OutBindFacade.getInstance().invoke(KJTOrderDetail.class, mDataMap));
			
		}
		
		return orderDetails;
		
	}
	
	/**
	 * 获取订单详情信息
	 * @param order_code
	 * 		订单编号
	 * @param order_code_seq
	 * 		拆单后的订单信息序列号
	 * @return List<OrderDetail>
	 * 		订单详情集合
	 */
	public List<MDataMap> getOrderDetailList(String order_code ,String order_code_seq){
		
		return DbUp.upTable("oc_order_kjt_detail").queryByWhere("order_code",order_code,"order_code_seq",order_code_seq);
		
		
	}
	
	/**
	 * 获取快递信息
	 * @param order_code
	 * @param waybill
	 * @param logistic_code
	 * @return
	 */
	public List<MDataMap> getExpressDetailList(String order_code,String waybill,String logisticse_code){
		
		MDataMap conditions = new MDataMap();
		
		conditions.put("order_code", order_code);
		
		conditions.put("waybill", waybill);
		
		conditions.put("logisticse_code", logisticse_code);
		
		return DbUp.upTable("oc_express_detail").queryAll("", "time DESC", "", conditions);
		
	}
	
	/**
	 * 转换快递详情信息
	 * @param mdataMaps
	 * 		转换数据集合
	 * @return
	 * @throws Exception
	 */
	public List<KJTExpressDetail> convertExpressInfo(MDataMap params) throws Exception{
		
		List<KJTExpressDetail> expressList = new ArrayList<KJTExpressDetail>();
		
		List<MDataMap> mdataMaps = getExpressDetailList(params.get("order_code")
				, params.get("waybill"), params.get("logisticse_code"));
		
		for (MDataMap mDataMap : mdataMaps) {
			
			expressList.add((KJTExpressDetail) OutBindFacade.getInstance().invoke(KJTExpressDetail.class, mDataMap));
			
		}
		
		return expressList;
		
	}
	
	
	
	

}
