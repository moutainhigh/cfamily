package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiMyCenterBtnResult {
	@ZapcomApi(value="名称")
	public String name;
	@ZapcomApi(value="位置", remark="移动端按钮排序")
	public Integer position;
	@ZapcomApi(value="类型")
	public String type;
	@ZapcomApi(value="类型字符串")
	public String typeChar;
	@ZapcomApi(value="图片路径")
	public String imagePath;
	@ZapcomApi(value="url地址")
	public String urlAddress;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeChar() {
		return typeChar;
	}
	public void setTypeChar(String typeChar) {
		this.typeChar = typeChar;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getUrlAddress() {
		return urlAddress;
	}
	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}
}
