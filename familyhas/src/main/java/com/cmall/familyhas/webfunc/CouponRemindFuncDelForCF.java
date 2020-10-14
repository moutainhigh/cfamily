package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 优惠券过期提醒设置删除
 * @author wei.che
 * @Date 2015-12-21
 */
public class CouponRemindFuncDelForCF extends FuncDelete {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		//查询出该提醒设置下的所有message uid
		List<Map<String, Object>> messageUidList = DbUp.upTable("oc_coupon_remind_message").dataSqlList("select message_uid as outsideUid from oc_coupon_remind_message where setting_uid =:settingUid ", 
				new MDataMap("settingUid",mDelMaps.get("uid")));
		if(messageUidList!=null && messageUidList.size()>0){
			//根据message uid 判断是否有‘已发送’的短信
			String messageUids =getUidsByList(messageUidList);
			List<Map<String, Object>> messageSendNumList = DbUp.upTable("za_message").dataSqlList("select count(1) as num from za_message where uid in ("+messageUids+") and flag_finish = 1 ",null);
			if(messageSendNumList!=null && messageSendNumList.size()>0){
				int sendNum = Integer.parseInt(messageSendNumList.get(0).get("num").toString());
				if(sendNum>0){//该设置下短信已有 ‘已发送’状态的短信，则不允许删除
					mResult.inErrorMessage(916423007,sendNum);
					return mResult;
				}
			}
			//若该设置下短信均未发送，则删除
			 DbUp.upTable("za_message").dataDelete(" uid in ("+messageUids+") ",null,"");
			 DbUp.upTable("oc_coupon_remind_message").dataDelete(" message_uid in ("+messageUids+") ",null,"");
			
		}
		if (mResult.upFlagTrue()) {
			mResult = super.funcDo(sOperateUid, mDataMap);
		}
		return mResult;
	}

	/**
	 * 组织sql in 的uid 查询条件
	 * @param List
	 * @return
	 */
	private String getUidsByList(List<Map<String, Object>> List){
		StringBuffer uids = new StringBuffer("");
		for (int i=0;i<List.size();i++) {
			if(i<List.size()-1){
				uids.append("'"+List.get(i).get("outsideUid")+"',");
			}else{
				uids.append("'"+List.get(i).get("outsideUid")+"'");
			}
		}
		if(StringUtils.isBlank(uids.toString())){
			uids.append("''");
		}
		return uids.toString();
	}
}
