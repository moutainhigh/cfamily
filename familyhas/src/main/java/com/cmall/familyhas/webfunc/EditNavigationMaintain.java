package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改导航类型
 * @author liqt
 *
 */
public class EditNavigationMaintain extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap maddMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		//当前登录人
		String operateMember = UserFactory.INSTANCE.create().getLoginName();
		//当前时间
		String operateTime = DateUtil.getNowTime();
		String startTime = maddMap.get("start_time");
		String endTime = maddMap.get("end_time");
		if(!DateUtil.getTimefag(endTime, startTime)){
			mResult.inErrorMessage(916401201);// 开始时间必须小于结束时间
			return mResult;
		}else if(!DateUtil.getTimefag(endTime, operateTime)){
			mResult.inErrorMessage(916401214);// 当前时间必须小于结束时间
			return mResult;
		}
		MDataMap mDataMap2 = new MDataMap();
		mDataMap2.put("uid", maddMap.get("uid"));
		mDataMap2.put("navigation_type", maddMap.get("navigation_type"));
		mDataMap2.put("before_pic", maddMap.get("before_pic"));
		mDataMap2.put("after_pic", maddMap.get("after_pic"));
		mDataMap2.put("start_time", startTime);
		mDataMap2.put("end_time", endTime);
		mDataMap2.put("operate_member", operateMember);
		mDataMap2.put("operate_time", operateTime);
		mDataMap2.put("type_name", maddMap.get("type_name"));
		mDataMap2.put("before_fontcolor", maddMap.get("before_fontcolor"));
		mDataMap2.put("after_fontcolor", maddMap.get("after_fontcolor"));
		mDataMap2.put("channel_limit", StringUtils.isBlank(maddMap.get("channel_limit"))?"":maddMap.get("channel_limit").toString());
		mDataMap2.put("channels", StringUtils.isBlank(maddMap.get("channels"))?"":maddMap.get("channels").toString());
		if(StringUtils.equals(mDataMap2.get("channel_limit"), "4497471600070001")) {
			mDataMap2.put("channels","");
		}
		
		if(!"4497467900040009".equals(maddMap.get("navigation_type"))
				&&!"4497467900040010".equals(maddMap.get("navigation_type"))
				&&!"4497467900040011".equals(maddMap.get("navigation_type"))) {
			mDataMap2.put("showmore_linktype", maddMap.get("showmore_linktype"));
			mDataMap2.put("showmore_linkvalue", maddMap.get("showmore_linkvalue"));
		}
		
		if("4497467900040007".equals(maddMap.get("navigation_type"))
				&& "4497471600020012".equals(maddMap.get("showmore_linktype"))) {
			mDataMap2.put("min_program_id", maddMap.get("min_program_id"));
		}else {
			mDataMap2.put("min_program_id", "");
		}
		if("4497471600020013".equals(StringUtils.trimToEmpty(maddMap.get("showmore_linktype")))) {
			String nav_code = StringUtils.trimToEmpty(maddMap.get("nav_code"));
			mDataMap2.put("nav_code", nav_code);
			MDataMap one = DbUp.upTable("fh_apphome_nav").one("nav_code",nav_code);
			String nav_icon = one.get("nav_icon");
			if(StringUtils.isBlank(nav_icon)) {
				mDataMap2.put("showmore_linkvalue",one.get("nav_name"));
			}else {
				String nav_name = MapUtils.getString(one, "nav_name", "");
				if(StringUtils.isBlank(nav_name)) {
					mDataMap2.put("showmore_linkvalue",one.get("remark"));
				}else {
					mDataMap2.put("showmore_linkvalue",nav_name);
				}
			}
		}else {
			mDataMap2.put("nav_code", "");
		}
		MDataMap mDataMap3 = DbUp.upTable("fh_app_navigation").oneWhere("release_flag", "", "", "uid",maddMap.get("uid"));
		String channel_limit = maddMap.get("channel_limit");
		String channels = maddMap.get("channels");
		if("4497467900040001".equals(maddMap.get("navigation_type"))){
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040001"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("firstpage_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					mDataMap2.put("advertise_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,firstpage_version,assortment_version,shoppingcart_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id",
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("firstpage_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				mDataMap2.put("advertise_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,firstpage_version,assortment_version,shoppingcart_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
			
			
		}else if ("4497467900040002".equals(maddMap.get("navigation_type"))) {
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040002"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					
					mDataMap2.put("assortment_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version", "");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					mDataMap2.put("advertise_version","");
					
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,assortment_version,firstpage_version,shoppingcart_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("assortment_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version", "");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				mDataMap2.put("advertise_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,assortment_version,firstpage_version,shoppingcart_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
			
			
		}else if ("4497467900040003".equals(maddMap.get("navigation_type"))) {
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040003"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("shoppingcart_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					mDataMap2.put("advertise_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,shoppingcart_version,firstpage_version,assortment_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("shoppingcart_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				mDataMap2.put("advertise_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,shoppingcart_version,firstpage_version,assortment_version,mine_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
			
		}else if("4497467900040004".equals(maddMap.get("navigation_type"))){
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040004"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("mine_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("background_version","");
					mDataMap2.put("advertise_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,mine_version,firstpage_version,assortment_version,shoppingcart_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("mine_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("background_version","");
				mDataMap2.put("advertise_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,mine_version,firstpage_version,assortment_version,shoppingcart_version,background_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
			
		}else if("4497467900040005".equals(maddMap.get("navigation_type"))){
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040005"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("background_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("advertise_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,background_version,mine_version,firstpage_version,assortment_version,shoppingcart_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("background_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("advertise_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,background_version,mine_version,firstpage_version,assortment_version,shoppingcart_version,advertise_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
		}else if("4497467900040006".equals(maddMap.get("navigation_type"))){
			mDataMap2.put("is_show", maddMap.get("is_show"));
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("channels,channel_limit,navigation_type,start_time,end_time,uid,channel_limit,channels", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040006"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						//时间重叠，判断渠道是否相同
						if("4497471600070002".equals(channel_limit)&&"4497471600070002".equals(m.get("channel_limit"))) {
							//导航类型绑定的不同渠道，同样可以发布
							List<String> asList = new ArrayList<String>(Arrays.asList(channels.split(",")));
							List<String> asList2 = new ArrayList<String>(Arrays.asList(m.get("channels").split(",")));
							asList.retainAll(asList2);
							if(asList.size()>0) {
								flag=false;
								break;
							}else {
								continue;
							}
						}else {
							flag=false;
							break;
						}
					}
				}
				if(flag){
					mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"channels,channel_limit,navigation_type,nav_code,is_show,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"channels,channel_limit,navigation_type,is_show,nav_code,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
		}
		else if("4497467900040007".equals(maddMap.get("navigation_type"))){
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid,channel_limit,channels", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040007"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						//时间重叠，判断渠道是否相同
						if("4497471600070002".equals(channel_limit)&&"4497471600070002".equals(m.get("channel_limit"))) {
							//导航类型绑定的不同渠道，同样可以发布
							List<String> asList = new ArrayList<String>(Arrays.asList(channels.split(",")));
							List<String> asList2 = new ArrayList<String>(Arrays.asList(m.get("channels").split(",")));
							asList.retainAll(asList2);
							if(asList.size()>0) {
								flag=false;
								break;
							}else {
								continue;
							}
						}else {
							flag=false;
							break;
						}
						
					}
				}
				if(flag){
					mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, "nav_code,navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id,channel_limit,channels",
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, "nav_code,navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id,channel_limit,channels",
						"uid");
			}
		}
		else if("4497467900040008".equals(maddMap.get("navigation_type"))){
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040008"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
						"uid");
			}
		}else if("4497467900040009".equals(maddMap.get("navigation_type")) || "4497467900040010".equals(maddMap.get("navigation_type")) || "4497467900040011".equals(maddMap.get("navigation_type"))) {
			if ("449746250001".equals(mDataMap3.get("release_flag"))){//如果已发布
				List<MDataMap> mList = DbUp.upTable("fh_app_navigation").queryAll("navigation_type,start_time,end_time,uid", "", "", new MDataMap("release_flag","449746250001","navigation_type","4497467900040008"));
				Boolean flag = true;
				//关于时间的一些规则
				for(MDataMap m : mList){
					if(m.get("uid").equals(maddMap.get("uid"))){
						continue;
					}
					if(DateUtil.getTimefag(startTime, m.get("end_time")) || DateUtil.getTimefag( m.get("start_time"),endTime)){//如果相同类型时间没有重复
						flag=true;
					}else{
						flag=false;
					}
				}
				if(flag){
					mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
					mDataMap2.put("firstpage_version","");
					mDataMap2.put("assortment_version","");
					mDataMap2.put("shoppingcart_version","");
					mDataMap2.put("mine_version","");
					mDataMap2.put("background_version","");
					DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
							"navigation_type,before_pic,after_pic,start_time,end_time,operate_member,operate_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,type_name,before_fontcolor,after_fontcolor,showmore_linktype,showmore_linkvalue,min_program_id", 
							"uid");
				}else{
					mResult.inErrorMessage(916421191);// 相同类型中有时间重叠的部分，请核对后重新修改
				}
			}else {
				mDataMap2.put("advertise_version", KvHelper.upCode("stVersion"));
				mDataMap2.put("firstpage_version","");
				mDataMap2.put("assortment_version","");
				mDataMap2.put("shoppingcart_version","");
				mDataMap2.put("mine_version","");
				mDataMap2.put("background_version","");
				DbUp.upTable("fh_app_navigation").dataUpdate(mDataMap2, 
						"navigation_type,before_pic,start_time,end_time,advertise_version,firstpage_version,assortment_version,mine_version,shoppingcart_version,background_version,min_program_id", 
						"uid");
			}
		}

		return mResult;
	}
	
}
