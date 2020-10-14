package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiProdGoodEvaluationListInput;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiProdGoodEvaluationListResult;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 分页获取商品评价模板内容
 * @author lgx
 * 
 */
public class ApiProdGoodEvaluationList extends RootApiForVersion<ApiProdGoodEvaluationListResult, ApiProdGoodEvaluationListInput> {
	
	public ApiProdGoodEvaluationListResult Process(ApiProdGoodEvaluationListInput inputParam, MDataMap mRequestMap) {
		Map<String,Object> ifIgnoreMap = DbUp.upTable("uc_ignore_category").dataSqlOne("SELECT * FROM usercenter.uc_ignore_category limit 1", new MDataMap());
		String ignore = "Y";//默认过滤
		if(ifIgnoreMap != null) {
			ignore = MapUtils.getString(ifIgnoreMap, "if_ignore","Y");
		}
		ApiProdGoodEvaluationListResult result = new ApiProdGoodEvaluationListResult();//返回结果
		// 首页定位栏目链接类型
		String homePositionLinkType = inputParam.getHomePositionLinkType();
		// 第几页
		int page = inputParam.getPage();
		// 调用渠道
		String channel = getApiClient().get("os");
		String channelId = getChannelId();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		ProductService pService = new ProductService();//实例化ProductService
		// 首页数据展示类型
		String homeShowType = "";
		// 返回的内容List
		List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
		// 总页数
		int totalPage = 0;
		// 起始索引
		int index = (page-1) * 20;
		if("4497471600580001".equals(homePositionLinkType)) {
			String listSql = "";
			String countSql = "";
			if("weapphjy".equals(channel)&&"Y".equals(ignore)) {
				// 如果是小程序
				listSql = "SELECT e.* FROM familyhas.fh_apphome_evaluation e WHERE e.is_delete = '0' AND " + 
						"	e.location_num > 0 AND e.first_category_code NOT IN ( " + 
						"		SELECT f.category_code FROM usercenter.uc_program_del_category f WHERE f.`level` = '2' " + 
						"	) AND e.second_category_code NOT IN ( " + 
						"		SELECT s.category_code FROM usercenter.uc_program_del_category s WHERE s.`level` = '3' " + 
						"	) AND e.third_category_code NOT IN ( " + 
						"		SELECT t.category_code FROM usercenter.uc_program_del_category t WHERE t.`level` = '4' " + 
						"	) ORDER BY e.location_num ASC LIMIT "+index+",20";
				countSql = "SELECT count(1) num FROM familyhas.fh_apphome_evaluation e WHERE e.is_delete = '0' AND" + 
						"	e.location_num > 0 AND e.first_category_code NOT IN ( " + 
						"		SELECT f.category_code FROM usercenter.uc_program_del_category f WHERE f.`level` = '2' " + 
						"	) AND e.second_category_code NOT IN ( " + 
						"		SELECT s.category_code FROM usercenter.uc_program_del_category s WHERE s.`level` = '3' " + 
						"	) AND e.third_category_code NOT IN ( " + 
						"		SELECT t.category_code FROM usercenter.uc_program_del_category t WHERE t.`level` = '4' )";
			}else {
				// 否则是app
				listSql = "SELECT * FROM fh_apphome_evaluation WHERE is_delete = '0' AND location_num > 0 ORDER BY location_num ASC LIMIT "+index+",20";
				countSql = "SELECT count(1) num FROM fh_apphome_evaluation WHERE is_delete = '0' AND location_num > 0 ";
			}
			
			// 查询所有商品评价模板维护的商品
			List<Map<String, Object>> evaProdList = DbUp.upTable("fh_apphome_evaluation").dataSqlList(listSql, new MDataMap());
			for (Map<String, Object> map : evaProdList) {
				HomeColumnContent columnContent = new HomeColumnContent();
				String product_code = map.get("product_code")+"";
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				List<String> productCodeArr = new ArrayList<String>();
				HomeColumnContentProductInfo prodInfo = new HomeColumnContentProductInfo();
				
				prodInfo.setProductCode(product_code);
				
				productCodeArr.add(product_code);
				skuQuery.setCode(StringUtils.join(productCodeArr,","));
				skuQuery.setMemberCode(userCode);
				skuQuery.setIsPurchase(1);
				// 获取商品最低销售价格和对应的划线价
				Map<String,PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
				if(null != productPriceMap.get(product_code)) {					
					BigDecimal sellPrice1 = productPriceMap.get(product_code).getSellPrice();
					// 销售价
					prodInfo.setSellPrice(sellPrice1.toString());
					String eventType = productPriceMap.get(product_code).getEventType();
					if("4497472600010001".equals(eventType) || "4497472600010002".equals(eventType) || "4497472600010005".equals(eventType) 
							 || "4497472600010018".equals(eventType) || "4497472600010024".equals(eventType)) {						
						BigDecimal skuPrice1 = productPriceMap.get(product_code).getSkuPrice();
						if(skuPrice1.compareTo(sellPrice1) > 0) {
							// 划线价
							prodInfo.setMarkPrice(skuPrice1.toString());
						}
					}
					if("4497472600010024".equals(eventType)) {
						String eventCode = productPriceMap.get(product_code).getEventCode();
						PlusModelEventQuery tQuery = new PlusModelEventQuery();
						tQuery.setCode(eventCode);
						PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(tQuery);
						columnContent.setManyCollage(eventInfo.getCollagePersonCount());
					}
				}else {
					PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(product_code));
					// 销售价
					prodInfo.setSellPrice(productInfo.getMinSellPrice().toString());
				}
				
				//获取自营标签
				Map<String, Object> prodMap = DbUp.upTable("pc_productinfo").dataSqlOne("SELECT * FROM pc_productinfo WHERE product_code = '"+product_code+"'", new MDataMap());
				LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
				String stChar = "";
				if("SI2003".equals(MapUtils.getString(prodMap, "small_seller_code", ""))) {
					stChar = "4497478100050000";
				}else {
					PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(MapUtils.getString(prodMap, "small_seller_code", "")));
					stChar = sellerInfo.getUc_seller_type();
				}
				Map<String, String> productTypeMap = WebHelper.getAttributeProductType(stChar);
				prodInfo.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
				
				// 如果修改过,返回表中存的数据
				String order_assessment = map.get("order_assessment")+"";
				String cover_img = map.get("cover_img")+"";
				columnContent.setPicture(cover_img);
				prodInfo.setEvaluationContent(order_assessment);
				// 封面图
				//压缩图片
				List<String> picUrlArr = new ArrayList<String>();
				picUrlArr.add(cover_img);
				List<PicInfo> picInfoList1 = pService.getPicInfoOprBigForMulti(Integer.parseInt("570"), picUrlArr,"");
				if(picInfoList1 != null) {
					PicInfo picInfo = picInfoList1.get(0);
					// 图片宽高
					columnContent.setPicOriginHeight(picInfo.getHeight());
					columnContent.setPicOriginWidth(picInfo.getWidth());
				}
				
				//5.4.2版本之后新增TagList商品活动标签
				List<String> tagList = pService.getTagListByProductCode(product_code, userCode, channelId);
				prodInfo.setTagList(tagList);
				
				// 商品标签详细信息
				ProductLabelService productLabelService = new ProductLabelService();
				List<PlusModelProductLabel> labelInfoList = productLabelService.getLabelInfoList(product_code);
				prodInfo.setLabelsInfo(labelInfoList);
				//562版本对于商品列表标签做版本兼容处理
				String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
				if(appVersion.compareTo("5.6.2")<0){
					Iterator<PlusModelProductLabel> iter = prodInfo.getLabelsInfo().iterator();
					while (iter.hasNext()) {
						PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
						if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
							iter.remove();
						}
					}
				}
				
				columnContent.setProductInfo(prodInfo);
				
				contentList.add(columnContent);
			}
			
			Map<String, Object> countMap = DbUp.upTable("fh_apphome_evaluation").dataSqlOne(countSql, new MDataMap());
			double num = MapUtils.getDoubleValue(countMap, "num");
			// 总页数
			totalPage = (int) Math.ceil(num/20);
			
			homeShowType = "0";
		}
		
		result.setContentList(contentList);
		result.setTotalPage(totalPage);
		result.setHomeShowType(homeShowType);
		
		return result;
	}
	
	
}
