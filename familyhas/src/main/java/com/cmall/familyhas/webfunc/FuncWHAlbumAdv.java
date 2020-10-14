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
 * 小程序广告配置内容维护
 * @author zhangbo
 * 
 * 
 */
public class FuncWHAlbumAdv extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		try {
			MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			String uid = upSubMap.get("uid");
			String userName =  UserFactory.INSTANCE.create().getRealName();
			String userCode = UserFactory.INSTANCE.create().getUserCode();
			if(StringUtils.isBlank(uid)) {
				//添加
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", WebHelper.upUuid());
				paramMap.put("advertise_code",WebHelper.upCode("ADVE"));
				paramMap.put("advertise_name",upSubMap.get("advertise_name"));
				paramMap.put("advertise_type", upSubMap.get("advertise_type"));
				paramMap.put("position", upSubMap.containsKey("position")?upSubMap.get("position"):"");
				paramMap.put("page_type", upSubMap.get("page_type"));
				paramMap.put("advertise_img", upSubMap.get("advertise_img"));
				paramMap.put("jump_address",upSubMap.get("jump_address"));
				paramMap.put("path",upSubMap.get("path"));
				int dataCount = DbUp.upTable("hp_music_album_adv").dataCount("advertise_type=:advertise_type and position=:position and (page_type=:page_type or page_type='44975022003')", paramMap);
				if(dataCount>0) {
					mResult.setResultCode(0);
					mResult.setResultMessage("存在相同类型,相同位置,相同页面广告！！");
					return mResult;
				}
				DbUp.upTable("hp_music_album_adv").dataInsert(paramMap);
			}
			else {
				//修改
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", uid);
				paramMap.put("advertise_name",upSubMap.get("advertise_name"));
				paramMap.put("advertise_type", upSubMap.get("advertise_type"));
				paramMap.put("position", upSubMap.containsKey("position")?upSubMap.get("position"):"");
				paramMap.put("page_type", upSubMap.get("page_type"));
				paramMap.put("advertise_img", upSubMap.get("advertise_img"));
				paramMap.put("jump_address",upSubMap.get("jump_address"));
				paramMap.put("path",upSubMap.get("path"));
				int dataCount = DbUp.upTable("hp_music_album_adv").dataCount("advertise_type=:advertise_type and  position=:position and (page_type=:page_type or page_type='44975022003') and uid!=:uid ", paramMap);
				if(dataCount>0) {
					mResult.setResultCode(0);
					mResult.setResultMessage("存在相同类型,相同位置,相同页面广告！！");
					return mResult;
				}
				DbUp.upTable("hp_music_album_adv").dataUpdate(paramMap, "advertise_name,advertise_type,position,page_type,advertise_img,jump_address,path", "uid");
				
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
