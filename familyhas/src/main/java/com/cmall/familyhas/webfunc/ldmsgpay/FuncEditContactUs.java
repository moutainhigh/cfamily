package com.cmall.familyhas.webfunc.ldmsgpay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEditContactUs extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		MDataMap mInsertMap = new MDataMap();

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
		// 定义组件判断标记
		boolean bFlagComponent = false;

		if (mResult.upFlagTrue()) {

			// 循环所有结构
			for (MWebField mField : mPage.getPageFields()) {

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

		if (mResult.upFlagTrue()) {
			DbUp.upTable(mPage.getPageTable()).dataUpdate(mInsertMap, "", "uid");

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inEdit(mField, mDataMap);

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
