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
 * 导出商品价格审批明细 
 */
public class FunExportForSkuPriceFlow extends RootProcess implements IWebFuncExport{

	String sql = "SELECT fm.zid,fm.flow_code, sku.product_code, sku.sku_code, sku.sku_name, skuf.cost_price_old, skuf.cost_price, skuf.sell_price_old, skuf.sell_price, skuf.start_time, skuf.end_time"
			+ " FROM systemcenter.sc_flow_main fm"
			+ " LEFT JOIN productcenter.pc_skuprice_change_flow skuf ON fm.flow_code = skuf.flow_code"
			+ " LEFT JOIN productcenter.pc_skuinfo sku ON skuf.sku_code = sku.sku_code"
			+ " WHERE fm.flow_type = '449717230013' AND fm.current_status = :current_status";
			//"AND fm.flow_code IN ();";
	
	String[] headNames = {"流程编码","商品编号","SKU编号","SKU名称","原成本价","变更后成本价","原销售价","变更后销售价","原毛利率","变更后毛利率","开始日期","结束日期","审核","审批意见"};
	String[] headCodes = {"flow_code","product_code","sku_code","sku_name","cost_price_old","cost_price","sell_price_old","sell_price","old_profit","profit","start_time","end_time"};
	
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
				if(i==8)
				{   
					BigDecimal old_first = new BigDecimal(data.get(headCodes[6]).toString());
					BigDecimal old_two = new BigDecimal(data.get(headCodes[4]).toString());
					String old_profit = numberFormat.format((old_first.floatValue()-old_two.floatValue())/old_first.floatValue()*100);
					row.createCell(i).setCellValue(String.valueOf(old_profit)+"%");
				}
				else if(i==9)
				{
					BigDecimal first = new BigDecimal(data.get(headCodes[7]).toString());
					BigDecimal two = new BigDecimal(data.get(headCodes[5]).toString());
					String profit = numberFormat.format((first.floatValue()-two.floatValue())/first.floatValue()*100);
					row.createCell(i).setCellValue(String.valueOf(profit)+"%");
				}
				else
				row.createCell(i).setCellValue(String.valueOf(data.get(headCodes[i])));
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
		List<String> productCodeList = new ArrayList<String>();
		
		// 商品编号
		int i = headList.indexOf("商品编号");
		if(i == -1) i = headList.indexOf("商品编码");
		
		if(i > -1){
			
			for(List<String> vs : dataList){
				if(StringUtils.isNotBlank(vs.get(i))){
					productCodeList.add("'"+vs.get(i)+"'");
				}
			}
		}
		
		if(productCodeList.isEmpty()) return new ArrayList<Map<String,Object>>();
		
		String exeSql = sql + " AND fm.outer_code IN ("+StringUtils.join(productCodeList,",")+") ORDER BY fm.zid desc"; 
		return DbUp.upTable("sc_flow_main").dataSqlList(exeSql, new MDataMap("current_status",getCurrentStatus(mPage)));
	}
	
	/**
	 * 查询全部数据
	 */
	private List<Map<String,Object>> upChartDataForAll(MWebPage mPage,MDataMap mReqMap,MDataMap mOptionMap){
		return DbUp.upTable("sc_flow_main").dataSqlList(sql+" ORDER BY fm.zid desc", new MDataMap("current_status",getCurrentStatus(mPage)));
	}
	
	private String getCurrentStatus(MWebPage mPage){
		if("page_chart_v_sc_flow_mian_skuprice_cw".equalsIgnoreCase(mPage.getPageCode())){
			return "4497172300130001";
		}else if("page_chart_v_sc_flow_mian_skuprice_yy".equalsIgnoreCase(mPage.getPageCode())){
			return "4497172300130004";
		}else{
			return "unkown";
		}
	}
}
