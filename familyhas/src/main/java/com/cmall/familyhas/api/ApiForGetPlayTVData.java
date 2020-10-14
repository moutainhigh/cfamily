package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.result.ApiForGetPlayTVDataResult;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 电视TV|当前正在直播数据接口
 * 
 * @author dyc
 *
 */
public class ApiForGetPlayTVData extends RootApiForManage<ApiForGetPlayTVDataResult, RootInput> {

	public ApiForGetPlayTVDataResult Process(RootInput inputParam,
			MDataMap mRequestMap) {
		ApiForGetPlayTVDataResult re = new ApiForGetPlayTVDataResult();
		List<MDataMap> list = new ArrayList<MDataMap>();
		
		String nowTime = DateUtil.getNowTime();
		String swhere = "form_end_date>='"+nowTime+"' and form_fr_date <= '"+nowTime+"' and so_id='1000001'";
		list = DbUp.upTable("pc_tv").queryAll("", "", swhere, new MDataMap());	
		
		if(list!=null&&list.size()>0){	
			for(MDataMap map : list){
				//根据商品编码查询sku信息
				int num = DbUp.upTable("pc_productinfo").count("product_code",map.get("good_id"),"product_status","4497153900060002");
				if(num>0){
					re.setProductCode(map.get("good_id"));
					break;
				}
			}
		}else{
			list =  new ArrayList<MDataMap>();
		}
		re.setNum(list.size());
		return re;
	}

}
