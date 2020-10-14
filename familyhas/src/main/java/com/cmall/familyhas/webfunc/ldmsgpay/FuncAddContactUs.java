package com.cmall.familyhas.webfunc.ldmsgpay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加
 * 
 * @author srnpr
 * 
 */
public class FuncAddContactUs extends RootFunc {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srnpr.zapweb.webface.IWebFunc#funcDo(java.lang.String,
	 * com.srnpr.zapcom.basemodel.MDataMap)
	 */
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
		
		//判断参数
		if(StringUtils.isEmpty(mAddMaps.get("video_url"))&&StringUtils.isEmpty(mAddMaps.get("pic_url"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("视频与图片必须有一个不为空");
			return mResult;
		}
		
		if(StringUtils.isNotEmpty(mAddMaps.get("video_url"))&&StringUtils.isEmpty(mAddMaps.get("video_main_pic"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("视频封面图不能为空");
			return mResult;
		}
		
		String video_main_pic = mAddMaps.get("video_main_pic");
		if(StringUtils.isNotEmpty(video_main_pic)&&!checkImg(video_main_pic)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("视频封面图格式不正确");
			return mResult;
		}
		String pic_url = mAddMaps.get("pic_url");
		if(StringUtils.isNotEmpty(pic_url)) {
			boolean flag = true;
			String pics[] = pic_url.split("\\|");
			for(int i = 0;i<pics.length;i++) {
				if(StringUtils.isNotEmpty(pics[i])&&!checkImg(pics[i])) {
					flag = false;
					break;
				}
			}
			if(!flag) {
				mResult.setResultCode(0);
				mResult.setResultMessage("图片链接格式不正确");
				return mResult;
			}
		}

		if (mResult.upFlagTrue()) {

			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {

				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}

				if (mAddMaps.containsKey(mField.getFieldName()) && StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
				}

				// 如果设置不为空 则进行各种校验
				if (StringUtils.isNotEmpty(mField.getFieldScope())) {

					MDataMap mFieldScope = new MDataMap().inUrlParams(mField.getFieldScope());

					MDataMap mScopeMap = mFieldScope.upSubMap(WebConst.CONST_WEB_PAGINATION_NAME);

					String sDefaultValue = "";

					if (mScopeMap.containsKey("defaultvalue")) {
						sDefaultValue = mScopeMap.get("defaultvalue");
					}

					// 判断默认值
					if (StringUtils.isNotEmpty(sDefaultValue)) {
						String sValue = "";

						if (StringUtils.contains(sDefaultValue, WebConst.CONST_WEB_SET_REPLACE)) {

							// 重新格式化参数
							sValue = WebHelper.recheckReplace(sDefaultValue, mDataMap);

						} else {
							sValue = sDefaultValue;
						}

						if (StringUtils.isNotEmpty(sValue)) {
							mDataMap.put(WebConst.CONST_WEB_FIELD_NAME + mField.getFieldName(), sValue);

							mInsertMap.put(mField.getColumnName(), sValue);
						}

					}

					// 如果有附件设置
					if (mScopeMap.containsKey("targetset")) {
						String sTargetSetString = mScopeMap.get("targetset");

						// 校验字段的唯一
						if (sTargetSetString.equals("unique")) {

							if (DbUp.upTable(mPage.getPageTable()).count(mField.getColumnName(),
									mInsertMap.get(mField.getColumnName())) > 0) {

								mResult.inErrorMessage(969905004, mField.getFieldNote());
							}

						}
						// 自动生成code
						else if (sTargetSetString.equals("code")) {

							MDataMap mSetMap = mFieldScope.upSubMap(WebConst.CONST_WEB_FIELD_SET);

							String sCodeName = mSetMap.get("codename");

							String sParentValue = mAddMaps.get(sCodeName);

							MDataMap mTopDataMap = DbUp.upTable(mPage.getPageTable()).oneWhere(mField.getColumnName(),
									"-" + mField.getColumnName(), "", sCodeName, sParentValue);

							if (mTopDataMap != null) {

								String sMaxString = mTopDataMap.get(mField.getColumnName());
								long lMax = Long.parseLong(StringUtils.right(sMaxString, 4)) + 1;

								mInsertMap.put(mField.getColumnName(),
										sParentValue + StringUtils.leftPad(String.valueOf(lMax), 4, "0"));

							} else {
								String sMaxAdd = sParentValue
										+ (mSetMap.containsKey("maxadd") ? mSetMap.get("maxadd") : "0001");

								mInsertMap.put(mField.getColumnName(), String.valueOf(sMaxAdd));

							}

						}

					}

				}

			}
		}

		if (mResult.upFlagTrue()) {
			DbUp.upTable(mPage.getPageTable()).dataInsert(mInsertMap);

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inAdd(mField, mDataMap);

					}
				}

			}

		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;

	}
	
	
	private boolean checkImg(String url) {
		String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG|.bmp|.jpg|.png|.tif|.gif|.pcx|.tga|.exif|.fpx|.svg|.psd|.cdr|.pcd|.dxf|.ufo|.eps|.ai|.raw|.WMF|.webp)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}
	
}
