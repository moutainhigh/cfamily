package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiRecommendedFrPageInfoInput;
import com.cmall.familyhas.api.result.ApiRecommendedFrPageInfoResult;
import com.srnpr.xmassystem.support.PlusSupportSystem;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 获取推荐好友页面信息
 * @author fq
 *
 */
public class ApiRecommendedFrPageInfo extends RootApiForToken<ApiRecommendedFrPageInfoResult, ApiRecommendedFrPageInfoInput> {

	public ApiRecommendedFrPageInfoResult Process(
			ApiRecommendedFrPageInfoInput inputParam, MDataMap mRequestMap) {
		
		ApiRecommendedFrPageInfoResult result = new ApiRecommendedFrPageInfoResult();
		MDataMap paramMap = new MDataMap();
		paramMap.put("manage_code", getManageCode());
		List<MDataMap> queryAll = DbUp.upTable("fh_app_share").queryAll("*", "", " manage_code=:manage_code", paramMap);
		StringBuffer rntNum = new StringBuffer();
		//添加发放卷信息  （当前配置的活动的相关券信息）
		List<MDataMap> couponList = DbUp.upTable("oc_coupon_relative").query("activity_code", "", " relative_type=7 and manage_code=:manage_code", paramMap, -1, -1);
	    if(couponList.size()>0){
	    	String paramList = "";
	    	for (MDataMap mDataMap : couponList) {
	    		paramList+="'"+mDataMap.get("activity_code")+"',";
			}
	    	paramList = paramList.substring(0, paramList.length() -1);
	    	//修改只查询已经发布的
	    	String sql = "SELECT * FROM ordercenter.oc_coupon_type a  WHERE a.status='4497469400030002' and a.activity_code in ("+paramList+")";
	    	List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_type").dataSqlList(sql, null);
	    	if(dataSqlList.size()>0){
	    		int vtotal = 0;  //金额券
	    		int ctotal = 0;  //礼金券
	    		int zNum = 0;  //折扣券
	    		for (Map<String, Object> map : dataSqlList) {
					String mt = (String)map.get("money_type");
					if("449748120001".equals(mt)){   //金额券
						vtotal +=Integer.valueOf(String.valueOf(map.get("money")));
					}
					else if("449748120003".equals(mt)){   //礼金券
						ctotal +=Integer.valueOf(String.valueOf(map.get("money")));
					}
					else if("449748120002".equals(mt)){   //折扣券
						zNum++;
					}
				}
	    		if(vtotal!=0||ctotal!=0){
	    			rntNum.append("获得福利:"+(vtotal+ctotal)+"元 ");
	    		}
	    	/*	if(ctotal!=0){
	    			rntNum.append("获得福利:"+ctotal+"元 ");
	    		}*/
	    		if(zNum != 0){
	    			rntNum.append("获得折扣券:"+zNum+"张 ");
	    		}
	    	}
	    }
		
		if(queryAll.size() > 0) {
			MDataMap mDataMap = queryAll.get(0);
			
			result.setPageTitle(mDataMap.get("title"));
			result.setImgUrl(mDataMap.get("picture"));
			result.setRecommendRuleInfo(mDataMap.get("description"));//"description");
			result.setRuleTitle(mDataMap.get("title_rule"));
			result.setShareContent(mDataMap.get("share_description"));
			result.setSharePic(mDataMap.get("share_pic"));
			result.setShareTitle(mDataMap.get("share_title"));
			result.setRntNum(rntNum.toString());
		
			/*
			 * 分享连接后台配置
			 */
//			result.setShare_link(mDataMap.get("share_link"));
			String recommendedFrPageInfo = bConfig("familyhas.recommendedFrPageInfo");
			result.setShareLink(recommendedFrPageInfo);
			/*
			 * 获取二维码连接 
			 * 添加头像和昵称字段
			 */
			PlusSupportSystem plusSupport = new PlusSupportSystem();
			String mobile = getOauthInfo().getLoginName();
			String sql = "select nickname,avatar from mc_member_sync where login_name = :login_name";
			Map<String,Object> map= DbUp.upTable("mc_member_sync").dataSqlOne(sql, new MDataMap("login_name",mobile));
			//查询手机号
			String qrCodeSrc = plusSupport.upQrCode(recommendedFrPageInfo+"?pm="+mobile+"&headImg="+map.get("avatar")+"&nickNm="+map.get("nickname"), 500);
			result.setQRcodeLink(qrCodeSrc);
			
			result.setResultCode(1);
			result.setResultMessage("");
		}
		
		return result;
	
	}

}
