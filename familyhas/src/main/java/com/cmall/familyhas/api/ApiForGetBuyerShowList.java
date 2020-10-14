package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetBuyerShowListInput;
import com.cmall.familyhas.api.model.BuyerShow;
import com.cmall.familyhas.api.model.EvaProduct;
import com.cmall.familyhas.api.model.EvaluationImg;
import com.cmall.familyhas.api.result.ApiForGetBuyerShowListResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.ordercenter.service.OrderService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 分页获取买家秀列表数据
 * @author lgx
 * 
 */
public class ApiForGetBuyerShowList extends RootApiForVersion<ApiForGetBuyerShowListResult, ApiForGetBuyerShowListInput> {
	
	static final int pageSize = 10;
	
	public ApiForGetBuyerShowListResult Process(ApiForGetBuyerShowListInput inputParam, MDataMap mRequestMap) {
		ApiForGetBuyerShowListResult result = new ApiForGetBuyerShowListResult();//返回结果
		
		HomeColumnService hcService = new HomeColumnService();
		
		// 第几页
		int page = inputParam.getPage();
		// 调用渠道
		String channelId = getChannelId();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		// 总页数
		int totalPage = 0;
		String isRemind = "0";
		String total_integral = "0";
		String canEvaluateMainpic = "";
		String tagType = "1";
		List<BuyerShow> buyerShowList = new ArrayList<BuyerShow>();
		// 起始索引
		int index = (page-1) * pageSize;
		
		// 是否显示评价或晒单送积分顶部悬浮提醒框
		if(!"".equals(userCode) && page == 1) {
			// 查询用户有没有待评价和好评待晒图的单
			String sql = "SELECT DISTINCT oi.order_code,od.product_code,od.sku_code,od.sku_num,pi.product_name,sku.sku_keyvalue,pi.mainpic_url,sku.sku_picurl,pi.small_seller_code FROM ordercenter.oc_orderinfo oi"
					   + " LEFT JOIN ordercenter.oc_orderdetail od ON oi.order_code = od.order_code"
					   + " LEFT JOIN productcenter.pc_skuinfo sku ON sku.sku_code = od.sku_code"
					   + " LEFT JOIN productcenter.pc_productinfo pi ON sku.product_code = pi.product_code"
					   + " WHERE od.gift_flag = '1' AND pi.product_name != '' AND oi.delete_flag = '0' AND oi.buyer_code = :buyer_code AND order_source not in('449715190014','449715190037') AND oi.order_status = '4497153900010005'"
					   + " AND oi.order_type NOT IN(" + new OrderService().getNotInOrderType() + ")";
			// 待评价订单
			String sql1 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' ) = 0  AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) "
						+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
			// 待晒单则是为上传图片的评价
			String sql2 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code  AND noe.grade > 3 AND noe.evaluation_status_user = '449746810001' AND noe.auto_good_evaluation_flag = 0 AND oder_photos = '' AND ccvids = '') > 0 AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) "
						+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
			MDataMap param = new MDataMap("buyer_code", userCode);
			Map<String, Object> totalMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql1 + ")t", param);
			Map<String, Object> totalMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql2 + ")t", param);
			int intValue1 = MapUtils.getIntValue(totalMap1, "total");
			int intValue2 = MapUtils.getIntValue(totalMap2, "total");
			
			List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip", "", "", new MDataMap());
			int integral1 = 0;
			int integral2 = 0;
			for(MDataMap map:evaluateList){
				integral1 += Integer.parseInt(map.get("integral_value"));
				if(map.get("evaluate_type").equals("评价图片")){
					integral2 += MapUtils.getIntValue(map, "integral_value");
				}
				if(map.get("evaluate_type").equals("买家秀")){
					integral2 += MapUtils.getIntValue(map, "integral_value");
				}
			}
			int totalIntegral = intValue1 * integral1 + intValue2 * integral2;
			total_integral = totalIntegral+"";
			if(totalIntegral > 0) {
				isRemind = "1";
				if(intValue1 > 0) {
					Map<String, Object> picMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url  FROM (" + sql1 + " ORDER BY oi.zid desc LIMIT 1) t", param);
					canEvaluateMainpic = MapUtils.getString(picMap1, "mainpic_url");
					tagType = "1";
				}else if(intValue2 > 0){
					Map<String, Object> picMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url FROM (" + sql2 + " ORDER BY oi.zid desc LIMIT 1) t", param);						
					canEvaluateMainpic = MapUtils.getString(picMap2, "mainpic_url");
					tagType = "2";
				}
			}
		}
		
		// 分页查询买家秀列表数据
		String buyerShowSql = "SELECT * FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ORDER BY b.create_time DESC LIMIT "+index+","+pageSize;
		List<Map<String, Object>> buyerShowMapList = DbUp.upTable("nc_buyer_show_info").dataSqlList(buyerShowSql, new MDataMap());
		for (Map<String, Object> map : buyerShowMapList) {
			BuyerShow buyerShow = new BuyerShow();
			
			// 买家秀uid
			String buyerShowUid = MapUtils.getString(map, "uid");
			// 评价uid
			String evaluation_uid = MapUtils.getString(map, "evaluation_uid");
			MDataMap order_evaluation = DbUp.upTable("nc_order_evaluation").one("uid",evaluation_uid);
			// 评论发表时间
			String oder_creattime = order_evaluation.get("oder_creattime");
			// 晒单内容
			String orderAssessment = order_evaluation.get("order_assessment");
			// 评价人编号
			String memberCode = order_evaluation.get("order_name");
			String user_mobile = order_evaluation.get("user_mobile");
			// 晒单人头像
			String avatar = "";
			// 晒单人昵称
			String nickname = "";
			if("MI150824100141".equals(memberCode)) {
				nickname = user_mobile.substring(0, 3) + "****" + user_mobile.substring(7);
			}else {
				Map<String, Object> member_sync = DbUp.upTable("mc_member_sync").dataSqlOne("SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"' ORDER BY last_update_time DESC LIMIT 1", new MDataMap());
				if(null == member_sync || null == member_sync.get("nickname") || "".equals(member_sync.get("nickname"))){
					// 如果昵称是空,查询手机号
					Map<String, Object> login_info = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003'", new MDataMap());
					if(null != login_info) {
						nickname = (String) login_info.get("login_name");
						if(hcService.isPhone(nickname)) {
							nickname = nickname.substring(0, 3) + "****" + nickname.substring(7);
						}
					}
				}else { // 如果昵称不是空
					nickname = (String) member_sync.get("nickname");
				}
				// 头像
				if(null != member_sync && null != member_sync.get("avatar") && !"".equals(member_sync.get("avatar"))){
					avatar = (String) member_sync.get("avatar");
				}
			}
			
			// 买家秀点赞量
			int approveCount = DbUp.upTable("nc_buyer_show_approve").count("buyer_show_or_buyer_show_eva_uid",buyerShowUid);
			String approveNum = "0";
			if(approveCount > 999) {
				approveNum = "999+";
			}else {
				approveNum = approveCount+"";
			}
			// 买家秀阅读量
			String readNum = "0";
			int readCount = DbUp.upTable("nc_buyer_show_read").count("buyer_show_uid",buyerShowUid);
			if(readCount > 99999) {
				readNum = "10w+";
			}else {
				readNum = readCount+"";
			}
			// 买家秀评价量
			String evaluationNum = "0";
			int evaluationCount = DbUp.upTable("nc_buyer_show_evaluation").count("buyer_show_uid",buyerShowUid);
			if(evaluationCount > 999) {
				evaluationNum = "999+";
			}else {
				evaluationNum = evaluationCount+"";
			}
			// 买家秀商品信息
			EvaProduct evaProduct = new EvaProduct();
			String product_code = order_evaluation.get("product_code");
			PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
			List<String> productCodeArr = new ArrayList<String>();
			productCodeArr.add(product_code);
			skuQuery.setCode(StringUtils.join(productCodeArr, ","));
			skuQuery.setMemberCode(userCode);
			skuQuery.setIsPurchase(1);
			skuQuery.setChannelId(channelId);
			// 获取商品最低销售价格和对应的划线价
			Map<String, PlusModelSkuInfo> productPriceMap = new ProductPriceService()
					.getProductMinPriceSkuInfo(skuQuery);
			if (null != productPriceMap.get(product_code)) {
				BigDecimal sellPrice1 = productPriceMap.get(product_code).getSellPrice();
				// 销售价
				evaProduct.setSellPrice(MoneyHelper.format(sellPrice1));
				BigDecimal skuPrice1 = productPriceMap.get(product_code).getSkuPrice();
				if (skuPrice1.compareTo(sellPrice1) > 0) {
					// 划线价
					evaProduct.setMarkPrice(MoneyHelper.format(skuPrice1));
				}
			} else {
				PlusModelProductInfo productInfo = new LoadProductInfo()
						.upInfoByCode(new PlusModelProductQuery(product_code));
				// 销售价
				evaProduct.setSellPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
			}
			MDataMap productinfo = DbUp.upTable("pc_productinfo").one("product_code",product_code);
			evaProduct.setProductCode(product_code);
			if(productinfo != null) {
				evaProduct.setProductName(productinfo.get("product_name"));
				evaProduct.setMainpicUrl(productinfo.get("mainpic_url"));
			}
			
			// 买家秀图片或者视频list
			List<EvaluationImg> evaluationImgList = new ArrayList<EvaluationImg>();
			// 评论图片
			String oder_photos = order_evaluation.get("oder_photos");
			String ccvids = order_evaluation.get("ccvids");
			String ccpics = order_evaluation.get("ccpics");
			if(!"".equals(ccvids) && !"".equals(ccpics)) {
				// 有评论视频
				EvaluationImg evaImg = new EvaluationImg();
				evaImg.setEvaluationImgUrl(ccpics);
				evaImg.setIsVideo("1");
				evaImg.setCcvid(ccvids);
				evaluationImgList.add(evaImg);
			}
			if(!oder_photos.equals("")) {
				// 有评论图片
				String[] photos = oder_photos.split("\\|");			
				for (String photo : photos) {
					EvaluationImg evaluationImg = new EvaluationImg();
					evaluationImg.setCcvid("");
					evaluationImg.setEvaluationImgUrl(photo);
					evaluationImg.setIsVideo("0");
					evaluationImgList.add(evaluationImg);
				}
			}
			
			// 当前用户是否点赞
			String isApprove = "0";
			if(!"".equals(userCode)) {
				MDataMap one = DbUp.upTable("nc_buyer_show_approve").one("buyer_show_or_buyer_show_eva_uid",buyerShowUid,"member_code",userCode);
				if(one != null) {
					isApprove = "1";
				}
			}
			// 当前用户是否关注
			String isFollow = "0";
			if(!"".equals(userCode)) {
				MDataMap one = DbUp.upTable("nc_buyer_show_fans").one("fans_member_code",userCode,"member_code",memberCode);
				if(one != null) {
					isFollow = "1";
				}
			}
			
			// 计算买家秀收益
			String buyerShowMoney = "0";
			Map<String, Object> tgzMoneyMap = DbUp.upTable("fh_tgz_order_detail").dataSqlOne("SELECT sum(tgz_money) buyer_show_money FROM fh_tgz_order_detail WHERE buyer_show_code = '"+buyerShowUid+"' ", new MDataMap());
			if(tgzMoneyMap != null) {
				buyerShowMoney = MapUtils.getString(tgzMoneyMap, "buyer_show_money","0");
				if(new BigDecimal(buyerShowMoney).compareTo(new BigDecimal("0.00")) <= 0) {
					buyerShowMoney = "0";
				}
			}
			
			buyerShow.setApproveNum(approveNum);
			buyerShow.setAvatar(avatar);
			buyerShow.setBuyerShowUid(buyerShowUid);
			buyerShow.setCreateTime(oder_creattime);
			buyerShow.setEvaluationImgList(evaluationImgList);
			buyerShow.setEvaluationNum(evaluationNum);
			buyerShow.setEvaProduct(evaProduct);
			buyerShow.setMemberCode(memberCode);
			buyerShow.setNickname(nickname);
			buyerShow.setOrderAssessment(orderAssessment);
			buyerShow.setReadNum(readNum);
			buyerShow.setIsApprove(isApprove);
			buyerShow.setIsFollow(isFollow);
			buyerShow.setBuyerShowMoney(buyerShowMoney);
			
			buyerShowList.add(buyerShow);

		}
		result.setBuyerShowList(buyerShowList);
		
		String countSql = "SELECT count(1) num FROM nc_buyer_show_info b WHERE b.is_delete = '0' AND b.check_status = '449748580001' ";
		Map<String, Object> countMap = DbUp.upTable("nc_buyer_show_info").dataSqlOne(countSql, new MDataMap());
		double num = MapUtils.getDoubleValue(countMap, "num");
		// 总页数
		totalPage = (int) Math.ceil(num/pageSize);
		
		result.setTotalPage(totalPage);
		result.setTotal_integral(total_integral);
		result.setIsRemind(isRemind);
		result.setCanEvaluateMainpic(canEvaluateMainpic);
		result.setTagType(tagType);
		
		return result;
	}
	
	
}
