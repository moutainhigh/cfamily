package com.cmall.familyhas.api.video;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.video.input.ApiForPersonCenterInput;
import com.cmall.familyhas.api.video.result.ApiForPersonCenterResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 个人中心接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiForPersonCenter extends RootApiForVersion<ApiForPersonCenterResult,ApiForPersonCenterInput> {
	
	public ApiForPersonCenterResult Process(ApiForPersonCenterInput input, MDataMap mRequestMap) {
		
		ApiForPersonCenterResult result = new ApiForPersonCenterResult();
		String memberCode = "";
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
		}
		//先增加个人中心访问量
		int cnt = DbUp.upTable("lv_user_homepage").dataCount("member_code = '"+input.getMemberCode()+"'", null);
		if(cnt>0){
			DbUp.upTable("lv_user_homepage").dataExec("UPDATE lv_user_homepage SET access_num = access_num+1 where member_code = '"+input.getMemberCode()+"'", null);
		}else{
			MDataMap insertMap = new MDataMap();
			insertMap.put("member_code", input.getMemberCode());
			DbUp.upTable("lv_user_homepage").dataInsert(insertMap);
			DbUp.upTable("lv_user_homepage").dataExec("UPDATE lv_user_homepage SET access_num = access_num+1 where member_code = '"+input.getMemberCode()+"'", null);
		}
		
		String sql = "SELECT ms.nickname,ms.avatar,sum(vi.praise_num) praise_num,uh.access_num,count(DISTINCT cd.member_code) fans_num,count(DISTINCT cd1.concern_usercode) care_num from membercenter.mc_member_sync ms LEFT JOIN lv_user_homepage uh on uh.member_code = ms.member_code"+ 
				" LEFT JOIN lv_concern_detail cd on cd.concern_usercode = ms.member_code LEFT JOIN lv_concern_detail cd1 on cd1.member_code = ms.member_code "+
				" LEFT JOIN lv_video_info vi ON ms.member_code = vi.member_code where ms.member_code = '"+input.getMemberCode()+"'";
		Map<String, Object> map = DbUp.upTable("lv_user_homepage").dataSqlOne(sql, null);
		result.setCareNum(map.get("care_num")==null?"0":map.get("care_num").toString());
		result.setFansNum(map.get("fans_num")==null?"0":map.get("fans_num").toString());
		result.setPraiseNum(map.get("praise_num")==null?"0":map.get("praise_num").toString());
		result.setVisitNum(map.get("access_num")==null?"0":map.get("access_num").toString());
		BigDecimal temp = new BigDecimal(10000);
		if(Integer.parseInt(result.getCareNum())>=10000){
			result.setCareNum(new BigDecimal(result.getCareNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
		}
		if(Integer.parseInt(result.getFansNum())>=10000){
			result.setFansNum(new BigDecimal(result.getFansNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
		}
		if(Integer.parseInt(result.getPraiseNum())>=10000){
			result.setPraiseNum(new BigDecimal(result.getPraiseNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
		}
		if(Integer.parseInt(result.getVisitNum())>=10000){
			result.setVisitNum(new BigDecimal(result.getVisitNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
		}
		result.setHeadPic(map.get("avatar")==null?"":map.get("avatar").toString());
		result.setNickName(map.get("nickname")==null?"":map.get("nickname").toString());
		if(StringUtils.isNotBlank(memberCode)){
			String sSql = "SELECT * from lv_concern_detail cd where cd.concern_usercode = '"+input.getMemberCode()+"' and cd.member_code = '"+memberCode+"'";
			List<Map<String, Object>> list = DbUp.upTable("lv_concern_detail").dataSqlList(sSql, null);
			if(list.size()>0){
				result.setIsCare("Y");
			}
		}		
		
		return result;
	}
}
