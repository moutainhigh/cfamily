package com.cmall.familyhas.webfunc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ImageMagicSupport;

 
/**
 * @description: 修改导航信息
 *
 * @author Yangcl
 * @date 2017年5月3日 下午2:10:33 
 * @version 1.0.0
 */
public class FhApphomeNavEdit extends FuncEdit {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String navName = mDataMap.get("zw_f_nav_name");
		String navIcon = mDataMap.get("zw_f_nav_icon");
		if(StringUtils.isBlank(navName) && StringUtils.isBlank(navIcon)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("【导航名称】和【小图标】不可同时为空");  // bInfo(941901133)
			return mResult;
		}
		if(navName.length() > 5){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("导航名称建议文字2-5个字符");  // bInfo(941901133)
			return mResult;
		}
		if(StringUtils.isNotBlank(navIcon)){
			ImageMagicSupport ms = new ImageMagicSupport();
			Map<String , String> map = ms.getImageSizeByUrl(navIcon);
			Double size = Double.valueOf(map.get("size"));
			if(size > 5){
				mResult.setResultCode(941901133);
				mResult.setResultMessage("小图标不得大于5Kb");  // bInfo(941901133)
				return mResult;
			}
			
			if(StringUtils.isNotBlank(navName) && StringUtils.isNotBlank(navIcon)){
				Integer width = Integer.valueOf(map.get("width"));
				Integer height = Integer.valueOf(map.get("height"));
				if(navName.length() == 2){  // 添加2个字时、图片尺寸159*114 
					if(width != 159 && height != 114){
						mResult.setResultCode(941901133);
						mResult.setResultMessage("添加2个字时、图片尺寸159*114");   
						return mResult;
					}
				}else if(navName.length() == 3){  // 添加3个字时、图片尺寸198*114 
					if(width != 198 && height != 114){
						mResult.setResultCode(941901133);
						mResult.setResultMessage("添加3个字时、图片尺寸198*114");   
						return mResult;
					}
				}else if(navName.length() == 4){  // 添加4个字时、图片尺寸240*114 
					if(width != 240 && height != 114){
						mResult.setResultCode(941901133);
						mResult.setResultMessage("添加4个字时、图片尺寸240*114");   
						return mResult;
					}
				}else if(navName.length() == 5){  // 添加5个字时、图片尺寸285*114 
					if(width != 285 && height != 114){
						mResult.setResultCode(941901133);
						mResult.setResultMessage("添加5个字时、图片尺寸285*114");   
						return mResult;
					}
				}
			}
			
			if(StringUtils.isBlank(navName) && StringUtils.isBlank(mDataMap.get("zw_f_remark"))){
				mResult.setResultCode(941901133);
				mResult.setResultMessage("当导航名称为空且仅显示小图标时【导航备注名称】不得为空");   
				return mResult;
			}
		}
		
		
		String position = mDataMap.get("zw_f_position");
		if( !this.isNumeric(String.valueOf(position))){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("在【位置】输入时包含非整数类型，请检查");  // bInfo(941901133)
			return mResult;
		}
		if( !this.isPositiveNumber(position)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("【位置】输入值应为正数");  // bInfo(941901133)
			return mResult;
		}
		
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if(StringUtils.isBlank(startTime) ||StringUtils.isBlank(endTime)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("开始时间或结束时间字段不得为空");  // bInfo(941901133)
			return mResult;
		}
		if(!this.compareDate(startTime , endTime)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("开始时间不得大于结束时间，请检查");  // bInfo(941901133)
			return mResult;
		}
		
		if(mDataMap.get("zw_f_class_type").equals("4497480100040003") && StringUtils.isBlank(mDataMap.get("zw_f_key_word"))){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("关键词不得为空!");  // bInfo(941901133)
			return mResult;
		}
		if(!mDataMap.get("zw_f_class_type").equals("4497480100040003")){
			mDataMap.put("zw_f_key_word" , ""); // 如果不是关键词则将其置空
		}
		
		String defaultFlag = mDataMap.get("zw_f_default_flag");
		if("449748600002".equals(defaultFlag)) {
			String navType = mDataMap.get("zw_f_nav_type");
			String sql = "SELECT nav_code,nav_name,remark FROM fh_apphome_nav WHERE is_delete='02' AND default_flag='449748600002' AND nav_type in (";
			if("4497471600100001".equals(navType)) {
				sql += "'4497471600100001','4497471600100005'";
			}else if("4497471600100002".equals(navType)) {
				sql += "'4497471600100002','4497471600100005'";
			}else if("4497471600100004".equals(navType)) {
				sql += "'4497471600100004'";
			}else {
				sql += "'4497471600100001','4497471600100002','4497471600100005'";
			}
			sql += ") AND (start_time <=:end_time OR end_time >=:start_time)";
			sql += " AND uid != '"+mDataMap.get("zw_f_uid")+"'";
			
			List<Map<String, Object>> list = DbUp.upTable("fh_apphome_nav").dataSqlList(sql, new MDataMap("start_time", startTime, "end_time", endTime));
			if(null != list && list.size() > 0) {
				String nav_name = MapUtils.getString(list.get(0), "nav_name").isEmpty() ? MapUtils.getString(list.get(0), "remark") : MapUtils.getString(list.get(0), "nav_name");
				mResult.setResultCode(941901133);
				mResult.setResultMessage("【" + nav_name + "】已被设置成居中显示!");
				return mResult;
			}
		}
		
		//添加公众号绑定判断
		if("4497471600100004".equals( mDataMap.get("zw_f_nav_type")) && StringUtils.isNotBlank(mDataMap.get("zw_f_out_channel_id"))) {
			String sql = "SELECT nav_code,nav_name,remark FROM fh_apphome_nav WHERE is_delete='02'  AND nav_type ='4497471600100004' and out_channel_id=:out_channel_id ";
			sql += " AND ((end_time <=:end_time and end_time >=:start_time) or (start_time <=:end_time and start_time >=:start_time) or (end_time >=:end_time and start_time <=:start_time))";
			sql += " AND uid != '"+mDataMap.get("zw_f_uid")+"'";
			
			List<Map<String, Object>> list = DbUp.upTable("fh_apphome_nav").dataSqlList(sql, new MDataMap("start_time", startTime, "end_time", endTime,"out_channel_id",mDataMap.get("zw_f_out_channel_id")));
			if(null != list && list.size() > 0) {
				String nav_name = MapUtils.getString(list.get(0), "nav_name").isEmpty() ? MapUtils.getString(list.get(0), "remark") : MapUtils.getString(list.get(0), "nav_name");
				mResult.setResultCode(941901133);
				mResult.setResultMessage("【" + nav_name + "】已绑定此公众号！");
				return mResult;
			}
		}
		
		String categoryLimit = mDataMap.get("zw_f_category_limit");
		if("449748560002".equals(categoryLimit) && StringUtils.isEmpty(mDataMap.get("zw_f_category_codes"))) {
			mResult.setResultCode(941901133);
			mResult.setResultMessage("请勾选【分类限制】的分类！");
			return mResult;
		}
		
		mDataMap.put("zw_f_update_time", DateHelper.upDate(new Date()));
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_update_user", create_user); 
		
//		String min_use = mDataMap.get("min_use");
//		updateMap.put("zid", mDataMap.get("zid")); 
//		updateMap.put("uid", mDataMap.get("uid")); 
//		DbUp.upTable("fh_hd_consume_config").update(updateMap);
		 
		super.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
	
	/**
	 * @description: 查库，把值返回给编辑页面|ftl模板只能支持String类型的返回值 
	 *
	 * @author Yangcl
	 * @date 2016年12月14日 上午10:27:07 
	 * @version 1.0.0.1
	 */
	public String getEntityByUid(String uid){
		MDataMap map = DbUp.upTable("fh_hd_consume_config").one("uid", uid);
		if(map != null && map.size() != 0){
			map.put("flag", "success");
		}else{
			map.put("flag", "error");
		}
		return JSONObject.toJSONString(map); 
	}
	
	private boolean isNumeric(String str){  
	   for(int i=str.length();--i>=0;){  
	      int chr=str.charAt(i);  
	      if(chr<48 || chr>57)  
	         return false;  
	   }  
	   return true;  
	} 
	
	/**
	 * @description: 判断正数，如果为小数则保留两位小数|0不是正数 
	 *
	 * @author Yangcl
	 * @date 2016年12月15日 上午10:28:44 
	 * @version 1.0.0.1
	 */
	private boolean isPositiveNumber(String str){
		boolean flag = true;
		if(str.matches("^\\d+(\\.\\d+)?$")){
			if(Double.valueOf(str) == 0){
				return false;
			}
			if(StringUtils.contains(str, ".") && StringUtils.substringAfter(str, ".").length() > 2){
				flag = false;
			}
		}else{
			flag = false;
		}
		return flag;
	}
	
	private boolean compareDate(String a , String b){ 
		return a.compareTo(b) < 0;
	}
}









