package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.cmall.dborm.txmodel.OcActivityFlashsales;
import com.cmall.familyhas.api.input.ApiForGetStockNumByStoreInput;
import com.cmall.familyhas.api.result.ApiForGetStockNumByStoreResult;
import com.cmall.familyhas.service.ShopCartService;
import com.cmall.ordercenter.service.FlashsalesService;
import com.cmall.productcenter.service.ProductStoreService;
import com.cmall.systemcenter.service.StoreService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 查询查询库存状态
 * @author jlin
 *
 */
public class ApiForGetStockNumByStore extends RootApiForManage<ApiForGetStockNumByStoreResult, ApiForGetStockNumByStoreInput> {

	public ApiForGetStockNumByStoreResult Process(ApiForGetStockNumByStoreInput inputParam,MDataMap mRequestMap) {
		ApiForGetStockNumByStoreResult result=new ApiForGetStockNumByStoreResult();
		
		String product_code=inputParam.getProduct_code();//商品编号
		String district_code = inputParam.getDistrict_code();//地区编码
		String sku_code = inputParam.getSku_code();//sku编码
		
//		ShopCartService cartService=new ShopCartService();//转换 SKU
//		sku_code=cartService.getSkuCodeForValue(product_code, sku_code);
		StringBuffer sb=new StringBuffer("");
		String ss[]=sku_code.split("&");
		if(ss!=null&&ss.length>0){
			
			sb.append("sku_key='").append(ss[0]).append("&").append(ss[1]).append("' or ");
			sb.append("sku_key='").append(ss[1]).append("&").append(ss[0]).append("'");
//			for (String s : ss) {
//				if(s.contains("=")){  这是个深坑
//					sb.append(" or ").append(" sku_key like '%"+s+"%'");
//				}
//			}
		}
		
//		select sku_code from pc_skuinfo where sell_productcode='132289' and  ( sku_key = 'color_id=0&style_id=2' OR  sku_key like 'style_id=2&color_id=0' )
		
		List<Map<String, Object>> list=DbUp.upTable("pc_skuinfo").dataSqlList("select sku_code from pc_skuinfo where sell_productcode=:product_code and ( "+sb+" ) ", new MDataMap("product_code",product_code,"sku_key",sku_code));
		if(list==null || list.size()<1){
			result.setResultCode(916401115);
			result.setResultMessage(bInfo(916401115,product_code,sku_code));
			return result;
		}
		
		sku_code=(String)list.get(0).get("sku_code");
		
		BigDecimal sell_price = new BigDecimal(0); 
		
		//查看闪购活动
		FlashsalesService flashsalesService = BeansHelper.upBean("bean_com_cmall_ordercenter_service_FlashsalesService");
		sell_price=flashsalesService.getVipPrice(sku_code);
		OcActivityFlashsales activityFlashsales = flashsalesService.getFlashsalesActivity(sku_code);
		
		//如果没有闪购活动，就到 sku中查看价格
		if(sell_price==null){
			try {
				sell_price=(BigDecimal)DbUp.upTable("pc_skuinfo").dataGet("sell_price", "sku_code=:sku_code", new MDataMap("sku_code",sku_code));
			} catch (Exception e) {
				result.setResultCode(916401000);
				result.setResultMessage(bInfo(916401000));
				return result;
			}
		}
		
		//返现金额
		DecimalFormat df = new DecimalFormat("0.00"); // 保留几位小数
		BigDecimal back_price=sell_price.multiply(new BigDecimal("0.05"));
		
		
		//到仓库中查看库存量
		int stockNum=0;
		String info = "";
		
//		StoreService storeService=BeansHelper.upBean("bean_com_cmall_systemcenter_service_StoreService");
//		stockNum = storeService.getStockNumByDistrict(district_code, sku_code);
		
		ProductStoreService productStoreService = new ProductStoreService();
		stockNum = productStoreService.getStockNumBySku(district_code, sku_code);
		
		if(stockNum>=30){
			info=bConfig("familyhas.store_info_has");
		}
		
		// 库存 <=30 ：库存紧张  库存<=0 : 缺货
		if(stockNum<30 && stockNum >0){
			info=bConfig("familyhas.store_info_less");
		}
		
		if(stockNum <=0 ){
			info=bConfig("familyhas.store_info_out");
		}
		
		//促销了信息 目前只有闪购
		if(activityFlashsales!=null){
			ApiForGetStockNumByStoreResult.Promotion promotion =new ApiForGetStockNumByStoreResult.Promotion();
			promotion.setName(activityFlashsales.getActivityName());
			promotion.setRemark(activityFlashsales.getRemark());
			result.getPromotionList().add(promotion);
		}
		
		
		result.setBack_price(df.format(back_price));
		result.setInfo(info);
		result.setSell_price(df.format(sell_price));
		result.setStock_num(stockNum);
		
		return result;
	}

}
