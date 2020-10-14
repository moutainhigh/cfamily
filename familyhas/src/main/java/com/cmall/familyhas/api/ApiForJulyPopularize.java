package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForJulyPopularizeInput;
import com.cmall.familyhas.api.model.JulyPopularizeProduct;
import com.cmall.familyhas.api.result.ApiForJulyPopularizeResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/** 
* @author Angel Joy
* @Time 2020年6月11日 上午10:35:29 
* @Version 1.0
* <p>Description:</p>
* 7月推广活动接口
*/
public class ApiForJulyPopularize extends RootApiForToken<ApiForJulyPopularizeResult, ApiForJulyPopularizeInput> {

	@Override
	public ApiForJulyPopularizeResult Process(ApiForJulyPopularizeInput inputParam, MDataMap mRequestMap) {
		String memberCode = getUserCode();
		Integer currentPage = inputParam.getCurrentPage();
		Integer pageNum =  inputParam.getPageNum();
		String custId = new PlusServiceAccm().getCustId(memberCode);
		String channelId = getChannelId();
		String source = inputParam.getSource();
		List<Map<String,Object>> ldProducts = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> hjyProducts = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> operProducts = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> allProducts = new ArrayList<Map<String,Object>>();
		String sqlLd = "SELECT product_code, vl_ors, 1 as buy_flag FROM familyhas.fh_share_ld_product WHERE cust_id = :cust_id GROUP BY product_code";
		ldProducts = DbUp.upTable("fh_share_ld_product").dataSqlList(sqlLd, new MDataMap("cust_id",custId));
		String yearBefore = DateUtil.addDateHour(DateUtil.getSysDateTimeString(), -8760);
		String sqlHjy = "SELECT  product_code, 'Y' AS vl_ors, 1 as buy_flag FROM ordercenter.oc_orderdetail WHERE order_code IN ( SELECT order_code FROM ordercenter.oc_orderinfo WHERE buyer_code = :buyer_code AND small_seller_code != 'SI2003' AND small_seller_code != '' AND order_status != '4497153900010006' AND create_time >'"+yearBefore+"'  ) GROUP BY product_code";
		hjyProducts = DbUp.upTable("oc_orderdetail").dataSqlList(sqlHjy, new MDataMap("buyer_code",memberCode));
		allProducts.addAll(ldProducts);
		allProducts.addAll(hjyProducts);
		List<String> productCodes = new ArrayList<String>();
		for(Map<String,Object> map : allProducts) {
			String productCode = "'"+MapUtils.getString(map, "product_code","")+"'";
			if(StringUtils.isNotEmpty(productCode)) {
				productCodes.add(productCode);
			}
		}
		String sqlOper = "";
		if(productCodes.size() == 0) {
			sqlOper = "SELECT product_code,vl_ors,0 AS buy_flag FROM familyhas.fh_share_oper_product ";
		}else {
			sqlOper = "SELECT product_code,vl_ors,0 AS buy_flag FROM familyhas.fh_share_oper_product WHERE product_code not in ("+ StringUtils.join(productCodes, ",")+")";
		}
		operProducts = DbUp.upTable("fh_share_oper_product").dataSqlList(sqlOper, new MDataMap());
		allProducts.addAll(operProducts);
		Integer start = (currentPage-1)*pageNum;
		List<JulyPopularizeProduct> listProduct = new ArrayList<JulyPopularizeProduct>();
		Integer totalNum = allProducts.size();
		Integer totalPage = 0;
		if(totalNum%pageNum == 0) {
			totalPage = totalNum/pageNum;
		}else {
			totalPage = (totalNum/pageNum) + 1;
		}
		while(true) {
			if(listProduct.size() == 0) {
				listProduct = this.getGoods(allProducts, memberCode,start, pageNum, channelId,source);
				currentPage ++;
				start = (currentPage-1)*pageNum;
			}else {
				currentPage--;
				break;
			}
			if(currentPage > totalPage) {
				currentPage--;
				break;
			}
		}
		ApiForJulyPopularizeResult result = new ApiForJulyPopularizeResult();
		result.setCurrentPage(currentPage);
		result.setGoods(listProduct);
		result.setResultCode(1);
		result.setTotalPage(totalPage);
		return result;
	}
	
	/**
	 * 
	 * @param sql
	 * @param memberCode
	 * @param custId
	 * @param start
	 * @param pageNum
	 * @return
	 * 2020年6月11日
	 * Angel Joy
	 * List<JulyPopularizeProduct>
	 */
	private List<JulyPopularizeProduct> getGoods(List<Map<String,Object>> allProducts,String memberCode,Integer start,Integer pageNum,String channelId,String source){
		List<JulyPopularizeProduct> listProduct = new ArrayList<JulyPopularizeProduct>();
		if(start >= allProducts.size()) {
			return listProduct;
		}
		Integer end = start + pageNum;
		if(end > allProducts.size() ) {
			end = allProducts.size();
		}
		List<Map<String,Object>> newMap = allProducts.subList(start, end);
		PlusSupportProduct psp = new PlusSupportProduct();
		ProductService ps = new ProductService();
		for(Map<String,Object> map : newMap) {
			String productCode = MapUtils.getString(map, "product_code");
			PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(productCode);
			PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
			List<PlusModelProductSkuInfo> skuList = plusModelProductinfo.getSkuList();
			BigDecimal rate = new BigDecimal("1000000");//设置一个无限大的值
			PlusModelSkuInfo skuInfo = new PlusModelSkuInfo();
			String smallSallerCode = plusModelProductinfo.getSmallSellerCode();
			for(PlusModelProductSkuInfo sku : skuList) {
				PlusModelSkuQuery pq = new PlusModelSkuQuery();
				pq.setFxFlag("0");
				pq.setCode(sku.getSkuCode());
				pq.setMemberCode(memberCode);
				pq.setIsPurchase(1);
				pq.setChannelId(channelId);
				PlusModelSkuInfo skuInfoB = psp.upSkuInfo(pq).getSkus().get(0);
				BigDecimal costPrice = sku.getCostPrice();
				if("SI2003".equals(smallSallerCode)) {//LD 品 需要加10%到成本
					costPrice = costPrice.multiply(new BigDecimal("1.1"));
				}
				if("N".equals(plusModelProductinfo.getVlOrs())) {//非一件代发品需要加50运费到成本
					costPrice = costPrice.add(new BigDecimal(50));
				}
				BigDecimal skuPrice = skuInfoB.getSellPrice();
				int a = rate.compareTo(skuPrice.subtract(costPrice));
				if(a == 1) {
					rate = skuPrice.subtract(costPrice);
					skuInfo = skuInfoB;
				}
			}
			//拿最小值与30作比较。如果高于三十则可以。
			if(rate.compareTo(new BigDecimal(bConfig("xmasorder.share_profit"))) >= 0) {
				JulyPopularizeProduct product = new JulyPopularizeProduct();
				product.setBuyFlag(MapUtils.getString(map, "buy_flag"));
				product.setMainPicUrl(plusModelProductinfo.getMainpicUrl());
				product.setSharePic(cutSharePic(plusModelProductinfo.getMainpicUrl(),source,productCode));
				product.setProductCode(productCode);
				product.setProductName(plusModelProductinfo.getProductName());
				product.setSaveValue(MoneyHelper.round(0, BigDecimal.ROUND_FLOOR, rate.multiply(new BigDecimal("0.2"))));
				product.setSellPrice(skuInfo.getSellPrice());
				if(skuInfo.getSellPrice().compareTo(skuInfo.getSkuPrice()) < 0) {
					product.setSkuPrice(skuInfo.getSkuPrice());
				}
				List<TagInfo> tagInfo = ps.getProductTagInfoList(productCode, memberCode, channelId);
				product.setTagInfoList(tagInfo);
				PlusSupportStock pss = new PlusSupportStock();
				if(pss.upAllStockForProduct(productCode) <= 0) {//库存为0
					continue;
				}
				if(!"4497153900060002".equals(plusModelProductinfo.getProductStatus())) {
					continue;
				}
				//排除橙意卡商品。
				if(bConfig("familyhas.plus_product_code").equals(productCode)) {
					continue;
				}
				listProduct.add(product);
			}
		}
		return listProduct;
	}
	
	private String cutSharePic(String url,String source,String productCode) {
		if(StringUtils.isEmpty(url)) {
			return "";
		}
		ProductService ps = new ProductService();
		List<String> picUrlArr = new ArrayList<String>();
		picUrlArr.add(url);
		List<PicInfo> picInfoList = new ArrayList<PicInfo>();
		if("xcx".equals(source)) {//小程序的分享图为5:4，在此方法内，需要将图的宽：高切割为300：240.
			picInfoList = ps.getPicInfoOprBigForMulti(300, picUrlArr,"");
			String newUrl = ps.getPicInfoByWH(300, 240,picInfoList.get(0).getPicNewUrl(),productCode);
			return newUrl;
		}else {
			picInfoList = ps.getPicInfoOprBigForMulti(300, picUrlArr,"");
			return picInfoList.get(0).getPicNewUrl();
		}
	}
	
	public static void main(String[] args) {
//		ProductService ps = new ProductService();
//		String url = "http://wcfiles.beta.huijiayou.cn/cfiles/staticfiles/scale/2c1eb/p0/b2f4d38ad9ef42aab8a1f6a693263570.jpg";
//		String newUrl = ps.getPicInfoByWH(300, 240,url,"123456");
//		System.out.println(newUrl);
		String yearBefore = DateUtil.addDateHour(DateUtil.getSysDateTimeString(), -8760);
		System.out.println(yearBefore);
	}
}
