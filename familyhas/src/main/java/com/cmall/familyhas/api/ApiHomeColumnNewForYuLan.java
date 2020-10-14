package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiHomeColumnNewForYuLanInput;
import com.cmall.familyhas.api.input.ApiHomeColumnNewInput;
import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiHomeColumnNewResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.familyhas.service.HomeColumnServiceforYuLan;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceSale;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有首页版式栏目(APP,WAP端调用)
 * modifed by zhangbo
 * 
 */
public class ApiHomeColumnNewForYuLan extends RootApiForManage<ApiHomeColumnNewResult, ApiHomeColumnNewForYuLanInput> {
	
	private final String PRODUCT_VIEW = "4497471600020004"; 	// 链接类型为商品详情
	private final String CATEGORY = "4497471600020003"; 		// 链接类型为分类
	private final String URL = "4497471600020001"; 				// 链接类型为URL
	
	static LoadProductInfo loadProductInfo = new LoadProductInfo();
	static PlusSupportProduct supportProduct = new PlusSupportProduct();
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	static ProductPriceService priceService = new ProductPriceService();
	
	@Override
	public ApiHomeColumnNewResult Process(ApiHomeColumnNewForYuLanInput inputParam,
			MDataMap mRequestMap) {
		if (StringUtils.isBlank(inputParam.getBuyerType())) {
			inputParam.setBuyerType("4497469400050002");
		}
		if (StringUtils.isBlank(inputParam.getViewType())) {
			inputParam.setViewType("4497471600100001");
		}
		ApiHomeColumnNewResult result = new ApiHomeColumnNewResult();
		List<HomeColumn> columnList = new ArrayList<HomeColumn>();
		HomeColumnServiceforYuLan hcService = new HomeColumnServiceforYuLan();
		
		ProductService pService = new ProductService();

		Integer isPurchase  = inputParam.getIsPurchase(); 
		String sellerCode = getManageCode();
		String userCode =  "";
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? "" : inputParam.getPicType(); // 图片格式
		String userType = inputParam.getBuyerType();
		
		String timePoint = inputParam.getTimePoint();
		String columnName = inputParam.getColumnName();
		String columnType = inputParam.getColumnType();
		String channelId = "449747430001";
		
		String systemTime = (StringUtils.isBlank(timePoint)?DateUtil.getSysDateTimeString():timePoint);
		
		String appVersion = "";
//		if(this.getApiClient() != null && StringUtils.isNotBlank(this.getApiClient().get("app_vision"))){
//			appVersion = this.getApiClient().get("app_vision");
//		}
		
		MDataMap navMap = DbUp.upTable("fh_apphome_nav").one("nav_code",inputParam.getNavCode());
		if("4497471600100002".equals(navMap.get("nav_type"))) {
			channelId = "449747430003";
		} else if("4497471600100003".equals(navMap.get("nav_type"))) {
			channelId = "449747430004";
		} else if("4497471600100004".equals(navMap.get("nav_type"))) {
			channelId = "449747430023";
		}
		
		//viewType 
		//4497471600100001:APP端
		//4497471600100002:WAP页position
		//4497471600100003:PC端
		//按viewType查询未删除,已发布,当前时间在设定的开始时间和结束时间之内的所有首页栏目,结果按位置列正序,创建时间倒序
		String sFields = "column_code,column_name,column_type,start_time,end_time,is_showmore,showmore_title,interval_second,showmore_linktype,"
						+ "showmore_linkvalue,show_name,pic_url,column_bgpic,is_had_edge_distance,columns_per_row,future_program,num_languanggao,show_num,event_code";
		String sOrders = "position asc , create_time desc";
		String sWhere = " start_time <= '" + systemTime	+ "' and end_time > '" + systemTime	+ "' and is_delete='449746250002' "
				        + "and release_flag='449746250001' and seller_code='"+sellerCode+"' "
						+ "and nav_code = '"+inputParam.getNavCode()+"'";
		
		if(StringUtils.isNotBlank(columnType)){
			sWhere += " and column_type ='"+columnType+"'";
		}
		if(StringUtils.isNotBlank(columnName)){
			sWhere += " and column_name ='"+columnName+"'";
		}
		
		
		List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column").queryAll(sFields, sOrders, sWhere, null);

		List<String> cols = new ArrayList<String>((int)(columnMapList.size() / 0.75) + 1);
		for(MDataMap m : columnMapList){
			cols.add("'"+m.get("column_code")+"'");
		}
		
		//查询当前时间在设定的开始时间和结束时间之内的未删除所有首页栏目内容
		String sWhereContent = " start_time <='" + systemTime + "' and end_time > '" + systemTime + "' and is_delete='449746250002'";
		if(!cols.isEmpty()){
			sWhereContent += " and column_code in("+StringUtils.join(cols, ",")+")";
		}
		
		List<MDataMap> columnContentMapList = DbUp.upTable("fh_apphome_column_content").queryAll("", "position asc,start_time desc", sWhereContent, null);
		// 获取所有要查询商品信息的商品编号
		MDataMap productCodeMap = new MDataMap();
		// 获取所有要查询的分类编号，key:categoryCode,value:0
		MDataMap categoryCodeMap = new MDataMap();
		// 图片Map，key:picOldUrl，value:picNewUrl
		MDataMap picUrlMap = new MDataMap();
		// 图片Map，key:picOldUrl，value:picNewUrl
		Map<String,PicInfo> picUrlObjMap = new HashMap<String,PicInfo>();
		
		
		Map<String,List<MDataMap>> columnContentGroupMap = new HashMap<String, List<MDataMap>>();
		for (int i = 0; i < columnContentMapList.size(); i++) {
			MDataMap mDataMap = columnContentMapList.get(i);
			if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
				String productCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(productCode)) {
					productCodeMap.put(productCode, "");
				}
			} else if (CATEGORY.equals(mDataMap.get("showmore_linktype"))) {
				String categoryCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(categoryCode)) {
					categoryCodeMap.put(categoryCode, "");
				}
			}

			if (StringUtils.isNotEmpty(mDataMap.get("picture"))) {
				picUrlMap.put(mDataMap.get("picture"), "");
			}

			if(!columnContentGroupMap.containsKey(mDataMap.get("column_code"))){
				columnContentGroupMap.put(mDataMap.get("column_code"), new ArrayList<MDataMap>());
			}
			
			columnContentGroupMap.get(mDataMap.get("column_code")).add(mDataMap);
		}
		
		for (int i = 0; i < columnMapList.size(); i++) {
			MDataMap mDataMap = columnMapList.get(i);
			if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
				String productCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(productCode)) {
					productCodeMap.put(productCode, "");
				}
			} else if (CATEGORY.equals(mDataMap.get("showmore_linktype"))) {
				String categoryCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(categoryCode)) {
					categoryCodeMap.put(categoryCode, "");
				}
			}
			
			//导航栏栏目获取模版背景图片字段
			if ("4497471600010004".equals(mDataMap.get("column_type")) && StringUtils.isNotBlank(mDataMap.get("column_bgpic"))) {
				picUrlMap.put(mDataMap.get("column_bgpic"),"");
			}
		}

		List<String> productCodeArr = new ArrayList<String>();
		for (String productCode : productCodeMap.keySet()) {
			productCodeArr.add(productCode);
		}
		List<String> categoryCodeArr = new ArrayList<String>();
		for (String categoryCode : categoryCodeMap.keySet()) {
			categoryCodeArr.add(categoryCode);
		}
		
		Map<String, HomeColumnContentProductInfo> productInfoMap = hcService.getProductInfo(productCodeArr, userType,userCode,isPurchase,channelId);
		MDataMap categoryNameMap = hcService.getCategoryName(categoryCodeArr,sellerCode);
		
		
		//获取商品信息中的商品图片，用来进行压缩
		for (String productCode : productInfoMap.keySet()) {
			picUrlMap.put(productInfoMap.get(productCode).getMainpicUrl(),"");
		}
		
		List<String> picUrlArr = new ArrayList<String>();
		for (String picUrl : picUrlMap.keySet()) {
			picUrlArr.add(picUrl);
		}
		//压缩图片
		List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), picUrlArr,picType);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			picUrlObjMap.put(picInfo.getPicNewUrl(), picInfo);
		}
		
		for (String productCode : productInfoMap.keySet()) {
			String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
			if (StringUtils.isNotBlank(mainpicUrl)) {
				productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
			}
		}
		
		boolean hasNavUrl = false;
		for (int i = 0; i < columnMapList.size(); i++) {
			MDataMap columnMap = columnMapList.get(i);
			HomeColumn homeColumn = new HomeColumn();
			List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
		
			homeColumn.setShowName(columnMap.get("show_name")); // 是否显示栏目名称
			homeColumn.setColumnName(columnMap.get("column_name"));
			homeColumn.setColumnID(columnMap.get("column_code"));
			//设置页面类型
			homeColumn.setPageType("0");
			homeColumn.setColumnType(columnMap.get("column_type"));
			homeColumn.setStartTime(columnMap.get("start_time"));
			homeColumn.setEndTime(columnMap.get("end_time"));
			homeColumn.setIsShowmore(columnMap.get("is_showmore")); // 是否显示更多
			homeColumn.setHasEdgeDistance(columnMap.get("is_had_edge_distance"));//是否保留边距
			homeColumn.setShowmoreLinktype(columnMap.get("showmore_linktype"));
			homeColumn.setColumnsPerRow(columnMap.get("columns_per_row"));
			homeColumn.setFutureProgram(columnMap.get("future_program"));
			
			//如果是多栏广告，返回选择的栏数
			if ("4497471600010021".equals(homeColumn.getColumnType())) {
				homeColumn.setNumLan(columnMap.get("num_languanggao")==null?"0":columnMap.get("num_languanggao").toString());
			}
			
			if (CATEGORY.equals(columnMap.get("showmore_linktype"))) {
				homeColumn.setShowmoreLinkvalue(categoryNameMap.get(columnMap.get("showmore_linkvalue")));
			} else {
				homeColumn.setShowmoreLinkvalue(columnMap.get("showmore_linkvalue"));
			}
			
			//导航栏栏目类型，并且模版背景图片不为空
			String columnBgpicUrl = columnMap.get("column_bgpic");
			if (StringUtils.isNotBlank(columnBgpicUrl) && "4497471600010004".equals(homeColumn.getColumnType())) {
				if (StringUtils.isNotBlank(picUrlMap.get(columnBgpicUrl))) {
					homeColumn.setColumnBgpic(picUrlObjMap.get(picUrlMap.get(columnBgpicUrl)));
				}
			}
			
			homeColumn.setShowmoreTitle(columnMap.get("showmore_title"));
			if(StringUtils.isNotBlank(columnMap.get("interval_second"))) {	
				homeColumn.setIntervalSecond(Integer.parseInt(columnMap.get("interval_second")));
			}
			
			// 根据栏目类型设置内容
			if ("4497471600010010".equals(homeColumn.getColumnType())){ // TV直播
				contentList = hcService.getTVDataList(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion,isPurchase,systemTime,channelId);
			}else if ("4497471600010016".equals(homeColumn.getColumnType())){ // TV直播滑动
				// 未开始的节目档数
				int futureProgram = 0;
				if(StringUtils.isNotBlank(columnMap.get("future_program"))){
					futureProgram = NumberUtils.toInt(""+DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code",columnMap.get("future_program"))));
					if(futureProgram < 0) futureProgram = 0;
				}
				
				contentList = hcService.getTVDataListNew(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion, futureProgram,isPurchase,systemTime,channelId);
			}else if ("4497471600010011".equals(homeColumn.getColumnType())){ // 闪购
				if("4497471600100003".equals(inputParam.getViewType())){
					contentList = hcService.getFlashActivityPc(sellerCode,maxWidth,picType);
				}else{
					contentList = hcService.getFlashActivityNew(sellerCode,maxWidth,picType,channelId);
				}
				// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
				if (null == contentList
						|| contentList.size() < 1
						|| systemTime.compareTo(contentList.get(0)
								.getEndTime()) > 0) {
					continue;
				} else {
					// 闪购栏目的开始结束时间设置为闪购活动的开始结束时间
					homeColumn.setStartTime(contentList.get(0).getStartTime());
					homeColumn.setEndTime(contentList.get(0).getEndTime());
				}
			}else if ("4497471600010022".equals(homeColumn.getColumnType())){ // 秒杀
				contentList = hcService.getSeckillDataList(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion,isPurchase,systemTime,channelId);
				if(null != contentList && !contentList.isEmpty()) {
					// 秒杀栏目的开始结束时间设置为秒杀活动的开始结束时间
					homeColumn.setStartTime(contentList.get(0).getStartTime());
					homeColumn.setEndTime(contentList.get(0).getEndTime());
				}
				
			}else if("4497471600010023".equals(homeColumn.getColumnType())) {//拼团
				//contentList = hcService.getCollageDataList(sellerCode, columnMap.get("event_code"), columnMap.get("show_num"), maxWidth,systemTime);
				int pageCount = hcService.getCollageDataPageCount(sellerCode, columnMap.get("event_code"), columnMap.get("show_num"),systemTime);
				contentList = hcService.getCollageDataList(sellerCode, columnMap.get("event_code"), 0, columnMap.get("show_num"), 1, maxWidth,systemTime);
			}else{ // 默认处理
				List<MDataMap> itemList = columnContentGroupMap.get(columnMap.get("column_code"));
				
				// 设置默认值，避免空指针异常
				if(itemList == null){
					itemList = new ArrayList<MDataMap>();
				}
				for(int  j =  0;j<itemList.size();j++){
					MDataMap columnContentMap  =  itemList.get(j);
					int posion = Integer.parseInt(columnContentMap.get("position"));
					HomeColumnContent columnContent = new HomeColumnContent();
					// 压缩图片
					String pic = picUrlMap.get(columnContentMap.get("picture"));
					columnContent.setPicture(StringUtils.isEmpty(pic) ? "" : pic);
					columnContent.setStartTime(columnContentMap.get("start_time"));
					columnContent.setEndTime(columnContentMap.get("end_time"));
					columnContent.setTitle(columnContentMap.get("title"));
					columnContent.setTitleColor(columnContentMap.get("title_color"));
					columnContent.setDescription(columnContentMap.get("description"));
					columnContent.setDescriptionColor(columnContentMap.get("description_color"));
					columnContent.setShowmoreLinktype(columnContentMap.get("showmore_linktype"));
					columnContent.setFloorModel(columnContentMap.get("floor_model"));
					String showmore_linkvalue = columnContentMap.get("showmore_linkvalue");
					if (StringUtils.isNotBlank(showmore_linkvalue) && "449746250001".equals(columnContentMap.get("skip_place"))) {
						String place_time = columnContentMap.get("place_time");
						if (StringUtils.isNotBlank(place_time)) {
								try {
									Long timeLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(place_time).getTime();
									String timeParam = "";
									if (showmore_linkvalue.indexOf("?") > 0) {
										timeParam = "&time=";
									}else{
										timeParam = "?time=";
									}
									showmore_linkvalue += (timeParam+timeLong.toString());
								} catch (ParseException e) {
									e.printStackTrace();
								}
						}
					}
					
					if (CATEGORY.equals(columnContentMap.get("showmore_linktype"))) {
						columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue));
					} else {
						columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(showmore_linkvalue) ? "" : showmore_linkvalue);
					}
					columnContent.setPosion(Integer.parseInt(StringUtils.isEmpty(columnContentMap.get("position")) ? "0" : columnContentMap.get("position")));
					columnContent.setIsShare(columnContentMap.get("is_share"));
					int firstHight = 0;
					//栏目类型为一栏广告/轮播图时，返回图片高度   
					if ("4497471600010002".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							|| "4497471600010001".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							) {
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							columnContent.setPicHeight(null == oneColumnPic ? 0 : oneColumnPic.getHeight());
					}
					
					//两栏不限高
					if ("4497471600010003".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							) {
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							if(posion==1) {
								firstHight = oneColumnPic.getHeight()/2;
								columnContent.setPicOriginHeight(null == oneColumnPic ? 0 : firstHight);
							}
					}
					//多栏不限高
					if ("4497471600010021".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							) {
							String string = columnMap.get("num_languanggao");
							int divideNum = 1;
							if("4497471600360002".equals(string)) {
								divideNum = 2;
							}else if("4497471600360003".equals(string)) {
								divideNum = 3;
							}else if("4497471600360004".equals(string)) {
								divideNum = 4;
							}
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							if(posion==1) {
								firstHight = oneColumnPic.getHeight()/divideNum;
								columnContent.setPicOriginHeight(null == oneColumnPic ? 0 : firstHight);
							}
					}
					//右两栏不限高
					if ("4497471600010006".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							) {
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							if(posion==1) {
								firstHight = oneColumnPic.getHeight()/2;
								columnContent.setPicOriginHeight(null == oneColumnPic ? 0 : firstHight);
							}
					}
					//左两栏不限高
					if ("4497471600010007".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
							) {
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							if(posion==3) {
								firstHight = oneColumnPic.getHeight()/2;
								columnContent.setPicOriginHeight(null == oneColumnPic ? 0 : firstHight);
							}
					}
					
				
					// 直播视频列表需要设置商品的名称和价格
					if("4497471600010017".equals(homeColumn.getColumnType())){
						columnContent.getProductInfo().setProductCode(columnContentMap.get("product_code"));
						columnContent.getProductInfo().setProductName(columnContentMap.get("product_name"));
						columnContent.getProductInfo().setSellPrice(MoneyHelper.format(new BigDecimal(columnContentMap.get("product_price"))));
						columnContent.getProductInfo().setMainpicUrl(StringUtils.trimToEmpty(pic));
						
						// 取商品下SKU的最低价
						if(StringUtils.isNotBlank(columnContent.getProductInfo().getProductCode())){
							PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(columnContent.getProductInfo().getProductCode()));
							BigDecimal price = null;
							for(PlusModelProductSkuInfo skuInfo : productInfo.getSkuList()){
								if(price == null){
									price = skuInfo.getSellPrice();
								}else if(price.compareTo(skuInfo.getSellPrice()) > 0){
									price = skuInfo.getSellPrice();
								}
							}
							if(price != null){
								columnContent.getProductInfo().setSellPrice(MoneyHelper.format(price));
							}
						}
					}
					
					// 直播视频模板需要设置视频的广告语和视频真实地址
					if("4497471600010020".equals(homeColumn.getColumnType())){
						columnContent.getProductInfo().setVideoAd(columnContentMap.get("video_ad"));
						String ccvid = columnContentMap.get("video_link");
						String videoLink = hcService.getVideoLinkUrlFromApi(ccvid);
						columnContent.setVideoLink(videoLink);
						columnContent.getProductInfo().setProductCode(columnContentMap.get("product_code"));
						columnContent.getProductInfo().setProductName(columnContentMap.get("product_name"));
						columnContent.getProductInfo().setSellPrice(MoneyHelper.format(new BigDecimal(columnContentMap.get("product_price"))));
						columnContent.getProductInfo().setMainpicUrl(StringUtils.trimToEmpty(pic));
						String eventcode = "";
						String eventtype = "";
						
						// 取商品下SKU的最低价
						if(StringUtils.isNotBlank(columnContent.getProductInfo().getProductCode())){
							PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(columnContent.getProductInfo().getProductCode()));
							BigDecimal price = null;
							for(PlusModelProductSkuInfo skuInfo : productInfo.getSkuList()){
								
								PlusModelSkuInfo tempinfo = supportProduct.upSkuInfoBySkuCode(skuInfo.getSkuCode(),userCode,"",isPurchase);
								eventcode = tempinfo.getEventCode();
								eventtype = tempinfo.getEventType();
								if(price == null){
									price = tempinfo.getSellPrice();
									
								}else if(price.compareTo(tempinfo.getSellPrice()) > 0){
									price = tempinfo.getSellPrice();
								}
								if(StringUtils.isNotBlank(eventcode)&&!eventtype.equals("4497472600010001")){
									columnContent.getProductInfo().setIsActivity("活动价");
								}
							}
							if(price != null){
								columnContent.getProductInfo().setSellPrice(MoneyHelper.format(price));
							}
							PlusModelEventSale eventSale = new PlusSupportEvent().upEventSalueByMangeCode(getManageCode(),channelId);
							List<PlusModelFullCutMessage> sale = new PlusServiceSale().getEventMessage(columnContent.getProductInfo().getProductCode(), eventSale,userCode);
							if(sale.size()>0){
								columnContent.getProductInfo().setIsActivity("活动价");
							}
						}
												
					}
					
					// 右两栏闪购
					if("4497471600010018".equals(homeColumn.getColumnType())){
						List<HomeColumnContent> content;
						if("4497471600100003".equals(inputParam.getViewType())){
							content = hcService.getFlashActivityPc(sellerCode,maxWidth,picType);
						}else{
							content = hcService.getFlashActivityNew(sellerCode,maxWidth,picType,channelId);
						}
						
						if (content == null || content.isEmpty() || systemTime.compareTo(content.get(0).getEndTime()) > 0) {
							continue;
						}
						
						homeColumn.setStartTime(content.get(0).getStartTime());
						homeColumn.setEndTime(content.get(0).getEndTime());
					}
					
					// 链接类型为“商品详情”时查询商品信息
					if (PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))&&!"4497471600010020".equals(homeColumn.getColumnType())) {
						HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
						//524:需要添加商品的分类标签
						PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productInfo.getProductCode()));
						String ssc =plusModelProductinfo.getSmallSellerCode();
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
						productInfo.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
						
						// 商品活动标签，仅限部分栏目类型：一栏多行
						if("4497471600010025".equals(homeColumn.getColumnType())) {
							productInfo.setTagList(pService.getTagListByProductCode(productInfo.getProductCode(), userCode,channelId));

							// 忽略下架商品
							if(!"4497153900060002".equals(plusModelProductinfo.getProductStatus())) {
								continue;
							}
							
							PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
							skuQuery.setCode(productInfo.getProductCode());
							skuQuery.setMemberCode(userCode);
							skuQuery.setIsPurchase(isPurchase);
							skuQuery.setChannelId(channelId);
							PlusModelSkuInfo skuInfo = priceService.getProductMinPriceSkuInfo(skuQuery, true).get(productInfo.getProductCode());
							
							// 忽略无库存商品
							if(skuInfo.getMaxBuy() <= 0) {
								continue;
							}
							
							// 活动价大于等于原价时不显示原价
							if(StringUtils.isNotBlank(productInfo.getSellPrice()) && StringUtils.isNotBlank(productInfo.getMarkPrice())) {
								if(new BigDecimal(productInfo.getSellPrice()).compareTo(new BigDecimal(productInfo.getMarkPrice())) >= 0) {
									productInfo.setMarkPrice("");
								}
							}
						}
						
						
						// 注释掉下面这个过滤，见bug#5756
						// if (null == productInfo ||
						// StringUtils.isEmpty(productInfo.getProductCode()))
						// {
						// //加这个if为了过滤掉已下架的商品
						// continue;
						// }
						columnContent.setProductInfo(productInfo);
					} else if (URL.equals(columnContentMap.get("showmore_linktype")) && "449746250001".equals(columnContentMap.get("is_share"))) {
						if (VersionHelper.checkServerVersion("3.5.51.54")) {
							// 链接类型为"URL"时并且是否分享为"是"时,设置“分享图片”，“分享标题”，“分享内容”，“分享链接”
							columnContent.setShareTitle(columnContentMap.get("share_title"));
							columnContent.setShareContent(columnContentMap.get("share_content"));
							columnContent.setShareLink(columnContentMap.get("share_link"));
							columnContent.setSharePic(columnContentMap.get("share_pic"));

							// “分享链接需要进行拼接重写”
							// http://share-static.sycdn.ichsy.com/cfamily/web/cfamilypage/specialshare?link=http://share.ichsy.com/index/20150525mr.html&alt=%E6%97%85%E8%A1%8C%E5%A6%86%E5%A4%87
							String shareUrlHead = bConfig("familyhas.share_url_head");
							String alt = "&wxTilte=";
							String shareTitle = columnContentMap.get("share_title");
							if (StringUtils.isEmpty(shareTitle)) {
								shareTitle = bConfig("familyhas.share_title");
							}
							String shareURL = columnContentMap.get("share_link");
							// 为了兼容历史数据分享链接为空的情况
							if (StringUtils.isEmpty(shareURL)) {
								shareURL = showmore_linkvalue;
							}
							String shareLink = shareUrlHead;
							try {
								shareLink += (URLEncoder.encode((shareURL.trim()),"utf8")+alt+URLEncoder.encode(shareTitle,"utf8"));
							} catch (UnsupportedEncodingException e) {
							}
							columnContent.setShareLink(shareLink);
						}
					}
					
					contentList.add(columnContent);
				}
			}
			
			homeColumn.setContentList(contentList); // 内容List
			
			if (null != homeColumn.getContentList() && homeColumn.getContentList().size() > 0) {
				columnList.add(homeColumn);
			} else if(columnMap.get("column_type").equals("4497471600010019")) {
				//导航栏URL-栏目没有内容
				columnList.add(homeColumn);
				hasNavUrl = true;
			}
		}
		
		if(!hasNavUrl) {
			result.setMaybeLove(navMap != null ? navMap.get("flag") : "");
		} else {
			result.setMaybeLove("4497480100020002"); 
		}
		
		result.setColumnList(columnList);
		return result;
	}

	

}
