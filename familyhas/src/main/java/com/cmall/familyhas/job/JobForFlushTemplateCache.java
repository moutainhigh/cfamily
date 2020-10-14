package com.cmall.familyhas.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.webfunc.FuncRefreshWebTemplete;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @author Angel Joy
* @Time 2020年5月6日 下午2:18:42 
* @Version 1.0
* <p>Description:</p>
*/
public class JobForFlushTemplateCache extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String sInfo) {
		MWebResult result = new MWebResult();
		String sql = "SELECT * FROM familyhas.fh_data_commodity WHERE event_code = :event_code";
		List<Map<String,Object>> list = DbUp.upTable("fh_data_commodity").dataSqlList(sql, new MDataMap("event_code",sInfo));
		List<String> pageNums = new ArrayList<String>();
		List<String> templateNums = new ArrayList<String>();
		if(list.size() == 0 || list.isEmpty()) {
			String sqlProduct = "SELECT * FROM systemcenter.sc_event_item_product WHERE event_code = :event_code AND flag_enable = 1";
			List<Map<String,Object>> products = DbUp.upTable("sc_event_item_product").dataSqlList(sqlProduct, new MDataMap("event_code",sInfo));
			List<String> productCodes = new ArrayList<String>();
			List<String> skuCodes = new ArrayList<String>();
			for(Map<String,Object> productMap : products) {
				String productCode = MapUtils.getString(productMap, "product_code", "");
				String skuCode = MapUtils.getString(productMap, "sku_code", "");
				if(StringUtils.isNotEmpty(productCode)) {
					productCode = "'"+productCode+"'";
					productCodes.add(productCode);
				}
				if(StringUtils.isNotEmpty(skuCode)) {
					skuCode = "'"+skuCode+"'";
					skuCodes.add(skuCode);
				}
			}
			String productCodeWhere = StringUtils.join(productCodes, ",");
			if(StringUtils.isNotEmpty(productCodeWhere)) {
				String sqlTemplateOne = "SELECT IFNULL(COUNT(1),0) num,template_number FROM familyhas.fh_data_commodity WHERE good_number in ("+productCodeWhere+") AND dal_status = '1001' AND end_time >= sysdate() group by template_number";
				List<Map<String,Object>> numMapListOne = DbUp.upTable("fh_data_commodity").dataSqlList(sqlTemplateOne, new MDataMap());
				for(Map<String,Object> numMap : numMapListOne) {
					Integer count = MapUtils.getInteger(numMap, "num", 0);
					if(count > 0) {
						String templateNum = MapUtils.getString(numMap, "template_number", "");
						if(StringUtils.isNotEmpty(templateNum)) {
							templateNums.add("'"+templateNum+"'");
						}
					}
				}
			}
			String skuCodeWhere = StringUtils.join(skuCodes, ",");
			if(StringUtils.isNotEmpty(skuCodeWhere)) {
				String sqlTemplateTwo = "SELECT IFNULL(COUNT(1),0) num,template_number FROM familyhas.fh_data_commodity WHERE commodity_number in ("+skuCodeWhere+") AND dal_status = '1001' AND end_time >= sysdate() group by template_number";
				List<Map<String,Object>> numMapListTwo = DbUp.upTable("fh_data_commodity").dataSqlList(sqlTemplateTwo, new MDataMap());
				for(Map<String,Object> numMap : numMapListTwo) {
					Integer count = MapUtils.getInteger(numMap, "num", 0);
					if(count > 0) {
						String templateNum = MapUtils.getString(numMap, "template_number", "");
						if(StringUtils.isNotEmpty(templateNum)) {
							templateNums.add("'"+templateNum+"'");
						}
					}
				}
			}
		}else {
			for(Map<String,Object> map : list) {
				String templateNum = MapUtils.getString(map, "template_number", "");
				if(StringUtils.isNotEmpty(templateNum)) {
					templateNums.add("'"+templateNum+"'");
				}
			}		
		}
		getTemplateCode(pageNums,templateNums);
		pageNums = removeDuplicate(pageNums);
		MDataMap mdataMap = new MDataMap();
		mdataMap.put("zw_f_page_code", StringUtils.join(pageNums,","));
		result = new FuncRefreshWebTemplete().funcDo("",mdataMap);
		return result;
	}
	
	/**
	 * 查询需要刷新的模板编号
	 * @param pageNums
	 * @param template_number
	 * 2020-8-28
	 * Angel Joy
	 * void
	 */
	private void getTemplateCode(List<String> pageNums,List<String> template_number) {
		if(template_number.size()==0 || template_number == null) {
			return ;
		}
		String templateWhere = StringUtils.join(template_number, ",");
		String sql2 = "SELECT * FROM familyhas.fh_page_template WHERE template_number in ("+templateWhere+")";
		List<Map<String,Object>> listPage = DbUp.upTable("fh_page_template").dataSqlList(sql2, new MDataMap());
		for(Map<String,Object> map2 : listPage) {
			MDataMap mm2 = new MDataMap(map2);
			if(mm2 == null || mm2.isEmpty()) {
				continue;
			}
			String page_number = mm2.get("page_number");//刷新该page_number的缓存。
			if(!pageNums.contains(page_number)) {
				pageNums.add(page_number);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> removeDuplicate(List<String> list) {   
	    @SuppressWarnings("rawtypes")
		HashSet h = new HashSet(list);   
	    list.clear();   
	    list.addAll(h);   
	    return list;   
	} 

	private static ConfigJobExec config = new ConfigJobExec();
	static {
		config.setExecType("449746990031");
	}
	
	@Override
	public ConfigJobExec getConfig() {
		return config;
	}
	
}
