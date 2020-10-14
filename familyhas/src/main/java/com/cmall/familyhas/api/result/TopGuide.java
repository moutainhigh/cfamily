package com.cmall.familyhas.api.result;

/**
 * 扫码购商品详情顶部悬浮
 */
public class TopGuide {

	/**
     * 是否显示
     */
	private boolean show = false;
	/**
     * 操作类型（整形）：1，打开小程序原生页。2，打开网址
     */
	private int guideType = 0;
	/**
     * 操作值，点击之后打开的地址
     */
	private String guideValue = "";
	/**
     * 图的url
     */
	private String guideImage = "";
	

	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}

	public int getGuideType() {
		return guideType;
	}
	public void setGuideType(int guideType) {
		this.guideType = guideType;
	}
	public String getGuideValue() {
		return guideValue;
	}
	public void setGuideValue(String guideValue) {
		this.guideValue = guideValue;
	}
	public String getGuideImage() {
		return guideImage;
	}
	public void setGuideImage(String guideImage) {
		this.guideImage = guideImage;
	}
	
}
