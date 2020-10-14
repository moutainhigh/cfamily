package com.cmall.familyhas.webfunc;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webface.IWebFuncExport;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webpage.PageExec;
import com.srnpr.zapweb.webpage.RootProcess;

/**
 * 导出商品价格审核查询列表数据（财务）
 */
public class FunExportForCWSheHe extends RootProcess implements IWebFuncExport{

	String sql = "select * from v_sc_flow_history_skuprice_cw_shenhe2 ";
	
	String[] headNames = {"商品编号","SKU编码","SKU名称","原成本价","变更后成本价","原销售价","变更后销售价","原毛利率","变更后毛利率","开始日期","结束日期","审核状态","操作人","操作时间"};
	String[] headCodes = {"product_code","sku_code","sku_name","cost_price_old","cost_price","sell_price_old","sell_price","profit_old","profit","start_time","end_tiem","current_status","creator","create_time"};
	
	@Override
	public void export(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		MWebPage mPage = WebUp.upPage(sOperateId);
		MDataMap mReqMap = convertRequest(request);
		MDataMap mOptionMap = new MDataMap("optionExport", "1");

		String exportName = mPage.getPageName() + "-" +FormatHelper.upDateTime(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".xls";
		flushHeader(response, exportName);
		
		List<Map<String,Object>> dataList = null;
		
		if(NumberUtils.toInt(mReqMap.get("zw_p_size")) == -1){  // 导出全部
			dataList = upChartDataForAll(mPage,mReqMap,mOptionMap);
		}else{  // 导出当前页
			dataList = upChartDataForPage(mPage,mReqMap,mOptionMap);
		}
		
		int rownum = 0;
		HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet("sheet1");
		HSSFRow row = sheet.createRow(rownum++);
		
		HSSFCellStyle hHeaderStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//表头加粗
		hHeaderStyle.setFont(font);
		
		// 创建表头
		HSSFCell hCell = null;
		for(int i = 0; i < headNames.length; i++){
			hCell = row.createCell(i);
			hCell.setCellValue(headNames[i]);
			hCell.setCellStyle(hHeaderStyle);
		}
		
		// 创建表格内容
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		for(Map<String,Object> data : dataList){
			row = sheet.createRow(rownum++);
			for(int i = 0; i < headCodes.length; i++){
				if("creator".equals(headCodes[i])&&"1".equals(data.get("auto_flag"))) {
					row.createCell(i).setCellValue("系统");
				}
				else {
					row.createCell(i).setCellValue(String.valueOf(data.get(headCodes[i])));
				}
				
			}
		}
		
		try {
			wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 先让浏览器显示一个下载的文件
	 */
	private void flushHeader(HttpServletResponse response, String exportName){
		try {
			response.setHeader("Content-Type","application/msexcel");
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", new URLCodec("UTF-8").encode(exportName)));
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.getOutputStream().flush();
		} catch (EncoderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询单页数据
	 */
	private List<Map<String,Object>> upChartDataForPage(MWebPage mPage,MDataMap mReqMap,MDataMap mOptionMap){
		MPageData pageData = new PageExec().upChartData(mPage, mReqMap, mOptionMap);
		List<String> headList = pageData.getPageHead();
		List<List<String>> dataList = pageData.getPageData();
		List<String> zidList = new ArrayList<String>();
		
		
		
		//获取zidList	
			for(List<String> vs : dataList){
				if(StringUtils.isNotBlank(vs.get(14))){
					zidList.add("'"+vs.get(14)+"'");
				}
			}

		if(zidList.isEmpty()) return new ArrayList<Map<String,Object>>();
		String exeSql = sql + " where zid  IN ("+StringUtils.join(zidList,",")+")"; 
		return DbUp.upTable("v_sc_flow_history_skuprice_cw_shenhe2").dataSqlList(exeSql,null);
	}
	/**
	 * 查询全部数据
	 */
	private List<Map<String,Object>> upChartDataForAll(MWebPage mPage,MDataMap mReqMap,MDataMap mOptionMap){
		MPageData pageData = new PageExec().upChartData(mPage, mReqMap, mOptionMap);
		List<String> headList = pageData.getPageHead();
		List<List<String>> dataList = pageData.getPageData();
		List<String> zidList = new ArrayList<String>();
				
		//获取zidList	
			for(List<String> vs : dataList){
				if(StringUtils.isNotBlank(vs.get(14))){
					zidList.add("'"+vs.get(14)+"'");
				}
			}

		if(zidList.isEmpty()) return new ArrayList<Map<String,Object>>();
		String exeSql = sql + " where zid  IN ("+StringUtils.join(zidList,",")+")"; 
		return DbUp.upTable("v_sc_flow_history_skuprice_cw_shenhe2").dataSqlList(exeSql,null);
	}
	

}
