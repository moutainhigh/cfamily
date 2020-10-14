package com.cmall.familyhas.webfunc;

import java.util.Map;


import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddMusicTemplate  extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		final int width = 300;
		ProductService productService  = new ProductService();
		
		MWebResult mResult = new MWebResult();
		//mDataMap.put("zw_f_template_id", WebHelper.upCode("100"));
		String template_id = mDataMap.get("zw_f_template_id");
		String template_type_id = mDataMap.get("zw_f_template_type_id");
		int count = DbUp.upTable("hp_music_album_template").count("template_id",template_id,"template_type_id",template_type_id);
		if(count>0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("模板编号在该模板类型下已存在!");
			return mResult;
		}
		mDataMap.put("zw_f_create_time", DateUtil.getSysDateTimeString());
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		
		//对预览图进行压缩处理返回
		String preview_img = mDataMap.get("zw_f_preview_img");
		
		PicInfo info = productService.getPicInfoOprBig(width,preview_img);
		//压缩后的返回值
		String zip_preview_img = info.getPicNewUrl();
		mDataMap.put("zw_f_preview_img", zip_preview_img);
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		
		/*
		 * 添加日志
		 */
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String content = "在表《"+mPage.getPageTable()+"》 添加一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		
		return mResult;
	}

}
