package com.cmall.familyhas.api;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiMusicTemplateListInput;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.WXDdfaultTemplateImgs;
import com.srnpr.zapweb.webapi.WXMusicTemplateResult;
import com.srnpr.zapweb.webapi.WXTemplateTypeInfos;

/**
 *微信小程序-音乐相册  模板列表接口
 *
 *2019/07/26 20:51:00
 *
 *zhangbo
 *
 */

public class ApiMusicAlbumTemplateList extends RootApi<WXMusicTemplateResult, ApiMusicTemplateListInput>{

	@Override
	public WXMusicTemplateResult Process(ApiMusicTemplateListInput inputParam, MDataMap mRequestMap) {
		
		// TODO Auto-generated method stub
		WXMusicTemplateResult wxMusicTemplateResult = new WXMusicTemplateResult();
		int pageSize = inputParam.getPageSize();
		int nextPage = inputParam.getNextPage();
		int nowPage = nextPage;
		int start = pageSize*(nextPage-1);
		int totalPage = 0;
		String page_type = inputParam.getPage_type();
		String template_type_id = inputParam.getTemplate_type_id();
		if("1".equals(page_type)) {
			
		}
		else if("2".equals(page_type)){
			
		}
		List<Map<String,Object>> dataSqlList = new ArrayList<Map<String,Object>>();
		//暂无多模板分类，只是一个
		if(StringUtils.isBlank(template_type_id)) {
			 dataSqlList = DbUp.upTable("hp_music_album_template_type").dataSqlList("select * from hp_music_album_template_type where use_flag='Y' order by update_time desc", null);
		}
		else {
			dataSqlList = DbUp.upTable("hp_music_album_template_type").dataSqlList("select * from hp_music_album_template_type where use_flag='Y' and template_type_id=:template_type_id order by update_time desc", new MDataMap("template_type_id",template_type_id));
		}
		
		if(dataSqlList!=null&&dataSqlList.size()>0) {
			Map<String, Object> map = dataSqlList.get(0);
			
			int count = DbUp.upTable("hp_music_album_template").count("template_type_id",map.get("template_type_id").toString(),"use_flag","Y");
			if(nextPage<0) return  wxMusicTemplateResult;
			totalPage = count/pageSize;
			if(count%pageSize!=0) {totalPage++;}
				
			wxMusicTemplateResult.setTemplate_type_id(map.get("template_type_id").toString());
			wxMusicTemplateResult.setTemplate_type_nm(map.get("template_type_nm").toString());
			List<WXTemplateTypeInfos> template_type_infos = wxMusicTemplateResult.getTemplate_type_infos();
			
			List<Map<String,Object>> dataSqlList2 = DbUp.upTable("hp_music_album_template").dataSqlList("select * from hp_music_album_template where use_flag='Y' and template_type_id=:template_type_id  order by template_sort asc limit "+start+","+pageSize+" ", new MDataMap("template_type_id",map.get("template_type_id").toString()));
			for (Map<String, Object> map2 : dataSqlList2) {
				WXTemplateTypeInfos templateTypeInfo = new WXTemplateTypeInfos();
				templateTypeInfo.setTemplate_title(map2.get("template_title").toString());
				templateTypeInfo.setDefault_template_yn(map2.get("default_template_yn").toString());
				templateTypeInfo.setOpen_title(map2.get("open_title").toString());
				templateTypeInfo.setTemplate_id(Integer.parseInt(map2.get("template_id").toString()));
				templateTypeInfo.setTemplate_preview_img(map2.get("preview_img").toString());
				templateTypeInfo.setTemplate_sort(Integer.parseInt(map2.get("template_sort").toString()));
				templateTypeInfo.setMax_up_imgs(Integer.parseInt(map2.get("max_up_imgs").toString()));
				List<WXDdfaultTemplateImgs> default_template_imgs = templateTypeInfo.getDefault_template_imgs();
				List<Map<String,Object>> dataSqlList3 = DbUp.upTable("hp_music_album_template_imgs").dataSqlList("select * from hp_music_album_template_imgs where template_id=:template_id", new MDataMap("template_id",map2.get("template_id").toString()));	
                if(dataSqlList3!=null&&dataSqlList3.size()>0) {
                	for (Map<String, Object> map3 : dataSqlList3) {
                		WXDdfaultTemplateImgs wxDdfaultTemplateImgs = new WXDdfaultTemplateImgs();
                    	wxDdfaultTemplateImgs.setImg_sort(Integer.parseInt(map3.get("img_sort").toString()));
                    	wxDdfaultTemplateImgs.setImg_url(map3.get("main_img").toString());
                    	default_template_imgs.add(wxDdfaultTemplateImgs);
					}
                	
                }
                template_type_infos.add(templateTypeInfo);  
			}
			
			
		}

		wxMusicTemplateResult.setCountPage(totalPage);
		wxMusicTemplateResult.setNowPage(nowPage);
		wxMusicTemplateResult.setResultCode(1);
		
		return wxMusicTemplateResult;
	}
	

	

	

}
