package com.cmall.familyhas.webfunc;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditMusicTemplatePic extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		

		MWebResult mResult = new MWebResult();
		MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		//压缩后的返回值
		upSubMap.put("update_time", DateUtil.getSysDateTimeString());
	    DbUp.upTable("hp_music_album_template_imgs").dataUpdate(upSubMap, "main_img,img_sort", "uid");
		
		//mResult = super.funcDo(sOperateUid, mDataMap);
		String content = "在表《"+mPage.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(upSubMap);
		TempletePageLog.upLog(content);
		return mResult;
	}
	
	
	

}
