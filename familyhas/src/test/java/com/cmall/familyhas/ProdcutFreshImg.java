package com.cmall.familyhas;

import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;


/**
 * 更改商品主图域名，更新商品表pc_productinfo   ,且刷新商品缓存
 * @author fq
 *
 */
public class ProdcutFreshImg {

	
	public static void main(String[] args) {
		
		String sSql = "  SELECT zid,uid,product_code,mainpic_url,product_status FROM productcenter.pc_productinfo WHERE mainpic_url LIKE '%image.sycdn.ichsy.com%'  ";
		
		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productinfo").dataSqlList(sSql, new MDataMap());
		MDataMap mDataMap = new MDataMap();
		for (Map<String, Object> map : dataSqlList) {
			String zid = String.valueOf(map.get("zid"));
			String uid = String.valueOf(map.get("uid"));
			String product_code = String.valueOf(map.get("product_code"));
			String product_status = String.valueOf(map.get("product_status"));
			String mainpic_url = String.valueOf(map.get("mainpic_url"));
			
			mainpic_url = mainpic_url.replaceAll("image.sycdn.ichsy.com", "image-family.huijiayou.cn");//替换域名
			
//			mDataMap.put("product_code", product_code);
			mDataMap.put("mainpic_url", mainpic_url);
			mDataMap.put("zid", zid);
			mDataMap.put("uid", uid);
			DbUp.upTable("pc_productinfo").update(mDataMap);//刷新商品主图的域名
			
			
			if("4497153900060002".equals(product_status)) {//  上架
				//将上架的商品的缓存数据刷新一次
				PlusHelperNotice.onChangeProductInfo(product_code);
			}
			
		}
		
	}
}
