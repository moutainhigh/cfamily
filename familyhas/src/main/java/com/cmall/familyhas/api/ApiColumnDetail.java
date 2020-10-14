package com.cmall.familyhas.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiColumnDetailInput;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.model.HomeColumnPc;
import com.cmall.familyhas.api.result.ApiColumnDetailResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有栏目查询(PC端调用)
 * 
 * @author zhaoxq
 * 
 */
public class ApiColumnDetail extends
		RootApiForVersion<ApiColumnDetailResult, ApiColumnDetailInput> {

	public ApiColumnDetailResult Process(ApiColumnDetailInput inputParam,
			MDataMap mRequestMap) {
		String buyType = "4497469400050002";
		if (!StringUtils.isBlank(inputParam.getBuyerType())) {
			buyType = inputParam.getBuyerType();
		}
		String columnCode = inputParam.getColumnID();
		String pageType = inputParam.getPageType();
		String systemTime = DateUtil.getSysDateTimeString();
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0"
				: inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? ""
				: inputParam.getPicType(); // 图片格式
		String userCode = (getFlagLogin() ? getOauthInfo().getUserCode() : "");
		ApiColumnDetailResult result = new ApiColumnDetailResult();
		List<HomeColumnPc> columnList = new ArrayList<HomeColumnPc>(); // 其他栏目List
		HomeColumnService hcService = new HomeColumnService();
		ProductService pService = new ProductService();
		String sellerCode = getManageCode();
		String table = "";
		if("0".equals(pageType)){
			table = "fh_apphome_column";
		}else{
			table = "fh_pctv_column";
		}
		String sFields = "column_code,column_name,column_type,start_time,end_time,is_showmore,"
				+ "showmore_title,showmore_linktype,showmore_linkvalue,show_name,pic_url";
		String sOrders = "position asc , create_time desc";
		String sWhere = " start_time <= '"+ systemTime
				+ "' and end_time > '"+ systemTime
				+ "' and is_delete='449746250002' and release_flag='449746250001' "
				+ "and column_code='"+columnCode+"'";
		List<MDataMap> columnMapList = DbUp.upTable(table)
				.queryAll(sFields, sOrders, sWhere, null);
		MDataMap columnMap = null;
		if (null != columnMapList && columnMapList.size()>0) {
			columnMap = columnMapList.get(0);
		}else{
			return result;
		}
		String sFieldsContent = "";
		String sWhereContent = " start_time <='" + systemTime
				+ "' and end_time > '" + systemTime
				+ "' and is_delete='449746250002' and column_code='"+columnCode+"'";
		List<MDataMap> columnContentMapList = DbUp.upTable(
				"fh_apphome_column_content").queryAll(sFieldsContent,
				"floor_model,position asc,start_time desc", sWhereContent, null);
		// 获取所有要查询商品信息的商品编号
		MDataMap productCodeMap = new MDataMap();
		// 获取所有要查询的分类编号，key:categoryCode,value:0
		MDataMap categoryCodeMap = new MDataMap();
		// 图片Map，key:picOldUrl，value:picNewUrl
		MDataMap picUrlMap = new MDataMap();
		// 图片Map，key:picOldUrl，value:picNewUrl
		Map<String,PicInfo> picUrlObjMap = new HashMap<String,PicInfo>();
		
		final String PRODUCT_VIEW = "4497471600020004"; // 链接类型为商品详情
		final String CATEGORY = "4497471600020003"; // 链接类型为分类
		final String URL = "4497471600020001"; // 链接类型为URL
		
		if(PRODUCT_VIEW.equals(columnMap.get("showmore_linktype"))) {
			String productCode = columnMap.get("showmore_linkvalue");
			if (StringUtils.isNotEmpty(productCode)) {
				productCodeMap.put(productCode, "");
			}
		}else if(CATEGORY.equals(columnMap.get("showmore_linktype"))) {
			String categoryCode = columnMap.get("showmore_linkvalue");
			if (StringUtils.isNotEmpty(categoryCode)) {
				categoryCodeMap.put(categoryCode, "");
			}
		}
			
		for (int i = 0; i < columnContentMapList.size(); i++) {
			MDataMap mDataMap = columnContentMapList.get(i);
			if (PRODUCT_VIEW.equals(mDataMap.get("showmore_linktype"))) {
				String productCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(productCode)) {
					productCodeMap.put(productCode, "");
				}
			} else if (CATEGORY.equals(mDataMap.get("showmore_linktype"))) {
				String categoryCode = mDataMap.get("showmore_linkvalue");
				if (StringUtils.isNotEmpty(categoryCode)) {
					categoryCodeMap.put(categoryCode, "");
				}
			}
			if (StringUtils.isNotEmpty(mDataMap.get("picture"))) {
				picUrlMap.put(mDataMap.get("picture"), "");
			}
		}

		List<String> productCodeArr = new ArrayList<String>();
		for (String productCode : productCodeMap.keySet()) {
			productCodeArr.add(productCode);
		}
		List<String> categoryCodeArr = new ArrayList<String>();
		for (String categoryCode : categoryCodeMap.keySet()) {
			categoryCodeArr.add(categoryCode);
		}
		
		Map<String, HomeColumnContentProductInfo> productInfoMap = hcService.getProductInfo(productCodeArr, buyType,userCode,getChannelId());
		MDataMap categoryNameMap = hcService.getCategoryName(categoryCodeArr,sellerCode);
		
		//获取商品信息中的商品图片，用来进行压缩
		for (String productCode : productInfoMap.keySet()) {
			picUrlMap.put(productInfoMap.get(productCode).getMainpicUrl(),"");
		}
		
		List<String> picUrlArr = new ArrayList<String>();
		for (String picUrl : picUrlMap.keySet()) {
			picUrlArr.add(picUrl);
		}
		//压缩图片
		List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(
				Integer.parseInt(maxWidth), picUrlArr,picType);
		for (PicInfo picInfo : picInfoList) {
			picUrlMap.put(picInfo.getPicOldUrl(), picInfo.getPicNewUrl());
			picUrlObjMap.put(picInfo.getPicNewUrl(), picInfo);
		}
		
		for (String productCode : productInfoMap.keySet()) {
			String mainpicUrl = productInfoMap.get(productCode).getMainpicUrl();
			if (StringUtils.isNotBlank(mainpicUrl)) {
				productInfoMap.get(productCode).setMainpicUrl(picUrlMap.get(mainpicUrl));
			}
		}
		
		HomeColumnPc homeColumn = new HomeColumnPc();
		homeColumn.setColumnID(columnMap.get("column_code"));
		homeColumn.setShowName(columnMap.get("show_name")); // 是否显示栏目名称
		if (!"449746250002".equals(columnMap.get("show_name"))) {
			homeColumn.setColumnName(columnMap.get("column_name"));
		}
		homeColumn.setColumnType(columnMap.get("column_type"));
		homeColumn.setStartTime(columnMap.get("start_time"));
		homeColumn.setEndTime(columnMap.get("end_time"));
		homeColumn.setIsShowmore(columnMap.get("is_showmore")); // 是否显示更多
		homeColumn.setShowmoreLinktype(columnMap
				.get("showmore_linktype"));
		if (CATEGORY.equals(columnMap.get("showmore_linktype"))) {
			homeColumn.setShowmoreLinkvalue(categoryNameMap
					.get(columnMap.get("showmore_linkvalue")));
		} else {
			homeColumn.setShowmoreLinkvalue(columnMap
					.get("showmore_linkvalue"));
		}
		homeColumn.setShowmoreTitle(columnMap.get("showmore_title"));
		String columnType = columnMap.get("column_type");

		List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
		// 不是“闪购”与“今日直播”时
		if (!"4497471600010011".equals(homeColumn.getColumnType())
				&& !"4497471600010010".equals(homeColumn.getColumnType())) {
			for (MDataMap columnContentMap : columnContentMapList) {
				HomeColumnContent columnContent = new HomeColumnContent();
				String floorModel = columnContentMap.get("floor_model");
				// 压缩图片
				String pic = picUrlMap.get(columnContentMap.get("picture"));
				columnContent.setPicture(StringUtils.isEmpty(pic) ? "" : pic);
				columnContent.setStartTime(columnContentMap.get("start_time"));
				columnContent.setEndTime(columnContentMap.get("end_time"));
				columnContent.setTitle(columnContentMap.get("title"));
				columnContent.setTitleColor(columnContentMap.get("title_color"));
				columnContent.setDescription(columnContentMap.get("description"));
				columnContent.setDescriptionColor(columnContentMap.get("description_color"));
				columnContent.setShowmoreLinktype(columnContentMap.get("showmore_linktype"));
				
				String showmore_linkvalue = columnContentMap.get("showmore_linkvalue");
				if (StringUtils.isNotBlank(showmore_linkvalue) && 
						"449746250001".equals(columnContentMap.get("skip_place"))) {
					String place_time = columnContentMap.get("place_time");
					if (StringUtils.isNotBlank(place_time)) {
						try {
							Long timeLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(place_time).getTime();
							String timeParam = "";
							if (showmore_linkvalue.indexOf("?") > 0) {
								timeParam = "&time=";
							}else{
								timeParam = "?time=";
							}
							showmore_linkvalue += (timeParam+timeLong.toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				// 链接类型为商品分类
				if (CATEGORY.equals(columnContentMap.get("showmore_linktype"))) {
					columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue));
				} 
				// 链接类型为“商品详情”时查询商品信息
				else if (PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))) {
					HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
					columnContent.setProductInfo(productInfo);
				// 连接类型为URL
				} else if (URL.equals(columnContentMap.get("showmore_linktype"))
						&& "449746250001".equals(columnContentMap.get("is_share"))) {
					columnContent.setShowmoreLinkvalue(
							StringUtils.isEmpty(showmore_linkvalue) ? "" : showmore_linkvalue);
					
					if (VersionHelper.checkServerVersion("3.5.51.54")) {
						// 链接类型为"URL"时并且是否分享为"是"时,设置“分享图片”，“分享标题”，“分享内容”，“分享链接”
						columnContent.setShareTitle(columnContentMap.get("share_title"));
						columnContent.setShareContent(columnContentMap.get("share_content"));
						columnContent.setShareLink(columnContentMap.get("share_link"));
						columnContent.setSharePic(columnContentMap.get("share_pic"));

						// “分享链接需要进行拼接重写”
						// http://share-static.sycdn.ichsy.com/cfamily/web/cfamilypage/specialshare?link=http://share.ichsy.com/index/20150525mr.html&alt=%E6%97%85%E8%A1%8C%E5%A6%86%E5%A4%87
						String shareUrlHead = bConfig("familyhas.share_url_head");
						String alt = "&wxTilte=";
						String shareTitle = columnContentMap.get("share_title");
						if (StringUtils.isEmpty(shareTitle)) {
							shareTitle = bConfig("familyhas.share_title");
						}
						String shareURL = columnContentMap.get("share_link");
						// 为了兼容历史数据分享链接为空的情况
						if (StringUtils.isEmpty(shareURL)) {
							shareURL = showmore_linkvalue;
						}
						String shareLink = shareUrlHead;
						try {
							shareLink += (URLEncoder.encode((shareURL.trim()),"utf8")+alt+URLEncoder.encode(shareTitle,"utf8"));
						} catch (UnsupportedEncodingException e) {
						}
						columnContent.setShareLink(shareLink);
					}
				}else {
					columnContent.setShowmoreLinkvalue(StringUtils.isEmpty(showmore_linkvalue) ? "" : showmore_linkvalue);
				}
				
				columnContent.setPosion(Integer.parseInt(StringUtils.isEmpty(
						columnContentMap.get("position")) ? "0" : columnContentMap.get("position")));
				columnContent.setIsShare(columnContentMap.get("is_share"));
					
				//栏目类型为一栏广告时，返回图片高度
				if ("4497471600010002".equals(homeColumn.getColumnType())&&StringUtils.isNotBlank(columnContent.getPicture())) {
						PicInfo oneColumnPic = picUrlObjMap.get(columnContent.getPicture());
						columnContent.setPicHeight(null == oneColumnPic ? 0 : oneColumnPic.getHeight());
				}
				if("4497471600010015".equals(columnType)){
					//底部广告位
					if("4497471600220003".equals(floorModel)){
						homeColumn.getAd3List().add(columnContent);
					//中间轮播广告位
					}else if("4497471600220002".equals(floorModel)){
						homeColumn.getAd1List().add(columnContent);
					//左侧底部品牌位
					}else if("4497471600220004".equals(floorModel)){
						homeColumn.getLogoList().add(columnContent);
						//左侧广告位
					}else if("4497471600220001".equals(floorModel)){
						homeColumn.setAd2linktype(columnContent.getShowmoreLinktype());
						homeColumn.setAd2linkvalue(columnContent.getShowmoreLinkvalue());
						homeColumn.setAd2picture(columnContent.getPicture());
					//右侧热销商品位
					}else if("4497471600220005".equals(floorModel)){
						homeColumn.getHotPointList().add(columnContent);
					}
				}else{
					contentList.add(columnContent);
				}
			}
		} else {
			// 闪购
			if ("4497471600010011".equals(homeColumn.getColumnType())) {
				contentList = hcService.getFlashActivityPc(sellerCode,maxWidth,picType,getChannelId());
				// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
				if (null == contentList
						|| contentList.size() < 1
						|| systemTime.compareTo(contentList.get(0)
								.getEndTime()) > 0) {
				} else {
					// 闪购栏目的开始结束时间设置为闪购活动的开始结束时间
					homeColumn.setStartTime(contentList.get(0)
							.getStartTime());
					homeColumn.setEndTime(contentList.get(0)
							.getEndTime());
				}
			}
			// TV直播
			if ("4497471600010010".equals(homeColumn.getColumnType())) {
				contentList = hcService.getTVDataList(buyType,maxWidth,picType,userCode , "4497471600100003" , "",getChannelId());
			}
		}
		
		homeColumn.setContentList(contentList); // 内容List
		columnList.add(homeColumn);
		result.setSysTime(systemTime);
		result.setColumnList(columnList);
		return result;
	}
}
