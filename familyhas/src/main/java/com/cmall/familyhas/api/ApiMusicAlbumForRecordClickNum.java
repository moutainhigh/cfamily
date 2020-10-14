package com.cmall.familyhas.api;

import static org.assertj.core.api.Assertions.doesNotHave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;

import com.cmall.familyhas.api.input.ApiMusicAlbumForRecordClickNumInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 微信小程序-音乐相册 记录广告点击信息
 *
 * 2019/09/25 10:07:00
 *
 * zhangbo
 *
 */

public class ApiMusicAlbumForRecordClickNum
		extends RootApi<RootResult, ApiMusicAlbumForRecordClickNumInput> {

	@Override
	public RootResult Process(ApiMusicAlbumForRecordClickNumInput inputParam,
			MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult result = new RootResult();
		try {
			String advertiseCode = inputParam.getAdvertiseCode();
			String openId = inputParam.getOpenId();
			if(StringUtils.isNotBlank(advertiseCode)) {
				Map<String, Object> map = DbUp.upTable("hp_music_album_adv_click").dataSqlOne("select * from hp_music_album_adv_click where open_id=:open_id and advertise_code=:advertise_code", new MDataMap("advertise_code",advertiseCode,"open_id",openId));
				if(map==null||map.size()==0) {
					MDataMap paramMap = new MDataMap();
					paramMap.put("uid",WebHelper.upUuid());
					paramMap.put("advertise_code", advertiseCode);
					paramMap.put("open_id", openId);
					paramMap.put("create_time",DateUtil.getSysDateTimeString());
					paramMap.put("click_num", "1");
					DbUp.upTable("hp_music_album_adv_click").dataInsert(paramMap);
				}else {
					int clickNum = Integer.parseInt(map.get("click_num").toString());
					clickNum++;
					DbUp.upTable("hp_music_album_adv_click").dataUpdate(new MDataMap("uid",map.get("uid").toString(),"click_num",clickNum+""), "click_num", "uid");
				}
				
				List<Map<String, Object>> list = DbUp.upTable("hp_music_album_adv_click").dataSqlList("select click_num from hp_music_album_adv_click where advertise_code=:advertise_code", new MDataMap("advertise_code",advertiseCode));
				int pvNum = 0;
				int uvNum = 0;
				for (Map<String, Object> map2 : list) {
					uvNum++;
					pvNum=pvNum+Integer.parseInt(map2.get("click_num").toString());
				}
				DbUp.upTable("hp_music_album_adv").dataUpdate(new MDataMap("uv",uvNum+"","pv",pvNum+"","advertise_code",advertiseCode), "uv,pv", "advertise_code");
			}

		} catch (Exception e) {
			// TODO: handle exception
			result.setResultCode(0);
			
		}
	
		return result;
	}
	

}



