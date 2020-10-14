package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiRecommendForLdPaySuccessInput;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiRecommendForLdPaySuccessResult;
import com.cmall.familyhas.util.ProductCodeCopyLoader;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/** 
* @author Angel Joy
* @Time 2020-8-17 15:57:52 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiRecommendForLdPaySuccess extends RootApiForVersion<ApiRecommendForLdPaySuccessResult, ApiRecommendForLdPaySuccessInput> {

	@Override
	public ApiRecommendForLdPaySuccessResult Process(ApiRecommendForLdPaySuccessInput inputParam, MDataMap mRequestMap) {
		ApiRecommendForLdPaySuccessResult result = new ApiRecommendForLdPaySuccessResult();
		Map<String,Object> productCodesMap = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne("SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1",new MDataMap());
		String productCodes = MapUtils.getString(productCodesMap,"product_codes","");
		String recommend_type = MapUtils.getString(productCodesMap,"recommend_type","");
		int pageSize = inputParam.getPageSize();
		if("4497471600660001".equals(recommend_type)) {
			result.setResultCode(0);
			result.setResultMessage("请访问猜你喜欢接口！");
			return result;
		}
		String ignore = "Y";//默认过滤
		Map<String,Object> ifIgnoreMap = DbUp.upTable("uc_ignore_category").dataSqlOne("SELECT * FROM usercenter.uc_ignore_category limit 1", new MDataMap());
		if(ifIgnoreMap != null) {
			ignore = MapUtils.getString(ifIgnoreMap, "if_ignore","Y");
		}
		String args[] = productCodes.split(",");
		
		List<ProductMaybeLove> totalList = new ArrayList<ProductMaybeLove>();
		boolean flag = true;
		int pageIndex = inputParam.getPageIndex();
		int total = args.length;
		int pagination = total%pageSize == 0?total/pageSize:(total/pageSize)+1;
		if(pageIndex > pagination) {
			result.setResultCode(0);
			result.setResultMessage("页码不正确！");
			return result;
		}
		while(flag) {
			int start = pageSize *(pageIndex-1);
			int end = start + pageSize;
			if(end > args.length) {
				end = args.length;
			}
			if(start> end) {
				start = end-1;
			}
			totalList = this.getData(start,end,args,ignore);
			if(totalList.size() == 0) {
				pageIndex ++;
			}
			if(totalList.size() != 0) {
				flag = false;
			}
			if(end == args.length) {
				flag = false;
			}
		}
		result.setProductMaybeLove(totalList);
		result.setPagination(pagination);
		if(pageIndex>pagination) {
			pageIndex = pagination;
		}
		result.setNowPage(pageIndex);
		result.setResultCode(1);
		return result;
	}
	
	private List<ProductMaybeLove> getData(int start,int end,String []args,String ignore) {
		List<ProductMaybeLove> totalList = new ArrayList<ProductMaybeLove>();
		for(int i = start;i < end;i++ ) {
			if(!StringUtils.isEmpty(args[i])) {
				ProductMaybeLove maybeLove = getMayBeLove(args[i],"",1,getChannelId(),ignore);
				if(maybeLove != null) {
					totalList.add(maybeLove);
				}
			}
		}
		return totalList;
	}
	
	/**
	 * 查询数据
	 * @param productCode
	 * @param requestId
	 * @param isPurchase
	 * @param channelId
	 * @param ignore
	 * @return
	 * 2020-8-20
	 * Angel Joy
	 * ProductMaybeLove
	 */
	private ProductMaybeLove getMayBeLove(String productCode, String requestId, int isPurchase, String channelId,String ignore) {
		// 如果是调编的商品则用新的商品编号替换
		String productCodeNew = ProductCodeCopyLoader.queryCode(productCode);
		ProductService pService = new ProductService();
		if(StringUtils.isNotBlank(productCodeNew)) {
			productCode = productCodeNew;
		}
		PlusModelProductInfo plusModelProductInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
		// 忽略下架商品
		if(plusModelProductInfo == null || !"4497153900060002".equals(plusModelProductInfo.getProductStatus())){
			return null;
		}
		//忽略库存数小于0的商品
		long allStock = new PlusSupportStock().upAllStockForProduct(productCode);
		if(allStock <= 0) {
			return null;
		}
		PlusModelProductInfo initProductInfoFromDb = new PlusSupportProduct().upProductInfo(productCode);
		List<String> categorys = initProductInfoFromDb.getCategorys();
		if("449747430023".equals(channelId) && "Y".equals(ignore)) {
			for(String str : categorys) {
				String validSql = "select * from uc_program_del_category where category_code =:category_code";
				MDataMap mWhereMap = new MDataMap();
				mWhereMap.put("category_code", str);
				
				List<Map<String,Object>> dataSqlList = DbUp.upTable("uc_program_del_category").dataSqlList(validSql, mWhereMap);
				
				//4级验证
				if(dataSqlList.size() > 0) {
					return null;
				}else {
					String sql = "select * from uc_sellercategory where category_code =:category_code and seller_code = 'SI2003'";
					
					List<Map<String,Object>> dataSqlList2 = DbUp.upTable("uc_sellercategory").dataSqlList(sql, mWhereMap);
					if(dataSqlList2.size() > 0) {
						String parent_code = MapUtils.getString(dataSqlList2.get(0),"parent_code");
						mWhereMap.put("category_code", parent_code);
						List<Map<String,Object>> dataSqlList3 = DbUp.upTable("uc_program_del_category").dataSqlList(validSql, mWhereMap);
						//3级验证
						if(dataSqlList3.size() > 0) {
							return null;
						}else {
							mWhereMap.put("category_code", parent_code);
							List<Map<String,Object>> dataSqlList4 = DbUp.upTable("uc_sellercategory").dataSqlList(sql, mWhereMap);
							if(dataSqlList4.size() > 0) {
								parent_code = MapUtils.getString(dataSqlList4.get(0),"parent_code");
								mWhereMap.put("category_code", parent_code);
								List<Map<String,Object>> dataSqlList5 = DbUp.upTable("uc_program_del_category").dataSqlList(validSql, mWhereMap);
								//2级验证
								if(dataSqlList5.size() > 0) {
									return null;
								}
							}
							
						}
					}
					
				}
			}
		}
		
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		skuQuery.setCode(productCode);
		skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
		skuQuery.setIsPurchase(isPurchase);
		skuQuery.setChannelId(channelId);
		PlusModelSkuInfo plusModelSkuInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery).get(productCode);
		//如果商品取不到价格则返回nulll
		if(plusModelSkuInfo == null) return null;
		
		//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
		String ssc =plusModelProductInfo.getSmallSellerCode();
		String st="";
		if("SI2003".equals(ssc)) {
			st="4497478100050000";
		}else {
			st = WebHelper.getSellerType(ssc);
		}
		@SuppressWarnings("rawtypes")
		Map productTypeMap = WebHelper.getAttributeProductType(st);
		
		ProductMaybeLove productMaybeLove = new ProductMaybeLove();
		productMaybeLove.setProductPrice(MoneyHelper.format(plusModelSkuInfo.getSellPrice()));  
		productMaybeLove.setLabelsList(plusModelProductInfo.getLabelsList());
		productMaybeLove.setLabelsPic(new ProductLabelService().getLabelInfo(plusModelProductInfo.getProductCode()).getListPic());
		productMaybeLove.setFlagTheSea(plusModelProductInfo.getFlagTheSea());		
		productMaybeLove.setMainpic_url(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, plusModelProductInfo.getMainpicUrl()).getPicNewUrl());
		productMaybeLove.setProcuctCode(plusModelProductInfo.getProductCode());
		productMaybeLove.setProductNameString(plusModelProductInfo.getProductName());
		productMaybeLove.setRecommendId(requestId);
		productMaybeLove.setSmallSellerCode(plusModelProductInfo.getSmallSellerCode());
		productMaybeLove.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
		productMaybeLove.setLabelsInfo(new ProductLabelService().getLabelInfoList(productCode));
		
		if(plusModelSkuInfo.getSellPrice().compareTo(plusModelSkuInfo.getSkuPrice()) < 0) {
			productMaybeLove.setMarket_price(MoneyHelper.format(plusModelSkuInfo.getSkuPrice()));
			productMaybeLove.setSkuPrice(plusModelSkuInfo.getSkuPrice());
		}
		
		return productMaybeLove;
	}

}
