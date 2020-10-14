package com.cmall.familyhas.webfunc;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.ordercenter.model.ReturnMoneyExt;
import com.cmall.ordercenter.service.ReturnMoneyService;
import com.cmall.systemcenter.service.ScDefineService;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webexport.RootExport;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webpage.PageExec;

/**
 * 导出退款单
 * 
 * @author jlin
 * 
 *
 */
public class ReturnMoneyExport extends RootExport {

	public void export(String sOperateId, HttpServletRequest request, HttpServletResponse response) {

		synchronized (this) {
			
			
			
		
		
		exportExcel(sOperateId, request, response);

		// 修改数据
		MPageData pageData = getPageData();
		
		
		List<String>  headList=pageData.getPageHead();
		headList.add(3, "订单状态");
		headList.add("收款账户号");
		headList.add("微公社支付金额");
		
		// 重写数据
		List<List<String>> pd = pageData.getPageData();

		MemberLoginSupport loginSupport = new MemberLoginSupport();
		ReturnMoneyService returnMoneyService = new ReturnMoneyService();
		ScDefineService scDefineService = new ScDefineService();
		MDataMap scMap=scDefineService.defineMap("44974628");
		MDataMap statusMap=scDefineService.defineMap("449715390001");
		
		for (List<String> list : pd) {
			list.set(1, loginSupport.getMemberLoginName(list.get(1)));
			list.set(2, Jsoup.parse(list.get(2)).text());
			
			String order_code=list.get(2);
			String order_status=(String)DbUp.upTable("oc_orderinfo").dataGet("order_status", "order_code=:order_code", new MDataMap("order_code",order_code));
			list.add(3,StringUtils.isNotBlank(order_status)?statusMap.get(order_status):"");
			
			
			
			list.set(9, Jsoup.parse(list.get(2)).text());
			
			ReturnMoneyExt returnMoneyExt = returnMoneyService.extInfo(list.get(9));
			
			if(StringUtils.isNotBlank(returnMoneyExt.getPaygate_order_code())){
				list.set(10, returnMoneyExt.getPaygate_order_code());
			}else{
				list.set(10, returnMoneyExt.getBig_order_code());
			}
			list.set(11, returnMoneyExt.getPay_sequenceid());
			list.set(12, scMap.get(returnMoneyExt.getPay_type()));
			list.add(returnMoneyExt.getPhp_code());
			BigDecimal payed_money=(BigDecimal)DbUp.upTable("oc_order_pay").dataGet("payed_money", "order_code=:order_code and pay_type=:pay_type", new MDataMap("order_code",order_code,"pay_type","449746280009"));
			list.add(payed_money==null?"0.00":payed_money.toString());
		}
		
		
		exportExcelFile(pageData, response);
		
		}
	}

	@Override
	public void exportExcel(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		MWebPage mPage = WebUp.upPage(sOperateId);
		MDataMap mReqMap = convertRequest(request);
		PageExec pExec = new PageExec();
		MDataMap mOptionMap = new MDataMap("optionExport", "1");

		String exportName = mPage.getPageName() + "-"
				+ FormatHelper.upDateTime(new Date(), "yyyy-MM-dd-HH-mm-ss");
		String sAgent = request.getHeader("USER-AGENT");

		if (StringUtils.isNotEmpty(sAgent)) {
			boolean bFlagIE = (request.getHeader("USER-AGENT")
					.toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT")
					.toLowerCase().indexOf("rv:11.0") > 0) ? true : false;


			if (bFlagIE) {
				try {
					exportName = URLEncoder.encode(exportName, "UTF8");
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
				}
			} else {

				try {
					exportName = new String(exportName.getBytes("UTF-8"),
							"ISO8859-1");
				} catch (UnsupportedEncodingException e) {

					e.printStackTrace();
				}

			}
		}
		
		String productChannel = mReqMap.get("zw_f_product_channel");
		
		// 自营的走一个特殊的查询条件，暂定非LD商品都算自营
		if("4497471600550004".equals(productChannel)) {
			mReqMap.remove("zw_f_product_channel");
			mReqMap.put("sub_query", "small_seller_code != 'SI2003'");
		}

		setPageData(pExec.upChartData(mPage, mReqMap, mOptionMap));
	}
	
}
