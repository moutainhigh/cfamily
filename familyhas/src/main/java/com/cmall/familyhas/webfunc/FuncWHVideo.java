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
public class FuncWHVideo extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		try {
			MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			String uid = upSubMap.get("uid");
			String userName =  UserFactory.INSTANCE.create().getRealName();
			String userCode = UserFactory.INSTANCE.create().getUserCode();
			if(upSubMap.get("video_name").length()>50) {
				mResult.setResultCode(0);
				mResult.setResultMessage("视频名称不能超过50字!");
				return mResult;
			}
			if(StringUtils.isNotBlank(upSubMap.get("add_text"))) {
				if(upSubMap.get("add_text").length()>200) {
					mResult.setResultCode(0);
					mResult.setResultMessage("备注字不能数超过200字!");
					return mResult;
				}
			}
			if(StringUtils.isBlank(upSubMap.get("video_type"))) {
					mResult.setResultCode(0);
					mResult.setResultMessage("视频分类不能为空!");
					return mResult;
			}
			if(StringUtils.isBlank(uid)) {
				//添加
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", WebHelper.upUuid());
				paramMap.put("video_name",upSubMap.get("video_name"));
				paramMap.put("video_type",upSubMap.get("video_type"));
				paramMap.put("video_link", upSubMap.get("video_link"));
				paramMap.put("appendix", upSubMap.get("appendix"));
				paramMap.put("add_text", upSubMap.get("add_text"));
				paramMap.put("operator", userCode);
				paramMap.put("operate_time", DateUtil.getNowTime() );
				DbUp.upTable("fh_video").dataInsert(paramMap);
			}
			else {
				//修改
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", uid);
				paramMap.put("video_name",upSubMap.get("video_name"));
				paramMap.put("video_type",upSubMap.get("video_type"));
				paramMap.put("video_link", upSubMap.get("video_link"));
				paramMap.put("appendix", upSubMap.get("appendix"));
				paramMap.put("add_text", upSubMap.get("add_text"));
				paramMap.put("operator", userCode);
				paramMap.put("operate_time", DateUtil.getNowTime() );
				DbUp.upTable("fh_video").dataUpdate(paramMap, "video_name,video_type,operator,operate_time,video_link,appendix,add_text", "uid");
				
			}
			int dataCount = DbUp.upTable("fh_video_operators").dataCount("user_code=:user_code", new MDataMap("user_code",userCode));
			if(dataCount==0) {
				//字典表做入库操作
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", WebHelper.upUuid());
				paramMap.put("user_code",userCode);
				paramMap.put("user_name",userName);
				DbUp.upTable("fh_video_operators").dataInsert(paramMap);
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
