package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.api.input.ApiRecProductInfoInput;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiRecProductInfoResult;
import com.cmall.familyhas.util.ProductCodeCopyLoader;
import com.cmall.groupcenter.behavior.api.ApiGetBfdRecResultInfo;
import com.cmall.groupcenter.behavior.common.OperFlagEnum;
import com.cmall.groupcenter.behavior.model.BfdRecProductInfo;
import com.cmall.groupcenter.behavior.model.BfdRecResultInfo;
import com.cmall.groupcenter.behavior.response.BfdRecResultResponse;
import com.cmall.productcenter.model.SolrData;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.kvsupport.KvFactory;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webdo.WebTemp;

/**
 * 推荐商品信息
 */
public class ApiRecProductInfo extends RootApiForVersion<ApiRecProductInfoResult, ApiRecProductInfoInput> {

	/** 推荐商品限定分类 */
	static final String TYPE_CLASSIFY = "4497480100040002";
	/** 推荐商品限定关键字 */
	static final String TYPE_KEYWORD = "4497480100040003";
	
	/** 缓存关键字 */
	static final String REDIS_KEY_PREFIX = "xs-bfd-";
	
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	public ApiRecProductInfoResult Process(ApiRecProductInfoInput inputParam, MDataMap mRequestMap) {
		ApiRecProductInfoResult recProductInfoResult = new ApiRecProductInfoResult();
		
		//操作位置标识标识 
		OperFlagEnum operFlagEnum = OperFlagEnum.getByName(inputParam.getOperFlag());
		boolean maybelove_flag = getZwDefineByName(FamilyConfig.MAYBELOVE_DISPLAY);
		boolean bfd_flag = getZwDefineByName(FamilyConfig.BFD_DISPLAY);
		//String channels = ","+StringUtils.trimToEmpty(bConfig("groupcenter.Bfd_channel")).toLowerCase()+",";
		
		MDataMap navMap = null;
		if(StringUtils.isNotBlank(inputParam.getNavCode())){
			navMap = DbUp.upTable("fh_apphome_nav").one("nav_code", inputParam.getNavCode());
		}
		
		// 避免空指针
		if(navMap == null) navMap = new MDataMap();
		
		// 限定类别是关键字并且关键字不为空则使用搜索接口
		if(TYPE_KEYWORD.equalsIgnoreCase(navMap.get("class_type")) && StringUtils.isNotBlank(navMap.get("key_word"))){
			fillDataFromSeach(recProductInfoResult, inputParam, navMap);
		}else if(TYPE_CLASSIFY.equalsIgnoreCase(navMap.get("class_type")) && StringUtils.isNotBlank(navMap.get("classify"))){
			fillDataFromSeach(recProductInfoResult, inputParam, navMap);
		}else if(bfd_flag && operFlagEnum != null){ //&& channels.contains(","+StringUtils.trimToEmpty(inputParam.getChannelNO()).toLowerCase()+",")){
			// 不是关键字就走百分点的接口
			fillDataFromBfd(recProductInfoResult, inputParam, navMap);
		}
		
		// 请求不到数据时取运营手动配的兜底数据
		if(maybelove_flag && recProductInfoResult.getProductMaybeLove().isEmpty()){
			fillDataFromDb(recProductInfoResult, inputParam);
		}
		
		return recProductInfoResult;
	}
	
	/**
	 * 使用搜索接口的数据
	 * @param recProductInfoResult
	 * @param inputParam
	 * @param navMap
	 */
	private void fillDataFromSeach(ApiRecProductInfoResult recProductInfoResult, ApiRecProductInfoInput inputParam, MDataMap navMap){
		int pageSize = inputParam.getPageSize() <= 0 ? 20 :  inputParam.getPageSize();
		int pageIndex = inputParam.getPageIndex() <= 0 ? 1 : inputParam.getPageIndex();
		int iStart = (pageIndex - 1) * pageSize;
		
		MDataMap dataMap = new MDataMap();
		dataMap.put("sortType","0");
		dataMap.put("sortFlag","2");
		dataMap.put("pageNo", iStart+"");
		dataMap.put("pageSize", pageSize+"");
		dataMap.put("sellercode","SI2003");
		
		
		// 限定商品分类
		if(TYPE_CLASSIFY.equalsIgnoreCase(navMap.get("class_type")) && StringUtils.isNotBlank(navMap.get("classify"))){
			String catName = (String)DbUp.upTable("uc_sellercategory").dataGet("category_name", "", new MDataMap("category_code",navMap.get("classify"),"seller_code","SI2003"));
			dataMap.put("keyWord", catName);
			dataMap.put("category","category");
		}else{
			dataMap.put("keyWord", navMap.get("key_word"));
			dataMap.put("key","key");
		}
		
		String respBody = null;
		List<SolrData> list = null;
		try {
			respBody = WebClientSupport.upPost(TopUp.upConfig("productcenter.webclienturlselect"), dataMap);
			list = JSON.parseObject(respBody,new TypeReference<List<SolrData>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(list == null || list.isEmpty()) return;
		
		ProductMaybeLove productMaybeLove = null;
		List<ProductMaybeLove>  productMaybeLoves = new ArrayList<ProductMaybeLove>();//暂存非LD商品
		for(int i = 0; i < list.size(); i++){
			productMaybeLove = getProductMaybeLoveInfo(list.get(i).getK1(),"",inputParam.getIsPurchase());
			if(productMaybeLove != null){
				if("SI2003".equals(productMaybeLove.getSmallSellerCode())) {
					recProductInfoResult.getProductMaybeLove().add(productMaybeLove);
				}else {
					productMaybeLoves.add(productMaybeLove);
				}
			}
		}
		if(productMaybeLoves.size()>0) {
			recProductInfoResult.getProductMaybeLove().addAll(productMaybeLoves);
		}
		
		// 如果是奇数则不返回最后一条，固定返回客户端偶数位的数据，避免客户端排版问题
		if((recProductInfoResult.getProductMaybeLove().size() % 2) == 1){
			if(recProductInfoResult.getProductMaybeLove().size() > 1){
				recProductInfoResult.setProductMaybeLove(recProductInfoResult.getProductMaybeLove().subList(0, recProductInfoResult.getProductMaybeLove().size() - 1));
			}
		}
		
		recProductInfoResult.setPagination(list.isEmpty() ? 0 : totalPage((int)(list.get(0).getCounts()), pageSize));
	}
	
	/**
	 * 使用百分点推荐数据
	 * @param recProductInfoResult
	 * @param inputParam
	 * @param navMap
	 */
	private void fillDataFromBfd(ApiRecProductInfoResult recProductInfoResult, ApiRecProductInfoInput inputParam, MDataMap navMap){
		String operFlag = "android_"+inputParam.getOperFlag();
		String appKey = bConfig("groupcenter.android");
		
		/*根据渠道号区分，android和ios，百分点要区分运营*/
		if("appStore".equalsIgnoreCase(inputParam.getChannelNO())){
			operFlag = "ios_"+inputParam.getOperFlag();
			appKey = bConfig("groupcenter.ios");
		}
		
		// 限定商品分类条件使用特定的flag
		if(TYPE_CLASSIFY.equalsIgnoreCase(navMap.get("class_type")) && StringUtils.isNotBlank(navMap.get("classify"))){
			operFlag = "appStore".equalsIgnoreCase(inputParam.getChannelNO()) ? "ios_withparam" : "android_withparam";
		}

		//int pageSize = inputParam.getPageSize() <= 0 ? 20 :  inputParam.getPageSize();
		//int pageIndex = inputParam.getPageIndex() <= 0 ? 1 : inputParam.getPageIndex();
		//int iStart = (pageIndex - 1) * pageSize;
		
		int pageSize = 30;
		int pageIndex = 1;
		
		// 商品详情显示24个商品
		if(OperFlagEnum.productdetail.toString().equalsIgnoreCase(inputParam.getOperFlag())){
			pageSize = 24;
		}
		
		// 获取百分点数据，取第一页时忽略缓存数据
		BfdRecResultResponse resp = getBfdRecResultInfo(pageIndex <= 1,inputParam.getUid(),inputParam.getIid(),operFlag,appKey,navMap);
		
		BfdRecResultInfo infos = null;
		if(!resp.getRecResultInfos().isEmpty()){
			infos = resp.getRecResultInfos().get(0);
		}
		
		List<ProductMaybeLove> list = new ArrayList<ProductMaybeLove>();
		if(infos != null && infos.getRecProductInfoList() != null){
			// 先过滤出有效的商品
			ProductMaybeLove productMaybeLove;
			List<ProductMaybeLove>  productMaybeLoves = new ArrayList<ProductMaybeLove>();//暂存非LD商品
			for(BfdRecProductInfo item : infos.getRecProductInfoList()){
				productMaybeLove = getProductMaybeLoveInfo(item.getProductCode(), infos.getOpenDS(),inputParam.getIsPurchase());
				if(productMaybeLove != null){
					if("SI2003".equals(productMaybeLove.getSmallSellerCode())) {
						list.add(productMaybeLove);
					}else {
						productMaybeLoves.add(productMaybeLove);
					}
				}
				
				// 只取指定数量的商品
				if(list.size() >= pageSize){
					break;
				}
			}
			if(productMaybeLoves.size()>0) {
				for(ProductMaybeLove  maybeLove :  productMaybeLoves) {
					if(list.size() >= pageSize){
						break;
					}
					list.add(maybeLove);
				}
			}
			
			recProductInfoResult.getProductMaybeLove().addAll(list);
		}
		
		// 固定返回1页，不再做分页
		recProductInfoResult.setPagination(1);
	}
	
	/**
	 * 从数据库中取运营手动配置的数据
	 * @param recProductInfoResult
	 * @param inputParam
	 */
	private void fillDataFromDb(ApiRecProductInfoResult recProductInfoResult,ApiRecProductInfoInput inputParam){
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("seller_code", "SI2003");
		int pageSize = inputParam.getPageSize() <= 0 ? 20 :  inputParam.getPageSize();
		int pageIndex = inputParam.getPageIndex() <= 0 ? 1 : inputParam.getPageIndex();
		int iStart = (pageIndex - 1) * pageSize;
		
		String sWhere ="STR_TO_DATE(start_time,'%Y-%m-%d %H:%i:%s')<NOW() AND STR_TO_DATE(end_time,'%Y-%m-%d %H:%i:%s')>NOW() and product_status='4497153900060002' and seller_code=:seller_code";
		List<MDataMap> mayBeLoveList = DbUp.upTable("v_maybe_love_product").query("product_code", "position desc", sWhere, mWhereMap, iStart, pageSize);
		ProductMaybeLove productMaybeLove;
		List<ProductMaybeLove>  productMaybeLoves = new ArrayList<ProductMaybeLove>();//暂存非LD商品
		for(MDataMap mDataMap : mayBeLoveList){
			if(mDataMap != null){
				productMaybeLove = getProductMaybeLoveInfo(mDataMap.get("product_code"),"",inputParam.getIsPurchase());
				if(productMaybeLove != null){
					if("SI2003".equals(productMaybeLove.getSmallSellerCode())) {
						recProductInfoResult.getProductMaybeLove().add(productMaybeLove);
					}else {
						productMaybeLoves.add(productMaybeLove);
					}
				}
			}
		}
		if(productMaybeLoves.size()>0) {
			recProductInfoResult.getProductMaybeLove().addAll(productMaybeLoves);
		}
		
		int count = DbUp.upTable("v_maybe_love_product").dataCount(sWhere, mWhereMap);
		recProductInfoResult.setPagination(totalPage(count, pageSize));
	}
	
	/**
	 * 请求百分点获取推荐数据，如果启用了缓存则优先使用缓存数据
	 * @param refresh 是否强制刷新缓存
	 * @param uid
	 * @param iid
	 * @param operFlag
	 * @param appKey
	 * @return
	 */
	private BfdRecResultResponse getBfdRecResultInfo(boolean refresh,String uid, String iid, String operFlag, String appKey, MDataMap navMap){
		// 分类编码
		String classify = navMap.get("classify"); 
		String catName = "";
		if(operFlag.contains("withparam") && StringUtils.isNotBlank(classify)){
			// 取分类名称
			catName = (String)DbUp.upTable("uc_sellercategory").dataGet("category_name", "", new MDataMap("category_code",classify,"seller_code","SI2003"));
		}
		
		String navCode = StringUtils.trimToEmpty(navMap.get("nav_code"));
		String key = DigestUtils.md5Hex(uid+"-"+operFlag+"-"+appKey+"-"+navCode+"-"+StringUtils.trimToEmpty(classify));
		KvFactory kv = new KvFactory(REDIS_KEY_PREFIX);
		BfdRecResultResponse resp = null;

		// 是否启用缓存
		boolean cached = "true".equalsIgnoreCase(bConfig("groupcenter.bfd_cached"));
		// 没有UID则不缓存
		if(StringUtils.isBlank(uid)){
			cached = false;
		}
		
		// 如果启用了缓存，优先从缓存读取
		if(!refresh && cached){
			try {
				String content = kv.get(key);
				if(content != null){
					resp = new GsonHelper().fromJson(kv.get(key), new BfdRecResultResponse());
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		
		if(resp == null){
			resp = new ApiGetBfdRecResultInfo().process(uid,iid,operFlag,appKey,catName);
			
			// 把百分点数据缓存下来，解决分页数据重复
			if(!resp.getRecResultInfos().isEmpty() && cached){
				try {
					kv.setex(key, 900, GsonHelper.toJson(resp));
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		return resp;
	}
	
	/**
	 * 取商品基本信息
	 * @param productCode
	 * @param recommendId
	 * @return
	 */
	private ProductMaybeLove getProductMaybeLoveInfo(String productCode,String recommendId,Integer isPurchase){
		// 如果是调编的商品则用新的商品编号替换
		String productCodeNew = ProductCodeCopyLoader.queryCode(productCode);
		if(StringUtils.isNotBlank(productCodeNew)) {
			productCode = productCodeNew;
		}
	
		PlusModelProductInfo plusModelProductInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
		//PlusModelProductInfo plusModelProductInfo = new LoadProductInfo().topInitInfo(new PlusModelProductQuery(productCode));
		//524
		String ssc =plusModelProductInfo.getSmallSellerCode();
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
		
				
		// 忽略下架商品
		if(plusModelProductInfo == null || !"4497153900060002".equals(plusModelProductInfo.getProductStatus())){
			return null;
		}
		
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		skuQuery.setCode(plusModelProductInfo.getProductCode());
		skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
		skuQuery.setIsPurchase(isPurchase);
		
		Map<String, BigDecimal> minSellPriceProductMap = new ProductPriceService().getProductMinPrice(skuQuery);// 获取商品最低销售价格
		
		// 如果商品取不到价格则返回nulll
		if(!minSellPriceProductMap.containsKey(productCode)) return null;
		
		// 修正异常价格
		if(minSellPriceProductMap.get(productCode).intValue() > 1000000){
			minSellPriceProductMap.put(productCode, plusModelProductInfo.getMaxSellPrice());
		}
		
		ProductMaybeLove productMaybeLove = new ProductMaybeLove();
		productMaybeLove.setProductPrice(MoneyHelper.format(minSellPriceProductMap.get(productCode)));// 兼容小数 - Yangcl  
		productMaybeLove.setLabelsList(plusModelProductInfo.getLabelsList());
		productMaybeLove.setLabelsPic(new ProductLabelService().getLabelInfo(plusModelProductInfo.getProductCode()).getListPic());
		productMaybeLove.setFlagTheSea(plusModelProductInfo.getFlagTheSea());
		productMaybeLove.setMainpic_url(plusModelProductInfo.getMainpicUrl());
		productMaybeLove.setProcuctCode(plusModelProductInfo.getProductCode());
		productMaybeLove.setProductNameString(plusModelProductInfo.getProductName());
		productMaybeLove.setMarket_price(plusModelProductInfo.getMarketPrice().toString());
		productMaybeLove.setRecommendId(recommendId);
		productMaybeLove.setSmallSellerCode(plusModelProductInfo.getSmallSellerCode());
		//524:添加商品分类标签
		productMaybeLove.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
		return productMaybeLove;
	}
	
	/**
	 * 根据定义名称获取定义信息
	 * @param defineName
	 * 		定义名称
	 * @return 定义信息
	 */
	public boolean getZwDefineByName(String defineName){
		
		String flagStr = "";
		
		boolean flag = true;
		
		flagStr = WebTemp.upTempDataOne("zw_define","define_note","define_name",defineName,"parent_did",FamilyConfig.ZW_DEFINE_MAYBELOVE_ID);
		
		if(StringUtils.isNotBlank(flagStr)){
			
			flag = Boolean.valueOf(flagStr);
			
		}
		
		return flag;
		
	}
	
	/**
	 * 计算总页数
	 * @param size
	 * 		总数
	 * @param pageSize
	 * 		每页显示行数
	 * @return 计算总页数
	 */
	public int totalPage(int totalSize,int pageSize){
		int pageCount = totalSize / pageSize;
		return (totalSize % pageSize) > 0 ? (pageCount + 1): pageCount;
	}
}
