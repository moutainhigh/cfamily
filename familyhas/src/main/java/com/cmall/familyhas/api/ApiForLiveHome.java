package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForLiveHomeInput;
import com.cmall.familyhas.api.model.Items;
import com.cmall.familyhas.api.result.ApiForLiveHomeResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
/**
 * 生活家列表接口
 * 
 * @author guz
 *
 */
public class ApiForLiveHome extends RootApiForManage<ApiForLiveHomeResult,ApiForLiveHomeInput>{

	public ApiForLiveHomeResult Process(ApiForLiveHomeInput inputParam,
			MDataMap mRequestMap) {

		ApiForLiveHomeResult result = new ApiForLiveHomeResult();
		//设置相关信息
		if(result.upFlagTrue()){
			
			String activity = inputParam.getActivity();
			
            MDataMap mDataMap = new MDataMap();
			
			mDataMap.put("info_category", activity);
			
			String sSql = "select photos,link_addr,info_title,up_time,down_time,share_img_url,share_title,share_content from fh_brand_preference where flag_show='449746530001' and info_category =:info_category order by sort_num desc";
			
			MDataMap cData = DbUp.upTable("fh_category").one("category_code",activity);
			
			if(cData != null){
				//栏目图片地址
				result.setBanner_img(cData.get("line_head") ==null ? "" : cData.get("line_head"));
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
					String up_time = (String)urMapParam.get("up_time");
					String down_time = (String)urMapParam.get("down_time");
					//添加分享信息
					items.getShare_info().setShare_img_url((String)urMapParam.get("share_img_url"));
					items.getShare_info().setShare_title((String)urMapParam.get("share_title"));
					items.getShare_info().setShare_content((String)urMapParam.get("share_content"));
					
					String now_time = com.cmall.familyhas.util.DateUtil.getNowTime();
					if(StringUtils.isEmpty(up_time) && !StringUtils.isEmpty(down_time)){
						//有效时间只有结束时间
						boolean flag = DateUtil.getTimefag(now_time,down_time);
						if(!flag){
							//图片地址
							items.setImg_url(String.valueOf(urMapParam.get("photos") ==null ? "" : urMapParam.get("photos")));
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
							items.setImg_url(String.valueOf(urMapParam.get("photos") ==null ? "" : urMapParam.get("photos")));
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
							items.setImg_url(String.valueOf(urMapParam.get("photos") ==null ? "" : urMapParam.get("photos")));
							//图片名称
							items.setItem_name(String.valueOf(urMapParam.get("info_title") ==null ? "" : urMapParam.get("info_title")));
							//链接地址
							items.setLink(String.valueOf(urMapParam.get("link_addr") ==null ? "" : urMapParam.get("link_addr")));
							result.getItems().add(items);
						}
					}else{//没有有效时间
						//图片地址
						items.setImg_url(String.valueOf(urMapParam.get("photos") ==null ? "" : urMapParam.get("photos")));
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
