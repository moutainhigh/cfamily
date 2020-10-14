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
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ContentColumnFuncAdd extends FuncAdd {
	
	// 支持商品维护的栏目类型
	static String[] PRODUCT_MAINTENANCE_TYPES = {"4497471600010008","4497471600010013","4497471600010014","4497471600010025"};
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String seller_code="SI2003";
		String columnName = mDataMap.get("zw_f_column_name");
		String columnType = mDataMap.get("zw_f_column_type");
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String table = mPage.getPageTable();
		String navCode = StringUtils.isBlank(mDataMap.get("zw_f_nav_code")) ? "" : mDataMap.get("zw_f_nav_code");
		
		//导航URL栏目名称默认为"导航栏URL",自动生成，重复
		if(!columnType.equals("4497471600010019")) {
			//确保栏目名称唯一
			int count = DbUp.upTable(table).count("column_name", columnName,    
					"is_delete", "449746250002",
					"view_type",mDataMap.get("zw_f_view_type"),
					"seller_code",seller_code,
					"nav_code",navCode);
			if (count > 0) {
				mResult.setResultCode(0);
				mResult.setResultMessage("栏目名称在此导航下已经存在");
				return mResult;
			}
		} else {
			//导航栏URL下只能有一个栏目'4497471600010019',检查是否有其他栏目数据在当前导航栏下
			String sql = "select zid from fh_apphome_column where nav_code='"+ navCode + "' and is_delete !='449746250001'";
			List<Map<String, Object>> list = DbUp.upTable(table).dataSqlList(sql, new MDataMap());
			if(list.size() > 0) {
				mResult.inErrorMessage(916423300);
				return mResult;
			}else {
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
		}
		
		
		//导航栏URL下只能有一个栏目'4497471600010019',检查是否有其他栏目数据在当前导航栏下
		String sql = "select * from fh_apphome_nav v, fh_apphome_column c where v.nav_code=c.nav_code and c.column_type='4497471600010019' and v.nav_code=:nav_code and c.is_delete!='449746250001'";
		List<Map<String,Object>> list = DbUp.upTable("fh_apphome_nav").dataSqlList(sql, new MDataMap("nav_code", navCode));
		if(list.size() > 0) {
			mResult.inErrorMessage(916423300);
			return mResult;
		}

		String starTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		if(StringUtils.isBlank(starTime) || StringUtils.isBlank(endTime)){
			mResult.setResultMessage("开始时间或结束时间不得为空");  // 开始时间或结束时间不得为空
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
			if (0 == this.checkTimeRepeat(columnType,starTime,endTime,mDataMap.get("zw_f_view_type"),seller_code,StringUtils.trimToEmpty(mDataMap.get("zw_f_nav_code")))) {
				mResult.inErrorMessage(916422123);
				return mResult;
			}
		}
		// 同一导航下，同一时间段只能存在一个商品评价模板/买家秀列表模板/买家秀入口模板
		if("4497471600010027".equals(columnType) || "4497471600010029".equals(columnType) || "4497471600010030".equals(columnType)){
			String sql1 = "SELECT * FROM fh_apphome_column WHERE nav_code = '"+navCode+"' AND column_type = '"+columnType+"' "
					+ "AND is_delete = '449746250002' AND start_time < '"+endTime+"' AND end_time > '"+starTime+"'";
			List<Map<String,Object>> list1 = DbUp.upTable("fh_apphome_column").dataSqlList(sql1, new MDataMap());
			MDataMap sc_define = DbUp.upTable("sc_define").one("define_code",columnType);
			if(list1.size() > 0) {
				mResult.inErrorMessage(916423300);
				mResult.setResultCode(-1);
				mResult.setResultMessage("同一导航下，同一时间段只能存在一个"+MapUtils.getString(sc_define, "define_name")+"模板");
				return mResult;
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
		String create_time = DateUtil.getNowTime();// 系统当前时间
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String cCode = WebHelper.upCode("COL");
		mDataMap.put("zw_f_seller_code", seller_code);
		mDataMap.put("zw_f_create_time", create_time);
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_update_time", create_time);
		mDataMap.put("zw_f_update_user", create_user);
		mDataMap.put("zw_f_column_code", cCode);
		mDataMap.put("zw_f_is_delete", "449746250002");// 未删除
		//添加栏目标题颜色
		if(StringUtils.isNotBlank(mDataMap.get("zw_f_column_name_corlor"))&&!mDataMap.get("zw_f_column_name_corlor").contains("#")) {
			mDataMap.put("zw_f_column_name_corlor", "#"+mDataMap.get("zw_f_column_name_corlor"));
		}
		ExecutorService fixedThreadPool = null;
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
				//自动维护时,手动调用定时一次，防止数据太大，开新线程处理
				fixedThreadPool = Executors.newFixedThreadPool(1);
				if(ArrayUtils.contains(PRODUCT_MAINTENANCE_TYPES, columnType)
						&& "44975017002".equals(mDataMap.get("zw_f_product_maintenance"))){
					fixedThreadPool.execute(new ApiForUpdateColumnContentService(cCode,mDataMap.get("zw_f_rule_code")));
				}
			}
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
	

	private int checkTimeRepeat(String columnType,String startTime,String endTime,String viewType,String sellerCode,String navCode){
		StringBuffer sWhere =new StringBuffer();
		sWhere.append(" column_type='").append(columnType).append("'");
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
