package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForBatchDelInput extends RootInput{

	
	private String tableName ="";
	
	private String fieldName ="";
	
	private String fieldValus ="";

	@ZapcomApi(remark="这个字段来判断是物理删除还是逻辑删除,0物理  1逻辑 ,以后有批量删除功能的可用这个类来处理(逻辑的话数据库定义字段is_delete 1删除 )")
	private String delFlag ="0";

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValus() {
		return fieldValus;
	}

	public void setFieldValus(String fieldValus) {
		this.fieldValus = fieldValus;
	}


}
