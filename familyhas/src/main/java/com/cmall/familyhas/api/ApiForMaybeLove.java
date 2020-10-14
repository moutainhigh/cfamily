package com.cmall.familyhas.api;




import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForMaybeLoveInput;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiForMaybeLoveResult;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 可能喜欢的商品
 * 
 * @author 李全通
 */
public class ApiForMaybeLove extends 
			RootApiForMember< ApiForMaybeLoveResult,  ApiForMaybeLoveInput>{
	
	public ApiForMaybeLoveResult Process(ApiForMaybeLoveInput api,
			MDataMap mResquestMap){
		if(StringUtils.isBlank(api.getBuyerType()))
		{
			api.setBuyerType("4497469400050002");
		}
		Integer isPurchase = api.getIsPurchase();
		String sellerCode = getManageCode();//获取商家编号
		int count=Integer.valueOf(Integer.parseInt(bConfig("familyhas.page_limit")));
		ApiForMaybeLoveResult result=new ApiForMaybeLoveResult();
		PageOption paging = new PageOption();
		if(api.getPageNum()<=0){
			return result;
			
		}
		String userType = api.getBuyerType();
		paging.setOffset(api.getPageNum()-1);
		paging.setLimit(count);
		MPageData mPageData=new MPageData();
		
		String sWhere ="STR_TO_DATE(start_time,'%Y-%m-%d %H:%i:%s')<NOW() AND STR_TO_DATE(end_time,'%Y-%m-%d %H:%i:%s')>NOW() AND product_status=4497153900060002 AND seller_code=:sellerCode";
		
		mPageData=DataPaging.upPageData("v_maybe_love_product", "product_code,product_name,min_sell_price,mainpic_url,market_price,start_time,end_time,position", "position desc,zid desc",sWhere, new MDataMap("sellerCode",sellerCode),paging);
		
		//查询数据库中喜欢的商品的总条数
		int page = mPageData.getPageResults().getTotal();
		//总页数
		int pagination=0;
		
		
		if(page%count==0){
			pagination=page/count;
		}
		else {
			pagination=page/count+1;
		}
		List<String> productArrList = new ArrayList<String>();
		
		
//		Collections.shuffle(mPageData.getListData());
		for(MDataMap mDataMap:mPageData.getListData()){
			productArrList.add(mDataMap.get("product_code"));
		}

		
		ProductService ps = new ProductService();
		//获取商品价格
		Map<String,BigDecimal> minSellPriceProductMap = new HashMap<String, BigDecimal>();
//		if (VersionHelper.checkServerVersion("3.5.62.55")) {
//			minSellPriceProductMap = ps.getMinProductActivityNew(productArrList,userType);
//			if (VersionHelper.checkServerVersion("3.5.95.55")) {
			PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
			skuQuery.setCode(StringUtils.join(productArrList,","));
			skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
			skuQuery.setIsPurchase(isPurchase);
			minSellPriceProductMap = new ProductPriceService().
					getProductMinPrice(skuQuery);// 获取商品最低销售价格
//			}else{
//				minSellPriceProductMap = new ProductService().
//						getMinProductActivityNew(productArrList, userType);// 获取商品最低销售价格
//			}
//		}else{
//			minSellPriceProductMap = ps.getMinProductActivity(productArrList);
//		}
		
				
//		Map<String, BigDecimal> minSellPriceProductMap = ps.getMinProductActivity(productArrList);
		ProductService productService = new ProductService();
		List<ProductMaybeLove> zero = new ArrayList<ProductMaybeLove>();
		for(MDataMap mDataMap:mPageData.getListData()){
			ProductMaybeLove maybeLove=new ProductMaybeLove();
			maybeLove.setProcuctCode(mDataMap.get("product_code"));
			maybeLove.setProductNameString(mDataMap.get("product_name")) ;
			
			if(minSellPriceProductMap.get(maybeLove.getProcuctCode()) ==  null){
				
				continue;
				
			}
			
//			maybeLove.setProductPrice(minSellPriceProductMap.get(maybeLove.getProcuctCode()).setScale(0,BigDecimal.ROUND_DOWN).toString());
			maybeLove.setProductPrice( MoneyHelper.format(minSellPriceProductMap.get(maybeLove.getProcuctCode())) );  // 兼容小数 - Yangcl
			maybeLove.setMainpic_url(mDataMap.get("mainpic_url")) ;
//			maybeLove.setMarket_price(new BigDecimal(mDataMap.get("market_price")).setScale(0,BigDecimal.ROUND_DOWN).toString());
			maybeLove.setMarket_price(MoneyHelper.format(new BigDecimal(mDataMap.get("market_price")))); // 兼容小数 - Yangcl
			if (productService.checkProductKjt(mDataMap.get("product_code"))) {
				maybeLove.setFlagTheSea("1");
			}
			// 缓存获取商品信息
			PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(mDataMap.get("product_code")));
			maybeLove.setLabelsList(plusModelProductinfo.getLabelsList());
			if(api.getPicWidth()>0)
			{
				PicInfo picInfo = productService.getPicInfoOprBig(api.getPicWidth(),mDataMap.get("mainpic_url"));
				maybeLove.setMainpic_url(picInfo.getPicNewUrl());
			}
			
			if("0".equals(mDataMap.get("position"))){
				zero.add(maybeLove);
			}else {
				result.getProductMaybeLove().add(maybeLove);
			}
		}
		Collections.shuffle(zero);
		result.getProductMaybeLove().addAll(zero);
		result.setPagination(pagination);
		return result;
		
	}

}
