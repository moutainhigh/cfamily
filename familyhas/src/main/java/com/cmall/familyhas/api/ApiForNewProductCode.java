package com.cmall.familyhas.api;


import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForNewProductCodeInput;
import com.cmall.familyhas.api.result.ApiForNewProductCodeResult;
import com.cmall.groupcenter.homehas.RsyncGetNewGoodId;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取复制后的商品编号
 */
public class ApiForNewProductCode extends RootApi<ApiForNewProductCodeResult, ApiForNewProductCodeInput>{

	public ApiForNewProductCodeResult Process(ApiForNewProductCodeInput in, MDataMap mRequestMap) {
		ApiForNewProductCodeResult result = new ApiForNewProductCodeResult();
		result.setProductCode(in.getProductCode());
		
		// 新商品编号
		String productCodeCopy = "";
		// 原商品编号
		String productCode = in.getProductCode();
		if(productCode.contains("_")){ // 如果包含特殊前缀则需要处理取出真实商品编号
			productCode = productCode.substring(productCode.lastIndexOf("_") + 1);
		}
		
		// 查询对照表
		MDataMap map = DbUp.upTable("pc_product_code_copy").one("product_code_old",productCode);
		if(map != null){
			productCodeCopy = map.get("product_code_new");
		}
		
		// 对照表不存在时查询家有接口，限定只支持1开头的商品编号
		if(StringUtils.isBlank(productCodeCopy) && productCode.startsWith("1")){
			String smallSellerCode = (String)DbUp.upTable("pc_productinfo").dataGet("small_seller_code", "", new MDataMap("product_code",productCode));
			if("SI2003".equals(smallSellerCode) || "SI2009".equals(smallSellerCode)){
				// 查询家有接口
				RsyncGetNewGoodId rsync = new RsyncGetNewGoodId();
				rsync.upRsyncRequest().setGood_id(productCode);
				if(rsync.doRsync() && !productCode.equals(rsync.upProcessResult().getNew_good_id())){
					productCodeCopy = StringUtils.trimToEmpty(rsync.upProcessResult().getNew_good_id());
				}
			}
			
			// 保存到本地对照表
			if(StringUtils.isNotBlank(productCodeCopy)){
				String sql = "insert ignore into pc_product_code_copy(uid,product_code_old,product_code_new,small_seller_code,create_time)";
					sql += " values(:uid,:product_code_old,:product_code_new,:small_seller_code,now())";
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
				mDataMap.put("product_code_old", productCode);
				mDataMap.put("product_code_new", productCodeCopy);
				mDataMap.put("small_seller_code", smallSellerCode);
				DbUp.upTable("pc_product_code_copy").dataExec(sql, mDataMap);
			}
		}
		
		// 检查一下新的商品编号不存在
		if(StringUtils.isNotBlank(productCodeCopy)){
			// 新商品不存在时等同于没有调编的情况
			if(DbUp.upTable("pc_productinfo").count("product_code",productCodeCopy) == 0){
				productCodeCopy = "";
			}
		}
		
		// 设置支持扫码购活动的缓存标识
		if(StringUtils.isNotBlank(productCodeCopy) && !XmasKv.upFactory(EKvSchema.ScannerAllow).exists(productCodeCopy)){
			// 查询一下原商品是否在二维码里面，如果再的话则把新的商品编号也设置为支持扫码购
			// 原商品编号支持扫码价则新的商品也需要支持
			if(DbUp.upTable("sc_erwei_code").count("product_code",productCode) > 0
					|| XmasKv.upFactory(EKvSchema.ScannerAllow).exists(productCode)){
				// 24小时
				XmasKv.upFactory(EKvSchema.ScannerAllow).setex(productCodeCopy, 86400, productCodeCopy);
			}
		}
		
		// 新的商品编号替换原商品编号，保证IC_SMG的前缀不变
		if(StringUtils.isNotBlank(productCodeCopy)){
			productCodeCopy = in.getProductCode().replace(productCode, productCodeCopy);
		}
		
		result.setProductCodeCopy(productCodeCopy);
		return result;
	}
	
	
}










