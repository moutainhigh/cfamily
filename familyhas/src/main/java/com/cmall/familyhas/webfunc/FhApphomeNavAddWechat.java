package com.cmall.familyhas.webfunc;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ImageMagicSupport;
 
/**
 * @description: 添加导航信息|微信专用
 *
 * @author Yangcl
 * @date 2017年5月3日 下午2:10:33 
 * @version 1.0.0
 */
public class FhApphomeNavAddWechat extends FuncAdd {

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

		mDataMap.put("zw_f_nav_code", WebHelper.upCode("NAV"));
		mDataMap.put("zw_f_create_time", DateHelper.upDate(new Date()));
		mDataMap.put("zw_f_update_time", DateHelper.upDate(new Date()));
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_update_user", create_user); 
		mDataMap.put("zw_f_nav_type" , "4497471600100002");  // wap或app端（app:01;wap:02;pc:03）
		mDataMap.put("zw_f_view_type" , "4497471600100002");  // wap或app端（app:01;wap:02;pc:03）
		
//		String max_percent = mDataMap.get("max_percent");
//		MDataMap insertMap = new MDataMap();
//		insertMap.put("uid", UUID.randomUUID().toString().replace("-", "")); 
//		DbUp.upTable("fh_hd_consume_config").dataInsert(insertMap);
		super.funcDo(sOperateUid, mDataMap);
		return mResult;
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
	 * @description: 判断正数|0不是正数 
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

















