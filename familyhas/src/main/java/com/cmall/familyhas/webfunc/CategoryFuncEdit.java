package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class CategoryFuncEdit extends RootFunc{ 

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String manageCode = "SI2003";
		
		/*所属分类*/
		String category_code = mAddMaps.get("category_code").replace("'", "").trim();
		MDataMap dataMap = DbUp.upTable("fh_category").one("category_code",category_code,"manage_code",manageCode);
		if(dataMap != null){
			String link_type = mAddMaps.get("link_address");
			if(isnotNull(link_type)) {
				dataMap.put("link_address",  link_type);
				if(link_type.equals("449747030001")) {
					//选择专题
					if(!isnotNull(mAddMaps.get("link_url"))) {
						//System.out.println("请选择填写专题页连接");
						mResult.setResultCode(0);
						mResult.setResultMessage("请选择填写专题页连接");
					}
					
				} else if(link_type.equals("449747030002")){
					//选择商品
					if(!isnotNull( mAddMaps.get("product_link"))) {
						//System.out.println("请选择商品");
						mResult.setResultCode(0);
						mResult.setResultMessage("请选择商品");
					}
				}
				
			} else {
				dataMap.put("link_address",   "");
			}
			
			dataMap.put("line_head",  isnotNull(mAddMaps.get("line_head")) ?  mAddMaps.get("line_head") : "");
			dataMap.put("product_code", isnotNull( mAddMaps.get("product_code")) ?  mAddMaps.get("product_code") : "");
			dataMap.put("product_name",  isnotNull(mAddMaps.get("product_name")) ?  mAddMaps.get("product_name") : "");
			dataMap.put("link_url",  isnotNull(mAddMaps.get("link_url")) ?  mAddMaps.get("link_url") : "");
			dataMap.put("product_link", isnotNull( mAddMaps.get("product_link")) ?  mAddMaps.get("product_link") : "");
			
			try{
				if (mResult.upFlagTrue()) {
					/**将惠家有信息更改fh_category表中*/
					DbUp.upTable("fh_category").update(dataMap);
				}
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
		}else{//第一次的时后走添加动作
			
			/*所属app*/
			String app_code = bConfig("familyhas.app_code");
			
			mAddMaps.put("category_code", category_code);
			
			mAddMaps.put("manage_code", app_code);
			mAddMaps.remove("uid");
			try{
				if (mResult.upFlagTrue()) {
					/**将惠家有信息插入fh_category表中*/
					DbUp.upTable("fh_category").dataInsert(mAddMaps);
				}
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
		}
		return mResult;
	}
	
	/**
	 * 判断非空
	 * @param str
	 * @return
	 */
	public boolean isnotNull(String str) {
		boolean result = false;
		if(null != str && !"".equals(str)) {
			result = true;
		} 
		return result;
	}
	
}
