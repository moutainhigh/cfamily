package com.cmall.familyhas.api;


import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBuyerShowAboutInput;
import com.cmall.familyhas.api.result.ApiForBuyerShowAboutResult;
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.VideoUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 新增晒单评价/晒单点赞/晒单关注/晒单评论点赞
 * @author lgx
 *
 */

public class ApiForBuyerShowAbout extends RootApiForToken<ApiForBuyerShowAboutResult, ApiForBuyerShowAboutInput>{
	
	public ApiForBuyerShowAboutResult Process(ApiForBuyerShowAboutInput inputParam, MDataMap mRequestMap) {
		ApiForBuyerShowAboutResult result = new ApiForBuyerShowAboutResult();
		
		String userCode = getUserCode();
		
		String touchType = inputParam.getTouchType();
		
		String nowTime = DateUtil.getSysDateTimeString();
		
		MDataMap insertMap = new MDataMap();
		if("1".equals(touchType)) {
			// 评价买家秀
			String buyerShowUid = inputParam.getBuyerShowUid();
			String contentEvaluation = inputParam.getContentEvaluation();
			contentEvaluation = replaceEmoji(contentEvaluation);
			// 评论内容审核过滤
			WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();
			String access_token = wxMusicAlbumService.getToken();
			if(StringUtils.isNotEmpty(contentEvaluation.replaceAll("\\s*", ""))) {
				boolean chekMessageFlag = new VideoUtils().checkContent(contentEvaluation, access_token);
				if(!chekMessageFlag) {//有违规消息。
					result.setResultCode(0);
					result.setResultMessage("您的消息：【"+contentEvaluation+"】存在违规信息，请核实后重新提交");
					return result;
				}
			}else {
				result.setResultCode(0);
				result.setResultMessage("评论内容不能为空");
				return result;
			}
			if(contentEvaluation.length() > 70) {
				result.setResultCode(0);
				result.setResultMessage("对不起,您最多只能评论70个字!");
				return result;
			}
			
			insertMap.put("buyer_show_uid", buyerShowUid);
			insertMap.put("member_code", userCode);
			insertMap.put("content_evaluation", contentEvaluation);
			insertMap.put("create_time", nowTime);
			DbUp.upTable("nc_buyer_show_evaluation").dataInsert(insertMap);
		}else if("2".equals(touchType)) {
			// 给买家秀点赞
			String buyerShowUid = inputParam.getBuyerShowUid();
			MDataMap one = DbUp.upTable("nc_buyer_show_approve").one("buyer_show_or_buyer_show_eva_uid",buyerShowUid,"member_code",userCode);
			if(one != null) {
				// 已经点过赞,则取消点赞
				DbUp.upTable("nc_buyer_show_approve").delete("buyer_show_or_buyer_show_eva_uid",buyerShowUid,"member_code",userCode);
				result.setCancelOrApprove("0");
			}else {
				// 没有点赞,则新增点赞记录
				insertMap.put("buyer_show_or_buyer_show_eva_uid", buyerShowUid);
				insertMap.put("member_code", userCode);
				insertMap.put("create_time", nowTime);
				DbUp.upTable("nc_buyer_show_approve").dataInsert(insertMap);
				result.setCancelOrApprove("1");
			}
		}else if("3".equals(touchType)) {
			// 关注发布买家秀用户
			String evaMemberCode = inputParam.getEvaMemberCode();
			if(evaMemberCode.equals(userCode)) {
				result.setResultCode(-1);
				result.setResultMessage("无需关注自己哦！");
			}else {				
				MDataMap one = DbUp.upTable("nc_buyer_show_fans").one("member_code",evaMemberCode,"fans_member_code",userCode);
				if(one != null) {
					// 已经关注,则取消关注
					DbUp.upTable("nc_buyer_show_fans").delete("member_code",evaMemberCode,"fans_member_code",userCode);
					result.setCancelOrApprove("0");
				}else {
					// 没有关注,则新增关注记录
					insertMap.put("member_code", evaMemberCode);
					insertMap.put("fans_member_code", userCode);
					insertMap.put("create_time", nowTime);
					DbUp.upTable("nc_buyer_show_fans").dataInsert(insertMap);
					result.setCancelOrApprove("1");
				}
			}
		}else if("4".equals(touchType)) {
			// 为买家秀的评论点赞
			String buyerShowEvaUid = inputParam.getBuyerShowEvaUid();
			MDataMap one = DbUp.upTable("nc_buyer_show_approve").one("buyer_show_or_buyer_show_eva_uid",buyerShowEvaUid,"member_code",userCode);
			if(one != null) {
				// 已经点过赞,则取消点赞
				DbUp.upTable("nc_buyer_show_approve").delete("buyer_show_or_buyer_show_eva_uid",buyerShowEvaUid,"member_code",userCode);
				result.setCancelOrApprove("0");
			}else {
				// 没有点赞,则新增点赞记录
				insertMap.put("buyer_show_or_buyer_show_eva_uid", buyerShowEvaUid);
				insertMap.put("member_code", userCode);
				insertMap.put("create_time", nowTime);
				DbUp.upTable("nc_buyer_show_approve").dataInsert(insertMap);
				result.setCancelOrApprove("1");
			}
		}
		result.setBackTouchType(touchType);
		
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
