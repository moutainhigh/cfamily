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

import com.cmall.familyhas.api.input.ApiHomeColumnInput;
import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnContentProductInfo;
import com.cmall.familyhas.api.result.ApiTVColumnResult;
import com.cmall.familyhas.model.TopThreeColumn;
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
 * 惠家有TV直播栏目(PC端调用)
 * 
 * @author zhaoxq
 * 
 */
public class ApiTVColumn extends
		RootApiForVersion<ApiTVColumnResult, ApiHomeColumnInput> {

	public ApiTVColumnResult Process(ApiHomeColumnInput inputParam,
			MDataMap mRequestMap) {
		if (StringUtils.isBlank(inputParam.getBuyerType())) {
			inputParam.setBuyerType("4497469400050002");
		}
		if (StringUtils.isBlank(inputParam.getViewType())) {
			inputParam.setViewType("4497471600100001");
		}
		String channelId = getChannelId();
		ApiTVColumnResult result = new ApiTVColumnResult();
		List<HomeColumn> columnList = new ArrayList<HomeColumn>(); // 其他栏目List
		List<HomeColumn> topThreeColumnList = new ArrayList<HomeColumn>(); // 前三个栏目List
		TopThreeColumn topThreeColumn = new TopThreeColumn(); // 前三个栏目
		HomeColumnService hcService = new HomeColumnService();
		String systemTime = DateUtil.getSysDateTimeString();
		ProductService pService = new ProductService();
		String userType = "";
		String maxWidth = "0";
		String picType = "";
		final String PRODUCT_VIEW = "4497471600020004"; // 链接类型为商品详情
		final String CATEGORY = "4497471600020003"; // 链接类型为分类
		final String URL = "4497471600020001"; // 链接类型为URL
		String sellerCode = getManageCode();
		String userCode = (getFlagLogin() ? getOauthInfo().getUserCode() : "");
		maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0"
				: inputParam.getMaxWidth(); // 最大宽度
		picType = StringUtils.isBlank(inputParam.getPicType()) ? ""
				: inputParam.getPicType(); // 图片格式
		userType = inputParam.getBuyerType();
		String sFields = "column_code,column_name,column_type,start_time,end_time,is_showmore,showmore_title,interval_second,showmore_linktype,showmore_linkvalue,show_name,pic_url";
		String sOrders = "position asc , create_time desc";
		String sWhere = " start_time <= '"
				+ systemTime
				+ "' and end_time > '"
				+ systemTime
				+ "' and is_delete='449746250002' and release_flag='449746250001' and seller_code='"+sellerCode+"' and view_type='"+inputParam.getViewType()+"'";
		List<MDataMap> columnMapList = DbUp.upTable("fh_pctv_column")
				.queryAll(sFields, sOrders, sWhere, null);
		if (null != columnMapList) {
			StringBuilder cols = new StringBuilder("''");
			for(MDataMap m : columnMapList) {
				cols.append(",'").append(m.get("column_code")).append("'");
			}
			
			String sFieldsContent = "";
			String sWhereContent = " start_time <='" + systemTime
					+ "' and end_time > '" + systemTime
					+ "' and is_delete='449746250002'";
			
			// 限制查询范围，解决查询无效数据问题
			if(cols.length() > 0){
				sWhereContent += " and column_code in("+cols+")";	
			}
			
			List<MDataMap> columnContentMapList = DbUp.upTable(
					"fh_apphome_column_content").queryAll(sFieldsContent,
					"position asc,start_time desc", sWhereContent, null);
			// 获取所有要查询商品信息的商品编号
			MDataMap productCodeMap = new MDataMap();
			// 获取所有要查询的分类编号，key:categoryCode,value:0
			MDataMap categoryCodeMap = new MDataMap();
			// 图片Map，key:picOldUrl，value:picNewUrl
			MDataMap picUrlMap = new MDataMap();

			// 图片Map，key:picOldUrl，value:picNewUrl
			Map<String,PicInfo> picUrlObjMap = new HashMap<String,PicInfo>();
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
			for (int i = 0; i < columnMapList.size(); i++) {
				MDataMap mDataMap = columnMapList.get(i);
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
			}

			List<String> productCodeArr = new ArrayList<String>();
			for (String productCode : productCodeMap.keySet()) {
				productCodeArr.add(productCode);
			}
			List<String> categoryCodeArr = new ArrayList<String>();
			for (String categoryCode : categoryCodeMap.keySet()) {
				categoryCodeArr.add(categoryCode);
			}
			
			Map<String, HomeColumnContentProductInfo> productInfoMap = hcService.getProductInfo(productCodeArr, userType,userCode,channelId);
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
			
			for (int i = 0; i < columnMapList.size(); i++) {
				MDataMap columnMap = columnMapList.get(i);

				HomeColumn homeColumn = new HomeColumn();
				List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();

				homeColumn.setShowName(columnMap.get("show_name")); // 是否显示栏目名称
				if (!"449746250002".equals(columnMap.get("show_name"))) {
					homeColumn.setColumnName(columnMap.get("column_name"));
				}
				homeColumn.setColumnID(columnMap.get("column_code"));
				homeColumn.setPageType("1");
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
				if(StringUtils.isNotBlank(columnMap.get("interval_second")))homeColumn.setIntervalSecond(Integer.parseInt(columnMap.get("interval_second")));
				// 不是“闪购”与“今日直播”时
				if (null != columnContentMapList
						&& !"4497471600010011".equals(homeColumn
								.getColumnType())
						&& !"4497471600010010".equals(homeColumn
								.getColumnType())) {
					for (MDataMap columnContentMap : columnContentMapList) {
						if (columnMap.get("column_code").equals(
								columnContentMap.get("column_code"))) {
							HomeColumnContent columnContent = new HomeColumnContent();
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
							columnContent.setFloorModel(columnContentMap.get("floor_model"));
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
							
							if (CATEGORY.equals(columnContentMap.get("showmore_linktype"))) {
								columnContent.setShowmoreLinkvalue(categoryNameMap.get(showmore_linkvalue));
							} else {
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
							// 链接类型为“商品详情”时查询商品信息
							if (PRODUCT_VIEW.equals(columnContentMap.get("showmore_linktype"))) {
								HomeColumnContentProductInfo productInfo = productInfoMap.get(showmore_linkvalue);
								columnContent.setProductInfo(productInfo);
							} else if (URL.equals(columnContentMap
									.get("showmore_linktype"))
									&& "449746250001".equals(columnContentMap
											.get("is_share"))) {
								if (VersionHelper
										.checkServerVersion("3.5.51.54")) {
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
							}
							contentList.add(columnContent);
						}
					}
				} else {
					// 闪购
					if ("4497471600010011".equals(homeColumn.getColumnType())) {
						contentList = hcService.getFlashActivityPc(sellerCode,maxWidth,picType,channelId);
						// 闪购时，闪购商品列表为空，或是闪购活动结束时间小于当前时间，则不展示闪购
						if (null == contentList
								|| contentList.size() < 1
								|| systemTime.compareTo(contentList.get(0)
										.getEndTime()) > 0) {
							continue;
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
						contentList = hcService.getTVDataList(userType,maxWidth,picType,userCode , "4497471600100003" , "",channelId); 
						if(contentList != null && contentList.size()>0){
							result.setTvUrl(contentList.get(0).getVideoUrlTV());
						}
					}
				}
				homeColumn.setContentList(contentList); // 内容List
				// 轮播图，二栏广告，导航栏时插入到前三个
				if ("4497471600010001".equals(homeColumn.getColumnType())
						|| "4497471600010003"
								.equals(homeColumn.getColumnType())
						|| "4497471600010004"
								.equals(homeColumn.getColumnType())) {
					if (null != homeColumn.getContentList()
							&& homeColumn.getContentList().size() > 0) {
						topThreeColumnList.add(homeColumn);
					}
				} else {
					if (null != homeColumn.getContentList()
							&& homeColumn.getContentList().size() > 0) {
						columnList.add(homeColumn);
					}
				}
			}
		}
		// topThreeColumnList数组里第一个元素一定是轮播图，第二个元素一定是二栏广告，第三个元素一定是导航栏
		for (int i = 0; i < 3; i++) {
			boolean flagExist = false; // 标志是否存在特定位置特定类型的栏目，如果不存在，则放入new
										// HomeColumn();
			for (int k = 0; k < topThreeColumnList.size(); k++) {
				if (i == 0
						&& "4497471600010001".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 轮播广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
				if (i == 1
						&& "4497471600010003".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 二栏广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
				if (i == 2
						&& "4497471600010004".equals(topThreeColumnList.get(k)
								.getColumnType())) {
					// 二栏广告
					topThreeColumnList.add(i, topThreeColumnList.get(k));
					topThreeColumnList.remove(k + 1);
					flagExist = true;
					break;
				}
			}
			if (!flagExist) {
				topThreeColumnList.add(i, new HomeColumn());
			}
		}
		topThreeColumn.setTopThreeColumnList(topThreeColumnList);
		result.setSysTime(systemTime);
		result.setColumnList(columnList);
		result.setTopThreeColumn(topThreeColumn);
		return result;
	}
}
