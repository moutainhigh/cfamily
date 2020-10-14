package com.cmall.familyhas.webfunc;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 消息推送接口
 * @author dyc
 * */
public class CommentPushService extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap map = new MDataMap();
		StringBuffer pushRtnMsg = new StringBuffer();//推送消息返回信息
		/**百度推送暂时不用
		AndroidPushBroadcastMessage androidPush = new AndroidPushBroadcastMessage();
		IosPushBroadcastMessage iosPush = new IosPushBroadcastMessage();
		String apiKey = bConfig("familyhas.apiKey");
		String secretKey = bConfig("familyhas.secretKey");
		**/
		
		if (mResult.upFlagTrue()) {
			String host = bConfig("familyhas.pushHost");
			MDataMap info = DbUp.upTable("nc_comment_push").one("uid", mDataMap.get("uid"));
			String jumpType = ""; 
			String jumpPosition = "";
			if(info.get("jump_type").equals("4497465000080001")){//首页
				jumpType="2";
				jumpPosition="0";
			}else if(info.get("jump_type").equals("4497465000080002")){//商品最终页
				jumpType="1";
				jumpPosition=info.get("jump_position");
			}else if(info.get("jump_type").equals("4497465000080003")){//电视TV
				jumpType="2";
				jumpPosition="1";
			}else if(info.get("jump_type").equals("4497465000080004")){//商品目录
				jumpType="2";
				jumpPosition="2";
			}else if(info.get("jump_type").equals("4497465000080005")){//订单列表
				jumpType="2";
				jumpPosition="7";
			}else if(info.get("jump_type").equals("4497465000080006")){//个人账户
				jumpType="2";
				jumpPosition="4";
			}else if(info.get("jump_type").equals("4497465000080007")){//微公社
				jumpType="2";
				jumpPosition="5";
			}else if(info.get("jump_type").equals("4497465000080008")){//活动详情页
				jumpType="4";
				jumpPosition=info.get("jump_position");
			}else if(info.get("jump_type").equals("4497465000080009")){//启动APP
				jumpType="3";
				jumpPosition="0";
			}
			String content = info.get("comment");
			String pushTime = info.get("push_time").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
			String url = host+"/52yungo/app/AddPushInfoApi?ctype="+jumpType+"&info="+content+"&pushtime="+pushTime+"&cvalue="+jumpPosition;
			String response = "";
			try {
				response = WebClientSupport.create().doGet(url);
				//消息推送成功更新消息状态
				map.put("uid", mDataMap.get("uid"));
				map.put("push_status", "4497465000070002");
				DbUp.upTable("nc_comment_push").dataUpdate(map, "push_status", "uid");
				pushRtnMsg.append(bInfo(916401130));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mResult.inErrorMessage(916401134);
				pushRtnMsg.append(response+"|"+e.getMessage());
			}
			/**百度推送暂时不用
			//查询通知信息
			MDataMap info = DbUp.upTable("nc_comment_push").one("uid", mDataMap.get("uid"));
			//发送通知给安卓用户
			mResult = androidPush.sendNotifyMsg(info.get("title"), info.get("comment"), apiKey, secretKey);
			if(mResult.getResultCode()!=1){
				pushRtnMsg.append(mResult.getResultMessage());
			}
			mResult = iosPush.sendNotifyMsg(info.get("title"), info.get("comment"), apiKey, secretKey);
			if(mResult.getResultCode()!=1){
				pushRtnMsg.append(";").append(mResult.getResultMessage());
			}
			//更新推送状态
			if(pushRtnMsg.length()==0){
				map.put("uid", mDataMap.get("uid"));
				map.put("push_status", "4497465000070002");
				DbUp.upTable("nc_comment_push").dataUpdate(map, "push_status", "uid");
				pushRtnMsg.append(bInfo(916401130));
			}
			**/
			//记录日志信息
			MDataMap log = new MDataMap();
			log.inAllValues("send_info",info.toString(),"return_info",pushRtnMsg.toString(),"time",DateUtil.getNowTime());
			DbUp.upTable("lc_pushcomment_log").dataInsert(log);
			
		}
		return mResult;
    }

}
