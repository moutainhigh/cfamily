package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import com.cmall.familyhas.api.model.PageTemplete;
import com.cmall.familyhas.api.model.TempleteProduct;
import com.cmall.familyhas.api.result.ApiGetTempletePageInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadWebTemplete;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelwebtemplete.PlusModelWebTempleteQuery;
import com.srnpr.xmassystem.modelwebtemplete.WebCommodity;
import com.srnpr.xmassystem.modelwebtemplete.WebTemplete;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class WebTemplateService extends BaseClass{
	
	
	public ApiGetTempletePageInfoResult getPageInfo(String pageNum) {
		
		ApiGetTempletePageInfoResult result = new ApiGetTempletePageInfoResult();
		if(StringUtil.isBlank(pageNum)) {//判断页面编号是否为空
			return result;
		}
		
//		Timestamp sys_time = DateUtil.getSysDateTimestamp();
		String sys_time = DateUtil.getSysDateTimeString();
		result.setSysTime(sys_time);
		PlusModelWebTempleteQuery tQuery = new PlusModelWebTempleteQuery();
		tQuery.setCode(pageNum);
		WebTempletePage upInfoByCode = new LoadWebTemplete().upInfoByCode(tQuery);
		
		if(StringUtil.isBlank(upInfoByCode.getPage_number())) {//如果没有该页面编号对应的页面信息，则直接返回
			return result;
		}
		
		List<String> picUrlArr = new ArrayList<String>();//压缩的专题广告图
		List<String> pcUrlArr = new ArrayList<String>();//压缩的商品主图
		
		/*
		 * 开始加载页面信息
		 */
		result.setPageNum(pageNum);
		result.setPageTitle(upInfoByCode.getTitle());
		result.setPageType(upInfoByCode.getDropdown_type());
		result.setProject_ad(upInfoByCode.getProject_ad());
		if("449747870002".equals(upInfoByCode.getIs_rel_onlive())) {
			result.setRelLive(true);//关联直播
			result.setLive_job_end_time(bConfig("familyhas.liveJobEndTime"));
			result.setLive_job_start_time(bConfig("familyhas.livejobStartTime"));
		}
		/*
		 * 添加专题模板的web的app分享信息
		 */
		String app_is_share = upInfoByCode.getApp_is_share();
		if("449747860002".equals(app_is_share)) {
			result.setShare(true);
			result.setShareImg(upInfoByCode.getShare_img());
			result.setShareContent(upInfoByCode.getShare_content());
			result.setShareTitle(upInfoByCode.getShare_title());
			
			String share_link = upInfoByCode.getShare_link();
			if(StringUtils.isBlank(share_link)) {
				result.setShareLink(bConfig("familyhas.webTemplateDefaultShareLink")+pageNum);
			} else {
				result.setShareLink(share_link);
			}
		}
		
		Map<String, String> userMap = new HashMap<String, String>();//用户mobile和membercode    key为：mobile；value：membercode；
		List<PageTemplete> tempList = new ArrayList<PageTemplete>();
		for (WebTemplete temp : upInfoByCode.getTempleteList()) {
			
			PageTemplete template = new PageTemplete();
			
			String template_type = temp.getTemplate_type();
			
			template.setTempleteBackColor(temp.getTemplate_backcolor());
			template.setIsShowDownDiscount(temp.getCommodity_min_dis());
			template.setPcBuyTypeImg(temp.getCommodity_buy_picture());
			listAddEle (picUrlArr , template.getPcBuyTypeImg());
			template.setPcTxtBackPic(temp.getCommodity_text_pic());
			listAddEle (picUrlArr , template.getPcTxtBackPic());
			template.setPcTxtBackType(temp.getCommodity_text_picture());
			template.setPcTxtBackVal(temp.getCommodity_text_value());
			template.setPcTxtColorType(temp.getCommodity_text_color());
			template.setTempleteType(temp.getTemplate_type());
			template.setTempleteNum(temp.getTemplate_number());
			
			List<TempleteProduct> pcList = new ArrayList<TempleteProduct>();
			
			if(template_type.equals("449747500001") || template_type.equals("449747500006")) {//轮播模板 || 两栏广告
			
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
				
					String commodity_number = comm.getCommodity_number();
					pc.setCategoryImg(comm.getPrograma_picture());
					listAddEle (picUrlArr , pc.getCategoryImg());
					pc.setPcPosition(comm.getCommodity_location());
					
					if(template_type.equals("449747500006")) {
						if(commodity_number.length() > 0) {
							pc.setOpenTypeVal(comm.getProduct_code());
							pc.setProductCode(comm.getProduct_code());
							pc.setSalesNum(toString(comm.getSale_num()));
						}
					} else {
						
						pc.setOpenType(comm.getSkip());
						if(comm.getSkip().equals("449747550004")) {
							String[] arr = comm.getSkip_input().split("->");
							pc.setOpenTypeVal(arr[arr.length-1]);
						} else if(comm.getSkip().equals("449747550002")){
							
							// 查询商品信息
							pc.setProductCode(comm.getProduct_code());
							pc.setOpenTypeVal(comm.getProduct_code());
						
						} else if(comm.getSkip().equals("449747550005")){
							if(StringUtils.isNotBlank(comm.getLive_mobile())) {
								
								userMap.put(comm.getLive_mobile(), "");
							}
							pc.setOpenTypeVal(comm.getLive_mobile());
						} else {
							pc.setOpenTypeVal(comm.getSkip_input());
						}
						
					}
					pcList.add(pc);
					
				}
					
			} else if(template_type.equals("449747500002") || template_type.equals("449747500003") || template_type.equals("449747500017")) {//两栏两行模版   || 一栏多行模版  || 扫码购模版
					
				for (WebCommodity comm : temp.getCommodList()) {
					
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					
					String commodity_number = comm.getCommodity_number();
					
					pc.setProductCode(comm.getProduct_code());
					pc.setCategoryImg(comm.getPrograma_picture());//模板一用
					listAddEle (picUrlArr , pc.getCategoryImg());
					pc.setIsShowDiscount(comm.getIs_dis());//是否展示折扣
					pc.setOpenType(comm.getSkip());//打开方式
					pc.setOpenTypeVal(comm.getSkip_input());//打开方式值
					pc.setPcDesc(comm.getCommodity_describe());//商品描述
					if(comm.getCommodity_picture().length() > 0) {
						pc.setPcImg(comm.getCommodity_picture());//商品图片
					} else {//默认展示主图
						pc.setPcImg(comm.getMain_url());//商品图片
					}
					listAddEle (pcUrlArr , pc.getPcImg());
					pc.setPcName(comm.getCommodity_name());//商品名称
					pc.setPcNum(commodity_number);//sku编号
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					
					///////////////////////////////////////////////////
					BigDecimal price = comm.getPrice();
					BigDecimal market_price = comm.getMarket_price();
					if(market_price.compareTo(BigDecimal.ZERO) <= 0) {
						market_price = BigDecimal.ZERO;
					}
					if(market_price.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = price.divide(market_price, 2, BigDecimal.ROUND_HALF_UP);
						pc.setPcDiscount(toString(discount));//商品折扣
					} else {
						pc.setPcDiscount("0");//商品折扣
					}
					pc.setMarketPrice(toString(market_price.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品市场价
					pc.setPcPrice(toString(price.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品售价
					
					pc.setSalesNum(toString(comm.getSale_num()));//商品库存
					
					if(template_type.equals("449747500017")) {
						pc.setPreferential_desc(comm.getPreferential_desc());
					}
					
					pcList.add(pc);
					
				}
			} else if(template_type.equals("449747500004")) {//视频模板
					
				template.setTempletePic(temp.getCommodity_picture());//模版图
				listAddEle (picUrlArr , template.getTempletePic());
				String videoUrlTV = bConfig("familyhas.video_url_TV");
				template.setTvUrl(videoUrlTV);
				/*
				 * 视频直播 添加直播的商品信息
				 */
				//查询直播商品信息(调用 '电视tv' 接口信息 )
				String swhere = "form_end_date>='"+sys_time+"' and form_fr_date <= '"+sys_time+"' and so_id='1000001'";
				List<MDataMap> products = DbUp.upTable("pc_tv").queryAll("good_id", "", swhere, new MDataMap());
				for (int i = 0; i < products.size(); i++) { 
					MDataMap onLivePcInfo = products.get(i);
					String pcNum = onLivePcInfo.get("good_id");
					TempleteProduct pc = new TempleteProduct();
					
					PlusModelProductQuery plus = new PlusModelProductQuery(pcNum);
					PlusModelProductInfo upInfoByCode1 = new LoadProductInfo().upInfoByCode(plus);
					
					pc.setCategoryImg(upInfoByCode1.getMainpicUrl());//商品主图|商品列表图（方图）
					DecimalFormat df = new DecimalFormat("#.00");
					pc.setMarketPrice(String.valueOf(df.format(upInfoByCode1.getMarketPrice())));//市场价
					//获取售价(获取所有sku 的最低价，取最低价格)
					PlusSupportProduct psp = new PlusSupportProduct();
					List<PlusModelProductSkuInfo> skuList = upInfoByCode1.getSkuList();
					BigDecimal minSkuSellPrice = BigDecimal.ZERO;
					if(skuList.size() > 0) {
						BigDecimal[] skuSellPriceArr = new BigDecimal[skuList.size()];
						for (int j = 0; j < skuList.size(); j++) {
							PlusModelSkuInfo price = psp.upSkuInfoBySkuCode(skuList.get(j).getSkuCode());
							skuSellPriceArr[j] = price.getSellPrice();
						}
						Arrays.sort(skuSellPriceArr);
						minSkuSellPrice = skuSellPriceArr[0];
					} else {//不存在sku，则取商品最低价格
						minSkuSellPrice = upInfoByCode1.getMinSellPrice();
					}
					pc.setPcPrice(String.valueOf(df.format(minSkuSellPrice)));//售价
					pc.setPcName(upInfoByCode1.getProductName());//商品名称
					pc.setProductCode(pcNum);//商品编号
					
					pcList.add(pc);
					
				}
				
			} else if(template_type.equals("449747500005")) {//标题模板
				template.setTemplate_title_name(temp.getTemplate_title_name());
				template.setTempletePic(temp.getCommodity_picture());//模版图
				listAddEle(picUrlArr,template.getTempletePic());
			} else if(template_type.equals("449747500007") || template_type.equals("449747500008")) {
				
				StringBuffer couponBF = new StringBuffer();
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					String coupon = comm.getCoupon();
					couponBF.append("'"+coupon+"'");
					couponBF.append(",");
				}
				if(couponBF.length() > 0) {
					couponBF.deleteCharAt(couponBF.length() -1);
					List<MDataMap> queryAll = DbUp.upTable("oc_coupon_cdkey").queryAll("activity_code,account_useTime,cdkey", "", "cdkey in("+couponBF.toString()+")", new MDataMap());
					Map<String, String> couponMap = new HashMap<String, String>();
					for (MDataMap coupon : queryAll) {
						couponMap.put(coupon.get("cdkey"), coupon.get("activity_code"));
					}
					for (WebCommodity comm : temp.getCommodList()) {
						//判断时间是否有效
						if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
							continue;
						}
						TempleteProduct pc = new TempleteProduct();
						pc.setCategoryImg(comm.getPrograma_picture());
						listAddEle(picUrlArr,pc.getCategoryImg());
						String coupon = comm.getCoupon();
						pc.setCoupon(coupon);
						String activity_code = couponMap.get(comm.getCoupon());
						if( null != activity_code) {
							pc.setActivity_code(activity_code);
						} 
						pcList.add(pc);
					}
				}
			}else if(template_type.equals("449747500009")) {//普通视频模板
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					TempleteProduct pc = new TempleteProduct();
					
					String commodity_number = comm.getCommodity_number();
					
					
					pc.setProductCode(comm.getProduct_code());
					pc.setCategoryImg( comm.getPrograma_picture());//栏目图
					listAddEle(picUrlArr,pc.getCategoryImg());
					//添加商品之后
					if(commodity_number != null && commodity_number.length() > 0){
						pc.setPcDesc(comm.getCommodity_describe());//商品描述
						String commodity_picture = comm.getCommodity_picture();
						if(commodity_picture.length() > 0) {
							pc.setPcImg(commodity_picture);//商品图片
						} else {//默认展示主图
							pc.setPcImg(comm.getMain_url());//商品图片
						}
						listAddEle(pcUrlArr, pc.getPcImg());
						pc.setPcName(comm.getCommodity_name());//商品名称
						pc.setPcPosition(comm.getCommodity_location());//商品展示位置
						pc.setPcNum(commodity_number);//商品编号
						BigDecimal price = comm.getPrice();
						BigDecimal market_price = comm.getMarket_price();
						if(market_price.compareTo(BigDecimal.ZERO) < 0) {
							market_price = BigDecimal.ZERO;
						}
						if(market_price.compareTo(new BigDecimal(0)) > 0) {
							BigDecimal discount = price.divide(market_price, 2, 4);
							pc.setPcDiscount(String.valueOf(discount));//商品折扣
						} else {
							pc.setPcDiscount("0");//商品折扣
						}
						pc.setMarketPrice(toString(market_price.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品市场价
						pc.setPcPrice(String.valueOf(price.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品售价
						pc.setPcStatus(comm.getProduct_status());//商品状态
					}
					pc.setUrl(comm.getUrl());
					pc.setTemplate_desc(comm.getTemplate_desc());
					
					pcList.add(pc);
					
				}
				
			}else if(template_type.equals("449747500010") || template_type.equals("449747500011") || template_type.equals("449747500014") || template_type.equals("449747500015") || template_type.equals("449747500016")) {//一行多栏（横滑），一行三栏,两栏多行（推荐）,右两栏推荐,左两栏推荐
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					String commodity_number = comm.getCommodity_number();
					
					String product_code = comm.getProduct_code();
					
					pc.setProductCode(product_code);
					pc.setCategoryImg( comm.getPrograma_picture());
					listAddEle(picUrlArr, pc.getCategoryImg());
					pc.setIsShowDiscount(comm.getIs_dis());//是否展示折扣
					String skip = comm.getSkip();
					String skip_input = comm.getSkip_input();
					pc.setOpenType(skip);//打开方式
					pc.setOpenTypeVal(skip_input);//打开方式值
					//判断打开方式
					if(skip.equals("449747550004")) {
						
						String[] arr = skip_input.split("->");
						pc.setOpenTypeVal(arr[arr.length-1]);
						
					} else if(skip.equals("449747550002")){
						pc.setProductCode(product_code);
						pc.setOpenTypeVal(product_code);
						
					} else if(skip.equals("449747550005")){
						if(StringUtils.isNotBlank(comm.getLive_mobile())) {
							userMap.put(comm.getLive_mobile(), "");
						}
						pc.setOpenTypeVal(comm.getLive_mobile());
					} else {
						pc.setOpenTypeVal(skip_input);
					}
					pc.setPcDesc(comm.getCommodity_describe());//商品描述
					String commodity_picture = comm.getCommodity_picture();
					if(commodity_picture.length() > 0) {
						pc.setPcImg( commodity_picture);//商品图片
					} else {//默认展示主图
						pc.setPcImg( comm.getMain_url());//商品图片
					}
					listAddEle(pcUrlArr, pc.getPcImg());
					pc.setPcName(comm.getCommodity_name());//商品名称
					pc.setPcNum(commodity_number);//商品编号
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					BigDecimal price = comm.getPrice();
					BigDecimal market_price = comm.getMarket_price();
					if(market_price.compareTo(BigDecimal.ZERO) < 0) {
						market_price = BigDecimal.ZERO;
					}
					if(market_price.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = price.divide(market_price, 2, 4);
						pc.setPcDiscount(String.valueOf(discount.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品折扣
					} else {
						pc.setPcDiscount("0");//商品折扣
					}
					pc.setMarketPrice(toString(market_price.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品市场价
					pc.setPcPrice(String.valueOf(price));//商品售价
					pc.setPcStatus(comm.getProduct_status());//商品状态
					pc.setWidth(comm.getWidth());//宽度
					
					if( template_type.equals("449747500014") || template_type.equals("449747500015") || template_type.equals("449747500016")) {
						pc.setTitle(comm.getTitle());
						pc.setTitle_color(comm.getTitle_color());
						pc.setDes_content(comm.getDescribes());
						pc.setDes_content_color(comm.getDescribe_color());
						
					}
					
					pcList.add(pc);
				
				}
				
			}else if(template_type.equals("449747500012") || template_type.equals("449747500013")){//分类模版   本地货模版
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();

					pc.setCategoryImg(comm.getPrograma_picture());//栏目图
					listAddEle(picUrlArr, pc.getCategoryImg());
					listAddEle(picUrlArr, pc.getCategoryImg());
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					pc.setUrl(comm.getUrl());
					pc.setImg(comm.getImg());
					listAddEle(picUrlArr, pc.getImg());
					pc.setCity_name(comm.getCity_name());
					pc.setWidth(comm.getWidth());
					pcList.add(pc);
				}
				
			} else if(template_type.equals("449747500018") ) {//页面定位模板
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();

					pc.setCategoryImg(comm.getPrograma_picture());//栏目图
					listAddEle(picUrlArr, pc.getCategoryImg());
					listAddEle(picUrlArr, pc.getCategoryImg());
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					pc.setUrl(comm.getUrl());
					pc.setImg(comm.getImg());
					listAddEle(picUrlArr, pc.getImg());
					pc.setCity_name(comm.getCity_name());
					pc.setWidth(comm.getWidth());
					pcList.add(pc);
				}
				
			} 
		
			template.setPcList(pcList);//模板对应的商品列表
			
			tempList.add(template );//添加模板
		}
		Set<Entry<String, String>> entrySet = userMap.entrySet();
		StringBuffer mobileBuffer = new StringBuffer();
		for (Entry<String, String> keyEntry : entrySet) {
			mobileBuffer.append(keyEntry.getKey());
			mobileBuffer.append(",");
		}
		if(mobileBuffer.length() > 0) {
			mobileBuffer.delete(mobileBuffer.length()-1, mobileBuffer.length());
			List<MDataMap> queryAll = DbUp.upTable("mc_login_info").queryAll("login_name,member_code", "", " manage_code = 'SI2003' and login_name IN("+mobileBuffer.toString()+")", new MDataMap());
			for (MDataMap mDataMap : queryAll) {
				userMap.put(mDataMap.get("login_name"), mDataMap.get("member_code"));
			}
			result.getUserMobile_MemCode().putAll(userMap);
			
		}
		//开始压缩图片
		Map<String, String> templeteAllImage = getTempleteAllImage(pcUrlArr,picUrlArr ,80);//取专题所有图片对应宽度的所有缓存图片
		//循环替换所有已经压缩的图片链接，将原图链接替换成指定宽度压缩后的图片
		for (int i = 0; i < tempList.size(); i++) {
			PageTemplete pageTemplete = tempList.get(i);
			pageTemplete.setPcBuyTypeImg(getValueByKey(templeteAllImage, pageTemplete.getPcBuyTypeImg()));
			pageTemplete.setPcTxtBackPic(getValueByKey(templeteAllImage, pageTemplete.getPcTxtBackPic()));
			pageTemplete.setTempletePic(getValueByKey(templeteAllImage, pageTemplete.getTempletePic()));
			List<TempleteProduct> pcList = pageTemplete.getPcList();
			for (TempleteProduct pc : pcList) {
				pc.setCategoryImg(getValueByKey(templeteAllImage , pc.getCategoryImg()));
				pc.setImg(getValueByKey(templeteAllImage, pc.getImg()));
				pc.setCategoryImg(getValueByKey(templeteAllImage , pc.getCategoryImg()));
				pc.setPcImg(getValueByKey(templeteAllImage, pc.getPcImg()));//商品图片
			}
		}
		result.setTempList(tempList);
		
		return result;
	}
	
	
	private String toString (Object obj) {
		if(null == obj) {
			obj = "";
		}
		return String.valueOf(obj).trim();
	}
	
	/**
	 * 日期比较
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	private int compare_date(String DATE1, String DATE2) {
        
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	/**
	 * 添加图片链接进行压缩，过滤空链接
	 */
	private void listAddEle (List<String > picArr ,String picUrl) {
		if(StringUtils.isNotBlank(picUrl) && !picArr.contains(picUrl)) {
			picArr.add(picUrl);
		}
	}
	
	/**
	 * 获取所有专题图片对应的压缩后的图片
	 * @param pcUrlArr 商品主图
	 * @param picUrlArr 专题栏目所有图片
	 * @param width
	 * @return
	 */
	private Map<String,String> getTempleteAllImage(List<String> pcUrlArr,List<String> picUrlArr ,Integer width) {
		
		Map<String,String> picMap = new HashMap<String,String>();//存放压缩后的图片（key：原图地址；value:压缩后的新图地址；）
		
		/*
		 * 压缩图片替换原图
		 */
		if(width > 0 && picUrlArr.size() > 0) {//广告图
			/*
			 * 进行图片压缩
			 */
			ProductService pService = new ProductService();
			//压缩图片
			List<PicInfo> picInfoList = pService.compressImage( false, width, picUrlArr, "" );
			for (PicInfo picInfo : picInfoList) {
				picMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			}
			
		}
		if(width > 0 && pcUrlArr.size() > 0) {//商品主图
			/*
			 * 进行图片压缩
			 */
			ProductService pService = new ProductService();
			//压缩图片
			List<PicInfo> picInfoList = pService.compressImage( true, width , pcUrlArr, "" );
			for (PicInfo picInfo : picInfoList) {
				picMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			}
			
		}
		return picMap;
	}
	
	/**
	 * 获取map中对应的值
	 */
	private String getValueByKey (Map<String, String> map,String key) {
		String result = "";
		if(null != map && StringUtils.isNotBlank(key) && map.containsKey(key)) {
			result = map.get(key);
		} else {
			result = key;
		}
		return result;
	}
	
	

}
