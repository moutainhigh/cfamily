package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.APiForCategoryInput;
import com.cmall.familyhas.api.model.BrandInfo;
import com.cmall.familyhas.api.model.SellerCategorySeInfo;
import com.cmall.familyhas.api.model.SellerCategoryThInfo;
import com.cmall.familyhas.api.result.APiForCategoryResult;
import com.cmall.groupcenter.behavior.api.ApiBfdRecCategory;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 惠家有类目接口
 * 
 * @author xiegj
 *
 */
public class APiForCategory extends RootApiForMember<APiForCategoryResult, APiForCategoryInput> {

	public APiForCategoryResult Process(APiForCategoryInput inputParam,
			MDataMap mRequestMap) {
		APiForCategoryResult result = new APiForCategoryResult();
		
		//查询本店铺所有分类
		MDataMap quDataMap = new MDataMap();
		quDataMap.put("seller_code", getManageCode());
		quDataMap.put("flaginable", "449746250001");
		List<MDataMap> list = DbUp.upTable("uc_sellercategory").queryAll("", "sort", "", quDataMap);
		List<SellerCategorySeInfo> seInfos = new ArrayList<SellerCategorySeInfo>();
		String infoCodes = "";
		for (int i = 0; i < list.size(); i++) {//操作二级分类
			SellerCategorySeInfo seinfo = new SellerCategorySeInfo();
			MDataMap seMap = list.get(i); 
			if("2".equals(seMap.get("level"))){
				seinfo.setCategoryCode(seMap.get("category_code"));
				seinfo.setCategoryName(seMap.get("category_name"));
				if("449746250001".equals(seMap.get("adv_flag"))){
					seinfo.setAdvPic(newUrl(inputParam.getMaxWidth(),seMap.get("adv_pic")));
					seinfo.setAdvUrl(seMap.get("adv_href"));
				}
				seInfos.add(seinfo);
				if("".equals(infoCodes)){
					infoCodes=seinfo.getCategoryCode();
				}else{
					infoCodes+=","+seinfo.getCategoryCode();
				}
			}
		}
		
		
		
		List<Map<String, Object>> brands = new ArrayList<Map<String,Object>>();
		if(!"".equals(infoCodes)){
			String sql = "select a.*,b.category_code from productcenter.pc_brandinfo a,usercenter.uc_sellercategory_brand_relation b " +
					"where b.seller_code='SI2003' and a.brand_code=b.brand_code and b.category_code in ('"+infoCodes.replace(",", "','")+"') ORDER BY b.create_time ";
			brands = DbUp.upTable("pc_brandinfo").dataSqlList(sql, new MDataMap());
		}
		for (int i = 0; i < seInfos.size(); i++) {
			SellerCategorySeInfo seinfo = seInfos.get(i);
			for (int j = 0; j < list.size(); j++) {
				MDataMap thMap = list.get(j);
				String thirdLevelCode = thMap.get("parent_code");
				//判断当前分类二级分类是否为目标码,性能优化，内部if只进一次
				for(int x  = 0;x<list.size();x++) {
					MDataMap mDataMap = list.get(x);
					SellerCategoryThInfo thInfo = new SellerCategoryThInfo();
					if(thirdLevelCode.equals(mDataMap.get("category_code"))) {
						if(mDataMap.get("parent_code").equals(seinfo.getCategoryCode())){
							thInfo.setCategoryCode(thMap.get("category_code"));
							thInfo.setCategoryName(thMap.get("category_name"));
							thInfo.setCategoryPic(newUrl(inputParam.getMaxWidth(),thMap.get("photo")));
							if("449746250001".equals(thMap.get("adv_flag"))){
								thInfo.setAdvPic(newUrl(inputParam.getMaxWidth(),thMap.get("adv_pic")));
								thInfo.setAdvUrl(thMap.get("adv_href"));
							}
							seInfos.get(i).getScs().add(thInfo);
						}else {
							break;
						}
					}
				}
				
			}
			for (int j = 0; j < brands.size(); j++) {
				Map<String, Object> brand = brands.get(j);
				if(brand.get("category_code").toString().equals(seinfo.getCategoryCode())&&"1".equals(brand.get("flag_enable").toString())){
					BrandInfo bi = new BrandInfo();
					bi.setBrandCode(brand.get("brand_code")==null?"":brand.get("brand_code").toString());
					bi.setBrandZNName(brand.get("brand_name")==null?"":brand.get("brand_name").toString());
					bi.setBrandUNName(brand.get("brand_name_en")==null?"":brand.get("brand_name_en").toString());
					bi.setBrandPic(newUrl(inputParam.getMaxWidth(),brand.get("brand_pic").toString()));
					seInfos.get(i).getBrands().add(bi);
				}
			}
		}
		
		// 如果是登录用户并且启用了百分点推荐分类
		if(getFlagLogin() && "true".equalsIgnoreCase(TopUp.upConfig("familyhas.bfd_category_enabled"))){
			// 取平台参数
			String client = mRequestMap.get("api_client");
			String platform = "android";
			if(StringUtils.isNotBlank(client)){
				platform = "ios";
				if(client.contains("\"os\":\"android\"")){
					platform = "android";
				}
			}
			
			// 平台参数不为空的时候再取推荐分类
			if(platform != null){
				// 从百分点解析出第三级分类名称列表
				List<String> catList = new ApiBfdRecCategory().doProcess(getOauthInfo().getLoginName(), platform);
				List<SellerCategoryThInfo> infoList = new ArrayList<SellerCategoryThInfo>();
				MDataMap map;
				// 根据分类名称列表查询分类
				if(!catList.isEmpty()){
					SellerCategoryThInfo thInfo;
					for(String name : catList){
						map = DbUp.upTable("uc_sellercategory").one("category_name",name,"level","4","seller_code","SI2003");
						if(map == null) continue;
						thInfo = new SellerCategoryThInfo();
						thInfo.setCategoryCode(map.get("category_code"));
						thInfo.setCategoryName(map.get("category_name"));
						thInfo.setCategoryPic(newUrl(inputParam.getMaxWidth(),map.get("photo")));
						infoList.add(thInfo);
					}
				}
				
				// 添加分类到原分类列表中
				if(!infoList.isEmpty()){
					SellerCategorySeInfo mainInfo = new SellerCategorySeInfo();
					mainInfo.setCategoryCode("44971604");
					mainInfo.setCategoryName(TopUp.upConfig("familyhas.bfd_category_name"));
					mainInfo.setScs(infoList);
					
					// 百分点推荐分类放到第一个位置
					seInfos.add(0, mainInfo);
				}
			}
		}
		
		result.setScs(seInfos);
		return result;
	}
	public String newUrl(Integer width,String url) {
		if(null==url||"".equals(url)){
			return "";
		}else {
			ProductService service = new ProductService();
			return service.getPicInfoOprBig(width, url).getPicNewUrl();
		}
	}
}
