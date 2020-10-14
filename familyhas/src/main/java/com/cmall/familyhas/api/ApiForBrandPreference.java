package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBrandPreferenceInput;
import com.cmall.familyhas.api.model.Items;
import com.cmall.familyhas.api.result.ApiForBrandPreferenceResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;


/**
 * 品牌特惠列表接口
 * 
 * @author guz
 *
 */
public class ApiForBrandPreference extends RootApiForMember<ApiForBrandPreferenceResult,ApiForBrandPreferenceInput>{

	public ApiForBrandPreferenceResult Process(
			ApiForBrandPreferenceInput inputParam, MDataMap mRequestMap) {
		ApiForBrandPreferenceResult result = new ApiForBrandPreferenceResult();
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType("4497469400050002");
		}
		result.setSysTime(DateUtil.getNowTime());
		//设置相关信息
		if(result.upFlagTrue()){
			String activity = inputParam.getActivity();
			
			String sSql = "";
			
            MDataMap mDataMap = new MDataMap();
			
			mDataMap.put("info_category", activity);
			mDataMap.put("manage_code",getManageCode());
			
			if(VersionHelper.checkServerVersion("3.5.51.52")){
				sSql = "select photos,info_code,discount_type,link_addr,info_title,up_time,down_time,share_flag,share_img_url,share_title,share_content from fh_brand_preference where flag_show='449746530001' and up_time <= now() and down_time >= now() and manage_code=:manage_code and info_category =:info_category order by sort_num desc,create_time desc";
			}else{
				sSql = "select photos,info_code,discount_type,link_addr,info_title,up_time,down_time,share_img_url,share_title,share_content from fh_brand_preference where flag_show='449746530001' and manage_code=:manage_code and info_category =:info_category  order by sort_num desc";
			}
			
			MDataMap cData = DbUp.upTable("fh_category").one("category_code",activity,"manage_code",getManageCode());
			ProductService productService = new ProductService();
			int picWidth = inputParam.getPicWidth();
			if(cData != null){
				String BannerImg = cData.get("line_head") ==null ? "" : cData.get("line_head");
				if(picWidth > 0 && StringUtils.isNotEmpty(BannerImg)){//对图片进行压缩
					PicInfo picInfo2 = productService.getPicInfoOprBig(picWidth,BannerImg);
					BannerImg = picInfo2.getPicNewUrl();
				}
				//栏目图片地址
				result.setBanner_img(BannerImg);
				//栏目名称
				result.setBanner_name(cData.get("category_name") ==null ? "" : cData.get("category_name"));
				//判断栏目类型
				if("449747030001".equals(cData.get("link_address"))) {
					result.setBanner_link(cData.get("link_url") ==null ? "" : cData.get("link_url"));
				} else {
					result.setBanner_link(cData.get("product_link") ==null ? "" : cData.get("product_link"));
				}
			}
			List <Map<String,Object>>  mList =  DbUp.upTable("fh_brand_preference").dataSqlList(sSql, mDataMap);
			if(mList.size()!=0){
				Map<String, Object> urMapParam = new MObjMap<String, Object>();
				for( int i = 0;i<mList.size();i++){
					urMapParam = mList.get(i);
					Items items = new Items();
					String infoCode = (String)urMapParam.get("info_code");
					String up_time = (String)urMapParam.get("up_time");
					String down_time = (String)urMapParam.get("down_time");
					items.setUpTime(up_time);
					items.setDownTime(down_time);
					items.setInfoCode(infoCode);
//					String discountType = String.valueOf(urMapParam.get("discount_type"));//折扣类型
					String discount = "";
//					if(StringUtils.isNotBlank(discountType) && !"1".equals(discountType)){
//						if("2".equals(discountType)){//x.x折起
////							discount = getProductDiscount(infoCode, "1", inputParam.getBuyerType());
//						}
//					}
					items.setDiscount(discount);
					//添加分享信息
					items.setShareFlag((String)urMapParam.get("share_flag"));
					String shareImgUrl = String.valueOf(urMapParam.get("share_img_url") ==null ? "" : urMapParam.get("share_img_url"));
					if(picWidth > 0 && StringUtils.isNotEmpty(shareImgUrl)){//对图片进行压缩
						PicInfo picInfo = productService.getPicInfoOprBig(picWidth,shareImgUrl);
						items.getShare_info().setShare_img_url(picInfo.getPicNewUrl());
					}
					items.getShare_info().setShare_title((String)urMapParam.get("share_title"));
					items.getShare_info().setShare_content((String)urMapParam.get("share_content"));
					
					String now_time = com.cmall.familyhas.util.DateUtil.getNowTime();
					String picNewUrl = String.valueOf(urMapParam.get("photos") ==null ? "" : urMapParam.get("photos"));
					if(picWidth > 0 && StringUtils.isNotEmpty(picNewUrl)){//对图片进行压缩
						PicInfo picInfo1 = productService.getPicInfoOprBig(picWidth,picNewUrl);
						//图片地址
						picNewUrl = picInfo1.getPicNewUrl();
					}
					if(StringUtils.isEmpty(up_time) && !StringUtils.isEmpty(down_time)){
						//有效时间只有结束时间
						boolean flag = DateUtil.getTimefag(now_time,down_time);
						if(!flag){
							//图片地址
							items.setImg_url(picNewUrl);
							//图片名称
							items.setItem_name(String.valueOf(urMapParam.get("info_title") ==null ? "" : urMapParam.get("info_title")));
							//链接地址
							items.setLink(String.valueOf(urMapParam.get("link_addr") ==null ? "" : urMapParam.get("link_addr")));
							result.getItems().add(items);
						}
						
					}else if(!StringUtils.isEmpty(up_time) && StringUtils.isEmpty(down_time)){
						//有效时间只有开始时间
						boolean flag = DateUtil.getTimefag(now_time,up_time);
						if(flag){
							//图片地址
							items.setImg_url(picNewUrl);
							//图片名称
							items.setItem_name(String.valueOf(urMapParam.get("info_title") ==null ? "" : urMapParam.get("info_title")));
							//链接地址
							items.setLink(String.valueOf(urMapParam.get("link_addr") ==null ? "" : urMapParam.get("link_addr")));
							result.getItems().add(items);
						}
						
					}else if(!StringUtils.isEmpty(up_time) && !StringUtils.isEmpty(down_time)){
						//有效时间有开始和结束时间
						boolean flag1 = DateUtil.getTimefag(now_time,up_time);
						boolean flag2 = DateUtil.getTimefag(now_time,down_time);
						if(flag1 && !flag2){
							//图片地址
							items.setImg_url(picNewUrl);
							//图片名称
							items.setItem_name(String.valueOf(urMapParam.get("info_title") ==null ? "" : urMapParam.get("info_title")));
							//链接地址
							items.setLink(String.valueOf(urMapParam.get("link_addr") ==null ? "" : urMapParam.get("link_addr")));
							result.getItems().add(items);
						}
					}else{//没有有效时间
						//图片地址
						items.setImg_url(picNewUrl);
						//图片名称
						items.setItem_name(String.valueOf(urMapParam.get("info_title") ==null ? "" : urMapParam.get("info_title")));
						//链接地址
						items.setLink(String.valueOf(urMapParam.get("link_addr") ==null ? "" : urMapParam.get("link_addr")));
						result.getItems().add(items);
					}
					
				}
			}
		}
		return result;
	}
	
	/** 
	* @Description:
	* @param infoCode 专题code
	* @param type 1：查询商品最低折扣，2，最高折扣
	* @param buyerType 是否内购会员 4497469400050001:内购会员，4497469400050002:注册会员
	* @author 张海生
	* @date 2015-5-13 上午10:36:13
	* @return String 
	* @throws 
	*/
	public String getProductDiscount(String infoCode, String type, String buyerType) {
		String discount = "";
		MDataMap pMap = new MDataMap();
		pMap.put("info_code", infoCode);
		pMap.put("product_status", "1");
		List<MDataMap> mdataList = DbUp.upTable("pc_brand_rel_product")
				.queryAll("product_code", "", "",pMap);
		if (mdataList != null && mdataList.size() > 0) {
			List<String> pCodeList = new ArrayList<String>();// 用于存放商品code
			for (MDataMap mDataMap : mdataList) {
				pCodeList.add(mDataMap.get("product_code"));
			}
			if ("1".equals(type)) {// 求最低折扣
				Map<String, BigDecimal> productPriceMap = new HashMap<String,BigDecimal>();
				if (VersionHelper.checkServerVersion("3.5.95.55")) {
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(StringUtils.join(pCodeList,","));
					skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
					productPriceMap = new ProductPriceService().
							getProductMinPrice(skuQuery);// 获取商品最低销售价格
				}else{
					productPriceMap = new ProductService().
							getMinProductActivityNew(pCodeList, buyerType);// 获取商品最低销售价格
					
				}
				//过滤未上架的商品  fq++
				List<MDataMap> proList = DbUp.upTable("pc_productinfo").queryIn(
						"product_code,market_price", "", " product_status='4497153900060002' ", new MDataMap(), 0,
						0, "product_code", StringUtils.join(pCodeList, ","));//获取商品市场价
				
//				List<MDataMap> proList = DbUp.upTable("pc_productinfo").queryIn(
//						"product_code,market_price", "", "", new MDataMap(), 0,
//						0, "product_code", StringUtils.join(pCodeList, ","));//获取商品市场价
				
				List<Double> discountList = new ArrayList<Double>();
				for (MDataMap mDataMap : proList) {
					String productCode = mDataMap.get("product_code");
					BigDecimal marktPrice = new BigDecimal(mDataMap.get("market_price"));
					BigDecimal salePrice = productPriceMap.get(productCode);
					if(marktPrice != null&&salePrice!=null && !"0.00".equals(marktPrice.toString())){
						discountList.add(salePrice.multiply(new BigDecimal(10)).divide(marktPrice, 1,
								BigDecimal.ROUND_HALF_UP).doubleValue());// 折扣（四舍五入取一位小数）
					}
				}
				Collections.sort(discountList);
				if(discountList.size()>0) 
					discount = discountList.get(0).toString();
				if("0.0".equals(discount)){
					discount = "";
				}
				if (discount.indexOf(".") > 0) {
					discount = discount.replaceAll("0+?$", "");// 去掉后面无用的零
					discount = discount.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
				}
			}
		}
		return discount;
	}

}
