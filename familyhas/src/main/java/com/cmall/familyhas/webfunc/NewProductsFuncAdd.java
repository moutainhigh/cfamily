package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 新品推荐添加
 * 
 * @author guz
 *
 */
public class NewProductsFuncAdd extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		/*商品编号*/
		String sku_code = mAddMaps.get("sku_code").replace("'", "").trim();
		MDataMap fbData = DbUp.upTable("fh_brand_preference").one("sku_code",sku_code,"info_category",mAddMaps.get("info_category").replace("'", "").trim());
		if(fbData == null){
			MDataMap pcData = DbUp.upTable("pc_skuinfo").one("product_code",sku_code);
			/*产品名称*/
			String sku_name = pcData.get("sku_name");
			
			/*内容编号*/
			String info_code = WebHelper.upCode("HJY");
			
			/*状态*/
			String flag_show = mAddMaps.get("flag_show").replace("'", "").trim();
			
			/*所属分类*/
			String info_category = mAddMaps.get("info_category").replace("'", "").trim();
			
			/*系统当前时间*/
			String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
			
			/*获取当前登录人*/
			
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			
			/*所属app*/
			String app_code = bConfig("familyhas.app_code");
			
			mAddMaps.put("create_time", create_time);
			
			mAddMaps.put("info_code", info_code);
			
			mAddMaps.put("info_category", info_category);
			
			String sort_num = mAddMaps.get("sort_num").replace("'", "").trim();
			
			if(null == sort_num || "".equals(sort_num)){
				mAddMaps.remove("sort_num");
			}
			
			String photos = mAddMaps.get("photos").trim();
			if(null == photos || "".equals(photos)){
				mAddMaps.remove("photos");
			}
			String up_time = mAddMaps.get("up_time").trim();
			if(null == up_time || "".equals(up_time)){
				mAddMaps.remove("up_time");
			}
			String down_time = mAddMaps.get("down_time").trim();
			if(null == down_time || "".equals(down_time)){
				mAddMaps.remove("down_time");
			}
			mAddMaps.put("create_member", create_user);
			
			mAddMaps.put("manage_code", app_code);
			
			mAddMaps.put("sku_code", sku_code);
			
			mAddMaps.put("sku_name", sku_name);
			try{
				if (mResult.upFlagTrue()) {
					/**将惠家有信息插入fh_brand_preference表中*/
					DbUp.upTable("fh_brand_preference").dataInsert(mAddMaps);
				}
			}catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(959701033);
			}
			return mResult;
		}else{
			//商品不能重复
			mResult.setResultCode(-1);
			mResult.setResultMessage("商品不能重复添加");
			return mResult;
		}
	}

}
