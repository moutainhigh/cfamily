package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.groupcenter.homehas.RsyncBankinfoBranchToLD;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 新增支行信息到LD系统
 */
public class FuncAddBankInfoBranchToLD extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		// 银行类型代码
		String bank_code = StringUtils.trimToEmpty(mAddMaps.get("bank_name"));
		// 支行名称
		String bank_name_branch = StringUtils.trimToEmpty(mAddMaps.get("bank_name_branch"));
		// 银联编号
		String bank_number = StringUtils.trimToEmpty(mAddMaps.get("bank_number"));
		// 开户行所属地编号
		String branch_area_address = StringUtils.trimToEmpty(mAddMaps.get("branch_area_address_city"));
		
		MDataMap bankinfo = DbUp.upTable("uc_bankinfo").one("bank_code",bank_code);
		if(bankinfo == null){
			result.setResultCode(0);
			result.setResultMessage("银行类型不存在");
			return result;
		}
		
		if(StringUtils.isBlank(bank_name_branch)){
			result.setResultCode(0);
			result.setResultMessage("支行名称不能为空");
			return result;
		}
		if(StringUtils.isBlank(bank_number)){
			result.setResultCode(0);
			result.setResultMessage("银联编号不能为空");
			return result;
		}
		if(StringUtils.isBlank(branch_area_address)){
			result.setResultCode(0);
			result.setResultMessage("请选择开户行所在地");
			return result;
		}
		if(StringUtils.isBlank(bankinfo.get("ld_code"))){
			result.setResultCode(0);
			result.setResultMessage("银行对应的LD编号为空");
			return result;
		}
		
		RsyncBankinfoBranchToLD rsync = new RsyncBankinfoBranchToLD();
		RsyncBankinfoBranchToLD.Bankinfo bi = new RsyncBankinfoBranchToLD.Bankinfo();
		bi.bank_cd = bankinfo.get("ld_code");
		bi.bankdoc = bank_name_branch;
		bi.bankdoc_cd = bank_number;
		bi.city_code = branch_area_address;
		rsync.upRsyncRequest().bank_info.add(bi);
		
		if(!rsync.doRsync()){
			result.setResultCode(0);
			result.setResultMessage("添加失败："+rsync.upResponseObject().message);
		}
		
		return result;
		
	}

}
