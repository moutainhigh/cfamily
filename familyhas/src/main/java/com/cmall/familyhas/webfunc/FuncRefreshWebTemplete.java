package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadWebTemplete;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelwebtemplete.PlusModelWebTempleteQuery;
import com.srnpr.xmassystem.modelwebtemplete.WebCommodity;
import com.srnpr.xmassystem.modelwebtemplete.WebTemplete;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.very.ImageCompressRun;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncRefreshWebTemplete extends RootFunc{
	
	private ExecutorService service;

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();

		String page_num = mDataMap.get("zw_f_page_code");
		String page_nums[] = page_num.split(",");
		for(int i = 0;i<page_nums.length; i++) {
			this.funcDoPro(page_nums[i]);
		}
		return mResult;
	}
	
	private void funcDoPro(String page_num) {
		if(!StringUtil.isBlank(page_num)) {
			MDataMap one = DbUp.upTable("fh_data_page").one("page_number",page_num);
			if(null != one) {
				//删除专题模板缓存
				XmasKv.upFactory(EKvSchema.WebTemplateCode).del(page_num+"-449747430001");
				XmasKv.upFactory(EKvSchema.WebTemplateCode).del(page_num+"-449747430003");
				XmasKv.upFactory(EKvSchema.WebTemplateCode).del(page_num+"-449747430023");
			}
		}
		
		/* 
		 * 将模板下的图片进行压缩 
		 */
		PlusModelWebTempleteQuery tQuery = new PlusModelWebTempleteQuery();
		tQuery.setCode(page_num+"-449747430001"); // 默认是APP渠道
		WebTempletePage upInfoByCode = new LoadWebTemplete().upInfoByCode(tQuery);
		List<String> picUrlArr = new ArrayList<String>();//压缩专题广告之类图片
		List<String> pcUrlArr = new ArrayList<String>();//压缩商品主图
		
		//记录需要压缩的图片
		addCompressImage(upInfoByCode.getTempleteList(),picUrlArr,pcUrlArr) ;
		//添加关联模板需要压缩的图片
		for (WebTemplete webTemplete : upInfoByCode.getTempleteList()) {
			for (WebCommodity webCommodity : webTemplete.getCommodList()) {
				if(null != webCommodity.getRel_templete() && webCommodity.getRel_templete().size() > 0) {
					addCompressImage(webCommodity.getRel_templete(),picUrlArr,pcUrlArr) ;
				}
			}
		}
		
		/*
		 * 进行图片压缩
		 */
		String imageWidthStr = bConfig("productcenter.imageWidth");
		String[] imageWidthArr = imageWidthStr.split(",");
		
		if(imageWidthArr.length > 0 && picUrlArr.size() > 0) {//压缩专题之类图片
			
			int threadSize = initThreadPool(picUrlArr.size());
			for (String width : imageWidthArr) {
				ImageCompressRun runModel = new ImageCompressRun(width+"webTemplete",StringUtils.join(picUrlArr, "|"),Integer.valueOf(width) ,"");
				service.execute(runModel);
			}
			
			//等待所有线程执行完毕退出释放锁
			service.shutdown();
	        try {  
	            boolean loop = true;  
	            do {  
	                loop = !service.awaitTermination(2, TimeUnit.SECONDS);
	            } while(loop);  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        }
			
		}
		
		if(imageWidthArr.length > 0 && pcUrlArr.size() > 0) {//压缩商品主图
			
			int threadSize = initThreadPool(pcUrlArr.size());
			for (String width : imageWidthArr) {
				ImageCompressRun runModel = new ImageCompressRun(width+"webTemplete", StringUtils.join(pcUrlArr, "|"),BigDecimal.valueOf(Integer.valueOf(width)*0.6).setScale(0).intValue() ,"");
				service.execute(runModel);
			}
			
			//等待所有线程执行完毕退出释放锁
			service.shutdown();
	        try {  
	            boolean loop = true;  
	            do {  
	                loop = !service.awaitTermination(2, TimeUnit.SECONDS);
	            } while(loop);  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        }
			
		}
	}

	public int initThreadPool(int listSize) {
		service = Executors.newFixedThreadPool(listSize);
		return listSize;
	}

	/**
	 * 添加图片链接进行压缩，过滤空链接
	 */
	private void listAddEle (List<String > picArr ,String ele) {
		if(StringUtils.isNotBlank(ele) && !picArr.contains(ele)) {
			picArr.add(ele);
		}
	}
	/**
	 * 添加需要压缩的图片
	 */
	private void addCompressImage(List<WebTemplete> templeteList , List<String> picUrlArr ,List<String> pcUrlArr) {
		LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
		PlusModelSkuQuery skuQuery;
		for (WebTemplete webTemplete : templeteList) {
			listAddEle (picUrlArr , webTemplete.getCommodity_buy_picture());
			listAddEle (picUrlArr , webTemplete.getCommodity_picture());
			listAddEle(picUrlArr, webTemplete.getCommodity_text_pic());
			for (WebCommodity webCommodity : webTemplete.getCommodList()) {
				listAddEle(picUrlArr, webCommodity.getCommodity_picture());
				listAddEle(picUrlArr, webCommodity.getPrograma_picture());
				listAddEle(picUrlArr, webCommodity.getImg());
				if(StringUtils.isNotBlank(webCommodity.getCommodity_number())) {
					//商品主图进行压缩
					//PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(webCommodity.getCommodity_number());
					skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(webCommodity.getCommodity_number());
					PlusModelSkuInfo plusModelSkuInfo = loadSkuInfo.upInfoByCode(skuQuery);
					listAddEle(pcUrlArr, plusModelSkuInfo.getProductPicUrl());
					
				}
			}
		}
	}
}
