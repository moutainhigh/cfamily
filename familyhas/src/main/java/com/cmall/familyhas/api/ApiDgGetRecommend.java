package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmall.familyhas.api.input.ApiDgGetRecommendInput;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiDgGetRecommendResult;
import com.cmall.familyhas.util.ProductCodeCopyLoader;
import com.cmall.productcenter.model.SolrData;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.HttpClientSupport;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiDgGetRecommend extends RootApiForVersion<ApiDgGetRecommendResult, ApiDgGetRecommendInput> {

	private final String NAV_TYPE_DEFAULT = "4497480100040001";//默认
	private final String NAV_TYPE_CATEGORY = "4497480100040002";//分类设置
	private final String NAV_TYPE_KEYWORD = "4497480100040003";//关键字
	private final int PAGE_NUM = 10;//达观推荐分页请求限定数量（ps:原是一次请求65）
	private final int TOTAL_PAGE = 6;//总页数
	
	
	@Override
	public ApiDgGetRecommendResult Process(ApiDgGetRecommendInput inputParam, MDataMap mRequestMap) {
		
		Map<String,Object> ifIgnoreMap = DbUp.upTable("uc_ignore_category").dataSqlOne("SELECT * FROM usercenter.uc_ignore_category limit 1", new MDataMap());
		String ignore = "Y";//默认过滤
		if(ifIgnoreMap != null) {
			ignore = MapUtils.getString(ifIgnoreMap, "if_ignore","Y");
		}
		
		ApiDgGetRecommendResult apiResult = new ApiDgGetRecommendResult();
		String channelId = getChannelId();
		
		if(StringUtils.isNotBlank(inputParam.getNavCode())) {
			inputParam.setNavCode(inputParam.getNavCode().replace("_1", ""));
		}
		//兼容扫码购进入的商品编号推荐
		if(StringUtils.isNotBlank(inputParam.getProductCode())&&inputParam.getProductCode().contains("IC_SMG_")) {
			inputParam.setProductCode(StringUtils.substring(inputParam.getProductCode(), 7));
		}
		
		// 判断配置参数是否要屏蔽
		if(!StringUtils.contains(bConfig("familyhas.recommend_get_config"), "dg_get")){
			return apiResult;
		}
		
		MDataMap appClient = getApiClient();
//		if(appClient != null && "weapphjy".equalsIgnoreCase(appClient.get("os"))) {
//			// 屏蔽小程序的猜你喜欢接口
//			return apiResult;
//		}
		String currentVersion = "";
		String appDevice="";
		if(appClient != null) {
			currentVersion = appClient.get("app_vision")==null?"":appClient.get("app_vision");
			appDevice = appClient.get("os")==null?"":appClient.get("os");
		}
		
		String userCode = "";
		if(getOauthInfo() != null) {
			userCode = getOauthInfo().getUserCode();
		}
		
		//根据openFlag区分应该返回前端多少条数据
		if("maybelove".equals(inputParam.getOperFlag())) {
			inputParam.setPageSize(PAGE_NUM);
		}else if("shopcart".equals(inputParam.getOperFlag())) {
			inputParam.setPageSize(PAGE_NUM);
		}else if("productdetail".equals(inputParam.getOperFlag())) {
			inputParam.setPageSize(24);
		}else if("paysuccess".equals(inputParam.getOperFlag())) {
			inputParam.setPageSize(PAGE_NUM);
		}else if("secondKillListVC".equals(inputParam.getOperFlag())){
			inputParam.setPageSize(PAGE_NUM);
		}else if("MessageVC".equals(inputParam.getOperFlag())){
			inputParam.setPageSize(PAGE_NUM);
		}else {
			//android 556之前的版本对个人中心没有做分页处理，这里做版本兼容一下
			if("android".equalsIgnoreCase(appDevice)&&AppVersionUtils.compareTo(currentVersion, "5.5.60") < 0&&"myhome".equals(inputParam.getOperFlag())) {
				inputParam.setPageSize(65);
			}else {
				inputParam.setPageSize(PAGE_NUM);
			}	
		}
		
		boolean testData = "Y".equals(bConfig("familyhas.daguan_recommend_testdata"));
		
		try {
			MDataMap navMap = null;
			if(!"".equals(inputParam.getNavCode())) {
				navMap = DbUp.upTable("fh_apphome_nav").one("nav_code", inputParam.getNavCode());
			}
			if(testData) {
				//本地测试模式，给测试环境提供接口测试使用
				getTestData(apiResult, inputParam.getPageSize(), channelId,ignore);
			} else if(navMap == null || NAV_TYPE_DEFAULT.equals(navMap.get("class_type"))) {//默认
				String url = "";
				//达观分页规则请求有变化
				inputParam.setPageIndex(inputParam.getPageIndex()*inputParam.getPageSize());
				if("1".equals(inputParam.getRecommendType())||"3".equals(inputParam.getRecommendType())) {//个性化推荐
					url = bConfig("productcenter.dg_personal_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
					url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
				}else if("2".equals(inputParam.getRecommendType())) {//相关推荐
					url = bConfig("productcenter.dg_relate_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
					url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&itemid=" + inputParam.getProductCode() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
				}else if("3".equals(inputParam.getRecommendType())) {//热门推荐
					url = bConfig("productcenter.dg_hot_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
					url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
				}else if("4".equals(inputParam.getRecommendType())) {//看了又看
					if(StringUtils.isBlank(inputParam.getProductCode())) {
						//为空继续之前推荐
						url = bConfig("productcenter.dg_personal_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
						url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
					}else {
						//不为空，走相关推荐
						url = bConfig("productcenter.dg_relate_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
						url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&itemid=" + inputParam.getProductCode() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
					}	
				}if("5".equals(inputParam.getRecommendType())) {//LD短信支付成功页面 商品推荐
					String phone = inputParam.getPhone();
					if(StringUtils.isNotEmpty(phone)) {
						MDataMap loginInfo = DbUp.upTable("mc_login_info").one("login_name",phone,"manage_code","SI2003");
						if(loginInfo != null && !loginInfo.isEmpty()) {
							userCode = loginInfo.get("member_code");
						}
					}
					if(StringUtils.isNotEmpty(userCode)) {
						url = bConfig("productcenter.dg_personal_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
						url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
					}else {
						url = bConfig("productcenter.dg_hot_recommend_url") + bConfig("productcenter.dg_app_name") + "?appid=" + bConfig("productcenter.dg_app_id");
						url += "&userid=" + userCode + "&imei=" + inputParam.getImei() + "&cid=" + inputParam.getcId() + "&cnt=" + inputParam.getPageSize()+ "&scene_type=" + inputParam.getOperFlag()+ "&start=" + inputParam.getPageIndex();
					}
				}
				
				if(null != navMap && NAV_TYPE_DEFAULT.equals(navMap.get("class_type")) && "449748560002".equals(navMap.get("category_limit")) && StringUtils.isNotEmpty(navMap.get("category_codes"))) {
					String exclude_cateid = "";
					String cateid = "";
			        if("449747430023".equals(channelId) && "Y".equals(ignore)) {//小程序以及需要过滤相关品类
			        	//小程序需要屏蔽违规的分类商品数据
						List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_program_del_category").dataSqlList("select * from uc_program_del_category", null);
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							List<String> temList = new ArrayList<>();
							for (Map<String, Object> map : dataSqlList) {
								temList.add(map.get("category_code").toString());
							}
							exclude_cateid = StringUtils.join(temList, ",");
						}
			        }
			        
			        String categoryCodes = navMap.get("category_codes");
			        if(categoryCodes.contains("449716040081") && "449747430023".equals(channelId)) {
			        	List<String> tempList = new ArrayList<String>();
			        	for(String categoryCode : categoryCodes.split(",")) {
			        		if(!"449716040081".equals(categoryCode)) tempList.add(categoryCode);
			        	}
			        	if(!tempList.isEmpty()) cateid = StringUtils.join(tempList, ",");
			        }else {
			        	cateid = categoryCodes;
			        }
			        
			        url += "&exclude_cateid="+exclude_cateid + "&cateid="+cateid;
				}
				
				String os = StringUtils.trimToEmpty(getApiClient().get("os"));
				String app_vision = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
				
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("api_type", "dg_get");
				mDataMap.put("app_platform", os);
				mDataMap.put("app_version", app_vision);
				mDataMap.put("member_code", userCode);
				mDataMap.put("request_time", FormatHelper.upDateTime());
				mDataMap.put("response_time", "");
				mDataMap.put("fail_content", "");
				
				String resultChar = HttpClientSupport.doGetDg(url);
				mDataMap.put("response_time", FormatHelper.upDateTime());
				
				JSONObject json = JSONObject.fromObject(resultChar);
				String status = json.getString("status");
				if(!"FAIL".equals(status)) {
					JSONArray array = json.getJSONArray("recdata");
					String requestId = json.getString("request_id");
					
					mDataMap.put("data_size", array.size()+"");
					
					List<ProductMaybeLove> loveList = new ArrayList<>();
					for(int i = 0; i < array.size(); i ++) {
						JSONObject object = array.getJSONObject(i);
						String itemId = object.getString("itemid");
						ProductMaybeLove myBeLove = getMayBeLove(itemId, requestId, inputParam.getIsPurchase(),channelId,ignore);
						if(myBeLove != null) {
							loveList.add(myBeLove);
						}
					}
					
					//为适应前端app展示，当奇数时去掉最后一条
					//前端app显示是整行，当奇数时下一页会从下一行开始，安卓控件不能适应，故后端做相应兼容
					if(loveList.size() % 2 == 1) {
						loveList.remove(loveList.size() - 1);
					}
					
					apiResult.setProductMaybeLove(loveList);
					if("productdetail".equals(inputParam.getOperFlag())||("android".equalsIgnoreCase(appDevice)&&AppVersionUtils.compareTo(currentVersion, "5.5.60") < 0&&"myhome".equals(inputParam.getOperFlag()))) {
						//保持不变
						apiResult.setPagination(loveList.size() > 0 ? 1 : 0);
					}else {
						//做分页处理,固定总页数
						apiResult.setPagination(TOTAL_PAGE);
					}
				}else {
					apiResult.setResultCode(0);
					apiResult.setResultMessage("数据异常,请联系管理员!");
					mDataMap.put("fail_content", resultChar);
				}
				
				// 记录达观接口调用日志
				if(StringUtils.isNotBlank(mDataMap.get("fail_content"))) {
					DbUp.upTable("lc_dg_api_log").dataInsert(mDataMap);
				}
			}else if(NAV_TYPE_CATEGORY.equals(navMap.get("class_type")) || NAV_TYPE_KEYWORD.equals(navMap.get("class_type"))) {//分类 or 关键字
				getSolrMayBeLove(inputParam, navMap, apiResult,channelId,ignore);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//新增拼团标识
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(appVersion.compareTo("5.4.0")>=0){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
			List<ProductMaybeLove> items = apiResult.getProductMaybeLove();
			for(ProductMaybeLove itemEntity : items){
				String productCode = itemEntity.getProcuctCode();
				//根据商品编号查询商品所参与的活动
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(productCode);
				skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
				skuQuery.setChannelId(channelId);
				Map<String,PlusModelSkuInfo> map = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
				PlusModelSkuInfo skuInfo = map.get(productCode);
				if("4497472600010024".equals(skuInfo.getEventType())){//拼团单
					itemEntity.setProductType("4497472000050001");//是拼团单
					itemEntity.setGroupBuying("4497472600010024");
					itemEntity.setGroupBuyingPrice(skuInfo.getGroupBuyingPrice());//设置拼团价
					itemEntity.setSkuPrice(skuInfo.getSkuPrice());//拼团前的价格
					String eventCode = skuInfo.getEventCode();
					PlusModelEventInfo eventInfo = new PlusSupportEvent().upEventInfoByCode(eventCode);
					String collagePersonCount = eventInfo.getCollagePersonCount();//拼团人数
					itemEntity.setCollagePersonCount(collagePersonCount);
				}else{
					itemEntity.setProductType("4497472000050002");//不是拼团单
				}
			}
		}
		//562版本对于商品列表标签做版本兼容处理
		if(appVersion.compareTo("5.6.2")<0){
			for(ProductMaybeLove info:apiResult.getProductMaybeLove()){
				Iterator<PlusModelProductLabel> iter = info.getLabelsInfo().iterator();
				while (iter.hasNext()) {
					PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
					if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
						iter.remove();
					}
				}
			}
		}
		return apiResult;
	}
	
	private void getSolrMayBeLove(ApiDgGetRecommendInput inputParam, MDataMap navMap, ApiDgGetRecommendResult apiResult,String channelId,String ignore) {
		int pageSize = inputParam.getPageSize() <= 0 ? 20 :  inputParam.getPageSize();
		int pageIndex = inputParam.getPageIndex() <= 0 ? 1 : inputParam.getPageIndex();
		int iStart = (pageIndex - 1) * pageSize;
		
		MDataMap dataMap = new MDataMap();
		dataMap.put("sortType","0");
		dataMap.put("sortFlag","2");
		dataMap.put("pageNo", iStart + "");
		dataMap.put("pageSize", pageSize + "");
		dataMap.put("sellercode","SI2003");
		
		// 限定商品分类
		if(NAV_TYPE_CATEGORY.equalsIgnoreCase(navMap.get("class_type")) && StringUtils.isNotBlank(navMap.get("classify"))) {
			String catName = (String)DbUp.upTable("uc_sellercategory").dataGet("category_name", "", new MDataMap("category_code",navMap.get("classify"),"seller_code","SI2003"));
			dataMap.put("keyWord", catName);
			dataMap.put("category","category");
		}else {
			dataMap.put("keyWord", navMap.get("key_word"));
			dataMap.put("key","key");
		}
		
		String respBody = null;
		List<SolrData> list = new ArrayList<SolrData>();
		try {
			respBody = WebClientSupport.upPost(TopUp.upConfig("productcenter.webclienturlselect"), dataMap);
			list = JSON.parseObject(respBody,new TypeReference<List<SolrData>>(){});
			if(list == null || list.isEmpty()) list = new ArrayList<SolrData>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<ProductMaybeLove> loveList = new ArrayList<ProductMaybeLove>();
		for(int i = 0; i < list.size(); i ++) {
			ProductMaybeLove maybeLove = getMayBeLove(list.get(i).getK1(), "", inputParam.getIsPurchase(),channelId,ignore);
			if(maybeLove != null) {
				loveList.add(maybeLove);
			}
		}
		//为适应前端app展示，当奇数时去掉最后一条
		//前端app显示是整行，当奇数时下一页会从下一行开始，安卓控件不能适应，故后端做相应兼容
		if(loveList.size() % 2 == 1) {
			loveList.remove(loveList.size() - 1);
		}
		
		apiResult.setProductMaybeLove(loveList);
		apiResult.setPagination(list.isEmpty() ? 0 : totalPage((int)(list.get(0).getCounts()), pageSize));
	}
	
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
	
	public int totalPage(int totalSize,int pageSize){
		int pageCount = totalSize / pageSize;
		return (totalSize % pageSize) > 0 ? (pageCount + 1) : pageCount;
	}
	
	private void getTestData(ApiDgGetRecommendResult apiResult, int pageSize, String channelId,String ignore) {
		//从订单
		List<MDataMap> productMapList = DbUp.upTable("pc_productsales_everyday").query("DISTINCT product_code", "zid desc", "", new MDataMap(), 0, 16);
		for(int i = 0; i < productMapList.size(); i ++) {
			ProductMaybeLove maybeLove = getMayBeLove(productMapList.get(i).get("product_code"), "", 1,channelId,ignore);
			if(maybeLove != null) {
				apiResult.getProductMaybeLove().add(maybeLove);
			}
		}
		apiResult.setPagination(1);
	}
}
