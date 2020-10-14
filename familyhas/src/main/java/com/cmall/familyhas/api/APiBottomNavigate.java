package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiBottomNavigateInput;
import com.cmall.familyhas.api.model.NewNavigation;
import com.cmall.familyhas.api.model.RedPackageObj;
import com.cmall.familyhas.api.result.APiBottomNavigateResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 首页底部导航接口
 * 
 * @author houwen
 * 
 */
public class APiBottomNavigate extends
		RootApiForVersion<APiBottomNavigateResult, APiBottomNavigateInput> {

	public APiBottomNavigateResult Process(APiBottomNavigateInput inputParam,
			MDataMap mRequestMap) {
			String version = getApiClient().get("app_vision");
			APiBottomNavigateResult result = new APiBottomNavigateResult();
			RedPackageObj rp = new RedPackageObj();
			RedPackageObj rp1 = new RedPackageObj();
			List<NewNavigation> newNavigations = new ArrayList<NewNavigation>();
			
	
			HomeColumnService hcService = new HomeColumnService();
			String navigateVersion = "";
			String firstpageVresion = "";
			String assortmentVersion = "";
			String shoppingcartVersion = "";
			String mineVersion = "";
			String adVersion = "";
			String option = "1"; //服务器没有导航图片，显示默认导航图片
			String adOption = "4"; //服务器没有导航图片，广告导航不显示
			final String CATEGORY = "4497471600020003"; // 链接类型为分类
			String sellerCode = getManageCode();
			
			String releaseFlag = "449746250001"; //是否发布
			List<MDataMap> li = DbUp.upTable("fh_app_navigation").query("channel_limit,channels,zid,navigation_type,nav_code,before_pic,after_pic,operate_time,firstpage_version,assortment_version,shoppingcart_version,mine_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,advertise_version,min_program_id,is_show", "navigation_type asc", "release_flag=:release_flag and start_time<=now() and end_time>now()", new MDataMap("release_flag",releaseFlag), 0, 0);
			if(li!=null&&!li.isEmpty()){
				boolean smallProgressFlag = false;
				for(MDataMap map2 : li){
					
					// 小程序屏蔽广告导航
					/*if("449747430023".equals(getChannelId()) && "4497467900040006".equals(map2.get("navigation_type"))&&"449746250002".equals(map2.get("is_show"))) {
						continue;
					}*/ 
					NewNavigation newNavigation = new NewNavigation();
					String channel_limit = map2.get("channel_limit");
					String channels = map2.get("channels");
					//System.out.println("channel_limit:"+channel_limit +";channels:"+channels+";navigation_type:"+map2.get("navigation_type"));
					if("4497467900040006".equals(map2.get("navigation_type"))&&StringUtils.isNotBlank(channel_limit)&&StringUtils.equals("4497471600070002", channel_limit)) {
						 List<String> channelList = Arrays.asList(StringUtils.split(channels, ","));
						 if(!channelList.contains(getChannelId())) {
							 continue;
						 }else {
							 newNavigation.setIs_show("449746250001");
						 }
					}else if("4497467900040006".equals(map2.get("navigation_type"))&&StringUtils.isNotBlank(channel_limit)&&StringUtils.equals("4497471600070001", channel_limit)) {
						 newNavigation.setIs_show("449746250001");
					}else {
						 newNavigation.setIs_show(map2.get("is_show").toString());
					}
					
					newNavigation.setBeforeFontColor(map2.get("before_fontcolor"));
					newNavigation.setAfterFontColor(map2.get("after_fontcolor"));
					newNavigation.setBeforePicUrl(map2.get("before_pic"));
					
					newNavigation.setAfterPicUrl(map2.get("after_pic"));
					newNavigation.setNavigationName(map2.get("type_name")); //导航名字
					newNavigation.setNavigationType(map2.get("navigation_type")); //导航类型
					newNavigation.setAdNavigationKey(map2.get("showmore_linktype")); //广告导航跳转类型
					
					
					if(StringUtils.isEmpty(newNavigation.getBeforeFontColor()) || StringUtils.isEmpty(newNavigation.getAfterFontColor())) {
						newNavigation.setBeforeFontColor("0e1214");
						newNavigation.setAfterFontColor("f0162b");
					}
					
					// 获取所有要查询的分类编号，key:categoryCode,value:""
				//	MDataMap categoryCodeMap = new MDataMap();
					List<String> categoryCodeArr = new ArrayList<String>();  //如果链接类型是分类的话，返回分类中文名
					if (CATEGORY.equals(map2.get("showmore_linktype"))) {
						String categoryCode = map2.get("showmore_linkvalue");
						if (StringUtils.isNotEmpty(categoryCode)) {
							//categoryCodeMap.put(categoryCode, "");
							categoryCodeArr.add(categoryCode);
						}
					}
				/*	for (String categoryCode : categoryCodeMap.keySet()) {
						categoryCodeArr.add(categoryCode);
					}*/
					MDataMap categoryNameMap = hcService.getCategoryName(categoryCodeArr,sellerCode);

					if (CATEGORY.equals(map2.get("showmore_linktype"))) {
						newNavigation.setNavigationValue(categoryNameMap
								.get(map2.get("showmore_linkvalue")));
					} else {
						newNavigation.setNavigationValue(map2
								.get("showmore_linkvalue"));
					}
					
					if("4497471600020013".equals(map2.get("showmore_linktype"))) {
						
						if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.1") < 0) {
							continue;
						}
						String nav_code = map2.get("nav_code");
						MDataMap one = DbUp.upTable("fh_apphome_nav").one("nav_code",nav_code);
						newNavigation.setNavigationValue(nav_code);
						String nav_name = one.get("nav_name");
						if(StringUtils.isBlank(nav_name)) {
							newNavigation.setNav_name(one.get("remark"));
						}else {
							newNavigation.setNav_name(nav_name);
						}
					}
					
					//newNavigation.setNavigationValue(map2.get("showmore_linkvalue")); //广告导航跳转值4497467900040007
					if(StringUtils.isNotBlank(map2.get("navigation_type")) && map2.get("navigation_type").equalsIgnoreCase("4497467900040007")) {						
						// todo wangmeng start
						if(StringUtils.isNotBlank(channel_limit)&&StringUtils.equals("4497471600070002", channel_limit)) {
							 List<String> channelList = Arrays.asList(StringUtils.split(channels, ","));
							// System.out.println(getChannelId());
							 if(!channelList.contains(getChannelId())) {
								 continue;
							 }else {
								 newNavigation.setIs_show("449746250001");
							 }
						}else if("4497467900040007".equals(map2.get("navigation_type"))&&StringUtils.isNotBlank(channel_limit)&&StringUtils.equals("4497471600070001", channel_limit)) {
							 newNavigation.setIs_show("449746250001");
						}else {
							 newNavigation.setIs_show(map2.get("is_show").toString());
						}
						//end
						
						
						rp.setIsHas(true);
						rp.setLinkType(map2.get("showmore_linktype"));
						rp.setLinkTo(map2.get("showmore_linkvalue"));
						if("4497471600020011".equals(map2.get("showmore_linktype"))) {
							rp.setLinkVal("1");
						}else if("4497471600020001".equals(map2.get("showmore_linktype"))) {
							rp.setLinkVal("2");
						}else if("4497471600020012".equals(map2.get("showmore_linktype"))) {
							smallProgressFlag = true;
							rp.setLinkVal("3");
							rp.setMinProgramId(map2.get("min_program_id"));
						}else if("4497471600020013".equals(map2.get("showmore_linktype"))) {
							/*if("449747430023".equals(getChannelId())) {
								continue;
							}*/
							if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.1") < 0) {
								continue;
							}
							String nav_code = map2.get("nav_code");
							rp.setLinkVal("4");
							rp.setLinkTo(nav_code);
						}else if("4497471600020016".equals(map2.get("showmore_linktype"))&& AppVersionUtils.compareTo(version, "5.6.5") >= 0) {
							//新添直播跳转
							/*if("449747430023".equals(getChannelId())) {
								continue;
							}*/
							if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.1") < 0) {
								continue;
							}
							String nav_code = map2.get("nav_code");
							rp.setLinkVal("5");
							rp.setLinkTo(nav_code);
						}else if("4497471600020017".equals(map2.get("showmore_linktype"))&& AppVersionUtils.compareTo(version, "5.6.5") >= 0) {
							//新添视频跳转
							/*if("449747430023".equals(getChannelId())) {
								continue;
							}*/
							if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.6.1") < 0) {
								continue;
							}
							String nav_code = map2.get("nav_code");
							rp.setLinkVal("6");
							rp.setLinkTo(nav_code);
						}
						rp.setNavigationName(map2.get("type_name"));
						rp.setNavigationType(map2.get("navigation_type"));
						rp.setShowPicURL(map2.get("before_pic"));
						//版本兼容控制
						if(("4497471600020016".equals(map2.get("showmore_linktype"))||"4497471600020017".equals(map2.get("showmore_linktype")))&&AppVersionUtils.compareTo(version, "5.6.5") < 0) {
							rp.setIsHas(false);
						}
					}else if(!"".equals(map2.get("navigation_type")) && "4497467900040008".equals(map2.get("navigation_type"))){
						rp1.setIsHas(true);
						rp1.setLinkType(map2.get("showmore_linktype"));
						rp1.setLinkTo(map2.get("showmore_linkvalue"));
						rp1.setLinkVal("1");
						rp1.setNavigationName(map2.get("type_name"));
						rp1.setNavigationType(map2.get("navigation_type"));
						rp1.setShowPicURL(map2.get("before_pic"));
					}
					else {
						newNavigations.add(newNavigation);
					}					
					
					if(!"".equals(map2.get("navigation_type")) && "4497467900040001".equals(map2.get("navigation_type"))){
						firstpageVresion = map2.get("firstpage_version");
						
					}else if (!"".equals(map2.get("navigation_type")) && "4497467900040002".equals(map2.get("navigation_type"))) {
						assortmentVersion = map2.get("assortment_version");
						
					}else if (!"".equals(map2.get("navigation_type")) && "4497467900040003".equals(map2.get("navigation_type"))) {
						shoppingcartVersion = map2.get("shoppingcart_version");
						
					}else if(!"".equals(map2.get("navigation_type")) && "4497467900040004".equals(map2.get("navigation_type"))){
						mineVersion = map2.get("mine_version");
					}else if(!"".equals(map2.get("navigation_type")) && "4497467900040006".equals(map2.get("navigation_type"))){
						adVersion = map2.get("advertise_version");
					}
					
					}
				
				if(firstpageVresion.equals("") || assortmentVersion.equals("") || shoppingcartVersion.equals("") || mineVersion.equals("")){
					option = "1";
					navigateVersion = firstpageVresion + assortmentVersion + shoppingcartVersion + mineVersion;
				}else{
					navigateVersion = firstpageVresion + assortmentVersion + shoppingcartVersion + mineVersion;
					if(navigateVersion.equals(inputParam.getNavigationVersion())){
						option = "0";
					}else {
						option = "2";
					}
				}
				if(adVersion.equals("")){
					adOption = "4";
				}else{
					if(adVersion.equals(inputParam.getAdNavigationVersion())){
						//adOption = "3";
						adOption = "5"; // 临时更新成5，强制客户端刷新图片
					}else{
						adOption = "5";
					}
				}
				
				result.setOption(option);
				result.setNavigationList(newNavigations);
				result.setNavigationVersion(navigateVersion);
				result.setAdNavigationVersion(adVersion);
				result.setAdOption(adOption);
				if (!smallProgressFlag || (StringUtils.isNotBlank(getApiClient().get("app_vision"))
						&& AppVersionUtils.compareTo(getApiClient().get("app_vision"), "5.5.70") >= 0)) {
					result.setRedPackage(rp);
				}
				result.setSearchRiAdObj(rp1);
			}else {
				result.setOption(option);  //服务器导航图片没有
				result.setNavigationList(newNavigations);
				result.setNavigationVersion(navigateVersion);
				result.setAdNavigationVersion(adVersion);	
				result.setAdOption(adOption);
				result.setRedPackage(rp);
				result.setSearchRiAdObj(rp1);
			}

		return result;
	}

}
