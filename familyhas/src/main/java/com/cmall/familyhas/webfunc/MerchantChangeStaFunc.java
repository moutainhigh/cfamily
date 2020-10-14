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
 * @ClassName: MerchantChangeStaFunc
 * @Description: 冻结或解冻商户
 * @author 张海生
 * @date 2015-6-11 上午9:44:14
 * 
 */
public class MerchantChangeStaFunc extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		MDataMap pMap = DbUp.upTable("uc_seller_info_extend").oneWhere("business_status", "", "", "uid", uid);
		Map<String,Object> resutlMap = DbUp.upTable("za_userinfo").dataSqlOne("select zu.flag_enable as flag,zu.user_name as user_name from zapdata.za_userinfo zu,usercenter.uc_seller_info_extend ue where zu.user_name=ue.user_name and ue.uid=:uid", new MDataMap("uid",uid));
		if("2".equals(resutlMap.get("flag").toString())) {
			result.setResultCode(0);
			result.setResultMessage("请先开启合作！");
			return result;
		}
		if (pMap != null) {
			MDataMap updataMap = new MDataMap();
			String businessStatus = pMap.get("business_status");
			if ("4497471600090001".equals(businessStatus)) {// 未冻结改为已冻结
				updataMap.put("business_status", "4497471600090002");
			} else {// 已冻结改为未冻结
				updataMap.put("business_status", "4497471600090001");
			}
			updataMap.put("uid", uid);
			int count = DbUp.upTable("uc_seller_info_extend").dataUpdate(updataMap, "business_status", "uid");
			if (count > 0) {
//				MDataMap pMap1 = DbUp.upTable("uc_seller_info_extend")
//						.oneWhere("business_status,user_name,small_seller_code", "", "", "uid", uid);//
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
//						uMap.put("flag_enable", "1");// 用户信息改为可用
//						DbUp.upTable("za_userinfo").dataUpdate(uMap, "flag_enable", "user_name");
						String outUserSql = "update za_userinfo set flag_enable = '1',cookie_user='' where user_name=:user_name ";
						DbUp.upTable("za_userinfo").dataExec(outUserSql, uMap);
					} else {
//						uMap.put("flag_enable", "0");// 用户信息改为不可用
//						DbUp.upTable("za_userinfo").dataUpdate(uMap, "flag_enable", "user_name");
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
			}
		} else {
			result.setResultCode(0);
			result.setResultMessage("操作失败");
		}
		return result;
	}

	/**
	 * 
	 * 方法: productDraftBySmallSellerCode <br>
	 * 描述: 查询冻结商户的发布商品审核流程中的商品，将商品存放到草稿箱，结束流程 <br>
	 * 作者: 张海宇 zhanghaiyu@huijiayou.cn<br>
	 * 时间: 2016年7月25日 下午5:40:32
	 * 
	 * @param small_seller_code
	 */
	private void productDraftBySmallSellerCode(String small_seller_code) {
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		// 根据商户编号查询所有审核中的商品列表
		//String sql = "SELECT ppf.uid,ppf.product_code,ppf.product_json,sfm.flow_code,ppf.flow_status FROM productcenter.pc_productinfo AS ppi LEFT JOIN productcenter.pc_productflow AS ppf ON ppi.product_code=ppf.product_code LEFT JOIN systemcenter.sc_flow_main AS sfm ON ppf.product_code = sfm.outer_code AND sfm.flow_isend = 0 WHERE ppi.seller_code = 'SI2003' AND ppf.flow_status in('10','20') AND ppi.product_status in('4497153900060001','4497153900060003') AND ppi.small_seller_code=:small_seller_code";
		String sql = "SELECT ppf.uid,ppf.product_code,ppf.product_json,sfm.flow_code,ppf.flow_status,ppi.uid puid FROM systemcenter.sc_flow_main AS sfm,productcenter.pc_productflow AS ppf,productcenter.pc_productinfo AS ppi WHERE ppf.product_code = sfm.outer_code AND ppi.product_code = ppf.product_code AND sfm.flow_isend = 0 AND ppi.seller_code = 'SI2003' AND ppf.flow_status IN ('10', '20') AND ppi.product_status IN ('4497153900060001','4497153900060003') AND ppi.small_seller_code = :small_seller_code";
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
				
					//修改为平台强制下架 2017-03-10 zhy
					MDataMap product = new MDataMap();
					product.put("product_code", map.get("product_code").toString());
//					product.put("product_status", "4497153900060001");
					product.put("product_status", "4497153900060004");
					DbUp.upTable("pc_productinfo").dataUpdate(product, "product_status", "product_code");
					
					// 添加商品流程历史记录
					UUID uuid2 = UUID.randomUUID();
					
					MDataMap insertDatamap = new MDataMap();
					
					insertDatamap.put("uid", uuid2.toString().replace("-", ""));
					insertDatamap.put("flow_code", map.get("puid").toString());
					insertDatamap.put("flow_type", "449717230011");
					insertDatamap.put("creator", userInfo.getUserCode());
					insertDatamap.put("create_time", DateUtil.getSysDateTimeString());
					insertDatamap.put("flow_remark", "商户冻结，驳回审核中的商品到草稿箱");
					insertDatamap.put("current_status",  "4497172300160010");
					DbUp.upTable("sc_flow_bussiness_history").dataInsert(insertDatamap);
					
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
					// 当前状态：商户冻结驳回4497172300160010,库里没有这个状态
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
//			String productCode = WebHelper.upCode(ProductService.ProductHead);
			// 根据商户编码查询商户类型 2016-11-14 zhy
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
