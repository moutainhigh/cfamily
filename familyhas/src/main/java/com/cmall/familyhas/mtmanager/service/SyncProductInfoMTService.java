package com.cmall.familyhas.mtmanager.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cmall.familyhas.mtmanager.inputresult.SyncProductCodeMTInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncProductInfoMTInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncProductInfoMTResult;
import com.cmall.familyhas.mtmanager.model.MTProductDescription;
import com.cmall.familyhas.mtmanager.model.MTProductInfo;
import com.cmall.familyhas.mtmanager.model.MTProductPic;
import com.cmall.familyhas.mtmanager.model.MTSkuInfo;
import com.cmall.productcenter.model.PcProductdescription;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.PcProductpic;
import com.cmall.productcenter.model.ProductSkuInfo;
import com.cmall.productcenter.model.UcSellercategoryProductRelation;
import com.cmall.productcenter.service.CategoryService;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * MT管家同步商品信息业务实现
 * @author pang_jhui
 *
 */
public class SyncProductInfoMTService extends BaseClass {
	
	/**
	 * 业务解析
	 * @param inputParam
	 * 		输入参数
	 * @param mDataMap
	 * 		扩展参数 
	 * @return SyncProductInfoMTResult
	 * 		处理结果
	 */
	public SyncProductInfoMTResult doProcess(SyncProductInfoMTInput inputParam , MDataMap mDataMap){
		
		ProductService productService = new ProductService();
		
		CategoryService categoryService = new CategoryService();
		
		SyncProductInfoMTResult productInfoMTResult = new SyncProductInfoMTResult();
		
		List<MTProductInfo> mtProductInfos = new ArrayList<MTProductInfo>();
		
		checkResult(productInfoMTResult, inputParam);	
		
		if(productInfoMTResult.upFlagTrue()){
			
			List<MDataMap> productCodes = queryProductCode(inputParam.getStartDate(), inputParam.getEndDate());
			
			for(MDataMap dataMap : productCodes){
				
				PcProductinfo pcProductinfo = productService.getProduct(dataMap.get("product_code"));
				
				//获取商品所属分类名称
				List<UcSellercategoryProductRelation> usprList = pcProductinfo.getUsprList();
				List<String> categoryCodeArr = new ArrayList<String>();			//分类codeList
				List<String> categoryNameArr = new ArrayList<String>();			//分类nameList
				if (null != usprList && !usprList.isEmpty()) {
					for (UcSellercategoryProductRelation uspr : usprList) {
						categoryCodeArr.add(uspr.getCategoryCode());
					}
					MDataMap categoryMap = categoryService.getCategoryName(categoryCodeArr, pcProductinfo.getSellerCode());
					for (String category : categoryMap.keySet()) {
						categoryNameArr.add(categoryMap.get(category));
					}
				}
				MTProductInfo mtProductInfo = createMTProdcutInfo(pcProductinfo);
				
				PlusModelSkuQuery query = new PlusModelSkuQuery();
				query.setCode(pcProductinfo.getProductSkuInfoList().get(0).getSkuCode());
				PlusModelSkuInfo skuInfo = new LoadSkuInfo().upInfoByCode(query);
				query.setCode(pcProductinfo.getProductCode());
				PlusModelSkuInfoSpread spread = new LoadSkuInfoSpread()
						.upInfoByCode(query);
				
				mtProductInfo.setSplitRole(skuInfo.getSmallSellerCode()+skuInfo.getValidateFlag()+spread.getPrchType()+spread.getDlrId()+spread.getSiteNo());
				mtProductInfo.setCategories(categoryNameArr);
				mtProductInfos.add(mtProductInfo);
			}
			
		}
		
		productInfoMTResult.setMtProductInfos(mtProductInfos);
		
		productInfoMTResult.setTotal(mtProductInfos.size());
		
		return productInfoMTResult;
		
	}
	
	/**
	 * 业务解析
	 * @param inputParam
	 * 		输入参数
	 * @param mDataMap
	 * 		扩展参数 
	 * @return SyncProductInfoMTResult
	 * 		处理结果
	 */
	public SyncProductInfoMTResult doProcess(SyncProductCodeMTInput inputParam, MDataMap mDataMap) {

		ProductService productService = new ProductService();

		CategoryService categoryService = new CategoryService();

		SyncProductInfoMTResult productInfoMTResult = new SyncProductInfoMTResult();

		List<MTProductInfo> mtProductInfos = new ArrayList<MTProductInfo>();

		PcProductinfo pcProductinfo = productService.getProduct(inputParam.getProductCode());

		if(pcProductinfo != null){
			
			// 获取商品所属分类名称
			List<UcSellercategoryProductRelation> usprList = pcProductinfo.getUsprList();
			List<String> categoryCodeArr = new ArrayList<String>(); // 分类codeList
			List<String> categoryNameArr = new ArrayList<String>(); // 分类nameList
			if (null != usprList && !usprList.isEmpty()) {
				for (UcSellercategoryProductRelation uspr : usprList) {
					categoryCodeArr.add(uspr.getCategoryCode());
				}
				MDataMap categoryMap = categoryService.getCategoryName(categoryCodeArr, pcProductinfo.getSellerCode());
				for (String category : categoryMap.keySet()) {
					categoryNameArr.add(categoryMap.get(category));
				}
			}
			MTProductInfo mtProductInfo = createMTProdcutInfo(pcProductinfo);
			mtProductInfo.setCategories(categoryNameArr);
			mtProductInfos.add(mtProductInfo);
			
		}

		productInfoMTResult.setMtProductInfos(mtProductInfos);

		productInfoMTResult.setTotal(mtProductInfos.size());

		return productInfoMTResult;

	}
	
	/**
	 * 创建mt管家产品信息
	 * @param pcProductinfo
	 * 		惠家有产品信息
	 * @return MTProductInfo
	 * 		mt管家产品信息
	 */
	public MTProductInfo createMTProdcutInfo(PcProductinfo pcProductinfo){
		
		MTProductInfo mtProductInfo = new MTProductInfo();
		
		BeanUtils.copyProperties(pcProductinfo, mtProductInfo);
		
		mtProductInfo.setMtProductDescription(createMTProductDescription(pcProductinfo.getDescription()));
		
		mtProductInfo.setMtSkuInfos(createMTSkuInfos(pcProductinfo.getProductSkuInfoList()));
		
		mtProductInfo.setProductPicList(createMTProductPics(pcProductinfo.getPcPicList()));
		
		return mtProductInfo;
		
	}
	
	/**
	 * 创建mt管家图片信息
	 * @param pcProductpic
	 * 		惠家有产品图片信息
	 * @return MTProductPic
	 * 		MT管家图片信息
	 */
	public List<MTProductPic> createMTProductPics(List<PcProductpic> pcProductpics){
		
		List<MTProductPic> mtProductPics = new ArrayList<MTProductPic>();
		
		for(PcProductpic pcProductpic : pcProductpics){
			
			MTProductPic mtProductPic = new MTProductPic();
			
			BeanUtils.copyProperties(pcProductpic, mtProductPic);
			
			mtProductPics.add(mtProductPic);
			
		}
		
		return mtProductPics;		
		
	}
	
	/**
	 * 创建mt管家产品描述
	 * @param pcProductdescription
	 * 		产品描述信息
	 * @return MTProductDescription
	 * 		MT管家描述信息
	 */
	public MTProductDescription createMTProductDescription(PcProductdescription pcProductdescription){

		MTProductDescription mtProductDescription = new MTProductDescription();
		
		BeanUtils.copyProperties(pcProductdescription, mtProductDescription);
		
		return mtProductDescription;
		
	}
	
	
	/**
	 * 创建mt管家sku信息
	 * @param pcSkuinfo
	 * 		惠家有sku信息
	 * @return MTSkuInfo
	 * 		mt管家sku信息
	 */
	public List<MTSkuInfo> createMTSkuInfos(List<ProductSkuInfo> pcSkuinfos){
		
		List<MTSkuInfo> mtSkuInfos = new ArrayList<MTSkuInfo>();
		
		for(ProductSkuInfo productSkuInfo : pcSkuinfos){
			
			MTSkuInfo mtSkuInfo = new MTSkuInfo();
			
			BeanUtils.copyProperties(productSkuInfo, mtSkuInfo);
			
			mtSkuInfos.add(mtSkuInfo);
			
		}
		
		return mtSkuInfos;
		
		
	}
	
	/**
	 * 转换成日历
	 * @param date
	 * 		日期
	 * @param format
	 * 		格式字符串
	 * @return Calendar
	 * @throws ParseException 
	 */
	public Calendar convertToCalendar(String date,String format) throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(dateFormat.parse(date));
		
		return calendar;
		
	}
	
	/**
	 * 比较两个日期的大小
	 * @param beginCalendar
	 * 		开始日期
	 * @param endCalendar
	 * 		结束日期
	 * @param field
	 * 		单位：年、月、日
	 * @param diff
	 * 		差异值
	 * @return 比较结果
	 * 大于0 开始日期
	 */
	public int compareCalendar(Calendar beginCalendar, Calendar endCalendar, int field,int diff){
		
		beginCalendar.add(field, diff);
		
		return beginCalendar.compareTo(endCalendar);
		
	}
	
	/**
	 * 校验日期
	 * @param mtResult
	 * 		返回结果
	 * @param input 输入参数
	 */
	public void checkResult(SyncProductInfoMTResult mtResult, SyncProductInfoMTInput input){
		
		Calendar beginCalendar = null;
		
		Calendar endCalendar = null;
		
		try {
			
			beginCalendar = convertToCalendar(input.getStartDate(), "yyyy-MM-dd");
			
		} catch (ParseException e) {
			
			mtResult.setResultCode(-1);
			mtResult.setResultMessage("输入的开始日期解析错误，日期默认解析格式【yyyy-MM-dd】,请检查输入的日期");
			
		}
		

		try {
			
			endCalendar = convertToCalendar(input.getEndDate(), "yyyy-MM-dd");
			
		} catch (ParseException e) {
			
			mtResult.setResultCode(-1);
			mtResult.setResultMessage("输入的结束日期解析错误，日期默认解析格式【yyyy-MM-dd】,请检查输入的日期");
			
		}
		
		if(!beginCalendar.equals(endCalendar)){
			
			int flag = compareCalendar(beginCalendar, endCalendar, Calendar.DATE, 1);
			
			if(flag > 0){
				
				mtResult.setResultCode(-1);
				mtResult.setResultMessage("输入开始日期晚于结束日期，请检查输入参数");
				
			}
			
			if(flag < 0){
				
				mtResult.setResultCode(-1);
				mtResult.setResultMessage("输入开始日期与结束日期相差大于一天，目前系统默认跨度为一天的查询，请检查输入的日期");
				
			}
			
		}		
		
		
	}
	
	/**
	 * 查询产品编码
	 * @param startDate
	 * 		开始日期
	 * @param endDate
	 * 		结束日期
	 * @return List<MDataMap>
	 * 		产品编码集合
	 */
	public List<MDataMap> queryProductCode(String startDate, String endDate){
		
		MDataMap mWhereMap = new MDataMap();
		
		mWhereMap.put("startDate", startDate);
		
		mWhereMap.put("endDate", endDate);
		
		/*惠家有*/
		mWhereMap.put("seller_code", bConfig("familyhas.app_code"));
		
		/*已上架*/
		mWhereMap.put("product_status", "4497153900060002");
		
		return DbUp.upTable("pc_productinfo").queryAll("product_code", "", "update_time>=:startDate and update_time<:endDate", mWhereMap);
		
	}
	

}
