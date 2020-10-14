package com.cmall.familyhas.api;


import java.math.BigDecimal;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiProductCommentAddCfInput;
import com.cmall.familyhas.api.model.AddCommentModel;
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.VideoUtils;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.util.XSSUtils;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * @title 增加评论
 * @author shenghaoran
 *
 */

public class ApiProductCommentAddCf extends RootApiForToken<RootResultWeb, ApiProductCommentAddCfInput>{
	
	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	/** 图片审核通过  */
	final String pictureAccept = "449747510001";
	/** 图片审核拒绝 */
	final String pictureRefuse = "449747510002";
	/**
	 * 上线下线：449746530001、449746530002  待审核 审核通过 审核拒绝：4497172100030001、4497172100030002、4497172100030003
	 */
	final String hasAccept = "4497172100030002";
	final String all = "全部";
	final String showPhoto = "有图";
	final String highPraise = "好评";
	final String commonPraise = "中评";
	final String lowPraise = "差评";
	/**图片审核通过*/
	final String headUrlAccept = "449746600001";
	final String defaultComment = "没有填写评价！";
	final String online = "449746530001";
	
	Map<String,String> gradeTypeMap = new HashMap<String, String>();
	
	{
		gradeTypeMap.put("1", "差评");
		gradeTypeMap.put("2", "中评");
		gradeTypeMap.put("3", "中评");
		gradeTypeMap.put("4", "好评");
		gradeTypeMap.put("5", "好评");
	}
	
	public RootResultWeb Process(ApiProductCommentAddCfInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		// 初评和晒单的时候用,是否分享到买家秀,0否;1是 (好评有文字+晒单显示) (不满足条件不传)
		String is_share = inputParam.getIs_share();
		
		/**
		 * 评论内容新增过滤
		 */
		List<AddCommentModel> comments = inputParam.getComments();
		WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();
		String access_token = wxMusicAlbumService.getToken();
		for(AddCommentModel comment : comments) {
			String content = comment.getComment_content();
			if(StringUtils.isNotEmpty(content)) {
				boolean chekMessageFlag = new VideoUtils().checkContent(content, access_token);
				if(!chekMessageFlag) {//有违规消息。
					result.setResultCode(0);
					result.setResultMessage("您的消息：【"+content+"】存在违规信息，请核实后重新提交");
					return result;
				}
			}
		}
		
		for(AddCommentModel comment : inputParam.getComments()){
			if(XSSUtils.hasXSS(comment.getComment_content())) {
                result.setResultCode(0);
				result.setResultMessage("评价内容包含非法字符！");
				return result;
			}
		}
		
		for(AddCommentModel comment : inputParam.getComments()){
			// 非评价后晒图需要检查评价内容
			if(!"2".equals(inputParam.getFlag())){
				if(StringUtils.isBlank(comment.getComment_content())){
	                result.setResultCode(0);
					result.setResultMessage("评价内容不能为空");
					break;
				}
			}
			
			// 晒图的评价必须上传图片
			if(comment.getComment_photo().isEmpty()&&comment.getCcvid().isEmpty() && "2".equals(inputParam.getFlag())){
                result.setResultCode(0);
				result.setResultMessage("请上传图片或视频");
				break;
			}
			
			// 忽略参数异常的评价
			if(StringUtils.isBlank(comment.getOrder_code()) || StringUtils.isBlank(comment.getSku_code())){
                continue;
			}
			
			MDataMap mData = null;
			// 晒图或追评
			if("3".equals(inputParam.getFlag()) || "2".equals(inputParam.getFlag()) || "4".equals(inputParam.getFlag())){
				if(StringUtils.isNotBlank(inputParam.getUid())){
					mData = DbUp.upTable("nc_order_evaluation").one("order_code",comment.getOrder_code(),"uid",inputParam.getUid());
				}
				if(mData == null){
	                result.setResultCode(0);
					result.setResultMessage("原评价不存在");
					break;
				}else if(!getUserCode().equalsIgnoreCase(mData.get("order_name"))){
	                result.setResultCode(0);
					result.setResultMessage("原评价人和当前用户不一致");
					break;
				}
				
				if("2".equals(inputParam.getFlag())){
					if(StringUtils.isNotBlank(mData.get("oder_photos"))||StringUtils.isNotBlank(mData.get("ccvids"))){
						result.setResultCode(0);
						result.setResultMessage("您已经晒图了");
						break;
					}
				}
				
				if("3".equals(inputParam.getFlag())){
					// 追评前必须晒图
					if(StringUtils.isBlank(mData.get("oder_photos"))&&StringUtils.isBlank(mData.get("ccvids"))){
						result.setResultCode(0);
						result.setResultMessage("请先晒图后再进行追评");
						break;
					}
					
					// 只支持追评一次
					if(DbUp.upTable("nc_order_evaluation_append").dataCount("", new MDataMap("evaluation_uid",mData.get("uid"))) > 0){
						result.setResultCode(0);
						result.setResultMessage("您已经追评过了");
						break;
					}
				}
			}else{
				// 忽略下单人和评论人不一致的评价
				String buyer = (String)DbUp.upTable("oc_orderinfo").dataGet("buyer_code", "", new MDataMap("order_code", comment.getOrder_code()));
				if(!getUserCode().equalsIgnoreCase(buyer)){
	                result.setResultCode(0);
					result.setResultMessage("下单人和当前用户不一致");
					break;
				}
				mData = DbUp.upTable("nc_order_evaluation").one("order_code",comment.getOrder_code(),"order_skuid", comment.getSku_code());
				if(mData != null){
	                result.setResultCode(0);
					result.setResultMessage("您已经评价过了");
					break;
				}
			} 									
			
			if("2".equals(inputParam.getFlag())){  // 晒单
				addCommentPic(comment, mData, is_share);
			}else if("3".equals(inputParam.getFlag())){ // 追评
				addCommentAppend(comment, mData);
			}else if("4".equals(inputParam.getFlag())){ // 修改差评
				editBadComment(comment, mData);
			}else{  // 默认新增评价
				addNewComment(comment, is_share);
			}
			
		}
		
		return result;
	}
	
	/**
	 * 新增评价
	 */
	private void addNewComment(AddCommentModel comment, String is_share){
		MDataMap headUrl = DbUp.upTable("mc_extend_info_star").oneWhere("member_avatar", "", "",  "member_code",getUserCode(),"app_code",getManageCode());
		String headIconUrl = "";
		if(headUrl != null) headIconUrl = StringUtils.trimToEmpty(headUrl.get("member_avatar"));
		
		MDataMap mData = new MDataMap();
		mData.put("grade_type", gradeTypeMap.get(comment.getGrade()));
		mData.put("order_name", getUserCode());
		mData.put("manage_code", getManageCode());
		mData.put("user_mobile", this.getOauthInfo().getLoginName());
		mData.put("grade", comment.getGrade());
		mData.put("order_assessment", replaceEmoji(comment.getComment_content()));
		mData.put("head_icon_url", headIconUrl);
		mData.put("oder_photos", StringUtils.join(comment.getComment_photo(),"|"));
		
		mData.put("oder_creattime", DateHelper.upNow());
		mData.put("check_flag", hasAccept);  //审核状态
		mData.put("flag_show", online);  //是否上下线（也可叫显示隐藏）
		
		MDataMap skuMap = DbUp.upTable("pc_skuinfo").one("sku_code", comment.getSku_code());
		if(skuMap == null){
			return;
		}
		mData.put("sku_name", skuMap.get("sku_name"));
		mData.put("order_code", comment.getOrder_code()); //订单编号
		mData.put("order_skuid", skuMap.get("sku_code"));
		mData.put("product_code", skuMap.get("product_code"));
		mData.put("pic_status", pictureAccept);//图片默认审核通过
		mData.put("ccvids", comment.getCcvid());
		boolean flag = this.checkPic(comment.getComment_photo());
		if(!flag) {
			mData.put("pic_status", "449747510002");//图片违规审核不通过
		}
		// 是否分享到买家秀,校验是否满足分享买家秀条件:上传图片或视频,且有评价文字的好评
		if(is_share.equals("1") && (StringUtils.isNotBlank(mData.get("oder_photos")) || StringUtils.isNotBlank(mData.get("ccvids"))) && Integer.parseInt(comment.getGrade())>=4 && StringUtils.isNotBlank(mData.get("order_assessment"))) {
			is_share = "1";
		}else {
			is_share = "0";
		}
		mData.put("is_share", is_share);
		mData.put("is_cancel", "0");
		String uid = DbUp.upTable("nc_order_evaluation").dataInsert(mData);
		
		// 如果分享到买家秀,则买家秀表新增
		if(is_share.equals("1")) {
			MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("evaluation_uid",uid);
			if(buyer_show == null) {
				// 买家秀表新增
				MDataMap insertMap = new MDataMap();
				insertMap.put("evaluation_uid", uid);
				insertMap.put("create_time", DateUtil.getSysDateTimeString());
				insertMap.put("check_status", "449748580001");
				insertMap.put("is_delete", "0");
				insertMap.put("member_code", getUserCode());
				DbUp.upTable("nc_buyer_show_info").dataInsert(insertMap);
			}
		}
		
		//评论送积分		
		String custId = plusServiceAccm.getCustId(getUserCode());// 家有客代号
		if(StringUtils.isNotBlank(custId)&&Integer.parseInt(comment.getGrade())>=4){
			List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip,limit_value", "", "", new MDataMap());
			BigDecimal integral = new BigDecimal(0);
			for(MDataMap map:evaluateList){
				if(map.get("evaluate_type").equals("评价文字")){
					int limit = Integer.parseInt(map.get("limit_value"));
					if(comment.getComment_content().length()>=limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
				if(map.get("evaluate_type").equals("评价图片")){
					int limit = Integer.parseInt(map.get("limit_value"));
					int size = comment.getComment_photo().size();
					if(StringUtils.isNotBlank(comment.getCcvid())) {
						size += 1;
					}
					if(size >= limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
				// 如果分享到买家秀,则赠送积分
				if(is_share.equals("1")) {
					if(map.get("evaluate_type").equals("买家秀")){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
			}
			
			if(integral.intValue()>0){
				this.giveIntegral(integral, custId, comment.getOrder_code(), comment.getSku_code());
			}
		}
	}
	
	/**
	 * 晒图
	 */
	private void addCommentPic(AddCommentModel comment,MDataMap mData, String is_share){
		if(StringUtils.isBlank(mData.get("oder_photos")) && StringUtils.isBlank(mData.get("ccvids")) && (!comment.getComment_photo().isEmpty() || StringUtils.isNotBlank(comment.getCcvid()))){
			MDataMap updateMap = new MDataMap();
			updateMap.put("zid", mData.get("zid"));
			updateMap.put("uid", mData.get("uid"));
			updateMap.put("oder_photos", StringUtils.join(comment.getComment_photo(),"|"));
			updateMap.put("ccvids", comment.getCcvid());
			boolean flag = this.checkPic(comment.getComment_photo());
			if(!flag) {
				updateMap.put("pic_status", "449747510002");//图片违规审核不通过
			}
			// 是否分享到买家秀,校验是否满足分享买家秀条件:上传图片或视频,且有评价文字的好评
			if(is_share.equals("1") && Integer.parseInt(mData.get("grade"))>=4 && StringUtils.isNotBlank(mData.get("order_assessment"))) {
				is_share = "1";
			}else {
				is_share = "0";
			}
			updateMap.put("is_share", is_share);
			DbUp.upTable("nc_order_evaluation").update(updateMap);
			
			// 如果分享到买家秀,则买家秀表新增
			if(is_share.equals("1")) {
				MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("evaluation_uid",mData.get("uid"));
				if(buyer_show == null) {
					// 买家秀表新增
					MDataMap insertMap = new MDataMap();
					insertMap.put("evaluation_uid", mData.get("uid"));
					insertMap.put("create_time", DateUtil.getSysDateTimeString());
					insertMap.put("check_status", "449748580001");
					insertMap.put("is_delete", "0");
					insertMap.put("member_code", getUserCode());
					DbUp.upTable("nc_buyer_show_info").dataInsert(insertMap);
				}
			}
		}
		
		//评论送积分		
		String custId = plusServiceAccm.getCustId(getUserCode());// 家有客代号
		if(StringUtils.isNotBlank(custId)&&Integer.parseInt(mData.get("grade"))>=4){
			List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip,limit_value", "", "", new MDataMap());
			BigDecimal integral = new BigDecimal(0);
			for(MDataMap map:evaluateList){
				if(map.get("evaluate_type").equals("评价图片")){
					int limit = Integer.parseInt(map.get("limit_value"));
					int size = comment.getComment_photo().size();
					if(StringUtils.isNotBlank(comment.getCcvid())) {
						size += 1;
					}
					if(size >= limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
				// 如果分享到买家秀,则赠送积分
				if(is_share.equals("1")) {
					if(map.get("evaluate_type").equals("买家秀")){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
			}
			
			if(integral.intValue()>0){
				this.giveIntegral(integral, custId, comment.getOrder_code(), comment.getSku_code());
			}
		}
	}
	
	/**
	 * 检查图片是否违规
	 * @param urls
	 * @return
	 */
	private boolean checkPic(List<String> urls) {
		WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();
		String access_token = wxMusicAlbumService.getToken();
		if(StringUtils.isNotBlank(access_token)) {
			for(String url:urls) {
				String review_state = wxMusicAlbumService.checkPic(url,access_token);
				if("0".equals(review_state)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 追评
	 */
	private void addCommentAppend(AddCommentModel comment,MDataMap mData){
		MDataMap mNewData = new MDataMap();
		mNewData.put("evaluation_uid", mData.get("uid"));
		mNewData.put("order_code", comment.getOrder_code()); //订单编号
		mNewData.put("order_assessment", replaceEmoji(comment.getComment_content()));
		mNewData.put("oder_photos", StringUtils.join(comment.getComment_photo(),"|"));
		mNewData.put("oder_creattime", DateHelper.upNow());
		mNewData.put("order_name", getUserCode());
		mNewData.put("manage_code", getManageCode());
		mNewData.put("order_skuid", mData.get("order_skuid"));
		mNewData.put("product_code", mData.get("product_code"));
		mNewData.put("pic_status", pictureAccept);//图片默认审核通过
		mNewData.put("check_flag", hasAccept);  //审核状态
		mNewData.put("flag_show", online);  //是否上下线（也可叫显示隐藏）
		mNewData.put("user_mobile", this.getOauthInfo().getLoginName());
		mNewData.put("sku_name", mData.get("sku_name"));
		mNewData.put("ccvids", comment.getCcvid());//先存本地给的UID
		boolean flag = this.checkPic(comment.getComment_photo());
		if(!flag) {
			mNewData.put("pic_status", "449747510002");//图片违规审核不通过
		}
		DbUp.upTable("nc_order_evaluation_append").dataInsert(mNewData);
		
		//评论送积分		
		String custId = plusServiceAccm.getCustId(getUserCode());// 家有客代号
		if(StringUtils.isNotBlank(custId)&&Integer.parseInt(mData.get("grade"))>=4){
			List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip,limit_value", "", "", new MDataMap());
			BigDecimal integral = new BigDecimal(0);
			for(MDataMap map:evaluateList){
				if(map.get("evaluate_type").equals("追价文字")){
					int limit = Integer.parseInt(map.get("limit_value"));
					if(comment.getComment_content().length()>=limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
				if(map.get("evaluate_type").equals("追评图片")){
					int limit = Integer.parseInt(map.get("limit_value"));
					int size = comment.getComment_photo().size();
					if(StringUtils.isNotBlank(comment.getCcvid())) {
						size += 1;
					}
					if(size >= limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
			}
			if(integral.intValue()>0){
				this.giveIntegral(integral, custId, comment.getOrder_code(), comment.getSku_code());
			}
		}
	}
	
	/**
	 * 修改差评
	 */
	private void editBadComment(AddCommentModel comment,MDataMap mData){
		if(Integer.parseInt(mData.get("grade"))<=3 && "5".equals(comment.getGrade())){
			MDataMap updateMap = new MDataMap();
			updateMap.put("zid", mData.get("zid"));
			updateMap.put("uid", mData.get("uid"));
			updateMap.put("oder_photos", StringUtils.join(comment.getComment_photo(),"|"));
			updateMap.put("grade", comment.getGrade());
			updateMap.put("grade_type", gradeTypeMap.get(comment.getGrade()));
			updateMap.put("order_assessment", replaceEmoji(comment.getComment_content()));
			updateMap.put("ccvids", comment.getCcvid());//先存本地给的UID
			DbUp.upTable("nc_order_evaluation").update(updateMap);
		}
		
		//评论送积分		
		String custId = plusServiceAccm.getCustId(getUserCode());// 家有客代号
		if(StringUtils.isNotBlank(custId)&&Integer.parseInt(comment.getGrade())>=4&&Integer.parseInt(mData.get("grade"))<=3){
			List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip,limit_value", "", "", new MDataMap());
			BigDecimal integral = new BigDecimal(0);
			for(MDataMap map:evaluateList){
				if(map.get("evaluate_type").equals("评价文字")){
					int limit = Integer.parseInt(map.get("limit_value"));
					if(comment.getComment_content().length()>=limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
				if(map.get("evaluate_type").equals("评价图片")){
					int limit = Integer.parseInt(map.get("limit_value"));
					int size = comment.getComment_photo().size();
					if(StringUtils.isNotBlank(comment.getCcvid())) {
						size += 1;
					}
					if(size >= limit){
						integral = integral.add(new BigDecimal(map.get("integral_value")));
					}
				}
			}
			if(integral.intValue()>0){
				this.giveIntegral(integral, custId, comment.getOrder_code(), comment.getSku_code());
			}
		}
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
	
	/**
	 * 赠送积分
	 */
	private void giveIntegral(BigDecimal giveMoney,String custId,String orderCode,String skuCode){
		giveMoney = plusServiceAccm.accmAmtToMoney(giveMoney,2);
		RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GI, giveMoney, custId, orderCode, skuCode);
		// 记录积分变更日志  - 积分共享增加
		if(teamResult.getResultCode() == 1) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("member_code", getUserCode());
			mDataMap.put("cust_id", custId);
			mDataMap.put("change_type", "449748080010");
			mDataMap.put("change_money", giveMoney.toString());
			mDataMap.put("remark", skuCode);
			mDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
		}else{
			String param = getUserCode()+","+giveMoney+","+custId+","+orderCode+","+skuCode;
			// 操作不成功，加入到定时任务中进行重试
			JobExecHelper.createExecInfo("449746990021", param, null);
		}
	}
}
