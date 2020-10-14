package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @ClassName: CdkeyRelativeFuncAdd 
* @Description: 添加优惠券相关设置
* @author 张海生
* @date 2015-6-5 下午3:49:16 
*  
*/
public class CdkeyRelativeFuncAdd extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		/* 系统当前时间 */
		String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		MDataMap insertMap = new MDataMap();
		insertMap.put("create_time", create_time);
		insertMap.put("create_user", create_user);
		insertMap.put("update_time", create_time);
		insertMap.put("update_user", create_user);
		String manageCode = UserFactory.INSTANCE.create().getManageCode();
		insertMap.put("manage_code", manageCode);
		try{
			DbUp.upTable("oc_coupon_relative").dataDelete("manage_code=:manage_code", new MDataMap("manage_code",manageCode), "manage_code");//先清除表中的数据
			DbUp.upTable("oc_coupon_relative_expand").dataDelete("manage_code=:manage_code", new MDataMap("manage_code",manageCode), "manage_code");//先清除表中的数据
			DbUp.upTable("oc_coupon_relative_behavior").dataDelete("manage_code=:manage_code", new MDataMap("manage_code",manageCode), "manage_code");//先清除表中的数据

			boolean flag = false;
			Integer cycle = 0;
			for (int i = 1; i <= 30; i++) {
				String acIds = mAddMaps.get("actictityIds"+i);
				if(StringUtils.isNotEmpty(acIds)){
					if(i<=7){
						insertMap.put("relative_type", ""+i);//相关类型(1:注册送券，2:在线支付下单送，3:在线支付支付送4:在线支付收获送5:货到付款下单送6:货到付款收获送,7:推荐人送券)
						for (String ac : acIds.split(",")) {
							insertMap.put("activity_code", ac);
							DbUp.upTable("oc_coupon_relative").dataInsert(insertMap);//插入到数据库
						}
					}else if(i>7&&i<=28){
						if(!flag) {
							flag = true;
						}
						insertMap.put("relative_type", "8");//8:签到相关
						for (String ac : acIds.split(",")) {
							insertMap.put("activity_code", ac);
							insertMap.put("sign_days_get_coupon", (i-7)+"");
							DbUp.upTable("oc_coupon_relative_expand").dataInsert(insertMap);//插入到数据库
						}
						cycle  = i - 7;
					}else if(i>28){
						//用户行为：连续n天启动 ；连续n天每天加入购物车m件商品
						if(insertMap.containsKey("sign_days_get_coupon")) {
							insertMap.remove("sign_days_get_coupon");
						}
						if(i==29&&mAddMaps.get("actictityIds29")!=null) {
							insertMap.put("activity_code",mAddMaps.get("actictityIds29"));
							insertMap.put("relative_type", "9");
							if(StringUtils.isBlank(mAddMaps.get("start_up1"))) {
								mResult.setResultCode(0);
								mResult.setResultMessage("请填写连续启动天数！");
								return mResult;
							}
							insertMap.put("start_up_days", mAddMaps.get("start_up1"));
						}
						else if(i==30&&mAddMaps.get("actictityIds30")!=null) {
							insertMap.put("activity_code", mAddMaps.get("actictityIds30"));
							insertMap.put("relative_type", "10");
							if(StringUtils.isBlank(mAddMaps.get("start_up2"))) {
								mResult.setResultCode(0);
								mResult.setResultMessage("请填写连续启动天数！");
								return mResult;
							}
							if(StringUtils.isBlank(mAddMaps.get("add_shop_car"))) {
								mResult.setResultCode(0);
								mResult.setResultMessage("请填写加入购物车件数！");
								return mResult;
							}
							insertMap.put("start_up_days", mAddMaps.get("start_up2"));
							insertMap.put("add_shop_car", mAddMaps.get("add_shop_car"));
						}
						DbUp.upTable("oc_coupon_relative_behavior").dataInsert(insertMap);//插入到数据库

					}
				}
			}
			
			if(flag) {
				//需要更新缓存
				XmasKv.upFactory(EKvSchema.SignCycle).set("signCycle",cycle+"");
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
