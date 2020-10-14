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
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * @descriptions 获取移动端个人中心按钮数据
 * @date 2017年09月19日上午10:51:39
 * @author Lujunjie
 * @version 1.0.2
 */
public class ApiMyCenterGetBtn extends RootApiForVersion<ApiMyCenterBtnListResult, ApiMyCenterConfigInput> {

	@Override
	public ApiMyCenterBtnListResult Process(ApiMyCenterConfigInput inputParam, MDataMap mRequestMap) {
		String channel_id ="";
		String channelId = getChannelId();
		if("449747430001".equals(channelId) || "449747430003".equals(channelId)) {
			channel_id = "449748610001";
		}else if("449747430023".equals(channelId)) {
			channel_id = "449748610002";
		}else {
			channel_id = "449748610004"; 
		}
		String version = getApiClient().get("app_vision");
		String memberCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		//5.5.4版本添加广告信息
		String adverEntrType = inputParam.getAdverEntrType();
		String sysDateTimeString = DateUtil.getSysDateTimeString();
		// 版本兼容 5.3.4版本开始支持客服与帮助 -rhb 20181204
		String sql = "select config.*, define.define_name type_char from fh_center_config config left join systemcenter.sc_define define "
				+ "on define.define_code = config.type ";
		if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.3.4") >= 0) {
			sql += "where config.type!='449748030004'";
		}else {
			sql += "where config.type!='449748030007'";
		}

		// 只有app有直播入口按钮,并且登录的手机号在主播列表有未结束的直播间
		String liveWhere = " and config.type!='449748030008'";
		if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.5") >= 0) {
			if("449747430001".equals(channelId) && StringUtils.isNotBlank(memberCode)) {
				Map<String, Object> loginNameMap = DbUp.upTable("mc_login_info").dataSqlOne("SELECT login_name FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003' ", new MDataMap());
				if(loginNameMap != null) {
					String login_name = MapUtils.getString(loginNameMap, "login_name");
					String liveSql = "SELECT * FROM lv_live_room WHERE anchor_phone = '"+login_name+"' AND is_delete = '0' AND live_status != '449746320002' ";
					List<Map<String, Object>> liveRoomList = DbUp.upTable("lv_live_room").dataSqlList(liveSql, new MDataMap());
					if(liveRoomList != null && liveRoomList.size() > 0) {
						liveWhere = "";
					}
				}
			}
		}
		sql += liveWhere;
		sql += "order by config.position";
		
		List<Map<String, Object>> list = DbUp.upTable("fh_center_config").upTemplate().queryForList(sql, new MDataMap());
		
		List<ApiMyCenterBtnResult> itemList = new ArrayList<ApiMyCenterBtnResult>();
		for(Map<String, Object> map : list) {
			ApiMyCenterBtnResult item = new ApiMyCenterBtnResult();
			item.setName(MapUtils.getString(map, "name"));
			item.setPosition(MapUtils.getInteger(map, "position"));
			item.setType(MapUtils.getString(map, "type"));
			item.setTypeChar(MapUtils.getString(map, "type_char"));
			item.setImagePath(MapUtils.getString(map, "image_path"));
			item.setUrlAddress(MapUtils.getString(map, "url_address"));
			itemList.add(item);
		}
		
		ApiMyCenterBtnListResult items = new ApiMyCenterBtnListResult();
		if(StringUtils.isNotBlank(adverEntrType)) {
			List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_advert").dataSqlList("select * from fh_advert where start_time<'"+sysDateTimeString+"' and end_time>'"+sysDateTimeString+"' and adver_entry_type=:adver_entry_type and is_release='449746250001' and channel_id in ('"+channel_id+"','449748610004') order by start_time", new MDataMap("adver_entry_type",adverEntrType));
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				Map<String, Object> map = dataSqlList.get(0);
				if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.0") < 0) {
					if(1 == Integer.parseInt(map.get("programa_num").toString())){
						items.getAdverInfo().setAdverEntrType(map.get("adver_entry_type").toString());
						List<Map<String,Object>> dataSqlListColumn = DbUp.upTable("fh_advert_column").dataSqlList("select * from fh_advert_column where advertise_code =:advertise_code", new MDataMap("advertise_code",map.get("advertise_code").toString()));
						for(Map<String,Object> mapCo : dataSqlListColumn) {
							items.getAdverInfo().setImgHeight(Integer.parseInt(mapCo.get("img_height").toString()));
							items.getAdverInfo().setImgWidth(Integer.parseInt(mapCo.get("img_width").toString()));
							items.getAdverInfo().setImgUrl(mapCo.get("img_url").toString());
							String is_share = mapCo.get("is_share").toString();
							if("449746250001".equals(is_share)) {
								is_share = "Y";
							}else if("449746250002".equals(is_share)) {
								is_share = "N";
							}
							items.getAdverInfo().setIsShare(is_share);
							items.getAdverInfo().setShareContent(mapCo.get("share_content").toString());
							items.getAdverInfo().setShareTitle(mapCo.get("share_title").toString());
							items.getAdverInfo().setUrlLink(mapCo.get("link_url").toString());
							items.getAdverInfo().setShareImgUrl(mapCo.get("share_img_url").toString());
						}
					}
				}else {
					items.getAdvert().setAdverEntrType(map.get("adver_entry_type").toString());
					items.getAdvert().setPrograma_num(map.get("programa_num").toString());
					List<Map<String,Object>> dataSqlListColumn = DbUp.upTable("fh_advert_column").dataSqlList("select * from fh_advert_column where advertise_code =:advertise_code", new MDataMap("advertise_code",map.get("advertise_code").toString()));
					for(Map<String,Object> mapCo : dataSqlListColumn) {
						AdvertiseColumnmentInfo co = new AdvertiseColumnmentInfo();
						co.setImgHeight(Integer.parseInt(mapCo.get("img_height").toString()));
						co.setImgWidth(Integer.parseInt(mapCo.get("img_width").toString()));
						co.setImgUrl(mapCo.get("img_url").toString());
						co.setSort_num(Integer.parseInt(mapCo.get("sort_num").toString()));
						co.setJumpType(mapCo.get("link_type").toString());
						co.setUrlLink(mapCo.get("link_url").toString());
						co.setIsShare(mapCo.get("is_share").toString());
						co.setShareContent(mapCo.get("share_content").toString());
						co.setShareTitle(mapCo.get("share_title").toString());
						co.setShareImgUrl(mapCo.get("share_img_url").toString());
						String linkValue = mapCo.get("link_url").toString();
						if("4497471600640001".equals(MapUtils.getString(mapCo,"link_type",""))) {//小程序跳轉
							String links [] = linkValue.split("\\|");
							if(links.length>1) {
								co.setAppId(links[1]);
							}
							if(links.length>0) {
								co.setPath(links[0]);
							}
						}
						items.getAdvert().getAdvertiseColumnmentInfos().add(co);
					}
				}
			}
		}
		items.setBtnList(itemList);
		return items;
	}
}
