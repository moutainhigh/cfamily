package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.result.ApiGiveDkIntegralResult;
import com.cmall.productcenter.common.DateUtil;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 打卡奖励积分
 * 
 * @author sunyan
 * 
 */
public class ApiGiveDkIntegral extends RootApiForToken<ApiGiveDkIntegralResult, RootInput> {

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	
	public ApiGiveDkIntegralResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiGiveDkIntegralResult result = new ApiGiveDkIntegralResult();	
		String event_code = "";
		String can_dk = "Y";
		int dk_day = 1;
		BigDecimal jfNum = new BigDecimal(0);
		String sql = "SELECT a.event_code from sc_hudong_event_info a where a.event_type_code = 449748210005 AND a.event_status = 4497472700020002 AND NOW() BETWEEN a.begin_time AND a.end_time";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_hudong_event_info").dataSqlList(sql, null);
		if(dataSqlList.size()>0){
			event_code = dataSqlList.get(0).get("event_code").toString();
			String sSql = "SELECT * from sc_huodong_event_dakajf_rule b where b.event_code = '"+event_code+"' ORDER BY b.jf_day ASC";
			List<Map<String, Object>> dataList = DbUp.upTable("sc_huodong_event_dakajf_rule").dataSqlList(sSql, null);
			if(dataList.size()>0){
				String tSql = "SELECT * from lc_huodong_event_dakajf_user WHERE jf_dk_time = (SELECT MAX(a.jf_dk_time) from lc_huodong_event_dakajf_user a where a.member_code = '"+getUserCode()+"' AND a.event_code = '"+event_code+"') AND member_code = '"+getUserCode()+"' AND event_code = '"+event_code+"'";
				List<Map<String, Object>> tempList = DbUp.upTable("lc_huodong_event_dakajf_user").dataSqlList(tSql, null);								
				if(tempList.size()>0){
					String dk_time = tempList.get(0).get("jf_dk_time").toString();
					long days = DateUtil.daysBetween(DateUtil.toSqlTimestamp(dk_time, "yyyy-MM-dd"), DateUtil.toSqlTimestamp(FormatHelper.upDateTime("yyyy-MM-dd"), "yyyy-MM-dd"));
					if(days < 1){
						can_dk = "N";
						dk_day = Integer.parseInt(tempList.get(0).get("jf_day").toString());
					}else if (days>=1&&days<2) {
						dk_day = Integer.parseInt(tempList.get(0).get("jf_day").toString());
						if(dk_day==7){
							dk_day = 1;
						}else{
							dk_day += 1;
						}
					}					
				}
				for(Map<String, Object> map:dataList){
					int jf_day = Integer.parseInt(map.get("jf_day").toString());
					if(jf_day==dk_day){
						if(map.get("jf_type").toString().equals("01")){
							jfNum = jfNum.add(new BigDecimal(map.get("jf_min_num").toString()));
						}else if (map.get("jf_type").toString().equals("02")) {
							int min = Integer.parseInt(map.get("jf_min_num").toString());
							int max = Integer.parseInt(map.get("jf_max_num").toString());
							jfNum = jfNum.add(new BigDecimal(getEvenNum(min, max)));
						}
					}
				}
			}else{
				result.setResultCode(0);
				result.setResultMessage("没有设置签到奖励数据");
				return result;
			}
		}else{
			result.setResultCode(0);
			result.setResultMessage("没有绑定生效的签到活动");
			return result;
		}
		
		if(can_dk.equals("Y")){
			try {
				this.giveIntegral(jfNum, getUserCode(), null, event_code, UpdateCustAmtInput.CurdFlag.DK);
				MDataMap iMap = new MDataMap();
				iMap.put("event_code", event_code);
				iMap.put("member_code", getUserCode());
				iMap.put("jf_dk_time", FormatHelper.upDateTime("yyyy-MM-dd"));
				iMap.put("jf_day", dk_day+"");
				iMap.put("jf_num", jfNum+"");

				DbUp.upTable("lc_huodong_event_dakajf_user").dataInsert(iMap);
				result.setIntegral(jfNum+"");
			} catch (Exception e) {
				result.setResultCode(0);
				result.setResultMessage(e.getMessage());
			}
		}else{
			result.setResultCode(0);
			result.setResultMessage("您今天已打过卡了");
		}		
		
		return result;
	}
	
	/**
	 * 赠送积分
	 */
	private void giveIntegral(BigDecimal giveMoney,String memberCode,String bigOrderCode,String orderCode,UpdateCustAmtInput.CurdFlag doType){
		giveMoney = plusServiceAccm.accmAmtToMoney(giveMoney,2);
		String custId = plusServiceAccm.getCustId(memberCode);// 家有客代号
		RootResult teamResult = plusServiceAccm.changeForAccmAmt(doType, giveMoney, custId, bigOrderCode, orderCode);
		// 记录积分变更日志  - 积分共享增加
		if(teamResult.getResultCode() == 1) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("member_code", memberCode);
			mDataMap.put("cust_id", custId);
			mDataMap.put("change_type", "449748080012");
			mDataMap.put("change_money", giveMoney.toString());
			mDataMap.put("remark", orderCode==null?"":orderCode);
			mDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
		}
	}
	
	public int getEvenNum(int a,int b) {
		int num = a+(int)(Math.random()*(b-a));
		if(num%2==0){
			return num;
		}else{
			return num+1;
		}		
	}
	
}
