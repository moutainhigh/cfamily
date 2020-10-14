package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.srnpr.xmasorder.channel.model.ChannelConst;
import com.srnpr.xmasorder.channel.service.PorscheOrderService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: FuncChannelSellerInvest
 * @Description: 渠道合作商预付款充值
 * @author lgx
 * 
 */
public class FuncChannelSellerInvest extends RootFunc {

	private static final long TIMEOUT = 30000;
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String channel_seller_code = mDataMap.get("channelSellerCode");
		// 充值金额
		String recharge_money = mDataMap.get("zw_f_recharge_money");
		String remark = mDataMap.get("zw_f_remark");
		String invest_voucher = mDataMap.get("zw_f_upload_show");
		
		Map<String, Object> channel_seller = DbUp.upTable("uc_channel_sellerinfo").dataSqlOne("SELECT * FROM uc_channel_sellerinfo WHERE channel_seller_code = '"+channel_seller_code+"'", new MDataMap());
		if(channel_seller==null) {
			result.setResultCode(-1);
			result.setResultMessage("查不到该条数据,充值失败");
			return result;
		}
		
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$"); // 判断小数点后2位的数字的正则表达式
		Matcher match = pattern.matcher(recharge_money);
		if(!match.matches()) {
			result.setResultCode(-1);
			result.setResultMessage("充值金额填写有误,请填写数字,支持两位小数");
			return result;
		}
		
		long begin = System.currentTimeMillis();
		String lockKey = ChannelConst.operationMoneyStart + channel_seller_code;
		String lockCode = "";
		try {
			//锁定渠道商
			while("".equals(lockCode = KvHelper.lockCodes(10, lockKey))) {
				if(System.currentTimeMillis() - begin > TIMEOUT) {
					result.setResultCode(-1);
					result.setResultMessage("请求超时");
					return result;
				}
			}
			
			if(result.upFlagTrue()) {
				// 预付款余额
				BigDecimal advance_balance =  (BigDecimal) channel_seller.get("advance_balance");
				BigDecimal new_advance_balance = advance_balance.add(new BigDecimal(recharge_money));
				MDataMap updateDataMap = new MDataMap();
				updateDataMap.put("channel_seller_code", channel_seller_code);
				updateDataMap.put("advance_balance", new_advance_balance.toString());
				int dataUpdate = DbUp.upTable("uc_channel_sellerinfo").dataUpdate(updateDataMap, "advance_balance", "channel_seller_code");
				
				if(dataUpdate > 0) {
					// 往 lc_channel_seller_invest_log 插入充值日志数据
					MDataMap map = new MDataMap();
					map.put("channel_seller_code", channel_seller_code);
					map.put("channel_seller_name", (String) channel_seller.get("channel_seller_name"));
					map.put("register_person", UserFactory.INSTANCE.create().getUserCode());
					map.put("recharge_money", recharge_money);
					map.put("recharge_date", com.cmall.familyhas.util.DateUtil.getNowTime());
					map.put("remark", remark);
					map.put("invest_voucher", invest_voucher);
					DbUp.upTable("lc_channel_seller_invest_log").dataInsert(map);
					
					// 往渠道商预存款变动日志表lc_operation_channel_money插入充值日志
					new PorscheOrderService().insertChannelMoneyLog(channel_seller_code, "449748420004", recharge_money, new_advance_balance.toString(), "", "系统充值");
				}else {
					result.setResultCode(-1);
					result.setResultMessage("充值失败");
					return result;
				}
			}
		} finally {
			if(!"".equals(lockCode)) KvHelper.unLockCodes(lockCode, lockKey);
		}
		
		return result;
	}


}
