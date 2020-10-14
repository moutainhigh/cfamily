package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiConfirmReceiveForFamilyInput;
import com.cmall.familyhas.api.result.ApiConfirmReceiveForFamilyResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.helper.OrderHelper;
import com.cmall.ordercenter.service.OrderService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiConfirmReceiveForFamily extends RootApiForToken<ApiConfirmReceiveForFamilyResult,ApiConfirmReceiveForFamilyInput> {

	public ApiConfirmReceiveForFamilyResult Process(ApiConfirmReceiveForFamilyInput input, MDataMap mRequestMap) {
		ApiConfirmReceiveForFamilyResult result = new ApiConfirmReceiveForFamilyResult();
			
		OrderService os = new OrderService();
		
		try {
			
			RootResult rr = os.changForRecieveByUser(OrderHelper.upOrderCodeByOutCode(input.getOrder_code()),getUserCode());
			result.setResultCode(rr.getResultCode());
			result.setResultMessage(rr.getResultMessage());
			//判断，确认收货成功，需要判断是否是分销订单，如果是，需要写入计算预估收益定时
			if(rr.getResultCode() == 1 &&DbUp.upTable("fh_agent_order_detail").count("order_code",OrderHelper.upOrderCodeByOutCode(input.getOrder_code()))>0) {
				JobExecHelper.createExecInfo("449746990028", OrderHelper.upOrderCodeByOutCode(input.getOrder_code()), DateUtil.addMinute(28800));//时间是20天后执行
			}	
			if(rr.getResultCode() == 1 &&DbUp.upTable("fh_share_order_detail").count("order_code",OrderHelper.upOrderCodeByOutCode(input.getOrder_code()))>0&&DbUp.upTable("za_exectimer").count("exec_info",OrderHelper.upOrderCodeByOutCode(input.getOrder_code()),"exec_type","449746990033") <= 0) {
				JobExecHelper.createExecInfo("449746990033", OrderHelper.upOrderCodeByOutCode(input.getOrder_code()), DateUtil.addMinute(21600));//时间是20天后执行
			}
			
			// 566确认收货接口增加评价+晒单送积分
			String userCode = getUserCode();
			String isRemind = "0";
			String total_integral = "0";
			// 写死"赚"的图片
			String canEvaluateMainpic = "https://image-mall.huijiayou.cn/hjyshop/img/566/d3.png";
			String tagType = "1";
			// 是否显示评价或晒单送积分顶部悬浮提醒框
			if(!"".equals(userCode)) {
				// 查询用户有没有待评价和好评待晒图的单
				String sql = "SELECT DISTINCT oi.order_code,od.product_code,od.sku_code,od.sku_num,pi.product_name,sku.sku_keyvalue,pi.mainpic_url,sku.sku_picurl,pi.small_seller_code FROM ordercenter.oc_orderinfo oi"
						   + " LEFT JOIN ordercenter.oc_orderdetail od ON oi.order_code = od.order_code"
						   + " LEFT JOIN productcenter.pc_skuinfo sku ON sku.sku_code = od.sku_code"
						   + " LEFT JOIN productcenter.pc_productinfo pi ON sku.product_code = pi.product_code"
						   + " WHERE oi.order_code = '"+input.getOrder_code()+"' AND od.gift_flag = '1' AND pi.product_name != '' AND oi.delete_flag = '0' AND oi.buyer_code = :buyer_code AND order_source not in('449715190014','449715190037') AND oi.order_status = '4497153900010005'"
						   + " AND oi.order_type NOT IN(" + new OrderService().getNotInOrderType() + ")";
				// 待评价订单
				String sql1 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code AND noe.evaluation_status_user = '449746810001' ) = 0  AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) "
							+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
				// 待晒单则是为上传图片的评价
				String sql2 = sql + " AND (SELECT COUNT(*) FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code  AND noe.grade > 3 AND noe.evaluation_status_user = '449746810001' AND noe.auto_good_evaluation_flag = 0 AND oder_photos = '' AND ccvids = '') > 0 AND ((SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) != '449746810002' or (SELECT evaluation_status_user FROM newscenter.nc_order_evaluation noe WHERE noe.order_code = oi.order_code AND noe.order_skuid = od.sku_code) IS NULL) "
							+ " AND od.product_code != '"+bConfig("xmassystem.plus_product_code")+"'";
				MDataMap param = new MDataMap("buyer_code", userCode);
				Map<String, Object> totalMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql1 + ")t", param);
				Map<String, Object> totalMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT COUNT(1) total FROM (" + sql2 + ")t", param);
				int intValue1 = MapUtils.getIntValue(totalMap1, "total");
				int intValue2 = MapUtils.getIntValue(totalMap2, "total");
				
				List<MDataMap> evaluateList = DbUp.upTable("sc_evaluate_configure").queryAll("evaluate_type,integral_value,tip", "", "", new MDataMap());
				int integral1 = 0;
				int integral2 = 0;
				for(MDataMap map:evaluateList){
					integral1 += Integer.parseInt(map.get("integral_value"));
					if(map.get("evaluate_type").equals("评价图片")){
						integral2 += MapUtils.getIntValue(map, "integral_value");
					}
					if(map.get("evaluate_type").equals("买家秀")){
						integral2 += MapUtils.getIntValue(map, "integral_value");
					}
				}
				int totalIntegral = intValue1 * integral1 + intValue2 * integral2;
				total_integral = totalIntegral+"";
				if(totalIntegral > 0) {
					isRemind = "1";
					if(intValue1 > 0) {
						//Map<String, Object> picMap1 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url  FROM (" + sql1 + " ORDER BY oi.zid desc LIMIT 1) t", param);
						//canEvaluateMainpic = MapUtils.getString(picMap1, "mainpic_url");
						tagType = "1";
					}else if(intValue2 > 0){
						//Map<String, Object> picMap2 = DbUp.upTable("oc_orderinfo").dataSqlOne("SELECT t.mainpic_url FROM (" + sql2 + " ORDER BY oi.zid desc LIMIT 1) t", param);						
						//canEvaluateMainpic = MapUtils.getString(picMap2, "mainpic_url");
						tagType = "2";
					}
				}
			}
			
			result.setCanEvaluateMainpic(canEvaluateMainpic);
			result.setIsRemind(isRemind);
			result.setTagType(tagType);
			result.setTotal_integral(total_integral);
			
		} catch (Exception e) {
			result.setResultCode(916401109);
			result.setResultMessage(bInfo(916401109));
		}
		return result;
	}
}