package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiStartPageInput;
import com.cmall.familyhas.api.model.StartupPageJump;
import com.cmall.familyhas.api.result.APiStartPageResult;
import com.cmall.groupcenter.model.UpdateEntity;
import com.cmall.ordercenter.familyhas.active.product.ActiveForSource;
import com.cmall.productcenter.model.NavigationVersion;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.MyService;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.model.AppNavigation;
import com.cmall.systemcenter.service.StartPageService;
import com.cmall.systemcenter.util.StringUtility;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * @title: com.cmall.familyhas.api.APiStartPage.java 
 * @description:  app启动接口
 *			位置信息：API信息 -> 惠家有 -> 启动页接口
 *
 * @author xiegj
 * @version 1.0.0
 *
 * @remark 新增字段：isSupport
 * @author Yangcl
 * @date 2016年9月22日 上午10:15:49 
 * @version 1.0.1
 */
public class APiStartPage extends RootApiForMember<APiStartPageResult, APiStartPageInput> {

	public APiStartPageResult Process(APiStartPageInput inputParam, MDataMap mRequestMap) {
		
		
		APiStartPageResult result = new APiStartPageResult();
		StartPageService service = new StartPageService();
		MDataMap map = new MDataMap();
		if (getFlagLogin()) {
			map.put("buyerCode", getOauthInfo().getUserCode());
		} else {
			map.put("buyerCode", "");
		}
		map.put("sellerCode", getManageCode());
		map.put("pushType", inputParam.getPushType());
		map.put("pushToken", inputParam.getPushToken());
		map.put("sqNum", inputParam.getSqNum());
		map.put("picNm", inputParam.getPicNm());
		MDataMap re = service.getPics(map);
		if("0".equals(re.get("residence_time"))){
			return result;
		}
		result.setPicNm(re.get("picNm"));
		result.setPicType(re.get("picType"));
		result.setPicUrl(re.get("picUrl"));
		result.setSqNum(re.get("sqNum"));
		//默认为老webView
		MDataMap relMap = DbUp.upTable("zw_define").one("define_name","webViewType");
		String webViewType = relMap.get("define_one").toString();
		result.setWebViewFlag(webViewType);
		//直播互动的连接地址
		result.setLiveInteractionLink(bConfig("familyhas.liveInteractionLink"));
		StartupPageJump pageJump = new StartupPageJump();
		pageJump.setPicType(re.get("picType"));
		pageJump.setButtonBackground(re.get("button_background"));
		pageJump.setButtonPic(re.get("button_pic"));
		pageJump.setButtonText(re.get("button_text"));
		pageJump.setButtonColor(re.get("button_color"));
		pageJump.setButtonType(re.get("button_type"));
		pageJump.setShowmoreLinktype(re.get("showmore_linktype"));
		pageJump.setShowmoreLinkvalue(re.get("showmore_linkvalue"));
		pageJump.setResidenceTime(re.get("residence_time"));
		pageJump.setYnCountdown(re.get("yn_countdown"));
		pageJump.setYnJumpButton(re.get("yn_jump_button"));
		result.setPageJump(pageJump);
		service.saveClient(inputParam.getClient(), getManageCode(),
				map.get("buyerCode"), re.get("sqNum"));
		List<UpdateEntity> enli = new ArrayList<UpdateEntity>();
		UpdateEntity rc = new UpdateEntity();
		UpdateEntity area = new UpdateEntity();
		UpdateEntity gc = new UpdateEntity();
		rc.setUpdateKey("rc");
		rc.setUpdateValue("1");
		area.setUpdateKey("area");
		area.setUpdateValue("1");
		gc.setUpdateKey("cg");
		gc.setUpdateValue("1");
		enli.add(gc);
		enli.add(rc);
		enli.add(area);
		result.setList(enli);
		// 图片等比缩放
		if (inputParam.getClient().getScreen() != null
				&& !"".equals(inputParam.getClient().getScreen())
				&& inputParam.getClient().getScreen().contains("x")) {
			String[] sc = inputParam.getClient().getScreen().split("x");
			int width = Integer.valueOf(sc[0]) > Integer.valueOf(sc[1]) ? Integer
					.valueOf(sc[1]) : Integer.valueOf(sc[0]);
			if(StringUtils.isNotEmpty(result.getPicUrl())){
						PicInfo pi = (new ProductService()).getPicInfoForMany(width,
								result.getPicUrl());
						result.setPicUrl(pi.getPicNewUrl());
						result.getPageJump().setPicUrl(pi.getPicNewUrl());
			}
			if(StringUtils.isNotEmpty(pageJump.getButtonPic())){
				PicInfo pi1 = (new ProductService()).getPicInfoForMany(width,
						pageJump.getButtonPic());
				result.getPageJump().setButtonPic(pi1.getPicNewUrl());
			}
		}
		String clientType = "";
		if(StringUtils.isNotBlank(inputParam.getClient().getOs()) && inputParam.getClient().getOs().equals("android")) {//只有android传送的设备类型为android，ios可能没有传递次参数，因此默认为ios
			clientType = StartPageService.ANDROID;
		} else {
			clientType = StartPageService.IOS;
		}
		if (StringUtility.isNotNull(inputParam.getSecret())) {
			result.setKeys(service.getKeysSecret(getManageCode(),clientType));
		} else {
			result.setKeys(service.getKeys(getManageCode(),clientType));
		}

		// 三星手机特定版本问题代码--过3.8.0版本之后可以进行删除
		if (StringUtility.isNotNull(inputParam.getClient().getModel())
				&& StringUtility.isNotNull(inputParam.getClient()
						.getApp_vision())
				&& "3.7.6".equals(inputParam.getClient().getApp_vision())
				&& StringUtils.indexOfAny(inputParam.getClient().getModel(),
						new String[] { "GT-N7100", "GT-N719", "GT-N7102",
								"GT-N7108" }) > -1) {
			result.setKeys(new MDataMap());
		}
		if (getOauthInfo() != null) {
			result.setBuyerType(new ActiveForSource()
					.checkIsVipSpecialForFamilyhas(getOauthInfo().getUserCode()) ? "4497469400050001"
					: "4497469400050002");
		}
		if(getManageCode().equals("SI3003")){
			result.setServiceTel(bConfig("familyhas.serviceTelBysharpei"));
			result.setTelOrdering(bConfig("familyhas.telOrderingBysharpei"));
		}else{
			result.setServiceTel(bConfig("familyhas.serviceTel"));
			result.setTelOrdering(bConfig("familyhas.telOrdering"));
		}
		
		ProductService productService = new ProductService();
		if(!"SI2011".equals(getManageCode())){
			Map<String,Object> nas = productService.addNavigationMaintain(inputParam.getWidth(),inputParam.getNavigationVersion());
			result.setAppNavigations((List<AppNavigation>)nas.get("nas"));
			UpdateEntity dhtp = new UpdateEntity();
			dhtp.setUpdateKey("dhtp");
			dhtp.setUpdateValue("0");
			if("1".equals(nas.get("flag").toString())){
				dhtp.setUpdateValue(nas.get("flag").toString());	
			}
			result.getList().add(dhtp);
			result.setNavigationVersion((NavigationVersion)nas.get("version"));
		}
		//"我的"头像后面的背景图片
		result.setBackgroundPic(new MyService().getMyPic(getManageCode()));
		
		String curr = DateHelper.upDate(new Date());
		String datawhere = " select * from sc_question_online where type = '449747900001' and ('" + curr + "' between begin_time and end_time) limit 1"; 
		List<Map<String, Object>> questionList = DbUp.upTable("sc_question_online").dataSqlList(datawhere, null);
		if(questionList != null && questionList.size() > 0){
			result.setIsSupport("1"); 
		}else{
			result.setIsSupport("0"); 
		}
		String is_flag = XmasKv.upFactory(EKvSchema.MessageUseable).get("useable");
		if(StringUtils.isBlank(is_flag)){
			MDataMap one = DbUp.upTable("sc_message_configure").one();
			is_flag = one.get("is_flag");
		}
		if(is_flag.equals("4497480100020001")){
			result.setIs_flag("Y");
		}else{
			result.setIs_flag("N");
		}
		
		return result;
	}

}














