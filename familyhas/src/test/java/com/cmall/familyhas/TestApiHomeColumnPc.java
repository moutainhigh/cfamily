package com.cmall.familyhas;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cmall.familyhas.api.ApiPcHomeColumn;
import com.cmall.familyhas.api.input.ApiHomeColumnInput;
import com.cmall.familyhas.api.result.ApiHomeColumnPcResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopTest;
public class TestApiHomeColumnPc extends TopTest {
	
	ApiPcHomeColumn getHomeColumn = new ApiPcHomeColumn();
	  
	   @BeforeClass
	    public static void setUp() throws Exception {
		   //dataDo.clear();
		   Date nowTime = new Date(System.currentTimeMillis());
		   System.out.println("   start>>>>>>>"+System.currentTimeMillis()+"    >>>>>>>>>");
		   System.out.println(nowTime.getTime());
	    }

	   
	    @Test
	    public void testGetOrders() {
	    	//Date nowTime = new Date(System.currentTimeMillis());
	    	
	    	//ApiHomeColumnInput input = new ApiHomeColumnInput();
	    	//input.setViewType("4497471600100003");
	    	//input.setOutOrderCode("20092919");
	    	//input.setBigOrderCode("OS150127100001");
	    	//input.setOrderStatus("4497153900010005");
	    	//input.setOrderStatusExt("4497153900140002");
	    	//input.setRegisterMobile("13488735339");
	    	//input.setReceivePerson("朱家华");
	    	//input.setAddress("北京");
	    	//input.setMobilephone("13264115721");
	    	//input.setProductName("女士香水");
	    	//input.setWaybill("1234567654321");*/
	    	//input.setCreateTimeStart("2014 10-11");
	    	//input.setCreateTimeEnd("2015 10-30");
	    	//ApiHomeColumnPcResult result = new ApiHomeColumnPcResult();
	    	//MDataMap mRequestMap  = new MDataMap();
	    	//System.out.println("prosess>>>>>>>"+nowTime.getTime());
	    	//result = getHomeColumn.Process(input,mRequestMap);
	    	//System.out.print(result.toString());
	    }
	    @AfterClass
	    public static void tearDown(){
		   Date nowTime = new Date(System.currentTimeMillis());
		   System.out.println("   end>>>>>>>"+System.currentTimeMillis()+"    >>>>>>>>>");
		   System.out.println(nowTime.getTime());
	    }

}
