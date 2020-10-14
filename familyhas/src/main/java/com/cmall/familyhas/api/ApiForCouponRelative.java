package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForCouponRelativeResult;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootApiForVersion;


/** 
* @ClassName: ApiForCouponRelative 
* @Description: 获取优惠券相关设置信息
* @author 张海生
* @date 2015-6-11 下午4:22:28 
*  
*/
public class ApiForCouponRelative extends RootApiForVersion<ApiForCouponRelativeResult, RootInput> {

	public ApiForCouponRelativeResult Process(RootInput inputParam,MDataMap mRequestMap) {
		
		ApiForCouponRelativeResult result = new ApiForCouponRelativeResult();
		List<MDataMap> pMapList = DbUp.upTable("oc_coupon_relative").queryAll(
				"relative_type,activity_code", "", "",
				new MDataMap("manage_code", getManageCode()));
		MDataMap resultMap = new MDataMap();
		if(pMapList != null && pMapList.size() > 0){
			List<String> rgList = new ArrayList<String>();//注册送券对应的活动
			List<String> zxList = new ArrayList<String>();//在线支付下单送对应的活动
			List<String> zzList = new ArrayList<String>();//在线支付支付支付送对应的活动
			List<String> zsList = new ArrayList<String>();//在线支付收获送对应的活动
			List<String> hxList = new ArrayList<String>();//活动付款下单送对应的活动
			List<String> hsList = new ArrayList<String>();//货到付款收货送对应的活动
			List<String> tjList = new ArrayList<String>();//推荐人送券对应的活动
			
			List<String> rgList1 = new ArrayList<String>();//注册送券对应的活动名称
			List<String> zxList1 = new ArrayList<String>();//在线支付下单送对应的活动名称
			List<String> zzList1 = new ArrayList<String>();//在线支付支付支付送对应的活动名称
			List<String> zsList1 = new ArrayList<String>();//在线支付收获送对应的活动名称
			List<String> hxList1 = new ArrayList<String>();//活动付款下单送对应的活动名称
			List<String> hsList1 = new ArrayList<String>();//货到付款收货送对应的活动名称
			List<String> tjList1 = new ArrayList<String>();//推荐人送券对应的活动名称
			MDataMap whereMap = new MDataMap();
			whereMap.put("activity_type", "449715400007");
			whereMap.put("seller_code", getManageCode());
			List<MDataMap> activityList = DbUp.upTable("oc_activity").queryAll(
					"activity_code,activity_name", "", "",whereMap);//查询优惠券活动
			for (MDataMap mDataMap : pMapList) {
				String activityCode = mDataMap.get("activity_code");
				String typeCode = mDataMap.get("relative_type");
				if("1".equals(typeCode)){
					rgList.add(activityCode);
					rgList1.add(this.getActivityName(activityCode, activityList));
				}else if("2".equals(typeCode)){
					zxList.add(activityCode);
					zxList1.add(this.getActivityName(activityCode, activityList));
				}else if("3".equals(typeCode)){
					zzList.add(activityCode);
					zzList1.add(this.getActivityName(activityCode, activityList));
				}else if("4".equals(typeCode)){
					zsList.add(activityCode);
					zsList1.add(this.getActivityName(activityCode, activityList));
				}else if("5".equals(typeCode)){
					hxList.add(activityCode);
					hxList1.add(this.getActivityName(activityCode, activityList));
				}else if("6".equals(typeCode)){
					hsList.add(activityCode);
					hsList1.add(this.getActivityName(activityCode, activityList));
				}else if("7".equals(typeCode)){
					tjList.add(activityCode);
					tjList1.add(this.getActivityName(activityCode, activityList));
				}
			}
			resultMap.put("a", StringUtils.join(rgList, ","));
			resultMap.put("b", StringUtils.join(zxList, ","));
			resultMap.put("c", StringUtils.join(zzList, ","));
			resultMap.put("d", StringUtils.join(zsList, ","));
			resultMap.put("e", StringUtils.join(hxList, ","));
			resultMap.put("f", StringUtils.join(hsList, ","));
			resultMap.put("g", StringUtils.join(tjList, ","));
			
			resultMap.put("aa", StringUtils.join(rgList1, ","));
			resultMap.put("bb", StringUtils.join(zxList1, ","));
			resultMap.put("cc", StringUtils.join(zzList1, ","));
			resultMap.put("dd", StringUtils.join(zsList1, ","));
			resultMap.put("ee", StringUtils.join(hxList1, ","));
			resultMap.put("ff", StringUtils.join(hsList1, ","));
			resultMap.put("gg", StringUtils.join(tjList1, ","));
		}
		
		List<MDataMap> p2MapList = DbUp.upTable("oc_coupon_relative_expand").queryAll(
				"relative_type,activity_code,sign_days_get_coupon", "", "",
				new MDataMap("manage_code", getManageCode()));
		if(p2MapList!=null&&p2MapList.size()>0){
			
			MDataMap whereMap = new MDataMap();
			whereMap.put("activity_type", "449715400007");
			whereMap.put("seller_code", getManageCode());
			List<MDataMap> activityList = DbUp.upTable("oc_activity").queryAll(
					"activity_code,activity_name", "", "",whereMap);//查询优惠券活动
			resultMap.put("sign", "8");
			StringBuilder returnStr = new StringBuilder();
			for(int i =0;i<p2MapList.size();i++){
				MDataMap mDataMap = p2MapList.get(i);
				String activityCode = mDataMap.get("activity_code");
				String activityName = this.getActivityName(activityCode, activityList);
				String signDays = mDataMap.get("sign_days_get_coupon");
				if(i<p2MapList.size()-1){
					returnStr.append(signDays+"="+activityName+"="+activityCode+",");
				}else{
					returnStr.append(signDays+"="+activityName+"="+activityCode);
				}
			}
			resultMap.put("signActivity", returnStr.toString());
		}
		
		//562用户行为：连续启动 连续添加购物车
		List<MDataMap> p3MapList = DbUp.upTable("oc_coupon_relative_behavior").queryAll(
				"relative_type,activity_code,start_up_days,add_shop_car", "", "",
				new MDataMap("manage_code", getManageCode()));
		if(p3MapList!=null&&p3MapList.size()>0){
			MDataMap whereMap = new MDataMap();
			whereMap.put("activity_type", "449715400007");
			whereMap.put("seller_code", getManageCode());
			List<MDataMap> activityList = DbUp.upTable("oc_activity").queryAll(
					"activity_code,activity_name", "", "",whereMap);//查询优惠券活动
			StringBuilder returnStr = new StringBuilder();
			resultMap.put("behavior_flag","behavior_flag");
			for(int i =0;i<p3MapList.size();i++){
				MDataMap mDataMap = p3MapList.get(i);
				String activityCode = mDataMap.get("activity_code");
				String activityName = this.getActivityName(activityCode, activityList);
				String startUpDays = mDataMap.get("start_up_days");
				String addShopCars = mDataMap.get("add_shop_car");
				String relative_type = mDataMap.get("relative_type");
				if(i<p3MapList.size()-1){
					returnStr.append(startUpDays+"="+addShopCars+"="+activityName+"="+activityCode+"="+relative_type+",");
				}else{
					returnStr.append(startUpDays+"="+addShopCars+"="+activityName+"="+activityCode+"="+relative_type);
				}
			}
			resultMap.put("behaviorActivity", returnStr.toString());
		}
		
		result.setRelativeMap(resultMap);
		return result;
	}
	
	/** 
	* @Description:根据活动code获取活动名称
	* @param activityCode 活动code
	* @param activityList 活动信息
	* @author 张海生
	* @date 2015-6-11 下午7:43:03
	* @return String 
	* @throws 
	*/
	public String getActivityName(String activityCode, List<MDataMap> activityList){
		String activityName = "";
		for (MDataMap mDataMap : activityList) {
			if(activityCode.equals(mDataMap.get("activity_code"))){
				activityName = mDataMap.get("activity_name");
				break;
			}
		}
		return activityName;
	}
}
