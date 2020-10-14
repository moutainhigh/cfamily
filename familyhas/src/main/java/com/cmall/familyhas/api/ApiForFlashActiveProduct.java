package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForFlashActiveProductResult;
import com.cmall.familyhas.api.input.ApiForFlashActiveProductResult.ActiceProduct;
import com.cmall.familyhas.api.result.ApiForFlashActiveProductInput;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.ordercenter.service.FlashsalesService;
import com.cmall.productcenter.service.ProductService;
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
public class ApiForFlashActiveProduct extends RootApiForManage<ApiForFlashActiveProductResult, ApiForFlashActiveProductInput> {

	public ApiForFlashActiveProductResult Process(ApiForFlashActiveProductInput inputParam,MDataMap mRequestMap) {
		
		ApiForFlashActiveProductResult activeResult = new ApiForFlashActiveProductResult();
		activeResult.setProductList(activeProductList(inputParam.getActivity_code(),inputParam.getBuyerType(),inputParam.getImgWidth()));
		return activeResult;
	}

	
	private List<ActiceProduct> activeProductList(String activity_code,String buyerType,int imgWidth){
		
		List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
		
		//取出闪购sku
		String sql="SELECT sku_code,location,product_code,min(vip_price) vip_price from oc_flashsales_skuInfo where activity_code=:activity_code and status=:status GROUP BY product_code ORDER BY location asc,product_code asc ";
		List<Map<String, Object>> list=DbUp.upTable("oc_flashsales_skuInfo").dataSqlList(sql, new MDataMap("activity_code",activity_code,"status","449746810001"));
		
//		Map<String, BigDecimal>  vip_price_map= new HashMap<String, BigDecimal>(list.size());
		
//		//判断是否内购
//		List<String> productCodeList=new ArrayList<String>();
//		for (Map<String, Object> map : list) {
//			productCodeList.add((String)map.get("product_code"));
//			vip_price_map.put((String)map.get("product_code"), (BigDecimal)map.get("vip_price"));
//		}
		
//		ProductService productService = new ProductService();
//		Map<String, BigDecimal> vip_price_map1=productService.getVipSpecialPrice(buyerType, productCodeList);
//		
//		if(vip_price_map1!=null){//不参与内购，则使用闪购价
//			vip_price_map = vip_price_map1;
//		}
//		
		
		for (Map<String, Object> map : list) {
			
			ActiceProduct product = new ActiceProduct();
			
//			String sku_code=map.get("sku_code");
			String product_code=(String)map.get("product_code");
			
//			MDataMap  skuInfo=DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
			MDataMap  productInfo=DbUp.upTable("pc_productinfo").one("product_code",product_code);
			
			String video_url=productInfo.get("video_url");
			String mainpic_url=productInfo.get("mainpic_url");
			String product_status=productInfo.get("product_status");
			BigDecimal market_price= new BigDecimal(productInfo.get("market_price"));
			
			if (market_price.compareTo(new BigDecimal(0)) == 0) {
				continue;
			}
			
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
			
			
			if(StringUtils.isNotBlank(mainpic_url)&&imgWidth>0){
				mainpic_url=new ProductService().getPicInfoOprBig(imgWidth, mainpic_url).getPicNewUrl();
			}
			product.setImg_url(mainpic_url);
//			product.setActivity_url("");
			product.setFile_url(video_url);
			
			product.setDiscount("￥"+product.getSell_price().subtract(product.getVip_price()).doubleValue());
			
			if(product.getSell_price().compareTo(BigDecimal.ZERO)>0&&product.getVip_price().compareTo(BigDecimal.ZERO)>0) {
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
	
	
//	private List<ActiceProduct> activeProductList(String activity_code){
//		
//		Map<String, ActiceProduct> productMap = new LinkedHashMap<String, ActiceProduct>();
//		List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
//		ActiveForproduct activeForproduct = new ActiveForproduct();
//		
//		String sql="SELECT sku_code,location,product_code,vip_price from oc_flashsales_skuInfo where activity_code=:activity_code and status='449746810001' ORDER BY location asc,product_code asc ";
//		
//		List<Map<String, Object>> list=DbUp.upTable("oc_flashsales_skuInfo").dataSqlList(sql, new MDataMap("activity_code",activity_code));
//		
//		for (Map<String, Object> map : list) {
//			
//			ActiceProduct product = new ActiceProduct();
//			
//			String sku_code=(String)map.get("sku_code");
//			String product_code=(String)map.get("product_code");
//			
////			MDataMap  skuInfo=DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
//			MDataMap  productInfo=DbUp.upTable("pc_productinfo").one("product_code",product_code);
//			
//			String video_url=productInfo.get("video_url");
//			String mainpic_url=productInfo.get("mainpic_url");
//			String product_status=productInfo.get("product_status");
//			BigDecimal market_price= new BigDecimal(productInfo.get("market_price"));
//			
//			if (market_price.compareTo(new BigDecimal(0)) == 0) {
//				continue;
//			}
//			
//			if(!"4497153900060002".equals(product_status)){  //只要已上架的商品
//				continue;
//			}
//			
//			
//			ActiveReq activeReq = new ActiveReq();
//			activeReq.setProduct_code(product_code);
//			activeReq.setSku_code(sku_code);
//			activeReq.setSku_num(1);
//			activeReq.setBuyer_code(getFlagLogin()?getOauthInfo().getUserCode():null);
//			ActiveReturn activeReturn = activeForproduct.activeGallery(activeReq, new ActiveResult());
//			BigDecimal vip_price = BigDecimal.ZERO;
//			if(activeReturn==null){
//				vip_price = activeForproduct.upPrice(activeReq);
//			}
//			vip_price=activeReturn.getActivity_price();
//			
//			
//			//开始判断一个最小的价格
//			
//			ActiceProduct product2=productMap.get(product_code);
//			if(product2==null){
//				productMap.put(product_code,product);
//			}else{
//				if(product2.getVip_price().compareTo(vip_price)>0){
//					productMap.put(product_code,product);
//				}else{
//					continue;
//				}
//			}
//			
//			
//			product.setSell_price(new BigDecimal(productInfo.get("market_price")));
//			product.setVip_price(vip_price);
//			product.setProduct_name(productInfo.get("product_name"));
//			product.setProduct_code(product_code);
////			product.setSales_num(salesNum1(activity_code, product_code));
//			product.setSales_num(0);
//			product.setIs_video(StringUtils.isNotBlank(video_url)?1:0);
////			product.setSell_count(sellCount(activity_code, product_code));
//			product.setSell_count(0);
//			product.setImg_url(mainpic_url);
////			product.setActivity_url("");
//			product.setFile_url(video_url);
//			
//			product.setDiscount("￥"+product.getSell_price().subtract(product.getVip_price()).doubleValue());
//			
//			if(product.getSell_price().compareTo(BigDecimal.ZERO)>0&&product.getVip_price().compareTo(BigDecimal.ZERO)>0){
//				product.setDiscountRate(""+product.getVip_price().multiply(new BigDecimal(100)).divide(product.getSell_price(), 0, BigDecimal.ROUND_HALF_UP));
//			}
//			else {
//				product.setDiscountRate("100");
//			}
//			
//			product.setGoods_link(FormatHelper.formatString(bConfig("ordercenter.product_detail"),product.getProduct_code(),product.getProduct_code()));
//			product.setLocation((Integer)map.get("location"));
//			
//		}
//		
//		for (Map.Entry<String, ActiceProduct> entry : productMap.entrySet()) {
//			productList.add(entry.getValue());
//		}
//		
//		return productList;
//	}
	
	
//	private List<ActiceProduct> activeProductList(String activity_code){
//		
//		List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
//		
//		String sql="SELECT sku_code,location,product_code,min(vip_price) vip_price from oc_flashsales_skuInfo where activity_code=:activity_code and status='449746810001' GROUP BY product_code ORDER BY location asc,product_code asc ";
//		
//		List<Map<String, Object>> list=DbUp.upTable("oc_flashsales_skuInfo").dataSqlList(sql, new MDataMap("activity_code",activity_code));
//		
//		for (Map<String, Object> map : list) {
//			
//			ActiceProduct product = new ActiceProduct();
//			
////			String sku_code=map.get("sku_code");
//			String product_code=(String)map.get("product_code");
//			
////			MDataMap  skuInfo=DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
//			MDataMap  productInfo=DbUp.upTable("pc_productinfo").one("product_code",product_code);
//			
//			String video_url=productInfo.get("video_url");
//			String mainpic_url=productInfo.get("mainpic_url");
//			String product_status=productInfo.get("product_status");
//			BigDecimal market_price= new BigDecimal(productInfo.get("market_price"));
//			
//			if (market_price.compareTo(new BigDecimal(0)) == 0) {
//				continue;
//			}
//			
//			if(!"4497153900060002".equals(product_status)){  //只要已上架的商品
//				continue;
//			}
//			
//			product.setSell_price(new BigDecimal(productInfo.get("market_price")));
//			product.setVip_price((BigDecimal)map.get("vip_price"));
//			product.setProduct_name(productInfo.get("product_name"));
//			product.setProduct_code(product_code);
////			product.setSales_num(salesNum1(activity_code, product_code));
//			product.setSales_num(0);
//			product.setIs_video(StringUtils.isNotBlank(video_url)?1:0);
////			product.setSell_count(sellCount(activity_code, product_code));
//			product.setSell_count(0);
//			product.setImg_url(mainpic_url);
////			product.setActivity_url("");
//			product.setFile_url(video_url);
//			
//			product.setDiscount("￥"+product.getSell_price().subtract(product.getVip_price()).doubleValue());
//			
//			if(product.getSell_price().compareTo(BigDecimal.ZERO)>0&&product.getVip_price().compareTo(BigDecimal.ZERO)>0)
//			{
//				product.setDiscountRate(""+product.getVip_price().multiply(new BigDecimal(100)).divide(product.getSell_price(), 0, BigDecimal.ROUND_HALF_UP));
//			}
//			else {
//				product.setDiscountRate("100");
//			}
//			
//			product.setGoods_link(FormatHelper.formatString(bConfig("ordercenter.product_detail"),product.getProduct_code(),product.getProduct_code()));
//			product.setLocation((Integer)map.get("location"));
//			
//			productList.add(product);
//		}
//		
//		return productList;
//	}
	
	
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
