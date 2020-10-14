package com.cmall.familyhas.webfunc;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;

import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @author zb
 */
public class FuncReleaseAdvertise extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String now = FormatHelper.upDateTime();
		String uid = mAddMaps.get("uid");
		if(StringUtils.isEmpty(uid)){
			mResult.setResultMessage("发布失败");
		}else{
			MDataMap one = DbUp.upTable("fh_advertisement_info").one("uid",uid);
			if(one!=null) {
				String releaseFlag = one.get("is_release").toString();
				if("0".equals(releaseFlag)) {//发布
					//如果当前时间大于发布时间
					if(compareDateTime(now, one.get("end_time").toString())){
						mResult.setResultMessage("发布失败,广告过期!");
						mResult.setResultCode(0);
						return mResult;
					}
					String querySql = "select * from fh_advertisement_info where adver_entry_type=:adver_entry_type and is_release='1' and uid!='"+uid+"'";
					List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_advertisement_info").dataSqlList(querySql, new MDataMap("adver_entry_type",one.get("adver_entry_type").toString()));
					if(dataSqlList!=null&&dataSqlList.size()>0) {
						StringBuilder sb = new StringBuilder();
						boolean flag = false;
						sb.append("[");
						for (Map<String, Object> map : dataSqlList) {
							if((compareDateTime(one.get("start_time").toString(), map.get("start_time").toString())&&!compareDateTime(one.get("start_time").toString(), map.get("end_time").toString()))||
									(compareDateTime(one.get("end_time").toString(), map.get("start_time").toString())&&!compareDateTime(one.get("end_time").toString(), map.get("end_time").toString()))||
									(!compareDateTime(one.get("start_time").toString(), map.get("start_time").toString())&&compareDateTime(one.get("end_time").toString(), map.get("end_time").toString()))) {
								flag =true;
								sb.append(map.get("advertise_code")+",");
							}
						}	
						String newStr = sb.substring(0, sb.toString().length()-1);
						newStr=newStr+"]";
						if(flag) {
							mResult.setResultMessage("发布失败,存在时间重叠的广告,重复广告编号为:"+newStr);
							return mResult;
						}
						
					}
					
					if(mResult.upFlagTrue()) {
						DbUp.upTable("fh_advertisement_info").dataUpdate(new MDataMap("uid",uid,"is_release","1"), "is_release", "uid");
					}
				}
				else {//取消发布
					DbUp.upTable("fh_advertisement_info").dataUpdate(new MDataMap("uid",uid,"is_release","0"), "is_release", "uid");
				}

			}
			else {
				mResult.setResultMessage("发布失败,未查询到该条广告信息！");
			}
			
		}
     	return mResult;
		
	}
	
	public static boolean compareDateTime(String startTime, String endTime) {
		// TODO Auto-generated method stub
		if(startTime.compareTo(endTime)>0) {
			return true;
		}
			return false;
	}
}
