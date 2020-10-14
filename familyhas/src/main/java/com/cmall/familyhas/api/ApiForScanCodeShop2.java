package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForScanCodeShopInput;
import com.cmall.familyhas.api.model.ProductItem;
import com.cmall.familyhas.api.model.ProductSkuInfoForApiNew;
import com.cmall.familyhas.api.result.ApiForScanCodeShopResult;
import com.cmall.groupcenter.homehas.RsyncProductForScanCode;
import com.cmall.groupcenter.homehas.RsyncProductForScanCode2;
import com.cmall.groupcenter.homehas.model.FormResult;
import com.cmall.groupcenter.homehas.model.GoodForScanCode;
import com.cmall.groupcenter.homehas.model.RsyncResponseProductForScanCode;
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
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 新扫码购实时获取直播节目商品信息(南京二台)
 * @author 任宏斌
 */
public class ApiForScanCodeShop2 extends RootApiForVersion<ApiForScanCodeShopResult, ApiForScanCodeShopInput> {
	
	ProductLogoService productLogoService = new ProductLogoService();
	
	@Override
	public ApiForScanCodeShopResult Process(ApiForScanCodeShopInput inputParam, MDataMap mRequestMap) {
		
		ApiForScanCodeShopResult result = new ApiForScanCodeShopResult();
		List<ProductItem> productItems = new ArrayList<ProductItem>();
		
		String so_id = inputParam.getSo_id();
		
		//没有频道 查南京二台节目
		if(StringUtils.isEmpty(so_id)) {
			so_id = "1000017";
		}

		//先从LD取节目商品
		RsyncProductForScanCode2 rsyncProductForScanCode = new RsyncProductForScanCode2();
		rsyncProductForScanCode.upRsyncRequest().setSo_id(so_id);
		rsyncProductForScanCode.doRsync();
		RsyncResponseProductForScanCode rsyncResponseProductForScanCode = rsyncProductForScanCode.upProcessResult();
		
		if(null != rsyncResponseProductForScanCode && rsyncResponseProductForScanCode.getSuccess() 
				&& rsyncResponseProductForScanCode.getResult() != null && rsyncResponseProductForScanCode.getResult().length > 0){
			
			FormResult[] formArray = rsyncResponseProductForScanCode.getResult();
			
			for (FormResult formResult : formArray) {//TODO LD默认报文顺序为 本档+上一档 若顺序改变 此处需更改
				
				List<GoodForScanCode> goodsList = formResult.getGoodsList();
				
				int i = 0;//LD可卖商品计数器
				if(null != goodsList && goodsList.size() > 0){
					for (GoodForScanCode goodForScanCode : goodsList) {
						ProductItem goods = handlerGoods(goodForScanCode.getGood_id());
						if(null != goods){
							goods.setFormId(formResult.getForm_id());
							productItems.add(goods);
							i ++ ;
						}
					}
				}
				
				int j = 0;//惠家有本档可卖商品计数器
				if(i == 0 && "1".equals(formResult.getForm_type())){//本档商品在惠家有不可售情况
					//取惠家有配置的本档节目
					List<MDataMap> pcNowGoodsList = DbUp.upTable("pc_tv").query("good_id,form_id", null, "form_fr_date<=NOW() and form_end_date>NOW() and so_id=:so_id", new MDataMap("so_id", so_id), -1, -1);
					
					if(null != pcNowGoodsList && pcNowGoodsList.size() > 0){
						for (MDataMap goodMap : pcNowGoodsList) {
							ProductItem goods = handlerGoods(goodMap.get("good_id"));
							if(null != goods){
								goods.setFormId(goodMap.get("form_id"));
								productItems.add(goods);
								j ++;
							}
						}
					}
				}else if(i == 0 && "2".equals(formResult.getForm_type())){//上一档商品在惠家有不可售情况 
					//取惠家有配置的上一档节目
					List<Map<String, Object>> pcPrevGoodsList = DbUp.upTable("pc_tv")
							.dataSqlList("SELECT good_id,form_id FROM pc_tv WHERE form_end_date=(SELECT MAX(form_end_date) FROM pc_tv WHERE form_end_date<NOW() and so_id=:so_id1) and so_id=:so_id2", new MDataMap("so_id1", so_id, "so_id2", so_id));
					
					if(null != pcPrevGoodsList && pcPrevGoodsList.size() > 0){
						for (Map<String, Object> goodMap : pcPrevGoodsList) {
							ProductItem goods = handlerGoods(goodMap.get("good_id") + "");
							if(null != goods){
								goods.setFormId(goodMap.get("form_id").toString());
								productItems.add(goods);
							}
						}
					}
				}
				
				if("1".equals(formResult.getForm_type())){
					if((i == 1) || (i == 0 && j == 1)){
						//LD本档商品只有1件 且在惠家有可卖  或  LD本档不可售  查惠家有配置本档为1件商品
						break;
					}
				}
			}
		}else{//同步LD节目单失败 则直接在惠家有配置表中查
			//取惠家有配置的本档节目
			List<MDataMap> pcNowGoodsList = DbUp.upTable("pc_tv").query("good_id,form_id", null, "form_fr_date<=NOW() and form_end_date>NOW() and so_id=:so_id", new MDataMap("so_id", so_id), -1, -1);
			
			int j = 0;//惠家有本档可卖商品计数器
			if(null != pcNowGoodsList && pcNowGoodsList.size() > 0){
				for (MDataMap goodMap : pcNowGoodsList) {
					ProductItem goods = handlerGoods(goodMap.get("good_id"));
					if(null != goods){
						goods.setFormId(goodMap.get("form_id").toString());
						productItems.add(goods);
					}
				}
			}
			
			if(j != 1){//本档配置商品不为1 则再查上一档商品
				//取惠家有配置的上一档节目
				List<Map<String, Object>> pcPrevGoodsList = DbUp.upTable("pc_tv")
						.dataSqlList("SELECT good_id,form_id FROM pc_tv WHERE form_end_date=(SELECT MAX(form_end_date) FROM pc_tv WHERE form_end_date<NOW() and so_id=:so_id1) and so_id=:so_id2",new MDataMap("so_id1", so_id, "so_id2", so_id));
				
				if(null != pcPrevGoodsList && pcPrevGoodsList.size() > 0){
					for (Map<String, Object> goodMap : pcPrevGoodsList) {
						ProductItem goods = handlerGoods(goodMap.get("good_id") + "");
						if(null != goods){
							goods.setFormId(goodMap.get("form_id").toString());
							productItems.add(goods);
						}
					}
				}
			}
		}
		
		result.setProductList(productItems);
		return result;
	}
	
	/**
	 * 根据商品编号从缓存中查询商品信息
	 * @param productCode
	 * @return ProductItem
	 */
	private ProductItem handlerGoods(String productCode){
		// 商品下架或商品不存在的时忽略此商品
		if(DbUp.upTable("pc_productinfo").count("product_code",productCode,"product_status","4497153900060002") == 0){
			return null;
		}
		
		ProductItem productItem = new ProductItem();
		
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
				skuObj.setMarketPrice(plusModelProductinfo.getMarketPrice());//市场价
				skuObj.setKeyValue(sku.getSkuKey());//sku规格
				skuObj.setMiniOrder(sku.getMiniOrder());	//起订数量
				skuObj.setSkuCode("IC_SMG_"+sku.getSkuCode());//sku编号，增加扫码购特定前缀
				
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
