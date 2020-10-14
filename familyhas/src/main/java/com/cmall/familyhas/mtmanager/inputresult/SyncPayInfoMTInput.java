package com.cmall.familyhas.mtmanager.inputresult;

import com.cmall.familyhas.mtmanager.model.MTAliPayInfo;
import com.cmall.familyhas.mtmanager.model.MTOrderPayInfo;
import com.cmall.familyhas.mtmanager.model.MTWeChatPayInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * MT支付信息同步输入参数
 * @author pang_jhui
 *
 */
public class SyncPayInfoMTInput extends RootInput {
	
	
	@ZapcomApi(value="订单相关支付信息")
	private MTOrderPayInfo mtOrderPayInfo;
	
	@ZapcomApi(value="微信支付信息")
	private MTWeChatPayInfo mtWeChatPayInfo;
	
	@ZapcomApi(value="支付宝信息")
	private MTAliPayInfo mtAliPayInfo;

	/**
	 * 获取订单支付相关信息
	 * @return
	 */
	public MTOrderPayInfo getMtOrderPayInfo() {
		return mtOrderPayInfo;
	}

	/**
	 * 设置订单支付相关信息
	 * @param mtOrderPayInfo
	 */
	public void setMtOrderPayInfo(MTOrderPayInfo mtOrderPayInfo) {
		this.mtOrderPayInfo = mtOrderPayInfo;
	}

	/**
	 * 获取微信支付信息
	 * @return
	 */
	public MTWeChatPayInfo getMtWeChatPayInfo() {
		return mtWeChatPayInfo;
	}

	/**
	 * 设置微信支付信息
	 * @param mtWeChatPayInfo
	 */
	public void setMtWeChatPayInfo(MTWeChatPayInfo mtWeChatPayInfo) {
		this.mtWeChatPayInfo = mtWeChatPayInfo;
	}

	/**
	 * 支付宝支付信息
	 * @return
	 */
	public MTAliPayInfo getMtAliPayInfo() {
		return mtAliPayInfo;
	}

	/**
	 * 设置支付宝支付信息
	 * @param mtAliPayInfo
	 */
	public void setMtAliPayInfo(MTAliPayInfo mtAliPayInfo) {
		this.mtAliPayInfo = mtAliPayInfo;
	}

}
