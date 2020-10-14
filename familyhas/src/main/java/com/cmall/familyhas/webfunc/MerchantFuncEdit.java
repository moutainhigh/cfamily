package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.AddShuiyin;
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
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;

public class MerchantFuncEdit extends FuncEdit {
	
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
		MDataMap updataMap = new MDataMap();
		String smallSellerCode = mDataMap.get("zw_f_small_seller_code");
		String shortName = mDataMap.get("zw_f_seller_short_name").trim();
		String shortNameFlag = shortName;
		String uid = mDataMap.get("zw_f_uid");
		/* 公司名称 */
		String smallCompanyName = mDataMap.get("zw_f_seller_company_name");
		/* 注册号 */
		String registrationNumber = mDataMap.get("zw_f_registration_number");
		/* 商户类型 */
		String ucSellerType = mDataMap.get("zw_f_uc_seller_type");
		
		/* 528：商户状态bug修改 */
		MDataMap paramMap = new MDataMap("zw_f_uid",uid);
		this.changeBusinessStatus(paramMap);
		
		
		if(StringUtils.isEmpty(uid)||smallSellerCode!="") {
			if(!smallSellerCode.equals("")){
				MDataMap uids = new MDataMap();
				uids.put("uid", mDataMap.get("zw_f_uid"));
				String smallCompanyNames = (String) DbUp.upTable("uc_seller_info_extend").dataGet("seller_company_name", null, uids);
				String registrationNumbers = (String) DbUp.upTable("uc_seller_info_extend").dataGet("registration_number", null, uids);
				if(!registrationNumber.equals(registrationNumbers)){
					//查询商户注册号是否存在
					int registrationNumberCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type",ucSellerType,"registration_number",registrationNumber);
					if(registrationNumberCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
				}
				if(!smallCompanyName.equals(smallCompanyNames)){
					//查询商户公司名称是否存在
					int smallCompanyNameCount = DbUp.upTable("uc_seller_info_extend").count("uc_seller_type",ucSellerType,"seller_company_name", smallCompanyName);
					if(smallCompanyNameCount>0) {
						mResult.setResultMessage("商户信息已存在");
						return mResult;
					}
				}
			}
		}

		for (; ;) {
			MDataMap whereMap = new MDataMap();
			whereMap.put("shortName", shortName);
			whereMap.put("smallSellerCode", smallSellerCode);
			int count = DbUp.upTable("uc_sellerinfo").dataCount("seller_short_name=:shortName and small_seller_code !=:smallSellerCode", whereMap);
			if(count == 0){//判断用户名是否重复，如果重复，从26个字符中随机取一个加子在用户名后面，再去比较
				break;
			}else{
				shortName = (shortNameFlag + numbersAndLetters[randGen.nextInt(25)]).trim();
				continue;
			}
		}
		
		mDataMap.put("zw_f_business_license_type", mDataMap.get("zw_f_business_license_type_h"));
		
		updataMap.put("seller_company_name", mDataMap.get("zw_f_seller_company_name"));
		updataMap.put("seller_name", mDataMap.get("zw_f_seller_company_name"));
		updataMap.put("seller_short_name", shortName);
		updataMap.put("small_seller_code", smallSellerCode);
		DbUp.upTable("uc_sellerinfo").dataUpdate(updataMap, "seller_company_name,seller_name,seller_short_name", "small_seller_code");//更新商户信息
		/* 系统更新时间 */
		String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前修改人 */
		String update_user = UserFactory.INSTANCE.create().getLoginName();
		
		BigDecimal moneyProportion = new BigDecimal(mDataMap.get("zw_f_money_proportion"));
		String moneyProportion1 = moneyProportion.divide(new BigDecimal(100)).toString();
		mDataMap.put("zw_f_money_proportion", moneyProportion1);//由于质保金比例在存入数据库中是小数，所以需要除以100再存
		mDataMap.put("update_time", update_time);
		mDataMap.put("update_user", update_user);
		mDataMap.put("zw_f_seller_short_name", shortName);
		//审核通过不修改登记时间 2016-07-11 zhy
		//mDataMap.put("zw_f_register_time", update_time);
		
		mDataMap.remove("zw_f_seller_code");//由于扩展表中没有seller_code这个字段，所以要移除
		String perstr = mDataMap.get("zw_f_upload_business_license");//未加水印的商户执照
		byte[] markImageByMoreIcon = AddShuiyin.markImageByMoreIcon(MerchantApproveFunc.class.getResourceAsStream("/shuiyin.png"), perstr, "jpg", null);
		MWebResult remoteUpload = WebUpload.create().remoteUpload("upload", perstr.substring(perstr.lastIndexOf("/")+1), markImageByMoreIcon);
		mDataMap.put("zw_f_upload_business_license", remoteUpload.getResultObject().toString());
		
		// 结算周期信息不支持修改
		mDataMap.remove("zw_f_account_clear_type");
		mDataMap.remove("zw_f_bill_point");
		mDataMap.remove("zw_f_bill_day");
		
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);//更新商户扩展信息
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

	private void changeBusinessStatus(MDataMap paramMap) {
		MWebResult result = new MWebResult();
		String uid = paramMap.get("zw_f_uid");
		MDataMap pMap = DbUp.upTable("uc_seller_info_extend").oneWhere("business_status", "", "", "uid", uid);
		if (pMap != null) {
//			MDataMap pMap1 = DbUp.upTable("uc_seller_info_extend")
//			.oneWhere("business_status,user_name,small_seller_code", "", "", "uid", uid);//
			/**
			 * 读写分离影响查询 2016-11-30 zhy
			 */
			Map<String, String> param = new HashMap<String, String>();
			param.put("uid", uid);
			Map<String, Object> map = DbUp.upTable("uc_seller_info_extend").upTemplate().queryForMap("select business_status,user_name,small_seller_code from usercenter.uc_seller_info_extend where uid=:uid",param);
			MDataMap pMap1 = null;
			if(map != null){
				pMap1 = new MDataMap(map);
			}
			if (pMap1 != null) {
				String businessStatus1 = pMap1.get("business_status");
				String userName = pMap1.get("user_name");
				MDataMap uMap = new MDataMap();
				uMap.put("user_name", userName);
				if ("4497471600090001".equals(businessStatus1)) {// 未冻结
					uMap.put("flag_enable", "1");// 用户信息改为可用
					DbUp.upTable("za_userinfo").dataUpdate(uMap, "flag_enable", "user_name");
				} else {
		//			uMap.put("flag_enable", "0");// 用户信息改为不可用
		//			DbUp.upTable("za_userinfo").dataUpdate(uMap, "flag_enable", "user_name");
					//528：清除cookie 使登录用户退出登录，权限的实时冻结
					String outUserSql = "update za_userinfo set flag_enable = '0',cookie_user='' where user_name=:user_name ";
					DbUp.upTable("za_userinfo").dataExec(outUserSql, uMap);
					MDataMap mWhereMap = new MDataMap();
					mWhereMap.put("small_seller_code", pMap1.get("small_seller_code"));
					mWhereMap.put("product_status", "4497153900060002");
					List<MDataMap> proMapList = DbUp.upTable("pc_productinfo").queryAll("uid,product_code", "", "",
							mWhereMap);
					MDataMap changeStatusMap = new MDataMap();
					for (MDataMap mDataMap2 : proMapList) {
						changeStatusMap.put("flow_bussinessid", mDataMap2.get("product_code"));
						changeStatusMap.put("from_status", "4497153900060002");
						changeStatusMap.put("to_status", "4497153900060003");
						changeStatusMap.put("flow_type", "449715390007");
						changeStatusMap.put("remark", "商户冻结商品下架");
						new FlowBussinessService().ChangeFlow(mDataMap2.get("uid"), "449715390007",
								"4497153900060002", "4497153900060003", UserFactory.INSTANCE.create().getUserCode(),
								"商户冻结商品下架", changeStatusMap);// 下架商品
					}
					// 商户被冻结，将审批中的商品均更改为商户冻结驳回
					productDraftBySmallSellerCode(pMap1.get("small_seller_code"));
				}
			}			
			
		} else {
			result.setResultCode(0);
			result.setResultMessage("操作失败");
		}
		
	}
	
	private void productDraftBySmallSellerCode(String small_seller_code) {
		MUserInfo userInfo = UserFactory.INSTANCE.create();
	    String sql = "SELECT ppf.uid,ppf.product_code,ppf.product_json,sfm.flow_code,ppf.flow_status FROM systemcenter.sc_flow_main AS sfm,productcenter.pc_productflow AS ppf,productcenter.pc_productinfo AS ppi WHERE ppf.product_code = sfm.outer_code AND ppi.product_code = ppf.product_code AND sfm.flow_isend = 0 AND ppi.seller_code = 'SI2003' AND ppf.flow_status IN ('10', '20') AND ppi.product_status IN ('4497153900060001','4497153900060003') AND ppi.small_seller_code = :small_seller_code";
		List<Map<String, Object>> list = DbUp.upTable("pc_productinfo").dataSqlList(sql,
				new MDataMap("small_seller_code", small_seller_code));
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (map != null && map.get("product_code") != null && !"".equals(map.get("product_code").toString())) {
					// 将商品存入草稿箱内
					String flstatus = map.get("flow_status").toString();
					if (SkuCommon.ProAddInit.equals(flstatus)) {// 新增商品处理
						map.put("flow_status", SkuCommon.ProAddOrRe);
					} else if (SkuCommon.ProUpaInit.equals(flstatus)) {// 修改商品处理
						map.put("flow_status", SkuCommon.ProUpaOrRe);
					}
					// 查询在草稿箱中是否存在未删除的商品信息
					Map<String, Object> draftbox = DbUp.upTable("pc_product_draftbox").dataSqlOne(
							"select product_code from productcenter.pc_product_draftbox where product_code=:product_code and flag_del=:flag_del",
							new MDataMap("product_code", map.get("product_code").toString(), "flag_del",
									"449746250002"));
					if (draftbox != null) {
						// 如果存在进行编辑操作
						MDataMap draftMap = new MDataMap();
						draftMap.put("product_code", map.get("product_code").toString());
						draftMap.put("flag_del", "449746250002");
						DbUp.upTable("pc_product_draftbox").dataUpdate(draftMap, "flag_del", "product_code");
					} else {
						// 添加商品到草稿箱
						String productJson = map.get("product_json").toString();
						MDataMap draft = this.getProductDraft(productJson);
						DbUp.upTable("pc_product_draftbox").dataInsert(draft);
					}

					MDataMap product = new MDataMap();
					product.put("product_code", map.get("product_code").toString());

					product.put("product_status", "4497153900060004");
					DbUp.upTable("pc_productinfo").dataUpdate(product, "product_status", "product_code");
					// 添加审批流程历史
					MDataMap flowHistory = new MDataMap();
					flowHistory.put("flow_code", map.get("flow_code").toString());
					flowHistory.put("flow_type", "449717230011");
					flowHistory.put("creator", userInfo.getUserCode());
					flowHistory.put("create_time", DateUtil.getSysDateTimeString());
					flowHistory.put("flow_remark", "商户冻结，驳回审核中的商品到草稿箱");
					flowHistory.put("current_status", "4497172300160010");
					DbUp.upTable("sc_flow_history").dataInsert(flowHistory);
					// 结束审批流程
					MDataMap flowMain = new MDataMap();
					flowMain.put("flow_isend", "1");
					flowMain.put("flow_code", map.get("flow_code").toString());
					flowMain.put("updator", userInfo.getUserCode());
					flowMain.put("update_time", DateUtil.getSysDateTimeString());
					// 当前状态：商户冻结驳回
					flowMain.put("current_status", "4497172300160010");
					DbUp.upTable("sc_flow_main").dataUpdate(flowMain, "flow_isend,updator,update_time,current_status",
							"flow_code");
				}
			}
			// 批处理pc_productflow
			DbUp.upTable("pc_productflow").dataExec(
					"UPDATE productcenter.pc_productflow SET flow_status = flow_status + 5 WHERE product_code IN (SELECT product_code FROM productcenter.pc_productinfo WHERE small_seller_code =:small_seller_code)AND flow_status IN (10, 20)",
					new MDataMap("small_seller_code", small_seller_code));
		}
	}
	
	private MDataMap getProductDraft(String product_json) {
		MDataMap draft = new MDataMap();
		PcProductinfo product = new PcProductinfo();
		JsonHelper<PcProductinfo> pHelper = new JsonHelper<PcProductinfo>();
		product = pHelper.StringToObj(product_json, product);
		String sysTime = DateUtil.getSysDateTimeString();
		String flowStatus = "449747670001";
		int flag = 0;
		if (flag == 0 && "449747670002".equals(flowStatus)) {

			MDataMap seller = DbUp.upTable("uc_seller_info_extend").oneWhere("uc_seller_type", "", "", "small_seller_code",
					product.getSmallSellerCode());
			String code_start = bConfig("productcenter.product" + seller.get("uc_seller_type"));
			String productCode = WebHelper.upCode(code_start);
			product.setProductCode(productCode);
		}
		if (product.getProductSkuInfoList() != null) {
			int size = product.getProductSkuInfoList().size();
			BigDecimal tempMinCostPrice = new BigDecimal(0.00);
			BigDecimal tempMaxCostPrice = new BigDecimal(0.00);

			BigDecimal tempMinSellPrice = new BigDecimal(0.00);
			BigDecimal tempMaxSellPrice = new BigDecimal(0.00);
			for (int i = 0; i < size; i++) {
				ProductSkuInfo pic = product.getProductSkuInfoList().get(i);
				BigDecimal costPrice = (null == pic.getCostPrice() ? BigDecimal.ZERO : pic.getCostPrice());
				if (i == 0) {
					tempMinCostPrice = costPrice;
					tempMaxCostPrice = costPrice;
				} else {
					if (tempMinCostPrice.compareTo(costPrice) == 1)
						tempMinCostPrice = costPrice;
					if (tempMaxCostPrice.compareTo(costPrice) == -1)
						tempMaxCostPrice = costPrice;
				}

				BigDecimal sellPrice = (null == pic.getSellPrice() ? BigDecimal.ZERO : pic.getSellPrice());
				if (i == 0) {
					tempMinSellPrice = sellPrice;
					tempMaxSellPrice = sellPrice;
				} else {
					if (tempMinSellPrice.compareTo(sellPrice) == 1)
						tempMinSellPrice = sellPrice;
					if (tempMaxSellPrice.compareTo(sellPrice) == -1)
						tempMaxSellPrice = sellPrice;
				}
			}
			draft.put("min_cost_price", tempMinCostPrice.toString());
			draft.put("max_cost_price", tempMinCostPrice.toString());
			draft.put("min_sell_price", tempMinCostPrice.toString());
			draft.put("max_sell_price", tempMinCostPrice.toString());
		}
		List<String> categoryCodes = new ArrayList<String>();
		// 商品虚类
		if (null != product.getUsprList() && product.getUsprList().size() > 0) {
			for (int i = 0; i < product.getUsprList().size(); i++) {
				categoryCodes.add(product.getUsprList().get(i).getCategoryCode());
			}
		}
		draft.put("product_code", product.getProductCode());
		draft.put("product_name", product.getProductName());
		draft.put("category_code", StringUtils.join(categoryCodes, ","));
		draft.put("product_status", product.getProductStatus());
		draft.put("flow_status", flowStatus);
		draft.put("product_json", product_json);
		MUserInfo userInfo = null;
		String userCode = "";
		if (UserFactory.INSTANCE != null) {
			try {
				userInfo = UserFactory.INSTANCE.create();
			} catch (Exception e) {
				userInfo = new MUserInfo();
			}

			if (userInfo != null) {
				userCode = userInfo.getUserCode();
			}
		}
		/**
		 * 增加商户编码
		 */
		draft.put("seller_code", product.getSellerCode());
		draft.put("small_seller_code", product.getSmallSellerCode());
		draft.put("create_time", sysTime);
		draft.put("creator", userCode);
		draft.put("update_time", sysTime);
		draft.put("updator", userCode);
		draft.put("flag_del", "449746250002");
		draft.put("uid", UUID.randomUUID().toString().replace("-", ""));
		return draft;
	}

}
