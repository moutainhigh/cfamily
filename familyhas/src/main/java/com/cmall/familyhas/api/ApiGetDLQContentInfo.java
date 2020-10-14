package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetDLQContentInfoInput;
import com.cmall.familyhas.api.model.DLQcontent;
import com.cmall.familyhas.api.model.DLQpicListModel;
import com.cmall.familyhas.api.model.DLQpicture;
import com.cmall.familyhas.api.model.DLQshare;
import com.cmall.familyhas.api.result.ApiGetDLQContentInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmasproduct.api.ApiSkuInfo;
import com.srnpr.xmasproduct.model.SkuInfos;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 大陆桥详情信息接口
 * @author fq
 *
 */
public class ApiGetDLQContentInfo extends RootApiForManage<ApiGetDLQContentInfoResult,ApiGetDLQContentInfoInput>{ 

	public ApiGetDLQContentInfoResult Process(ApiGetDLQContentInfoInput inputParam,
			MDataMap mRequestMap) {
		
		ApiGetDLQContentInfoResult result = new ApiGetDLQContentInfoResult();
		List<DLQcontent> contents = new ArrayList<DLQcontent>();
		List<DLQpicture> picList = new ArrayList<DLQpicture>();
		
		//页面编号
		String page_number = inputParam.getPage_number();
		//tv编号
		String tv_number = inputParam.getTv_number();
		if(StringUtils.isEmpty(tv_number) || tv_number.trim().length() <= 0 || "null".equals(tv_number)) {
			tv_number = "";
		}
		MDataMap one = DbUp.upTable("fh_dlq_page").one("page_number",page_number,"page_type",inputParam.getP_type());
		if(null != one) {
			result.setTv_number(tv_number);//增加视频编号
			result.setCuisine_name(one.get("cuisine_name"));
			result.setPage_number(page_number);
			result.setSpecial_name(one.get("special_name"));
			result.setUrl(one.get("url"));
			result.setCuisine_pic(one.get("cuisine_picture"));
			

			//分享信息
			DLQshare share_info = new DLQshare();
			share_info.setAd_name(one.get("special_name"));//暂定为 专题名称（special_name）
			share_info.setShare_content(one.get("share_content"));
			share_info.setShare_pic(one.get("share_img"));
			share_info.setShare_title(one.get("share_title"));
			result.setShare_info(share_info);
				
			
			MDataMap paramMap = new MDataMap();
			paramMap.put("page_number", page_number);
			paramMap.put("delete_state", "1001");
			paramMap.put("tv_number", tv_number);
			paramMap.put("p_type", inputParam.getP_type());
			
			List<MDataMap> contentMapList = DbUp.upTable("fh_dlq_content").queryAll("*", "cast(location as signed)", "page_number=:page_number and delete_state =:delete_state and tv_number=:tv_number ", paramMap);
			LoadProductInfo loadProductInfo = new LoadProductInfo();
			for (int i = 0; i < contentMapList.size(); i++) {
				MDataMap mDataMap = contentMapList.get(i);
				DLQcontent t_content = new DLQcontent();
				String id_number = mDataMap.get("id_number");
				t_content.setPrograma_english(mDataMap.get("programa_english"));
				t_content.setPrograma_name(mDataMap.get("programa_name"));
				t_content.setId_number(id_number);
				if("N1".equals(id_number)) {
					t_content.setFood_name(mDataMap.get("food_name"));
					t_content.setWeight(mDataMap.get("weight"));
				} else if("N2".equals(id_number)) {
					String product_code = mDataMap.get("common_number");
					t_content.setCommon_number(product_code);//商品编号
					//需要获取商品的信息
					PlusModelProductQuery plus = new PlusModelProductQuery(product_code);
					PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
					String productStatus = upInfoByCode.getProductStatus();
					if("4497153900060002".equals(productStatus)) {
						t_content.setProduct_status("1");
					} else {
						t_content.setProduct_status("0");
						continue;//   过滤下架的商品
					}
					t_content.setProduct_name(upInfoByCode.getProductName());
					t_content.setPicture(mDataMap.get("picture"));
					//获取库存
					PlusSupportProduct psp = new PlusSupportProduct();
					long salesNum = 0;
					
					
					 PlusModelProductInfo plusModelProductInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(product_code));
					 List<PlusModelProductSkuInfo> skus  = plusModelProductInfo.getSkuList();
					 
					 List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
					 BigDecimal minPrice=BigDecimal.ZERO;
					 for (int s = 0; s < skus.size(); s++) {
						 PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(skus.get(s).getSkuCode());
						 listPrice.add(upSkuInfoBySkuCode.getSellPrice());
						 salesNum += upSkuInfoBySkuCode.getLimitStock();
					 }
					 
					 if(listPrice!=null && listPrice.size()>1){
						 //价格倒排序
						 Collections.sort(listPrice, new Comparator<BigDecimal>() {
							 public int compare(BigDecimal beginTimeOne, BigDecimal beginTimeTwo) {
								 BigDecimal one =beginTimeOne;
								 BigDecimal two =beginTimeTwo;
								 return one.compareTo(two);
							 }
						 });
						 minPrice=listPrice.get(0);
					 }else if(listPrice!=null && listPrice.size()==1){
						 
						 minPrice=listPrice.get(0);
					 }
					 //--------------------结束------------------------------
					
					/*BigDecimal minPrice = new BigDecimal(0.00);
					for (int j = 0; j < skuList.size(); j++) {
						PlusModelProductSkuInfo plusModelProductSkuInfo = skuList.get(j);
						PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(plusModelProductSkuInfo.getSkuCode());
						salesNum += upSkuInfoBySkuCode.getLimitStock();
						if(j == 0) {
							minPrice = plusModelProductSkuInfo.getSellPrice();
						} else if (minPrice.compareTo(plusModelProductSkuInfo.getSellPrice()) > 0) {
							minPrice = plusModelProductSkuInfo.getSellPrice();
						}
					}*/
					t_content.setSales_num(String.valueOf(salesNum));//商品库存
					DecimalFormat df = new DecimalFormat("#.00");
					t_content.setMark_price(String.valueOf(df.format(upInfoByCode.getMarketPrice())));//市场价
					
					t_content.setSell_price(String.valueOf(df.format(minPrice)));
					
				} else if("N3".equals(id_number)) {
					String picString = mDataMap.get("picture");
					if(!StringUtils.isNotBlank(picString)) {
						continue;//过滤没有上传图片的情况
					}
					t_content.setPicture(mDataMap.get("picture"));
					t_content.setDescribe(mDataMap.get("co_describe"));
					
				} else if("N5".equals(id_number)) {
					String product_code = mDataMap.get("common_number");
					if(!StringUtils.isNotBlank(product_code)) {//过滤无商品编号的选项
						continue;
					}
					t_content.setCommon_number(product_code);
					PlusModelProductQuery plus = new PlusModelProductQuery(product_code);
					PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
					
					t_content.setProduct_name(upInfoByCode.getProductName());
					String productStatus = upInfoByCode.getProductStatus();
					
					if("4497153900060002".equals(productStatus)) {
						t_content.setProduct_status("1");
					} else {
						t_content.setProduct_status("0");
						continue;//   过滤下架的商品
					}
					
					/**
					 * 功能变更，没有图片的取商品主图（列表图）
					 */
					String pic = mDataMap.get("picture");
					if(StringUtils.isNotBlank(pic)) {
						t_content.setPicture(pic);
					} else {
						t_content.setPicture(upInfoByCode.getMainpicUrl());
					}
					
					//获取库存
					PlusSupportProduct psp = new PlusSupportProduct();
					long salesNum = 0;
					
					 PlusModelProductInfo plusModelProductInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(product_code));
					 List<PlusModelProductSkuInfo> skus  = plusModelProductInfo.getSkuList();
					 
					 List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
					 BigDecimal minPrice=BigDecimal.ZERO;
					 for (int s = 0; s < skus.size(); s++) {
						 PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(skus.get(s).getSkuCode());
						 listPrice.add(upSkuInfoBySkuCode.getSellPrice());
						 salesNum += upSkuInfoBySkuCode.getLimitStock();
					 }
					 
					 if(listPrice!=null && listPrice.size()>1){
						 //价格倒排序
						 Collections.sort(listPrice, new Comparator<BigDecimal>() {
							 public int compare(BigDecimal beginTimeOne, BigDecimal beginTimeTwo) {
								 BigDecimal one =beginTimeOne;
								 BigDecimal two =beginTimeTwo;
								 return one.compareTo(two);
							 }
						 });
						 minPrice=listPrice.get(0);
					 }else if(listPrice!=null && listPrice.size()==1){
						 
						 minPrice=listPrice.get(0);
					 }
					t_content.setSales_num(String.valueOf(salesNum));//商品库存
					DecimalFormat df = new DecimalFormat("#.00");
					t_content.setMark_price(String.valueOf(df.format(upInfoByCode.getMarketPrice())));
					t_content.setSell_price(String.valueOf(df.format(minPrice)));
					
					
				} else if("N7".equals(id_number)) {
					//赞助商
					t_content.setPicture(mDataMap.get("picture"));
				} else if("N8".equals(id_number)) {
					t_content.setActivity_code(mDataMap.get("activity_code"));
				}
				
				if("1001".equals(inputParam.getP_type())) {//渠道类型为1001 
					if("N9".equals(id_number)) {//添加本期描述
						String t_describ = mDataMap.get("t_describ");
						if(!StringUtils.isNotBlank(t_describ)) {
							continue;//过滤没有本期描述的数据
						}
						t_content.setT_descb(t_describ);
					} else if("N10".equals(id_number)) {//添加栏目描述
						String column_desc = mDataMap.get("column_desc");
						if(!StringUtils.isNotBlank(column_desc)) {
							continue;//过滤没有栏目描述的数据
						}
						t_content.setColumn_descb(column_desc);
					}
				}
				
				contents.add(t_content);
				
			}
			//广告位
			String systime = DateUtil.getSysDateTimeString();
			List<MDataMap> picMapList = DbUp.upTable("fh_dlq_picture").queryAll("*", "cast(location as signed)", "page_number=:page_number and delete_state =:delete_state and tv_number=:tv_number and id_number = '1000' and start_time<='"+systime+"' and '"+systime+"'<=end_time ", paramMap);
			for (int j = 0; j < picMapList.size(); j++) {
				DLQpicture pcModel = new DLQpicture();
				MDataMap mDataMap2 = picMapList.get(j);
				
				pcModel.setEnd_time(mDataMap2.get("end_time"));
				pcModel.setPic(mDataMap2.get("pic"));
				pcModel.setStart_time(mDataMap2.get("start_time"));
				pcModel.setUrl(mDataMap2.get("url"));
				pcModel.setLocation(mDataMap2.get("location"));
				picList.add(pcModel);
			}
			result.setPicList(picList);
			result.setContents(contents);
			
			//获取推荐列表
			//判断是否有tv编号
			MDataMap tjParamMap = new MDataMap();
			tjParamMap.put("page_number", page_number);
			tjParamMap.put("page_status", "1");
			tjParamMap.put("tv_number", tv_number);
//			String sSql = "SELECT cuisine_name,cuisine_picture,page_number FROM familyhas.fh_dlq_page WHERE state = :state AND page_number IN (" +
//					
//							"SELECT page_number FROM familyhas.fh_dlq_content WHERE tv_number= :tv_number  and page_number != :page_number  GROUP BY page_number" +
//							") ORDER BY zid DESC LIMIT 0,10 ";
			
			String sSql = "SELECT cuisine_name,cuisine_picture,page_number FROM familyhas.fh_dlq_page WHERE page_number IN (" +
					"SELECT page_number FROM familyhas.fh_dlq_status WHERE page_number != :page_number AND tv_number = :tv_number AND  page_status = :page_status" +
					") ORDER BY cast(page_sort as signed) DESC LIMIT 0,10";
			
			List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_dlq_page").dataSqlList(sSql, tjParamMap);
			
			List<DLQpicListModel> recomendList = new ArrayList<DLQpicListModel>();
			for (Map<String, Object> map : dataSqlList) {
				DLQpicListModel dlQpicListModel = new DLQpicListModel();
				dlQpicListModel.setPage_number(String.valueOf(map.get("page_number")));
				dlQpicListModel.setPic_link(String.valueOf(map.get("cuisine_picture")));
				dlQpicListModel.setTitle(String.valueOf(map.get("cuisine_name")));
				recomendList.add(dlQpicListModel);
			}
			
			//添加评论内容
			result.setCommentList(new ApiGetDLQComment().getDLQCommentList(tv_number+"-"+page_number));
			
			
			result.setRecomendList(recomendList);
			
		} else {
			result.setResultCode(0);
			result.setResultMessage("没有页面信息");
		}
		return result;
	}

}
