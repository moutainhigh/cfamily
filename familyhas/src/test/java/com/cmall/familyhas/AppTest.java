package com.cmall.familyhas;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import com.cmall.groupcenter.behavior.api.ApiGetBfdRecResultInfo;
import com.cmall.groupcenter.behavior.response.BfdRecResultResponse;
import com.srnpr.zapcom.topdo.TopTest;

/**
 * Unit test for simple App.
 */
public class AppTest extends TopTest
{
	@org.junit.Test
	public void run(){
		
		//System.out.println(bConfig("xmasproduct.kjShopCode").contains("SF03100327 ")?"123":"456");
//		BfdRecResultResponse resp = new ApiGetBfdRecResultInfo().process("18600260367","","android_maybelove","47edd999a2c95f189f56f13d84382860");
//		System.out.println(resp.getRecResultInfos());
		System.out.println(this.getEvenNum(18, 50));
	}
	
	public int getEvenNum(int a,int b) {
		int num = a+(int)(Math.random()*(b-a));
		if(num%2==0){
			return num;
		}else{
			return num+1;
		}		
	}
	
}
