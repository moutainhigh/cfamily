package com.cmall.familyhas.webfunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jsoup.select.Evaluator.IsEmpty;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.homehas.model.RsyncResponseOrderStatus.Result;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 可能喜欢的商品添加
 * 
 * @author liql
 * 
 */
public class ProductMaybeLoveFuncAdd extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		try {
			String keyValueList = mAddMaps.get("product_code_select").trim();
			if (StringUtils.isEmpty(keyValueList)) {
				mResult.inErrorMessage(916401220);
			}else{
				//做时间的比较
				MDataMap mSetTimeMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar start_time = Calendar.getInstance();
				Calendar end_time = Calendar.getInstance();
				Calendar now_time = Calendar.getInstance();
				start_time.setTime(df.parse(mSetTimeMap.get("start_time")));
				end_time.setTime(df.parse(mSetTimeMap.get("end_time")));
				now_time.setTime(df.parse(df.format(new Date())));
				int result1 = end_time.compareTo(start_time);
				int result2 = end_time.compareTo(now_time);
				// 创建时间为当年系统时间
				SimpleDateFormat create_time = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				/* 获取当前登录人 */
				String create_user = UserFactory.INSTANCE.create().getLoginName();
				mAddMaps.put("create_user", create_user);
				String[] splitsValue = keyValueList.split(",");
				// 判断是否选择了商品
				if (splitsValue.length > 0 && null != splitsValue
						&& !"".equals(splitsValue)) {
					List<MDataMap> query = new ArrayList<MDataMap>();
					StringBuffer peoduct_code_list = new StringBuffer();
					
					String productCode = "";
					for (int i = 0; i < splitsValue.length; i++) {
						productCode = splitsValue[i];
						boolean flag = true;
						if(result1>0&&result2>0){
							query = DbUp.upTable("pc_product_maybelove").queryAll("product_code,end_time,start_time", "", "product_code=:product_code", new MDataMap("product_code",splitsValue[i]));
							if(null!=query && !query.isEmpty()){
								for(MDataMap mDataMap2 : query){
									Calendar old_end_time = Calendar.getInstance();
									Calendar old_start_time = Calendar.getInstance();
									old_end_time.setTime(df.parse(mDataMap2.get("end_time")));//已经存在商品的结束时间
									old_start_time.setTime(df.parse(mDataMap2.get("start_time")));//已经存在商品的开始时间
									if ((old_start_time.compareTo(start_time)>0&&old_start_time.compareTo(end_time)>0)
											||(old_end_time.compareTo(start_time)<0&&old_end_time.compareTo(end_time)<0)) {
									} else {
										flag=false;
										peoduct_code_list.append(splitsValue[i]).append(",");
										break;
									}
								}
							}
						}else {
							flag=false;
							mResult.inErrorMessage(916421257);
						}
						if(flag){
							mAddMaps.remove("product_code_select");
							mAddMaps.put("product_code", productCode);
							mAddMaps.put("uid",
									UUID.randomUUID().toString().replace("-", ""));
							mAddMaps.put("create_time", create_time.format(new Date()));
							mAddMaps.put("position", mSetTimeMap.get("position"));
							if (mResult.upFlagTrue()) {
								/** 将惠家有信息插入pc_product_maybelove表中 */
								DbUp.upTable("pc_product_maybelove").dataInsert(
										mAddMaps);
							}
						}
					}
					if (peoduct_code_list.length() > 0) {
						// 去掉最后一个逗号
						peoduct_code_list.deleteCharAt(peoduct_code_list.length() - 1);
						mResult.setResultCode(916401219);
						mResult.setResultMessage(bInfo(916401219, "["
								+ peoduct_code_list + "]"));
					}
				}
				
			}
			if (mResult.upFlagTrue()) {
				mResult.setResultMessage(bInfo(969909001));
			}
		} catch (ParseException e1) {
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
