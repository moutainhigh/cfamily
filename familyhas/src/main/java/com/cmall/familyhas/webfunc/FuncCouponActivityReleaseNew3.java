package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.helper.PlusHelperScheduler;
import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType.CouponTypeLimit;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncCouponActivityRelease
 * @Description: 优惠券活动发布管理（发布/取消发布）
 * @author ligj
 * time:2015-05-02 15:24:00
 * 
 */
public class FuncCouponActivityReleaseNew3 extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String activity_code = mDataMap.get("zw_f_activity_code");
		MDataMap mdata = DbUp.upTable("oc_activity").oneWhere("flag,end_time,provide_num,provide_type,begin_time,activity_type","","","activity_code", activity_code);
		String flag = mdata.get("flag");
		
		
		//系统返利同一时间段只能发布一个
		if("4497471600060005".equals(mdata.get("provide_type"))&&"0".equals(flag)) {
			String begin_time = mdata.get("begin_time");
			String end_time = mdata.get("end_time");
			 Map<String, Object> one = DbUp.upTable("oc_activity").dataSqlOne("select IFNULL(count(*),0) num from oc_activity where provide_type='4497471600060005' and  flag=1 and activity_code!=:activity_code and ((begin_time<='"+begin_time+"' and end_time>='"+end_time+"') or (begin_time>='"+begin_time+"' and begin_time<='"+end_time+"') or (end_time>='"+begin_time+"' and  end_time<='"+end_time+"') )  ", new MDataMap("activity_code", activity_code));
			 int parseInt = Integer.parseInt(one.get("num").toString());
			 if(parseInt>0) {
				 mResult.setResultCode(0);
					mResult.setResultMessage("同一时间段只能发布一个系统返利活动！");
					return mResult;
			 }
		}
		// 如果是发布活动则检查活动使用限制条件是否正确设置
		if("0".equals(flag)) {
			List<MDataMap> typeCodeList = DbUp.upTable("oc_coupon_type").queryAll("coupon_type_code,coupon_type_name", "", "", new MDataMap("activity_code", activity_code, "status", "4497469400030002", "limit_condition", "4497471600070002"));
			for(MDataMap map : typeCodeList) {
				if(DbUp.upTable("oc_coupon_type_limit").count("coupon_type_code",map.get("coupon_type_code")) == 0) {
					mResult.setResultCode(0);
					mResult.setResultMessage("未维护优惠券类型使用限制条件:"+map.get("coupon_type_code"));
					return mResult;
				}
			}
			if(mdata.get("activity_type").equals("449715400008")){
				List<MDataMap> fbList = DbUp.upTable("oc_activity").queryAll("begin_time,end_time", "", "", new MDataMap("activity_type", "449715400008", "flag", "1"));
				for(MDataMap map1 : fbList) {
					if(mdata.get("begin_time").compareTo(map1.get("end_time"))<=0&&mdata.get("end_time").compareTo(map1.get("begin_time"))>=0) {
						mResult.setResultCode(0);
						mResult.setResultMessage("发布时间重合");
						return mResult;
					}
				}
			}
			
		}
		
		MDataMap dataMap = new MDataMap();
		/* 获取当前修改人 */
		String updater = UserFactory.INSTANCE.create().getLoginName();
		if ("1".equals(flag)) {				// 已发布就设为未发布
			dataMap.put("flag", "0");
		} else if ("0".equals(flag)) {// 未发布就设为已发布
			//控制未发布的失效优惠活动不能设为已发布
			if (mdata.get("end_time").compareTo(DateUtil.getSysDateTimeString()) <= 0 ) {
				mResult.inErrorMessage(916401227);
				return mResult;
			}
			dataMap.put("flag", "1");
		}
		dataMap.put("updator", updater);
		dataMap.put("update_time", DateUtil.getSysDateTimeString());
		dataMap.put("activity_code", activity_code);
		
		// 先清理缓存再更新表
		new LoadCouponActivity().deleteInfoByCode(activity_code);
		
		DbUp.upTable("oc_activity").dataUpdate(dataMap,
				"flag,updator,update_time", "activity_code");
		
		// 检查发放份数是在不一致，如果不一致则更新
		List<MDataMap> couponTypeList = DbUp.upTable("oc_coupon_type").queryByWhere("activity_code", activity_code);
		int provideNum = NumberUtils.toInt(mdata.get("provide_num"));
		
		if(provideNum > 0) {
			int totalNum;
			for(MDataMap map : couponTypeList) {
				// 折扣券和0元优惠券只存份数
				if("449748120002".equals(map.get("money_type")) || new BigDecimal(map.get("money")).compareTo(BigDecimal.ZERO) <= 0) {
					totalNum = new BigDecimal(map.get("total_money")).intValue();
					// 总发放份数
					map.put("total_money", provideNum+"");
				} else {
					totalNum = new BigDecimal(map.get("total_money")).divide(new BigDecimal(map.get("money")),0, BigDecimal.ROUND_HALF_UP).intValue();
					// 总发放金额
					map.put("total_money", new BigDecimal(provideNum).multiply(new BigDecimal(map.get("money"))).intValue()+"");
				}
				
				// 更新发放份数
				if(totalNum != provideNum) {
					String sSql = "UPDATE oc_coupon_type set total_money = :total_money, surplus_money = total_money - privide_money WHERE zid = :zid";
					DbUp.upTable("oc_coupon_type").dataExec(sSql, map);
				}

			}
		}
		
		// 全部处理完成后再次刷新缓存
		new LoadCouponActivity().refresh(new PlusModelQuery(activity_code));;
		for(MDataMap map : couponTypeList) {
			new LoadCouponType().refresh(new PlusModelQuery(map.get("coupon_type_code")));;
			
			//5.5.8发布活动时存在商品限定时 再更新solr索引 -rhb 20191230
			if("0".equals(flag)) {
				PlusModelCouponType plusModelCouponType = new LoadCouponType()
						.upInfoByCode(new PlusModelQuery(map.get("coupon_type_code")));
				//限制条件：指定
				if("4497471600070002".equals(plusModelCouponType.getLimitCondition())) {
					CouponTypeLimit couponTypeLimit = plusModelCouponType.getCouponTypeLimit();
					//商品正向限定
					if("4497471600070002".equals(couponTypeLimit.getProductLimit())
							&& couponTypeLimit.getExceptProduct() == 0) {
						//添加异步任务
						PlusHelperScheduler.sendSchedler(EPlusScheduler.UpdateCouponGoods,
								KvHelper.upCode(EPlusScheduler.UpdateCouponGoods.toString()), map.get("coupon_type_code"));
					}
				}
			}
		}
		
		// 刷新分销券活动缓存
		if(mdata.get("activity_type").equals("449715400008")){
			new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
		}
		
		return mResult;
	}

}
