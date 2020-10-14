package com.cmall.familyhas.service;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.ApiGetEventSkuInfo;
import com.cmall.familyhas.api.input.ApiGetSkuInfoInput;
import com.cmall.familyhas.api.result.ApiGetSkuInfoResult;
import com.cmall.groupcenter.groupapp.model.GetFriendInformationInfoResult;
import com.cmall.groupcenter.groupapp.model.GetGoodsCricleListInfoInput;
import com.cmall.groupcenter.groupapp.model.GetGoodsCricleListInfoResult;
import com.cmall.groupcenter.groupapp.model.GoodComparatorDesc;
import com.cmall.groupcenter.groupapp.model.GoodsCricleInfo;
import com.cmall.groupcenter.groupapp.model.GoodsInfo;
import com.cmall.groupcenter.groupapp.model.Person;
import com.cmall.groupcenter.groupapp.model.ShareModel;
import com.cmall.groupcenter.groupdo.GroupConst;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.model.PageResults;
import com.cmall.groupcenter.model.QueryBankInfoResult;
import com.cmall.groupcenter.pc.model.PcVirtualPager;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.membercenter.helper.NickNameHelper;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.google.gson.internal.LinkedTreeMap;
import com.srnpr.xmasproduct.api.ApiSkuInfo;
import com.srnpr.xmasproduct.model.SkuInfos;
import com.srnpr.xmassystem.modelevent.PlusModelFullMoney;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.LogInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webdo.WebTemp;

public class GoodsCricleService extends BaseClass{
	
	
	/**
	 * 查询 热销榜 超级返 数据
	 * @param inputParam
	 * @param apikey
	 * @param token
	 * @return
	 */
	public GetGoodsCricleListInfoResult GetGoodsCricleListInfo(GetGoodsCricleListInfoInput inputParam,String apikey,String token){
   	 	
	  int qtype= Integer.parseInt(inputParam.getSectionType());

   	  String goodCricleSql ="select content from  gc_goods_cricle_temp_info where  type=:type";
	  Map<String, Object> rmap = DbUp.upTable("gc_goods_cricle_temp_info").dataSqlOne(goodCricleSql, new MDataMap("type",String.valueOf(qtype)));
	  String content= String.valueOf(rmap.get("content"));
	  
	 JsonHelper<List<GoodsCricleInfo>> jH = new JsonHelper<List<GoodsCricleInfo>>();
	 List<GoodsCricleInfo> list = jH.StringToObj(content,new ArrayList() );
	 int pagesize = inputParam.getPaging().getLimit();
	 pagesize = pagesize==0 ? 10 :pagesize;
	 // 构造一个分页器
	 PcVirtualPager<GoodsCricleInfo> pager = new PcVirtualPager<GoodsCricleInfo>(list.size(), inputParam.getPaging().getOffset()+1, pagesize,list);
	
     GetGoodsCricleListInfoResult result = new GetGoodsCricleListInfoResult();
     result.setGoodsContentList(pager.getCurrentPageData());
     
     PageResults pageResult = new PageResults();
     pageResult.setCount(pager.getTotalResults());
     pageResult.setMore(pager.getPageIndex()<pager.getPageCount() ? 1 :0 );
     pageResult.setTotal(pager.getCurrentPageData().size());
     result.setPaged(pageResult);
		
	  return result;
	}
	
	
	//生成热销榜 超级返的对象，将对象转换json，入库
     public GetGoodsCricleListInfoResult generateGoodsCricleInfo(GetGoodsCricleListInfoInput inputParam,String apikey,String token){
    	 GetGoodsCricleListInfoResult result=new GetGoodsCricleListInfoResult();
    	 int qtype= Integer.parseInt(inputParam.getSectionType());
    	String qtypename; 
    	if(qtype==0)
    		qtypename="热销榜";
		else
			qtypename="超返利";
    			
    	List<Map<String, Object>> sukProductList = this.getProductList(inputParam,result);
    	
    	
		List<GoodsCricleInfo> goodsContentList = new ArrayList<GoodsCricleInfo>();

    	for(Map<String, Object> sukProduct: sukProductList){
    		String productCode = String.valueOf(sukProduct.get("product_code"));
        	//System.out.println("productCode:"+productCode);
    		HashMap<String, Object> httpProductMap = this.getHttpResponseMap(productCode,token);
        	SkuInfos skuInfos = this.getSkuInfos(productCode);
        	GoodsInfo gInfo=this.getGoodInfoObjFromHttpMap(new GoodsInfo(),httpProductMap,skuInfos);
        	//查询剩余库存
        	gInfo.setLimitStock(String.valueOf(this.getLimitStock(skuInfos.getSkus())));
        	GoodsCricleInfo ci = new GoodsCricleInfo();
        	
        	
	    	String prex = bConfig("groupcenter.wei_shop_url");
	    	gInfo.setGoodDetailUrl(prex+"Product_Detail.html?pid="+productCode);
        	gInfo.setGoodsSourceUrl("惠家有");
        	//超返利
        	if(qtype==1 && sukProduct.get("rebatef")!=null){
        		gInfo.setRebateScale(Double.parseDouble(sukProduct.get("rebatef").toString()));
        		Double rebateMoney =Double.parseDouble(sukProduct.get("rebatef").toString())/100 * Double.parseDouble(gInfo.getCurrentPrice());
        		NumberFormat nf = NumberFormat.getNumberInstance(); 
            	nf.setMaximumFractionDigits(2); 
       		 	gInfo.setRebateMoney(String.valueOf(nf.format(rebateMoney)));
        		//剩余时间
        		String endTimStr = String.valueOf(sukProduct.get("end_time"));
        		Date endTime = DateHelper.parseDate(endTimStr);
        		Long offsetTime = endTime.getTime()- new Date().getTime();
        		ci.setOffsetTime(offsetTime);
        	} 
        	//热销榜
        	else {
        		//返利比数
        		String salesCount ="0";
        		if(sukProduct.get("salesCount")!=null){
        			salesCount =sukProduct.get("salesCount").toString();
        		}
        		gInfo.setSalesCount(salesCount);
        		Double reRate = this.getHuiRebateRate();
        		if(reRate!=null) {
        			//热销榜
            		Double rebateMoney =reRate/100 * Double.parseDouble(gInfo.getCurrentPrice());
            		NumberFormat nf = NumberFormat.getNumberInstance(); 
                	nf.setMaximumFractionDigits(2); 
           		 	gInfo.setRebateMoney(String.valueOf(nf.format(rebateMoney)));
        		}
        	}
        	
        	//分享实体
			ShareModel smodel = new ShareModel();
			smodel.setShareContent(String.valueOf(httpProductMap.get("discriptInfo")));
			String shareTitle = "["+qtypename+"] "+gInfo.getGoodsName()+","+gInfo.getCurrentPrice()+"元,最低返"+gInfo.getRebateMoney()+"元";
			smodel.setShareTitle(shareTitle);
			//图片(旧图)
			LinkedTreeMap map = (LinkedTreeMap)httpProductMap.get("mainpicUrl");
	    	if(map!=null) {
	    		smodel.setSharePicUrl(String.valueOf(map.get("picOldUrl")));
	    	}
	    	
	    	//String shareUrl = prex+"/cgroup/web/grouppageSecond/productdetail.html?productUrl="+bConfig("familyhas.shareUrl")+gInfo.getGoodsCode()+"&api_key="+apikey;
	    	//String shareUrl = prex+"/cgroup/web/grouppageSecond/productdetail.html?productUrl="+bConfig("familyhas.shareUrl")+gInfo.getGoodsCode()+"&api_key="+apikey;

	    	smodel.setShareUrl(prex+"Product_Detail.html?pid="+productCode);
			ci.setShareModel(smodel);
			//查询微公社分享数
			int shareCount =this.getShareNumByProductCode(gInfo.getGoodsCode());
			ci.setShareCount(shareCount);
			ci.setGoodsInfo(gInfo);
			
			goodsContentList.add(ci);
    	}
    	/*//超级返的重新排序
    	if(qtype==1){
        	result.setGoodsContentList(this.getSortGoodInfo(goodsContentList));
    	} else {
        	result.setGoodsContentList(goodsContentList);
    	}*/
    	result.setGoodsContentList(goodsContentList);
    	return  result;
     }
     
     //根据商品编号获取，分享数
     public int getShareNumByProductCode(String productCode){
    	 int count=0;
    	 String productShareSql ="SELECT   SUM(qq_share_cout+qq_space_share_cout+wei_share_count+wei_friendcircle_share_count+sina_share_count+sms_share_count+wei_gs_share_count) as mcount, count(1) FROM `gc_product_share_log` where product_code=:product_code GROUP BY product_code ";
    	 Map<String, Object> rmap = DbUp.upTable("gc_product_share_log").dataSqlOne(productShareSql, new MDataMap("product_code",productCode));
    	 if(rmap!=null && rmap.get("mcount")!=null){
    		count= Integer.parseInt(rmap.get("mcount").toString());
    	 }
    	return  count;
     }
     
     
     
     public SkuInfos getSkuInfos(String productCode){
    	 PlusModelSkuQuery inputParam = new PlusModelSkuQuery();
    	 inputParam.setCode(productCode);
    	 SkuInfos skuInfos = new ApiSkuInfo().Process(inputParam, null);
    	 return skuInfos;
     }
     
     //获取商品剩余库存
     public int getLimitStock(List<PlusModelSkuInfo> skus){
    	 int limitStock=0;
    	 if(skus!=null && skus.size()>0){
    		 for(int i=0;i<skus.size();i++){
    			 limitStock=limitStock+new Long(skus.get(i).getLimitStock()).intValue();
        	 }
    	 }
    	 return limitStock;
     }
     
     //转换 商品信息
     public GoodsInfo  getGoodInfoObjFromHttpMap(GoodsInfo gdInfo,HashMap<String, Object> httpProductMap,SkuInfos skuInfos){
    	//商品-获取商品基本信息 -api获取
    	 //商品编号
    	String productCode= String.valueOf(httpProductMap.get("productCode"));
    	//商品名称
    	String productName= String.valueOf(httpProductMap.get("productName"));
    	//返现金额
    	String disMoney= String.valueOf(httpProductMap.get("disMoney"));
    	//售价
    	//String sellPrice = String.valueOf(httpProductMap.get("sellPrice"));
    	//原价
    	String discount = String.valueOf(httpProductMap.get("marketPrice"));//国杰
    	
    	String productStatus = String.valueOf(httpProductMap.get("productStatus"));//商品上下架状态

    	List<PlusModelSkuInfo> listMoney = skuInfos.getSkus();
    	BigDecimal sellPriceT =new BigDecimal(0);
    	int count=0;
    	for(PlusModelSkuInfo p:listMoney){
	    		if(count==0){
	    			sellPriceT=p.getSellPrice();
	    		}
	    		count++;
	    		if(sellPriceT.compareTo(p.getSellPrice()) ==1){
	    			sellPriceT= p.getSellPrice();
	        		//System.out.println("sellPriceT="+ sellPriceT);
	    		}
	    	}
    	String sellPrice= String.valueOf(sellPriceT);
    	//System.out.println("noo:"+sellPrice);
    	
    	gdInfo.setGoodsCode(productCode);
    	gdInfo.setGoodsName(productName);
    	gdInfo.setRebateMoney(disMoney);
    	
    	
    	gdInfo.setCurrentPrice(sellPrice);
    	gdInfo.setOriginalPrice(discount);
    	gdInfo.setProductStatus(productStatus);
    	
    	gdInfo.setDiscountPrice(Double.parseDouble(sellPrice)/Double.parseDouble(sellPrice));
    	
    	//图片
    	LinkedTreeMap map = (LinkedTreeMap)httpProductMap.get("mainpicUrl");
    	if(map!=null) {
        	gdInfo.setGoodsIcon(String.valueOf(map.get("picNewUrl")));
    	}
    	
    	//商品-获取SKU的价格和库存 api
    	//gdInfo.setCurrentPrice(skuInfos.getSellPrice());
    	
    	
    	return gdInfo;
     }
     
     //获取商品列表
     public  List<Map<String, Object>> getProductList(GetGoodsCricleListInfoInput inputParam,GetGoodsCricleListInfoResult result){
    	 String sql = "";
    	 int qtype= Integer.parseInt(inputParam.getSectionType());

    	 //热销榜
    	 if(qtype==0){
    		
    		 	    sql="     SELECT" +
    		 		"        pp.product_code," +
    		 		"				count(DISTINCT order_code) AS salesCount" +
    		 		"			FROM" +
    		 		"				groupcenter.gc_reckon_log gr" +
    		 		"			INNER JOIN productcenter.pc_skuinfo sku ON gr.sku_code = sku.sku_code  AND gr.reckon_change_type = '4497465200030001'" +
    		 		"			INNER JOIN productcenter.pc_productinfo pp ON pp.product_code = sku.product_code" +
    		 		"			AND sku.sale_yn = 'Y'" +
    		 		"			WHERE" +
    		 		"				1=1" +
    		 		"			AND gr.sku_code IS NOT NULL" +
    		 		"			AND gr.sku_code != ''" +
    		 		"			GROUP BY" +
    		 		"		   pp.product_code	" +
    		 		"      ORDER BY 	count(DISTINCT(order_code)) DESC," +
    		 		"			pp.create_time LIMIT 50" 
    				 +"  ";

    	 }
 		else
 		{	
 			//返利榜
 			sql="SELECT" +
 					"	ff.product_code,ff.rebatef,ff.end_time " +
 					" FROM" +
 					"	(" +
 					"		SELECT" +
 					"			srs.product_code,srs.rebatef,srs.end_time " +
 					"		FROM" +
 					"			(" +
 					"				SELECT" +
 					"					" +
 					" 					max(substring_index(srs.rebate_scale, \",\", 1)) rebatef "+
					" 					,min(end_time) as end_time, "+
					" 					srs.product_code, "+
					" 					max(srs.create_time) as create_time "+ 
 					"				FROM" +
 					"					groupcenter.gc_sku_rebate_scale srs" +
 			        "  					INNER  JOIN productcenter.pc_skuinfo sku  ON srs.sku_code = sku.sku_code AND  sku.sale_yn='Y' AND sku.product_code = srs.product_code "+
 					"				WHERE" +
 					"					srs.supper_rebate_flag = '4497472500070001'" +
 					"				AND srs.flag_enable=1 AND   ( NOW() > start_time  and now()<end_time)  " +
 					"   			 GROUP BY srs.product_code "+
 					"			) srs" +
 					"		LEFT JOIN (" +
 					"			SELECT" +
 					"				product_code," +
 					"				SUM(" +
 					"					qq_share_cout + qq_space_share_cout + wei_share_count + wei_friendcircle_share_count + sina_share_count + sms_share_count + wei_gs_share_count" +
 					"				) AS mcount," +
 					"				count(1)" +
 					"			FROM" +
 					"				groupcenter.gc_product_share_log" +
 					"			WHERE" +
 					"				1 = 1" +
 					"			GROUP BY" +
 					"				product_code" +
 					"			ORDER BY" +
 					"				mcount DESC" +
 					"			LIMIT 50" +
 					"		) sh ON sh.product_code = srs.product_code" +
 					"		WHERE" +
 					"			1 = 1" +
 					"		ORDER BY" +
 					"			sh.mcount DESC," +
 					"			cast(srs.rebatef AS signed) DESC,srs.end_time ASC," +
 					"		 srs.create_time ASC " +
 					"		LIMIT 50" +
 					"	) ff " +
 					" WHERE " +
 					"	1 = 1 ";
 		}
    	 
    	 List<Map<String, Object>> list = DbUp.upTable("pc_skuinfo").dataSqlList(sql, null);
     	
    	 int pagesize = inputParam.getPaging().getLimit();
    	 pagesize = pagesize==0 ? 10 :pagesize;
    	 // 构造一个分页器
    	 PcVirtualPager<Map<String, Object>> pager = new PcVirtualPager<Map<String, Object>>(list.size(), inputParam.getPaging().getOffset()+1, pagesize,list);
         
         PageResults pageResult = new PageResults();
         pageResult.setCount(pager.getTotalResults());
         pageResult.setMore(pager.getPageIndex()<pager.getPageCount() ? 1 :0 );
         pageResult.setTotal(pager.getCurrentPageData().size());
         result.setPaged(pageResult);
    	return pager.getCurrentPageData();
     }
     
     //判断是否还有下一页
     public  int getMore(int totalResults,int pageSize,int currentPage){
    	 int pageCount=0;
    	 pageSize = pageSize ==0 ? 10 : pageSize;
    	 if (totalResults % pageSize == 0) {
             pageCount = totalResults / pageSize;
         } else {
             pageCount = totalResults / pageSize + 1;
         }
    	if(currentPage+1<pageCount){
    		return 1;
    	}
    	 
    	 return 0;
     }
     
     
     /**
      * 获取商品信息
      * @param productCode
      * @param token
      * @return
      */
     public HashMap<String,Object>  getHttpResponseMap(String productCode,String token) {
    	 /*Map<String, String> map = new HashMap<String, String>();
  		map.put("api_key", "betafamilyhas");
  		map.put("api_target", "com_cmall_familyhas_api_ApiGetEventSkuInfo");
  		//map.put("api_secret", token);
  		map.put("api_input", "{\"version\":1,\"productCode\":\""+productCode+"\",\"picWidth\":0,\"buyerType\":\"4497469400050002\"}");
  		
  		String weiPrefix = bConfig("groupcenter.app_recommendPageUrl");
  		String url =weiPrefix+"/cgroup/jsonapi/com_cmall_familyhas_api_ApiGetEventSkuInfo";
  		String responseStr = Http_Request_Post.doPost(url, map, "utf-8");
 		HashMap<String,Object> responMap = new JsonHelper<HashMap<String,Object>>().GsonFromJson(responseStr, new HashMap<String,Object>());
  		//System.out.println("product code :"+productCode+"  返回的消息是:" + responseStr);
  		*/
     	 
 		ApiGetEventSkuInfo api = new ApiGetEventSkuInfo();
 		ApiGetSkuInfoInput input = new ApiGetSkuInfoInput();
 		input.setVersion(1);
 		input.setPicWidth(0);
 		input.setProductCode(productCode);
 		input.setBuyerType("4497469400050002");
 		ApiGetSkuInfoResult result = api.Process(input, null);
 		
 		String responseStr = new JsonHelper<ApiGetSkuInfoResult>().ObjToString(result);
 		
        //String responseStr="{\"resultLast\":\"2016-01-21 14:05:30\",\"resultCache\":60,\"resultCode\":1,\"resultMessage\":\"\",\"productCode\":\"8016408716\",\"productName\":\"刀刀刀刀刀刀刀刀刀刀具具具具具具具具具具刀刀刀刀刀刀刀刀刀刀具具具具具具具具具具\",\"sellPrice\":12,\"marketPrice\":1,\"flagSale\":0,\"productStatus\":\"4497153900060003\",\"videoUrl\":\"http://www.qhw.yshqi.com/beta_down/video/41s.mp4\",\"brandName\":\"双星\",\"brandCode\":\"44971602100030\",\"mainpicUrl\":{\"picOldUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24c66/88e9d21814424fd1a229d1872d006b9e.jpg\",\"picNewUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/imzoom/24df2/c8a4abb1dd24491ca78a8ca85025d923.jpg\",\"width\":0,\"height\":810,\"oldWidth\":0,\"oldHeight\":0},\"productComment\":[{\"skuCode\":null,\"skuColor\":\"黄色\",\"skuStyle\":\"规格11\",\"userMobile\":\"114****1477\",\"userFace\":\"\",\"commentContent\":\"用户未评价，系统默认好评！\",\"grade\":\"5\",\"gradeType\":\"好评\",\"commentTime\":\"2015-12-28\",\"replyContent\":\"\",\"replyTime\":\"\",\"commentPhotoList\":[]}],\"highPraiseRate\":\"100\",\"commentSumCounts\":10552,\"pcPicList\":[{\"picOldUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24c66/43721263c765414386448d2336fb6595.jpg\",\"picNewUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/imzoom/24e42/21c828f3a0ba49e49f9d7ea3b5de573a.jpg\",\"width\":0,\"height\":810,\"oldWidth\":0,\"oldHeight\":0}],\"discriptPicList\":[{\"picOldUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24c66/14e6422cc1a24ccea11df2ab36496a90.jpg\",\"picNewUrl\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/imzoom/24e42/bb9c028993fe45dea77021f6026fbbb4.jpg\",\"width\":0,\"height\":810,\"oldWidth\":0,\"oldHeight\":0}],\"skuList\":[{\"skuCode\":\"8019431804\",\"skuName\":\"刀具  黄色 规格11\",\"keyValue\":\"4497462000010001=44974620000100010010&4497462000020001=44974620000200010001\",\"stockNumSum\":0,\"sellPrice\":12,\"marketPrice\":1,\"activityInfo\":[],\"vipSpecialPrice\":\"0\",\"disMoney\":0.60,\"skuMaxBuy\":99,\"miniOrder\":1,\"limitBuy\":99,\"showLimitNum\":0}],\"discriptInfo\":\"啊是否实得分\",\"labelsList\":[\"\"],\"maxBuyCount\":99,\"propertyList\":[{\"propertyKeyCode\":\"4497462000010001\",\"propertyKeyName\":\"颜色\",\"propertyValueList\":[{\"propertyValueCode\":\"44974620000100010010\",\"propertyValueName\":\"黄色\"}]},{\"propertyKeyCode\":\"4497462000020001\",\"propertyKeyName\":\"款式\",\"propertyValueList\":[{\"propertyValueCode\":\"44974620000200010001\",\"propertyValueName\":\"规格11\"}]}],\"propertyInfoList\":[{\"propertykey\":\"商品编码\",\"propertyValue\":\"8016408716\"},{\"propertykey\":\"自定义属性1\",\"propertyValue\":\"内容11\"}],\"flagCheap\":0,\"exitVideo\":1,\"flagIncludeGift\":0,\"gift\":\"\",\"authorityLogo\":[{\"logoContent\":\"官网正品\",\"logoPic\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24d8c/ba09b8a317b744fda9bf6ed530a48704.png\",\"logoLocation\":7},{\"logoContent\":\"全场包邮\",\"logoPic\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24a71/bb9c4a0bff13403689c01599b14267d7.png\",\"logoLocation\":6},{\"logoContent\":\"正品保障\",\"logoPic\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24a71/e3f1ce219b674ce48010f99e403e122f.png\",\"logoLocation\":5},{\"logoContent\":\"7天无理由退换货\",\"logoPic\":\"http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/24abe/aabe40459c43401eb464afa26fa00c71.png\",\"logoLocation\":4}],\"endTime\":\"\",\"familyPriceName\":\"家有价\",\"discount\":-11.00,\"disMoney\":0.60,\"shareUrl\":\"http://share-qhbeta-cfamily.qhw.yshqi.com/s/p/8016408716\",\"saleNum\":\"12\",\"collectionProduct\":0,\"vipSpecialActivity\":0,\"vipSpecialTip\":\"\",\"vipSpecialPrice\":\"0\",\"limitBuyTip\":\"限购99件\",\"priceLabel\":\"\",\"vipSecKill\":0,\"buttonMap\":{\"buyBtn\":1,\"shopCarBtn\":1,\"callBtn\":0},\"minSellPrice\":\"12\",\"maxSellPrice\":\"12\",\"sysDateTime\":\"2016-01-21 14:05:30\",\"otherShow\":[],\"flagTheSea\":\"0\",\"commonProblem\":[]}";
 	    HashMap<String,Object> responMap = new JsonHelper<HashMap<String,Object>>().GsonFromJson(responseStr, new HashMap<String,Object>());

 		return responMap;
 	}
     
     //获取惠家友，返利比例
     public Double getHuiRebateRate(){
    	 
    	 String sql="SELECT" +
    			 "	substring_index(gtr.rebate_rate, \",\", 1) rebatef  " +
    			 "FROM" +
    			 "	`gc_wopen_appmanage` gwa" +
    			 " INNER JOIN gc_trader_rebate gtr ON gwa.trade_code = gtr.trader_code and gwa.app_code=:app_code";

    	 Map<String, Object> rmap = DbUp.upTable("gc_trader_rebate").dataSqlOne(sql, new MDataMap("app_code","SI2003"));
    	 if(rmap!=null && rmap.get("rebatef")!=null){
    		 return Double.parseDouble(String.valueOf(rmap.get("rebatef")));
    	 }
    	 return null;
    	 
     }
    	 
    	 
    	
     //折扣排序
     public List<GoodsCricleInfo> getSortGoodInfo(List<GoodsCricleInfo> cricleInfo){
   	 List<GoodsCricleInfo> newgcInfo = new ArrayList<GoodsCricleInfo> ();
    	 List <GoodsInfo > gs = new ArrayList<GoodsInfo>();
	   	 for(GoodsCricleInfo old : cricleInfo ){
	   		 System.out.println("原来的：分享数:"+old.getShareCount() +" 返利比例:"+old.getGoodsInfo().getRebateScale()+" 毫秒:"+old.getOffsetTime() );
	   		 

	   		 
	   		  //gs.add(old.getGoodsInfo());
	   	 }
   	 
	   	 Collections.sort(cricleInfo, new GoodComparatorDesc());
	   	 /*for(GoodsInfo newGood : gs ){
	   		 //System.out.println("现在的：分享数："+f.getDiscountPrice());
	   		 for(GoodsCricleInfo old : cricleInfo){
	   			if(old.getGoodsInfo().getGoodsCode().equals(newGood.getGoodsCode())){
	   				GoodsCricleInfo gc = new GoodsCricleInfo();
	   				gc.setGoodsInfo(newGood);
	   				gc.setOffsetTime(old.getOffsetTime());
	   				gc.setShareCount(old.getShareCount());
	   				gc.setShareModel(old.getShareModel());
	   				newgcInfo.add(gc);
	   			}
	   		 }
	   	 }*/
	   	
	   	 for(GoodsCricleInfo old : cricleInfo ){
	   		 System.out.println("现在的：分享数："+old.getShareCount()+ " 返利比例:"+old.getGoodsInfo().getRebateScale()+" 毫秒:"+old.getOffsetTime());

	   	 }
   	
   	 return cricleInfo;
    }
     
     
     
    
     
   
     
     
     
    
}
