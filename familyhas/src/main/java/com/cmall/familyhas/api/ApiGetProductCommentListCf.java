package com.cmall.familyhas.api;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.api.input.CfProductCommentListInput;
import com.cmall.familyhas.api.model.CcVideo;
import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.familyhas.api.model.ProductCommentAppend;
import com.cmall.familyhas.api.result.CfProductCommentListResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.productcenter.model.PicAllInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

public class ApiGetProductCommentListCf extends RootApiForVersion<CfProductCommentListResult, CfProductCommentListInput>{

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

	public CfProductCommentListResult Process(CfProductCommentListInput inputParam, MDataMap mRequestMap) {
		
		CfProductCommentListResult result = new CfProductCommentListResult();
		String currUserCode ="";
		if (PlusHelperEvent.checkEventItem(inputParam.getProductCode())) {
			PlusModelEventItemProduct eventItemtInfo = new PlusSupportEvent().upItemProductByIcCode(inputParam.getProductCode());
			if (null != eventItemtInfo) {
				inputParam.setProductCode(eventItemtInfo.getProductCode());
			}
		}
//		
//		//缓存中获取到评论列表
//		PlusModelCommentList commentList = new LoadProductCommentList().upInfoByCode(new PlusModelProductCommentQuery(inputParam.getProductCode()));
//		
		//currUserCode= inputParam.getUserCode().toString();
		if(StringUtils.isBlank(currUserCode)&&getFlagLogin()) {
			currUserCode = getOauthInfo().getUserCode();
		}
		
		
		
		if(result.upFlagTrue()){
			MDataMap paramsMap = new MDataMap();
			paramsMap.put("product_code", inputParam.getProductCode());
			paramsMap.put("manage_code", getManageCode());
			paramsMap.put("check_flag", hasAccept);
			paramsMap.put("grade_type", inputParam.getGradeType());
			paramsMap.put("order_photos","");
			paramsMap.put("pic_status",pictureAccept);
			MPageData mPageData = new MPageData();
			//根据商品编码和评价等级查询商品列表评论
			
			String sqlWhere = "SELECT * FROM ( select * from "
						+ "( 	(SELECT *,1 flag FROM newscenter.nc_order_evaluation a WHERE a.product_code = :product_code AND a.auto_good_evaluation_flag = 0 AND a.grade_type = '好评' ) UNION ALL "
							+ " (SELECT *,2 flag FROM newscenter.nc_order_evaluation a WHERE a.product_code = :product_code AND a.auto_good_evaluation_flag = 0 AND a.grade_type = '中评' ) UNION ALL "
							+ " (SELECT *,3 flag FROM newscenter.nc_order_evaluation a WHERE a.product_code = :product_code AND a.auto_good_evaluation_flag = 0 AND a.grade_type = '差评' ) UNION ALL "
							+ " (SELECT *,4 flag FROM newscenter.nc_order_evaluation a WHERE a.product_code = :product_code AND a.auto_good_evaluation_flag = 1 ) "
						+ " )ddd order by ddd.is_down,ddd.flag,ddd.oder_creattime DESC "
					+ " )bbb WHERE bbb.manage_code=:manage_code and bbb.check_flag=:check_flag";
			//String sqlWhere = " product_code=:product_code and manage_code=:manage_code and check_flag=:check_flag";
			if(!inputParam.getGradeType().equals(all) && !inputParam.getGradeType().equals(showPhoto)){
				sqlWhere += " and grade_type=:grade_type ";
			}else if(inputParam.getGradeType().equals(showPhoto)){
				sqlWhere += " and oder_photos!=:order_photos and pic_status=:pic_status";
			}
			mPageData=DataPaging.upPageData("nc_order_evaluation",sqlWhere, paramsMap,inputParam.getPaging());
			//CCVideoService ccservice = new CCVideoService();
			if(mPageData.getListData().size() != 0){
				for (MDataMap mDataMap : mPageData.getListData()) {
					CdogProductComment comment = new CdogProductComment();
				    setComment(mDataMap, comment, inputParam.getScreenWidth(),currUserCode);
				    if(inputParam.getGradeType().equals(showPhoto)){
						comment.setGradeType(showPhoto);
					}
				    String ccvids = mDataMap.get("ccvids");
				    String ccpics = mDataMap.get("ccpics");
					String duration = mDataMap.get("duration");
					if(StringUtils.isNotEmpty(ccvids)) {//添加视频信息
						String args[] = ccvids.split("\\|");
						String pics[] = ccpics.split("\\|");
						String dus[] = duration.split("\\|");
						if(args.length == pics.length && args.length == dus.length) {
							for(int i = 0;i<args.length;i++) {
								CcVideo cc = new CcVideo();
								cc.setCcvid(args[i]);
								cc.setImg(pics[i]);
								cc.setTime(Integer.parseInt(dus[i]));
								if(!StringUtils.isEmpty(cc.getImg())&&mDataMap.get("pic_status").equals(pictureAccept)) {
									comment.getVideoList().add(cc);
								}
							}
						}
					}
					result.getProductComment().add(comment);
				}
				result.setPaged(mPageData.getPageResults());
			}
			//计算好评率
			String sql = "select grade_type,count(1) from nc_order_evaluation where product_code=:product_code and manage_code=:manage_code and check_flag=:check_flag"
					      + " group by grade_type";
			 List<Map<String, Object>> dataSqlList = DbUp.upTable("nc_order_evaluation").dataSqlList(sql, paramsMap);
			for (Map<String, Object> mDataMap : dataSqlList) {
				if(highPraise.equals(mDataMap.get("grade_type"))){
					result.setHighPraiseCounts(Integer.parseInt(mDataMap.get("count(1)").toString()));
				}else if(commonPraise.equals(mDataMap.get("grade_type"))){
					result.setCommonPraiseCounts(Integer.parseInt(mDataMap.get("count(1)").toString()));
				}else if(lowPraise.equals(mDataMap.get("grade_type"))){
					result.setLowPraiseCounts(Integer.parseInt(mDataMap.get("count(1)").toString()));
				}
			}
			//计算有图个数
			int picNum = DbUp.upTable("nc_order_evaluation").dataCount("product_code = '"+inputParam.getProductCode()+"' and check_flag='"+hasAccept+"' and oder_photos != '' and manage_code = '"+getManageCode()+"' and pic_status='"+pictureAccept+"'", new MDataMap());
			result.setPictureCounts(picNum);
			result.setCommentSumCounts(result.getHighPraiseCounts() + result.getCommonPraiseCounts() + result.getLowPraiseCounts());
			if(result.getCommentSumCounts() != 0){
				double x = result.getHighPraiseCounts() * 1.0;
		    	double y = result.getCommentSumCounts() * 1.0;
		    	NumberFormat nf = NumberFormat.getPercentInstance();
		        nf.setMinimumFractionDigits( 0 );
		        String prise = nf.format(x/y);
		        result.setHighPraiseRate(prise.substring(0, prise.length()-1));
			}
		}
		return result;
	}
	
	public void setComment(MDataMap mDataMap, CdogProductComment comment,Integer screenWidth,String currUserCode){
		String approve_flag="N";
		String oppose_flag="N";
		if(StringUtils.isNotBlank(currUserCode)) {

			//一个自然日内只能做一次操作
			 String date = DateUtil.getSysDateString();
			 String startTime = date+" 00:00:00";
			 String endTime = date+" 23:59:59";
			Map<String, Object> map = DbUp.upTable("nc_order_evaluation_attitude").dataSqlOne("select * from nc_order_evaluation_attitude where  operate_time>'"+startTime+"' and operate_time<'"+endTime+"' and user_code=:user_code and evalution_uid=:evalution_uid", new MDataMap("evalution_uid",mDataMap.get("uid").toString(),"user_code",currUserCode));
			if(map!=null) {
				approve_flag = StringUtils.isBlank(map.get("approve_flag").toString())?"N":map.get("approve_flag").toString();
				oppose_flag = StringUtils.isBlank(map.get("oppose_flag").toString())?"N":map.get("oppose_flag").toString();
				
			}
		}
		ProductService productService = new ProductService();
		comment.setCommentContent(mDataMap.get("order_assessment").replaceAll("&nbsp;"," ").replaceAll("&lt", "<").replaceAll("&gt", ">").replaceAll("&amp", "&"));
		comment.setCommentTime(mDataMap.get("oder_creattime").split(" ")[0]);
		comment.setGradeType(mDataMap.get("grade_type"));
		comment.setGrade(mDataMap.get("grade"));
		comment.setSkuCode(mDataMap.get("order_skuid"));
		comment.setApproveNum(Integer.parseInt(mDataMap.get("approve_num")));
		comment.setOpposeNum(Integer.parseInt(mDataMap.get("oppose_num")));
		//comment.setCurrMemberOperatFlag(currMemberOperatFlag);
		comment.setApproveFlag(approve_flag);
		comment.setOpposeFlag(oppose_flag);
		comment.setUid(mDataMap.get("uid").toString());
		String mobile = mDataMap.get("user_mobile");
		String userCode = mDataMap.get("order_name");
		MDataMap nickMap = DbUp.upTable("mc_extend_info_star").oneWhere("nickname,member_avatar,status", "", "", "member_code",userCode,"app_code",getManageCode());
		if(nickMap != null && nickMap.get("status").equals(headUrlAccept))
			comment.setUserFace(nickMap.get("member_avatar"));
		if(mDataMap.get("reply_content").length() != 0)
		comment.setReplyContent(bConfig("familyhas.reply_name")+"："+mDataMap.get("reply_content"));
		if(mDataMap.get("reply_createtime").split(" ")[0].length() != 0)
		comment.setReplyTime("回复于"+mDataMap.get("reply_createtime").split(" ")[0]);
		if(mDataMap.get("pic_status").equals(pictureAccept)){
			List<PicAllInfo> picForProduct = productService.getPicForProduct(screenWidth, mDataMap.get("oder_photos"));
			comment.setCommentPhotoList(picForProduct);
		}
		/**
		 * 评论头像问题
		 */
		if(mobile != null) {
			MDataMap commentUser = DbUp.upTable("mc_member_sync").one("login_name", StringUtils.trimToEmpty(mobile));
			if(commentUser != null) {
				comment.setUserFace(StringUtils.trimToEmpty(commentUser.get("avatar")));
				comment.setUserMobile(StringUtils.trimToEmpty(commentUser.get("nickname")));
			} else {
				if(mobile.length() > 7){
					comment.setUserMobile(mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length()));
				}
			}
		}
		//查找sku规格颜色
		MDataMap mSkuDataMap = DbUp.upTable("pc_skuinfo").one("sku_code",mDataMap.get("order_skuid"));
		if(mSkuDataMap != null){
			String keyvalue = mSkuDataMap.get("sku_keyvalue");
			if(StringUtility.isNotNull(keyvalue)){
				String[] values = keyvalue.split("&");
				if(values.length > 1){
					String[] colors = values[0].split("=");
					String[] styles = values[1].split("=");
					if(colors.length > 1 && colors[1].toString().indexOf("共同") < 0){
						comment.setSkuColor(colors[1].toString());
					}
					if(styles.length > 1 && styles[1].toString().indexOf("共同") < 0){
						comment.setSkuStyle(styles[1].toLowerCase());
					}
				}
			}									
		}
		
		ProductCommentAppend productCommentAppend = null;
		MDataMap mData;
		
		mData = DbUp.upTable("nc_order_evaluation_append").one("evaluation_uid",mDataMap.get("uid"));
		if(mData != null){
			// 文字未审核通过则整个追评都不显示
			if(hasAccept.equals(mData.get("check_flag"))){
				productCommentAppend = new ProductCommentAppend();
				productCommentAppend.setCommentContent(mData.get("order_assessment"));
				productCommentAppend.setCommentTime(mData.get("oder_creattime").split(" ")[0]);
				if(StringUtils.isNotBlank(mData.get("reply_content"))){
					productCommentAppend.setReplyContent(bConfig("familyhas.reply_name")+"："+StringUtils.trimToEmpty(mData.get("reply_content")));
					productCommentAppend.setReplyTime("回复于"+StringUtils.trimToEmpty(mData.get("reply_createtime")).split(" ")[0]);
				}
				
				if(mData.get("pic_status").equals(pictureAccept)){
					productCommentAppend.setCommentPhotoList(productService.getPicForProduct(screenWidth, mData.get("oder_photos")));
				}
				
				String orderCreateTime = (String)DbUp.upTable("oc_orderinfo").dataGet("create_time", "", new MDataMap("order_code", mData.get("order_code")));
				
				try {
					// 计算相差天数
					if(orderCreateTime != null){
						long diff = DateUtils.parseDate(mData.get("oder_creattime"), "yyyy-MM-dd HH:mm:ss").getTime()
								- DateUtils.parseDate(orderCreateTime, "yyyy-MM-dd HH:mm:ss").getTime();
						int day = Math.round(diff / (3600000*24F) + 0.5F); // 包含当天
						if(day == 0) day = 1;
						productCommentAppend.setCommentDay("购买"+day+"天后追评");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String ccvids = mData.get("ccvids");
			    String ccpics = mData.get("ccpics");
				String duration = mData.get("duration");
				if(StringUtils.isNotEmpty(ccvids)) {//添加视频信息
					String args[] = ccvids.split("\\|");
					String pics[] = ccpics.split("\\|");
					String dus[] = duration.split("\\|");
					if(args.length == pics.length && args.length == dus.length) {
						for(int i = 0;i<args.length;i++) {
							CcVideo cc = new CcVideo();
							cc.setCcvid(args[i]);
							cc.setImg(pics[i]);
							cc.setTime(Integer.parseInt(dus[i]));
							if(!StringUtils.isEmpty(cc.getImg())&&mData.get("pic_status").equals(pictureAccept)) {
								productCommentAppend.getVideoList().add(cc);
							}
						}
					}
				}
				comment.getCommentAppendList().add(productCommentAppend);
			}
		}
		
	}
}
