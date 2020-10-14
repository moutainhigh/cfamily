package com.cmall.familyhas.app;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 惠家友app首页栏目维护---修改
 * 
 * @author fengls
 * 
 */

public class FucEditForAppHome extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		MDataMap mInsertMap = new MDataMap();

		recheckMapField(mResult, mPage, mAddMaps);
		

		// 定义组件判断标记
		boolean bFlagComponent = false;

		if (mResult.upFlagTrue()) {

			// 循环所有结构
			for (MWebField mField : mPage.getPageFields()) {
				
				// app_tag_flag字段的校验，1:是默认的
				if (mField.getColumnName().equals("app_tag_flag")) {

					String app_tag_flag = mAddMaps.get(mField
							.getFieldName());
					if ("449747000001".equals(app_tag_flag)) {
						String sql = "update fh_apphome_tag set app_tag_flag = 449747000002 where app_tag_flag= 449747000001";
						DbUp.upTable("fh_apphome_tag").dataExec(sql, null);
					} else {
						Object tag_flag = DbUp
								.upTable("fh_apphome_tag")
								.dataGet(
										"min(CONVERT(app_tag_flag,signed))",
										null, null);
						if (tag_flag == null
								|| !"449747000001".equals(tag_flag + "")) {
							mInsertMap.put("app_tag_flag", "449747000001");
						}
					}

				}
				
				// tag_sort字段校验。为数字，并且自动增加。从1开始,并且不能和其他的值重复。
				if (mField.getColumnName().equals("tag_sort")) {

					String tag_sort = mAddMaps.get(mField.getFieldName());
					// 根据app_tag_name校验表中是否有数据存在
					if (!isNumeric1(tag_sort)) {
						mResult.inErrorMessage(916401141);
					}else{
						int count = DbUp.upTable("fh_apphome_tag").dataCount("tag_sort="+tag_sort,  new MDataMap());
					
						if(count>1){
							mResult.inErrorMessage(916401142);
							
						}if(count ==1){
						
							String tag_sort2 =  (String) DbUp.upTable("fh_apphome_tag").dataGet("tag_sort", "uid=\'"+mDataMap.get("zw_f_uid")+"\'", new MDataMap());
							 if(!tag_sort2.equals(mAddMaps.get("tag_sort"))){
								 mResult.inErrorMessage(916401142);
							 }
							
						}
						
					}
				}

				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}

				if (mAddMaps.containsKey(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getColumnName());

					mInsertMap.put(mField.getColumnName(), sValue);
				} else if (mField.getFieldTypeAid().equals("104005103")) {
					// 特殊判断修改时如果没有传值 则自动赋空
					mInsertMap.put(mField.getColumnName(), "");
				}

			}
		}

		// 创建时间为当年系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式

		mInsertMap.put("update_time", df.format(new Date())); // new
																// Date()为获取当前系统时间

		if (UserFactory.INSTANCE.create().getLoginName() == null
				|| UserFactory.INSTANCE.create().getLoginName().equals("")) {
			mResult.inErrorMessage(941901073);
		} else {
			// mInsertMap.put("create_user",
			// UserFactory.INSTANCE.create().getLoginName()); //获取当前登录名
			mInsertMap.put("update_user", UserFactory.INSTANCE.create()
					.getLoginName()); // 获取当前登录名
		}

		if (mResult.upFlagTrue()) {
			DbUp.upTable(mPage.getPageTable())
					.dataUpdate(mInsertMap, "", "uid");

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inEdit(
								mField, mDataMap);

					}
				}

			}

		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

	/**
	 * 校验表中是否有数据存在
	 * 
	 * @param skin_type
	 *            uuid
	 * @return
	 */
	private boolean checkInfo(String app_tag_name, String uuid) {

		int atCount = DbUp.upTable("fh_apphome_tag").count("app_tag_name",
				app_tag_name);
		if (atCount == 1) {
			String uid = getBoutidByUid(app_tag_name);
			// 没做修改不提示添加重复
			if (uid.equals(uuid)) {
				return false;
			} else {
				return true;
			}
		}
		if (atCount > 1) {
			return true;
		}
		return false;
	}

	public String getBoutidByUid(String app_tag_name) {
		MDataMap AdvertiseGenreData = DbUp.upTable("fh_apphome_tag").one(
				"app_tag_name", app_tag_name);
		return AdvertiseGenreData.get("uid");
	}

	/**
	 * 判断一个string是否为数字。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric1(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	

	
}
