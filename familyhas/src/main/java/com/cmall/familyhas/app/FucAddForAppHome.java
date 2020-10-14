package com.cmall.familyhas.app;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
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
 * 惠家友app首页栏目维护---增加
 * 
 * @author fengls
 * 
 */
public class FucAddForAppHome extends RootFunc {

	public static final String APP_CODE = "SI2003";

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();
		// 定义组件判断标记
		boolean bFlagComponent = false;

		recheckMapField(mResult, mPage, mAddMaps);

		if (mResult.upFlagTrue()) {

			for (MWebField mField : mPage.getPageFields()) {
				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}
				if (mAddMaps.containsKey(mField.getFieldName())
						&& StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
					// app_tag_name字段的校验
					if (mField.getColumnName().equals("app_tag_name")) {

						String app_tag_name = mAddMaps.get(mField
								.getFieldName());

						// 根据app_tag_name校验表中是否有数据存在
						if (checkInfo(app_tag_name)) {
							mResult.setResultCode(916401140);
							mResult.setResultMessage(bInfo(916401140));
							return mResult;
						}
					}

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

				}
			}

		}

		// tag_sort字段校验。为数字，并且自动增加。从1开始
		Object max_sort = DbUp.upTable("fh_apphome_tag").dataGet(
				"max(CONVERT(tag_sort,signed))", null, null);
		int max_tag_sort = 1;
		if (null != max_sort) {
			max_tag_sort = Integer.parseInt(String.valueOf(max_sort)) + 1;
		}
		mInsertMap.put("tag_sort", max_tag_sort + "");

		// 设置app_code的值。
		mInsertMap.put("app_code", APP_CODE);
		// 创建时间为当年系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
		mInsertMap.put("create_time", df.format(new Date())); // new
																// Date()为获取当前系统时间
		mInsertMap.put("update_time", df.format(new Date())); // new
																// Date()为获取当前系统时间

		if (UserFactory.INSTANCE.create().getLoginName() == null
				|| UserFactory.INSTANCE.create().getLoginName().equals("")) {
			mResult.inErrorMessage(941901073);
		} else {
			mInsertMap.put("create_user", UserFactory.INSTANCE.create()
					.getLoginName()); // 获取当前登录名
			mInsertMap.put("update_user", UserFactory.INSTANCE.create()
					.getLoginName()); // 获取当前登录名
		}

		if (mResult.upFlagTrue()) {

			DbUp.upTable(mPage.getPageTable()).dataInsert(mInsertMap);

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {

					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inAdd(mField,
								mDataMap);
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
	 * @param app_tag_name
	 * @return
	 */
	private boolean checkInfo(String app_tag_name) {
		int atCount = DbUp.upTable("fh_apphome_tag").count("app_tag_name",
				app_tag_name);
		if (atCount >= 1) {
			return true;
		}
		return false;
	}

}
