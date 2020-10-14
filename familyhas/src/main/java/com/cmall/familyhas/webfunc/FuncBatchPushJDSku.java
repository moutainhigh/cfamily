package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 导表推送京东商品到白名单
 */
public class FuncBatchPushJDSku extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		List<MDataMap> mapList = new ArrayList<MDataMap>();
		try {
			mapList = new ExcelSupport().upExcelFromUrl(uploadFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("解析上传的文件失败:"+e);
			return mResult;
		}
		
		Set<String> skuSet = new HashSet<String>();
		List<String> errorList = new ArrayList<String>();
		String skuCode;
		
		// 检查SKU编号是否合法
		for(MDataMap map : mapList) {
			skuCode = StringUtils.trimToEmpty(map.get("SKU编号"));
			if(StringUtils.isBlank(skuCode)) continue;
			
			if(DbUp.upTable("pc_jingdong_product").count("sku_code", skuCode) == 0) {
				errorList.add(skuCode);
				continue;
			} 
			
			skuSet.add(skuCode);
		}
		
		if(!errorList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("以下SKU编号不在京东商品列表中:"+StringUtils.join(errorList,","));
			return mResult;
		}
		
		// 循环单个添加到白名单
		for(String v : skuSet) {
			MDataMap product = DbUp.upTable("pc_jingdong_product").one("sku_code", v);
			
			// 不存在的情况下再添加
			if(DbUp.upTable("pc_jingdong_choosed_products").count("jd_sku_id",product.get("sku_code")) == 0) {
				DbUp.upTable("pc_jingdong_choosed_products").dataInsert(new MDataMap(
						"jd_sku_id", product.get("sku_code"),
						"jd_erppid", product.get("spu_code"),
						"is_enabled", "1"
						));
			}
			
			product.put("push_state", "449746250001");
			DbUp.upTable("pc_jingdong_product").dataUpdate(product, "push_state", "zid");
		}
		
		return mResult;
	}
}
