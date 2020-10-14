package com.cmall.familyhas.webfunc.ldmsgpay;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @Author fufu
* @Time 2020-8-19 16:18:52 
* @Version 1.0
* <p>Description:</p>
*/
public class AddProductForOperateFunc extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		MDataMap insert = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if(StringUtils.isEmpty(insert.get("uid"))) {//执行新增操作
			insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
			DbUp.upTable("sc_recommend_product_ldpay").dataInsert(insert);
		}else {
			if("4497471600660001".equals(insert.get("recommend_type"))) {//选择猜你喜欢时，清空商品
				insert.put("product_codes", "");
			}
			DbUp.upTable("sc_recommend_product_ldpay").dataUpdate(insert, "if_recommend,recommend_type,product_codes", "uid");
		}
		result.setResultCode(1);
		result.setResultMessage("操作成功");
		return result;
	}

}
