package com.cmall.familyhas.webfunc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

 
/**
 * @description: 返豆比例设置-修改
 *		 
 * @author Yangcl
 * @date 2016年12月13日 下午5:50:28 
 * @version 1.0.0
 */
public class HdProduceConfigEdit extends FuncEdit {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String percent = mDataMap.get("percent");
		if( !this.isNumeric(String.valueOf(percent)) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("输入包含非整数类型，请检查");  // bInfo(941901133)
			return mResult;
		}
		String seller_type = mDataMap.get("seller_type").split("@")[0];
		String seller_type_name = mDataMap.get("seller_type").split("@")[1]; 
		if(StringUtils.isBlank(seller_type) ||StringUtils.isBlank(percent) ){
			mResult.setResultCode(941901133);
			mResult.setResultMessage("关键字段不得为空");  // bInfo(941901133)
			return mResult;
		}
		MDataMap where = new MDataMap();
		where.put("seller_type", seller_type);
		List<MDataMap> list = DbUp.upTable("fh_hd_produce_config").query("uid", "", "seller_type=:seller_type", where, -1, -1);
		if(list != null){ 
			boolean flag = false;
			if(list.size() > 1){
				flag = true;
			}else if (list.size() == 1){
				String uid_ = list.get(0).get("uid"); 
				flag = !mDataMap.get("uid").equals(uid_);
			} 
			if(flag){
				mResult.setResultCode(941901133);
				mResult.setResultMessage("数据库已包含【" + seller_type_name + "】的记录");   
				return mResult;
			}
		}
		
		MDataMap updateMap = new MDataMap();
		updateMap.put("seller_type", seller_type);
		updateMap.put("seller_type_name", seller_type_name); 
		updateMap.put("percent", percent);
		
		HjybeanService service = new HjybeanService();
		service.updateHomehasBeanConsumeConfig(updateMap);
		
		updateMap.put("zid", mDataMap.get("zid")); 
		updateMap.put("uid", mDataMap.get("uid")); 
		DbUp.upTable("fh_hd_produce_config").update(updateMap);
		 
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
		MDataMap map = DbUp.upTable("fh_hd_produce_config").one("uid", uid);
		if(map != null && map.size() != 0){
			map.put("flag", "success");
		}else{
			map.put("flag", "error");
		}
		return JSONObject.toJSONString(map); 
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
