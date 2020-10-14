package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiHomeColumnContentListInput;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.result.ApiHomeColumnContentListResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.Item;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 商品列表接口 
 * 目前只针对三栏两行和两栏两行
 * @author Administrator
 */
public class ApiHomeColumnContentList extends RootApiForVersion<ApiHomeColumnContentListResult, ApiHomeColumnContentListInput> {
	
	@Override
	public ApiHomeColumnContentListResult Process(ApiHomeColumnContentListInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		String channelId = getChannelId();
		String app_vision = getApiClientValue("app_vision");
		ApiHomeColumnContentListResult result = new ApiHomeColumnContentListResult();
		HomeColumnService hcService = new HomeColumnService();
		ProductService ps = new ProductService();
		int pageSize = 12;
		int nextPage = inputParam.getNextPage();
		int totalPage= 1;
		int start = pageSize*(nextPage-1);
		if(nextPage>0) {
			String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
			String columnCode = inputParam.getColumnCode();
			String sellerCode = getManageCode();
			String systemTime = DateUtil.getSysDateTimeString();//系统当前时间
			String viewType = inputParam.getViewType();
			int screenWidth = inputParam.getScreenWidth();
			List<Item> contentList = new ArrayList<Item>();
			String columnName = "";
			String sFields = "column_code, product_maintenance,rule_code,column_name,column_type";
	        String sOrders = "position asc , create_time desc";
	        String sWhere ="";
	        if("4497471600100004".equals(viewType)) {//小程序分开
	        	sWhere = " start_time <= '" + systemTime	+ "' and end_time > '" + systemTime	+ "' and is_delete = '449746250002' "
	  					+ "and release_flag = '449746250001' and seller_code = '" + sellerCode + "' and view_type = '"+viewType+"'" 
	  					+ "and column_code='"+columnCode+"'";
	        }else {//app或者全部
	        	sWhere = " start_time <= '" + systemTime	+ "' and end_time > '" + systemTime	+ "' and is_delete = '449746250002' "
	  					+ "and release_flag = '449746250001' and seller_code = '" + sellerCode + "' and (view_type = '"+viewType+"' or view_type='4497471600100005' )" 
	  					+ "and column_code='"+columnCode+"'";
	        }
	        List<MDataMap> columnMapList = DbUp.upTable("fh_apphome_column").queryAll(sFields, sOrders, sWhere, null);
	        if(columnMapList!=null&&columnMapList.size()>0) {
	        	MDataMap mDataMap = columnMapList.get(0);
	        	columnName=mDataMap.get("column_name").toString();
	        	if(mDataMap.get("column_type").toString().equals("4497471600010026")){
	        		List<HomeColumnContent> contList = hcService.getFlashActivityWeApp(sellerCode, "0", "",channelId);
	        		int count = contList.size();
	        		totalPage = count/pageSize;
					if(count%pageSize!=0) {totalPage++;}
					if(contList.size()>start+pageSize){
						contList = contList.subList(start, start+pageSize);
					}else{
						contList = contList.subList(start, contList.size());
					}
					
					for(HomeColumnContent con:contList){
						Item item = new Item();
						item.setProductCode(con.getProductInfo().getProductCode());
						item.setProductName(con.getProductInfo().getProductName());
						item.setOriginalPrice(new BigDecimal(con.getProductInfo().getMarkPrice()));//市场价
						item.setCurrentPrice(new BigDecimal(con.getProductInfo().getSellPrice()));//销售价
						item.setFlagTheSea(con.getProductInfo().getFlagTheSea());//是否海外购  1代表是  0代表不是
						item.setStockNum("有货");
						item.setTagList(ps.getTagListByProductCode(con.getProductInfo().getProductCode(), userCode,channelId));//标签
						PicInfo imgUrl = ps.getPicInfoOprBig(screenWidth, con.getProductInfo().getMainpicUrl());
						item.setImgUrl(imgUrl==null?"":imgUrl.getPicNewUrl());
						item.setProClassifyTag(con.getProductInfo().getProClassifyTag());
						item.setLabelsList(con.getProductInfo().getLabelsList());
						contentList.add(item);
					}
	        	}else if(mDataMap.get("column_type").toString().equals("4497471600010028")) {
	        			List<HomeColumnContent> contList = hcService.getFenXiaoActivityWeApp("0",false,app_vision,userCode,channelId);
		        		int count = contList.size();
		        		totalPage = count/pageSize;
						if(count%pageSize!=0) {totalPage++;}
						if(contList.size()>start+pageSize){
							contList = contList.subList(start, start+pageSize);
						}else{
							contList = contList.subList(start, contList.size());
						}
						for(HomeColumnContent con:contList){
							Item item = new Item();
							item.setProductCode(con.getProductInfo().getProductCode());
							item.setProductName(con.getProductInfo().getProductName());
							item.setOriginalPrice(new BigDecimal(con.getProductInfo().getMarkPrice()));//市场价
							item.setCurrentPrice(new BigDecimal(con.getProductInfo().getSellPrice()));//销售价
							item.setFlagTheSea(con.getProductInfo().getFlagTheSea());//是否海外购  1代表是  0代表不是
							item.setStockNum("有货");
							List<String> tag = new ArrayList<String>();
							
							// 新版本走分销收益
							if(StringUtils.isBlank(app_vision) || AppVersionUtils.compareTo("5.6.2", getApiClientValue("app_vision")) <= 0) {
								tag.add("收益￥"+con.getProductInfo().getCouponValue()+"元");
							} else {
								tag.add("优惠券￥"+con.getProductInfo().getCouponValue()+"元");
							}
							
							item.setTagList(tag);//标签
							PicInfo imgUrl = ps.getPicInfoOprBig(screenWidth, con.getProductInfo().getMainpicUrl());
							item.setImgUrl(imgUrl==null?"":imgUrl.getPicNewUrl());
							item.setProClassifyTag(con.getProductInfo().getProClassifyTag());
							item.setLabelsList(con.getProductInfo().getLabelsList());
							item.setCouponValue(con.getProductInfo().getCouponValue());
							contentList.add(item);
						}
	        		
	        	}else{
	        		int count=DbUp.upTable("fh_apphome_column_content").dataCount("start_time <='"+systemTime+"' and end_time >'"+systemTime+"' and is_delete = '449746250002' and  column_code ='"+columnCode+"'", null);
		        	totalPage = count/pageSize;
					if(count%pageSize!=0) {totalPage++;}
		        	//List<Map<String, Object>> columnContentMapList = DbUp.upTable("fh_apphome_column_content").dataSqlList("select * from fh_apphome_column_content where start_time <='"+systemTime+"' and end_time >'"+systemTime+"' and is_delete = '449746250002' and  column_code ='"+columnCode+"' order by position asc, start_time desc limit "+start+","+pageSize+"", null);
		        	List<Map<String, Object>> columnContentMapList = DbUp.upTable("fh_apphome_column_content").dataSqlList("SELECT a.* FROM familyhas.fh_apphome_column_content a,productcenter.pc_productinfo b WHERE a.showmore_linkvalue=b.product_code and b.product_status='4497153900060002' and a.start_time<='"+systemTime+"'and a.end_time >'"+systemTime+"' and a.is_delete = '449746250002' and  a.column_code ='"+columnCode+"' "
		        			+ "AND EXISTS (SELECT c.sku_code	FROM systemcenter.sc_store_skunum c	WHERE c.sku_code IN ( SELECT d.sku_code FROM productcenter.pc_skuinfo d WHERE d.product_code = b.product_code and d.sale_yn='Y') AND c.stock_num > 0)  order by a.position asc, a.start_time desc limit "+start+","+pageSize+"",null);
		        	
		        	//List<String> productCodeList= new ArrayList<String>();
		        	for(Map<String, Object> mdataMap : columnContentMapList) {
		    			String productCode = mdataMap.get("showmore_linkvalue").toString();
			        	Item item = new Item();
		    			//常规逻辑，弃用，走搜索逻辑	
						try {
	                           // ProductLabelsService productLabelsService = new ProductLabelsService();
							   ProductLabelService productLabelService = new ProductLabelService();							   
								PlusModelProductInfo plusModelProductinfo = new PlusModelProductInfo(); 
								try {
									plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
								} catch (Exception e) { 
									XmasKv.upFactory(EKvSchema.Product).del(productCode);
									plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
								}
								
								item.setProductCode(productCode);
								item.setProductName(plusModelProductinfo.getProductName());
								item.setOriginalPrice(plusModelProductinfo.getMinSellPrice());//市场价
								item.setCurrentPrice(plusModelProductinfo.getMinSellPrice());//销售价
								item.setFlagTheSea(plusModelProductinfo.getFlagTheSea());//是否海外购  1代表是  0代表不是
								item.setStockNum("有货");
								item.setTagList(ps.getTagListByProductCode(productCode, userCode,channelId));//标签
								
								//524：添加商品分类标签
								PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
								String ssc =productInfo.getSmallSellerCode();
								String st="";
								if("SI2003".equals(ssc)) {st="4497478100050000";}
								else {	st = WebHelper.getSellerType(ssc);	}
								if(StringUtils.isNotBlank(productInfo.getMainpicUrl())) {
									PicInfo imgUrl = ps.getPicInfoOprBig(screenWidth, productInfo.getMainpicUrl());
									item.setImgUrl(imgUrl==null?"":imgUrl.getPicNewUrl());
								}
								//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
								Map<String,String> productTypeMap = WebHelper.getAttributeProductType(st);
								item.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
								// 546添加,返回商品列表标签的宽高
								item.setProClassifyTagH(productTypeMap.get("proTypeListPicHeight").toString());
								item.setProClassifyTagW(productTypeMap.get("proTypeListPicWidth").toString());
								/**添加商品标签**/
								item.setLabelsPic(productLabelService.getLabelInfo(productInfo.getProductCode()).getListPic());
								if (null!=productInfo.getLabelsList() && productInfo.getLabelsList().size()>0) {
									item.setLabelsList(productInfo.getLabelsList());
								}
								contentList.add(item);
							}				
						catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

		    		}
	        	}
	        	
	    		
	    	if(!mDataMap.get("column_type").toString().equals("4497471600010028")) {
	    		//拼团相关
	    		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
	    		if(StringUtils.isEmpty(appVersion)) {
	    			appVersion = "5.4.2";
	    		}
	    		String isMemberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";//用户编号
	    		if(AppVersionUtils.compareTo(appVersion,"5.4.0")>=0){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
	    			for(Item itemEntity : contentList){
	    				String productCode = itemEntity.getProductCode();
	    				//根据商品编号查询商品所参与的活动
	    				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
	    				skuQuery.setCode(productCode);
	    				skuQuery.setMemberCode(userCode);
	    				skuQuery.setChannelId(channelId);
	    				Map<String,PlusModelSkuInfo> map = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
	    				PlusModelSkuInfo skuInfo = map.get(productCode);
	    				if("4497472600010024".equals(skuInfo.getEventType())){//拼团单
	    					itemEntity.setProductType("4497472000050001");
	    					itemEntity.setGroupBuying("4497472600010024");
	    					itemEntity.setGroupBuyingPrice(skuInfo.getGroupBuyingPrice());//设置拼团价
	    					itemEntity.setSkuPrice(skuInfo.getSkuPrice());
	    					String eventCode = skuInfo.getEventCode();
	    					PlusModelEventInfo eventInfo = new PlusSupportEvent().upEventInfoByCode(eventCode);
	    					String collagePersonCount = eventInfo.getCollagePersonCount();//拼团人数
	    					itemEntity.setCollagePersonCount(collagePersonCount);
	    				}else{
	    					itemEntity.setProductType("4497472000050002");//不是拼团单
	    				}
	    				itemEntity.setCurrentPrice(skuInfo.getSellPrice());
	    				//542需求，新增字段展示活动标签
	    				List<String> tags = ps.getTagListByProductCode(productCode,isMemberCode,channelId);
	    				itemEntity.setTagList(tags);
	    				
	    				if(StringUtils.isBlank(appVersion) || AppVersionUtils.compareTo("5.5.80", appVersion) <= 0) {
		    				List<TagInfo> tagInfoList = ps.getProductTagInfoList(productCode, isMemberCode,channelId);
		    				itemEntity.setTagInfoList(tagInfoList);
	    				}
	    			}
	    		}
	    	}
	    		result.setColumnName(columnName);
	    		result.setContentList(contentList);
	        }
		}

		result.setTotalPage(totalPage);
		return result;
	}
}
