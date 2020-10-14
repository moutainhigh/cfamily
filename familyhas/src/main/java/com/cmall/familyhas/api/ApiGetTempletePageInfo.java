package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetTempletePageInfoInput;
import com.cmall.familyhas.api.model.PageTemplete;
import com.cmall.familyhas.api.model.TempleteProduct;
import com.cmall.familyhas.api.result.ApiGetTempletePageInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetTempletePageInfo extends RootApiForManage<ApiGetTempletePageInfoResult,ApiGetTempletePageInfoInput>{

	public ApiGetTempletePageInfoResult Process(
			ApiGetTempletePageInfoInput inputParam, MDataMap mRequestMap) {
		
		ApiGetTempletePageInfoResult result = new ApiGetTempletePageInfoResult();
		
		
		String pageNum = inputParam.getPageNum();
		
		Timestamp sys_time = DateUtil.getSysDateTimestamp();
		result.setSysTime(DateUtil.getSysDateTimeString());
		Map<String, Object> dataSqlOne = DbUp.upTable("fh_data_page").dataSqlOne(
				"SELECT * FROM familyhas.fh_data_page WHERE dal_status != '1000' AND page_number = '"+pageNum+"'",
				null);
		
		if(null != dataSqlOne) {
			String dropdown_type = String.valueOf(dataSqlOne.get("dropdown_type"));//页面类型
			String input_text = String.valueOf(dataSqlOne.get("title"));//页面标题
			result.setPageNum(pageNum);
			result.setPageTitle(input_text);
			result.setPageType(dropdown_type);
			result.setProject_ad(String.valueOf(dataSqlOne.get("project_ad")));
			if("449747870002".equals(String.valueOf(dataSqlOne.get("is_rel_onlive")))) {
				result.setRelLive(true);//关联直播
				result.setLive_job_end_time(bConfig("familyhas.liveJobEndTime"));
				result.setLive_job_start_time(bConfig("familyhas.livejobStartTime"));
			}
			/*
			 * 添加专题模板的web的app分享信息
			 */
			String app_is_share = String.valueOf(dataSqlOne.get("app_is_share"));
			if("449747860002".equals(app_is_share)) {
				result.setShare(true);
				result.setShareImg(String.valueOf(dataSqlOne.get("share_img")));
				result.setShareContent(String.valueOf(dataSqlOne.get("share_content")));
				result.setShareTitle(String.valueOf(dataSqlOne.get("share_title")));
				
				String share_link = String.valueOf(dataSqlOne.get("share_link"));
				if(StringUtils.isBlank(share_link)) {
					result.setShareLink(bConfig("familyhas.webTemplateDefaultShareLink")+pageNum);
				} else {
					result.setShareLink(share_link);
				}
			}
			
			Map<String, String> userMap = new HashMap<String, String>();//用户mobile和membercode    key为：mobile；value：membercode；
			List<PageTemplete> tempList = new ArrayList<PageTemplete>();
			MDataMap param = new MDataMap();
			String sSql = "SELECT p_t.template_name,t.commodity_buy_picture,t.commodity_min_dis,t.commodity_picture,t.commodity_text_color,t.commodity_text_pic," +
					"t.commodity_text_picture,t.commodity_text_value,t.template_backcolor,t.template_number,t.template_type,t.template_title_name" +
					" FROM familyhas.fh_page_template p_t JOIN familyhas.fh_data_template t ON p_t.template_number = t.template_number AND p_t.page_number = :page_number AND p_t.dal_status !='1000' AND t.dal_status !='1000' ORDER BY cast(p_t.sort as signed) ";
			param.put("page_number", pageNum);
			//查询所有对应的模板
			List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_data_template").dataSqlList(sSql, param);
			for (Map<String, Object> map : dataSqlList) {
				
				PageTemplete template = new PageTemplete();
				String template_number = toString(map.get("template_number"));
				String template_type = toString(map.get("template_type"));
				map.get("template_name");
				
				/*
				 * 判断模版类型
				 */
				template.setTempleteBackColor(toString(map.get("template_backcolor")));
				
				template.setIsShowDownDiscount(toString(map.get("commodity_min_dis")));
				template.setPcBuyTypeImg(toString(map.get("commodity_buy_picture")));
				template.setPcTxtBackPic(toString(map.get("commodity_text_pic")));
				template.setPcTxtBackType(toString(map.get("commodity_text_picture")));
				template.setPcTxtBackVal(toString(map.get("commodity_text_value")));
				template.setPcTxtColorType(toString(map.get("commodity_text_color")));
				template.setTempleteBackColor(toString(map.get("template_backcolor")));
				template.setTempleteType(template_type);
				template.setTempleteNum(template_number);
				
				//查询模板对应的内容信息
				
				String sql2 = "";
				
				List<TempleteProduct> pcList = new ArrayList<TempleteProduct>();
				//判断模版类型
				if(template_type.equals("449747500001") || template_type.equals("449747500006")) {//轮播模板 || 两栏广告
//					List<MDataMap> queryAll = DbUp.upTable("fh_data_commodity").queryAll("template_number,commodity_location,programa_picture,skip_input,commodity_number" +
//							",programa_picture,skip,skip_input", " commodity_location ", " template_number = '"+template_number+"' AND dal_status != '1000'  AND start_time <= '"+sys_time+"' AND end_time >= '"+sys_time+"'", new MDataMap());
					
					sql2 = "SELECT c.template_number,c.commodity_location,c.programa_picture,c.skip_input,c.commodity_number,c.programa_picture,c.skip,c.live_mobile," +
							"sku.sku_code,pc.product_code FROM " +
							"( " +
							" SELECT * FROM familyhas.fh_data_commodity WHERE template_number = :template_number AND dal_status != '1000' AND start_time <= :curntTime AND end_time >= :curntTime" +
							") c " +
							"LEFT JOIN productcenter.pc_skuinfo sku " +
							"ON c.commodity_number = sku.sku_code " +
							"LEFT JOIN productcenter.pc_productinfo pc " +
							"ON sku.product_code = pc.product_code   ORDER BY cast(c.commodity_location as signed) ";
					MDataMap paramMap = new MDataMap();
					paramMap.put("template_number", template_number);
					paramMap.put("curntTime", sys_time.toString());
					
					List<Map<String, Object>> queryAll = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, paramMap);
					for (Map<String, Object> mDataMap : queryAll) {
						TempleteProduct pc = new TempleteProduct();
						String commodity_location = toString(mDataMap.get("commodity_location"));
						String programa_picture = toString(mDataMap.get("programa_picture"));
						String skip = toString(mDataMap.get("skip"));
						String skip_input = toString(mDataMap.get("skip_input"));
						String commodity_number = toString(mDataMap.get("commodity_number"));
						String product_code = toString(mDataMap.get("product_code"));
						pc.setCategoryImg(programa_picture);
						pc.setPcPosition(commodity_location);
						if(template_type.equals("449747500006")) {
							if(commodity_number.length() > 0) {
								
								//获取库存
								/*PlusModelSkuInfo skuSuppore = new PlusSupportProduct().upSkuInfoBySkuCode(commodity_number, "");
								pc.setOpenTypeVal(skuSuppore.getProductCode());
								pc.setProductCode(skuSuppore.getProductCode());
								if(StringUtils.isNotEmpty(skuSuppore.getEventCode())) {
									pc.setSalesNum(String.valueOf(skuSuppore.getLimitStock()));//商品库存
								} else {
									int upAllStock = new PlusSupportStock().upAllStock(commodity_number);
									pc.setSalesNum(String.valueOf(upAllStock));//商品库存
								}*/
								pc.setOpenTypeVal(product_code);
								pc.setProductCode(product_code);
								//更改获取库存方法 (一个商品单个sku，则取sku的库存，否则去商品库存)
								PlusModelProductQuery plus = new PlusModelProductQuery(product_code);
								PlusSupportProduct psp = new PlusSupportProduct();
								PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
								List<PlusModelProductSkuInfo> skuList = upInfoByCode.getSkuList();
								long salesNum = 0;
								for (int i = 0; i < skuList.size(); i++) {
									PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(skuList.get(i).getSkuCode());
									salesNum += upSkuInfoBySkuCode.getLimitStock();
								}
								
								pc.setSalesNum(toString(salesNum));
								
							}
							
						} else {
							pc.setOpenType(skip);
							if(skip.equals("449747550004")) {
								String[] arr = skip_input.split("->");
								pc.setOpenTypeVal(arr[arr.length-1]);
							} else if(skip.equals("449747550002")){
								/**
								 * 兼容   之前已经将sku编号转换成商品编号   的版本
								 */
								String tempPcNum = commodity_number;
								String PcNumPre = tempPcNum.substring(0, 4);
								if(PcNumPre.equals("8019")) {
									//获取库存
									/*PlusModelSkuInfo skuSuppore = new PlusSupportProduct().upSkuInfoBySkuCode(commodity_number, "");
									pc.setOpenTypeVal(skuSuppore.getProductCode());*/
									pc.setProductCode(product_code);
									pc.setOpenTypeVal(product_code);
								} else {
									//兼容之前添加的是商品编号而非sku 编号问题
									pc.setProductCode(commodity_number);
									pc.setOpenTypeVal(commodity_number);
								}
							} else if(skip.equals("449747550005")){
								if(StringUtils.isNotBlank(toString(mDataMap.get("live_mobile")))) {
									
									userMap.put(toString(mDataMap.get("live_mobile")), "");
								}
								pc.setOpenTypeVal(toString(mDataMap.get("live_mobile")));
							} else {
								pc.setOpenTypeVal(skip_input);
							}
						}
						
						pcList.add(pc);
					}
					
				} else if(template_type.equals("449747500002") || template_type.equals("449747500003")) {//两栏两行模版   || 一栏多行模版
					
					sql2 = "SELECT c.template_number,c.commodity_describe,c.commodity_location,c.commodity_name,c.commodity_number,c.commodity_picture,c.is_dis,c.programa_picture,c.skip," +
							" c.skip_input,pc.product_status,pc.mainpic_url,pc.market_price,pc.product_code" +
							" FROM (SELECT * FROM familyhas.fh_data_commodity WHERE template_number = '"+template_number+"' AND dal_status != '1000' AND start_time <= '"+sys_time+"' AND end_time >= '"+sys_time+"') c  LEFT JOIN productcenter.pc_skuinfo sku " +
							" ON sku.sku_code = c.commodity_number  " +
							" LEFT JOIN productcenter.pc_productinfo pc ON sku.product_code = pc.product_code  ORDER BY cast(c.commodity_location as signed)  ";
					//详细信息列表
					List<Map<String, Object>> dataSqlList2 = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, new MDataMap());
					for (Map<String, Object> map2 : dataSqlList2) {
						
						TempleteProduct pc = new TempleteProduct();
						String commodity_number = toString(map2.get("commodity_number"));
						
						pc.setProductCode(toString(map2.get("product_code")));
						pc.setCategoryImg(toString(map2.get("programa_picture")));//模板一用
						pc.setIsShowDiscount(toString(map2.get("is_dis")));//是否展示折扣
						pc.setOpenType(toString(map2.get("skip")));//打开方式
						pc.setOpenTypeVal(toString(map2.get("skip_input")));//打开方式值
						pc.setPcDesc(toString(map2.get("commodity_describe")));//商品描述
						String commodity_picture = toString(map2.get("commodity_picture"));
						if(commodity_picture.length() > 0) {
							pc.setPcImg(commodity_picture);//商品图片
						} else {//默认展示主图
							pc.setPcImg(String.valueOf(map2.get("mainpic_url")));//商品图片
						}
						pc.setPcName(toString(map2.get("commodity_name")));//商品名称
						pc.setPcNum(commodity_number);//商品编号
						pc.setPcPosition(toString(map2.get("commodity_location")));//商品展示位置
						PlusModelSkuInfo pcModel = new PlusSupportProduct().upSkuInfoBySkuCode(commodity_number, "");
						BigDecimal price = pcModel.getSellPrice();
						String market_price = toString(map2.get("market_price"));
						if(market_price.equals("")) {
							market_price = "0";
						}
						BigDecimal mar_price = BigDecimal.valueOf(Double.valueOf(market_price));
						if(mar_price.compareTo(new BigDecimal(0)) > 0) {
							BigDecimal discount = price.divide(mar_price, 2, 4);
							pc.setPcDiscount(String.valueOf(discount));//商品折扣
						} else {
							pc.setPcDiscount("0");//商品折扣
						}
						pc.setMarketPrice(market_price);//商品市场价
						pc.setPcPrice(String.valueOf(price));//商品售价
						pc.setPcStatus(toString(map2.get("product_status")));//商品状态
						//商品库存		 				
						/*if(StringUtils.isNotEmpty(pcModel.getEventCode())) {
							pc.setSalesNum(String.valueOf(pcModel.getLimitStock()));//商品库存
						} else {
							int upAllStock = new PlusSupportStock().upAllStock(commodity_number);
							pc.setSalesNum(String.valueOf(upAllStock));//商品库存
						}*/
						//更改获取库存方法 (一个商品单个sku，则取sku的库存，否则去商品库存)
						PlusModelProductQuery plus = new PlusModelProductQuery(pc.getProductCode());
						PlusSupportProduct psp = new PlusSupportProduct();
						PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
						List<PlusModelProductSkuInfo> skuList = upInfoByCode.getSkuList();
						long salesNum = 0;
						for (int i = 0; i < skuList.size(); i++) {
							PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(skuList.get(i).getSkuCode());
							salesNum += upSkuInfoBySkuCode.getLimitStock();
						}
						pc.setSalesNum(toString(salesNum));//商品库存
						
						pcList.add(pc);
					}
					
				} else if(template_type.equals("449747500004")) {//视频模板
					template.setTempletePic(toString(map.get("commodity_picture")));//模版图
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
						PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
						
						pc.setCategoryImg(upInfoByCode.getMainpicUrl());//商品主图|商品列表图（方图）
						DecimalFormat df = new DecimalFormat("#.00");
						pc.setMarketPrice(String.valueOf(df.format(upInfoByCode.getMarketPrice())));//市场价
						//获取售价(获取所有sku 的最低价，取最低价格)
						PlusSupportProduct psp = new PlusSupportProduct();
						List<PlusModelProductSkuInfo> skuList = upInfoByCode.getSkuList();
						BigDecimal minSkuSellPrice = new BigDecimal(0);
						if(skuList.size() > 0) {
							BigDecimal[] skuSellPriceArr = new BigDecimal[skuList.size()];
							for (int j = 0; j < skuList.size(); j++) {
								PlusModelSkuInfo price = psp.upSkuInfoBySkuCode(skuList.get(j).getSkuCode());
								skuSellPriceArr[j] = price.getSellPrice();
							}
							Arrays.sort(skuSellPriceArr);
							minSkuSellPrice = skuSellPriceArr[0];
						} else {//不存在sku，则取商品最低价格
							minSkuSellPrice = upInfoByCode.getMinSellPrice();
						}
						pc.setPcPrice(String.valueOf(df.format(minSkuSellPrice)));//售价
						pc.setPcName(upInfoByCode.getProductName());//商品名称
						pc.setProductCode(pcNum);//商品编号
						
						pcList.add(pc);
						
					}
				} else if(template_type.equals("449747500005")) {//标题模板
					template.setTemplate_title_name(toString(map.get("template_title_name")));
					template.setTempletePic(toString(map.get("commodity_picture")));//模版图
				} else if(template_type.equals("449747500007") || template_type.equals("449747500008")) {
					sql2 = "SELECT programa_picture,coupon FROM familyhas.fh_data_commodity " +
							"WHERE template_number = '"+template_number+"' AND dal_status = '1001' AND start_time < '"+sys_time+"' " +
							" AND '"+sys_time+"' < end_time  ORDER BY cast(commodity_location as signed) ";
					List<Map<String, Object>> couponList = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, new MDataMap());
					StringBuffer couponBF = new StringBuffer();
					for (Map<String, Object> map2 : couponList) {
						String coupon = toString(map2.get("coupon"));
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
						for (Map<String, Object> map2 : couponList) {
							TempleteProduct pc = new TempleteProduct();
							pc.setCategoryImg(toString(map2.get("programa_picture")));
							String coupon = toString(map2.get("coupon"));
							pc.setCoupon(coupon);
							String activity_code = couponMap.get(map2.get("coupon"));
							if( null != activity_code) {
								pc.setActivity_code(activity_code);
							} 
							pcList.add(pc);
						}
					}
				}else if(template_type.equals("449747500009")) {//普通视频模板
					//查询视频商品信息
					sql2 = "SELECT c.template_number,c.commodity_describe,c.commodity_location,c.commodity_name,c.commodity_number,c.commodity_picture,c.programa_picture,c.url,c.template_desc," +
							" pc.product_status,pc.mainpic_url,pc.market_price,pc.product_code" +
							" FROM (SELECT * FROM familyhas.fh_data_commodity WHERE template_number = '"+template_number+"' AND dal_status != '1000' AND start_time <= '"+sys_time+"' AND end_time >= '"+sys_time+"') c  LEFT JOIN productcenter.pc_skuinfo sku " +
							" ON sku.sku_code = c.commodity_number  " +
							" LEFT JOIN productcenter.pc_productinfo pc ON sku.product_code = pc.product_code  ORDER BY cast(c.commodity_location as signed)  ";
					//详细列表
					List<Map<String, Object>> splist = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, new MDataMap());
					for(Map<String, Object> sqMap : splist){
						TempleteProduct pc = new TempleteProduct();
						String commodity_number = toString(sqMap.get("commodity_number"));

						pc.setProductCode(toString(sqMap.get("product_code")));
						pc.setCategoryImg(toString(sqMap.get("programa_picture")));//栏目图
						//添加商品之后
						if(commodity_number != null && commodity_number.length() > 0){
							pc.setPcDesc(toString(sqMap.get("commodity_describe")));//商品描述
							String commodity_picture = toString(sqMap.get("commodity_picture"));
							if(commodity_picture.length() > 0) {
								pc.setPcImg(commodity_picture);//商品图片
							} else {//默认展示主图
								pc.setPcImg(String.valueOf(sqMap.get("mainpic_url")));//商品图片
							}
							pc.setPcName(toString(sqMap.get("commodity_name")));//商品名称
							pc.setPcPosition(toString(sqMap.get("commodity_location")));//商品展示位置
							pc.setPcNum(commodity_number);//商品编号
							PlusModelSkuInfo pcModel = new PlusSupportProduct().upSkuInfoBySkuCode(commodity_number, "");
							BigDecimal price = pcModel.getSellPrice();
							String market_price = toString(sqMap.get("market_price"));
							if(market_price.equals("")) {
								market_price = "0";
							}
							BigDecimal mar_price = BigDecimal.valueOf(Double.valueOf(market_price));
							if(mar_price.compareTo(new BigDecimal(0)) > 0) {
								BigDecimal discount = price.divide(mar_price, 2, 4);
								pc.setPcDiscount(String.valueOf(discount));//商品折扣
							} else {
								pc.setPcDiscount("0");//商品折扣
							}
							pc.setMarketPrice(market_price);//商品市场价
							pc.setPcPrice(String.valueOf(price));//商品售价
							pc.setPcStatus(toString(sqMap.get("product_status")));//商品状态
						}
						pc.setUrl(toString(sqMap.get("url")));
						pc.setTemplate_desc(toString(sqMap.get("template_desc")));
						
						pcList.add(pc);
					}
					
					
				}else if(template_type.equals("449747500010") || template_type.equals("449747500011")) {//一行多栏（横滑），一行三栏
					sql2 = "SELECT c.template_number,c.commodity_describe,c.commodity_location,c.commodity_name,c.commodity_number,c.commodity_picture,c.is_dis,c.programa_picture,c.skip,c.live_mobile," +
							" c.skip_input,c.width,pc.product_status,pc.mainpic_url,pc.market_price,pc.product_code" +
							" FROM (SELECT * FROM familyhas.fh_data_commodity WHERE template_number = '"+template_number+"' AND dal_status != '1000' AND start_time <= '"+sys_time+"' AND end_time >= '"+sys_time+"') c  LEFT JOIN productcenter.pc_skuinfo sku " +
							" ON sku.sku_code = c.commodity_number  " +
							" LEFT JOIN productcenter.pc_productinfo pc ON sku.product_code = pc.product_code  ORDER BY cast(c.commodity_location as signed)  ";
					//详细信息列表
					List<Map<String, Object>> hhlist = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, new MDataMap());
					for(Map<String, Object> hhMap : hhlist){
						TempleteProduct pc = new TempleteProduct();
						
						pc.setProductCode(toString(hhMap.get("product_code")));
						pc.setCategoryImg(toString(hhMap.get("programa_picture")));//模板一用
						pc.setIsShowDiscount(toString(hhMap.get("is_dis")));//是否展示折扣
						String skip = toString(hhMap.get("skip"));
						String skip_input = toString(hhMap.get("skip_input"));
						String commodity_number = toString(hhMap.get("commodity_number"));
						String product_code = toString(hhMap.get("product_code"));
						pc.setOpenType(toString(hhMap.get("skip")));//打开方式
						pc.setOpenTypeVal(toString(hhMap.get("skip_input")));//打开方式值
						//判断打开方式
						if(skip.equals("449747550004")) {
							
							String[] arr = skip_input.split("->");
							pc.setOpenTypeVal(arr[arr.length-1]);
							
						} else if(skip.equals("449747550002")){
							pc.setProductCode(product_code);
							pc.setOpenTypeVal(product_code);
							
						} else if(skip.equals("449747550005")){
							if(StringUtils.isNotBlank(toString(hhMap.get("live_mobile")))) {
								
								userMap.put(toString(hhMap.get("live_mobile")), "");
							}
							pc.setOpenTypeVal(toString(hhMap.get("live_mobile")));
						} else {
							pc.setOpenTypeVal(skip_input);
						}
						pc.setPcDesc(toString(hhMap.get("commodity_describe")));//商品描述
						String commodity_picture = toString(hhMap.get("commodity_picture"));
						if(commodity_picture.length() > 0) {
							pc.setPcImg(commodity_picture);//商品图片
						} else {//默认展示主图
							pc.setPcImg(String.valueOf(hhMap.get("mainpic_url")));//商品图片
						}
						pc.setPcName(toString(hhMap.get("commodity_name")));//商品名称
						pc.setPcNum(commodity_number);//商品编号
						pc.setPcPosition(toString(hhMap.get("commodity_location")));//商品展示位置
						PlusModelSkuInfo pcModel = new PlusSupportProduct().upSkuInfoBySkuCode(commodity_number, "");
						BigDecimal price = pcModel.getSellPrice();
						String market_price = toString(hhMap.get("market_price"));
						if(market_price.equals("")) {
							market_price = "0";
						}
						BigDecimal mar_price = BigDecimal.valueOf(Double.valueOf(market_price));
						if(mar_price.compareTo(new BigDecimal(0)) > 0) {
							BigDecimal discount = price.divide(mar_price, 2, 4);
							pc.setPcDiscount(String.valueOf(discount));//商品折扣
						} else {
							pc.setPcDiscount("0");//商品折扣
						}
						pc.setMarketPrice(market_price);//商品市场价
						pc.setPcPrice(String.valueOf(price));//商品售价
						pc.setPcStatus(toString(hhMap.get("product_status")));//商品状态
						pc.setWidth(toString(hhMap.get("width")));//宽度
						
						pcList.add(pc);
					}
					
					
				}else if(template_type.equals("449747500012") || template_type.equals("449747500013")){//分类模版   本地货模版
					//查询视频商品信息
					sql2 = "SELECT c.template_number,c.commodity_location,c.programa_picture,c.url,c.img,c.city_name,c.width " +
							" FROM (SELECT * FROM familyhas.fh_data_commodity WHERE template_number = '"+template_number+"' AND dal_status != '1000' AND start_time <= '"+sys_time+"' AND end_time >= '"+sys_time+"') c  LEFT JOIN productcenter.pc_skuinfo sku " +
							" ON sku.sku_code = c.commodity_number  " +
							" LEFT JOIN productcenter.pc_productinfo pc ON sku.product_code = pc.product_code  ORDER BY cast(c.commodity_location as signed)  ";
					//详细列表
					List<Map<String, Object>> splist = DbUp.upTable("fh_data_commodity").dataSqlList(sql2, new MDataMap());
					for(Map<String, Object> sqMap : splist){
						TempleteProduct pc = new TempleteProduct();

						pc.setCategoryImg(toString(sqMap.get("programa_picture")));//栏目图
						pc.setPcPosition(toString(sqMap.get("commodity_location")));//商品展示位置
						pc.setUrl(toString(sqMap.get("url")));
						pc.setImg(toString(sqMap.get("img")));
						pc.setCity_name(toString(sqMap.get("city_name")));
						pc.setWidth(toString(sqMap.get("width")));
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
			result.setTempList(tempList);
			
		}
		
		return result;
	}
	
	private String toString (Object obj) {
		if(null == obj) {
			obj = "";
		}
		return String.valueOf(obj).trim();
	}
	
	
}
