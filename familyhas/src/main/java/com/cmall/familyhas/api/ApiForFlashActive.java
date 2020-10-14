package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForFlashActiveResult;
import com.cmall.familyhas.api.input.ApiForFlashActiveResult.ActiceProduct;
import com.cmall.familyhas.api.input.ApiForFlashActiveResult.Activity;
import com.cmall.familyhas.api.result.ApiForFlashActiveInput;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.service.FlashsalesService;
import com.cmall.productcenter.service.ProductStoreService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 时间轴 闪购接口
 * @author jlin
 *
 */
public class ApiForFlashActive extends RootApiForManage<ApiForFlashActiveResult, ApiForFlashActiveInput> {

	public ApiForFlashActiveResult Process(ApiForFlashActiveInput inputParam,MDataMap mRequestMap) {
		
		ApiForFlashActiveResult activeResult = new ApiForFlashActiveResult();
		
		String now=DateUtil.getSysDateTimeString();
		String today=now.substring(0, 10);
		String now_last_time=today+" 23:59:59";
		
		//今天的闪购信息
		List<Activity> today_active=activeList(now, now_last_time,getManageCode());
		
		activeResult.getActiveList().addAll(today_active);
		
		//明天的闪购信息
		if(today_active.size()<2){
			
			String tomorrow=DateUtil.toString(DateUtil.addDays(new Date(), 1));
			String tomorrow_last_time=tomorrow+" 23:59:59";
			String tomorrow_start_time=tomorrow+" 00:00:00";
			
			List<Activity> tomorrow_active=activeList(tomorrow_start_time, tomorrow_last_time,getManageCode());
			
			activeResult.getActiveList().addAll(tomorrow_active);
		}
		
		
		activeResult.setSystemTime(FormatHelper.upDateTime());
		
		
		/*
		 * 查询闪购的栏目信息
		 */
		MDataMap cData = DbUp.upTable("fh_category").one("category_code","467703130008000100060001");
		if(cData != null){
			//栏目图片地址
			activeResult.setBanner_img(cData.get("line_head") ==null ? "" : cData.get("line_head"));
			//栏目名称
			activeResult.setBanner_name(cData.get("category_name") ==null ? "" : cData.get("category_name"));
			//判断栏目类型
			if("449747030001".equals(cData.get("link_address"))) {
				activeResult.setBanner_link(cData.get("link_url") ==null ? "" : cData.get("link_url"));
			} else {
				activeResult.setBanner_link(cData.get("product_link") ==null ? "" : cData.get("product_link"));
			}
		}
		
		return activeResult;
	}

	
	private List<Activity> activeList (String startTime,String endTime,String app_code){
		
		String today=DateUtil.getSysDateString();
		
		List<Activity> list = new LinkedList<ApiForFlashActiveResult.Activity>();
		
		List<MDataMap> today_active_list=DbUp.upTable("oc_activity_flashsales").queryAll("start_time,end_time,activity_code,activity_name", "start_time", "status=:status and app_code=:app_code and ((start_time>=:startTime and start_time<=:endTime) "+(today.equals(startTime.substring(0, 10))?"or (start_time<:startTime and end_time>=:startTime)":"")+") ", new MDataMap("status","449746740002","app_code",app_code,"startTime",startTime,"endTime",endTime));
			
		for (MDataMap today_active : today_active_list) {
			
			String start_time=today_active.get("start_time");
			String end_time=today_active.get("end_time");
			String activity_code=today_active.get("activity_code");
			String activity_name=today_active.get("activity_name");
			
			Activity activity = new Activity();
			activity.setActivity_code(activity_code);
			activity.setActivity_name(activity_name);
			activity.setEnd_time(end_time);
			activity.setStart_time(start_time);
//			activity.setProductList(activeProductList(activity_code));
			
			list.add(activity);
		}
		
		return list;
	}
	
	private List<ActiceProduct> activeProductList(String activity_code){
		
		List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
		
		String sql="SELECT sku_code,location,product_code,min(vip_price) vip_price from oc_flashsales_skuInfo where activity_code=:activity_code and status='449746810001' GROUP BY product_code ORDER BY location asc,product_code asc ";
		
		List<Map<String, Object>> list=DbUp.upTable("oc_flashsales_skuInfo").dataSqlList(sql, new MDataMap("activity_code",activity_code));
		
		for (Map<String, Object> map : list) {
			
			ActiceProduct product = new ActiceProduct();
			
//			String sku_code=map.get("sku_code");
			String product_code=(String)map.get("product_code");
			
//			MDataMap  skuInfo=DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
			MDataMap  productInfo=DbUp.upTable("pc_productinfo").one("product_code",product_code);
			
			String video_url=productInfo.get("video_url");
			String mainpic_url=productInfo.get("mainpic_url");
			String product_status=productInfo.get("product_status");
			
			if(!"4497153900060002".equals(product_status)){  //只要已上架的商品
				continue;
			}
			
			product.setSell_price(new BigDecimal(productInfo.get("market_price")));
			product.setVip_price((BigDecimal)map.get("vip_price"));
			product.setProduct_name(productInfo.get("product_name"));
			product.setProduct_code(product_code);
//			product.setSales_num(salesNum1(activity_code, product_code));
			product.setSales_num(0);
			product.setIs_video(StringUtils.isNotBlank(video_url)?1:0);
//			product.setSell_count(sellCount(activity_code, product_code));
			product.setSell_count(0);
			product.setImg_url(mainpic_url);
//			product.setActivity_url("");
			product.setFile_url(video_url);
			
			product.setDiscount("￥"+product.getSell_price().subtract(product.getVip_price()).doubleValue());
			
			if(product.getSell_price().compareTo(BigDecimal.ZERO)>0&&product.getVip_price().compareTo(BigDecimal.ZERO)>0)
			{
				product.setDiscountRate(""+product.getVip_price().multiply(new BigDecimal(100)).divide(product.getSell_price(), 0, BigDecimal.ROUND_HALF_UP));
			}
			else {
				product.setDiscountRate("100");
			}
			
			product.setGoods_link(FormatHelper.formatString(bConfig("ordercenter.product_detail"),product.getProduct_code(),product.getProduct_code()));
			product.setLocation((Integer)map.get("location"));
			
			productList.add(product);
		}
		
		return productList;
	}
	
	
	private int sellCount(String activity_code,String product_code){
		
		ProductStoreService productStoreService = new ProductStoreService();
		FlashsalesService flashsalesService = new FlashsalesService();
		
		MDataMap activityMap=DbUp.upTable("oc_activity_flashsales").one("activity_code",activity_code);
		String now=DateUtil.getSysDateTimeString();
		String start_time=activityMap.get("start_time");
		String end_time=activityMap.get("end_time");
		
		List<MDataMap> fskuList=DbUp.upTable("oc_flashsales_skuInfo").queryByWhere("activity_code",activity_code,"product_code",product_code,"status","449746810001");
		
//		闪购已抢百分比=商品已上闪购时长/闪购总时长-随机数（0-20%）+该商品闪购销量/该商品闪购销量上限*（1-商品已上闪购时长/闪购总时长+随机数（与之前随机数相同））
		int allNum=0;
		for (MDataMap fskuMap : fskuList) {
			
			String update_time=fskuMap.get("update_time");
			String sku_code=fskuMap.get("sku_code");
//			String purchase_limit_day_num=fskuMap.get("purchase_limit_day_num");
			String sales_num=fskuMap.get("sales_num");
			
			
			if(DateUtil.compareTime(start_time, update_time, DateUtil.DATE_FORMAT_DATETIME)>0){
				update_time=start_time;
			}
			
			try {
				
				BigDecimal time1=new BigDecimal(String.valueOf(DateUtil.subtime(update_time, now, DateUtil.DATE_FORMAT_DATETIME)));//商品已上闪购时长
				BigDecimal time2=new BigDecimal(String.valueOf(DateUtil.subtime(start_time, end_time, DateUtil.DATE_FORMAT_DATETIME)));//闪购总时长
				BigDecimal random=new BigDecimal(String.valueOf(random()));//随机数
//				BigDecimal random=new BigDecimal("0.17");
				BigDecimal saleNum1=new BigDecimal(String.valueOf(flashsalesService.salesNum(activity_code, sku_code, MemberConst.MANAGE_CODE_HOMEHAS)));//该商品闪购销量
				BigDecimal saleNum2=new BigDecimal(sales_num);//该商品闪购销量上限
				
				bLogInfo(0, "惠家有-测试-一闪购已抢百分比-随机数："+random+"|sku_code:"+sku_code+"|activity_code:"+activity_code);
				
//				如果活动销售量==促销库存 ，则卖出数量=活动销售量
				if(saleNum1.compareTo(saleNum2)>=0){
					allNum+=saleNum2.intValue();
					continue;
				}
				
				
				int stock_num=productStoreService.getStockNumBySku(sku_code);
				if(stock_num==0&&BigDecimal.ZERO.compareTo(saleNum1)==0){
					continue;
				}
				
				if(stock_num==0&&BigDecimal.ZERO.compareTo(saleNum1)<0){
					saleNum2=saleNum1;
				}
				
				//-------------------------
				BigDecimal prdfixNum=time1.divide(time2,2,BigDecimal.ROUND_HALF_UP).subtract(random);
				
				BigDecimal res=(saleNum1.divide(saleNum2,2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.ONE.subtract(prdfixNum)).add(prdfixNum)).multiply(saleNum2);
				
				int i=res.intValue();
				
				if(i<0){
					i=0;
				}
				allNum+=i;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return allNum;
	}
	
	private double random() {
		Random random = new Random();
		int i=random.nextInt(20);
		double ran=i/100.00;
		return ran;
	}
	
	private int salesNum(String activity_code,String product_code){
		Map<String, Object>  map=DbUp.upTable("oc_flashsales_skuInfo").dataSqlOne("SELECT sum(sales_num) num from oc_flashsales_skuInfo where status='449746810001' and activity_code=:activity_code and product_code=:product_code", new MDataMap("activity_code",activity_code,"product_code",product_code));
		if(map!=null&&map.size()>0){
			
			BigDecimal num=(BigDecimal)map.get("num");
			if(num!=null){
				return num.intValue();
			}
		}
		return 0;
	}
	
	
	private int salesNum1(String activity_code,String product_code){
		
		ProductStoreService productStoreService = new ProductStoreService();
		int num=0;
		
		List<MDataMap> list=DbUp.upTable("oc_flashsales_skuInfo").queryAll("sales_num,sku_code", "", "status='449746810001' and activity_code=:activity_code and product_code=:product_code", new MDataMap("activity_code",activity_code,"product_code",product_code));
		if(list!=null&&list.size()>0){
			for (MDataMap mDataMap : list) {
				String sku_code=mDataMap.get("sku_code");
				int sales_num=Integer.valueOf(mDataMap.get("sales_num"));
				//判断库存是否为0
				if(productStoreService.getStockNumBySku(sku_code)<=0){
					continue;
				}
				
				num+=sales_num;
			}
			
		}
		
		return num;
	}
}
