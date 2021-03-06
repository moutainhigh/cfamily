package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @descriptions  3.9.6版本 App首页弹窗 - 广告弹窗 - 数据编辑功能
 * 
 * @refactor 覆盖默认的编辑方法并重写
 * @author Yangcl
 * @date 2016-5-4-下午4:45:08
 * @version 1.0.0
 */
public class EditFhAppHomeDialog extends FuncEdit {
	/**
	 * @descriptions 
	 *  
	 * @param sOperateUid
	 * @param mDataMap
	 * @return mResult
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-4-下午4:47:11
	 * @version 1.0.0.1
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		try {
			String startTime = mDataMap.get("zw_f_start_time").toString();   // 当前要添加的这条记录的时间 
			String endTime = mDataMap.get("zw_f_end_time").toString();
			if(!StringUtils.isNotBlank(startTime) || !StringUtils.isNotBlank(endTime)){
				mResult.inErrorMessage(959701033 ); 
		    	mResult.setResultMessage("开始时间或结束时间不合法");
			}
			String uuid = mDataMap.get("zw_f_uid").toString();
			
			String sql = "select * from fh_app_home_dialog where delete_flag = 1 and (";
			sql += "(end_time >='" + startTime +"' and start_time <='" + startTime +"' ) or ('";
			sql += startTime + "' <= start_time and end_time <= '" + endTime + "' ))";
			
			List<Map<String, Object>> list = DbUp.upTable("fh_app_home_dialog").dataSqlList(sql , new MDataMap());
			for(Map<String, Object> map : list){       // 与历史记录进行比对
				String uuid_ = (String)map.get("uid");
				if(uuid_.equals(uuid)){
					continue;
				}
				
				String startTime_ = (String) map.get("start_time");
				String endTime_ = (String) map.get("end_time");   
				if(!StringUtils.isNotBlank(startTime_) || !StringUtils.isNotBlank(endTime_)){
					continue;
				}
			    if(this.compareDate(startTime, endTime_) || !this.compareDate(endTime, startTime_)  ){ 
			    	continue;
			    }else{
			    	if(startTime.equals((String) map.get("start_time"))){
			    		continue;
			    	}
			    	mResult.inErrorMessage(959701033 ); 
			    	mResult.setResultMessage("开始时间与结束时间与历史记录冲突, 请修正");
			    	return mResult;
			    }
			}
			if(this.compareDate(startTime, endTime)){  
		    	mResult.inErrorMessage(959701033);  
		    	mResult.setResultMessage("当前记录开始时间不得相同或在结束时间之后");
		    	return mResult;
		    }
			
			String popCount = mDataMap.get("zw_f_pop_count").toString();
			if(!validate(popCount, "^\\d{1,2}$")){
				mResult.inErrorMessage(959701033);  
		    	mResult.setResultMessage("弹出次数为整数, 且不得大于100");
		    	return mResult;
			}
			
			String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();
			String update_user_code = UserFactory.INSTANCE.create().getUserCode();
			String update_user = UserFactory.INSTANCE.create().getLoginName();
			if (mResult.upFlagTrue()) {
				mDataMap.put("zw_f_update_user_code", update_user_code);
				mDataMap.put("zw_f_update_user", update_user);
				mDataMap.put("zw_f_update_time", update_time);
			}
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e){
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		
		
		return mResult;
	}
	
	/**
	 * @descriptions 比较两个时间的大小
	 *  	如果两个时间相等则返回0
	 * @param a not null
	 * @param b not null 
	 * @return boolean 
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-5-下午2:52:13
	 * @version 1.0.0.1
	 */
	private boolean compareDate(String a, String b) {
	    return a.compareTo(b) > 0;
	}
	
	/**
	 * @descriptions 正则验证
	 *  
	 * @param str 
	 * @param regExp 
	 * @return boolean
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-5-下午4:02:45
	 * @version 1.0.0.1
	 */
	private boolean validate(String str , String regExp){ 
	   Pattern pattern = Pattern.compile(regExp); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
}

























