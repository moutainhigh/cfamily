package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 启动页
 * 
 * @author ligj
 * 
 */
public class StartpageFuncEdit extends RootFunc {
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

			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {
				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}
				if (mAddMaps.containsKey(mField.getFieldName())
						&& StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
				}
			}
			if (bFlagComponent) {
				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {
						WebUp.upComponent(mField.getSourceCode()).inAdd(mField,mDataMap);
					}
				}
			}
		}

		if (mResult.upFlagTrue()) {
			
			//先判断登录是否有效
			if(UserFactory.INSTANCE.create().getLoginName() == null || UserFactory.INSTANCE.create().getLoginName().equals("")){
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			String picId = WebHelper.upCode("PXH");
			String start_time = mAddMaps.get("start_time");
			String end_time = mAddMaps.get("end_time");
			String uid = mAddMaps.get("uid");
			//开始时间必须小于结束时间
			if (start_time.compareTo(end_time) >= 0) {
				mResult.inErrorMessage(916401201);
				return mResult;
			}
			
			//判断开始结束时间是否修改过
			MDataMap flagTimeUpdate = DbUp.upTable("nc_startpage").one("uid",uid,
													"start_time",start_time,"end_time",end_time);
			if(null == flagTimeUpdate || flagTimeUpdate.isEmpty()){
				/*所属app*/
				String app_code = bConfig("familyhas.app_code");
				String sWhere = " ((start_time BETWEEN '"+start_time+"' and '"+end_time+"') "+
						" or (end_time BETWEEN '"+start_time+"' and '"+end_time+"') "+
						" or ('"+start_time+"' BETWEEN start_time and end_time) "+
						" or ('"+end_time+"' BETWEEN start_time and end_time)) and app_code='"+app_code+"' and uid != '"+uid+"'";
				MDataMap startPage = DbUp.upTable("nc_startpage").oneWhere("uid", "", sWhere);
				if (startPage != null && !startPage.isEmpty()) {
					mResult.inErrorMessage(916401202);
					return mResult;
				}
			}
			
			mInsertMap.put("pic_id", picId);
			if("4497471600180001".equals(mAddMaps.get("yn_jump_button"))){//如果是否跳转为否
				mInsertMap.put("showmore_linktype", "");
				mInsertMap.put("showmore_linkvalue", "");
				mInsertMap.put("button_pic", "");
				mInsertMap.put("button_text", "");
				mInsertMap.put("button_color", "");
				mInsertMap.put("button_background", "");
			}
			if("4497471600190001".equals(mAddMaps.get("button_type"))){//如果按钮类型为图片
				mInsertMap.put("button_text", "");
				mInsertMap.put("button_color", "");
				mInsertMap.put("button_background", "");
			}else if("4497471600190002".equals(mAddMaps.get("button_type"))){//如果按钮类型为文本
				mInsertMap.put("button_pic", "");
			}
			if(StringUtils.isEmpty(mInsertMap.get("residence_time"))){
				mInsertMap.remove("residence_time");
			}
			
			// 如未传值则默认更新为空字符串
			mInsertMap.put("showmore_linkvalue", StringUtils.trimToEmpty(mAddMaps.get("showmore_linkvalue")));
			
			//插入数据
			DbUp.upTable(mPage.getPageTable()).dataUpdate(mInsertMap, "", "uid");
		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;

	}
}
