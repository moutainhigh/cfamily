package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiGetTempletePageInfoInput;
import com.cmall.familyhas.api.result.ApiGiveDkIntegralResult;
import com.cmall.groupcenter.util.HttpUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 浏览专题送积分
 * 
 * @author sunyan
 * 
 */
public class ApiGiveZtIntegral extends RootApiForToken<ApiGiveDkIntegralResult, ApiGetTempletePageInfoInput> {

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	
	public ApiGiveDkIntegralResult Process(ApiGetTempletePageInfoInput inputParam, MDataMap mRequestMap) {
		ApiGiveDkIntegralResult result = new ApiGiveDkIntegralResult();	
		String userCode = getUserCode();
		String mobile = getOauthInfo().getLoginName();
		String sLockKey=KvHelper.lockCodes(10, userCode);
		if(StringUtils.isBlank(sLockKey)){
			result.setResultMessage("您已领取该次积分");
			return result;
		}
		String event_code = "";
		int seq = 0;
		BigDecimal jfNum = new BigDecimal(0);
		String sql = "SELECT a.event_code from sc_hudong_event_info a where a.event_type_code = 449748210007 AND a.event_status = 4497472700020002 AND NOW() BETWEEN a.begin_time AND a.end_time";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_hudong_event_info").dataSqlList(sql, null);
		if(dataSqlList.size()>0){
			event_code = dataSqlList.get(0).get("event_code").toString();
			String sSql = "SELECT * from sc_hudong_event_ad_jf_rule where event_code = '"+event_code+"'";
			List<Map<String, Object>> dataList = DbUp.upTable("sc_huodong_event_dakajf_rule").dataSqlList(sSql, null);
			if(dataList.size()>0){
				String tSql = "SELECT IFNULL(MAX(event_seq),0) event_seq from lc_hudong_event_ad_jf_lq WHERE page_number = '"+inputParam.getPageNum()+"' AND member_code = '"+userCode+"' AND TO_DAYS(etr_date) = TO_DAYS(NOW())";
				List<Map<String, Object>> tempList = DbUp.upTable("lc_hudong_event_ad_jf_lq").dataSqlList(tSql, null);								
				if(tempList.size()>0){
					seq = Integer.parseInt(tempList.get(0).get("event_seq").toString());
					int lq_times = Integer.parseInt(dataList.get(0).get("lq_times").toString());
					if(seq>=lq_times){
						result.setResultCode(0);
						result.setResultMessage("已达今日最大领取次数");
						return result;
					}else{
						seq += 1;
						switch (seq) {
						case 1:
							jfNum = jfNum.add(new BigDecimal(Integer.parseInt(dataList.get(0).get("f_integral").toString())));
							break;
						case 2:
							jfNum = jfNum.add(new BigDecimal(Integer.parseInt(dataList.get(0).get("s_integral").toString())));
							break;
						case 3:
							jfNum = jfNum.add(new BigDecimal(Integer.parseInt(dataList.get(0).get("t_integral").toString())));
							break;

						default:
							break;
						}
					}
				}
			}else{
				result.setResultCode(0);
				result.setResultMessage("没有设置专题奖励数据");
				return result;
			}
		}else{
			result.setResultCode(0);
			result.setResultMessage("没有绑定生效的浏览专题互动活动");
			return result;
		}
		
		try {
			String date = FormatHelper.upDateTime("yyyy-MM-dd");
			String temp = XmasKv.upFactory(EKvSchema.Ztsjf).hget(date,userCode+"|"+inputParam.getPageNum());
			if(StringUtils.isNotBlank(temp)&&seq<=Integer.parseInt(temp)){
				result.setResultCode(0);
				result.setResultMessage("用户今日已领取过该次积分，请不要重复领取");
				return result;
			}			
			this.giveIntegral(jfNum, userCode, null, event_code, UpdateCustAmtInput.CurdFlag.ZT,mobile);
			if (StringUtils.isBlank(temp)) {
				XmasKv.upFactory(EKvSchema.Ztsjf).hset(date,userCode+"|"+inputParam.getPageNum(), seq+"");
				XmasKv.upFactory(EKvSchema.Ztsjf).expire(date, 3600*24);

			}else{
				XmasKv.upFactory(EKvSchema.Ztsjf).hset(date,userCode+"|"+inputParam.getPageNum(), seq+"");
			}
			MDataMap iMap = new MDataMap();
			iMap.put("event_code", event_code);
			iMap.put("event_seq", seq+"");
			iMap.put("member_code", userCode);
			iMap.put("page_number", inputParam.getPageNum());
			iMap.put("jf_num", jfNum+"");

			DbUp.upTable("lc_hudong_event_ad_jf_lq").dataInsert(iMap);
			result.setIntegral(jfNum+"");
			KvHelper.unlockCode(sLockKey, userCode);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setResultMessage(e.getMessage());
		}		
		
		return result;
	}
	
	/**
	 * 赠送积分
	 */
	private void giveIntegral(BigDecimal giveMoney,String memberCode,String bigOrderCode,String orderCode,UpdateCustAmtInput.CurdFlag doType,String mobile){
		giveMoney = plusServiceAccm.accmAmtToMoney(giveMoney,2);
		String custId = plusServiceAccm.getCustId(memberCode);// 家有客代号
		// 如果客代号为空,调用接口生成客代
		if(null == custId || "".equals(custId)) {
			// 调用接口生成客代
			String url = bConfig("groupcenter.rsync_homehas_url")+"createUser";
			Map<String, Object> postParams = new HashMap<String, Object>();
			postParams.put("mobile", mobile);
			String postResult = HttpUtil.post(url, JSONArray.toJSONString(postParams), "UTF-8");
			JSONObject jo = JSONObject.parseObject(postResult);
			custId = jo.get("cust_id").toString();
		}
		RootResult teamResult = plusServiceAccm.changeForAccmAmt(doType, giveMoney, custId, bigOrderCode, orderCode);
		// 记录积分变更日志  - 积分共享增加
		if(teamResult.getResultCode() == 1) {
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("member_code", memberCode);
			mDataMap.put("cust_id", custId);
			mDataMap.put("change_type", "449748080015");
			mDataMap.put("change_money", giveMoney.toString());
			mDataMap.put("remark", orderCode==null?"":orderCode);
			mDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
		}
	}
	
}
