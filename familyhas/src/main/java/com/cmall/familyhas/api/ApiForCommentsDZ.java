package com.cmall.familyhas.api;


import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.CommentDZInput;
import com.cmall.familyhas.api.result.CommentDZResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;


public class ApiForCommentsDZ extends RootApiForVersion<CommentDZResult, CommentDZInput>{

	@Override
	public CommentDZResult Process(CommentDZInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		CommentDZResult commentDZResult = new CommentDZResult();
		String evalutionUid = inputParam.getEvalutionUid();
		String flag = inputParam.getFlag();
		String approveFlag = "N";
		String opposeFlag = "N";
		String userCode = inputParam.getUserCode();
		if(StringUtils.isBlank(userCode)) {
			userCode=getOauthInfo().getUserCode();
		}
		String nowTime = DateUtil.getNowTime();
		String date = DateUtil.getSysDateString();
		 String startTime = date+" 00:00:00";
		 String endTime = date+" 23:59:59";
		
		 Map<String, Object> dataSqlOne = DbUp.upTable("nc_order_evaluation_attitude").dataSqlOne("select * from nc_order_evaluation_attitude where  operate_time>'"+startTime+"' and operate_time<'"+endTime+"' and  evalution_uid=:evalution_uid and user_code=:user_code", new  MDataMap("user_code",userCode,"evalution_uid",evalutionUid));
		
		Map<String, Object> dataSqlTwo = DbUp.upTable("nc_order_evaluation").dataSqlOne("select * from nc_order_evaluation where uid=:uid", new MDataMap("uid",evalutionUid));
		int appNum = Integer.parseInt(dataSqlTwo.get("approve_num").toString()); 
		int oppNum = Integer.parseInt(dataSqlTwo.get("oppose_num").toString()); 
		
		if(dataSqlOne!=null) {//今日已经点赞，做更新操作
			if("Y+".equalsIgnoreCase(flag)) {
				appNum++;
				approveFlag="Y";
				if("Y".equals(dataSqlOne.get("oppose_flag"))) {//已经做了点反赞的操作,取消之前操作
					oppNum--;
					opposeFlag = "N";
					DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"approve_flag","Y","oppose_flag","N"), "approve_flag,oppose_flag", "user_code,uid");
					DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("approve_num",appNum+"","uid",evalutionUid,"oppose_num",oppNum+""), "approve_num,oppose_num", "uid");
				}else {//什么操作都取消了
					DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"approve_flag","Y","oppose_flag","N"), "approve_flag,oppose_flag", "user_code,uid");
					DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("approve_num",appNum+"","uid",evalutionUid,"oppose_num",oppNum+""), "approve_num,oppose_num", "uid");
				}

			}
			else if("Y-".equalsIgnoreCase(flag)) {//取消点赞
				appNum--;
				approveFlag="N";
				opposeFlag = "N";
				DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"approve_flag","N"), "approve_flag", "user_code,uid");
				DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("approve_num",appNum+"","uid",evalutionUid), "approve_num", "uid");
				
			}
			else if("N+".equalsIgnoreCase(flag)) {//点反赞
				oppNum++;
				opposeFlag = "Y";
				if("Y".equals(dataSqlOne.get("approve_flag"))) {//之前点过正赞
					appNum--;
					approveFlag="N";
					DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"oppose_flag","Y","approve_flag","N"), "oppose_flag,approve_flag", "user_code,uid");
					DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("oppose_num",oppNum+"","uid",evalutionUid,"approve_num",appNum+""), "oppose_num,approve_num", "uid");
				}
				else {
					DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"oppose_flag","Y","approve_flag","N"), "oppose_flag,approve_flag", "user_code,uid");
					DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("oppose_num",oppNum+"","uid",evalutionUid,"approve_num",appNum+""), "oppose_num,approve_num", "uid");
				}
		     }
			else if("N-".equalsIgnoreCase(flag)) {
				oppNum--;
				DbUp.upTable("nc_order_evaluation_attitude").dataUpdate(new MDataMap("user_code",userCode,"uid",dataSqlOne.get("uid").toString(),"oppose_flag","N"), "oppose_flag", "user_code,uid");
				DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("oppose_num",oppNum+"","uid",evalutionUid), "oppose_num", "uid");
			}
			
		} 
		else {//做入库操作
			if("Y+".equalsIgnoreCase(flag)) {
				appNum++;
				approveFlag = "Y";
				DbUp.upTable("nc_order_evaluation_attitude").dataInsert(new MDataMap("uid",WebHelper.upUuid(),"user_code",userCode,"evalution_uid",evalutionUid,"approve_flag","Y","operate_time",nowTime));	
				DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("approve_num",appNum+"","uid",evalutionUid), "approve_num", "uid");
	
			}
			else if("N+".equalsIgnoreCase(flag)) {
				oppNum++;
				opposeFlag = "Y";
				DbUp.upTable("nc_order_evaluation_attitude").dataInsert(new MDataMap("uid",WebHelper.upUuid(),"user_code",userCode,"evalution_uid",evalutionUid,"oppose_flag","Y","operate_time",nowTime));
				DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("oppose_num",oppNum+"","uid",evalutionUid), "oppose_num", "uid");

		     }
			
		}
		commentDZResult.setApproveNum(appNum);
		commentDZResult.setOpposeNum(oppNum);
		commentDZResult.setApproveFlag(approveFlag);
		commentDZResult.setOpposeFlag(opposeFlag);
		
		return commentDZResult;
	}

}
