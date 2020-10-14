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
public class FuncReleaseAdvert extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String now = FormatHelper.upDateTime();
		String uid = mAddMaps.get("uid");
		if(StringUtils.isEmpty(uid)){
			mResult.setResultMessage("发布失败");
		}else{
			MDataMap one = DbUp.upTable("fh_advert").one("uid",uid);
			if(one!=null) {
				String releaseFlag = one.get("is_release").toString();
				if("449746250002".equals(releaseFlag)) {//发布
					//如果当前时间大于发布时间
					if(compareDateTime(now, one.get("end_time").toString())){
						mResult.setResultMessage("发布失败,广告过期!");
						mResult.setResultCode(0);
						return mResult;
					}
					String channel_id = one.get("channel_id").toString();
					String querySql = "";
					if("449748610004".equals(channel_id)) {
						querySql = "select * from fh_advert where adver_entry_type=:adver_entry_type and is_release='449746250001' and uid!='"+uid+"'";
					}else {
						querySql = "select * from fh_advert where adver_entry_type=:adver_entry_type and is_release='449746250001' and uid!='"+uid+"' and channel_id in ('"+channel_id+"','449748610004')";
					}
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
						DbUp.upTable("fh_advert").dataUpdate(new MDataMap("uid",uid,"is_release","449746250001"), "is_release", "uid");
					}
				}
				else {//取消发布
					DbUp.upTable("fh_advert").dataUpdate(new MDataMap("uid",uid,"is_release","449746250002"), "is_release", "uid");
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
