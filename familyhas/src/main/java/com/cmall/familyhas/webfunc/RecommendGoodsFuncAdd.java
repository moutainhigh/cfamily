package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 推荐商品添加
 * @author zhouguohui
 *
 */
public class RecommendGoodsFuncAdd extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String product_appcode = mAddMaps.get("product_appcode").trim();
		
		String sSql = "select app_name from uc_appinfo where app_code=:app_code";
		MDataMap map=new MDataMap();
		map.inAllValues("app_code",product_appcode);
		
		Map<String,Object> mapList = DbUp.upTable("uc_appinfo").dataSqlOne(sSql, map);
		String product_appname =mapList.get("app_name").toString();
		String product_num = mAddMaps.get("product_num").replace("'", "").trim();
		if(null== product_num ||"".equals(product_num)){
			product_num="0";
		}
        //创建时间为当年系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		/*获取当前登录人*/
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String  keyValueList =mAddMaps.get("product_code").trim(); 
		
		MDataMap pcData = DbUp.upTable("pc_productinfo").one("product_code",keyValueList);
		/*产品名称*/
		String productName = pcData.get("product_name");
		
		
		//String  keyTextList =mDataMap.get("keyTextList").trim(); 
		mAddMaps.put("product_appcode", product_appcode);
		mAddMaps.put("product_appname", product_appname);
		mAddMaps.put("product_num", product_num);
		mAddMaps.put("product_user", create_user);
		mAddMaps.put("product_time", df.format(new Date()));
		String[] splits = keyValueList.split(",");
		
		if(splits.length>0&&null!=splits && !"".equals(splits)){
			//String[] splitsText = keyTextList.split(",");
			for (int i = 0; i < splits.length; i++) {
				String sSqlGoods = "select zid,uid,product_code from pc_search_recommend_goods where product_code=:product_code ";
				MDataMap mapProductCode=new MDataMap();
				mapProductCode.inAllValues("product_code",splits[i]);
				Map<String,Object> mapListGoods = DbUp.upTable("pc_search_recommend_goods").dataSqlOne(sSqlGoods, mapProductCode);
				if(mapListGoods!=null){
					mAddMaps.remove("app_code");
					mAddMaps.remove("column_nameZero");
					mAddMaps.put("zid",mapListGoods.get("zid").toString());
					mAddMaps.put("uid",mapListGoods.get("uid").toString());
					mAddMaps.put("product_code",splits[i]);
					mAddMaps.put("product_name",productName);
					try{
						if (mResult.upFlagTrue()) {
							/**将惠家有信息修改pc_search_recommend_goods表中*/
							DbUp.upTable("pc_search_recommend_goods").update(mAddMaps);
						}
					}catch (Exception e) {
						e.printStackTrace();
						mResult.inErrorMessage(959701033);
					}
				}else{
					mAddMaps.remove("app_code");
					mAddMaps.remove("column_nameZero");
					mAddMaps.remove("zid");
					mAddMaps.remove("uid");
					mAddMaps.put("product_code",splits[i]);
					mAddMaps.put("product_name",productName);
					try{
						if (mResult.upFlagTrue()) {
							/**将惠家有信息插入pc_search_recommend_goods表中*/
							DbUp.upTable("pc_search_recommend_goods").dataInsert(mAddMaps);
						}
					}catch (Exception e) {
						e.printStackTrace();
						mResult.inErrorMessage(959701033);
					}
				}
				
				
			}
		}else{
			mResult.inErrorMessage(959701033);
		}
		
		return mResult;
	}
	
}

