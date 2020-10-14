package com.cmall.familyhas.webfunc;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncEditChannelProduct
 * @Description: 渠道商品修改
 * @author lgx
 * 
 */
public class FuncEditChannelProduct extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String skuCode = mDataMap.get("skuCode");
		// 供货价比例
		String supply_price_proportion = mDataMap.get("zw_f_supply_price_proportion");
		String remark = mDataMap.get("zw_f_remark");
		
		Map<String, Object> channel_product = DbUp.upTable("pc_channel_productinfo").dataSqlOne("SELECT * FROM pc_channel_productinfo WHERE sku_code = '"+skuCode+"' AND is_delete = 0", new MDataMap());
		if(channel_product==null) {
			result.setResultCode(-1);
			result.setResultMessage("查不到该条数据,修改失败");
			return result;
		}
		
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$"); // 判断小数点后2位的数字的正则表达式
		Matcher match = pattern.matcher(supply_price_proportion);
		if(!match.matches()) {
			result.setResultCode(-1);
			result.setResultMessage("供货价比例填写有误,请填写数字,支持两位小数");
			return result;
		}
		
		String nowTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		MDataMap updateDataMap = new MDataMap();
		updateDataMap.put("sku_code", skuCode);
		updateDataMap.put("update_time", nowTime);
		updateDataMap.put("supply_price_proportion", supply_price_proportion);
		int dataUpdate = DbUp.upTable("pc_channel_productinfo").dataUpdate(updateDataMap, "supply_price_proportion,update_time", "sku_code");
		
		if(dataUpdate > 0) {			
			// 往 lc_channel_product_register_log 插入修改日志数据
			MDataMap map = new MDataMap();
			map.put("sku_code", skuCode);
			map.put("product_name", (String) channel_product.get("product_name"));
			map.put("register_time", nowTime);
			map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
			map.put("register_name", "修改");
			map.put("remark", remark);
			DbUp.upTable("lc_channel_product_register_log").dataInsert(map);
		}else {
			result.setResultCode(-1);
			result.setResultMessage("修改失败");
			return result;
		}
		
		return result;
	}


}
