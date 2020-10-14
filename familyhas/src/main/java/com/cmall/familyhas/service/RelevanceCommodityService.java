package com.cmall.familyhas.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class RelevanceCommodityService extends BaseClass {

	/**
	 * 专题模板关联商品批量删除功能
	 * @param uid
	 * @return
	 */
	public int batchDelRelevanceCommodity(List<String> uids) {
		int delNum = 0;
		if (null != uids) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("dal_status", "1000");
			delNum = DbUp.upTable("fh_data_commodity").dataExec("update fh_data_commodity set dal_status=:dal_status where uid in ('"+StringUtils.join(uids,"','")+"')", mDataMap);
		}
		if(delNum>0){
			StringBuffer sb = new StringBuffer();
			for (String string : uids) {
				sb.append("{\"zw_f_uid\":" + string + "}");
				sb.append(",");
			}
			String jsonArray = sb.toString().substring(0,sb.toString().length()-1); 
			String content = "在表《fh_data_commodity》 批量删除" + uids.size() + "条记录:" + "[" + jsonArray + "]";
			TempletePageLog.upLog(content);
		}
		return delNum;
	}
}
