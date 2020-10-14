package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加小程序支付成功维护商品
 * @author lgx
 * 
 */
public class FuncAddAppletPayProd extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap)  {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String keyValueList = mAddMaps.get("product_code_select").trim();
		if (StringUtils.isEmpty(keyValueList)) {
			mResult.inErrorMessage(916401220);
		}else{
			MDataMap mSetTimeMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
			// 创建时间为当年系统时间
			SimpleDateFormat create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			/* 获取当前登录人 */
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			mAddMaps.put("create_user", create_user);
			String[] splitsValue = keyValueList.split(",");
			
			// 添加的product_code已经存在
			StringBuffer cunzaiProd =  new StringBuffer();
			// 判断是否选择了商品
			if (splitsValue.length > 0 && null != splitsValue && !"".equals(splitsValue)) {
				for (String productCode : splitsValue) {
					MDataMap one = DbUp.upTable("pc_applet_pay_success").one("product_code", productCode);
					if(null != one && !one.isEmpty()){
						cunzaiProd.append(productCode+",");
					}else {
						mAddMaps.remove("product_code_select");
						mAddMaps.put("product_code", productCode);
						mAddMaps.put("uid", UUID.randomUUID().toString().replace("-", ""));
						mAddMaps.put("create_time", create_time.format(new Date()));
						mAddMaps.put("position", mSetTimeMap.get("position"));
						if (mResult.upFlagTrue()) {
							DbUp.upTable("pc_applet_pay_success").dataInsert(mAddMaps);
						}
					}
				}
			}
			if(cunzaiProd.toString().trim().length()>0) {
				// 添加的product_code已经存在
				mResult.setResultMessage("以下商品编码已经存在:"+cunzaiProd.toString().trim().substring(0,cunzaiProd.toString().trim().length()-1));
			}else {
				mResult.setResultMessage("操作成功");
			}
			
		}
		
		return mResult;
	}
}
