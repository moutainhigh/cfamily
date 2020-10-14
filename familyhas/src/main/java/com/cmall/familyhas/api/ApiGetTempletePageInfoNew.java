package com.cmall.familyhas.api;

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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import com.cmall.familyhas.api.input.ApiGetTempletePageInfoInput;
import com.cmall.familyhas.api.model.PageHuDongInfo;
import com.cmall.familyhas.api.model.PageTemplete;
import com.cmall.familyhas.api.model.TemplateCouponType;
import com.cmall.familyhas.api.model.TempleteProduct;
import com.cmall.familyhas.api.result.ApiGetTempletePageInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.duohuozhu.utils.MD5Util;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.load.LoadEventExcludeProduct;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadEventVipDiscount;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadWebTemplete;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelEventExclude;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelevent.PlusModelVipDiscount;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelwebtemplete.PlusModelWebTempleteQuery;
import com.srnpr.xmassystem.modelwebtemplete.WebCommodity;
import com.srnpr.xmassystem.modelwebtemplete.WebTemplete;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.service.PlusServicePurchase;
import com.srnpr.xmassystem.service.PlusServiceVipDiscount;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topcache.SimpleCache;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
/**
 * 获取web专题模板信息（新，走缓存）
 * @author fq
 *
 */
public class ApiGetTempletePageInfoNew extends RootApiForVersion<ApiGetTempletePageInfoResult,ApiGetTempletePageInfoInput>{

	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();
	PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
	LoadCouponType loadCouponType = new LoadCouponType();
	LoadCouponActivity loadCouponActivity = new LoadCouponActivity();
	ProductService pService = new ProductService();
	private static final String needLoginTemplateType = "449747500007,449747500008,449747500020,449747500022";
	
	@Override
	public ApiGetTempletePageInfoResult Process(ApiGetTempletePageInfoInput inputParam, MDataMap mRequestMap) {
		
		ApiGetTempletePageInfoResult result = new ApiGetTempletePageInfoResult();
		
		String pageNum = inputParam.getPageNum();
		String mobile = inputParam.getMobile();//客户登录手机号，过滤是否有会员折扣相关活动，从而展示活动的价格
		String channelId = inputParam.getChannelId();
		
		if(StringUtil.isBlank(pageNum)) {//判断页面编号是否为空
			return result;
		}
		
		String memberCode = null;
		if(StringUtils.isNotBlank(mobile)) {
			memberCode = (String)DbUp.upTable("mc_login_info").dataGet("member_code", "", new MDataMap("manage_code", getManageCode(), "login_name", mobile));
		}
		
		// 如果传了token则优先使用当前登录的用户编号
		// 542版本开始用token替换传入的手机号参数
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
			mobile = getOauthInfo().getLoginName();
		}
		
//		Timestamp sys_time = DateUtil.getSysDateTimestamp();
		String sys_time = DateUtil.getSysDateTimeString();
		result.setSysTime(sys_time);
		PlusModelWebTempleteQuery tQuery = new PlusModelWebTempleteQuery();
		tQuery.setCode(pageNum+"-"+channelId);
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
		
		
		/*
		 * 解析转换模板
		 */
		List<PageTemplete> parseTemplateInfo = this.parseTemplateInfo( upInfoByCode.getTempleteList() ,picUrlArr,pcUrlArr, userMap, sys_time ,inputParam.getSourceCode(), memberCode, channelId);
		
		//判断是否存在需要登录的模板
		boolean flag = false;
		for (PageTemplete pageTemplete : parseTemplateInfo) {
			String templeteType = pageTemplete.getTempleteType();
			if("449747500018".equals(templeteType) || "449747500021".equals(templeteType)) {
				List<TempleteProduct> pcList = pageTemplete.getPcList();
				if(pcList != null && pcList.size() > 0) {					
					for (TempleteProduct templeteProduct : pcList) {
						List<PageTemplete> rel_templete = templeteProduct.getRel_templete();
						if(rel_templete != null && rel_templete.size() > 0) {
							for (PageTemplete pageTemplete2 : rel_templete) {
								if(needLoginTemplateType.contains(pageTemplete2.getTempleteType())) {
									result.setNeedLogin(true);
									flag = true;
									break;
								}
							}
							if(flag) {
								break;
							}
						}
					}
					if(flag) {
						break;
					}
				}
			}else {				
				if(needLoginTemplateType.contains(pageTemplete.getTempleteType())) {
					result.setNeedLogin(true);
					break;
				}
			}
		}
		
		tempList.addAll(parseTemplateInfo);
		
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
		Map<String, String> templeteAllImage = getTempleteAllImage(pcUrlArr,picUrlArr ,Constants.IMG_WIDTH_SP00);//取专题所有图片对应宽度的所有缓存图片
		//循环替换所有已经压缩的图片链接，将原图链接替换成指定宽度压缩后的图片
		replaceImage(tempList,templeteAllImage);//普通模板
		
		for (int i = 0; i < tempList.size(); i++) {//关联模板
			List<TempleteProduct> pcList = tempList.get(i).getPcList();
			String type = tempList.get(i).getTempleteType();
			for (TempleteProduct pc : pcList) {
				if( null != pc.getRel_templete() && pc.getRel_templete().size() > 0) {
					replaceImage(pc.getRel_templete(),templeteAllImage);
				}
				if("449747500003".equals(type)||"449747500010".equals(type)||"449747500011".equals(type)){
					pc.setPcImg(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, pc.getPcImg()).getPicNewUrl());
					pc.setCategoryImg(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP03, pc.getCategoryImg()).getPicNewUrl());
				}
				if("449747500002".equals(type)||"449747500006".equals(type)||"449747500014".equals(type)){
					pc.setPcImg(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, pc.getPcImg()).getPicNewUrl());
					pc.setCategoryImg(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, pc.getCategoryImg()).getPicNewUrl());
				}
			}
			/**
			 * NG++ 新增兑换码兑换模板，如果是兑换码兑换，根据登陆用户查询兑换码。
			 */
			if("449747500022".equals(type)) {
				PageTemplete p = tempList.get(i);
				MDataMap templateMap = DbUp.upTable("fh_data_template").one("template_number",p.getTempleteNum());
				String activityCode = templateMap.get("activity_code");
				PlusModelQuery queryActivity = new PlusModelQuery(activityCode);
				PlusModelCouponActivity activity = new LoadCouponActivity().upInfoByCode(queryActivity);
				String sqlActivity = "SELECT * FROM ordercenter.oc_activity WHERE activity_code = '"+activityCode+"' AND flag = 1 AND begin_time <= sysdate() AND end_time >= sysdate()";
				Map<String,Object> map = DbUp.upTable("oc_activity").dataSqlOne(sqlActivity, null);
				if(activity != null&&map != null) {
					String exchangeCode = "";
					Integer count = 0;
					if(!StringUtils.isEmpty(memberCode)&&!StringUtils.isEmpty(activityCode)) {
						String sql = "SELECT * FROM ordercenter.oc_coupon_redeem WHERE activity_code = '"+activityCode+"' AND is_redeem = 0 AND member_code = '"+memberCode+"' order by zid desc limit 1";
						Map<String,Object> redeemCodeMap = DbUp.upTable("oc_coupon_redeem").dataSqlOne(sql, null);
						if(redeemCodeMap != null) {
							exchangeCode = redeemCodeMap.get("activity_cdkey").toString();
						}
						count = DbUp.upTable("oc_coupon_redeem").count("activity_code",activityCode,"member_code",memberCode,"is_redeem","0");
						if(count == null) {
							count = 0;
						}
					}
					p.setExchangeCode(exchangeCode);
					p.setExchangeCodeCount(count.toString());
					p.setExchangeEndTime(activity.getEndTime());
					p.setActivityCode(activityCode);
				}
			}
			
		}
		result.setTempList(tempList);
		
		//过滤是否有会员登记活动
		refreshVipLevelActivity(mobile,tempList,sys_time,channelId);
		Map<String,List<String>> tagMap = new HashMap<String,List<String>>();
		//5.4.2版本中新增活动标签列表
		/*
		ProductService ps = new ProductService();
		for(PageTemplete temp : tempList) {
			List<TempleteProduct> products = temp.getPcList();
			for(TempleteProduct product : products) {
				String productCode = product.getProductCode();
				
				List<String> tagList = tagMap.get(productCode); 
				if(tagList == null || tagList.size()==0) {
					tagList = ps.getTagListByProductCode(productCode,memberCode);
					if(tagList!=null&&tagList.size()>0) {
						tagMap.put(productCode, tagList);
					}
				}
				product.setTagList(tagList);
			}
		}
		*/
		
		//查询专题参加的浏览专题送积分活动信息
		String sql = "SELECT a.event_code,a.url,c.* FROM sc_hudong_event_ad_template a INNER JOIN sc_hudong_event_info b ON a.event_code = b.event_code INNER JOIN sc_hudong_event_ad_jf_rule c ON a.event_code = c.event_code"+
					 " WHERE b.event_type_code = 449748210007 AND b.event_status = 4497472700020002 AND NOW() BETWEEN b.begin_time AND b.end_time AND a.page_number = '"+pageNum+"'";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_hudong_event_ad_template").dataSqlList(sql, null);
		if(dataSqlList.size()>0){
			result.setIsZtHuDong("Y");
			PageHuDongInfo info = new PageHuDongInfo();
			info.setEvent_code(dataSqlList.get(0).get("event_code")+"");
			info.setUrl(dataSqlList.get(0).get("url")+"");
			info.setLq_times(dataSqlList.get(0).get("lq_times")+"");
			info.setF_time(dataSqlList.get(0).get("f_time")+"");
			info.setF_integral(dataSqlList.get(0).get("f_integral")+"");
			info.setS_time(dataSqlList.get(0).get("s_time")+"");
			info.setS_integral(dataSqlList.get(0).get("s_integral")+"");
			info.setT_time(dataSqlList.get(0).get("t_time")+"");
			info.setT_integral(dataSqlList.get(0).get("t_integral")+"");
			if(StringUtils.isNotBlank(memberCode)){
				String sSql = "SELECT IFNULL(MAX(event_seq),0) event_seq from lc_hudong_event_ad_jf_lq WHERE page_number = '"+pageNum+"' AND member_code = '"+memberCode+"' AND TO_DAYS(etr_date) = TO_DAYS(NOW())";
				List<Map<String, Object>> list = DbUp.upTable("lc_hudong_event_ad_jf_lq").dataSqlList(sSql, null);
				if(list.size()>0){
					info.setUser_lq_times(list.get(0).get("event_seq").toString());
				}
			}
			result.setHuDongInfo(info);
			String signed = MD5Util.MD5Encode(pageNum+"jiayougo", "utf-8");
			result.setJfSign(signed);
		}
		
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
        
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
			List<PicInfo> picInfoList = pService.compressImage( false, 1080, picUrlArr, "" );
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
			List<PicInfo> picInfoList = pService.compressImage( false, 570 , pcUrlArr, "" );
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
	/**
	 * 解析redis中模板数据
	 * @param templeteList
	 * @param picUrlArr
	 * @param pcUrlArr
	 * @param userMap
	 * @param sys_time
	 * @param sourceCode
	 * @return
	 */
	private List<PageTemplete> parseTemplateInfo(List<WebTemplete> templeteList ,List<String> picUrlArr,List<String> pcUrlArr,Map<String, String> userMap,String sys_time ,String sourceCode, String memberCode, String channelId ) {
		
		List<PageTemplete> tempList = new ArrayList<PageTemplete>();
		
		for (WebTemplete temp : templeteList) {
			
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
			template.setSell_price_color(temp.getSell_price_color());
			template.setTemplateTitleColorSelected(temp.getTemplate_title_color_selected());
			template.setTemplateTitleColor(temp.getTemplate_title_color());
			template.setTemplateBackcolorSelected(temp.getTemplate_backcolor_selected());
			template.setColumnNum(temp.getColumn_num());
			template.setSplitBar(temp.getSplit_bar());
			List<TempleteProduct> pcList = new ArrayList<TempleteProduct>();
			
			if(template_type.equals("449747500001") || template_type.equals("449747500006")) {//轮播模板 || 两栏广告
			
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
				    //5.4.2 查询商品下架记录表中是否有下架记录
					int count = DbUp.upTable("pc_productinfo").count("product_code",comm.getProduct_code(),"product_status","4497153900060002");
					
					String commodity_number = comm.getCommodity_number();
					pc.setCategoryImg(comm.getPrograma_picture());
					listAddEle (picUrlArr , pc.getCategoryImg());
					pc.setPcPosition(comm.getCommodity_location());
					pc.setCostPrice(comm.getCostPrice());
					pc.setSmallSellerCode(comm.getSmallSellerCode());
					pc.setSkuPrice(comm.getSkuPrice());
					
					if(template_type.equals("449747500006")) {
						if(commodity_number.length() > 0) {
							pc.setOpenTypeVal(comm.getProduct_code());
							pc.setProductCode(comm.getProduct_code());
							pc.setSalesNum(count>0?toString(comm.getSale_num()):"0");
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
						} else if(comm.getSkip().equals("449747550006")){
							String tem = bConfig("xmassystem.plus_product_code")+"|"+bConfig("xmassystem.plus_sku_code");
							pc.setOpenTypeVal(tem);
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
					 //5.4.2 查询商品下架记录表中是否有下架记录
					int count = DbUp.upTable("pc_productinfo").count("product_code",comm.getProduct_code(),"product_status","4497153900060002");
					
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
					pc.setCostPrice(comm.getCostPrice());
					pc.setSmallSellerCode(comm.getSmallSellerCode());
					pc.setSkuPrice(comm.getSkuPrice());
					
					
					///////////////////////////////////////////////////
					BigDecimal price = comm.getPrice();
					
					if(template_type.equals("449747500003")) {//556:一栏多行模板添加自营标签
						PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(comm.getProduct_code()));
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
						pc.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());		
					}
					if(template_type.equals("449747500017")) {//如果是扫码购模板，则获取扫码价
						if ("APP".equals(sourceCode)) {//app来源的扫码
							if(comm.getApp_smg_price().compareTo(BigDecimal.ZERO) > 0) {//扫码价必须大于0
								price = comm.getApp_smg_price();
							}
						} else {//默认为微信商城扫码
							if(comm.getSmg_price().compareTo(BigDecimal.ZERO) > 0) {//扫码价必须大于0
								price = comm.getSmg_price();
							}
						}
					}
					
					
					BigDecimal skuPrice = comm.getSkuPrice();
					if(skuPrice.compareTo(BigDecimal.ZERO) <= 0) {
						skuPrice = BigDecimal.ZERO;
					}
					if(skuPrice.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = price.divide(skuPrice, 2, BigDecimal.ROUND_HALF_UP);
						pc.setPcDiscount(toString(discount));//商品折扣
					} else {
						pc.setPcDiscount("0");//商品折扣
					}
					pc.setMarketPrice(toString(comm.getMarket_price()));//商品市场价
					pc.setPcPrice(toString(price));//商品售价
					////////////////////////////////////////////////////////////////////////
					//pc.setSalesNum(toString(comm.getSale_num()));//商品库存
					//5.4.2
					pc.setSalesNum(count>0?toString(comm.getSale_num()):"0");
					
					if(template_type.equals("449747500017")) {
						pc.setPreferential_desc(comm.getPreferential_desc());
					}
					
					// 商品标签列表图片
					//PlusModelProductLabel label = new ProductLabelService().getLabelInfo(pc.getProductCode());
					//if(label != null && label.getFlagEnable() == 1 && StringUtils.isNotBlank(label.getListPic())){
					//	pc.getProductLabelPicList().add(label.getListPic());
					//}
					pc.setLabelsInfo(new ProductLabelService().getLabelInfoList(pc.getProductCode()));
					
					//5.5.0商品活动标签为拼团，则将"马上抢购"变为"去拼团"
					if(template_type.equals("449747500003")) {
						PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
						plusModelSkuQuery.setCode(commodity_number);
						plusModelSkuQuery.setChannelId(channelId);
						PlusModelSkuInfo skuInfo = new LoadSkuInfo().upInfoByCode(plusModelSkuQuery);
						//此处刷新一下活动 避免活动过期 缓存未更新
						PlusModelSkuInfo refreshSkuEvent = new PlusSupportEvent().refreshSkuEvent(skuInfo, plusModelSkuQuery);
						if(refreshSkuEvent.getEventType().contains("4497472600010024")) {
							pc.setButtonType("2");
						}else {
							pc.setButtonType("1");
						}
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
				String swhere = "form_end_date>='"+sys_time+"' and form_fr_date <= '"+sys_time+"'  and so_id='1000001'";
				List<MDataMap> products = DbUp.upTable("pc_tv").queryAll("good_id", "", swhere, new MDataMap());
				for (int i = 0; i < products.size(); i++) { 
					MDataMap onLivePcInfo = products.get(i);
					String pcNum = onLivePcInfo.get("good_id");
					TempleteProduct pc = new TempleteProduct();
					
					PlusModelProductQuery plus = new PlusModelProductQuery(pcNum);
					PlusModelProductInfo upInfoByCode1 = new LoadProductInfo().upInfoByCode(plus);
					
					pc.setCategoryImg(upInfoByCode1.getMainpicUrl());//商品主图|商品列表图（方图）
					DecimalFormat df = new DecimalFormat("#.##");
					pc.setMarketPrice(String.valueOf(df.format(upInfoByCode1.getMarketPrice())));//市场价
					//获取售价(获取所有sku 的最低价，取最低价格)
					PlusSupportProduct psp = new PlusSupportProduct();
					List<PlusModelProductSkuInfo> skuList = upInfoByCode1.getSkuList();
					BigDecimal minSkuSellPrice = BigDecimal.ZERO;
					if(skuList.size() > 0) {
						BigDecimal[] skuSellPriceArr = new BigDecimal[skuList.size()];
						for (int j = 0; j < skuList.size(); j++) {
							PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
							plusModelSkuQuery.setCode(skuList.get(j).getSkuCode());
							plusModelSkuQuery.setChannelId(channelId);
							PlusModelSkuInfo price = psp.upSkuInfo(plusModelSkuQuery).getSkus().get(0);
							skuSellPriceArr[j] = price.getSellPrice();
							pc.setSkuPrice(price.getSkuPrice());
						}
						Arrays.sort(skuSellPriceArr);
						minSkuSellPrice = skuSellPriceArr[0];
					} else {//不存在sku，则取商品最低价格
						minSkuSellPrice = upInfoByCode1.getMinSellPrice();
					}
					pc.setPcPrice(String.valueOf(df.format(minSkuSellPrice)));//售价
					pc.setPcName(upInfoByCode1.getProductName());//商品名称
					pc.setProductCode(pcNum);//商品编号
					pc.setCostPrice(upInfoByCode1.getCost_price());
					pc.setSmallSellerCode(upInfoByCode1.getSmallSellerCode());
					
					// 商品标签列表图片
					PlusModelProductLabel label = new ProductLabelService().getLabelInfo(pc.getProductCode());
					if(label != null && label.getFlagEnable() == 1 && StringUtils.isNotBlank(label.getListPic())){
						pc.getProductLabelPicList().add(label.getListPic());
					}
					
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
						//新加宽度设置   width   fq++
						if(template_type.equals("449747500008")) {//将之前优惠券模板的 “一栏一行” 的更改为 “一栏多行” ， 因此加width 前段控制显示宽度，超一屏幕则影藏
							//由于之前设置的一栏一行没有设置宽度属性，为兼容老数据，没有宽度的默认为100 (对于前段来说就是100% )
							if(StringUtils.isNotBlank(comm.getWidth())) {
								pc.setWidth(comm.getWidth());
							} else {
								pc.setWidth("100");
							}
							
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
						pc.setCostPrice(comm.getCostPrice());
						pc.setSmallSellerCode(comm.getSmallSellerCode());
						pc.setSkuPrice(comm.getSkuPrice());
						
						BigDecimal price = comm.getPrice();
						BigDecimal skuPrice = comm.getSkuPrice();
						if(skuPrice.compareTo(BigDecimal.ZERO) < 0) {
							skuPrice = BigDecimal.ZERO;
						}
						if(skuPrice.compareTo(new BigDecimal(0)) > 0) {
							BigDecimal discount = price.divide(skuPrice, 2, 4);
							pc.setPcDiscount(String.valueOf(discount));//商品折扣
						} else {
							pc.setPcDiscount("0");//商品折扣
						}
						pc.setMarketPrice(toString(comm.getMarket_price()));//商品市场价
						pc.setPcPrice(String.valueOf(price));//商品售价
						pc.setPcStatus(comm.getProduct_status());//商品状态
					}
					pc.setUrl(comm.getUrl());
					pc.setTemplate_desc(comm.getTemplate_desc());
					
					// 商品标签列表图片
					PlusModelProductLabel label = new ProductLabelService().getLabelInfo(pc.getProductCode());
					if(label != null && label.getFlagEnable() == 1 && StringUtils.isNotBlank(label.getListPic())){
						pc.getProductLabelPicList().add(label.getListPic());
					}
					
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
					pc.setCostPrice(comm.getCostPrice());
					pc.setSmallSellerCode(comm.getSmallSellerCode());
					pc.setSkuPrice(comm.getSkuPrice());
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
						//552一栏三行模板 没库存或下架商品不展示
						if(template_type.equals("449747500011")) {
							
							if(!"4497153900060002".equals(comm.getProduct_status())) {
								continue;
							}
							long actualStock = new PlusSupportStock().upAllStock(comm.getCommodity_number());
							if(actualStock <= 0) {
								continue;
							}
						}
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
					BigDecimal skuPrice = comm.getSkuPrice();
					if(skuPrice.compareTo(BigDecimal.ZERO) < 0) {
						skuPrice = BigDecimal.ZERO;
					}
					if(skuPrice.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = price.divide(skuPrice, 2, 4);
						pc.setPcDiscount(String.valueOf(discount.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品折扣
					} else {
						pc.setPcDiscount("0");//商品折扣
					}
					pc.setMarketPrice(toString(comm.getMarket_price()));//商品市场价
					pc.setPcPrice(String.valueOf(price));//商品售价
					pc.setPcStatus(comm.getProduct_status());//商品状态
					pc.setWidth(comm.getWidth());//宽度
					
					if( template_type.equals("449747500014") || template_type.equals("449747500015") || template_type.equals("449747500016")) {
						pc.setTitle(comm.getTitle());
						pc.setTitle_color(comm.getTitle_color());
						pc.setDes_content(comm.getDescribes());
						pc.setDes_content_color(comm.getDescribe_color());
						
					}
					
					// 商品标签列表图片
					PlusModelProductLabel label = new ProductLabelService().getLabelInfo(product_code);
					if(label != null && label.getFlagEnable() == 1 && StringUtils.isNotBlank(label.getListPic())){
						pc.getProductLabelPicList().add(label.getListPic());
					}
					pc.setLabelsInfo(new ProductLabelService().getLabelInfoList(pc.getProductCode()));
					
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
					if(template_type.equals("449747500013")) {
						pc.setTitle_color(comm.getTitle_color());
						pc.setTitle_checked_color(comm.getTitle_checked_color());
					}
					pcList.add(pc);
				}
				
			} else if(template_type.equals("449747500018") ) {//页面定位模板
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					pc.setTitle(comm.getTitle());//标签内容
					pc.setTitle_color(comm.getTitle_color());
					pc.setTitle_checked_color(comm.getTitle_checked_color());
					
					pc.setRel_template_number(comm.getSub_template_number());//关联模板类型
					
					if(StringUtils.isNotBlank(comm.getSub_template_number())) {
						
						//递归调用本方法一次。（定位模板会关联模板）
						List<PageTemplete> parseTemplateInfo = this.parseTemplateInfo( comm.getRel_templete() , picUrlArr, pcUrlArr,userMap, sys_time ,sourceCode, memberCode, channelId );
						
						if(parseTemplateInfo.size() > 0) {
							for (int i = 0; i < parseTemplateInfo.size(); i++) {//关联模板
								List<TempleteProduct> pcList1 = parseTemplateInfo.get(i).getPcList();
								String type = parseTemplateInfo.get(i).getTempleteType();
								for (TempleteProduct pc1 : pcList1) {
									if("449747500003".equals(type)||"449747500010".equals(type)||"449747500011".equals(type)){
										pc1.setPcImg(pService.getPicInfoOprBig(400, pc1.getPcImg()).getPicNewUrl());
										pc1.setCategoryImg(pService.getPicInfoOprBig(400, pc1.getCategoryImg()).getPicNewUrl());
									}
									if("449747500002".equals(type)||"449747500006".equals(type)||"449747500014".equals(type)){
										pc1.setPcImg(pService.getPicInfoOprBig(570, pc1.getPcImg()).getPicNewUrl());
										pc1.setCategoryImg(pService.getPicInfoOprBig(570, pc1.getCategoryImg()).getPicNewUrl());
									}
								}
								
							}
							pc.setRel_templete(parseTemplateInfo);
						}
						
					}

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
			else if(template_type.equals("449747500019") ) {//倒计时模板 关联配置的时间共用关联商品model，提高复用性
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
//					if(compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
//						continue;
//					}
					TempleteProduct pc = new TempleteProduct();
					pc.setPcPosition(comm.getCommodity_location());
					pc.setSmallSellerCode(comm.getSmallSellerCode());
					pc.setImg(comm.getImg());
					pc.setTargetTime(comm.getEnd_time());
					pc.setTitle(comm.getTitle());
					pc.setTitle_color(comm.getTitle_color());
					pcList.add(pc);
					
				}	
			} else if(template_type.equals("449747500020") ) {// 优惠券积分兑换模版
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					pc.setPcPosition(comm.getCommodity_location());
					String couponTypeCode = comm.getCoupon();
					
					if(StringUtils.isBlank(couponTypeCode)) {
						continue;
					}
					
					PlusModelCouponType plusModelCouponType = loadCouponType.upInfoByCode(new PlusModelQuery(couponTypeCode));
					// 排除未发布的类型编码
					if(!"4497469400030002".equals(plusModelCouponType.getStatus())) {
						continue;
					}
					
					// 排查未发布的活动
					PlusModelCouponActivity plusModelCouponActivity = loadCouponActivity.upInfoByCode(new PlusModelQuery(plusModelCouponType.getActivityCode()));
					if(plusModelCouponActivity.getFlag() != 1) {
						continue;
					}
					
					TemplateCouponType couponType = new TemplateCouponType();
					pc.setCouponType(couponType);
					
					couponType.setUid(plusModelCouponType.getUid());
					couponType.setCouponTypeCode(plusModelCouponType.getCouponTypeCode());
					couponType.setCouponTypeName(plusModelCouponType.getCouponTypeName());
					couponType.setSurplusMoney(plusModelCouponType.getSurplusMoney().toString());
					couponType.setStartTime(plusModelCouponType.getStartTime());
					couponType.setEndTime(plusModelCouponType.getEndTime());
					couponType.setMoneyType(plusModelCouponType.getMoneyType());
					couponType.setMoney(plusModelCouponType.getMoney().toString());
					couponType.setLimitMoney(plusModelCouponType.getLimitMoney().toString());
					couponType.setLimitScope(plusModelCouponType.getLimitScope());
					couponType.setExchangeType(plusModelCouponType.getExchangeType());
					couponType.setExchangeValue(plusModelCouponType.getExchangeValue());
					couponType.setValidType(plusModelCouponType.getValidType());
					couponType.setValidDay(plusModelCouponType.getValidDay());
					couponType.setActionType(plusModelCouponType.getActionType());
					couponType.setActionValue(plusModelCouponType.getActionValue());
					
					if(StringUtils.isNotBlank(memberCode)) {
						if(plusModelCouponType.getSurplusMoney().compareTo(BigDecimal.ZERO) <= 0) {
							// 检查剩余金额是否已经兑换光了
							couponType.setExchangeStatus(TemplateCouponType.EXCHANGE_STATUS_NONE);
						} else if(DbUp.upTable("oc_coupon_info").count("member_code", memberCode, "coupon_type_code", plusModelCouponType.getCouponTypeCode()) > 0) {
							// 检查是否已经兑换过了
							couponType.setExchangeStatus(TemplateCouponType.EXCHANGE_STATUS_YES);
						}
					}
					
					pcList.add(pc);
				}
			}else if(template_type.equals("449747500022")) {
				template.setTempletePic(temp.getCommodity_picture());//模版图
				// 兑换码兑换模板
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					pc.setPcPosition(comm.getCommodity_location());
					pc.setPcImg(comm.getMain_url());
					pc.setProductCode(comm.getProduct_code());//商品编号
					pc.setPcNum(comm.getCommodity_number());//SKU编号
					pc.setPcName(comm.getCommodity_name());
					pc.setPcPrice(comm.getPrice().toString());
					pc.setSalesNum(comm.getSale_num()+"");
					pc.setPcStatus(comm.getProduct_status());
					pcList.add(pc);
				}
			}else if(template_type.equals("449747500021") ) {//定位横滑模板
				
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					pc.setTitle(comm.getTitle());//标签内容
					pc.setTitle_color(comm.getTitle_color());
					pc.setTitle_checked_color(comm.getTitle_checked_color());
					
					pc.setRel_template_number(comm.getSub_template_number());//关联模板类型
					
					if(StringUtils.isNotBlank(comm.getSub_template_number())) {
						
						//递归调用本方法一次。（定位模板会关联模板）
						List<PageTemplete> parseTemplateInfo = this.parseTemplateInfo( comm.getRel_templete() , picUrlArr, pcUrlArr,userMap, sys_time ,sourceCode, memberCode, channelId );
						
						if(parseTemplateInfo.size() > 0) {
							pc.setRel_templete(parseTemplateInfo);
						}
						
					}

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
				
			}else if(template_type.equals("449747500023")||template_type.equals("449747500026")) {//拼团模板
				List<String> paramList = new ArrayList<String>();
				String eventCode = temp.getEvent_code();//拼团活动编号
				List<WebCommodity> commodList = temp.getCommodList();
				if(StringUtils.isNotBlank(eventCode)) {
					paramList.add("'"+eventCode+"'");
				}else if(commodList.size()>0){
					for (int i=0;i< commodList.size();i++) {
						paramList.add("'"+commodList.get(i).getEvent_code()+"'");
					}
				}
				if(paramList.size()==0) {
					continue;
				}
				String join ="(" +StringUtils.join(paramList, ",")+")";

				PlusSupportEvent plusEvent = new PlusSupportEvent();
				PlusSupportStock plusStock = new PlusSupportStock();
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				
				// 修复拼团取不到小程序渠道价格问题
				if(StringUtils.isNotBlank(channelId)) {
					skuQuery.setChannelId(channelId);
				}
				
				LoadEventItemProduct itemProduct = new LoadEventItemProduct();
				PlusModelEventItemQuery itemQuery = new PlusModelEventItemQuery();
				String sellerCode = getManageCode();
				String sql = "select p.sku_code, p.product_code, p.seat, p.favorable_price, p.item_code, i.begin_time, i.end_time, p.sales_advertisement, t.product_name, t.min_sell_price, t.market_price, t.mainpic_url, "
						+ "p.cover_img, t.small_seller_code, i.event_code, i.collage_person_count, t.product_status,i.collage_type "
						+ "from sc_event_item_product p, sc_event_info i, productcenter.pc_productinfo t where p.event_code = i.event_code and i.event_code in "+join+" and (i.event_status='4497472700020002' or i.event_status='4497472700020004') "
						+ "and i.event_type_code = '4497472600010024' and t.seller_code = :sellerCode and i.begin_time <= sysdate() and i.end_time >= sysdate() and p.flag_enable = 1 "
						+ "and t.product_code = p.product_code and t.product_status = '4497153900060002' group by p.product_code order by i.end_time asc, p.seat asc, p.zid desc ";
				List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode", sellerCode));
				for(Map<String, Object> map : list) {
					MDataMap stockParams = new MDataMap();
					TempleteProduct pc = new TempleteProduct();
					boolean showFlag = false;
					
					stockParams.put("event_code", MapUtils.getString(map, "event_code", ""));
					stockParams.put("product_code", MapUtils.getString(map, "product_code", ""));
					List<Map<String, Object>> itemList = DbUp.upTable("sc_event_item_product").dataSqlList("select p.item_code, p.sku_code,p.sales_num,p.rate_of_progress from sc_event_item_product p where p.event_code = :event_code and p.flag_enable = 1 and "
							+ "p.product_code = :product_code", stockParams);
					
					long limitStock=0;
					long actualStock = 0;
					int allSaleNum = 0;
					int num = 0;
					int allProgress = 0;
					for(Map<String, Object> item : itemList) {
						 long sublimitStock = plusEvent.upEventItemSkuStock(MapUtils.getString(item, "item_code", ""));
						 long subactualStock = plusStock.upAllStock(MapUtils.getString(item, "sku_code", ""));
						 allProgress = allProgress +Integer.parseInt((item.get("rate_of_progress")==null||StringUtils.isBlank(item.get("rate_of_progress").toString()))?"0":item.get("rate_of_progress").toString());
						 num=num+1;
						 allSaleNum=allSaleNum + Integer.parseInt(item.get("sales_num").toString());
						if(sublimitStock > 0 && subactualStock > 0) {
							limitStock=limitStock+sublimitStock;
							actualStock=actualStock+subactualStock;
							showFlag = true;
						}
					}
					if(!showFlag) {//库存小于0不让前端显示
						continue;
					}
					
					pc.setProductCode(MapUtils.getString(map, "product_code", ""));//商品编号
					pc.setPcName(MapUtils.getString(map, "product_name", ""));
					pc.setPcDesc(MapUtils.getString(map, "sales_advertisement", ""));
					pc.setCollageType(MapUtils.getString(map, "collage_type", ""));
					skuQuery.setCode(MapUtils.getString(map, "product_code", ""));
					Map<String, PlusModelSkuInfo> priceMap = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
					PlusModelSkuInfo skuInfo = priceMap.get(MapUtils.getString(map, "product_code", ""));
					pc.setPcPrice(MoneyHelper.format(skuInfo.getGroupBuyingPrice()));
					pc.setMarketPrice(MoneyHelper.format(skuInfo.getSkuPrice()));
					//判断是否是拼团大图，如果是，主图切换成广告图
					if(template_type.equals("449747500026")) {
						pc.setPcImg(skuInfo.getDescriptionUrlHref());//商品图放广告图
					}else {
						pc.setPcImg(MapUtils.getString(map, "mainpic_url", ""));
					}
					
					pc.setPcStatus(MapUtils.getString(map, "product_status", ""));
					
					//获取是否抢光
					itemQuery.setCode(MapUtils.getString(map, "item_code", ""));
					PlusModelEventItemProduct eventItemProduct = itemProduct.upInfoByCode(itemQuery);
					long saleStock = eventItemProduct.getSalesStock();
					if(saleStock <= 0) {
						continue;
					}
					
					//获取几人团
					String collagePersonCount = MapUtils.getString(map, "collage_person_count", "0");
					pc.getTagList().add(collagePersonCount + "人团");
					//562 添加活动进行的进度
					long minStore = Math.min(limitStock, actualStock);
					int saleNumbers = allSaleNum -Integer.valueOf(minStore+"");
					int averageProgress = (num==0?100:(allProgress/num));
					int rateOfProgress =(allSaleNum==0)?100:((averageProgress!=100?((saleNumbers/allSaleNum)*(100-averageProgress)+averageProgress):100));
					rateOfProgress= rateOfProgress>100?100:rateOfProgress;	
					pc.setRateOfProgress(rateOfProgress+"");
					OrderService os  = new OrderService();
					pc.setSellNum(os.getProductOrderNum(skuInfo.getSmallSellerCode(), skuInfo.getProductCode(), "4497153900010005"));
					pc.setSaveValue((MoneyHelper.roundUp(skuInfo.getSkuPrice().subtract(skuInfo.getGroupBuyingPrice()))).intValue());
					pcList.add(pc);
				}
			} else if(template_type.equals("449747500024")) {//悬浮模板
				for (WebCommodity comm : temp.getCommodList()) {
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					pc.setCategoryImg(comm.getPrograma_picture());
					listAddEle (picUrlArr , pc.getCategoryImg());
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
						
					pcList.add(pc);
				}
			} else if(template_type.equals("449747500025")) {//精编商品模板
				for (WebCommodity comm : temp.getCommodList()) {
					
					//判断时间是否有效
					if(compare_date(sys_time, comm.getStart_time()) < 0 || compare_date(sys_time, comm.getEnd_time()) >= 0 ) {
						continue;
					}
					
					TempleteProduct pc = new TempleteProduct();
					 //5.4.2 查询商品下架记录表中是否有下架记录
					int count = DbUp.upTable("pc_productinfo").count("product_code",comm.getProduct_code(),"product_status","4497153900060002");
					
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
					pc.setPcPosition(comm.getCommodity_location());//商品展示位置
					pc.setCostPrice(comm.getCostPrice());
					pc.setSmallSellerCode(comm.getSmallSellerCode());
					pc.setSkuPrice(comm.getSkuPrice());
					
					//取价格 如果存在活动价 则原售价展示为划线价
					BigDecimal price = new BigDecimal(Integer.MAX_VALUE);
					BigDecimal market_price = new BigDecimal(Integer.MAX_VALUE);
					
					PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
					plusModelSkuQuery.setCode(pc.getProductCode());//商品编号
					Map<String, PlusModelSkuInfo> priceMap = new ProductPriceService().getProductMinPriceIncloudGroupPrice(plusModelSkuQuery);
					PlusModelSkuInfo skuInfo = priceMap.get(pc.getProductCode());
					if(price.compareTo(skuInfo.getGroupBuyingPrice()) >= 0) {
						price = skuInfo.getGroupBuyingPrice();
						market_price = skuInfo.getSkuPrice();
					}
					
					comm.setPrice(price);
					comm.setSkuPrice(market_price);
					
					if(market_price.compareTo(BigDecimal.ZERO) <= 0) {
						market_price = BigDecimal.ZERO;
					}
					if(market_price.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = price.divide(market_price, 2, BigDecimal.ROUND_HALF_UP);
						pc.setPcDiscount(toString(discount));//商品折扣
					} else {
						pc.setPcDiscount("0");//商品折扣
					}
					pc.setMarketPrice(toString(market_price));//商品市场价
					pc.setPcPrice(toString(price));//商品售价
					//5.4.2
					pc.setSalesNum(count>0?toString(comm.getSale_num()):"0");
					
					if(template_type.equals("449747500017")) {
						pc.setPreferential_desc(comm.getPreferential_desc());
					}
					
					// 商品标签列表图片
					PlusModelProductLabel label = new ProductLabelService().getLabelInfo(pc.getProductCode());
					if(label != null && label.getFlagEnable() == 1 && StringUtils.isNotBlank(label.getListPic())){
						pc.getProductLabelPicList().add(label.getListPic());
					}
					
					pcList.add(pc);
					
				}
			}      

			template.setPcList(pcList);
			
			tempList.add(template);//添加模板
		}
		
		return tempList;
	}
	
	private void replaceImage (List<PageTemplete> tempList,Map<String, String> templeteAllImage) {
		if(null != tempList) {
			for (int i = 0; i < tempList.size(); i++) {
				PageTemplete pageTemplete = tempList.get(i);
				pageTemplete.setPcBuyTypeImg(getValueByKey(templeteAllImage, pageTemplete.getPcBuyTypeImg()));
				pageTemplete.setPcTxtBackPic(getValueByKey(templeteAllImage, pageTemplete.getPcTxtBackPic()));
				pageTemplete.setTempletePic(getValueByKey(templeteAllImage, pageTemplete.getTempletePic()));
				List<TempleteProduct> pcList = pageTemplete.getPcList();
				for (TempleteProduct pc : pcList) {
					pc.setCategoryImg(getValueByKey(templeteAllImage , pc.getCategoryImg()));
					pc.setImg(getValueByKey(templeteAllImage, pc.getImg()));
					pc.setPcImg(getValueByKey(templeteAllImage, pc.getPcImg()));//商品图片
				}
			}
		}
	}
	
	
	private void refreshVipLevelActivity(String mobile,List<PageTemplete> tempList,String sys_time,String channelId) {
		/**
		 * 判断会员是否有会有折扣相关活动
		 */
		boolean isHasActivity = false;
		boolean isNeigou = false;
		boolean isVipDicount = false;
		String memberCode = "";
		String psps = "";
		
		// 取用户编号
		if(StringUtils.isNotBlank(mobile)) {
			MDataMap one = DbUp.upTable("mc_login_info").one("login_name",mobile,"manage_code","SI2003");
			if(one != null) {
				memberCode = one.get("member_code");
			}
		}
		
		// 判断是否有内购活动
		if(StringUtils.isNotBlank(memberCode)) {
			psps = new PlusServicePurchase().getPurchase("SI2003", memberCode,channelId);
			if(StringUtils.isNotBlank(psps)){
				isHasActivity = true;
				isNeigou = true;
			}
		}
		
		//判断是否有效的会员折扣活动
		if(!isHasActivity){
			PlusModelSaleQuery discountQuery = new PlusModelSaleQuery();
			discountQuery.setCode("SI2003");
			List<PlusModelVipDiscount> discountList = new LoadEventVipDiscount().upInfoByCode(discountQuery ).getListVipDiscount();
			
//			PlusModelMemberLevel levelInfo = null;
			for (PlusModelVipDiscount plusModelVipDiscount : discountList) {
				if(PlusSupportEvent.compareDate(sys_time,plusModelVipDiscount.getEndTime())<=0 && PlusSupportEvent.compareDate(plusModelVipDiscount.getBeginTime(),sys_time)<=0) {
//					if(levelInfo == null){
//						levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
//					}
//					
//					// 获取客户等级失败时忽略会员日活动
//					if(levelInfo == null){
//						break;
//					}
//					
//					// 客户等级是顾客时不刷新价格，因为默认就是顾客的等级
//					if(XmasSystemConst.CUST_LVL_CD.equals(levelInfo.getLevel())){
//						break;
//					}
					
					isHasActivity = true;
					isVipDicount = true;
					break;
				}
			}
		}
		
		// 有活动时，刷新一下商品价格
		if(isHasActivity) {
			List<TempleteProduct> pcList  = new ArrayList<TempleteProduct>();
			// 更换价格
			for (PageTemplete pageTemplete : tempList) {
				String templeteType = pageTemplete.getTempleteType();
				if(templeteType.equals("449747500002") || 
					   templeteType.equals("449747500003") || 
					   templeteType.equals("449747500004") ||
					   templeteType.equals("449747500009") ||
					   templeteType.equals("449747500010") ||
					   templeteType.equals("449747500011") ||
					   templeteType.equals("449747500014") ||
					   templeteType.equals("449747500015") ||
					   templeteType.equals("449747500016") ){
					
					pcList.addAll(pageTemplete.getPcList());
				}
				
				// 页面定位模版取里面的内容
				if(templeteType.equals("449747500018")) {
					for(TempleteProduct tp : pageTemplete.getPcList()){
						List<PageTemplete> pList = tp.getRel_templete();
						if(pList == null || pList.isEmpty()) continue;
						
						for(PageTemplete p : pList){
							templeteType = p.getTempleteType();
							if(templeteType.equals("449747500002") || 
								   templeteType.equals("449747500003") || 
								   templeteType.equals("449747500004") ||
								   templeteType.equals("449747500009") ||
								   templeteType.equals("449747500010") ||
								   templeteType.equals("449747500011") ||
								   templeteType.equals("449747500014") ||
								   templeteType.equals("449747500015") ||
								   templeteType.equals("449747500016") ){
								pcList.addAll(p.getPcList());
							}
						}
					}
				}
			}
			
			DecimalFormat df = new DecimalFormat("#.##");
			BigDecimal ngPrice = new BigDecimal(23); // 默认内购金额
			String eventCode = "";
			PlusModelEventExclude excludeProduct = new PlusModelEventExclude();
			
			if(isNeigou && psps.split(",").length == 3){ 
				// 如果存在内购活动则取活动设置的内购金额
				ngPrice = new BigDecimal(psps.split(",")[0]);
				eventCode = psps.split(",")[1];
				excludeProduct = new LoadEventExcludeProduct().upInfoByCode(new PlusModelQuery(eventCode));
			}
			
			PlusModelSkuInfo skuInfo = null;
			List<String> productCodeList = new ArrayList<String>();
			
			// 内购特殊处理
			for (TempleteProduct templeteProduct : pcList) {
				// 忽略活动排除的商品
				if(excludeProduct.getProductCodeList().contains(templeteProduct.getProductCode())) {
					continue;
				}
				
				productCodeList.add(templeteProduct.getProductCode());
				
				if(isNeigou && "SI2003".equalsIgnoreCase(templeteProduct.getSmallSellerCode()) && templeteProduct.getCostPrice().compareTo(BigDecimal.ZERO) > 0){ // 内购直接取商品成本加23
					skuInfo = new PlusModelSkuInfo();
					skuInfo.setSellPrice(templeteProduct.getCostPrice().add(ngPrice).setScale(0, BigDecimal.ROUND_HALF_UP));
					
					// 活动价高于商品原价时取商品原价
					if(templeteProduct.getSkuPrice().compareTo(BigDecimal.ZERO)  > 0 
							&& skuInfo.getSellPrice().compareTo(templeteProduct.getSkuPrice()) > 0
							){
						skuInfo.setSellPrice(templeteProduct.getSkuPrice());
					}
					
					templeteProduct.setPcPrice(df.format(skuInfo.getSellPrice()));//设置价格
					BigDecimal skuPrice = templeteProduct.getSkuPrice();
					//判断是否展示折扣，如果展示折扣，则刷新折扣信息
					if("449746250001".equals(templeteProduct.getIsShowDiscount()) && skuPrice.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = skuInfo.getSellPrice().divide(skuPrice, 2, 4);
						templeteProduct.setPcDiscount(String.valueOf(discount.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品折扣
						templeteProduct.setButtonType("1");//重置下按钮类型 改为马上抢购
					}
				}
			}
			
			// 会员日活动特殊处理
			if(isVipDicount && !productCodeList.isEmpty()){
				Map<String, PlusModelVipDiscount> vipDisMap = new PlusServiceVipDiscount().getVipDiscountActivity("SI2003", memberCode, StringUtils.join(productCodeList,","),channelId);
				PlusModelVipDiscount vipDiscount = null;
				
				for (TempleteProduct templeteProduct : pcList) {
					// 会员日仅支持LD商品
					if(!"SI2003".equalsIgnoreCase(templeteProduct.getSmallSellerCode())) continue;
					
					vipDiscount = vipDisMap.get(templeteProduct.getProductCode());
					if(vipDiscount == null){ // 非会员日支持的活动再获取一次价格把默认的会员日折扣价覆盖
						skuInfo = cache.get(templeteProduct.getPcNum());
						
						if(skuInfo == null){// 暂时加个5分钟缓存
							skuInfo = plusSupportProduct.upSkuInfoBySkuCode(templeteProduct.getPcNum(), memberCode, memberCode, 0,channelId);
							cache.put(templeteProduct.getPcNum(), skuInfo);
						}
						
						if(skuInfo == null) continue;
					}else{
						BigDecimal discont = BigDecimal.valueOf(vipDiscount.getDisount()).divide(BigDecimal.valueOf(100));
						skuInfo = new PlusModelSkuInfo();
						BigDecimal computePrice = discont.multiply(templeteProduct.getSkuPrice()).setScale(0, BigDecimal.ROUND_HALF_UP);
						BigDecimal maxSaleMoney = vipDiscount.getMaxDiscountMoney();
						if(maxSaleMoney.compareTo(BigDecimal.ZERO) > 0 && templeteProduct.getSkuPrice().subtract(computePrice).compareTo(maxSaleMoney) > 0) {
							computePrice = templeteProduct.getSkuPrice().subtract(maxSaleMoney);
						}
						skuInfo.setSellPrice(computePrice);
					}
					
					templeteProduct.setPcPrice(df.format(skuInfo.getSellPrice()));//设置价格
					BigDecimal skuPrice = templeteProduct.getSkuPrice();
					//判断是否展示折扣，如果展示折扣，则刷新折扣信息
					if("449746250001".equals(templeteProduct.getIsShowDiscount()) && skuPrice.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal discount = skuInfo.getSellPrice().divide(skuPrice, 2, 4);
						templeteProduct.setPcDiscount(String.valueOf(discount.setScale(2, BigDecimal.ROUND_HALF_UP)));//商品折扣
					}
				}
			}
		}
		
	}
	
	private static SimpleCache cache = new SimpleCache(new SimpleCache.Config(300,300,"zt-cfamily",false)); 
}
