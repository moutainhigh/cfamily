package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券活动 迭代3
 * @author ligj
 * time:2015-05-02 15:33:00
 *
 */
public class FuncAddCouponActivityNew4 extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		String activityName = mDataMap.get("zw_f_activity_name");
		String remark = mDataMap.get("zw_f_remark");
		
		if (activityName.length() > 100) {
			//活动名称最长100个字符
			mResult.inErrorMessage(916421246,"活动名称",100);
			return mResult;
		}
		if (remark.length() > 100) {
			//活动名称最长100个字符
			mResult.inErrorMessage(916421246,"活动描述",100);
			return mResult;
		}
		
		String startTime = mDataMap.get("zw_f_begin_time");
		String endTime = mDataMap.get("zw_f_end_time");
		
		//活动优惠券发放限额
		String assign_line = mDataMap.get("zw_f_assign_line");
		if(StringUtils.isEmpty(assign_line)) {
			mDataMap.put("zw_f_assign_line", "0.0");
		}
		
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		
		if (endTime.compareTo(startTime) <= 0) {
			//开始时间必须小于结束时间!
			mResult.inErrorMessage(916401201);
			return mResult;
		}else if (endTime.compareTo(createTime) <= 0) {
//			当前时间必须小于结束时间!
			mResult.inErrorMessage(916401214);
			return mResult;
		}
		
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_creator", create_user);
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updator", create_user);
			mDataMap.put("zw_f_activity_code", WebHelper.upCode("AC"));
			mDataMap.put("zw_f_activity_type", "449715400008");
			mDataMap.put("zw_f_seller_code", UserFactory.INSTANCE.create().getManageCode());
			mDataMap.put("zw_f_is_multi_use", "Y");
			mDataMap.put("zw_f_provide_type", "4497471600060002");
			mDataMap.put("zw_f_is_detail_show", "449748350002");
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(916422120);
		}
		return mResult;
	}
}
