package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBrandPreferenceContentInput;
import com.cmall.familyhas.api.model.BrandPic;
import com.cmall.familyhas.api.model.BrandProduct;
import com.cmall.familyhas.api.result.ApiForBrandPreferenceContentResult;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.CategoryService;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.service.ProductStoreService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;


/** 
* @ClassName: ApiForBrandPreferenceContent 
* @Description: 品牌专题内容
* @author 张海生
* @date 2015-5-12 下午2:13:55 
*  
*/
public class ApiForBrandPreferenceContent
		extends
		RootApiForMember<ApiForBrandPreferenceContentResult, ApiForBrandPreferenceContentInput> {
	private final String CATEGORY  = "4497471600020003";		//链接类型为分类
	public ApiForBrandPreferenceContentResult Process(
			ApiForBrandPreferenceContentInput inputParam, MDataMap mRequestMap) {
		ApiForBrandPreferenceContentResult result = new ApiForBrandPreferenceContentResult();
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType("4497469400050002");
		}
		String infoCode = inputParam.getInfoCode();// 专题code
		MDataMap mdata = DbUp.upTable("fh_brand_preference")
				.oneWhere("discount_type,up_time,down_time", "", "",
						"info_code", infoCode);// 查询专题信息
		if (mdata != null) {
			result.setUpTime(mdata.get("up_time"));
			result.setDownTime(mdata.get("down_time"));
		}else{
			return result;
		}
		List<MDataMap> brandPicList = DbUp.upTable(
				"fh_brand_cotent_preference").queryAll(
				"brand_pic,brand_location,link_type,link_value", "", "",
				new MDataMap("info_code", infoCode));// 查询专题广告图
		List<String> categoryCodeArr = new ArrayList<String>();
		ProductService productService = new ProductService();
		for (MDataMap mDataMap : brandPicList) {
			BrandPic brandPic = new BrandPic();
			String brandPicurl = mDataMap.get("brand_pic");
			if (inputParam.getPicWidth() > 0
					&& StringUtils.isNotEmpty(brandPicurl)) {//图片压缩
				PicInfo picInfo = productService.getPicInfoOprBig(inputParam.getPicWidth(),brandPicurl);
				brandPicurl = picInfo.getPicNewUrl();
			}
			brandPic.setBrandPic(brandPicurl);
			brandPic.setBrandLocation(mDataMap.get("brand_location"));
			String linkType = mDataMap.get("link_type");
			brandPic.setLinkType(linkType);
			String linkValue = mDataMap.get("link_value");
			if(CATEGORY.equals(linkType)){
				categoryCodeArr.add(linkValue);
			}
			brandPic.setLinkValue(linkValue);
			result.getBrandPicList().add(brandPic);
		}
		if(categoryCodeArr != null && categoryCodeArr.size() > 0){
			CategoryService ct = new CategoryService();
			MDataMap ctMap = ct.getCategoryName(categoryCodeArr, "SI2003");
			for (BrandPic brandPic : result.getBrandPicList()) {
				if(CATEGORY.equals(brandPic.getLinkType())){//获取分类名称
					brandPic.setLinkValue(ctMap.get(brandPic.getLinkValue()));
				}
			}
		}
		String sSql = "SELECT pr.product_code,pr.product_status,pr.product_sort,pi.mainpic_url,pi.product_name,pi.market_price " +
				"FROM pc_brand_rel_product pr LEFT JOIN pc_productinfo pi ON pr.product_code = pi.product_code " +
				"WHERE pr.info_code =:info_code AND pr.product_status = '1' order by pr.product_sort desc,pr.zid desc";
		 List<Map<String, Object>> proList = DbUp.upTable("pc_brand_rel_product").dataSqlList(sSql, new MDataMap("info_code",infoCode));
		if(proList != null && proList.size() > 0){
			List<String> pList = new ArrayList<String>();
			for (Map<String, Object> mDataMap : proList) {
				String t_productCode = (String)mDataMap.get("product_code");
				
				/*
				 * 过滤下架的商品
				 * fq
				 */
				PlusModelProductQuery plus = new PlusModelProductQuery(t_productCode);
				PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
				if("4497153900060002".equals(upInfoByCode.getProductStatus())) {//已上架
					pList.add(t_productCode);
				}
			}
//			ProductService pService = BeansHelper
//					.upBean("bean_com_cmall_productcenter_service_ProductService");
//			Map<String, BigDecimal> productPriceMap = pService
//					.getMinProductActivityNew(pList, inputParam.getBuyerType());// 获取商品最低价格
			Map<String, BigDecimal> productPriceMap = new HashMap<String,BigDecimal>();
			if (VersionHelper.checkServerVersion("3.5.95.55")) {
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(StringUtils.join(pList,","));
				skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
				productPriceMap = new ProductPriceService().
						getProductMinPrice(skuQuery);// 获取商品最低销售价格
			}else{
				productPriceMap = new ProductService().
						getMinProductActivityNew(pList, inputParam.getBuyerType());// 获取商品最低销售价格
			}
			ProductStoreService pStoreService = new ProductStoreService();
			//获取商品是否有库存
			Map<String,Integer> productStockMap = pStoreService.getStockNumAll("", StringUtils.join(pList,","),1);
			List<Double> discoutList = new ArrayList<Double>();
			for (Map<String, Object> mDataMap : proList) {
				String productCode = (String)mDataMap.get("product_code");
				
				/*
				 * 过滤下架的商品
				 * fq
				 */
				PlusModelProductQuery plus = new PlusModelProductQuery(productCode);
				PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
				if(!"4497153900060002".equals(upInfoByCode.getProductStatus())) {//非上架商品  过滤
					continue;
				}
				
				BrandProduct brandProduct = new BrandProduct();
				String newMainPicUrl = (String)mDataMap.get("mainpic_url");
				if(inputParam.getPicWidth() > 0 && StringUtils.isNotEmpty(newMainPicUrl)){//图片压缩
					PicInfo picInfo1 = productService.getPicInfoOprBig(inputParam.getPicWidth(),newMainPicUrl);
					newMainPicUrl = picInfo1.getPicNewUrl();
				}
				brandProduct.setPic(newMainPicUrl);
				brandProduct.setProcuctCode(productCode);
				brandProduct.setProductName((String)mDataMap.get("product_name"));
				brandProduct.setMarketPrice((BigDecimal)mDataMap.get("market_price"));
				if (productPriceMap != null) {
					brandProduct.setSalePrice(productPriceMap.get(productCode));
				}
				BigDecimal marktPrice = brandProduct.getMarketPrice();
				String discount = "";
				if(marktPrice != null && !"0.00".equals(marktPrice.toString())){
					BigDecimal discount1 = brandProduct.getSalePrice().multiply(new BigDecimal(10)).divide(brandProduct.getMarketPrice(), 1,
							BigDecimal.ROUND_HALF_UP);// 折扣（四舍五入取一位小数）
					discoutList.add(discount1.doubleValue());
					discount = discount1.toString();
					if("0.0".equals(discount)){
						discount = "";
					}
					if (discount.indexOf(".") > 0) {
						discount = discount.replaceAll("0+?$", "");// 去掉后面无用的零
						discount = discount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
					}
				}
				brandProduct.setDiscount(discount);
				String storeNum = "";
				Integer store = productStockMap.get(productCode);
				if (productStockMap != null && store != null && store.intValue() == 1) {
					storeNum = "1";// 有货
				} else {
					storeNum = "0";// 售罄
				}
				brandProduct.setStoreFlag(storeNum);
				result.getProductList().add(brandProduct);
			}
			Collections.sort(discoutList);
			if(discoutList.size() > 0) {
				
				String beginDiscount = discoutList.get(0).toString();
				if("0.0".equals(beginDiscount)){
					beginDiscount = "";
				}
				if (beginDiscount.indexOf(".") > 0) {
					beginDiscount = beginDiscount.replaceAll("0+?$", "");// 去掉后面无用的零
					beginDiscount = beginDiscount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
				}
				result.setDiscount(beginDiscount);
			}
			
		}
		return result;
	}

}
