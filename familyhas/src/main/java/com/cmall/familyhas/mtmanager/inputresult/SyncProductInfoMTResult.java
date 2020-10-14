package com.cmall.familyhas.mtmanager.inputresult;

import java.util.List;

import com.cmall.familyhas.mtmanager.model.MTProductInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * mt管家同步产品信息返回信息
 * @author pang_jhui
 *
 */
public class SyncProductInfoMTResult extends RootResultWeb {
	
	@ZapcomApi(value="总数")
	private int total;
	
	@ZapcomApi(value="商品信息")
	private List<MTProductInfo> mtProductInfos;

	/**
	 * 获取商品总数
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置商品总数
	 * @param total
	 */
	public void setTotal(int total) {
		this.total = total;
	}


	/**
	 * 设置产品信息
	 * @param mtProductInfo
	 */
	public void setMtProductInfos(List<MTProductInfo> mtProductInfos) {
		this.mtProductInfos = mtProductInfos;
	}

	/**
	 * 获取产品信息
	 * @return
	 */
	public List<MTProductInfo> getMtProductInfos() {
		return mtProductInfos;
	}
	
	

}
