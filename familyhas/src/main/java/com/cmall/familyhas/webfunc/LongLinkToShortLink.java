package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.xmassystem.service.ShortLinkService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @author Angel Joy
* @Time 2020-8-6 10:52:26 
* @Version 1.0
* <p>Description:</p>
*  长连接转短连接新增方法类
*/
public class LongLinkToShortLink extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		ShortLinkService sls = new ShortLinkService();
		String expireTime =  mDataMap.get("zw_f_expire_time");
		if(StringUtils.isEmpty(expireTime)) {
			expireTime = DateUtil.addMinute(1576800);
		}
		boolean flag = sls.insertIntoDb(mDataMap.get("zw_f_long_link"), userInfo.getRealName(), expireTime);
		if(flag) {
			result.setResultCode(1);
			result.setResultMessage("操作成功");
		}else {
			result.setResultCode(0);
			result.setResultMessage("操作失败");
		}
		return result;
	}

}
