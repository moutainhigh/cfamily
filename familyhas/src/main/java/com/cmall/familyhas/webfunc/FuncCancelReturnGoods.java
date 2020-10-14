package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.ApiForCancelReturn;
import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.dcb.PushReturnGoodsStatusService;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 取消退货
 * 
 * @author jlin
 *
 */
public class FuncCancelReturnGoods extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String return_code = mDataMap.get("zw_f_return_code");
		String remark = mDataMap.get("zw_f_remark");

		MDataMap info = DbUp.upTable("oc_return_goods").one("return_code", return_code);

		String flowBussinessUid = info.get("uid");
		String fromStatus = info.get("status");
		String toStatus = "4497153900050006";
		String flowType = "449715390005";

		String userCode = UserFactory.INSTANCE.create().getUserCode();
		MDataMap md = new MDataMap();
		md.put("remark", remark);
		
		// 京东商品
		if(Constants.SMALL_SELLER_CODE_JD.equals(info.get("small_seller_code"))) {
			MDataMap jdAfterSale = DbUp.upTable("oc_order_jd_after_sale").one("asale_code", return_code);
			// 检查一下京东售后单是否允许取消
			if(jdAfterSale != null && StringUtils.isNotBlank(jdAfterSale.get("afs_service_id"))
					&& !(","+jdAfterSale.get("allow_operations")+",").contains(",1,")
					) {
				mResult.setResultCode(10000);
				mResult.setResultMessage("该单不允许取消");
				return mResult;
			}
			
			// 同步取消京东的售后单，排除状态： 审核不通过(20) 、取消 60;
			if(jdAfterSale != null 
					&& StringUtils.isNotBlank(jdAfterSale.get("afs_service_id"))
					&& !ArrayUtils.contains(new String[]{"60","20"}, jdAfterSale.get("afs_service_step"))){
				RootResult cancelResult = new JdAfterSaleSupport().execAuditCancelQuery(return_code, remark);
				if(cancelResult.getResultCode() != 1) {
					mResult.setResultCode(10000);
					mResult.setResultMessage(cancelResult.getResultMessage());
					return mResult;
				}
				
				// 取消成功后刷新一次售后单
				new JdAfterSaleSupport().execServiceDetailInfoQuery(return_code);
			}
		}

		RootResult re=new FlowBussinessService().ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, md);
		mResult.setResultCode(re.getResultCode());
		mResult.setResultMessage(re.getResultMessage());
		if(re.getResultCode() == 1){
			PushReturnGoodsStatusService service = new PushReturnGoodsStatusService();
			service.pushReturnGoodsStatus(return_code, toStatus,"0","客服审批拒绝");
			//判断是否是分销商品售后单，如果时，需要写入定时任务。
			boolean flag  = new ApiForCancelReturn().checkIfAgent(return_code);
			if(flag) {
				JobExecHelper.createExecInfo("449746990026", return_code, DateUtil.addMinute(5));
			}
		}
		return mResult;
	}
}
