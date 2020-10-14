package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebSource;
import com.srnpr.zapweb.webmodel.MWebView;
import com.srnpr.zapweb.webpage.ControlPage;
import com.srnpr.zapweb.webpage.RootExec;

/**
 * 
 * @author zhy
 * @description 订单查询重组查询语句，主要针对于发货商类型
 */
public class SearchOrderService extends RootExec {

	public MPageData upChartData(ControlPage page) {
		MWebPage webPage = page.getWebPage();
		MDataMap mReqMap = page.getReqMap();
		MDataMap mOptionMap = new MDataMap();

		// 返回参数
		MPageData mReturnData = new MPageData();

		String sSortString = "-zid";

		// 查询条件
		String sWhere = "";

		MDataMap mPaginationMap = new MDataMap();

		if (StringUtils.isNotEmpty(webPage.getDataScope())) {

			MDataMap mScopeMap = new MDataMap();
			mScopeMap.inAllValues(FormatHelper.upUrlStrings(WebHelper.recheckReplace(webPage.getDataScope(), mReqMap)));
			mPaginationMap = mScopeMap.upSubMap(WebConst.CONST_WEB_PAGINATION_NAME);

		}

		/********** 开始处理分页输入参数逻辑 ********************************/
		{
			MDataMap mReqpageMap = mReqMap.upSubMap(WebConst.CONST_WEB_PAGINATION_NAME);

			if (mReqpageMap != null && mReqpageMap.size() > 0) {
				for (String sKey : mReqpageMap.keySet()) {
					mPaginationMap.put(sKey, mReqpageMap.get(sKey));
				}

			}

			if (mPaginationMap != null && mPaginationMap.size() > 0) {

				// 判断页数总计
				if (mPaginationMap.containsKey("count")) {
					mReturnData.setPageCount(Integer.valueOf(mPaginationMap.get("count")));
				}
				// 判断当前页
				if (mPaginationMap.containsKey("index")) {
					mReturnData.setPageIndex(Integer.valueOf(mPaginationMap.get("index")));
				}
				// 判断总页数
				if (mPaginationMap.containsKey("size")) {
					mReturnData.setPageSize(Integer.valueOf(mPaginationMap.get("size")));
				}
				// 判断排序
				if (mPaginationMap.containsKey("sort")) {
					sSortString = mPaginationMap.get("sort");
				}

				// 如果定义了角色判断字段
				if (mPaginationMap.containsKey("user_role")) {
					MUserInfo mUserInfo = UserFactory.INSTANCE.create();

					String sField = mPaginationMap.get("user_role");

					String sRoleInfo = StringUtils.defaultIfBlank(mUserInfo.getUserRole(), WebConst.CONST_WEB_EMPTY);

					List<String> list = new ArrayList<String>();
					for (String s : sRoleInfo.split(WebConst.CONST_SPLIT_LINE)) {
						if (StringUtils.isNotEmpty(s)) {
							list.add(" INSTR(" + sField + ",'" + s + "')>0 ");
						}
					}

					if (StringUtils.isNotEmpty(sWhere)) {
						sWhere = sWhere + " and ";
					}

					sWhere = sWhere + " (" + StringUtils.join(list, " or ") + ") ";

				}

				// 如果定义了管理权限 则取出该用户编号
				if (mPaginationMap.containsKey("user_manage")) {
					MUserInfo mUserInfo = UserFactory.INSTANCE.create();

					String sField = mPaginationMap.get("user_manage");

					String sManage = StringUtils.defaultIfBlank(mUserInfo.getManageCode(), WebConst.CONST_WEB_EMPTY);

					if (StringUtils.isNotEmpty(sWhere)) {
						sWhere = sWhere + " and ";
					}

					sWhere = sWhere + sField + "='" + sManage + "' ";

				}

				// 判断如果附加了SQL预条件定义
				if (mPaginationMap.containsKey("sql_where")) {

					String sField = mPaginationMap.get("sql_where");

					if (StringUtils.isNotEmpty(sField)) {
						sField = (StringUtils.isEmpty(sWhere) ? "" : " and ")
								+ WebHelper.recheckReplace(sField, mReqMap);
					}

					sWhere = sWhere + sField;

				}

			}

		}

		// 数据
		List<List<String>> listData = new ArrayList<List<String>>();

		// 获取字段
		List<MWebField> listFields = recheckFields(webPage.getPageFields(), mReqMap);

		List<MWebOperate> listOperates = null;
		// 定义列表中显示的按钮列
		String sOperateArea = "116001003";

		/********** 开始根据输入参数重置各种设置 ********************************/
		{

			// 判断如果是导出操作 则设置各种数据条件
			if (mOptionMap.containsKey("optionExport") && mOptionMap.get("optionExport").equals("1")) {
				sOperateArea = "";
				// mReturnData.setPageSize(-1);
				mReturnData.setPageCount(1);

			}
		}

		// 判断是否初始化列表页的附加按钮列
		if (StringUtils.isNotEmpty(sOperateArea)) {
			listOperates = recheckOperates(webPage.getPageOperate(), sOperateArea);
		}

		/********** 开始处理表头 ********************************/
		{

			List<String> listHeader = new ArrayList<String>();
			for (MWebField mField : listFields) {
				listHeader.add(mField.getFieldNote());
			}

			if (StringUtils.isNotEmpty(sOperateArea)) {

				for (MWebOperate mWebOperate : listOperates) {
					listHeader.add(mWebOperate.getOperateName());
				}
			}

			mReturnData.setPageHead(listHeader);
		}

		MDataMap mQueryMap = new MDataMap();

		/********** 开始处理查询输入 ********************************/
		{

			// 开始加载查询条件判断
			if (mReqMap.size() > 0) {

				ArrayList<String> aWhereStrings = new ArrayList<String>();

				MWebView mView = WebUp.upQueryView(webPage.getViewCode());
				List<MWebField> listQuery = recheckFields(mView.getFields(), mReqMap);

				for (MWebField mField : listQuery) {

					switch (Integer.parseInt(mField.getQueryTypeAid())) {

					// 如果是范围查询
					case 104009002:
					case 104009020:// 添加 范围时分秒 update by jlin 2014-9-22 11:11:11

						if (StringUtils.isNotEmpty(mReqMap
								.get(mField.getPageFieldName() + WebConst.CONST_WEB_FIELD_AFTER + "between_from"))) {

							aWhereStrings.add(mField.getColumnName() + ">=:" + mField.getColumnName()
									+ WebConst.CONST_WEB_FIELD_AFTER + "between_from");
							mQueryMap.put(mField.getColumnName() + WebConst.CONST_WEB_FIELD_AFTER + "between_from",
									mReqMap.get(mField.getPageFieldName() + WebConst.CONST_WEB_FIELD_AFTER
											+ "between_from"));

						}

						if (StringUtils.isNotEmpty(mReqMap
								.get(mField.getPageFieldName() + WebConst.CONST_WEB_FIELD_AFTER + "between_to"))) {

							aWhereStrings.add(mField.getColumnName() + "<=:" + mField.getColumnName()
									+ WebConst.CONST_WEB_FIELD_AFTER + "between_to");
							mQueryMap.put(mField.getColumnName() + WebConst.CONST_WEB_FIELD_AFTER + "between_to",
									mReqMap.get(
											mField.getPageFieldName() + WebConst.CONST_WEB_FIELD_AFTER + "between_to"));

						}

						break;

					// 如果是like查询
					case 104009012:

						if (StringUtils.isNotEmpty(mField.getPageFieldValue())) {
							aWhereStrings.add(" " + mField.getColumnName() + " like :" + mField.getColumnName());
							mQueryMap.put(mField.getColumnName(), "%" + mField.getPageFieldValue() + "%");
						}
						break;
					// 起始于
					case 104009019:
						if (StringUtils.isNotEmpty(mField.getPageFieldValue())) {
							aWhereStrings.add(" " + mField.getColumnName() + " like :" + mField.getColumnName());
							mQueryMap.put(mField.getColumnName(), mField.getPageFieldValue() + "%");
						}
						break;

					// 默认走等于
					default:

						if (StringUtils.isNotEmpty(mField.getPageFieldValue())) {
							if ("product_source".equals(mField.getColumnName())) {
								// 如果是LD商品
								if ("4497478100050000".equals(mField.getPageFieldValue())) {
									aWhereStrings.add(" small_seller_code in('SI2003','')");
								} else {
									List<Map<String, Object>> sellers = DbUp.upTable("uc_seller_info_extend")
											.dataSqlList(
													"select * from usercenter.uc_seller_info_extend where uc_seller_type = :uc_seller_type and small_seller_code <>''",
													new MDataMap("uc_seller_type", mField.getPageFieldValue()));
									if (sellers != null && sellers.size() > 0) {
										StringBuffer code = new StringBuffer();
										for (Map<String, Object> map : sellers) {
											MDataMap seller = new MDataMap(map);
											if (!"".equals(seller.get("small_seller_code"))) {
												code.append("\'").append(seller.get("small_seller_code").toString())
														.append("\',");
											}
										}
										aWhereStrings.add(
												" small_seller_code in (" + code.substring(0, code.length() - 1) + ")");
									} else {
										aWhereStrings.add(" small_seller_code is null ");
									}
								}
							} else {
								aWhereStrings.add(" " + mField.getColumnName() + " = :" + mField.getColumnName());
								mQueryMap.put(mField.getColumnName(), mField.getPageFieldValue());
							}
						}

						break;
					}

				}

				if (aWhereStrings.size() > 0) {

					if (StringUtils.isNotEmpty(sWhere)) {
						aWhereStrings.add(sWhere);
					}

					sWhere = StringUtils.join(aWhereStrings, " and ");
				}

			}
		}

		/********** 开始统计数据 ********************************/
		{

			// 判断如果没有请求则重新统计数量
			if (mReturnData.getPageCount() < 0) {
				mReturnData.setPageCount(DbUp.upTable(webPage.getPageTable()).dataCount(sWhere, mQueryMap));

			}

			// 判断最大数量是否有
			if (mReturnData.getPageMax() < 0) {
				mReturnData.setPageMax(
						(int) Math.ceil((double) mReturnData.getPageCount() / (double) mReturnData.getPageSize()));
			}
		}

		/********** 开始处理数据 ********************************/
		{

			// 开始加载数据
			for (MDataMap mData : DbUp.upTable(webPage.getPageTable()).query(
					WebHelper.upFieldSql(webPage.getPageFields()), sSortString, sWhere, mQueryMap,
					(mReturnData.getPageIndex() - 1) * mReturnData.getPageSize(), mReturnData.getPageSize())) {
				List<String> listEach = new ArrayList<String>();

				// 循环展示列
				for (MWebField mField : listFields) {

					String sFieldText = "";

					// 判断如果是组件则重新输出文字
					if (mField.getFieldTypeAid().equals("104005003")) {

						if (StringUtils.isNotEmpty(sOperateArea)) {
							sFieldText = WebUp.upComponent(mField.getSourceCode()).upListText(mField, mData);
						} else {
							sFieldText = WebUp.upComponent(mField.getSourceCode()).upExportText(mField, mData);
						}

					} else {
						sFieldText = mData.get(mField.getFieldName());
					}

					// 判断是否有展示替换
					if (!StringUtils.isEmpty(mField.getFieldScope())) {

						if (mField.getFieldScope().indexOf(WebConst.CONST_WEB_PAGINATION_NAME + "showreplace") > -1) {
							MDataMap mFieldScope = new MDataMap().inUrlParams(mField.getFieldScope());
							MDataMap mScopeMap = mFieldScope.upSubMap(WebConst.CONST_WEB_PAGINATION_NAME);
							if (mScopeMap.containsKey("showreplace")) {
								sFieldText = WebHelper.recheckReplace(mScopeMap.get("showreplace"), mData);
							}

						}

					}

					listEach.add(sFieldText);

				}

				// 判断是否是加载附加按钮列
				if (StringUtils.isNotEmpty(sOperateArea)) {
					for (MWebOperate mWebOperate : listOperates) {
						listEach.add(reloadOperateText(mWebOperate, mData));
					}
				}

				listData.add(listEach);

			}
		}

		/********** 根据规则重新处理数据 ********************************/
		{
			// 重新加载输出字段 判断加载替换显示等操作
			for (int i = 0, j = listFields.size(); i < j; i++) {

				// 如果是下拉框
				if (listFields.get(i).getFieldTypeAid().equals("104005019")
						|| listFields.get(i).getFieldTypeAid().equals("104005103")) {
					MWebSource mSource = WebUp.upSource(listFields.get(i).getSourceCode());

					List<String> listSqlSub = new ArrayList<String>();

					for (List<String> listSub : listData) {
						listSqlSub.add("" + listSub.get(i) + "");
					}

					// 转义数据源参数 20150824 GaoYang add start
					String sSourceParam = recheckReplace(listFields.get(i).getSourceParam());
					// 转义数据源参数 20150824 GaoYang add end

					List<MDataMap> listResultDataMaps = DbUp.upTable(mSource.getSourceFrom()).queryAll(
							mSource.getFieldText() + " as field_text,"
									+ mSource
											.getFieldValue()
									+ " as field_value ",
							"",
							" instr(:field_list,concat("
									// update by jlin 2015-08-06 不带查询调害死人
									+ mSource.getFieldValue() + ",','))>0  "
									+ (StringUtils.isNotEmpty(mSource.getWhereEdit())
											? ("and " + FormatHelper.formatString(mSource.getWhereEdit(), sSourceParam))
											: ""),
							new MDataMap("field_list", StringUtils.join(listSqlSub, WebConst.CONST_SPLIT_COMMA)
									+ WebConst.CONST_SPLIT_COMMA));

					MDataMap mKeyMap = new MDataMap();
					for (MDataMap mMap : listResultDataMaps) {
						mKeyMap.put(mMap.get("field_value"), mMap.get("field_text"));
					}

					for (int n = 0, m = listData.size(); n < m; n++) {

						String sNowString = listData.get(n).get(i);

						List<String> listNowList = new ArrayList<String>();

						for (String s : sNowString.split(WebConst.CONST_SPLIT_COMMA)) {
							if (mKeyMap.containsKey(s)) {
								listNowList.add(mKeyMap.get(s));
							}
						}

						listData.get(n).set(i, StringUtils.join(listNowList, WebConst.CONST_SPLIT_COMMA));
					}

				}
			}
		}

		mReturnData.setPageData(listData);
		return mReturnData;

	}

	/**
	 * 20150824 GaoYang add 转义数据源参数
	 * 
	 * @param sourceParam
	 * @return
	 */
	private String recheckReplace(String sourceParam) {

		if (StringUtils.contains(sourceParam, WebConst.CONST_WEB_SET_REPLACE)) {

			Pattern p = Pattern.compile("\\[@(.+?)\\$(.*?)\\]");
			Matcher m = p.matcher(sourceParam);
			while (m.find()) {
				String sFull = m.group(0);
				String sKey = m.group(1);
				String sAttr = m.group(2);

				String sReplace = "";
				if (sKey.equals("user")) {
					MUserInfo mUserInfo = UserFactory.INSTANCE.create();
					if (mUserInfo.getFlagLogin() == 1) {
						if (sAttr.equals("manageCode")) {
							sReplace = mUserInfo.getManageCode();
						} else if (sAttr.equals("loginName")) {
							sReplace = mUserInfo.getLoginName();
						} else if (sAttr.equals("realName")) {
							sReplace = mUserInfo.getRealName();
						} else if (sAttr.equals("userCode")) {
							sReplace = mUserInfo.getUserCode();
						} else if (sAttr.equals("traderCode")) {
							sReplace = mUserInfo.getTraderCode();
						}
					}
				}
				sourceParam = sourceParam.replace(sFull, sReplace);
			}
		}

		return sourceParam;
	}

	/**
	 * @description 根据商户编码查询商户类型
	 * @param small_seller_code
	 * @return
	 */
	public String getSellerType(String small_seller_code) {
		String sellerTypeName = "";
		//添加新的small_seller_code = SI2009
		if (small_seller_code == null || "".equals(small_seller_code) || "SI2003".equals(small_seller_code)||"SI2009".equals(small_seller_code)) {
			sellerTypeName = "LD系统";
		} else {
			MDataMap map = new MDataMap();
			map.put("small_seller_code", small_seller_code);
			Map<String, Object> result = DbUp.upTable("uc_seller_info_extend").dataSqlOne(
					"select uc_seller_type from usercenter.uc_seller_info_extend where small_seller_code=:small_seller_code",
					map);
			String sellerType = result != null ? result.get("uc_seller_type").toString() : "";
			if(StringUtils.isBlank(sellerType)) {
				sellerTypeName = "";
			}else{
				sellerTypeName = (String)DbUp.upTable("sc_define").dataGet("define_name", "", new MDataMap("define_code", sellerType, "parent_code", "449747810005"));
			}
		}

		return sellerTypeName;
	}
}
