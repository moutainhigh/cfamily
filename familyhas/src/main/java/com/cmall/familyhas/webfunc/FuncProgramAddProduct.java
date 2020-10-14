package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncProgramAddProduct extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult result = new MWebResult();
		result.setResultCode(0);
		result.setResultMessage("添加失败");
		String pc_code = mDataMap.get("pc_code");
		String program_code = mDataMap.get("program_code");
		if(null != pc_code && !pc_code.equals("") && null != program_code && !"".equals(program_code)) {
			MDataMap productInfo = DbUp.upTable("pc_productinfo").one("product_code",pc_code);
			if(null != productInfo) {
				String min_sell_price = productInfo.get("min_sell_price");
				String max_sell_price = productInfo.get("max_sell_price");
				
				
				Map<String, Object> dataSqlOne = DbUp.upTable("fh_program_product").dataSqlOne("select max(cast(sort as SIGNED)) sort from fh_program_product where program_code='"+program_code+"'", new MDataMap());
				Object sort = dataSqlOne.get("sort");
				Integer maxSort = 0;
				if(null != sort && !"".equals(sort)) {
					maxSort = Integer.valueOf(String.valueOf(sort));
				} 
				maxSort++;
				DbUp.upTable("fh_program_product").insert(
						"program_code",program_code,
						"product_code",pc_code,
						"product_name",productInfo.get("product_name"),
						"product_status",productInfo.get("product_status"),
						"min_sell_price",min_sell_price+"-"+max_sell_price,
						"sort",maxSort.toString()
						);
				result.setResultCode(1);
				result.setResultMessage("");
			}
		}
		
		return result;
	}

}
