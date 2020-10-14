package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.model.PicInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topdo.TopUp;


/**
 * 首页版式栏目
 * @author ligj 
 *
 */
public class HomeColumn {

	@ZapcomApi(value = "栏目名称")
	private String columnName = "";

	@ZapcomApi(value = "栏目名称颜色")
	private String columnNameColor = "";

	@ZapcomApi(value = "栏目ID")
	private String columnID = "";
	
	@ZapcomApi(value = "页面类型",remark = "0：首页，1：TV页")
	private String pageType = "";
	
	@ZapcomApi(value = "开始时间")
	private String startTime = "";
	
	@ZapcomApi(value = "结束时间")
	private String endTime = "";
	
	@ZapcomApi(value = "服务器时间")
	private String sysTime = DateUtil.getSysDateTimeString();

	@ZapcomApi(value = "栏目类型",remark=
					"4497471600010001：轮播广告\n"+
					"4497471600010002：一栏广告\n"+
					"4497471600010003：二栏广告\n"+
					"4497471600010004：导航栏\n"+
					"4497471600010005：一栏推荐\n"+
					"4497471600010006：右两栏推荐\n"+
					"4497471600010007：左两栏推荐\n"+
					"4497471600010008：商品推荐\n"+
					"4497471600010009：两栏多行推荐\n" +
					"4497471600010010：TV直播 \n" +
					"4497471600010011：闪购 \n" +
					"4497471600010012：通知模板 \n" +
					"4497471600010013：三栏两行推荐 \n" +
					"4497471600010014：两栏两行推荐\n"+
					"4497471600010015：楼层模板\n"+ 
					"4497471600010022：秒杀模板\n"+
					"4497471600010027：商品评价\n"+
					"4497471600010028：分销模板\n"+
					"4497471600010029：买家秀列表\n"+
					//"4497471600010030：买家秀入口"
					"449748590001：买家秀入口样式一\n"+
					"449748590002：买家秀入口样式二\n"+
					"4497471600010034：拼团大图样式")
	private String columnType = "";
	
	@ZapcomApi(value = "内容List")
	private List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();

	@ZapcomApi(value = "是否显示更多",remark="链接类型为“URL”此字段有效;\n" +
								"449746250001：是\n"+
								"449746250002：否")
	private String isShowmore = "";
	
	@ZapcomApi(value = "‘显示更多’标题",remark="显示更多时此字段有效")
	private String showmoreTitle = "";
	
	@ZapcomApi(value = "‘显示更多’链接类型",remark="显示更多时此字段有效；\n"+
										"4497471600020001：URL\n"+
										"4497471600020002：关键词搜索\n"+
										"4497471600020003：分类搜索\n"+
										"4497471600020004：商品详情\n"
										+ "4497471600020009：拼团列表"
										+ "4497471600020010：秒杀列表（544新增）"
										+"4497471600020015:拼团大图（564新增）需要做版本兼容，564以下版本，替换成拼团列表")
	private String showmoreLinktype = "";
	
	@ZapcomApi(value = "‘显示更多’链接值",remark="显示更多时此字段有效")
	private String showmoreLinkvalue = "";

	@ZapcomApi(value = "轮播间隔",remark="单位:秒，栏目类型为通知栏时此字段有效:4497471600010012")
	private int intervalSecond = 1;

	@ZapcomApi(value = "模版背景图",remark="栏目类型为导航栏时此字段有效：4497471600010004")
	private PicInfo columnBgpic = new PicInfo();

	
	@ZapcomApi(value = "是否显示栏目名称",remark="449746250001：是，449746250002：否")
	private String showName = "449746250001";
	
	@ZapcomApi(value = "是否保留边距" ,remark = "4497479100010001:否；4497479100010002:是")
	private String hasEdgeDistance = "4497479100010001";
	
	@ZapcomApi(value = "功能圈每行显示个数",remark="4497480100030001:一行4栏，4497480100030002:一行5栏")
	private String columnsPerRow;
	
	@ZapcomApi(value = "未开始的TV节目档数",remark="栏目类型为TV直播滑动时此字段有效：4497471600010016", demo = "4497471600350001")
	private String futureProgram;
	
	@ZapcomApi(value = "多栏广告栏数",remark="多栏广告栏数,多栏广告此字段有效;4497471600360001-4497471600360004依次为一栏-四栏，")
	private String numLan = "0";
	
	@ZapcomApi(value="分类编号",remark="针对跳转为商品列表类型,搜索为关键字‘分类名称’,再加上分类编号作为优化补充")
	private String categoryCode="";
	
	@ZapcomApi(value="首页定位栏目链接类型",remark="商品评价:4497471600580001")
	private String homePositionLinkType="";
	
	@ZapcomApi(value="区分猜你喜欢还是商品评价",remark="猜你喜欢:4497471600590001;商品评价:4497471600590002")
	private String guessLikeOrEvaluation="";
	
	@ZapcomApi(value="视频模板是否开启直播互动标识",remark="0:不开启,1:开启")
	private String ifShowLiveInteractionFlag=TopUp.upConfig("familyhas.ifShowLiveInteractionFlag");
	
	@ZapcomApi(value="是否显示评价或晒单送积分顶部悬浮提醒框",remark="有待评价或者待晒单的订单则显示提醒框:0否1是")
	private String isRemind = "0";
	
	@ZapcomApi(value="评价+晒单可以获得的总积分",remark="如果有待评价或者待晒单的订单,计算可获得积分总和")
	private String total_integral = "0";
	
	@ZapcomApi(value="可评价晒单的商品主图",remark="如果有待评价或者待晒单的订单,去最新订单商品图")
	private String canEvaluateMainpic = "";
	
	@ZapcomApi(value = "评价中心标签", remark = "1 待评价、2 待晒单 、 3 已评价 ")
	private String tagType = "1";
	
	@ZapcomApi(value = "买家秀列表List", remark="买家秀列表模板/买家秀入口样式1 共用")
	private List<BuyerShow> buyerShowList = new ArrayList<BuyerShow>();
	
	/*@ZapcomApi(value="买家秀入口展示样式", remark="样式1:449748590001; 样式2:449748590002")
	private String showStyle = "";*/
	
	@ZapcomApi(value = "买家秀图标", remark = "买家秀入口样式2返回")
	private String buyerShowIcon = "";
	
	@ZapcomApi(value = "买家秀的用户头像List",  remark="买家秀入口样式2返回使用")
	private List<String> buyerShowAvatarList = new ArrayList<String>();
	
	@ZapcomApi(value="买家秀内容分页总页码")
	private int totalPage = 1;

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getIfShowLiveInteractionFlag() {
		return ifShowLiveInteractionFlag;
	}

	public void setIfShowLiveInteractionFlag(String ifShowLiveInteractionFlag) {
		this.ifShowLiveInteractionFlag = ifShowLiveInteractionFlag;
	}
	
	public String getBuyerShowIcon() {
		return buyerShowIcon;
	}

	public void setBuyerShowIcon(String buyerShowIcon) {
		this.buyerShowIcon = buyerShowIcon;
	}

	public List<String> getBuyerShowAvatarList() {
		return buyerShowAvatarList;
	}

	public void setBuyerShowAvatarList(List<String> buyerShowAvatarList) {
		this.buyerShowAvatarList = buyerShowAvatarList;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(String isRemind) {
		this.isRemind = isRemind;
	}

	public String getTotal_integral() {
		return total_integral;
	}

	public void setTotal_integral(String total_integral) {
		this.total_integral = total_integral;
	}

	public String getCanEvaluateMainpic() {
		return canEvaluateMainpic;
	}

	public void setCanEvaluateMainpic(String canEvaluateMainpic) {
		this.canEvaluateMainpic = canEvaluateMainpic;
	}

	public List<BuyerShow> getBuyerShowList() {
		return buyerShowList;
	}

	public void setBuyerShowList(List<BuyerShow> buyerShowList) {
		this.buyerShowList = buyerShowList;
	}


	
	public String getColumnNameColor() {
		return columnNameColor;
	}

	public void setColumnNameColor(String columnNameColor) {
		this.columnNameColor = columnNameColor;
	}
	
	public String getGuessLikeOrEvaluation() {
		return guessLikeOrEvaluation;
	}

	public void setGuessLikeOrEvaluation(String guessLikeOrEvaluation) {
		this.guessLikeOrEvaluation = guessLikeOrEvaluation;
	}

	public String getHomePositionLinkType() {
		return homePositionLinkType;
	}

	public void setHomePositionLinkType(String homePositionLinkType) {
		this.homePositionLinkType = homePositionLinkType;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	

	public String getNumLan() {
		return numLan;
	}

	public void setNumLan(String numLan) {
		this.numLan = numLan;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public String getColumnID() {
		return columnID;
	}

	public void setColumnID(String columnID) {
		this.columnID = columnID;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public List<HomeColumnContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<HomeColumnContent> contentList) {
		this.contentList = contentList;
	}

	public String getIsShowmore() {
		return isShowmore;
	}

	public void setIsShowmore(String isShowmore) {
		this.isShowmore = isShowmore;
	}

	public String getShowmoreTitle() {
		return showmoreTitle;
	}

	public void setShowmoreTitle(String showmoreTitle) {
		this.showmoreTitle = showmoreTitle;
	}

	public String getShowmoreLinktype() {
		return showmoreLinktype;
	}

	public void setShowmoreLinktype(String showmoreLinktype) {
		this.showmoreLinktype = showmoreLinktype;
	}

	public String getShowmoreLinkvalue() {
		return showmoreLinkvalue;
	}

	public void setShowmoreLinkvalue(String showmoreLinkvalue) {
		this.showmoreLinkvalue = showmoreLinkvalue;
	}

	public int getIntervalSecond() {
		return intervalSecond;
	}

	public void setIntervalSecond(int intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public PicInfo getColumnBgpic() {
		return columnBgpic;
	}

	public void setColumnBgpic(PicInfo columnBgpic) {
		this.columnBgpic = columnBgpic;
	}

	public String getHasEdgeDistance() {
		return hasEdgeDistance;
	}

	public void setHasEdgeDistance(String hasEdgeDistance) {
		this.hasEdgeDistance = hasEdgeDistance;
	}

	public String getColumnsPerRow() {
		return columnsPerRow;
	}

	public void setColumnsPerRow(String columnsPerRow) {
		this.columnsPerRow = columnsPerRow;
	}

	public String getFutureProgram() {
		return futureProgram;
	}

	public void setFutureProgram(String futureProgram) {
		this.futureProgram = futureProgram;
	}
	
}
