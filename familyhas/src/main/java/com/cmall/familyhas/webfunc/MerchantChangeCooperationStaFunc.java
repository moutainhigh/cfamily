package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.productcenter.common.DateUtil;
import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.ProductSkuInfo;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @ClassName: MerchantChangeCooperationStaFunc
 * @Description: 修改商户合作状态
 * @author zhangbo
 * 
 */
public class MerchantChangeCooperationStaFunc extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		Map<String,Object> resutlMap = DbUp.upTable("za_userinfo").dataSqlOne("select zu.flag_enable as flag,zu.user_name as user_name from zapdata.za_userinfo zu,usercenter.uc_seller_info_extend ue where zu.user_name=ue.user_name and ue.uid=:uid", new MDataMap("uid",uid));
		
		if(resutlMap==null) {
			result.setResultCode(0);
			result.setResultMessage("操作失败");
			return result;
		}
		else {
			
			String flag = resutlMap.get("flag").toString();
			if("0".equals(flag)) {
				String outUserSql = "update za_userinfo set flag_enable = '2',cookie_user='' where user_name=:user_name ";
				DbUp.upTable("za_userinfo").dataExec(outUserSql, new MDataMap("user_name",resutlMap.get("user_name").toString()));
			}
			else if("2".equals(flag)) {
				String outUserSql = "update za_userinfo set flag_enable = '0',cookie_user='' where user_name=:user_name ";
				DbUp.upTable("za_userinfo").dataExec(outUserSql, new MDataMap("user_name",resutlMap.get("user_name").toString()));
			}
			else {
				result.setResultCode(0);
				result.setResultMessage("操作失败");
			}
			return result;
			
		}
		
		
	
}
}