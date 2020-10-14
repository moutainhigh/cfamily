package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
 * 商品软删除
* @author Angel Joy
* @Time 2020年4月16日 下午2:04:03 
* @Version 1.0
* <p>Description:</p>
* 4497153900060003 :商家下架
* 4497153900060004 :平台强制下架
*/
public class FuncForDeleteProduct extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		if(StringUtils.isEmpty(uid)) {
			result.setResultCode(0);
			result.setResultMessage("未知异常！");
			return result;
		}
		//判断是都是上架商品，上架商品禁止删除。
		MDataMap productInfo = DbUp.upTable("pc_productinfo").one("uid",uid);
		if(productInfo == null || productInfo.isEmpty()) {
			result.setResultCode(0);
			result.setResultMessage("未知异常！");
			return result;
		}
		String product_status = productInfo.get("product_status");
		if(product_status.equals("4497153900060003")||product_status.equals("4497153900060004")) {//下架或是强制下架商品才可以删除
			productInfo.put("if_delete", "1");
			DbUp.upTable("pc_productinfo").dataUpdate(productInfo, "if_delete", "uid");
			result.setResultCode(1);
			return result;
		}else {
			result.setResultCode(0);
			result.setResultMessage("商品状态不允许删除！");
			return result;
		}
	}

}
