package com.cmall.familyhas.webfunc;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改渠道合作商
 * @author lgx
 *
 */
public class FuncEditChannelSeller extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		//String channel_link_mobile = mDataMap.get("zw_f_channel_link_mobile");
		// 渠道推送商品类型
		String channel_product_type = mDataMap.get("zw_f_channel_product_type");
		String uid = mDataMap.get("zw_f_uid");
		
		if (null == channel_product_type || "".equals(channel_product_type)) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("请选择渠道推送商品!");
			return mResult;
		}
		/*if(!isPhone(channel_link_mobile)) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("渠道联系电话填写有误,请检查之后重新填写!");
			return mResult;
		}*/
		
		Map<String, Object> channel_seller = DbUp.upTable("uc_channel_sellerinfo").dataSqlOne("SELECT * FROM uc_channel_sellerinfo WHERE uid = '"+uid+"'", new MDataMap());
		if(channel_seller == null) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("查不到该条数据,修改失败!");
			return mResult;
		}
		
		/*修改时间*/
		String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		if (mResult.upFlagTrue()) {
			mDataMap.put("zw_f_update_time", update_time);
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		
		if(mResult.getResultCode() == 1) {
			// 往 lc_channel_freeze_cooperation_log 插入日志数据
			MDataMap map = new MDataMap();
			map.put("channel_seller_code", (String) channel_seller.get("channel_seller_code"));
			map.put("channel_seller_name", mDataMap.get("zw_f_channel_seller_name"));
			map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map.put("register_time", update_time);
			map.put("remark", UserFactory.INSTANCE.create().getLoginName()+"修改了渠道合作商数据");
			DbUp.upTable("lc_channel_freeze_cooperation_log").dataInsert(map);
		}
		
		return mResult;
	}
	
	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}
	
}
