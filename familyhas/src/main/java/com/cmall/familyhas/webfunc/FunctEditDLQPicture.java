package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FunctEditDLQPicture extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult funcDo = new MWebResult();
		//根据uid 获取该条数据信息
		String uid = mDataMap.get("zw_f_uid");
		MDataMap one = DbUp.upTable("fh_dlq_picture").one("uid",uid);
		if(null != one) {
			String id_number = one.get("id_number");
			if("1000".equals(id_number)) {//上下部广告
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					
					Date gg_start_time_date = sdf.parse(mDataMap.get("zw_f_start_time"));
					Date gg_end_time_date = sdf.parse(mDataMap.get("zw_f_end_time"));
					if (gg_start_time_date.after(gg_end_time_date)) {
						funcDo.upFlagTrue();
						funcDo.setResultCode(0);
						funcDo.setResultMessage("开始时间大于结束时间，请重新选择时间");
						return funcDo;
					}
				} catch (Exception e) {
					funcDo.setResultCode(0);
					funcDo.setResultMessage("时间格式错误，请重新选择");
					return funcDo;
				}
				MDataMap parmMap = new MDataMap();
				String location = one.get("location");
				parmMap.put("start_time", mDataMap.get("zw_f_start_time"));
				parmMap.put("end_time", mDataMap.get("zw_f_end_time"));
				parmMap.put("page_number", one.get("page_number"));
				parmMap.put("location", location);
				parmMap.put("uid", uid);
				parmMap.put("tv_number", mDataMap.get("zw_f_tv_number"));
				List<MDataMap> queryAll = DbUp.upTable("fh_dlq_picture").queryAll("*", "", " ((start_time <:start_time and end_time >:start_time) or (start_time <:end_time and end_time >:end_time) or (end_time <=:end_time and start_time >=:start_time)) and id_number='1000' and delete_state='1001' and page_number=:page_number and location=:location and tv_number =:tv_number and uid != :uid", parmMap);
				if(queryAll.size() > 0) {
					funcDo.setResultCode(0);
					String message = "";
					if (location.equals("449747760001")) {
						message = "上部";
					} else if (location.equals("449747760002")) {
						message = "下部";
					}
					funcDo.setResultMessage("已存在该时段的"+message +"广告");
					return funcDo;
				}
				
			}
		}
		
		mDataMap.put("zw_f_update_time", DateUtil.getSysDateTimeString());
		funcDo = super.funcDo(sOperateUid, mDataMap);
		/*
		 * 添加日志
		 */
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String content = "在表《"+mPage.getPageTable()+"》 修改一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		
		return funcDo;
		
		
	}
}
