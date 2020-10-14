package com.cmall.familyhas.webfunc;

import java.util.Map;

import org.apache.poi.ss.util.ImageUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddMusicTemplatePic  extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		

		String tId = mDataMap.get("zw_f_template_id");
		Map<String, Object> map = DbUp.upTable("hp_music_album_template").dataSqlOne("select preview_img from hp_music_album_template where template_id=:template_id", new MDataMap("template_id",tId));
		MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		MWebResult mResult = new MWebResult();
		upSubMap.put("create_time", DateUtil.getSysDateTimeString());
		upSubMap.put("update_time", DateUtil.getSysDateTimeString());
		upSubMap.put("preview_img", map.get("preview_img").toString()==null?"":map.get("preview_img").toString());
		upSubMap.put("uid", WebHelper.upUuid());
		DbUp.upTable("hp_music_album_template_imgs").dataInsert(upSubMap);
		//mResult = super.funcDo(sOperateUid, mDataMap);
		
		/*
		 * 添加日志
		 */
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String content = "在表《"+mPage.getPageTable()+"》 添加一条记录:"+JSON.toJSONString(upSubMap);
		TempletePageLog.upLog(content);
		
		return mResult;
	}

}
