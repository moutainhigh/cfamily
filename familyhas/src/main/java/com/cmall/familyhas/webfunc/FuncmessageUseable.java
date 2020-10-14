package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * 类: FuncshoppingCartlike <br>
 * 描述: 客户端消息是否可用设置 <br>
 * 作者: sunyan<br>
 * 时间: 2019年5月28日14:02:05
 */
public class FuncmessageUseable extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult result = new MWebResult();
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		String operateValue = mAddMaps.get("is_flag");

		MDataMap one = DbUp.upTable("sc_message_configure").one();
		if(null != one) {//修改
			one.put("is_flag", operateValue);
			DbUp.upTable(mPage.getPageTable()).update(one);
		} else {//添加
			DbUp.upTable(mPage.getPageTable()).insert("is_flag",operateValue);
		}
		if (result.upFlagTrue()) {
			result.setResultMessage(bInfo(969909001));
		}
		// 缓存化
		XmasKv.upFactory(EKvSchema.MessageUseable).set("useable", operateValue);
		return result;
	}
}

