package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.cmall.familyhas.api.input.ApiForGetTvFormInfoInput;
import com.cmall.familyhas.api.model.TodayProduct;
import com.cmall.familyhas.api.result.ApiForGetTvFormInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 查询TV节目信息接口
 */
public class ApiForGetTvFormInfo extends RootApiForVersion<ApiForGetTvFormInfoResult, ApiForGetTvFormInfoInput> {

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	static LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	public ApiForGetTvFormInfoResult Process(ApiForGetTvFormInfoInput inputParam, MDataMap mRequestMap) {
		ApiForGetTvFormInfoResult result = new ApiForGetTvFormInfoResult();
		String day = FormatHelper.upDateTime("yyyy-MM-dd");
		String channelId = getChannelId();
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		
		try {
			// 如果有传入则以传入参数为准
			day = DateFormatUtils.format(DateUtils.parseDate(inputParam.getDate(), new String[]{"yyyy-MM-dd"}), "yyyy-MM-dd");
		} catch (ParseException e) {}
		
		// 查询指定天的节目表，增加时间限制最多只支持查询第二天的节目单
		String sWhere = "so_id = '1000001' AND form_end_date > now() AND form_fr_date < now()";
		
		List<MDataMap> queryAllList = DbUp.upTable("pc_tv").queryAll("", "form_fr_date,form_seq", sWhere, new MDataMap("day", day+"%"));
		
		ApiForGetTvFormInfoResult.TvForm tvForm = null;
		TodayProduct item = null;
		String productCode;
		PlusModelProductInfo productInfo;
		for(MDataMap map : queryAllList) {
			if(tvForm == null || !tvForm.getFormId().equals(map.get("form_id"))) {
				tvForm = new ApiForGetTvFormInfoResult.TvForm();
			}
			
			tvForm.setFormId(map.get("form_id"));
			tvForm.setTitle(map.get("title_nm"));
			tvForm.setStartDate(map.get("form_fr_date"));
			tvForm.setEndDate(map.get("form_end_date"));
			result.getList().add(tvForm);
			
			productCode = map.get("good_id");
			productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
			if(!"4497153900060002".equals(productInfo.getProductStatus())) {
				continue;
			}
			
			PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
			skuQuery.setCode(productCode);
			skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
			skuQuery.setChannelId(getChannelId());
			PlusModelSkuInfo skuPriceInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery).get(productCode);
			if(skuPriceInfo == null) {
				continue;
			}
			
			item = new TodayProduct();
			item.setId(productCode);
			
			String ssc = productInfo.getSmallSellerCode();
			String st="";
			if("SI2003".equals(ssc)) {
				st="4497478100050000";
			}
			else {
				PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(ssc));
				st = sellerInfo.getUc_seller_type();
			}
			//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
			Map productTypeMap = WebHelper.getAttributeProductType(st);
			item.setProClassifyTag(productTypeMap.get("proTypeListPic").toString() );
			item.setName(productInfo.getProductName());
			item.setSalePrice(MoneyHelper.format(skuPriceInfo.getSellPrice()));

			if(skuPriceInfo.getSellPrice().compareTo(skuPriceInfo.getSkuPrice()) < 0) {
				item.setMarkPrice(MoneyHelper.format(skuPriceInfo.getSkuPrice()));
				String dis = skuPriceInfo.getSellPrice().multiply(new BigDecimal(100)).divide(skuPriceInfo.getSkuPrice(), 0, BigDecimal.ROUND_HALF_UP).intValue()+"";
				if(dis.endsWith("0")){
					dis = dis.substring(0, dis.length() - 1);
				}
				item.setDiscount(dis);
				item.setSaveValue(MoneyHelper.format(skuPriceInfo.getSkuPrice().subtract(skuPriceInfo.getSellPrice())));
				item.setEventType(skuPriceInfo.getEventType());
			}
			
			item.setProductPic(productInfo.getMainpicUrl());
			
			if("SI2003".equals(productInfo.getSmallSellerCode())) {
				//取productcenter.pc_productadpic中的图片
				item.setAdPic(getPcProductAdpic(productInfo.getProductCode()));
			} else {
				item.setAdPic(productInfo.getAdpicUrl()); //商品广告图
			}
			
			item.setVideoUrlTV(bConfig("familyhas.video_url_TV"));
			
			ProductService service = new ProductService();
			item.setTagInfoList(service.getProductTagInfoList(productCode, memberCode, channelId));
			tvForm.getProductList().add(item);
		}
		
		result.setVideoUrlTV(bConfig("familyhas.video_url_TV"));
		return result;
	}
	
	/**
	 * 根据规则取商品的广告图信息
	 * @param productCode
	 * @return
	 */
	private static String getPcProductAdpic(String productCode) {
		String ret = "";
		//取得商品广告图信息
		MDataMap pcAdpicListMapParam = new MDataMap();
		pcAdpicListMapParam.put("product_code", productCode);
		pcAdpicListMapParam.put("now", DateUtil.getSysDateTimeString());
		List<MDataMap> pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "start_date desc",
				"product_code=:product_code  and (sku_code='' or sku_code is null) and start_date <=:now and end_date >=:now", pcAdpicListMapParam, -1, -1);
		if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
			ret = pcAdpicListMap.get(0).get("pic_url");
		} else {
			pcAdpicListMapParam = new MDataMap();
			pcAdpicListMapParam.put("product_code", productCode);
			pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "",
					"product_code=:product_code  and (sku_code='' or sku_code is null) and (start_date='' or start_date is null) and (end_date='' or end_date is null)", pcAdpicListMapParam, -1, -1);
			if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
				ret = pcAdpicListMap.get(0).get("pic_url");
			}
		}
		return ret;
	}
}
