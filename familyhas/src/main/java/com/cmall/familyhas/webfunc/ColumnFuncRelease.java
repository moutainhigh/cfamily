package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: ColumnFuncRelease
 * @Description: 栏目发布管理
 * @author 张海生
 * @date 2015-3-19 下午6:22:35
 * 
 */
public class ColumnFuncRelease extends RootFunc {

	/**
	 * @Description:栏目发布于取消发布
	 * @param @param sOperateUid
	 * @param @param mDataMap
	 * @author 张海生
	 * @date 2015-3-19 下午6:22:15
	 * @throws
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		// TODO Auto-generated method stub
		MWebResult mResult = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		
		MDataMap mdata = DbUp.upTable("fh_apphome_column").one("uid", uid);
		String mainTable = "fh_apphome_column";
		if(mdata == null){
			mainTable = "fh_pctv_column";
			mdata = DbUp.upTable("fh_pctv_column").one("uid", uid);
		}
		
		String releaseFlag = mdata.get("release_flag");
		String columnCode = mdata.get("column_code");
		MDataMap dataMap = new MDataMap();
		dataMap.put("uid", uid);
		/* 系统更新时间 */
		String update_time = DateUtil.getNowTime();
		/* 获取当前修改人 */
		String update_user = UserFactory.INSTANCE.create().getLoginName();
		if ("449746250001".equals(releaseFlag)) {// 已发布就设为未发布
			dataMap.put("release_flag", "449746250002");
			dataMap.put("update_time", update_time);
			dataMap.put("update_user", update_user);
			DbUp.upTable(mainTable).dataUpdate(dataMap, "release_flag,update_time,update_user", "uid");
		} else if ("449746250002".equals(releaseFlag)) {// 未发布就设为已发布
			//确保栏目名称唯一
			String columnType = mdata.get("column_type"); // 栏目类型
			//栏目类型为导航栏URL跳过名称校验.这种类型栏目所有名称都为'导航栏URL'
			if(!columnType.equals("4497471600010019")) {
				String sql = "select  * from familyhas.fh_apphome_column "
						+ "where " 
						+ " column_name = '" + mdata.get("column_name") + "' and is_delete = '449746250002' and nav_code='"+mdata.get("nav_code")+"'";
				List<Map<String, Object>> list = DbUp.upTable(mainTable).dataSqlList(sql, null);
				int columnCount = list.size();
				if (columnCount >= 2) { 
					String navCode = mdata.get("nav_code");
					MDataMap one = DbUp.upTable("fh_apphome_nav").one("nav_code", navCode);
					if(one != null){
						String nav_type = one.get("nav_type");
						sql = "select  * from familyhas.fh_apphome_nav where nav_code in(";
						String in_ = "";
						for(Map<String, Object> m : list){
							in_ += "'" + m.get("nav_code") + "',";
						}
						in_ = in_.substring(0, in_.length()-1);
						sql += in_ + ") and nav_type = '" + nav_type + "' and is_delete = '02' ";
						List<Map<String, Object>> list_ = DbUp.upTable(mainTable).dataSqlList(sql, null);
						if(list_.size() > 1){
							mResult.inErrorMessage(916401213);
							return mResult;
						}
					}
				}
			}
			if(columnType.equals("4497471600010011")){
				int count = DbUp.upTable(mainTable).dataCount("release_flag = '449746250001' and column_type = '4497471600010026' and view_type = '"+mdata.get("view_type")+"' and nav_code = '"+mdata.get("nav_code")+"' and '"+mdata.get("start_time")+"' <= end_time and start_time <= '"+mdata.get("end_time")+"'", null);
				if(count>0){
					mResult.setResultCode(-1);
					mResult.setResultMessage("闪购和闪购横滑模板同一时间只能发布一种。");
					return mResult;
				}
			}
			if(columnType.equals("4497471600010026")){
				int count = DbUp.upTable(mainTable).dataCount("release_flag = '449746250001' and column_type = '4497471600010011' and view_type = '"+mdata.get("view_type")+"' and nav_code = '"+mdata.get("nav_code")+"' and '"+mdata.get("start_time")+"' <= end_time and start_time <= '"+mdata.get("end_time")+"'", null);
				if(count>0){
					mResult.setResultCode(-1);
					mResult.setResultMessage("闪购和闪购横滑模板同一时间只能发布一种。");
					return mResult;
				}
			}
			// 558 同一导航下，同一时间段只能存在一个商品评价模板/买家秀列表模板
			if(columnType.equals("4497471600010027") || "4497471600010029".equals(columnType)){
				int count = DbUp.upTable(mainTable).dataCount("release_flag = '449746250001' and column_type = '"+columnType+"' and view_type = '"+mdata.get("view_type")+"' and '"+mdata.get("start_time")+"' < end_time and start_time < '"+mdata.get("end_time")+"'", null);
				MDataMap sc_define = DbUp.upTable("sc_define").one("define_code",columnType);
				if(count>0){
					mResult.setResultCode(-1);
					mResult.setResultMessage(MapUtils.getString(sc_define, "define_name")+"模板同一时间只能发布一种。");
					return mResult;
				}
			}

			if ("4497471600010022".equals(columnType) || "4497471600010010".equals(columnType) || "4497471600010011".equals(columnType) || "4497471600010016".equals(columnType) || "4497471600010026".equals(columnType) || "4497471600010028".equals(columnType) || "4497471600010029".equals(columnType) || "4497471600010030".equals(columnType) || "4497471600010036".equals(columnType) || "4497471600010037".equals(columnType)) {//栏目类型为TV直播或闪购时不需要维护栏目内容
				dataMap.put("release_flag", "449746250001");
				dataMap.put("update_time", update_time);
				dataMap.put("update_user", update_user);
				DbUp.upTable(mainTable).dataUpdate(dataMap, "release_flag,update_time,update_user", "uid");
			}else if("4497471600010027".equals(columnType)) { // 商品评价模板下必须要有维护的评价商品
				String countSql = "SELECT count(1) num FROM fh_apphome_evaluation WHERE is_delete = '0' AND location_num > 0 ";
				Map<String, Object> countMap = DbUp.upTable("fh_apphome_evaluation").dataSqlOne(countSql, new MDataMap());
				if(countMap != null) {
					int num = MapUtils.getIntValue(countMap, "num");
					if(num > 0) {
						dataMap.put("release_flag", "449746250001");
						dataMap.put("update_time", update_time);
						dataMap.put("update_user", update_user);
						DbUp.upTable(mainTable).dataUpdate(dataMap, "release_flag,update_time,update_user", "uid");
					}else {
						mResult.setResultCode(-1);
						mResult.setResultMessage("商品评价模板维护商品不能为空");
						return mResult;
					}
				}else {
					mResult.setResultCode(-1);
					mResult.setResultMessage("商品评价模板维护商品不能为空");
					return mResult;
				}
			} else {
				String prod_recommend = mdata.get("product_maintenance");
				// 561添加:一栏多行栏目类型 并且是 智能推荐 时,没有维护栏目内容也可以发布
				int count = DbUp.upTable("fh_apphome_column_content").count( "column_code", columnCode, "is_delete", "449746250002");
				if (count > 0 || columnType.equals("4497471600010034") || columnType.equals("4497471600010019") || columnType.equals("4497471600010023")|| columnType.equals("4497471600010024") || "44975017003".equals(prod_recommend) ) {
					//栏目类型为导航栏URL,没有栏目内容
					dataMap.put("release_flag", "449746250001");
					dataMap.put("update_time", update_time);
					dataMap.put("update_user", update_user);
					DbUp.upTable(mainTable).dataUpdate(dataMap,
							"release_flag,update_time,update_user", "uid");
				} else {
					mResult.inErrorMessage(916401217);// 栏目下必须有栏目内容才可以发布
				}
			}
		}
		return mResult;
	}

}
