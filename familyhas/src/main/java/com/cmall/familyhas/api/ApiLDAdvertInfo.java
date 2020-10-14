package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiMyCenterConfigInput;
import com.cmall.familyhas.api.result.AdvertiseColumnmentInfo;
import com.cmall.familyhas.api.result.ApiMyCenterBtnListResult;
import com.cmall.familyhas.api.result.ApiMyCenterBtnResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * @descriptions LD短信支付广告信息
 * @date 2017年09月19日上午10:51:39
 * @author Lujunjie
 * @version 1.0.2
 */
public class ApiLDAdvertInfo extends RootApiForVersion<ApiMyCenterBtnListResult, ApiMyCenterConfigInput> {

	@Override
	public ApiMyCenterBtnListResult Process(ApiMyCenterConfigInput inputParam, MDataMap mRequestMap) {
		String adverEntrType = "ADTP002";
		String sysDateTimeString = DateUtil.getSysDateTimeString();
		
		ApiMyCenterBtnListResult items = new ApiMyCenterBtnListResult();
		if(StringUtils.isNotBlank(adverEntrType)) {
			List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_advert").dataSqlList("select * from fh_advert where start_time<'"+sysDateTimeString+"' and end_time>'"+sysDateTimeString+"' and adver_entry_type=:adver_entry_type and is_release='449746250001' and channel_id in ('449748610003','449748610004') order by start_time", new MDataMap("adver_entry_type",adverEntrType));
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				items.getAdvert().setAdverEntrType(map.get("adver_entry_type").toString());
				items.getAdvert().setPrograma_num(map.get("programa_num").toString());
				List<Map<String,Object>> dataSqlListColumn = DbUp.upTable("fh_advert_column").dataSqlList("select * from fh_advert_column where advertise_code =:advertise_code", new MDataMap("advertise_code",map.get("advertise_code").toString()));
				for(Map<String,Object> mapCo : dataSqlListColumn) {
					AdvertiseColumnmentInfo co = new AdvertiseColumnmentInfo();
					co.setImgHeight(Integer.parseInt(mapCo.get("img_height").toString()));
					co.setImgWidth(Integer.parseInt(mapCo.get("img_width").toString()));
					co.setImgUrl(mapCo.get("img_url").toString());
					co.setSort_num(Integer.parseInt(mapCo.get("sort_num").toString()));
					co.setUrlLink(mapCo.get("link_url").toString());
					co.setIsShare(mapCo.get("is_share").toString());
					co.setShareContent(mapCo.get("share_content").toString());
					co.setShareTitle(mapCo.get("share_title").toString());
					co.setShareImgUrl(mapCo.get("share_img_url").toString());
					items.getAdvert().getAdvertiseColumnmentInfos().add(co);
				}
			}
		}
		return items;
	}
}
