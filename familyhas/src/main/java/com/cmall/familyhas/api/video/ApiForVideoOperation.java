package com.cmall.familyhas.api.video;

import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.video.input.ApiForVideoOperateInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 短视频相关数据操作接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiForVideoOperation extends RootApiForVersion<RootResult,ApiForVideoOperateInput> {
	
	public RootResult Process(ApiForVideoOperateInput input, MDataMap mRequestMap) {
		
		RootResult result = new RootResult();
		String memberCode = "";
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
		}
		if(StringUtils.isBlank(memberCode)){
			if(input.getDoType().equals("R")||input.getDoType().equals("C")||input.getDoType().equals("D")){
				result.setResultCode(0);
				result.setResultMessage("请登录操作");
				return result;
			}
		}
		if(input.getDoType().equals("R")){
			int cnt = DbUp.upTable("lv_praise_detail").dataCount("member_code = '"+memberCode+"' and video_code = '"+input.getVideoCode()+"'", null);
			if(cnt>0){
				DbUp.upTable("lv_praise_detail").dataDelete("member_code = '"+memberCode+"' and video_code = '"+input.getVideoCode()+"'", null, "");
				DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET praise_num = praise_num-1 where video_code = '"+input.getVideoCode()+"'", null);
			}else{
				MDataMap insertMap = new MDataMap();
				insertMap.put("member_code", memberCode);
				insertMap.put("video_code", input.getVideoCode());
				DbUp.upTable("lv_praise_detail").dataInsert(insertMap);
				DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET praise_num = praise_num+1 where video_code = '"+input.getVideoCode()+"'", null);
			}			
		}else if (input.getDoType().equals("C")) {
			if(StringUtils.trimToEmpty(input.getComments()).length()<=0){
				result.setResultCode(0);
				result.setResultMessage("评论内容不能为空");
				return result;
			}
			if(StringUtils.trimToEmpty(input.getComments()).length()>70){
				result.setResultCode(0);
				result.setResultMessage("评论内容超长");
				return result;
			}
			String content = replaceEmoji(input.getComments());
			MDataMap insertMap = new MDataMap();
			insertMap.put("comment_member", memberCode);
			insertMap.put("video_code", input.getVideoCode());
			insertMap.put("content", content);
			DbUp.upTable("lv_video_comment").dataInsert(insertMap);
			DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET comment_num = comment_num+1 where video_code = '"+input.getVideoCode()+"'", null);
		}else if (input.getDoType().equals("S")) {
			DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET watch_num = watch_num+1 where video_code = '"+input.getVideoCode()+"'", null);
		}else if (input.getDoType().equals("D")) {
			DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET is_delete = 1 where video_code = '"+input.getVideoCode()+"'", null);
		}else if (input.getDoType().equals("F")) {
			DbUp.upTable("lv_video_info").dataExec("UPDATE lv_video_info SET share_num = share_num+1 where video_code = '"+input.getVideoCode()+"'", null);
		}
		
		
		return result;
	}
	
	/**
	 * 替换emoji为问号
	 */
	private String replaceEmoji(String text){
		CharsetEncoder encoder = java.nio.charset.Charset.forName("GB2312").newEncoder();
		StringBuilder build = new StringBuilder();
		for(int i = 0,j = text.length(); i < j; i++){
			if(encoder.canEncode(text.charAt(i))){
				build.append(text.charAt(i));
			}else{
				build.append('?');
			}
		}
		return build.toString();
	}
}
