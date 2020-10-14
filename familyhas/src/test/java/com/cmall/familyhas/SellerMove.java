package com.cmall.familyhas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cmall.dborm.txmodel.PcQualificationInfo;
import com.cmall.productcenter.model.PcSellerQualification;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopTest;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 普通商户向平台入驻商户迁移
 * @author zht
 * SF03100414_PF03100059  2017-03-29
 * SF03100312_PF03100087  2017-04-26
 * SF03150819100001_PF03100088  2017-04-26
 * SF03100195_PF03100089  2017-04-26  @
 * SF03100154_PF03100090  2017-04-26
 * SF03100288_PF03100091 (*) 2017-04-26
 * SF03100209_PF03100092  2017-04-26
 * SF03100454_PF03100093 (*)  2017-04-27
 * SF03100275_PF03100094 (*) 2017-04-27
 * SF03100586_PF03100095 (*) 2017-04-27
 * SF03100191_PF03100096 2017-04-27
 * SF03100502_PF03100097 2017-04-27
 * SF03100613_PF03100098 (*) 2017-04-27
 * SF03100612_PF03100099 (*) 2017-04-27
 * SF03100263_PF03100100 (*) 2017-04-27
 * SF03100755_PF03100101 (未移质保金) 2017-04-27
 * 
 * SF03100136_PF03100135 (未移质保金) 2017-06-22
 * SF03100311_PF03100136 (未移质保金) 2017-06-22
 * SF03100280_PF03100137 (未移质保金) 2017-06-22
 * SF03100083_PF03100138 (未移质保金) 2017-06-22
 * SF03100091_PF03100139 (未移质保金) 2017-06-22
 * SF03100295_PF03100140 (未移质保金) 2017-06-22
 * SF03100286_PF03100141 (未移质保金) 2017-06-22
 * SF03100197_PF03100143 (未移质保金) 2017-06-22
 * SF03100233_PF03100144 (未移质保金) 2017-06-22
 * SF03100242_PF03100145 (未移质保金) 2017-06-22
 * SF03100016_PF03100146 (未移质保金) 2017-06-22
 * SF03100507_PF03100147 (未移质保金) 2017-06-22
 * SF03100281_PF03100148 (未移质保金) 2017-06-22
 * SF03100488_PF03100149 (未移质保金) 2017-06-22
 * SF03100364_PF03100150 (未移质保金) 2017-06-22
 * SF03100574_PF03100151 (未移质保金) 2017-06-22
 * SF03100071_PF03100152 (未移质保金) 2017-06-22
 * 
 * SF03100392_PF03100174 (未移质保金) 2017-07-20
 * SF03100771_PF03100175 (未移质保金) 2017-07-20
 * SF03100661_PF03100176 (未移质保金) 2017-07-20
 */
public class SellerMove extends TopTest {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			(((pc_product_draftbox)))
//			(((pc_skuprice_change_flow)))

//			uc_sellerinfo(finish)
//			uc_seller_info_extend(finish)
//			za_userinfo(finish)
//			za_usermenu(finish)
//			za_userrole(finish)
//			oc_address_info(finish)
	
//			pc_qualification_info(finish)	
//			pc_seller_qualification(finish)
//			pc_seller_qualification_draftbox(finish)	
//			uc_seller_info_extend_draft(finish)
//			pc_productinfo(finish)	
//			pc_productinfo_ext(finish)
//			pc_productdescription(finish)
//			pc_skuinfo(finish, sell_productcode 与product_code的区别)
//			sc_store_skunum(finish)
//			pc_product_md(zd_table没有这个记录)
//			pc_product_top50(finish)
//			pc_productcategory_rel(finish)
//			pc_productpic(finish)
//			pc_productproperty(finish)
//			pc_solr_exclude_product(finish)
//			uc_sellercategory_product_relation(finish)
//			pc_productinfo_create(finish)
//			pc_product_authority_logo(finish)
//			sc_flow_main
//			sc_flow_history
//			sc_flow_business_history
//			pc_product_sale_everyday
	
//			uc_sellercategory(del)
//			uc_sellercategory_brand_relation(del)
	
	public static void main(String[] args) {
		String[] vs = {"SF03100867"};
		for(String v : vs){
			SellerMove sm = new SellerMove();
			sm.copySeller(v);
		}

//		sm.moveRetentionMoney("SF03100755", "");
		
		
		//********************************************************************************************
		//迁移商户资质  
		//SF03100414(UI101228)_PF03100059(UI101744)
		//SF03100312(UI101118)_PF03100087(UI101775)
		//SF03150819100001(UI100683)_PF03100088(UI101776)
		//SF03100195(UI100978)_PF03100089(UI101777)
		//SF03100154(UI100915)_PF03100090(UI101779)
		//SF03100288(UI101091)_PF03100091(UI101780)
		//SF03100209(UI100996)_PF03100092(UI101781)
		//SF03100454(UI101288)_PF03100093(UI101782)
		//SF03100275(UI101078)_PF03100094(UI101783)
		//SF03100586(UI101382)_PF03100095(UI101784)
		//SF03100191(UI100975)_PF03100096(UI101785)
		//SF03100502(UI101317)_PF03100097(UI101787)
		//SF03100613(UI101406)_PF03100098(UI101788)
		//SF03100612(UI101410)_PF03100099(UI101789)
		//SF03100263(UI101063)_PF03100100(UI101790)
		//SF03100755(UI101555)_PF03100101(UI101791)
		
//		String smallSellerCode="SF03100755";
//		String oldUserCode = "UI101555";
//		String newSellerCode="PF03100101";
//		String newUserCode = "UI101791";
//		FileWriter logger = null;
//		try {
//			logger = new FileWriter(new File("d:/" + smallSellerCode + "_" + newSellerCode + "_qualification.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//SF03100195_PF03100089
//		sm.moveSellerQualification(smallSellerCode, newSellerCode, oldUserCode, newUserCode, logger);
		//*********************************************************************************************
	}

	public void copySeller(String smallSellerCode) {
		//商户基本信息
		//user4497478100050001=SF03
		//user4497478100050002=HF03
		//user4497478100050003=HF03
		//user4497478100050004=PF03
		
		//product4497478100050001=8016
		//sku4497478100050001=8019
		//product4497478100050002=6016
		//sku4497478100050002=6019
		//product4497478100050003=6016
		//sku4497478100050003=6019
		//product4497478100050004=7016
		//sku4497478100050004=7019
		
		String newSellerCode = WebHelper.upCode("PF03");
		
		FileWriter logger = null;
		try {
			logger = new FileWriter(new File("d:/" + smallSellerCode + "_" + newSellerCode + ".txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter productLogger = null;
		try {
			productLogger = new FileWriter(new File("d:/" + smallSellerCode +  "_" + newSellerCode +"_product" +  ".txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//复制商户信息
		Map<String, Object> sellerMap = DbUp.upTable("uc_sellerinfo").dataSqlOne("select * from uc_sellerinfo where small_seller_code=:ssc", 
				new MDataMap("ssc",smallSellerCode));
		if(sellerMap != null && !sellerMap.isEmpty()) {
			//原商户编号
			sellerMap.put("small_seller_code", newSellerCode);
			sellerMap.remove("uid");
			sellerMap.remove("zid");
			sellerMap.remove("ld_dlr_id");
			DbUp.upTable("uc_sellerinfo").dataInsert(new MDataMap(sellerMap));
			writeLog(logger, "delete from usercenter.uc_sellerinfo where small_seller_code='" + newSellerCode + "';" , true);
			System.out.println("delete from usercenter.uc_sellerinfo where small_seller_code='" + newSellerCode + "';");
		}
		
		//复制商户扩展信息
		String newUserCode = "", oldUserCode="";
		Map<String, Object> sellerExtendMap = DbUp.upTable("uc_seller_info_extend").dataSqlOne("select * from uc_seller_info_extend where small_seller_code=:ssc", 
				new MDataMap("ssc",smallSellerCode));
		if(sellerExtendMap != null && !sellerExtendMap.isEmpty()) {
			//原商户编号
			sellerExtendMap.put("small_seller_code", newSellerCode);
			sellerExtendMap.put("create_time", sdf.format(new Date()));
			//普通商户改成平台入驻商户
			sellerExtendMap.put("uc_seller_type", "4497478100050004");
			//平台用户登录名
			String userName = sellerExtendMap.get("user_name") == null ? "" : sellerExtendMap.get("user_name").toString();
			if(StringUtils.isNotBlank(userName)) {
				//用户信息
				List<Map<String,Object>> userInfoList = DbUp.upTable("za_userinfo").dataSqlList("select * from za_userinfo where manage_code=:ssc and user_name=:user_name", 
						new MDataMap("ssc",smallSellerCode, "user_name", userName));
				if(userInfoList != null && !userInfoList.isEmpty()) {
					if(userInfoList.size() > 1) {
						System.out.println("za_userinfo larger than 1");
						return ;
					}
					for(Map<String,Object> userInfoMap : userInfoList) {
						newUserCode = WebHelper.upCode("UI");
						oldUserCode = userInfoMap.get("user_code").toString();
						userInfoMap.put("user_code", newUserCode);
						userInfoMap.put("user_name", userName + "001");
						userInfoMap.put("manage_code", newSellerCode);
						userInfoMap.remove("uid");
						userInfoMap.remove("zid");
						DbUp.upTable("za_userinfo").dataInsert(new MDataMap(userInfoMap));
						writeLog(logger, "delete from zapdata.za_userinfo where manage_code='" + newSellerCode + "';", true);
						System.out.println("delete from zapdata.za_userinfo where manage_code='" + newSellerCode + "';");
						
						//用户菜单
						List<Map<String,Object>> userMenuList = DbUp.upTable("za_usermenu").dataSqlList("select * from za_usermenu where user_code=:ssc", 
								new MDataMap("ssc", oldUserCode));
						if(userMenuList != null && !userMenuList.isEmpty()) {
							//原商户编号
							for(Map<String,Object> userMenuMap : userMenuList) {
								userMenuMap.put("user_code", newUserCode);
								userMenuMap.remove("uid");
								userMenuMap.remove("zid");
								DbUp.upTable("za_usermenu").dataInsert(new MDataMap(userMenuMap));
								writeLog(logger, "delete from zapdata.za_usermenu where user_code='" + newUserCode + "';", true);
								System.out.println("delete from zapdata.za_usermenu where user_code='" + newUserCode + "';");
							}
							
						}
						
						//用户角色
						List<Map<String,Object>> userRoleList = DbUp.upTable("za_userrole").dataSqlList("select * from za_userrole where user_code=:ssc", 
								new MDataMap("ssc", oldUserCode));
						if(userRoleList != null && !userRoleList.isEmpty()) {
							//原商户编号
							for(Map<String,Object> userRoleMap : userRoleList) {
								userRoleMap.put("user_code", newUserCode);
								userRoleMap.remove("uid");
								userRoleMap.remove("zid");
								DbUp.upTable("za_userrole").dataInsert(new MDataMap(userRoleMap));
								writeLog(logger, "delete from zapdata.za_userrole where user_code='" + newUserCode + "';", true);
								System.out.println("delete from zapdata.za_userrole where user_code='" + newUserCode + "';");
							}
							
						}
						
						//za_userorganization
						List<Map<String,Object>> userOrgList = DbUp.upTable("za_userorganization").dataSqlList("select * from za_userorganization where user_code=:ssc", 
								new MDataMap("ssc", oldUserCode));
						if(userOrgList != null && !userOrgList.isEmpty()) {
							//原商户编号
							for(Map<String,Object> userOrgMap : userOrgList) {
								userOrgMap.put("user_code", newUserCode);
								userOrgMap.remove("uid");
								userOrgMap.remove("zid");
								DbUp.upTable("za_userorganization").dataInsert(new MDataMap(userOrgMap));
								writeLog(logger, "delete from zapdata.za_userorganization where user_code='" + newUserCode + "';", true);
								System.out.println("delete from zapdata.za_userorganization where user_code='" + newUserCode + "';");
							}
							
						}
					}
					
				}
				sellerExtendMap.put("user_name", userName + "001");
			}
			
			//account_clear_type
			sellerExtendMap.remove("uid");
			sellerExtendMap.remove("zid");
			DbUp.upTable("uc_seller_info_extend").dataInsert(new MDataMap(sellerExtendMap));
			writeLog(logger, "delete from usercenter.uc_seller_info_extend where small_seller_code='" + newSellerCode + "';", true);
			System.out.println("delete from usercenter.uc_seller_info_extend where small_seller_code='" + newSellerCode + "';");
		}
		
		//复制商户售后地址(new)
		List<Map<String,Object>> addressInfoList = DbUp.upTable("oc_address_info").dataSqlList("select * from oc_address_info where small_seller_code=:ssc", 
				new MDataMap("ssc", smallSellerCode));
		if(addressInfoList != null && !addressInfoList.isEmpty()) {
			//原商户编号
			for(Map<String,Object> addressInfoMap : addressInfoList) {
				addressInfoMap.put("small_seller_code", newSellerCode);
				addressInfoMap.remove("uid");
				addressInfoMap.remove("zid");
				DbUp.upTable("oc_address_info").dataInsert(new MDataMap(addressInfoMap));
				writeLog(logger, "delete from ordercenter.oc_address_info where small_seller_code='" + newSellerCode + "';", true);
				System.out.println("delete from ordercenter.oc_address_info where small_seller_code='" + newSellerCode + "';");
			}
			
		}
		
		//复制商户资质
		moveSellerQualification(smallSellerCode, newSellerCode, oldUserCode, newUserCode, logger);
		
		//复制商户草稿箱中的数据
		Map<String, Object> sellerDraftMap = DbUp.upTable("uc_seller_info_extend_draft").dataSqlOne("select * from uc_seller_info_extend_draft where small_seller_code=:ssc", 
				new MDataMap("ssc",smallSellerCode));
		if(sellerDraftMap != null && !sellerDraftMap.isEmpty()) {
			//原商户编号
			sellerDraftMap.put("small_seller_code", newSellerCode);
			//普通商户改成平台入驻商户
			sellerDraftMap.put("uc_seller_type", "4497478100050004");
			sellerDraftMap.remove("uid");
			sellerDraftMap.remove("zid");
			DbUp.upTable("uc_seller_info_extend_draft").dataInsert(new MDataMap(sellerDraftMap));
			writeLog(logger, "delete from usercenter.uc_seller_info_extend_draft where small_seller_code='" + newSellerCode + "';", true);
			System.out.println("delete from usercenter.uc_seller_info_extend_draft where small_seller_code='" + newSellerCode + "';");
		}
		
		//初始化质保金管理数据
		initSellerRetentionMoney(logger,smallSellerCode, newSellerCode);
		
		//复制商品基本信息
		List<Map<String,Object>> productList = DbUp.upTable("pc_productinfo")
				.dataSqlList("select * from pc_productinfo where small_seller_code=:ssc", new MDataMap("ssc",smallSellerCode));
		if(productList != null && !productList.isEmpty()) {
			for(Map<String, Object> productMap : productList) {
				String newProductCode = WebHelper.upCode("7016");
				System.out.println("##### Gen new product:" + newProductCode);
				
				
				String product_code_old = productMap.get("product_code_old") == null ? "" : productMap.get("product_code_old").toString();
				if(StringUtils.isBlank(product_code_old)) {
					productMap.put("product_code_old", productMap.get("product_code").toString());
				} 
				String product_code_copy = productMap.get("product_code_copy") == null ? "" : productMap.get("product_code_copy").toString();
				if(StringUtils.isNotBlank(product_code_copy)) {
					productMap.put("product_code_copy", newProductCode);
				}
				
				product_code_old = productMap.get("product_code").toString();
				String old_product_status = productMap.get("product_status").toString();
				
				productMap.put("product_code", newProductCode);
				productMap.put("small_seller_code", newSellerCode);
				String newDate = sdf.format(new Date());
				productMap.put("create_time", newDate);
				productMap.put("update_time", newDate);
				//强制下架
				productMap.put("product_status", "4497153900060004");
				productMap.remove("uid");
				productMap.remove("zid");
				DbUp.upTable("pc_productinfo").dataInsert(new MDataMap(productMap));
				
				writeLog(productLogger, product_code_old + "," + newProductCode + "," + old_product_status, true);
				writeLog(logger, "delete from productcenter.pc_productinfo where product_code='" + newProductCode  + "';", true);
				System.out.println("delete from productcenter.pc_productinfo where product_code='" + newProductCode  + "';");
				
				//复制商品扩展信息(修改结算类型,有采购类型)
				List<Map<String,Object>> productExtList = DbUp.upTable("pc_productinfo_ext")
						.dataSqlList("select * from pc_productinfo_ext where product_code=:product_code", new MDataMap("product_code", product_code_old));
				if(productExtList != null && !productExtList.isEmpty()) {
					for(Map<String, Object> productExtMap : productExtList) {
						//代收代付
						productExtMap.put("purchase_type", "4497471600160003");
						//服务费结算
						productExtMap.put("settlement_type", "4497471600110003");
						
						String productOldSubTab = productExtMap.get("product_code_old") == null ? "" : productExtMap.get("product_code_old").toString();
						if(StringUtils.isBlank(productOldSubTab)) {
							productExtMap.put("product_code_old", product_code_old);
						}
						
						productExtMap.put("dlr_id", newSellerCode);
						productExtMap.put("product_code", newProductCode);
						productExtMap.remove("uid");
						productExtMap.remove("zid");
						DbUp.upTable("pc_productinfo_ext").dataInsert(new MDataMap(productExtMap));
						writeLog(logger, "delete from productcenter.pc_productinfo_ext where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_productinfo_ext where product_code='" + newProductCode  + "';");
					}
				}
				
				//复制商品描述
				List<Map<String,Object>> productDescList = DbUp.upTable("pc_productdescription")
						.dataSqlList("select * from pc_productdescription where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productDescList != null && !productDescList.isEmpty()) {
					for(Map<String, Object> productDescMap : productDescList) {
						productDescMap.put("product_code", newProductCode);
						productDescMap.remove("uid");
						productDescMap.remove("zid");
						DbUp.upTable("pc_productdescription").dataInsert(new MDataMap(productDescMap));
						writeLog(logger, "delete from productcenter.pc_productdescription where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_productdescription where product_code='" + newProductCode  + "';");
					}
				}
				
				//复制商品7日退换信息
				List<Map<String,Object>> logoList = DbUp.upTable("pc_product_authority_logo")
						.dataSqlList("select * from pc_product_authority_logo where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(logoList != null && !logoList.isEmpty()) {
					for(Map<String, Object> logoMap : logoList) {
						logoMap.put("product_code", newProductCode);
						logoMap.remove("uid");
						logoMap.remove("zid");
						DbUp.upTable("pc_product_authority_logo").dataInsert(new MDataMap(logoMap));
						writeLog(logger, "delete from productcenter.pc_product_authority_logo where product_code='" + newProductCode  + "';", true);
						System.out.println("delete from productcenter.pc_product_authority_logo where product_code='" + newProductCode  + "';");
					}
				}
				
				
				//复制商品sku信息
				List<Map<String,Object>> skuList = DbUp.upTable("pc_skuinfo")
						.dataSqlList("select * from pc_skuinfo where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(skuList != null && !skuList.isEmpty()) {
					for(Map<String, Object> skuMap : skuList) {
						String newSkuCode = WebHelper.upCode("7019");
						
						//sell_productcode与product_code的关系
						String sku_code_old = skuMap.get("sku_code_old") == null ? "" : skuMap.get("sku_code_old").toString();
						if(StringUtils.isNotBlank(sku_code_old)) {
							skuMap.put("sku_code_old", newSkuCode);
						}
						
						sku_code_old = skuMap.get("sku_code").toString();
						skuMap.put("sku_code", newSkuCode);
						skuMap.put("product_code", newProductCode);
						skuMap.remove("uid");
						skuMap.remove("zid");
						DbUp.upTable("pc_skuinfo").dataInsert(new MDataMap(skuMap));
						writeLog(logger, "delete from productcenter.pc_skuinfo where sku_code='" + newSkuCode  + "';", true);
						System.out.println("delete from productcenter.pc_skuinfo where sku_code='" + newSkuCode  + "';");
						
						//复制sku库存表
						List<Map<String,Object>> skuStoreList = DbUp.upTable("sc_store_skunum")
								.dataSqlList("select * from sc_store_skunum where sku_code=:sku_code", new MDataMap("sku_code",sku_code_old));
						if(skuStoreList != null && !skuStoreList.isEmpty()) {
							for(Map<String, Object> skuStoreMap : skuStoreList) {
								skuStoreMap.put("sku_code", newSkuCode);
								skuStoreMap.remove("uid");
								skuStoreMap.remove("zid");
								DbUp.upTable("sc_store_skunum").dataInsert(new MDataMap(skuStoreMap));
								writeLog(logger, "delete from systemcenter.sc_store_skunum where sku_code='" + newSkuCode  + "';", true);
								System.out.println("delete from systemcenter.sc_store_skunum where sku_code='" + newSkuCode  + "';");
							}
						}
						
						//复制商品图片(有sku的情况)
						List<Map<String,Object>> productPicList = DbUp.upTable("pc_productpic")
								.dataSqlList("select * from pc_productpic where product_code=:product_code and sku_code=:sku_code", 
										new MDataMap("product_code",product_code_old, "sku_code", sku_code_old));
						if(productPicList != null && !productPicList.isEmpty()) {
							for(Map<String, Object> productPicMap : productPicList) {
								String productOldSubTab = productPicMap.get("product_code_old") == null ? "" : productPicMap.get("product_code_old").toString();
								if(StringUtils.isBlank(productOldSubTab)) {
									productPicMap.put("product_code_old", product_code_old);
								}
								
								productPicMap.put("product_code", newProductCode);
								productPicMap.put("sku_code", newSkuCode);
								productPicMap.remove("uid");
								productPicMap.remove("zid");
								DbUp.upTable("pc_productpic").dataInsert(new MDataMap(productPicMap));
								writeLog(logger, "delete from productcenter.pc_productpic where product_code='" + newProductCode  + "' and sku_code='" + newSkuCode  + "';", true);
								System.out.println("delete from productcenter.pc_productpic where product_code='" + newProductCode  + "' and sku_code='" + newSkuCode  + "';");
							}
						}
						
						//复制商品评论
						List<Map<String,Object>> commentList = DbUp.upTable("nc_order_evaluation")
								.dataSqlList("select * from nc_order_evaluation where product_code=:product_code and order_skuid=:sku_code", 
										new MDataMap("product_code",product_code_old, "sku_code", sku_code_old));
						if(commentList != null && !commentList.isEmpty()) {
							for(Map<String, Object> commentMap : commentList) {
								commentMap.put("product_code", newProductCode);
								commentMap.put("order_skuid", newSkuCode);
								commentMap.remove("uid");
								commentMap.remove("zid");
								DbUp.upTable("nc_order_evaluation").dataInsert(new MDataMap(commentMap));
								writeLog(logger, "delete from newscenter.nc_order_evaluation where product_code='" + newProductCode  + "' and order_skuid='" + newSkuCode  + "';", true);
								System.out.println("delete from newscenter.nc_order_evaluation where product_code='" + newProductCode  + "' and order_skuid='" + newSkuCode  + "';");
							}
						}
						
					}
				}
				
//				//商品和商开的关系
//				List<Map<String,Object>> productMDList = DbUp.upTable("pc_product_md")
//						.dataSqlList("select * from pc_product_md where product_code=:product_code", new MDataMap("product_code",product_code_old));
//				if(productMDList != null && !productMDList.isEmpty()) {
//					for(Map<String, Object> productMDMap : productMDList) {
//						productMDMap.put("product_code", newProductCode);
//						productMDMap.remove("uid");
//						productMDMap.remove("zid");
//						DbUp.upTable("pc_product_md").dataInsert(new MDataMap(productMDMap));
//					}
//					System.out.println("delete from productcenter.pc_product_md where product_code='" + newProductCode  + "';");
//				}
				
				//复制Top50
				List<Map<String,Object>> productTop50List = DbUp.upTable("pc_product_top50")
						.dataSqlList("select * from pc_product_top50 where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productTop50List != null && !productTop50List.isEmpty()) {
					for(Map<String, Object> productTop50Map : productTop50List) {
						productTop50Map.put("product_code", newProductCode);
						productTop50Map.remove("uid");
						productTop50Map.remove("zid");
						DbUp.upTable("pc_product_top50").dataInsert(new MDataMap(productTop50Map));
					}
					writeLog(logger, "delete from productcenter.pc_product_top50 where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_product_top50 where product_code='" + newProductCode  + "';");
				}
				
				
				//复制商品和分类关联
				List<Map<String,Object>> productCategoryList = DbUp.upTable("pc_productcategory_rel")
						.dataSqlList("select * from pc_productcategory_rel where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productCategoryList != null && !productCategoryList.isEmpty()) {
					for(Map<String, Object> productCategoryMap : productCategoryList) {
						productCategoryMap.put("product_code", newProductCode);
						productCategoryMap.remove("uid");
						productCategoryMap.remove("zid");
						DbUp.upTable("pc_productcategory_rel").dataInsert(new MDataMap(productCategoryMap));
					}
					writeLog(logger, "delete from productcenter.pc_productcategory_rel where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productcategory_rel where product_code='" + newProductCode  + "';");
				}
				
				//复制商品图片(无sku的情况)
				List<Map<String,Object>> productPicList = DbUp.upTable("pc_productpic")
						.dataSqlList("select * from pc_productpic where product_code=:product_code and sku_code = ''", new MDataMap("product_code",product_code_old));
				if(productPicList != null && !productPicList.isEmpty()) {
					for(Map<String, Object> productPicMap : productPicList) {
						String productOldSubTab = productPicMap.get("product_code_old") == null ? "" : productPicMap.get("product_code_old").toString();
						if(StringUtils.isBlank(productOldSubTab)) {
							productPicMap.put("product_code_old", product_code_old);
						}
						
						productPicMap.put("product_code", newProductCode);
						productPicMap.remove("uid");
						productPicMap.remove("zid");
						DbUp.upTable("pc_productpic").dataInsert(new MDataMap(productPicMap));
					}
					writeLog(logger, "delete from productcenter.pc_productpic where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productpic where product_code='" + newProductCode  + "';");
				}
				
				//复制商品属性
				List<Map<String,Object>> productPropertyList = DbUp.upTable("pc_productproperty")
						.dataSqlList("select * from pc_productproperty where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(productPropertyList != null && !productPropertyList.isEmpty()) {
					for(Map<String, Object> productPropertyMap : productPropertyList) {
						productPropertyMap.put("product_code", newProductCode);
						productPropertyMap.remove("uid");
						productPropertyMap.remove("zid");
						DbUp.upTable("pc_productproperty").dataInsert(new MDataMap(productPropertyMap));
					}
					writeLog(logger, "delete from productcenter.pc_productproperty where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productproperty where product_code='" + newProductCode  + "';");
				}
				
				//复制商品搜索索引表
				List<Map<String,Object>> solrExcludeProductList = DbUp.upTable("pc_solr_exclude_product")
						.dataSqlList("select * from pc_solr_exclude_product where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(solrExcludeProductList != null && !solrExcludeProductList.isEmpty()) {
					for(Map<String, Object> excludeProductMap : solrExcludeProductList) {
						excludeProductMap.put("product_code", newProductCode);
						excludeProductMap.remove("uid");
						excludeProductMap.remove("zid");
						DbUp.upTable("pc_solr_exclude_product").dataInsert(new MDataMap(excludeProductMap));
					}
					writeLog(logger, "delete from productcenter.pc_solr_exclude_product where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_solr_exclude_product where product_code='" + newProductCode  + "';");
				}
				
				//uc_sellercategory_product_relation
				List<Map<String,Object>> cprList = DbUp.upTable("uc_sellercategory_product_relation")
						.dataSqlList("select * from uc_sellercategory_product_relation where product_code=:product_code", new MDataMap("product_code",product_code_old));
				if(cprList != null && !cprList.isEmpty()) {
					for(Map<String, Object> cprMap : cprList) {
						cprMap.put("product_code", newProductCode);
						cprMap.remove("uid");
						cprMap.remove("zid");
						DbUp.upTable("uc_sellercategory_product_relation").dataInsert(new MDataMap(cprMap));
					}
					writeLog(logger, "delete from usercenter.uc_sellercategory_product_relation where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from usercenter.uc_sellercategory_product_relation where product_code='" + newProductCode  + "';");
				}
				
//				List<Map<String,Object>> createList = DbUp.upTable("pc_productinfo_create")
//						.dataSqlList("select * from pc_productinfo_create where product_code=:product_code", new MDataMap("product_code",product_code_old));
//				if(createList != null && !createList.isEmpty()) {
//					for(Map<String, Object> createMap : createList) {
//						createMap.put("product_code", newProductCode);
//						createMap.remove("uid");
//						createMap.remove("zid");
//						DbUp.upTable("pc_productinfo_create").dataInsert(new MDataMap(createMap));
//					}
//					System.out.println("delete from productcenter.pc_productinfo_create where product_code='" + newProductCode  + "';");
//				}
				
//				List<Map<String,Object>> pcDraftBoxList = DbUp.upTable("pc_product_draftbox")
//						.dataSqlList("SELECT * FROM productcenter.pc_product_draftbox WHERE flag_del = '449746250002' AND small_seller_code=:smallSellerCode AND product_code NOT IN (" +
//									 "SELECT outer_code FROM systemcenter.sc_flow_main WHERE flow_type IN ('449717230016','449717230011'))", new MDataMap("smallSellerCode", smallSellerCode));
//				if(pcDraftBoxList != null && !pcDraftBoxList.isEmpty()) {
//					for(Map<String, Object> pcDraftBoxMap : pcDraftBoxList) {
//						pcDraftBoxMap.put("product_code", newProductCode);
//						pcDraftBoxMap.remove("uid");
//						pcDraftBoxMap.remove("zid");
//						DbUp.upTable("pc_product_draftbox").dataInsert(new MDataMap(pcDraftBoxMap));
//					}
//					System.out.println("delete from usercenter.uc_sellercategory_product_relation where product_code='" + newProductCode  + "';");
//				}
				
//				复制sc_flow_main(商品质检)
				List<Map<String,Object>> flowList = DbUp.upTable("sc_flow_main")
						.dataSqlList("SELECT * FROM systemcenter.sc_flow_main WHERE flow_type IN ('449717230016','449717230011') "
								+ "AND outer_code=:product_code order by zid desc", new MDataMap("product_code",product_code_old));
				if(flowList != null && !flowList.isEmpty()) {
					boolean found =true;
					Map<String, Object> firstMap = flowList.get(0);
					String currentStatus = firstMap.get("current_status") == null ? "" : firstMap.get("current_status").toString();
					if(!StringUtils.equals(currentStatus, "4497153900060008") && !StringUtils.equals(currentStatus, "4497172300160005")) {
						found = false;	
					}
					if(found) {
						//按zid正序插入
						Collections.reverse(flowList);
						for(Map<String, Object> flowMap : flowList) {
							String creator = flowMap.get("creator") == null ? "" : flowMap.get("creator").toString();
							if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
								flowMap.put("creator", newUserCode);
							}
							
							String updator = flowMap.get("updator") == null ? "" : flowMap.get("updator").toString();
							if(StringUtils.isNotBlank(updator) && updator.equals(oldUserCode)) {
								flowMap.put("updator", newUserCode);
							}
							
							String flowTitle = flowMap.get("flow_title") == null ? "" : flowMap.get("flow_title").toString();
							if(StringUtils.isNotBlank(flowTitle) && flowTitle.contains(smallSellerCode)) {
								flowMap.put("flow_title", flowTitle.replaceAll(smallSellerCode, newSellerCode));
							}
							
							String flowRemark = flowMap.get("flow_remark") == null ? "" : flowMap.get("flow_remark").toString();
							if(StringUtils.isNotBlank(flowRemark) && flowRemark.contains(smallSellerCode)) {
								flowMap.put("flow_remark", flowRemark.replaceAll(smallSellerCode, newSellerCode));
							}
							if(StringUtils.isNotBlank(flowRemark) && flowRemark.contains(product_code_old)) {
								flowMap.put("flow_remark", flowRemark.replaceAll(product_code_old, newProductCode));
							}
							
							
							String flowCode = flowMap.get("flow_code") == null ? "" : flowMap.get("flow_code").toString();
							String flowType = flowMap.get("flow_type") == null ? "" : flowMap.get("flow_type").toString();
							
							String newFlowCode = WebHelper.upCode("SF");
							flowMap.put("outer_code", newProductCode);
							flowMap.put("flow_code", newFlowCode);
							flowMap.remove("uid");
							flowMap.remove("zid");
							DbUp.upTable("sc_flow_main").dataInsert(new MDataMap(flowMap));
							writeLog(logger, "delete from systemcenter.sc_flow_main where outer_code='" + newProductCode  + "';", true);
							System.out.println("delete from systemcenter.sc_flow_main where outer_code='" + newProductCode  + "';");
							
							//sc_flow_history
							List<Map<String,Object>> flowHistoryList = DbUp.upTable("sc_flow_history")
									.dataSqlList("select * from sc_flow_history where flow_code=:flow_code and flow_type=:flow_type", new MDataMap("flow_code",flowCode, "flow_type", flowType));
							if(flowHistoryList != null && !flowHistoryList.isEmpty()) {
								for(Map<String,Object> flowHistoryMap : flowHistoryList) {
									creator = flowHistoryMap.get("creator") == null ? "" : flowHistoryMap.get("creator").toString();
									if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
										flowHistoryMap.put("creator", newUserCode);
									}
									flowHistoryMap.put("flow_code", newFlowCode);
									flowHistoryMap.remove("uid");
									flowHistoryMap.remove("zid");
									DbUp.upTable("sc_flow_history").dataInsert(new MDataMap(flowHistoryMap));
								}
								writeLog(logger, "delete from systemcenter.sc_flow_history where flow_code='" + newFlowCode  + "';", true);
								System.out.println("delete from systemcenter.sc_flow_history where flow_code='" + newFlowCode  + "';");
							}
							
							//sc_flow_bussiness_history
							List<Map<String,Object>> flowBusHistoryList = DbUp.upTable("sc_flow_bussiness_history")
									.dataSqlList("select * from sc_flow_bussiness_history where flow_code=:flow_code and flow_type=:flow_type", new MDataMap("flow_code",flowCode, "flow_type", flowType));
							if(flowBusHistoryList != null && !flowBusHistoryList.isEmpty()) {
								for(Map<String,Object> flowBusHistoryMap : flowBusHistoryList) {
									creator = flowBusHistoryMap.get("creator") == null ? "" : flowBusHistoryMap.get("creator").toString();
									if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
										flowBusHistoryMap.put("creator", newUserCode);
									}
									flowBusHistoryMap.put("flow_code", newFlowCode);
									flowBusHistoryMap.remove("uid");
									flowBusHistoryMap.remove("zid");
									DbUp.upTable("sc_flow_bussiness_history").dataInsert(new MDataMap(flowBusHistoryMap));
								}
								writeLog(logger, "delete from systemcenter.sc_flow_bussiness_history where flow_code='" + newFlowCode  + "';", true);
								System.out.println("delete from systemcenter.sc_flow_bussiness_history where flow_code='" + newFlowCode  + "';");
							}

						}
					}
				}
				
				//复制商品销量
				List<Map<String,Object>> productSaleList = DbUp.upTable("pc_productsales_everyday")
						.dataSqlList("select * from pc_productsales_everyday where product_code=:product_code", new MDataMap("product_code", product_code_old));
				if(productSaleList != null && !productSaleList.isEmpty()) {
					for(Map<String,Object> productSaleMap : productSaleList) {
						productSaleMap.put("product_code", newProductCode);
						productSaleMap.remove("uid");
						productSaleMap.remove("zid");
						DbUp.upTable("pc_productsales_everyday").dataInsert(new MDataMap(productSaleMap));
					}
					writeLog(logger, "delete from productcenter.pc_productsales_everyday where product_code='" + newProductCode  + "';", true);
					System.out.println("delete from productcenter.pc_productsales_everyday where product_code='" + newProductCode  + "';");
				}
				//商品循环
			}
		}
		try {
			logger.flush();
			logger.close();
			
			productLogger.flush();
			productLogger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End!!");
	}
	
	public void refreshProduct(String productCodes) {
		if(StringUtils.isNotBlank(productCodes)) {
			String[] pcArr = productCodes.split(",");
			for(String productCode : pcArr) {
				//改为已上架
				int success = DbUp.upTable("pc_productinfo").dataUpdate(new MDataMap("product_status","4497153900060002", "product_code", productCode), "product_status", "product_code");
				if(success > 0) {
					//刷新solr及缓存
					MDataMap dataMap = new MDataMap();
					dataMap.put("productCode", productCode);
					try {
						WebClientSupport.upPost(TopUp.upConfig("productcenter.webclienturladdone"), dataMap);
						PlusHelperNotice.onChangeProductInfo(productCode);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("#product_code:" + productCode + " refresh failure.");
					}
				}
			}
		}
	}
	
	public void moveSellerQualification(String smallSellerCode, String newSellerCode, String oldUserCode, String newUserCode, FileWriter logger) {
		//商户资质数据
		List<Map<String,Object>> sellerQualifInfoList1 = DbUp.upTable("pc_seller_qualification").dataSqlList("select * from pc_seller_qualification where small_seller_code=:ssc", 
				new MDataMap("ssc",smallSellerCode));
		if(sellerQualifInfoList1 != null && !sellerQualifInfoList1.isEmpty()) {
			for(Map<String, Object> quaInfoMap1 : sellerQualifInfoList1) {
				String oldQualifCode = quaInfoMap1.get("seller_qualification_code") == null ? "" : quaInfoMap1.get("seller_qualification_code").toString();
				String newQualifCode = WebHelper.upCode("SQ");
				quaInfoMap1.put("seller_qualification_code", newQualifCode);
				quaInfoMap1.put("small_seller_code", newSellerCode);
				quaInfoMap1.remove("uid");
				quaInfoMap1.remove("zid");
				DbUp.upTable("pc_seller_qualification").dataInsert(new MDataMap(quaInfoMap1));
				writeLog(logger, "delete from productcenter.pc_seller_qualification where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';", true);
				System.out.println("delete from productcenter.pc_seller_qualification where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';");
				
				//商户资质数据
				List<Map<String,Object>> sellerQualifInfoList = DbUp.upTable("pc_qualification_info").dataSqlList("select * from pc_qualification_info where small_seller_code=:ssc and seller_qualification_code=:sqc", 
						new MDataMap("ssc",smallSellerCode, "sqc", oldQualifCode));
				if(sellerQualifInfoList != null && !sellerQualifInfoList.isEmpty()) {
					for(Map<String, Object> quaInfoMap : sellerQualifInfoList) {
						quaInfoMap.put("seller_qualification_code", newQualifCode);
						quaInfoMap.put("small_seller_code", newSellerCode);
						quaInfoMap.remove("uid");
						quaInfoMap.remove("zid");
						DbUp.upTable("pc_qualification_info").dataInsert(new MDataMap(quaInfoMap));
					}
					writeLog(logger, "delete from productcenter.pc_qualification_info where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';", true);
					System.out.println("delete from productcenter.pc_qualification_info where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';");
				}

				
				//商户资质草稿箱中的数据
				List<Map<String,Object>> sellerQualifDraftList = DbUp.upTable("pc_seller_qualification_draftbox").dataSqlList("select * from pc_seller_qualification_draftbox where small_seller_code=:ssc and seller_qualification_code=:sqc", 
						new MDataMap("ssc",smallSellerCode, "sqc", oldQualifCode));
				if(sellerQualifDraftList != null && !sellerQualifDraftList.isEmpty()) {
					for(Map<String, Object> quaDraftMap : sellerQualifDraftList) {
						quaDraftMap.put("seller_qualification_code", newQualifCode);
						quaDraftMap.put("small_seller_code", newSellerCode);
						String json = quaDraftMap.get("qualification_json") == null ? "" : quaDraftMap.get("qualification_json").toString();
						if(StringUtils.isNotEmpty(json)) {
							JsonHelper<PcSellerQualification> jsonHelper=new JsonHelper<PcSellerQualification>();
							PcSellerQualification obj = jsonHelper.StringToObj(json, new PcSellerQualification());
							if(obj != null) {
								obj.setSellerQualificationCode(newQualifCode);
								obj.setSmallSellerCode(newSellerCode);
								List<PcQualificationInfo> list = obj.getQualificationList();
								for(PcQualificationInfo info : list) {
									info.setSellerQualificationCode(newQualifCode);
									info.setSmallSellerCode(newSellerCode);
								}
							}
							
							String newJson = jsonHelper.ObjToString(obj);
							if(StringUtils.isNotEmpty(newJson)) {
								quaDraftMap.put("qualification_json", newJson);
							}
						}
						quaDraftMap.remove("uid");
						quaDraftMap.remove("zid");
						DbUp.upTable("pc_seller_qualification_draftbox").dataInsert(new MDataMap(quaDraftMap));
					}
					writeLog(logger, "delete from productcenter.pc_seller_qualification_draftbox where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';", true);
					System.out.println("delete from productcenter.pc_seller_qualification_draftbox where small_seller_code='" + newSellerCode + "' and seller_qualification_code='" + newQualifCode + "';");
				}
				
				//流程中的质检数据只考虑审批通过的
				List<Map<String,Object>> flowList = DbUp.upTable("sc_flow_main")
						.dataSqlList("SELECT * FROM systemcenter.sc_flow_main WHERE flow_type ='449717230015' "
								+ "AND outer_code=:qualif_code order by zid desc", new MDataMap("qualif_code",oldQualifCode));
				if(flowList != null && !flowList.isEmpty()) {
					boolean found = true;
					Map<String, Object> flowMap1 = flowList.get(0);
					String currentStatus = flowMap1.get("current_status") == null ? "" : flowMap1.get("current_status").toString();
					if(!StringUtils.equals(currentStatus, "4497172300150003")) {
						found = false;
					}
					
					if(found) {
						Collections.reverse(flowList);
						for(Map<String, Object> flowMap : flowList) {
							String creator = flowMap.get("creator") == null ? "" : flowMap.get("creator").toString();
							if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
								flowMap.put("creator", newUserCode);
							}
							
							String updator = flowMap.get("updator") == null ? "" : flowMap.get("updator").toString();
							if(StringUtils.isNotBlank(updator) && updator.equals(oldUserCode)) {
								flowMap.put("updator", newUserCode);
							}
							
							String flowTitle = flowMap.get("flow_title") == null ? "" : flowMap.get("flow_title").toString();
							if(StringUtils.isNotBlank(flowTitle) && flowTitle.contains(smallSellerCode)) {
								flowMap.put("flow_title", flowTitle.replaceAll(smallSellerCode, newSellerCode));
							}
							
							String flowRemark = flowMap.get("flow_remark") == null ? "" : flowMap.get("flow_remark").toString();
							if(StringUtils.isNotBlank(flowRemark) && flowRemark.contains(smallSellerCode)) {
								flowMap.put("flow_remark", flowRemark.replaceAll(smallSellerCode, newSellerCode));
							}
							if(StringUtils.isNotBlank(flowRemark) && flowRemark.contains(oldQualifCode)) {
								flowMap.put("flow_remark", flowRemark.replaceAll(oldQualifCode, newQualifCode));
							}
							
							
							String flowCode = flowMap.get("flow_code") == null ? "" : flowMap.get("flow_code").toString();
							String flowType = flowMap.get("flow_type") == null ? "" : flowMap.get("flow_type").toString();
							
							String newFlowCode = WebHelper.upCode("SF");
							flowMap.put("outer_code", newQualifCode);
							flowMap.put("flow_code", newFlowCode);
							flowMap.remove("uid");
							flowMap.remove("zid");
							DbUp.upTable("sc_flow_main").dataInsert(new MDataMap(flowMap));
							writeLog(logger, "delete from systemcenter.sc_flow_main where flow_code='" + newFlowCode  + "';", true);
							System.out.println("delete from systemcenter.sc_flow_main where flow_code='" + newFlowCode  + "';");
							
							//sc_flow_history
							List<Map<String,Object>> flowHistoryList = DbUp.upTable("sc_flow_history")
									.dataSqlList("select * from sc_flow_history where flow_code=:flow_code and flow_type=:flow_type", new MDataMap("flow_code",flowCode, "flow_type", flowType));
							if(flowHistoryList != null && !flowHistoryList.isEmpty()) {
								for(Map<String,Object> flowHistoryMap : flowHistoryList) {
									creator = flowHistoryMap.get("creator") == null ? "" : flowHistoryMap.get("creator").toString();
									if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
										flowHistoryMap.put("creator", newUserCode);
									}
									flowHistoryMap.put("flow_code", newFlowCode);
									flowHistoryMap.remove("uid");
									flowHistoryMap.remove("zid");
									DbUp.upTable("sc_flow_history").dataInsert(new MDataMap(flowHistoryMap));
								}
								writeLog(logger, "delete from systemcenter.sc_flow_history where flow_code='" + newFlowCode  + "';", true);
								System.out.println("delete from systemcenter.sc_flow_history where flow_code='" + newFlowCode  + "';");
							}
							
							//sc_flow_bussiness_history
							List<Map<String,Object>> flowBusHistoryList = DbUp.upTable("sc_flow_bussiness_history")
									.dataSqlList("select * from sc_flow_bussiness_history where flow_code=:flow_code and flow_type=:flow_type", new MDataMap("flow_code",flowCode, "flow_type", flowType));
							if(flowBusHistoryList != null && !flowBusHistoryList.isEmpty()) {
								for(Map<String,Object> flowBusHistoryMap : flowBusHistoryList) {
									creator = flowBusHistoryMap.get("creator") == null ? "" : flowBusHistoryMap.get("creator").toString();
									if(StringUtils.isNotBlank(creator) && creator.equals(oldUserCode)) {
										flowBusHistoryMap.put("creator", newUserCode);
									}
									flowBusHistoryMap.put("flow_code", newFlowCode);
									flowBusHistoryMap.remove("uid");
									flowBusHistoryMap.remove("zid");
									DbUp.upTable("sc_flow_bussiness_history").dataInsert(new MDataMap(flowBusHistoryMap));
								}	
								writeLog(logger, "delete from systemcenter.sc_flow_bussiness_history where flow_code='" + newFlowCode  + "';", true);
								System.out.println("delete from systemcenter.sc_flow_bussiness_history where flow_code='" + newFlowCode  + "';");
							}
						}
					}
				}
			}
		}
	}
	
	public void moveRetentionMoney(String smallSellerCode, String newSmallSellerCode) {
		List<Map<String,Object>> retentionList = DbUp.upTable("oc_bill_seller_retention_money")
				.dataSqlList("select * from oc_bill_seller_retention_money where small_seller_code=:small_seller_code", new MDataMap("small_seller_code", smallSellerCode));
		if(retentionList != null && !retentionList.isEmpty()) {
			for(Map<String,Object> retentionMap : retentionList) {
				retentionMap.put("small_seller_code", newSmallSellerCode);
				retentionMap.remove("uid");
				retentionMap.remove("zid");
				DbUp.upTable("oc_bill_seller_retention_money").dataInsert(new MDataMap(retentionMap));
			}
			System.out.println("delete from ordercenter.oc_bill_seller_retention_money where small_seller_code='" + newSmallSellerCode  + "';");
		}		
	}
	
	/**
	 * 初始化质保金管理数据
	 */
	public void initSellerRetentionMoney(FileWriter logger, String smallSellerCode, String newSmallSellerCode) {
		//复制商户质保金基础数据
		MDataMap retentionMoney = DbUp.upTable("oc_seller_retention_money").one("small_seller_code", smallSellerCode);
		if(retentionMoney != null){
			if(DbUp.upTable("oc_seller_retention_money").count("small_seller_code", newSmallSellerCode) == 0){
				retentionMoney.remove("uid");
				retentionMoney.remove("zid");
				retentionMoney.put("small_seller_code", newSmallSellerCode);
				retentionMoney.put("receive_retention_money", "0");
				retentionMoney.put("adjust_retention_money", "0");
				retentionMoney.put("deduct_retention_money", "0");
				retentionMoney.put("retention_money_sum", "0");
				retentionMoney.put("receive_retention_money_date", "");
				retentionMoney.put("adjust_retention_money_date", "");
				retentionMoney.put("create_time", FormatHelper.upDateTime());
				retentionMoney.put("creator", "商户平移");
				retentionMoney.put("update_time", "");
				retentionMoney.put("updator", "");
				DbUp.upTable("oc_seller_retention_money").dataInsert(retentionMoney);
				
				writeLog(logger, "delete from ordercenter.oc_seller_retention_money where small_seller_code='" + newSmallSellerCode + "';", true);
				System.out.println("delete from ordercenter.oc_seller_retention_money where small_seller_code='" + newSmallSellerCode + "';");
			}
		}		
	}
	
	private void writeLog(FileWriter logger, String content, boolean newLine) {
		if(logger != null) {
			try {
				logger.write(content);
				if(newLine) {
					logger.write("\r\n");
				}
				logger.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
