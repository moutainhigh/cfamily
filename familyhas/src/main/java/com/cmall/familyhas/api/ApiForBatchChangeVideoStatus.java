package com.cmall.familyhas.api;


import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiForBatchChangeVideoStatusInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;


public class ApiForBatchChangeVideoStatus extends RootApi<RootResult,  ApiForBatchChangeVideoStatusInput>{


	@Override
	public RootResult Process(ApiForBatchChangeVideoStatusInput inputParam, MDataMap mRequestMap) {
		RootResult result  = new RootResult();
		
		String nowTime = DateUtil.getNowTime();
		String nextStatus = inputParam.getNextStatus();
		//String fromStatus = inputParam.getFromStatus();
		String remark = inputParam.getRemark();
		String videoCodes = inputParam.getVideoCodes();
		String userCode = UserFactory.INSTANCE.create().getUserCode();
		String videoCodeStr =" ('"+ videoCodes.replace(",", "','")+"') ";
		
		if(StringUtils.isBlank(nextStatus)) {
			//逻辑删除 is_delete=1
			DbUp.upTable("lv_video_info").dataExec("update lv_video_info set is_delete='1' where video_code in "+videoCodeStr, null);
			
		}else if("01".equals(nextStatus)) {
			//编辑
			DbUp.upTable("lv_video_info").dataExec("update lv_video_info set title='"+remark+"' where video_code in "+videoCodeStr, null);

		}else if("4497471600600002".equals(nextStatus)) {
			//审核失败
			DbUp.upTable("lv_video_info").dataExec("update lv_video_info set status='"+nextStatus+"' where video_code in "+videoCodeStr, null);
			String[] split = videoCodes.split(",");
			for (String vCode : split) {
				DbUp.upTable("lc_video_approval_log").insert("uid",WebHelper.upUuid(),"video_code",vCode,"approval_user",userCode,"approval_time",nowTime,"approval_content",remark,"tostatus",nextStatus);
			}
		}else if("4497471600600003".equals(nextStatus)) {
			//已发布
			DbUp.upTable("lv_video_info").dataExec("update lv_video_info set status='"+nextStatus+"',publish_time='"+nowTime+"'  where video_code in "+videoCodeStr, null);
			String[] split = videoCodes.split(",");
			for (String vCode : split) {
				DbUp.upTable("lc_video_approval_log").insert("uid",WebHelper.upUuid(),"video_code",vCode,"approval_user",userCode,"approval_time",nowTime,"approval_content",remark,"tostatus",nextStatus);
			}

		}else if("4497471600600004".equals(nextStatus)) {
			//暂停发布
			DbUp.upTable("lv_video_info").dataExec("update lv_video_info set status='"+nextStatus+"' where video_code in "+videoCodeStr, null);
			String[] split = videoCodes.split(",");
			for (String vCode : split) {
				DbUp.upTable("lc_video_approval_log").insert("uid",WebHelper.upUuid(),"video_code",vCode,"approval_user",userCode,"approval_time",nowTime,"approval_content",remark,"tostatus",nextStatus);
			}

		}

		return result;
	}



}
