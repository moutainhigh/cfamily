package com.cmall.familyhas.webfunc;

import java.util.UUID;

import org.apache.commons.lang3.*;

import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
 
/**
 * @description: 返豆比例设置-新增
 *
 * @author Yangcl
 * @date 2016年12月13日 下午4:25:50 
 * @version 1.0.0
 */
public class HdProduceConfigAdd extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		 
		String max_percent = mDataMap.get("percent");
		if( !this.isNumeric(String.valueOf(max_percent)) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("输入包含非整数类型，请检查");  // bInfo(941901133)
			return mResult;
		}
		
		String seller_type = mDataMap.get("seller_type").split("@")[0];
		String seller_type_name = mDataMap.get("seller_type").split("@")[1]; 
		if(StringUtils.isBlank(seller_type) ||StringUtils.isBlank(max_percent) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("关键字段不得为空");  // bInfo(941901133)
			return mResult;
		}
		
		MDataMap entity = DbUp.upTable("fh_hd_produce_config").one("seller_type", seller_type);
		if(entity != null){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("数据库已包含【" + seller_type_name + "】的记录");  // bInfo(941901133)
			return mResult;
		}
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("uid", UUID.randomUUID().toString().replace("-", "")); 
		insertMap.put("seller_type", seller_type);
		insertMap.put("seller_type_name", seller_type_name);
		insertMap.put("percent", max_percent);
		DbUp.upTable("fh_hd_produce_config").dataInsert(insertMap);
		
		// 对数据进行缓存 
		HjybeanService service = new HjybeanService();
		service.updateHomehasBeanConsumeConfig(insertMap);
		
		return mResult;
	}
	 
	public boolean isNumeric(String str){  
	   for(int i=str.length();--i>=0;){  
	      int chr=str.charAt(i);  
	      if(chr<48 || chr>57)  
	         return false;  
	   }  
	   return true;  
	} 
 
}

















