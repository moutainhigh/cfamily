package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.service.ApiForUpdateColumnContentService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ContentColumnFuncEdit extends FuncEdit {
	
	// 支持商品维护的栏目类型
	static String[] PRODUCT_MAINTENANCE_TYPES = {"4497471600010008","4497471600010013","4497471600010014","4497471600010025"};
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String columnName = mDataMap.get("zw_f_column_name");
		String uid = mDataMap.get("zw_f_uid");
		String columnType = mDataMap.get("zw_f_column_type");
		String seller_code = "SI2003";
		MDataMap mdata = new MDataMap();
		
		mdata.put("uid", uid);
		mdata.put("columnName", columnName);
		mdata.put("deleteFlag", "449746250002");
		mdata.put("seller_code", "SI2003");
		mdata.put("view_type", mDataMap.get("zw_f_view_type"));
		
		String navCode = DbUp.upTable("fh_apphome_column").one("uid",uid).get("nav_code");
		mdata.put("nav_code", navCode);
		
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String table = mPage.getPageTable();
		/*查询栏目名称数*/
		int count = 0;
		if(!columnName.equals(mDataMap.get("zw_f_column_name_old"))){
			count = DbUp.upTable(table).dataCount("column_name=:columnName and is_delete=:deleteFlag and uid<>:uid and view_type=:view_type and seller_code=:seller_code and nav_code=:nav_code", mdata);
		}
		
		/*判断栏目名称是否唯一是否唯一*/
		
		if (count > 0) {
			mResult.inErrorMessage(916401213);
			return mResult;
		}
		String starTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if(StringUtils.isBlank(starTime) || StringUtils.isBlank(endTime)){
			mResult.inErrorMessage(916423223);  // 开始时间和结束时间不得为空 
			return mResult;
		}
		if (!DateUtil.getTimefag(endTime, starTime)) {
			mResult.inErrorMessage(916401201);// 开始时间必须小于结束时间
			return mResult;
		} else if (!DateUtil.getTimefag(endTime, DateUtil.getNowTime())) {
			mResult.inErrorMessage(916401214);// 当前时间必须小于结束时间
			return mResult;
		}
		//确保通知模板记录唯一
		if("4497471600010012".equals(columnType)){
			if (0 == this.checkTimeRepeat(uid,columnType,starTime,endTime,mDataMap.get("zw_f_view_type"),seller_code,navCode)) {
				mResult.inErrorMessage(916422123);
				return mResult;
			}
		}
		Map<String, Object> dataSqlOne = DbUp.upTable("fh_apphome_column").dataSqlOne("SELECT * FROM fh_apphome_column WHERE uid = '"+uid+"'", new MDataMap());
		if(dataSqlOne != null && dataSqlOne.size() > 0) {
			// 同一导航下，同一时间段只能存在一个商品评价模板/买家秀列表模板/买家秀入口模板
			if("4497471600010027".equals(columnType) || "4497471600010029".equals(columnType) || "4497471600010030".equals(columnType)){
				String sql1 = "SELECT * FROM fh_apphome_column WHERE nav_code = '"+dataSqlOne.get("nav_code")+"' AND column_type = '"+columnType+"' "
						+ "AND is_delete = '449746250002' AND uid != '"+uid+"' AND start_time < '"+endTime+"' AND end_time > '"+starTime+"'";
				List<Map<String,Object>> list1 = DbUp.upTable("fh_apphome_column").dataSqlList(sql1, new MDataMap());
				MDataMap sc_define = DbUp.upTable("sc_define").one("define_code",columnType);
				if(list1.size() > 0) {
					mResult.inErrorMessage(916423300);
					mResult.setResultCode(-1);
					mResult.setResultMessage("同一导航下，同一时间段只能存在一个"+MapUtils.getString(sc_define, "define_name")+"模板");
					return mResult;
				}
			}
		}
		//导航url栏目，配置连接字段添加
		if("4497471600010019".equals(columnType)){
			String lv = mDataMap.get("zw_f_showmore_linkvalue");
			if(StringUtils.isNotBlank(lv)&&!lv.contains("webLocation")) {
				if(lv.indexOf("?")<0) {
					lv = lv+"?";
				}else {
					lv = lv+"&";
				}
				lv = lv+"webLocation=10001";
				mDataMap.put("zw_f_showmore_linkvalue", lv);
			}
		}
		//三栏两行，两栏两行的操作时间录入
		if("4497471600010013".equals(columnType)||"4497471600010014".equals(columnType)){
			//获取操作日时间 格式：yyyy-MM-dd
			String nowDate = DateUtil.getSysDateString();
			mDataMap.put("zw_f_operate_time", nowDate);
		}
		
		// 商品维护限制信息
		if(ArrayUtils.contains(PRODUCT_MAINTENANCE_TYPES, columnType)){
			String zw_f_product_maintenance = mDataMap.get("zw_f_product_maintenance");
			
			if("44975017002".equals(zw_f_product_maintenance)) {
				// 自动选品
				if(StringUtils.isBlank(mDataMap.get("zw_f_rule_code"))) {
					mResult.setResultCode(-1);
					mResult.setResultMessage("请您填写规则编号");
					return mResult;
				}
			} else if("44975017003".equals(zw_f_product_maintenance)) {
				//智能推荐
				String zw_f_category_limit = mDataMap.get("zw_f_category_limit");
				String zw_f_category_codes = mDataMap.get("zw_f_category_codes");
				if("449748560002".equals(zw_f_category_limit) && StringUtils.isBlank(zw_f_category_codes)) {
					mResult.setResultCode(-1);
					mResult.setResultMessage("请您选择分类");
					return mResult;
				}
			} else {
				mDataMap.put("zw_f_rule_code", "");
				mDataMap.put("zw_f_category_limit", "");
				mDataMap.put("zw_f_category_codes", "");
			}
		}else {
			mDataMap.put("zw_f_product_maintenance","44975017001");
			mDataMap.put("zw_f_rule_code", "");
			mDataMap.put("zw_f_category_limit", "");
			mDataMap.put("zw_f_category_codes", "");
		}
		
		String showmore = mDataMap.get("zw_f_is_showmore");
		if ("449746250002".equals(showmore)) {// 不显示更多
			mDataMap.put("zw_f_showmore_title", "");
			mDataMap.put("zw_f_showmore_linktype", "");
			mDataMap.put("zw_f_showmore_linkvalue", "");
		}
		
		//添加栏目标题颜色
		if(StringUtils.isNotBlank(mDataMap.get("zw_f_column_name_corlor"))&&!mDataMap.get("zw_f_column_name_corlor").contains("#")) {
			mDataMap.put("zw_f_column_name_corlor", "#"+mDataMap.get("zw_f_column_name_corlor"));
		}
		/* 系统更新时间 */
		String update_time = DateUtil.getNowTime();
		/* 获取当前修改人 */
		String update_user = UserFactory.INSTANCE.create().getLoginName();

		mDataMap.put("update_time", update_time);
		mDataMap.put("update_user", update_user);
		ExecutorService fixedThreadPool = null;
		try {
			if (mResult.upFlagTrue()&&StringUtils.isNotBlank(columnType)) {
				MDataMap one = DbUp.upTable("fh_apphome_column").one("uid",mDataMap.get("zw_f_uid").toString());
				if(ArrayUtils.contains(PRODUCT_MAINTENANCE_TYPES, columnType)) {
					if("44975017002".equals(mDataMap.get("zw_f_product_maintenance"))) {
						String oldRuleCode = one.get("rule_code");
						if(!mDataMap.get("zw_f_rule_code").equals(oldRuleCode)) {
							fixedThreadPool = Executors.newFixedThreadPool(1);
							fixedThreadPool.execute(new ApiForUpdateColumnContentService(one.get("column_code").toString(),mDataMap.get("zw_f_rule_code")));
						}
					} else {
						DbUp.upTable("fh_apphome_column_content").delete("column_code",one.get("column_code").toString());
					}
				}
			}
			mResult = super.funcDo(sOperateUid, mDataMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}finally {
			/**
			 * 关闭线程池
			 */
			if (fixedThreadPool != null) {
				fixedThreadPool.shutdown();
			}
		}
		return mResult;
	}
	
	private int checkTimeRepeat(String uid,String columnType,String startTime,String endTime,String viewType,String sellerCode,String navCode){
		StringBuffer sWhere =new StringBuffer();
		sWhere.append(" column_type='").append(columnType).append("'");
		sWhere.append(" and uid != '").append(uid).append("'");
		sWhere.append(" and (('").append(startTime).append("' between start_time and end_time)");
		sWhere.append( "or ('").append(endTime).append("' between start_time and end_time)) ");
		sWhere.append(" and is_delete='449746250002'");
		sWhere.append(" and view_type='").append(viewType).append("'");
		sWhere.append(" and seller_code='").append(sellerCode).append("'");
		sWhere.append(" and nav_code='").append(navCode).append("'");
		List<MDataMap> mapList = DbUp.upTable("fh_apphome_column").queryAll("uid", null, sWhere.toString(),null);
		if(mapList != null && mapList.size()>0){
			return 0;
		}
		return 1;
	}
}
