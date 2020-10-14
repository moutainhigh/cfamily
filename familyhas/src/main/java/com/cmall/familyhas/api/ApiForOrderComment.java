package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.api.input.ApiForOrderCommentInput;
import com.cmall.familyhas.api.model.CcVideo;
import com.cmall.familyhas.api.model.ProductComment;
import com.cmall.familyhas.api.model.ProductCommentAppend;
import com.cmall.familyhas.api.result.ApiForOrderCommentResult;
import com.cmall.familyhas.api.result.ApiForOrderCommentResult.ProductItem;
import com.cmall.familyhas.service.cc.CCVideoService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 评价中心支撑接口
 */
public class ApiForOrderComment extends RootApiForToken<ApiForOrderCommentResult, ApiForOrderCommentInput> {

	ProductService productService = new ProductService();
	PlusServiceSeller plusServiceSeller = new PlusServiceSeller();
	
	/** 待评价商品 */
	static final String TAG_TYPE_1 = "1";
	/** 待晒单商品 */
	static final String TAG_TYPE_2 = "2";
	/** 已评价商品 */
	static final String TAG_TYPE_3 = "3";
	
	/** 每页显示数量 */
	static final int PAGE_SIZE = 10;
	
	@Override
	public ApiForOrderCommentResult Process(ApiForOrderCommentInput inputParam, MDataMap mRequestMap) {
		List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip", "", "", new MDataMap());
		int integral = 0;String tip1 = "",tip3 = "";
		// 分享到买家秀赠送积分总量
		int buyerShowIntegral = 0;
		for(MDataMap map:evaluateList){
			if(map.get("evaluate_type").equals("买家秀")){
				buyerShowIntegral = MapUtils.getIntValue(map, "integral_value");
			}else {
				integral += Integer.parseInt(map.get("integral_value"));
			}
			if(map.get("evaluate_type").equals("评价文字")){
				tip1 = map.get("tip");
			}
			if(map.get("evaluate_type").equals("追价文字")){
				tip3 = map.get("tip");
			}
		}		
		String sql = "SELECT DISTINCT oi.order_code,od.product_code,od.sku_code,od.sku_num,pi.product_name,sku.sku_keyvalue,pi.mainpic_url,sku.sku_picurl,pi.small_seller_code FROM ordercenter.oc_orderinfo oi"
				   + " LEFT JOIN ordercenter.oc_orderdetail od ON oi.order_code = od.order_code"
				   + " LEFT JOIN productcenter.pc_skuinfo sku ON sku.sku_code = od.sku_code"
				   + " LEFT JOIN productcenter.pc_productinfo pi ON sku.product_code = pi.product_code"
				   + " WHERE od.gift_flag = '1' AND pi.product_name != '' AND oi.delete_flag = '0' AND oi.buyer_code = :buyer_code AND order_source not in('449715190014','449715190037') AND oi.order_status = '4497153900010005'"
				   + " AND oi.order_type NOT IN(" + new OrderService().getNotInOrderType() + ")";
		
		ApiForOrderCommentResult result = new ApiForOrderCommentResult();
		result.setIntegral(integral+"");
		result.setBuyerShowIntegral(buyerShowIntegral+"");
		int pageNum = NumberUtils.toInt(inputParam.getPageNum(),1);
		if(pageNum < 1) pageNum = 1;
		
		MDataMap param = new MDataMap("buyer_code",getUserCode());
		if(StringUtils.isNotBlank(inputParam.getOrderCode())){
			sql += " AND oi.order_code = :order_code";
			param.put("order_code", inputParam.getOrderCode());
		}
		if(StringUtils.isNotBlank(inputParam.getSkuCode())){
			sql += " AND od.sku_code = :sku_code";
			param.put("sku_code", inputParam.getSkuCode());
		}
		
		
		if(TAG_TYPE_1.equals(inputParam.getTagType())){
			sql += " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' ) = 0  AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) ";
			result.setTipText(tip1);
		}else if(TAG_TYPE_2.equals(inputParam.getTagType())){
			// 待晒单则是为上传图片的评价
			sql += " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' AND noe.auto_good_evaluation_flag = 0 AND oder_photos = '' AND ccvids = '') > 0 AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) ";
		}else if(TAG_TYPE_3.equals(inputParam.getTagType())){
			// 已评价是已经上传图片的评价
			sql += " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' AND noe.auto_good_evaluation_flag = 0 AND (oder_photos != '' or ccvids != '')) > 0 AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) ";
			result.setTipText(tip3);
		}else {
			return result;
		}
		
		// 屏蔽橙意会员卡商品标识
		sql += " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
		
		// 设置页码
		Map<String, Object> map = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(*) total FROM (" + sql + ")t", param);
		result.setNowPage(pageNum);
		result.setCountPage(Math.round(NumberUtils.toFloat(map.get("total")+"")/PAGE_SIZE + 0.5F));
		
		// 查询商品数据
		sql += " ORDER BY oi.zid desc LIMIT " + (pageNum - 1) * PAGE_SIZE + ", "+PAGE_SIZE;
		List<Map<String, Object>> dataList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, param);
		
		ProductItem productItem = null;
		ProductComment productComment = null;
		ProductCommentAppend productCommentAppend = null;
		String[] keyvalue = null;
		MDataMap mData;
		String picUrl;
		PicInfo picInfo;
		String smallSellerCode;
		
		/**
		 * 评论头像问题
		 */
		String nickName = "";
		String mobile = getOauthInfo().getLoginName();
		String avatar = "";
		if(mobile != null) {
			MDataMap commentUser = DbUp.upTable("mc_member_sync").one("login_name", StringUtils.trimToEmpty(mobile));
			if(commentUser != null) {
				avatar = StringUtils.trimToEmpty(commentUser.get("avatar"));
				nickName = StringUtils.trimToEmpty(commentUser.get("nickname"));
			} else {
				if(mobile.length() > 7){
					nickName = mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length());
				}
			}
		}
		
		CCVideoService ccservice = new CCVideoService();
		for(Map<String, Object> data : dataList){
			productComment = null;
			productCommentAppend = null;
			
			productItem = new ProductItem();
			productItem.setOrderCode(data.get("order_code")+"");
			productItem.setProductCode(data.get("product_code")+"");
			productItem.setSkuCode(data.get("sku_code")+"");
			productItem.setProductName(data.get("product_name")+"");
			productItem.setSkuNum(data.get("sku_num")+"");
			
			smallSellerCode = data.get("small_seller_code")+"";
			if(StringUtils.isNotBlank(smallSellerCode) && !smallSellerCode.equalsIgnoreCase("SI2003")){
				if(plusServiceSeller.isKJSeller(smallSellerCode)){
					productItem.setFlagTheSea("1");
				}
			}
			
			keyvalue = data.get("sku_keyvalue").toString().split("&");
			if(!keyvalue[0].endsWith("共同")){
				productItem.setColor(keyvalue[0].split("=")[1]);
			}
			if(!keyvalue[1].endsWith("共同")){
				productItem.setStyle(keyvalue[1].split("=")[1]);
			}
			
			picUrl = null;
			if(data.get("sku_picurl") != null){
				picUrl = data.get("sku_picurl").toString();
			}
			
			if(picUrl == null && data.get("mainpic_url") != null){
				picUrl = data.get("mainpic_url").toString();
			}
			
			// 压缩一下图片大小
			if(StringUtils.isNotBlank(picUrl)){
				picInfo = productService.getPicInfo(350, picUrl);
				productItem.setPicUrl(picInfo.getPicNewUrl());
			}
			
			result.getProductItemList().add(productItem);
			
			// 查询评价
			mData = DbUp.upTable("nc_order_evaluation").one("order_code",productItem.getOrderCode(),"order_skuid",productItem.getSkuCode());
			if(mData != null){
				productComment = new ProductComment();
				//增加商品评价视频
				String ccvids = mData.get("ccvids");
				String ccpics = mData.get("ccpics");
				String duration = mData.get("duration");
				if(StringUtils.isNotEmpty(ccvids)) {//添加视频信息
					String args[] = ccvids.split("\\|");
					String pics[] = ccpics.split("\\|");
					String dus[] = duration.split("\\|");
					for(int i = 0;i<args.length;i++) {
						CcVideo cc = new CcVideo();
						cc.setCcvid(args[i]);
						//判断视频是否合法。
						String pic_status = mData.get("pic_status");
						if("449747510001".equals(pic_status)) {//审核通过素材
							cc.setStatus(1);
						}else {
							cc.setStatus(2);
						}
						if(pics.length==args.length) {
							cc.setImg(pics[i]);
						}
						if(dus.length == args.length) {
							cc.setTime(Integer.parseInt(dus[i]));
						}
						if(StringUtils.isEmpty(cc.getImg())) {
							cc.setStatus(0);
							cc.setImg(bConfig("familyhas.default_check_img"));
						}
						productComment.getVideoList().add(cc);
					}
				}
				productComment.setUid(mData.get("uid"));
				productComment.setUserMobile(nickName);
				productComment.setUserFace(avatar);
				//552添加点赞信息
				productComment.setApproveNum(Integer.parseInt(mData.get("approve_num").toString()));
				productComment.setOpposeNum(Integer.parseInt(mData.get("oppose_num").toString()));
				String userCode = getUserCode();
				String approve_flag="N";
				String oppose_flag="N";
				if(StringUtils.isNotBlank(userCode)) {
					//一个自然日内只能做一次操作
					 String date = DateUtil.getSysDateString();
					 String startTime = date+" 00:00:00";
					 String endTime = date+" 23:59:59";
					Map<String, Object> smap = DbUp.upTable("nc_order_evaluation_attitude").dataSqlOne("select * from nc_order_evaluation_attitude where  operate_time>'"+startTime+"' and operate_time<'"+endTime+"' and user_code=:user_code and evalution_uid=:evalution_uid", new MDataMap("evalution_uid",mData.get("uid").toString(),"user_code",userCode));
					if(smap!=null) {
						approve_flag = StringUtils.isBlank(smap.get("approve_flag").toString())?"N":smap.get("approve_flag").toString();
						oppose_flag = StringUtils.isBlank(smap.get("oppose_flag").toString())?"N":smap.get("oppose_flag").toString();
						
					}
					productComment.setApproveFlag(approve_flag);
					productComment.setOpposeFlag(oppose_flag);
					
				}
//				productComment.setUserFace(mData.get("head_icon_url"));
				productComment.setSkuStyle(productItem.getStyle());
				productComment.setSkuColor(productItem.getColor());
				productComment.setSkuCode(productItem.getSkuCode());
				productComment.setGradeType(mData.get("grade_type"));
				productComment.setGrade(mData.get("grade"));
				productComment.setCommentTime(mData.get("oder_creattime").split(" ")[0]);
				productComment.setCommentContent(mData.get("order_assessment"));
				productComment.setCommentPhotoList(productService.getPicForProduct(inputParam.getScreenWidth(), mData.get("oder_photos")));
				if(StringUtils.isNotBlank(mData.get("reply_content"))){
					productComment.setReplyTime("回复于"+StringUtils.trimToEmpty(mData.get("reply_createtime")).split(" ")[0]);
					productComment.setReplyContent(bConfig("familyhas.reply_name")+"："+StringUtils.trimToEmpty(mData.get("reply_content")));
				}
				productComment.setIsShare(mData.get("is_share").equals("")?"0":mData.get("is_share"));
				productComment.setIsCancel(mData.get("is_cancel").equals("")?"0":mData.get("is_cancel"));
				productItem.getProductCommentList().add(productComment);
			}
			
			// 查询追评
			if(productComment != null){
				mData = DbUp.upTable("nc_order_evaluation_append").one("evaluation_uid",productComment.getUid());
				if(mData != null){
					productCommentAppend = new ProductCommentAppend();
					//增加商品评价视频
					String ccvids = mData.get("ccvids");
					String ccpics = mData.get("ccpics");
					String duration = mData.get("duration");
					if(StringUtils.isNotEmpty(ccvids)) {//添加视频信息
						String args[] = ccvids.split("\\|");
						String pics[] = ccpics.split("\\|");
						String dus[] = duration.split("\\|");
						for(int i = 0;i<args.length;i++) {
							CcVideo cc = new CcVideo();
							cc.setCcvid(args[i]);
							cc.setStatus(1);
							if(pics.length==args.length) {
								cc.setImg(pics[i]);
							}
							if(dus.length == args.length) {
								if(StringUtils.isNotEmpty(dus[i])) {
									cc.setTime(Integer.parseInt(dus[i]));
								}
							}
							if(StringUtils.isEmpty(cc.getImg())) {
								cc.setStatus(0);
								cc.setImg(bConfig("familyhas.default_check_img"));
							}
							productCommentAppend.getVideoList().add(cc);
						}
					}
					productCommentAppend.setCommentContent(mData.get("order_assessment"));
					productCommentAppend.setCommentTime(mData.get("oder_creattime").split(" ")[0]);
					productCommentAppend.setCommentPhotoList(productService.getPicForProduct(inputParam.getScreenWidth(), mData.get("oder_photos")));
					if(StringUtils.isNotBlank(mData.get("reply_content"))){
						productCommentAppend.setReplyTime("回复于"+StringUtils.trimToEmpty(mData.get("reply_createtime")).split(" ")[0]);
						productCommentAppend.setReplyContent(bConfig("familyhas.reply_name")+"："+StringUtils.trimToEmpty(mData.get("reply_content")));
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
					
					productComment.getCommentAppendList().add(productCommentAppend);
				}
			}
		}
		
		return result;
	}
	
}
