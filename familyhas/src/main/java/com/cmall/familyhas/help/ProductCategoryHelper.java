package com.cmall.familyhas.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

public class ProductCategoryHelper {

	private final static MDataMap  productCategoryCache= new MDataMap();
	
	private static String QUERY_CATEGROY_BY_PRODUCTCODE = "select category_code from uc_sellercategory_product_relation where product_code=:product_code and seller_code = 'SI2003'";
	
	public String getProductCategory(String seller_code){
		String categoryhtml= getCategoryInfo();
		
		/*if(productCategoryCache.containsKey(seller_code)){
			categoryhtml = productCategoryCache.get(seller_code);
		}else{
			categoryhtml= ;
			productCategoryCache.put(seller_code, categoryhtml);
		}*/
		return categoryhtml;
	}
	public static void refreshProductCategoryCache(String seller_code){
		//productCategoryCache.put(seller_code, getCategoryInfo());
	}
	public List<Map<String, Object>> getProductNavigate(String productCode){
		return getProductCategoryNameByCategroyId(getProductCategoryByProductCode(productCode));
	}
	//获取分类并拼装成html
	private static String getCategoryInfo(){
		String king = TopUp.upConfig("homepool.jyh_king");//家有尊享结点名称
		
		//查询结果要去掉家有尊享结点
		StringBuffer sb = new StringBuffer();
		sb.append("<ul>");
		String querySql = "select category_code,category_name from uc_sellercategory where LENGTH(category_code)=12 and seller_code ='SI2009' and flaginable='449746250001' and category_name !='"+king+"' order by sort+0";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(querySql, new MDataMap());
		for(Map<String,Object> map : list){
			sb.append("<li><a href=\"../../web/list/facet?categroyCode=").append((String) map.get("category_code")).append("\">").append((String) map.get("category_name")).append("</a></li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	/**
	 * 获取默认尊享分类categroyCode
	 */
	public String getKingCategoryCode(){
		StringBuffer sb = new StringBuffer();
		sb.append("");
		String king = TopUp.upConfig("homepool.jyh_king");//家有尊享结点名称
		String querySql = "select category_code,category_name from uc_sellercategory where LENGTH(category_code)=16 and seller_code ='SI2009' and flaginable='449746250001' and substring(category_code,1,12)=(select category_code from uc_sellercategory where LENGTH(category_code)=12 and  seller_code ='SI2009' and flaginable='449746250001' and category_name='"+king+"') order by sort";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(querySql, new MDataMap());
		if(list.size() > 0)
			sb.append(list.get(0).get("category_code"));
		return sb.toString();
	}
	
	
	/**
	 * 获取家有尊享分类列表
	 * */
	
	public String getKingCategoryHtml(){
		StringBuffer sb = new StringBuffer();
		String king = TopUp.upConfig("homepool.jyh_king");//家有尊享结点名称
		sb.append("<ul>");
		String querySql = "select category_code,category_name from uc_sellercategory where LENGTH(category_code)=16 and seller_code ='SI2009' and flaginable='449746250001' and substring(category_code,1,12)=(select category_code from uc_sellercategory where LENGTH(category_code)=12 and  seller_code ='SI2009' and flaginable='449746250001' and category_name='"+king+"') order by sort";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(querySql, new MDataMap());
		int num = 0;
		int length = list.size();
		for(Map<String,Object> map : list){
			sb.append("<li><a categroyCode=\""+map.get("category_code")+"\" id=\"tab"+num+"\"  href=\"../../web/vip/category?num="+num+"&categroyCode=").append((String) map.get("category_code")).append("\">").append((String) map.get("category_name")).append("</a></li>");
			num ++;
		}
		sb.append("</ul>");
		
		return sb.toString();
	}
	
	//获取搜索热词
	public String getKeyWordName(){
		String keyName= "";
		String querySql = "select  top_keyword from pc_hot_word where top_appcode=\"SI2009\" order by rand() limit 1 ";
		Map<String, Object> map = DbUp.upTable("pc_hot_word").dataSqlOne(querySql, new MDataMap());
		if(null!=map){
			keyName=map.get("top_keyword").toString();                                                        
		}else{
			keyName="输入您想要的商品";
		}
		return keyName;
	}
	
	private List<Map<String, Object>> getProductCategoryNameByCategroyId(String categroyId){
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		
		if(null == categroyId)
			return results;
		String querySql = "select level,category_code,category_name,parent_code from uc_sellercategory where seller_code ='SI2003' and category_code=:category_code";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(querySql, new MDataMap("category_code",categroyId));
		
		if(list != null && list.size() > 0){
			results.add(list.get(0));
			int level = Integer.parseInt((String)list.get(0).get("level"));
			while(level > 2){
				list = DbUp.upTable("uc_sellercategory").dataSqlList("select level,category_code,category_name,parent_code from uc_sellercategory where seller_code ='SI2003' and category_code="+(String)list.get(0).get("parent_code"), new MDataMap());
				level = Integer.parseInt((String)list.get(0).get("level"));
				if(level >= 2){
					results.add(list.get(0));
				}
			}
		}
		return results;
	}
	private String getProductCategoryByProductCode(String productCode){
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory_product_relation").dataSqlList(QUERY_CATEGROY_BY_PRODUCTCODE, new MDataMap("product_code",productCode));
		if(list != null && list.size() > 0){
			return (String) list.get(0).get("category_code");
		}
		return null;
	}
}
