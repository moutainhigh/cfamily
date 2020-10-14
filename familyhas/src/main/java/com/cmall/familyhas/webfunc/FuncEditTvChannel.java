package com.cmall.familyhas.webfunc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券活动
 * @author ligj
 *
 */
public class FuncEditTvChannel extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		try{
			/*获取当前登录人*/
//			String create_user = UserFactory.INSTANCE.create().getLoginName();
			/*系统当前时间*/
//			String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
			Map<String, Object> tmpList = new HashMap<String, Object>();
			String areaCode = "",tName = "";
			if(StringUtils.isNotBlank(mDataMap.get("county"))){
				areaCode = mDataMap.get("county");
				tmpList = DbUp.upTable("sc_tmp").dataSqlOne("select t.*,CONCAT(ttt.`name`,tt.`name`,t.`name`) t_name from sc_tmp t LEFT JOIN sc_tmp tt ON t.p_code = tt.code LEFT JOIN sc_tmp ttt on tt.p_code = ttt.code where t.code = :code and t.use_yn = 'Y' and t.send_yn = 'Y'", new MDataMap("code", areaCode));
				tName = tmpList.get("t_name").toString();
			}else if(StringUtils.isNotBlank(mDataMap.get("city"))){
				areaCode = mDataMap.get("city");
				tmpList = DbUp.upTable("sc_tmp").dataSqlOne("select t.*,CONCAT(tt.`name`,t.`name`) t_name from sc_tmp t LEFT JOIN sc_tmp tt ON t.p_code = tt.code where t.code = :code and t.use_yn = 'Y' and t.send_yn = 'Y'", new MDataMap("code", areaCode));
				tName = tmpList.get("t_name").toString();
			}else if(StringUtils.isNotBlank(mDataMap.get("province"))){
				areaCode = mDataMap.get("province");
				tmpList = DbUp.upTable("sc_tmp").dataSqlOne("select t.* from sc_tmp t where t.code = :code and t.use_yn = 'Y' and t.send_yn = 'Y'", new MDataMap("code", areaCode));
				tName = tmpList.get("name").toString();
			}
			if(tName.contains("市辖区")){
				tName = tName.replaceAll("市辖区", "");
			}
			
			mDataMap.put("zw_f_code", areaCode);
			mDataMap.put("zw_f_name", tName);
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
