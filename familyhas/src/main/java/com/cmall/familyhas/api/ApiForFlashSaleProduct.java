package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.FlashSaleInput;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.result.AdvertisementImg;
import com.cmall.familyhas.api.result.FlashSaleProduct;
import com.cmall.familyhas.api.result.FlashSaleProductListResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.enumer.EImageWidthSuffix;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadFlashSaleProduct;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelFlashSaleProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusProductModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MFileItem;
import com.srnpr.zapweb.webmodel.MOauthInfo;
import com.srnpr.zapweb.websupport.OauthSupport;

/**
 * @Date 2019-05-10 11:43:00
 * @author Angel Joy
 * @version 1.0
 * @Desc 秒杀列表接口
 * @Edit 2019-09-10 13:48:20
 * @author Angel Joy
 * @EditDesc 小程序预约需要处理按钮，因此小程序或是微信商城需要入参openId字段以及source来源：xcx:小程序，wxshop，微信商城，ios:苹果APP ,android ：安卓APP
 */
public class ApiForFlashSaleProduct extends RootApiForVersion<FlashSaleProductListResult, FlashSaleInput> {

	@Override
	public FlashSaleProductListResult Process(FlashSaleInput inputParam, MDataMap mRequestMap) {
		String memberCode = "";
		String openId = inputParam.getOpenId();
		String source = inputParam.getSource();
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		String uniqid =  StringUtils.trimToEmpty(this.getApiClient().get("uniqid"));
		
		PlusSupportEvent plusEvent = new PlusSupportEvent();
		PlusSupportStock plusStock = new PlusSupportStock();
		LoadProductInfo loadProductInfo = new LoadProductInfo();
		if (StringUtils.isNotEmpty(mRequestMap.get("api_token"))) {

			MOauthInfo oauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token"));

			if (oauthInfo != null) {
				memberCode = oauthInfo.getUserCode();
			}

		}
		FlashSaleProductListResult result = new FlashSaleProductListResult();
		String eventCode = inputParam.getEventCode();
		LoadFlashSaleProduct load = new LoadFlashSaleProduct();
		PlusProductModelQuery query = new PlusProductModelQuery();
		query.setCode(eventCode);
		PlusModelFlashSaleProduct modelFlashSaleProduce = load.upInfoByCode(query);
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		if (modelFlashSaleProduce != null) {
			maps = modelFlashSaleProduce.getItems();
		}
		LoadEventInfo loadEvent = new LoadEventInfo();
		PlusModelEventQuery tQuery = new PlusModelEventQuery();
		tQuery.setCode(eventCode);
		PlusModelEventInfo eventInfo = loadEvent.upInfoByCode(tQuery);
		//针对现在进行的秒杀活动，如果为智能排序的话，调达观的推荐接口
		boolean flag = false;
		if (eventInfo == null) {// 异常数据
			result.setResultCode(0);
			result.setResultMessage("数据异常，请联系管理员");
			return result;
		}
		String beginTime = eventInfo.getBeginTime();
		String endTime = eventInfo.getEndTime();
		result.setBeginTime(beginTime);
		result.setEndTime(endTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status = "0";
		try {
			Date begin = sdf.parse(beginTime);
			Date end = sdf.parse(endTime);
			Date nowDate = new Date();
			if (nowDate.after(begin)&&nowDate.before(end)) {// 当前时间晚于开始时间，早于结束时间秒杀已经开始
				status = "1";
				if("449748550002".equals(eventInfo.getSortOrder())) {
					flag = true;
				 }
			}else if(nowDate.after(end)) {//秒杀已经结束
				status = "2";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.setStatus(status);
		PlusSupportProduct psp = new PlusSupportProduct();
		ProductService ps = new ProductService();
		List<FlashSaleProduct> items = new ArrayList<FlashSaleProduct>();
		for (Map<String, Object> map : maps) {
			MDataMap m = new MDataMap(map);
			FlashSaleProduct product = new FlashSaleProduct();
			product.setProductCode(m.get("product_code"));
			product.setProductName(m.get("sku_name"));
			String imgUrl = m.get("mainpic_url");
			PicInfo imgInfo = ps.getPicInfoOprBig(400,imgUrl);//压缩主图
			product.setProductUrl(imgInfo.getPicNewUrl());
			product.setSkuCode(m.get("sku_code"));
			PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(m.get("sku_code"), memberCode, "", 1);
			
			if (status.equals("1")) {// 已经开始秒杀。实时计算当前商品售价
				if("4497471600450002".equals(m.get("favorable_price_type"))) {
					String pr = m.get("profit_rate").toString();
					BigDecimal newFavorablePrice = psp.computePriceByGross(m.get("sku_code").toString(),pr);
					newFavorablePrice = MoneyHelper.roundHalfUp(newFavorablePrice);
					product.setKillPrice(newFavorablePrice);
				}
				else {
					product.setKillPrice(new BigDecimal(m.get("favorable_price")));

				}
				product.setOrgSellPrice(skuInfo.getSkuPrice());
				Button button = new Button();
				button.setButtonCode("4497477800080020");
				button.setButtonTitle("去抢购");
				product.getButtons().add(button);
				//562添加活动商品进度
				MDataMap stockParams = new MDataMap();
				stockParams.put("event_code", m.get("event_code"));
				stockParams.put("product_code", m.get("product_code"));
				List<Map<String, Object>> itemList = DbUp.upTable("sc_event_item_product").dataSqlList("select p.item_code, p.sku_code,p.sales_num,p.rate_of_progress from sc_event_item_product p where p.event_code = :event_code and p.flag_enable = 1 and "
						+ "p.product_code = :product_code", stockParams);
				long limitStock=0;
				long actualStock = 0;
				int allSaleNum = 0;
				int num = 0;
				int allProgress = 0;
				boolean showFlag = false;
				for(Map<String, Object> item : itemList) {				
					 long sublimitStock = plusEvent.upEventItemSkuStock(MapUtils.getString(item, "item_code", ""));
					 long subactualStock = plusStock.upAllStock(MapUtils.getString(item, "sku_code", ""));
					 num=num+1;
					 allProgress = allProgress +Integer.parseInt((item.get("rate_of_progress")==null||StringUtils.isBlank(item.get("rate_of_progress").toString()))?"0":item.get("rate_of_progress").toString());
					 allSaleNum=allSaleNum + Integer.parseInt(item.get("sales_num").toString());
					 PlusModelSkuInfo subskuInfo = psp.upSkuInfoBySkuCode(item.get("sku_code").toString(), memberCode, "", 1);
					 if(sublimitStock > 0 && subactualStock > 0&&6!=subskuInfo.getBuyStatus()) {
						limitStock=limitStock+sublimitStock;
						actualStock=actualStock+subactualStock;
						showFlag = true;
					}
				}
			/*	PlusModelProductInfo upInfoByCode = loadProductInfo.upInfoByCode(new PlusModelProductQuery(m.get("product_code")));
				if(!"4497153900060002".equals(upInfoByCode.getProductStatus())) {
					showFlag = false;
				}*/


				long minStore = Math.min(limitStock, actualStock);
				int saleNumbers = allSaleNum -Integer.valueOf(minStore+"");
				int averageProgress = (num==0?100:(allProgress/num));
				int rateOfProgress =(allSaleNum==0||!showFlag)?100:((int) (averageProgress!=100?((Double.parseDouble(saleNumbers+"")/Double.parseDouble(allSaleNum+""))*(100-averageProgress)+averageProgress):100));
				rateOfProgress= rateOfProgress>100?100:rateOfProgress;	
				product.setRateOfProgress(rateOfProgress+"");
				
			}else {//还未开始秒杀，秒杀价格设置为设置价格，划线价格为实际售价
				if("4497471600450002".equals(m.get("favorable_price_type"))) {
					String pr = m.get("profit_rate").toString();
					BigDecimal newFavorablePrice = psp.computePriceByGross(m.get("sku_code").toString(),pr);
					newFavorablePrice = MoneyHelper.roundHalfUp(newFavorablePrice);
					product.setKillPrice(newFavorablePrice);
				}else {
					product.setKillPrice(new BigDecimal(m.get("favorable_price")));
				}
				product.setOrgSellPrice(skuInfo.getSellPrice());
				Button button = new Button();
				if(StringUtils.isEmpty(openId)||StringUtils.isEmpty(source)) {//来源非小程序
					button.setButtonCode("4497477800080021");
					button.setButtonTitle("提醒我");
				}else if("xcx".equals(source)&& !StringUtils.isEmpty(openId)) {//来源小程序需要校验用户是否已经订阅通知
					Integer count = DbUp.upTable("nc_push_news_subscribe").count("event_code",eventCode,"product_code",m.get("product_code"),"open_id",openId,"source",source);
					if(count > 0) {//订阅
						button.setButtonCode("4497477800080023");
						button.setButtonTitle("取消预约");
					}else {
						button.setButtonCode("4497477800080021");
						button.setButtonTitle("提醒我");
					}
				}
				product.getButtons().add(button);
			}
			List<String> tags = new ArrayList<String>();//ps.getTagListByProductCode(m.get("product_code"), memberCode);
			tags.add("秒杀");
			//524:添加返回商品分类标签
			String smallSellerCode = skuInfo.getSmallSellerCode();
			String st="";
			if("SI2003".equals(smallSellerCode)) {
				st="4497478100050000";
			}
			else {
				LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
				PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(smallSellerCode));
				if(sellerInfo != null){
					st = sellerInfo.getUc_seller_type();
				}
			}
			//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
			product.setTagList(tags);
			//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
			Map<String,String> productTypeMap = WebHelper.getAttributeProductType(st);
			
			if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
				//List<TagInfo> tagInfoList = ps.getProductTagInfoList(product.getProductCode(), "");
				List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
				tagInfoList.add(new TagInfo("秒杀", TagInfo.Style.Normal));
				product.setTagInfoList(tagInfoList);
			}
			
			product.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
			
			//5.5.8增加所有sku实际库存 前端用于库存提示
			int allSkuRealStock = new PlusSupportStock().upAllStockForProduct(m.get("product_code"));
			product.setAllSkuRealStock(allSkuRealStock);
			
			
			items.add(product);
		}
		if(flag) {
			HomeColumnService hcService = new HomeColumnService();
			if(getFlagLogin()) {
				List<String> killProducts = hcService.getDGRecommendSecKillProducts(getOauthInfo().getUserCode(),uniqid);
				List<FlashSaleProduct> newItems = new ArrayList<FlashSaleProduct>();
				//智能推荐排序逻辑处理
				List<String> temProducts = new ArrayList<>();
				for (FlashSaleProduct flashSaleProduct : items) {
					temProducts.add(flashSaleProduct.getProductCode());
				}
				killProducts.retainAll(temProducts);
				if(killProducts.size()>0) {
					List<String> list = hcService.removeDuplicateContain(killProducts);
					for (String pCode : list) {
						int index = temProducts.indexOf(pCode);
						newItems.add(items.get(index));
					}
					items.removeAll(newItems);
		            if(items.size()>0) {
		            	newItems.addAll(items);
		            }
					items = newItems;	
				}	
			}
		}
		result.setItems(items);
		//获取秒杀轮播数据
		List<AdvertisementImg> lunBoList = getLunBoList(inputParam.getImgWidth());
		result.setLunBoList(lunBoList);
		return result;
	}
	
	private List<AdvertisementImg> getLunBoList(int imgWidth) {
		//秒杀列表轮播图
		String sSql = " SELECT images,urls FROM systemcenter.sc_sales_image WHERE activity_status='4497473400020001' AND show_type='449748390003' AND start_time <= now() AND end_time >= now() ORDER BY start_time ";
		Map<String, Object> dataSqlOne = DbUp.upTable("sc_sales_image").dataSqlOne(sSql, new MDataMap());//因为时间不能冲突，所以一个时间点只有一条数据符合条件
		List<AdvertisementImg> imgList = new ArrayList<AdvertisementImg>();
		if(null != dataSqlOne) {
			String images = String.valueOf(dataSqlOne.get("images"));
			String urls = String.valueOf(dataSqlOne.get("urls"));
			String[] imgArr = images.split("\\|");
			String[] urlArr = urls.split("\\|");			
			//获取压缩图片
			Map<String, MFileItem> compressImage = new PlusVeryImage().compressImage(false, imgWidth, images, "", EImageWidthSuffix.SALESIMAGES);			
			for (int i = 0; i < imgArr.length; i++) {
				AdvertisementImg imgModel = new AdvertisementImg();
				MFileItem t_item = compressImage.get(imgArr[i]);
				if(null != t_item) {
					imgModel.setImgUrl(t_item.getFileUrl());
				} else {
					imgModel.setImgUrl(imgArr[i]);
				}
				{
					//赋值图片宽高
					if(StringUtils.isNotBlank(imgArr[i])) {
						PlusVeryImage pvi = new PlusVeryImage();
						Map<String, MFileItem> map = pvi.upImageZoom(imgArr[i], 0);
						if(!map.isEmpty()){
							MFileItem item = map.values().iterator().next();
							imgModel.setPicWidth(item.getWidth());
							imgModel.setPicHeight(item.getHeight());
						}
					}
				
				}
				imgModel.setForwardUrl(StringUtils.replace(urlArr[i], "&amp;", "&").trim());
				if(urlArr[i].contains("http")) {
					imgModel.setSkipType("4497471600020001");
				}else {
					imgModel.setSkipType("4497471600020004");
				}
				imgList.add(imgModel);
			}
		}
		return imgList;
	}
   
	
}
