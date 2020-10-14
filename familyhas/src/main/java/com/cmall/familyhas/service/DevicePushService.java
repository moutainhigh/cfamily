package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.cmall.familyhas.model.DevicePushModel;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 设备推送service
 * @author fq
 *
 */
public class DevicePushService extends BaseClass {

	/**
	 * 根据手机号获取设备信息
	 * 
	 * @param mobiles
	 *            多个手机号用“,”号拼接
	 * @return 返回对应设备信息
	 */
	public static List<DevicePushModel> getDevicesByMobile(String mobiles) {

		List<DevicePushModel> result = new ArrayList<DevicePushModel>();
		
		mobiles = StringFilter(mobiles);
		
		String sSql = " SELECT mobile,channel_id,device_token,device_type,update_time,bund_status FROM logcenter.lc_push_device_info WHERE bund_status = '1001' AND  mobile IN ("+mobiles+")  " ;
		List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_push_device_info").dataSqlList(sSql, new MDataMap("mobile", mobiles));
		for (Map<String, Object> map : dataSqlList) {
			DevicePushModel model = new DevicePushModel();
			model.setBund_status(String.valueOf(map.get("bund_status")));
			model.setChannel_id(String.valueOf(map.get("channel_id")));
			model.setDevice_type(String.valueOf(map.get("device_type")));
			model.setMobile(String.valueOf(map.get("mobile")));
			model.setBund_status(String.valueOf(map.get("bund_status")));
			model.setUpdate_time(String.valueOf(map.get("update_time")));
			String deviceToken = String.valueOf(map.get("device_token"));
			if(!"".equals(deviceToken)) {
				model.setDevice_token(deviceToken);
				model.setSupportXinGe("0");
			}
			result.add(model);
		}
		
		return result;
	}
	
	/**
	 * 根据条件查询 推送设备信息
	 * @param sParams
	 * @return
	 */
	public static List<MDataMap> queryByWhere (String... sParams) {
		
		List<MDataMap> list = DbUp.upTable("lc_push_device_info").queryByWhere(sParams);
		return list;
		
	}

	/**
	 * 添加设备信息
	 * @param model
	 */
	public static void addDeviceInfo(DevicePushModel model) {
		
		DbUp.upTable("lc_push_device_info").insert("mobile", model.getMobile(),
				"channel_id", model.getChannel_id(), "device_type",
				model.getDevice_type(), "update_time", DateUtil.getNowTime(),"device_token",model.getDevice_token(),"notice_switch",model.getNoticeSwitch(),"app_version",model.getApp_vision()
				);
		
	}
	
	/**
	 * 根据条件更新数据信息
	 * @param model
	 * @param whereFiled 根据字段更改的值
	 */
	public static void updateDeviceByWhere (MDataMap mDataMap,String updateFields,String whereFiled) {
		
		DbUp.upTable("lc_push_device_info").dataUpdate(mDataMap, updateFields, whereFiled);
		
	}
	
	/**
	 * 根据 uid 更改绑定设备的状态
	 * @param uids 多个uid用“,” 号拼接
	 */
	public static void updateStatusByUids (String uids) {
		
		String sSql = " UPDATE logcenter.lc_push_device_info SET bund_status = '1000' WHERE uid IN (:uids) ";
		DbUp.upTable("lc_push_device_info").dataExec(sSql, new MDataMap("uids",uids));
		
	}
	
	/**
	 * 根据条件删除数据
	 * @param sParams
	 */
	public static void delByWhere(String... sParams) {
		
		DbUp.upTable("lc_push_device_info").dataDelete("",  new MDataMap(sParams), "");
		
	}
	
	/**
	 *  字符串过滤
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	private static String StringFilter(String str) throws PatternSyntaxException {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
