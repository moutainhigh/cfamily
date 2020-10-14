package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiMyCenterConfigInput;
import com.cmall.familyhas.api.result.ApiMyCenterConfigResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * @descriptions 校验个人中心配置数据是否重复
 * @refactor 除了类型 URL地址外，其他的类型，数据只能有一个
 * @date 2017年09月18日下午10:51:39
 * @author Lujunjie
 * @version 1.0.2
 */
public class ApiMyCenterConfig extends RootApiForManage<ApiMyCenterConfigResult, ApiMyCenterConfigInput> {

	@Override
	public ApiMyCenterConfigResult Process(ApiMyCenterConfigInput inputParam, MDataMap mRequestMap) {
		String type = mRequestMap.get("type");
		String uid = mRequestMap.get("uid");
		
		boolean flag = true;
		String msg = "";
		if(uid == null) {//添加操作
			//URL地址类型只能有一个
			if(!"449748030006".equals(type)) {
				int count = DbUp.upTable("fh_center_config").dataCount("type = '" + type + "'", new MDataMap());
				if(count > 0) {
					flag = false;
					msg = "该类型已存在!";
				}
			}
		}else {//修改操作 
			//URL地址类型只能有一个
			if(!"449748030006".equals(type)) {
				List<MDataMap> list = DbUp.upTable("fh_center_config").queryByWhere("type", type);
				if(list.size() > 0) {
					MDataMap map = list.get(0);
					if(!uid.equals(map.get("uid"))) {
						flag = false;
						msg = "该类型已存在!";
					}
				}
			}
		}
		
		ApiMyCenterConfigResult result = new ApiMyCenterConfigResult();
		result.setSuccess(flag);
		result.setMsg(msg);
		return result;
	}
}
