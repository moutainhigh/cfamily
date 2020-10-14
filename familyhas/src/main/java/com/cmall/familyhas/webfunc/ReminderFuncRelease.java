package com.cmall.familyhas.webfunc;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @Description: 提示语发布管理
 * @author ligj
 * 
 */
public class ReminderFuncRelease extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		MDataMap mdata = DbUp.upTable("nc_reminder_content").one("uid", uid);
		
		String seller_type = mdata.get("seller_type");//商户类型
		String view_page = mdata.get("view_page");	//展示界面
		String status = mdata.get("status");	//发布状态
		String seller_codes = mdata.get("seller_codes");	//指定商户
		String updateStatus = "";			//要更改成的状态
		//如果此条数据进行的是发布操作，则先判断是否符合重复规则
		//即：同一个商户类型，同一个界面不可发布多条
		//"4497469400030001"代表未发布，"4497469400030002"代表已发布
		if ("4497469400030001".equals(status)) {
			List<MDataMap> reminderList = DbUp.upTable("nc_reminder_content")
					.queryByWhere("seller_type", seller_type,"status","4497469400030002","flag_del","1");
			if (null!=reminderList && !reminderList.isEmpty()) {
//				for (MDataMap reminderMap : reminderList) {
//					for (String viewPage : view_page.split(",")) {
//						for (String mapViewPage : reminderMap.get("view_page").split(",")) {
//							if (mapViewPage.equals(viewPage)) {
//								mResult.setResultCode(916423005);
//								mResult.setResultMessage(bInfo(916423005));
//								return mResult;
//							}
//						}
//					}
//				}
				for (MDataMap reminderMap : reminderList) {
					for (String viewPage : view_page.split(",")) {
						if (reminderMap.get("view_page").contains(viewPage)) {
							if ("4497471600260001".equals(seller_type) 
									|| "4497471600260002".equals(seller_type)
									|| "4497471600260003".equals(seller_type)
									|| "4497471600260005".equals(seller_type)) {
								mResult.setResultCode(916423005);
								mResult.setResultMessage(bInfo(916423005,""));
								return mResult;
							}else if ("4497471600260004".equals(seller_type)) {
								//指定商户
								if (StringUtils.isNotBlank(seller_codes)) {
									for (String sellerCode : seller_codes.split(",")) {
										if (reminderMap.get("seller_codes").contains(sellerCode)) {
											mResult.setResultCode(916423005);
											mResult.setResultMessage(bInfo(916423005,"商户"+sellerCode+"已经存在其他指定商户类型中！"));
											return mResult;
										}
									}
								}
							}
						}
					}
				}
			}
			updateStatus = "4497469400030002";		//更改为已发布
		}else{
			updateStatus = "4497469400030001";		//更改为未发布
		}
		
		String update_user = UserFactory.INSTANCE.create().getLoginName();
		MDataMap dataMap = new MDataMap();
		dataMap.put("uid", uid);
		dataMap.put("status", updateStatus);
		dataMap.put("updater", update_user);
		dataMap.put("update_time", DateUtil.getNowTime());
		
		DbUp.upTable("nc_reminder_content").dataUpdate(dataMap,
				"status,updater,update_time", "uid");
		return mResult;
	}

}
