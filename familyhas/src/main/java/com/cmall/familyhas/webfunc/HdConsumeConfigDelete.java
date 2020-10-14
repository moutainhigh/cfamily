package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @description: 惠豆使用设置-删除
 *
 * @author Yangcl
 * @date 2017年3月28日 下午2:26:16 
 * @version 1.0.0
 */
public class HdConsumeConfigDelete extends FuncDelete {  
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		mResult = super.funcDo(sOperateUid, mDataMap);
		XmasKv.upFactory(EKvSchema.HomehasBeanConfig).del("Consume");  
		
		return mResult;
	}
}








