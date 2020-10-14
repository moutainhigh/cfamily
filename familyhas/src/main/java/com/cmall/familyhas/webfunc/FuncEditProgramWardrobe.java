package com.cmall.familyhas.webfunc;

import java.util.List;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditProgramWardrobe extends FuncEdit{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		List<MDataMap> queryAll = DbUp.upTable("fh_program_wardrobe").queryAll("*", "", "", null);
		if(queryAll.size() > 0 ) {
			//修改
			MDataMap one = queryAll.get(0);
			if(null != one) {
				one.put("wardrobe_describe", mDataMap.get("zw_f_describe"));
				one.put("share_content", mDataMap.get("zw_f_share_content"));
				one.put("share_img", mDataMap.get("zw_f_share_img"));
				//one.put("share_link", mDataMap.get("zw_f_share_link"));
				one.put("share_title", mDataMap.get("zw_f_share_title"));
				
				DbUp.upTable("fh_program_wardrobe").update(one);
			}
		} else {
			//添加
			DbUp.upTable("fh_program_wardrobe").insert(
					"wardrobe_describe",mDataMap.get("zw_f_describe"),
					"share_content",mDataMap.get("zw_f_share_content"),
					"share_img",mDataMap.get("zw_f_share_img"),
					//"share_link",mDataMap.get("zw_f_share_link"),
					"share_title",mDataMap.get("zw_f_share_title")
					);
		}
		return new MWebResult();
		
	}
	
}
