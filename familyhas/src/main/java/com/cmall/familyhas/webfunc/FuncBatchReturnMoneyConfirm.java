package com.cmall.familyhas.webfunc;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MMessage;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.MessageSupport;

/**
 * 批量确认退款
 * @author zhaojunling
 */
public class FuncBatchReturnMoneyConfirm extends RootFunc {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		InputStream in = null;
		List<String[]> dataList = null; 
		try {
			in = new URL(uploadFileUrl).openStream();
			Workbook wb = new HSSFWorkbook(in);
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum();
			int end = sheet.getLastRowNum();
			dataList = new ArrayList<String[]>((int)(end/0.75+1));
			
			start++; // 忽略第一行标题
			Row row;
			String[] cols = null;
			while(start <= end){
				cols = new String[2];
				row = sheet.getRow(start);
				start++;
				
				// 第一个单元格是退款单号
				cols[0] = row.getCell(0,Row.RETURN_NULL_AND_BLANK) == null ? "" : row.getCell(0,Row.RETURN_NULL_AND_BLANK).getStringCellValue();
				// 第二个单元格是备注
				cols[1] = row.getCell(1,Row.RETURN_NULL_AND_BLANK) == null ? "" : row.getCell(1,Row.RETURN_NULL_AND_BLANK).getStringCellValue();
				
				// 忽略退款单号为空的行
				if(StringUtils.isBlank(cols[0])){
					continue;
				}
				
				cols[1] = StringUtils.trimToEmpty(cols[1]);
				dataList.add(cols);
			}
			
		} catch (IOException e) {
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件异常："+e);
			return mResult;
		}
		
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		MDataMap returnMoney = null;
		MDataMap logMap = new MDataMap();
		for(String[] data : dataList){
			returnMoney = DbUp.upTable("oc_return_money").one("return_money_code", data[0], "status", "4497153900040003");
			if(returnMoney == null) continue;
			
			// 更新售后单状态
			updateAfterSaleStatus(userInfo,returnMoney);
			
			logMap.put("return_money_no", data[0]);
			logMap.put("info", "[批量更新]"+data[1]);
			logMap.put("create_time", FormatHelper.upDateTime());
			logMap.put("create_user", userInfo.getUserCode());
			logMap.put("refund_flag", "");
			logMap.put("status", "4497153900040001");
			DbUp.upTable("lc_return_money_status").dataInsert(logMap);
			
			returnMoney.put("status", "4497153900040001");
			DbUp.upTable("oc_return_money").dataUpdate(returnMoney, "status", "zid");
		}
		
		return mResult;
	}
	
	private void updateAfterSaleStatus(MUserInfo userInfo,MDataMap returnMoney){
		MDataMap afterSaleInfo = null;
		String returnGoodsCode = returnMoney.get("return_goods_code");
		String now = DateUtil.getSysDateTimeString();
		
		if(StringUtils.isNotBlank(returnGoodsCode)) {
			afterSaleInfo = DbUp.upTable("oc_order_after_sale").one("asale_code",returnGoodsCode);
		}
		
		// 更新退款状态
		if(afterSaleInfo != null && !"4497477800050002".equals(afterSaleInfo.get("asale_status"))) {
			DbUp.upTable("oc_order_after_sale").dataUpdate(new MDataMap("asale_code",returnGoodsCode,"asale_status","4497477800050002","update_time",now), "", "asale_code");
			MDataMap loasMap=new MDataMap();
			loasMap.put("asale_code", returnGoodsCode);
			loasMap.put("create_user", userInfo.getUserCode());
			loasMap.put("create_time", now);
			loasMap.put("asale_status", "4497477800050002");
			loasMap.put("remark", "[财务退款]已退款");
			loasMap.put("lac_code", WebHelper.upCode("LAC"));
			DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
			
			
			MDataMap lsasMap=new MDataMap();
			lsasMap.put("asale_code", returnGoodsCode);
			lsasMap.put("lac_code", loasMap.get("lac_code"));
			lsasMap.put("create_source", "4497477800070001");
			lsasMap.put("create_time", now);
			
			
			MDataMap returnGoods=DbUp.upTable("oc_return_goods").one("return_code",returnGoodsCode);
			MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100012");
			lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"),returnGoods.get("expected_return_money"),returnGoods.get("expected_return_money"),returnGoods.get("expected_return_group_money"),"0.00","0"));
			lsasMap.put("serial_title", templateMap.get("template_title"));
			lsasMap.put("template_code", templateMap.get("template_code"));
			DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
			
			MMessage messages = new MMessage();
			messages.setMessageContent(bConfig("ordercenter.ChangeReturnMoney_msm"));
			messages.setMessageReceive(returnMoney.get("mobile"));
			messages.setSendSource("4497467200020006");
			MessageSupport.INSTANCE.sendMessage(messages);
		}
	}
	
}
