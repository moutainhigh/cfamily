package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.AddShuiyin;
import com.cmall.familyhas.util.PinyinUtil;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.model.ScFlowMain;
import com.cmall.systemcenter.service.FlowService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 加入草稿箱
 * @author LHY
 * 2016年4月29日 下午2:02:16
 */
public class MerchantSaveDrafts extends RootFunc {
	/**
	   * 产生随机字符串
	   * */
	private static Random randGen = new Random();;
	private static char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz").toCharArray();

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		// 548添加,过滤输入数据中的空格
		Set<Entry<String, String>> entrySet = mDataMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String value = entry.getValue();
			if(null != value && !"".equals(value)) {
				value = value.replaceAll("\\s", "");
				mDataMap.put(entry.getKey(), value);
			}
		}
				
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		mAddMaps.remove("json");//json这条数据暂时没用,所以这里要删除
		
		String uid = mAddMaps.get("uid");
		
		String sellerCode = mAddMaps.get("seller_code");
		
		if(StringUtils.isEmpty(sellerCode)) {
			sellerCode = AppConst.MANAGE_CODE_HOMEHAS;
		}
		/* 商户编号 */
		String smallSellerCode = mAddMaps.get("small_seller_code");
		/* 公司名称 */
		String smallCompanyName = mAddMaps.get("seller_company_name");
		/* 注册号 */
		String registrationNumber = mAddMaps.get("registration_number");
		/* 商户类型 */
		String ucSellerType = mAddMaps.get("uc_seller_type");

		if(StringUtils.isEmpty(smallSellerCode)) {
//			smallSellerCode = WebHelper.upCode("SF03");
			//读取配置文件获取商户编码前缀 2016-11-14 zhy
			String code_start = bConfig("usercenter.user"+mAddMaps.get("uc_seller_type"));
			smallSellerCode = WebHelper.upCode(code_start);
		}else{
			//判读当前的审批流程是否存在。
			FlowService fs = new FlowService();
			ScFlowMain sfm = fs.getApprovalFlowByOurterCode(smallSellerCode, "449717230011");
			
			if(sfm != null){
				mResult.inErrorMessage(916423013);
				return mResult;
			}
		}
		if(StringUtils.isEmpty(uid)) {
			MDataMap Merchant = new MDataMap();
			Merchant.put("registration_number", mAddMaps.get("registration_number"));
			String flagdelcount = (String) DbUp.upTable("uc_seller_info_extend_draft").dataGet("flag_del", null,Merchant);
			if (flagdelcount!=null) {
				if(flagdelcount.equals("4497478100040001")){
					//查询商户公司名称是否存在
					int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"seller_company_name", smallCompanyName);
					if(smallCompanyNameCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
					//查询商户注册号是否存在
					int registrationNumberCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"registration_number",registrationNumber);
					if(registrationNumberCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
				}
				
			}
		}
 		if(StringUtils.isEmpty(uid)) {
			MDataMap Merchant = new MDataMap();
			Merchant.put("seller_company_name", mAddMaps.get("seller_company_name"));
			String flagdelcount = (String) DbUp.upTable("uc_seller_info_extend_draft").dataGet("flag_del", null,Merchant);
			if (flagdelcount!=null) {
				if(flagdelcount.equals("4497478100040001")){
					//查询商户公司名称是否存在
					int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"seller_company_name", smallCompanyName);
					if(smallCompanyNameCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
					//查询商户注册号是否存在
					int registrationNumberCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"registration_number",registrationNumber);
					if(registrationNumberCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
				}
			}
		}
 		String smallsellerCode = mAddMaps.get("small_seller_code");
		if(StringUtils.isEmpty(uid)||smallsellerCode!="") {
			if(!smallsellerCode.equals("")){
				MDataMap uids = new MDataMap();
				uids.put("uid", mAddMaps.get("uid"));
				uids.put("uid", mAddMaps.get("uid"));
				String smallCompanyNames = (String) DbUp.upTable("uc_seller_info_extend_draft").dataGet("seller_company_name", null, uids);
				String registrationNumbers = (String) DbUp.upTable("uc_seller_info_extend_draft").dataGet("registration_number", null, uids);
				if(!smallCompanyName.equals(smallCompanyNames) || !registrationNumber.equals(registrationNumbers)){
					//查询商户公司名称是否存在
					int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"seller_company_name", smallCompanyName);
					if(smallCompanyNameCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
					//查询商户注册号是否存在
					int registrationNumberCount = DbUp.upTable("uc_seller_info_extend_draft").count("uc_seller_type",ucSellerType,"registration_number",registrationNumber);
					if(registrationNumberCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
				}
			}
		}

		if(StringUtils.isEmpty(uid)) {
			int smallSellerCodeCount = DbUp.upTable("uc_seller_info_extend_draft").count("small_seller_code", smallSellerCode);
			if(smallSellerCodeCount>0) {
				mResult.setResultMessage("公司编号已存在");
				return mResult;
			}
		}
		String shortName = mAddMaps.get("seller_short_name").trim();//公司简称
		String shortNameFlag = shortName;
		for (; ;) {
			int count = DbUp.upTable("uc_sellerinfo").count("seller_short_name",shortName);
			if(count == 0){//判断用户名是否重复，如果重复，从26个字符中随机取一个加子在用户名后面，再去比较
				break;
			}else{
				shortName = (shortNameFlag + numbersAndLetters[randGen.nextInt(25)]).trim();
				continue;
			}
		}
		
		String userCode = UserFactory.INSTANCE.create().getUserCode();
		String userName = mAddMaps.get("user_name");//用户名
		if(StringUtils.isEmpty(userName)) {
			userName = PinyinUtil.getFirstPingYin(shortName).replaceAll("\\s*", "");
		}
		String nameFlag = userName;
		for (; ;) {
			int userCount = DbUp.upTable("za_userinfo").count("user_name",userName);
			if(userCount == 0){//判断用户名是否重复，如果重复，从26个字符中随机取一个加子在用户名后面，再去比较
				break;
			}else{
				userName = (nameFlag + numbersAndLetters[randGen.nextInt(25)]).replaceAll("\\s*", "");
				continue;
			}
			
		}
		mAddMaps.put("seller_code", sellerCode);
		mAddMaps.put("small_seller_code", smallSellerCode);
		mAddMaps.put("seller_short_name", shortName);
		mAddMaps.put("user_name", userName);
		if (!"0".equals(mAddMaps.get("money_proportion"))) {
			mAddMaps.put("money_proportion", new BigDecimal(mAddMaps.get("money_proportion")).divide(new BigDecimal(100)).toString());//由于质保金比例在存入数据库中是小数，所以需要除以100再存
		}else{
			mAddMaps.put("money_proportion","0");
		}
		if(StringUtils.isEmpty(mAddMaps.get("quality_retention_money"))) {
			mAddMaps.put("quality_retention_money", "0");
		}
		String perstr = mAddMaps.get("upload_business_license");//未加水印的商户执照
		byte[] markImageByMoreIcon = AddShuiyin.markImageByMoreIcon(MerchantApproveFunc.class.getResourceAsStream("/shuiyin.png"), perstr, "jpg", null);
		MWebResult remoteUpload = WebUpload.create().remoteUpload("upload", perstr.substring(perstr.lastIndexOf("/")+1), markImageByMoreIcon);
		mAddMaps.put("upload_business_license", remoteUpload.getResultObject().toString());
		
		// 判断结算周期是不是自定义
		if (!"4497478100030006".equals(mAddMaps.get("account_clear_type"))) {
			// 不是自定义,清空账单节点和天数字段
			mAddMaps.put("bill_point", "");
			mAddMaps.put("bill_day", "");
		}
		
		if(StringUtils.isNotEmpty(uid)) {//修改
			mAddMaps.put("editId", userCode);
			mAddMaps.put("update_user", userCode);
			mAddMaps.put("update_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("uc_seller_info_extend_draft").dataUpdate(mAddMaps, "", "uid");
		} else {//新增
			mAddMaps.put("flow_status", "4497471600300001");			//新增的数据肯定是草稿类型
			mAddMaps.put("register_time", DateUtil.getSysDateTimeString());
			mAddMaps.put("create_user", userCode);
			mAddMaps.put("create_time", DateUtil.getSysDateTimeString());
			mAddMaps.put("flag_del", "4497478100040001");
			DbUp.upTable("uc_seller_info_extend_draft").dataInsert(mAddMaps);// 把商户信息添加到表中
		}
		return mResult;
	}
}