package com.cmall.familyhas.webfunc;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.dborm.txmodel.LcProductinfoBatch;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.accountmarketing.util.ReadExcelUtil;
import com.cmall.productcenter.model.Category;
import com.cmall.productcenter.model.PcProductdescription;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.PcProductpic;
import com.cmall.productcenter.model.PcProductproperty;
import com.cmall.productcenter.service.CategoryService;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncModProductForBatch extends RootFunc{

	private final String  PRODUCT_STATUS_UNSHELVE= "4497153900060003";			//商品已下架
	private final String PRODUCT_STATUS_SALE = "4497153900060002";				//商品上架	
	private final String UPDATE_STATUS_SUCCESS = "4497465200110001";			//更新成功
	private final String UPDATE_STATUS_FAIL = "4497465200110002";				//更新失败						
	private final String  CUSTOM_PROPERTY= "449736200004";						//自定义属性
	
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		
		MDataMap mInputMap = upFieldMap(mDataMap);
		
		
		if (mWebResult.upFlagTrue()) {
			String BATCH_TOTAL_UID =  UUID.randomUUID().toString().replace("-", "");	//批量导入统计表中的uid，在此先生成的目的在于跟导入详情信息表相关联
			String fileRemoteUrl = mInputMap.get("upload_show");
			String sellerCode=	bConfig("familyhas.app_code");		//获取appCode
			int totalNum = 0;						//总数量
			int successNum = 0;					//成功更新数量
			
			ProductService pService = new ProductService();
			try {
				if(StringUtils.isBlank(fileRemoteUrl)){
					throw new Exception("下载地址不存在");
				}
				MUserInfo uc = UserFactory.INSTANCE.create();//当前用户所属店铺编号
				if(uc==null){
					//无权修改商品
					mWebResult.inErrorMessage(941901065, bInfo(941901064));
					return mWebResult;
				}
				List<LcProductinfoBatch> dataLists = null;
				try {
					dataLists = downloadAndAnalysisFile(fileRemoteUrl);
				} catch (Exception e) {
					mWebResult.setResultCode(-1);
					mWebResult.setResultMessage("上传发生问题，请重新下载模版！");
					return mWebResult ;
				}
				totalNum = ((null == dataLists) ? 0 :dataLists.size());
				if (totalNum == 0) {
					mWebResult.setResultCode(-1);
					mWebResult.setResultMessage("上传文件没数据");
					mWebResult.inOtherResult(mWebResult);
					return mWebResult;
				}
				
				//模版中的商品code拼成的字符串
				StringBuffer proBuffAll = new StringBuffer();
				for (LcProductinfoBatch lcBatch : dataLists) {		//模版中所有的商品信息
					proBuffAll.append(lcBatch.getProductCode());
					proBuffAll.append("','");
				}
				String productCodesAllStr = ((proBuffAll == null || String.valueOf(proBuffAll).length() <= 3)? "" : String.valueOf(proBuffAll).substring(0, String.valueOf(proBuffAll).length()-3));
				
				//获取惠家有所有的商品code与商品信息
				String productWhere = " seller_code='"+sellerCode+"' and product_code in ('"+productCodesAllStr+"')";
				List<MDataMap> productCodes = new ArrayList<MDataMap>(); 
				if (StringUtils.isNotEmpty(productCodesAllStr)) {
					productCodes = DbUp.upTable("pc_productinfo").queryAll("", "", productWhere, null);
				}
				Map<String, MDataMap> productCodeMap = new HashMap<String, MDataMap>();
				for (int i = 0; i < productCodes.size(); i++) {
					productCodeMap.put(productCodes.get(i).get("product_code"), productCodes.get(i));
				}
				//模版中所有属于惠家有的商品code拼成的字符串
				StringBuffer proBuff = new StringBuffer();
				for (String productCode : productCodeMap.keySet()) {
					for (LcProductinfoBatch lcBatch : dataLists) {		//模版中所有的商品信息
						if (productCode.equals(lcBatch.getProductCode())) {
							proBuff.append(productCode);
							proBuff.append("','");
						}
					}
				}
				String productCodesStr = ((proBuff == null || String.valueOf(proBuff).length() <= 3)? "" : String.valueOf(proBuff).substring(0, String.valueOf(proBuff).length()-3));
				//获取商品关联的轮播图
				String picWhere = " product_code in ('"+productCodesStr+"') ";
				List<MDataMap> productPics = new ArrayList<MDataMap>();
					
				if (StringUtils.isNotEmpty(productCodesStr)) {
					productPics = DbUp.upTable("pc_productpic").queryAll("product_code,pic_url", "", picWhere, null);
				}
				List<PcProductpic> picList = new ArrayList<PcProductpic>();
				for (MDataMap pcProductpicMap : productPics) {
					PcProductpic pic = new PcProductpic();
					pic.setProductCode(pcProductpicMap.get("product_code"));
					pic.setPicUrl(pcProductpicMap.get("pic_url"));
					picList.add(pic);
				}

				//获取商品关联的商品描述
				List<MDataMap> productDescript = new ArrayList<MDataMap>();
				if (StringUtils.isNotEmpty(productCodesStr)) {
					productDescript = DbUp.upTable("pc_productdescription").queryAll("", "", picWhere, null);
				}
				Map<String,Map<String,String>> productDescriptMap = new HashMap<String, Map<String,String>>();
				for (MDataMap pcDescriptMap : productDescript) {
					Map<String,String> descriptKeyword = new HashMap<String, String>();
					descriptKeyword.put("description_info", pcDescriptMap.get("description_info"));
					descriptKeyword.put("description_pic", pcDescriptMap.get("description_pic"));
					descriptKeyword.put("keyword", pcDescriptMap.get("keyword"));
					productDescriptMap.put(pcDescriptMap.get("product_code"), descriptKeyword);
				}
				
				//获取商品品牌名称
				StringBuffer proBrandBuff = new StringBuffer();				//模版中存在商品编号的商品品牌名称
				for (String productCode : productCodeMap.keySet()) {
					for (LcProductinfoBatch lcBatch : dataLists) {
						if (productCode.equals(lcBatch.getProductCode())) {
							proBrandBuff.append(lcBatch.getBrandCode());
							proBrandBuff.append("','");
						}
					}
				}
				String proBrandStr =  ((proBrandBuff == null || String.valueOf(proBrandBuff).length() <= 3)? "" : String.valueOf(proBrandBuff).substring(0, String.valueOf(proBrandBuff).length()-3));
				String brandWhere = " brand_name in ('"+proBrandStr+"')";
				List<MDataMap> brands = new ArrayList<MDataMap>();
				if (StringUtils.isNotEmpty(proBrandStr)) {
					brands = DbUp.upTable("pc_brandinfo").queryAll("brand_name,brand_code", "", brandWhere, null);
				}
				Map<String, String> brandsMap = new HashMap<String, String>();
				for (int i = 0; i < brands.size(); i++) {
					brandsMap.put(brands.get(i).get("brand_name"), brands.get(i).get("brand_code"));
				}
				
				//获取惠家有第三级虚类名称
				StringBuffer sellerCategortyBuff = new StringBuffer();
				for (String productCode : productCodeMap.keySet()) {
					for (LcProductinfoBatch lcBatch : dataLists) {
						if (productCode.equals(lcBatch.getProductCode())) {
							if (StringUtils.isNotEmpty(lcBatch.getCategoryName())) {			//传入的虚类之间用空格隔开
								String[] caArr = lcBatch.getCategoryName().split(" ");
								for (String string : caArr) {						//将虚类列表循环添加到结果集中，过滤掉空值
									if (StringUtils.isNotEmpty(string)) {
										sellerCategortyBuff.append(string);
										sellerCategortyBuff.append("','");
									}
								}
							}
						}
					}
				}
				String sellerCategortyStr = ((sellerCategortyBuff == null || String.valueOf(sellerCategortyBuff).length() <= 3)? "" : String.valueOf(sellerCategortyBuff).substring(0, String.valueOf(sellerCategortyBuff).length()-3));
				String sellerCategoriesWhere = " category_name in ('"+sellerCategortyStr+"') and level='3' and seller_code='"+sellerCode+"'";
				List<MDataMap> sellercategories = DbUp.upTable("uc_sellercategory").queryAll("category_name,category_code", "",sellerCategoriesWhere , null);
				Map<String, String> sellercategoriesMap = new HashMap<String, String>();
				for (int i = 0; i < sellercategories.size(); i++) {
					sellercategoriesMap.put(sellercategories.get(i).get("category_name"), sellercategories.get(i).get("category_code"));
				}

				//获取商品关联的自定义属性列表
				List<MDataMap> propertyProductList = new ArrayList<MDataMap>();
				if (StringUtils.isNotEmpty(productCodesStr)) {
					propertyProductList = DbUp.upTable("pc_productproperty").queryAll("product_code,property_key,property_value", "", " property_type='"+CUSTOM_PROPERTY+"' and "+picWhere, null);
				}
				CategoryService categoryService = new CategoryService();		//查询实类
				
				//获取到的一级二级三级实类都放到这个map中
				Map<String, Map<String,Map<String,Category>>> categoriesMap = new HashMap<String, Map<String,Map<String,Category>>>();
				//通过以下三个map可以获取到不同级别实类对应的code编码
				Map<String, Category> firstCategoriesMap = new HashMap<String, Category>();
				Map<String, Category> secondCategoriesMap = new HashMap<String, Category>();
				Map<String, Category> thirdCategoriesMap = new HashMap<String, Category>();
				//第一级实类
				List<Category> firstCategories = new ArrayList<Category>();
				
				//控制一级实类的个数是在模版中存在的，这样可以减少查询出来的数据量，减少循环次数
				StringBuffer firstCategoriesBuff = new StringBuffer();
				for (String productCode : productCodeMap.keySet()) {
					for (LcProductinfoBatch lcBatch : dataLists) {
						if (productCode.equals(lcBatch.getProductCode()) && StringUtils.isNotEmpty(lcBatch.getCategoryNameFirst())) {
							firstCategoriesBuff.append(lcBatch.getCategoryNameFirst());
							firstCategoriesBuff.append("','");
						}
					}
				}
				
				List<MDataMap> categoryFirstList = new ArrayList<MDataMap>();
				if (firstCategoriesBuff != null && String.valueOf(firstCategoriesBuff).length() > 3) {
					String firstCategoriesStr =  String.valueOf(firstCategoriesBuff).substring(0, String.valueOf(firstCategoriesBuff).length()-3);
					String firstCategoriesWhere = " category_name in ('"+firstCategoriesStr+"') and LENGTH(category_code)='12' ";
					categoryFirstList = DbUp.upTable("pc_categoryinfo").query("", "",firstCategoriesWhere,null, -1, -1);
				}
				for(MDataMap m : categoryFirstList){
					Category c = new Category();
					c.setCategoryCode(m.get("category_code"));
					c.setCategoryName(m.get("category_name"));
					firstCategories.add(c);
				}
				
				for (int a = 0; a < firstCategories.size(); a++) {
					firstCategoriesMap.put(firstCategories.get(a).getCategoryName(), firstCategories.get(a));
					//第二级实类
					List<Category> secondCategories = categoryService.getCategoryListForCm(2, firstCategories.get(a).getCategoryCode());
					Map<String, Map<String, Category>> nextMap = new HashMap<String, Map<String, Category>>();
					for (int j = 0; j < secondCategories.size(); j++) {
						secondCategoriesMap.put(secondCategories.get(j).getCategoryName(), secondCategories.get(j));
						//第三级实类
						List<Category> thirdCategories = categoryService.getCategoryListForCm(3, secondCategories.get(j).getCategoryCode());
						Map<String, Category> lastMap = new HashMap<String, Category>();
						for (int k = 0; k < thirdCategories.size(); k++) {
							thirdCategoriesMap.put(thirdCategories.get(k).getCategoryName(),thirdCategories.get(k));
							lastMap.put(thirdCategories.get(k).getCategoryName(),thirdCategories.get(k) );			//第三级map
						}
						nextMap.put(secondCategories.get(j).getCategoryName(), lastMap);				//第二级map
					}
					categoriesMap.put(firstCategories.get(a).getCategoryName(), nextMap);		//最终实类map
				}
				
				for(LcProductinfoBatch lcProductinfoBatch : dataLists){
					LcProductinfoBatch lcProductinfoBatchInsert = lcProductinfoBatch.clone();					//初始的修改信息
					MDataMap productinfoOld = productCodeMap.get(lcProductinfoBatch.getProductCode());		//商品的初始基本信息
					boolean flag = true;					//标识数据是否有误，为true时为正常
					StringBuffer failReason = new StringBuffer();
					//首先判断商品编号是否存在与商品表中
					if (null == productinfoOld) {
						/*******
						 * 进行找不到商品时的错误操作
						 */
						lcProductinfoBatchInsert.setUpdateStatus(UPDATE_STATUS_FAIL);
						lcProductinfoBatchInsert.setFailReason("商品找不到："+lcProductinfoBatch.getProductCode()+"；");
						lcProductinfoBatchInsert.setRelevanceUid(BATCH_TOTAL_UID);
						this.saveBatchModLog(lcProductinfoBatchInsert);			//记录日志
						flag = false;
						continue;
					}
					if (StringUtils.isNotEmpty(lcProductinfoBatch.getBrandCode())) {
						//判断商品品牌是否存在与商品品牌表中
						if (null == brandsMap.get(lcProductinfoBatch.getBrandCode())) {
							/*******
							 * 进行找不到商品品牌时的错误操作
							 */
							failReason.append("商品品牌找不到："+lcProductinfoBatch.getBrandCode()+"；");
							flag = false;
						}
					}else{
						lcProductinfoBatch.setBrandCode(productinfoOld.get("brand_code"));			//商品品牌为空时不进行修改
					}
					if (StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryName())) {
						//判断虚类是否存在虚类表中并且是第三级虚类
						
						String[] caArr = lcProductinfoBatch.getCategoryName().trim().split(" ");
//						boolean flagCategory = true;				//为true是代表虚类存在系统中
						for (String category : caArr) {						//将虚类列表循环添加到结果集中，过滤掉空值
							if (StringUtils.isNotEmpty(category)) {
								if (null == sellercategoriesMap.get(category)) {
									/*******
									 * 进行找不到商品虚类时的错误操作
									 */
									failReason.append("商品虚类找不到："+category+"；");
									flag = false;
								}
							}
						}
					}
					//实类一级二级三级分类全部不为空时进行修改
					boolean flagCategories = (StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameFirst())&& StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameSecond())&&
							StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameThird()));				//标识实类是否被修改
					
					String pcCategoryCode = "";				//第三级实类编号
					if (flagCategories) {
						//判断第一级实类是否存在
						if (null == categoriesMap.get(lcProductinfoBatch.getCategoryNameFirst())) {
							/*******
							 * 进行找不到第一级实类时的操作
							 */
							failReason.append("商品第一级实类找不到："+lcProductinfoBatch.getCategoryNameFirst()+"；");
							flag = false;
						}else{
							Map<String,Map<String,Category>> secondMap = categoriesMap.get(lcProductinfoBatch.getCategoryNameFirst());
							if (null == secondMap.get(lcProductinfoBatch.getCategoryNameSecond())) {
								/*******
								 * 进行找不到第二级实类时的操作
								 */
								failReason.append("商品第二级实类找不到："+lcProductinfoBatch.getCategoryNameSecond()+"；");
								flag = false;
							}else{
								Map<String,Category> thirdMap = secondMap.get(lcProductinfoBatch.getCategoryNameSecond());
								if (null == thirdMap.get(lcProductinfoBatch.getCategoryNameThird())) {
									/*******
									 * 进行找不到第三级实类时的操作
									 */
									failReason.append("商品第三级实类找不到："+lcProductinfoBatch.getCategoryNameThird()+"；");
									flag = false;
								}else{
									pcCategoryCode = thirdMap.get(lcProductinfoBatch.getCategoryNameThird()).getCategoryCode();
								}
							}
						}
					}
					
					//第一/二/三级实类至少有一个不为空并且至少有一个为空时
					if((StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameFirst())|| StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameSecond())||
							StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryNameThird()))&& !flagCategories ){
							failReason.append("商品实类不能部分为空；");
							flag = false;
					}
					
					//自定义属性列表
					List<PcProductproperty> propertyList = new ArrayList<PcProductproperty>();
					String propertiesStr = lcProductinfoBatch.getProperties();
					if (StringUtils.isNotEmpty(propertiesStr)) {
						
						String[] propertiesKeyValues = propertiesStr.trim().split("&");
						for (String propertiesKeyValue : propertiesKeyValues) {
							String[] propertiesKeyValueArr = propertiesKeyValue.trim().split("=");
							if (propertiesKeyValueArr.length == 2) {
								PcProductproperty property = new PcProductproperty();
								property.setProductCode(lcProductinfoBatch.getProductCode());
								property.setPropertyKey(propertiesKeyValueArr[0]);	//属性名
								property.setPropertyValue(propertiesKeyValueArr[1]);//属性值
								property.setPropertyType(CUSTOM_PROPERTY);			//自定义属性类型
								propertyList.add(property);
							}else{
								/*******
								 * 自定义属性格式错误，应为：规格名称=规格值&规格名称=规格值
								 */
								failReason.append("商品自定义属性格式错误"+"；");
								flag = false;
							}
						}
					}else{
						//自定义属性为空时不进行修改
						for (MDataMap map : propertyProductList) {
							if (map.get("product_code").equals(lcProductinfoBatch.getProductCode())) {
								PcProductproperty property = new PcProductproperty();
								property.setProductCode(lcProductinfoBatch.getProductCode());
								property.setPropertyKey(map.get("property_key"));
								property.setPropertyValue(map.get("property_value"));
								property.setPropertyType(CUSTOM_PROPERTY);			//自定义属性
								propertyList.add(property);
							}
						}
					}
					BigDecimal productWeight = new BigDecimal(0);
					BigDecimal productVolume = new BigDecimal(0);
					BigDecimal productLength = new BigDecimal(0);
					BigDecimal productWidth = new BigDecimal(0);
					BigDecimal productHigh = new BigDecimal(0);
					boolean flagWeight = true;		//标识重量是否被修改
					boolean flagVolumn = true;
					boolean flagSize= true;
					try {
						String weight = lcProductinfoBatch.getProductWeight();
						String volumn = lcProductinfoBatch.getProductVolume();
						String length = lcProductinfoBatch.getProductLength();
						String width = lcProductinfoBatch.getProductWidth();
						String high = lcProductinfoBatch.getProductHigh();
						
						flagWeight = StringUtils.isNotEmpty(weight);
						flagVolumn = StringUtils.isNotEmpty(volumn);
						flagSize = (StringUtils.isNotEmpty(length) || StringUtils.isNotEmpty(width) || StringUtils.isNotEmpty(high));			//长宽高至少有一个被修改则全部替换
						
						//商品重量
						 productWeight = BigDecimal.valueOf(Double.parseDouble((!flagWeight) ? "0" : weight));
						//商品体积
						 productVolume = BigDecimal.valueOf(Double.parseDouble((!flagVolumn) ? "0" : volumn));
						//商品长度
						 productLength = (StringUtils.isEmpty(length) ? null :  BigDecimal.valueOf(Double.parseDouble(length)).setScale(2, BigDecimal.ROUND_FLOOR));
						//商品宽度
						 productWidth = (StringUtils.isEmpty(width) ? null :   BigDecimal.valueOf(Double.parseDouble(width)).setScale(2, BigDecimal.ROUND_FLOOR));
						//商品高度
						 productHigh =  (StringUtils.isEmpty(high) ? null :  BigDecimal.valueOf(Double.parseDouble(high)).setScale(2, BigDecimal.ROUND_FLOOR));
					} catch (Exception e) {
						/****
						 * 商品的重量，体积，长宽高转换错误
						 */
						flag = false;
						failReason.append("商品的重量，体积，长宽高转换出错，必须是数字。"+"；");
					}
					
					//校验商品标签
					if (StringUtils.isNotEmpty(lcProductinfoBatch.getKeyword())) {
						
						/****
						 * 商品标签长度超长
						 */
						if (lcProductinfoBatch.getKeyword().replace(" ", "").length() > 10) {
							flag = false;
							failReason.append("商品标签总长度不能超过10个字符"+"；");
						}
					}else{
						//没有修改商品标签
						lcProductinfoBatch.setKeyword(productDescriptMap.get(lcProductinfoBatch.getProductCode()).get("keyword"));
					}
					
					if (!flag) {
						lcProductinfoBatchInsert.setUpdateStatus(UPDATE_STATUS_FAIL);
						lcProductinfoBatchInsert.setFailReason(failReason.toString());				//错误原因
						lcProductinfoBatchInsert.setRelevanceUid(BATCH_TOTAL_UID);
						this.saveBatchModLog(lcProductinfoBatchInsert);			//记录日志
						continue;
					}
					
					//获取商品对应的轮播图
					List<PcProductpic> pcPicList = new ArrayList<PcProductpic>(); 
					for (PcProductpic pic : picList) {
						if (pic.getProductCode().equals(lcProductinfoBatch.getProductCode())) {
							pcPicList.add(pic);
						}
					}
					
					String marketPriceStr = productinfoOld.get("market_price");		//市场价格
					BigDecimal marketPrice = new BigDecimal(Double.valueOf((marketPriceStr == null || "".equals(marketPriceStr)) ? "0" : marketPriceStr));
					//没有修改商品名称
					if (StringUtils.isEmpty(lcProductinfoBatch.getProductName())) {
						lcProductinfoBatch.setProductName(productinfoOld.get("product_name"));
					}
					//没有修改商品简称
//					if (StringUtils.isEmpty(lcProductinfoBatch.getProductShortname())) {
						lcProductinfoBatch.setProductShortname(productinfoOld.get("product_shortname"));
//					}
					//没有修改商品描述信息
					if (StringUtils.isEmpty(lcProductinfoBatch.getDescriptionInfo())) {
						lcProductinfoBatch.setDescriptionInfo(productDescriptMap.get(lcProductinfoBatch.getProductCode()).get("description_info"));
					}
					
					//没有修改重量时
					if (!flagWeight) {
						productWeight = (StringUtils.isEmpty(productinfoOld.get("product_weight")) ? new BigDecimal(0) : new BigDecimal(Double.valueOf(productinfoOld.get("product_weight"))) );
					}
					//没有修改体积时
					if (!flagVolumn) {
						productVolume = (StringUtils.isEmpty(productinfoOld.get("product_volume")) ? new BigDecimal(0) : new BigDecimal(Double.valueOf(productinfoOld.get("product_volume"))) );
					}
					
					//长宽高
					String productVolumeItem = (null == productLength ? "" : new BigDecimal(productLength.doubleValue()))+","+
															(null == productWidth ? "" : new BigDecimal(productWidth.doubleValue()))+","+ (null ==productHigh ? "" : new BigDecimal(productHigh.doubleValue()));
					//长宽高都大于0时计算体积
					if ((null == productLength ? false : productLength.compareTo(new BigDecimal(0)) > 0) && (null == productWidth ? false : productWidth.compareTo(new BigDecimal(0)) > 0 )
							&&  (null == productHigh ? false : productHigh.compareTo(new BigDecimal(0)) > 0)) {
						productVolume = productLength.multiply(productWidth.multiply(productHigh));		//转换成double主要是为了去除小数点后末尾的0
					}
					//长宽高都没有被修改时
					if (!flagSize) {
						productVolumeItem = productinfoOld.get("product_volume_item");
					}
					//没有修改商品货号
					if (StringUtils.isEmpty(lcProductinfoBatch.getSellProductcode())) {
						lcProductinfoBatch.setSellProductcode(productinfoOld.get("sell_productcode"));
					}
					//没有修改视频地址
					if (StringUtils.isEmpty(lcProductinfoBatch.getVideoUrl())) {
						lcProductinfoBatch.setVideoUrl(productinfoOld.get("video_url"));
					}
					StringBuffer error = new StringBuffer();		//修改商品返回的错误信息
					PcProductinfo pp = new PcProductinfo();	//商品DTO
					PcProductdescription description = new PcProductdescription();	//商品描述
					description.setDescriptionInfo(lcProductinfoBatch.getDescriptionInfo());
					description.setKeyword(lcProductinfoBatch.getKeyword());
					description.setDescriptionPic(productDescriptMap.get(lcProductinfoBatch.getProductCode()).get("description_pic"));
					
					pp.setProductCode(lcProductinfoBatch.getProductCode());									//商品编号
					pp.setProdutName(lcProductinfoBatch.getProductName());									//商品名称
					pp.setProductShortname(lcProductinfoBatch.getProductShortname());						//商品简称
					pp.setBrandCode(brandsMap.get(lcProductinfoBatch.getBrandCode()));				    	//品牌编号
					pp.setBrandName(lcProductinfoBatch.getBrandCode());										//品牌名称
					pp.setDescription(description);															//描述信息以及商品标签
					pp.setPcProductpropertyList(propertyList);												//商品自定义规格列表
					//转换成double的目的为去除小数点儿后末尾的0,缺点儿是至少会保留一位小数
					pp.setProductWeight(new BigDecimal(productWeight.doubleValue()));						//商品重量				
					pp.setProductVolume(new BigDecimal(productVolume.doubleValue()));						//商品体积
					pp.setProductVolumeItem(productVolumeItem);												//商品的长宽高
					pp.setSellProductcode(lcProductinfoBatch.getSellProductcode());							//商品货号
					pp.setVideoUrl(lcProductinfoBatch.getVideoUrl());										//视频链接地址
					
					
					//以下是不需要修改的字段的内容
					pp.setMainPicUrl(productinfoOld.get("mainpic_url"));									//商品主图
					pp.setPcPicList(pcPicList);																//商品轮播图
					pp.setMarketPrice(marketPrice);															//市场价格
					pp.setTransportTemplate(productinfoOld.get("transport_template"));						//运费模版
						
					pService.updateProduct(pp, error);				//进行更新操作
					if (StringUtils.isEmpty(error.toString())) {
						//修改实类关联操作
						if (flagCategories) {				//修改商品关联实类
							MDataMap whereMap = new MDataMap();
							whereMap.put("category_code", pcCategoryCode);
							whereMap.put("product_code", lcProductinfoBatch.getProductCode());
							DbUp.upTable("pc_productcategory_rel").dataUpdate(whereMap, "category_code", "product_code");
						}
						//修改商品关联虚类，先删除后添加
						if (StringUtils.isNotEmpty(lcProductinfoBatch.getCategoryName())) {
							DbUp.upTable("uc_sellercategory_product_relation").delete("product_code",lcProductinfoBatch.getProductCode(),"seller_code",sellerCode);
							
							String[] caArr = lcProductinfoBatch.getCategoryName().split(" ");
							for (String category : caArr) {						//将虚类列表循环添加到结果集中，过滤掉空值
								if (StringUtils.isNotEmpty(category)) {
									if (null != sellercategoriesMap.get(category)) {
										DbUp.upTable("uc_sellercategory_product_relation").insert("product_code", lcProductinfoBatch.getProductCode(),"seller_code",sellerCode,"category_code",sellercategoriesMap.get(category));
									}
								}
							}
						}
						
						this.productStatusUnshelve(lcProductinfoBatch.getProductCode());	//商品下架
						
						lcProductinfoBatchInsert.setUpdateStatus(UPDATE_STATUS_SUCCESS);
						lcProductinfoBatchInsert.setRelevanceUid(BATCH_TOTAL_UID);
						this.saveBatchModLog(lcProductinfoBatchInsert);			//记录日志
						successNum ++ ;
					} else {
						//调用更新商品方法后返回的错误
						lcProductinfoBatchInsert.setUpdateStatus(UPDATE_STATUS_FAIL);
						lcProductinfoBatchInsert.setFailReason("失败原因："+error+"；");
						lcProductinfoBatchInsert.setRelevanceUid(BATCH_TOTAL_UID);
						this.saveBatchModLog(lcProductinfoBatchInsert);			//记录日志
						continue;
					}
				}
				//插入统计导入结果
				MDataMap mInsertTotal = new MDataMap();
				mInsertTotal.put("uid", BATCH_TOTAL_UID);
				mInsertTotal.put("filename", DateUtil.getSysDateTimeString(new SimpleDateFormat("yyyyMMddHHmmss"))+"上传");				//文件名，暂时没有获取到
				mInsertTotal.put("mod_num", String.valueOf(totalNum));
				mInsertTotal.put("success_num", String.valueOf(successNum));
				mInsertTotal.put("upload_time", DateUtil.getSysDateTimeString());
				mInsertTotal.put("upload_user", UserFactory.INSTANCE.create().getLoginName());
				DbUp.upTable("lc_productinfo_batch_total").dataInsert(mInsertTotal);
				
			} catch (Exception e) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("发生错误，错误原因："+e.getMessage());
			}

			mWebResult.inOtherResult(mWebResult);

		}
		return mWebResult;
	}
	
	/**
	 * 保存日志
	 * @param lcProductinfoBatch
	 * @return
	 */
	private int saveBatchModLog(LcProductinfoBatch lcProductinfoBatch){
		MDataMap mInsertMap = new MDataMap();
		mInsertMap.put("update_status", lcProductinfoBatch.getUpdateStatus());
		mInsertMap.put("product_code", StringUtils.isEmpty(lcProductinfoBatch.getProductCode()) ? "" :  lcProductinfoBatch.getProductCode());
		mInsertMap.put("product_name",StringUtils.isEmpty(lcProductinfoBatch.getProductName()) ? "" :  lcProductinfoBatch.getProductName());
		mInsertMap.put("product_shortname",StringUtils.isEmpty(lcProductinfoBatch.getProductShortname()) ? "" :  lcProductinfoBatch.getProductShortname());
		mInsertMap.put("brand_code",StringUtils.isEmpty(lcProductinfoBatch.getBrandCode()) ? "" :  lcProductinfoBatch.getBrandCode());
		mInsertMap.put("description_info",StringUtils.isEmpty(lcProductinfoBatch.getDescriptionInfo()) ? "" :  lcProductinfoBatch.getDescriptionInfo());
		mInsertMap.put("category_name",StringUtils.isEmpty(lcProductinfoBatch.getCategoryName()) ? "" :  lcProductinfoBatch.getCategoryName());
		mInsertMap.put("category_name_first",StringUtils.isEmpty(lcProductinfoBatch.getCategoryNameFirst()) ? "" :  lcProductinfoBatch.getCategoryNameFirst());
		mInsertMap.put("category_name_second",StringUtils.isEmpty(lcProductinfoBatch.getCategoryNameSecond()) ? "" :  lcProductinfoBatch.getCategoryNameSecond());
		mInsertMap.put("category_name_third",StringUtils.isEmpty(lcProductinfoBatch.getCategoryNameThird()) ? "" :  lcProductinfoBatch.getCategoryNameThird());
		mInsertMap.put("properties",StringUtils.isEmpty(lcProductinfoBatch.getProperties()) ? "" :  lcProductinfoBatch.getProperties());
		mInsertMap.put("keyword",StringUtils.isEmpty(lcProductinfoBatch.getKeyword()) ? "" :  lcProductinfoBatch.getKeyword());
		mInsertMap.put("product_weight",StringUtils.isEmpty(lcProductinfoBatch.getProductWeight()) ? "" :  lcProductinfoBatch.getProductWeight());
		mInsertMap.put("product_volume",StringUtils.isEmpty(lcProductinfoBatch.getProductVolume()) ? "" :  lcProductinfoBatch.getProductVolume());
		mInsertMap.put("product_length",StringUtils.isEmpty(lcProductinfoBatch.getProductLength()) ? "" :  lcProductinfoBatch.getProductLength());
		mInsertMap.put("product_width",StringUtils.isEmpty(lcProductinfoBatch.getProductWidth()) ? "" :  lcProductinfoBatch.getProductWidth());
		mInsertMap.put("product_high",StringUtils.isEmpty(lcProductinfoBatch.getProductHigh()) ? "" :  lcProductinfoBatch.getProductHigh());
		mInsertMap.put("sell_productcode",StringUtils.isEmpty(lcProductinfoBatch.getSellProductcode()) ? "" :  lcProductinfoBatch.getSellProductcode());
		mInsertMap.put("video_url",StringUtils.isEmpty(lcProductinfoBatch.getVideoUrl()) ? "" :  lcProductinfoBatch.getVideoUrl());
		mInsertMap.put("relevance_uid", lcProductinfoBatch.getRelevanceUid());
		mInsertMap.put("fail_reason", StringUtils.isEmpty(lcProductinfoBatch.getFailReason()) ? "修改成功" :  lcProductinfoBatch.getFailReason());
		DbUp.upTable("lc_productinfo_batch").dataInsert(mInsertMap);
		return 0;
	}
	
	private List<LcProductinfoBatch> downloadAndAnalysisFile(String fileRemoteUrl) throws Exception{
		
		String extension = fileRemoteUrl.lastIndexOf(".") == -1 ? "" : fileRemoteUrl.substring(fileRemoteUrl.lastIndexOf(".") + 1);
		java.net.URL resourceUrl = new java.net.URL(fileRemoteUrl);
		InputStream content = (InputStream) resourceUrl.getContent();
		ReadExcelUtil<LcProductinfoBatch> readExcelUtil = new ReadExcelUtil<LcProductinfoBatch>();
		
		return readExcelUtil.readExcel(false, null, content, 
				new String[]{"productCode","productName","brandCode","descriptionInfo","categoryName","categoryNameFirst","categoryNameSecond","categoryNameThird"
				,"properties","keyword","productWeight","productVolume","productLength","productWidth","productHigh","sellProductcode","videoUrl"},
				new Class[]{String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class
				,String.class,String.class,String.class,String.class,String.class}, 
				LcProductinfoBatch.class, extension);
	}
	/**
	 * 商品下架
	 * @return
	 */
	private int productStatusUnshelve(String productCode){
		
		MDataMap productMap =  DbUp.upTable("pc_productinfo").oneWhere("uid,product_status", "", "", "product_code",productCode,"product_status",PRODUCT_STATUS_SALE);
		if (null != productMap && !productMap.isEmpty()) {
			FlowBussinessService fs = new FlowBussinessService();
			String flowBussinessUid=productMap.get("uid");
			String fromStatus= PRODUCT_STATUS_SALE;
			String toStatus= PRODUCT_STATUS_UNSHELVE;
			String flowType = "449715390006";			//商家后台商品状态
			MUserInfo userInfo = UserFactory.INSTANCE.create();
			String userCode = userInfo.getUserCode();
			String remark = "批量导入修改商品信息，商品自动下架";
			
			MDataMap mSubMap = new MDataMap();
			mSubMap.put("flow_bussinessid", flowBussinessUid);
			mSubMap.put("from_status", fromStatus);
			mSubMap.put("to_status", toStatus);
			mSubMap.put("flow_type", flowType);
			mSubMap.put("remark", remark);
			RootResult ret =
					fs.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, mSubMap);
		}
		return 0;
	}
}