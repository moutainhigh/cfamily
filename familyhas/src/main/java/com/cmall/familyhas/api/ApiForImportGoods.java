package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForImportGoodsInput;
import com.cmall.familyhas.api.model.NewProducts;
import com.cmall.familyhas.api.result.ApiForImportGoodsResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.MObjMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;
/**
 * 进口货列表接口
 * 
 * @author guz
 *
 */
public class ApiForImportGoods extends RootApiForMember<ApiForImportGoodsResult,ApiForImportGoodsInput>{

	public ApiForImportGoodsResult Process(ApiForImportGoodsInput inputParam,
			MDataMap mRequestMap) {
		
		
		ApiForImportGoodsResult result = new ApiForImportGoodsResult();
		if(StringUtils.isBlank(inputParam.getBuyerType()))
		{
			inputParam.setBuyerType("4497469400050002");
		}
		//设置相关信息
		if(result.upFlagTrue()){
			    String activity = inputParam.getActivity();
			    String manageCode = this.getManageCode();
			    MDataMap mDataMap = new MDataMap("info_category", activity,"manageCode",manageCode);
				String sSql = "select photos,info_code,sku_code,selling_describe,up_time,down_time,link_addr from fh_brand_preference where flag_show='449746530001' and info_category =:info_category and manage_code =:manageCode  order by sort_num desc";
				
			MDataMap cData = DbUp.upTable("fh_category").one("category_code",activity);
			if(cData != null){
				//栏目图片地址
				result.setBanner_img(cData.get("line_head") ==null ? "" : cData.get("line_head"));
				//栏目名称
				result.setBanner_name(cData.get("category_name") ==null ? "" : cData.get("category_name"));
				//判断栏目类型
				if("449747030001".equals(cData.get("link_address"))) {
					result.setBanner_link(cData.get("link_url") ==null ? "" : cData.get("link_url"));
				} else {
					result.setBanner_link(cData.get("product_link") ==null ? "" : cData.get("product_link"));
				}
			}
			List <Map<String,Object>>  mList =  DbUp.upTable("fh_brand_preference").dataSqlList(sSql, mDataMap);
			List<String> productCodeList = new ArrayList<String>();//存放商品编号
			if(mList.size() > 0){
				for (Map<String, Object> map : mList) {
					productCodeList.add((String)map.get("sku_code"));
				}	
//				ProductService pService = BeansHelper
//						.upBean("bean_com_cmall_productcenter_service_ProductService");
//				Map<String, BigDecimal> productPriceMap = pService
//						.getMinProductActivityNew(productCodeList, inputParam.getBuyerType());// 获取商品最低销售价格
				Map<String, BigDecimal> productPriceMap = new HashMap<String,BigDecimal>();
				if (VersionHelper.checkServerVersion("3.5.95.55")) {
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(StringUtils.join(productCodeList,","));
					skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
					productPriceMap = new ProductPriceService().
							getProductMinPrice(skuQuery);// 获取商品最低销售价格
				}else{
					productPriceMap = new ProductService().
							getMinProductActivityNew(productCodeList, inputParam.getBuyerType());// 获取商品最低销售价格
				}
				Map<String, Object> urMapParam = new MObjMap<String, Object>();
				for( int i = 0;i<mList.size();i++){
					urMapParam = mList.get(i);
					NewProducts items = new NewProducts();
					String up_time = (String)urMapParam.get("up_time");
					String down_time = (String)urMapParam.get("down_time");
					String now_time = com.cmall.familyhas.util.DateUtil.getNowTime();
					if(StringUtils.isEmpty(up_time) && !StringUtils.isEmpty(down_time)){
						boolean flag = DateUtil.getTimefag(now_time,down_time);
						if(flag)
							continue;
					}else if(!StringUtils.isEmpty(up_time) && StringUtils.isEmpty(down_time)){
						//有效时间只有开始时间
						boolean flag = DateUtil.getTimefag(now_time,up_time);
						if(!flag)
							continue;
					}else if(!StringUtils.isEmpty(up_time) && !StringUtils.isEmpty(down_time)){
						//有效时间有开始和结束时间
						boolean flag1 = DateUtil.getTimefag(now_time,up_time);
						boolean flag2 = DateUtil.getTimefag(now_time,down_time);
						if(!flag1 || flag2)
							continue;
					}
					//图片名称
					items.setItem_name("");
					//商品编号
					String sku_code = String.valueOf(urMapParam.get("sku_code"));
					MDataMap pcData = DbUp.upTable("pc_productinfo").one("product_code",sku_code);
					if(pcData == null)
						continue;
					if(null != urMapParam.get("photos") && !"".equals(urMapParam.get("photos"))){
						items.setImg_url(String.valueOf(urMapParam.get("photos")));
					}else{
						items.setImg_url(String.valueOf(pcData.get("mainpic_url")));
					}
					//商品编号
					items.setGoods_num(String.valueOf(pcData.get("product_code") ==null ? "" : pcData.get("product_code")));
					//商品名称
					items.setGoods_name(String.valueOf(pcData.get("product_name") ==null ? "" : pcData.get("product_name")));
					
					if(productPriceMap.get(sku_code)==null)
					{
						continue;
					}
					
					//现价
					if(null != pcData.get("min_sell_price") && !"".equals(pcData.get("min_sell_price"))){
						items.setCurrent_price(Double.parseDouble(String.valueOf(productPriceMap.get(sku_code))));
					}
					
					//原价
					if(null != pcData.get("market_price") && !"".equals(pcData.get("market_price"))){
						items.setList_price(Double.parseDouble(String.valueOf(pcData.get("market_price"))));
					}
					
					//卖点描述
					items.setGoods_description(String.valueOf(urMapParam.get("selling_describe") ==null ? "" : urMapParam.get("selling_describe")));
					result.getItems().add(items);
				}
			}		
		}
		return result;
	}

}
