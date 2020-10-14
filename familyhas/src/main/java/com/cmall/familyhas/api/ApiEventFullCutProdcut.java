
package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiEventFullCutProductInput;
import com.cmall.familyhas.api.model.EventListGoodsEntity;
import com.cmall.familyhas.api.model.PriceRange;
import com.cmall.familyhas.api.model.PropertyInfoForProtuct;
import com.cmall.familyhas.api.model.PropertyValueInfo;
import com.cmall.familyhas.api.model.SkuGoodsDetail;
import com.cmall.familyhas.api.result.ApiEventFullCutProductResult;
import com.cmall.familyhas.api.result.PriceChooseObj;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.DateUtilA;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmasproduct.api.ApiSkuInfo;
import com.srnpr.xmasproduct.model.ProductActivity;
import com.srnpr.xmasproduct.model.SkuInfos;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadEventSale;
import com.srnpr.xmassystem.load.LoadGiftSkuInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadProductSales;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelevent.PlusModelFullMoney;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyValueInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/***
 * 满减 活动接口处理类
 * @author zhouguohui
 *
 */
public class ApiEventFullCutProdcut extends RootApiForVersion<ApiEventFullCutProductResult, ApiEventFullCutProductInput>{

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	public ApiEventFullCutProductResult Process(
			ApiEventFullCutProductInput inputParam, MDataMap mRequestMap) {
		ApiEventFullCutProductResult result = new ApiEventFullCutProductResult();
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		String eventCode= inputParam.getActivityCode();
		int sortType = inputParam.getSortType();
		int pageNo = inputParam.getPageNo();
		int pageSize=inputParam.getPageSize();
		String beginTime =inputParam.getBeginTime();
		String categoryCode = inputParam.getCategoryCode();
		PriceRange priceChooseObjIn = inputParam.getPriceChooseObj();
		String channelId = getChannelId();
		MOauthInfo moauthInfo = getOauthInfo();
		List<String> temp = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();
		if(StringUtils.isNotEmpty(categoryCode.trim())) {
			String sqlCategory = "SELECT category_code FROM usercenter.uc_sellercategory_pre  where parent_code in (SELECT category_code FROM usercenter.uc_sellercategory_pre where parent_code = '"+ categoryCode+"' AND flaginable = '449746250001')  AND flaginable = '449746250001'";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_sellercategory_pre").dataSqlList(sqlCategory, null);
			for (Map<String, Object> map : dataSqlList) {
				tempList.add(map.get("category_code").toString());
			}
		}
		for (String s : tempList) {
			temp.add(s.replace(s, "'" + s + "'"));
		}
		String memberCode = "";
		if(moauthInfo!=null){
			memberCode = moauthInfo.getUserCode();
		}
		/*String endTime=inputParam.getEndTime();*/
		/**如果活动编号为空直接返回**/
		if(StringUtils.isEmpty(eventCode)){
			return result;
		}
		ProductService proService = new ProductService();
		
		String sqlQueryFull = "select ev.begin_time,ev.end_time,ev.event_description,ev.is_supraposition_flag, fu.* from  sc_event_info ev , "+
				               " sc_full_cut fu where  ev.event_code=:eventCode and (event_status='4497472700020002' or event_status='4497472700020004') and ev.event_code=fu.event_code ";
		Map<String, Object> fullCut = DbUp.upTable("sc_full_cut").dataSqlOne(sqlQueryFull,new MDataMap("eventCode",eventCode));
		
		//如果活动未开始或活动已结束，则直接返回
		if(fullCut==null || DateUtilA.compare(DateUtil.getSysDateTimeString(), fullCut.get("end_time").toString())>0 ||
				DateUtilA.compare(fullCut.get("begin_time").toString(),DateUtil.getSysDateTimeString() )>0 ){
			return result;
		}else{
			
			StringBuilder sqlQueryPro = new StringBuilder();
			sqlQueryPro.append(" SELECT :field_tmp: FROM productcenter.pc_productinfo pf ");
			sqlQueryPro.append(" LEFT JOIN usercenter.uc_sellercategory_product_relation pr ON pf.product_code = pr.product_code ");
			sqlQueryPro.append(" :sales_tmp: "); // 占位字符串，后面查询总数的时候不用查询销量
			sqlQueryPro.append(" :browse_tmp: ");//占位字符串，后面查询总数的时候不用查询浏览量
			sqlQueryPro.append(" WHERE pf.seller_code = 'SI2003' AND pf.product_status = '4497153900060002' ");
			if(temp.size()>0) {
				sqlQueryPro.append(" AND pr.category_code in ( ").append(StringUtils.join(temp, ",")).append(")");
			}
			sqlQueryPro.append(" AND pf.min_sell_price >= ").append(MoneyHelper.format(priceChooseObjIn.getMinPrice()));
			sqlQueryPro.append(" AND pf.min_sell_price <= ").append(MoneyHelper.format(priceChooseObjIn.getMaxPrice()));
			
			// 有限制条件动态拼接SQL
			if(fullCut.get("full_cut_limit").equals("449747640002")){
				// 仅包含分类
				if(fullCut.get("full_cut_category_type").equals("4497476400020002")){
					List<String> vs = new ArrayList<String>();
					String[] cats = fullCut.get("full_cut_category_code").toString().split(",");
					for(String cat : cats){
						if(StringUtils.isNotBlank(cat)){
							vs.add("pr.category_code LIKE '%"+cat+"%'");
						}
					}
					
					if(!vs.isEmpty()){
						sqlQueryPro.append(" AND (").append(StringUtils.join(vs," OR ")).append(") ");
					}
				}
				
				// 排除分类
				if(fullCut.get("full_cut_category_type").equals("4497476400020003")){
					List<String> vs = new ArrayList<String>();
					String[] cats = fullCut.get("full_cut_category_code").toString().split(",");
					for(String cat : cats){
						if(StringUtils.isNotBlank(cat)){
							vs.add("pr.category_code NOT LIKE '%"+cat+"%'");
						}
					}
					
					if(!vs.isEmpty()){
						sqlQueryPro.append(" AND (").append(StringUtils.join(vs," OR ")).append(") ");
					}
				}
				
				// 仅包含品牌
				if(fullCut.get("full_cut_brand_type").equals("4497476400020002")){
					String[] brandCodes = fullCut.get("full_cut_brand_code").toString().split(",");
					sqlQueryPro.append(" AND pf.brand_code IN('").append(StringUtils.join(brandCodes,"','")).append("')");
				}
				
				// 排除品牌
				if(fullCut.get("full_cut_brand_type").equals("4497476400020003")){
					String[] brandCodes = fullCut.get("full_cut_brand_code").toString().split(",");
					sqlQueryPro.append(" AND pf.brand_code NOT IN('").append(StringUtils.join(brandCodes,"','")).append("')");
				}
				
				// 仅包含商品
				if(fullCut.get("full_cut_product_type").equals("4497476400020002")){
					String[] proCode = fullCut.get("full_cut_product_code").toString().split(",");
					sqlQueryPro.append(" AND pf.product_code IN('").append(StringUtils.join(proCode,"','")).append("')");
				}
				
				// 排除商品
				if(fullCut.get("full_cut_product_type").equals("4497476400020003")){
					String[] proCode = fullCut.get("full_cut_product_code").toString().split(",");
					sqlQueryPro.append(" AND pf.product_code NOT IN('").append(StringUtils.join(proCode,"','")).append("')");
				}
			}
			
			// 替换查询的字段为真正需要查询的字段
			String sql = sqlQueryPro.toString().replace(":field_tmp:", "pf.product_code,ss.sales");
			// 替换销量的SQL片段
			sql = sql.replace(":sales_tmp:", " LEFT JOIN (SELECT sd.product_code,SUM(sd.sales) sales FROM productcenter.pc_productsales_everyday sd WHERE DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= DATE(sd.day) GROUP BY product_code) ss ON ss.product_code = pf.product_code ");
			sql = sql.replace(":browse_tmp:", " LEFT JOIN (SELECT bh.product_code product_code,bh.browse_num browse_num FROM productcenter.pc_browse_history_week bh  GROUP BY product_code) sb ON sb.product_code = pf.product_code ");
			sql = sql.replace(":price_tmp:", " LEFT JOIN (SELECT bh.product_code product_code,bh.browse_num browse_num FROM productcenter.pc_browse_history_week bh  GROUP BY product_code) sb ON sb.product_code = pf.product_code ");
			sql = sql + " group by pf.product_code ";
			if(sortType==2){
				sql = sql + " order by sales desc ";
			}
			if(sortType == 1) {
				sql = sql + " order by browse_num desc ";
			}
//			sql = sql + " limit "+pageNo*pageSize+" ,"+pageSize;
			
			List<Map<String, Object>> proList =  DbUp.upTable("pc_productinfo").dataSqlList(sql,new MDataMap());
			Map<String, Object> proListPage = null;
			
			 if(proList!=null && proList.size()>0){
				String sqlSum = sqlQueryPro.toString().replace(":field_tmp:", "count(*) as sumCount");
				sqlSum = sqlSum.replace(":sales_tmp:", ""); // 去掉销量的查询占位字符串，提高查询速度
				sqlSum = sqlSum.replace(":browse_tmp:", ""); // 去掉浏览量的查询占位字符串，提高查询速度
				 proListPage = DbUp.upTable("pc_productinfo").dataSqlOne(sqlSum,new MDataMap());
				 List<EventListGoodsEntity> fullCutProduct = new ArrayList<EventListGoodsEntity>();
				 ApiSkuInfo apiSkuInfo = new ApiSkuInfo();
				 LoadGiftSkuInfo  loadSkuInfo = new LoadGiftSkuInfo();
				 
				/* List<String> listPro = new ArrayList<String>();
				 for(Map<String, Object> map : proList){
					 listPro.add(map.get("product_code").toString());
				 }
				 MDataMap mDataMap =  ps.getProductFictitiousSales(selleCode,listPro,30);*/
				 
				 
				 
				 loop: for(Map<String, Object> map : proList){
					 PlusModelSkuQuery query = new PlusModelSkuQuery();
					 query.setCode(map.get("product_code").toString());
					 query.setMemberCode(memberCode);
					 query.setChannelId(getChannelId());
					 apiSkuInfo.checkAndInit(query, mRequestMap);
					 SkuInfos skuInfos = apiSkuInfo.Process(query, new MDataMap());
					 
					 //通过app活动标签点击显示满减商品列表，去掉如果商品参加其他活动，不在满减列表中显示的逻辑。因为LD多重满减活动商品是可能参加特价和扫码购的
//					 if(!skuInfos.getEvents().isEmpty()){
//						 continue;
//					 }else{
					 
					 	/*
					 	 * //通过app活动标签点击显示满减商品列表，去掉如果商品参加其他活动，不在满减列表中显示的逻辑。因为LD多重满减活动商品是可能参加特价和扫码购的
					 	 * 由于放开 207 行代码的判断
					 	 * 则   如果商品参与的不是特殊LD的满减，且商品还参与了特价，则不展示该商品
					 	 */
					 	
					 
					 
				/*	 	if(!"449747630008".equals(fullCut.get("full_cut_type"))) {//判断是否是LD多重满减活动
					 		if(!skuInfos.getEvents().isEmpty()){
								 continue;
							 }
					 	}*/
						 
					 //5.4.2支持满减叠加
							if(!skuInfos.getEvents().isEmpty()) {
								 PlusModelEventFull eventFullInfo = null;
								 for ( ProductActivity pa : skuInfos.getEvents()) {
									if(!"4497472600010008".equals(pa.getEventType())) {
										eventFullInfo = new PlusSupportEvent().upEventSalueByMangeCodeAndProductCode(getManageCode(),map.get("product_code").toString(),pa.getEventType());
										if(eventFullInfo==null)
										{continue loop;}
										else {
											break;
										}
								}
							}
						}
				
		
					 
						 if(!PlusHelperEvent.checkEventItem(map.get("product_code").toString())){
							 boolean is_true=false;
							 List<PlusModelFullCutMessage> saleMessage =  skuInfos.getSaleMessage();
							 for(int r=0;r<saleMessage.size();r++){
								 PlusModelFullCutMessage fullCutMessage = saleMessage.get(r);
								 if(!eventCode.equals(fullCutMessage.getEventCode())) {
										//如果当前列表页面的商品满足了别的活动 并且时间比当前时间的靠后  剔除该商品
									  if(DateUtilA.compareDateNew(fullCutMessage.getBeginTime(),beginTime)>0){
										  is_true = true;
										continue;
									  }	
								 }
							 }
							 if(is_true){
								 continue; 
							 }
								
						 }
						 
//						 由于在以上代码中覆盖了商品编号  ---在此处重新赋值
						 query.setCode(map.get("product_code").toString());
						 PlusModelGitfSkuInfoList gitList = loadSkuInfo.upInfoByCode(query);
						 List<String> list = new ArrayList<String>();
						 if(gitList.getGiftSkuinfos()!=null && gitList.getGiftSkuinfos().size()>0){
							 list.add("赠品");
						 }
						 PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(map.get("product_code").toString()));
						 List<PicInfo> picInfoList = proService.upImageInfo(600,productInfo.getMainpicUrl());
						 String urlImg = "";
						 if(picInfoList!=null){
							 for(int m=0;m<picInfoList.size();m++){
								 PicInfo picInfo = picInfoList.get(m);
								 urlImg=picInfo.getPicNewUrl();
							 }
						 }else{
							 urlImg = productInfo.getMainpicUrl();
						 }
						 //赠品  LoadGiftSkuInfo
						 EventListGoodsEntity goodsEntity = new EventListGoodsEntity();
						 //524 商品标签
						 String ssc =productInfo.getSmallSellerCode();
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
						
						 goodsEntity.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString().toString());
						 goodsEntity.setBuyStatus(skuInfos.getBuyStatus());
						 goodsEntity.setCommodityCode(map.get("product_code").toString());
						 goodsEntity.setCommodityName(productInfo.getProductName());  //商品名称
						 goodsEntity.setCommodityPic(urlImg);//商品图片
						 goodsEntity.setLabelsList(productInfo.getLabelsList());
						 List<PlusModelSkuInfo> skus  = skuInfos.getSkus();
						 List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
						 BigDecimal sellPrice=BigDecimal.ZERO;
						 for (int i = 0; i < skus.size(); i++) {
							 PlusModelSkuInfo plusModelSkuPrice =skus.get(i);
							 listPrice.add(plusModelSkuPrice.getSellPrice());
						 }
				
						 if(listPrice!=null && listPrice.size()>1){
							 //价格倒排序
							 Collections.sort(listPrice, new Comparator<BigDecimal>() {
								 public int compare(BigDecimal beginTimeOne, BigDecimal beginTimeTwo) {
									 BigDecimal one =beginTimeOne;
									 BigDecimal two =beginTimeTwo;
									 return one.compareTo(two);
								 }
							 });
							 sellPrice=listPrice.get(0);
						 }else if(listPrice!=null && listPrice.size()==1){
							 
							 sellPrice=listPrice.get(0);
						 }
						 
						 goodsEntity.setCurrentPrice(sellPrice); //现价
						 goodsEntity.setFlagTheSea(productInfo.getFlagTheSea());//是否海外购
						 goodsEntity.setOtherShow(list);//参加赠品
						 goodsEntity.setProductStatus(productInfo.getProductStatus());//商品状态
						 List<PlusModelSkuPropertyInfo> listSkuPro =  productInfo.getPropertyList();
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
						 goodsEntity.setPropertyList(listForPro);//商品规格
						 
						 PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(map.get("product_code").toString());		
						PlusModelProductSales productSales = new LoadProductSales().upInfoByCode(plusModelProductQuery);
						if(productSales!=null){
							goodsEntity.setSaleNum(productSales.getFictitionSales30());
						}else{
							goodsEntity.setSaleNum(0);
						}
						// goodsEntity.setSaleNum(mDataMap==null?0:mDataMap.get(map.get("product_code").toString())==null?0:Integer.parseInt(mDataMap.get(map.get("product_code").toString()))); 
						 
						 List<PlusModelProductSkuInfo> skuInfoKeyValue = productInfo.getSkuList();
						 Map<String,String> mapKeyValue = new HashMap<String,String>();
						 for(int s=0;s<skuInfoKeyValue.size();s++){
							 PlusModelProductSkuInfo proSkuInfo = skuInfoKeyValue.get(s);
							 mapKeyValue.put(proSkuInfo.getSkuCode(), proSkuInfo.getSkuKey());
						 }
						 
						 List<PlusModelSkuInfo> modelSkuInfo =  skuInfos.getSkus();
						 List<SkuGoodsDetail> listSkuFullCut = new ArrayList<SkuGoodsDetail>();
						 for (int i = 0; i < modelSkuInfo.size(); i++) {
							 PlusModelSkuInfo plusModelSkuInfo = modelSkuInfo.get(i);
							 SkuGoodsDetail detail = new SkuGoodsDetail();
							 detail.setSkuCode(plusModelSkuInfo.getSkuCode());
							 detail.setSkuName(plusModelSkuInfo.getSkuName());
							 detail.setKeyValue(mapKeyValue.get(plusModelSkuInfo.getSkuCode()));    
							 detail.setStockNumSum(0); 
							 detail.setSellPrice(plusModelSkuInfo.getSellPrice());
							 detail.setMarketPrice(plusModelSkuInfo.getSourcePrice());
							 detail.setActivityInfo(null);   
							 detail.setVipSpecialPrice("0");     
							 detail.setDisMoney(BigDecimal.ZERO); 
							 detail.setSkuMaxBuy((int)plusModelSkuInfo.getLimitBuy());
							 detail.setMiniOrder(0);   
							 detail.setLimitBuy((int)plusModelSkuInfo.getLimitBuy());
							 detail.setLimitStock(plusModelSkuInfo.getLimitStock());
							 detail.setBuyStatus(plusModelSkuInfo.getBuyStatus());
							 detail.setShowLimitNum(0);
							 detail.setSkuPic(plusModelSkuInfo.getSkuPicUrl());
							 if(detail.getSellPrice().compareTo(priceChooseObjIn.getMinPrice()) >= 0 && detail.getSellPrice().compareTo(priceChooseObjIn.getMaxPrice()) <= 0 ) {
								 listSkuFullCut.add(detail);
							 }
						}
						 goodsEntity.setSkuList(listSkuFullCut);
						 if(listSkuFullCut.size() > 0) {
							 fullCutProduct.add(goodsEntity);
						 }
						
//					 }
					 
				 }
				 				 
				 result.setFullCutDescription(fullCut.get("event_description")==null?"":fullCut.get("event_description").toString());
				 if(sortType==2){
						
				 //当前按销量排序
					Collections.sort(fullCutProduct, new Comparator<Object>() {
					      public int compare(Object fullCutOne, Object fullCutTwo) {
					    	  Integer  one =((EventListGoodsEntity)fullCutOne).getSaleNum();
					    	  Integer  two =((EventListGoodsEntity)fullCutTwo).getSaleNum();
					    	  return two.compareTo(one);
					      }
				    });
					
					
				 }
				 if(sortType==3){						
					//价格升序
					Collections.sort(fullCutProduct, new Comparator<Object>() {
					      public int compare(Object fullCutOne, Object fullCutTwo) {
					    	  BigDecimal  one =((EventListGoodsEntity)fullCutOne).getCurrentPrice();
					    	  BigDecimal  two =((EventListGoodsEntity)fullCutTwo).getCurrentPrice();
					    	  return one.compareTo(two);
					      }
				    });		
				  }
				 if(sortType==4){						
						//价格降序
						Collections.sort(fullCutProduct, new Comparator<Object>() {
						      public int compare(Object fullCutOne, Object fullCutTwo) {
						    	  BigDecimal  one =((EventListGoodsEntity)fullCutOne).getCurrentPrice();
						    	  BigDecimal  two =((EventListGoodsEntity)fullCutTwo).getCurrentPrice();
						    	  return two.compareTo(one);
						      }
					    });		
				  }
				 
				 int pageNum=0;
				 int recordNum=0;
				 if(fullCutProduct!=null){
					 recordNum = fullCutProduct.size();
					 
					 if ((recordNum%pageSize) == 0) {
						 pageNum = (int) recordNum / pageSize;
					 } else {
						pageNum = (int) recordNum / pageSize + 1;
					 }
				 }
				 
				 result.setPageNum(pageNum);
				 result.setRecordNum(recordNum);
				 int start = 0;
				 int end = 0;
				 if("449747430023".equals(channelId)) {//小程序，从1开始
					 if(pageNo == 0) {//默认从第一页开始，兼容前端传0页的BUG
						 pageNo = 1;
					 }
					 start = (pageNo-1)*pageSize;
					 end = (pageNo-1)*pageSize+pageSize;
				 }else {
					 start = pageNo*pageSize;
					 end = pageNo*pageSize+pageSize;
				 }
				 if(end>fullCutProduct.size()){
					 end = fullCutProduct.size();
				 }
				 if(start < 0) {
					 start = 0;
				 }
				 if(start <= end) {
					 fullCutProduct = fullCutProduct.subList(start, end);
				 }
				 result.setFullCutProduct(fullCutProduct);
				 
			 }
			
		}
		/**
		 * 5.4.2新增tagList标签
		 */
		ProductService ps = new ProductService();
		List<EventListGoodsEntity> list = result.getFullCutProduct();
		for(EventListGoodsEntity entity : list) {
			String productCode = entity.getCommodityCode();
			List<String> tagList = ps.getTagListByProductCode(productCode, memberCode,getChannelId());
			entity.setTagList(tagList);
			
			if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
				List<TagInfo> tagInfoList = ps.getProductTagInfoList(productCode, memberCode,getChannelId());
				entity.setTagInfoList(tagInfoList);
			}
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
		//购物车没有商品时，通过商品详情页进的满减列表页，需要返回满减信息，方便前端处理
		String sqlPrice = "select * from sc_full_cut_price  where full_cut_code=:fullCutCode order by full_price asc limit 1";
		Map<String,Object> mapPrice = DbUp.upTable("sc_full_cut_price").dataSqlOne(sqlPrice,new MDataMap("fullCutCode",MapUtils.getString(fullCut,"full_cut_code","")));
		if(mapPrice != null) {
			result.setAddFullMoney(MapUtils.getString(mapPrice,"full_price",""));
		}
		if("449747630004".equals(MapUtils.getString(fullCut,"full_cut_type",""))||"449747630005".equals(MapUtils.getString(fullCut,"full_cut_type",""))) {
			result.setAddFullMoneyType(2);
		}else {
			result.setAddFullMoneyType(1);
		}
		return result;
	}
	
}
