package com.cmall.familyhas.api;



import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForBatchDelInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 *<p>Description:公用批量删除接口 <／p> 
 * @author zb
 * @date 2020年6月29日
 *
 */

public class ApiForBatchDel extends RootApi<RootResult,  ApiForBatchDelInput>{


	@Override
	public RootResult Process(ApiForBatchDelInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResult rootResult = new RootResult();
		String tableName = inputParam.getTableName();
		String fieldValus = inputParam.getFieldValus();
		String fieldName = inputParam.getFieldName();
		String delFlag = inputParam.getDelFlag();
		String whereIds =fieldName+" in ('"+ fieldValus.replace(",", "','")+"')";
		if(StringUtils.isNotBlank(tableName)) {
			if("0".equals(delFlag)) {
				DbUp.upTable(tableName).dataExec("delete from "+tableName+" where "+whereIds, null);
			}else {
				DbUp.upTable(tableName).dataExec("update  "+tableName+" set is_delete='1' where  "+whereIds, null);
			}
			rootResult.setResultCode(1);
			rootResult.setResultMessage("操作成功!");
		}
		else {
			rootResult.setResultCode(0);
			rootResult.setResultMessage("请出入表名！");
		}
		return rootResult;
	}



}
