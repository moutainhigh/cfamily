package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiHomeColumnNewInput;
import com.cmall.familyhas.api.model.BuyerShow;
import com.cmall.familyhas.api.model.EvaProduct;
import com.cmall.familyhas.api.model.EvaluationImg;
import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiHomeColumnNewResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有首页版式栏目(APP,WAP端调用)
 * @author zhaojunling
 * 
 */
public class ApiHomeColumnNew extends RootApiForVersion<ApiHomeColumnNewResult, ApiHomeColumnNewInput> {
	
	private final String PRODUCT_VIEW = "4497471600020004"; 	// 链接类型为商品详情
	private final String CATEGORY = "4497471600020003"; 		// 链接类型为分类
	private final String URL = "4497471600020001"; 				// 链接类型为URL
	private final String BRAND = "4497471600020014"; 				// 链接类型为URL
	
	
	// 支持智能推荐的栏目类型
	static String[] PRODUCT_MAINTENANCE_TYPES = {"4497471600010008","4497471600010013","4497471600010014","4497471600010025"};
	
	static LoadProductInfo loadProductInfo = new LoadProductInfo();
	static PlusSupportProduct supportProduct = new PlusSupportProduct();
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	static PlusSupportStock supportStock = new PlusSupportStock();
	
	static ProductPriceService priceService = new ProductPriceService();
	
	public ApiHomeColumnNewResult Process(ApiHomeColumnNewInput inputParam,
			MDataMap mRequestMap) {
		if (StringUtils.isBlank(inputParam.getBuyerType())) {
			inputParam.setBuyerType("4497469400050002");
		}
		if (StringUtils.isBlank(inputParam.getViewType())) {
			inputParam.setViewType("4497471600100001");
		}
		ApiHomeColumnNewResult result = new ApiHomeColumnNewResult();
		List<HomeColumn> columnList = new ArrayList<HomeColumn>();
		HomeColumnService hcService = new HomeColumnService();
		ProductLabelService labelService = new ProductLabelService();
		String systemTime = DateUtil.getSysDateTimeString();
		ProductService pService = new ProductService();
        OrderService orderService = new OrderService();
		if(StringUtils.isNotBlank(inputParam.getNavCode())) {
			inputParam.setNavCode(inputParam.getNavCode().replace("_1", ""));
		}
		if(StringUtils.isNotBlank(inputParam.getYuLan_navCode())) {
			inputParam.setNavCode(inputParam.getYuLan_navCode().replace("_1", ""));
		}
		
		Integer isPurchase  = inputParam.getIsPurchase(); 
		String sellerCode = getManageCode();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? "" : inputParam.getPicType(); // 图片格式
		String userType = inputParam.getBuyerType();
		String channelId = getChannelId();
		// 后台预览导航栏目专用入参
		String yuLan_column_type = inputParam.getYuLan_column_type();
		String yuLan_navCode = inputParam.getYuLan_navCode();
		String yuLan_column_name = "";
		String yuLan_time_point = "";
		try {
			yuLan_time_point = URLDecoder.decode(inputParam.getYuLan_time_point(), "UTF-8");
			yuLan_column_name = URLDecoder.decode(inputParam.getYuLan_column_name(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String appVersion = "";
		String uniqid = "";
		if(this.getApiClient() != null && StringUtils.isNotBlank(this.getApiClient().get("app_vision"))){
			appVersion = this.getApiClient().get("app_vision");
			uniqid = this.getApiClient().get("uniqid");
		}
		if("".equals(appVersion)) {
			appVersion = "5.6.4";
		}
		
		//viewType 
		//4497471600100001:APP端
		//4497471600100002:WAP页position
		//4497471600100003:PC端
		String sFields = " column_name_corlor,category_codes,category_limit,product_maintenance,column_code,column_name,column_type,start_time,end_time,is_showmore,showmore_title,interval_second,showmore_linktype,"
				+ " showmore_linkvalue,show_name,pic_url,column_bgpic,is_had_edge_distance,columns_per_row,future_program,num_languanggao,show_num,event_code,show_style";
		String sOrders = " position asc , create_time desc";
		String sWhere = "";
		if("".equals(yuLan_navCode)) {
			// 如果预览专用导航编码是空,则表明不是预览
			//按viewType查询未删除,已发布,当前时间在设定的开始时间和结束时间之内的所有首页栏目,结果按位置列正序,创建时间倒序
			sWhere = " start_time <= '" + systemTime	+ "' and end_time > '" + systemTime	+ "' and is_delete='449746250002' "
					+ " and release_flag='449746250001' and seller_code='"+sellerCode+"' "
					+ " and nav_code = '"+inputParam.getNavCode()+"' and view_type in ('" + inputParam.getViewType() + "','4497471600100005')";
		}else {
			inputParam.setNavCode(yuLan_navCode);
			String viewType = inputParam.getViewType();
			String viewTypeString = "";
			if(!"".equals(viewType)) {
				viewTypeString = " and view_type in ('" + viewType + "','4497471600100005') ";
			}
			
			if("".equals(yuLan_time_point)) {
				yuLan_time_point = systemTime;
			}
			sWhere = " start_time <= '" + yuLan_time_point	+ "' and end_time > '" + yuLan_time_point	+ "' and is_delete='449746250002' "
					+ "  and seller_code='"+sellerCode+"' "
					+ " and nav_code = '"+inputParam.getNavCode()+"' " + viewTypeString;
			if(!"".equals(yuLan_column_name)) {
				sWhere += " and column_name = '"+yuLan_column_name+"' ";
			}
			if(!"".equals(yuLan_column_type)) {
				sWhere += " and column_type = '"+yuLan_column_type+"' ";
			}
		}
		
		List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column").queryAll(sFields, sOrders, sWhere, null);

		// 558首页新增商品评价模板 商品评价 栏目map
		MDataMap evaMap = new MDataMap();
		// 564买家秀列表
		MDataMap buyerShowMap = new MDataMap();
		// 商品评价 和 买家秀列表  谁在前面展示谁
		if (columnMapList != null && columnMapList.size() > 0) {
			for (MDataMap columnMap : columnMapList) {
				// 如果该首页导航中的栏目包含  商品评价  模板,则其他栏目都不返回,只返回  商品评价  模板内容
				if("4497471600010027".equals(columnMap.get("column_type"))) {
					evaMap = columnMap;
					break;
				}else if ("4497471600010029".equals(columnMap.get("column_type"))) {
					buyerShowMap = columnMap;
					break;
				}
			}
		}
		// 如果包含  商品评价  模板内容,走  商品评价模板结果返回逻辑
		if(evaMap != null && evaMap.size() > 0) {
			HomeColumn homeColumn = new HomeColumn();
			List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
			
			homeColumn.setColumnName(evaMap.get("column_name"));
			homeColumn.setColumnID(evaMap.get("column_code"));
			//设置页面类型
			homeColumn.setPageType("0");
			homeColumn.setStartTime(evaMap.get("start_time"));
			homeColumn.setEndTime(evaMap.get("end_time"));
			homeColumn.setColumnType(evaMap.get("column_type"));
			homeColumn.setIsShowmore(evaMap.get("is_showmore")); // 是否显示更多
			//homeColumn.setShowmoreTitle(evaMap.get("showmore_title"));
			//homeColumn.setShowmoreLinktype(evaMap.get("showmore_linktype"));
			//homeColumn.setShowmoreLinkvalue(evaMap.get("showmore_linkvalue"));
			if(StringUtils.isNotBlank(evaMap.get("interval_second"))) {	
				homeColumn.setIntervalSecond(Integer.parseInt(evaMap.get("interval_second")));
			}
			homeColumn.setShowName(evaMap.get("show_name")); // 是否显示栏目名称
			homeColumn.setHasEdgeDistance(evaMap.get("is_had_edge_distance"));//是否保留边距
			homeColumn.setColumnsPerRow(evaMap.get("columns_per_row"));
			//homeColumn.setFutureProgram(evaMap.get("future_program"));
			
			// 查询所有商品评价模板维护的商品
			List<Map<String, Object>> evaProdList = DbUp.upTable("fh_apphome_evaluation").
					dataSqlList("SELECT * FROM fh_apphome_evaluation WHERE is_delete = '0' AND location_num > 0 ORDER BY location_num ASC LIMIT 20", new MDataMap());
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
				skuQuery.setIsPurchase(isPurchase);
				skuQuery.setChannelId(channelId);
				// 获取商品最低销售价格和对应的划线价
				Map<String,PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
				if(null != productPriceMap.get(product_code)) {
					BigDecimal sellPrice1 = productPriceMap.get(product_code).getSellPrice();
					// 销售价
					prodInfo.setSellPrice(MoneyHelper.format(sellPrice1));
					String eventType = productPriceMap.get(product_code).getEventType();
					if("4497472600010001".equals(eventType) || "4497472600010002".equals(eventType) || "4497472600010005".equals(eventType) 
							 || "4497472600010018".equals(eventType) || "4497472600010024".equals(eventType)) {						
						BigDecimal skuPrice1 = productPriceMap.get(product_code).getSkuPrice();
						if(skuPrice1.compareTo(sellPrice1) > 0) {
							// 划线价
							prodInfo.setMarkPrice(MoneyHelper.format(skuPrice1));
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
					prodInfo.setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
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
				
				//String is_edit = map.get("is_edit")+"";
				//if("1".equals(is_edit)) {
				// 如果修改过,返回表中存的数据
				//String evaluation_uid = map.get("evaluation_uid")+"";
				String order_assessment = map.get("order_assessment")+"";
				String cover_img = map.get("cover_img")+"";
				columnContent.setPicture(cover_img);
				prodInfo.setEvaluationContent(order_assessment);
				// 封面图
				//压缩图片
				List<String> picUrlArr = new ArrayList<String>();
				picUrlArr.add(cover_img);
				List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt("570"), picUrlArr,"");
				if(picInfoList != null) {
					PicInfo picInfo = picInfoList.get(0);
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
			homeColumn.setContentList(contentList);
			
			// 首页定位栏目链接类型
			homeColumn.setHomePositionLinkType("4497471600580001");
			// 区分猜你喜欢还是商品评价
			homeColumn.setGuessLikeOrEvaluation("4497471600590002");
			
			columnList.add(homeColumn);
			
			//result.setColumnList(columnList);
			// 隐藏猜你喜欢
			result.setMaybeLove("4497480100020002");

		}else if(buyerShowMap != null && buyerShowMap.size() > 0) {
			HomeColumn homeColumn = new HomeColumn();
			List<BuyerShow> buyerShowList = new ArrayList<BuyerShow>();

			homeColumn.setColumnName(buyerShowMap.get("column_name"));
			homeColumn.setColumnID(buyerShowMap.get("column_code"));
			// 设置页面类型
			homeColumn.setPageType("0");
			homeColumn.setStartTime(buyerShowMap.get("start_time"));
			homeColumn.setEndTime(buyerShowMap.get("end_time"));
			homeColumn.setColumnType(buyerShowMap.get("column_type"));
			homeColumn.setIsShowmore(buyerShowMap.get("is_showmore")); // 是否显示更多
			// homeColumn.setShowmoreTitle(evaMap.get("showmore_title"));
			// homeColumn.setShowmoreLinktype(evaMap.get("showmore_linktype"));
			// homeColumn.setShowmoreLinkvalue(evaMap.get("showmore_linkvalue"));
			if (StringUtils.isNotBlank(buyerShowMap.get("interval_second"))) {
				homeColumn.setIntervalSecond(Integer.parseInt(buyerShowMap.get("interval_second")));
			}
			homeColumn.setShowName(buyerShowMap.get("show_name")); // 是否显示栏目名称
			homeColumn.setHasEdgeDistance(buyerShowMap.get("is_had_edge_distance"));// 是否保留边距
			homeColumn.setColumnsPerRow(buyerShowMap.get("columns_per_row"));
			// homeColumn.setFutureProgram(evaMap.get("future_program"));

			// 是否显示评价或晒单送积分顶部悬浮提醒框
			if(!"".equals(userCode)) {
				// 查询用户有没有待评价和好评待晒图的单
				String sql = "SELECT DISTINCT oi.order_code,od.product_code,od.sku_code,od.sku_num,pi.product_name,sku.sku_keyvalue,pi.mainpic_url,sku.sku_picurl,pi.small_seller_code FROM ordercenter.oc_orderinfo oi"
						   + " LEFT JOIN ordercenter.oc_orderdetail od ON oi.order_code = od.order_code"
						   + " LEFT JOIN productcenter.pc_skuinfo sku ON sku.sku_code = od.sku_code"
						   + " LEFT JOIN productcenter.pc_productinfo pi ON sku.product_code = pi.product_code"
						   + " WHERE od.gift_flag = '1' AND pi.product_name != '' AND oi.delete_flag = '0' AND oi.buyer_code = :buyer_code AND order_source not in('449715190014','449715190037') AND oi.order_status = '4497153900010005'"
						   + " AND oi.order_type NOT IN(" + new OrderService().getNotInOrderType() + ")";
				// 待评价订单
				String sql1 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001') = 0  AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) IS NULL) "
							+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
				// 待晒单则是为上传图片的评价
				String sql2 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code  AND noe.grade > 3 AND noe.evaluation_status_user = '449746810001' AND noe.auto_good_evaluation_flag = 0 AND oder_photos = '' AND ccvids = '') > 0 AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) IS NULL) "
							+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
				MDataMap param = new MDataMap("buyer_code", userCode);
				Map<String, Object> totalMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql1 + ")t", param);
				Map<String, Object> totalMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql2 + ")t", param);
				int intValue1 = MapUtils.getIntValue(totalMap1, "total");
				int intValue2 = MapUtils.getIntValue(totalMap2, "total");
				
				List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip", "", "", new MDataMap());
				int integral1 = 0;
				int integral2 = 0;
				for(MDataMap map:evaluateList){
					integral1 += Integer.parseInt(map.get("integral_value"));
					if(map.get("evaluate_type").equals("评价图片")){
						integral2 += MapUtils.getIntValue(map, "integral_value");
					}
					if(map.get("evaluate_type").equals("买家秀")){
						integral2 += MapUtils.getIntValue(map, "integral_value");
					}
				}
				int totalIntegral = intValue1 * integral1 + intValue2 * integral2;
				if(totalIntegral > 0) {
					String canEvaluateMainpic = "";
					if(intValue1 > 0) {
						Map<String, Object> picMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url  FROM (" + sql1 + " ORDER BY oi.zid desc LIMIT 1) t", param);
						canEvaluateMainpic = MapUtils.getString(picMap1, "mainpic_url");
						homeColumn.setTagType("1");
					}else if(intValue2 > 0){
						Map<String, Object> picMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url FROM (" + sql2 + " ORDER BY oi.zid desc LIMIT 1) t", param);						
						canEvaluateMainpic = MapUtils.getString(picMap2, "mainpic_url");
						homeColumn.setTagType("2");
					}
					homeColumn.setTotal_integral(totalIntegral+"");
					homeColumn.setIsRemind("1");
					homeColumn.setCanEvaluateMainpic(canEvaluateMainpic);
				}
			}
			
			// 查询买家秀列表第一页(前十条)数据
			String buyerShowSql = "SELECT * FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ORDER BY b.create_time DESC LIMIT 10";
			List<Map<String, Object>> buyerShowMapList = DbUp.upTable("nc_buyer_show_info").dataSqlList(buyerShowSql, new MDataMap());
			for (Map<String, Object> map : buyerShowMapList) {
				BuyerShow buyerShow = new BuyerShow();
				
				// 买家秀uid
				String buyerShowUid = MapUtils.getString(map, "uid");
				// 评价uid
				String evaluation_uid = MapUtils.getString(map, "evaluation_uid");
				Map<String, Object> dataSqlOne = DbUp.upTable("nc_order_evaluation").dataSqlOne("SELECT user_mobile, order_name, oder_creattime, order_assessment, product_code, oder_photos, ccvids, ccpics FROM nc_order_evaluation WHERE uid = '"+evaluation_uid+"'", new MDataMap());
				MDataMap order_evaluation = new MDataMap(dataSqlOne);
				// 评论发表时间
				String oder_creattime = order_evaluation.get("oder_creattime");
				// 晒单内容
				String orderAssessment = order_evaluation.get("order_assessment");
				// 评价人编号
				String memberCode = order_evaluation.get("order_name");
				String user_mobile = order_evaluation.get("user_mobile");
				// 晒单人头像
				String avatar = "";
				// 晒单人昵称
				String nickname = "";
				if("MI150824100141".equals(memberCode)) {
					nickname = user_mobile.substring(0, 3) + "****" + user_mobile.substring(7);
				}else {
					Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"' ORDER BY last_update_time DESC LIMIT 1", new MDataMap());
					if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
						// 如果昵称是空,查询手机号
						Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003'", new MDataMap());
						if(null != login_info) {
							nickname = (String) login_info.get("login_name");
							if(hcService.isPhone(nickname)) {
								nickname = nickname.substring(0, 3) + "****" + nickname.substring(7);
							}
						}
					}else { // 如果昵称不是空
						nickname = (String) member_sync.get("nickname");
					}
					// 头像
					if(null != member_sync && null != member_sync.get("avatar") && !"".equals(member_sync.get("avatar"))){
						avatar = (String) member_sync.get("avatar");
					}
				}
				
				// 买家秀点赞量
				int approveCount = DbUp.upTable("nc_buyer_show_approve").count("buyer_show_or_buyer_show_eva_uid",buyerShowUid);
				String approveNum = "0";
				if(approveCount > 999) {
					approveNum = "999+";
				}else {
					approveNum = approveCount+"";
				}
				// 买家秀阅读量
				String readNum = "0";
				int readCount = DbUp.upTable("nc_buyer_show_read").count("buyer_show_uid",buyerShowUid);
				if(readCount > 99999) {
					readNum = "10w+";
				}else {
					readNum = readCount+"";
				}
				// 买家秀评价量
				String evaluationNum = "0";
				int evaluationCount = DbUp.upTable("nc_buyer_show_evaluation").count("buyer_show_uid",buyerShowUid);
				if(evaluationCount > 999) {
					evaluationNum = "999+";
				}else {
					evaluationNum = evaluationCount+"";
				}
				// 买家秀商品信息
				EvaProduct evaProduct = new EvaProduct();
				String product_code = order_evaluation.get("product_code");
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				List<String> productCodeArr = new ArrayList<String>();
				productCodeArr.add(product_code);
				skuQuery.setCode(StringUtils.join(productCodeArr, ","));
				skuQuery.setMemberCode(userCode);
				skuQuery.setIsPurchase(isPurchase);
				skuQuery.setChannelId(channelId);
				// 获取商品最低销售价格和对应的划线价
				Map<String, PlusModelSkuInfo> productPriceMap = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery);
				if (null != productPriceMap.get(product_code)) {
					BigDecimal sellPrice1 = productPriceMap.get(product_code).getSellPrice();
					// 销售价
					evaProduct.setSellPrice(MoneyHelper.format(sellPrice1));
					BigDecimal skuPrice1 = productPriceMap.get(product_code).getSkuPrice();
					if (skuPrice1.compareTo(sellPrice1) > 0) {
						// 划线价
						evaProduct.setMarkPrice(MoneyHelper.format(skuPrice1));
					}
				} else {
					PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(product_code));
					// 销售价
					evaProduct.setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
				}
				MDataMap productinfo = DbUp.upTable("pc_productinfo").one("product_code",product_code);
				evaProduct.setProductCode(product_code);
				if(productinfo != null) {
					evaProduct.setProductName(productinfo.get("product_name"));
					evaProduct.setMainpicUrl(productinfo.get("mainpic_url"));
				}
				
				// 买家秀图片或者视频list
				List<EvaluationImg> evaluationImgList = new ArrayList<EvaluationImg>();
				// 评论图片
				String oder_photos = order_evaluation.get("oder_photos");
				String ccvids = order_evaluation.get("ccvids");
				String ccpics = order_evaluation.get("ccpics");
				if(!"".equals(ccvids) && !"".equals(ccpics)) {
					// 有评论视频
					EvaluationImg evaImg = new EvaluationImg();
					evaImg.setEvaluationImgUrl(ccpics);
					evaImg.setIsVideo("1");
					evaImg.setCcvid(ccvids);
					evaluationImgList.add(evaImg);
				}
				if(!oder_photos.equals("")) {
					// 有评论图片
					String[] photos = oder_photos.split("\\|");			
					for (String photo : photos) {
						EvaluationImg evaluationImg = new EvaluationImg();
						evaluationImg.setCcvid("");
						evaluationImg.setEvaluationImgUrl(photo);
						evaluationImg.setIsVideo("0");
						evaluationImgList.add(evaluationImg);
					}
				}
				
				// 当前用户是否点赞
				String isApprove = "0";
				if(!"".equals(userCode)) {				
					MDataMap one = DbUp.upTable("nc_buyer_show_approve").one("buyer_show_or_buyer_show_eva_uid",buyerShowUid,"member_code",userCode);
					if(one != null) {
						isApprove = "1";
					}
				}
				// 当前用户是否关注
				String isFollow = "0";
				if(!"".equals(userCode)) {				
					MDataMap one = DbUp.upTable("nc_buyer_show_fans").one("fans_member_code",userCode,"member_code",memberCode);
					if(one != null) {
						isFollow = "1";
					}
				}
				
				// 计算买家秀收益
				String buyerShowMoney = "0";
				Map<String, Object> tgzMoneyMap = DbUp.upTable("fh_tgz_order_detail").dataSqlOne("SELECT sum(tgz_money) buyer_show_money FROM fh_tgz_order_detail WHERE buyer_show_code = '"+buyerShowUid+"' ", new MDataMap());
				if(tgzMoneyMap != null) {
					buyerShowMoney = MapUtils.getString(tgzMoneyMap, "buyer_show_money","0");
					if(new BigDecimal(buyerShowMoney).compareTo(new BigDecimal("0.00")) <= 0) {
						buyerShowMoney = "0";
					}
				}
				
				buyerShow.setApproveNum(approveNum);
				buyerShow.setAvatar(avatar);
				buyerShow.setBuyerShowUid(buyerShowUid);
				buyerShow.setCreateTime(oder_creattime);
				buyerShow.setEvaluationImgList(evaluationImgList);
				buyerShow.setEvaluationNum(evaluationNum);
				buyerShow.setEvaProduct(evaProduct);
				buyerShow.setMemberCode(memberCode);
				buyerShow.setNickname(nickname);
				buyerShow.setOrderAssessment(orderAssessment);
				buyerShow.setReadNum(readNum);
				buyerShow.setIsApprove(isApprove);
				buyerShow.setIsFollow(isFollow);
				buyerShow.setBuyerShowMoney(buyerShowMoney);
				
				buyerShowList.add(buyerShow);

			}
			homeColumn.setBuyerShowList(buyerShowList);
			
			String countSql = "SELECT count(1) num FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ";
			Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_info").dataSqlOne(countSql, new MDataMap());
			double num = MapUtils.getDoubleValue(countMap, "num");
			// 总页数
			int totalPage = (int) Math.ceil(num/10);
			homeColumn.setTotalPage(totalPage);

			columnList.add(homeColumn);

			// 隐藏猜你喜欢
			result.setMaybeLove("4497480100020002");

		} else {
			List<String> cols = new ArrayList<String>((int) (columnMapList.size() / 0.75) + 1);
			HashMap<String, List<String>> oneColumn = new HashMap<String, List<String>>();
			for (MDataMap m : columnMapList) {
				// 需要判断是否采用的只能推荐
				if (ArrayUtils.contains(PRODUCT_MAINTENANCE_TYPES, m.get("column_type")) && "44975017003".equals(m.get("product_maintenance"))) {
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(m.get("category_limit"));
					paramList.add(m.get("category_codes"));
					paramList.add(m.get("show_num"));
					oneColumn.put(m.get("column_code"), paramList);
				}
				cols.add(m.get("column_code"));
				

			}
			
			//查询当前时间在设定的开始时间和结束时间之内的未删除所有首页栏目内容
			String sWhereContent = " start_time <='" + systemTime + "' and end_time > '" + systemTime + "'";
			//如果有预览时间则以预览时间为准 
			if(StringUtils.isNotBlank(yuLan_time_point)) {
				sWhereContent = " start_time <='" + yuLan_time_point + "' and end_time > '" + yuLan_time_point + "'";
			}
			
			sWhereContent += " and is_delete='449746250002'";
			sWhereContent += " and column_code in('"+StringUtils.join(cols, "','")+"')";
			
			List<MDataMap> columnContentMapList = DbUp.upTable("fh_apphome_column_content").queryAll("", "position asc,start_time desc", sWhereContent, null);
			// 获取所有要查询商品信息的商品编号
			MDataMap productCodeMap = new MDataMap();
			// 获取所有要查询的分类编号，key:categoryCode,value:0
			MDataMap categoryCodeMap = new MDataMap();
			// 图片Map，key:picOldUrl，value:picNewUrl
			MDataMap picUrlMap = new MDataMap();
			// 图片Map，key:picOldUrl，value:picNewUrl
			Map<String,PicInfo> picUrlObjMap = new HashMap<String,PicInfo>();
			
			
			//存储一栏多行栏目智能推荐的商品数据
			HashMap<String, List<String>> oneColumnRecommendProductsMap = new HashMap<String,List<String>>();
			if(oneColumn!=null&&oneColumn.keySet().size()>0) {
				for (Iterator<String> iterator = oneColumn.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next().toString();
					List<String> list = oneColumn.get(key);
					String categoryLimit = list.get(0);
					String category_codes = "";
					int show_num = 0;
					if("449748560002".equals(categoryLimit)) {
						//分类限制，获取限制的分类
						 category_codes = list.get(1);
						 show_num = Integer.parseInt(list.get(2).toString());
					}
					List<String> dgRecommendProducts = hcService.getDGRecommendProducts(userCode, category_codes,key,channelId,uniqid,show_num);
					oneColumnRecommendProductsMap.put(key, dgRecommendProducts);
					for (String pCode : dgRecommendProducts) {
						productCodeMap.put(pCode, "");
					}
				}
			}
			 List<String> columnTypeCodeList = new ArrayList<String>();
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
				if (("4497471600010004".equals(mDataMap.get("column_type"))||"4497471600010011".equals(mDataMap.get("column_type"))) && StringUtils.isNotBlank(mDataMap.get("column_bgpic"))) {
					picUrlMap.put(mDataMap.get("column_bgpic"),"");
				}
				//一栏多行通用大图模板，优先使用商品广告图
				if("4497471600010031".equals(mDataMap.get("column_type"))) {
					columnTypeCodeList.add(mDataMap.get("column_code"));
				}					
				
			}
			
			
			Map<String,List<MDataMap>> columnContentGroupMap = new HashMap<String, List<MDataMap>>();
			for (int i = 0; i < columnContentMapList.size(); i++) {
				MDataMap mDataMap = columnContentMapList.get(i);
				if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
					String productCode = mDataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(productCode)) {
						productCodeMap.put(productCode, "");
						//一栏多行通用大模板优先选用商品一般信息修改中的广告图字段
						if(columnTypeCodeList.contains(mDataMap.get("column_code"))) {
							PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
							if(StringUtils.isNotBlank(productInfo.getAdpicUrl())) {
								picUrlMap.put(productInfo.getAdpicUrl(),"");
							}
						}
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
			
			List<String> productCodeArr = new ArrayList<String>();
			for (String productCode : productCodeMap.keySet()) {
				productCodeArr.add(productCode);
			}
			List<String> categoryCodeArr = new ArrayList<String>();
			for (String categoryCode : categoryCodeMap.keySet()) {
				categoryCodeArr.add(categoryCode);
			}
			
			Map<String, HomeColumnContentProductInfo> productInfoMap = hcService.getProductInfo(productCodeArr, userType,userCode,isPurchase, channelId);
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
			LOOP:for (int i = 0; i < columnMapList.size(); i++) {
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
				String showmoreLinkType = columnMap.get("showmore_linktype");
				homeColumn.setShowmoreLinktype(showmoreLinkType);
				homeColumn.setColumnsPerRow(columnMap.get("columns_per_row"));
				homeColumn.setFutureProgram(columnMap.get("future_program"));
				homeColumn.setColumnNameColor(columnMap.get("column_name_corlor"));
				
				//如果是多栏广告，返回选择的栏数
				if ("4497471600010021".equals(homeColumn.getColumnType())) {
					homeColumn.setNumLan(columnMap.get("num_languanggao")==null?"0":columnMap.get("num_languanggao").toString());
				}
				
				if (CATEGORY.equals(showmoreLinkType)) {
					homeColumn.setShowmoreLinkvalue(categoryNameMap.get(columnMap.get("showmore_linkvalue"))==null?"":categoryNameMap.get(columnMap.get("showmore_linkvalue")));
					homeColumn.setCategoryCode(columnMap.get("showmore_linkvalue"));
				} else if("4497471600020015".equals(showmoreLinkType)||"4497471600020009".equals(showmoreLinkType)){
					if(AppVersionUtils.compareTo(appVersion,"5.6.4")<0) {
						homeColumn.setShowmoreLinktype("4497471600020009");
						homeColumn.setShowmoreLinkvalue("4497471600020009");
					}else {
						homeColumn.setShowmoreLinktype(showmoreLinkType);
						homeColumn.setShowmoreLinkvalue(showmoreLinkType);
					}
				}else {
					homeColumn.setShowmoreLinkvalue(columnMap.get("showmore_linkvalue"));
				}
				/**
				 * 544兼容老版本秒杀模板 4497471600010022 秒杀模板 ng++ 2019-05-30
				 */
				if(AppVersionUtils.compareTo(appVersion,"5.4.4")>=0) {
					if("4497471600010022".equals(homeColumn.getColumnType())) {
						homeColumn.setShowmoreLinktype("4497471600020010");
						homeColumn.setShowmoreLinkvalue("4497471600020010");
					}
				}
				
				
				//导航栏栏目类型||闪购栏目类型，并且模版背景图片不为空
				String columnBgpicUrl = columnMap.get("column_bgpic");
				if (StringUtils.isNotBlank(columnBgpicUrl) && ("4497471600010004".equals(homeColumn.getColumnType())||"4497471600010011".equals(homeColumn.getColumnType()))) {
					if (StringUtils.isNotBlank(picUrlMap.get(columnBgpicUrl))) {
						homeColumn.setColumnBgpic(picUrlObjMap.get(picUrlMap.get(columnBgpicUrl)));
					}
				}
				
				homeColumn.setShowmoreTitle(columnMap.get("showmore_title"));
				if(StringUtils.isNotBlank(columnMap.get("interval_second"))) {	
					homeColumn.setIntervalSecond(Integer.parseInt(columnMap.get("interval_second")));
				}
				
				// 根据栏目类型设置内容
				if ("4497471600010010".equals(homeColumn.getColumnType())||"4497471600010036".equals(homeColumn.getColumnType())){ // TV直播或者TV直播列表样式
					contentList = hcService.getTVDataList(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion,isPurchase, channelId);
				}else if ("4497471600010016".equals(homeColumn.getColumnType())){ // TV直播滑动
					// 未开始的节目档数
					int futureProgram = 0;
					if(StringUtils.isNotBlank(columnMap.get("future_program"))){
						futureProgram = NumberUtils.toInt(""+DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code",columnMap.get("future_program"))));
						if(futureProgram < 0) futureProgram = 0;
					}
					
					contentList = hcService.getTVDataListNew(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion, futureProgram,isPurchase, channelId);
				}else if ("4497471600010011".equals(homeColumn.getColumnType())){ // 闪购
					if("4497471600100003".equals(inputParam.getViewType())){
						contentList = hcService.getFlashActivityPc(sellerCode,maxWidth,picType, channelId);
					}else{
						contentList = hcService.getFlashActivityNew(sellerCode,maxWidth,picType,userCode,isPurchase, channelId);
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
                        //如果配置了背景图则返回颜色，否则颜色置空
						if(StringUtils.isBlank(homeColumn.getColumnBgpic().getPicNewUrl())) {
							homeColumn.setColumnNameColor("");
						}
						
					}
				}else if ("4497471600010022".equals(homeColumn.getColumnType())){ // 秒杀
					contentList = hcService.getSeckillDataList(userType,maxWidth,picType,userCode , inputParam.getViewType() , appVersion,isPurchase, channelId,userCode,uniqid);
					if(null != contentList && !contentList.isEmpty()) {
						// 秒杀栏目的开始结束时间设置为秒杀活动的开始结束时间
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());
					}
					
				}else if("4497471600010023".equals(homeColumn.getColumnType())||"4497471600010034".equals(homeColumn.getColumnType())) {//拼团&拼团大图模式
					String memberCode = null;
					if(getOauthInfo() != null) {
						memberCode = getOauthInfo().getUserCode();
					}
					int pageCount = hcService.getCollageDataPageCount(sellerCode, columnMap.get("event_code"), columnMap.get("show_num"), channelId);
					contentList = hcService.getCollageDataList(memberCode, sellerCode, columnMap.get("event_code"), 0, columnMap.get("show_num"), pageCount, maxWidth, channelId);
				}else if("4497471600010024".equals(homeColumn.getColumnType())) {//拼团包邮
					//拼团包邮模板,属于4栏模板
					int pageCount = hcService.getCollageDataPageCount(sellerCode, columnMap.get("event_code"), "4", channelId);//规定一次展示4个
					if(pageCount!=0) {
						if(pageCount==1) {
							contentList = hcService.getCollageDataListNew(userCode, sellerCode, columnMap.get("event_code"), 0, "4", pageCount, "248", channelId);
						}
						else if(pageCount==2) {
							contentList = hcService.getCollageDataListNew(userCode, sellerCode, columnMap.get("event_code"), 0, "8", pageCount, "248", channelId);
						}
						else {//最多划拉三页
							contentList = hcService.getCollageDataListNew(userCode, sellerCode, columnMap.get("event_code"), 0, "12", pageCount, "248", channelId);
						}
					}
					if(null != contentList && !contentList.isEmpty()) {
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());
					} else {
						// 无秒杀商品,不返回此模板
						continue LOOP;
					}
				} else if ("4497471600010030".equals(homeColumn.getColumnType())) { // 买家秀入口
					String showStyle = columnMap.get("show_style");
					//homeColumn.setShowStyle(showStyle);
					if("449748590001".equals(showStyle)) {
						// 样式1:返回最近20条买家秀内容
						List<BuyerShow> buyerShowList = new ArrayList<BuyerShow>();
						String buyerShowSql = "SELECT b.evaluation_uid FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ORDER BY b.create_time DESC LIMIT 20";
						List<Map<String, Object>> buyerShowMapList = DbUp.upTable("nc_buyer_show_info").dataSqlList(buyerShowSql, new MDataMap());
						if(buyerShowMapList != null && buyerShowMapList.size() > 0) {
							for (Map<String, Object> map : buyerShowMapList) {
								BuyerShow buyerShow = new BuyerShow();
								String evaluation_uid = (String) map.get("evaluation_uid");
								MDataMap orderEvaluation = DbUp.upTable("nc_order_evaluation").one("uid",evaluation_uid);
								if(orderEvaluation != null) {
									buyerShow.setOrderAssessment(orderEvaluation.get("order_assessment"));
									MDataMap productinfo = DbUp.upTable("pc_productinfo").one("product_code",orderEvaluation.get("product_code"));
									if(productinfo != null) {
										buyerShow.setProdPicUrl(productinfo.get("mainpic_url"));
										buyerShowList.add(buyerShow);
									}
								}
							}
						}
						homeColumn.setBuyerShowList(buyerShowList);
						homeColumn.setColumnType("449748590001");
					}else if("449748590002".equals(showStyle)){
						// 样式2:展示最新3个买家秀的用户头像
						List<String> buyerShowAvatarList = new ArrayList<String>();
						String avatarSql = "SELECT s.avatar FROM newscenter.nc_buyer_show_info b  LEFT JOIN membercenter.mc_member_sync s ON s.member_code = b.member_code " 
								+ " WHERE b.is_delete = '0' AND b.check_status = '449748580001'  AND s.avatar IS NOT NULL AND s.avatar != '' " 
								+ " ORDER BY b.create_time DESC LIMIT 3";
						List<Map<String, Object>> avatarMapList = DbUp.upTable("nc_buyer_show_info").dataSqlList(avatarSql, new MDataMap());
						if(avatarMapList != null && avatarMapList.size() > 0) {
							for (Map<String, Object> map : avatarMapList) {
								buyerShowAvatarList.add((String) map.get("avatar"));
							}
						}
						homeColumn.setBuyerShowAvatarList(buyerShowAvatarList);
						String buyer_show_icon = bConfig("familyhas.buyer_show_icon");
						homeColumn.setBuyerShowIcon(buyer_show_icon);
						homeColumn.setColumnType("449748590002");
					}else {
						continue LOOP;
					}
				}else{ // 默认处理
					//如果是智能推荐，则用达观推荐的数据
					boolean subflag = false;
					List<String> subList = new ArrayList<>();
					if(oneColumnRecommendProductsMap.containsKey(columnMap.get("column_code"))) {
						subList = oneColumnRecommendProductsMap.get(columnMap.get("column_code"));
						if(subList.size()==0) {
							continue LOOP;
						}
						subflag=true;
					}
					List<MDataMap> itemList = columnContentGroupMap.get(columnMap.get("column_code"));
					
					// 设置默认值，避免空指针异常
					if(itemList == null){
						itemList = new ArrayList<MDataMap>();
					}
					
					if(subflag) {
						for (int k=0 ;k<subList.size();k++) {
							HomeColumnContent columnContent = new HomeColumnContent();
							int posion =k;
							columnContent.setStartTime(columnMap.get("start_time"));
							columnContent.setEndTime(columnMap.get("end_time"));
							columnContent.setShowmoreLinktype("4497471600020004");
							columnContent.setShowmoreLinkvalue(subList.get(k));
							HomeColumnContentProductInfo productInfo = productInfoMap.get(subList.get(k));
							productInfo.setTagList(pService.getTagListByProductCode(productInfo.getProductCode(),userCode, channelId));
							// 忽略下架商品
							if(!"4497153900060002".equals(productInfo.getProductStatus())) {
								continue;
							}
							// 忽略无库存商品
							if(supportStock.upAllStockForProduct(productInfo.getProductCode()) <= 0) {
								continue;
							}
							//压缩图片
							if(StringUtils.isNotBlank(productInfo.getMainpicUrl())){
								productInfo.setMainpicUrl(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, productInfo.getMainpicUrl()).getPicNewUrl());
							}
							columnContent.setProductInfo(productInfo);
							columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(subList.get(k)) ? "" : subList.get(k));
							columnContent.setPosion(posion);
							columnContent.setIsShare("449746250002");
							contentList.add(columnContent);
						}
					}else {
						if("4497471600010035".equals(homeColumn.getColumnType())&&itemList.size()<3) {
							//滑动轮播模板至少三个数据才做展示
							continue;
						}
						Loop:for(int  j =  0;j<itemList.size();j++){
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
							columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue)==null?"":categoryNameMap.get(showmore_linkvalue));
							columnContent.setCategoryCode(showmore_linkvalue);
						} else if(BRAND.equals(columnContentMap.get("showmore_linktype"))) {
							//品牌搜索，返回品牌中文名称（目前只有导航栏可配）
							MDataMap temMap = DbUp.upTable("pc_brandinfo").one("brand_code",columnContentMap.get("showmore_linkvalue"));
							columnContent.setShowmoreLinkvalue(StringUtils.trim(temMap.get("brand_name")));
						}else if(("4497471600020016".equals(columnContentMap.get("showmore_linktype"))||"4497471600020017".equals(columnContentMap.get("showmore_linktype")))&&AppVersionUtils.compareTo(appVersion, "5.6.5") < 0) {
							//直播视频添加版本控制
							continue;
						}else {
							columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(showmore_linkvalue) ? "" : showmore_linkvalue);
						}
						
						
						
						columnContent.setPosion(Integer.parseInt(StringUtils.isEmpty(columnContentMap.get("position")) ? "0" : columnContentMap.get("position")));
						columnContent.setIsShare(columnContentMap.get("is_share"));
						int firstHight = 0;
						//栏目类型为一栏广告/轮播图时，返回图片高度   
						if ("4497471600010002".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())//一栏
								|| "4497471600010001".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
								) {
							PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
							columnContent.setPicHeight(null == oneColumnPic ? 0 : oneColumnPic.getHeight());
						}
						if ("4497471600010008".equals(homeColumn.getColumnType())||"4497471600010013".equals(homeColumn.getColumnType())){//三栏
							if(StringUtils.isNotBlank(columnContent.getPicture())){
								columnContent.setPicture(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, columnContent.getPicture()).getPicNewUrl());
							}
						}
						if ("4497471600010009".equals(homeColumn.getColumnType())||"4497471600010014".equals(homeColumn.getColumnType())){//两栏
							if(StringUtils.isNotBlank(columnContent.getPicture())){
								columnContent.setPicture(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, columnContent.getPicture()).getPicNewUrl());
							}
						}
						//导航栏图片指定规格压缩处理
						if ("4497471600010004".equals(homeColumn.getColumnType())){
							if(StringUtils.isNotBlank(columnContent.getPicture())){
								if("4497480100030001".equals(homeColumn.getColumnsPerRow())) {  //一行四栏
									columnContent.setPicture(pService.getPicInfoOprBig(248, columnContent.getPicture()).getPicNewUrl());
								}else if("4497480100030002".equals(homeColumn.getColumnsPerRow())) {//一行五栏
									columnContent.setPicture(pService.getPicInfoOprBig(200, columnContent.getPicture()).getPicNewUrl());
								}
								
							}
						}
						
						//两栏不限高
						if ("4497471600010003".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())
								) {
							    //两栏图片规格压缩
							    /*PicInfo oneColumnPic = pService.getPicInfoOprBig(570, columnContent.getPicture());
							    columnContent.setPicture(oneColumnPic.getPicNewUrl());*/
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
								int imgWidth = 800;
								int divideNum = 1;
								if("4497471600360002".equals(string)) {
									divideNum = 2;
									imgWidth=570;
								}else if("4497471600360003".equals(string)) {
									divideNum = 3;
									imgWidth=400;
								}else if("4497471600360004".equals(string)) {
									divideNum = 4;
									imgWidth=248;
								}
								PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
								 /*PicInfo oneColumnPic = pService.getPicInfoOprBig(imgWidth, columnContent.getPicture());
								 columnContent.setPicture(oneColumnPic.getPicNewUrl());*/
								if(posion==1) {
									firstHight = oneColumnPic.getHeight()/divideNum;
									columnContent.setPicOriginHeight(null == oneColumnPic ? 0 : firstHight);
								}
						}
						//一栏广告图
						if("4497471600010005".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())) {
							columnContent.setPicture(pService.getPicInfoOprBig(800, columnContent.getPicture()).getPicNewUrl());	
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
								
								// 取商品下SKU的最低价
								if(StringUtils.isNotBlank(columnContent.getProductInfo().getProductCode())){
									PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
									skuQuery.setCode(columnContent.getProductInfo().getProductCode());
									skuQuery.setMemberCode(userCode);
									skuQuery.setChannelId(channelId);
									PlusModelSkuInfo skuInfo = priceService.getProductMinPriceSkuInfo(skuQuery, true).get(columnContent.getProductInfo().getProductCode());
									
									PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(columnContent.getProductInfo().getProductCode()));
									columnContent.getProductInfo().setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
									
									if(skuInfo != null) {
										columnContent.getProductInfo().setSellPrice(MoneyHelper.format(skuInfo.getSellPrice()));
										if(StringUtils.isNotBlank(skuInfo.getEventCode())) {
											columnContent.getProductInfo().setIsActivity("活动价");
											// 售价比原价小时显示划线价
											if(skuInfo.getSellPrice().compareTo(skuInfo.getSkuPrice()) < 0) {
												columnContent.getProductInfo().setMarkPrice(MoneyHelper.format(skuInfo.getSkuPrice()));
											
												// 特定活动类型显示折扣
												if(ArrayUtils.contains(new String[]{"4497472600010018","4497472600010030"}, skuInfo.getEventType())) {
													// 折扣 = 活动价 / 原价 * 100
													columnContent.getProductInfo().setDiscount(skuInfo.getSellPrice().multiply(new BigDecimal(100)).divide(skuInfo.getSkuPrice(),0,BigDecimal.ROUND_HALF_UP).intValue()+"");
													columnContent.getProductInfo().setEventType(skuInfo.getEventType());
													if(columnContent.getProductInfo().getDiscount().endsWith("0")){
														columnContent.getProductInfo().setDiscount(columnContent.getProductInfo().getDiscount().substring(0, columnContent.getProductInfo().getDiscount().length()-1));
													}
												}
											}
										}
									}
								}
								
							}
							
							// 右两栏闪购
							if("4497471600010018".equals(homeColumn.getColumnType())){
								List<HomeColumnContent> content;
								if("4497471600100003".equals(inputParam.getViewType())){
									content = hcService.getFlashActivityPc(sellerCode,maxWidth,picType, channelId);
								}else{
									content = hcService.getFlashActivityNew(sellerCode,maxWidth,picType,userCode,isPurchase, channelId);
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
								
								// 商品活动标签，仅限部分栏目类型：一栏多行，一栏多行通用大图模板
								if("4497471600010025".equals(homeColumn.getColumnType())||"4497471600010031".equals(homeColumn.getColumnType())) {
									productInfo.setTagList(pService.getTagListByProductCode(productInfo.getProductCode(),userCode, channelId));
									
									if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
										List<TagInfo> tagInfoList = pService.getProductTagInfoList(productInfo.getProductCode(), userCode,channelId);
										productInfo.setTagInfoList(tagInfoList);
									}
									
									// 忽略下架商品
									if(!"4497153900060002".equals(productInfo.getProductStatus())) {
										continue;
									}
									
									// 忽略无库存商品
									if(supportStock.upAllStockForProduct(productInfo.getProductCode()) <= 0) {
										continue;
									}
									//一栏多行通用大图模板还需要视频广告，连接，下单量
									if("4497471600010031".equals(homeColumn.getColumnType())) {
										//columnContent.getProductInfo().setVideoAd(columnContentMap.get("video_ad"));
										productInfo.setVideoAd(columnContentMap.get("video_ad"));
										String ccvid = columnContentMap.get("video_link");
										String videoLink = hcService.getVideoLinkUrlFromApi(ccvid);
										columnContent.setVideoLink(videoLink);
										PlusModelProductInfo subProductInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(columnContentMap.get("product_code")));
										if(StringUtils.isNotBlank(subProductInfo.getAdpicUrl())) {
											String picStr = picUrlMap.get(subProductInfo.getAdpicUrl());
											columnContent.setPicture(picStr);
										}
										//兼容小前段统一字段获取处理
										productInfo.setMainpicUrl(columnContent.getPicture());
										columnContent.setPicUrl(columnContent.getPicture());
										//销量  真实销量+虚拟销量
										int productOrderNum = orderService.getProductOrderNum(subProductInfo.getProductCode())+subProductInfo.getFictitiousSales();
										productInfo.setSalesNums(productOrderNum);
										hcService.setEventInfo(columnContent,productInfo,userCode,channelId);
										
									}
									
									
								}
								
								
								//压缩图片
								if ("4497471600010008".equals(homeColumn.getColumnType())||"4497471600010013".equals(homeColumn.getColumnType())//三栏
										|| "4497471600010025".equals(homeColumn.getColumnType())){
									if(StringUtils.isNotBlank(productInfo.getMainpicUrl())){
										productInfo.setMainpicUrl(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, productInfo.getMainpicUrl()).getPicNewUrl());
									}
								}
								if ("4497471600010009".equals(homeColumn.getColumnType())||"4497471600010014".equals(homeColumn.getColumnType())){//两栏
									if(StringUtils.isNotBlank(productInfo.getMainpicUrl())){
										productInfo.setMainpicUrl(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, productInfo.getMainpicUrl()).getPicNewUrl());
									}
								}
								
								// 注释掉下面这个过滤，见bug#5756
								// if (null == productInfo ||
								// StringUtils.isEmpty(productInfo.getProductCode()))
								// {
								// //加这个if为了过滤掉已下架的商品
								// continue;
								// }
								if((("4497471600010014".equals(homeColumn.getColumnType())))){
									if((contentList.size()<12 && supportStock.upAllStockForProduct(productInfo.getProductCode()) > 0)) {//有库存
										columnContent.setProductInfo(productInfo);	
									}else {
										continue Loop;
									}
								}else if(("4497471600010013".equals(homeColumn.getColumnType()))){
									if((contentList.size()<18 && supportStock.upAllStockForProduct(productInfo.getProductCode()) > 0)) {//有库存
										columnContent.setProductInfo(productInfo);	
									}else {
										continue Loop;
									}
								}else {
									columnContent.setProductInfo(productInfo);
								}
								
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
	
				}

				for(HomeColumnContent ct : contentList) {
				/*	if(ct.getProductInfo() != null && StringUtils.isNotBlank(ct.getProductInfo().getMarkPrice())) {
						// 活动价大于等于原价时不显示原价
						if(new BigDecimal(ct.getProductInfo().getSellPrice()).compareTo(new BigDecimal(ct.getProductInfo().getMarkPrice())) >= 0) {
							ct.getProductInfo().setMarkPrice("");
						}
					}*/
					if(ct.getProductInfo() != null){
						ct.getProductInfo().setLabelsInfo(labelService.getLabelInfoList(ct.getProductInfo().getProductCode()));
						//562版本对于商品列表标签做版本兼容处理
						if(appVersion.compareTo("5.6.2")<0){
							Iterator<PlusModelProductLabel> iter = ct.getProductInfo().getLabelsInfo().iterator();
							while (iter.hasNext()) {
								PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
								if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
									iter.remove();
								}
							}
						}
					}
				}
				
				homeColumn.setContentList(contentList); // 内容List
				
				if (null != homeColumn.getContentList() && homeColumn.getContentList().size() > 0) {
					columnList.add(homeColumn);
				} else if (columnMap.get("column_type").equals("4497471600010030")) {
					// 买家秀入口没有栏目内容
					String countSql = "SELECT count(1) num FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ";
					Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_info").dataSqlOne(countSql, new MDataMap());
					if(countMap != null) {
						int num = MapUtils.getIntValue(countMap, "num");
						if(num > 0) {
							// 老版本就不展示买家秀入口
							if(StringUtils.isNotBlank(appVersion) && AppVersionUtils.compareTo(appVersion, "5.6.4") >= 0) {
								columnList.add(homeColumn);
							}
						}
					}
					hasNavUrl = true;
				} else if (columnMap.get("column_type").equals("4497471600010019")) {
					// 导航栏URL-栏目没有内容
					columnList.add(homeColumn);
					hasNavUrl = true;
				}
			}
			
			if(!hasNavUrl) {
				// 猜你喜欢控制开关
				MDataMap navMap = DbUp.upTable("fh_apphome_nav").oneWhere("flag", "", "", "nav_code", StringUtils.trimToEmpty(inputParam.getNavCode()));
				result.setMaybeLove(navMap != null ? navMap.get("flag") : "");
			} else {
				result.setMaybeLove("4497480100020002"); 
			}
		}
		
		result.setColumnList(columnList);
		result.setNavCode(inputParam.getNavCode());
		result.setSysTime(FormatHelper.upDateTime());
		return result;
	}


}
