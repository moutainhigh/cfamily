package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.api.input.ApiForGetTVDataInput;
import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.model.TodayProduct;
import com.cmall.familyhas.api.result.ApiForGetTVDataResult;
import com.cmall.familyhas.api.result.ApiForGetTVDataResult.AdvertisementImg;
import com.cmall.familyhas.api.result.ApiForGetTVDataResult.HostInfo;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.productcenter.service.ProductStoreService;
import com.cmall.systemcenter.service.DefineService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.enumer.EImageWidthSuffix;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webmodel.MFileItem;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * @descriptions 电视TV|今日直播数据接口
 * @author dyc
 * @version 1.0.1
 * 
 * @refactor 不再查询pc_productinfo表，开始使用Redis缓存；
 * 					 ApiForGetTVDataResult中的TodayProduct类加入轮播图pcPicList属性
 * 					 inputParam新增图片宽度picWidth属性
 * 
 * @date 2016年5月25日下午5:22:39
 * @author Yangcl
 * @version 1.0.2
 */
public class ApiForGetTVData extends RootApiForVersion<ApiForGetTVDataResult, ApiForGetTVDataInput> {

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	
	public ApiForGetTVDataResult Process(ApiForGetTVDataInput inputParam, MDataMap mRequestMap) {
		
		ApiForGetTVDataResult result = new ApiForGetTVDataResult();
		if(StringUtils.isBlank(inputParam.getBuyerType())){
			inputParam.setBuyerType("4497469400050002");
		}
		Integer isPurchase = inputParam.getIsPurchase();
		String activity = inputParam.getActivity();
		String pre = "";
		String queryOrder="form_fr_date";
		String channelId = getChannelId();
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  //设置日期格式
		result.setSystemDate(fm.format(new Date()));
		
		/*
		 * 查询直播轮播图信息
		 */
		String now1=DateUtil.getSysDateTimeString();
		String sSql = " SELECT images,urls FROM systemcenter.sc_sales_image WHERE activity_status='4497473400020001' AND show_type='449748390002' AND start_time <= '"+now1+"' AND end_time >= '"+now1+"' ORDER BY start_time ";
		Map<String, Object> dataSqlOne = DbUp.upTable("sc_sales_image").dataSqlOne(sSql, new MDataMap());//因为时间不能冲突，所以一个时间点只有一条数据符合条件
		if(null != dataSqlOne) {
			String images = String.valueOf(dataSqlOne.get("images"));
			String urls = String.valueOf(dataSqlOne.get("urls"));
			List<AdvertisementImg> imgList = new ArrayList<AdvertisementImg>();
			String[] imgArr = images.split("\\|");
			String[] urlArr = urls.split("\\|");
			
			//获取压缩图片
			Map<String, MFileItem> compressImage = new PlusVeryImage().compressImage(false, 0, images, "", EImageWidthSuffix.SALESIMAGES);
			
			for (int i = 0; i < imgArr.length; i++) {
				AdvertisementImg imgModel = new AdvertisementImg();
				MFileItem t_item = compressImage.get(imgArr[i]);
				PlusVeryImage plsImage = new PlusVeryImage();
				if(null != t_item) {
					imgModel.setImgUrl(t_item.getFileUrl());
				} else {
					imgModel.setImgUrl(imgArr[i]);
				}
				imgModel.setForwardUrl(urlArr[i]);
				imgModel.setWidth(plsImage.getImgInfo(imgModel.getImgUrl()).getWidth());
				imgModel.setHeight(plsImage.getImgInfo(imgModel.getImgUrl()).getHeight());
				imgList.add(imgModel);
			}
			result.setImgList(imgList);
		}
		
		try {
			if(StringUtils.isNotBlank(inputParam.getDate())&&StringUtils.left(format.format(format.parse(inputParam.getDate())), 10).compareTo(StringUtils.left(FormatHelper.upDateTime(), 10))>0)
			{
				inputParam.setDate(StringUtils.left( DateHelper.upDateTimeAdd("1M"),10));
				
			}
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		}
		if("SI2009".equals(getManageCode())){
			pre = "9";
		}
		if("SI2003".equals(getManageCode())){
			queryOrder = "-form_fr_date";
		}
		if(inputParam.getSort().equals("0")){//正序
			queryOrder = "form_fr_date";
		}else if(inputParam.getSort().equals("1")){
			queryOrder = "-form_fr_date";
		}
		MDataMap cData = DbUp.upTable("fh_category").one("category_code",activity);
		if(cData != null){
			//栏目图片地址
			result.setBanner_img(cData.get("line_head") ==null ? "" : cData.get("line_head"));
			//栏目名称
			result.setBanner_name(cData.get("category_name") ==null ? "" : cData.get("category_name"));
			//判断栏目类型
			if("449747030001".equals(cData.get("link_address"))) {
				result.setBanner_link(cData.get("link_url") ==null ? "" : cData.get("link_url"));
			} else {
				result.setBanner_link(cData.get("product_link") ==null ? "" : cData.get("product_link"));
			}
		}
		try {
			
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  //设置日期格式
			List<MDataMap> listI = new ArrayList<MDataMap>();
			DecimalFormat df = new DecimalFormat("####");
			if(inputParam.getVipNo().equals("jyhwzdy")&&(inputParam.getDate()==null||"".equals(inputParam.getDate())||format.format(new Date()).equals(inputParam.getDate()))){
				//如果是家有汇网站调用
				inputParam.setDate(fm.format(new Date()));
			}
			//参数为空和参数为当日时间时获取当日已开始播出的数据
			if(inputParam.getDate()==null||"".equals(inputParam.getDate())||format.format(new Date()).equals(inputParam.getDate())){
				
				
				
				String start = format.format(new Date())+" 00:00:00";
				String end = fm.format(new Date());
//				String end = format.format(new Date())+" 23:59:59";
				String swhere = "form_fr_date>='"+start+"' and form_fr_date <= '"+end+"' and so_id='1000001'";
				List<MDataMap> queryAllList = DbUp.upTable("pc_tv").queryAll("", queryOrder, swhere, new MDataMap());
				
				
				//将未直播的数据加入listI  (只有当天的直播列表才可能返回未直播的数据)
				Calendar s = Calendar.getInstance();
				Calendar e = Calendar.getInstance();
				Calendar now = Calendar.getInstance();
				int saveStartingIndex = -1;//记录时最后一场正在直播的数据的索引
				for (int i = 0; i < queryAllList.size(); i++) {
					MDataMap mDataMap = queryAllList.get(i);
					s.setTime(fm.parse(mDataMap.get("form_fr_date")));
					e.setTime(fm.parse(mDataMap.get("form_end_date")));
					if(now.after(s) && now.before(e)) {
						saveStartingIndex = i;
					}
				}
				
				if(saveStartingIndex >= 0) {//有直播的数据（有正在直播的数据，则将未）
					
					for (int i = 0; i < queryAllList.size(); i++) {
						listI.add(queryAllList.get(i));
						if(i == saveStartingIndex) {
							/**
							 * 添加未直播的数据,档数由接口传入的档数编号定义  fq++
							 */
							listI.addAll(getFutrueData(inputParam.getFutureProgramCode(),inputParam.getSort()));
						}
					}
					
				} else {//没有正在直播数据，则将未直播的数据放在已经结束的数据之前
					listI.addAll(getFutrueData(inputParam.getFutureProgramCode(),inputParam.getSort()));
					listI.addAll(queryAllList);
				}
				
			}else{
				Calendar t1 = Calendar.getInstance();
				t1.setTime(format.parse(inputParam.getDate()));
				Calendar today = Calendar.getInstance();
				//System.out.println(today.getTime());
				if(t1.before(today)){
					String start = format.format(t1.getTime())+" 00:00:00";
					t1.add(Calendar.DATE, 1);
					String end = format.format(t1.getTime())+" 00:00:00";
					String swhere = "form_fr_date>='"+start+"' and form_fr_date < '"+end+"' and so_id='1000001'";
					listI = DbUp.upTable("pc_tv").queryAll("", queryOrder, swhere, new MDataMap());					
				}
			}			
			List<MDataMap> list = new ArrayList<MDataMap>();
			//过滤已下架的商品
			if (listI != null && listI.size() > 0) {
				// 获取商品信息
				PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
				for (MDataMap mDataMap : listI) {
					String productCode = mDataMap.get("good_id");
					if (StringUtils.isNotEmpty(productCode)) {
						plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
						if ("4497153900060002".equals(plusModelProductinfo.getProductStatus())) {
							list.add(mDataMap);
						}
					}
				}
			}
			
			if(list!=null&&list.size()>0){
				//商品总数
				int totalNum = list.size();
				int offset = inputParam.getPaging().getOffset();//起始页
				int limit = inputParam.getPaging().getLimit();//每页条数
				if(offset==0&&limit==0){
					limit = totalNum;
				}
				int startNum = limit*offset;//开始条数
				int endNum = startNum+limit;//结束条数
				int more = 1;//有更多数据
				if(endNum>=totalNum){
					endNum = totalNum;
					more = 0;
				}
				//如果起始条件大于总数则返回0条数据
				if(startNum>totalNum){
					startNum = 0;
					endNum = 0;
					more = 0;
				}
				
				//分页信息
				PageResults pageResults = new PageResults();
				pageResults.setTotal(totalNum);
				pageResults.setCount(endNum-startNum);
				pageResults.setMore(more);
				result.setPaged(pageResults);
				//返回商品列表
				List<MDataMap> subList = list.subList(startNum, endNum);
				List<String> productCodesArr = new ArrayList<String>();
				MDataMap salesMap = new MDataMap();
				Map<String,BigDecimal> minPriceMap = new HashMap<String, BigDecimal>();
				Map<String,Integer> mapStockNum = new HashMap<String, Integer>();
				if("SI2003".equals(getManageCode())){
					for(MDataMap map : subList){
						productCodesArr.add(map.get("good_id"));
					}
					salesMap = new ProductService().getProductFictitiousSales("SI2003",productCodesArr,30);
//					//判断是否展示内购价
//					if(null != inputParam.getBuyerType() && inputParam.getBuyerType().equals("4497469400050001")) {
//						minPriceMap = new ProductService().getMinProductActivityNew(productCodesArr, "4497469400050001");
//					} else {
//						minPriceMap = new ProductService().getMinProductActivityNew(productCodesArr,inputParam.getBuyerType());
//					}
//					if (VersionHelper.checkServerVersion("3.5.95.55")) {
						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						skuQuery.setCode(StringUtils.join(productCodesArr,","));
						skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
						skuQuery.setIsPurchase(isPurchase);
						skuQuery.setChannelId(channelId);
						minPriceMap = new ProductPriceService().
								getProductMinPrice(skuQuery);// 获取商品最低销售价格
//					}else{
//						minPriceMap = new ProductService().
//								getMinProductActivityNew(productCodesArr, inputParam.getBuyerType());// 获取商品最低销售价格
//					}
					mapStockNum = new ProductStoreService().getStockNumAll("SI2003", StringUtils.join(productCodesArr, WebConst.CONST_SPLIT_COMMA),1);

				}
				
				String formidString = "";
				Calendar now = Calendar.getInstance();
				for(MDataMap map : subList){
					TodayProduct product = new TodayProduct();
					product.setId(pre+map.get("good_id"));
					product.setPlayTime(map.get("form_fr_date"));//播出时间
					product.setEndTime(map.get("form_end_date"));//结束时间					
					Calendar start = Calendar.getInstance();
					Calendar end = Calendar.getInstance();
					start.setTime(fm.parse(map.get("form_fr_date")));
					end.setTime(fm.parse(map.get("form_end_date")));
					
					if(now.before(end)&&now.after(start)){
						product.setPlayStatus(1);//直播状态-正在直播
						product.setVideoUrlTV(TopUp.upConfig("familyhas.video_url_TV"));
						// 直播中的也设置回放地址
						product.setPlaybackUrl(getPlaybackUrl(start.getTime(), end.getTime()));
					}else if(now.before(start)){
						product.setPlayStatus(2);//直播状态-未开始
					}
					
					// 已结束的设置回放地址
					if(now.after(end)) {
						product.setPlaybackUrl(getPlaybackUrl(start.getTime(), end.getTime()));
					}
					
					// 获取商品信息
					PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(product.getId());
					PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo();
					plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
					//plusModelProductinfo = new LoadProductInfo().topInitInfo(plusModelProductQuery);
					//524:获取商品分类标签  
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
					
					
					product.setProClassifyTag(productTypeMap.get("proTypeListPic").toString() );
					
					
					//根据商品编码查询sku信息       替换为Redis 
//					MDataMap sku = DbUp.upTable("pc_productinfo").one("product_code",product.getId(),"product_status","4497153900060002");
					int num = 0;
					if(plusModelProductinfo != null){
															
						product.setName(plusModelProductinfo.getProductName());			// 商品名称						
						
						PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
						skuQuery.setCode(product.getId());
						skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
						skuQuery.setChannelId(channelId);
						PlusModelSkuInfo skuInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery, true).get(product.getId());						
						
						if(skuInfo != null) {
							if(StringUtils.isNotBlank(skuInfo.getEventCode())) {								
								if(skuInfo.getSellPrice().compareTo(skuInfo.getSkuPrice()) < 0) {
									product.setMarkPrice(MoneyHelper.format(skuInfo.getSkuPrice()));
									// 特定活动类型显示折扣
									if(ArrayUtils.contains(new String[]{"4497472600010018","4497472600010030"}, skuInfo.getEventType())) {
										// 折扣 = 活动价 / 原价 * 100
										product.setSaveValue(skuInfo.getSellPrice().multiply(new BigDecimal(100)).divide(skuInfo.getSkuPrice(),0,BigDecimal.ROUND_HALF_UP).intValue()+"");
										product.setEventType(skuInfo.getEventType());
										if(product.getSaveValue().endsWith("0")){
											product.setSaveValue(product.getSaveValue().substring(0, product.getSaveValue().length()-1));
										}
									}else if("4497471600010023".equals(skuInfo.getEventType())){
										//节省=原价-活动价
										product.setSaveValue(MoneyHelper.roundHalfUp(skuInfo.getSkuPrice().subtract(skuInfo.getSellPrice())).toString());
										product.setEventType(skuInfo.getEventType());
									}else{
										//节省=原价-活动价
										product.setSaveValue(MoneyHelper.roundHalfUp(skuInfo.getSkuPrice().subtract(skuInfo.getSellPrice())).toString());
										product.setEventType(skuInfo.getEventType());
									}

								}
							}
						}
						product.setLabelsInfo(new ProductLabelService().getLabelInfoList(product.getId()));
//						BigDecimal markPrice = plusModelProductinfo.getMarketPrice();
//						product.setMarkPrice(markPrice.toString()); // 商品市场价格
//						
//						BigDecimal maxSellPrice = plusModelProductinfo.getMaxSellPrice();
//						String discountPrice = df.format(Math.abs(Double.parseDouble(markPrice.toString())-Double.parseDouble(maxSellPrice.toString())));
//						product.setDiscountPrice(discountPrice); // 折扣价
//						String discount = df.format(Double.parseDouble(maxSellPrice.toString())*100/Double.parseDouble(markPrice.toString()));
//						product.setDiscount(discount); // 折扣率
						
						if (null != minPriceMap && !minPriceMap.isEmpty()) {
							if (minPriceMap.get(product.getId())!=null) {
								product.setSalePrice(df.format(minPriceMap.get(product.getId())));//取商品最低价
							}
						}
						
						product.setSaleNo(0);//已售件数--暂定为0
						product.setProductPic(plusModelProductinfo.getMainpicUrl()); // 商品列表图（方图）
//						if("SI2003".equals(plusModelProductinfo.getSmallSellerCode())) {
//							//取productcenter.pc_productadpic中的图片
//							product.setAdPic(getPcProductAdpic(product.getId()));
//						} else {
//							product.setAdPic(plusModelProductinfo.getAdpicUrl()); //商品广告图
//						}
						product.setAdPic(plusModelProductinfo.getAdpicUrl());
						if(StringUtils.isNotBlank(plusModelProductinfo.getVideoUrl())){
							product.setHasVideo(1); //是否有视频
						}
						
						Integer picWidth = inputParam.getPicWidth(); // 图片宽度 新增轮播图使用
						ProductService pService = new ProductService();
						List<String> pcPiclist = plusModelProductinfo.getPcPicList();
						if(pcPiclist != null && pcPiclist.size() > 0){
							List<PicInfo> picInfoList = pService.getPicInfoForMulti(picWidth , pcPiclist);
							product.setPcPicList(picInfoList); // 轮播图   copy from ApiGetEventSkuInfoNew.java
						}
						
						if("SI2009".equals(getManageCode())){
							//查询广告语
							List<MDataMap> skuList = DbUp.upTable("pc_skuinfo").queryAll("sku_adv", "sell_count", "product_code='"+product.getId()+"' and seller_code='"+getManageCode()+"'", new MDataMap());
							for (MDataMap skuMap : skuList) {
								if(StringUtility.isNotNull(skuMap.get("sku_adv"))){
									product.setSkuAdv(skuMap.get("sku_adv"));
									break;
								}
							}
						}
						
						product.setProductDetail("");//商品详情连接
						
						int stockNumBySku = (mapStockNum.get(product.getId())==null?0:mapStockNum.get(product.getId()));
						product.setStock(stockNumBySku);
						if(stockNumBySku > 0) {
							product.setFlagStock("有货");
						} else {
							product.setFlagStock("抢光了");
						}
						
						//增加商品销量
						if(VersionHelper.checkServerVersion("3.5.32.51")) {
							if("SI2009".equals(getManageCode())){
								Map<String,Map<String,String>> saleNumMap = new ProductService().getProductSales(getManageCode(), product.getId());
								if(saleNumMap!=null){
									if(saleNumMap.get(product.getId())!=null){
										String saleNum = saleNumMap.get(product.getId()).get("thirty_day");
										product.setSaleNum(Integer.parseInt(StringUtility.isNull(saleNum)?"0":saleNum));
									}
								}
							}else if("SI2003".equals(getManageCode())){
								//统计销量
								if (null != salesMap && !salesMap.isEmpty()) {
									if (StringUtils.isNotEmpty(salesMap.get(product.getId()))) {
										product.setSaleNum(Integer.parseInt(salesMap.get(product.getId())));
									}
								}
							}
							
						}
						
						/**
						 * 564版本新增
						 */
						product.setFormId(map.get("form_id"));
						//564增加节目主持人列表
						List<Map<String, Object>> infoList = DbUp.upTable("sc_tv_host").dataSqlList("SELECT DISTINCT ph.user_id,ph.host_nm,sh.host_pic from sc_tv_host sh right JOIN productcenter.pc_tv_host ph on ph.host_nm = sh.host_nm where ph.form_id = "+product.getFormId(), null);
						if(infoList.size()>0){
							String host_id = "";
							for(Map<String, Object> maph:infoList){
								if(StringUtils.isBlank(host_id)){
									host_id += maph.get("user_id").toString();
								}else{
									host_id += ","+maph.get("user_id").toString();
								}
							}
							product.setHostIds(host_id);
						}						
						
						result.getProducts().add(product);
					}else{
						num++;
					}
					result.getPaged().setTotal(result.getPaged().getTotal()-num);
					result.getPaged().setCount(result.getPaged().getCount()-num);
					
					if(StringUtils.isBlank(formidString)){
						formidString += map.get("form_id");
					}else{
						formidString += ","+map.get("form_id");
					}
				}
				
				//564增加节目主持人列表
				if(StringUtils.isNotBlank(formidString)){
					List<Map<String, Object>> infoList = DbUp.upTable("sc_tv_host").dataSqlList("SELECT DISTINCT ph.user_id,ph.host_nm,sh.host_pic from sc_tv_host sh right JOIN productcenter.pc_tv_host ph on ph.host_nm = sh.host_nm where ph.form_id in ("+formidString+")", null);
					if(infoList.size()>0){
						for(Map<String, Object> map:infoList){
							HostInfo info = new HostInfo();
							info.setZid(map.get("user_id").toString());
							info.setHostName(map.get("host_nm").toString());
							info.setHostPic(map.get("host_pic")==null?"":map.get("host_pic").toString());
							result.getHostList().add(info);
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<TodayProduct> products = result.getProducts();
		for(TodayProduct product : products) {
			String productCode = product.getId();
			ProductService service = new ProductService();
			String memberCode = "";
			MOauthInfo outhMap = getOauthInfo();
			if(outhMap != null) {
				memberCode = outhMap.getUserCode();
			}
			
			List<TagInfo> tagInfoList = service.getProductTagInfoList(productCode, memberCode,channelId);
			product.setTagInfoList(tagInfoList);
		}
		
		return result;
	}
	
	/**
	 * 未质保档数编号
	 * @param FutureProgramCode
	 * @return
	 */
	private List<MDataMap> getFutrueData (String futureProgramCode,String sort) {
		List<MDataMap> result = new ArrayList<MDataMap>();
		//查询对应档数编号对应的档数
		if(StringUtils.isNotBlank(futureProgramCode)) {
			
			List<MDataMap> futurePNumList = new DefineService().getDefineInfoByParentCode("449747160035");
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
			for (MDataMap mDataMap : futurePNumList) {
				if(mDataMap.get("define_code").equals(futureProgramCode)) {
					Integer num = Integer.valueOf(mDataMap.get("define_name"));//档数
					String cunrntTime = fm.format(new Date());
					String sWhere = "form_fr_date>='"+cunrntTime+"' and so_id='1000001'";//即将开始的数据
//					List<MDataMap> t_list = DbUp.upTable("pc_tv").query("", "form_fr_date", sWhere, new MDataMap(), 0, num);//未开始的档数
					
					//由于按档数查询，因此先查询指定的要的档数，再查询商品
					String sSql = "SELECT * FROM productcenter.pc_tv t1,(SELECT DISTINCT form_id FROM productcenter.pc_tv WHERE "+sWhere+" order by form_fr_date LIMIT 0,"+num+") t2 WHERE t1.form_id = t2.form_id order by t1.form_fr_date ";
					List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_tv").dataSqlList(sSql, new MDataMap());
					for (Map<String, Object> map : dataSqlList) {
						MDataMap m = new MDataMap();
						Set<String> keySet = map.keySet();
						for (String key : keySet) {
							m.put(key, String.valueOf(map.get(key)));
						}
						result.add(m);
					}
					if(!"0".equals(sort)) {//倒叙排列
						Collections.reverse(result);
					}
					
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据规则取商品的广告图信息
	 * @param productCode
	 * @return
	 */
	private static String getPcProductAdpic(String productCode) {
		String ret = "";
		//取得商品广告图信息
		MDataMap pcAdpicListMapParam = new MDataMap();
		pcAdpicListMapParam.put("product_code", productCode);
		pcAdpicListMapParam.put("now", DateUtil.getSysDateTimeString());
		List<MDataMap> pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "start_date desc",
				"product_code=:product_code  and (sku_code='' or sku_code is null) and start_date <=:now and end_date >=:now", pcAdpicListMapParam, -1, -1);
		if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
			ret = pcAdpicListMap.get(0).get("pic_url");
		} else {
			pcAdpicListMapParam = new MDataMap();
			pcAdpicListMapParam.put("product_code", productCode);
			pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "",
					"product_code=:product_code  and (sku_code='' or sku_code is null) and (start_date='' or start_date is null) and (end_date='' or end_date is null)", pcAdpicListMapParam, -1, -1);
			if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
				ret = pcAdpicListMap.get(0).get("pic_url");
			}
		}
		return ret;
	}

	private String getPlaybackUrl(Date startDate, Date endDate) {
		// 延后1分钟，跳过回放开头上一档的播放
		startDate = DateUtils.addMinutes(startDate, 1);
		
		String url = bConfig("familyhas.video_playback");
		String s = DateFormatUtils.format(startDate, "yyyyMMddHHmmss");
		String e = DateFormatUtils.format(endDate, "yyyyMMddHHmmss");
		return FormatHelper.formatString(url, s, e);
	}
}
