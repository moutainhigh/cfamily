package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.productcenter.model.PcProductflow;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.ProductChangeFlag;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.MailSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 同步沙皮狗的商品信息、SKU信息、库存信息（不包含库存数）到沙皮狗
 * 
 * @author ligj
 * 
 */
public class FuncRnsyProductInfoSharpei extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String sharpeiSellerCode = "SI3003";
		String familyhasSellerCode = "SI2003";
		String productCodePrefix = "6";
		String familyhasCategoryCode = "1"; // 等惠家有商品增加添加sku功能时这个值就需要改变一下了
		String rnsyProductCode = mDataMap.get("zw_f_product_code"); // 需要同步上商品编号
		MDataMap productInfoTarget = new MDataMap();//源商品的商品信息
		// 商品编号为空，提示信息：商品编号错误，查不到商品信息！
		if (StringUtils.isBlank(rnsyProductCode)) {
			mResult.inErrorMessage(916421263);
			return mResult;
		}
		if (mResult.upFlagTrue()) {
			// 已经同步过的商品会进行更新操作
			String sWhereFilter = " product_code='" + rnsyProductCode
					+ "' or product_code='" + productCodePrefix
					+ rnsyProductCode + "'";
			List<MDataMap> productMapList = DbUp
					.upTable("pc_productinfo")
					.queryAll(
							"uid,product_code,seller_code,small_seller_code,product_status,product_code_copy",
							"", sWhereFilter, null);
			int addFlag = 1; // 为1时进行复制新增，0进行更新操作,2沙皮狗商品为上架状态，无法操作
			int dogFlag = 1; // 是否为有来源商品编号的商品，1是，0否,为是时不允许同步
			// 查不到商品信息时返回错误，提示信息：商品编号错误，查不到商品信息！
			if (productMapList == null || productMapList.isEmpty()) {
				mResult.inErrorMessage(916421263);
				return mResult;
			}
			for (MDataMap productMap : productMapList) {
				String sellerCode = productMap.get("seller_code");
				String productCodeCopy = productMap.get("product_code_copy");
				// 判断是否沙皮狗商品,目前非沙皮狗商品的商品来源字段都为非空
				if (StringUtils.isEmpty(productCodeCopy)
						&& sellerCode.equals(sharpeiSellerCode)) {
					dogFlag = 0;
				}
				// 判断进行新增操作还是更新操作
				if (sellerCode.equals(familyhasSellerCode)) {
					addFlag = 0;
					productInfoTarget = productMap;
				}
			}
			// 有来源商品编号的商品不允许被同步，提示信息：此商品不允许被同步，请查看该商品的来源。
			if (dogFlag == 1) {
				mResult.inErrorMessage(916421264);
				return mResult;
			}
			// ----------
			// 开始进行同步---->
			// ----------
			ProductService ps = new ProductService();
			PcProductinfo product = ps.getProduct(rnsyProductCode);
			String newProductCode = (productCodePrefix + product
					.getProductCode());

			product.setSellerCode(familyhasSellerCode); // sell_code
			product.setProductCodeCopy(product.getProductCode());
			product.setProductCode(newProductCode); // productCode
			product.getDescription().setProductCode(newProductCode); // description
			if (null != product.getPcPicList()) {
				for (int i = 0; i < product.getPcPicList().size(); i++) {
					product.getPcPicList().get(i)
							.setProductCode(newProductCode); // picList
					product.getPcPicList().get(i).setZid(0);
					product.getPcPicList().get(i).setUid("");
				}
			}
			if (null != product.getUsprList()) {
				for (int i = 0; i < product.getUsprList().size(); i++) {
					product.getUsprList().get(i).setProductCode(newProductCode); // 店铺商品分类关系usprList
					product.getUsprList().get(i)
							.setSellerCode(familyhasSellerCode);
				}
			}
			if (null != product.getPcProductpropertyList()) {
				for (int i = 0; i < product.getPcProductpropertyList().size(); i++) {
					product.getPcProductpropertyList().get(i)
							.setProductCode(newProductCode); // 商品关联属性信息pcProductpropertyList
					product.getPcProductpropertyList().get(i).setZid(0);
					product.getPcProductpropertyList().get(i).setUid("");
				}
			}
			if (null != product.getProductSkuInfoList()) {
				List<String> skuCodeOldArr = new ArrayList<String>();
				for (int i = 0; i < product.getProductSkuInfoList().size(); i++) {
					skuCodeOldArr.add(product.getProductSkuInfoList().get(i)
							.getSkuCode());
				}
				// 为更新操作时查到惠家有商品的sku编号与沙皮狗商品的sku编号的对应关系
				Map<String, String> skuCodeMap = new HashMap<String, String>();
				if (addFlag == 0) {
					List<MDataMap> skuCodeMapList = DbUp.upTable("pc_skuinfo")
							.queryAll(
									"sku_code,sku_code_old",
									"",
									"sku_code_old in ('"
											+ StringUtils.join(skuCodeOldArr,
													"','") + "')", null);
					for (MDataMap mDataMap2 : skuCodeMapList) {
						skuCodeMap.put(mDataMap2.get("sku_code_old"),
								mDataMap2.get("sku_code"));
					}

				}
				for (int i = 0; i < product.getProductSkuInfoList().size(); i++) {
					product.getProductSkuInfoList().get(i)
							.setProductCode(newProductCode); // 商品的Sku列表的属性信息productSkuInfoList
					product.getProductSkuInfoList().get(i)
							.setSellerCode(familyhasSellerCode);
					product.getProductSkuInfoList()
							.get(i)
							.setSkuCodeOld(
									product.getProductSkuInfoList().get(i)
											.getSkuCode());
					product.getProductSkuInfoList().get(i).setUid("");
					// 修改商品编号
					if (addFlag == 0) {
						product.getProductSkuInfoList()
								.get(i)
								.setSkuCode(
										skuCodeMap.get(product
												.getProductSkuInfoList().get(i)
												.getSkuCode()));
					}
				}
			}

			// 扩展信息
			{
				product.getPcProductinfoExt().setProductCode(newProductCode);
			}

			product.getPcProductcategoryRel().setProductCode(newProductCode); // 实类
			product.getPcProductcategoryRel().setFlagMain(1);
			product.getPcProductcategoryRel().setCategoryCode(
					familyhasCategoryCode);

			String JSON = new JsonHelper<PcProductinfo>().ObjToString(product);
			if (null == product.getPcProdcutflow()) {
				product.setPcProdcutflow(new PcProductflow());
			}
			product.getPcProdcutflow().setProductCode(newProductCode);
			product.getPcProdcutflow().setProductJson(JSON);
			StringBuffer error = new StringBuffer();
			// 因为有前缀“6”，所以此处的长度必须要大于1才进行添加操作
			if (product.getProductCode().length() > 1) {
				if (addFlag == 1) {
					product.setProductStatus("4497153900060003");
					product.setFlagSale(0);
					product.setZid(0);
					product.setUid("");
					ps.AddProductSharPei(product, error);
				} else if (addFlag == 0) {
					MUserInfo userInfo = null;
					String manageCode = "";
					String userCode = "";
					if (UserFactory.INSTANCE != null) {
						try {
							userInfo = UserFactory.INSTANCE.create();
						} catch (Exception e) {
						}

						if (userInfo != null) {
							manageCode = userInfo.getManageCode();
							userCode = userInfo.getUserCode();
							if (userCode == null || "".equals(userCode)) {
								userCode = manageCode;
							}
						}
					}
					ps.UpdateProductTx(product, error, userCode,
							new ProductChangeFlag());
					// 商品下架
					if ("4497153900060002".equals(productInfoTarget.get("product_status"))) {
						String flowBussinessUid = productInfoTarget.get("uid"); // 商品Uid
						String toStatus = "4497153900060003"; // 更改到的状态
						String flowType = "449715390006"; // 流程类型449715390006：商家后台商品状态
						String remark = "沙皮狗互联互通更新商品自动下架";
						// 更新状态
						MDataMap updMap = new MDataMap();
						updMap.put("uid", flowBussinessUid);
						updMap.put("flag_sale", "0");
						updMap.put("update_time",
								DateUtil.getSysDateTimeString());
						updMap.put("product_status", toStatus);
						int retcode = DbUp.upTable("pc_productinfo")
								.dataUpdate(updMap,
										"flag_sale,update_time,product_status",
										"uid");
						if (1 == retcode) {
							MDataMap insertDatamap = new MDataMap();
							insertDatamap.put("uid", UUID.randomUUID()
									.toString().replace("-", ""));
							insertDatamap.put("flow_code", flowBussinessUid);
							insertDatamap.put("flow_type", flowType);
							insertDatamap.put("creator", userCode);
							insertDatamap.put("create_time",
									DateUtil.getSysDateTimeString());
							insertDatamap.put("flow_remark", remark);
							insertDatamap.put("current_status", toStatus);
							DbUp.upTable("sc_flow_bussiness_history")
									.dataInsert(insertDatamap);

							// 商品下架发送邮件
							String mail_receive = bConfig("productcenter.mail_receive_familyhas"); // 邮件收件人
							String mail_title = bConfig("productcenter.mail_title_familyhas"); // 邮件标题
							String mail_content = bConfig("productcenter.mail_content_familyhas"); // 邮件内容
							for (String receiveUser : mail_receive.split(",")) {
								MailSupport.INSTANCE
										.sendMail(
												receiveUser,
												FormatHelper.formatString(
														mail_title,
														product.getProductCode(),
														product.getProductName(),
														DateUtil.getSysDateTimeString()),
												FormatHelper.formatString(
														mail_content,
														product.getProductCode(),
														product.getProductName(),
														DateUtil.getSysDateTimeString()));
							}
						}
					}
				}
				PlusHelperNotice.onChangeProductInfo(product.getProductCode());
				// 触发消息队列
				ProductJmsSupport pjs = new ProductJmsSupport();
				pjs.onChangeForProductChangeAll(product.getProductCode());
			}
			// 添加或更新发生错误
			if (StringUtils.isNotBlank(error.toString())) {
				mResult.inErrorMessage(951001004, error.toString());
			}
		}
		return mResult;
	}

}
