package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiCollageTeamInfoInput;
import com.cmall.familyhas.api.result.ApiCollagePersonInfo;
import com.cmall.familyhas.api.result.ApiCollageTeamInfoResult;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 邀请拼团页接口
 * @author Bloodline
 *
 */
public class ApiCollageTeamInfo extends RootApiForToken<ApiCollageTeamInfoResult, ApiCollageTeamInfoInput> {
	
	@Override
	public ApiCollageTeamInfoResult Process(ApiCollageTeamInfoInput inputParam, MDataMap mRequestMap) {
		String collageCode = inputParam.getCollageCode();
		String maxWidth = inputParam.getMaxWidth();
		String userCode = getUserCode();
		ProductService productService = new ProductService();
		PlusSupportProduct plusProduct = new PlusSupportProduct();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		ApiCollageTeamInfoResult apiResult = new ApiCollageTeamInfoResult();
		
		MDataMap collageMap = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
		String collageStatus = collageMap.get("collage_status");//拼团状态
		apiResult.setCollageStatus(collageStatus);
		
		String eventCode = collageMap.get("event_code");//活动编码
		MDataMap collageItemInfo = DbUp.upTable("sc_event_collage_item").one("collage_code",collageCode,"collage_member",userCode);
		if(collageItemInfo == null || collageItemInfo.isEmpty()) {//没有数据的时候，证明是扫码过来的拼团，需要查团长
			collageItemInfo = DbUp.upTable("sc_event_collage_item").one("collage_code",collageCode,"collage_member_type","449748310001");
		}
		String orderCode = collageItemInfo.get("collage_ord_code");
		String skuCode = "";
		String itemCode = "";
		String sqlDetailForOrd = "SELECT * FROM ordercenter.oc_orderdetail WHERE order_code = '"+orderCode +"' AND gift_flag = 1 limit 1";
		Map<String,Object> orderDetail = DbUp.upTable("oc_orderdetail").dataSqlOne(sqlDetailForOrd, null);
		if(orderDetail == null) {
			return apiResult;
		}
		skuCode = MapUtils.getString(orderDetail, "sku_code", "");
		String sqlEventItem = "SELECT * FROM systemcenter.sc_event_item_product WHERE event_code = '"+eventCode+"' AND sku_code = '"+skuCode+"' AND flag_enable =1 limit 1";
		Map<String,Object> eventItem = DbUp.upTable("sc_event_item_product").dataSqlOne(sqlEventItem, null);
		if(eventItem == null) {
			return apiResult;
		}
		itemCode = MapUtils.getString(eventItem, "item_code", "");
		String productSql = "select t.product_code, t.product_name, t.market_price, p.favorable_price, p.sales_num, t.mainpic_url, p.cover_img, p.sales_advertisement, p.flag_enable "
				+ "from productcenter.pc_productinfo t left join sc_event_item_product p on p.sku_code = :skuCode and t.product_code = p.product_code and p.item_code = :itemCode "
				+ "where t.product_code = (select s.product_code from productcenter.pc_skuinfo s where s.sku_code = :skuCode)";
		Map<String, Object> productMap = DbUp.upTable("sc_event_item_product").dataSqlOne(productSql, new MDataMap("itemCode", itemCode, "skuCode", skuCode));
		
		apiResult.setProductCode(MapUtils.getString(productMap, "product_code", ""));//商品编码
		apiResult.setProductName(MapUtils.getString(productMap, "product_name", ""));//商品名称
		apiResult.setDescription(MapUtils.getString(productMap, "sales_advertisement", ""));//广告语
		
		skuQuery.setCode(skuCode);
		PlusModelSkuResult skuResult = plusProduct.upSkuInfo(skuQuery); 
		apiResult.setMarketPrice(skuResult.getSkus().get(0).getSkuPrice().toString());//市场价
		apiResult.setSalesNum(skuResult.getSkus().get(0).getLimitStock());//促销剩余库存
		apiResult.setActivityPrice(MapUtils.getString(productMap, "favorable_price", "0"));//活动价
		
		//优先走精修图片，如果没有精修图片，则返回商品图
		String cover_img = MapUtils.getString(productMap, "cover_img", "");
		if(!"".equals(cover_img) && "1".equals(MapUtils.getString(productMap, "flag_enable", ""))) {
			apiResult.setProductImg(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), cover_img).getPicNewUrl());//商品图片
		}else {
			apiResult.setProductImg(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), MapUtils.getString(productMap, "mainpic_url", "")).getPicNewUrl());//商品图片
		}
		apiResult.setPicUrl(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), MapUtils.getString(productMap, "mainpic_url", "")).getPicNewUrl());//商品原图
		
		String eventSql = "select i.end_time, i.collage_person_count,i.collage_type from sc_event_info i where i.event_code = :eventCode";
		Map<String, Object> eventMap = DbUp.upTable("sc_event_info").dataSqlOne(eventSql, new MDataMap("eventCode", eventCode));
		String endTime = collageMap.get("expire_time");
		if(endTime.length()>19) {
			endTime = endTime.substring(0, 19);
		}
		apiResult.setEndTime(endTime);//活动结束时间,改为取团的失效时间
		apiResult.setCollagePerson(MapUtils.getString(eventMap, "collage_person_count", ""));//参团人数
		apiResult.setCollageType(MapUtils.getString(eventMap, "collage_type"));
		//参团人员信息(昵称+头像)
		ApiCollagePersonInfo finalPerson = null;
		List<ApiCollagePersonInfo> personList = new ArrayList<ApiCollagePersonInfo>();
		String collagePersonSql = "select i.collage_member, i.re_collage, i.collage_member_type, "
				+ "(select s.nickname from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) nickname, "
				+ "(select s.avatar from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) avatar "
				+ "from sc_event_collage_item i where i.collage_code = :collageCode and i.is_confirm = '449748320002' "
				+ "order by i.collage_member_type asc, i.zid asc";
		List<Map<String, Object>> collagePersonList = DbUp.upTable("sc_event_collage_item").dataSqlList(collagePersonSql, new MDataMap("collageCode", collageCode));
		for(Map<String, Object> collagePerson : collagePersonList) {
			if("1".equals(MapUtils.getString(collagePerson, "re_collage", ""))) {
				if(MapUtils.getString(collagePerson, "collage_member", "").equals(getUserCode())) {
					apiResult.setReCollage("1");//如果当前用户为重新组团，则返回标示(在支付成功页面，展示有区别)
				}
			}
			/**
			 * 处理头像 NG++ 20190723
			 */
			String avater = MapUtils.getString(collagePerson, "avatar", "");
			String nickName = MapUtils.getString(collagePerson, "nickname", "");
			String memberCode = MapUtils.getString(collagePerson, "collage_member", "");
			if(memberCode.contains("XN")) {//robot 团
				MDataMap robotInfo = DbUp.upTable("mc_robot_info").one("member_code",memberCode);
				if(robotInfo!=null && !robotInfo.isEmpty()) {
					avater = robotInfo.get("head_photo");
					nickName = robotInfo.get("nick_name");
				}
			}
			//将当前用户放在集合的最后面(方便前端操作)，显示在前端最靠前的位置
			if(!MapUtils.getString(collagePerson, "collage_member_type", "").equals("449748310001") && MapUtils.getString(collagePerson, "collage_member", "").equals(getUserCode())) {
				finalPerson = new ApiCollagePersonInfo();
				finalPerson.setNickname(nickName);
				finalPerson.setHeadPhoto(avater);
			}else {
				ApiCollagePersonInfo personInfo = new ApiCollagePersonInfo();
				personInfo.setNickname(nickName);
				personInfo.setHeadPhoto(avater);
				personList.add(personInfo);	
			}
		}
		if(finalPerson != null) {
			personList.add(finalPerson);
		}
		apiResult.setPersonInfo(personList);
		//如果是邀新团，需要判断该用户是否满足条件
		checkUser(userCode,apiResult);
		return apiResult;
	}
	
	/**
	 * 
	 * @param userCode
	 * @param result
	 * 2020年5月20日
	 * Angel Joy
	 * void
	 */
	private void checkUser(String userCode,ApiCollageTeamInfoResult result) {
		//校验用户是否满足资格
		String collageType = result.getCollageType();
		if("4497473400050001".equals(collageType)) {//普通团，直接返回可以参团。
			return;
		}
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("member_code",userCode,"manage_code","SI2003");
		if(loginInfo == null) {
			return;
		}
		String createTime = loginInfo.get("create_time");
		String expireTime = DateUtil.addDateHour(createTime, 24);
		if(DateUtil.compareDate1(DateUtil.getSysDateTimeString(),expireTime)) {
			result.setSupportCollage("0");//非新用户
			return;
		}
		//校验有没有下过单
		if(DbUp.upTable("oc_orderinfo").count("buyer_code",userCode)>0) {
			result.setSupportCollage("0");//下过单，非新用户
			return;
		}
		result.setSupportCollage("1");
	}

	/**
	 * 旧逻辑暂时保留，机器人开团后需要大改
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	private ApiCollageTeamInfoResult oldProcess(ApiCollageTeamInfoInput inputParam, MDataMap mRequestMap) {
		String collageCode = inputParam.getCollageCode();
		String maxWidth = inputParam.getMaxWidth();
		
		ProductService productService = new ProductService();
		PlusSupportProduct plusProduct = new PlusSupportProduct();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		ApiCollageTeamInfoResult apiResult = new ApiCollageTeamInfoResult();
		
		String sql = "select d.sku_code, c.event_code, c.collage_status, a.out_active_code,c.expire_time expire_time from sc_event_collage_item i, sc_event_collage c, ordercenter.oc_orderdetail d, ordercenter.oc_order_activity a "
				+ "where i.collage_code = :collageCode and i.is_confirm = '449748320002' and i.collage_member_type = '449748310001' and i.collage_ord_code = d.order_code "
				+ "and c.collage_code = i.collage_code and d.order_code = a.order_code and d.sku_code = a.sku_code and a.activity_type = '4497472600010024'";
		Map<String, Object> map = DbUp.upTable("sc_event_collage_item").dataSqlOne(sql, new MDataMap("collageCode", collageCode));
		if(map != null) {
			String collageStatus = MapUtils.getString(map, "collage_status", "");//拼团状态
			apiResult.setCollageStatus(collageStatus);
			
			String eventCode = MapUtils.getString(map, "event_code", "");//活动编码
			String itemCode = MapUtils.getString(map, "out_active_code", "");//活动明细编码
			String skuCode = MapUtils.getString(map, "sku_code", "");//sku编码
			String productSql = "select t.product_code, t.product_name, t.market_price, p.favorable_price, p.sales_num, t.mainpic_url, p.cover_img, p.sales_advertisement, p.flag_enable "
					+ "from productcenter.pc_productinfo t left join sc_event_item_product p on p.sku_code = :skuCode and t.product_code = p.product_code and p.item_code = :itemCode "
					+ "where t.product_code = (select s.product_code from productcenter.pc_skuinfo s where s.sku_code = :skuCode)";
			Map<String, Object> productMap = DbUp.upTable("sc_event_item_product").dataSqlOne(productSql, new MDataMap("itemCode", itemCode, "skuCode", skuCode));
			
			apiResult.setProductCode(MapUtils.getString(productMap, "product_code", ""));//商品编码
			apiResult.setProductName(MapUtils.getString(productMap, "product_name", ""));//商品名称
			apiResult.setDescription(MapUtils.getString(productMap, "sales_advertisement", ""));//广告语
			
			skuQuery.setCode(MapUtils.getString(map, "sku_code", ""));
			PlusModelSkuResult skuResult = plusProduct.upSkuInfo(skuQuery); 
			apiResult.setMarketPrice(skuResult.getSkus().get(0).getSkuPrice().toString());//市场价
			apiResult.setSalesNum(skuResult.getSkus().get(0).getLimitStock());//促销剩余库存
			apiResult.setActivityPrice(MapUtils.getString(productMap, "favorable_price", "0"));//活动价
			
			//优先走精修图片，如果没有精修图片，则返回商品图
			String cover_img = MapUtils.getString(productMap, "cover_img", "");
			if(!"".equals(cover_img) && "1".equals(MapUtils.getString(productMap, "flag_enable", ""))) {
				apiResult.setProductImg(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), cover_img).getPicNewUrl());//商品图片
			}else {
				apiResult.setProductImg(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), MapUtils.getString(productMap, "mainpic_url", "")).getPicNewUrl());//商品图片
			}
			apiResult.setPicUrl(productService.getPicInfoOprBig(Integer.parseInt(maxWidth), MapUtils.getString(productMap, "mainpic_url", "")).getPicNewUrl());//商品原图
			
			String eventSql = "select i.end_time, i.collage_person_count from sc_event_info i where i.event_code = :eventCode";
			Map<String, Object> eventMap = DbUp.upTable("sc_event_info").dataSqlOne(eventSql, new MDataMap("eventCode", eventCode));
			String endTime = MapUtils.getString(map, "expire_time", "");
			if(endTime.length()>19) {
				endTime = endTime.substring(0, 19);
			}
			apiResult.setEndTime(endTime);//活动结束时间,改为取团的失效时间
			apiResult.setCollagePerson(MapUtils.getString(eventMap, "collage_person_count", ""));//参团人数
			
			//参团人员信息(昵称+头像)
			ApiCollagePersonInfo finalPerson = null;
			List<ApiCollagePersonInfo> personList = new ArrayList<ApiCollagePersonInfo>();
			String collagePersonSql = "select i.collage_member, i.re_collage, i.collage_member_type, "
					+ "(select s.nickname from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) nickname, "
					+ "(select s.avatar from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) avatar "
					+ "from sc_event_collage_item i where i.collage_code = :collageCode and i.is_confirm = '449748320002' "
					+ "order by i.collage_member_type asc, i.zid asc";
			List<Map<String, Object>> collagePersonList = DbUp.upTable("sc_event_collage_item").dataSqlList(collagePersonSql, new MDataMap("collageCode", collageCode));
			this.setRobotInfo(collagePersonList);//有部分订单可能是机器人拼单成功，需要获取机器人信息
			for(Map<String, Object> collagePerson : collagePersonList) {
				if("1".equals(MapUtils.getString(collagePerson, "re_collage", ""))) {
					if(MapUtils.getString(collagePerson, "collage_member", "").equals(getUserCode())) {
						apiResult.setReCollage("1");//如果当前用户为重新组团，则返回标示(在支付成功页面，展示有区别)
					}
				}
				/**
				 * 处理头像 NG++ 20190723
				 */
				String avater = MapUtils.getString(collagePerson, "avatar", "");
				String nickName = MapUtils.getString(collagePerson, "nickname", "");
				String memberCode = MapUtils.getString(collagePerson, "collage_member", "");
				if(memberCode.contains("XN")) {//robot 团
					MDataMap robotInfo = DbUp.upTable("mc_robot_info").one("member_code",memberCode);
					if(robotInfo!=null && !robotInfo.isEmpty()) {
						avater = robotInfo.get("head_photo");
						nickName = robotInfo.get("nick_name");
					}
				}
				//将当前用户放在集合的最后面(方便前端操作)，显示在前端最靠前的位置
				if(!MapUtils.getString(collagePerson, "collage_member_type", "").equals("449748310001") && MapUtils.getString(collagePerson, "collage_member", "").equals(getUserCode())) {
					finalPerson = new ApiCollagePersonInfo();
					finalPerson.setNickname(nickName);
					finalPerson.setHeadPhoto(avater);
				}else {
					ApiCollagePersonInfo personInfo = new ApiCollagePersonInfo();
					personInfo.setNickname(nickName);
					personInfo.setHeadPhoto(avater);
					personList.add(personInfo);	
				}
			}
			if(finalPerson != null) {
				personList.add(finalPerson);
			}
			apiResult.setPersonInfo(personList);
			
		}
		return apiResult;
	}

	/**
	 * 查询机器人的头像以及昵称
	 * @param collagePersonList
	 */
	private void setRobotInfo(List<Map<String, Object>> collagePersonList) {
		for (Map<String, Object> map : collagePersonList) {
			if(map == null) {
				continue;
			}
			String memberCode = MapUtils.getString(map, "collage_member", "");
			if(memberCode.contains("X")) {//以X开头的为robot
				MDataMap robotInfo = DbUp.upTable("mc_robot_info").one("member_code",memberCode);
				if(robotInfo == null || robotInfo.isEmpty()) {
					continue;
				}
				map.put("avatar", robotInfo.get("head_photo"));
				map.put("nickname", robotInfo.get("nick_name"));
			}
		}
	}
}
