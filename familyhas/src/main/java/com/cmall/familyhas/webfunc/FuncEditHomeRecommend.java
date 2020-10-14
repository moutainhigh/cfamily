package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.usercenter.service.SellercategoryService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改首页推荐分类
 * @author lee
 *
 */
public class FuncEditHomeRecommend extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		
		String categoryCode = mDataMap.get("zw_f_category_code");		//分类编号   ，此字段在addHomeRecommend.ftl中定义，为了接收分类控件回调
		
		if (StringUtils.isBlank(categoryCode)) {
			//请选择一个分类
			mResult.setResultCode(916423210);
			mResult.setResultMessage(bInfo(916423210));
			return mResult;
		}
		
		String categoryName = new SellercategoryService().getCateGoryByCode(categoryCode,"SI2003");		//分类名称
		
		mDataMap.put("zw_f_category_code",categoryCode);
		mDataMap.put("zw_f_category_name",categoryName == null ? "" : categoryName);
		mDataMap.put("zw_f_update_time", createTime);
		mDataMap.put("zw_f_update_user", create_user);
		
		
		//位置不能重复，位置越大，越靠前
		if (this.checkRepeatSort(mDataMap.get("zw_f_sort"),mDataMap.get("zw_f_uid")) == 1) {
			mResult.setResultCode(916423211);	//位置重复
			mResult.setResultMessage(bInfo(916423211));
			return mResult;
		}
		//分类不能重复添加
		if (this.checkRepeatCategory(categoryCode,mDataMap.get("zw_f_uid")) >= 1) {
			mResult.setResultCode(916423212);
			mResult.setResultMessage(bInfo(916423212));
			return mResult;
		}
		mResult = super.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
	private int checkRepeatSort(String sort,String uid){
		String sWhere =" sort='"+sort+"' and uid != '"+uid+"' ";
		MDataMap map = DbUp.upTable("fh_home_recommend").oneWhere("uid", "", sWhere);
		if (null == map || map.isEmpty()) {
			return 0;
		}
		return 1;
	}
	private int checkRepeatCategory(String categoryCode,String uid){
		String sWhere =" category_code='"+categoryCode+"' and uid != '"+uid+"' ";
		MDataMap map = DbUp.upTable("fh_home_recommend").oneWhere("uid", "", sWhere);
		if (null == map || map.isEmpty()) {
			return 0;
		}
		return 1;
	}
	
}
