package com.cmall.familyhas.webfunc;

import java.util.UUID;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
 
/**
 * @description: 惠豆使用设置-新增
 *
 * @author Yangcl
 * @date 2016年12月13日 下午4:25:50 
 * @version 1.0.0
 */
public class HdConsumeConfigAdd extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		 
		String max_percent = mDataMap.get("max_percent");
		String min_use = mDataMap.get("min_use");
		String ratio = mDataMap.get("ratio");
		if( !this.isNumeric(String.valueOf(max_percent)) ||!this.isNumeric(String.valueOf(min_use)) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("输入包含非整数类型，请检查");  // bInfo(941901133)
			return mResult;
		}
		if( !this.isPositiveNumber(ratio)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("惠豆值应为正数");  // bInfo(941901133)
			return mResult;
		}
		
		String seller_type = "";
		String seller_type_name = "";
		String flag = "seller449747810005000";
		for(int i = 0 ; i < 5 ; i ++){
			if(StringUtils.isNotBlank(mDataMap.get(flag + i))){
				seller_type += mDataMap.get(flag + i).split("@")[0] +","; 
				seller_type_name += mDataMap.get(flag + i).split("@")[1] +","; 
			}
		}
		if(StringUtils.isNotBlank(seller_type)){
			seller_type = seller_type.substring(0 , seller_type.length()-1);
			seller_type_name = seller_type_name.substring(0 , seller_type_name.length()-1);
		}
		if(StringUtils.isBlank(seller_type) ||StringUtils.isBlank(min_use) ||StringUtils.isBlank(max_percent) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("关键字段不得为空");  // bInfo(941901133)
			return mResult;
		}
		
		MDataMap where = new MDataMap();
		where.put("zid", "100000eew"); 
		int count = DbUp.upTable("fh_hd_consume_config").dataCount("zid!=:zid" , where);
		if(count != 0){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("数据库已存在一条记录，请修改这条记录来实现您的业务需求。");  // bInfo(941901133)
			return mResult;
		}
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("seller_type", seller_type);
		insertMap.put("seller_type_name", seller_type_name);
		insertMap.put("min_use", min_use);
		insertMap.put("max_percent", max_percent);
		insertMap.put("ratio", ratio);
//		insertMap.put("uid", UUID.randomUUID().toString().replace("-", "")); 
		DbUp.upTable("fh_hd_consume_config").dataInsert(insertMap);
		
		// 对数据进行缓存
		HjybeanService service = new HjybeanService();
		service.updateHomehasBeanConsumeConfig(insertMap);
		
		return mResult;
	}
	 
	private boolean isNumeric(String str){  
	   for(int i=str.length();--i>=0;){  
	      int chr=str.charAt(i);  
	      if(chr<48 || chr>57)  
	         return false;  
	   }  
	   return true;  
	} 
 
	/**
	 * @description: 判断正数|0不是正数 
	 *
	 * @author Yangcl
	 * @date 2016年12月15日 上午10:28:44 
	 * @version 1.0.0.1
	 */
	private boolean isPositiveNumber(String str){
		boolean flag = true;
		if(str.matches("^\\d+(\\.\\d+)?$")){
			if(Double.valueOf(str) == 0){
				return false;
			}
			if(StringUtils.contains(str, ".") && StringUtils.substringAfter(str, ".").length() > 2){
				flag = false;
			}
		}else{
			flag = false;
		}
		return flag;
	}
}

















