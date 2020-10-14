package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditMusicTemplate extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		final int width = 300;
		ProductService productService  = new ProductService();
		
		MWebResult mResult = new MWebResult();
		
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		//对预览图进行压缩处理返回
		String preview_img = mDataMap.get("zw_f_preview_img");
		PicInfo info = productService.getPicInfoOprBig(width,preview_img);
		//压缩后的返回值
		String zip_preview_img = info.getPicNewUrl();
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		mDataMap.put("zw_f_preview_img", zip_preview_img);
		
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		String content = "在表《"+mPage.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		return mResult;
	}
	
	
	

}
