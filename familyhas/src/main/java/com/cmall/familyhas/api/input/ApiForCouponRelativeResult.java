package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootResultWeb;



/** 
* @ClassName: ApiForCdkeyRelativeResult 
* @Description: 优惠码券相关设置
* @author 张海生
* @date 2015-6-11 下午4:20:34 
*  
*/
public class ApiForCouponRelativeResult extends RootResultWeb {

	@ZapcomApi(value="不同类型对应的活动")
	private MDataMap relativeMap = new MDataMap();

	public MDataMap getRelativeMap() {
		return relativeMap;
	}

	public void setRelativeMap(MDataMap relativeMap) {
		this.relativeMap = relativeMap;
	}

}
