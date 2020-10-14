package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.groupcenter.homehas.RsyncGetStock;
import com.cmall.groupcenter.homehas.RsyncGoodGiftList;
import com.cmall.groupcenter.homehas.RsyncSyncGoodsById;
import com.cmall.groupcenter.homehas.RsyncSyncgetSYGoodbyColor;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 手工同步LD方商品信息、SKU信息、库存信息
 * @author jlin
 *
 */
public class FuncRnsyLDProductInfo  extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		if (mResult.upFlagTrue()) {

			String product_code=mAddMaps.get("product_code");
			
			if(StringUtils.isBlank(product_code)){
				mResult.inErrorMessage(916401208);
				return mResult;
			}
			
			//同步商品信息
			RsyncSyncGoodsById rsyncSyncGoodsById = new RsyncSyncGoodsById();
			rsyncSyncGoodsById.upRsyncRequest().setGood_id(product_code);
			rsyncSyncGoodsById.doRsync();
			if(!rsyncSyncGoodsById.isSuccess()){
				mResult.inErrorMessage(916401209);
				return mResult;
			}
			
			//同步SKU信息
			RsyncSyncgetSYGoodbyColor rsyncSyncgetSYGoodbyColor = new RsyncSyncgetSYGoodbyColor();
			rsyncSyncgetSYGoodbyColor.upRsyncRequest().setGood_id(product_code);
			rsyncSyncgetSYGoodbyColor.doRsync();
			if(!rsyncSyncgetSYGoodbyColor.isSuccess()){
				mResult.inErrorMessage(916401210);
				return mResult;
			}
			
			//同步库存信息
			RsyncGetStock rsyncGetStock=new RsyncGetStock();
			rsyncGetStock.upRsyncRequest().setGood_id(product_code);
			rsyncGetStock.doRsync();
			
			if(!rsyncGetStock.responseSucc()){
				mResult.inErrorMessage(916401211);
				return mResult;
			}
			
			// 赠品同步
			new RsyncGoodGiftList().doRsync(product_code);
			
			PlusHelperNotice.onChangeProductInfo(product_code);
			//触发消息队列
			ProductJmsSupport pjs = new ProductJmsSupport();
			pjs.onChangeForProductChangeAll(product_code);
			//刷新索引，目前只能使用惠家有的，家有惠暂时无法调用
//			SearchSolrDataService searchSolrDataService = new SearchSolrDataService();
//			ProductService ps  =  new  ProductService();
//			int i=0;
//			while((i++)<3&&searchSolrDataService.insertSolrData(AppConst.MANAGE_CODE_HOMEHAS, bConfig("productcenter.cluster"),null,ps.getMinProductActivity(null))==0){}
			
		}
		
		return mResult;
	}
	
}
