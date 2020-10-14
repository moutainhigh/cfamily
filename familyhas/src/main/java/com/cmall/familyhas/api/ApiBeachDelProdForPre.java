package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiBeachDelProdForPreInput;
import com.cmall.familyhas.api.result.ApiBeachDelProdForPreResult;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 前端分类批量删除商品
 * @author lgx
 * 
 */
public class ApiBeachDelProdForPre extends RootApi<ApiBeachDelProdForPreResult, ApiBeachDelProdForPreInput> {

	@Override
	public ApiBeachDelProdForPreResult Process(ApiBeachDelProdForPreInput inputParam, MDataMap mRequestMap) {
		ApiBeachDelProdForPreResult result = new ApiBeachDelProdForPreResult();

		String nodeId = inputParam.getNodeId();
		String delProdUids = inputParam.getDelProdUids();
		result.setNodeId(nodeId);
		
		if (StringUtils.isBlank(delProdUids)) {
			result.setResultCode(99);
			result.setResultMessage("请选择要删除的商品");
			return result;
		}
		
		StringBuffer uidSb =  new StringBuffer();
		String[] uids = delProdUids.split(",");
		for (String uid : uids) {
			if(uid != null && !"".equals(uid)) {
				uidSb.append("'" + uid + "',");
			}
		}
		String uidString = uidSb.toString().trim().substring(0,uidSb.toString().trim().length()-1);
		// 查询要删除的商品
		List<Map<String, Object>> prodList = DbUp.upTable("uc_sellercategory_pre_product").dataSqlList("SELECT * FROM uc_sellercategory_pre_product WHERE uid in("+uidString+")", new MDataMap());
		// 删除商品
		DbUp.upTable("uc_sellercategory_pre_product").dataExec("DELETE FROM uc_sellercategory_pre_product WHERE uid in("+uidString+")", new MDataMap());
		
		// 刷新solr缓存
		ProductJmsSupport productJmsSupport = new ProductJmsSupport();
		if(null != prodList && prodList.size() > 0) {			
			for (Map<String, Object> map : prodList) {
				String product_code = MapUtils.getString(map, "product_code");
				if(!"".equals(product_code)) {
					productJmsSupport.updateSolrData(product_code);
				}
			}
		}
		
		result.setResultMessage("删除成功!");
		
		return result;
	}


}
