package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;

public class FarmService extends BaseClass {

	/**
	 * 根据果树阶段 获取浇水比例
	 * @param treeStage
	 * @return
	 */
	public BigDecimal getWaterRatioByStage(String treeStage) {
		BigDecimal ratio = BigDecimal.ZERO;
		Object getRatio = DbUp.upTable("sc_huodong_farm_config").dataGet("begin_num", "type=:type and stage=:stage",
				new MDataMap("type", "449748480001", "stage", treeStage));
		if (null != getRatio) {
			ratio = new BigDecimal(getRatio.toString()).setScale(2, RoundingMode.HALF_UP);
		}
		return ratio;
	}

	/**
	 * 根据果树当前阶段 获取下一果树阶段
	 * @param treeStage
	 * @return
	 */
	public String stageExchange(String treeStage) {
		String newStage = "";
		
		if(StringUtils.isEmpty(treeStage)) return newStage;
		
		switch (treeStage) {
			case "449748450001":
				newStage = "449748450002";
				break;
			case "449748450002":
				newStage = "449748450003";
				break;
			case "449748450003":
				newStage = "449748450004";
				break;
			case "449748450004":
				newStage = "449748450005";
				break;
			case "449748450005":
				newStage = "449748450005";
				break;
			default:
				break;
		}
		return newStage;
	}

	/**
	 * 初始化用户任务
	 * @param eventCode
	 * @param memberCode
	 */
	public void createTask(String eventCode, String memberCode, String today) {
		String sSql = "select * from sc_huodong_farm_task";
		List<Map<String, Object>> taskConfigList = DbUp.upTable("sc_huodong_farm_task").dataSqlList(sSql, new MDataMap());
		if(null != taskConfigList) {
			for (Map<String, Object> taskConfig : taskConfigList) {
				DbUp.upTable("sc_huodong_farm_user_task").insert(
						"uid", WebHelper.upUuid(),
						"event_code", eventCode,
						"member_code", memberCode,
						"task_code", WebHelper.upCode("FT"),
						"task_type", MapUtils.getString(taskConfig, "task_type"),
						"task_status", "0",
						"task_num", MapUtils.getString(taskConfig, "task_num"),
						"already_num", "0",
						"create_time", today);
			}
		}
	}

	/**
	 * 随机获取今日热卖的一款商品
	 * @return
	 */
	public String getTaskProductCode() {
		boolean success = false; //是否查到商品
		String productCode = "";
		PlusSupportStock plusSupportStock = new PlusSupportStock();
		LoadProductInfo loadProductInfo = new LoadProductInfo();
		
		String day = FormatHelper.upDateTime(DateUtils.addDays(new Date(), -1),"yyyy-MM-dd");
		String sql = "SELECT product_code FROM productcenter.pc_productsales_everyday WHERE day=:day ORDER BY RAND() LIMIT 1";
		
		do {
			Map<String, Object> product = DbUp.upTable("pc_productsales_everyday").dataSqlOne(sql, new MDataMap("day", day));
			productCode = MapUtils.getString(product, "product_code", "");
			if(StringUtils.isNotEmpty(productCode)) {
				if("4497153900060002".equals(loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode)).getProductStatus()) 
						&&  plusSupportStock.upAllStockForProduct(productCode) > 0) { 
					success = true;
				}
			}else {
				success = true;
			}
		}while(!success);
		
		return productCode;
	}

	/**
	 * 随机获取专题连接
	 * @return
	 */
	public String getTaskPageLink() {
		String now = DateUtil.getSysDateTimeString();
		String sql = " SELECT d.showmore_linkvalue FROM familyhas.fh_apphome_column_content d " + 
				" LEFT JOIN familyhas.fh_apphome_column c ON d.column_code=c.column_code " + 
				" LEFT JOIN familyhas.fh_apphome_nav n ON c.nav_code = n.nav_code " + 
				" WHERE n.release_flag='01' AND n.start_time<=:now AND n.end_time>:now AND n.is_delete='02' " + 
				" AND c.release_flag='449746250001' AND c.start_time<=:now AND c.end_time>:now and c.is_delete='449746250002' " + 
				" AND d.start_time<=:now AND d.end_time>:now AND d.is_delete='449746250002' " + 
				" AND d.showmore_linktype='4497471600020001' AND d.showmore_linkvalue LIKE '%/cfamily/web/template%' " + 
				" AND d.showmore_linkvalue NOT LIKE '%sign=%' " + 
				" ORDER BY RAND() LIMIT 1 ";
		Map<String, Object> page = DbUp.upTable("fh_apphome_column_content").dataSqlOne(sql, new MDataMap("now", now));
		return MapUtils.getString(page, "showmore_linkvalue", "");
	}

	/**
	 * 赠送水量到水壶
	 * @param eventCode
	 * @param memberCode
	 * @param waterNum
	 * @return 
	 */
	public RootResult sendWater2Kettle(String eventCode, String memberCode, String waterNum) {
		RootResult result = new RootResult();
		MDataMap kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", eventCode, "member_code", memberCode);
		String kettleCode = kettle.get("kettle_code");
		
		String waterLockKey = Constants.KETTLE_PREFIX + kettleCode;
		String waterLockCode = "";
		try {
			long begin = System.currentTimeMillis();
			while("".equals(waterLockCode = KvHelper.lockCodes(1, waterLockKey))) {
				if((System.currentTimeMillis() - begin) > 3000L) {
					result.setResultCode(0);
					result.setResultMessage("请求超时");
					return result;
				}
				
				Thread.sleep(1);
			}
			
			kettle = DbUp.upTable("sc_huodong_farm_user_kettle").one("event_code", eventCode, "member_code", memberCode);
			
			int newKettleWater = Integer.parseInt(waterNum) + Integer.parseInt(kettle.get("kettle_water"));
			
			DbUp.upTable("sc_huodong_farm_user_kettle").dataUpdate(
					new MDataMap("kettle_code", kettleCode, "kettle_water", newKettleWater + ""), "kettle_water","kettle_code");
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(0);
			result.setResultMessage("系统异常");
			return result;
		} finally {
			if(!"".equals(waterLockCode)) KvHelper.unLockCodes(waterLockCode, waterLockKey);
		}
		
		return result;
	}

	
	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}
	
}
