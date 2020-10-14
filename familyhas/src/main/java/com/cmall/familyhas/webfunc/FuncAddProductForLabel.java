package com.cmall.familyhas.webfunc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加标签下的所属商品
 * @author houwen
 *
 */
public class FuncAddProductForLabel extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String label = mDataMap.get("zw_f_label");
		String product_code = mDataMap.get("zw_f_product_codes");
		MDataMap mwhereMap = new MDataMap();
		mwhereMap.put("keyword", label);
		if(StringUtils.isNotBlank(label)){
		List<MDataMap> labellist = DbUp.upTable("pc_productdescription").queryAll("keyword,product_code", "", "INSTR(keyword,:keyword)",mwhereMap);
		Map<String, String> productLabelMap = new HashMap<String, String>();
		
		if(labellist!=null && !labellist.isEmpty()){
			
			for (MDataMap mDataMap2 : labellist) {
				String temp = mDataMap2.get("keyword");
				String ss = "";
				for(String key:temp.split(",")){
					if(!key.equals(label)){
						if(StringUtils.isBlank(ss)){
							ss += key;
						}else{
							ss += ","+key;
						}
					}
				}
				productLabelMap.put(mDataMap2.get("product_code"), ss);
			}	
		}
		
		if(StringUtils.isNotBlank(product_code)){
			for (String product : product_code.split(",")) {
				MDataMap tempMap = DbUp.upTable("pc_productdescription").one("product_code",product);
				String temp = "";
				if(null!=tempMap){
					temp = tempMap.get("keyword");
					if(!temp.contains(label)){
						temp += ","+label;
					}
				}else{
					temp = label;
				}
				
				productLabelMap.put(product, temp);
			}
		}
		
		LoadProductInfo loadProductInfo = new LoadProductInfo();
	
		//ProductJmsSupport pjs = new ProductJmsSupport();
		for (String productCode : productLabelMap.keySet()) {
			
			MDataMap pcMap = new MDataMap();
			pcMap.put("product_code", productCode);
			pcMap.put("keyword",productLabelMap.get(productCode));
			
			DbUp.upTable("pc_productdescription").dataUpdate(pcMap, "keyword", "product_code");
			
			//PlusHelperNotice.onChangeProductInfo(productCode);
			//触发消息队列
			//pjs.onChangeForProductChangeAll(productCode);
			
			loadProductInfo.deleteInfoByCode(productCode);
		}
		
	}
		return mResult;
}
}
