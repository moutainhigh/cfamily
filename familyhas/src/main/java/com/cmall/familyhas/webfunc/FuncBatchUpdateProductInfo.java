package com.cmall.familyhas.webfunc;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.productcenter.common.DateUtil;
import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 批量更新待上架商品的基本信息
 */
public class FuncBatchUpdateProductInfo extends FuncAdd{
	
	JsonHelper<PcProductinfo> pHelper=new JsonHelper<PcProductinfo>();
	ProductService service = new ProductService();
	
	static final String KEY_PRODUCT_CODE = "商品编号";
	static final String KEY_CATEGORY = "商品分类";
	static final String KEY_PRODUCT_NAME = "商品名称";
	static final String KEY_MARKET_PRICE = "市场价格";
	static final String KEY_PROPERTY = "商品规格";
	static final String KEY_BRAND_NAME = "商品品牌";
	static final String KEY_PRODUCT_DESC = "商品描述";
	static final String KEY_LABELS = "关键字";
	static final String KEY_FICTITIOUS_SALES = "虚拟销售量基数";

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
		
		List<String> error = new ArrayList<String>();
		Set<String> productCodeSets = new HashSet<String>();
		Map<String,String> categoryCodeMap = getCategoryMap();
		
		MDataMap productInfo;
		String brandCode;
		String categoryCode;
		String productName;
		String marketPrice;
		String productDesc;
		String labels;
		String fictitiousSales;
		String productCode;
		for(MDataMap map : list) {
			productCode = StringUtils.trimToEmpty(map.get(KEY_PRODUCT_CODE));
			productInfo = DbUp.upTable("pc_productinfo").one("product_code",productCode);
			
			if(!productCodeSets.add(productCode)) {
				error.add("商品重复："+productCode);
				continue;
			}
			
			if(DbUp.upTable("pc_productinfo").count("product_code",productCode,"product_status","4497153900060001") == 0) {
				error.add("商品不是待上架状态："+productCode);
				continue;
			}
			
			categoryCode = StringUtils.trimToEmpty(getCategoryCode(map.get(KEY_CATEGORY), categoryCodeMap));
			if(StringUtils.isBlank(categoryCode)) {
				error.add("商品分类错误："+productCode);
			}
			
			productName = StringUtils.trimToEmpty(map.get(KEY_PRODUCT_NAME));
			if(StringUtils.isBlank(productName)) {
				error.add("商品名称不能为空："+productCode);
			}
			
			marketPrice = StringUtils.trimToEmpty(map.get(KEY_MARKET_PRICE));
			if(StringUtils.isBlank(marketPrice)) {
				error.add("商品市场价不能为空："+productCode);
			} else {
				if(!marketPrice.matches("\\d+(\\.\\d+)?")){
					error.add("商品市场价格式错误："+productCode);
					continue;
				} else if(new BigDecimal(marketPrice).compareTo(BigDecimal.ZERO) <= 0) {
					error.add("商品市场价必须大于0："+productCode);
					continue;
				}
			}
			
			brandCode = StringUtils.trimToEmpty(getBrandCode(map.get(KEY_BRAND_NAME)));
			if(StringUtils.isBlank(brandCode)) {
				error.add("商品品牌错误："+productCode);
			}
			
			fictitiousSales = StringUtils.trimToEmpty(map.get(KEY_FICTITIOUS_SALES));
			if(StringUtils.isNotBlank(fictitiousSales)) {
				if(NumberUtils.toInt(fictitiousSales,-1) < 0) {
					error.add("虚拟销售量基数不能小于0："+productCode);
				}
			}

		}
		
		if(!error.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入数据错误！<br>"+StringUtils.join(error,","));
			return mResult;
		}
		
		List<String> fieldList = new ArrayList<String>();
		for(MDataMap map : list) {
			productCode = StringUtils.trimToEmpty(map.get(KEY_PRODUCT_CODE));
			productInfo = DbUp.upTable("pc_productinfo").one("product_code",productCode);
			
			if(DbUp.upTable("pc_productinfo").count("product_code",productCode,"product_status","4497153900060001") == 0) {
				error.add("商品不是待上架状态："+productCode);
				continue;
			}
			
			categoryCode = StringUtils.trimToEmpty(getCategoryCode(map.get(KEY_CATEGORY), categoryCodeMap));
			if(StringUtils.isNotBlank(categoryCode)) {
				DbUp.upTable("uc_sellercategory_product_relation").delete("product_code", productCode);
				DbUp.upTable("uc_sellercategory_product_relation").dataInsert(new MDataMap(
						"product_code", productCode,
						"category_code", categoryCode,
						"seller_code", "SI2003"
						));
			}
			
			productName = StringUtils.trimToEmpty(map.get(KEY_PRODUCT_NAME));
			if(StringUtils.isNotBlank(productName) && !productName.equals(productInfo.get("product_name"))) {
				productInfo.put("product_name", productName);
				fieldList.add("product_name");
			}
			
			marketPrice = StringUtils.trimToEmpty(map.get(KEY_MARKET_PRICE));
			if(StringUtils.isNotBlank(marketPrice) && !marketPrice.equals(productInfo.get("market_price"))) {
				if(!marketPrice.matches("\\d+(\\.\\d+)?")){
					error.add("商品市场价格式错误："+productCode);
					continue;
				} else if(new BigDecimal(marketPrice).compareTo(BigDecimal.ZERO) <= 0) {
					error.add("商品市场价必须大于0："+productCode);
					continue;
				}
				productInfo.put("market_price", marketPrice);
				fieldList.add("market_price");
			}
			
			brandCode = StringUtils.trimToEmpty(getBrandCode(map.get(KEY_BRAND_NAME)));
			if(StringUtils.isNotBlank(brandCode) && !brandCode.equals(productInfo.get("brand_code"))) {
				productInfo.put("brand_code", brandCode);
				fieldList.add("brand_code");
			}
			
			productDesc = StringUtils.trimToEmpty(map.get(KEY_PRODUCT_DESC));
			if(StringUtils.isNotBlank(productDesc)) {
				DbUp.upTable("pc_productdescription").dataUpdate(new MDataMap("description_info",productDesc,"product_code", productCode), "description_info", "product_code");
			}
			
			labels = StringUtils.trimToEmpty(map.get(KEY_LABELS));
			if(StringUtils.isNotBlank(labels) && !labels.equals(productInfo.get("labels"))) {
				productInfo.put("labels", labels);
				fieldList.add("labels");
			}
			
			fictitiousSales = StringUtils.trimToEmpty(map.get(KEY_FICTITIOUS_SALES));
			if(StringUtils.isNotBlank(fictitiousSales)) {
				DbUp.upTable("pc_productinfo_ext").dataUpdate(new MDataMap("fictitious_sales",fictitiousSales,"product_code", productCode), "fictitious_sales", "product_code");
			}

			List<String[]> proList = getPropertyList(StringUtils.trimToEmpty(map.get(KEY_PROPERTY)));
			if(!proList.isEmpty()) {
				DbUp.upTable("pc_productproperty").delete("product_code", productCode,"property_type","449736200004");
				for(String[] vs : proList) {
					DbUp.upTable("pc_productproperty").dataInsert(new MDataMap(
							"product_code", productCode,
							"property_key", StringUtils.trimToEmpty(vs[0]),
							"property_value", StringUtils.trimToEmpty(vs[1]),
							"property_type", "449736200004"
							));
				}
			}
			
			if(!fieldList.isEmpty()) {
				DbUp.upTable("pc_productinfo").dataUpdate(productInfo, StringUtils.join(fieldList,","), "zid");
			}
			
			PcProductinfo pro = service.getProduct(productCode);
			
			//插入商品历史流水信息
			MDataMap productFlow = new MDataMap();
			productFlow.put("flow_code", WebHelper.upCode(ProductService.ProductFlowHead));
			productFlow.put("product_code", productCode);
			productFlow.put("product_json",pHelper.ObjToString(pro));
			productFlow.put("flow_status", SkuCommon.FlowStatusInit);
			productFlow.put("creator", "PcSkuPriceChangeTimeJob");
			productFlow.put("create_time",DateUtil.getSysDateTimeString());
			productFlow.put("updator", "PcSkuPriceChangeTimeJob");
			productFlow.put("update_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("pc_productflow").dataInsert(productFlow);
		}
		
		return mResult;
	}
	
	private String getBrandCode(String name) {
		if(StringUtils.isBlank(name)) return "";
		MDataMap map = DbUp.upTable("pc_brandinfo").oneWhere("brand_code", "", "", "brand_name", name, "flag_enable", "1");
		return map == null ? "" : map.get("brand_code");
	}
	
	private String getCategoryCode(String name, Map<String,String> categoryCodeMap) {
		if(StringUtils.isBlank(name)) return "";
		return categoryCodeMap.containsKey(name) ? categoryCodeMap.get(name) : "";
	}
	
	private List<String[]> getPropertyList(String text) {
		if(StringUtils.isBlank(text)) return new ArrayList<String[]>();
		// 全角空格转成半角空格然后根据换行拆分为每个属性
		String[] vs = text.replaceAll("　", " ").split("\n");
		List<String[]> list = new ArrayList<String[]>();
		for(String v : vs) {
			if(v.contains(" ") && v.length() > 1) {
				list.add(new String[]{v.substring(0, v.indexOf(" ")),v.substring(v.indexOf(" ")+1)});
			}
		}
		return list;
	}
	
	private Map<String,String> getCategoryMap() {
		Map<String,String> map = new HashMap<String, String>();
		String sql = "SELECT s3.category_code, CONCAT(s1.category_name,'->',s2.category_name,'->',s3.category_name) category_name FROM `uc_sellercategory` s3";
			sql += " LEFT JOIN uc_sellercategory s2 ON s3.parent_code = s2.category_code";
			sql += " LEFT JOIN uc_sellercategory s1 ON s2.parent_code = s1.category_code";
			sql += " WHERE s3.`level` = 4";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(sql, new MDataMap());
		for(Map<String, Object> m : list) {
			map.put(m.get("category_name")+"", m.get("category_code")+"");
		}
		return map;
	}
}
