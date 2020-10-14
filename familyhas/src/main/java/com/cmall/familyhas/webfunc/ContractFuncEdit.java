package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @ClassName: contractTypeFuncAdd 
* @Description: 编辑合同类型
* @author 张海生
* @date 2015-9-10 下午4:14:12 
*  
*/
public class ContractFuncEdit extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String create_time = DateUtil.getNowTime();// 系统当前时间
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_create_time", create_time);
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_update_time", create_time);
		mDataMap.put("zw_f_update_user", create_user);
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if (!DateUtil.getTimefag(endTime, startTime)) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("合同到期日要大于合同开始日");
			return mResult;
		} else if (!DateUtil.getTimefag(endTime, DateUtil.getNowTime())) {
			mResult.setResultCode(-2);
			mResult.setResultMessage("合同到期日要大于当前时间");
			return mResult;
		}
		String contractType = mDataMap.get("zw_f_contract_type");
		MDataMap cp= DbUp.upTable("fh_contract_type").oneWhere("contract_type_name", "", "", "contract_type_code",contractType);
		if(cp != null && !"解除协议".equals(cp.get("contract_type_name"))){
			mDataMap.put("zw_f_dissolution_time", "");
			mDataMap.put("zw_f_dissolution_instructions","");
		}else{
			String dissolutionTime = mDataMap.get("zw_f_dissolution_time");
			if (!DateUtil.getTimefag(dissolutionTime, startTime)) {
				mResult.setResultCode(-3);
				mResult.setResultMessage("解除协议日期要大于开始日期");
				return mResult;
			}
		}
		String contractCode = mDataMap.get("zw_f_contract_code");
		MDataMap whereMap = new MDataMap();
		whereMap.put("contract_code", contractCode);
		whereMap.put("uid", mDataMap.get("zw_f_uid"));
		int count = DbUp.upTable("fh_contract").dataCount("contract_code=:contract_code and uid<>:uid", whereMap);
		if(count > 0){
			mResult.setResultCode(-4);
			mResult.setResultMessage("合同编号不能重复");
			return mResult;
		}
		try {
			mResult = super.funcDo(sOperateUid, mDataMap);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
