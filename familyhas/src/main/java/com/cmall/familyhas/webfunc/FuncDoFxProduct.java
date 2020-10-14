package com.cmall.familyhas.webfunc;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.load.LoadCouponListForProduct;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportFenxiao;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 操作分销商品
 * @author sy
 *
 */
public class FuncDoFxProduct extends RootFunc {
	
	static PlusSupportFenxiao plusSupportFenxiao = new PlusSupportFenxiao();

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String activity_code = mDataMap.get("obj[activity_code]")==null?"":mDataMap.get("obj[activity_code]");
		String product_code = mDataMap.get("obj[product_code]")==null?"":mDataMap.get("obj[product_code]");
		String do_type = mDataMap.get("obj[do_type]")==null?"":mDataMap.get("obj[do_type]");
		if (mResult.upFlagTrue()) {
			if(do_type.equals("2")){//作废商品
				MDataMap mInsertMap = new MDataMap();
				mInsertMap.put("flag_enable", "0");
				mInsertMap.put("activity_code", activity_code);
				mInsertMap.put("produt_code", product_code);
				DbUp.upTable("oc_activity_agent_product").dataUpdate(mInsertMap, "flag_enable", "activity_code,produt_code");
				new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
				new LoadCouponListForProduct().deleteInfoByCode(product_code);
			}else if (do_type.equals("3")) {//初始化商品分销券金额
				MDataMap couponInfo = plusSupportFenxiao.getFenxiaoCouponInfo(product_code);
				
				String sql1="SELECT * from ordercenter.oc_activity_agent_product where activity_code = :activity_code and produt_code = :product_code and flag_enable = 1 LIMIT 1";
				Map<String, Object> temp = DbUp.upTable("oc_activity_agent_product").dataSqlOne(sql1, new MDataMap("activity_code",activity_code,"product_code",product_code));
				
				MDataMap mInsertMap = new MDataMap();
				mInsertMap.put("coupon_money", couponInfo.get("coupon_money"));
				mInsertMap.put("activity_code", activity_code);
				mInsertMap.put("produt_code", product_code);
				mInsertMap.put("coupon_type_code", temp.get("coupon_type_code").toString());
				mInsertMap.put("sell_price", couponInfo.get("sell_price"));
				mInsertMap.put("cost_price", couponInfo.get("cost_price"));
				DbUp.upTable("oc_activity_agent_product").dataUpdate(mInsertMap, "coupon_money,cost_price,sell_price", "activity_code,coupon_type_code,produt_code");

				MDataMap mInsertMap1 = new MDataMap();
				mInsertMap1.put("money", couponInfo.get("coupon_money"));
				mInsertMap1.put("activity_code", activity_code);
				mInsertMap1.put("coupon_type_code", temp.get("coupon_type_code")==null?"":temp.get("coupon_type_code").toString());
				DbUp.upTable("oc_coupon_type").dataUpdate(mInsertMap1, "money", "activity_code,coupon_type_code");
				new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
				new LoadCouponListForProduct().deleteInfoByCode(product_code);
			}else if (do_type.equals("4")) {//批量作废
				for(String pro_code:product_code.split(",")){
					if(StringUtils.isNotBlank(pro_code)){
						MDataMap mInsertMap = new MDataMap();
						mInsertMap.put("flag_enable", "0");
						mInsertMap.put("activity_code", activity_code);
						mInsertMap.put("produt_code", pro_code);
						DbUp.upTable("oc_activity_agent_product").dataUpdate(mInsertMap, "flag_enable", "activity_code,produt_code");
						new LoadCouponListForProduct().deleteInfoByCode(pro_code);
					}
				}
				new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
			}else if (do_type.equals("5")) {//批量初始化商品分销券金额
				for(String pro_code:product_code.split(",")){
					if(StringUtils.isNotBlank(pro_code)){
						MDataMap couponInfo = plusSupportFenxiao.getFenxiaoCouponInfo(pro_code);
						
						MDataMap mInsertMap = new MDataMap();
						mInsertMap.put("coupon_money", couponInfo.get("coupon_money"));
						mInsertMap.put("activity_code", activity_code);
						mInsertMap.put("produt_code", pro_code);
						mInsertMap.put("sell_price", couponInfo.get("sell_price"));
						mInsertMap.put("cost_price", couponInfo.get("cost_price"));
						DbUp.upTable("oc_activity_agent_product").dataUpdate(mInsertMap, "coupon_money,cost_price,sell_price", "activity_code,produt_code");
						String sql1="SELECT * from ordercenter.oc_activity_agent_product where activity_code = :activity_code and produt_code = :product_code and flag_enable = 1 LIMIT 1";
						Map<String, Object> temp = DbUp.upTable("oc_activity_agent_product").dataSqlOne(sql1, new MDataMap("activity_code",activity_code,"product_code",pro_code));
						MDataMap mInsertMap1 = new MDataMap();
						mInsertMap1.put("money", couponInfo.get("coupon_money"));
						mInsertMap1.put("activity_code", activity_code);
						mInsertMap1.put("coupon_type_code", temp.get("coupon_type_code")==null?"":temp.get("coupon_type_code").toString());
						DbUp.upTable("oc_coupon_type").dataUpdate(mInsertMap1, "money", "activity_code,coupon_type_code");
						new LoadCouponListForProduct().deleteInfoByCode(pro_code);
					}
				}
				new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
			}
		}
		
		return mResult;
	}
}
