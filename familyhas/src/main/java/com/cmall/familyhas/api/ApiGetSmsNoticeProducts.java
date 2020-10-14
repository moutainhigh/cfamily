package com.cmall.familyhas.api;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiGetSmsNoticeProductsInput;
import com.cmall.familyhas.api.model.ProductItem;
import com.cmall.familyhas.api.model.ProductSkuInfoForApiNew;
import com.cmall.familyhas.api.result.ApiGetSmsNoticeProductsResult;
import com.cmall.productcenter.service.ProductLogoService;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasproduct.model.ProductActivity;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
/**
 * 
 *<p>Description:短信召回（扫码购/进线损耗）的商品列表接口 <／p> 
 * @author zb
 * @date 2020年9月16日
 *
 */

public class ApiGetSmsNoticeProducts extends RootApiForToken <ApiGetSmsNoticeProductsResult, ApiGetSmsNoticeProductsInput>{
	ProductLogoService productLogoService = new ProductLogoService();
	@Override
	public ApiGetSmsNoticeProductsResult Process(ApiGetSmsNoticeProductsInput inputParam, MDataMap mRequestMap) {
		ApiGetSmsNoticeProductsResult result = new ApiGetSmsNoticeProductsResult();
		String[] couponCodes = inputParam.getCouponCodes().split(",");
		String memberCode = getUserCode();
		for (String cCode : couponCodes) {
			Map<String, Object> dataSqlOne = DbUp.upTable("oc_coupon_info").dataSqlOne("select * from oc_coupon_info where coupon_code=:coupon_code and member_code=:member_code and status=0 and start_time<now() and end_time>now() ",new MDataMap("coupon_code",cCode,"member_code",memberCode));
			if(dataSqlOne!=null) {
				String couponType = dataSqlOne.get("coupon_type_code").toString();
				String activityCode = dataSqlOne.get("activity_code").toString();
				String couponValue = dataSqlOne.get("initial_money").toString();
				int dataCount = DbUp.upTable("oc_activity").dataCount(" activity_code=:activity_code and flag=1 and begin_time<=now() and end_time>now() ", new MDataMap("activity_code",activityCode));
				MDataMap one = DbUp.upTable("oc_coupon_type_limit").one("coupon_type_code",couponType);
				if(dataCount>0) {
					//活动有效
					if(one!=null) {
						String pCode = one.get("product_codes");
						ProductItem handlerGood = handlerGoods(pCode,cCode,couponValue);
						result.getProductList().add(handlerGood);
					}
				}else {
					//活动失效
					if(one!=null) {
						String pCode = one.get("product_codes");
						ProductItem handlerGood = handlerGoods(pCode,null,null);
						result.getProductList().add(handlerGood);
					}
				}
			}
		}	
		return result;
	}

	/**
	 * 根据商品编号从缓存中查询商品信息
	 * @param productCode
	 * @return ProductItem
	 */
	private ProductItem handlerGoods(String productCode,String couponCode,String couponValue){
		// 商品下架或商品不存在的时忽略此商品
		if(DbUp.upTable("pc_productinfo").count("product_code",productCode,"product_status","4497153900060002") == 0){
			return null;
		}
		ProductItem productItem = new ProductItem();
		if(couponCode!=null) {
			productItem.setCouponCode(couponCode);
			productItem.setCouponValue(couponValue);	
		}
		// 获取商品信息
		PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(productCode);
		PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
		// 缓存获取商品信息
		plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
		if (null != plusModelProductinfo && null != plusModelProductinfo.getProductCode()
				&& !"".equals(plusModelProductinfo.getProductCode()) 
				&& "4497153900060002".equals(plusModelProductinfo.getProductStatus())) {//过滤掉未上架商品
			String tvTips = (String)DbUp.upTable("pc_productinfo").dataGet("tv_tips", "", new MDataMap("product_code",productCode));
			
			String productName = plusModelProductinfo.getProductName();
			productName = productName.replaceAll("</?[^>]+>", "");
			productItem.setProductName(productName);//商品名称
			productItem.setProductCode(plusModelProductinfo.getProductCode());//商品编号
			productItem.setMainpicUrl(plusModelProductinfo.getMainpicUrl());//商品主图
			productItem.setTvTips(tvTips);
			productItem.setTvFlag("SI2003".equalsIgnoreCase(plusModelProductinfo.getSmallSellerCode()) ? 1 : 0);
			//是否被收藏
			ProductService pService = new ProductService();
			productItem.setCollectionProduct(pService.getIsCollectionProduct(plusModelProductinfo.getProductCode(), getFlagLogin() ? getOauthInfo().getUserCode() : null));
			//商品标签对应的图片地址
			PlusModelProductLabel pmpl = new ProductLabelService().getLabelInfo(plusModelProductinfo.getProductCode());
			if(pmpl.getFlagEnable() == 1){
				productItem.setLabelsPic(pmpl.getInfoPic());
			}
			
			//权威标志
			productItem.setAuthorityLogo(productLogoService.getProductAuthorityLogo(plusModelProductinfo.getSellerCode(), plusModelProductinfo.getSmallSellerCode(), productCode));
			
			//商品规格
			productItem.setPropertyList(plusModelProductinfo.getPropertyList());
			
			String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : null;
			
			//skuList
			List<ProductSkuInfoForApiNew> skuList = new ArrayList<ProductSkuInfoForApiNew>();
			PlusModelSkuInfo plusModelSkuInfo = null;
			ProductActivity act = null;
			for (PlusModelProductSkuInfo sku : plusModelProductinfo.getSkuList()) {
				ProductSkuInfoForApiNew skuObj = new ProductSkuInfoForApiNew();
				skuObj.setSkuName(sku.getSkuName());//sku名称
				skuObj.setSellPrice(sku.getSellPrice());//销售价
				skuObj.setMarketPrice(sku.getSellPrice());//市场价改成原价
				skuObj.setKeyValue(sku.getSkuKey());//sku规格
				skuObj.setMiniOrder(sku.getMiniOrder());	//起订数量
				skuObj.setSkuCode(sku.getSkuCode());
				
				plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(skuObj.getSkuCode(), memberCode, memberCode, 1);
				skuObj.setBuyStatus(plusModelSkuInfo.getBuyStatus());
				skuObj.setLimitBuy((int)plusModelSkuInfo.getLimitBuy());
				skuObj.setSellPrice(plusModelSkuInfo.getSellPrice());
				skuObj.setSkuPicUrl(plusModelSkuInfo.getSkuPicUrl());
				skuObj.setMiniOrder((int)plusModelSkuInfo.getMinBuy());
				skuObj.setSkuMaxBuy((int)plusModelSkuInfo.getMaxBuy());
				
				if(StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())){
					act = new ProductActivity();
					act.setEventName(plusModelSkuInfo.getSellNote());
					act.setEventType(plusModelSkuInfo.getEventType());
					act.setProductEventPic(plusModelSkuInfo.getDescriptionUrlHref());
					skuObj.getEvents().add(act);
			
					// 参与了扫码购活动的情况下，显示默认标签
					if("4497472600010004".equals(act.getEventType())){
						productItem.setTvTips("立减30");
					}
					
					// 参与了会员日活动的情况下，显示会员价标签
					if("4497472600010018".equals(act.getEventType())){
						productItem.setTvTips(plusModelSkuInfo.getSellNote());
					}
					
					// 内购
					if("4497472600010006".equals(act.getEventType())){
						productItem.setTvTips("内购");
					}
				}
				
				// 价格一样时屏蔽划线价
				if(skuObj.getSellPrice().compareTo(skuObj.getMarketPrice()) >= 0) {
					skuObj.setMarketPrice(null);
				}
				
				skuList.add(skuObj);
			}
			productItem.setSkuList(skuList);
			
			scannerAllow(productCode);
		}else{
			return null;
		}
		return productItem;
	}
	
	/**
	 * 扫码购商品标识
	 * @param productCode
	 */
	private void scannerAllow(String productCode){
		// 设置支持扫码购活动
		if(!XmasKv.upFactory(EKvSchema.ScannerAllow).exists(productCode)){
			// 默认30天
			XmasKv.upFactory(EKvSchema.ScannerAllow).setex(productCode, 3600*24*30, productCode);
		}
	}


}
