package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiAddBrowseHistoryInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 新增浏览历史
 * @author liqt
 *
 */
public class ApiAddBrowseHistory extends RootApiForToken<RootResultWeb, ApiAddBrowseHistoryInput>{
	public RootResultWeb Process(ApiAddBrowseHistoryInput input,MDataMap mDataMap){
		//获取用户编号
		String memberCode = getUserCode();
		RootResultWeb result = new RootResultWeb();
		
		//what's the fuck meaning? reflect a class(ApiAddBrowseHistory) 
		// serves for a request thread. [modified by zht]
		//ApiAddBrowseHistory add =new ApiAddBrowseHistory();
		try {
			//what's the fuck meaning? reflect a class(ApiAddBrowseHistory) 
			// serves for a request thread. [modified by zht]
			//add.addBrowseHistory(input.getProductCodes(), memberCode);	
			addBrowseHistory(input.getProductCodes(), memberCode);	
		} catch (Exception e) {
			e.printStackTrace();
			result.inErrorMessage(959701033);
		}
		
		return result;
	}
	
	//添加浏览历史方法
	public Boolean addBrowseHistory(List<String> productCodes,String memberCode){
		if(memberCode==null || memberCode.isEmpty()){
			return true;
		}
		RootResultWeb result = new RootResultWeb();
		MDataMap mAddMap = new MDataMap();
		mAddMap.put("member_code", memberCode);
		//将product_code的list转换成string
		for(int i=0;i<productCodes.size();i++){
			String productCode = productCodes.get(i);
			//将IC开头
			if(PlusHelperEvent.checkEventItem(productCode)){
				PlusModelSkuInfo info = new PlusSupportProduct().upSkuInfoBySkuCode(productCode,memberCode);
				if(StringUtils.isNotBlank(info.getProductCode())){
					productCode=info.getProductCode();
				}
			}
			if(i==0){
				mAddMap.put("product_code", productCode);				
			}else {
				mAddMap.put("product_code", mAddMap.get("product_code")+","+productCode);
			}
		}
		mAddMap.put("create_time", DateUtil.getNowTime());
		mAddMap.put("status", "0");
		
		
		String str = mAddMap.get("product_code");
		if(StringUtils.isNotBlank(str)) {
			String[] s = str.split(",");
			//用户浏览商品日志表. add by zht
			for(int i=0;i<s.length;i++) {
				mAddMap.put("product_code", s[i]);
				DbUp.upTable("pc_browse_history_log").dataInsert(mAddMap);
			}
			
			//个人中心-用户浏览商品历史记录
			mAddMap.remove("status");
			List<MDataMap> mDataMap2 = DbUp.upTable("pc_browse_history").queryAll("product_code", "", "member_code=:member_code", new MDataMap("member_code",memberCode));
			if(mDataMap2 == null || mDataMap2.isEmpty()) {
				//当前用户的浏览记录如果为空则直接插入
				for(int i=0;i<s.length;i++) {
					mAddMap.put("product_code", s[i]);
					DbUp.upTable("pc_browse_history").dataInsert(mAddMap);
				}
			} else {
				//当前用户的浏览记录如果不为空则判断是否已经存在该商品，不存在插入，存在则更新
				for (int i = 0; i < s.length; i++) {
					mAddMap.put("product_code", s[i]);
					boolean flag = true;
					for(MDataMap map : mDataMap2){
						if(map.get("product_code").equals(s[i])){
							flag = true;
							break;
						}else {
							flag = false;
						}
					}
					if (result.upFlagTrue() && !flag) {
						DbUp.upTable("pc_browse_history").dataInsert(mAddMap);
					} else {
						DbUp.upTable("pc_browse_history").dataUpdate(mAddMap,
								"create_time", "member_code,product_code");
					}
				}
			}	
		}
		return true;
	}
}
