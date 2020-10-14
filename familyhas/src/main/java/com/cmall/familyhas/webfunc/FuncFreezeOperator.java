package com.cmall.familyhas.webfunc;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.kvsupport.KvFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 冻结操作用户<br>
 * 每日查询单个订单超过一定次数则冻结用户,防止订单被恶意查询
 * @author zhaojunling
 * 
 */
public class FuncFreezeOperator extends RootFunc {

	private KvFactory kv = XmasKv.upFactory(EKvSchema.FreezeOperator);
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		if(StringUtils.isBlank(sOperateUid)) return new MWebResult();
		
		MWebResult mResult = new MWebResult();
		String theDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
		
		String val = kv.get(sOperateUid);
		if(val == null || !val.startsWith(theDate)){
			// 初始化当日缓存
			kv.set(sOperateUid, theDate+"-1");
			return mResult;
		}
		
		// 已查询次数
		int upNum = NumberUtils.toInt(val.split("-")[1], 0);
		// 最大默认配置的最大查询次数
		int maxNum = NumberUtils.toInt(bConfig("familyhas.freeze_operator_query_order"));
		
		// 如果对特定用户配置了次数，则优先使用特定配置的值
		MDataMap mdataMap = DbUp.upTable("za_userinfo_freeze").oneWhere("*","","","user_code", sOperateUid);
		if(mdataMap != null){
			maxNum = NumberUtils.toInt(mdataMap.get("max_num_query_order"), 0);
		}
		
		if(maxNum <= 0) maxNum = 999;
		if(upNum > maxNum){
			// 冻结当前操作用户
			MDataMap map = new MDataMap();
			map.put("user_code", sOperateUid);
			map.put("flag_enable", "0");
			DbUp.upTable("za_userinfo").dataUpdate(map, "flag_enable", "user_code");
			
			// 插入冻结日志
			map = new MDataMap();
			map.put("user_code", sOperateUid);
			map.put("reason", "当日单条订单数据查询次数超限");
			map.put("operator", "system");
			map.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			DbUp.upTable("lc_user_freeze_log").dataInsert(map);
			
			// 设置冻结用户的结果码
			mResult.setResultCode(0);
			// 冻结用户后重置查询次数为0，以便于解冻后不影响继续查询
			upNum = 0;
		}
		
		kv.set(sOperateUid, theDate+"-"+(upNum+1));
		
		return mResult;
	}
	
	
}