package com.cmall.familyhas.webfunc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 栏目内容
 * @author ligj
 *
 */
public class FuncAddColumnContent extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String columnCode = mDataMap.get("zw_f_column_code");
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		String position = mDataMap.get("zw_f_position");
		String floorModel = mDataMap.get("zw_f_floor_model");
		
		mDataMap.put("zw_f_showmore_linkvalue", StringUtils.trimToEmpty(mDataMap.get("zw_f_showmore_linkvalue")));
		
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		if(mDataMap.get("zw_f_column_type").equals("4497471600010020")){
//			List<MDataMap> dateList = this.getDateTime(columnCode);
//			startTime = dateList.get(0).get("start_time");
//			endTime = dateList.get(0).get("end_time");
//			mDataMap.put("zw_f_start_time" , startTime);
//			mDataMap.put("zw_f_end_time" , endTime);
		}		
		
		if (endTime.compareTo(startTime) <= 0) {
			//开始时间必须小于结束时间!
			mResult.inErrorMessage(916401201);
			return mResult;
		}else if (endTime.compareTo(createTime) <= 0) {
//			当前时间必须小于结束时间!
			mResult.inErrorMessage(916401214);
			return mResult;
		}else 
			if (0 < this.checkTimeRepeat(columnCode,position,startTime, endTime,floorModel)) {
			//同一位置不允许时间重叠!
			mResult.inErrorMessage(916401215);
			return mResult;
		}
		
		// 清除多余逗号，解决前端js逻辑不完善导致的数据异常
		mDataMap.put("zw_f_showmore_linktype", mDataMap.get("zw_f_showmore_linktype").replace(",", "").trim());
		
		// 商品详情
		if("4497471600020004".equals(mDataMap.get("zw_f_showmore_linktype"))) {
			if(StringUtils.isBlank(mDataMap.get("zw_f_showmore_linkvalue"))) {
				mResult.setResultCode(0);
				mResult.setResultMessage("商品不能为空");
				return mResult;
			}
		}
		
		// 领取优惠券
		if("4497471600020018".equals(mDataMap.get("zw_f_showmore_linktype"))) {
			String cdkey = StringUtils.trimToEmpty(mDataMap.get("zw_f_showmore_linkvalue"));
			if(StringUtils.isBlank(cdkey)) {
				mResult.setResultCode(0);
				mResult.setResultMessage("兑换码不能为空");
				return mResult;
			}
			MDataMap cdkeyMap = DbUp.upTable("oc_coupon_cdkey").oneWhere("activity_code", "", "", "cdkey", cdkey, "manage_code", "SI2003");
			if(cdkeyMap == null) {
				mResult.setResultCode(0);
				mResult.setResultMessage("兑换码不存在");
				return mResult;
			}

			// 检查是否系统发放
			MDataMap acMap = DbUp.upTable("oc_activity").oneWhere("provide_type", "", "", "activity_code", cdkeyMap.get("activity_code"));
			if(!"4497471600060002".equals(acMap.get("provide_type"))) {
				mResult.setResultCode(0);
				mResult.setResultMessage("仅支持系统发放优惠券活动");
				return mResult;
			}
		}
		
		if(mDataMap.get("zw_f_column_type").equals("4497471600010017")||mDataMap.get("zw_f_column_type").equals("4497471600010031")){  //【视频播放列表】相关和一栏多行通用模板
			if(StringUtils.isBlank(mDataMap.get("zw_f_showmore_linkvalue"))){
				mResult.inErrorMessage(916423220);  // 【视频播放列表】url 不可为空 
				return mResult;
			}
			if(StringUtils.isBlank(mDataMap.get("zw_f_product_name"))){
				mResult.inErrorMessage(916423221);  // 【视频播放列表】商品名称不可为空
				return mResult;
			}
			if(StringUtils.isBlank(mDataMap.get("zw_f_product_price"))){
				mResult.inErrorMessage(916423222);  // 【视频播放列表】商品价格 不可为空 
				return mResult;
			}
		}else if(mDataMap.get("zw_f_column_type").equals("4497471600010020")){ //【视频播放模板】相关
			if(StringUtils.isBlank(mDataMap.get("zw_f_product_name"))){
				mResult.inErrorMessage(916423221);  // 【视频播放模板】商品名称不可为空
				return mResult;
			}
			if(StringUtils.isBlank(mDataMap.get("zw_f_product_price"))){
				mResult.inErrorMessage(916423222);  // 【视频播放模板】商品价格 不可为空 
				return mResult;
			}
			// 5.6.1改版 广告语修改为非必填
			//if(StringUtils.isBlank(mDataMap.get("zw_f_video_ad"))){
			//	mResult.inErrorMessage(916423224);  // 【视频播放模板】视频广告语不可为空
			//	return mResult;
			//}
			if(StringUtils.isBlank(mDataMap.get("zw_f_video_link"))){
				mResult.inErrorMessage(916423225);  // 【视频播放模板】封面商品视频链接不可为空 
				return mResult;
			}
		}else{
			mDataMap.put("zw_f_product_price" , "0.0");  
		}
		
		if (StringUtils.isNotEmpty(floorModel)){
			//AD1同一时间段最多加六条，AD2同一时间段只能添加一条，AD3同一时间段可添加三条，品牌同一时间最多六个
			List<MDataMap> mapList = getContent(columnCode,startTime, endTime,floorModel);
			if(mapList != null && mapList.size()>0){
				//底部广告位
				if(mapList.size()>=3 && "4497471600220003".equals(floorModel)){
					//AD3同一时间段可添加三条
					mResult.inErrorMessage(913421185);
					return mResult;
				//中间轮播广告位
				}else if(mapList.size()>=6 && "4497471600220002".equals(floorModel)){
					//AD1同一时间段最多加六条
					mResult.inErrorMessage(916421183);
					return mResult;
				//左侧底部品牌位
				}else if(mapList.size()>=6 && "4497471600220004".equals(floorModel)){
					//品牌同一时间最多六个
					mResult.inErrorMessage(913421186);
					return mResult;
				//左侧广告位
				}else if(mapList.size()>=1 && "4497471600220001".equals(floorModel)){
					//AD2同一时间段只能添加一条
					mResult.inErrorMessage(913421184);
					return mResult;
				}
			}
		}
		
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_create_time", createTime);
			mDataMap.put("zw_f_create_user", create_user);
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", create_user);
			
			mDataMap.put("zw_f_column_code", columnCode);
			mDataMap.put("zw_f_is_delete", "449746250002");
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

	private int checkTimeRepeat(String columnCode,String position,String startTime,String endTime,String floorModel){
		String sWhere =" column_code='"+columnCode+"' and position='"+position+"' and (('"+startTime+"' between start_time and end_time) or ('"+endTime+"' between start_time and end_time) or (start_time between '"+startTime+"' and '"+endTime+"') or (end_time between '"+startTime+"' and '"+endTime+"')) and is_delete='449746250002'";
		if (StringUtils.isNotEmpty(floorModel)){
			sWhere =sWhere+" and floor_model='"+floorModel+"'";
		}
		MDataMap map = DbUp.upTable("fh_apphome_column_content").oneWhere("uid", "", sWhere);
		if (null == map || map.isEmpty()) {
			return 0;
		}
		return 1;
	}
	
	private List<MDataMap> getContent(String columnCode,String startTime,String endTime,String floorModel){
		String sWhere =" column_code='"+columnCode+"' and (('"+startTime+"' between start_time and end_time) or ('"+endTime+"' between start_time and end_time)) and is_delete='449746250002'";
		
			sWhere =sWhere+" and floor_model='"+floorModel+"'";
		
		List<MDataMap> mapList = DbUp.upTable("fh_apphome_column_content").queryAll("uid", null, sWhere,null);
		return mapList;
	}
	
	private List<MDataMap> getDateTime(String columnCode){
		String sWhere =" column_code='"+columnCode+"'";
		
		List<MDataMap> mapList = DbUp.upTable("fh_apphome_column").queryAll("uid,start_time,end_time", null, sWhere,null);
		return mapList;
	}
}
