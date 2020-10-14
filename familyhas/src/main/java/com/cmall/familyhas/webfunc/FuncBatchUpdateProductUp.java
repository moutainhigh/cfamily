package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

public class FuncBatchUpdateProductUp extends RootFunc {
	
	class ImportEntry {
		
		String productCode;
		String skuCode;
		String sellPrice;
		String costPrice;
		
		public ImportEntry(String productCode, String skuCode, String sellPrice, String costPrice) {
			super();
			this.productCode = productCode;
			this.skuCode = skuCode;
			this.sellPrice = sellPrice;
			this.costPrice = costPrice;
		}
		
	}

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		List<MDataMap> list = null;
		try {
			list = new ExcelSupport().upExcelFromUrl(uploadFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件内容失败："+e);
			return mResult;
		}
		
		if(list == null || list.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入文件无内容");
			return mResult;
		}
		
		// 检查是否有错误数据
		List<String> errList = new ArrayList<String>();
		String code;
		Set<String> codes = new HashSet<String>();
		List<ImportEntry> entryList = new ArrayList<FuncBatchUpdateProductUp.ImportEntry>();
		// 排重并忽略空值
		int lineNum = 1;
		for(MDataMap map : list) {
			lineNum++;
			// 忽略空行
			if(StringUtils.isBlank(map.get("商品编号"))
					&& StringUtils.isBlank(map.get("SKU编号"))) {
				continue;
			}
			
			if(StringUtils.isBlank(map.get("商品编号"))
					|| StringUtils.isBlank(map.get("SKU编号"))) {
				String msg = "[行"+lineNum+"商品编号或SKU编号不能为空]";
				errList.add(msg);
				continue;
			}
			
			code = StringUtils.trimToEmpty(map.get("SKU编号"));
			
			if(StringUtils.isBlank(map.get("销售价"))){
				errList.add("销售价不能为空："+code);
			}
			
			if(StringUtils.isBlank(map.get("成本价"))){
				errList.add("成本价不能为空："+code);
			}
			
			if(!codes.add(code)) {
				errList.add("SKU编号重复："+code);
			}
			
			entryList.add(new ImportEntry(StringUtils.trimToEmpty(map.get("商品编号")), code, StringUtils.trimToEmpty(map.get("销售价")), StringUtils.trimToEmpty(map.get("成本价"))));
		}
		
		if(!errList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入数据错误！<br>"+StringUtils.join(errList,","));
			return mResult;
		}
		
		// 检查是否有错误数据
		MDataMap skuData,mData;
		String productCode;
		Map<String,String> temMap = new HashMap<String, String>();
		for(ImportEntry entry : entryList) {
			skuData = DbUp.upTable("pc_skuinfo").oneWhere("product_code,sell_price,cost_price", "", "", "sku_code", entry.skuCode);
			if(skuData == null) {
				errList.add(entry.skuCode+"[不存在]");
				continue;
			}
			
			productCode = skuData.get("product_code");
			mData = DbUp.upTable("pc_productinfo").oneWhere("product_code,product_status", "", "", "product_code", productCode);
			
			// 只支持已下架状态的商品进行批量操作
			if(!"4497153900060003".equals(mData.get("product_status"))) {
				if(!temMap.containsKey(productCode)) {
					errList.add(productCode+"[商品状态不符]");
					temMap.put(productCode, "");
				}
				continue;
			}
			
			if(DbUp.upTable("pc_skuinfo").count("sale_yn","Y","product_code", productCode) == 0) {
				if(!temMap.containsKey(productCode)) {
					errList.add(productCode+"[无可售SKU]");
					temMap.put(productCode, "");
				}
				continue;
			}
			
			if(new BigDecimal(skuData.get("cost_price")).compareTo(new BigDecimal(entry.costPrice).setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				if(!temMap.containsKey(productCode)) {
					errList.add(entry.skuCode+"[成本价不一致]");
					temMap.put(productCode, "");
				}
				continue;
			}
			
			if(StringUtils.isBlank(entry.sellPrice) || new BigDecimal(entry.sellPrice).compareTo(BigDecimal.ZERO) < 0) {
				if(!temMap.containsKey(productCode)) {
					errList.add(entry.skuCode+"[销售价异常]");
					temMap.put(productCode, "");
				}
				continue;
			}
		}
		
		if(!errList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入数据错误！<br>"+StringUtils.join(errList,","));
			return mResult;
		}
		
		String userCode = UserFactory.INSTANCE.create().getUserCode();
		
		codes = new HashSet<String>();
		for(ImportEntry entry : entryList) {
			skuData = DbUp.upTable("pc_skuinfo").oneWhere("zid,sku_code,cost_price,sell_price,product_code,sell_price,cost_price", "", "", "sku_code", entry.skuCode);
			mData = DbUp.upTable("pc_productinfo").oneWhere("zid,uid,product_code,product_status", "", "", "product_code", skuData.get("product_code"));
			
			if(StringUtils.isNotBlank(entry.sellPrice) && new BigDecimal(entry.sellPrice).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(skuData.get("sell_price"))) != 0) {
				MDataMap lc_skuprice_change_flow = new MDataMap();
				lc_skuprice_change_flow.put("product_code", skuData.get("product_code").toString());
				lc_skuprice_change_flow.put("sku_code", skuData.get("sku_code").toString());
				lc_skuprice_change_flow.put("cur_cost_price", skuData.get("cost_price").toString());
				lc_skuprice_change_flow.put("cur_sell_price",skuData.get("sell_price").toString());
				lc_skuprice_change_flow.put("change_cost_price", skuData.get("cost_price"));
				lc_skuprice_change_flow.put("change_sell_price", skuData.get("sell_price"));
				lc_skuprice_change_flow.put("create_time", DateUtil.getSysDateTimeString());
				
				MDataMap skuprice_change_flow = new MDataMap();
				skuprice_change_flow.put("flow_code", WebHelper.upCode("SF"));
				skuprice_change_flow.put("product_code", skuData.get("product_code").toString());
				skuprice_change_flow.put("sku_code", skuData.get("sku_code").toString());
				skuprice_change_flow.put("cost_price_old", skuData.get("cost_price").toString());
				skuprice_change_flow.put("sell_price_old",skuData.get("sell_price").toString());
				skuprice_change_flow.put("cost_price", skuData.get("cost_price"));
				skuprice_change_flow.put("sell_price", skuData.get("sell_price"));
				skuprice_change_flow.put("start_time", FormatHelper.upDateTime("yyyy-MM-dd"));
				skuprice_change_flow.put("end_time", "2099-12-31");
				skuprice_change_flow.put("is_delete", "2");
				skuprice_change_flow.put("status", "4497172300130002");
				skuprice_change_flow.put("auto_flag", "1");
				skuprice_change_flow.put("do_type", "1");
				
				// 更新售价
				skuData.put("sell_price", entry.sellPrice);
				DbUp.upTable("pc_skuinfo").dataUpdate(skuData,"sell_price","zid");
				
				// 记录价格变更记录
				lc_skuprice_change_flow.put("change_sell_price", skuData.get("sell_price"));
				skuprice_change_flow.put("sell_price", skuData.get("sell_price"));
				DbUp.upTable("lc_skuprice_change_flow").dataInsert(lc_skuprice_change_flow);
				DbUp.upTable("pc_skuprice_change_flow").dataInsert(skuprice_change_flow);
				
				// 更新商品的最小和最大销售价
				MDataMap map = DbUp.upTable("pc_skuinfo").oneWhere("min(sell_price) min_sell_price,max(sell_price) max_sell_price", "", "", "product_code", skuData.get("product_code"));
				mData.put("min_sell_price", map.get("min_sell_price"));
				mData.put("max_sell_price", map.get("max_sell_price"));
				DbUp.upTable("pc_productinfo").dataUpdate(mData, "min_sell_price,max_sell_price", "zid");
				
				codes.add(skuData.get("product_code"));
			}
			
			if(!"4497153900060002".equals(mData.get("product_status"))) {
				mData.put("product_status", "4497153900060002");
				mData.put("update_time", FormatHelper.upDateTime());
				DbUp.upTable("pc_productinfo").dataUpdate(mData, "product_status,update_time", "zid");
				
				MDataMap insertDatamap = new MDataMap();
				insertDatamap.put("uid", UUID.randomUUID().toString().replace("-", ""));
				insertDatamap.put("flow_code", mData.get("uid"));
				insertDatamap.put("flow_type", "449717230011");
				insertDatamap.put("creator", userCode);
				insertDatamap.put("create_time", DateUtil.getSysDateTimeString());
				insertDatamap.put("flow_remark", "批量导入商品上架");
				insertDatamap.put("current_status", "4497153900060002");
				DbUp.upTable("sc_flow_bussiness_history").dataInsert(insertDatamap);
			}
			
			PlusHelperNotice.onChangeProductInfo(skuData.get("product_code"));
			ProductJmsSupport productJmsSupport = new  ProductJmsSupport();
			productJmsSupport.updateSolrData(skuData.get("product_code"));
		}
		
		// 如果有商品价格变更则记录变更日志
		JsonHelper<PcProductinfo> pHelper=new JsonHelper<PcProductinfo>();
		ProductService service = new ProductService();
		for(String pCode : codes) {
			PcProductinfo pro = service.getProduct(pCode);
			
			//插入商品历史流水信息
			MDataMap productFlow = new MDataMap();
			productFlow.put("flow_code", WebHelper.upCode(ProductService.ProductFlowHead));
			productFlow.put("product_code", pCode);
			productFlow.put("product_json",pHelper.ObjToString(pro));
			productFlow.put("flow_status", SkuCommon.FlowStatusInit);
			productFlow.put("creator", userCode);
			productFlow.put("create_time",DateUtil.getSysDateTimeString());
			productFlow.put("updator", userCode);
			productFlow.put("update_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("pc_productflow").dataInsert(productFlow);
		}
		
		//更新分类商品数量表
		XmasKv.upFactory(EKvSchema.IsUpdateCategoryProductCount).set("isUpdateCateProd","update");
		//更新品牌商品数量表
		XmasKv.upFactory(EKvSchema.IsUpdateBrandProductCount).set("isUpdateBrandProductCount","update");
		
		return mResult;
	}
	
}
