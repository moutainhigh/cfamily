package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiHomeNavTabInput;
import com.cmall.familyhas.api.model.HomeNav;
import com.cmall.familyhas.api.result.ApiHomeNavTabResult;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.websupport.ImageMagicSupport;

/**
 * 首页导航栏
 * 
 * @author zhaojunling
 */
public class ApiHomeNavTabList extends RootApiForVersion<ApiHomeNavTabResult, ApiHomeNavTabInput> {

	static final String COLS = "out_channel_id,nav_name,nav_code,position,start_time,end_time,nav_icon,flag,remark,isfx_show,default_flag";
	static final String COLMN_COUNT = "start_time <= :systemTime and end_time > :systemTime and is_delete = '449746250002' "
			+ "and release_flag='449746250001' and nav_code = :nav_code";
	static final String COLMN_CONTENT_COUNT = "start_time <= :systemTime and end_time > :systemTime and column_code =:column_code and is_delete='449746250002'";
	
	ImageMagicSupport ims = new ImageMagicSupport();
	
	@Override
	public ApiHomeNavTabResult Process(ApiHomeNavTabInput inputParam, MDataMap mRequestMap) {
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		String viewType = StringUtils.isBlank(inputParam.getViewType()) ? "4497471600100001" : inputParam.getViewType();
		String outChannelId =inputParam.getOutChannelId();
		// 预览的导航项编码
		String yuLan_navCode = inputParam.getYuLan_navCode();
		String nowTime = FormatHelper.upDateTime();
		boolean isFxUser = false;		
		
		String whereSql = "";
		if("".equals(yuLan_navCode)) {
			whereSql = "release_flag = '01' and is_delete = '02' and seller_code = 'SI2003' and nav_type in ('" + viewType + "','4497471600100005') and start_time <= :nowTime and end_time > :nowTime";
			if(viewType.equals("4497471600100004")){
				if(StringUtils.isNotBlank(userCode)&&DbUp.upTable("fh_agent_member_info").count("member_code",userCode) > 0&&DbUp.upTable("fh_waihu_member_info").count("member_code",userCode) == 0){
					isFxUser = true;
				}
				if(isFxUser){
					whereSql = "release_flag = '01' and is_delete = '02' and seller_code = 'SI2003' and nav_type in ('" + viewType + "') and start_time <= :nowTime and end_time > :nowTime";
				}else{
					whereSql = "release_flag = '01' and is_delete = '02' and seller_code = 'SI2003' and nav_type in ('" + viewType + "') and start_time <= :nowTime and end_time > :nowTime and isfx_show = '449748600001'";
				}
				
			}
			if(StringUtils.isBlank(outChannelId)) {
				//如果入参没有传入公众号，则排除绑定公众号的导航
				whereSql=whereSql+" and out_channel_id='' ";
			}else {
				whereSql=whereSql+" and (out_channel_id='' or out_channel_id='"+outChannelId+"') ";
			}
		}else {
			whereSql = " nav_code = '"+ yuLan_navCode +"' ";
		}
		
		List<MDataMap> homeNavList = DbUp.upTable("fh_apphome_nav").queryAll(COLS, "position asc, create_time desc", whereSql, new MDataMap("nowTime",nowTime));
		
		String appVersion = getApiClient().get("app_vision");
		if(StringUtils.isEmpty(appVersion)) {			
			appVersion = "5.6.4";
		}
		ApiHomeNavTabResult result = new ApiHomeNavTabResult();
		HomeNav navItem = null;
		MDataMap param =  new MDataMap("systemTime", nowTime);
		int[] wh;
		boolean fxFlag = false;
		boolean defaultFlag = false;
		boolean outChannelFlag = false;
		String fxNavCode = "",defaultNavCode = "",outChannelNavCode="";
		for(MDataMap map : homeNavList){
			param.put("nav_code", map.get("nav_code"));
			
			// 忽略无任何栏目的导航
			if(!"".equals(yuLan_navCode) || hasColumnContent(param, appVersion, viewType)){
				navItem = new HomeNav();
				navItem.setIcon(map.get("nav_icon"));
				navItem.setNavCode(map.get("nav_code"));
				navItem.setNavName(StringUtils.trimToEmpty(map.get("nav_name")));
				navItem.setPosition(map.get("position"));
				navItem.setRemark(StringUtils.trimToEmpty(map.get("remark")));
				navItem.setDefaultFlag(map.get("default_flag"));
				navItem.setIsfxShow(StringUtils.trimToEmpty(map.get("isfx_show")));
				navItem.setOutChannelId(StringUtils.trimToEmpty(map.get("out_channel_id")));
				if(navItem.getIsfxShow().equals("449748600002")){
					fxFlag = true;
					if(StringUtils.isBlank(fxNavCode)){
						fxNavCode = navItem.getNavCode();
					}
				}
				if(navItem.getDefaultFlag().equals("449748600002")){
					defaultFlag = true;
					defaultNavCode = navItem.getNavCode();
				}
				if(StringUtils.isNotBlank(outChannelId)&&StringUtils.equals("4497471600100004", viewType)&&outChannelId.equals(navItem.getOutChannelId())) {
					outChannelFlag = true;
					outChannelNavCode = navItem.getNavCode();
				}
				
				if(StringUtils.isNotBlank(navItem.getIcon())){
					wh = getIconWithHeightUseCache(navItem.getIcon());
					navItem.setIconWith(wh[0]);
					navItem.setIconHeight(wh[1]);
				}
				
				result.getHomeNavList().add(navItem);
			}
		}
		if(!defaultFlag && StringUtils.isBlank(defaultNavCode)) {
			if(result.getHomeNavList().size()>0){
				// 如果导航中没有默认选中的,取第一个导航设置为默认选中
				result.getHomeNavList().get(0).setDefaultFlag("449748600002");
				defaultNavCode = result.getHomeNavList().get(0).getNavCode();
			}		
		}
		//选中优先级 ：  分销>公众号渠道>后台配置
		if(outChannelFlag) {
			for(HomeNav info:result.getHomeNavList()){
				if(info.getOutChannelId().equals(outChannelId)){
					info.setDefaultFlag("449748600002");
				}
				if(StringUtils.isNotBlank(defaultNavCode)&&info.getNavCode().equals(defaultNavCode)&&!defaultNavCode.equals(outChannelNavCode)){					
						info.setDefaultFlag("449748600001");
				}
			}
		}
		if(fxFlag){//分销代理默认显示“仅分销用户可见”值为“是”的导航，如果有多个导航，则默认显示排序靠前的导航
			for(HomeNav info:result.getHomeNavList()){
				if(info.getNavCode().equals(fxNavCode)){
					info.setDefaultFlag("449748600002");
				}
				if(StringUtils.isNotBlank(defaultNavCode)&&info.getNavCode().equals(defaultNavCode)){
					if(!defaultNavCode.equals(fxNavCode)) {						
						info.setDefaultFlag("449748600001");
					}
				}
			}
		}
		
		//552版本增加首页滚动消息开关
		result.setScrollSwitch(bConfig("familyhas.home_scroll_message_switch"));
		
		return result;
	}
	
	/**
	 * 检查导航标签页下面是否有可显示的栏目
	 * @param param
	 * @return true 有，false 没有
	 */
	private boolean hasColumnContent(MDataMap param, String appVersion, String viewType){
		List<MDataMap> columnList = DbUp.upTable("fh_apphome_column").queryAll("column_code,column_type", "position asc , create_time desc", COLMN_COUNT, param);
		if(columnList.isEmpty()) return false;
		
		// 564如果导航下展示的是买家秀列表,老版本就不返回该导航栏
		for (MDataMap columnMap : columnList) {
			String columnType = columnMap.get("column_type");
			if(StringUtils.isBlank(columnType)) {
				return false;
			}
			// 如果该首页导航中 商品评价 模板在前面,则展示 商品评价 模板
			if ("4497471600010027".equals(columnType)) {
				// 如果是商品评价栏目类型,查询 首页评价模板表 是否有有效商品数据
				String countSql = "SELECT count(1) num FROM fh_apphome_evaluation WHERE is_delete = '0' AND location_num > 0 ";
				Map<String, Object> countMap = DbUp.upTable("fh_apphome_evaluation").dataSqlOne(countSql, new MDataMap());
				if(countMap != null) {
					int num = MapUtils.getIntValue(countMap, "num");
					if(num > 0) {
						return true;
					}else {
						return false;
					}
				}
			}else if ("4497471600010029".equals(columnType)) {
				String countSql = "SELECT count(1) num FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ";
				Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_info").dataSqlOne(countSql, new MDataMap());
				if(countMap != null) {
					int num = MapUtils.getIntValue(countMap, "num");
					if(num > 0) {
						// 如果买家秀列表在前面,老版本就不返回该导航栏
						if(StringUtils.isNotBlank(appVersion) && AppVersionUtils.compareTo(appVersion, "5.6.4") >= 0) {
							return true;
						}else {					
							return false;
						}
					}else {
						return false;
					}
				}
			}
		}
		
		for(MDataMap columnMap : columnList){
			String columnType = columnMap.get("column_type");
			if(StringUtils.isBlank(columnType)) {
				return false;
			}
			
			if(!columnType.equals("4497471600010019")) {
				param.put("column_code", columnMap.get("column_code"));
				if("4497471600010030".equals(columnType)) {
					// 买家秀入口
					String countSql = "SELECT count(1) num FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ";
					Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_info").dataSqlOne(countSql, new MDataMap());
					if(countMap != null) {
						int num = MapUtils.getIntValue(countMap, "num");
						if(num > 0) {
							// 老版本就不展示买家秀入口
							if(StringUtils.isNotBlank(appVersion) && AppVersionUtils.compareTo(appVersion, "5.6.4") >= 0) {
								return true;
							}
						}
					}
				}else {
					// 其他栏目查询fh_apphome_column_content表
					if(DbUp.upTable("fh_apphome_column_content").dataCount(COLMN_CONTENT_COUNT, param) > 0){
						return true;
					}
				}
			} else {
				if(StringUtils.isNotBlank(appVersion) && AppVersionUtils.compareTo(appVersion, "5.0.2") >= 0) {
					return true;
				} else if(viewType.equals("4497471600100002")) {
					return true;
				} else {
					//appversion少于502不显示导航栏URL
					return false;
				}
			}
		}
		
		return false;
	}
	
	private int[] getIconWithHeightUseCache(String url){
		if(StringUtils.isBlank(url)) return new int[]{10,10};
		int[] wh = {10,10};
		
		// 先取缓存
		String text = XmasKv.upFactory(EKvSchema.ImageWidthHeight).get(url);
		if(StringUtils.isNotBlank(text)){
			String[] whs = text.split("-");
			if(whs.length >= 2){
				wh[0] = NumberUtils.toInt(whs[0]);
				wh[1] = NumberUtils.toInt(whs[1]);
			}
		}
		
		// 缓存不存在则下载图片取宽度
		if(wh[0] == 10 && wh[1] == 10){
			try {
				wh = ims.getImageInfoByUrl(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(wh[0] > 10 || wh[1] > 10){
				XmasKv.upFactory(EKvSchema.ImageWidthHeight).setex(url, NumberUtils.toInt(bConfig("xmassystem.imageWidthHeight")), wh[0]+"-"+wh[1]);
			}
		}
		
		return wh;
	}

}
