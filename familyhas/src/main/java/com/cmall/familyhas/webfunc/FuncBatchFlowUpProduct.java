package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.cmall.systemcenter.service.FlowService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd; 
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 网站编辑批量审批上架
 */
public class FuncBatchFlowUpProduct extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		List<MDataMap> excelDataList = null;
		try {
			excelDataList = new ExcelSupport().upExcelFromUrl(uploadFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件失败："+e);
			return mResult;
		}
		
		if(excelDataList == null || excelDataList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入文件无内容");
			return mResult;
		}
		
		// 三方商户编码
		List<String> sanfangCodeList = Arrays.asList(bConfig("familyhas.sanfang_small_seller_code").split(","));
		
		ArrayList<String> errors = new ArrayList<String>();
		String productCode;
		MDataMap infoMap;
		Set<String> codeList = new HashSet<String>();
		for(MDataMap data : excelDataList) {
			productCode = StringUtils.trimToEmpty(data.get("商品编号"));
			if(StringUtils.isBlank(productCode)) {
				continue;
			}
			
			infoMap = DbUp.upTable("pc_productinfo").oneWhere("brand_code,small_seller_code", "", "", "product_code", productCode);
			
			if(infoMap == null) {
				errors.add(productCode+"[商品不存在]");
				continue;
			}
			
			// 检测商户限制
			if(!sanfangCodeList.contains(infoMap.get("small_seller_code"))) {
				errors.add(productCode+"[此商户不支持批量]");
				continue;
			}
			
			// 检查品牌
			if(StringUtils.isBlank(infoMap.get("brand_code"))) {
				errors.add(productCode+"[品牌未设置]");
				continue;
			}
			
			// 检查分类
			if(DbUp.upTable("uc_sellercategory_product_relation").count("product_code", productCode,"seller_code","SI2003") == 0) {
				errors.add(productCode+"[分类未设置]");
				continue;
			}
			
			codeList.add(productCode);
		}
		
		if(!errors.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("问题商品："+StringUtils.join(errors,","));
			return mResult;
		}
		
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		MDataMap flowMain;
		RootResult res;
		for(String code : codeList) {
			productCode = code;
			
			flowMain = DbUp.upTable("sc_flow_main").oneWhere("", "", "current_status in('4497172300160003','4497172300160013') and outer_code = :outer_code", "outer_code", productCode);
			if(flowMain == null) continue;
			
			if(flowMain.get("current_status").equals("4497172300160003")) {
				res = shenhe(flowMain, userInfo);
				if(res.getResultCode() != 1) {
					errors.add(productCode+"["+res.getResultMessage()+"]");
					continue;
				}
				
				flowMain.put("current_status", "4497172300160013");
			}
			
			if(flowMain.get("current_status").equals("4497172300160013")) {
				res = up(flowMain, userInfo);
				if(res.getResultCode() != 1) {
					errors.add(productCode+"["+res.getResultMessage()+"]");
				}
			}
		}
		
		if(!errors.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("审核失败商品："+StringUtils.join(errors,","));
			return mResult;
		}
		
		return mResult;
		
	}
	
	private RootResult shenhe(MDataMap flowMan, MUserInfo userInfo) {
		String flowCode = flowMan.get("flow_code");
		String flowFromStatus = "4497172300160003";
		String flowToStatus = "4497172300160013";
		String flowUserCode = userInfo.getUserCode();
		String flowRoleCode = userInfo.getUserRole();
		String flowRemark = "批量审批";
		MDataMap flowMSubMap = new MDataMap("product_code", flowMan.get("outer_code"));
		FlowService flow = new FlowService();
		return flow.ChangeFlow(flowCode, flowFromStatus, flowToStatus, flowUserCode, flowRoleCode,
				flowRemark, flowMSubMap);
	}
	
	private RootResult up(MDataMap flowMan, MUserInfo userInfo) {
		String flowCode = flowMan.get("flow_code");
		String flowFromStatus = "4497172300160013";
		String flowToStatus = "4497172300160005";
		String flowUserCode = userInfo.getUserCode();
		String flowRoleCode = userInfo.getUserRole();
		String flowRemark = "批量审批上架";
		MDataMap flowMSubMap = new MDataMap("product_code", flowMan.get("outer_code"));
		FlowService flow = new FlowService();
		return flow.ChangeFlow(flowCode, flowFromStatus, flowToStatus, flowUserCode, flowRoleCode,
				flowRemark, flowMSubMap);
	}
}
