package com.cmall.familyhas.api;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiBfAuditOperate extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String sku_codes = mDataMap.get("sku_codes");
		String[] sku_code_array = sku_codes.split(",");
		String remark = mDataMap.get("remark");
		
		boolean flag = true;
		String realMsg = "";
		int failureCount = 0;
		for(int i = 0; i < sku_code_array.length; i ++) {
			String sku_code = sku_code_array[i];
			MDataMap bfSku = DbUp.upTable("pc_bf_skuinfo").one("sku_code", sku_code);
			if(bfSku == null) {
				failureCount += 1;
				flag = false;
				realMsg = "此sku不能通过";
			}else {
				String skuStatus = bfSku.get("sku_status");
				if("1".equals(skuStatus)) {
					String createTime = DateUtil.getNowTime();
					String user = UserFactory.INSTANCE.create().getRealName();
					MDataMap skuInfo = DbUp.upTable("pc_skuinfo").one("sku_code", sku_code);
					
					MDataMap updateMap = new MDataMap();
					updateMap.put("zid", bfSku.get("zid"));
					updateMap.put("uid", bfSku.get("uid"));
					updateMap.put("sku_status", "2");
					DbUp.upTable("pc_bf_skuinfo").update(updateMap);
					
					DbUp.upTable("pc_bf_review_log").insert("sku_code", sku_code, "sku_name", skuInfo.get("sku_name"), "operate_status", "运营总监审核通过", "remark", remark, "operator", user, 
							"operate_time", createTime);
				}else {
					failureCount += 1;
					flag = false;
					realMsg = "此sku不能通过";
				}
			}
		}
		
		//单个sku审核，成功则显示成功，失败，显示具体原因；多个sku推送，都显示推送成功，显示失败个数
		String msg = "";
		MWebResult mResult = new MWebResult();
		if(flag && sku_code_array.length == 1) {
			msg = "审批成功";
			mResult.setResultCode(0);
		}else if(!flag && sku_code_array.length == 1) {
			msg = realMsg;
			mResult.setResultCode(1);
		}else {
			msg = "审批成功，失败" + failureCount + "条。";
			mResult.setResultCode(0);
		}
		mResult.setResultMessage(msg);
		return mResult;
	}
}
