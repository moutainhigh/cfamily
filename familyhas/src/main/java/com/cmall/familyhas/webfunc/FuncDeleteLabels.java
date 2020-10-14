package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除标签
 * 
 * @author 李国杰
 * 
 */
public class FuncDeleteLabels extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String createTime = DateUtil.getNowTime();
		String user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_update_time", createTime);
		mDataMap.put("zw_f_update_user", user);
		mDataMap.put("zw_f_flag_enable", "0");
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		MDataMap labelsMap = DbUp.upTable("pc_product_labels").one("uid",mDataMap.get("zw_f_uid"));
		if (null != labelsMap) {
			XmasKv.upFactory(EKvSchema.ProductLabels).del(labelsMap.get("label_code"));
		}
		return mResult;
	}
}
