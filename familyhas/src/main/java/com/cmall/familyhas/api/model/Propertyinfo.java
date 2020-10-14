package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**   
*    
* 项目名称：familyhas   
* 类名称：PropertyinfoForSku   
* 类描述：   
* 创建人：李国杰
*
* @version    
*    
*/
public class Propertyinfo  {
    
	@ZapcomApi(value = "属性名称")
    private String propertykey  = ""  ;

	@ZapcomApi(value = "属性值")
    private String propertyValue  = ""  ;



	public String getPropertykey() {
		return propertykey;
	}

	public void setPropertykey(String propertykey) {
		this.propertykey = propertykey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}

