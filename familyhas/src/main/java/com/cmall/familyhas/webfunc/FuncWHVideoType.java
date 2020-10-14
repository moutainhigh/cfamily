package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

import scala.annotation.meta.param;

/**
 * 视频分类维护
 * @author zhangbo
 * 
 * 
 */
public class FuncWHVideoType extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		try {
			MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			String uid = upSubMap.get("uid");
			if(upSubMap.get("type_name").length()>50) {
				mResult.setResultCode(0);
				mResult.setResultMessage("视频分类名称不能超过50字!");
				return mResult;
			}
			if(StringUtils.isBlank(uid)) {
				//添加
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", WebHelper.upUuid());
				paramMap.put("type_code",WebHelper.upCode("VTP") );
				paramMap.put("type_name",upSubMap.get("type_name"));
				paramMap.put("sort", upSubMap.get("sort"));
				paramMap.put("operator",  UserFactory.INSTANCE.create().getRealName());
				paramMap.put("operate_time", DateUtil.getNowTime() );
				DbUp.upTable("fh_video_type").dataInsert(paramMap);
			}
			else {
				//修改
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", uid);
				paramMap.put("type_name",upSubMap.get("type_name"));
				paramMap.put("sort", upSubMap.get("sort"));
				paramMap.put("operator",  UserFactory.INSTANCE.create().getRealName());
				paramMap.put("operate_time", DateUtil.getNowTime() );
				DbUp.upTable("fh_video_type").dataUpdate(paramMap, "type_name,sort,operator,operate_time", "uid");
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception	
			e.printStackTrace();
			mResult.setResultCode(941901133);
			mResult.setResultMessage(bInfo(941901133));
		}
		return mResult;
	}
}
