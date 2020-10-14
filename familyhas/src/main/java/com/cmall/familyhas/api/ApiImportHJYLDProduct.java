package com.cmall.familyhas.api;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.input.ApiImportHJYLDProductInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 
 * 类: ApiImportHJYLDProduct <br>
 * 描述: 导入惠家有自营LD 商品关系 <br>
 * 作者: 付强 fuqiang@huijiayou.cn<br>
 * 时间: 2016-8-5 下午12:04:31
 */
public class ApiImportHJYLDProduct extends RootApiForManage<RootResult, ApiImportHJYLDProductInput>{

	@Override
	public RootResult Process(ApiImportHJYLDProductInput inputParam,
			MDataMap mRequestMap) {
		
		RootResult result = new RootResult();
		String upload_excel_url = inputParam.getUpload_excel_url();
		if(StringUtils.isNotBlank(upload_excel_url)) {
			
			try {
				StringBuffer hadData = new StringBuffer();
				StringBuffer execSql = new StringBuffer();
				StringBuffer datasql = new StringBuffer();
				
				execSql.append(" INSERT INTO productcenter.pc_hjyLD_rel_LD_productinfo (uid,product_code,sku_code,ld_product_code,ld_sku_code,create_user,create_time) VALUES");
				
				ExcelSupport excelSupport = new ExcelSupport();
				List<MDataMap> upExcelFromUrl = excelSupport.upExcelFromUrl(upload_excel_url);
				for (MDataMap mDataMap : upExcelFromUrl) {
					if(!mDataMap.isEmpty()) {
						String loginname=UserFactory.INSTANCE.create().getLoginName();
						if(loginname==null||"".equals(loginname)){
							result.setResultCode(941901073);
							result.setResultMessage(TopUp.upLogInfo(941901073));
							return result;
						}
						//先判断是否已经有关联的关系
						MDataMap mWhereMap = new MDataMap();
						mWhereMap.put("product_code", mDataMap.get("product_code"));
						mWhereMap.put("sku_code", mDataMap.get("sku_code"));
						mWhereMap.put("ld_sku_code", mDataMap.get("LD_sku_code"));
						mWhereMap.put("ld_product_code", mDataMap.get("LD_product_code"));
						int dataCount = DbUp.upTable("pc_hjyLD_rel_LD_productinfo").dataCount(" (sku_code = :sku_code and product_code = :product_code) or (ld_product_code =:ld_product_code and ld_sku_code = :ld_sku_code) ", mWhereMap);
						if(dataCount > 0) {
							hadData.append("[此数据已存在关联关系："+JSON.toJSONString(mDataMap)+"]");
							continue;
						}
						
						String sUid = UUID.randomUUID().toString().replace("-", "");
						
						datasql.append("('"+sUid+"','"+mDataMap.get("product_code") + "','"+mDataMap.get("sku_code")+"','"+mDataMap.get("LD_product_code")+"','"+mDataMap.get("LD_sku_code")+"','"+loginname+"','"+DateUtil.getSysDateTimeString()+"'),");
					} else {
						break;
					}
					
				}
				if(hadData.length() > 0) {
					result.setResultCode(0);
					result.setResultMessage("有重载关系的重复数据，请核查后重新导入，"+hadData.toString());
					return result;
				}
				
				if(datasql.length() > 0) {
					execSql.append(datasql.substring(0, datasql.length() -1));
					DbUp.upTable("pc_hjyLD_rel_LD_productinfo").dataExec(execSql.toString(), new MDataMap());
					
				}
				
			} catch (Exception e) {
				result.setResultCode(0);
				result.setResultMessage("excel解析异常，请检查后重新导入");
				e.printStackTrace();
			}
			
		}
		
		
		return result;
	}
	

}
