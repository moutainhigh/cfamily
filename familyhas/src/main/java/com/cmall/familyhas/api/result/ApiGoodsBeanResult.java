package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiGoodsBeanResult {
	@ZapcomApi(value = "商品图片",remark = "商品图片")
	private String cover_img;
	@ZapcomApi(value = "商品url",remark = "商品url")
    private String url;
	@ZapcomApi(value = "价格",remark = "价格")
    private String price;
	@ZapcomApi(value = "价格2",remark = "微信接口返回,什么意思暂不明确")
    private String price2;
	@ZapcomApi(value = "价格类型",remark = "微信接口返回,什么意思暂不明确")
    private String price_type;
	@ZapcomApi(value = "商品名称",remark = "商品名称")
    private String name;
	
	
	
    public String getPrice2() {
		return price2;
	}

	public void setPrice2(String price2) {
		this.price2 = price2;
	}

	public String getPrice_type() {
		return price_type;
	}

	public void setPrice_type(String price_type) {
		this.price_type = price_type;
	}

	public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
