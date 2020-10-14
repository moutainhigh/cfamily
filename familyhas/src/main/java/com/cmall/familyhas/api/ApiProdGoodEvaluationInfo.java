package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiProdGoodEvaluationInfoInput;
import com.cmall.familyhas.api.model.EvaProduct;
import com.cmall.familyhas.api.model.EvaluationImg;
import com.cmall.familyhas.api.model.ProdEvaluation;
import com.cmall.familyhas.api.result.ApiProdGoodEvaluationInfoResult;
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
 * 获取商品所有评价内容图片及商品信息	
 * @author lgx
 * 
 */
public class ApiProdGoodEvaluationInfo extends RootApiForVersion<ApiProdGoodEvaluationInfoResult, ApiProdGoodEvaluationInfoInput> {
	
	public ApiProdGoodEvaluationInfoResult Process(ApiProdGoodEvaluationInfoInput inputParam, MDataMap mRequestMap) {
		ApiProdGoodEvaluationInfoResult result = new ApiProdGoodEvaluationInfoResult();//返回结果
		// 商品编号
		String product_code = inputParam.getProduct_code();
		String channelId = getChannelId();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		if(product_code == null || "".equals(product_code)) {
			result.setResultCode(-1);
			result.setResultMessage("商品编号不能为空");
			return result;
		}
		// 图片总数
		int imgCount = 0;
		// 评价商品信息
		EvaProduct evaProduct = new EvaProduct();
		// 商品评价内容List
		List<ProdEvaluation> evaluationList = new ArrayList<ProdEvaluation>();
		ProductService pService = new ProductService();//实例化ProductService
		
		String sql = "SELECT * FROM pc_productinfo WHERE product_code = '"+product_code+"'";
		Map<String, Object> prodInfo = DbUp.upTable("pc_productinfo").dataSqlOne(sql, new MDataMap());
		evaProduct.setMainpicUrl((String) prodInfo.get("mainpic_url"));
		evaProduct.setProductCode(product_code);
		evaProduct.setProductName((String) prodInfo.get("product_name"));
		
		List<String> productCodeArr = new ArrayList<String>();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		productCodeArr.add(product_code);
		skuQuery.setCode(StringUtils.join(productCodeArr,","));
		skuQuery.setMemberCode(userCode);
		skuQuery.setIsPurchase(1);
		// 获取商品最低销售价格和对应的划线价
		Map<String,PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
		if(null != productPriceMap.get(product_code)) {
			BigDecimal sellPrice1 = productPriceMap.get(product_code).getSellPrice();
			// 销售价
			evaProduct.setSellPrice(sellPrice1.toString());
			String eventType = productPriceMap.get(product_code).getEventType();
			if("4497472600010001".equals(eventType) || "4497472600010002".equals(eventType) || "4497472600010005".equals(eventType) 
					 || "4497472600010018".equals(eventType) || "4497472600010024".equals(eventType)) {						
				BigDecimal skuPrice1 = productPriceMap.get(product_code).getSkuPrice();
				if(skuPrice1.compareTo(sellPrice1) > 0) {
					// 划线价
					evaProduct.setMarkPrice(skuPrice1.toString());
				}
			}
			if("4497472600010024".equals(eventType)) {
				String eventCode = productPriceMap.get(product_code).getEventCode();
				PlusModelEventQuery tQuery = new PlusModelEventQuery();
				tQuery.setCode(eventCode);
				PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(tQuery);
				evaProduct.setManyCollage(eventInfo.getCollagePersonCount());
			}
		}else {
			PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(product_code));
			// 销售价
			evaProduct.setSellPrice(productInfo.getMinSellPrice().toString());
		}
		
		//5.4.2版本之后新增TagList商品活动标签
		List<String> tagList = pService.getTagListByProductCode(product_code, userCode, channelId);
		evaProduct.setTagList(tagList);
		
		// 商品标签详细信息
		ProductLabelService productLabelService = new ProductLabelService();
		List<PlusModelProductLabel> labelInfoList = productLabelService.getLabelInfoList(product_code);
		evaProduct.setLabelsInfo(labelInfoList);
		//562版本对于商品列表标签做版本兼容处理
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(appVersion.compareTo("5.6.2")<0){
			Iterator<PlusModelProductLabel> iter = evaProduct.getLabelsInfo().iterator();
			while (iter.hasNext()) {
				PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
				if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
					iter.remove();
				}
			}
		}
		
		//获取自营标签
		LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
		String stChar = "";
		if("SI2003".equals(MapUtils.getString(prodInfo, "small_seller_code", ""))) {
			stChar = "4497478100050000";
		}else {
			PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(MapUtils.getString(prodInfo, "small_seller_code", "")));
			stChar = sellerInfo.getUc_seller_type();
		}
		Map<String, String> productTypeMap = WebHelper.getAttributeProductType(stChar);
		evaProduct.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
		
		// 该商品的封面评论
		Map<String, Object> coverEva = DbUp.upTable("fh_apphome_evaluation").dataSqlOne("SELECT * FROM fh_apphome_evaluation WHERE product_code = '"+product_code+"'", new MDataMap());
		Map<String, Object> evaInfo = DbUp.upTable("nc_order_evaluation").dataSqlOne("SELECT * FROM nc_order_evaluation WHERE uid = '"+coverEva.get("evaluation_uid")+"' ", new MDataMap());
		// 封面评论
		ProdEvaluation prodEva = new ProdEvaluation();
		List<EvaluationImg> evaluationImgList = new ArrayList<EvaluationImg>();
		prodEva.setEvaluationContent((String) evaInfo.get("order_assessment"));
		// 封面图
		String coverImg = (String) coverEva.get("cover_img");
		// 评论图片
		String oder_photos = (String) evaInfo.get("oder_photos");
		String ccvids = (String) evaInfo.get("ccvids");
		String ccpics = (String) evaInfo.get("ccpics");
		if(!"".equals(ccvids) && !"".equals(ccpics)) { // 有评论视频
			EvaluationImg evaImg = new EvaluationImg();
			evaImg.setEvaluationImgUrl(coverImg);
			if(coverImg.equals(ccpics)) { // 封面图是视频截图
				evaImg.setIsVideo("1");
				evaImg.setCcvid(ccvids);
			}else { // 封面图是评论图片
				evaImg.setIsVideo("0");
				evaImg.setCcvid("");
			}
			evaluationImgList.add(evaImg);
			imgCount++;
			if(coverImg.equals(ccpics)) { // 封面图是视频截图
				if(!oder_photos.equals("")) {
					String[] photos = oder_photos.split("\\|");			
					for (String photo : photos) {
						EvaluationImg evaluationImg = new EvaluationImg();
						evaluationImg.setCcvid("");
						evaluationImg.setEvaluationImgUrl(photo);
						evaluationImg.setIsVideo("0");
						evaluationImgList.add(evaluationImg);
						imgCount++;
					}
				}
			}else { // 封面图是评论图片
				if(!oder_photos.equals("")) {
					String[] photos = oder_photos.split("\\|");			
					for (String photo : photos) {
						if(!photo.equals(coverImg)) {						
							EvaluationImg evaluationImg = new EvaluationImg();
							evaluationImg.setCcvid("");
							evaluationImg.setEvaluationImgUrl(photo);
							evaluationImg.setIsVideo("0");
							evaluationImgList.add(evaluationImg);
							imgCount++;
						}
					}
				}
				EvaluationImg evaluationImg2 = new EvaluationImg();
				evaluationImg2.setCcvid(ccvids);
				evaluationImg2.setEvaluationImgUrl(ccpics);
				evaluationImg2.setIsVideo("1");
				evaluationImgList.add(evaluationImg2);
				imgCount++;
			}
		}else { // 没有评论视频
			EvaluationImg evaImg = new EvaluationImg();
			evaImg.setEvaluationImgUrl(coverImg);
			evaImg.setIsVideo("0");
			evaImg.setCcvid("");
			evaluationImgList.add(evaImg);
			imgCount++;
			if(!oder_photos.equals("")) {
				String[] photos = oder_photos.split("\\|");			
				for (String photo : photos) {
					if(!photo.equals(coverImg)) {						
						EvaluationImg evaluationImg = new EvaluationImg();
						evaluationImg.setCcvid("");
						evaluationImg.setEvaluationImgUrl(photo);
						evaluationImg.setIsVideo("0");
						evaluationImgList.add(evaluationImg);
						imgCount++;
					}
				}
			}
		}
		prodEva.setEvaluationImgList(evaluationImgList);
		evaluationList.add(prodEva);
		
		String sql2 = "SELECT * FROM nc_order_evaluation " + 
				"WHERE product_code = '"+product_code+"' AND flag_show = '449746530001' AND check_flag = '4497172100030002' " + 
				"AND pic_status = '449747510001' AND grade >= 4 AND (oder_photos != '' OR (ccvids != '' AND ccpics != '')) AND uid != '"+coverEva.get("evaluation_uid")+"' " + 
				"ORDER BY oder_creattime DESC ";
		List<Map<String, Object>> evaluationMapList = DbUp.upTable("nc_order_evaluation").dataSqlList(sql2, new MDataMap());
		for (Map<String, Object> evaluationMap : evaluationMapList) {
			ProdEvaluation prodEvaluation = new ProdEvaluation();
			ArrayList<EvaluationImg> evaluationImgList1 = new ArrayList<EvaluationImg>();
			prodEvaluation.setEvaluationContent((String) evaluationMap.get("order_assessment"));
			String oder_photos1 = (String) evaluationMap.get("oder_photos");
			if(!oder_photos1.equals("")) {
				// 评论图片
				String[] photos1 = oder_photos1.split("\\|");
				for (String photo1 : photos1) {
					EvaluationImg evaluationImg1 = new EvaluationImg();
					evaluationImg1.setCcvid("");
					evaluationImg1.setEvaluationImgUrl(photo1);
					evaluationImg1.setIsVideo("0");
					evaluationImgList1.add(evaluationImg1);
					imgCount++;
				}
			}
			String ccvids1 = (String) evaluationMap.get("ccvids");
			String ccpics1 = (String) evaluationMap.get("ccpics");
			if(!"".equals(ccvids1) && !"".equals(ccpics1)) { // 有评论视频
				EvaluationImg evaluationImg3 = new EvaluationImg();
				evaluationImg3.setCcvid(ccvids1);
				evaluationImg3.setEvaluationImgUrl(ccpics1);
				evaluationImg3.setIsVideo("1");
				evaluationImgList1.add(evaluationImg3);
				imgCount++;
			}
			prodEvaluation.setEvaluationImgList(evaluationImgList1);
			evaluationList.add(prodEvaluation);
		}
		
		result.setEvaProduct(evaProduct);
		result.setEvaluationList(evaluationList);
		result.setImgCount(imgCount);
		
		return result;
	}
}
