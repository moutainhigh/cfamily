package com.cmall.familyhas.webfunc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.AddShuiyin;
import com.cmall.familyhas.util.PinyinUtil;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.model.ScFlowMain;
import com.cmall.systemcenter.service.FlowService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @author LHY
 * 商户提交审核
 */
public class MerchantApproveFunc extends FuncAdd {
	/**
	 * 产生随机字符串
	 */
	private static Random randGen = new Random();;
	private static char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz").toCharArray();
	//perstr.substring(perstr.lastIndexOf("/")+1,perstr.lastIndexOf(".")).concat("-shuiyin").concat(perstr.substring(perstr.lastIndexOf(".")))
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
		String smallSellerCode = "";
		String lock = "";	//锁
		String uid = mAddMaps.get("uid");
		try {
			String sellerCode = mAddMaps.get("seller_code");
			if(StringUtils.isEmpty(sellerCode)) {
				sellerCode = AppConst.MANAGE_CODE_HOMEHAS;
			}
			String perstr = mAddMaps.get("upload_business_license");//未加水印的商户执照
			byte[] markImageByMoreIcon = AddShuiyin.markImageByMoreIcon(MerchantApproveFunc.class.getResourceAsStream("/shuiyin.png"), perstr, "jpg", null);
			MWebResult remoteUpload = WebUpload.create().remoteUpload("upload", perstr.substring(perstr.lastIndexOf("/")+1), markImageByMoreIcon);
			mAddMaps.put("upload_business_license", remoteUpload.getResultObject().toString());
			mDataMap.put("zw_f_upload_business_license", remoteUpload.getResultObject().toString());
			/* 商户编号 */
			smallSellerCode = mAddMaps.get("small_seller_code");
			/* 公司名称 */
			String smallCompanyName = mAddMaps.get("seller_company_name");
			/* 注册号 */
			String registrationNumber = mAddMaps.get("registration_number");
			/* 商户类型 */
			String ucSellerType = mAddMaps.get("uc_seller_type");
			
			if(StringUtils.isEmpty(smallSellerCode)) {
//				smallSellerCode = WebHelper.upCode("SF03");
				//读取配置文件获取商户编码前缀 2016-11-14 zhy
				String code_start = bConfig("usercenter.user"+mAddMaps.get("uc_seller_type"));
				smallSellerCode = WebHelper.upCode(code_start);
			}else{
				//判读当前的审批流程是否存在。
				FlowService fs = new FlowService();
				ScFlowMain sfm = fs.getApprovalFlowByOurterCode(smallSellerCode, "449717230014");
				
				if(sfm != null){
					mResult.inErrorMessage(916423013);
					return mResult;
				}
			}
			if (StringUtils.isEmpty(uid)) {
				// 查询商户公司名称是否存在
				int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type", ucSellerType,
						"seller_company_name", smallCompanyName);
				if (smallCompanyNameCount > 0) {
					mResult.setResultMessage("商户信息已存在");
					return mResult;
				}
				// 查询商户注册号是否存在
				int registrationNumberCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type",
						ucSellerType, "registration_number", registrationNumber);
				if (registrationNumberCount > 0) {
					mResult.setResultMessage("商户信息已存在");
					return mResult;
				}
			}
			String smallsellerCode = mAddMaps.get("small_seller_code");
			if (StringUtils.isEmpty(uid) || smallsellerCode != "") {
				if (!smallsellerCode.equals("")) {
					MDataMap uids = new MDataMap();
					uids.put("uid", mAddMaps.get("uid"));
					String smallCompanyNames = (String) DbUp.upTable("uc_seller_info_extend")
							.dataGet("seller_company_name", null, uids);
					String registrationNumbers = (String) DbUp.upTable("uc_seller_info_extend")
							.dataGet("registration_number", null, uids);
					if (!smallCompanyName.equals(smallCompanyNames)) {
						int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type",
								ucSellerType, "seller_company_name", smallCompanyName);
						if (smallCompanyNameCount > 0) {
							mResult.setResultMessage("商户信息已存在");
							return mResult;
						}
					}
					if (!registrationNumber.equals(registrationNumbers)) {
						// 查询商户注册号是否存在
						int registrationNumberCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type",
								ucSellerType, "registration_number", registrationNumber);
						if (registrationNumberCount > 0) {
							mResult.setResultMessage("商户信息已存在");
							return mResult;
						}
					}
				}
			}
			int smallSellerCodeCount = DbUp.upTable("uc_sellerinfo").count("small_seller_code", smallSellerCode);
			if(smallSellerCodeCount>0) {
				mResult.setResultMessage("公司编号已存在");
				return mResult;
			}
			/* 注册号(营业执照号)  去重判断 取消。  fq++
			int registrationumberCount = DbUp.upTable("uc_seller_info_extend").count("registration_number", mAddMaps.get("registration_number"));
			if(registrationumberCount>0) {
				mResult.setResultCode(916423015);
				mResult.setResultMessage(bInfo(916423015));
				return mResult;
			}*/
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
			String userName = PinyinUtil.getFirstPingYin(shortName).replaceAll("\\s*", "");//用户名
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
			lock = WebHelper.addLock(smallSellerCode, 60);
			if(StringUtils.isNotEmpty(lock)) {
				/* 系统当前时间 */
				String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
				/* 获取当前登录人 */
				String create_user = UserFactory.INSTANCE.create().getUserCode();
				
				MDataMap merchantMap = new MDataMap();
				merchantMap.put("seller_code", sellerCode);
				merchantMap.put("small_seller_code", smallSellerCode);
				merchantMap.put("seller_company_name", mAddMaps.get("seller_company_name"));
				merchantMap.put("seller_name", mAddMaps.get("seller_company_name"));
				merchantMap.put("seller_short_name", shortName);
				merchantMap.put("seller_status", "4497172300040004");
				merchantMap.put("editId", create_user);
				merchantMap.put("flow_status", "4497172300140001");//合同专员等待审批
				DbUp.upTable("uc_sellerinfo").dataInsert(merchantMap);// 把商户信息添加到表中
				
				mDataMap.put("zw_f_create_user", create_user);
				mDataMap.put("zw_f_create_time", createTime);
				mDataMap.put("zw_f_update_user", create_user);
				mDataMap.put("zw_f_update_time", createTime);
				mDataMap.put("zw_f_small_seller_code", smallSellerCode);
				mDataMap.put("zw_f_register_name", mAddMaps.get("register_name"));
				if (!"0".equals(mAddMaps.get("money_proportion"))) {
					mDataMap.put("zw_f_money_proportion", new BigDecimal(mAddMaps.get("money_proportion")).divide(new BigDecimal(100)).toString());//由于质保金比例在存入数据库中是小数，所以需要除以100再存
					mAddMaps.put("money_proportion",  new BigDecimal(mAddMaps.get("money_proportion")).divide(new BigDecimal(100)).toString());	//提交审核后改变草稿箱数据
				}else{
					mDataMap.put("zw_f_money_proportion","0");
					mAddMaps.put("money_proportion", "0");
				}
				mDataMap.put("zw_f_register_time", createTime);
				mDataMap.put("zw_f_seller_short_name", shortName);
				mDataMap.put("zw_f_user_name", userName);
				mDataMap.remove("zw_f_seller_code");
				// 判断结算周期是不是自定义
				if (!"4497478100030006".equals(mAddMaps.get("account_clear_type"))) {
					// 不是自定义,清空账单节点和天数字段
					mDataMap.put("zw_f_bill_point", "");
					mDataMap.put("zw_f_bill_day", "");
					mAddMaps.put("bill_point", "");
					mAddMaps.put("bill_day", "");
				}
				
				mResult = super.funcDo(sOperateUid, mDataMap);// 把商户信息添加到扩展表
				
//				int count = DbUp.upTable("uc_seller_info_extend").count("small_seller_code", smallSellerCode);
				/**
				 * 由于读写分离，导致无法提交审核，修改数据获取方式 2016-11-16 zhy
				 */
				Map<String, Object> map = DbUp.upTable("uc_seller_info_extend").upTemplate().queryForMap("select count(1) as c from usercenter.uc_seller_info_extend where small_seller_code=:small_seller_code", new MDataMap("small_seller_code", smallSellerCode));
	            int count = Integer.valueOf(map.get("c").toString());
				if(mResult.upFlagTrue()==false || count==0) {
					DbUp.upTable("uc_sellerinfo").delete("small_seller_code", smallSellerCode);
					DbUp.upTable("uc_seller_info_extend").delete("small_seller_code", smallSellerCode);
					mResult.setResultMessage("提交审核失败:"+mResult.getResultMessage());
					WebHelper.unLock(smallSellerCode);
					return mResult;
				}
				
				//修改草稿箱数据
				smallSellerCodeCount = DbUp.upTable("uc_seller_info_extend_draft").count("small_seller_code", smallSellerCode);
				if(smallSellerCodeCount>0) {
//					MDataMap mDataMap2 = new MDataMap();
//					mDataMap2.put("small_seller_code", smallSellerCode);
					mAddMaps.put("flag_del", "4497478100040002");
					DbUp.upTable("uc_seller_info_extend_draft").dataUpdate(mAddMaps, "", "uid");
				}
				//加入审批的流程
				ScFlowMain flow = new ScFlowMain();
				flow.setCreator(create_user);
				flow.setCurrentStatus("4497172300140001");
				String title = bInfo(916423014, smallSellerCode);
				flow.setFlowTitle(smallSellerCode);
				flow.setFlowType("449717230014");
				flow.setFlowUrl("");
				flow.setOuterCode(smallSellerCode);
				flow.setFlowRemark(title);
				flow.setNext_operator_id("");
				//创建的审批流程
				new FlowService().CreateFlow(flow);

			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		WebHelper.unLock(lock);
		return mResult;
	}
}