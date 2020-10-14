package com.cmall.familyhas.api;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForPurchaseAddressInput;
import com.cmall.familyhas.api.input.ApiForPurchaseShenPiInput;
import com.cmall.familyhas.api.result.ApiForPurchaseAddressResult;
import com.cmall.familyhas.model.AddressDetail;
import com.cmall.familyhas.util.AlbumPicHandler;
import com.cmall.familyhas.util.CreatePurchaseOrders;
import com.cmall.familyhas.webfunc.MerchantApproveFunc;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;


public class ApiForPurchaseShenPi extends RootApi<RootResult,  ApiForPurchaseShenPiInput>{


	@Override
	public RootResult Process(ApiForPurchaseShenPiInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult rootResult = new RootResult();
		String next_status = inputParam.getNext_status();
		String purchase_order_id = inputParam.getPurchase_order_id();
		String remark = inputParam.getRemark()==null?"":inputParam.getRemark();
		String[] ps = purchase_order_id.split(",");
		ArrayList<String> newPids = new ArrayList<String>();
		String realName = UserFactory.INSTANCE.create().getRealName();
		for (String pod : ps) {
			//待审核状态的进行状态变更
			if(StringUtils.equals("449748490002", next_status)||StringUtils.equals("449748490003", next_status)) {
				int count = DbUp.upTable("oc_purchase_order").count("status","449748490001","purchase_order_id",pod);
				if(count>0) {
					newPids.add(pod);
					DbUp.upTable("oc_purchase_order").dataUpdate(new MDataMap("status",next_status,"approve_remark",remark,"purchase_order_id",pod), "approve_remark,status", "purchase_order_id");
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("uid", WebHelper.upUuid());
					mDataMap.put("purchase_order_id", pod);
					mDataMap.put("purchase_order_status", next_status);
					mDataMap.put("purchase_order_text",remark );
					mDataMap.put("creater", realName);
					mDataMap.put("create_time", FormatHelper.upDateTime());
					DbUp.upTable("oc_purchase_order_shenpi").dataInsert(mDataMap);
					}
			}
			else {
				newPids.add(pod);
				DbUp.upTable("oc_purchase_order").dataUpdate(new MDataMap("status",next_status,"approve_remark",remark,"purchase_order_id",pod), "approve_remark,status", "purchase_order_id");
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", WebHelper.upUuid());
				mDataMap.put("purchase_order_id", pod);
				mDataMap.put("purchase_order_status", next_status);
				mDataMap.put("purchase_order_text",remark );
				mDataMap.put("creater", realName);
				mDataMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("oc_purchase_order_shenpi").dataInsert(mDataMap);
			}

		}

		if("449748490003".equals(next_status)) {
			//通过，创建订单
		 ExecutorService fixedThreadPool = null;
			try {
				 fixedThreadPool = Executors.newFixedThreadPool(1);
				 fixedThreadPool.execute(new CreatePurchaseOrders(newPids,remark,realName));
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (fixedThreadPool != null) {
					fixedThreadPool.shutdown();
				}
			}
		}
		//采购完成，修改订单表中的订单状态
		if("449748490004".equals(next_status)) {
			//通过，创建订单
	        for (String pId : newPids) {
	        	Map<String, Object> dataSqlOne = DbUp.upTable("mc_login_info").dataSqlOne("select a.member_code mCode from membercenter.mc_login_info a,ordercenter.oc_purchase_order b where a.login_name=b.phone and b.purchase_order_id=:purchase_order_id", new MDataMap("purchase_order_id",pId));
				String memberCode = dataSqlOne.get("mCode").toString();
	        	List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_purchase_order_detail").dataSqlList("select order_id from oc_purchase_order_detail where purchase_order_id=:purchase_order_id and if_delete='1' and if_selected='1' ", new MDataMap("purchase_order_id",pId));
				List<String> purIds = new ArrayList<String>();
				for (Map<String, Object> map : dataSqlList) {
					purIds.add(map.get("order_id").toString().replace(map.get("order_id").toString(), "'" + map.get("order_id").toString() + "'"));
					   DbUp.upTable("lc_orderstatus").dataInsert(new MDataMap("code",map.get("order_id").toString(),"info","交易成功","create_time",FormatHelper.upDateTime(),"create_user",memberCode,"now_status","4497153900010005"));
				}	
               DbUp.upTable("oc_orderinfo").dataExec("update oc_orderinfo set order_status='4497153900010005' where order_code in ("+StringUtils.join(purIds, ",")+")", null);

			}
		}
		
		
		return rootResult;
	}



}
