package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForBigWheelDrawInput;
import com.cmall.familyhas.api.model.HuodongEventDzpjpRule;
import com.cmall.familyhas.api.result.ApiForBigWheelDrawResult;
import com.cmall.groupcenter.util.HttpUtil;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webmodel.MMessage;
import com.srnpr.zapweb.websupport.MessageSupport;
import com.srnpr.zapweb.webwx.WxGateSupport;

/**
 * 大转盘-抽奖接口
 * @author lgx
 *
 */
public class ApiForBigWheelDraw extends RootApiForVersion<ApiForBigWheelDrawResult, ApiForBigWheelDrawInput> {

	// 大转盘抽奖消耗积分数
	private final String BigWheelDrawIntegral = bConfig("familyhas.bigWheelDrawIntegral");
	
	public ApiForBigWheelDrawResult Process(ApiForBigWheelDrawInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelDrawResult result = new ApiForBigWheelDrawResult();
		
		String memberCode = getOauthInfo().getUserCode();
		String eventCode = "";
		
		String nowTime = FormatHelper.upDateTime();
		//String startTime = nowTime.substring(0, 10)+" 00:00:00";
		//String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210004' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的福利转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		int totalIntegral = 0;
		int drawIntegral = Integer.parseInt(BigWheelDrawIntegral);
		
		// 抽奖类型( 0:免费抽 ; 1:积分抽奖 )
		String drawType = "0";
		// 是否可以抽奖
		Boolean drawFlag = false;
		// 获取用户剩余抽奖次数
		String sql1 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
		Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sql1, new MDataMap());
		int sycs = MapUtils.getInteger(drawNumMap, "sycs", 0);
		
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		// 家有客代号
		String custId = plusServiceAccm.getCustId(memberCode);
		if(sycs > 0) {
			// 如果有剩余次数,更新剩余抽奖次数减1
			sycs = sycs - 1;
			MDataMap mDataMap4 = new MDataMap();				
			mDataMap4.put("uid", (String) drawNumMap.get("uid"));
			mDataMap4.put("sycs", sycs+"");
			mDataMap4.put("update_time", nowTime);
			DbUp.upTable("sc_huodong_event_sycs").dataUpdate(mDataMap4, "sycs,update_time", "uid");
			drawFlag = true;
			drawType = "0";
		}else {
			// 如果剩余抽奖次数为0,则扣减积分
			// 用户总积分
			GetCustAmtResult plusModelCustAmt = plusServiceAccm.getPlusModelCustAmt(custId);
			BigDecimal accm = new BigDecimal(0);
			if (plusModelCustAmt != null) {
				accm = plusServiceAccm.moneyToAccmAmt(plusModelCustAmt.getPossAccmAmt(),1);
			}
			totalIntegral = accm.intValue()>0?accm.intValue():0;
			if(totalIntegral >= drawIntegral) {
				drawFlag = true;
				// 扣积分兑换抽奖次数
				// 积分转钱
				BigDecimal giveMoney1 = plusServiceAccm.accmAmtToMoney(new BigDecimal(drawIntegral),2);
				RootResult teamResult1 = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.DZC, giveMoney1, custId, "", "DD"+eventCode);
				// 记录积分变更日志  
				if(teamResult1.getResultCode() == 1) {
					// 用户总积分扣减
					totalIntegral -= drawIntegral;
					
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("member_code", memberCode);
					mDataMap.put("cust_id", custId);
					mDataMap.put("change_type", "449748080014");
					mDataMap.put("change_money", giveMoney1.toString());
					mDataMap.put("remark", eventCode);
					mDataMap.put("create_time", FormatHelper.upDateTime());
					DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
				}else {
					result.setResultCode(-1);
					result.setResultMessage("积分兑换次数失败!");
					return result;
				}
			}
			drawType = "1";
		}
		
		if(drawFlag) {
			// 大转盘奖品信息
			String sSql2 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_status = '1' AND event_code = '" + eventCode +"'";
			List<Map<String, Object>> eventDzpjpList = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlList(sSql2, new MDataMap());
			
			List<HuodongEventDzpjpRule> initDrawList = new ArrayList<HuodongEventDzpjpRule>();
			for (Map<String, Object> map : eventDzpjpList) {
				HuodongEventDzpjpRule huodongEventDzpjpRule = new HuodongEventDzpjpRule();
				huodongEventDzpjpRule.setJpCode(map.get("jp_code").toString());
				huodongEventDzpjpRule.setJpTitle(map.get("jp_title").toString());
				huodongEventDzpjpRule.setJpType(map.get("jp_type").toString());
				huodongEventDzpjpRule.setJpZjgl((int) map.get("jp_zjgl"));
				huodongEventDzpjpRule.setJpImg(map.get("jp_img").toString());
				huodongEventDzpjpRule.setJfNum((int) map.get("jf_num"));
				huodongEventDzpjpRule.setJpNum((int) map.get("jp_num"));
				huodongEventDzpjpRule.setProductName((String) map.get("product_name"));
				huodongEventDzpjpRule.setCouponTypeCode((String) map.get("coupon_type_code"));
				initDrawList.add(huodongEventDzpjpRule);
			}
			
			// 开始抽奖,返回抽中奖品信息
			HuodongEventDzpjpRule draw = generateAward(initDrawList);
			
			// 再查一下数据库
			String sql3 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_code = '"+draw.getJpCode()+"' AND jp_status = '1' AND event_code = '" + eventCode +"'";
			Map<String, Object> drawMap = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlOne(sql3, new MDataMap());
			int jp_num = (int) drawMap.get("jp_num");
			int jp_zjgl = (int) drawMap.get("jp_zjgl");
			if(jp_num > 0) {
				// 剩余奖品数量
				jp_num = jp_num-1;
				if(jp_num == 0) {
					// 抽完之后奖品数为0,更新该奖品中奖概率为0,且将该奖品之前的中奖概率加到"谢谢参与"上
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("uid", (String) drawMap.get("uid"));
					mDataMap.put("jp_num", "0");
					mDataMap.put("jp_zjgl", "0");
					DbUp.upTable("sc_huodong_event_dzpjp_rule").dataUpdate(mDataMap, "jp_num,jp_zjgl", "uid");
					
					// 查一条谢谢参与的奖品信息
					String sql4 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_type = '4497471600470004' AND jp_status = '1' AND event_code = '" + eventCode+"'";
					Map<String, Object> xieXieMap = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlOne(sql4, new MDataMap());
					if(xieXieMap == null) {
						// 改为抽取最少积分的奖项
						sql4 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_type = '4497471600470002' AND jp_status = '1' AND event_code = '"+eventCode+"' ORDER BY jf_num ASC LIMIT 1";
						xieXieMap = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlOne(sql4, new MDataMap());
					}
					int xie_jp_zjgl = (int) xieXieMap.get("jp_zjgl");
					xie_jp_zjgl += jp_zjgl;
					MDataMap mDataMap2 = new MDataMap();
					mDataMap2.put("uid", (String) xieXieMap.get("uid"));
					mDataMap2.put("jp_zjgl", xie_jp_zjgl+"");
					DbUp.upTable("sc_huodong_event_dzpjp_rule").dataUpdate(mDataMap2, "jp_zjgl", "uid");
					
				}else {
					// 抽完之后奖品数不为0
					MDataMap mDataMap3 = new MDataMap();
					mDataMap3.put("uid", (String) drawMap.get("uid"));
					mDataMap3.put("jp_num", jp_num+"");
					DbUp.upTable("sc_huodong_event_dzpjp_rule").dataUpdate(mDataMap3, "jp_num", "uid");
				}
				
			}else {
				// 查一条谢谢参与的奖品信息
				String sql4 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_type = '4497471600470004' AND jp_status = '1' AND event_code = '" + eventCode+"'";
				Map<String, Object> map = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlOne(sql4, new MDataMap());
				if(map == null) {
					// 改为抽取最少积分的奖项
					sql4 = "SELECT * FROM sc_huodong_event_dzpjp_rule WHERE jp_type = '4497471600470002' AND jp_status = '1' AND event_code = '"+eventCode+"' ORDER BY jf_num ASC LIMIT 1";
					map = DbUp.upTable("sc_huodong_event_dzpjp_rule").dataSqlOne(sql4, new MDataMap());
				}
				draw.setJpCode(map.get("jp_code").toString());
				draw.setJpTitle(map.get("jp_title").toString());
				draw.setJpType(map.get("jp_type").toString());
				draw.setJpZjgl((int) map.get("jp_zjgl"));
				draw.setJpImg(map.get("jp_img").toString());
				draw.setJfNum((int) map.get("jf_num"));
				draw.setJpNum((int) map.get("jp_num"));
				draw.setProductName((String) map.get("product_name"));
			}
			
			Map<String, Object> loginInfo = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+memberCode	+"'", new MDataMap());
			// 处理抽奖
			if(draw.getJpType().equals("4497471600470001")) { // 实物商品
				// 发送短信
				MMessage messages = new MMessage();
				messages.setMessageContent("恭喜您在惠家有大转盘活动中抽到"+draw.getJpTitle()+"，奖品我们将在2-4个工作日邮寄请注意查收，感谢支持~");
				messages.setMessageReceive((String) loginInfo.get("login_name"));
				messages.setSendSource("4497467200020006");
				MessageSupport.INSTANCE.sendMessage(messages);
				
				// 发送微信
				WxGateSupport support = new WxGateSupport();
				String receives = support.bConfig("familyhas.bigWheelWXReceives");
				List<String> list = support.queryOpenId(receives);
				String msg = String.format("[%s][%s]", "手机号为'"+loginInfo.get("login_name")+"'的用户抽中实物奖品"+draw.getJpTitle(), "请注意联系客户领取！") ;
				for(String v : list) {
					support.sendWarnCountMsg("福利转盘中奖信息", "抽中实物奖品"+draw.getJpTitle(), v, msg);
				}
				
			}else if(draw.getJpType().equals("4497471600470002")) { // 积分
				// 发积分
				// 积分转钱
				BigDecimal jfNum = new BigDecimal(draw.getJfNum());
				BigDecimal giveMoney = plusServiceAccm.accmAmtToMoney(jfNum,2);
				// 如果客代号为空,调用接口生成客代
				if(null == custId || "".equals(custId)) {
					// 调用接口生成客代
					String url = bConfig("groupcenter.rsync_homehas_url")+"createUser";
					Map<String, Object> postParams = new HashMap<String, Object>();
					postParams.put("mobile", loginInfo.get("login_name"));
					String postResult = HttpUtil.post(url, JSONArray.toJSONString(postParams), "UTF-8");
					JSONObject jo = JSONObject.parseObject(postResult);
					custId = jo.get("cust_id").toString();
				}
				// 赋予积分
				RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.DZ, giveMoney, custId, "", eventCode);
				// 记录积分变更日志
				if(teamResult.getResultCode() == 1) {
					// 用户总积分增加
					totalIntegral += draw.getJfNum();
					
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("member_code", memberCode);
					mDataMap.put("cust_id", custId);
					mDataMap.put("change_type", "449748080013");
					mDataMap.put("change_money", giveMoney.toString());
					mDataMap.put("remark", draw.getJpCode());
					mDataMap.put("create_time", FormatHelper.upDateTime());
					DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
				}else{
					String param = memberCode+","+giveMoney+","+eventCode+","+custId;
					// 操作不成功，加入到定时任务中进行重试
					JobExecHelper.createExecInfo("449746990022", param, null);
				}
			}else if(draw.getJpType().equals("4497471600470003")) { // 优惠券
				// 发优惠券
				CouponsService couponsService = new CouponsService();
				
				RootResultWeb result2 = couponsService.distributeCouponsByCouponCode(draw.getCouponTypeCode(), (String) loginInfo.get("login_name"), "2");
				if(result2.getResultCode()==1) {
					
				}else {
					result.setResultCode(result2.getResultCode());
					result.setResultMessage(result2.getResultMessage());
				}
			}
			
			// 添加抽奖记录
			MDataMap mDataMap5 = new MDataMap();
			mDataMap5.put("uid", WebHelper.upUuid());
			mDataMap5.put("event_code", eventCode);
			mDataMap5.put("jp_code", draw.getJpCode());
			mDataMap5.put("member_code", memberCode);
			// 用户昵称
			String sql2 = "SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"'";
			Map<String, Object> member = DbUp.upTable("mc_member_sync").dataSqlOne(sql2, new MDataMap());
			if(member != null) {
				mDataMap5.put("nickname", MapUtils.getString(member, "nickname"));
			}else {
				mDataMap5.put("nickname", "");
			}
			
			mDataMap5.put("zj_time", nowTime);
			mDataMap5.put("jp_title", draw.getJpTitle());
			mDataMap5.put("jp_type", draw.getJpType());
			// 抽奖类型( 0:免费抽 ; 1:积分抽奖 )
			mDataMap5.put("draw_type", drawType);
			// 除了实物奖品,其他都是自动发放
			if(draw.getJpType().equals("4497471600470001")) {					
				mDataMap5.put("lj_yn", "N");
			}else {					
				mDataMap5.put("lj_yn", "Y");
			}
			// 抽奖编码
			String jpCodeSeq = WebHelper.upCode("CJJL");
			mDataMap5.put("jp_code_seq", jpCodeSeq);
			DbUp.upTable("lc_huodong_event_jl").dataInsert(mDataMap5);
			
			// 剩余抽奖次数
			result.setSycs(sycs);
			// 奖品编号
			result.setJpCode(draw.getJpCode());
			
			// 奖品信息
			//result.setHuodongEventDzpjpRule(draw);
			// 抽奖编码
			result.setJpCodeSeq(jpCodeSeq);
			// 总积分
			result.setTotalIntegral(totalIntegral);
			// 每次抽奖扣减积分数
			result.setDrawIntegral(drawIntegral);
		}else {
			// 不满足抽奖条件
			result.setResultCode(-1);
			result.setResultMessage("您的账户积分不足，快去赚取更多积分吧!");
			return result;
		}
		
		return result;
	}


    /**
     * 生成奖项
     * @return
     */
    public static HuodongEventDzpjpRule generateAward(List<HuodongEventDzpjpRule> initDrawList) {
        //List<HuodongEventDzpjpRule> initData = initDrawList;
        long result = randomnum(1, 100);
        int line = 0;
        int temp = 0;
        HuodongEventDzpjpRule returnobj = null;
        //int index = 0;
        for (int i = 0; i < initDrawList.size(); i++) {
        	HuodongEventDzpjpRule obj2 = initDrawList.get(i);
            int c = obj2.getJpZjgl();
            temp = temp + c;
            line = 100 - temp;
            if (c != 0) {
                if (result > line && result <= (line + c)) {
                    returnobj = obj2;
                    break;
                }
            }
        }
        return returnobj;
    }

    // 获取2个值之间的随机数
    private static long randomnum(int smin, int smax){
            int range = smax - smin;
            double rand = Math.random();
            return (smin + Math.round(rand * range));
    }


}

