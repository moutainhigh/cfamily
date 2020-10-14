package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改商品标签
 * 
 * @author 李国杰
 * 
 */
public class FuncEditLabels extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		
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
		int count = this.checkRepeat(mDataMap.get("zw_f_label_name"),mDataMap.get("zw_f_label_code"));
		if (count < 1) {
			String createTime = DateUtil.getNowTime();
			String user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", user);
			mResult = super.funcDo(sOperateUid, mDataMap);
			XmasKv.upFactory(EKvSchema.ProductLabels).del(mDataMap.get("zw_f_label_code"));
		}else{
			mResult.setResultCode(941901133);
			mResult.setResultMessage(bInfo(941901133));
		}
		
		return mResult;
	}
	private int checkRepeat(String labelName,String labelCode){
		int count = 0;
		if (StringUtils.isNotBlank(labelName)) {
			String sWhere = "label_name='"+labelName+"' and label_code != '"+labelCode+"' and flag_enable='1'";
			count = DbUp.upTable("pc_product_labels").dataCount(sWhere, null);
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
