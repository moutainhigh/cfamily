package com.cmall.familyhas.webfunc;

import com.cmall.systemcenter.dcb.PushReturnGoodsStatusService;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * 客服确认退货
 * @author jlin
 *
 */
public class FuncSureReturnGoods extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String return_code = mDataMap.get("zw_f_return_code");
		String remark = mDataMap.get("zw_f_remark");
		String flag_return_goods = mDataMap.get("zw_f_flag_return_goods");
		
		MDataMap info = DbUp.upTable("oc_return_goods").one("return_code", return_code);

		String flowBussinessUid = info.get("uid");
		String fromStatus = info.get("status");
		String toStatus = "4497153900050004";
		if("4497477800090001".equals(flag_return_goods)) {//需要回寄的时候，到待完善物流状态
			toStatus = "4497153900050005";
		}
		String flowType = "449715390005";

		String userCode = UserFactory.INSTANCE.create().getUserCode();
		MDataMap md = new MDataMap();
		md.put("remark", remark);
		
		if(!info.get("flag_return_goods").equals(flag_return_goods)){
			DbUp.upTable("oc_return_goods").dataUpdate(new MDataMap("return_code", return_code,"flag_return_goods",flag_return_goods), "flag_return_goods", "return_code");
		}
		
		RootResult re=new FlowBussinessService().ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, md);
		
		
		mResult.setResultCode(re.getResultCode());
		mResult.setResultMessage(re.getResultMessage());
		
		if(mResult.getResultCode() == 1){
			PushReturnGoodsStatusService service = new PushReturnGoodsStatusService();
			service.pushReturnGoodsStatus(return_code, toStatus,"0","客服确认");
		}else {
			if("4497153900050002".equals(fromStatus)) {//商户否决审核
				mResult.setResultMessage("该售后单已维护过物流单号，物流如有变更，请直接修改");
			}
		}
		return mResult;
	}
}
