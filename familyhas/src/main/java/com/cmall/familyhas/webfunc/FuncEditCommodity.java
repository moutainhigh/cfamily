package com.cmall.familyhas.webfunc;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.xmassystem.support.PlusSupportWebTemplete;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditCommodity extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		
		/*
		 * 判断关联的模板是否是页面定位模板（不能关联页面定位模板）
		 */
		String template_type = mDataMap.get("zw_f_template_type");//本模板的模板类型
		if("449747500018".equals(template_type)) {
			String sub_template_number = mDataMap.get("zw_f_sub_template_number");
			if(StringUtils.isNotBlank(sub_template_number)) {
				String[] num = sub_template_number.split(",");
				StringBuffer template_numbers = new StringBuffer();
				for (String n : num) {
					template_numbers.append("'"+n+"',");
				}
				String template_numbers_str = template_numbers.substring(0, template_numbers.length()-1);
				
				List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_data_template").dataSqlList(
						" SELECT template_number,template_type FROM familyhas.fh_data_template WHERE template_number IN ("+template_numbers_str+"); ", new MDataMap());
				
				String errorLog = "";
				boolean isRelTemplateTypeFor18 = false;//关联的模板中是否有页面定位模板
				for (Map<String, Object> map : dataSqlList) {
					Object template_number = map.get("template_number");
					Object rel_template_type = map.get("template_type");
					if("449747500018".toString().equals(rel_template_type)) {
						errorLog += "["+template_number.toString()+"]";
						isRelTemplateTypeFor18 = true;
					}
				}
				if(isRelTemplateTypeFor18) {
					mResult.setResultCode(0);
					mResult.setResultMessage("关联的模板中:"+errorLog+"为页面定位模板");
					return mResult;
				}
				
			} else {
				mResult.setResultCode(0);
				mResult.setResultMessage("没有关联专题模板");
				return mResult;
			}
		}	
		
		// 积分兑换的优惠券类型编号校验
		if("449747500020".equals(template_type)) {
			String couponTypeCode = mDataMap.get("zw_f_coupon");
			if(StringUtils.isBlank(couponTypeCode)) {
				mResult.setResultCode(0);
				mResult.setResultMessage("请填写优惠券类型编号");
				return mResult;
			}
			
			MDataMap couponTypeInfo = DbUp.upTable("oc_coupon_type").one("coupon_type_code",couponTypeCode);
			if(couponTypeInfo == null) {
				mResult.setResultCode(0);
				mResult.setResultMessage("未查询到对应的优惠券类型信息");
				return mResult;
			}
			
			MDataMap activityInfo = DbUp.upTable("oc_activity").one("activity_code", couponTypeInfo.get("activity_code"));
			if(!"4497471600060002".equals(activityInfo.get("provide_type"))) {
				mResult.setResultCode(0);
				mResult.setResultMessage("不支持的优惠券类型编号，仅支持系统发放类型活动下的优惠券");
				return mResult;
			}
			
			// 检查活动的有效时间
			if(activityInfo.get("end_time").compareTo(FormatHelper.upDateTime()) <= 0) {
				mResult.setResultCode(0);
				mResult.setResultMessage("优惠券对应的活动已经结束");
				return mResult;
			}
		}
		
		// 550:悬浮模板内容在添加时校验时间是否重叠,如果存在重叠则提示
		if("449747500024".equals(template_type)) {
			String template_number = mDataMap.get("zw_f_template_number");
			String start_time = mDataMap.get("zw_f_start_time");
			String end_time = mDataMap.get("zw_f_end_time");
			String uid = mDataMap.get("zw_f_uid");
			// 如果当前悬浮模板存在开始时间小于新增内容结束时间,并且结束时间大于新增内容开始时间的数据,则证明时间重叠
			List<Map<String, Object>> commodityList = DbUp.upTable("fh_data_commodity").dataSqlList
					("SELECT * FROM fh_data_commodity c WHERE c.template_number = '"+template_number+"' AND c.uid <> '"+uid+"' AND( c.start_time < '"+end_time+"' AND c.end_time > '"+start_time+"')", new MDataMap());
			if(commodityList == null || commodityList.size() <= 0) {
				// 没有满足条件的,证明时间没有重叠
			}else {
				// 否则证明时间存在重叠,提示修改开始和结束时间
				mResult.setResultCode(-1);
				mResult.setResultMessage("新增内容开始结束时间与原有内容时间存在重叠,请修改开始结束时间");
				return mResult;
			}
		}
		if("449747500023".equals(template_type)) {// 556:拼团模板
			String template_number = mDataMap.get("zw_f_template_number");
			String event_code = mDataMap.get("zw_f_event_code");
			 MDataMap one = DbUp.upTable("sc_event_info").one("event_code",event_code,"event_type_code","4497472600010024");
			if(one==null) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("活动编号错误,请检查重新填写!");
				return mResult;
			}
			//int count2 = DbUp.upTable("fh_data_commodity").count("event_code",event_code,"template_number",template_number,"dal_status","1001");
			int count2 = DbUp.upTable("fh_data_commodity").dataCount("event_code=:event_code and template_number=:template_number and dal_status=:dal_status and uid!=:uid", new MDataMap("event_code",event_code,"template_number",template_number,"dal_status","1001","uid",mDataMap.get("zw_f_uid")));
			if(count2!=0) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("该模板下已经关联此活动!");
				return mResult;
			}
				MDataMap paramMap = new MDataMap();
				paramMap.put("uid", mDataMap.get("zw_f_uid"));
				paramMap.put("template_number", template_number);
				paramMap.put("commodity_location",mDataMap.get("zw_f_commodity_location").toString());
				paramMap.put("event_code",event_code);
				paramMap.put("start_time", one.get("begin_time").toString());
				paramMap.put("end_time", one.get("end_time").toString());
				DbUp.upTable("fh_data_commodity").dataUpdate(paramMap, "commodity_location,event_code", "uid");
				 // 添加日志
				MWebOperate mOperate2 = WebUp.upOperate(sOperateUid);
				MWebPage mPage2 = WebUp.upPage(mOperate2.getPageCode());
				String content = "在表《"+mPage2.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(paramMap);
				TempletePageLog.upLog(content);
			
			    return mResult;
		
		}
		
		mResult = super.funcDo(sOperateUid, mDataMap);
		String content = "在表《"+mPage.getPageTable()+"》 修改了一条记录:"+JSON.toJSONString(mDataMap);
		TempletePageLog.upLog(content);
		
		if(mResult.upFlagTrue()) {
			new PlusSupportWebTemplete().onDataTemplateChanged(mDataMap.get("zw_f_template_number"));
		}
		
		return mResult;
	}
	
	
	

}
