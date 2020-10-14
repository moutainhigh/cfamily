package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 视频分类删除
 * @author zhangbo
 * 
 * 
 */
public class FuncDelVideo extends FuncDelete {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		try {
			MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			String uid = upSubMap.get("uid");
			MDataMap one = DbUp.upTable("fh_video").one("uid",uid);
			String operator = one.get("operator");
			DbUp.upTable("fh_video").delete("uid",uid);	
			int count = DbUp.upTable("fh_video").dataCount("operator=:operator", new MDataMap("operator",(StringUtils.isBlank(operator)?"":operator)));
			if(count==0) {
				DbUp.upTable("fh_video_operators").delete("user_code",operator);
			}
			
		} catch (Exception e) {
			// TODO: handle exception	
			e.printStackTrace();
			mResult.setResultCode(941901133);
			mResult.setResultMessage(bInfo(941901133));
		}
		return mResult;
	}
}
