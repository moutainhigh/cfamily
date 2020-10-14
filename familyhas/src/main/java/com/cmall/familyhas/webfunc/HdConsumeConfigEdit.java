package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

 
/**
 * @description: 惠豆使用设置-修改
 *		FuncEditCouponType
 * @author Yangcl
 * @date 2016年12月13日 下午5:50:28 
 * @version 1.0.0
 */
public class HdConsumeConfigEdit extends FuncEdit {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String min_use = mDataMap.get("min_use");
		String max_percent = mDataMap.get("max_percent");
		String ratio = mDataMap.get("ratio");
		if( !this.isNumeric(String.valueOf(max_percent)) ||!this.isNumeric(String.valueOf(min_use)) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("输入包含非整数类型，请检查");  // bInfo(941901133)
			return mResult;
		}
		if( !this.isPositiveNumber(ratio)){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("惠豆值应为正数，且保留两位小数");  // bInfo(941901133)
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
		
		MDataMap updateMap = new MDataMap();
		updateMap.put("seller_type", seller_type);
		updateMap.put("seller_type_name", seller_type_name); 
		updateMap.put("min_use", min_use);
		updateMap.put("max_percent", max_percent);
		updateMap.put("ratio", ratio);
		
		HjybeanService service = new HjybeanService();
		service.updateHomehasBeanConsumeConfig(updateMap);
		
		updateMap.put("zid", mDataMap.get("zid")); 
		updateMap.put("uid", mDataMap.get("uid")); 
		DbUp.upTable("fh_hd_consume_config").update(updateMap);
		 
		
		return mResult;
	}
	
	/**
	 * @description: 查库，把值返回给编辑页面|ftl模板只能支持String类型的返回值 
	 *
	 * @author Yangcl
	 * @date 2016年12月14日 上午10:27:07 
	 * @version 1.0.0.1
	 */
	public String getEntityByUid(String uid){
		MDataMap map = DbUp.upTable("fh_hd_consume_config").one("uid", uid);
		if(map != null && map.size() != 0){
			map.put("flag", "success");
		}else{
			map.put("flag", "error");
		}
		return JSONObject.toJSONString(map); 
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
	 * @description: 判断正数，如果为小数则保留两位小数|0不是正数 
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









