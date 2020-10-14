package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.groupcenter.service.OrderService;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class UnionOrder extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult result = new MWebResult();
		
		String orderCode = mDataMap.get("zw_f_order_code");
		
		result = execByOrderCode(orderCode);
		
		if(result.upFlagTrue()) {
			//联单成功，检查同步支付信息是否超过1000次，如果超过1000次，需要将次数置零，
			MDataMap map = DbUp.upTable("za_exectimer").one("exec_type","449746990001","exec_info",orderCode);
			if(null!=map) {
				String execNumber = map.get("exec_number");
				if(Integer.parseInt(execNumber)>=1000) {
					String zid = map.get("zid");
					MDataMap mdata = new MDataMap();
					mdata.put("zid", zid);
					mdata.put("exec_number", "0");
					DbUp.upTable("za_exectimer").dataUpdate(mdata, "exec_number", "zid");
				}
			}
		}
		
		return result;
		
	}
	
	
	public MWebResult execByOrderCode(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		//校验一下是否是拼团单，如果是拼团单需要校验是否拼团成功。
		MDataMap collageItemMap = DbUp.upTable("sc_event_collage_item").one("collage_ord_code",orderCode);
		if(collageItemMap !=null && !collageItemMap.isEmpty()) {//不为空的时候证明是拼团单
			String is_confirm = collageItemMap.get("is_confirm");
			if("449748320001".equals(is_confirm)) {//待确认订单，也就是未支付的，不允许脸蛋
				mWebResult.setResultMessage("拼团订单不允许联单！！！");
				return mWebResult;
			}
			String collage_code = collageItemMap.get("collage_code");
			MDataMap collageInfo = DbUp.upTable("sc_event_collage").one("collage_code",collage_code);
			if(collageInfo == null || collageInfo.isEmpty()) {//如果为空，说明异常拼团，直接返回失败
				mWebResult.setResultMessage("拼团订异常！！！");
				return mWebResult;
			}
			String collage_status = collageInfo.get("collage_status");
			if("449748300001".equals(collage_status)||"449748300003".equals(collage_status)) {//拼团中跟拼团失败的单子不允许同步
				mWebResult.setResultMessage("拼团未成功订单，不允许联单！！！");
				return mWebResult;
			}
		}
		if(orderMap==null||"4497153900010006".equals(orderMap.get("order_status"))){
			DbUp.upTable("lc_change_channel").dataUpdate(new MDataMap("is_success","0", "is_send","1", "order_code",orderCode,
					"comment","order_status:4497153900010006"), "is_success,is_send,comment", "order_code");
			return mWebResult;
		}
		
		String buyer_code=orderMap.get("buyer_code");
		String out_code=orderMap.get("out_order_code");
		if(StringUtils.isNotBlank(out_code)){
			return mWebResult;
		}
		
		MemberLoginSupport memberLoginSupport = new MemberLoginSupport();
		String mobileid = memberLoginSupport.getMoblie(buyer_code);
		
		//查询订单相关信息
		OrderService service = new OrderService();
		mWebResult = service.rsyncOrder2(orderCode, mobileid);
		if (mWebResult.getResultCode()!=1) {
			
		} else {
			//检查一下订单是否已经被取消了
			if(DbUp.upTable("oc_orderinfo").count("order_code",orderCode,"order_status","4497153900010006")>0) {
				MDataMap mDataMap = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
				String out_order_code=mDataMap.get("out_order_code");
				String now=DateUtil.getSysDateTimeString();
				if(StringUtils.isNotBlank(out_order_code)){
					DbUp.upTable("oc_order_cancel_h").insert("order_code",orderCode,"buyer_code",buyer_code,"out_order_code",out_order_code,"call_flag","1","create_time",now,"update_time",now,"canceler","system");
				}
			}
		}
		
		
		return mWebResult;
	}
	
}
