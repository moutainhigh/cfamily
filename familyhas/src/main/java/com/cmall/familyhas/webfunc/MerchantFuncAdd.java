package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.Random;

import com.cmall.familyhas.util.PinyinUtil;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class MerchantFuncAdd extends FuncAdd {

	/**
	   * 产生随机字符串
	   * */
	private static Random randGen = new Random();;
	private static char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz").toCharArray();

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		/* 商户编号 */
		String smallSellerCode = WebHelper.upCode("SF03");
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
		MDataMap merchantMap = new MDataMap();
		merchantMap.put("seller_code", "SI2003");
		merchantMap.put("small_seller_code", smallSellerCode);
		merchantMap.put("seller_company_name",
				mAddMaps.get("seller_company_name"));
		merchantMap.put("seller_name", mAddMaps.get("seller_company_name"));
		merchantMap.put("seller_short_name", shortName);
		merchantMap.put("seller_status", "4497172300040004");
		merchantMap.put("editId", UserFactory.INSTANCE.create().getUserCode());
		DbUp.upTable("uc_sellerinfo").dataInsert(merchantMap);// 把商户信息添加到表中
		/* 系统当前时间 */
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_create_user", create_user);
		mDataMap.put("zw_f_create_time", createTime);
		mDataMap.put("zw_f_update_user", create_user);
		mDataMap.put("zw_f_update_time", createTime);
		mDataMap.put("zw_f_small_seller_code", smallSellerCode);
		try {
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
			mDataMap.put("zw_f_register_name", mAddMaps.get("register_name"));
			BigDecimal moneyProportion = new BigDecimal(mAddMaps.get("money_proportion"));
			String moneyProportionString = moneyProportion.divide(new BigDecimal(100)).toString();//由于质保金比例在存入数据库中是小数，所以需要除以100再存
			mDataMap.put("zw_f_money_proportion", moneyProportionString);
			mDataMap.put("zw_f_register_time", createTime);
			mDataMap.put("zw_f_seller_short_name", shortName);
			mDataMap.put("zw_f_user_name", userName);
			mResult = super.funcDo(sOperateUid, mDataMap);// 把商户信息添加到扩展表
			if (mResult.upFlagTrue()) {
				String nowTime = DateUtil.getNowTime();
				String user_code = WebHelper.upCode("UI");
				MDataMap insertDatamap = new MDataMap();
				insertDatamap.put("user_code", user_code);
				insertDatamap.put("manage_code", smallSellerCode);
				insertDatamap.put("create_time", nowTime);
				insertDatamap.put("user_name", userName);
				insertDatamap.put("real_name", shortName);
				insertDatamap.put("user_password",
						SecrurityHelper.MD5Customer(bConfig("familyhas.dsf_password")));//密码默认88888888
				// //
				insertDatamap.put("user_type_did", "467721200003");
				
				MDataMap updateDatamap2 = new MDataMap();
				updateDatamap2.put("role_code", "4677031800020001");
				updateDatamap2.put("user_code", user_code);
				try {
					DbUp.upTable("za_userinfo").dataInsert(insertDatamap);
					DbUp.upTable("za_userrole").dataInsert(updateDatamap2);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

	public static void main(String[] args) {
		// ChineseCharToEn cte = new ChineseCharToEn();
		// System.out.println("获取拼音首字母："+ cte.getAllFirstLetter("北京联席办"));
//		System.out.println(MerchantFuncAdd.getPingYin("乾和bp晟云"));
	}

}
