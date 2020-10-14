package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 渠道商品删除
 * @author lgx
 *
 */
public class FuncDeleteChannelProduct extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MDataMap channel_product = DbUp.upTable("pc_channel_productinfo").one("uid",mMaps.get("uid"));
		if(channel_product==null) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("查不到该条数据,删除失败");
			return mResult;
		}
		String nowTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		MDataMap delDataMap = new MDataMap();
		delDataMap.put("uid", mMaps.get("uid"));
		delDataMap.put("update_time", nowTime);
		delDataMap.put("is_delete", "1");
		int dataDel = DbUp.upTable("pc_channel_productinfo").dataUpdate(delDataMap, "update_time,is_delete", "uid");
		
		if(dataDel > 0) {			
			// 往 lc_channel_product_register_log 插入删除日志数据
			MDataMap map = new MDataMap();
			map.put("sku_code", channel_product.get("sku_code"));
			map.put("product_name", channel_product.get("product_name"));
			map.put("register_time", nowTime);
			map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map.put("register_name", "删除");
			map.put("remark", UserFactory.INSTANCE.create().getRealName()+"删除一件商品");
			DbUp.upTable("lc_channel_product_register_log").dataInsert(map);
		}else {
			mResult.setResultCode(-1);
			mResult.setResultMessage("删除失败");
			return mResult;
		}
		
		return mResult;
	}

}
