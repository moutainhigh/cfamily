package com.cmall.familyhas.api;


import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiForActivityCouponInput;
import com.cmall.familyhas.api.input.ApiIssueCouponForWeChatBoundUserInput;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.homehas.HomehasSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;



public class ApiIssueCouponForWeChatBoundUser extends RootApi<RootResultWeb,  ApiIssueCouponForWeChatBoundUserInput>{

	@Override
	public RootResultWeb Process(ApiIssueCouponForWeChatBoundUserInput inputParam, MDataMap mRequestMap) {
		RootResultWeb rootResult = new RootResultWeb();
		String validate_code = inputParam.getValidate_code();
		String member_code = inputParam.getMember_code();
		MDataMap one = DbUp.upTable("oc_coupon_wechat_validate").one("validate_code",validate_code);
		if(one==null||!StringUtils.equals(member_code, one.get("member_code"))) {
			rootResult.setResultCode(0);
			rootResult.setResultMessage("领券编号错误!");
			//unBindWX(member_code);
			return rootResult;
		}else {
			//发券
			MDataMap one1 = DbUp.upTable("mc_member_wechat_bound").one("member_code",member_code);
			//新绑定的只能发一次
			String is_issue = one1.get("is_issue");
			if("1".equals(is_issue)) {
				rootResult.setResultCode(0);
				rootResult.setResultMessage("此用户已经发放优惠券!");
				return rootResult;
			}
			List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_wechat_bound").dataSqlList("select * from oc_coupon_wechat_bound", null);
			if(dataSqlList!=null&&dataSqlList.size()>0) {
				String  activity_code = dataSqlList.get(0).get("activity_code").toString();
				boolean distributeCoupons = distributeCoupons(activity_code,one1.get("phone_num"));
				if(!distributeCoupons) {
					rootResult.setResultCode(0);
					rootResult.setResultMessage("优惠券发放失败!");
				}else {
					//记录发放日志
					MDataMap mDataMap = new MDataMap();
					String userCode = UserFactory.INSTANCE.create().getUserCode();
					mDataMap.put("uid",WebHelper.upUuid());
					mDataMap.put("issuer",userCode);
					mDataMap.put("receiver",member_code);
					mDataMap.put("activity_code",activity_code);
					mDataMap.put("issue_time", DateUtil.getNowTime());
                    DbUp.upTable("lc_wechat_bound_coupon").dataInsert(mDataMap);
                    //更新优惠券发放状态
                    DbUp.upTable("mc_member_wechat_bound").dataUpdate(new MDataMap("member_code",member_code,"is_issue","1"), "is_issue", "member_code");
				}
			}else {
				rootResult.setResultCode(0);
				rootResult.setResultMessage("未配置优惠券活动!");
			} 
			
		}

		return rootResult;
	}
	
	
	public boolean distributeCoupons(String activityCode, String phone) {

		ApiForActivityCoupon afac = new ApiForActivityCoupon();
		ApiForActivityCouponInput input = new ApiForActivityCouponInput();
		input.setActivityCode(activityCode);
		input.setMobile(phone);
		input.setValidateFlag("2");
		RootResultWeb process = afac.Process(input, new MDataMap());
		if (process.getResultCode() == 939301319) {
			System.out.println("'绑定微信发放优惠券失败~");
			return false;
		}
		return true;
	}
	
	private void unBindWX(String member_code) {
		//解绑
		MDataMap one = DbUp.upTable("mc_member_wechat_bound").one("member_code",member_code);
		if(StringUtils.isNotBlank(one.get("phone_num"))) {
			//调用解绑接口
			HomehasSupport homehasSupport = new HomehasSupport();
			MWebResult result = homehasSupport.unBindWX(one.get("phone_num"));
			if(result.getResultCode()==1) {
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("is_bound", "0");
				mDataMap.put("member_code", member_code);
				mDataMap.put("nick_name_wechat", "");
				mDataMap.put("registe_time", "");
				mDataMap.put("is_issue", "0");
				DbUp.upTable("mc_member_wechat_bound").dataUpdate(mDataMap, "is_bound,nick_name_wechat,registe_time,is_issue", "member_code");
			}
		}
	}

}
