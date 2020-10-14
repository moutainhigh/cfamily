package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiHomeColumnWeAppInput;
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
import com.srnpr.xmassystem.enumer.EKvSchema;
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
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有首页版式栏目(微信小程序)
 * @author zhaojunling
 * 
 */
public class ApiHomeColumnXcx extends RootApiForVersion<ApiHomeColumnNewResult, ApiHomeColumnWeAppInput> {
	
	private final String PRODUCT_VIEW = "4497471600020004"; 	// 链接类型为商品详情
	private final String CATEGORY = "4497471600020003"; 		// 链接类型为分类搜索
	private final String URL = "4497471600020001"; 				// 链接类型为URL
	private final String BRAND = "4497471600020014"; 			// 链接类型为品牌
	private final String XCX = "4497471600020012"; 				// 链接类型为URL
	
	// 支持智能推荐的栏目类型
	static String[] PRODUCT_MAINTENANCE_TYPES = {"4497471600010008","4497471600010013","4497471600010014","4497471600010025"};
	
	private final String BANNER = "4497471600010001";			//轮播广告模板
	private final String FAST_BUY = "4497471600010011";			//闪购模板
	private final String FAST_BUY_HH = "4497471600010026";			//闪购横滑模板
	private final String TV_LIVE_H = "4497471600010016";		//TV直播横滑
	private final String TV_LIVE_L = "4497471600010036";		//TV直播列表
	private final String TV_LIVE = "4497471600010010";			//TV直播
	private final String VIDEO_LIST = "4497471600010020";		//视频模板数组
	private final String SECOND_BUY = "4497471600010022";       //秒杀
	private final String GROUP_BUY_SHIP= "4497471600010024";    //拼团包邮
	private final String RECOMMEND_BUY = "4497471600010008";    //商品推荐
	private final String ONE_ADVERTEMENT = "4497471600010002";  //一栏广告
	private final String THREE_COLUMN = "4497471600010013";     //三栏两行
	private final String TWO_COLUMN = "4497471600010014";       //两栏两行
//	private final String ONE_COL_MORE_ROW = "4497471600010025"; //一栏多行
	// 558商品评价模板
	//private final String PROD_EVALUATION = "4497471600010027"; //商品评价模板

	private final String FENXIAO = "4497471600010028"; //分销
	private final String FENXIAO_YLDH = "4497471600010037"; //分销一栏多行
	//private final String BUYER_SHOW_LIST = "4497471600010029"; //买家秀列表
	private final String BUYER_SHOW_ENTER = "4497471600010030"; //买家秀入口
//	private final String BUYER_SHOW_ENTER1 = "449748590001"; //买家秀入口样式1
//	private final String BUYER_SHOW_ENTER2 = "449748590002"; //买家秀入口样式2

	static ProductPriceService priceService = new ProductPriceService();
	static PlusSupportStock supportStock = new PlusSupportStock();
	
	public ApiHomeColumnNewResult Process(ApiHomeColumnWeAppInput inputParam, MDataMap mRequestMap) {
		ApiHomeColumnNewResult result = new ApiHomeColumnNewResult();//返回结果
		List<HomeColumn> columnList = new ArrayList<HomeColumn>();
		/**
		 * 判断小程序是否过滤指定分类
		 */
		Map<String,Object> ifIgnoreMap = DbUp.upTable("uc_ignore_category").dataSqlOne("SELECT * FROM usercenter.uc_ignore_category limit 1", new MDataMap());
		String ignore = "Y";//默认过滤
		if(ifIgnoreMap != null) {
			ignore = MapUtils.getString(ifIgnoreMap, "if_ignore","Y");
		}
//		HomeColumn banner = null;//轮播广告模板,columnType: 4497471600010001
//		HomeColumn fastBuy = null;//闪购模板,columnType: 4497471600010011
//		HomeColumn fastBuyHh = null;//闪购横滑模板,columnType: 4497471600010026
//		HomeColumn tvLiveH = null;//TV直播横滑,columnType: 4497471600010016
//		HomeColumn tvLive = null;//TV直播模板,columnType: 4497471600010010
//		HomeColumn secondBuy = null;//秒杀模板,columnType: 4497471600010022
//		HomeColumn groupBuyShip = null;//拼团包邮模板,columnType: 4497471600010024
//		HomeColumn prodEvaluation = null;//商品评价模板,columnType: 4497471600010027
//		HomeColumn fenxiao = null;//分销模板,columnType: 4497471600010028
//		HomeColumn buyerShowList = null;//买家秀列表,columnType: 4497471600010029
//		HomeColumn buyerShowEnter1 = null;//买家秀入口样式1,columnType: 449748590001
//		HomeColumn buyerShowEnter2 = null;//买家秀入口样式2,columnType: 449748590002
//		List<HomeColumn> videoList = new ArrayList<HomeColumn>();//视频模板数组,columnType: 4497471600010020
//		List<HomeColumn> oneAdverList = new ArrayList<HomeColumn>();//一栏广告模板数组,columnType: 4497471600010002
//		List<HomeColumn> recommdList = new ArrayList<HomeColumn>();//商品推荐模板数组,columnType: 4497471600010008
//		List<HomeColumn> threeTwoList = new ArrayList<HomeColumn>();//三栏两行模板数组,columnType: 4497471600010013
//		List<HomeColumn> twoTwoList = new ArrayList<HomeColumn>();//两栏两行模板数组,columnType: 4497471600010014
//		List<HomeColumn> type25ColumnList = new ArrayList<HomeColumn>();//一栏多行模板数组,columnType: 4497471600010025
		HomeColumnService hcService = new HomeColumnService();//实例化HomeColumnService
		ProductService pService = new ProductService();//实例化ProductService
		LoadProductInfo loadProductInfo = new LoadProductInfo();//实例化LoadProductInfo
//		PlusSupportProduct supportProduct = new PlusSupportProduct();//实例化PlusSupportProduct
//		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();//实例化PlusSupportEvent
//		PlusServiceSale plusServiceSale = new PlusServiceSale();//实例化PlusServiceSale
	    LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
		ProductLabelService labelService = new ProductLabelService();
		OrderService orderService = new OrderService();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String systemTime = DateUtil.getSysDateTimeString();//系统当前时间
		String sellerCode = getManageCode();
		String userType = inputParam.getBuyerType();//用户类型
		String channelId = getChannelId();
		String appVersion = StringUtils.trimToEmpty(this.getApiClient().get("app_vision"));
		if("".equals(appVersion)) {
			appVersion = "5.6.4";
		}
		String uniqid =  StringUtils.trimToEmpty(this.getApiClient().get("uniqid"));
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); //最大宽度
		//查询未删除,已发布,当前时间在设定的开始时间和结束时间之内的所有微信小程序首页栏目,结果按位置列正序,创建时间倒序
		String sFields = " column_name_corlor,show_num,column_bgpic,category_codes,category_limit,product_maintenance,event_code,rule_code,product_maintenance,column_code, column_name, show_name, start_time, end_time, showmore_linktype, column_type, columns_per_row, "
						+ " interval_second, is_had_edge_distance, is_showmore, future_program, showmore_title, showmore_linkvalue, show_style";
		String sOrders = " position asc , create_time desc";
		String sWhere = " start_time <= '" + systemTime	+ "' and end_time > '" + systemTime	+ "' and is_delete = '449746250002' "
						+ " and release_flag = '449746250001' and seller_code = '" + sellerCode + "' and view_type = '4497471600100004' and nav_code = '"+inputParam.getNavCode()+"' " 
						+ " and (column_type = '4497471600010001' or column_type = '4497471600010004' or column_type = '4497471600010011' or column_type = '4497471600010016' or column_type = '4497471600010010' or column_type = '4497471600010020' "
						+ " or column_type = '4497471600010023' or column_type = '4497471600010022' or column_type = '4497471600010008' or column_type = '4497471600010002' or column_type = '4497471600010028' "
						+ " or column_type = '4497471600010024' or column_type = '4497471600010013' or column_type = '4497471600010014' or column_type = '4497471600010025' or column_type = '4497471600010026' "
						+ " or column_type = '4497471600010027' or column_type = '4497471600010029' or column_type = '4497471600010030' or column_type = '4497471600010031' or column_type = '4497471600010035' or column_type = '4497471600010036' or column_type = '4497471600010037' )";
		
		// 首屏
		int iStart = 0;
		int iNumber = 100;
		if("1".equals(inputParam.getLoadFlag())) {
			// 首屏返回前三个模版数据
			iNumber = 3;
		} else if("2".equals(inputParam.getLoadFlag())) {
			// 非首屏从第4模版开始返回数据
			iStart = 3;
		}
		
		//List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column").queryAll(sFields, sOrders, sWhere, null);
		List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column").query(sFields, sOrders, sWhere, null,iStart,iNumber);

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
			String sql = "";
			// 查询所有商品评价模板维护的商品
			if("Y".equals(ignore)) {//需要过滤
				sql = "SELECT e.* FROM familyhas.fh_apphome_evaluation e WHERE e.is_delete = '0' AND " + 
						"	e.location_num > 0 AND e.first_category_code NOT IN ( " + 
						"		SELECT f.category_code FROM usercenter.uc_program_del_category f WHERE f.`level` = '2' " + 
						"	) AND e.second_category_code NOT IN ( " + 
						"		SELECT s.category_code FROM usercenter.uc_program_del_category s WHERE s.`level` = '3' " + 
						"	) AND e.third_category_code NOT IN ( " + 
						"		SELECT t.category_code FROM usercenter.uc_program_del_category t WHERE t.`level` = '4' " + 
						"	) ORDER BY e.location_num ASC LIMIT 20";
			}else {
				sql = "SELECT e.* FROM familyhas.fh_apphome_evaluation e WHERE e.is_delete = '0' AND " + 
						"	e.location_num > 0  ORDER BY e.location_num ASC LIMIT 20";
			}
			List<Map<String, Object>> evaProdList = DbUp.upTable("fh_apphome_evaluation").dataSqlList(sql, new MDataMap());
			
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
				loadSellerInfo = new LoadSellerInfo();
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
				String sql1 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' ) = 0  AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code LIMIT 1) IS NULL) "
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
				MDataMap order_evaluation = DbUp.upTable("nc_order_evaluation").one("uid",evaluation_uid);
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
				skuQuery.setIsPurchase(1);
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
			for(int i = 0; i < columnMapList.size(); i ++) {
				MDataMap mdataMap = columnMapList.get(i);
				//分销一栏多行模板后面的栏目不展示
				if("4497471600010037".equals(mdataMap.get("column_type"))){
					// 分销模版只对分销用户开放
					if(StringUtils.isBlank(userCode)) continue;
					if(DbUp.upTable("fh_agent_member_info").count("member_code",userCode) == 0) {
						continue;
					}
					// 屏蔽外呼用户
					if(DbUp.upTable("fh_waihu_member_info").count("member_code",userCode) > 0) {
						continue;
					}
					columnMapList = columnMapList.subList(0, i+1);
					break;
				}
			}			
			//查询商品分类编码对应的分类名称
			List<String> categoryCodeList = new ArrayList<String>();
			//查询当前时间在设定的开始时间和结束时间之内的未删除所有小程序首页栏目内容
			String sWhereContent = " start_time <='" + systemTime + "' and end_time > '" + systemTime + "' and is_delete = '449746250002'";
			Map<String,Integer> paramMap = new HashMap<String, Integer>(); 
			HashMap<String, List<String>> oneColumn = new HashMap<String,List<String>>();
			List<String> picUrlList = new ArrayList<String>();//存放需要压缩的图片路径
			List<String> columnTypeCodeList = new ArrayList<String>();
			for(int i = 0; i < columnMapList.size(); i ++) {
				
				MDataMap mdataMap = columnMapList.get(i);
				if(CATEGORY.equals(mdataMap.get("showmore_linktype"))) {
					String category_code = mdataMap.get("showmore_linkvalue");
					categoryCodeList.add(category_code);
				}
				if(i == 0) {
					sWhereContent += " and a.column_code in ( '" + mdataMap.get("column_code") + "'";
				}else {
					sWhereContent += ",'" + mdataMap.get("column_code") + "'";
				}
				if(i == columnMapList.size() - 1) {
					sWhereContent += ")";
				}
				if(THREE_COLUMN.equals(mdataMap.get("column_type"))||TWO_COLUMN.equals(mdataMap.get("column_type"))) {
					paramMap.put(mdataMap.get("column_code"), 0);
				}
				//闪购模板获取模版背景图片字段
				if (("4497471600010011".equals(mdataMap.get("column_type")) || "4497471600010004".equals(mdataMap.get("column_type"))) && StringUtils.isNotBlank(mdataMap.get("column_bgpic"))) {
					picUrlList.add(mdataMap.get("column_bgpic"));
				}
				
				//一行多栏模板需要判断是否采用的只能推荐
				if(ArrayUtils.contains(PRODUCT_MAINTENANCE_TYPES, mdataMap.get("column_type")) && "44975017003".equals(mdataMap.get("product_maintenance"))) {
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(mdataMap.get("category_limit"));
					paramList.add(mdataMap.get("category_codes"));
					paramList.add(mdataMap.get("show_num"));
					oneColumn.put(mdataMap.get("column_code"), paramList);
				}
				//一栏多行通用大图模板，优先使用商品广告图
				if("4497471600010031".equals(mdataMap.get("column_type"))) {
					columnTypeCodeList.add(mdataMap.get("column_code"));
				}				
			}
			
			List<MDataMap> columnContentMapList = new ArrayList<MDataMap>();
			List<Map<String, Object>> dataSqlList  = new ArrayList<Map<String, Object>>();
			if(columnMapList.size() > 0) {
				//columnContentMapList = DbUp.upTable("fh_apphome_column_content").queryAll("", "position asc, start_time desc", sWhereContent, null);
				 dataSqlList = DbUp.upTable("fh_apphome_column_content").dataSqlList("select a.* from fh_apphome_column_content a LEFT JOIN productcenter.pc_productinfo b ON a.showmore_linkvalue = b.product_code where "+sWhereContent+" and (a.showmore_linktype!='4497471600020004' OR (a.showmore_linktype = '4497471600020004' AND b.product_status = '4497153900060002')) order by position asc, start_time desc",null);
			}
			for (Map<String, Object> mDataMap : dataSqlList) {
				
				MDataMap param = new MDataMap(mDataMap);
				columnContentMapList.add(param);
			}
			
		
			//循环首页版式内容，准备数据
			Map<String, List<MDataMap>> contentMapList = new HashMap<String, List<MDataMap>>();//存放column_code 对应的版式内容数据
			
			List<String> productCodeList = new ArrayList<String>();//商品编码(针对链接类型为商品分类)
			for(MDataMap mdataMap : columnContentMapList) {
				String column_code = mdataMap.get("column_code");
				if(!contentMapList.containsKey(column_code)) {
					contentMapList.put(column_code, new ArrayList<MDataMap>());
				}
				if(!paramMap.containsKey(column_code)) {
					contentMapList.get(column_code).add(mdataMap);
					}
				else if(paramMap.get(column_code)<18){
					contentMapList.get(column_code).add(mdataMap);
				}
				if(StringUtils.isNotEmpty(mdataMap.get("picture"))) {
					picUrlList.add(mdataMap.get("picture"));
				}
				
				if(PRODUCT_VIEW.equals(mdataMap.get("showmore_linktype"))) {//链接类型为商品时
					String productCode = mdataMap.get("showmore_linkvalue");
					if(!paramMap.containsKey(column_code)) {
						if(StringUtils.isNotEmpty(productCode)) {
							productCodeList.add(productCode);
							//一栏多行通用大模板优先选用商品一般信息修改中的广告图字段
							if(columnTypeCodeList.contains(mdataMap.get("column_code"))) {
								PlusModelProductInfo productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
								if(StringUtils.isNotBlank(productInfo.getAdpicUrl())) {
									picUrlList.add(productInfo.getAdpicUrl());
								}
							}
						}
					}else if(paramMap.get(column_code)<18){
						paramMap.put(column_code,paramMap.get(column_code)+1);
						productCodeList.add(productCode);
					}
				}
				else if (CATEGORY.equals(mdataMap.get("showmore_linktype"))) {
					String categoryCode = mdataMap.get("showmore_linkvalue");
					if (StringUtils.isNotEmpty(categoryCode)) {
						categoryCodeList.add(categoryCode);
					}
				}
			}
			MDataMap categoryNameMap = hcService.getCategoryName(categoryCodeList, sellerCode);
			
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
						 show_num = Integer.parseInt(list.get(2));
					}
					List<String> dgRecommendProducts = hcService.getDGRecommendProducts(userCode, category_codes,key,channelId,uniqid,show_num);
					oneColumnRecommendProductsMap.put(key, dgRecommendProducts);
					for (String pCode : dgRecommendProducts) {
						productCodeList.add(pCode);
					}
				}
			}
			//获取商品信息，将商品中对应的图片压缩
			Map<String, HomeColumnContentProductInfo> productInfoMap = hcService.getProductInfo(productCodeList, userType, userCode, channelId);
			for(String productCode : productInfoMap.keySet()) {
				picUrlList.add(productInfoMap.get(productCode).getMainpicUrl());
			}
			//压缩图片
			MDataMap picUrlMap = new MDataMap();//存储旧地址-新地址
			Map<String, PicInfo> picUrlObjMap = new HashMap<String, PicInfo>();//存储 新路径-图片信息
			List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), picUrlList, "");
			for (PicInfo picInfo : picInfoList) {
				picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
				picUrlObjMap.put(picInfo.getPicNewUrl(), picInfo);
			}
			boolean hasNavUrl = false;
			LOOP: for(MDataMap columnMap : columnMapList) {
				//只有导航栏才有模板背景图，故没有
				HomeColumn homeColumn = new HomeColumn();
				homeColumn.setFutureProgram(columnMap.get("future_program"));//未开始的TV节目档数
				homeColumn.setShowmoreTitle(columnMap.get("showmore_title"));//显示更多标题
				homeColumn.setColumnType(columnMap.get("column_type"));//栏目类型
				if(StringUtils.isNotBlank(columnMap.get("interval_second"))) {//轮播间隔
					homeColumn.setIntervalSecond(Integer.parseInt(columnMap.get("interval_second")));
				}
				homeColumn.setIsShowmore(columnMap.get("is_showmore"));//是否显示更多
				homeColumn.setColumnsPerRow(columnMap.get("columns_per_row"));//功能全每行显示个数
				homeColumn.setStartTime(columnMap.get("start_time"));//开始时间
				homeColumn.setEndTime(columnMap.get("end_time"));//结束日期
				homeColumn.setPageType("0");//页面类型 首页
				homeColumn.setShowmoreLinktype(columnMap.get("showmore_linktype"));//显示更多链接类型
				if(CATEGORY.equals(columnMap.get("showmore_linktype"))) {//显示更多链接值
					homeColumn.setShowmoreLinkvalue(categoryNameMap.get(columnMap.get("showmore_linkvalue"))==null?"":categoryNameMap.get(columnMap.get("showmore_linkvalue")));
					homeColumn.setCategoryCode(columnMap.get("showmore_linkvalue"));
				}else {
					homeColumn.setShowmoreLinkvalue(columnMap.get("showmore_linkvalue"));
				}
				homeColumn.setShowName(columnMap.get("show_name"));//是否显示栏目名称
				homeColumn.setHasEdgeDistance(columnMap.get("is_had_edge_distance"));//是否保留边距
				homeColumn.setColumnID(columnMap.get("column_code"));//栏目ID
				homeColumn.setColumnName(columnMap.get("column_name"));//栏目名称
				homeColumn.setColumnNameColor(columnMap.get("column_name_corlor"));//栏目名称颜色
				//导航栏、闪购栏目类型，并且模版背景图片不为空
				String columnBgpicUrl = columnMap.get("column_bgpic");
				if (StringUtils.isNotBlank(columnBgpicUrl) && ("4497471600010004".equals(homeColumn.getColumnType())||"4497471600010011".equals(homeColumn.getColumnType()))) {
					if (StringUtils.isNotBlank(picUrlMap.get(columnBgpicUrl))) {
						homeColumn.setColumnBgpic(picUrlObjMap.get(picUrlMap.get(columnBgpicUrl)));
					}else {
						homeColumn.setColumnNameColor("");
					}
				}
				//内容List
				List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
				String columnType = homeColumn.getColumnType();
				if(FENXIAO.equals(columnType)||FENXIAO_YLDH.equals(columnType)) {//分销模板
					// 分销模版只对分销用户开放
					if(StringUtils.isBlank(userCode)) continue;
					if(DbUp.upTable("fh_agent_member_info").count("member_code",userCode) == 0) {
						continue;
					}
					// 屏蔽外呼用户
					if(DbUp.upTable("fh_waihu_member_info").count("member_code",userCode) > 0) {
						continue;
					}
					
					contentList = hcService.getFenXiaoActivityWeApp(maxWidth,true,getApiClientValue("app_vision"),userCode,channelId);
					if(contentList==null||contentList.size()<3) {
						continue;
					}
					if(FENXIAO_YLDH.equals(columnType)){
						if(contentList.size()>10){
							contentList = contentList.subList(0, 10);
						}						
						homeColumn.setTotalPage((int) Math.ceil(contentList.size()/10.00));
					}
				}
				else if(FAST_BUY.equals(columnType)) {//微信小程序闪购模板
					contentList = hcService.getFlashActivityWeApp(sellerCode, maxWidth, "",channelId);
					// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
					if(contentList.size() <= 0) {
						continue;
					}else {
						if(contentList.size()>6){
							contentList = contentList.subList(0, 6);
						}
						// 闪购栏目的开始结束时间设置为闪购活动的开始结束时间
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());
					}
				}else if(FAST_BUY_HH.equals(columnType)) {//微信小程序闪购横滑模板
					contentList = hcService.getFlashActivityWeApp(sellerCode, maxWidth, "",channelId);
					// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
					if(contentList.size() <= 0) {
						continue;
					}else {
						// 闪购栏目的开始结束时间设置为闪购活动的开始结束时间
						if(contentList.size()>10){
							contentList = contentList.subList(0, 10);
						}					
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());					
					}
				}else if(TV_LIVE_H.equals(columnType)) {//TV直播横滑
					// 未开始的节目档数
					int futureProgram = 0;
					if(StringUtils.isNotBlank(columnMap.get("future_program"))){
						futureProgram = NumberUtils.toInt(""+DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code",columnMap.get("future_program"))));
						if(futureProgram < 0) futureProgram = 0;
					}
					
					//微信小程序商城首页
					contentList = hcService.getTVDataListNew(userType, maxWidth, "", userCode , "4497471600100004" , "", futureProgram,channelId);
				}else if(TV_LIVE.equals(columnType)||TV_LIVE_L.equals(columnType)) {//TV直播
					//微信小程序商城首页
					contentList = hcService.getTVDataList(userType, maxWidth, "", userCode , "4497471600100004", "",channelId);
				}else if(SECOND_BUY.equals(columnType)) {//秒杀
					if(AppVersionUtils.compareTo(getApiClient().get("app_vision"),"5.4.4")>=0) {
							homeColumn.setShowmoreLinktype("4497471600020010");
							homeColumn.setShowmoreLinkvalue("4497471600020010");
	
					}
					//微信小程序秒杀模板
					if("4497469400050001".equals(userType)) {  //显示内购活动
						contentList = hcService.getSeckillDataList(userType, maxWidth, "jpg", userCode, "4497471600100004", "", 1,channelId,userCode,uniqid);
					}
					else {//不显示内购
						contentList = hcService.getSeckillDataList(userType, maxWidth, "jpg", userCode, "4497471600100004", "", 0,channelId,userCode,uniqid);
					}
					if(null != contentList && !contentList.isEmpty()) {
						// 栏目的结束时间，同app的取值方式
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());
						//这个是活动的结束时间
	/*					if(StringUtils.isBlank(homeColumn.getStartTime())) {
							String today = systemTime.substring(0, 10)+" 00:00:00";			
							String swhere = "event_type_code='4497472600010001' and begin_time >= '"+today+"' and " + "begin_time<='" + systemTime + "' and end_time >= '" + systemTime + "' and event_status='4497472700020002'";
							List<MDataMap> seckillEvent = DbUp.upTable("sc_event_info").query("", "begin_time desc", swhere, new MDataMap(),-1,-1);
							if(null == seckillEvent || seckillEvent.isEmpty()) {
								//当前档没有 则判断是否有下一档
								String endTime = systemTime.substring(0,11) + "23:59:59";
								swhere = "event_type_code='4497472600010001' and " + "begin_time>='" + systemTime + "' and begin_time <= '" + endTime + "' and event_status='4497472700020002'";
								seckillEvent = DbUp.upTable("sc_event_info").query("", "begin_time", swhere, new MDataMap(),-1,-1);
								if(null == seckillEvent || seckillEvent.isEmpty()) {
									MDataMap mDataMap = seckillEvent.get(0);
									homeColumn.setStartTime(mDataMap.get("begin_time").toString());
									homeColumn.setEndTime(mDataMap.get("end_time").toString());
								}
							}
							else {
								MDataMap mDataMap = seckillEvent.get(0);
								homeColumn.setStartTime(mDataMap.get("begin_time").toString());
								homeColumn.setEndTime(mDataMap.get("end_time").toString());
							}
						}*/
					}else {
						//无秒杀商品,不返回此模板
						continue LOOP;
					}
					
				}else if(GROUP_BUY_SHIP.equals(columnType)) {//拼团
					//微信小程序拼团包邮模板
					int pageCount = hcService.getCollageDataPageCount(sellerCode, columnMap.get("event_code"), "4", channelId);//规定一次展示4个
					if(pageCount!=0) {
						if(pageCount==1) {
							contentList = hcService.getCollageDataList(userCode, sellerCode, columnMap.get("event_code"), 0, "4", pageCount, "248", channelId);
						}
						else if(pageCount==2) {
							contentList = hcService.getCollageDataList(userCode, sellerCode, columnMap.get("event_code"), 0, "8", pageCount, "248", channelId);
						}
						else {//最多划拉三页
							contentList = hcService.getCollageDataList(userCode, sellerCode, columnMap.get("event_code"), 0, "12", pageCount, "248", channelId);
						}
					}
					if(null != contentList && !contentList.isEmpty()) {
						homeColumn.setStartTime(contentList.get(0).getStartTime());
						homeColumn.setEndTime(contentList.get(0).getEndTime());
					}else {
						//无秒杀商品,不返回此模板
						continue LOOP;
					}
					
				}else if(BUYER_SHOW_ENTER.equals(columnType)) { //买家秀入口
					String showStyle = columnMap.get("show_style");
					//homeColumn.setShowStyle(showStyle);
					if("449748590001".equals(showStyle)) {
						// 样式1:返回最近20条买家秀内容
						List<BuyerShow> buyerShowLists = new ArrayList<BuyerShow>();
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
										buyerShowLists.add(buyerShow);
									}
								}
							}
						}
						homeColumn.setBuyerShowList(buyerShowLists);
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
					
				}else {//其他，主要包括：轮播广告模板,视频模板数组 和一栏广告数组,商品推荐数组,三栏两行数组,两栏联行数组
					
					//如果是一栏多行，并且为智能推荐，则用达观推荐的数据
					boolean subflag = false;
					List<String> subList = new ArrayList<>();
					if(oneColumnRecommendProductsMap.containsKey(columnMap.get("column_code"))) {
						subList = oneColumnRecommendProductsMap.get(columnMap.get("column_code"));
						if(subList.size()==0) {
							continue LOOP;
						}
						subflag=true;
					}
					List<MDataMap> items = contentMapList.get(homeColumn.getColumnID());
					if(items == null) {
						items = new ArrayList<MDataMap>();
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
						if("4497471600010035".equals(homeColumn.getColumnType())&&items.size()<3) {
							//滑动轮播模板至少三个数据才做展示
							continue;
						}
						Loop:for(MDataMap columnContentMap : items) {
							HomeColumnContent columnContent = new HomeColumnContent();
							String pic = picUrlMap.get(columnContentMap.get("picture"));
							columnContent.setPicture(StringUtils.isEmpty(pic) ? "" : pic); //图片地址
							columnContent.setTitleColor(columnContentMap.get("title_color"));//标题颜色
							columnContent.setDescriptionColor(columnContentMap.get("description_color"));//描述颜色
							columnContent.setEndTime(columnContentMap.get("end_time"));//结束时间
							columnContent.setStartTime(columnContentMap.get("start_time"));//开始时间
							columnContent.setColumnCode(homeColumn.getColumnID()); //加入栏目编号,列表接口需要传该参
							//获取链接值
							String showmore_linkvalue = columnContentMap.get("showmore_linkvalue");
							if(CATEGORY.equals(columnContentMap.get("showmore_linktype"))) {
								//columnContent.setShowmoreLinkvalue(columnContentMap.get("showmore_linkvalue"));
								columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue)==null?"":categoryNameMap.get(showmore_linkvalue));
								columnContent.setCategoryCode(showmore_linkvalue);
							}else if(BRAND.equals(columnContentMap.get("showmore_linktype"))) {
								//品牌搜索，返回品牌中文名称（目前只有导航栏可配）
								MDataMap temMap = DbUp.upTable("pc_brandinfo").one("brand_code",columnContentMap.get("showmore_linkvalue"));
								columnContent.setShowmoreLinkvalue(StringUtils.trim(temMap.get("brand_name")));
							}else {
								if(StringUtils.isNotBlank(showmore_linkvalue) && "449746250001".equals(columnContentMap.get("skip_place"))) {
									String place_time = columnContentMap.get("place_time");
									if(StringUtils.isNotBlank(place_time)) {
										try {
											Long timeLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(place_time).getTime();
											String timeParam = "";
											if(showmore_linkvalue.indexOf("?") > 0) {
												timeParam = "&time=";
											}else {
												timeParam = "?time=";
											}
											showmore_linkvalue += (timeParam + timeLong.toString());
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
								}
								columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(showmore_linkvalue) ? "" : showmore_linkvalue);
							}
							if(("4497471600020016".equals(columnContentMap.get("showmore_linktype"))||"4497471600020017".equals(columnContentMap.get("showmore_linktype")))&&AppVersionUtils.compareTo(appVersion, "5.6.5") < 0) {
								//直播视频添加版本控制
								continue;
							}
							columnContent.setTitle(columnContentMap.get("title"));//标题
							columnContent.setFloorModel(columnContentMap.get("floor_model"));//楼层模板
							columnContent.setPosion(Integer.parseInt(StringUtils.isEmpty(columnContentMap.get("position")) ? "0" : columnContentMap.get("position")));//位置
							columnContent.setDescription(columnContentMap.get("description"));//描述
							columnContent.setIsShare(columnContentMap.get("is_share"));//是否分享
							columnContent.setShowmoreLinktype(columnContentMap.get("showmore_linktype"));//链接类型
							
							if(URL.equals(columnContentMap.get("showmore_linktype")) && "449746250001".equals(columnContentMap.get("is_share"))) {//当前显示类型为url，并且为分享
								columnContent.setShareContent(columnContentMap.get("share_content"));//分享内容
								columnContent.setShareTitle(columnContentMap.get("share_title"));//分享标题
								columnContent.setSharePic(columnContentMap.get("share_pic"));//分享图片
	
								//“分享链接需要进行拼接重写”
								String shareUrlHead = bConfig("familyhas.share_url_head");
								String alt = "&wxTilte=";
								String shareTitle = columnContentMap.get("share_title");
								if(StringUtils.isEmpty(shareTitle)) {
									shareTitle = bConfig("familyhas.share_title");
								}
								String shareURL = columnContentMap.get("share_link");
								//为了兼容历史数据分享链接为空的情况
								if(StringUtils.isEmpty(shareURL)) {
									shareURL = columnContent.getShowmoreLinkvalue();
								}
								String shareLink = shareUrlHead;
								try {
									shareLink += (URLEncoder.encode((shareURL.trim()), "utf8") + alt + URLEncoder.encode(shareTitle, "utf8"));
								}catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								columnContent.setShareLink(shareLink);//分享链接
							}
							if(XCX.equals(columnContentMap.get("showmore_linktype"))) {//跳转类型为小程序时，需要处理showMoreLinkValue
								MDataMap showMoreLinkValueMap = new MDataMap();
								String showMore[] = showmore_linkvalue.split("\\|");
								showMoreLinkValueMap.put("path",showMore[0]);
								if(showMore.length>1) {
									showMoreLinkValueMap.put("appId",showMore[1]);
								}
								columnContent.setShowmoreLinkvalue(JSONObject.toJSONString(showMoreLinkValueMap));
							}
							// 链接类型为“商品详情”时查询商品信息
							if (PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))) {
								HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
								
								// 商品活动标签，仅限部分栏目类型：一栏多行
								if("4497471600010025".equals(homeColumn.getColumnType())||"4497471600010031".equals(homeColumn.getColumnType())) {
									productInfo.setTagList(pService.getTagListByProductCode(productInfo.getProductCode(), userCode, channelId));
									
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
										//兼容小程序统一字段获取处理
										productInfo.setMainpicUrl(columnContent.getPicture());
										//销量
										int productOrderNum = orderService.getProductOrderNum(subProductInfo.getSmallSellerCode(),subProductInfo.getProductCode(),"4497153900010005")+subProductInfo.getFictitiousSales();
										productInfo.setSalesNums(productOrderNum);
										hcService.setEventInfo(columnContent,productInfo,userCode,channelId);
									}
								}
	
								//压缩图片
								if (THREE_COLUMN.equals(homeColumn.getColumnType())){
									if(StringUtils.isNotBlank(productInfo.getMainpicUrl())){
										productInfo.setMainpicUrl(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, productInfo.getMainpicUrl()).getPicNewUrl());
									}
								}
								if (TWO_COLUMN.equals(homeColumn.getColumnType())){
									if(StringUtils.isNotBlank(productInfo.getMainpicUrl())){
										productInfo.setMainpicUrl(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, productInfo.getMainpicUrl()).getPicNewUrl());
									}
								}
								if(((TWO_COLUMN.equals(homeColumn.getColumnType())))){
									//if((contentList.size()<12&&productStockMap.containsKey(productInfo.getProductCode())&&1 == productStockMap.get(productInfo.getProductCode()))) {//有库存
									if(contentList.size()<12){	
									columnContent.setProductInfo(productInfo);	
									}else {
										continue Loop;
									}
								}else if((THREE_COLUMN.equals(homeColumn.getColumnType()))){
									//if((contentList.size()<18&&productStockMap.containsKey(productInfo.getProductCode())&&1 == productStockMap.get(productInfo.getProductCode()))) {//有库存
									if((contentList.size()<18)){
										columnContent.setProductInfo(productInfo);	
									}else {
										continue Loop;
									}
								}else {
									columnContent.setProductInfo(productInfo);	
								}
							} 
							if(BANNER.equals(homeColumn.getColumnType())||ONE_ADVERTEMENT.equals(homeColumn.getColumnType())) {//轮播广告模板、一栏广告
								if(StringUtils.isNotBlank(columnContent.getPicture())) {//轮播广告模板,图片不为空，设置图片高度
									PicInfo picInfoOprBig = picUrlObjMap.get(columnContent.getPicture());
									//PicInfo picInfoOprBig = pService.getPicInfoOprBig(1080,columnContent.getPicture());
									columnContent.setPicHeight(null == picInfoOprBig ? 0 : picInfoOprBig.getHeight());//图片高度
								}
								
								if(PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))) {//链接类型为商品分类时
									HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
									if(picUrlMap.containsKey(productInfo.getMainpicUrl())){
										productInfo.setMainpicUrl(picUrlMap.get(productInfo.getMainpicUrl()));
									}
									
									columnContent.setProductInfo(productInfo);
								}
							}else if(VIDEO_LIST.equals(homeColumn.getColumnType())) {//视频模板组
								String ccvid = columnContentMap.get("video_link");
								String videoLink = hcService.getVideoLinkUrlFromApi(ccvid);
								columnContent.setVideoLink(videoLink);//封面商品视频链接
								
								//获取商品信息
								HomeColumnContentProductInfo productInfo = new HomeColumnContentProductInfo();
								productInfo.setSellPrice(MoneyHelper.format(new BigDecimal(columnContentMap.get("product_price"))));//销售价
								productInfo.setProductCode(columnContentMap.get("product_code"));//商品编号
								productInfo.setMainpicUrl(StringUtils.trimToEmpty(pic));//图片
								productInfo.setVideoAd(columnContentMap.get("video_ad"));//广告语
								productInfo.setProductName(columnContentMap.get("product_name"));//商品名称
								
								//获取活动价,默认取商品下SKU的最低价
								if(StringUtils.isNotBlank(productInfo.getProductCode())) {
									PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
									skuQuery.setCode(columnContent.getProductInfo().getProductCode());
									skuQuery.setMemberCode(userCode);
									skuQuery.setChannelId(channelId);
									PlusModelSkuInfo skuInfo = priceService.getProductMinPriceSkuInfo(skuQuery, true).get(productInfo.getProductCode());
									
									PlusModelProductInfo product = loadProductInfo.upInfoByCode(new PlusModelProductQuery(columnContent.getProductInfo().getProductCode()));
									productInfo.setSellPrice(MoneyHelper.format(product.getMinSellPrice()));
									
									if(skuInfo != null) {
										productInfo.setSellPrice(MoneyHelper.format(skuInfo.getSellPrice()));
										if(StringUtils.isNotBlank(skuInfo.getEventCode())) {
											productInfo.setIsActivity("活动价");
											// 售价比原价小时显示划线价
											if(skuInfo.getSellPrice().compareTo(skuInfo.getSkuPrice()) < 0) {
												productInfo.setMarkPrice(MoneyHelper.format(skuInfo.getSkuPrice()));
											
												// 特定活动类型显示折扣
												if(ArrayUtils.contains(new String[]{"4497472600010018","4497472600010030"}, skuInfo.getEventType())) {
													// 折扣 = 活动价 / 原价 * 100
													productInfo.setDiscount(skuInfo.getSellPrice().multiply(new BigDecimal(100)).divide(skuInfo.getSkuPrice(),0,BigDecimal.ROUND_HALF_UP).intValue()+"");
													productInfo.setEventType(skuInfo.getEventType());
													if(productInfo.getDiscount().endsWith("0")){
														productInfo.setDiscount(productInfo.getDiscount().substring(0, productInfo.getDiscount().length()-1));
													}
													if(columnContent.getProductInfo().getDiscount().equals("100")) {
														columnContent.getProductInfo().setDiscount("");
													}
												}
											}
										}
									}
								}
								
								columnContent.setProductInfo(productInfo);//商品信息
							}
							else if(RECOMMEND_BUY.equals(homeColumn.getColumnType())) {//商品推荐
								if(StringUtils.isNotBlank(columnContent.getPicture())){
									columnContent.setPicture(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, columnContent.getPicture()).getPicNewUrl());
								}
							}else if(TWO_COLUMN.equals(homeColumn.getColumnType())) {//两栏两行
								if(StringUtils.isNotBlank(columnContent.getPicture())){
									columnContent.setPicture(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, columnContent.getPicture()).getPicNewUrl());
								}
							}else if(THREE_COLUMN.equals(homeColumn.getColumnType())) {//三栏两行
								if(StringUtils.isNotBlank(columnContent.getPicture())){
									columnContent.setPicture(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, columnContent.getPicture()).getPicNewUrl());
								}
							}
							
							contentList.add(columnContent);
						}
					}
			
				}
				
				for(HomeColumnContent ct : contentList) {
					/*if(ct.getProductInfo() != null && StringUtils.isNotBlank(ct.getProductInfo().getMarkPrice())) {
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
				
				if(contentList.size()>0) {
					homeColumn.setContentList(contentList);
					columnList.add(homeColumn);
				}else if (columnMap.get("column_type").equals("4497471600010030")) {
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
		result.setSysTime(FormatHelper.upDateTime());
		return result;
	}
	
	
}
