package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiCollectionProductInput;
import com.cmall.familyhas.api.result.APiCollectionProductResult;
import com.cmall.productcenter.common.DateUtil;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 惠家有商品收藏以及取消收藏
 * 
 * @author ligj
 * 
 */
public class APiCollectionProduct extends
		RootApiForToken<APiCollectionProductResult, APiCollectionProductInput> {

	public APiCollectionProductResult Process(
			APiCollectionProductInput inputParam, MDataMap mRequestMap) {
		APiCollectionProductResult result = new APiCollectionProductResult();
		String operateType = inputParam.getOperateType();
		List<String> inputProductCodes = inputParam.getProductCode();
		if (null == inputProductCodes || inputProductCodes.size() < 1) {
			return result;
		}
		Map<String,Integer> productCodeMap = new HashMap<String, Integer>();		//商品去重复
		for (String productCode : inputProductCodes) {
			productCodeMap.put(productCode, 0);
		}
		List<String> productCodes = new ArrayList<String>();
		for (String productCode : productCodeMap.keySet()) {
			productCodes.add(productCode);
		}
		String collect = "";
		if ("1".equals(operateType)) {
			for (String productCode : productCodes) {
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("member_code", getUserCode());
				mDataMap.put("product_code", productCode);
				collect = (String) DbUp.upTable("fh_product_collection").dataGet("operate_type", null,mDataMap);
				if ("4497472000020001".equals(collect)) {
					result.setResultCode(916423301);
					result.setResultMessage(bInfo(916423301)); 
					return result;
				}
			}
		}
		if ("1".equals(operateType)) {
			//现有的收藏数量
			int count = DbUp.upTable("fh_product_collection").count("member_code",getUserCode(),"operate_type","4497472000020001");
			int maxCollectionNum = Integer.parseInt(bConfig("familyhas.maxCollectionNum"));		//最大收藏数
			if (maxCollectionNum < (count+productCodes.size())) {
				result.setResultCode(916401234);
				result.setResultMessage(bInfo(916401234));
				return result;
			}
		}
		List<MDataMap> mapList = DbUp.upTable("fh_product_collection").queryAll("uid,operate_type,product_code","",
				"product_code in ('"+StringUtils.join(productCodes,"','")+"') and member_code = '"+getUserCode()+"'",null);
		
		for (String productCode : productCodes) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("product_code", productCode);
			mDataMap.put("member_code", getUserCode());
			mDataMap.put("operate_time", DateUtil.getSysDateTimeString());
			
			MDataMap mapInfo = null;		//标志是否存在收藏信息
			for (MDataMap map : mapList) {
				if ("0".equals(operateType)&&productCode.equals(map.get("product_code"))) {
					mDataMap.put("operate_type", "4497472000020002");	//取消收藏
					mapInfo = map;
					//如果状态本来就没有收藏，则结束内层循环
					if ("4497472000020002".equals(map.get("operate_type"))) {
						break;
					}
					break;
				}else if ("1".equals(operateType) && productCode.equals(map.get("product_code"))) {
					mDataMap.put("operate_type", "4497472000020001");	//收藏
					mapInfo = map;
					//如果状态本来就为已收藏，则结束内层循环
					if ("4497472000020001".equals(map.get("operate_type"))) {
						break;
					}
					break;
				}
			}
			if ("0".equals(operateType)&&(mapInfo == null || mapInfo.isEmpty() || "4497472000020002".equals(mapInfo.get("operate_type")))) {
				continue;
			}else if ("1".equals(operateType) && mapInfo != null && !mapInfo.isEmpty() && "4497472000020001".equals(mapInfo.get("operate_type"))) {
				continue;
			}
			if (mapInfo != null && !mapInfo.isEmpty()) {
				//已经存在，更新
				mDataMap.put("uid", mapInfo.get("uid"));
				if("1".equals(operateType)) {
					//534新需求，存商品指定sku，价格
					String skuCode = inputParam.getSkuCode();
					String skuPrice = inputParam.getSkuPrice();
					if("".equals(skuCode)&&"".equals(skuPrice)) {
						//根据商品码，获取第一个sku码及价格
						if (!PlusHelperEvent.checkEventItem(productCode)) {
							PlusModelSkuQuery query = new PlusModelSkuQuery();
							query.setCode(new PlusSupportProduct().upProductSku(productCode));
							query.setIsPurchase(1);
							PlusModelSkuResult plusModelSkuResult = new PlusSupportProduct().upSkuInfo(query);
							PlusModelSkuInfo plusModelSkuInfo = plusModelSkuResult.getSkus().get(0);
							skuCode = plusModelSkuInfo.getSkuCode();
							skuPrice= plusModelSkuInfo.getSellPrice().toString();
						}
						
					}
					mDataMap.put("sku_code",skuCode );
					mDataMap.put("sku_price", skuPrice);
					DbUp.upTable("fh_product_collection").dataUpdate(mDataMap, "operate_time,operate_type,sku_code,sku_price", "uid");
					mDataMap.remove("sku_code");
					mDataMap.remove("sku_price");
				}else if("0".equals(operateType)){
					DbUp.upTable("fh_product_collection").dataUpdate(mDataMap, "operate_time,operate_type", "uid");
				}
				this.recordLog(mDataMap);		//记录操作日志
			}else{
				//添加一条记录
				//534新需求，存商品指定sku，价格
				String skuCode = inputParam.getSkuCode();
				String skuPrice = inputParam.getSkuPrice();
				if("".equals(skuCode)&&"".equals(skuPrice)) {
					//根据商品码，获取第一个sku码及价格
					if (!PlusHelperEvent.checkEventItem(productCode)) {
						PlusModelSkuQuery query = new PlusModelSkuQuery();
						query.setCode(new PlusSupportProduct().upProductSku(productCode));
						query.setIsPurchase(1);
						PlusModelSkuResult plusModelSkuResult = new PlusSupportProduct().upSkuInfo(query);
						PlusModelSkuInfo plusModelSkuInfo = plusModelSkuResult.getSkus().get(0);
						skuCode = plusModelSkuInfo.getSkuCode();
						skuPrice= plusModelSkuInfo.getSellPrice().toString();
					}
					
				}
				mDataMap.put("sku_code",skuCode );
				mDataMap.put("sku_price", skuPrice);
				mDataMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
				DbUp.upTable("fh_product_collection").dataInsert(mDataMap);
				mDataMap.remove("sku_code");
				mDataMap.remove("sku_price");
				this.recordLog(mDataMap);		//记录操作日志
			}
		}
		
		return result;
	}
	
	/**
	 * 记录操作日志
	 */
	public void recordLog(MDataMap insertMap){
		insertMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
		insertMap.put("create_time", DateUtil.getSysDateTimeString());
		DbUp.upTable("lc_product_collection").dataInsert(insertMap);
	}
}
