package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;


/**
 * @description: 发布导航信息| 微信专用
 *
 * @author Yangcl
 * @date 2017年5月3日 下午2:10:33 
 * @version 1.0.0
 */
public class FhApphomeNavReleaseWechat extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		String releaseFlag = mDataMap.get("zw_f_release_flag");  
		if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(releaseFlag)){
			// 验证下面是否有 fh_apphome_column 
			String sql = "select c.* from familyhas.`fh_apphome_column` c left join familyhas.`fh_apphome_nav` n on n.nav_code = c.nav_code "
					+ "where "
					+ "c.view_type='4497471600100002' "
					+ "and c.is_delete='449746250002' "
					+ "and c.seller_code='SI2003' "
					+ "and n.uid = '" + uid + "' ";
			List<Map<String, Object>> list = DbUp.upTable("fh_apphome_column").dataSqlList(sql, null);
			if(list == null || list.size() == 0){
				mResult.setResultCode(941901133);
				mResult.setResultMessage("该导航下没有可维护的内容，不能进行发布相关操作!");  
				return mResult;
			}
			
			
			if(releaseFlag.equals("01")){                              // 是否发布 01:是 02:否
				releaseFlag = "02";  // 准备取消发布
			}else{
				releaseFlag = "01"; // 准备执行发布
			}
			String update_user = UserFactory.INSTANCE.create().getLoginName();
			MDataMap um = new MDataMap();
			um.put("release_flag", releaseFlag); 
			um.put("update_user", update_user);
			um.put("update_time", DateUtil.getSysDateTimeString());
			um.put("uid", uid); 
			DbUp.upTable("fh_apphome_nav").dataUpdate(um, "release_flag,update_user,update_time", "uid");
		}
		
		return mResult;
	}
}



















