package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加标签
 * @author ligj
 *
 */
public class FuncAddLabel extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if(!StringUtils.isNotBlank(startTime) || !StringUtils.isNotBlank(endTime)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("非法的时间范围");
			return mResult;
		}
		if(this.compareDate(startTime, endTime)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("开始时间不得大于结束时间");
			return mResult;
		}
		
		String listPic = mDataMap.get("zw_f_list_pic");
		String infoPic = mDataMap.get("zw_f_info_pic");
		String infoActivityPic = mDataMap.get("zw_f_info_activity_pic");
		if(StringUtils.isBlank(listPic) && StringUtils.isBlank(infoPic) && StringUtils.isBlank(infoActivityPic)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("请至少上传一个标签图片");
			return mResult;
		}
		String labelPicSkip = mDataMap.get("zw_f_event_label_pic_skip");
		if(StringUtils.isNotBlank(labelPicSkip) && StringUtils.isBlank(infoActivityPic)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("未设置【商品详情活动标签】不得填写【活动图片跳转链接】，请检查");
			return mResult;
		}
		
		
		//标签名字不能重复
		int count = this.checkRepeat(mDataMap.get("zw_f_label_name"));
		if (count < 1) {
			mDataMap.put("zw_f_label_code",WebHelper.upCode("LB"));
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", create_user);
			mDataMap.put("zw_f_flag_enable", "1");
			mResult = super.funcDo(sOperateUid, mDataMap);
		}else{
			mResult.setResultCode(941901133);
			mResult.setResultMessage(bInfo(941901133));
		}
		return mResult;
	}
	private int checkRepeat(String labelName){
		int count = 0;
		if (StringUtils.isNotBlank(labelName)) {
			count = DbUp.upTable("pc_product_labels").count("label_name",labelName,"flag_enable","1");
		}
		return count;
				
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
}
