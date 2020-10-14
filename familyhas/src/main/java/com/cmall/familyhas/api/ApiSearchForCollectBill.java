package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cmall.familyhas.api.input.ApiSearchForCollectBillInput;
import com.cmall.familyhas.api.model.EventListGoodsEntity;
import com.cmall.familyhas.api.model.PropertyInfoForProtuct;
import com.cmall.familyhas.api.model.PropertyValueInfo;
import com.cmall.familyhas.api.model.SkuGoodsDetail;
import com.cmall.familyhas.api.result.ApiSearchForCollectBillResult;
import com.cmall.familyhas.api.result.PriceChooseObj;
import com.cmall.ordercenter.familyhas.active.product.ActiveForSource;
import com.cmall.productcenter.model.BigContent;
import com.cmall.productcenter.model.DetailContent;
import com.cmall.productcenter.model.Item;
import com.cmall.productcenter.model.Pager;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.model.Product;
import com.cmall.productcenter.model.SolrData;
import com.cmall.productcenter.model.api.ApiSearchResultsResult;
import com.cmall.productcenter.service.ProductGiftsSearch;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.util.SolrQueryUtil;
import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmasproduct.api.ApiSkuInfo;
import com.srnpr.xmasproduct.model.SkuInfos;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyValueInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * @author zhangbo
 */
public class ApiSearchForCollectBill extends
		RootApiForVersion<ApiSearchForCollectBillResult, ApiSearchForCollectBillInput> {

	@SuppressWarnings("unchecked")
	public ApiSearchForCollectBillResult Process(ApiSearchForCollectBillInput inputParam,
			MDataMap mRequestMap) {
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType(new ActiveForSource()
			.checkIsVipSpecialForFamilyhas(getOauthInfo().getUserCode()) ? "4497469400050001"
			: "4497469400050002");
		}
		ApiSearchResultsResult re = new ApiSearchResultsResult();
	    //搜索结果返回完毕，前端需要包装成满减活动列表返回
		ApiSearchForCollectBillResult result = new ApiSearchForCollectBillResult();
		Integer isPurchase = inputParam.getIsPurchase();
		/**添加列表显示的默认样式 ：1、小图列表；2、大图列表***/
		re.setStyleValue(Integer.parseInt(TopUp.upConfig("productcenter.styleValue")));
		String sellercode = getManageCode();
		List<Item> listItem = new ArrayList<Item>();
		List<EventListGoodsEntity> productList = new ArrayList<EventListGoodsEntity>();
		EventListGoodsEntity eventListGoodsEntity = null;
		Pager pager = new Pager();
		BigDecimal minPrice = inputParam.getPriceChooseObj().getMinPrice().add(new BigDecimal(Double.parseDouble("0.01")));
		BigDecimal maxPrice = inputParam.getPriceChooseObj().getMaxPrice();
		String buyerType = inputParam.getBuyerType();
		String memberCode = getOauthInfo().getUserCode();
		String categoryCode = inputParam.getCategoryCode();
		ProductService productService = new ProductService();
		
		// 0、默认；1、销量；2、上架时间；3、价格；4、人气     默认为：0
		int sortType = inputParam.getSortType();
		if (sortType <= 0) {
			sortType = 0;
		}
		// 1、正序；2、倒序 默认为：2
		int sortFlag = inputParam.getSortFlag();
		if (sortFlag <= 0) {
			sortFlag = 2;
		}
		// 每页读取记录数 默认为10
		int pageSize = inputParam.getPageSize();
		if (pageSize <= 0) {
			pageSize = 10;
		}
		// 读取页码 默认为1
		int pageNo = inputParam.getPageNo();
		if (pageNo <= 0) {
			pageNo = 1;
		}
		int page = pageNo;
		pageNo = (pageNo - 1) * pageSize;
		// 屏幕宽度
		int screenWidth = Constants.IMG_WIDTH_SP02;

		String tag = null;
		String[] tagList = null;
		/**
		 * 惠家有3.8.0版本 返回标签为数组
		 */
		if(VersionHelper.checkServerVersion("3.5.72.55")&&sellercode.equals("SI2003")){

			    /**20150909添加 新版solr5.2.1**/
				if(TopUp.upConfig("productcenter.hjywebclient").equals("yes")){
					MDataMap dataMap = new MDataMap();
					dataMap.put("sortType",sortType+"");
					dataMap.put("sortFlag",sortFlag+"");
					dataMap.put("pageNo", pageNo+"");
					dataMap.put("pageSize", pageSize+"");
					dataMap.put("sellercode",getManageCode());
					dataMap.put("minPrice",minPrice+"");
					dataMap.put("maxPrice",maxPrice+"");
					//小程序临时兼容
					dataMap.put("os",AppVersionUtils.compareTo(
								null == getApiClient().get("app_vision") ? "" : getApiClient().get("app_vision"),"5.1.4") == 0 
								? "weapphjy" : (null == getApiClient().get("os") ? "" : getApiClient().get("os")));
				//分类搜索入参
				dataMap.put("keyWord","");
				dataMap.put("couponTypeCode","");
				dataMap.put("category","category");
				List<String> tempList = new ArrayList<String>();
				if(StringUtils.isEmpty(categoryCode)) {
					//首次进入默认获取全部二级分类
					List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_sellercategory_pre").dataSqlList("select category_code from uc_sellercategory_pre where seller_code='SI2003' and level='2' and flaginable = '449746250001'  order by sort asc", null);
		            for (Map<String, Object> map : dataSqlList) {
		            	tempList.add(map.get("category_code").toString());
					}
				}else {
					tempList.add(categoryCode);
				}
				//dataMap.put("categoryCode", StringUtils.join(tempList, ","));
				{
			        //获取四级分类(因为直接二级搜多到的商品数量不全,所以直接定位到四级)
					List<String> temp = new ArrayList<String>();
					for (String s : tempList) {
						temp.add(s.replace(s, "'" + s + "'"));
					}
		            List<Map<String,Object>> sqlList = DbUp.upTable("uc_sellercategory_pre").dataSqlList("select category_code from uc_sellercategory_pre where seller_code='SI2003' and level='2' and flaginable = '449746250001' and category_code in ("+StringUtils.join(temp, ",")+")  order by sort asc", null);
		            List<String> category4List = new ArrayList<String>();
		            for (Map<String, Object> categoryMap : sqlList) {
		    			String categoryCodeTmp = MapUtils.getString(categoryMap, "category_code", "");
						String level = MapUtils.getString(categoryMap, "level", "");
						if ("3".equals(level)) {
							String threeCategorySql = "select * from uc_sellercategory_pre where seller_code = '"
									+ sellercode + "'" + " and parent_code = '" + categoryCodeTmp + "' and flaginable = '449746250001'";
										
							List<Map<String, Object>> fourCategoryList = DbUp.upTable("uc_sellercategory_pre")
									.dataSqlList(threeCategorySql, new MDataMap());
										
							for (int i = 0; i < fourCategoryList.size(); i++) {
								Map<String, Object> fourCategory = fourCategoryList.get(i);
								String fourCategoryCode = MapUtils.getString(fourCategory, "category_code","");
								category4List.add(fourCategoryCode);
							}
						} else {
							category4List.add(categoryCodeTmp);
						}
					}
		            dataMap.put("categoryCode",  StringUtils.join(category4List, ",")); 
				}

				try {
					String pro = WebClientSupport.upPost(TopUp.upConfig("productcenter.webclienturlselect"), dataMap);
					long longSum = 0;
					if(pro!=null && !pro.equals("")){
						List<SolrData> list = JSON.parseObject(pro,new TypeReference<List<SolrData>>(){}); 
						if(!list.isEmpty()){
							ProductLabelService productLabelService = new ProductLabelService();
							for(int i=0;i<list.size();i++){
								if(i==0){
									longSum= list.get(i).getCounts();
								}
								eventListGoodsEntity = new EventListGoodsEntity();
								ProductGiftsSearch pgs = new ProductGiftsSearch();
								Map<String, String> map = pgs.getProductGiftsSearch(list.get(i).getK1(), getChannelId());
								String productCode = list.get(i).getK1();
								// 根据product code 在缓存中取商品信息  
								PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo(); 
								try {
									plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
								} catch (Exception e) { 
									XmasKv.upFactory(EKvSchema.Product).del(productCode);
									plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
								}
								Boolean lcflag = true;
								 // 抄底价商品 449747110001:否，449747110002:是  
								if(StringUtils.isNotBlank(plusModelProductinfo.getLowGood()) && plusModelProductinfo.getLowGood().equals("449747110002")){
									lcflag = false;
								} 
								List<String> listOtherShow = new ArrayList<String>();
								if(lcflag && map!=null && !map.isEmpty()){
									listOtherShow.add("赠品");
								}
								//活动列表格式数据封装
								eventListGoodsEntity.setActivityList(list.get(i).getL7());
								eventListGoodsEntity.setBuyStatus(1);
								eventListGoodsEntity.setCommodityCode(list.get(i).getK1());
								eventListGoodsEntity.setCommodityName(list.get(i).getS9());
								eventListGoodsEntity.setCurrentPrice(new BigDecimal(list.get(i).getD2()).setScale(2, RoundingMode.HALF_UP));
								eventListGoodsEntity.setFlagTheSea(list.get(i).getI3()==0?"0":"1");
								eventListGoodsEntity.setOtherShow(listOtherShow);
								eventListGoodsEntity.setProductStatus(plusModelProductinfo.getProductStatus());
								eventListGoodsEntity.setTagList(list.get(i).getL6());
								 //5.5.8增加所有sku实际库存 前端用于库存提示
								int allSkuRealStock = new PlusSupportStock().upAllStockForProduct(list.get(i).getK1());
								eventListGoodsEntity.setAllSkuRealStoc(allSkuRealStock);
								eventListGoodsEntity.setSku_stock(allSkuRealStock);
								 List<PlusModelSkuPropertyInfo> listSkuPro =  plusModelProductinfo.getPropertyList();
								 List<PropertyInfoForProtuct> listForPro = new ArrayList<PropertyInfoForProtuct>();
								 for(int k=0;k<listSkuPro.size();k++){
									 PlusModelSkuPropertyInfo modelSku = listSkuPro.get(k);
									 PropertyInfoForProtuct forProtuct = new PropertyInfoForProtuct();
									 forProtuct.setPropertyKeyCode(modelSku.getPropertyKeyCode());
									 forProtuct.setPropertyKeyName(modelSku.getPropertyKeyName());
									 List<PropertyValueInfo> propertyValueList = new ArrayList<PropertyValueInfo>();
									 List<PlusModelSkuPropertyValueInfo> valueInfo =  modelSku.getPropertyValueList();
									 for(int f=0;f<valueInfo.size();f++){
										 PlusModelSkuPropertyValueInfo info = valueInfo.get(f);
										 PropertyValueInfo proInfo = new PropertyValueInfo();
										 proInfo.setPropertyValueCode(info.getPropertyValueCode());
										 proInfo.setPropertyValueName(info.getPropertyValueName());
										 propertyValueList.add(proInfo);
									 }
									 forProtuct.setPropertyValueList(propertyValueList);
									 listForPro.add(forProtuct);
								 }
								eventListGoodsEntity.setPropertyList(listForPro);
								if(StringUtils.isNotBlank(list.get(i).getK1())) {
									 PlusModelSkuQuery query = new PlusModelSkuQuery();
									 query.setCode(list.get(i).getK1());
									 query.setMemberCode(memberCode);
									 ApiSkuInfo apiSkuInfo = new ApiSkuInfo();
									 apiSkuInfo.checkAndInit(query, mRequestMap);
									 SkuInfos skuInfos = apiSkuInfo.Process(query, new MDataMap());
									 if(skuInfos.getSkuPrice().contains("-")) {
										 String[] split = StringUtils.split(skuInfos.getSkuPrice(),"-");
										 skuInfos.setSkuPrice(split[0]);
									 }
                                   
                                	 if(StringUtils.isNotBlank(skuInfos.getSkuPrice())) {
                                		 eventListGoodsEntity.setOriginalPrice(new BigDecimal(skuInfos.getSkuPrice()).setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal(0))==0?null:new BigDecimal(skuInfos.getSkuPrice()).setScale(2, RoundingMode.HALF_UP)); 
                                	 } 
		
									 List<PlusModelSkuInfo> modelSkuInfo =  skuInfos.getSkus();
									 List<SkuGoodsDetail> listSkuFullCut = new ArrayList<SkuGoodsDetail>();
									 List<PlusModelProductSkuInfo> skuInfoKeyValue = plusModelProductinfo.getSkuList();
									 Map<String,String> mapKeyValue = new HashMap<String,String>();
									 for(int s=0;s<skuInfoKeyValue.size();s++){
										 PlusModelProductSkuInfo proSkuInfo = skuInfoKeyValue.get(s);
										 mapKeyValue.put(proSkuInfo.getSkuCode(), proSkuInfo.getSkuKey());
									 }

									 for (int j = 0; j < modelSkuInfo.size(); j++) {
										 PlusModelSkuInfo plusModelSkuInfo = modelSkuInfo.get(j);
										 PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
										 plusModelQuery.setCode(plusModelSkuInfo.getSkuCode());
										 PlusModelSkuResult upSkuInfo = new PlusSupportProduct().upSkuInfo(plusModelQuery);
										 SkuGoodsDetail detail = new SkuGoodsDetail();
										 detail.setSkuCode(plusModelSkuInfo.getSkuCode());
										 detail.setSkuName(plusModelSkuInfo.getSkuName());
										 detail.setKeyValue(mapKeyValue.get(plusModelSkuInfo.getSkuCode()));    
										 detail.setStockNumSum(Integer.valueOf((plusModelSkuInfo.getLimitStock()+plusModelSkuInfo.getLimitSellStock()+""))); 
										 detail.setSellPrice(plusModelSkuInfo.getSellPrice());
										 detail.setMarketPrice(plusModelSkuInfo.getSourcePrice());
										 detail.setActivityInfo(null);   
										 detail.setVipSpecialPrice("0");     
										 detail.setDisMoney(BigDecimal.ZERO); 
										 detail.setSkuMaxBuy((int)plusModelSkuInfo.getLimitBuy());
										 detail.setMiniOrder(Integer.parseInt(upSkuInfo.getSkus().get(0).getMinBuy()+""));   
										 detail.setLimitBuy((int)plusModelSkuInfo.getLimitBuy());
										 detail.setLimitStock(plusModelSkuInfo.getLimitStock());
										 detail.setBuyStatus(plusModelSkuInfo.getBuyStatus());
										 detail.setShowLimitNum(0);
										 detail.setSkuPic(plusModelSkuInfo.getSkuPicUrl());
										 listSkuFullCut.add(detail);
									}
									 eventListGoodsEntity.setSkuList(listSkuFullCut);
								}
								
								
								//524：添加商品分类标签
								PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(list.get(i).getK1()));
								String ssc =productInfo.getSmallSellerCode();
								String st="";
								if("SI2003".equals(ssc)) {
									st="4497478100050000";
								}
								else {
									st = WebHelper.getSellerType(ssc);
								}
								
								if(StringUtils.isNotBlank(productInfo.getMainpicUrl())) {
									PicInfo imgUrl = productService.getPicInfoOprBig(screenWidth, productInfo.getMainpicUrl());
									eventListGoodsEntity.setCommodityPic(imgUrl==null?"":imgUrl.getPicNewUrl());
								}
								//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
								Map<String,String> productTypeMap = WebHelper.getAttributeProductType(st);
								eventListGoodsEntity.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
								/**添加商品标签**/
								eventListGoodsEntity.setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
								if (null!=productInfo.getLabelsList() && productInfo.getLabelsList().size()>0) {
									eventListGoodsEntity.setLabelsList(productInfo.getLabelsList());
								}
								
								productList.add(eventListGoodsEntity);
							}
						}
						pager.setPageNo(page);
						int lengNum = 0;
						if ((longSum % pageSize) == 0) {
							lengNum = (int) longSum / pageSize;
						} else {
							lengNum = (int) longSum / pageSize + 1;
						}
						pager.setPageNum(lengNum);
						pager.setPageSize(pageSize);
						pager.setRecordNum((int) longSum);
						re.setPager(pager);
						re.setItem(listItem);
						re.setNumber(1);
						result.setPageNum(lengNum);
						result.setRecordNum((int)longSum);
						result.setProductList(productList);
						result.setRepurchaseEvent(inputParam.getRepurchaseEvent());

					}
					}
				 catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

				//把筛选下内容返给前端
				//demo:家有服务:1-家有自营,1-促销商品,1-非促销商品;价格区间:2-最低价&最高价
				String upConfig = TopUp.upConfig("productcenter.allSelectContents");
				String[] split = upConfig.split(";");
				List<BigContent> lists = new ArrayList<>();
				for(String str : split) {
					BigContent bigContent = new BigContent();
					String[] split2 = str.split(":");
					bigContent.setTitle(split2[0]);
					List<DetailContent> contents = new ArrayList<DetailContent>();
					String[] split3 = split2[1].split(",");
					for(String str2 : split3) {
						DetailContent detailContent = new DetailContent();
						String[] split4 = str2.split("-");
						detailContent.setType(split4[0]);
						detailContent.setText(split4[1]);
						contents.add(detailContent);
					}
					bigContent.setContents(contents);
					lists.add(bigContent);
				}
				re.setLists(lists);
				//新增拼团标识
				//只有5.4.0之后版本走此逻辑。
				String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
				if(StringUtils.isEmpty(appVersion)) {
					appVersion = "5.4.2";
				}
				String isMemberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";//用户编号
				if(AppVersionUtils.compareTo(appVersion,"5.4.0")>=0 && re.getItem() != null){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
					List<EventListGoodsEntity> productList2 = result.getProductList();
					//过滤掉拼团的商品
					List<EventListGoodsEntity> ptProductList=new ArrayList<EventListGoodsEntity>();
					for (EventListGoodsEntity eventListGoodsEntity2 : productList2) {
						String commodityCode = eventListGoodsEntity2.getCommodityCode();
						//根据商品编号查询商品所参与的活动
						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						skuQuery.setCode(commodityCode);
						skuQuery.setMemberCode(getOauthInfo().getUserCode());
						Map<String,PlusModelSkuInfo> map = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
						PlusModelSkuInfo skuInfo = map.get(commodityCode);
						if(!"".equals(skuInfo.getEventType())) {
							//参加了活动
							if("4497472600010024".equals(skuInfo.getEventType())){//拼团单,屏蔽
								ptProductList.add(eventListGoodsEntity2);
								continue;
							}else{
								eventListGoodsEntity2.setProductType("4497472000050002");//不是拼团单
							}
							if(skuInfo.getSellPrice().compareTo(BigDecimal.ZERO)==0) {
								//过滤脏数据
								continue;
							}
						}else {
						//未参加活动,不显示划线价
							eventListGoodsEntity2.setOriginalPrice(null);
						}
						eventListGoodsEntity2.setCurrentPrice(skuInfo.getSellPrice());
						ProductService ps = new ProductService();
			
						List<String> tags = ps.getTagListByProductCode(commodityCode,isMemberCode,getChannelId());
						eventListGoodsEntity2.setTagList(tags);
						if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
							List<TagInfo> tagInfoList = ps.getProductTagInfoList(commodityCode, isMemberCode, getChannelId());
							eventListGoodsEntity2.setTagInfoList(tagInfoList);
						}
					}
					productList2.removeAll(ptProductList);
				}
			
				//获取配置文件中的价格区间
				String priceRange = TopUp.upConfig("familyhas.price_range");
				String[] priceRangeArr = StringUtils.split(priceRange, ",");
				for (int i=0;i<=priceRangeArr.length-1;i++) {
					if(i==priceRangeArr.length-1) {
						PriceChooseObj priceChooseObj = new PriceChooseObj();
						priceChooseObj.setMinPrice(BigDecimal.valueOf(Double.parseDouble(priceRangeArr[i])));
						priceChooseObj.setMaxPrice(BigDecimal.valueOf(Double.parseDouble("100000000")));
						result.getPriceList().add(priceChooseObj);
					}else {
						PriceChooseObj priceChooseObj = new PriceChooseObj();
						priceChooseObj.setMinPrice(BigDecimal.valueOf(Double.parseDouble(priceRangeArr[i])));
						priceChooseObj.setMaxPrice(BigDecimal.valueOf(Double.parseDouble(priceRangeArr[i+1])));
						result.getPriceList().add(priceChooseObj);
					}
				}
				//价格差计算
				BigDecimal allPay = inputParam.getAllPay();
				RepurchaseEvent repurchaseEvent = inputParam.getRepurchaseEvent();
				
				MDataMap one = DbUp.upTable("sc_event_info").one("event_code",repurchaseEvent.getEvent_code());
				BigDecimal repurchaseCondition = new BigDecimal(Double.parseDouble(one.get("repurchase_condition")));
				//BigDecimal repurchaseCondition  = BigDecimal.valueOf(Double.valueOf(one.get("repurchase_condition").toString()));
				if(repurchaseCondition.compareTo(allPay)<=0) {
					result.getRepurchaseEvent().setFlag("1");
				}else {
					BigDecimal subtract = repurchaseCondition.subtract(allPay);
					subtract.setScale(2,BigDecimal.ROUND_HALF_UP);
					result.getRepurchaseEvent().setValue(subtract.doubleValue());
				}
				result.getRepurchaseEvent().setEvent_code(repurchaseEvent.getEvent_code());
				result.getRepurchaseEvent().setEvent_name(one.get("event_name"));
				result.getRepurchaseEvent().setRepurchase_num(Integer.parseInt(one.get("repurchase_num")));
				result.getRepurchaseEvent().setLimit_money(repurchaseCondition);
				result.setTotalMoney(allPay.toString());
				return result;
			}
	
}
