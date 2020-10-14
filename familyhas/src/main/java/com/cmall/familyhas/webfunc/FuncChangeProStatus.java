package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.model.ScFlowMain;
import com.cmall.systemcenter.service.FlowService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncChangeProStatus extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap map = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);	
		Map<String,Object> returnMap = DbUp.upTable("pc_productinfo").dataSqlOne("select * from "
				+ "pc_productinfo where uid='" + map.get("uid") + "'", null);
		//直接修改已经上架的商品的状态，不用走审批流程
		MDataMap paramMap = new MDataMap();
		if("4497153900060001".equals(returnMap.get("product_status")))
		{
			mResult.setResultCode(0);
			mResult.setResultMessage("待上架商品不能修改状态");
			return mResult;
		}
		//已经上架的可以直接下架，不走审批
		if("4497153900060002".equals(returnMap.get("product_status")))
		{
			paramMap.put("product_status", "4497153900060003");
			paramMap.put("uid", map.get("uid").toString());
			int dataUpdate = DbUp.upTable("pc_productinfo").dataUpdate(paramMap,
					"product_status",
					"uid");
		}
		else
			//已经下架的上架走法务审核，商品状态不改
		{
			//paramMap.put("product_status", "4497153900060002");
			
			MDataMap pMap = new MDataMap();
			String createTime = DateUtil.getSysDateTimeString();
			String sql = "select product_code from pc_productinfo where uid=:uid";
			List<Map<String,Object>> list =DbUp.upTable("pc_productinfo").dataSqlList(sql, new MDataMap("uid",map.get("uid").toString()));
			
			/**
			 *  查询是否有法务待审批流程，如果存在提示正在审批中
			 */
			MDataMap flowIsExists = DbUp.upTable("sc_flow_main").one("outer_code",list.get(0).get("product_code").toString(),"flow_type","449717230016","flow_isend","0","current_status","4497172300160011");
			if(flowIsExists != null){
				mResult.setResultCode(-1);
				mResult.setResultMessage("商品修改正在审批中");
				return mResult;
			}
			else {
				// 加入审批的流程
				MUserInfo uc = UserFactory.INSTANCE.create();//当前用户所属店铺编号
				ScFlowMain flow = new ScFlowMain();
				flow.setCreator(uc.getUserCode());
				flow.setCurrentStatus("4497172300160011");
				String title = "修改商品"+list.get(0).get("product_code").toString()+"信息，待法务审批";
				flow.setFlowTitle(list.get(0).get("product_code").toString());
				// flow.setFlowType("449717230011");
				// 修改添加商品跳转节点 2016-06-24 zhy
				flow.setFlowType("449717230016");
				String preViewUrl = bConfig("productcenter.PreviewCheckProductUrl") + list.get(0).get("product_code").toString() + "_1";
				flow.setFlowUrl(preViewUrl);
				flow.setCreator(uc.getUserCode());
				flow.setOuterCode(list.get(0).get("product_code").toString());
				flow.setFlowRemark(title);
				FlowService flowService = new FlowService();
				flowService.CreateFlow(flow);
				/**
				 * ================= end ==============
				 */
			}
			
			
		}
			
		return mResult;
	}

}
