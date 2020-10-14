package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.APiForCategoryInput;
import com.cmall.familyhas.api.model.BrandInfo;
import com.cmall.familyhas.api.model.SellerCategorySeInfo;
import com.cmall.familyhas.api.model.SellerCategoryThInfo;
import com.cmall.familyhas.api.model.SellerCategoryThreeInfo;
import com.cmall.familyhas.api.result.APiForCategoryResult;
import com.cmall.groupcenter.behavior.api.ApiBfdRecCategory;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 惠家有类目接口
 * 
 * @author xiegj
 *
 */
public class APiForCategoryNew extends RootApiForVersion<APiForCategoryResult, APiForCategoryInput> {

	@SuppressWarnings("deprecation")
	@Override
	public APiForCategoryResult Process(APiForCategoryInput inputParam, MDataMap mRequestMap) {
		APiForCategoryResult apiResult = new APiForCategoryResult();
		List<SellerCategorySeInfo> twoCategoryList = new ArrayList<SellerCategorySeInfo>();// 二级分类列表

		// 小程序临时兼容
		String os = AppVersionUtils.compareTo(
				null == getApiClient().get("app_vision") ? "" : getApiClient().get("app_vision"), "5.1.4") == 0
						? "weapphjy"
						: (null == getApiClient().get("os") ? "" : getApiClient().get("os"));

		// 由于四级编码的类型过多，为性能考虑，将四级类型全查出来
		List<Map<String, Object>> fourList = searchAllPreFourCategory(os);
		List<Map<String, Object>> productCountList = searchAllPreFourProductCount();

		MDataMap twoParamMap = new MDataMap();
		twoParamMap.put("seller_code", getManageCode());
		twoParamMap.put("flaginable", "449746250001");
		twoParamMap.put("level", "2");
		// 小程序屏蔽掉医疗器械分类
		twoParamMap.put("category_code", "weapphjy".equals(os) ? "449716040081" : "");
		List<MDataMap> twoCategorys = DbUp.upTable("uc_sellercategory_pre").queryAll("", "sort",
				"seller_code=:seller_code and flaginable=:flaginable and level=:level and category_code!=:category_code",
				twoParamMap);
		for (MDataMap twoCategory : twoCategorys) {
			// 查询前台二级编码对应的后台绑定的品牌
			List<Map<String, Object>> brandList = searchTwoCodeBrand(twoCategory.get("category_code"));
			// 获取三级分类编码
			List<Map<String, Object>> threeCategoryList = searhPreThreeCategory(twoCategory.get("category_code"));
			// 格式化三级分类信息并将三级分类
			List<SellerCategoryThreeInfo> threeInfoList = convertThreeCategory(threeCategoryList,
					inputParam.getMaxWidth(), fourList, productCountList, os);
			// 当二级分类有品牌或三级分类才展示二级分类数据
			if (brandList.size() > 0 || threeInfoList.size() > 0) {
				SellerCategorySeInfo seInfo = new SellerCategorySeInfo();
				seInfo.setCategoryCode(twoCategory.get("category_code"));
				seInfo.setCategoryName(twoCategory.get("category_name"));
				if ("449746250001".equals(twoCategory.get("adv_flag"))) {
					seInfo.setAdvPic(newUrl(inputParam.getMaxWidth(), twoCategory.get("adv_pic")));
					seInfo.setAdvUrl(twoCategory.get("adv_href"));
				}

				// 格式化品牌信息
				List<BrandInfo> brandInfoList = convertBrandInfo(brandList, inputParam.getMaxWidth());
				seInfo.setBrands(brandInfoList);
				seInfo.setScsThree(threeInfoList);

				twoCategoryList.add(seInfo);
			}
		}

		apiResult.setScs(twoCategoryList);
		return apiResult;
	}

	private List<SellerCategoryThreeInfo> convertThreeCategory(List<Map<String, Object>> threeCategoryList,
			Integer maxWidth, List<Map<String, Object>> fourList, List<Map<String, Object>> productCountList,
			String os) {
		List<SellerCategoryThreeInfo> categoryList = new ArrayList<SellerCategoryThreeInfo>();
		Iterator<Map<String, Object>> iterator = threeCategoryList.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> threeCategory = iterator.next();
			// 小程序临时 屏蔽医疗器械分类
			if ("weapphjy".equals(os)
					&& ("4497160400920004".equals(MapUtils.getString(threeCategory, "category_code", "")))) {
				iterator.remove();
				continue;
			}
			SellerCategoryThreeInfo threeInfo = new SellerCategoryThreeInfo();
			threeInfo.setCategoryCode(MapUtils.getString(threeCategory, "category_code", ""));
			threeInfo.setCategoryName(MapUtils.getString(threeCategory, "category_name", ""));
			threeInfo.setCategoryPic(newUrl(maxWidth, MapUtils.getString(threeCategory, "photo", "")));
			if ("449746250001".equals(threeCategory.get("adv_flag"))) {
				threeInfo.setAdvPic(newUrl(maxWidth, MapUtils.getString(threeCategory, "adv_pic", "")));
				threeInfo.setAdvUrl(MapUtils.getString(threeCategory, "adv_href", ""));
			}

			// 获取四级编码
			List<SellerCategoryThInfo> fourCategoryList = new ArrayList<SellerCategoryThInfo>();
			for (Map<String, Object> four : fourList) {
				if (MapUtils.getString(four, "parent_code", "")
						.equals(MapUtils.getString(threeCategory, "category_code", ""))) {
					if(MapUtils.getString(four, "category_type", "").equals("449748510001")) {
						for (Map<String, Object> productCount : productCountList) {
							if (MapUtils.getString(four, "category_code", "")
									.equals(MapUtils.getString(productCount, "category_code", ""))) {
								SellerCategoryThInfo thFourCategory = convertFourCategory(four, maxWidth);
								fourCategoryList.add(thFourCategory);
								break;
							}
						}
					}else if(MapUtils.getString(four, "category_type", "").equals("449748510002")) {
						// 新建子分类---添加商品
						List<Map<String,Object>> dataSqlList = DbUp.upTable("uc_sellercategory_pre_product").dataSqlList("SELECT * FROM uc_sellercategory_pre_product WHERE category_code = '"+MapUtils.getString(four, "category_code", "")+"'", new MDataMap());
						if(null != dataSqlList && dataSqlList.size() > 0) {
							SellerCategoryThInfo thFourCategory = convertFourCategory(four, maxWidth);
							fourCategoryList.add(thFourCategory);
						}
					}else if(MapUtils.getString(four, "category_type", "").equals("449748510003")) {
						// 新建子分类---维护属性值
						String sql = "SELECT r.zid FROM uc_properties_product_relation r WHERE r.properties_value_code in " + 
								"(SELECT v.properties_value_code FROM uc_sellercategory_pre_properties_value v WHERE v.category_code = '"+MapUtils.getString(four, "category_code", "")+"')";
						List<Map<String,Object>> dataSqlList = DbUp.upTable("uc_sellercategory_pre_properties_value").dataSqlList(sql, new MDataMap());
						if(null != dataSqlList && dataSqlList.size() > 0) {
							SellerCategoryThInfo thFourCategory = convertFourCategory(four, maxWidth);
							fourCategoryList.add(thFourCategory);
						}
					}
				}
			}
			threeInfo.setScs(fourCategoryList);

			if (fourCategoryList.size() > 0) {
				categoryList.add(threeInfo);
			}
		}

		return categoryList;
	}

	private SellerCategoryThInfo convertFourCategory(Map<String, Object> fourCategory, Integer maxWidth) {
		SellerCategoryThInfo fourInfo = new SellerCategoryThInfo();
		fourInfo.setCategoryCode(MapUtils.getString(fourCategory, "category_code", ""));
		fourInfo.setCategoryName(MapUtils.getString(fourCategory, "category_name", ""));
		fourInfo.setCategoryPic(newUrl(maxWidth, MapUtils.getString(fourCategory, "photo", "")));
		if ("449746250001".equals(MapUtils.getString(fourCategory, "adv_flag", ""))) {
			fourInfo.setAdvPic(newUrl(maxWidth, MapUtils.getString(fourCategory, "adv_pic", "")));
			fourInfo.setAdvUrl(MapUtils.getString(fourCategory, "adv_href", ""));
		}

		return fourInfo;
	}

	private List<BrandInfo> convertBrandInfo(List<Map<String, Object>> brandList, Integer maxWidth) {
		List<BrandInfo> infoList = new ArrayList<BrandInfo>();
		for (Map<String, Object> brand : brandList) {
			BrandInfo brandInfo = new BrandInfo();
			brandInfo.setBrandCode(brand.get("brand_code") == null ? "" : brand.get("brand_code").toString());
			brandInfo.setBrandZNName(brand.get("brand_name") == null ? "" : brand.get("brand_name").toString());
			brandInfo.setBrandUNName(brand.get("brand_name_en") == null ? "" : brand.get("brand_name_en").toString());
			brandInfo.setBrandPic(newUrl(maxWidth, brand.get("brand_pic").toString()));

			infoList.add(brandInfo);
		}
		return infoList;
	}

	@Deprecated
	public List<Map<String, Object>> searhPreFourCode(String twoCode) {
		String sql = "select p4.category_code from uc_sellercategory_pre p4 where p4.parent_code in "
				+ "(select p3.category_code from uc_sellercategory_pre p3 where p3.parent_code = '" + twoCode
				+ "' and p3.flaginable = '449746250001' and p3.seller_code = 'SI2003' and p3.level = '3') "
				+ "and p4.flaginable = '449746250001' and p4.seller_code = 'SI2003' and p4.level = '4'";
		return DbUp.upTable("uc_sellercategory_pre").dataSqlList(sql, new MDataMap());
	}

	private List<Map<String, Object>> searhPreThreeCategory(String twoCode) {
		String sql = "select p3.* from uc_sellercategory_pre p3 where p3.parent_code = '" + twoCode
				+ "' and p3.flaginable = '449746250001' and p3.seller_code = 'SI2003' and p3.level = '3' order by p3.sort";
		return DbUp.upTable("uc_sellercategory_pre").dataSqlList(sql, new MDataMap());
	}

	private List<Map<String, Object>> searchAllPreFourCategory(String os) {
		String section = "";
		if ("weapphjy".equals(os)) {
			section = " and category_code not in ('44971604008200030012','44971604008200030013','44971604008200010001','44971604008200030011','44971604008200010012','44971604008200030014','44971604009200080004','44971604008200010003','44971604008200010015','44971604008200050006','44971604008200050005','44971604009200030002','44971604009200030001','44971604009200030005','44971604008400030014','44971604008400030015','44971604008400020006','44971604008400040004','44971604008600020003','44971604008400030007','44971604008400010007','44971604008700020010','44971604008200010013','44971604008200030002','44971604008200060009','44971604008200020009','44971604008200070002') ";
		}
		String sql = "select p4.* from uc_sellercategory_pre p4 where p4.flaginable = '449746250001' and p4.seller_code = 'SI2003' and p4.level = '4'"
				+ section + " order by p4.parent_code, p4.sort";
		return DbUp.upTable("uc_sellercategory_pre").dataSqlList(sql, new MDataMap());
	}

	private List<Map<String, Object>> searchAllPreFourProductCount() {
		String sql = "select * from uc_sellercategory_product_count where product_count > 0";
		return DbUp.upTable("uc_sellercategory_product_count").dataSqlList(sql, new MDataMap());
	}

	private List<Map<String, Object>> searchTwoCodeBrand(String twoCategoryCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (twoCategoryCode != null && !"".equals(twoCategoryCode)) {
			String sql = "select b.* from productcenter.pc_brandinfo b, uc_sellercategory_brand_relation r where r.seller_code = 'SI2003' and b.brand_code = r.brand_code and r.category_code = '"
					+ twoCategoryCode + "' " + "order by r.create_time";
			list = DbUp.upTable("uc_sellercategory").dataSqlList(sql, new MDataMap());
		}
		return list;
	}

	@Deprecated
	public List<Map<String, Object>> searctTwoCodeBrand(List<Map<String, Object>> fourList) {
		String fourCodes = "";
		for (int i = 0; i < fourList.size(); i++) {
			Map<String, Object> four = fourList.get(i);
			if (i == 0) {
				fourCodes += MapUtils.getString(four, "category_code", "");
			} else {
				fourCodes = fourCodes + "," + MapUtils.getString(four, "category_code", "");
			}
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!"".equals(fourCodes)) {
			String sql = "select b.* from productcenter.pc_brandinfo b, uc_sellercategory_brand_relation r where r.seller_code = 'SI2003' and b.brand_code = r.brand_code and r.category_code in "
					+ "(select s3.parent_code from uc_sellercategory s3 where s3.category_code in "
					+ "(select s4.parent_code from uc_sellercategory s4 where s4.category_code in (" + fourCodes
					+ ") and s4.flaginable = '449746250001' and s4.seller_code = 'SI2003' and s4.level = '4') "
					+ "and s3.flaginable = '449746250001' and s3.seller_code = 'SI2003' and s3.level = '3') "
					+ "order by r.create_time";
			list = DbUp.upTable("uc_sellercategory").dataSqlList(sql, new MDataMap());
		}
		return list;
	}

	/**
	 * 旧版本，已废弃(仅留存用)
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public APiForCategoryResult ProcessTemp(APiForCategoryInput inputParam, MDataMap mRequestMap) {
		APiForCategoryResult result = new APiForCategoryResult();

		// 查询本店铺所有分类
		MDataMap quDataMap = new MDataMap();
		quDataMap.put("seller_code", getManageCode());
		quDataMap.put("flaginable", "449746250001");
		List<MDataMap> list = DbUp.upTable("uc_sellercategory").queryAll("", "sort", "", quDataMap);
		List<SellerCategorySeInfo> seInfos = new ArrayList<SellerCategorySeInfo>();
		String infoCodes = "";

		// 查询所有四级分类下的商品
		String sql1 = "select category_code,product_count from uc_sellercategory_product_count";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_sellercategory_product_count").dataSqlList(sql1,
				new MDataMap());

		// 查询所有品牌下的商品
		String sql2 = "select brand_code,product_count from uc_brand_product_count";
		List<Map<String, Object>> dataSqlList2 = DbUp.upTable("uc_brand_product_count").dataSqlList(sql2,
				new MDataMap());

		for (int i = 0; i < list.size(); i++) {// 操作二级分类
			SellerCategorySeInfo seinfo = new SellerCategorySeInfo();
			MDataMap seMap = list.get(i);
			if ("2".equals(seMap.get("level"))) {
				seinfo.setCategoryCode(seMap.get("category_code"));
				seinfo.setCategoryName(seMap.get("category_name"));
				if ("449746250001".equals(seMap.get("adv_flag"))) {
					seinfo.setAdvPic(newUrl(inputParam.getMaxWidth(), seMap.get("adv_pic")));
					seinfo.setAdvUrl(seMap.get("adv_href"));
				}
				seInfos.add(seinfo);
				if ("".equals(infoCodes)) {
					infoCodes = seinfo.getCategoryCode();
				} else {
					infoCodes += "," + seinfo.getCategoryCode();
				}
			}
		}

		List<Map<String, Object>> brands = new ArrayList<Map<String, Object>>();
		if (!"".equals(infoCodes)) {
			String sql = "select a.*,b.category_code from productcenter.pc_brandinfo a,usercenter.uc_sellercategory_brand_relation b "
					+ "where b.seller_code='SI2003' and a.brand_code=b.brand_code and b.category_code in ('"
					+ infoCodes.replace(",", "','") + "') ORDER BY b.create_time ";
			brands = DbUp.upTable("pc_brandinfo").dataSqlList(sql, new MDataMap());
		}
		for (int i = 0; i < seInfos.size(); i++) {
			SellerCategorySeInfo seinfo = seInfos.get(i);
			for (int j = 0; j < list.size(); j++) {
				MDataMap thMap = list.get(j);// 此为三级分类,将三级分类放入二级集合，四级 放入此三级分类
				if (thMap.get("parent_code").equals(seinfo.getCategoryCode())) {
					SellerCategoryThreeInfo sellerCategoryThreeInfo = new SellerCategoryThreeInfo();
					sellerCategoryThreeInfo.setCategoryCode(thMap.get("category_code"));
					sellerCategoryThreeInfo.setCategoryName(thMap.get("category_name"));
					sellerCategoryThreeInfo.setCategoryPic(newUrl(inputParam.getMaxWidth(), thMap.get("photo")));
					if ("449746250001".equals(thMap.get("adv_flag"))) {
						sellerCategoryThreeInfo.setAdvPic(newUrl(inputParam.getMaxWidth(), thMap.get("adv_pic")));
						sellerCategoryThreeInfo.setAdvUrl(thMap.get("adv_href"));
					}
					// 找到此三级分类下所有四级分类，并放入三级分类
					for (int x = 0; x < list.size(); x++) {
						MDataMap fourMap = list.get(x);
						if (fourMap.get("parent_code").equals(thMap.get("category_code"))) {
							SellerCategoryThInfo thInfo = new SellerCategoryThInfo();
							thInfo.setCategoryCode(fourMap.get("category_code"));
							thInfo.setCategoryName(fourMap.get("category_name"));
							thInfo.setCategoryPic(newUrl(inputParam.getMaxWidth(), fourMap.get("photo")));
							if ("449746250001".equals(fourMap.get("adv_flag"))) {
								thInfo.setAdvPic(newUrl(inputParam.getMaxWidth(), fourMap.get("adv_pic")));
								thInfo.setAdvUrl(fourMap.get("adv_href"));
							}
							// 判断此四级分类中有没有商品，如果没有 不展示
							String categoryCode = fourMap.get("category_code");
							if (null != dataSqlList && dataSqlList.size() > 0) {
								for (Map<String, Object> map1 : dataSqlList) {
									String category = map1.get("category_code").toString();
									if (category.equals(categoryCode)) {
										sellerCategoryThreeInfo.getScs().add(thInfo);
										break;
									}
								}
							}
						}
					}

					if (sellerCategoryThreeInfo.getScs().size() > 0) {
						seInfos.get(i).getScsThree().add(sellerCategoryThreeInfo);// 降三级放入二级
					}
				}
			}
			for (int j = 0; j < brands.size(); j++) {
				Map<String, Object> brand = brands.get(j);
				if (brand.get("category_code").toString().equals(seinfo.getCategoryCode())
						&& "1".equals(brand.get("flag_enable").toString())) {
					BrandInfo bi = new BrandInfo();
					bi.setBrandCode(brand.get("brand_code") == null ? "" : brand.get("brand_code").toString());
					bi.setBrandZNName(brand.get("brand_name") == null ? "" : brand.get("brand_name").toString());
					bi.setBrandUNName(brand.get("brand_name_en") == null ? "" : brand.get("brand_name_en").toString());
					bi.setBrandPic(newUrl(inputParam.getMaxWidth(), brand.get("brand_pic").toString()));
					// 如果品牌下无商品则不显示此品牌
					if (null != dataSqlList2 && dataSqlList2.size() > 0) {
						for (Map<String, Object> map1 : dataSqlList2) {
							String brand_code = map1.get("brand_code").toString();
							if (bi.getBrandCode().equals(brand_code)) {
								seInfos.get(i).getBrands().add(bi);
								break;
							}
						}
					}
				}
			}
		}

		// 如果二级分类下无品牌，无分类，则二级分类不展示,并且二级 无 广告
		Iterator<SellerCategorySeInfo> it = seInfos.iterator();
		while (it.hasNext()) {
			SellerCategorySeInfo next = it.next();
			if (next.getBrands().size() == 0 && next.getScsThree().size() == 0 && "".equals(next.getAdvPic())) {
				it.remove();
			}
		}

		// 如果是登录用户并且启用了百分点推荐分类
		if (getFlagLogin() && "true".equalsIgnoreCase(TopUp.upConfig("familyhas.bfd_category_enabled"))) {
			// 取平台参数
			String client = mRequestMap.get("api_client");
			String platform = "";
			if (StringUtils.isNotBlank(client)) {
				platform = "ios";
				if (client.contains("\"os\":\"android\"")) {
					platform = "android";
				}
			}

			// 平台参数不为空的时候再取推荐分类
			if (platform != null) {
				// 从百分点解析出第三级分类名称列表
				List<String> catList = new ApiBfdRecCategory().doProcess(getOauthInfo().getLoginName(), platform);
				List<SellerCategoryThInfo> infoList = new ArrayList<SellerCategoryThInfo>();
				MDataMap map;
				// 根据分类名称列表查询分类
				if (!catList.isEmpty()) {
					SellerCategoryThInfo thInfo;
					for (String name : catList) {
						map = DbUp.upTable("uc_sellercategory").one("category_name", name, "level", "4", "seller_code",
								"SI2003");
						if (map == null)
							continue;
						thInfo = new SellerCategoryThInfo();
						thInfo.setCategoryCode(map.get("category_code"));
						thInfo.setCategoryName(map.get("category_name"));
						thInfo.setCategoryPic(newUrl(inputParam.getMaxWidth(), map.get("photo")));
						// 如果此四级分类下有商品才展示
						if (null != dataSqlList && dataSqlList.size() > 0) {
							for (Map<String, Object> map1 : dataSqlList) {
								String category = map1.get("category_code").toString();
								if (category.equals(map.get("category_code"))) {
									infoList.add(thInfo);
									break;
								}
							}
						}
					}
				}

				// 添加分类到原分类列表中
				if (!infoList.isEmpty()) {
					SellerCategorySeInfo mainInfo = new SellerCategorySeInfo();
					mainInfo.setCategoryCode("44971604");
					mainInfo.setCategoryName(TopUp.upConfig("familyhas.bfd_category_name"));
					mainInfo.setScs(infoList);
					mainInfo.setIsBfdCatefory("1");
					// 百分点推荐分类放到第一个位置
					seInfos.add(0, mainInfo);
				}
			}
		}

		result.setScs(seInfos);
		return result;
	}

	public String newUrl(Integer width, String url) {
		if (null == url || "".equals(url)) {
			return "";
		} else {
			ProductService service = new ProductService();
			return service.getPicInfoOprBig(width, url).getPicNewUrl();
		}
	}
}
