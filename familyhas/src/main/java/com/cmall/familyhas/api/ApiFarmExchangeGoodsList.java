package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiFarmExchangeGoodsListInput;
import com.cmall.familyhas.api.model.FarmExchangeSku;
import com.cmall.familyhas.api.result.ApiFarmExchangeGoodsListResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 农场兑换商品列表接口<br/>
 * 规则：<br/>
 * 返回列表0号位 是果实(后台配置的<tt>第一个<tt>对应果实) 若后台没有配置 则添加一个空的商品 若不可售 用<tt>exchangeFlag</tt>标识<br/>
 * 返回列表非0号位 是商品(后台配置的所有商品 除了<tt>第一个<tt>对应果实)
 * @remark 
 * @author 任宏斌
 * @date 2020年2月10日
 */
public class ApiFarmExchangeGoodsList extends RootApiForToken<ApiFarmExchangeGoodsListResult, ApiFarmExchangeGoodsListInput>{

	LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
	LoadProductInfo loadProductInfo = new LoadProductInfo();
	PlusSupportStock plusSupportStock = new PlusSupportStock();
	
	@Override
	public ApiFarmExchangeGoodsListResult Process(ApiFarmExchangeGoodsListInput inputParam, MDataMap mRequestMap) {
		ApiFarmExchangeGoodsListResult result = new ApiFarmExchangeGoodsListResult();
		String eventCode = inputParam.getEventCode();
		String treeCode = inputParam.getTreeCode();
		
		//判断当前是否存在惠惠农场活动
		String nowTime = DateUtil.getSysDateTimeString();
		String sSql1 = "SELECT event_code FROM sc_hudong_event_info "
				+ "WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' "
				+ "AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time DESC LIMIT 1";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(null == eventInfoMap || !eventCode.equals(MapUtils.getString(eventInfoMap, "event_code"))) {
			result.setResultCode(91642600);
			result.setResultMessage(bInfo(91642600));
			return result;
		}
		
		MDataMap tree = DbUp.upTable("sc_huodong_farm_user_tree").one("tree_code", treeCode);
		String treeStage = tree.get("tree_stage");
		String changeFlag = tree.get("change_flag");
		
		if(!"449748450005".equals(treeStage)) {
			result.setResultCode(91642606);
			result.setResultMessage(bInfo(91642606));
			return result;
		}
		
		if("1".equals(changeFlag)) {
			result.setResultCode(91642607);
			result.setResultMessage(bInfo(91642607));
			return result;
		}
		
		String fruitCode = "";
		//String baseSql = "SELECT product_code,product_name,sku_code,product_type "
		//		+ " FROM systemcenter.sc_huodong_event_farm_product WHERE event_code=:event_code AND is_delete='0' ";
		// 排除活动售价大于商品售价的商品
		String baseSql = "SELECT p.product_code, p.product_name, p.sku_code, p.product_type  FROM systemcenter.sc_huodong_event_farm_product p " + 
				" LEFT JOIN productcenter.pc_skuinfo s ON p.sku_code = s.sku_code " + 
				" WHERE p.event_code=:event_code AND p.is_delete='0' AND p.activity_price < s.sell_price ";
		
		String fruitSql = baseSql + " AND p.product_type = :product_type ORDER BY p.create_time ASC limit 1";
		Map<String, Object> fruit = DbUp.upTable("sc_huodong_event_farm_product").dataSqlOne(fruitSql, new MDataMap("event_code", eventCode, "product_type", tree.get("tree_type")));
		if(null == fruit || !checkSale(MapUtils.getString(fruit, "product_code"), MapUtils.getString(fruit, "sku_code"))) {
			FarmExchangeSku farmExchangeSku = new FarmExchangeSku();
			farmExchangeSku.setTreeType(tree.get("tree_type"));
			result.getSkuList().add(farmExchangeSku);
		}else {
			result.getSkuList().add(dealSku(fruit));
			fruitCode = MapUtils.getString(fruit, "sku_code");
		}
		
		
		String productSql = baseSql;
		if(!"".equals(fruitCode)) {
			productSql += " AND p.sku_code != :sku_code ORDER BY p.create_time DESC";
		}
		List<Map<String, Object>> productList = DbUp.upTable("sc_huodong_event_farm_product").dataSqlList(productSql, new MDataMap("event_code", eventCode, "sku_code", fruitCode));
		if(null != productList && productList.size() > 0) {
			for (Map<String, Object> product : productList) {
				if(checkSale(MapUtils.getString(product, "product_code"), MapUtils.getString(product, "sku_code"))) {
					result.getSkuList().add(dealSku(product));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 封装商品实体
	 * @param product
	 * @return
	 */
	private FarmExchangeSku dealSku(Map<String, Object> product) {
		FarmExchangeSku farmExchangeSku = new FarmExchangeSku();
		
		PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
		plusModelSkuQuery.setCode(MapUtils.getString(product, "sku_code"));
		PlusModelSkuInfo skuInfo = loadSkuInfo.upInfoByCode(plusModelSkuQuery);
		
		farmExchangeSku.setProductCode(MapUtils.getString(product, "product_code"));
		farmExchangeSku.setSkuCode(MapUtils.getString(product, "sku_code"));
		farmExchangeSku.setSkuName(skuInfo.getSkuName());
		farmExchangeSku.setSkuKey(skuInfo.getSkuKey());
		farmExchangeSku.setSkuKeyvalue(skuInfo.getSkuKeyvalue());
		farmExchangeSku.setSkuImg(skuInfo.getSkuPicUrl());
		farmExchangeSku.setExchangeFlag("1");
		farmExchangeSku.setTreeType(MapUtils.getString(product, "product_type"));
		
		return farmExchangeSku;
	}
	
	/**
	 * 校验sku是否可售
	 * @param productCode
	 * @param skuCode
	 * @return
	 */
	private boolean checkSale(String productCode, String skuCode) {
		
		PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
		String productStatus = productInfo.getProductStatus();
		
		if(!"4497153900060002".equals(productStatus)) { return false;}
		if(plusSupportStock.upSalesStock(skuCode) <= 0) { return false;}
		
		return true;
	}
	
}
