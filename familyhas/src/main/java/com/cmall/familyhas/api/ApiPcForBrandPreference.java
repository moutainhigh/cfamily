package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBrandPreferenceInput;
import com.cmall.familyhas.api.model.PcItems;
import com.cmall.familyhas.api.result.ApiForPcBrandPreferenceResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;


/**
 * PC品牌特惠列表接口
 * 
 * @author dyc
 *
 */
public class ApiPcForBrandPreference extends RootApiForManage<ApiForPcBrandPreferenceResult,ApiForBrandPreferenceInput>{

	public ApiForPcBrandPreferenceResult Process(
			ApiForBrandPreferenceInput inputParam, MDataMap mRequestMap) {
		ApiForPcBrandPreferenceResult result = new ApiForPcBrandPreferenceResult();
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType("4497469400050002");
		}
		result.setSysTime(DateUtil.getNowTime());
		//设置相关信息
		if(result.upFlagTrue()){
			String activity = inputParam.getActivity();
			
			String sSql = "SELECT f.photos,f.info_code,f.discount_type,f.link_addr,f.info_title,f.up_time,f.down_time,f.share_flag,f.share_img_url,f.share_title,f.share_content,p.brand_code,p.brand_name,p.brand_pic,p.brand_note FROM (select photos,info_code,discount_type,link_addr,info_title,up_time,down_time,share_flag,share_img_url,share_title,share_content,brand_code,sort_num,create_time from fh_brand_preference where flag_show='449746530001' and up_time <= now() and down_time >= now() and manage_code=:manage_code and info_category =:info_category) f LEFT JOIN productcenter.pc_brandinfo p ON f.brand_code = p.brand_code order by f.sort_num desc,f.create_time desc";
			
            MDataMap mDataMap = new MDataMap();
			
			mDataMap.put("info_category", activity);
			mDataMap.put("manage_code",getManageCode());
			
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
					PcItems items = new PcItems();
					items.setBrandCode(urMapParam.get("brand_code")==null?"":urMapParam.get("brand_code").toString());
					items.setBrandName(urMapParam.get("brand_name")==null?"":urMapParam.get("brand_name").toString());
					items.setBrandPic(urMapParam.get("brand_pic")==null?"":urMapParam.get("brand_pic").toString());
					items.setBranddesc(urMapParam.get("brand_note")==null?"":urMapParam.get("brand_note").toString());
					String infoCode = (String)urMapParam.get("info_code");
					String up_time = (String)urMapParam.get("up_time");
					String down_time = (String)urMapParam.get("down_time");
					items.setUpTime(up_time);
					items.setDownTime(down_time);
					items.setInfoCode(infoCode);
					String discountType = String.valueOf(urMapParam.get("discount_type"));//折扣类型
					String discount = "";
					if(StringUtils.isNotBlank(discountType) && !"1".equals(discountType)){
						if("2".equals(discountType)){//x.x折起
							discount = new ApiForBrandPreference().getProductDiscount(infoCode, "1", inputParam.getBuyerType());
							if(StringUtils.isBlank(discount)) {
								continue;
							}
						}
					}
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
	
	

}
