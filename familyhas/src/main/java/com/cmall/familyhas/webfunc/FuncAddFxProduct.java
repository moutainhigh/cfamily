package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.load.LoadCouponListForProduct;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportFenxiao;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加分销商品
 * @author sy
 *
 */
public class FuncAddFxProduct extends FuncAdd {

	static PlusSupportFenxiao plusSupportFenxiao = new PlusSupportFenxiao();
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		MDataMap mInsertMap = new MDataMap();
		String activity_code = mDataMap.get("obj[activity_code]")==null?"":mDataMap.get("obj[activity_code]");
		String product_codes = mDataMap.get("obj[product_code]")==null?"":mDataMap.get("obj[product_code]");
		for(String product_code:product_codes.split(",")){
			if(StringUtils.isNotBlank(product_code)){
				int count = DbUp.upTable("oc_activity_agent_product").count("activity_code",activity_code,"produt_code",product_code,"flag_enable","1");
				if(count>0){
					mResult.setResultCode(-1);
					mResult.setResultMessage("该商品"+product_code+"已经存在");
					return mResult;
				}
				MDataMap couponInfo = plusSupportFenxiao.getFenxiaoCouponInfo(product_code);
				//添加优惠券类型
				MDataMap maps=new MDataMap();
				String coupon_type_code = WebHelper.upCode("CT");
				maps.put("coupon_type_code", coupon_type_code);
				maps.put("coupon_type_name", "分销券");
				maps.put("activity_code", activity_code);
				maps.put("money", couponInfo.get("coupon_money"));
				maps.put("status", "4497469400030002");
				maps.put("limit_condition", "4497471600070002");
				maps.put("limit_scope", "指定商品可用");
				maps.put("valid_type", "4497471600080001");
				maps.put("valid_day", "999");
				
				maps.put("create_time", createTime);
				maps.put("creater", create_user);
				maps.put("update_time", createTime);
				maps.put("updater", create_user);
				maps.put("manage_code", UserFactory.INSTANCE.create().getManageCode());
				DbUp.upTable("oc_coupon_type").dataInsert(maps);
				//添加优惠券商品限制
				MDataMap maps1=new MDataMap();
				maps1.put("coupon_type_code", coupon_type_code);
				maps1.put("activity_code", activity_code);
				maps1.put("brand_limit", "4497471600070001");
				maps1.put("product_limit", "4497471600070002");
				maps1.put("category_limit", "4497471600070001");
				maps1.put("channel_limit", "4497471600070002");
				maps1.put("activity_limit", "449747110001");
				maps1.put("product_codes", product_code);
				maps1.put("channel_codes", "449747430023");
				DbUp.upTable("oc_coupon_type_limit").dataInsert(maps1);
				
				mInsertMap.put("zw_f_update_user", create_user);
				mInsertMap.put("zw_f_create_time", createTime);
				mInsertMap.put("zw_f_update_time", createTime);
				mInsertMap.put("zw_f_activity_code", activity_code);
				mInsertMap.put("zw_f_produt_code", product_code);
				mInsertMap.put("zw_f_sell_price", couponInfo.get("sell_price"));
				mInsertMap.put("zw_f_cost_price", couponInfo.get("cost_price"));
				mInsertMap.put("zw_f_coupon_money", couponInfo.get("coupon_money"));
				mInsertMap.put("zw_f_coupon_type_code", coupon_type_code);
				mResult = super.funcDo(sOperateUid, mInsertMap);
				new LoadCouponListForProduct().deleteInfoByCode(product_code);
			}
		}		
		new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));		
		return mResult;
	}
}
