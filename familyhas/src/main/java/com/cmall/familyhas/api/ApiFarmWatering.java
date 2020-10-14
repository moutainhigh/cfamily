package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiFarmWateringInput;
import com.cmall.familyhas.api.result.ApiFarmWateringResult;
import com.cmall.familyhas.service.FarmService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 农场浇水接口
 * @remark 
 * @author 任宏斌
 * @date 2020年2月6日
 */
public class ApiFarmWatering extends RootApiForToken<ApiFarmWateringResult, ApiFarmWateringInput>{

	private static final long TIMEOUT = 3000;
	private FarmService farmService = new FarmService();
	
	@Override
	public ApiFarmWateringResult Process(ApiFarmWateringInput inputParam, MDataMap mRequestMap) {
		ApiFarmWateringResult result = new ApiFarmWateringResult();
		
		String memberCode = getUserCode();
		String eventCode = inputParam.getEventCode();
		String treeCode = inputParam.getTreeCode();
		String byMemberCode = inputParam.getByMemberCode();
		// 验证传入的被浇水人用户编号是否正确
		if(!"".equals(byMemberCode)) {
			Map<String, Object> login_info_yqr = DbUp.upTable("mc_login_info").dataSqlOne("SELECT * FROM mc_login_info WHERE member_code = '"+byMemberCode+"'", new MDataMap());
			if(login_info_yqr == null) {
				result.setResultCode(-1);
				result.setResultMessage("用户信息有误!");
				return result;
			}
		}
		//判断当前是否存在惠惠农场活动
		String nowTime = DateUtil.getSysDateTimeString();
		String sSql1 = "SELECT event_code FROM sc_hudong_event_info "
				+ "WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' "
				+ "AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time DESC LIMIT 1";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(null == eventInfoMap || !eventCode.equals(MapUtils.getString(eventInfoMap, "event_code"))) {
			result.setResultCode(91642600);
			result.setResultMessage(bInfo(91642600));
			return result;
		}
		
		boolean createLogFlag = false;
		
		String treeLockKey = Constants.TREE_PREFIX + treeCode;
		String treeLockCode = "";
		try {
			long begin = System.currentTimeMillis();
			while("".equals(treeLockCode = KvHelper.lockCodes(1, treeLockKey))) {
				if((System.currentTimeMillis() - begin) > TIMEOUT) {
					result.setResultCode(0);
					result.setResultMessage("请求超时");
					return result;
				}
				
				Thread.sleep(1);
			}
			
			//查用户树所处阶段
			String sql1 = "select tree_type,tree_stage,surplus_contribute,member_code FROM sc_huodong_farm_user_tree WHERE tree_code=:tree_code";
			Map<String, Object> userTree = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(sql1, new MDataMap("tree_code", treeCode));
			//未种树
			if(null == userTree) {
				result.setResultCode(91642601);
				result.setResultMessage(bInfo(91642601));
				return result;
			}
			
			//矫正一下是给自己浇水 还是给别人浇水
			if(memberCode.equals(MapUtils.getString(userTree, "member_code"))) { 
				byMemberCode = "";
			}else {
				byMemberCode = MapUtils.getString(userTree, "member_code");
			}
			
			String treeStage = MapUtils.getString(userTree, "tree_stage");
			BigDecimal surplusContribute = BigDecimal.valueOf(MapUtils.getDoubleValue(userTree, "surplus_contribute")).setScale(2, RoundingMode.HALF_UP);
			//果树已经成熟 不能再浇水
			if("449748450005".equals(treeStage)) {
				result.setResultCode(91642602);
				result.setResultMessage(bInfo(91642602));
				return result;
			}
			
			//校验用户水壶水量
			Object kettle_code = DbUp.upTable("sc_huodong_farm_user_kettle")
					.dataGet("kettle_code", "event_code=:event_code and member_code=:member_code", new MDataMap("event_code", eventCode, "member_code", memberCode));
			if(kettle_code == null) {
				result.setResultCode(91642603);
				result.setResultMessage(bInfo(91642603));
				return result;
			}
			
			String kettleCode = kettle_code.toString();
			String waterLockKey = Constants.KETTLE_PREFIX + kettleCode;
			String waterLockCode = "";
			try {
				long begin1 = System.currentTimeMillis();
				while("".equals(waterLockCode = KvHelper.lockCodes(1, waterLockKey))) {
					if((System.currentTimeMillis() - begin1) > TIMEOUT) {
						result.setResultCode(0);
						result.setResultMessage("请求超时");
						return result;
					}
					
					Thread.sleep(1);
				}
				MDataMap kettleInfo = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", eventCode, "member_code", memberCode);
				
				//扣减水壶中的水量
				BigDecimal kettleWater = new BigDecimal(kettleInfo.get("kettle_water")).subtract(BigDecimal.TEN).setScale(0, RoundingMode.HALF_UP);
				if (kettleWater.compareTo(BigDecimal.ZERO) < 0) {
					result.setResultCode(91642603);
					result.setResultMessage(bInfo(91642603));
					return result;
				}
				
				DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(new MDataMap("kettle_water", kettleWater.toString(), "kettle_code", kettleCode), "kettle_water", "kettle_code");
				
				createLogFlag = true;
				result.setKettleWater(kettleWater.intValue());
			} catch (Exception e) {
				e.printStackTrace();
				result.setResultCode(0);
				result.setResultMessage("系统异常");
				return result;
			} finally {
				if(!"".equals(waterLockCode)) KvHelper.unLockCodes(waterLockCode, waterLockKey);
			}
			
			//获取当前阶段每次浇水比例
			BigDecimal waterRatio = farmService.getWaterRatioByStage(treeStage);
			BigDecimal newSurplusContribute = surplusContribute.subtract(waterRatio).setScale(2, RoundingMode.HALF_UP);
			if(newSurplusContribute.compareTo(BigDecimal.ZERO) <= 0) {
				//果树进入下一阶段
				treeStage = farmService.stageExchange(treeStage);
				if("449748450005".equals(treeStage)) {
					newSurplusContribute = BigDecimal.ZERO;
				} else {               
					newSurplusContribute = new BigDecimal(100);
				}
				result.setTreeStage(treeStage);
			}
			
			DbUp.upTable("sc_huodong_farm_user_tree").dataUpdate(new MDataMap("tree_code", treeCode, "tree_stage",
					treeStage, "surplus_contribute", newSurplusContribute.toString()), "tree_stage,surplus_contribute","tree_code");
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("系统异常");
			return result;
		} finally {
			if(!"".equals(treeLockCode)) KvHelper.unLockCodes(treeLockCode, treeLockKey);
		}
		
		//生成日志
		if(createLogFlag) {
			if(StringUtils.isEmpty(byMemberCode)) {
				//自己给自己浇水
				DbUp.upTable("sc_huodong_farm_log").insert(
						"uid", WebHelper.upUuid(),
						"event_code", eventCode,
						"member_code", memberCode,
						"description", bConfig("familyhas.farm_water_me"),
						"create_time", DateUtil.getSysDateTimeString(),
						"water_num", "-10");
			}else{
				//自己给别人浇水
				String nickname = "";
				String otherNickname = "";
				String sql = "SELECT ml.member_code,IFNULL(ms.nickname,CONCAT(SUBSTRING(ml.login_name, 1, 3),'****',SUBSTRING(ml.login_name, 8, 4))) nickname "
						+ "FROM membercenter.mc_login_info ml LEFT JOIN membercenter.mc_member_sync ms ON ml.member_code=ms.member_code "
						+ "WHERE ml.member_code in ('" + memberCode + "','" + byMemberCode + "')";
				List<Map<String, Object>> nickList = DbUp.upTable("mc_login_info").dataSqlList(sql, new MDataMap());
				for (Map<String, Object> nick : nickList) {
					if(memberCode.equals(nick.get("member_code"))) {
						nickname = nick.get("nickname") + "";
					}else {
						otherNickname = nick.get("nickname") + "";
					}
				}
				DbUp.upTable("sc_huodong_farm_log").insert(
						"uid", WebHelper.upUuid(),
						"event_code", eventCode,
						"member_code", memberCode,
						"description", FormatHelper.formatString(bConfig("familyhas.farm_water_other"), otherNickname),
						"create_time", DateUtil.getSysDateTimeString(),
						"other_member_code", byMemberCode,
						"other_nickname", otherNickname,
						"water_num", "-10");
				DbUp.upTable("sc_huodong_farm_log").insert(
						"uid", WebHelper.upUuid(),
						"event_code", eventCode,
						"member_code", byMemberCode,
						"description", FormatHelper.formatString(bConfig("familyhas.farm_other_water"), nickname),
						"create_time", DateUtil.getSysDateTimeString(),
						"other_member_code", memberCode,
						"other_nickname", nickname,
						"water_num", "+10");
			}
		}
			
		return result;
	}
	
}
